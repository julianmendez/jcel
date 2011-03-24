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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.core.graph.IntegerBinaryRelation;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.core.normalization.IdGenerator;
import de.tudresden.inf.lat.jcel.core.normalization.OntologyNormalizer;

/**
 * Classifies an ontology using the CEL algorithm. The rules are:
 * 
 * <ul>
 * <li>CR1 : <b>if</b> A<sub>1</sub>, &hellip; , A<sub>n</sub> &isin; S(X)
 * <b>and</b> A<sub>1</sub> &cap; &hellip; &cap; A<sub>n</sub> &sube; B &isin; O
 * <b>and</b> B &notin; S(X) <b>then</b> S(X) := S(X) &cup; {B}</li>
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
public class DefaultProcessor implements Processor {

	private static final Integer classBottomElement = IntegerDatatype.classBottomElement;
	private static final Integer classTopElement = IntegerDatatype.classTopElement;
	private static final Logger logger = Logger
			.getLogger(DefaultProcessor.class.getName());
	private static final Integer propertyBottomElement = IntegerDatatype.propertyBottomElement;
	private static final Integer propertyTopElement = IntegerDatatype.propertyTopElement;
	private IntegerSubsumerGraph classGraph = null;
	private IntegerHierarchicalGraph classHierarchy = null;
	private IntegerBinaryRelation emptyRelation = new IntegerBinaryRelation();
	private ExtendedOntology extendedOntology = null;
	private IdGenerator idGenerator = null;
	private boolean isReady = false;
	private IntegerSubsumerGraph objectPropertyGraph = null;
	private IntegerHierarchicalGraph objectPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> propertyUsedByClass = null;
	private Deque<ExtensionEntry> queueEntries = new ArrayDeque<ExtensionEntry>();
	private Deque<Integer> queueKeys = new ArrayDeque<Integer>();
	private Map<Integer, IntegerBinaryRelation> setR = null;
	private Map<Integer, Set<Integer>> transitiveSubsumed = null;

	public DefaultProcessor(Set<IntegerAxiom> axioms) {
		preProcess(axioms);
	}

	protected void addToQueue(Integer className,
			Collection<ExtensionEntry> entrySet) {
		for (ExtensionEntry entry : entrySet) {
			this.queueKeys.push(className);
			this.queueEntries.push(entry);
		}
	}

	protected IntegerSubsumerGraph createClassGraph(
			Set<Integer> originalClassSet, Set<NormalizedIntegerAxiom> axiomSet) {

		Set<Integer> classIdSet = new HashSet<Integer>();
		classIdSet.addAll(originalClassSet);
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			classIdSet.addAll(axiom.getClassesInSignature());
		}
		IntegerSubsumerGraph ret = new IntegerSubsumerGraphImpl(
				classBottomElement, classTopElement);
		for (Integer index : classIdSet) {
			ret.addAncestor(index, classTopElement);
		}
		ret.addAncestor(classTopElement, classTopElement);
		return ret;
	}

	protected IntegerSubsumerGraph createObjectPropertyGraph(
			Set<Integer> originalPropertySet,
			Set<NormalizedIntegerAxiom> axiomSet) {
		IntegerSubsumerGraph ret = new IntegerSubsumerGraphImpl(
				propertyBottomElement, propertyTopElement);
		Set<Integer> propertyIdSet = new HashSet<Integer>();
		propertyIdSet.addAll(originalPropertySet);
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			propertyIdSet.addAll(axiom.getObjectPropertiesInSignature());
		}
		for (Integer index : propertyIdSet) {
			ret.addAncestor(index, propertyTopElement);
		}
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			if (axiom instanceof RI2Axiom) {
				RI2Axiom current = (RI2Axiom) axiom;
				ret.addAncestor(current.getSubProperty(), current
						.getSuperProperty());
			}
		}
		makeTransitiveClosure(ret);
		return ret;
	}

	protected Map<Integer, Set<Integer>> createPropertyUseMap() {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (Integer cA : getClassGraph().getElements()) {
			Set<Integer> propertySet = new HashSet<Integer>();
			for (Integer r : getObjectPropertyGraph().getElements()) {
				if (!(getInternalRelation(r).getBySecond(cA).isEmpty())) {
					propertySet.add(r);
				}
			}
			ret.put(cA, propertySet);
		}
		return ret;
	}

	protected Map<Integer, IntegerBinaryRelation> createSetR(
			Collection<Integer> collection) {
		Map<Integer, IntegerBinaryRelation> ret = new HashMap<Integer, IntegerBinaryRelation>();
		for (Integer index : collection) {
			ret.put(index, new IntegerBinaryRelation());
		}
		return ret;
	}

	protected Map<Integer, Set<Integer>> createTransitiveSubsumed() {
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

	private IntegerSubsumerGraph getClassGraph() {
		return this.classGraph;
	}

	/**
	 * Returns the class hierarchy after the classification is finished.
	 * 
	 * @return the class hierarchy after the classification is finished, or
	 *         <code>null</code> otherwise.
	 */
	public IntegerHierarchicalGraph getClassHierarchy() {
		return this.classHierarchy;
	}

	protected ExtendedOntology getExtendedOntology() {
		return this.extendedOntology;
	}

	protected IntegerBinaryRelation getInternalRelation(Integer relationId) {
		IntegerBinaryRelation ret = getRelationPairMap().get(relationId);
		if (ret == null) {
			ret = this.emptyRelation;
		}
		return ret;
	}

	private Integer getMaximum(Set<Integer> set) {
		Integer ret = null;
		if (!set.isEmpty()) {
			ret = set.iterator().next();
		}
		for (Integer elem : set) {
			if (elem > ret) {
				ret = elem;
			}
		}
		return ret;
	}

	private IntegerSubsumerGraph getObjectPropertyGraph() {
		return this.objectPropertyGraph;
	}

	@Override
	public IntegerHierarchicalGraph getObjectPropertyHierarchy() {
		return this.objectPropertyHierarchy;
	}

	protected Set<Integer> getPropertyUsedByClass(Integer cA) {
		return this.propertyUsedByClass.get(cA);
	}

	protected Map<Integer, IntegerBinaryRelation> getRelationPairMap() {
		return this.setR;
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}

	protected boolean isReflexiveTransitiveSubsumed(Integer leftPropertyName,
			Integer rightPropertyName) {
		return this.objectPropertyGraph != null
				&& this.objectPropertyGraph.getSubsumers(leftPropertyName) != null
				&& this.objectPropertyGraph.getSubsumers(leftPropertyName)
						.contains(rightPropertyName);
	}

	protected void makeTransitiveClosure(IntegerSubsumerGraph graph) {
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

	protected void postProcess() {
		this.classGraph.removeElem(this.idGenerator.getFirstClassId());
		this.objectPropertyGraph.removeElem(this.idGenerator
				.getFirstPropertyId());
		this.classHierarchy = new IntegerHierarchicalGraphImpl(this.classGraph);
		this.objectPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				this.objectPropertyGraph);
		this.classGraph = null;
		this.objectPropertyGraph = null;
	}

	protected void prepareQueue(ExtendedOntology ontology) {
		Set<Integer> classNameSet = new HashSet<Integer>();
		classNameSet.addAll(ontology.getClassSet());
		for (Integer className : classNameSet) {
			addToQueue(className, ontology.getClassEntries(className));
			addToQueue(className, ontology.getClassEntries(classTopElement));
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
	 * @param axioms
	 *            set of axioms, i.e. the ontology
	 */
	protected void preProcess(Set<IntegerAxiom> axioms) {
		this.isReady = false;

		logger.fine("configuring processor ...");

		// These sets include the declared entities that are not present in the
		// normalized axioms.
		Set<Integer> originalClassSet = new HashSet<Integer>();
		Set<Integer> originalObjectPropertySet = new HashSet<Integer>();
		for (IntegerAxiom axiom : axioms) {
			originalClassSet.addAll(axiom.getClassesInSignature());
			originalObjectPropertySet.addAll(axiom.getObjectPropertiesInSignature());
		}
		originalClassSet.add(classBottomElement);
		originalClassSet.add(classTopElement);
		originalObjectPropertySet.add(propertyBottomElement);
		originalObjectPropertySet.add(propertyTopElement);

		this.idGenerator = new IdGenerator(getMaximum(originalClassSet) + 1,
				getMaximum(originalObjectPropertySet) + 1);

		logger.finer("normalizing ontology ...");
		OntologyNormalizer normalizer = new OntologyNormalizer(this.idGenerator);
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		ontology.addAll(normalizer.normalize(axioms));

		logger.finer("auxiliary classes created : "
				+ (this.idGenerator.getNextClassId() - this.idGenerator
						.getFirstClassId()));
		logger.finer("auxiliary properties created : "
				+ (this.idGenerator.getNextPropertyId() - this.idGenerator
						.getFirstPropertyId()));

		logger.finer("creating extended ontology ...");
		this.extendedOntology = new ExtendedOntology();
		this.extendedOntology.load(ontology);

		logger.finer("creating class graph ...");
		this.classGraph = createClassGraph(originalClassSet, ontology);

		logger.finer("creating property graph ...");
		this.objectPropertyGraph = createObjectPropertyGraph(
				originalObjectPropertySet, ontology);

		this.setR = createSetR(this.objectPropertyGraph.getElements());

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

	protected void process(Integer cA, ExtensionEntry eX) {
		if (eX.isImplication()) {
			processImplication(cA, eX.asImplication());
		} else if (eX.isExistential()) {
			processExistential(cA, eX.asExistential());
		} else {
			throw new RuntimeException(
					"Internal error: entry was not recognized " + eX);
		}
	}

	protected void processBottom(Integer className) {
		this.classGraph.addAncestor(className, classBottomElement);

		for (Iterator<Integer> it = this.setR.keySet().iterator(); it.hasNext();) {
			Integer relation = it.next();
			for (Integer firstComponent : getInternalRelation(relation)
					.getBySecond(className)) {
				Collection<Integer> subsumers = this.classGraph
						.getSubsumers(firstComponent);
				if (!subsumers.contains(classBottomElement)) {
					processBottom(firstComponent);
				}
			}
		}
	}

	protected void processExistential(Integer cA, ExistentialEntry eX) {
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

	protected void processImplication(Integer cA, ImplicationEntry eX) {
		Set<Integer> vecB = eX.getOperands();
		Integer cB = eX.getSuperClass();
		Collection<Integer> sSofA = this.classGraph.getSubsumers(cA);
		Integer bottom = classBottomElement;

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

					Set<Integer> classSet = getInternalRelation(r).getBySecond(
							cA);

					for (Integer cAprime : classSet) {

						addToQueue(cAprime, existentialEntries);
					}
				}
			}
		}
	}

	protected void processNewEdge(Integer cA, Integer r, Integer cB) {
		IntegerBinaryRelation rels = null;
		for (Integer s : this.transitiveSubsumed.get(r)) {

			rels = getRelationPairMap().get(s);
			if (rels == null) {
				rels = new IntegerBinaryRelation();
				getRelationPairMap().put(s, rels);
			}
			rels.add(cA, cB);
			getPropertyUsedByClass(cB).add(s);

			for (Integer cBprime : this.classGraph.getSubsumers(cB)) {
				addToQueue(cA, getExtendedOntology().getExistentialEntries(s,
						cBprime));
			}

			for (RI3Axiom axiom : getExtendedOntology()
					.getSubPropertyAxiomSetByRight(s)) {

				Integer t = axiom.getLeftSubProperty();
				Integer u = axiom.getSuperProperty();

				Set<Integer> classSet = getInternalRelation(t).getBySecond(cA);

				for (Integer cAprime : classSet) {

					if (!getInternalRelation(u).contains(cAprime, cB)) {
						processNewEdge(cAprime, u, cB);
					}
				}
			}

			for (RI3Axiom axiom : getExtendedOntology()
					.getSubPropertyAxiomSetByLeft(s)) {

				Integer t = axiom.getRightSubProperty();
				Integer u = axiom.getSuperProperty();

				Set<Integer> classSet = getInternalRelation(t).getByFirst(cB);

				for (Integer cBprime : classSet) {

					if (!getInternalRelation(u).contains(cA, cBprime)) {
						processNewEdge(cA, u, cBprime);
					}
				}
			}
		}
	}
}
