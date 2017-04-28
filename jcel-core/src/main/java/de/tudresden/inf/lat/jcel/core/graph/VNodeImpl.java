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
import java.util.Objects;
import java.util.Set;

/**
 * This class implements a node which is a pair of a class and a set of
 * existential expressions.
 * 
 * @author Julian Mendez
 * 
 * @see VNode
 */
public class VNodeImpl implements VNode {

	private final int classIdentifier;
	private final Set<VNodeObjectSomeValuesFrom> existentialSet = new HashSet<>();

	/**
	 * Constructs a new node.
	 * 
	 * @param id
	 *            class identifier
	 */
	public VNodeImpl(int id) {
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
	public boolean addExistential(int propertyId, int classId) {
		boolean ret = this.existentialSet.add(new VNodeObjectSomeValuesFromImpl(propertyId, classId));
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
		Objects.requireNonNull(other);
		return this.existentialSet.addAll(other.getExistentialEntries());
	}

	@Override
	public boolean containsExistential(int propertyId, int classId) {
		return this.existentialSet.contains(new VNodeObjectSomeValuesFromImpl(propertyId, classId));
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof VNode)) {
			VNode other = (VNode) o;
			ret = (getClassId() == other.getClassId()) && getExistentialEntries().equals(other.getExistentialEntries());
		}
		return ret;
	}

	@Override
	public int getClassId() {
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
		return this.classIdentifier + (31 * this.existentialSet.hashCode());
	}

	@Override
	public boolean isEmpty() {
		return this.existentialSet.isEmpty();
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(this.classIdentifier);
		ret.append(" ");
		this.existentialSet.forEach(expression -> {
			ret.append(expression.toString());
			ret.append(", ");

		});
		ret.append(" ");
		return ret.toString();
	}

}
