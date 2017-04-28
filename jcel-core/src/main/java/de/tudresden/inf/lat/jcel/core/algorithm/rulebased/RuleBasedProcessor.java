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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.io.IOException;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
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

	private class WorkerThreadR extends Thread {
		@Override
		public void run() {
			while (RuleBasedProcessor.this.status.getNumberOfREntries() > 0) {
				while (RuleBasedProcessor.this.status.getNumberOfREntries() > 0) {
					processREntries();
				}
				try {
					Thread.sleep(threadWaitingTime);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

	private class WorkerThreadS extends Thread {
		@Override
		public void run() {
			while (RuleBasedProcessor.this.status.getNumberOfSEntries() > 0) {
				while (RuleBasedProcessor.this.status.getNumberOfSEntries() > 0) {
					processSEntries();
				}
				try {
					Thread.sleep(threadWaitingTime);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

	private static final Logger logger = Logger.getLogger(RuleBasedProcessor.class.getName());

	private static final long loggingFrequency = 0x1000000;
	private static final long threadWaitingTime = 0x20;
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
	private final boolean multiThreadedMode = false;
	private IntegerHierarchicalGraph objectPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> sameIndividualMap = null;
	private ClassifierStatusImpl status = null;
	private WorkerThreadR threadR1 = null;
	private WorkerThreadR threadR2 = null;
	private WorkerThreadS threadS1 = null;
	private WorkerThreadS threadS2 = null;

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
	public RuleBasedProcessor(Set<Integer> originalObjectProperties, Set<Integer> originalClasses,
			Set<NormalizedIntegerAxiom> normalizedAxiomSet, OntologyExpressivity expressivity,
			NormalizedIntegerAxiomFactory factory, IntegerEntityManager entityManager) {
		Objects.requireNonNull(originalObjectProperties);
		Objects.requireNonNull(originalClasses);
		Objects.requireNonNull(normalizedAxiomSet);
		Objects.requireNonNull(expressivity);
		Objects.requireNonNull(factory);
		Objects.requireNonNull(entityManager);
		this.factory = factory;
		this.entityManager = entityManager;

		CompletionRuleChainSelector selector = new CompletionRuleChainSelector(expressivity);
		selector.activateProfiler();
		this.chainR = selector.getRChain();
		this.chainS = selector.getSChain();

		preProcess(createExtendedOntology(originalObjectProperties, originalClasses, normalizedAxiomSet));
	}

	public void addAxioms(Set<NormalizedIntegerAxiom> normalizedAxiomSet) {
		Objects.requireNonNull(normalizedAxiomSet);
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
	private Map<Integer, Set<Integer>> computeDirectTypes(IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> ret = new HashMap<>();
		Set<Integer> individuals = getEntityManager().getIndividuals();
		individuals.forEach(indiv -> {
			Set<Integer> subsumers = hierarchicalGraph.getParents(getEntityManager().getAuxiliaryNominal(indiv).get());
			subsumers.forEach(elem -> {
				if (getEntityManager().getAuxiliaryNominals().contains(elem)) {
					throw new IllegalStateException("An individual has another individual as direct subsumer.");
				}
			});
			ret.put(indiv, Collections.unmodifiableSet(subsumers));
		});
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
		Set<Integer> ret = new HashSet<>();
		Set<Integer> toVisit = new HashSet<>();
		toVisit.add(c);
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> newToVisit = new HashSet<>();
			this.status.getObjectPropertiesByFirst(elem).forEach(r -> {
				IntegerBinaryRelation relation = this.status.getRelationSet().get(r);
				newToVisit.addAll(relation.getByFirst(elem));
			});
			newToVisit.removeAll(ret);
			toVisit.addAll(newToVisit);
		}
		return ret;
	}

	private Set<Integer> computeReachability(Integer c, Map<Integer, Set<Integer>> reachableNodeCache) {
		Set<Integer> reachableNodes = reachableNodeCache.get(c);
		if (Objects.isNull(reachableNodes)) {
			reachableNodes = computeReachability(c);
			reachableNodeCache.put(c, reachableNodes);
		}
		return reachableNodes;
	}

	private Map<Integer, Set<Integer>> computeSameIndividualMap(IntegerHierarchicalGraph hierarchicalGraph) {
		Map<Integer, Set<Integer>> ret = new HashMap<>();
		Set<Integer> individuals = getEntityManager().getIndividuals();
		individuals.forEach(indiv -> {
			Set<Integer> equivalentClasses = hierarchicalGraph
					.getEquivalents(getEntityManager().getAuxiliaryNominal(indiv).get());
			Set<Integer> equivalents = new HashSet<>();
			equivalentClasses.forEach(elem -> {
				if (getEntityManager().getAuxiliaryNominals().contains(elem)) {
					equivalents.add(getEntityManager().getIndividual(elem).get());
				}
			});
			ret.put(indiv, Collections.unmodifiableSet(equivalents));
		});
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

	private ExtendedOntology createExtendedOntology(Set<Integer> originalObjectPropertySet,
			Set<Integer> originalClassSet, Set<NormalizedIntegerAxiom> axioms) {
		SubPropertyNormalizer subPropNormalizer = new SubPropertyNormalizer(getOntologyObjectFactory(),
				getEntityManager());
		Set<NormalizedIntegerAxiom> saturatedNormalizedAxiomSet = subPropNormalizer.apply(axioms);
		ExtendedOntology extendedOntology = new ExtendedOntologyImpl();
		extendedOntology.load(saturatedNormalizedAxiomSet);
		originalObjectPropertySet.forEach(elem -> extendedOntology.addObjectProperty(elem));
		originalClassSet.forEach(elem -> extendedOntology.addClass(elem));
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
		List<Map.Entry<String, String>> ret = new ArrayList<>();
		ret.add(createEntry("processor", getClass().getSimpleName()));
		ret.add(createEntry("iterations per log entry", "" + loggingFrequency));
		ret.add(createEntry("classes read (including TOP and BOTTOM classes)",
				"" + getEntityManager().getEntities(IntegerEntityType.CLASS, false).size()));
		ret.add(createEntry("object properties read (including TOP and BOTTOM object properties)",
				"" + getEntityManager().getEntities(IntegerEntityType.OBJECT_PROPERTY, false).size()));
		ret.add(createEntry("auxiliary classes created (including nominals)",
				"" + getEntityManager().getEntities(IntegerEntityType.CLASS, true).size()));
		ret.add(createEntry("auxiliary classes created for nominals", "" + getEntityManager().getIndividuals().size()));
		ret.add(createEntry("auxiliary object properties created",
				"" + getEntityManager().getEntities(IntegerEntityType.OBJECT_PROPERTY, true).size()));
		ret.add(createEntry("chain S", this.chainS.toString()));
		ret.add(createEntry("chain R", this.chainR.toString()));
		return ret;
	}

	@Override
	public IntegerHierarchicalGraph getDataPropertyHierarchy() throws UnclassifiedOntologyException {
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
	private Set<Integer> getDescendants(IntegerHierarchicalGraph hierarchicalGraph, Integer vertex) {
		Set<Integer> visited = new HashSet<>();
		Set<Integer> queue = new HashSet<>();
		queue.add(vertex);
		while (!queue.isEmpty()) {
			Integer elem = queue.iterator().next();
			queue.remove(elem);
			visited.add(elem);
			Set<Integer> children = new HashSet<>();
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

	protected IntegerEntityManager getEntityManager() {
		return this.entityManager;
	}

	protected IntegerSubsumerGraph getObjectPropertyGraph() {
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
		Objects.requireNonNull(relationId);
		return this.status.getRelationSet().get(relationId);
	}

	/**
	 * Returns a set containing all relation ids.
	 * 
	 * @return the set of all relation ids
	 */
	protected Set<Integer> getRelationIdSet() {
		return Collections.unmodifiableSet(this.status.getRelationSet().getElements());
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
		List<Map.Entry<String, String>> ret = new ArrayList<>();
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
		this.objectPropertyHierarchy = new IntegerHierarchicalGraphImpl(getObjectPropertyGraph());

		removeAuxiliaryClassesExceptNominals();
		IntegerHierarchicalGraph hierarchicalGraph = new IntegerHierarchicalGraphImpl(this.status.getClassGraph());
		processNominals(hierarchicalGraph);
		this.directTypes = computeDirectTypes(hierarchicalGraph);
		this.sameIndividualMap = computeSameIndividualMap(hierarchicalGraph);

		removeAuxiliaryNominals();
		this.classHierarchy = new IntegerHierarchicalGraphImpl(this.status.getClassGraph());
	};

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
		this.dataPropertyHierarchy = new IntegerHierarchicalGraphImpl(new IntegerSubsumerGraphImpl(
				IntegerEntityManager.bottomDataPropertyId, IntegerEntityManager.topDataPropertyId));
		Set<Integer> classNameSet = new HashSet<>();
		classNameSet.addAll(this.status.getExtendedOntology().getClassSet());
		classNameSet.forEach(className -> {
			this.status.addNewSEntry(className, className);
			this.status.addNewSEntry(className, topClassId);
		});

		logger.fine("processor configured.");
		logger.fine(showConfigurationInfo());

		int numberOfCores = Runtime.getRuntime().availableProcessors();
		logger.fine("number of cores : " + numberOfCores);

		// uncomment the following two lines to allow multithreaded mode
		// int suggestedNumberOfThreads = (numberOfCores / 2) - 1;
		// this.multiThreadedMode = suggestedNumberOfThreads >= 4;

		if (this.multiThreadedMode) {
			logger.fine("running processor on multiple threads.");
		} else {
			logger.fine("running processor on a single thread.");
		}
	}

	@Override
	public boolean process() {
		boolean ret = false;
		if (this.multiThreadedMode) {
			ret = processMultiThreaded();
		} else {
			ret = processSingleThreaded();
		}
		if (ret) {
			if (this.loggingCount < 1) {
				this.loggingCount = loggingFrequency;
				logger.fine(showStatusInfo());
			}
		}
		return ret;
	};

	private boolean processMultiThreaded() {
		if (!this.isReady) {
			if (Objects.nonNull(this.threadS1) && Objects.nonNull(this.threadS2) && Objects.nonNull(this.threadR1)
					&& Objects.nonNull(this.threadR2) && this.threadS1.getState().equals(Thread.State.TERMINATED)
					&& this.threadS2.getState().equals(Thread.State.TERMINATED)
					&& this.threadR1.getState().equals(Thread.State.TERMINATED)
					&& this.threadR2.getState().equals(Thread.State.TERMINATED)
					&& (this.status.getNumberOfSEntries() == 0) && (this.status.getNumberOfREntries() == 0)) {

				logger.fine(showStatusInfo());
				postProcess();
				logger.fine(showConfigurationInfo());
				this.isReady = true;
			} else {

				if ((Objects.isNull(this.threadS1)) || (Objects.nonNull(this.threadS1)
						&& this.threadS1.getState().equals(Thread.State.TERMINATED))) {
					this.threadS1 = new WorkerThreadS();
					this.threadS1.start();
					logger.finest("starting new thread S-1 ...");
				}

				if ((Objects.isNull(this.threadS2)) || (Objects.nonNull(this.threadS2)
						&& this.threadS2.getState().equals(Thread.State.TERMINATED))) {
					this.threadS2 = new WorkerThreadS();
					this.threadS2.start();
					logger.finest("starting new thread S-2 ...");
				}

				if ((Objects.isNull(this.threadR1)) || (Objects.nonNull(this.threadR1)
						&& this.threadR1.getState().equals(Thread.State.TERMINATED))) {
					this.threadR1 = new WorkerThreadR();
					this.threadR1.start();
					logger.finest("starting new thread R-1 ...");
				}

				if ((Objects.isNull(this.threadR2)) || (Objects.nonNull(this.threadR2)
						&& this.threadR2.getState().equals(Thread.State.TERMINATED))) {
					this.threadR2 = new WorkerThreadR();
					this.threadR2.start();
					logger.finest("starting new thread R-2 ...");
				}

				try {
					if (this.status.getNumberOfREntries() < this.status.getNumberOfSEntries()) {
						if ((this.iteration % 2) == 0) {
							this.threadR1.join();
						} else {
							this.threadR2.join();
						}
					} else {
						if ((this.iteration % 2) == 0) {
							this.threadS1.join();
						} else {
							this.threadS2.join();
						}
					}
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
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
		Map<Integer, Set<Integer>> reachabilityCache = new HashMap<>();
		Set<Integer> nominals = getEntityManager().getAuxiliaryNominals();
		nominals.forEach(indiv -> {
			Set<Integer> descendants = getDescendants(hierarchicalGraph, indiv);
			descendants.forEach(c -> {
				descendants.forEach(d -> {
					Collection<Integer> sC = getClassGraph().getSubsumers(c);
					Collection<Integer> sD = getClassGraph().getSubsumers(d);
					if (!(sD.containsAll(sC))) {
						if (computeReachability(c, reachabilityCache).contains(d)) {
							sD.forEach(elem -> this.status.getClassGraph().addAncestor(c, elem));
						}
						nominals.forEach(nominal -> {
							if (computeReachability(nominal, reachabilityCache).contains(d)) {
								sD.forEach(elem -> this.status.getClassGraph().addAncestor(c, elem));
							}
						});
					}
				});
			});
		});
	}

	private boolean processREntries() {
		boolean ret = false;
		REntry entry = null;
		try {
			entry = this.status.removeNextREntry();
		} catch (NoSuchElementException e) {
		}
		if (Objects.nonNull(entry)) {
			ret = true;
			int property = entry.getProperty();
			int leftClass = entry.getLeftClass();
			int rightClass = entry.getRightClass();
			boolean applied = this.status.addToR(property, leftClass, rightClass);
			if (applied) {
				this.chainR.apply(this.status, property, leftClass, rightClass);
				this.loggingCount--;
				this.iteration++;
			}
		}
		return ret;
	}

	private boolean processSEntries() {
		boolean ret = false;
		SEntry entry = null;
		try {
			entry = this.status.removeNextSEntry();
		} catch (NoSuchElementException e) {
		}
		if (Objects.nonNull(entry)) {
			ret = true;
			int subClass = entry.getSubClass();
			int superClass = entry.getSuperClass();
			boolean applied = this.status.addToS(subClass, superClass);
			if (applied) {
				this.chainS.apply(this.status, subClass, superClass);
				this.loggingCount--;
				this.iteration++;
			}
		}
		return ret;
	}

	private boolean processSingleThreaded() {
		if (!this.isReady) {
			if ((this.status.getNumberOfSEntries() == 0) && (this.status.getNumberOfREntries() == 0)) {
				logger.fine(showStatusInfo());
				postProcess();
				logger.fine(showConfigurationInfo());
				this.isReady = true;
			} else {
				if (this.status.getNumberOfSEntries() > this.status.getNumberOfREntries()) {
					processSEntries();
				} else {
					processREntries();
				}
			}
		}
		return !this.isReady;
	}

	private void removeAuxiliaryClassesExceptNominals() {
		Set<Integer> reqClasses = new HashSet<>();
		getClassGraph().getElements().forEach(elem -> {
			if (!getEntityManager().isAuxiliary(elem)) {
				reqClasses.add(elem);
			}
		});
		reqClasses.addAll(getEntityManager().getAuxiliaryNominals());
		this.status.getClassGraph().retainAll(reqClasses);
	}

	private void removeAuxiliaryNominals() {
		Set<Integer> reqClasses = new HashSet<>();
		reqClasses.addAll(getClassGraph().getElements());
		reqClasses.removeAll(getEntityManager().getAuxiliaryNominals());
		this.status.getClassGraph().retainAll(reqClasses);
	}

	/**
	 * This method removes the auxiliary object properties that were not
	 * generated as inverse of another one.
	 */
	private void removeAuxiliaryObjectProperties() {
		Set<Integer> reqObjectProperties = new HashSet<>();
		getObjectPropertyGraph().getElements().forEach(elem -> {
			if (!getEntityManager().isAuxiliary(elem)) {
				reqObjectProperties.add(elem);
			}
		});
		this.status.getObjectPropertyGraph().retainAll(reqObjectProperties);
	}

	public String showConfigurationInfo() {
		StringBuffer sbuf = new StringBuffer();
		getConfigurationInfo().forEach(entry -> {
			sbuf.append(entry.getKey());
			sbuf.append(" = ");
			sbuf.append(entry.getValue());
			sbuf.append("\n");
		});
		return sbuf.toString();
	}

	public String showStatusInfo() {
		StringBuffer sbuf = new StringBuffer();
		getStatusInfo().forEach(entry -> {
			sbuf.append(entry.getKey());
			sbuf.append("=");
			sbuf.append(entry.getValue());
			sbuf.append(" ");
		});
		return sbuf.toString();
	}

	public void outputSetS(Writer writer) throws IOException {
		this.status.outputSetS(writer);
	}

	public void outputSetR(Writer writer) throws IOException {
		this.status.outputSetR(writer);
	}

}
