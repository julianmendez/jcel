/*
 *
 * Copyright 2009-2013 Julian Mendez
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
	public boolean containsPair(int subsumee, int subsumer);

	/**
	 * Returns the bottom element.
	 * 
	 * @return the bottom element
	 */
	public int getBottomElement();

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
	public Collection<Integer> getSubsumers(int vertex);

	/**
	 * Returns the top element.
	 * 
	 * @return the top element
	 */
	public int getTopElement();

}
