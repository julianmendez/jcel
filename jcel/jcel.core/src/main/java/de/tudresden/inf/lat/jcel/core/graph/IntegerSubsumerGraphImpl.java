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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a subsumer graph. This implementation keeps a set of
 * subsumers for each vertex. It also considers bottom and its equivalents as
 * special cases.
 * 
 * @author Julian Mendez
 */
public class IntegerSubsumerGraphImpl implements IntegerSubsumerGraph {

	private Integer bottomElement = 0;
	private Collection<Integer> emptyCollection = Collections
			.unmodifiableCollection(new EfficientArray());
	private Set<Integer> equivToBottom = new HashSet<Integer>();
	private Map<Integer, Collection<Integer>> setS = new HashMap<Integer, Collection<Integer>>();
	private Integer topElement = 1;

	/**
	 * Constructs an empty subsumer graph.
	 * 
	 * @param bottom
	 *            bottom class identifier
	 * @param top
	 *            top class identifier
	 */
	public IntegerSubsumerGraphImpl(Integer bottom, Integer top) {
		if (bottom == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (top == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.bottomElement = bottom;
		this.topElement = top;
		this.setS.put(this.bottomElement, this.emptyCollection);
		this.setS.put(this.topElement, new EfficientArray());
		this.equivToBottom.add(this.bottomElement);
	}

	/**
	 * Adds a new vertex to the graph.
	 * 
	 * @param vertex
	 */
	public void add(Integer vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (!this.setS.containsKey(vertex)) {
			Collection<Integer> aux = new EfficientArray();
			aux.add(vertex);
			this.setS.put(vertex, aux);
		}
	}

	/**
	 * Defines that one vertex is a subsumee of another vertex. If one or both
	 * elements do not belong to the vertex collection, they are added anyway.
	 * 
	 * @param subId
	 *            the subsumee
	 * @param superId
	 *            the subsumer
	 */
	public void addAncestor(Integer subId, Integer superId) {
		if (subId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (!this.equivToBottom.contains(subId)) {
			if (this.bottomElement.equals(superId)) {
				this.equivToBottom.add(subId);
				this.setS.put(subId, this.emptyCollection);
			} else {
				add(subId);
				add(superId);
				this.setS.get(subId).add(superId);
			}
		}
	}

	@Override
	public Integer getBottomElement() {
		return this.bottomElement;
	}

	/**
	 * Returns the number of elements in the internal maps that are referred by
	 * the keys, without counting the keys themselves. This method recalculates
	 * the value every time it is called.
	 * 
	 * @return the number of elements in the internal maps that are referred by
	 *         the keys, without counting the keys themselves
	 */
	public long getDeepSize() {
		long ret = 0;
		for (Integer key : this.setS.keySet()) {
			ret += this.setS.get(key).size();
		}
		return ret;
	}

	@Override
	public Collection<Integer> getElements() {
		return Collections.unmodifiableSet(this.setS.keySet());
	}

	@Override
	public Collection<Integer> getSubsumers(Integer vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> ret;
		if (this.equivToBottom.contains(vertex)) {
			ret = getElements();
		} else {
			ret = this.setS.get(vertex);
			if (ret == null) {
				ret = Collections.emptySet();
			} else {
				ret = Collections.unmodifiableCollection(ret);
			}
		}
		return ret;
	}

	@Override
	public Integer getTopElement() {
		return this.topElement;
	}

	/**
	 * Retains only the elements contained in the collection.
	 * 
	 * @param collection
	 *            collection of elements to be retained
	 */
	public void retainAll(Collection<Integer> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> keySet = new HashSet<Integer>();
		keySet.addAll(this.setS.keySet());
		for (Integer key : keySet) {
			if (collection.contains(key)) {
				Collection<Integer> value = new ArrayList<Integer>();
				for (Integer elem : getSubsumers(key)) {
					if (collection.contains(elem)) {
						value.add(elem);
					}
				}
				this.setS.put(key, value);
			}
		}
		for (Integer key : keySet) {
			if (!collection.contains(key)) {
				this.setS.remove(key);
				if (this.equivToBottom.contains(key)) {
					this.equivToBottom.remove(key);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("equivalent to bottom: ");
		for (Integer elem : this.equivToBottom) {
			ret.append(" " + elem);
		}
		ret.append("\n");
		for (Integer id : this.setS.keySet()) {
			Collection<Integer> related = getSubsumers(id);
			if (this.equivToBottom.contains(id)) {
				ret.append("* ");
			}
			ret.append(id);
			ret.append(" (");
			ret.append(related.size());
			ret.append(") : ");
			for (Integer elem : related) {
				ret.append(elem + " ");
			}
			ret.append("\n");
		}
		return ret.toString();
	}

}
