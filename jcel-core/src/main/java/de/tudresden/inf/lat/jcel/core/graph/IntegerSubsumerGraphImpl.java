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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements a subsumer graph. This implementation keeps a set of
 * subsumers for each vertex. It also considers bottom and its equivalents as
 * special cases.
 *
 * @author Julian Mendez
 */
public class IntegerSubsumerGraphImpl implements IntegerSubsumerGraph {

	private final int bottomElement;
	private final Collection<Integer> emptyCollection = Collections.unmodifiableCollection(new ArraySet());
	private final Set<Integer> equivToBottom = new HashSet<>();
	private final Map<Integer, Collection<Integer>> setS = new ConcurrentHashMap<>();
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
		this.setS.put(this.topElement, Collections.synchronizedCollection(new ArraySet()));
		this.equivToBottom.add(this.bottomElement);
	}

	/**
	 * Adds a new vertex to the graph.
	 *
	 * @param vertex
	 *            vertex
	 * @return <code>true</code> if and only if the vertex was added
	 */
	public boolean add(int vertex) {
		boolean ret = false;
		if (!this.setS.containsKey(vertex)) {
			this.setS.put(vertex, Collections.synchronizedCollection(new ArraySet()));
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
	 * @return <code>true</code> if and only if the relation was added
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
		return Objects.nonNull(getSubsumers(subsumee)) && getSubsumers(subsumee).contains(subsumer);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerSubsumerGraph)) {
			IntegerSubsumerGraph other = (IntegerSubsumerGraph) o;
			ret = (getBottomElement() == other.getBottomElement()) && (getTopElement() == other.getTopElement())
					&& getElements().equals(other.getElements());
			ret = ret && getElements().stream().allMatch(elem -> getSubsumers(elem).equals(other.getSubsumers(elem)));
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
		return this.setS.keySet().stream() //
				.map(key -> this.setS.get(key).size()) //
				.reduce(0, (accum, elem) -> (accum + elem));
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
			if (Objects.isNull(ret)) {
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
		Objects.requireNonNull(collection);
		Set<Integer> keySet = new HashSet<>();
		keySet.addAll(this.setS.keySet());
		keySet.forEach(key -> {
			if (collection.contains(key)) {
				Collection<Integer> value = Collections.synchronizedCollection(new ArraySet());
				getSubsumers(key).forEach(elem -> {
					if (collection.contains(elem)) {
						value.add(elem);
					}
				});
				this.setS.put(key, value);
			}
		});
		keySet.forEach(key -> {
			if (!collection.contains(key)) {
				this.setS.remove(key);
				if (this.equivToBottom.contains(key)) {
					this.equivToBottom.remove(key);
				}
			}
		});
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("equivalent to bottom: ");
		this.equivToBottom.forEach(elem -> ret.append(" " + elem));
		ret.append("\n");
		this.setS.keySet().forEach(id -> {
			Collection<Integer> related = getSubsumers(id);
			if (this.equivToBottom.contains(id)) {
				ret.append("* ");
			}
			ret.append(id);
			ret.append(" (");
			ret.append(related.size());
			ret.append(") : ");
			related.forEach(elem -> ret.append(elem + " "));
			ret.append("\n");
		});
		return ret.toString();
	}

}
