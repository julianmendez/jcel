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
 * This class implements a node which is a pair of a class and a set of
 * existential entries.
 * 
 * @see VNode
 * 
 * @author Julian Mendez
 */
public class VNodeImpl implements VNode {

	private Integer classIdentifier = null;
	private Map<Integer, Set<Integer>> existentialMap = new HashMap<Integer, Set<Integer>>();

	public VNodeImpl(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classIdentifier = id;
	}

	public VNodeImpl(VNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classIdentifier = node.getClassId();
		this.existentialMap = node.getExistentialMap();
	}

	@Override
	public boolean addExistential(Integer propertyId, Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> set = this.existentialMap.get(classId);
		if (set == null) {
			set = new HashSet<Integer>();
			this.existentialMap.put(classId, set);
		}
		boolean ret = set.add(propertyId);
		this.existentialMap.put(classId, set);
		return ret;
	}

	@Override
	public boolean addExistentialsOf(VNode other) {
		if (other == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		for (Integer id : other.getExistentialMap().keySet()) {
			for (Integer relation : other.getExistentialMap().get(id)) {
				boolean changed = addExistential(id, relation);
				ret = ret || changed;
			}
		}
		return ret;
	}

	@Override
	public VNodeImpl clone() {
		VNodeImpl ret = new VNodeImpl(this.classIdentifier);
		ret.existentialMap = new HashMap<Integer, Set<Integer>>();
		for (Integer key : this.existentialMap.keySet()) {
			Set<Integer> value = new HashSet<Integer>();
			value.addAll(this.existentialMap.get(key));
			ret.existentialMap.put(key, value);
		}
		return ret;
	}

	@Override
	public boolean containsExistential(Integer propertyId, Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		Set<Integer> set = this.existentialMap.get(classId);
		if (set != null) {
			ret = set.contains(propertyId);
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof VNodeImpl) {
			VNodeImpl other = (VNodeImpl) o;
			ret = this.classIdentifier.equals(other.classIdentifier)
					&& this.existentialMap.equals(other.existentialMap);
		}
		return ret;
	}

	@Override
	public Integer getClassId() {
		return this.classIdentifier;
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
		for (Integer key : this.existentialMap.keySet()) {
			ret += this.existentialMap.get(key).size();
		}
		return ret;
	}

	@Override
	public Map<Integer, Set<Integer>> getExistentialMap() {
		return Collections.unmodifiableMap(this.existentialMap);
	}

	@Override
	public int hashCode() {
		return this.classIdentifier + 31 * this.existentialMap.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return this.existentialMap.isEmpty();
	}

	@Override
	public void setClassId(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classIdentifier = classId;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(this.classIdentifier);
		ret.append(" ");
		for (Integer id : this.existentialMap.keySet()) {
			for (Integer relation : this.existentialMap.get(id)) {
				ret.append("exists ");
				ret.append(relation);
				ret.append("-");
				ret.append(".");
				ret.append(id);
				ret.append(", ");
			}
		}
		ret.append(" ");
		return ret.toString();
	}
}
