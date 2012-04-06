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

package de.tudresden.inf.lat.jcel.coreontology.datatype;

/**
 * 
 * @author Julian Mendez
 */
public interface OntologyExpressivity {

	/**
	 * Tells whether the ontology analyzer has found class bottom.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         class bottom
	 */
	public boolean hasBottom();

	/**
	 * Tells whether the ontology analyzer has found data types.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         data types
	 */
	public boolean hasDatatype();

	/**
	 * Tells whether the ontology analyzer has found functional object
	 * properties.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         functional object properties
	 */
	public boolean hasFunctionalObjectProperty();

	/**
	 * Tells whether the ontology analyzer has found individuals.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         individuals
	 */
	public boolean hasIndividual();

	/**
	 * Tells whether the ontology analyzer has found inverse object properties.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         inverse object properties
	 */
	public boolean hasInverseObjectProperty();

	/**
	 * Tells whether the ontology analyzer has found nominals.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         nominals
	 */
	public boolean hasNominal();

	/**
	 * Tells whether the ontology analyzer has found reflexive object
	 * properties.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         reflexive object properties
	 */
	public boolean hasReflexiveObjectProperty();

	/**
	 * Tells whether the ontology analyzer has found sub object properties.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         sub object properties
	 */
	public boolean hasSubObjectPropertyOf();

	/**
	 * Tells whether the ontology analyzer has found sub object property chains.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         sub object property chains
	 */
	public boolean hasSubPropertyChainOf();

	/**
	 * Tells whether the ontology analyzer has found transitive object
	 * properties.
	 * 
	 * @return <code>true</code> if and only if the ontology analyzer has found
	 *         transitive object properties
	 */
	public boolean hasTransitiveObjectProperty();

}
