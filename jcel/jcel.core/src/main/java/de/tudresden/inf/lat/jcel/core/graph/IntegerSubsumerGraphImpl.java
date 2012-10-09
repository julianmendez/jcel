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
import java.util.Iterator;
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

	private final int bottomElement;
	private Collection<Integer> emptyCollection = Collections
			.unmodifiableCollection(new ArraySet());
	private Set<Integer> equivToBottom = new HashSet<Integer>();
	private Map<Integer, Collection<Integer>> setS = new HashMap<Integer, Collection<Integer>>();
	private final int topElement;

	/**
	 * Constructs an empty subsumer graph.
	 * 
	 * @param bottom
	 *            bottom class identifier
	 * @param top
	 *            top class identifier
	 */
	public IntegerSubsumerGraphImpl(int bottom, int top) {
		this.bottomElement = bottom;
		this.topElement = top;
		this.setS.put(this.bottomElement, this.emptyCollection);
		this.setS.put(this.topElement, new ArraySet());
		this.equivToBottom.add(this.bottomElement);
	}

	/**
	 * Adds a new vertex to the graph.
	 * 
	 * @param vertex
	 */
	public boolean add(int vertex) {
		boolean ret = false;
		if (!this.setS.containsKey(vertex)) {
			this.setS.put(vertex, new ArraySet());
			ret = true;
		}
		return ret;
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
	public boolean addAncestor(int subId, int superId) {
		boolean ret = false;
		if (!this.equivToBottom.contains(subId)) {
			if (this.bottomElement == superId) {
				ret |= this.equivToBottom.add(subId);
				this.setS.put(subId, this.emptyCollection);
			} else {
				ret |= add(subId);
				ret |= add(superId);
				Collection<Integer> set = this.setS.get(subId);
				if (!set.contains(superId)) {
					ret |= set.add(superId);
				}
			}
		}
		return ret;
	}

	@Override
	public boolean containsPair(int subsumee, int subsumer) {
		return getSubsumers(subsumee) != null
				&& getSubsumers(subsumee).contains(subsumer);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerSubsumerGraph) {
			IntegerSubsumerGraph other = (IntegerSubsumerGraph) o;
			ret = (getBottomElement() == other.getBottomElement())
					&& (getTopElement() == other.getTopElement())
					&& getElements().equals(other.getElements());
			for (Iterator<Integer> it = getElements().iterator(); ret
					&& it.hasNext();) {
				Integer elem = it.next();
				ret = ret
						&& getSubsumers(elem).equals(other.getSubsumers(elem));
			}
		}
		return ret;
	}

	@Override
	public int getBottomElement() {
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
	public Collection<Integer> getSubsumers(int vertex) {
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
	public int getTopElement() {
		return this.topElement;
	}

	@Override
	public int hashCode() {
		return this.setS.hashCode();
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