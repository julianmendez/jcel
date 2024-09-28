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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * This class implements a binary relation. Its elements are integer numbers.
 *
 * @author Julian Mendez
 */
public class IntegerBinaryRelationImpl implements IntegerBinaryRelation {

	private final OptMap<Integer, Collection<Integer>> byFirstComp = new OptMapImpl<>(new ConcurrentHashMap<>());
	private final OptMap<Integer, Collection<Integer>> bySecondComp = new OptMapImpl<>(new ConcurrentHashMap<>());

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
	 *            element
	 * @return <code>true</code> if and only if the element was added
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
	 *            first element
	 * @param second
	 *            second element
	 * @return <code>true</code> if and only if the pair was added
	 */
	public boolean add(int first, int second) {
		boolean ret = false;

		ret |= add(first);
		ret |= add(second);

		Optional<Collection<Integer>> optByFirst = this.byFirstComp.get(first);
		if (!optByFirst.isPresent()) {
			throw new IllegalStateException("Element is not present in the relation: '" + first + "'.");
		}

		Optional<Collection<Integer>> optBySecond = this.bySecondComp.get(second);
		if (!optBySecond.isPresent()) {
			throw new IllegalStateException("Element is not present in the relation: '" + second + "'.");
		}

		boolean found = false;
		if (optByFirst.get().size() < optBySecond.get().size()) {
			found = optByFirst.get().contains(second);
		} else {
			found = optBySecond.get().contains(first);
		}

		if (!found) {
			ret |= optByFirst.get().add(second);
			ret |= optBySecond.get().add(first);
		}

		return ret;
	}

	private boolean addTo(int elem, OptMap<Integer, Collection<Integer>> map) {
		boolean ret = false;
		if (!map.get(elem).isPresent()) {
			map.put(elem, new ArraySet());
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean contains(int first, int second) {
		boolean ret = false;
		Optional<Collection<Integer>> byFirst = this.byFirstComp.get(first);
		ret = (byFirst.isPresent()) && byFirst.get().contains(second);
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerBinaryRelation)) {
			IntegerBinaryRelation other = (IntegerBinaryRelation) o;
			ret = getElements().equals(other.getElements());

			ret = ret && getElements().stream().allMatch(elem -> getByFirst(elem).equals(other.getByFirst(elem)));
		}
		return ret;
	}

	@Override
	public Collection<Integer> getByFirst(int first) {
		Collection<Integer> ret = Collections.emptySet();
		Optional<Collection<Integer>> optSet = this.byFirstComp.get(first);
		if (optSet.isPresent()) {
			ret = Collections.unmodifiableCollection(optSet.get());
		}
		return ret;
	}

	@Override
	public Collection<Integer> getBySecond(int second) {
		Collection<Integer> ret = Collections.emptySet();
		Optional<Collection<Integer>> optSet = this.bySecondComp.get(second);
		if (optSet.isPresent()) {
			ret = Collections.unmodifiableCollection(optSet.get());
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

		ret += this.byFirstComp.keySet().stream() //
				.map(key -> this.byFirstComp.get(key).get().size()) //
				.reduce(0, (accum, elem) -> (accum + elem));

		ret += this.bySecondComp.keySet().stream() //
				.map(key -> this.bySecondComp.get(key).get().size()) //
				.reduce(0, (accum, elem) -> (accum + elem));

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
		elements.forEach(firstComponent -> {
			Collection<Integer> connectedElem = getByFirst(firstComponent);
			connectedElem.forEach(secondComponent -> {
				sbuf.append(" (");
				sbuf.append(firstComponent);
				sbuf.append(",");
				sbuf.append(secondComponent);
				sbuf.append(")");
			});
		});
		sbuf.append(" ]");
		return sbuf.toString();
	}

}
