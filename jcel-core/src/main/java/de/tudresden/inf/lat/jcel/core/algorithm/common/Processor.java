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

package de.tudresden.inf.lat.jcel.core.algorithm.common;

import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;

/**
 * An object implementing this interface can classify a set of axioms. After the
 * classification is finished, it is possible to get the resulting class
 * hierarchy and object property hierarchy.
 * 
 * @author Julian Mendez
 */
public interface Processor {

	/**
	 * Returns the class hierarchy after the classification has finished.
	 * 
	 * @return the class hierarchy if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	IntegerHierarchicalGraph getClassHierarchy() throws UnclassifiedOntologyException;

	/**
	 * Returns the data property hierarchy after the classification has
	 * finished.
	 * 
	 * @return the data property hierarchy if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	IntegerHierarchicalGraph getDataPropertyHierarchy() throws UnclassifiedOntologyException;

	/**
	 * Returns the map of direct types for every individual.
	 * 
	 * @return the the map of direct types if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	Map<Integer, Set<Integer>> getDirectTypes() throws UnclassifiedOntologyException;

	/**
	 * Returns the object property hierarchy after the classification has
	 * finished.
	 * 
	 * @return the object property hierarchy if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	IntegerHierarchicalGraph getObjectPropertyHierarchy() throws UnclassifiedOntologyException;

	/**
	 * Returns the map of same individuals.
	 * 
	 * @return the the map of same individuals if the classification has
	 *         finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	Map<Integer, Set<Integer>> getSameIndividualMap() throws UnclassifiedOntologyException;

	/**
	 * Tells whether the processor has classified the class hierarchy and object
	 * property hierarchy.
	 * 
	 * @return <code>true</code> is it has finished, or <code>false</code>
	 *         otherwise.
	 */
	boolean isReady();

	/**
	 * Makes one step in the process of classification. Depending on the type of
	 * processor, it could represent the whole classification.
	 * 
	 * @return <code>true</code> if it should be called again in order to finish
	 *         the processing, and <code>false</code> otherwise.
	 */
	boolean process();

}
