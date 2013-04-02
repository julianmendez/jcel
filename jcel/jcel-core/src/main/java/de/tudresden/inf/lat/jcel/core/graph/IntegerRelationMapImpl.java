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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a map of binary relations.
 * 
 * @author Julian Mendez
 */
public class IntegerRelationMapImpl implements IntegerRelationMap {

	private Map<Integer, IntegerBinaryRelationImpl> relationMap = new HashMap<Integer, IntegerBinaryRelationImpl>();
	private Map<Integer, Collection<Integer>> relationSetByFirst = new HashMap<Integer, Collection<Integer>>();
	private Map<Integer, Collection<Integer>> relationSetBySecond = new HashMap<Integer, Collection<Integer>>();

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
	 */
	public boolean add(int relationId, int first, int second) {
		boolean ret = false;
		IntegerBinaryRelationImpl relation = this.relationMap.get(relationId);
		if (relation == null) {
			relation = new IntegerBinaryRelationImpl();
			this.relationMap.put(relationId, relation);
			ret = true;
		}
		ret |= relation.add(first, second);

		Collection<Integer> byFirst = this.relationSetByFirst.get(first);
		if (byFirst == null) {
			byFirst = new HashSet<Integer>();
			this.relationSetByFirst.put(first, byFirst);
			ret = true;
		}
		ret |= byFirst.add(relationId);

		Collection<Integer> bySecond = this.relationSetBySecond.get(second);
		if (bySecond == null) {
			bySecond = new HashSet<Integer>();
			this.relationSetBySecond.put(second, bySecond);
			ret = true;
		}
		ret |= bySecond.add(relationId);

		return ret;
	}

	@Override
	public boolean contains(int relationId) {
		return this.relationMap.containsKey(relationId);
	}

	@Override
	public boolean contains(int relationId, int first, int second) {
		boolean ret = false;
		IntegerBinaryRelation relation = this.relationMap.get(relationId);
		if (relation != null) {
			ret = relation.contains(first, second);
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerRelationMap) {
			IntegerRelationMap other = (IntegerRelationMap) o;
			ret = getElements().equals(other.getElements());
			for (Iterator<Integer> it = getElements().iterator(); ret
					&& it.hasNext();) {
				Integer elem = it.next();
				ret = ret && get(elem).equals(other.get(elem));
			}
		}
		return ret;
	}

	@Override
	public IntegerBinaryRelation get(int relationId) {
		return this.relationMap.get(relationId);
	}

	@Override
	public Collection<Integer> getByFirst(int relationId, int first) {
		Collection<Integer> ret = Collections.emptySet();
		IntegerBinaryRelation relation = this.relationMap.get(relationId);
		if (relation != null) {
			ret = relation.getByFirst(first);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getBySecond(int relationId, int second) {
		Collection<Integer> ret = Collections.emptySet();
		IntegerBinaryRelation relation = this.relationMap.get(relationId);
		if (relation != null) {
			ret = relation.getBySecond(second);
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
		for (int key : this.relationMap.keySet()) {
			ret += this.relationMap.get(key).getDeepSize();
		}
		for (int key : this.relationSetByFirst.keySet()) {
			ret += this.relationSetByFirst.get(key).size();
		}
		for (int key : this.relationSetBySecond.keySet()) {
			ret += this.relationSetBySecond.get(key).size();
		}
		return ret;
	}

	@Override
	public Set<Integer> getElements() {
		return this.relationMap.keySet();
	}

	@Override
	public Collection<Integer> getRelationsByFirst(int first) {
		Collection<Integer> ret = this.relationSetByFirst.get(first);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableCollection(ret);
	}

	@Override
	public Collection<Integer> getRelationsBySecond(int second) {
		Collection<Integer> ret = this.relationSetBySecond.get(second);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableCollection(ret);
	}

	@Override
	public int hashCode() {
		return this.relationMap.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		for (Integer relationId : getElements()) {
			sbuf.append(relationId);
			sbuf.append(" ");
			sbuf.append(this.relationMap.get(relationId).toString());
			sbuf.append("\n");
		}
		return sbuf.toString();
	}

}
