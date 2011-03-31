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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a binary relation. Its elements are integer numbers.
 * 
 * @author Julian Mendez
 */
public class IntegerBinaryRelationImpl implements IntegerBinaryRelation {

	private Map<Integer, Collection<Integer>> byFirstComp = null;
	private Map<Integer, Collection<Integer>> bySecondComp = null;

	/**
	 * Constructs an empty binary relation.
	 */
	public IntegerBinaryRelationImpl() {
		this.byFirstComp = new HashMap<Integer, Collection<Integer>>();
		this.bySecondComp = new HashMap<Integer, Collection<Integer>>();
	}

	/**
	 * Adds an element to this binary relation. Although there is not any pair
	 * associated to the particular element, the element is belongs to the set
	 * of elements.
	 * 
	 * @param elem
	 */
	public void add(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addTo(elem, this.byFirstComp);
		addTo(elem, this.bySecondComp);
	}

	/**
	 * Adds a pair to this binary relation.
	 * 
	 * @param first
	 * @param second
	 */
	public void add(Integer first, Integer second) {
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		add(first);
		add(second);
		this.byFirstComp.get(first).add(second);
		this.bySecondComp.get(second).add(first);
	}

	private void addTo(Integer elem, Map<Integer, Collection<Integer>> map) {
		Collection<Integer> connected = map.get(elem);
		if (connected == null) {
			connected = new EfficientArray();
			map.put(elem, connected);
		}
	}

	@Override
	public boolean contains(Integer first, Integer second) {
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> connected = this.byFirstComp.get(first);
		return (connected != null && connected.contains(second));
	}

	@Override
	public Collection<Integer> getByFirst(Integer first) {
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> ret = Collections.emptySet();
		Collection<Integer> set = this.byFirstComp.get(first);
		if (set != null) {
			ret = Collections.unmodifiableCollection(set);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getBySecond(Integer second) {
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> ret = Collections.emptySet();
		Collection<Integer> set = this.bySecondComp.get(second);
		if (set != null) {
			ret = Collections.unmodifiableCollection(set);
		}
		return ret;
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
		for (Integer key : this.byFirstComp.keySet()) {
			ret += this.byFirstComp.get(key).size();
		}
		for (Integer key : this.bySecondComp.keySet()) {
			ret += this.bySecondComp.get(key).size();
		}
		return ret;
	}

	@Override
	public Set<Integer> getElements() {
		return this.byFirstComp.keySet();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		Set<Integer> elements = getElements();
		sbuf.append("[");
		for (Integer firstComponent : elements) {
			Collection<Integer> connectedElem = getByFirst(firstComponent);
			for (Integer secondComponent : connectedElem) {
				sbuf.append(" (");
				sbuf.append(firstComponent);
				sbuf.append(",");
				sbuf.append(secondComponent);
				sbuf.append(")");
			}
		}
		sbuf.append(" ]");
		return sbuf.toString();
	}

}
