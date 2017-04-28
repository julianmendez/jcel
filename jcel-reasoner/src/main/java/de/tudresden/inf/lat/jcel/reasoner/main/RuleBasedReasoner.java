/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.reasoner.main;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.rulebased.RuleBasedProcessor;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.ComplexAxiomExpressivityDetector;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.normalization.ObjectPropertyIdFinder;
import de.tudresden.inf.lat.jcel.ontology.normalization.OntologyNormalizer;

/**
 * This class models a rule-based reasoner.
 * 
 * @author Julian Mendez
 */
public class RuleBasedReasoner implements IntegerReasoner {

	private static final Logger logger = Logger.getLogger(RuleBasedReasoner.class.getName());

	private final Map<IntegerClassExpression, Integer> auxClassInvMap = new HashMap<>();
	private final Map<Integer, IntegerClassExpression> auxClassMap = new HashMap<>();
	private boolean classified = false;
	private final OntologyEntailmentChecker entailmentChecker = new OntologyEntailmentChecker(this);
	private final IntegerOntologyObjectFactory factory;
	private boolean interruptRequested = false;
	private RuleBasedProcessor processor = null;
	private final long timeOut = 0;

	public RuleBasedReasoner(Set<ComplexIntegerAxiom> ontology, IntegerOntologyObjectFactory factory) {
		Objects.requireNonNull(ontology);
		Objects.requireNonNull(factory);
		this.factory = factory;
		this.processor = createProcessor(ontology);
	}

	@Override
	public void classify() {
		if (!this.classified) {
			logger.fine("starting classification ...");
			flush();

			while (this.processor.process()) {
				if (this.interruptRequested) {
					this.interruptRequested = false;
					throw new RuntimeException("Classification interrupted.");
				}
			}
			logger.fine("classification finished.");
		}

		this.classified = true;
	}

	private RuleBasedProcessor createProcessor(Set<ComplexIntegerAxiom> ontology) {
		logger.fine("creating processor (phase 1) ...");

		OntologyExpressivity expressivity = new ComplexAxiomExpressivityDetector(ontology);

		logger.fine("description logic family : " + expressivity.toString() + " .");

		Set<Integer> originalClassSet = new HashSet<>();
		Set<Integer> originalObjectPropertySet = new HashSet<>();

		ontology.forEach(axiom -> {
			originalClassSet.addAll(axiom.getClassesInSignature());
			originalObjectPropertySet.addAll(axiom.getObjectPropertiesInSignature());
		});

		logger.fine("number of axioms : " + ontology.size());
		logger.fine("number of classes : " + originalClassSet.size());
		logger.fine("number of object properties : " + originalObjectPropertySet.size());

		logger.fine("normalizing ontology ...");
		OntologyNormalizer axiomNormalizer = new OntologyNormalizer();
		Set<NormalizedIntegerAxiom> normalizedAxiomSet = axiomNormalizer.normalize(ontology, this.factory);

		logger.fine("creating processor (phase 2) ...");
		RuleBasedProcessor ret = new RuleBasedProcessor(originalObjectPropertySet, originalClassSet, normalizedAxiomSet,
				expressivity, this.factory.getNormalizedAxiomFactory(), this.factory.getEntityManager());
		logger.fine("processor created.");
		return ret;
	}

	@Override
	public void dispose() {
		// it does nothing
	}

	protected IntegerClass flattenClassExpression(IntegerClassExpression ce) {
		IntegerClass ret = null;
		if (ce instanceof IntegerClass) {
			ret = (IntegerClass) ce;
		} else {
			Integer classIndex = this.auxClassInvMap.get(ce);
			if (Objects.isNull(classIndex)) {
				Integer auxClassId = this.factory.getEntityManager().createAnonymousEntity(IntegerEntityType.CLASS,
						false);
				ret = getDataTypeFactory().createClass(auxClassId);
				this.auxClassMap.put(auxClassId, ce);
				this.auxClassInvMap.put(ce, auxClassId);
				Set<IntegerClassExpression> argument = new HashSet<>();
				argument.add(ret);
				argument.add(ce);

				Set<ComplexIntegerAxiom> extendedOntology = new HashSet<>();
				Set<IntegerAnnotation> annotations = Collections.emptySet();
				extendedOntology
						.add(this.factory.getComplexAxiomFactory().createEquivalentClassesAxiom(argument, annotations));

				OntologyNormalizer axiomNormalizer = new OntologyNormalizer();
				Set<NormalizedIntegerAxiom> extendedNormalizedAxiomSet = axiomNormalizer.normalize(extendedOntology,
						this.factory);

				this.processor.addAxioms(extendedNormalizedAxiomSet);

				this.classified = false;
			} else {
				ret = getDataTypeFactory().createClass(classIndex);
			}
		}

		return ret;
	}

	@Override
	public void flush() {
		this.classified = false;
	}

	@Override
	public Set<IntegerClass> getBottomClassNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		return toIntegerClass(graph.getEquivalents(graph.getBottomElement()));
	}

	@Override
	public Set<IntegerDataProperty> getBottomDataPropertyNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getDataPropertyHierarchy();
		return toIntegerDataProperty(graph.getEquivalents(graph.getBottomElement()));
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getBottomObjectPropertyNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getObjectPropertyHierarchy();
		return toIntegerObjectPropertyExpression(graph.getEquivalents(graph.getBottomElement()));
	}

	@Override
	public Set<Set<IntegerClass>> getDataPropertyDomains(IntegerDataProperty pe, boolean direct) {
		Objects.requireNonNull(pe);
		throw new UnsupportedQueryException("Unsupported query: DataPropertyDomains of " + pe);
	}

	@Override
	public Set<IntegerClass> getDataPropertyValues(IntegerNamedIndividual ind, IntegerDataProperty pe) {
		Objects.requireNonNull(ind);
		Objects.requireNonNull(pe);
		throw new UnsupportedQueryException("Unsupported query: DataPropertyValues of " + ind + "," + pe);
	}

	private IntegerDataTypeFactory getDataTypeFactory() {
		return this.factory.getDataTypeFactory();
	}

	@Override
	public Set<Set<IntegerNamedIndividual>> getDifferentIndividuals(IntegerNamedIndividual ind) {
		Objects.requireNonNull(ind);
		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerClass>> getDisjointClasses(IntegerClassExpression ce) {
		Objects.requireNonNull(ce);
		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerDataProperty>> getDisjointDataProperties(IntegerDataPropertyExpression pe) {
		Objects.requireNonNull(pe);
		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerObjectPropertyExpression>> getDisjointObjectProperties(IntegerObjectPropertyExpression pe) {
		Objects.requireNonNull(pe);
		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IntegerClass> getEquivalentClasses(IntegerClassExpression ce) {
		Objects.requireNonNull(ce);
		IntegerClass cls = flattenClassExpression(ce);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		return toIntegerClass(graph.getEquivalents(cls.getId()));
	}

	@Override
	public Set<IntegerDataProperty> getEquivalentDataProperties(IntegerDataProperty pe) {
		Objects.requireNonNull(pe);
		throw new UnsupportedQueryException("Unsupported query: EquivalentDataProperties of " + pe);
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getEquivalentObjectProperties(IntegerObjectPropertyExpression pe) {
		Objects.requireNonNull(pe);
		classify();
		Integer propId = getObjectPropertyExpressionId(pe);
		IntegerHierarchicalGraph graph = getProcessor().getObjectPropertyHierarchy();
		return toIntegerObjectPropertyExpression(graph.getEquivalents(propId));
	}

	@Override
	public Set<Set<IntegerNamedIndividual>> getInstances(IntegerClassExpression ce, boolean direct) {
		Objects.requireNonNull(ce);
		Set<Set<IntegerNamedIndividual>> ret = new HashSet<>();
		IntegerClass cls = flattenClassExpression(ce);
		classify();

		Integer classId = cls.getId();
		Set<Integer> classIdSet = new HashSet<>();
		classIdSet.add(classId);
		if (!direct) {
			classIdSet.addAll(getProcessor().getClassHierarchy().getDescendants(classId));
		}

		Set<Integer> indivIdSet = new HashSet<>();
		indivIdSet.addAll(getProcessor().getDirectTypes().keySet());

		while (!indivIdSet.isEmpty()) {
			Integer indivId = indivIdSet.iterator().next();
			indivIdSet.remove(indivId);

			Set<Integer> types = getProcessor().getDirectTypes().get(indivId);
			boolean found = types.contains(classId);

			if (!found && !direct) {
				Set<Integer> classIdSetCopy = new HashSet<>();
				classIdSetCopy.addAll(classIdSet);
				classIdSetCopy.retainAll(types);
				found = !classIdSetCopy.isEmpty();
			}

			if (found) {
				Set<Integer> equivIndivId = getProcessor().getSameIndividualMap().get(indivId);
				Set<IntegerNamedIndividual> elem = toIntegerNamedIndividual(equivIndivId);
				indivIdSet.removeAll(equivIndivId);
				ret.add(elem);
			}
		}

		return ret;
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getInverseObjectProperties(IntegerObjectPropertyExpression pe) {
		Objects.requireNonNull(pe);
		classify();
		// TODO implement this query
		throw new UnsupportedQueryException("Unsupported query: InverseObjectProperties of " + pe);
	}

	@Override
	public Set<Set<IntegerClass>> getObjectPropertyDomains(IntegerObjectPropertyExpression pe, boolean direct) {
		Objects.requireNonNull(pe);
		classify();
		// TODO implement this query
		throw new UnsupportedQueryException("Unsupported query: ObjectPropertyDomains of " + pe + "," + direct);
	}

	private Integer getObjectPropertyExpressionId(IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(new ObjectPropertyIdFinder(this.factory.getEntityManager()));
	}

	@Override
	public Set<Set<IntegerClass>> getObjectPropertyRanges(IntegerObjectPropertyExpression pe, boolean direct) {
		Objects.requireNonNull(pe);
		classify();
		// TODO implement this query
		throw new UnsupportedQueryException("Unsupported query: ObjectPropertyRanges of " + pe + "," + direct);
	}

	@Override
	public Set<Set<IntegerNamedIndividual>> getObjectPropertyValues(IntegerNamedIndividual ind,
			IntegerObjectPropertyExpression pe) {
		Objects.requireNonNull(ind);
		Objects.requireNonNull(pe);
		classify();
		// TODO implement this query
		throw new UnsupportedQueryException("Unsupported query: ObjectPropertyValues of " + ind + "," + pe);
	}

	public Processor getProcessor() {
		return this.processor;
	}

	@Override
	public String getReasonerName() {
		return VersionInfo.reasonerName;
	}

	@Override
	public String getReasonerVersion() {
		return VersionInfo.reasonerVersion;
	}

	@Override
	public Set<IntegerNamedIndividual> getSameIndividuals(IntegerNamedIndividual ind) {
		Objects.requireNonNull(ind);
		classify();
		return toIntegerNamedIndividual(getProcessor().getSameIndividualMap().get(ind.getId()));
	}

	@Override
	public Set<Set<IntegerClass>> getSubClasses(IntegerClassExpression ce, boolean direct) {
		Objects.requireNonNull(ce);
		IntegerClass cls = flattenClassExpression(ce);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getChildren(cls.getId());
		} else {
			set = graph.getDescendants(cls.getId());
		}
		Set<Set<IntegerClass>> ret = new HashSet<>();
		set.forEach(currentElem -> ret.add(toIntegerClass(graph.getEquivalents(currentElem))));
		return ret;
	}

	@Override
	public Set<Set<IntegerDataProperty>> getSubDataProperties(IntegerDataProperty pe, boolean direct) {
		Objects.requireNonNull(pe);
		throw new UnsupportedQueryException("Unsupported query: SubDataProperties of " + pe);
	}

	@Override
	public Set<Set<IntegerObjectPropertyExpression>> getSubObjectProperties(IntegerObjectPropertyExpression pe,
			boolean direct) {
		Objects.requireNonNull(pe);
		classify();
		Integer propId = getObjectPropertyExpressionId(pe);
		IntegerHierarchicalGraph graph = getProcessor().getObjectPropertyHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getChildren(propId);
		} else {
			set = graph.getDescendants(propId);
		}
		Set<Set<IntegerObjectPropertyExpression>> ret = new HashSet<>();
		set.forEach(currentElem -> ret.add(toIntegerObjectPropertyExpression(graph.getEquivalents(currentElem))));
		return ret;
	}

	@Override
	public Set<Set<IntegerClass>> getSuperClasses(IntegerClassExpression ce, boolean direct) {
		Objects.requireNonNull(ce);
		IntegerClass cls = flattenClassExpression(ce);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getParents(cls.getId());
		} else {
			set = graph.getAncestors(cls.getId());
		}
		Set<Set<IntegerClass>> ret = new HashSet<>();
		set.forEach(currentElem -> {
			Set<Integer> equivalents = graph.getEquivalents(currentElem);
			ret.add(toIntegerClass(equivalents));
		});
		return ret;
	}

	@Override
	public Set<Set<IntegerDataProperty>> getSuperDataProperties(IntegerDataProperty pe, boolean direct) {
		Objects.requireNonNull(pe);
		throw new UnsupportedQueryException("Unsupported query: SuperDataProperties of " + pe);
	}

	@Override
	public Set<Set<IntegerObjectPropertyExpression>> getSuperObjectProperties(IntegerObjectPropertyExpression pe,
			boolean direct) {
		Objects.requireNonNull(pe);
		classify();
		Integer propId = getObjectPropertyExpressionId(pe);
		IntegerHierarchicalGraph graph = getProcessor().getObjectPropertyHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getParents(propId);
		} else {
			set = graph.getAncestors(propId);
		}
		Set<Set<IntegerObjectPropertyExpression>> ret = new HashSet<>();
		set.forEach(currentElem -> ret.add(toIntegerObjectPropertyExpression(graph.getEquivalents(currentElem))));
		return ret;
	}

	@Override
	public long getTimeOut() {
		return this.timeOut;
	}

	@Override
	public Set<IntegerClass> getTopClassNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		return toIntegerClass(graph.getEquivalents(graph.getTopElement()));
	}

	@Override
	public Set<IntegerDataProperty> getTopDataPropertyNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getDataPropertyHierarchy();
		return toIntegerDataProperty(graph.getEquivalents(graph.getTopElement()));
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getTopObjectPropertyNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getObjectPropertyHierarchy();
		return toIntegerObjectPropertyExpression(graph.getEquivalents(graph.getTopElement()));
	}

	@Override
	public Set<Set<IntegerClass>> getTypes(IntegerNamedIndividual ind, boolean direct) {
		Objects.requireNonNull(ind);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		Map<Integer, Set<Integer>> map = getProcessor().getDirectTypes();
		Set<Integer> directElemSet = map.get(ind.getId());
		if (Objects.isNull(directElemSet)) {
			directElemSet = Collections.emptySet();
		}
		Set<Integer> set = null;
		if (direct) {
			set = directElemSet;
		} else {
			set = new HashSet<>();
			for (Integer current : directElemSet) {
				set.addAll(graph.getAncestors(current));
			}
		}
		Set<Set<IntegerClass>> ret = new HashSet<>();
		set.forEach(currentElem -> ret.add(toIntegerClass(graph.getEquivalents(currentElem))));
		return ret;
	}

	@Override
	public Set<IntegerClass> getUnsatisfiableClasses() {
		return getBottomClassNode();
	}

	@Override
	public void interrupt() {
		this.classified = false;
		this.interruptRequested = true;
	}

	public boolean isClassified() {
		return this.classified;
	}

	@Override
	public boolean isConsistent() {
		classify();
		return !getUnsatisfiableClasses().contains(getProcessor().getClassHierarchy().getTopElement());
	}

	@Override
	public boolean isEntailed(ComplexIntegerAxiom axiom) {
		Objects.requireNonNull(axiom);
		classify();
		boolean ret = axiom.accept(this.entailmentChecker);
		return ret;
	}

	@Override
	public boolean isEntailed(Set<ComplexIntegerAxiom> axioms) {
		Objects.requireNonNull(axioms);
		classify();

		return axioms.stream().allMatch(axiom -> axiom.accept(this.entailmentChecker));
	}

	@Override
	public boolean isSatisfiable(IntegerClassExpression classExpression) {
		Objects.requireNonNull(classExpression);
		IntegerClass cls = flattenClassExpression(classExpression);
		classify();
		return !getUnsatisfiableClasses().contains(cls);
	}

	private Set<IntegerClass> toIntegerClass(Set<Integer> set) {
		Set<IntegerClass> ret = new HashSet<>();
		set.forEach(elem -> ret.add(getDataTypeFactory().createClass(elem)));
		return ret;
	}

	private Set<IntegerDataProperty> toIntegerDataProperty(Set<Integer> set) {
		Set<IntegerDataProperty> ret = new HashSet<>();
		set.forEach(elem -> ret.add(getDataTypeFactory().createDataProperty(elem)));
		return ret;
	}

	private Set<IntegerNamedIndividual> toIntegerNamedIndividual(Set<Integer> set) {
		Set<IntegerNamedIndividual> ret = new HashSet<>();
		set.forEach(elem -> ret.add(getDataTypeFactory().createNamedIndividual(elem)));
		return ret;
	}

	private Set<IntegerObjectPropertyExpression> toIntegerObjectPropertyExpression(Set<Integer> set) {
		Set<IntegerObjectPropertyExpression> ret = new HashSet<>();
		set.forEach(elem -> ret.add(getDataTypeFactory().createObjectProperty(elem)));
		return ret;
	}

}
