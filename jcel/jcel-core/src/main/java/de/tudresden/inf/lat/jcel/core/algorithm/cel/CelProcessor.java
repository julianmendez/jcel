/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core.algorithm.cel;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.common.UnclassifiedOntologyException;
import de.tudresden.inf.lat.jcel.core.graph.IntegerBinaryRelation;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerRelationMapImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;

/**
 * Classifies an ontology using the CEL algorithm. The rules are:
 * 
 * <ul>
 * <li>CR1 : <b>if</b> A<sub>1</sub>, &hellip; , A<sub>n</sub> &isin; S(X)
 * <b>and</b> A<sub>1</sub> &sqcap; &hellip; &sqcap; A<sub>n</sub> &sqsube; B
 * &isin; O <b>and</b> B &notin; S(X) <b>then</b> S(X) := S(X) &cup; {B}</li>
 * <li>CR2 : <b>if</b> A &isin; S(X) <b>and</b> A &sqsube; &exist; r <i>.</i> B
 * &isin; O <b>and</b> (X,B) &notin; R(r) <b>then</b> R(r) := R(r) &cup;{(X,B)}</li>
 * <li>CR3 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> A &isin; S(Y) <b>and</b>
 * &exist; r <i>.</i> A &sqsube; B &isin; O <b>and</b> B &notin; S(X)
 * <b>then</b> S(X) := S(X) &cup; {B}</li>
 * <li>CR4 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> &perp; &isin; S(Y)
 * <b>and</b> &perp; &notin; S(X) <b>then</b> S(X) := S(X) &cup; {&perp;}</li>
 * <li>CR5 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> r &sqsube; s &isin; O
 * <b>and</b> (X,Y) &notin; R(s) <b>then</b> R(s) := R(s) &cup; {(X,Y)}</li>
 * <li>CR6 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> (Y,Z) &isin; R(s) <b>and</b>
 * r &compfn; s &sqsube; t &isin; O <b>and</b> (X,Z) &notin; R(t) <b>then</b>
 * R(t) := R(t) &cup; {(X,Z)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CelProcessor implements Processor {

	private static final Integer bottomClassId = IntegerEntityManager.bottomClassId;
	private static final Integer bottomObjectPropertyId = IntegerEntityManager.bottomObjectPropertyId;
	private static final Logger logger = Logger.getLogger(CelProcessor.class
			.getName());
	private static final Integer topClassId = IntegerEntityManager.topClassId;
	private static final Integer topObjectPropertyId = IntegerEntityManager.topObjectPropertyId;

	private final NormalizedIntegerAxiomFactory axiomFactory;
	private IntegerSubsumerGraphImpl classGraph = null;
	private IntegerHierarchicalGraph classHierarchy = null;
	private IntegerHierarchicalGraph dataPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> directTypes = null;
	private final IntegerEntityManager entityManager;
	private CelExtendedOntology extendedOntology = null;
	private boolean isReady = false;
	private IntegerSubsumerGraphImpl objectPropertyGraph = null;
	private IntegerHierarchicalGraph objectPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> propertyUsedByClass = null;
	private final Deque<ExtensionEntry> queueEntries = new ArrayDeque<ExtensionEntry>();
	private final Deque<Integer> queueKeys = new ArrayDeque<Integer>();
	private IntegerRelationMapImpl relationSet = null;
	private Map<Integer, Set<Integer>> sameIndividualMap = null;
	private Map<Integer, Set<Integer>> transitiveSubsumed = null;

	/**
	 * Constructs a new CEL processor.
	 * 
	 * @param normalizedAxiomSet
	 *            set of axioms
	 */
	public CelProcessor(Set<Integer> originalObjectProperties,
			Set<Integer> originalClasses,
			Set<NormalizedIntegerAxiom> normalizedAxiomSet,
			NormalizedIntegerAxiomFactory factory, IntegerEntityManager manager) {
		if (originalObjectProperties == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (originalClasses == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (normalizedAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.axiomFactory = factory;
		this.entityManager = manager;
		preProcess(originalObjectProperties, originalClasses,
				normalizedAxiomSet);
	}

	private void addToQueue(Integer className,
			Collection<ExtensionEntry> entrySet) {
		for (ExtensionEntry entry : entrySet) {
			this.queueKeys.push(className);
			this.queueEntries.push(entry);
		}
	}

	/**
	 * @param hierarchicalGraph
	 *            graph containing direct subsumers
	 * @return a map with all the direct types for each individual.
	 */
	private Map<Integer, Set<Integer>> computeDirectTypes(
			IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		Set<Integer> individuals = getEntityManager().getEntities(
				IntegerEntityType.INDIVIDUAL, false);
		for (Integer indiv : individuals) {
			Set<Integer> subsumers = hierarchicalGraph
					.getParents(getEntityManager().getAuxiliaryNominal(indiv));
			for (Integer elem : subsumers) {
				if (getEntityManager().getAuxiliaryNominals().contains(elem)) {
					throw new IllegalStateException(
							"An individual has another individual as direct subsumer.");
				}
			}
			ret.put(indiv, Collections.unmodifiableSet(subsumers));
		}
		return ret;
	}

	private Map<Integer, Set<Integer>> computeSameIndividualMap(
			IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		Set<Integer> individuals = getEntityManager().getEntities(
				IntegerEntityType.INDIVIDUAL, false);
		for (Integer indiv : individuals) {
			Set<Integer> equivalentClasses = hierarchicalGraph
					.getEquivalents(getEntityManager().getAuxiliaryNominal(
							indiv));
			Set<Integer> equivalents = new HashSet<Integer>();
			for (Integer elem : equivalentClasses) {
				if (getEntityManager().getAuxiliaryNominals().contains(elem)) {
					equivalents.add(getEntityManager().getIndividual(elem));
				}
			}
			ret.put(indiv, Collections.unmodifiableSet(equivalents));
		}
		return ret;
	}

	private IntegerSubsumerGraphImpl createClassGraph(
			Set<Integer> originalClassSet, Set<NormalizedIntegerAxiom> axiomSet) {

		Set<Integer> classIdSet = new HashSet<Integer>();
		classIdSet.addAll(originalClassSet);
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			classIdSet.addAll(axiom.getClassesInSignature());
		}
		IntegerSubsumerGraphImpl ret = new IntegerSubsumerGraphImpl(
				bottomClassId, topClassId);
		for (Integer index : classIdSet) {
			ret.addAncestor(index, topClassId);
		}
		ret.addAncestor(topClassId, topClassId);
		return ret;
	}

	private IntegerSubsumerGraphImpl createObjectPropertyGraph(
			Set<Integer> originalPropertySet,
			Set<NormalizedIntegerAxiom> axiomSet) {
		IntegerSubsumerGraphImpl ret = new IntegerSubsumerGraphImpl(
				bottomObjectPropertyId, topObjectPropertyId);
		Set<Integer> propertyIdSet = new HashSet<Integer>();
		propertyIdSet.addAll(originalPropertySet);
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			propertyIdSet.addAll(axiom.getObjectPropertiesInSignature());
		}
		for (Integer index : propertyIdSet) {
			ret.addAncestor(index, topObjectPropertyId);
		}
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			if (axiom instanceof RI2Axiom) {
				RI2Axiom current = (RI2Axiom) axiom;
				ret.addAncestor(current.getSubProperty(),
						current.getSuperProperty());
			}
		}
		makeTransitiveClosure(ret);
		return ret;
	}

	private Map<Integer, Set<Integer>> createPropertyUseMap() {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (Integer cA : getClassGraph().getElements()) {
			Set<Integer> propertySet = new HashSet<Integer>();
			for (Integer r : getObjectPropertyGraph().getElements()) {
				if (!(this.relationSet.getBySecond(r, cA).isEmpty())) {
					propertySet.add(r);
				}
			}
			ret.put(cA, propertySet);
		}
		return ret;
	}

	private IntegerRelationMapImpl createRelationSet(
			Collection<Integer> collection) {
		IntegerRelationMapImpl ret = new IntegerRelationMapImpl();
		for (Integer index : collection) {
			ret.add(index);
		}
		return ret;
	}

	private Map<Integer, Set<Integer>> createTransitiveSubsumed() {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (Integer r : getObjectPropertyGraph().getElements()) {
			Set<Integer> related = new HashSet<Integer>();
			for (Integer s : getObjectPropertyGraph().getElements()) {
				if (isReflexiveTransitiveSubsumed(r, s)) {
					related.add(s);
				}
			}
			ret.put(r, related);
		}
		return ret;
	}

	/**
	 * Returns the class graph.
	 * 
	 * @return the class graph.
	 */
	protected IntegerSubsumerGraph getClassGraph() {
		return this.classGraph;
	}

	@Override
	public IntegerHierarchicalGraph getClassHierarchy() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return this.classHierarchy;
	}

	@Override
	public IntegerHierarchicalGraph getDataPropertyHierarchy()
			throws UnclassifiedOntologyException {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return this.dataPropertyHierarchy;
	}

	/**
	 * Computes the descendants using only a hierarchical graph.
	 * 
	 * @param hierarchicalGraph
	 *            a hierarchical graph containing parents and children
	 * @param vertex
	 *            starting vertex to compute the descendants
	 * @return the descendants according the graph
	 */
	private Set<Integer> getDescendants(
			IntegerHierarchicalGraph hierarchicalGraph, Integer vertex) {
		Set<Integer> visited = new HashSet<Integer>();
		Set<Integer> queue = new HashSet<Integer>();
		queue.add(vertex);
		while (!queue.isEmpty()) {
			Integer elem = queue.iterator().next();
			queue.remove(elem);
			visited.add(elem);
			Set<Integer> children = new HashSet<Integer>();
			children.addAll(hierarchicalGraph.getChildren(elem));
			children.removeAll(visited);
			queue.addAll(children);
		}
		return visited;
	}

	@Override
	public Map<Integer, Set<Integer>> getDirectTypes() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return Collections.unmodifiableMap(this.directTypes);
	}

	/**
	 * Returns the id generator.
	 * 
	 * @return the id generator.
	 */
	protected IntegerEntityManager getEntityManager() {
		return this.entityManager;
	}

	private CelExtendedOntology getExtendedOntology() {
		return this.extendedOntology;
	}

	private IntegerSubsumerGraph getObjectPropertyGraph() {
		return this.objectPropertyGraph;
	}

	@Override
	public IntegerHierarchicalGraph getObjectPropertyHierarchy() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return this.objectPropertyHierarchy;
	}

	public NormalizedIntegerAxiomFactory getOntologyObjectFactory() {
		return this.axiomFactory;
	}

	private Set<Integer> getPropertyUsedByClass(Integer cA) {
		return this.propertyUsedByClass.get(cA);
	}

	/**
	 * Returns the binary relation for a given id, or and empty relation if the
	 * id is unknown.
	 * 
	 * @param relationId
	 *            relation id
	 * @return the binary relation for the given id, or an empty relation if no
	 *         relation is already defined
	 */
	protected IntegerBinaryRelation getRelation(Integer relationId) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.get(relationId);
	}

	/**
	 * Returns a set containing all relation ids.
	 * 
	 * @return the set of all relation ids
	 */
	protected Set<Integer> getRelationIdSet() {
		return Collections.unmodifiableSet(this.relationSet.getElements());
	}

	@Override
	public Map<Integer, Set<Integer>> getSameIndividualMap() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return Collections.unmodifiableMap(this.sameIndividualMap);
	}

	/**
	 * This is a graph reachability algorithm that tests whether an element d is
	 * reachable from an element c using a path where each segment is from any
	 * of the properties in R.
	 * 
	 * @param c
	 *            first element in the path
	 * @param d
	 *            last element in the path
	 * @return <code>true</code> if it is possible to reach d from c using any
	 *         possible segment, <code>false</code> otherwise
	 */
	private boolean isConnectedTo(Integer c, Integer d) {
		Set<Integer> visited = new HashSet<Integer>();
		Set<Integer> toVisit = new HashSet<Integer>();
		toVisit.add(c);
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			visited.add(elem);
			Set<Integer> newToVisit = new HashSet<Integer>();
			for (Integer r : getPropertyUsedByClass(elem)) {
				IntegerBinaryRelation relation = this.relationSet.get(r);
				newToVisit.addAll(relation.getByFirst(elem));
			}
			newToVisit.removeAll(visited);
			toVisit.addAll(newToVisit);
		}
		return visited.contains(d);
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}

	private boolean isReflexiveTransitiveSubsumed(Integer leftPropertyName,
			Integer rightPropertyName) {
		return (this.objectPropertyGraph != null)
				&& this.objectPropertyGraph.containsPair(leftPropertyName,
						rightPropertyName);
	}

	private void makeTransitiveClosure(IntegerSubsumerGraphImpl graph) {
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;
			for (Integer elem : graph.getElements()) {
				Collection<Integer> subsumerSet = graph.getSubsumers(elem);
				Set<Integer> allSubsumers = new HashSet<Integer>();
				allSubsumers.add(elem);
				for (Integer otherElem : subsumerSet) {
					allSubsumers.addAll(graph.getSubsumers(otherElem));
				}
				allSubsumers.removeAll(subsumerSet);
				if (!allSubsumers.isEmpty()) {
					hasChanged = true;
					for (Integer subsumer : allSubsumers) {
						graph.addAncestor(elem, subsumer);
					}
				}
			}
		}
	}

	/**
	 * Post processes the data after the classification phase.
	 */
	protected void postProcess() {
		removeAuxiliaryObjectProperties();
		this.objectPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				this.objectPropertyGraph);
		this.objectPropertyGraph = null;

		removeAuxiliaryClassesExceptNominals();
		IntegerHierarchicalGraph hierarchicalGraph = new IntegerHierarchicalGraphImpl(
				this.classGraph);
		processNominals(hierarchicalGraph);
		this.directTypes = computeDirectTypes(hierarchicalGraph);
		this.sameIndividualMap = computeSameIndividualMap(hierarchicalGraph);

		removeAuxiliaryNominals();
		this.classHierarchy = new IntegerHierarchicalGraphImpl(this.classGraph);
		this.classGraph = null;
	}

	private void prepareQueue(CelExtendedOntology ontology) {
		Set<Integer> classNameSet = new HashSet<Integer>();
		classNameSet.addAll(ontology.getClassSet());
		for (Integer className : classNameSet) {
			addToQueue(className, ontology.getClassEntries(className));
			addToQueue(className, ontology.getClassEntries(topClassId));
		}
	}

	/**
	 * The configuration follows the following steps:
	 * <ul>
	 * <li>normalizes the ontology creating auxiliary entities</li>
	 * <li>creates an extended ontonlogy based on the normalized ontology</li>
	 * <li>adds the classes</li>
	 * <li>creates the property hierarchy</li>
	 * <li>prepares all the queues to run the algorithm</li>
	 * </ul>
	 * 
	 * @param normalizedAxiomSet
	 *            set of axioms, i.e. the ontology
	 */
	protected void preProcess(Set<Integer> originalObjectProperties,
			Set<Integer> originalClasses,
			Set<NormalizedIntegerAxiom> normalizedAxiomSet) {
		if (originalObjectProperties == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (originalClasses == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (normalizedAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.isReady = false;

		logger.fine("using " + getClass().getSimpleName() + " ...");

		logger.fine("configuring processor ...");

		this.dataPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				new IntegerSubsumerGraphImpl(
						IntegerEntityManager.bottomDataPropertyId,
						IntegerEntityManager.topDataPropertyId));

		// These sets include the declared entities that are not present in the
		// normalized axioms.
		Set<Integer> originalClassSet = new HashSet<Integer>();
		originalClassSet.addAll(originalClasses);
		originalClassSet.add(bottomClassId);
		originalClassSet.add(topClassId);

		Set<Integer> originalObjectPropertySet = new HashSet<Integer>();
		originalObjectPropertySet.addAll(originalObjectProperties);
		originalObjectPropertySet.add(bottomObjectPropertyId);
		originalObjectPropertySet.add(topObjectPropertyId);

		logger.finer("normalizing ontology ...");
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		ontology.addAll(normalizedAxiomSet);

		logger.finer("auxiliary classes created (including nominals) : "
				+ getEntityManager().getEntities(IntegerEntityType.CLASS, true)
						.size());
		logger.finer("auxiliary classes created for nominals : "
				+ (getEntityManager().getIndividuals().size()));
		logger.finer("auxiliary object properties created : "
				+ getEntityManager().getEntities(
						IntegerEntityType.OBJECT_PROPERTY, true).size());

		logger.finer("creating extended ontology ...");
		this.extendedOntology = new CelExtendedOntology();
		this.extendedOntology.load(ontology);

		logger.finer("creating class graph ...");
		this.classGraph = createClassGraph(originalClassSet, ontology);

		logger.finer("creating property graph ...");
		this.objectPropertyGraph = createObjectPropertyGraph(
				originalObjectPropertySet, ontology);

		this.relationSet = createRelationSet(this.objectPropertyGraph
				.getElements());

		this.propertyUsedByClass = createPropertyUseMap();

		this.transitiveSubsumed = createTransitiveSubsumed();

		logger.finer("preparing queue ...");
		this.queueKeys.clear();
		this.queueEntries.clear();
		prepareQueue(this.extendedOntology);

		logger.fine("processor configured.");
	}

	@Override
	public boolean process() {
		if (!this.isReady) {
			if (!this.queueKeys.isEmpty()) {
				process(this.queueKeys.pop(), this.queueEntries.pop());
			} else if (!this.isReady) {
				postProcess();
				this.isReady = true;
			}
		}
		return !this.isReady;
	}

	private void process(Integer cA, ExtensionEntry eX) {
		if (eX.isImplication()) {
			processImplication(cA, eX.asImplication());
		} else if (eX.isExistential()) {
			processExistential(cA, eX.asExistential());
		} else {
			throw new RuntimeException(
					"Internal error: entry was not recognized " + eX);
		}
	}

	private void processBottom(Integer className) {
		this.classGraph.addAncestor(className, bottomClassId);

		for (Integer relation : this.relationSet.getElements()) {
			for (Integer firstComponent : this.relationSet.getBySecond(
					relation, className)) {
				if (!this.classGraph
						.containsPair(firstComponent, bottomClassId)) {
					processBottom(firstComponent);
				}
			}
		}
	}

	private void processExistential(Integer cA, ExistentialEntry eX) {
		Integer r = eX.getPropertyId();
		Integer cB = eX.getClassId();
		Integer bottom = bottomClassId;

		if (!this.relationSet.contains(r, cA, cB)) {

			if (this.classGraph.containsPair(cB, bottom)
					&& !this.classGraph.containsPair(cA, bottom)) {

				processBottom(cA);

			}

			processNewEdge(cA, r, cB);
		}
	}

	private void processImplication(Integer cA, ImplicationEntry eX) {
		Set<Integer> vecB = eX.getOperands();
		Integer cB = eX.getSuperClass();
		Collection<Integer> sSofA = this.classGraph.getSubsumers(cA);
		Integer bottom = bottomClassId;

		if (sSofA.containsAll(vecB) && !sSofA.contains(cB)) {

			if (cB.equals(bottom)) {

				processBottom(cA);

			} else {

				this.classGraph.addAncestor(cA, cB);

				addToQueue(cA, getExtendedOntology().getClassEntries(cB));

				Set<Integer> propertySet = getPropertyUsedByClass(cA);

				for (Integer r : propertySet) {

					Set<ExtensionEntry> existentialEntries = getExtendedOntology()
							.getExistentialEntries(r, cB);

					Collection<Integer> classSet = this.relationSet
							.getBySecond(r, cA);

					for (Integer cAprime : classSet) {

						addToQueue(cAprime, existentialEntries);
					}
				}
			}
		}
	}

	private void processNewEdge(Integer cA, Integer r, Integer cB) {
		for (Integer s : this.transitiveSubsumed.get(r)) {

			this.relationSet.add(s, cA, cB);
			getPropertyUsedByClass(cB).add(s);

			for (Integer cBprime : this.classGraph.getSubsumers(cB)) {
				addToQueue(cA,
						getExtendedOntology().getExistentialEntries(s, cBprime));
			}

			for (RI3Axiom axiom : getExtendedOntology()
					.getSubPropertyAxiomSetByRight(s)) {

				Integer t = axiom.getLeftSubProperty();
				Integer u = axiom.getSuperProperty();

				Collection<Integer> classSet = this.relationSet.getBySecond(t,
						cA);

				for (Integer cAprime : classSet) {

					if (!this.relationSet.contains(u, cAprime, cB)) {
						processNewEdge(cAprime, u, cB);
					}
				}
			}

			for (RI3Axiom axiom : getExtendedOntology()
					.getSubPropertyAxiomSetByLeft(s)) {

				Integer t = axiom.getRightSubProperty();
				Integer u = axiom.getSuperProperty();

				Collection<Integer> classSet = this.relationSet.getByFirst(t,
						cB);

				for (Integer cBprime : classSet) {

					if (!this.relationSet.contains(u, cA, cBprime)) {
						processNewEdge(cA, u, cBprime);
					}
				}
			}
		}
	}

	/**
	 * Processes the nominals after the execution of the classification
	 * algorithm. It requires a hierarchical graph to get the descendants.
	 * 
	 * @param hierarchicalGraph
	 *            the hierarchical graph
	 */
	private void processNominals(IntegerHierarchicalGraph hierarchicalGraph) {
		Set<Integer> nominals = getEntityManager().getAuxiliaryNominals();
		for (Integer indiv : nominals) {
			Set<Integer> descendants = getDescendants(hierarchicalGraph, indiv);
			for (Integer c : descendants) {
				for (Integer d : descendants) {
					Collection<Integer> sC = getClassGraph().getSubsumers(c);
					Collection<Integer> sD = getClassGraph().getSubsumers(d);
					if (!(sD.containsAll(sC))) {
						if (isConnectedTo(c, d)) {
							for (Integer elem : sD) {
								this.classGraph.addAncestor(c, elem);
							}
						}
						for (Integer nominal : nominals) {
							if (isConnectedTo(nominal, d)) {
								for (Integer elem : sD) {
									this.classGraph.addAncestor(c, elem);
								}
							}
						}
					}
				}
			}
		}
	}

	private void removeAuxiliaryClassesExceptNominals() {
		Set<Integer> reqClasses = new HashSet<Integer>();
		for (Integer elem : getClassGraph().getElements()) {
			if (!getEntityManager().isAuxiliary(elem)) {
				reqClasses.add(elem);
			}
		}
		reqClasses.addAll(getEntityManager().getAuxiliaryNominals());
		this.classGraph.retainAll(reqClasses);
	}

	private void removeAuxiliaryNominals() {
		Set<Integer> reqClasses = new HashSet<Integer>();
		reqClasses.addAll(getClassGraph().getElements());
		reqClasses.removeAll(getEntityManager().getAuxiliaryNominals());
		this.classGraph.retainAll(reqClasses);
	}

	private void removeAuxiliaryObjectProperties() {
		Set<Integer> reqObjectProperties = new HashSet<Integer>();
		for (Integer elem : getObjectPropertyGraph().getElements()) {
			if (!getEntityManager().isAuxiliary(elem)) {
				reqObjectProperties.add(elem);
			}
		}
		this.objectPropertyGraph.retainAll(reqObjectProperties);
	}

}
