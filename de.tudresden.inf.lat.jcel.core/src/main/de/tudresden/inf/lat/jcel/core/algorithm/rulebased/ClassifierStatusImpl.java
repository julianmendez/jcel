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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.ExtendedOntology;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.ExtendedOntologyImpl;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.core.graph.IntegerRelationMapImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerBidirectionalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.core.normalization.IdGenerator;

/**
 * An object of this class keeps the status of the classifier.
 * 
 * @author Julian Mendez
 */
public class ClassifierStatusImpl implements ClassifierStatus {

	// private static final Logger logger = Logger
	// .getLogger(ClassifierStatusImpl.class.getName());

	private static final Integer classBottomElement = IntegerDatatype.classBottomElement;
	private static final Integer classTopElement = IntegerDatatype.classTopElement;
	private static final Integer propertyBottomElement = IntegerDatatype.propertyBottomElement;
	private static final Integer propertyTopElement = IntegerDatatype.propertyTopElement;

	private IntegerSubsumerGraphImpl classGraph = null;
	private ExtendedOntology extendedOntology = new ExtendedOntologyImpl();
	private IdGenerator idGenerator = null;
	private Map<VNodeImpl, Integer> invNodeSet = new HashMap<VNodeImpl, Integer>();
	private Map<Integer, VNodeImpl> nodeSet = new HashMap<Integer, VNodeImpl>();
	private Integer nodeSetLastId = null;
	private IntegerSubsumerBidirectionalGraphImpl objectPropertyGraph = null;
	private IntegerRelationMapImpl relationSet = null;

	/**
	 * Constructs a new classifier status.
	 * 
	 * @param generator
	 *            identifier generator
	 * @param ontology
	 *            extended ontology
	 */
	public ClassifierStatusImpl(IdGenerator generator, ExtendedOntology ontology) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.idGenerator = generator;
		this.extendedOntology = ontology;

		createClassGraph();
		createObjectPropertyGraph();
		createRelationSet();
		createSetOfNodes();
	}

	/**
	 * Adds a new triplet to the set R.
	 * 
	 * @param entry
	 *            the new triplet
	 * @return <code>true</code> if the triplet was effectively added,
	 *         <code>false</code> otherwise
	 */
	public boolean addToR(REntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.relationSet.contains(entry.getProperty(),
				entry.getLeftClass(), entry.getRightClass())) {
			this.relationSet.add(entry.getProperty(), entry.getLeftClass(),
					entry.getRightClass());
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds a new pair to the set S.
	 * 
	 * @param entry
	 *            the new pair
	 * @return <code>true</code> if the pair was effectively added,
	 *         <code>false</code> otherwise
	 */
	public boolean addToS(SEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.classGraph.getElements().contains(entry.getSubClass())) {
			this.classGraph.add(entry.getSubClass());
		}
		if (!this.classGraph.getSubsumers(entry.getSubClass()).contains(
				entry.getSuperClass())) {
			this.classGraph.addAncestor(entry.getSubClass(),
					entry.getSuperClass());
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean contains(VNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return (this.invNodeSet.get(node) != null);
	}

	private void createClassGraph() {
		this.classGraph = new IntegerSubsumerGraphImpl(classBottomElement,
				classTopElement);
		for (Integer index : getExtendedOntology().getClassSet()) {
			this.classGraph.addAncestor(index, classTopElement);
		}
		this.classGraph.addAncestor(classTopElement, classTopElement);

		this.nodeSet.clear();
		this.invNodeSet.clear();
		this.nodeSetLastId = this.classGraph.getElements().iterator().next();
		for (Integer elem : this.classGraph.getElements()) {
			VNodeImpl node = new VNodeImpl(elem);
			this.nodeSet.put(elem, node);
			this.invNodeSet.put(node, elem);
			if (elem > this.nodeSetLastId) {
				this.nodeSetLastId = elem;
			}
		}
		this.nodeSetLastId++;
	}

	private void createObjectPropertyGraph() {
		this.objectPropertyGraph = new IntegerSubsumerBidirectionalGraphImpl(
				propertyBottomElement, propertyTopElement);
		for (Integer index : this.extendedOntology.getObjectPropertySet()) {
			this.objectPropertyGraph.addAncestor(index, propertyTopElement);
			Integer inverseProp = this.idGenerator
					.createOrGetInverseObjectPropertyOf(index);
			this.objectPropertyGraph.addAncestor(inverseProp,
					propertyTopElement);
		}
		for (Integer property : this.extendedOntology.getObjectPropertySet()) {
			Set<RI2Axiom> axiomSet = this.extendedOntology
					.getRI2rAxioms(property);
			for (RI2Axiom axiom : axiomSet) {
				this.objectPropertyGraph.addAncestor(axiom.getSubProperty(),
						axiom.getSuperProperty());
			}
		}
		makeTransitiveClosure(this.objectPropertyGraph);
	}

	@Override
	public Integer createOrGetNodeId(VNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.invNodeSet.get(node);
		if (ret == null) {
			ret = node.getClassId();
			if (!node.isEmpty()) {
				ret = this.nodeSetLastId;
				VNodeImpl newNode = new VNodeImpl(node.getClassId());
				newNode.addExistentialsOf(node);
				this.nodeSet.put(ret, newNode);
				this.invNodeSet.put(newNode, ret);
				this.nodeSetLastId++;
			}
		}
		return ret;
	}

	private void createRelationSet() {
		Collection<Integer> collection = getObjectPropertyGraph().getElements();
		this.relationSet = new IntegerRelationMapImpl();
		for (Integer index : collection) {
			this.relationSet.add(index);
		}
	}

	private void createSetOfNodes() {
		for (Integer classId : getExtendedOntology().getClassSet()) {
			createOrGetNodeId(new VNodeImpl(classId));
		}
	}

	/**
	 * Deletes the class graph.
	 */
	protected void deleteClassGraph() {
		this.classGraph = null;
	}

	/**
	 * Deletes the object property graph.
	 */
	protected void deleteObjectPropertyGraph() {
		this.objectPropertyGraph = null;
	}

	@Override
	public Integer getClassBottomElement() {
		return this.classGraph.getBottomElement();
	}

	/**
	 * Returns the class graph.
	 * 
	 * @return the class graph
	 */
	protected IntegerSubsumerGraphImpl getClassGraph() {
		return this.classGraph;
	}

	@Override
	public Integer getClassTopElement() {
		return this.classGraph.getTopElement();
	}

	/**
	 * Returns the number of nodes in the relation set.
	 * 
	 * @return the number of nodes in the relation set
	 */
	public long getDeepSizeOfR() {
		return this.relationSet.getDeepSize();
	}

	/**
	 * Returns the number of nodes in the subsumer set.
	 * 
	 * @return the number of nodes in the subsumer set
	 */
	public long getDeepSizeOfS() {
		return this.classGraph.getDeepSize();
	}

	/**
	 * Returns the number of elements in the node set.
	 * 
	 * @return the number of elements in the node set
	 */
	public long getDeepSizeOfV() {
		long ret = 0;
		for (Integer nodeId : this.nodeSet.keySet()) {
			VNodeImpl node = this.nodeSet.get(nodeId);
			ret += node.getDeepSize();
		}
		return ret;
	}

	@Override
	public ExtendedOntology getExtendedOntology() {
		return this.extendedOntology;
	}

	@Override
	public Collection<Integer> getFirstBySecond(Integer propertyId,
			Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getBySecond(propertyId, classId);
	}

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	protected IdGenerator getIdGenerator() {
		return this.idGenerator;
	}

	@Override
	public Integer getInverseObjectPropertyOf(Integer propertyId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.idGenerator.createOrGetInverseObjectPropertyOf(propertyId);
	}

	@Override
	public VNode getNode(Integer nodeId) {
		if (nodeId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.nodeSet.get(nodeId);
	}

	@Override
	public Collection<Integer> getObjectPropertiesByFirst(Integer cA) {
		if (cA == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getRelationsByFirst(cA);
	}

	@Override
	public Collection<Integer> getObjectPropertiesBySecond(Integer cA) {
		if (cA == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getRelationsBySecond(cA);
	}

	/**
	 * Returns the object property graph.
	 * 
	 * @return the object property graph
	 */
	protected IntegerSubsumerBidirectionalGraphImpl getObjectPropertyGraph() {
		return this.objectPropertyGraph;
	}

	/**
	 * Returns the set of relations.
	 * 
	 * @return the set of relations
	 */
	protected IntegerRelationMapImpl getRelationSet() {
		return this.relationSet;
	}

	@Override
	public Collection<Integer> getSecondByFirst(Integer propertyId,
			Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getByFirst(propertyId, classId);
	}

	/**
	 * Returns the number of nodes.
	 * 
	 * @return the number of nodes
	 */
	public long getSizeOfV() {
		return this.nodeSet.size();
	}

	@Override
	public Collection<Integer> getSubsumers(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.classGraph.getSubsumers(classId);
	}

	private void makeTransitiveClosure(
			IntegerSubsumerBidirectionalGraphImpl graph) {
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

}
