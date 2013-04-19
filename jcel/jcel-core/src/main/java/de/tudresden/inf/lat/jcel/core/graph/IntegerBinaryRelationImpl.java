/*
 *
 * Copyright 2009-2013 Julian Mendez
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements a binary relation. Its elements are integer numbers.
 * 
 * @author Julian Mendez
 */
public class IntegerBinaryRelationImpl implements IntegerBinaryRelation {

	private Map<Integer, Collection<Integer>> byFirstComp = new ConcurrentHashMap<Integer, Collection<Integer>>();
	private Map<Integer, Collection<Integer>> bySecondComp = new ConcurrentHashMap<Integer, Collection<Integer>>();

	/**
	 * Constructs an empty binary relation.
	 */
	public IntegerBinaryRelationImpl() {
	}

	/**
	 * Adds an element to this binary relation. Although there is not any pair
	 * associated to the particular element, the element is belongs to the set
	 * of elements.
	 * 
	 * @param elem
	 */
	public boolean add(int elem) {
		boolean ret = false;
		ret |= addTo(elem, this.byFirstComp);
		ret |= addTo(elem, this.bySecondComp);
		return ret;
	}

	/**
	 * Adds a pair to this binary relation.
	 * 
	 * @param first
	 * @param second
	 */
	public boolean add(int first, int second) {
		boolean ret = false;

		ret |= add(first);
		ret |= add(second);

		Collection<Integer> byFirst = this.byFirstComp.get(first);
		Collection<Integer> bySecond = this.bySecondComp.get(second);

		boolean found = false;
		if (byFirst.size() < bySecond.size()) {
			found = byFirst.contains(second);
		} else {
			found = bySecond.contains(first);
		}

		if (!found) {
			ret |= byFirst.add(second);
			ret |= bySecond.add(first);
		}

		return ret;
	}

	private boolean addTo(int elem, Map<Integer, Collection<Integer>> map) {
		boolean ret = false;
		if (map.get(elem) == null) {
			map.put(elem, new ArraySet());
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean contains(int first, int second) {
		boolean ret = false;
		Collection<Integer> byFirst = this.byFirstComp.get(first);
		ret = (byFirst != null) && byFirst.contains(second);
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerBinaryRelation) {
			IntegerBinaryRelation other = (IntegerBinaryRelation) o;
			ret = getElements().equals(other.getElements());
			for (Iterator<Integer> it = getElements().iterator(); ret
					&& it.hasNext();) {
				Integer elem = it.next();
				ret = ret && getByFirst(elem).equals(other.getByFirst(elem));
			}
		}
		return ret;
	}

	@Override
	public Collection<Integer> getByFirst(int first) {
		Collection<Integer> ret = Collections.emptySet();
		Collection<Integer> set = this.byFirstComp.get(first);
		if (set != null) {
			ret = Collections.unmodifiableCollection(set);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getBySecond(int second) {
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
	public int hashCode() {
		return this.byFirstComp.hashCode();
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
