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

import de.tudresden.inf.lat.jcel.coreontology.common.OptMap;
import de.tudresden.inf.lat.jcel.coreontology.common.OptMapImpl;

/**
 * This class implements a map of binary relations.
 *
 * @author Julian Mendez
 */
public class IntegerRelationMapImpl implements IntegerRelationMap {

	private final OptMap<Integer, IntegerBinaryRelationImpl> relationMap = new OptMapImpl<>(new ConcurrentHashMap<>());
	private final OptMap<Integer, Collection<Integer>> relationSetByFirst = new OptMapImpl<>(new ConcurrentHashMap<>());
	private final OptMap<Integer, Collection<Integer>> relationSetBySecond = new OptMapImpl<>(
			new ConcurrentHashMap<>());

	/**
	 * Constructs an empty map of binary relations.
	 */
	public IntegerRelationMapImpl() {
	}

	/**
	 * Adds an empty binary relation.
	 *
	 * @param relationId
	 *            relation identifier
	 * @return <code>true</code> if and only if the relation identifier was
	 *         added
	 */
	public boolean add(int relationId) {
		boolean ret = false;
		if (!this.relationMap.containsKey(relationId)) {
			this.relationMap.put(relationId, new IntegerBinaryRelationImpl());
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds a pair to a binary relation.
	 *
	 * @param relationId
	 *            relation id
	 * @param first
	 *            first component
	 * @param second
	 *            second component
	 * @return <code>true</code> if and only if the pair was added
	 */
	public boolean add(int relationId, int first, int second) {
		boolean ret = false;
		Optional<IntegerBinaryRelationImpl> optRelation = this.relationMap.get(relationId);
		if (!optRelation.isPresent()) {
			optRelation = Optional.of(new IntegerBinaryRelationImpl());
			this.relationMap.put(relationId, optRelation.get());
			ret = true;
		}
		ret |= optRelation.get().add(first, second);

		Optional<Collection<Integer>> optByFirst = this.relationSetByFirst.get(first);
		if (!optByFirst.isPresent()) {
			optByFirst = Optional.of(new ArraySet());
			this.relationSetByFirst.put(first, optByFirst.get());
			ret = true;
		}
		ret |= optByFirst.get().add(relationId);

		Optional<Collection<Integer>> optBySecond = this.relationSetBySecond.get(second);
		if (!optBySecond.isPresent()) {
			optBySecond = Optional.of(new ArraySet());
			this.relationSetBySecond.put(second, optBySecond.get());
			ret = true;
		}
		ret |= optBySecond.get().add(relationId);

		return ret;
	}

	@Override
	public boolean contains(int relationId) {
		return this.relationMap.containsKey(relationId);
	}

	@Override
	public boolean contains(int relationId, int first, int second) {
		boolean ret = false;
		Optional<IntegerBinaryRelationImpl> optRelation = this.relationMap.get(relationId);
		if (optRelation.isPresent()) {
			ret = optRelation.get().contains(first, second);
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerRelationMap)) {
			IntegerRelationMap other = (IntegerRelationMap) o;
			ret = getElements().equals(other.getElements());

			ret = ret && getElements().stream().allMatch(elem -> get(elem).equals(other.get(elem)));
		}
		return ret;
	}

	@Override
	public IntegerBinaryRelation get(int relationId) {
		IntegerBinaryRelation ret = null;
		if (this.relationMap.get(relationId).isPresent()) {
			ret = this.relationMap.get(relationId).get();
		} else {
			ret = new IntegerBinaryRelationImpl();
		}
		return ret;
	}

	@Override
	public Collection<Integer> getByFirst(int relationId, int first) {
		Collection<Integer> ret = Collections.emptySet();
		Optional<IntegerBinaryRelationImpl> optRelation = this.relationMap.get(relationId);
		if (optRelation.isPresent()) {
			ret = optRelation.get().getByFirst(first);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getBySecond(int relationId, int second) {
		Collection<Integer> ret = Collections.emptySet();
		Optional<IntegerBinaryRelationImpl> optRelation = this.relationMap.get(relationId);
		if (optRelation.isPresent()) {
			ret = optRelation.get().getBySecond(second);
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

		ret += this.relationMap.keySet().stream() //
				.map(key -> this.relationMap.get(key).get().getDeepSize()) //
				.reduce(0L, (accum, elem) -> (accum + elem));

		ret += this.relationSetByFirst.keySet().stream() //
				.map(key -> this.relationSetByFirst.get(key).get().size()) //
				.reduce(0, (accum, elem) -> (accum + elem));

		ret += this.relationSetBySecond.keySet().stream() //
				.map(key -> this.relationSetBySecond.get(key).get().size()) //
				.reduce(0, (accum, elem) -> (accum + elem));

		return ret;
	}

	@Override
	public Set<Integer> getElements() {
		return this.relationMap.keySet();
	}

	@Override
	public Collection<Integer> getRelationsByFirst(int first) {
		Optional<Collection<Integer>> optSet = this.relationSetByFirst.get(first);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableCollection(optSet.get());
	}

	@Override
	public Collection<Integer> getRelationsBySecond(int second) {
		Optional<Collection<Integer>> optSet = this.relationSetBySecond.get(second);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableCollection(optSet.get());
	}

	@Override
	public int hashCode() {
		return this.relationMap.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		getElements().forEach(relationId -> {
			sbuf.append(relationId);
			sbuf.append(" ");
			sbuf.append(this.relationMap.get(relationId).toString());
			sbuf.append("\n");
		});
		return sbuf.toString();
	}

}
