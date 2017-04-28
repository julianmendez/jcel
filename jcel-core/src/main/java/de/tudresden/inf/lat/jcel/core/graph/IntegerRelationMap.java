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
	 * @return <code>true</code> if and only if the binary relation belongs to
	 *         this map
	 */
	boolean contains(int relationId);

	/**
	 * Tells whether a binary relation contains a give pair.
	 *
	 * @param relationId
	 *            relation id
	 * @param first
	 *            first component
	 * @param second
	 *            second component
	 * @return <code>true</code> if and only if the pair belongs to the given
	 *         relation
	 */
	boolean contains(int relationId, int first, int second);

	/**
	 * @param relationId
	 *            relation id
	 * @return the binary relation with the given relation id
	 */
	IntegerBinaryRelation get(int relationId);

	/**
	 * Returns the elements connecting from a particular element in the first
	 * component.
	 *
	 * @param relation
	 *            relation id
	 * @param first
	 *            first component
	 * @return the collection of elements connecting from a first component, or
	 *         an empty set if the first component is not defined.
	 */
	Collection<Integer> getByFirst(int relation, int first);

	/**
	 * Returns the elements connecting to a particular element in the second
	 * component.
	 *
	 * @param relation
	 *            relation id
	 * @param second
	 *            second component
	 * @return the collection of elements connecting to a second component, or
	 *         an empty set if the second component is not defined
	 */
	Collection<Integer> getBySecond(int relation, int second);

	/**
	 * @return the set of binary relations that have been added to this set.
	 */
	Set<Integer> getElements();

	/**
	 * Returns the relations which use a particular element in the first
	 * component.
	 *
	 * @param first
	 *            first component
	 * @return the collection of relations which use a particular first
	 *         component, or an empty set if there are none.
	 */
	Collection<Integer> getRelationsByFirst(int first);

	/**
	 * Returns the relations which use a particular element in the second
	 * component.
	 *
	 * @param second
	 *            second component
	 * @return the collection of relations which use a particular second
	 *         component, or an empty set if there are none.
	 */
	Collection<Integer> getRelationsBySecond(int second);

}
