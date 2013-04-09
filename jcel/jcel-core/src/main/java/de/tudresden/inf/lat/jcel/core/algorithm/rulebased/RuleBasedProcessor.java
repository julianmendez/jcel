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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.common.UnclassifiedOntologyException;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.graph.IntegerBinaryRelation;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.core.saturation.SubPropertyNormalizer;
import de.tudresden.inf.lat.jcel.coreontology.axiom.ExtendedOntology;
import de.tudresden.inf.lat.jcel.coreontology.axiom.ExtendedOntologyImpl;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;

/**
 * An object of this class is an implementation of a classification algorithm.
 * 
 * @author Julian Mendez
 */
public class RuleBasedProcessor implements Processor {

	private static final Logger logger = Logger
			.getLogger(RuleBasedProcessor.class.getName());

	private static final long loggingFrequency = 0x1000000;
	private static final Integer topClassId = IntegerEntityManager.topClassId;

	private RChain chainR = null;
	private SChain chainS = null;
	private IntegerHierarchicalGraph classHierarchy = null;
	private IntegerHierarchicalGraph dataPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> directTypes = null;
	private final IntegerEntityManager entityManager;
	private final NormalizedIntegerAxiomFactory factory;
	private boolean isReady = false;
	private long iteration = 0;
	private long loggingCount = loggingFrequency;
	private IntegerHierarchicalGraph objectPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> sameIndividualMap = null;
	private ClassifierStatusImpl status = null;
	private Thread threadR = null;
	private Thread threadS = null;

	/**
	 * Constructs a new rule-based processor.
	 * 
	 * @param originalObjectProperties
	 *            set of original object properties
	 * @param originalClasses
	 *            set of original classes
	 * @param normalizedAxiomSet
	 *            set of normalized axioms
	 * @param expressivity
	 *            expressivity
	 * @param factory
	 *            factory of normalized integer axioms
	 * @param entityManager
	 *            entity manager
	 */
	public RuleBasedProcessor(Set<Integer> originalObjectProperties,
			Set<Integer> originalClasses,
			Set<NormalizedIntegerAxiom> normalizedAxiomSet,
			OntologyExpressivity expressivity,
			NormalizedIntegerAxiomFactory factory,
			IntegerEntityManager entityManager) {
		if (originalObjectProperties == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (originalClasses == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (normalizedAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (expressivity == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entityManager == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.factory = factory;
		this.entityManager = entityManager;

		CompletionRuleChainSelector selector = new CompletionRuleChainSelector(
				expressivity);
		selector.activateProfiler();
		this.chainR = selector.getRChain();
		this.chainS = selector.getSChain();

		preProcess(createExtendedOntology(originalObjectProperties,
				originalClasses, normalizedAxiomSet));
	}

	public void addAxioms(Set<NormalizedIntegerAxiom> normalizedAxiomSet) {
		if (normalizedAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.fine("adding axioms ...");
		this.status.getExtendedOntology().load(normalizedAxiomSet);
		preProcess(this.status.getExtendedOntology());
		logger.fine("processor reset.");
	}

	/**
	 * @param hierarchicalGraph
	 *            graph containing direct subsumers
	 * @return a map with all the direct types for each individual.
	 */
	private Map<Integer, Set<Integer>> computeDirectTypes(
			IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		Set<Integer> individuals = getEntityManager().getIndividuals();
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

	/**
	 * This is a graph reachability algorithm that returns all elements d
	 * reachable from an element c using a path where each segment is from any
	 * of the properties in R.
	 * 
	 * @param c
	 *            first element in the path
	 * @return the set of all nodes reachable from c using any possible segment
	 */
	private Set<Integer> computeReachability(Integer c) {
		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> toVisit = new HashSet<Integer>();
		toVisit.add(c);
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> newToVisit = new HashSet<Integer>();
			for (Integer r : this.status.getObjectPropertiesByFirst(elem)) {
				IntegerBinaryRelation relation = this.status.getRelationSet()
						.get(r);
				newToVisit.addAll(relation.getByFirst(elem));
			}
			newToVisit.removeAll(ret);
			toVisit.addAll(newToVisit);
		}
		return ret;
	}

	private Set<Integer> computeReachability(Integer c,
			Map<Integer, Set<Integer>> reachableNodeCache) {
		Set<Integer> reachableNodes = reachableNodeCache.get(c);
		if (reachableNodes == null) {
			reachableNodes = computeReachability(c);
			reachableNodeCache.put(c, reachableNodes);
		}
		return reachableNodes;
	}

	private Map<Integer, Set<Integer>> computeSameIndividualMap(
			IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		Set<Integer> individuals = getEntityManager().getIndividuals();
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

	/**
	 * Convenience method to create a map entry. This method returns a map
	 * entry.
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return a map entry created using the paramenters
	 */
	private Map.Entry<String, String> createEntry(String key, String value) {
		return new AbstractMap.SimpleEntry<String, String>(key, value);
	}

	private ExtendedOntology createExtendedOntology(
			Set<Integer> originalObjectPropertySet,
			Set<Integer> originalClassSet, Set<NormalizedIntegerAxiom> axioms) {
		SubPropertyNormalizer subPropNormalizer = new SubPropertyNormalizer(
				getOntologyObjectFactory(), getEntityManager());
		Set<NormalizedIntegerAxiom> saturatedNormalizedAxiomSet = subPropNormalizer
				.apply(axioms);
		ExtendedOntology extendedOntology = new ExtendedOntologyImpl();
		extendedOntology.load(saturatedNormalizedAxiomSet);
		for (Integer elem : originalObjectPropertySet) {
			extendedOntology.addObjectProperty(elem);
		}
		for (Integer elem : originalClassSet) {
			extendedOntology.addClass(elem);
		}
		return extendedOntology;
	}

	/**
	 * Returns the class graph.
	 * 
	 * @return the class graph.
	 */
	protected IntegerSubsumerGraph getClassGraph() {
		return this.status.getClassGraph();
	}

	@Override
	public IntegerHierarchicalGraph getClassHierarchy() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return this.classHierarchy;
	}

	/**
	 * Returns information about how the processor configuration.
	 * 
	 * @return information about how the processor configuration
	 */
	public List<Map.Entry<String, String>> getConfigurationInfo() {
		List<Map.Entry<String, String>> ret = new ArrayList<Map.Entry<String, String>>();
		ret.add(createEntry("processor", getClass().getSimpleName()));
		ret.add(createEntry("iterations per log entry", "" + loggingFrequency));
		ret.add(createEntry(
				"classes read (including TOP and BOTTOM classes)",
				""
						+ getEntityManager().getEntities(
								IntegerEntityType.CLASS, false).size()));
		ret.add(createEntry(
				"object properties read (including TOP and BOTTOM object properties)",
				""
						+ getEntityManager().getEntities(
								IntegerEntityType.OBJECT_PROPERTY, false)
								.size()));
		ret.add(createEntry(
				"auxiliary classes created (including nominals)",
				""
						+ getEntityManager().getEntities(
								IntegerEntityType.CLASS, true).size()));
		ret.add(createEntry("auxiliary classes created for nominals", ""
				+ getEntityManager().getIndividuals().size()));
		ret.add(createEntry(
				"auxiliary object properties created",
				""
						+ getEntityManager().getEntities(
								IntegerEntityType.OBJECT_PROPERTY, true).size()));
		ret.add(createEntry("chain S", this.chainS.toString()));
		ret.add(createEntry("chain R", this.chainR.toString()));
		return ret;
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

	private IntegerEntityManager getEntityManager() {
		return this.entityManager;
	}

	private IntegerSubsumerGraph getObjectPropertyGraph() {
		return this.status.getObjectPropertyGraph();
	}

	@Override
	public IntegerHierarchicalGraph getObjectPropertyHierarchy() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return this.objectPropertyHierarchy;
	}

	/**
	 * Returns the ontology object factory.
	 * 
	 * @return the ontology object factory.
	 */
	public NormalizedIntegerAxiomFactory getOntologyObjectFactory() {
		return this.factory;
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

		return this.status.getRelationSet().get(relationId);
	}

	/**
	 * Returns a set containing all relation ids.
	 * 
	 * @return the set of all relation ids
	 */
	protected Set<Integer> getRelationIdSet() {
		return Collections.unmodifiableSet(this.status.getRelationSet()
				.getElements());
	}

	@Override
	public Map<Integer, Set<Integer>> getSameIndividualMap() {
		if (!isReady()) {
			throw new UnclassifiedOntologyException();
		}
		return Collections.unmodifiableMap(this.sameIndividualMap);
	}

	/**
	 * Returns information about the processor status.
	 * 
	 * @return information about the processor status.
	 */
	public List<Map.Entry<String, String>> getStatusInfo() {
		List<Map.Entry<String, String>> ret = new ArrayList<Map.Entry<String, String>>();
		ret.add(createEntry("iteration", "" + this.iteration));
		ret.add(createEntry("Q_S", "" + this.status.getNumberOfSEntries()));
		ret.add(createEntry("Q_R", "" + this.status.getNumberOfREntries()));
		ret.add(createEntry("S", "" + this.status.getDeepSizeOfS()));
		ret.add(createEntry("R", "" + this.status.getDeepSizeOfR()));
		ret.add(createEntry("V", "" + this.status.getSizeOfV()));
		ret.add(createEntry("subV", "" + this.status.getDeepSizeOfV()));
		return ret;
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}

	/**
	 * Post processes the data after the classification phase.
	 */
	protected void postProcess() {
		removeAuxiliaryObjectProperties();
		this.objectPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				getObjectPropertyGraph());
		this.status.deleteObjectPropertyGraph();

		removeAuxiliaryClassesExceptNominals();
		IntegerHierarchicalGraph hierarchicalGraph = new IntegerHierarchicalGraphImpl(
				this.status.getClassGraph());
		processNominals(hierarchicalGraph);
		this.directTypes = computeDirectTypes(hierarchicalGraph);
		this.sameIndividualMap = computeSameIndividualMap(hierarchicalGraph);

		removeAuxiliaryNominals();
		this.classHierarchy = new IntegerHierarchicalGraphImpl(
				this.status.getClassGraph());
		this.status.deleteClassGraph();
	}

	/**
	 * The configuration follows the following steps:
	 * <ul>
	 * <li>creates the property hierarchy</li>
	 * <li>prepares all the queues to run the algorithm</li>
	 * </ul>
	 * 
	 * @param ontology
	 *            ontology
	 */
	protected void preProcess(ExtendedOntology ontology) {
		logger.fine("configuring processor ...");

		this.isReady = false;
		this.status = new ClassifierStatusImpl(getEntityManager(), ontology);
		this.dataPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				new IntegerSubsumerGraphImpl(
						IntegerEntityManager.bottomDataPropertyId,
						IntegerEntityManager.topDataPropertyId));
		Set<Integer> classNameSet = new HashSet<Integer>();
		classNameSet.addAll(this.status.getExtendedOntology().getClassSet());
		for (Integer className : classNameSet) {
			this.status.addNewSEntry(className, className);
			this.status.addNewSEntry(className, topClassId);
		}

		logger.fine("processor configured.");
		logger.fine(showConfigurationInfo());
	}

	@Override
	public boolean process() {
		if (!this.isReady) {
			if (threadS != null && threadR != null
					&& threadS.getState().equals(Thread.State.TERMINATED)
					&& threadR.getState().equals(Thread.State.TERMINATED)
					&& this.status.getNumberOfSEntries() == 0
					&& this.status.getNumberOfREntries() == 0) {

				logger.fine(showStatusInfo());
				postProcess();
				logger.fine(showConfigurationInfo());
				this.isReady = true;
			} else {

				if (threadS == null
						|| (threadS != null && threadS.getState().equals(
								Thread.State.TERMINATED))) {
					threadS = new Thread() {
						@Override
						public void run() {
							while (status.getNumberOfSEntries() > 0) {
								processSEntries();
							}
						}
					};
					threadS.start();
				}

				if (threadR == null
						|| (threadR != null && threadR.getState().equals(
								Thread.State.TERMINATED))) {
					threadR = new Thread() {
						@Override
						public void run() {
							while (status.getNumberOfREntries() > 0) {
								processREntries();
							}
						}
					};
					threadR.start();
				}

				try {
					if (status.getNumberOfREntries() < status
							.getNumberOfSEntries()) {
						threadR.join();
					} else {
						threadS.join();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (this.loggingCount < 1) {
					this.loggingCount = loggingFrequency;
					logger.fine(showStatusInfo());
				}
			}
		}
		return !this.isReady;
	}

	/**
	 * Processes the nominals after the execution of the classification
	 * algorithm. It requires a hierarchical graph to get the descendants.
	 * 
	 * @param hierarchicalGraph
	 *            the hierarchical graph
	 */
	private void processNominals(IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> reachabilityCache = new HashMap<Integer, Set<Integer>>();
		Set<Integer> nominals = getEntityManager().getAuxiliaryNominals();
		for (Integer indiv : nominals) {
			Set<Integer> descendants = getDescendants(hierarchicalGraph, indiv);
			for (Integer c : descendants) {
				for (Integer d : descendants) {
					Collection<Integer> sC = getClassGraph().getSubsumers(c);
					Collection<Integer> sD = getClassGraph().getSubsumers(d);
					if (!(sD.containsAll(sC))) {
						if (computeReachability(c, reachabilityCache).contains(
								d)) {
							for (Integer elem : sD) {
								this.status.getClassGraph()
										.addAncestor(c, elem);
							}
						}
						for (Integer nominal : nominals) {
							if (computeReachability(nominal, reachabilityCache)
									.contains(d)) {
								for (Integer elem : sD) {
									this.status.getClassGraph().addAncestor(c,
											elem);
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean processREntries() {
		boolean ret = false;
		if (status.getNumberOfREntries() > 0) {
			REntry entry = status.removeNextREntry();
			ret = true;
			int property = entry.getProperty();
			int leftClass = entry.getLeftClass();
			int rightClass = entry.getRightClass();
			boolean applied = status.addToR(property, leftClass, rightClass);
			if (applied) {
				chainR.apply(status, property, leftClass, rightClass);
				loggingCount--;
				iteration++;
			}
		}
		return ret;
	}

	private boolean processSEntries() {
		boolean ret = false;
		if (status.getNumberOfSEntries() > 0) {
			SEntry entry = status.removeNextSEntry();
			ret = true;
			int subClass = entry.getSubClass();
			int superClass = entry.getSuperClass();
			boolean applied = status.addToS(subClass, superClass);
			if (applied) {
				chainS.apply(status, subClass, superClass);
				loggingCount--;
				iteration++;
			}
		}
		return ret;
	}

	private void removeAuxiliaryClassesExceptNominals() {
		Set<Integer> reqClasses = new HashSet<Integer>();
		for (Integer elem : getClassGraph().getElements()) {
			if (!getEntityManager().isAuxiliary(elem)) {
				reqClasses.add(elem);
			}
		}
		reqClasses.addAll(getEntityManager().getAuxiliaryNominals());
		this.status.getClassGraph().retainAll(reqClasses);
	}

	private void removeAuxiliaryNominals() {
		Set<Integer> reqClasses = new HashSet<Integer>();
		reqClasses.addAll(getClassGraph().getElements());
		reqClasses.removeAll(getEntityManager().getAuxiliaryNominals());
		this.status.getClassGraph().retainAll(reqClasses);
	}

	/**
	 * This method removes the auxiliary object properties that were not
	 * generated as inverse of another one.
	 */
	private void removeAuxiliaryObjectProperties() {
		Set<Integer> reqObjectProperties = new HashSet<Integer>();
		for (Integer elem : getObjectPropertyGraph().getElements()) {
			if (!getEntityManager().isAuxiliary(elem)) {
				reqObjectProperties.add(elem);
			}
		}
		this.status.getObjectPropertyGraph().retainAll(reqObjectProperties);
	}

	public String showConfigurationInfo() {
		StringBuffer sbuf = new StringBuffer();
		for (Map.Entry<String, String> entry : getConfigurationInfo()) {
			sbuf.append(entry.getKey());
			sbuf.append(" = ");
			sbuf.append(entry.getValue());
			sbuf.append("\n");
		}
		return sbuf.toString();
	}

	public String showStatusInfo() {
		StringBuffer sbuf = new StringBuffer();
		for (Map.Entry<String, String> entry : getStatusInfo()) {
			sbuf.append(entry.getKey());
			sbuf.append("=");
			sbuf.append(entry.getValue());
			sbuf.append(" ");
		}
		return sbuf.toString();
	}

}
