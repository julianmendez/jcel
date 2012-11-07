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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.graph.IntegerRelationMapImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerBidirectionalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.coreontology.axiom.ExtendedOntology;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;

/**
 * An object of this class keeps the status of the classifier.
 * 
 * @author Julian Mendez
 */
public class ClassifierStatusImpl implements ClassifierStatus {

	private static final int bottomClassId = IntegerEntityManager.bottomClassId;
	private static final int bottomObjectPropertyId = IntegerEntityManager.bottomObjectPropertyId;
	private static final int topClassId = IntegerEntityManager.topClassId;
	private static final int topObjectPropertyId = IntegerEntityManager.topObjectPropertyId;

	private IntegerSubsumerGraphImpl classGraph = null;
	private Map<Integer, Set<Integer>> cognateFunctPropMap = new HashMap<Integer, Set<Integer>>();
	private final ExtendedOntology extendedOntology;
	private IntegerEntityManager idGenerator = null;
	private Map<VNodeImpl, Integer> invNodeSet = new HashMap<VNodeImpl, Integer>();
	private Object monitorClassGraph = new Object();
	private Object monitorRelationSet = new Object();
	private Object monitorSetQsubR = new Object();
	private Object monitorSetQsubS = new Object();
	private Map<Integer, VNodeImpl> nodeSet = new HashMap<Integer, VNodeImpl>();
	private IntegerSubsumerBidirectionalGraphImpl objectPropertyGraph = null;
	private IntegerRelationMapImpl relationSet = null;
	private Set<REntry> setQsubR = new TreeSet<REntry>();
	private Set<SEntry> setQsubS = new TreeSet<SEntry>();

	/**
	 * Constructs a new classifier status.
	 * 
	 * @param generator
	 *            identifier generator
	 * @param ontology
	 *            extended ontology
	 */
	public ClassifierStatusImpl(IntegerEntityManager generator,
			ExtendedOntology ontology) {
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
		createMapOfObjectPropertiesWithFunctionalAncestor();
	}

	@Override
	public boolean addNewREntry(int propertyId, int leftClassId,
			int rightClassId) {
		boolean ret = false;
		synchronized (this.monitorSetQsubR) {
			ret = this.setQsubR.add(new REntryImpl(propertyId, leftClassId,
					rightClassId));
		}
		return ret;
	}

	@Override
	public boolean addNewSEntry(int subClassId, int superClassId) {
		boolean ret = false;
		synchronized (this.monitorSetQsubS) {
			ret = this.setQsubS.add(new SEntryImpl(subClassId, superClassId));
		}
		return ret;
	}

	/**
	 * Adds a new triplet to the set R.
	 * 
	 * @param property
	 *            property
	 * @param leftClass
	 *            left class
	 * @param rightClass
	 *            right class
	 * @return <code>true</code> if the triplet was effectively added,
	 *         <code>false</code> otherwise
	 */
	public boolean addToR(int property, int leftClass, int rightClass) {
		boolean ret = false;
		synchronized (this.monitorRelationSet) {
			ret = this.relationSet.add(property, leftClass, rightClass);
		}
		return ret;
	}

	/**
	 * Adds a new pair to the set S.
	 * 
	 * @param subClass
	 *            sub class
	 * @param superClass
	 *            super class
	 * @return <code>true</code> if the pair was effectively added,
	 *         <code>false</code> otherwise
	 */
	public boolean addToS(int subClass, int superClass) {
		boolean ret = false;
		synchronized (this.monitorClassGraph) {
			ret = this.classGraph.addAncestor(subClass, superClass);
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
		synchronized (this.monitorClassGraph) {
			this.classGraph = new IntegerSubsumerGraphImpl(bottomClassId,
					topClassId);
			for (int index : getExtendedOntology().getClassSet()) {
				this.classGraph.addAncestor(index, topClassId);
			}
			this.classGraph.addAncestor(topClassId, topClassId);
		}
		this.nodeSet.clear();
		this.invNodeSet.clear();
		for (int elem : this.classGraph.getElements()) {
			VNodeImpl node = new VNodeImpl(elem);
			this.nodeSet.put(elem, node);
			this.invNodeSet.put(node, elem);
		}
	}

	private void createMapOfObjectPropertiesWithFunctionalAncestor() {
		for (int s : this.extendedOntology.getFunctionalObjectProperties()) {
			Collection<Integer> cognates = getSubObjectProperties(s);
			for (int r : cognates) {
				Set<Integer> currentSet = this.cognateFunctPropMap.get(r);
				if (currentSet == null) {
					currentSet = new HashSet<Integer>();
					this.cognateFunctPropMap.put(r, currentSet);
				}
				currentSet.addAll(cognates);
			}
		}
	}

	private void createObjectPropertyGraph() {
		this.objectPropertyGraph = new IntegerSubsumerBidirectionalGraphImpl(
				bottomObjectPropertyId, topObjectPropertyId);
		for (int index : this.extendedOntology.getObjectPropertySet()) {
			this.objectPropertyGraph.addAncestor(index, topObjectPropertyId);
			int inverseProp = this.idGenerator
					.createOrGetInverseObjectPropertyOf(index);
			this.objectPropertyGraph.addAncestor(inverseProp,
					topObjectPropertyId);
		}
		for (int property : this.extendedOntology.getObjectPropertySet()) {
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
	public int createOrGetNodeId(VNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.invNodeSet.get(node);
		if (ret == null) {
			ret = node.getClassId();
			if (!node.isEmpty()) {
				ret = getIdGenerator().createAnonymousEntity(
						IntegerEntityType.CLASS, true);
				VNodeImpl newNode = new VNodeImpl(node.getClassId());
				newNode.addExistentialsOf(node);
				this.nodeSet.put(ret, newNode);
				this.invNodeSet.put(newNode, ret);
			}
		}
		return ret;
	}

	private void createRelationSet() {
		Collection<Integer> collection = getObjectPropertyGraph().getElements();
		synchronized (this.monitorRelationSet) {
			this.relationSet = new IntegerRelationMapImpl();
			for (int index : collection) {
				this.relationSet.add(index);
			}
		}
	}

	private void createSetOfNodes() {
		for (int classId : getExtendedOntology().getClassSet()) {
			createOrGetNodeId(new VNodeImpl(classId));
		}
	}

	/**
	 * Deletes the class graph.
	 */
	protected void deleteClassGraph() {
		synchronized (this.monitorClassGraph) {
			this.classGraph = null;
		}
	}

	/**
	 * Deletes the object property graph.
	 */
	protected void deleteObjectPropertyGraph() {
		this.objectPropertyGraph = null;
	}

	/**
	 * Returns the class graph.
	 * 
	 * @return the class graph
	 */
	protected IntegerSubsumerGraphImpl getClassGraph() {
		return this.classGraph;
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
		for (int nodeId : this.nodeSet.keySet()) {
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
	public Collection<Integer> getFirstBySecond(int propertyId, int classId) {
		Collection<Integer> ret = null;
		synchronized (this.monitorRelationSet) {
			ret = this.relationSet.getBySecond(propertyId, classId);
		}
		return ret;
	}

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	protected IntegerEntityManager getIdGenerator() {
		return this.idGenerator;
	}

	@Override
	public int getInverseObjectPropertyOf(int propertyId) {
		return this.idGenerator.createOrGetInverseObjectPropertyOf(propertyId);
	}

	@Override
	public VNode getNode(int nodeId) {
		return this.nodeSet.get(nodeId);
	}

	/**
	 * Returns the number of R-entries to be processed.
	 * 
	 * @return the number of R-entries to be processed
	 */
	public int getNumberOfREntries() {
		int ret = 0;
		synchronized (this.monitorSetQsubR) {
			ret = this.setQsubR.size();
		}
		return ret;
	}

	/**
	 * Returns the number of S-entries to be processed.
	 * 
	 * @return the number of S-entries to be processed
	 */

	public int getNumberOfSEntries() {
		int ret = 0;
		synchronized (this.monitorSetQsubS) {
			ret = this.setQsubS.size();
		}
		return ret;
	}

	@Override
	public Collection<Integer> getObjectPropertiesByFirst(int cA) {
		Collection<Integer> ret = null;
		synchronized (this.monitorRelationSet) {
			ret = this.relationSet.getRelationsByFirst(cA);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getObjectPropertiesBySecond(int cA) {
		Collection<Integer> ret = null;
		synchronized (this.monitorRelationSet) {
			ret = this.relationSet.getRelationsBySecond(cA);
		}
		return ret;
	}

	@Override
	public Set<Integer> getObjectPropertiesWithFunctionalAncestor(
			int objectProperty) {
		Set<Integer> ret = this.cognateFunctPropMap.get(objectProperty);
		if (ret == null) {
			ret = Collections.emptySet();
		} else {
			ret = Collections.unmodifiableSet(ret);
		}
		return ret;
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
	public Collection<Integer> getSecondByFirst(int propertyId, int classId) {
		Collection<Integer> ret = null;
		synchronized (this.monitorRelationSet) {
			ret = this.relationSet.getByFirst(propertyId, classId);
		}
		return ret;
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
	public Collection<Integer> getSubObjectProperties(int objectProperty) {
		return Collections.unmodifiableCollection(this.objectPropertyGraph
				.getSubsumees(objectProperty));
	}

	@Override
	public Collection<Integer> getSubsumers(int classId) {
		Collection<Integer> ret = null;
		synchronized (this.monitorClassGraph) {
			ret = this.classGraph.getSubsumers(classId);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getSuperObjectProperties(int objectProperty) {
		return Collections.unmodifiableCollection(this.objectPropertyGraph
				.getSubsumers(objectProperty));
	}

	private void makeTransitiveClosure(
			IntegerSubsumerBidirectionalGraphImpl graph) {
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;
			for (int elem : graph.getElements()) {
				Collection<Integer> subsumerSet = graph.getSubsumers(elem);
				Set<Integer> allSubsumers = new HashSet<Integer>();
				allSubsumers.add(elem);
				for (int otherElem : subsumerSet) {
					allSubsumers.addAll(graph.getSubsumers(otherElem));
				}
				allSubsumers.removeAll(subsumerSet);
				if (!allSubsumers.isEmpty()) {
					hasChanged = true;
					for (int subsumer : allSubsumers) {
						graph.addAncestor(elem, subsumer);
					}
				}
			}
		}
	}

	/**
	 * Returns the next R-entry and removes it from the set to be processed.
	 * 
	 * @return the next R-entry and removes it from the set to be processed
	 * 
	 * @throws NoSuchElementException
	 *             if the set of R-entries is empty
	 */
	public REntry removeNextREntry() {
		REntry ret = null;
		synchronized (this.monitorSetQsubR) {
			if (this.setQsubR.isEmpty()) {
				throw new NoSuchElementException();
			}

			ret = this.setQsubR.iterator().next();
			this.setQsubR.remove(ret);
		}
		return ret;
	}

	/**
	 * Returns the next S-entry and removes it from the set to be processed.
	 * 
	 * @return the next S-entry and removes it from the set to be processed
	 * @throws NoSuchElementException
	 *             if the set of S-entries is empty
	 */
	public SEntry removeNextSEntry() {
		SEntry ret = null;
		synchronized (this.monitorSetQsubS) {
			if (this.setQsubS.isEmpty()) {
				throw new NoSuchElementException();
			}

			ret = this.setQsubS.iterator().next();
			this.setQsubS.remove(ret);
		}
		return ret;
	}

}
