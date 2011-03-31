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
 * This interface models a map of binary relations.
 * 
 * @author Julian Mendez
 */
public interface IntegerRelationMap {

	/**
	 * Tells whether a binary relation belongs to this map.
	 * 
	 * @param relationId
	 *            relation id
	 * @return true if and only if the binary relation belongs to this map
	 */
	public boolean contains(Integer relationId);

	/**
	 * Tells whether a binary relation contains a give pair.
	 * 
	 * @param relationId
	 *            relation id
	 * @param first
	 *            first component
	 * @param second
	 *            second component
	 * @return true if and only if the pair belongs to the given relation
	 */
	public boolean contains(Integer relationId, Integer first, Integer second);

	/**
	 * @param relationId
	 *            relation id
	 * @return the binary relation with the given relation id
	 */
	public IntegerBinaryRelation get(Integer relationId);

	/**
	 * Returns the elements connecting from a particular element in the first
	 * component.
	 * 
	 * @param first
	 *            first component
	 * @return the collection of elements connecting from a first component, or
	 *         an empty set if the first component is not defined.
	 */
	public Collection<Integer> getByFirst(Integer relation, Integer first);

	/**
	 * Returns the elements connecting to a particular element in the second
	 * component.
	 * 
	 * @param second
	 *            second component
	 * @return the collection of elements connecting to a second component, or
	 *         an empty set if the second component is not defined
	 */
	public Collection<Integer> getBySecond(Integer relation, Integer second);

	/**
	 * @return the set of binary relations that have been added to this set.
	 */
	public Set<Integer> getElements();

	/**
	 * Returns the relations which use a particular element in the first
	 * component.
	 * 
	 * @param first
	 *            first component
	 * @return the collection of relations which use a particular first
	 *         component, or an empty set if there are none.
	 */
	public Collection<Integer> getRelationsByFirst(Integer first);

	/**
	 * Returns the relations which use a particular element in the second
	 * component.
	 * 
	 * @param second
	 *            second component
	 * @return the collection of relations which use a particular second
	 *         component, or an empty set if there are none.
	 */
	public Collection<Integer> getRelationsBySecond(Integer second);

}
