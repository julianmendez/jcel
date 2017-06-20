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

package de.tudresden.inf.lat.jcel.core.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.tudresden.inf.lat.jcel.coreontology.common.OptMap;
import de.tudresden.inf.lat.jcel.coreontology.common.OptMapImpl;

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
	private final OptMap<Integer, Set<Integer>> children = new OptMapImpl<>(new TreeMap<>());
	private final OptMap<Integer, Set<Integer>> equivalents = new OptMapImpl<>(new TreeMap<>());
	private final OptMap<Integer, Set<Integer>> parents = new OptMapImpl<>(new TreeMap<>());
	private final OptMap<Integer, Integer> representative = new OptMapImpl<>(new TreeMap<>());
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
		Objects.requireNonNull(bottom);
		Objects.requireNonNull(top);
		this.bottomElement = bottom;
		this.topElement = top;
	}

	private Set<Integer> get(OptMap<Integer, Set<Integer>> map, Integer key) {
		Optional<Set<Integer>> optSet = map.get(key);
		if (!optSet.isPresent()) {
			throw new IllegalStateException("Illegal state of internal map, error retrieving '" + key + "'.");
		}
		return optSet.get();
	}

	/**
	 * Constructs a hierarchical graph using another graph.
	 * 
	 * @param origGraph
	 *            a subsumer graph
	 */
	public IntegerHierarchicalGraphImpl(IntegerSubsumerGraph origGraph) {
		Objects.requireNonNull(origGraph);
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
		Set<Integer> sCN = new TreeSet<>();
		sCN.addAll(setS.getElements());
		sCN.remove(getBottomElement());
		sCN.remove(getTopElement());

		Set<Integer> equivToTop = new TreeSet<>();
		equivToTop.addAll(setS.getSubsumers(getTopElement()));
		equivToTop.forEach(elem -> makeEquivalent(getTopElement(), elem));

		Set<Integer> classified = new TreeSet<>();
		classified.addAll(equivToTop);

		sCN.forEach(cA -> {
			if (!classified.contains(cA)) {
				dagClassify(cA, classified, setS);
			}
		});
	}

	private void computeInconsistentDag(IntegerSubsumerGraph setS) {
		Collection<Integer> elements = setS.getElements();
		reset(elements);
		elements.forEach(elem -> makeEquivalent(getBottomElement(), elem));
	}

	private void dagClassify(Integer cA, Set<Integer> classified, IntegerSubsumerGraph setS) {
		Set<Integer> candidates = new TreeSet<>();
		candidates.add(getTopElement());
		Set<Integer> subsumersA = new TreeSet<>();
		subsumersA.addAll(setS.getSubsumers(cA));
		subsumersA.remove(cA);
		subsumersA.remove(getTopElement());
		subsumersA.forEach(cB -> {
			Set<Integer> subsumersB = new TreeSet<>();
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
		});
		dagInsert(cA, candidates);
		classified.add(cA);
	}

	private void dagInsert(Integer cA, Set<Integer> candidates) {
		Set<Integer> marked = new TreeSet<>();
		candidates.forEach(cB -> {
			Set<Integer> parentSet = get(this.parents, cB);
			parentSet.forEach(cX -> marked.add(cX));
		});
		Set<Integer> notMarkedCandidates = new TreeSet<>();
		notMarkedCandidates.addAll(candidates);
		notMarkedCandidates.removeAll(marked);
		this.parents.put(cA, notMarkedCandidates);
		notMarkedCandidates.forEach(cB -> get(this.children, cB).add(cA));
	}

	/**
	 * Makes a disjoint union with another graph. They must share the same
	 * bottom and top elements.
	 * 
	 * @param otherGraph
	 *            other graph to make the union
	 */
	public void disjointUnion(IntegerHierarchicalGraph otherGraph) {
		Objects.requireNonNull(otherGraph);
		if (getBottomElement().equals(otherGraph.getBottomElement())
				&& getTopElement().equals(otherGraph.getTopElement())) {
			Set<Integer> set = new HashSet<>();
			set.addAll(getElements());
			set.remove(getBottomElement());
			set.remove(getTopElement());
			set.retainAll(otherGraph.getElements());

			if (!set.isEmpty()) {
				throw new IllegalArgumentException("Graphs are not disjoint.");
			}

			Set<Integer> otherSet = new HashSet<>();
			otherSet.addAll(otherGraph.getElements());
			otherSet.forEach(elem -> {

				if (!this.children.get(elem).isPresent()) {
					this.children.put(elem, new HashSet<>());
				}
				get(this.children, elem).addAll(otherGraph.getChildren(elem));

				if (!this.parents.get(elem).isPresent()) {
					this.parents.put(elem, new HashSet<>());
				}
				get(this.parents, elem).addAll(otherGraph.getParents(elem));

				if (!this.equivalents.get(elem).isPresent()) {
					this.equivalents.put(elem, new HashSet<>());
				}
				if (!this.representative.get(elem).isPresent()) {
					this.representative.put(elem, elem);
				}
				otherGraph.getEquivalents(elem).forEach(otherElem -> makeEquivalent(elem, otherElem));

			});

		} else {
			throw new IllegalArgumentException("Both graphs have different bottom element or different top element.");
		}
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerHierarchicalGraph)) {
			IntegerHierarchicalGraph other = (IntegerHierarchicalGraph) o;
			ret = getBottomElement().equals(other.getBottomElement()) && getTopElement().equals(other.getTopElement())
					&& getElements().equals(other.getElements());

			ret = ret && getElements().stream()
					.allMatch(elem -> getChildren(elem).equals(other.getChildren(elem))
							&& getParents(elem).equals(other.getParents(elem))
							&& getEquivalents(elem).equals(other.getEquivalents(elem)));
		}
		return ret;
	}

	@Override
	public Set<Integer> getAncestors(Integer orig) {
		Objects.requireNonNull(orig);
		Set<Integer> ret = new HashSet<>();
		Set<Integer> toVisit = new HashSet<>();
		toVisit.addAll(get(this.parents, orig));
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> related = new HashSet<>();
			related.addAll(get(this.parents, elem));
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
		Objects.requireNonNull(elem);
		return Collections.unmodifiableSet(get(this.children, elem));
	}

	@Override
	public Set<Integer> getDescendants(Integer orig) {
		Objects.requireNonNull(orig);
		Set<Integer> ret = new HashSet<>();
		Set<Integer> toVisit = new HashSet<>();
		toVisit.addAll(get(this.children, orig));
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> related = new HashSet<>();
			related.addAll(get(this.children, elem));
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
		Objects.requireNonNull(elem);
		if (!this.representative.get(elem).isPresent()) {
			throw new IllegalStateException("Representative not found: '" + elem + "'.");
		}
		return Collections.unmodifiableSet(get(this.equivalents, this.representative.get(elem).get()));
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
		Objects.requireNonNull(elem);
		return Collections.unmodifiableSet(get(this.parents, elem));
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
		if (!this.representative.get(cA).isPresent()) {
			throw new IllegalStateException("Representative not found: '" + cA + "'.");
		}
		Integer repA = this.representative.get(cA).get();

		if (!this.representative.get(cB).isPresent()) {
			throw new IllegalStateException("Representative not found: '" + cB + "'.");
		}
		Integer repB = this.representative.get(cB).get();

		if (!repA.equals(repB)) {
			Integer rep = Math.min(repA, repB);
			Integer exRep = Math.max(repA, repB);
			get(this.equivalents, rep).addAll(get(this.equivalents, exRep));
			get(this.equivalents, exRep).forEach(elem -> this.representative.put(elem, rep));
			this.equivalents.remove(exRep);
		}
	}

	private void reset(Collection<Integer> elements) {
		this.children.clear();
		this.parents.clear();
		this.equivalents.clear();
		this.representative.clear();

		elements.forEach(elem -> {
			this.children.put(elem, new TreeSet<>());
			this.parents.put(elem, new TreeSet<>());
			Set<Integer> equiv = new TreeSet<>();
			equiv.add(elem);
			this.equivalents.put(elem, equiv);
			this.representative.put(elem, elem);
		});
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
		Set<Integer> parentsOfBottom = new HashSet<>();
		getElements().forEach(elem -> {
			if (get(this.children, elem).isEmpty()) {
				parentsOfBottom.add(elem);
			}
		});
		Set<Integer> equivToBottom = get(this.equivalents, this.bottomElement);
		parentsOfBottom.removeAll(equivToBottom);
		if (!equivToBottom.contains(this.topElement) && parentsOfBottom.isEmpty()) {
			parentsOfBottom.add(this.topElement);
		}
		parentsOfBottom.forEach(elem -> get(this.children, elem).add(this.bottomElement));
		equivToBottom.forEach(elem -> this.parents.put(elem, parentsOfBottom));
	}

	private void updateChildren() {
		getElements().forEach(elem -> {
			Set<Integer> elemChildren = new HashSet<>();
			getEquivalents(elem).forEach(index -> {
				get(this.children, index).forEach(child -> elemChildren.addAll(getEquivalents(child)));
			});
			getEquivalents(elem).forEach(index -> this.children.put(index, elemChildren));
		});
	}

	private void updateParents() {
		getElements().forEach(elem -> {
			Set<Integer> elemParents = new HashSet<>();
			getEquivalents(elem).forEach(index -> {
				get(this.parents, index).forEach(parent -> elemParents.addAll(getEquivalents(parent)));
			});
			getEquivalents(elem).forEach(index -> this.parents.put(index, elemParents));
		});
	}

}
