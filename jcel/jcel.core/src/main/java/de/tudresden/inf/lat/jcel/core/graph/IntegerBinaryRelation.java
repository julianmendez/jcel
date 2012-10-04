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
import java.util.Set;

/**
 * This interface models a binary relation. Its elements are integer numbers.
 * 
 * @author Julian Mendez
 */
public interface IntegerBinaryRelation {

	/**
	 * Tells whether a pair belongs to this binary relation.
	 * 
	 * @param first
	 *            first component
	 * @param second
	 *            second component
	 * @return true if and only if the pair (first, second) belongs to this
	 *         binary relation.
	 */
	public boolean contains(int first, int second);

	/**
	 * Returns the elements connecting from a particular element in the first
	 * component.
	 * 
	 * @param first
	 *            first component
	 * @return the collection of elements connecting from a first component, or
	 *         an empty set if the first component is not defined.
	 */
	public Collection<Integer> getByFirst(int first);

	/**
	 * Returns the elements connecting to a particular element in the second
	 * component.
	 * 
	 * @param second
	 *            second component
	 * @return the collection of elements connecting to a second component, or
	 *         an empty set if the second component is not defined
	 */
	public Collection<Integer> getBySecond(int second);

	/**
	 * @return the set of elements that have been added to this binary relation.
	 */
	public Set<Integer> getElements();

}
