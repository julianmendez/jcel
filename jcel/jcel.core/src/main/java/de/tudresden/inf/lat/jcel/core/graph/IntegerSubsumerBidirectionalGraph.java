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
 * This interface models a subsumer graph where it is possible to get also the
 * subsumees for a particular vertex. There is one element that is a subsumer of
 * all the others which is called <i>top</i>. There is one element that is
 * subsumee of all the others which is called <i>bottom</i>.
 * 
 * @author Julian Mendez
 */
public interface IntegerSubsumerBidirectionalGraph extends IntegerSubsumerGraph {

	/**
	 * Returns the collection of subsumees of a given vertex.
	 * 
	 * @param vertex
	 *            to get the collection of subsumees
	 * @return the collection of subsumees of the given vertex
	 */
	public Collection<Integer> getSubsumees(int vertex);

}
