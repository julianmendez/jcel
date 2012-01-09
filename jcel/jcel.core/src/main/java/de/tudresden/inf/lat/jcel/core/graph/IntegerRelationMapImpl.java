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
import java.util.HashSet;
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
	public void add(Integer relationId) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (!this.relationMap.containsKey(relationId)) {
			this.relationMap.put(relationId, new IntegerBinaryRelationImpl());
		}
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
	public void add(Integer relationId, Integer first, Integer second) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerBinaryRelationImpl relation = this.relationMap.get(relationId);
		if (relation == null) {
			relation = new IntegerBinaryRelationImpl();
			this.relationMap.put(relationId, relation);
		}
		relation.add(first, second);

		Collection<Integer> byFirst = this.relationSetByFirst.get(first);
		if (byFirst == null) {
			byFirst = new HashSet<Integer>();
			this.relationSetByFirst.put(first, byFirst);
		}
		byFirst.add(relationId);

		Collection<Integer> bySecond = this.relationSetBySecond.get(second);
		if (bySecond == null) {
			bySecond = new HashSet<Integer>();
			this.relationSetBySecond.put(second, bySecond);
		}
		bySecond.add(relationId);
	}

	@Override
	public boolean contains(Integer relationId) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationMap.containsKey(relationId);
	}

	@Override
	public boolean contains(Integer relationId, Integer first, Integer second) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		IntegerBinaryRelation relation = this.relationMap.get(relationId);
		if (relation != null) {
			ret = relation.contains(first, second);
		}
		return ret;
	}

	@Override
	public IntegerBinaryRelation get(Integer relationId) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerBinaryRelation ret = this.relationMap.get(relationId);
		return ret;
	}

	@Override
	public Collection<Integer> getByFirst(Integer relationId, Integer first) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> ret = Collections.emptySet();
		IntegerBinaryRelation relation = this.relationMap.get(relationId);
		if (relation != null) {
			ret = relation.getByFirst(first);
		}
		return ret;
	}

	@Override
	public Collection<Integer> getBySecond(Integer relationId, Integer second) {
		if (relationId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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
		for (Integer key : this.relationMap.keySet()) {
			ret += this.relationMap.get(key).getDeepSize();
		}
		for (Integer key : this.relationSetByFirst.keySet()) {
			ret += this.relationSetByFirst.get(key).size();
		}
		for (Integer key : this.relationSetBySecond.keySet()) {
			ret += this.relationSetBySecond.get(key).size();
		}
		return ret;
	}

	@Override
	public Set<Integer> getElements() {
		return this.relationMap.keySet();
	}

	@Override
	public Collection<Integer> getRelationsByFirst(Integer first) {
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> ret = this.relationSetByFirst.get(first);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableCollection(ret);
	}

	@Override
	public Collection<Integer> getRelationsBySecond(Integer second) {
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Collection<Integer> ret = this.relationSetBySecond.get(second);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableCollection(ret);
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
