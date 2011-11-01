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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class models a binary relation. Its elements are integer numbers.
 * 
 * @author Julian Mendez
 */
public class IntegerBinaryRelation {

	private Map<Integer, Set<Integer>> byFirstComp = null;
	private Map<Integer, Set<Integer>> bySecondComp = null;
	private Set<Integer> emptySet = Collections
			.unmodifiableSet(new HashSet<Integer>());

	public IntegerBinaryRelation() {
		this.byFirstComp = new HashMap<Integer, Set<Integer>>();
		this.bySecondComp = new HashMap<Integer, Set<Integer>>();
	}

	/**
	 * Adds an element to this binary relation. Although there is not any pair
	 * associated to the particular element, the element is belongs to the set
	 * of elements.
	 * 
	 * @param elem
	 */
	public void add(Integer elem) {
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
		add(first);
		add(second);
		this.byFirstComp.get(first).add(second);
		this.bySecondComp.get(second).add(first);
	}

	protected void addTo(Integer elem, Map<Integer, Set<Integer>> map) {
		Set<Integer> connected = map.get(elem);
		if (connected == null) {
			connected = new HashSet<Integer>();
			map.put(elem, connected);
		}
	}

	/**
	 * Tells whether a pair belongs to this binary relation.
	 * 
	 * @param first
	 *            first component
	 * @param second
	 *            second component
	 * @return true if and only if the pair (first, second) belongs to this
	 *         binary relation.
	 */
	public boolean contains(Integer first, Integer second) {
		Set<Integer> connected = this.byFirstComp.get(first);
		return (connected != null && connected.contains(second));
	}

	/**
	 * Returns the elements connecting from a particular element in the first
	 * component.
	 * 
	 * @param first
	 *            first component
	 * @return the set of elements connecting from a first component, or null if
	 *         the first component is not defined.
	 */
	public Set<Integer> getByFirst(Integer first) {
		Set<Integer> ret = this.emptySet;
		Set<Integer> set = this.byFirstComp.get(first);
		if (set != null) {
			ret = Collections.unmodifiableSet(set);
		}
		return ret;
	}

	/**
	 * Returns the elements connecting to a particular element in the second
	 * component.
	 * 
	 * @param second
	 *            second component
	 * @return the set of elements connecting to a second component, or
	 *         <tt>null</tt> if the second component is not defined
	 */
	public Set<Integer> getBySecond(Integer second) {
		Set<Integer> ret = this.emptySet;
		Set<Integer> set = this.bySecondComp.get(second);
		if (set != null) {
			ret = Collections.unmodifiableSet(set);
		}
		return ret;
	}

	/**
	 * @return the set of elements that has been added to this binary relation.
	 */
	public Set<Integer> getElements() {
		return this.byFirstComp.keySet();
	}

	/**
	 * The string representation of this binary relation.
	 */
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		Set<Integer> elements = getElements();
		sbuf.append("[");
		for (Integer firstComponent : elements) {
			Set<Integer> connectedElem = getByFirst(firstComponent);
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
