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

package de.tudresden.inf.lat.jcel.core.classifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;
import de.tudresden.inf.lat.jcel.core.graph.IntegerBinaryRelation;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.core.normalization.IdGenerator;
import de.tudresden.inf.lat.jcel.core.normalization.OntologyNormalizer;

/**
 * Classifies an ontology using the CEL algorithm. The rules are:
 * 
 * <ul>
 * <li>CR1 : <b>if</b> A<sub>1</sub>, &hellip; , A<sub>n</sub> &isin; S(X) <b>and</b>
 * A<sub>1</sub> &cap; &hellip; &cap; A<sub>n</sub> &sube; B &isin; O <b>and</b> B
 * &notin; S(X) <b>then</b> S(X) := S(X) &cup; {B}</li>
 * <li>CR2 : <b>if</b> A &isin; S(X) <b>and</b> A &sube; &exist; r . B &isin; O
 * <b>and</b> (X,B) &notin; R(r) <b>then</b> R(r) := R(r) &cup;{(X,B)}</li>
 * <li>CR3 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> A &isin; S(Y) <b>and</b>
 * &exist; r . A &sube; B &isin; O <b>and</b> B &notin; S(X) <b>then</b> S(X) :=
 * S(X) &cup; {B}</li>
 * <li>CR4 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> &#8869; &isin; S(Y)
 * <b>and</b> &#8869; &notin; S(X) <b>then</b> S(X) := S(X) &cup; {&#8869;}</li>
 * <li>CR5 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> r &sube; s &isin; O
 * <b>and</b> (X,Y) &notin; R(s) <b>then</b> R(s) := R(s) &cup; {(X,Y)}</li>
 * <li>CR6 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> (Y,Z) &isin; R(s) <b>and</b>
 * r &#8728; s &sube; t &isin; O <b>and</b> (X,Z) &notin; R(t) <b>then</b> R(t)
 * := R(t) &cup; {(X,Z)}</li>
 * </ul>
 * 
 * (Squared symbols were replaced by rounded ones)
 * 
 * @author Julian Mendez
 */
public class Processor {

	private static final Integer classBottomElement = IntegerDescription.NOTHING;
	private static final Integer classTopElement = IntegerDescription.THING;
	private static final Logger logger = Logger.getLogger(Processor.class
			.getName());
	protected static final Integer loggingTimeGap = 1000;

	private IntegerSubsumerGraph classGraph = null;
	private ExtendedOntology extendedOntology = null;
	private IdGenerator idGenerator = null;
	private IntegerSubsumerGraph propertyGraph = null;
	private Map<Integer, List<ExtensionEntry>> queueMap = null;
	private Map<Integer, IntegerBinaryRelation> setR = null;
	private Map<Integer, Set<Integer>> transitiveSubsumed = null;

	public Processor() {
	}

	protected void addNewEntries(Integer cA, Set<ExtensionEntry> classEntries) {
		List<ExtensionEntry> queue = this.queueMap.get(cA);
		for (ExtensionEntry entry : classEntries) {
			queue.add(entry);
		}
	}

	/**
	 * The configuration follows the following steps:
	 * <ul>
	 * <li>normalizes the ontology creating auxiliary entities</li>
	 * <li>creates an extended ontonlogy based on the normalized ontology</li>
	 * <li>adds the concepts</li>
	 * <li>creates the role hierarchy</li>
	 * <li>prepares all the queues to run the algorithm</li>
	 * </ul>
	 * 
	 * @param axiomSet
	 *            set of axioms, i.e. the ontology
	 * @param propertySize
	 *            number of consecutive properties used in the axioms, starting
	 *            from 0
	 * @param classSize
	 *            number of consecutive classes used in the axioms, starting
	 *            from 0
	 */
	public void configure(Set<IntegerAxiom> axiomSet, Integer propertySize,
			Integer classSize) {

		this.idGenerator = new IdGenerator(propertySize, classSize);
		Integer propertyBottomElement = this.idGenerator
				.createNewPropertyId();
		Integer propertyTopElement = this.idGenerator.createNewPropertyId();

		logger.fine("normalizing ontology ...");

		Set<NormalizedIntegerAxiom> ontology = null;
		OntologyNormalizer normalizer = new OntologyNormalizer(idGenerator);
		ontology = new HashSet<NormalizedIntegerAxiom>();
		ontology.addAll(normalizer.normalize(axiomSet));

		logger.fine("auxiliary classes created : "
				+ (this.idGenerator.getNextClassId() - this.idGenerator
						.getFirstClassId()));
		logger.fine("auxiliary properties created : "
				+ (this.idGenerator.getNextPropertyId() - this.idGenerator
						.getFirstPropertyId()));

		Integer classSetSize = this.idGenerator.getNextClassId();
		Integer propertySetSize = this.idGenerator.getNextPropertyId();

		logger.fine("creating extended ontology ...");
		this.extendedOntology = new ExtendedOntology();
		this.extendedOntology.load(ontology);

		logger.fine("initializing class graph ...");
		this.classGraph = initializeClassGraph(classSetSize,
				classBottomElement, classTopElement);

		this.setR = new HashMap<Integer, IntegerBinaryRelation>();
		this.propertyGraph = new IntegerSubsumerGraphImpl(
				propertyBottomElement, propertyTopElement);
		this.queueMap = new HashMap<Integer, List<ExtensionEntry>>();

		logger.fine("creating property hierarchy ...");
		createRoleHierarchy(ontology, propertySetSize);

		logger.fine("loading queues ...");
		loadAllQueues(this.extendedOntology);

		logger.fine("processor configured.");
	}

	protected void createRoleHierarchy(Set<NormalizedIntegerAxiom> axiomSet,
			Integer propertySetSize) {
		Integer propertyTopElement = this.propertyGraph.getTopElement();
		for (int index = 0; index < propertySetSize; index++) {
			this.setR.put(index, new IntegerBinaryRelation());
			this.propertyGraph.addAncestor(index, propertyTopElement);
		}
		for (IntegerAxiom axiom : axiomSet) {
			if (axiom instanceof RI2Axiom) {
				RI2Axiom current = (RI2Axiom) axiom;
				this.propertyGraph.addAncestor(current.getSubProperty(),
						current.getSuperProperty());
			}
		}
		makeTransitiveClosure(this.propertyGraph);
	}

	public IntegerSubsumerGraph getClassGraph() {
		return this.classGraph;
	}

	public ExtendedOntology getExtendedOntology() {
		return this.extendedOntology;
	}

	protected IntegerBinaryRelation getInternalRelation(Integer relationId) {
		IntegerBinaryRelation ret = getRelationPairMap().get(relationId);
		if (ret == null) {
			ret = new IntegerBinaryRelation();
			getRelationPairMap().put(relationId, ret);
		}
		return ret;
	}

	public IntegerSubsumerGraph getPropertyGraph() {
		return this.propertyGraph;
	}

	public Map<Integer, IntegerBinaryRelation> getRelationPairMap() {
		return this.setR;
	}

	public Set<Integer> getRoleNameSet() {
		return this.propertyGraph.getElements();
	}

	protected IntegerSubsumerGraph initializeClassGraph(Integer classIdSetSize,
			Integer nothing, Integer thing) {

		IntegerSubsumerGraph set = new IntegerSubsumerGraphImpl(nothing, thing);
		for (int index = 0; index < classIdSetSize; index++) {
			set.addAncestor(index, thing);
		}
		return set;
	}

	protected boolean isReflexiveTransitiveSubsumed(Integer leftRoleName,
			Integer rightRoleName) {
		return this.propertyGraph != null
				&& this.propertyGraph.getSubsumers(leftRoleName) != null
				&& this.propertyGraph.getSubsumers(leftRoleName).contains(
						rightRoleName);
	}

	protected void loadAllQueues(ExtendedOntology ontology) {
		Set<Integer> conceptNameSet = new HashSet<Integer>();
		conceptNameSet.addAll(ontology.getClassSet());
		for (Integer conceptName : conceptNameSet) {
			List<ExtensionEntry> queue = new ArrayList<ExtensionEntry>();
			queue.addAll(ontology.getClassEntries(conceptName));
			queue.addAll(ontology.getClassEntries(classTopElement));
			this.queueMap.put(conceptName, queue);
		}
		{
			this.transitiveSubsumed = new HashMap<Integer, Set<Integer>>();
			for (Integer r : getRoleNameSet()) {
				Set<Integer> related = new HashSet<Integer>();
				for (Integer s : getRoleNameSet()) {
					if (isReflexiveTransitiveSubsumed(r, s)) {
						related.add(s);
					}
				}
				this.transitiveSubsumed.put(r, related);
			}
		}
	}

	protected void makeTransitiveClosure(IntegerSubsumerGraph graph) {
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;
			for (Integer elem : graph.getElements()) {
				Set<Integer> subsumerSet = graph.getSubsumers(elem);
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

	public void process(Integer cA, ExtensionEntry eX)
			throws ProcessingException {
		if (eX.isImplication()) {
			processImplication(cA, eX.asImplication());
		} else if (eX.isExistential()) {
			processExistential(cA, eX.asExistential());
		} else {
			throw new ProcessingException(
					"Internal error: CelEntry was not recognized " + eX);
		}
	}

	public boolean processAllQueues() throws ProcessingException {
		boolean ret = false;
		int queueit = 0;
		Date last = new Date();
		Date current = new Date();
		for (Integer conceptName : this.classGraph.getElements()) {
			boolean result = processQueue(conceptName);
			ret = ret || result;
			current = new Date();
			long difference = current.getTime() - last.getTime();
			if (difference > loggingTimeGap) {
				logger.fine(":: [" + difference + "] " + queueit + " "
						+ conceptName);
			}
			last = current;
			queueit++;
		}
		return ret;
	}

	protected void processBottom(Integer conceptName) {
		this.classGraph.addAncestor(conceptName, classBottomElement);

		for (Iterator<Integer> it = this.setR.keySet().iterator(); it.hasNext();) {
			Integer relation = it.next();
			for (Integer firstComponent : getInternalRelation(relation)
					.getBySecond(conceptName)) {
				Set<Integer> subsumers = this.classGraph
						.getSubsumers(firstComponent);
				if (!subsumers.contains(classBottomElement)) {
					processBottom(firstComponent);
				}
			}
		}
	}

	public void processExistential(Integer cA, ExistentialEntry eX)
			throws ProcessingException {
		Integer r = eX.getPropertyId();
		Integer cB = eX.getClassId();
		Integer bottom = classBottomElement;

		if (!getInternalRelation(r).contains(cA, cB)) {

			if (this.classGraph.getSubsumers(cB).contains(bottom)
					&& !this.classGraph.getSubsumers(cA).contains(bottom)) {

				processBottom(cA);

			}

			processNewEdge(cA, r, cB);
		}
	}

	public void processImplication(Integer cA, ImplicationEntry eX) {

		Set<Integer> vecB = eX.getOperands();
		Integer cB = eX.getSuperClass();
		Set<Integer> sSofA = this.classGraph.getSubsumers(cA);
		Integer bottom = classBottomElement;

		if (sSofA.containsAll(vecB) && !sSofA.contains(cB)) {

			if (cB.equals(bottom)) {

				processBottom(cA);

			} else {

				this.classGraph.addAncestor(cA, cB);

				addNewEntries(cA, getExtendedOntology().getClassEntries(cB));

				for (Integer r : getRoleNameSet()) {

					Set<Integer> conceptSet = getInternalRelation(r)
							.getBySecond(cA);
					for (Integer cAprime : conceptSet) {

						addNewEntries(cAprime, getExtendedOntology()
								.getExistentialEntries(r, cB));
					}
				}
			}
		}
	}

	public void processNewEdge(Integer cA, Integer r, Integer cB)
			throws ProcessingException {

		for (Integer s : this.transitiveSubsumed.get(r)) {

			getInternalRelation(s).add(cA, cB);

			for (Integer cBprime : this.classGraph.getSubsumers(cB)) {
				addNewEntries(cA, getExtendedOntology().getExistentialEntries(
						s, cBprime));
			}

			for (RI3Axiom axiom : getExtendedOntology()
					.getSubPropertyAxiomSetByRight(s)) {

				Integer t = axiom.getLeftSubProperty();
				Integer u = axiom.getSuperProperty();

				Set<Integer> conceptSet = getInternalRelation(t)
						.getBySecond(cA);

				for (Integer cAprime : conceptSet) {

					if (!getInternalRelation(u).contains(cAprime, cB)) {
						processNewEdge(cAprime, u, cB);
					}
				}
			}

			for (RI3Axiom axiom : getExtendedOntology()
					.getSubPropertyAxiomSetByLeft(s)) {

				Integer t = axiom.getRightSubProperty();
				Integer u = axiom.getSuperProperty();

				Set<Integer> conceptSet = getInternalRelation(t).getByFirst(cB);

				for (Integer cBprime : conceptSet) {

					if (!getInternalRelation(u).contains(cA, cBprime)) {
						processNewEdge(cA, u, cBprime);
					}
				}
			}
		}
	}

	protected boolean processQueue(Integer conceptName)
			throws ProcessingException {
		boolean ret = false;
		List<ExtensionEntry> queue = this.queueMap.get(conceptName);
		if (queue != null && queue.size() > 0) {
			this.queueMap.put(conceptName, new ArrayList<ExtensionEntry>());
			ret = true;
			for (ExtensionEntry entry : queue) {
				process(conceptName, entry);
			}
		}
		return ret;
	}

	/**
	 * Removes the auxiliary classes and properties, but does not remove the
	 * property bottom and the property top
	 */
	public void removeAuxEntities() {
		this.propertyGraph.removeElem(idGenerator.getFirstPropertyId() + 2);
		this.classGraph.removeElem(idGenerator.getFirstClassId());
	}
}
