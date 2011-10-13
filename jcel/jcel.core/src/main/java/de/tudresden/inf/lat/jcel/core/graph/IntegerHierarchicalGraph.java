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

import java.util.Set;

/**
 * This interface is implemented by classes modeling a directed graph
 * considering equivalent classes. Each vertex can have <i>children</i>,
 * <i>parents</i>, and <i>equivalents</i>. <br/>
 * The children of a vertex are all those vertices that are direct successors. <br/>
 * The parents of a vertex are all those vertices that are direct predecessors. <br/>
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
	public Set<Integer> getAncestors(Integer vertex);

	/**
	 * Returns the bottom element.
	 * 
	 * @return the bottom element
	 */
	public Integer getBottomElement();

	/**
	 * Returns all direct successors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all direct successors of the given vertex
	 */
	public Set<Integer> getChildren(Integer vertex);

	/**
	 * Returns all successors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all successors of the given vertex
	 */
	public Set<Integer> getDescendants(Integer vertex);

	/**
	 * Returns all the vertices in the graph.
	 * 
	 * @return all the vertices in the graph
	 */
	public Set<Integer> getElements();

	/**
	 * Returns all vertices that are in a cycle for a given vertex and the
	 * vertex itself.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all vertices that are in a cycle for the given vertex and the
	 *         vertex itself
	 */
	public Set<Integer> getEquivalents(Integer vertex);

	/**
	 * Returns all direct predecessors of a particular given vertex.
	 * 
	 * @param vertex
	 *            starting vertex
	 * @return all direct predecessors of the given vertex
	 */
	public Set<Integer> getParents(Integer vertex);

	/**
	 * Returns the top element.
	 * 
	 * @return the top element
	 */
	public Integer getTopElement();

}
