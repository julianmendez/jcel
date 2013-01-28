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

package de.tudresden.inf.lat.jcel.core.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class implements the algorithm that computes the class hierarchy from
 * the subsumer set. This implementation starts finding direct subsumees from
 * top, marks them, and then looks for their direct subsumees. In this way, it
 * builds the new graph in levels according to the distance to top.
 * 
 * @author Julian Mendez
 */
public class IntegerHierarchicalGraphImpl implements IntegerHierarchicalGraph {

	private final Integer bottomElement;
	private Map<Integer, Set<Integer>> children = new TreeMap<Integer, Set<Integer>>();
	private Map<Integer, Set<Integer>> equivalents = new TreeMap<Integer, Set<Integer>>();
	private Map<Integer, Set<Integer>> parents = new TreeMap<Integer, Set<Integer>>();
	private Map<Integer, Integer> representative = new TreeMap<Integer, Integer>();
	private final Integer topElement;

	/**
	 * Constructs an empty hierarchical graph.
	 * 
	 * @param bottom
	 *            bottom class identifier
	 * @param top
	 *            top class identifier
	 */
	public IntegerHierarchicalGraphImpl(Integer bottom, Integer top) {
		if (bottom == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (top == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.bottomElement = bottom;
		this.topElement = top;
	}

	/**
	 * Constructs a hierarchical graph using another graph.
	 * 
	 * @param origGraph
	 *            a subsumer graph
	 */
	public IntegerHierarchicalGraphImpl(IntegerSubsumerGraph origGraph) {
		if (origGraph == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.bottomElement = origGraph.getBottomElement();
		this.topElement = origGraph.getTopElement();

		if (origGraph.containsPair(getTopElement(), getBottomElement())) {
			computeInconsistentDag(origGraph);
		} else {
			computeDag(origGraph);
			updateParents();
			updateChildren();
			updateBottom();
		}
	}

	private void computeDag(IntegerSubsumerGraph setS) {
		reset(setS.getElements());
		Set<Integer> sCNTop = new TreeSet<Integer>();
		sCNTop.addAll(setS.getElements());
		sCNTop.remove(getBottomElement());

		Set<Integer> equivToTop = new TreeSet<Integer>();
		equivToTop.addAll(setS.getSubsumers(getTopElement()));
		for (Integer elem : equivToTop) {
			makeEquivalent(getTopElement(), elem);
		}

		Set<Integer> classified = new TreeSet<Integer>();
		classified.addAll(equivToTop);

		for (Integer cA : sCNTop) {
			if (!classified.contains(cA)) {
				dagClassify(cA, classified, setS);
			}
		}
	}

	private void computeInconsistentDag(IntegerSubsumerGraph setS) {
		Collection<Integer> elements = setS.getElements();
		reset(elements);
		for (Integer elem : elements) {
			makeEquivalent(getBottomElement(), elem);
		}
	}

	private void dagClassify(Integer cA, Set<Integer> classified,
			IntegerSubsumerGraph setS) {
		Set<Integer> candidates = new TreeSet<Integer>();
		candidates.add(getTopElement());
		Set<Integer> subsumersA = new TreeSet<Integer>();
		subsumersA.addAll(setS.getSubsumers(cA));
		subsumersA.remove(cA);
		subsumersA.remove(getTopElement());
		for (Integer cB : subsumersA) {
			Set<Integer> subsumersB = new TreeSet<Integer>();
			subsumersB.addAll(setS.getSubsumers(cB));
			if (subsumersB.contains(cA)) {
				classified.add(cB);
				makeEquivalent(cA, cB);
			} else {
				if (!classified.contains(cB)) {
					dagClassify(cB, classified, setS);
				}
				candidates.add(cB);
			}
		}
		dagInsert(cA, candidates);
		classified.add(cA);
	}

	private void dagInsert(Integer cA, Set<Integer> candidates) {
		Set<Integer> marked = new TreeSet<Integer>();
		for (Integer cB : candidates) {
			Set<Integer> parenSet = this.parents.get(cB);
			for (Integer cX : parenSet) {
				marked.add(cX);
			}
		}
		Set<Integer> notMarkedCandidates = new TreeSet<Integer>();
		notMarkedCandidates.addAll(candidates);
		notMarkedCandidates.removeAll(marked);
		this.parents.put(cA, notMarkedCandidates);
		for (Integer cB : notMarkedCandidates) {
			this.children.get(cB).add(cA);
		}
	}

	/**
	 * Makes a disjoint union with another graph. They must share the same
	 * bottom and top elements.
	 * 
	 * @param otherGraph
	 *            other graph to make the union
	 */
	public void disjointUnion(IntegerHierarchicalGraph otherGraph) {
		if (otherGraph == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (getBottomElement().equals(otherGraph.getBottomElement())
				&& getTopElement().equals(otherGraph.getTopElement())) {
			Set<Integer> set = new HashSet<Integer>();
			set.addAll(getElements());
			set.remove(getBottomElement());
			set.remove(getTopElement());
			set.retainAll(otherGraph.getElements());

			if (!set.isEmpty()) {
				throw new IllegalArgumentException("Graphs are not disjoint.");
			}

			Set<Integer> otherSet = new HashSet<Integer>();
			otherSet.addAll(otherGraph.getElements());
			for (Integer elem : otherSet) {

				if (this.children.get(elem) == null) {
					this.children.put(elem, new HashSet<Integer>());
				}
				this.children.get(elem).addAll(otherGraph.getChildren(elem));

				if (this.parents.get(elem) == null) {
					this.parents.put(elem, new HashSet<Integer>());
				}
				this.parents.get(elem).addAll(otherGraph.getParents(elem));

				if (this.equivalents.get(elem) == null) {
					this.equivalents.put(elem, new HashSet<Integer>());
				}
				if (this.representative.get(elem) == null) {
					this.representative.put(elem, elem);
				}
				for (Integer otherElem : otherGraph.getEquivalents(elem)) {
					makeEquivalent(elem, otherElem);
				}

			}

		} else {
			throw new IllegalArgumentException(
					"Both graphs have different bottom element or different top element.");
		}
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerHierarchicalGraph) {
			IntegerHierarchicalGraph other = (IntegerHierarchicalGraph) o;
			ret = getBottomElement().equals(other.getBottomElement())
					&& getTopElement().equals(other.getTopElement())
					&& getElements().equals(other.getElements());
			for (Iterator<Integer> it = getElements().iterator(); ret
					&& it.hasNext();) {
				Integer elem = it.next();
				ret = ret
						&& getChildren(elem).equals(other.getChildren(elem))
						&& getParents(elem).equals(other.getParents(elem))
						&& getEquivalents(elem).equals(
								other.getEquivalents(elem));
			}
		}
		return ret;
	}

	@Override
	public Set<Integer> getAncestors(Integer orig) {
		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> toVisit = new HashSet<Integer>();
		toVisit.addAll(this.parents.get(orig));
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> related = new HashSet<Integer>();
			related.addAll(this.parents.get(elem));
			related.removeAll(ret);
			toVisit.addAll(related);
		}
		return ret;
	}

	@Override
	public Integer getBottomElement() {
		return this.bottomElement;
	}

	@Override
	public Set<Integer> getChildren(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableSet(this.children.get(elem));
	}

	@Override
	public Set<Integer> getDescendants(Integer orig) {
		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> toVisit = new HashSet<Integer>();
		toVisit.addAll(this.children.get(orig));
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> related = new HashSet<Integer>();
			related.addAll(this.children.get(elem));
			related.removeAll(ret);
			toVisit.addAll(related);
		}
		return ret;
	}

	@Override
	public Set<Integer> getElements() {
		return Collections.unmodifiableSet(this.representative.keySet());
	}

	@Override
	public Set<Integer> getEquivalents(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableSet(this.equivalents
				.get(this.representative.get(elem)));
	}

	/**
	 * Returns one vertex for each equivalence class of vertices in the graph.
	 * 
	 * @return one vertex for each equivalence class of vertices in the graph
	 */
	public Set<Integer> getNonEquivalentElements() {
		return Collections.unmodifiableSet(this.equivalents.keySet());
	}

	@Override
	public Set<Integer> getParents(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableSet(this.parents.get(elem));
	}

	@Override
	public Integer getTopElement() {
		return this.topElement;
	}

	@Override
	public int hashCode() {
		return this.parents.hashCode();
	}

	private void makeEquivalent(Integer cA, Integer cB) {
		Integer repA = this.representative.get(cA);
		Integer repB = this.representative.get(cB);
		if (!repA.equals(repB)) {
			Integer rep = Math.min(repA, repB);
			Integer exRep = Math.max(repA, repB);
			this.equivalents.get(rep).addAll(this.equivalents.get(exRep));
			for (Integer elem : this.equivalents.get(exRep)) {
				this.representative.put(elem, rep);
			}
			this.equivalents.remove(exRep);
		}
	}

	private void reset(Collection<Integer> elements) {
		this.children.clear();
		this.parents.clear();
		this.equivalents.clear();
		this.representative.clear();

		for (Integer elem : elements) {
			this.children.put(elem, new TreeSet<Integer>());
			this.parents.put(elem, new TreeSet<Integer>());
			Set<Integer> equiv = new TreeSet<Integer>();
			equiv.add(elem);
			this.equivalents.put(elem, equiv);
			this.representative.put(elem, elem);
		}
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("\n* children : ");
		ret.append(this.children);
		ret.append("\n* parents : ");
		ret.append(this.parents);
		ret.append("\n* equivalents : ");
		ret.append(this.equivalents);
		ret.append("\n* representative : ");
		ret.append(this.representative);
		ret.append("\n");
		return ret.toString();
	}

	private void updateBottom() {
		Set<Integer> parentsOfBottom = new HashSet<Integer>();
		for (Integer elem : getElements()) {
			if (this.children.get(elem).isEmpty()) {
				parentsOfBottom.add(elem);
			}
		}
		Set<Integer> equivToBottom = this.equivalents.get(this.bottomElement);
		parentsOfBottom.removeAll(equivToBottom);
		if (!equivToBottom.contains(this.topElement)
				&& parentsOfBottom.isEmpty()) {
			parentsOfBottom.add(this.topElement);
		}
		for (Integer elem : parentsOfBottom) {
			this.children.get(elem).add(this.bottomElement);
		}
		for (Integer elem : equivToBottom) {
			this.parents.put(elem, parentsOfBottom);
		}
	}

	private void updateChildren() {
		for (Integer elem : getElements()) {
			Set<Integer> elemChildren = new HashSet<Integer>();
			for (Integer index : getEquivalents(elem)) {
				for (Integer child : this.children.get(index)) {
					elemChildren.addAll(getEquivalents(child));
				}
			}
			for (Integer index : getEquivalents(elem)) {
				this.children.put(index, elemChildren);
			}
		}
	}

	private void updateParents() {
		for (Integer elem : getElements()) {
			Set<Integer> elemParents = new HashSet<Integer>();
			for (Integer index : getEquivalents(elem)) {
				for (Integer parent : this.parents.get(index)) {
					elemParents.addAll(getEquivalents(parent));
				}
			}
			for (Integer index : getEquivalents(elem)) {
				this.parents.put(index, elemParents);
			}
		}
	}

}
