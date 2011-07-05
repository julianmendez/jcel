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

package de.tudresden.inf.lat.jcel.owlapi.classifier;

import java.util.Set;

/**
 * This interface is for a connected directed graph where the vertices are
 * considered to be arranged in a hierarchical structure. Hence, each node can
 * have <i>ancestors</i> (superElements) and <i>descendants</i> (subElements).
 * Vertices in the same cycle are called <i>equivalents</i>.
 * 
 * @param <T>
 *            type of vertices.
 * 
 * @author Julian Mendez
 */
interface HierarchicalGraph<T> {

	/**
	 * Returns the set of vertices in the graph.
	 * 
	 * @return the set of vertices in the graph.
	 */
	public Set<T> getElements();

	/**
	 * Returns the vertices that are equivalent to a given one.
	 * 
	 * @param vertex
	 *            the vertex to get the equivalents.
	 * @return the equivalents.
	 */
	public Set<T> getEquivalents(T vertex);

	/**
	 * Returns the direct or indirect descendants of a particular vertex grouped
	 * in sets of equivalent vertices.
	 * 
	 * @param vertex
	 *            the vertex to get the descendants.
	 * @param direct
	 *            <code>true</code> to get only the direct descendants, or
	 *            <code>false</code> to get all of them
	 * @return the descendants grouped in sets of equivalent vertices.
	 */
	public Set<Set<T>> getSubElements(T vertex, boolean direct);

	/**
	 * Returns the direct or indirect ancestors of a particular vertex grouped
	 * in sets of equivalent vertices.
	 * 
	 * @param vertex
	 *            vertex to get the ancestors.
	 * @param direct
	 *            <code>true</code> to get only the direct ancestors, or
	 *            <code>false</code> to get all of them
	 * @return the ancestors of a particular vertex grouped in sets of
	 *         equivalent vertices.
	 */
	public Set<Set<T>> getSuperElements(T vertex, boolean direct);

}
