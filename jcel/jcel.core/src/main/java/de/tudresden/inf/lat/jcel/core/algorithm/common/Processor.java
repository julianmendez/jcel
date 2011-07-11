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

package de.tudresden.inf.lat.jcel.core.algorithm.common;

import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;

/**
 * An object implementing this interface can classify a set of axioms. After the
 * classification is finished, it is possible to get the resulting class
 * hierarchy and object property hierarchy.
 * 
 * @author Julian Mendez
 */
public interface Processor {

	/**
	 * Returns the set of axioms used to create this processor.
	 * 
	 * @return the set of axioms used to create this processor
	 */
	public Set<ComplexIntegerAxiom> getAxiomSet();

	/**
	 * Returns the class hierarchy after the classification has finished.
	 * 
	 * @return the class hierarchy if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	public IntegerHierarchicalGraph getClassHierarchy()
			throws UnclassifiedOntologyException;

	/**
	 * Returns the map of direct types for every individual.
	 * 
	 * @return the the map of direct types if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	public Map<Integer, Set<Integer>> getDirectTypes()
			throws UnclassifiedOntologyException;

	/**
	 * Returns the object property hierarchy after the classification has
	 * finished.
	 * 
	 * @return the object property hierarchy if the classification has finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	public IntegerHierarchicalGraph getObjectPropertyHierarchy()
			throws UnclassifiedOntologyException;

	/**
	 * Returns the map of same individuals.
	 * 
	 * @return the the map of same individuals if the classification has
	 *         finished
	 * @throws UnclassifiedOntologyException
	 *             if the classification has not finished yet
	 */
	public Map<Integer, Set<Integer>> getSameIndividualMap()
			throws UnclassifiedOntologyException;

	/**
	 * Tells whether the processor has classified the class hierarchy and object
	 * property hierarchy.
	 * 
	 * @return <code>true</code> is it has finished, or <code>false</code>
	 *         otherwise.
	 */
	public boolean isReady();

	/**
	 * Makes one step in the process of classification. Depending on the type of
	 * processor, it could represent the whole classification.
	 * 
	 * @return <code>true</code> if it should be called again in order to finish
	 *         the processing, and <code>false</code> otherwise.
	 */
	public boolean process();

}
