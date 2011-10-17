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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.common.UnclassifiedOntologyException;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.IntegerBinaryRelation;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IdGenerator;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDatatype;

/**
 * An object of this class is an implementation of a classification algorithm.
 * 
 * @author Julian Mendez
 */
public class RuleBasedProcessor implements Processor {

	private static final Integer classTopElement = IntegerDatatype.classTopElement;
	private static final Logger logger = Logger
			.getLogger(RuleBasedProcessor.class.getName());
	private static final long loggingFrequency = 0x100000;

	private ComplexIntegerAxiomFactory axiomFactory = null;
	private RChain chainR = null;
	private SChain chainS = null;
	private IntegerHierarchicalGraph classHierarchy = null;
	private IntegerHierarchicalGraph dataPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> directTypes = null;
	private boolean isReady = false;
	private long iteration = 0;
	private long loggingCount = loggingFrequency;
	private IntegerHierarchicalGraph objectPropertyHierarchy = null;
	private Set<ComplexIntegerAxiom> originalAxiomSet = null;
	private Map<Integer, Set<Integer>> sameIndividualMap = null;
	private Set<REntry> setQsubR = new TreeSet<REntry>();
	private Set<SEntry> setQsubS = new TreeSet<SEntry>();
	private ClassifierStatusImpl status = null;

	/**
	 * Constructs a new rule-based processor.
	 * 
	 * @param axioms
	 *            set of complex axioms to be normalized and classified
	 */
	public RuleBasedProcessor(Set<ComplexIntegerAxiom> axioms,
			ComplexIntegerAxiomFactory factory) {
		if (axioms == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.axiomFactory = factory;
		preProcess(axioms);
	}

	private void addSuggestedChanges(List<XEntry> changes) {
		for (XEntry entry : changes) {
			if (entry.isSEntry()) {
				SEntry sEntry = (SEntry) entry;
				if (!this.status.getSubsumers(sEntry.getSubClass()).contains(
						sEntry.getSuperClass())) {
					this.setQsubS.add(sEntry);
				}
			} else if (entry.isREntry()) {
				REntry rEntry = (REntry) entry;
				if (!this.status.getSecondByFirst(rEntry.getProperty(),
						rEntry.getLeftClass()).contains(rEntry.getRightClass())) {
					this.setQsubR.add(rEntry);
				}
			}
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
		Set<Integer> individuals = getIdGenerator().getIndividuals();
		for (Integer indiv : individuals) {
			Set<Integer> subsumers = hierarchicalGraph
					.getParents(getIdGenerator().getAuxiliaryNominal(indiv));
			for (Integer elem : subsumers) {
				if (getIdGenerator().getAuxiliaryNominals().contains(elem)) {
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
		Set<Integer> individuals = getIdGenerator().getIndividuals();
		for (Integer indiv : individuals) {
			Set<Integer> equivalentClasses = hierarchicalGraph
					.getEquivalents(getIdGenerator().getAuxiliaryNominal(indiv));
			Set<Integer> equivalents = new HashSet<Integer>();
			for (Integer elem : equivalentClasses) {
				if (getIdGenerator().getAuxiliaryNominals().contains(elem)) {
					equivalents.add(getIdGenerator().getIndividual(elem));
				}
			}
			ret.put(indiv, Collections.unmodifiableSet(equivalents));
		}
		return ret;
	}

	@Override
	public Set<ComplexIntegerAxiom> getAxiomSet() {
		return this.originalAxiomSet;
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
	protected IdGenerator getIdGenerator() {
		return this.status.getIdGenerator();
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
			for (Integer r : this.status.getObjectPropertiesByFirst(elem)) {
				IntegerBinaryRelation relation = this.status.getRelationSet()
						.get(r);
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

	private void prepareSets() {
		this.setQsubS.clear();
		this.setQsubR.clear();
		Set<Integer> classNameSet = new HashSet<Integer>();
		classNameSet.addAll(this.status.getExtendedOntology().getClassSet());
		for (Integer className : classNameSet) {
			this.setQsubS.add(new SEntryImpl(className, className));
			this.setQsubS.add(new SEntryImpl(className, classTopElement));
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
	 * @param origAxiomSet
	 *            set of axioms, i.e. the ontology
	 */
	protected void preProcess(Set<ComplexIntegerAxiom> origAxiomSet) {
		if (origAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.originalAxiomSet = origAxiomSet;
		this.isReady = false;

		this.dataPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				new IntegerSubsumerGraphImpl(
						IntegerDatatype.dataPropertyBottomElement,
						IntegerDatatype.dataPropertyTopElement));

		logger.fine("using " + getClass().getSimpleName() + " ...");

		logger.fine("configuring processor ...");

		logger.fine("number of axioms : " + this.originalAxiomSet.size());

		logger.fine("preprocessing ontology ...");
		OntologyPreprocessor preprocessor = new OntologyPreprocessor(
				this.originalAxiomSet, this.axiomFactory);

		logger.fine("description logic family : "
				+ preprocessor.getExpressivity().toString() + " .");

		logger.fine("number of normalized axioms : "
				+ preprocessor.getExtendedOntology().getAxiomSet().size());

		preprocessor.activateProfiler();

		this.chainR = preprocessor.getRChain();
		this.chainS = preprocessor.getSChain();
		logger.fine("set of completion rules : \n" + this.chainS + "\n"
				+ this.chainR + "\n");

		logger.fine("classes read (including TOP and BOTTOM classes) : "
				+ (preprocessor.getIdGenerator().getFirstClassId()));
		logger.fine("object properties read (including TOP and BOTTOM object properties) : "
				+ (preprocessor.getIdGenerator().getFirstObjectPropertyId()));
		logger.fine("auxiliary classes created (including nominals) : "
				+ (preprocessor.getIdGenerator().getNextClassId() - preprocessor
						.getIdGenerator().getFirstClassId()));
		logger.fine("auxiliary classes created for nominals : "
				+ (preprocessor.getIdGenerator().getIndividuals().size()));
		logger.fine("auxiliary object properties created : "
				+ (preprocessor.getIdGenerator().getNextObjectPropertyId() - preprocessor
						.getIdGenerator().getFirstObjectPropertyId()));

		logger.fine("creating class graph and object property graph ...");

		this.status = new ClassifierStatusImpl(preprocessor.getIdGenerator(),
				preprocessor.getExtendedOntology());

		logger.finer("preparing queue ...");
		prepareSets();

		logger.fine("processor configured.");
	}

	@Override
	public boolean process() {
		if (!this.isReady) {
			if (this.setQsubS.isEmpty() && this.setQsubR.isEmpty()) {
				postProcess();
				logger.fine(showUsedRules());
				this.isReady = true;
			} else {
				if (this.setQsubS.size() > this.setQsubR.size()) {
					SEntry entry = this.setQsubS.iterator().next();
					this.setQsubS.remove(entry);
					this.status.addToS(entry);
					List<XEntry> changes = this.chainS
							.apply(this.status, entry);
					addSuggestedChanges(changes);
				} else {
					REntry entry = this.setQsubR.iterator().next();
					this.setQsubR.remove(entry);
					this.status.addToR(entry);
					List<XEntry> changes = this.chainR
							.apply(this.status, entry);
					addSuggestedChanges(changes);
				}
				this.loggingCount--;
				this.iteration++;
				if (this.loggingCount < 1) {
					this.loggingCount = loggingFrequency;
					logger.fine(showSetSizes());
					// logger.fine(showUsedRules());
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
		Set<Integer> nominals = getIdGenerator().getAuxiliaryNominals();
		for (Integer indiv : nominals) {
			Set<Integer> descendants = getDescendants(hierarchicalGraph, indiv);
			for (Integer c : descendants) {
				for (Integer d : descendants) {
					Collection<Integer> sC = getClassGraph().getSubsumers(c);
					Collection<Integer> sD = getClassGraph().getSubsumers(d);
					if (!(sD.containsAll(sC))) {
						if (isConnectedTo(c, d)) {
							for (Integer elem : sD) {
								this.status.getClassGraph()
										.addAncestor(c, elem);
							}
						}
						for (Integer nominal : nominals) {
							if (isConnectedTo(nominal, d)) {
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

	private void removeAuxiliaryClassesExceptNominals() {
		Set<Integer> reqClasses = new HashSet<Integer>();
		for (Integer elem : getClassGraph().getElements()) {
			if (elem < getIdGenerator().getFirstClassId()) {
				reqClasses.add(elem);
			}
		}
		reqClasses.addAll(getIdGenerator().getAuxiliaryNominals());
		this.status.getClassGraph().retainAll(reqClasses);
	}

	private void removeAuxiliaryNominals() {
		Set<Integer> reqClasses = new HashSet<Integer>();
		reqClasses.addAll(getClassGraph().getElements());
		reqClasses.removeAll(getIdGenerator().getAuxiliaryNominals());
		this.status.getClassGraph().retainAll(reqClasses);
	}

	private void removeAuxiliaryObjectProperties() {
		Set<Integer> reqObjectProperties = new HashSet<Integer>();
		for (Integer elem : getObjectPropertyGraph().getElements()) {
			if (elem < getIdGenerator().getFirstObjectPropertyId()) {
				reqObjectProperties.add(elem);
			}
		}
		this.status.getObjectPropertyGraph().retainAll(reqObjectProperties);
	}

	private String showSetSizes() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[ iteration=");
		sbuf.append(this.iteration);
		sbuf.append(" sizes of Q_S=");
		sbuf.append(this.setQsubS.size());
		sbuf.append(" Q_R=");
		sbuf.append(this.setQsubR.size());
		sbuf.append(" S=");
		sbuf.append(this.status.getDeepSizeOfS());
		sbuf.append(" R=");
		sbuf.append(this.status.getDeepSizeOfR());
		sbuf.append(" V=");
		sbuf.append(this.status.getSizeOfV());
		sbuf.append(" subV=");
		sbuf.append(this.status.getDeepSizeOfV());
		sbuf.append(" ]");
		return sbuf.toString();
	}

	private String showUsedRules() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n");
		sbuf.append(this.chainS.toString());
		sbuf.append("\n");
		sbuf.append(this.chainR.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}

}
