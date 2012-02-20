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
 * This interface models a subsumer graph. Each vertex has at least a subsumer.
 * There is one element that is a subsumer of all the others which is called
 * <i>top</i>. There is one element that is subsumee of all the others which is
 * called <i>bottom</i>.
 * 
 * @author Julian Mendez
 */
public interface IntegerSubsumerGraph {

	/**
	 * Tells whether this subsumer graph contains the given pair.
	 * 
	 * @param subsumee
	 *            subsumee
	 * @param subsumer
	 *            subsumer
	 * @return <code>true</code> if and only if this subsumer graph contains the
	 *         given pair
	 */
	public boolean containsPair(Integer subsumee, Integer subsumer);

	/**
	 * Returns the bottom element.
	 * 
	 * @return the bottom element
	 */
	public Integer getBottomElement();

	/**
	 * Returns the collection of vertices in the graph.
	 * 
	 * @return the collection of vertices in the graph
	 */
	public Collection<Integer> getElements();

	/**
	 * Returns the collection of subsumers of a given vertex.
	 * 
	 * @param vertex
	 *            to get the collection of subsumers
	 * @return the collection of subsumers of the given vertex
	 */
	public Collection<Integer> getSubsumers(Integer vertex);

	/**
	 * Returns the top element.
	 * 
	 * @return the top element
	 */
	public Integer getTopElement();

}
