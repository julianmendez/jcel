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
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements a node which is a pair of a class and a set of
 * existential expressions.
 * 
 * @see VNode
 * 
 * @author Julian Mendez
 */
public class VNodeImpl implements VNode {

	private Integer classIdentifier = null;
	private Set<VNodeObjectSomeValuesFrom> existentialSet = new HashSet<VNodeObjectSomeValuesFrom>();

	/**
	 * Constructs a new node.
	 * 
	 * @param id
	 *            class identifier
	 */
	public VNodeImpl(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classIdentifier = id;
	}

	/**
	 * Adds an existential pair to this node.
	 * 
	 * @param propertyId
	 *            object property identifier in the pair
	 * @param classId
	 *            class identifier in the pair
	 * @return <code>true</code> if and only if the node did not already contain
	 *         the specified pair
	 */
	public boolean addExistential(Integer propertyId, Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = this.existentialSet
				.add(new VNodeObjectSomeValuesFromImpl(propertyId, classId));
		return ret;
	}

	/**
	 * Adds the existential pairs of another node.
	 * 
	 * @param other
	 *            a node
	 * @return <code>true</code> if and only if set of pairs was changed
	 */
	public boolean addExistentialsOf(VNode other) {
		if (other == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.existentialSet.addAll(other.getExistentialEntries());
	}

	@Override
	public boolean containsExistential(Integer propertyId, Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.existentialSet.contains(new VNodeObjectSomeValuesFromImpl(
				propertyId, classId));
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof VNodeImpl) {
			VNodeImpl other = (VNodeImpl) o;
			ret = this.classIdentifier.equals(other.classIdentifier)
					&& this.existentialSet.equals(other.existentialSet);
		}
		return ret;
	}

	@Override
	public Integer getClassId() {
		return this.classIdentifier;
	}

	/**
	 * Returns the number of elements in the set of entries.
	 * 
	 * @return the number of elements in the set of entries
	 */
	public long getDeepSize() {
		return this.existentialSet.size();
	}

	@Override
	public Collection<VNodeObjectSomeValuesFrom> getExistentialEntries() {
		return Collections.unmodifiableSet(this.existentialSet);
	}

	@Override
	public int hashCode() {
		return this.classIdentifier + 31 * this.existentialSet.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return this.existentialSet.isEmpty();
	}

	/**
	 * Sets the class identifier.
	 * 
	 * @param classId
	 *            class identifier
	 */
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
		for (VNodeObjectSomeValuesFrom expression : this.existentialSet) {
			ret.append(expression.toString());
			ret.append(", ");

		}
		ret.append(" ");
		return ret.toString();
	}

}
