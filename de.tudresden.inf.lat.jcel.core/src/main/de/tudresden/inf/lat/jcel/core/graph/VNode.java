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

import java.util.Map;
import java.util.Set;

/**
 * Represents a pair containing a class and a set of expressions of the form
 * &exist; r <i>.</i> A . These expressions are called existential entries.
 * 
 * @author Julian Mendez
 */
public interface VNode extends Cloneable {

	/**
	 * Adds a new existential entry to the node.
	 * 
	 * @param objectPropertyId
	 *            object property identifier
	 * @param classId
	 *            class identifier
	 * @return <code>true</code> if and only if the set of existential entries
	 *         has changed
	 */
	public boolean addExistential(Integer objectPropertyId, Integer classId);

	/**
	 * Adds all the existential entries of another node to this one.
	 * 
	 * @param other
	 *            the other node
	 * @return <code>true</code> if and only if the set of existential entries
	 *         has changed
	 */
	public boolean addExistentialsOf(VNode other);

	/**
	 * Creates and returns a copy of this object. The copy must have the
	 * following properties:
	 * <ul>
	 * <li><code>x.clone() != x</code></li>
	 * <li><code>x.clone().getClass() == x.getClass()</code></li>
	 * <li><code>x.clone().equals(x)</code></li>
	 * </ul>
	 * 
	 * @return a copy of this object
	 */
	public VNode clone();

	/**
	 * Tells whether this node contains the given existential entry.
	 * 
	 * @param objectPropertyId
	 *            object property identifier
	 * @param classId
	 *            class identifier
	 * @return <code>true</code> if and only if the node contains the given
	 *         existential entry
	 */
	public boolean containsExistential(Integer objectPropertyId, Integer classId);

	/**
	 * Returns the class identifier for this node.
	 * 
	 * @return the class identifier for this node
	 */
	public Integer getClassId();

	/**
	 * Returns a map containing the existential entries. The keys are object
	 * property identifiers, and the sets are class identifiers related to them.
	 * 
	 * @return a map containing the existential entries
	 */
	public Map<Integer, Set<Integer>> getExistentialMap();

	/**
	 * Tell whether a node has an empty set of existential entries.
	 * 
	 * @return <code>true</code> if and only if this node contains an empty set
	 *         of existential entries
	 */
	public boolean isEmpty();

	/**
	 * Sets a new class identifier for this node.
	 * 
	 * @param classId
	 *            the new class identifier
	 */
	public void setClassId(Integer classId);

}
