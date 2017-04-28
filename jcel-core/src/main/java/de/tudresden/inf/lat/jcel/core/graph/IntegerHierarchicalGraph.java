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

import java.util.Set;

/**
 * This interface is implemented by classes modeling a directed graph
 * considering equivalent classes. Each vertex can have <i>children</i>,
 * <i>parents</i>, and <i>equivalents</i>. <br>
 * The children of a vertex are all those vertices that are direct successors.
 * <br>
 * The parents of a vertex are all those vertices that are direct predecessors.
 * <br>
 * If a vertex <i>v</i> belongs to a cycle <i>C</i>, all the vertices in
 * <i>C</i> are equivalents of <i>v</i>. If a vertex <i>v</i> does not belong to
 * a cycle, the equivalents of <i>v</i> is a singleton containing only <i>v</i>.
 * 
 * @author Julian Mendez
 */
public interface IntegerHierarchicalGraph {

	/**
	 * Returns all predecessors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all predecessors of the given vertex
	 */
	Set<Integer> getAncestors(Integer vertex);

	/**
	 * Returns the bottom element.
	 * 
	 * @return the bottom element
	 */
	Integer getBottomElement();

	/**
	 * Returns all direct successors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all direct successors of the given vertex
	 */
	Set<Integer> getChildren(Integer vertex);

	/**
	 * Returns all successors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all successors of the given vertex
	 */
	Set<Integer> getDescendants(Integer vertex);

	/**
	 * Returns all the vertices in the graph.
	 * 
	 * @return all the vertices in the graph
	 */
	Set<Integer> getElements();

	/**
	 * Returns all vertices that are in a cycle for a given vertex and the
	 * vertex itself.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all vertices that are in a cycle for the given vertex and the
	 *         vertex itself
	 */
	Set<Integer> getEquivalents(Integer vertex);

	/**
	 * Returns all direct predecessors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all direct predecessors of the given vertex
	 */
	Set<Integer> getParents(Integer vertex);

	/**
	 * Returns the top element.
	 * 
	 * @return the top element
	 */
	Integer getTopElement();

}
