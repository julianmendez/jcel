/*
 * Copyright 2009 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 * jcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.reasoner.main;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.rulebased.RuleBasedProcessor;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.ComplexAxiomExpressivityDetector;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.OntologyExpressivity;
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

	private static final Logger logger = Logger
			.getLogger(RuleBasedReasoner.class.getName());

	private static final String reasonerName = "jcel";

	private Map<IntegerClassExpression, Integer> auxClassInvMap = new HashMap<IntegerClassExpression, Integer>();
	private Map<Integer, IntegerClassExpression> auxClassMap = new HashMap<Integer, IntegerClassExpression>();
	private boolean bufferingMode;
	private boolean classified = false;
	private OntologyEntailmentChecker entailmentChecker = new OntologyEntailmentChecker(
			this);
	private Set<ComplexIntegerAxiom> extendedOntology = new HashSet<ComplexIntegerAxiom>();
	private final IntegerOntologyObjectFactory factory;
	private boolean interruptRequested = false;
	private final Set<ComplexIntegerAxiom> ontology;
	private Set<ComplexIntegerAxiom> pendingAxiomAdditions = new HashSet<ComplexIntegerAxiom>();
	private Set<ComplexIntegerAxiom> pendingAxiomRemovals = new HashSet<ComplexIntegerAxiom>();
	private Processor processor = null;
	private long timeOut = 0;

	public RuleBasedReasoner(Set<ComplexIntegerAxiom> ont,
			IntegerOntologyObjectFactory factory, boolean buffering) {
		if (ont == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.ontology = ont;
		this.factory = factory;
		this.bufferingMode = buffering;
	}

	public boolean addAxiom(ComplexIntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classified = false;
		return this.pendingAxiomAdditions.add(axiom);
	}

	@Override
	public void classify() {
		if (!this.classified) {
			logger.fine("starting classification ...");
			flush();

			Set<ComplexIntegerAxiom> originalAxiomSet = new HashSet<ComplexIntegerAxiom>();
			originalAxiomSet.addAll(this.ontology);
			originalAxiomSet.addAll(this.extendedOntology);

			OntologyExpressivity expressivity = new ComplexAxiomExpressivityDetector(
					originalAxiomSet);

			logger.fine("description logic family : " + expressivity.toString()
					+ " .");

			Set<Integer> originalClassSet = new HashSet<Integer>();
			Set<Integer> originalObjectPropertySet = new HashSet<Integer>();

			for (ComplexIntegerAxiom axiom : originalAxiomSet) {
				originalClassSet.addAll(axiom.getClassesInSignature());
				originalObjectPropertySet.addAll(axiom
						.getObjectPropertiesInSignature());
			}

			logger.fine("number of axioms : " + originalAxiomSet.size());
			logger.fine("number of classes : " + originalClassSet.size());
			logger.fine("number of object properties : "
					+ originalObjectPropertySet.size());

			OntologyNormalizer axiomNormalizer = new OntologyNormalizer();
			Set<NormalizedIntegerAxiom> normalizedAxiomSet = axiomNormalizer
					.normalize(originalAxiomSet, this.factory);
			originalAxiomSet.clear();

			this.processor = new RuleBasedProcessor(originalObjectPropertySet,
					originalClassSet, normalizedAxiomSet, expressivity,
					this.factory.getNormalizedAxiomFactory(),
					this.factory.getEntityManager());

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
			if (classIndex == null) {
				Integer auxClassId = this.factory.getEntityManager()
						.createAnonymousEntity(IntegerEntityType.CLASS, false);
				ret = getDataTypeFactory().createClass(auxClassId);
				this.auxClassMap.put(auxClassId, ce);
				this.auxClassInvMap.put(ce, auxClassId);
				Set<IntegerClassExpression> argument = new HashSet<IntegerClassExpression>();
				argument.add(ret);
				argument.add(ce);
				this.extendedOntology.add(this.factory.getComplexAxiomFactory()
						.createEquivalentClassesAxiom(argument));
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

		this.ontology.removeAll(this.pendingAxiomRemovals);
		this.pendingAxiomRemovals.clear();

		this.ontology.addAll(this.pendingAxiomAdditions);
		this.pendingAxiomAdditions.clear();
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
		IntegerHierarchicalGraph graph = getProcessor()
				.getDataPropertyHierarchy();
		return toIntegerDataProperty(graph.getEquivalents(graph
				.getBottomElement()));
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getBottomObjectPropertyNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor()
				.getObjectPropertyHierarchy();
		return toIntegerObjectPropertyExpression(graph.getEquivalents(graph
				.getBottomElement()));
	}

	@Override
	public boolean getBufferingMode() {
		return this.bufferingMode;
	}

	@Override
	public Set<Set<IntegerClass>> getDataPropertyDomains(
			IntegerDataProperty pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(
				"Unsupported query: DataPropertyDomains of " + pe);
	}

	@Override
	public Set<IntegerClass> getDataPropertyValues(IntegerNamedIndividual ind,
			IntegerDataProperty pe) {
		if (ind == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(
				"Unsupported query: DataPropertyValues of " + ind + "," + pe);
	}

	private IntegerDataTypeFactory getDataTypeFactory() {
		return this.factory.getDataTypeFactory();
	}

	@Override
	public Set<Set<IntegerNamedIndividual>> getDifferentIndividuals(
			IntegerNamedIndividual ind) {
		if (ind == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerClass>> getDisjointClasses(IntegerClassExpression ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerDataProperty>> getDisjointDataProperties(
			IntegerDataPropertyExpression pe) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerObjectPropertyExpression>> getDisjointObjectProperties(
			IntegerObjectPropertyExpression pe) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IntegerClass> getEquivalentClasses(IntegerClassExpression ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerClass cls = flattenClassExpression(ce);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		return toIntegerClass(graph.getEquivalents(cls.getId()));
	}

	@Override
	public Set<IntegerDataProperty> getEquivalentDataProperties(
			IntegerDataProperty pe) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(
				"Unsupported query: EquivalentDataProperties of " + pe);
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getEquivalentObjectProperties(
			IntegerObjectPropertyExpression pe) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		Integer propId = getObjectPropertyExpressionId(pe);
		IntegerHierarchicalGraph graph = getProcessor()
				.getObjectPropertyHierarchy();
		return toIntegerObjectPropertyExpression(graph.getEquivalents(propId));
	}

	@Override
	public Set<Set<IntegerNamedIndividual>> getInstances(
			IntegerClassExpression ce, boolean direct) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<IntegerNamedIndividual>> ret = new HashSet<Set<IntegerNamedIndividual>>();
		IntegerClass cls = flattenClassExpression(ce);
		classify();

		Integer classId = cls.getId();
		Set<Integer> classIdSet = new HashSet<Integer>();
		classIdSet.add(classId);
		if (!direct) {
			classIdSet.addAll(getProcessor().getClassHierarchy()
					.getDescendants(classId));
		}

		Set<Integer> indivIdSet = new HashSet<Integer>();
		indivIdSet.addAll(getProcessor().getDirectTypes().keySet());

		while (!indivIdSet.isEmpty()) {
			Integer indivId = indivIdSet.iterator().next();
			indivIdSet.remove(indivId);

			Set<Integer> types = getProcessor().getDirectTypes().get(indivId);
			boolean found = types.contains(classId);

			if (!found && !direct) {
				Set<Integer> classIdSetCopy = new HashSet<Integer>();
				classIdSetCopy.addAll(classIdSet);
				classIdSetCopy.retainAll(types);
				found = !classIdSetCopy.isEmpty();
			}

			if (found) {
				Set<Integer> equivIndivId = getProcessor()
						.getSameIndividualMap().get(indivId);
				Set<IntegerNamedIndividual> elem = toIntegerNamedIndividual(equivIndivId);
				indivIdSet.removeAll(equivIndivId);
				ret.add(elem);
			}
		}

		return ret;
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getInverseObjectProperties(
			IntegerObjectPropertyExpression pe) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerClass>> getObjectPropertyDomains(
			IntegerObjectPropertyExpression pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	private Integer getObjectPropertyExpressionId(
			IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(new ObjectPropertyIdFinder(this.factory
				.getEntityManager()));
	}

	@Override
	public Set<Set<IntegerClass>> getObjectPropertyRanges(
			IntegerObjectPropertyExpression pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<IntegerNamedIndividual>> getObjectPropertyValues(
			IntegerNamedIndividual ind, IntegerObjectPropertyExpression pe) {
		if (ind == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ComplexIntegerAxiom> getPendingAxiomAdditions() {
		return Collections.unmodifiableSet(this.pendingAxiomAdditions);
	}

	@Override
	public Set<ComplexIntegerAxiom> getPendingAxiomRemovals() {
		return Collections.unmodifiableSet(this.pendingAxiomRemovals);
	}

	public Processor getProcessor() {
		return this.processor;
	}

	@Override
	public String getReasonerName() {
		return reasonerName;
	}

	@Override
	public String getReasonerVersion() {
		return getClass().getPackage().getImplementationVersion();
	}

	@Override
	public Set<ComplexIntegerAxiom> getRootOntology() {
		return Collections.unmodifiableSet(this.ontology);
	}

	@Override
	public Set<IntegerNamedIndividual> getSameIndividuals(
			IntegerNamedIndividual ind) {
		if (ind == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		return toIntegerNamedIndividual(getProcessor().getSameIndividualMap()
				.get(ind.getId()));
	}

	@Override
	public Set<Set<IntegerClass>> getSubClasses(IntegerClassExpression ce,
			boolean direct) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerClass cls = flattenClassExpression(ce);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getChildren(cls.getId());
		} else {
			set = graph.getDescendants(cls.getId());
		}
		Set<Set<IntegerClass>> ret = new HashSet<Set<IntegerClass>>();
		for (Integer currentElem : set) {
			ret.add(toIntegerClass(graph.getEquivalents(currentElem)));
		}
		return ret;
	}

	@Override
	public Set<Set<IntegerDataProperty>> getSubDataProperties(
			IntegerDataProperty pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(
				"Unsupported query: SubDataProperties of " + pe);
	}

	@Override
	public Set<Set<IntegerObjectPropertyExpression>> getSubObjectProperties(
			IntegerObjectPropertyExpression pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		Integer propId = getObjectPropertyExpressionId(pe);
		IntegerHierarchicalGraph graph = getProcessor()
				.getObjectPropertyHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getChildren(propId);
		} else {
			set = graph.getDescendants(propId);
		}
		Set<Set<IntegerObjectPropertyExpression>> ret = new HashSet<Set<IntegerObjectPropertyExpression>>();
		for (Integer currentElem : set) {
			ret.add(toIntegerObjectPropertyExpression(graph
					.getEquivalents(currentElem)));
		}
		return ret;
	}

	@Override
	public Set<Set<IntegerClass>> getSuperClasses(IntegerClassExpression ce,
			boolean direct) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerClass cls = flattenClassExpression(ce);
		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getParents(cls.getId());
		} else {
			set = graph.getAncestors(cls.getId());
		}
		Set<Set<IntegerClass>> ret = new HashSet<Set<IntegerClass>>();
		for (Integer currentElem : set) {
			ret.add(toIntegerClass(graph.getEquivalents(currentElem)));
		}
		return ret;
	}

	@Override
	public Set<Set<IntegerDataProperty>> getSuperDataProperties(
			IntegerDataProperty pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(
				"Unsupported query: SuperDataProperties of " + pe);
	}

	@Override
	public Set<Set<IntegerObjectPropertyExpression>> getSuperObjectProperties(
			IntegerObjectPropertyExpression pe, boolean direct) {
		if (pe == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		Integer propId = getObjectPropertyExpressionId(pe);
		IntegerHierarchicalGraph graph = getProcessor()
				.getObjectPropertyHierarchy();
		Set<Integer> set = null;
		if (direct) {
			set = graph.getParents(propId);
		} else {
			set = graph.getAncestors(propId);
		}
		Set<Set<IntegerObjectPropertyExpression>> ret = new HashSet<Set<IntegerObjectPropertyExpression>>();
		for (Integer currentElem : set) {
			ret.add(toIntegerObjectPropertyExpression(graph
					.getEquivalents(currentElem)));
		}
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
		IntegerHierarchicalGraph graph = getProcessor()
				.getDataPropertyHierarchy();
		return toIntegerDataProperty(graph
				.getEquivalents(graph.getTopElement()));
	}

	@Override
	public Set<IntegerObjectPropertyExpression> getTopObjectPropertyNode() {
		classify();
		IntegerHierarchicalGraph graph = getProcessor()
				.getObjectPropertyHierarchy();
		return toIntegerObjectPropertyExpression(graph.getEquivalents(graph
				.getTopElement()));
	}

	@Override
	public Set<Set<IntegerClass>> getTypes(IntegerNamedIndividual ind,
			boolean direct) {
		if (ind == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		IntegerHierarchicalGraph graph = getProcessor().getClassHierarchy();
		Map<Integer, Set<Integer>> map = getProcessor().getDirectTypes();
		Set<Integer> directElemSet = map.get(ind.getId());
		if (directElemSet == null) {
			directElemSet = Collections.emptySet();
		}
		Set<Integer> set = null;
		if (direct) {
			set = directElemSet;
		} else {
			set = new HashSet<Integer>();
			for (Integer current : directElemSet) {
				set.addAll(graph.getAncestors(current));
			}
		}
		Set<Set<IntegerClass>> ret = new HashSet<Set<IntegerClass>>();
		for (Integer currentElem : set) {
			ret.add(toIntegerClass(graph.getEquivalents(currentElem)));
		}
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
		return !getUnsatisfiableClasses().contains(
				getProcessor().getClassHierarchy().getTopElement());
	}

	@Override
	public boolean isEntailed(ComplexIntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		boolean ret = axiom.accept(this.entailmentChecker);
		return ret;
	}

	@Override
	public boolean isEntailed(Set<ComplexIntegerAxiom> axioms) {
		if (axioms == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		classify();
		boolean ret = true;
		for (Iterator<ComplexIntegerAxiom> it = axioms.iterator(); ret
				&& it.hasNext();) {
			ComplexIntegerAxiom axiom = it.next();
			ret = ret && axiom.accept(this.entailmentChecker);
		}
		return ret;
	}

	@Override
	public boolean isSatisfiable(IntegerClassExpression classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerClass cls = flattenClassExpression(classExpression);
		classify();
		return !getUnsatisfiableClasses().contains(cls);
	}

	public boolean removeAxiom(ComplexIntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classified = false;
		return this.pendingAxiomRemovals.add(axiom);
	}

	private Set<IntegerClass> toIntegerClass(Set<Integer> set) {
		Set<IntegerClass> ret = new HashSet<IntegerClass>();
		for (Integer elem : set) {
			if (!this.auxClassMap.containsKey(elem)) {
				ret.add(getDataTypeFactory().createClass(elem));
			}
		}
		return ret;
	}

	private Set<IntegerDataProperty> toIntegerDataProperty(Set<Integer> set) {
		Set<IntegerDataProperty> ret = new HashSet<IntegerDataProperty>();
		for (Integer elem : set) {
			ret.add(getDataTypeFactory().createDataProperty(elem));
		}
		return ret;
	}

	private Set<IntegerNamedIndividual> toIntegerNamedIndividual(
			Set<Integer> set) {
		Set<IntegerNamedIndividual> ret = new HashSet<IntegerNamedIndividual>();
		for (Integer elem : set) {
			ret.add(getDataTypeFactory().createNamedIndividual(elem));
		}
		return ret;
	}

	private Set<IntegerObjectPropertyExpression> toIntegerObjectPropertyExpression(
			Set<Integer> set) {
		Set<IntegerObjectPropertyExpression> ret = new HashSet<IntegerObjectPropertyExpression>();
		for (Integer elem : set) {
			ret.add(getDataTypeFactory().createObjectProperty(elem));
		}
		return ret;
	}

}
