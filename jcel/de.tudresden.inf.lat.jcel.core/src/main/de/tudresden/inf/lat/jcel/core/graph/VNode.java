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

/**
 * Represents a pair containing a class and a set of expressions of the form
 * &exist; r <i>.</i> A . These expressions are called existential entries.
 * 
 * @author Julian Mendez
 */
public interface VNode {

	/**
	 * Tells whether this node contains the given existential expression.
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
	 * Returns a collection containing the existential expressions.
	 * 
	 * @return a collection containing the existential expressions
	 */
	public Collection<VNodeObjectSomeValuesFrom> getExistentialEntries();

	/**
	 * Tell whether a node has an empty set of existential expressions.
	 * 
	 * @return <code>true</code> if and only if this node contains an empty set
	 *         of existential expressions
	 */
	public boolean isEmpty();

}
