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

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class implements the algorithm that computes the concept hierarchy from
 * the subsumer set. This implementation starts finding direct subsumees from
 * top, marks them, and then looks for their direct subsumees. In this way, it
 * builds the new graph in levels according to the distance to top.
 * 
 * @author Julian Mendez
 */
public class IntegerHierarchicalGraphImpl implements IntegerHierarchicalGraph {

	// private static final Logger logger = Logger.getLogger(DagProcessor.class
	// .getName());

	private Integer bottomElement = null;
	private Map<Integer, Set<Integer>> children = null;
	private Map<Integer, Set<Integer>> equivalents = null;
	private Map<Integer, Set<Integer>> parents = null;
	private Integer topElement = null;

	public IntegerHierarchicalGraphImpl(IntegerSubsumerGraph origGraph) {
		this.bottomElement = origGraph.getBottomElement();
		this.topElement = origGraph.getTopElement();
		if (origGraph.getSubsumers(getTopElement())
				.contains(getBottomElement())) {
			computeInconsistentDag(origGraph);
		} else {
			computeDag(origGraph);
		}
	}

	protected void computeDag(IntegerSubsumerGraph setS) {
		reset(setS.getElements());
		Set<Integer> sCNTop = new TreeSet<Integer>();
		sCNTop.addAll(setS.getElements());
		sCNTop.remove(getBottomElement());

		Set<Integer> classified = new TreeSet<Integer>();
		classified.add(getTopElement());

		for (Integer cA : sCNTop) {
			if (!classified.contains(cA)) {
				dagClassify(cA, classified, setS);
			}
		}
		for (Integer cA : sCNTop) {
			Set<Integer> chSet = this.children.get(cA);
			if (chSet.size() == 0) {
				chSet.add(getBottomElement());
			}
		}
	}

	protected void computeInconsistentDag(IntegerSubsumerGraph setS) {
		Set<Integer> elements = setS.getElements();
		reset(elements);
		for (Integer elem : elements) {
			this.equivalents.put(elem, elements);
		}
	}

	protected void dagClassify(Integer cA, Set<Integer> classified,
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
				this.equivalents.get(cA).add(cB);
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

	protected void dagInsert(Integer cA, Set<Integer> candidates) {
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

	public Integer getBottomElement() {
		return this.bottomElement;
	}

	@Override
	public Set<Integer> getChildren(Integer elem) {
		return Collections.unmodifiableSet(this.children.get(elem));
	}

	@Override
	public Set<Integer> getElements() {
		return Collections.unmodifiableSet(this.equivalents.keySet());
	}

	@Override
	public Set<Integer> getEquivalents(Integer elem) {
		return Collections.unmodifiableSet(this.equivalents.get(elem));
	}

	@Override
	public Set<Integer> getParents(Integer elem) {
		return Collections.unmodifiableSet(this.parents.get(elem));
	}

	public Integer getTopElement() {
		return this.topElement;
	}

	protected void reset(Set<Integer> elements) {
		this.children = new TreeMap<Integer, Set<Integer>>();
		this.parents = new TreeMap<Integer, Set<Integer>>();
		this.equivalents = new TreeMap<Integer, Set<Integer>>();

		for (Integer elem : elements) {
			this.children.put(elem, new TreeSet<Integer>());
			this.parents.put(elem, new TreeSet<Integer>());
			Set<Integer> equiv = new TreeSet<Integer>();
			equiv.add(elem);
			this.equivalents.put(elem, equiv);
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
		ret.append("\n");
		return ret.toString();
	}
}
