/*
 * Copyright 2009 Julian Mendez
 *
 *
 *  This file is part of jcel.
 *
 *  jcel is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jcel is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Set;

/**
 * An object of this class generates new identification numbers for object
 * properties and classes.
 * 
 * @author Julian Mendez
 */
public interface IdGenerator {

	/**
	 * Creates a new class identifier.
	 * 
	 * @return the new class identifier
	 */
	public Integer createNewClassId();

	/**
	 * Creates a new object property identifier.
	 * 
	 * @return the new object property identifier
	 */
	public Integer createNewObjectPropertyId();

	/**
	 * Returns the class identifier corresponding to the given individual. If
	 * that class does not exist, it creates a new auxiliary class.
	 * 
	 * @param individual
	 *            individual to get the class
	 * @return the class identifier for the given individual
	 */
	public Integer createOrGetClassIdForIndividual(Integer individual);

	/**
	 * Returns the inverse object property of the given object property. If this
	 * property does not exist, it creates a new auxiliary object property.
	 * 
	 * @param propertyId
	 *            property identifier to create an inverse object property
	 * @return the inverse object property of the given object property
	 * @throws IndexOutOfBoundsException
	 *             if the property identifier is greater than the last auxiliary
	 *             property created
	 */
	public Integer createOrGetInverseObjectPropertyOf(Integer propertyId)
			throws IndexOutOfBoundsException;

	/**
	 * This method gives the auxiliary nominal related to a specific individual.
	 * 
	 * @param individual
	 *            the individual
	 * @return the requested class or <code>null</code> if the individual does
	 *         not have any related auxiliary nominal.
	 */
	public Integer getAuxiliaryNominal(Integer individual);

	/**
	 * Returns the set of auxiliary nominals.
	 * 
	 * @return the set of auxiliary nominals
	 */
	public Set<Integer> getAuxiliaryNominals();

	/**
	 * Returns the first generated class identifier.
	 * 
	 * @return the first generated class identifier
	 */
	public Integer getFirstClassId();

	/**
	 * Returns the first generated object property identifier.
	 * 
	 * @return the first generated object property identifier
	 */
	public Integer getFirstObjectPropertyId();

	/**
	 * This method gives the individual related to a specific auxiliary nominal.
	 * 
	 * @param auxNominal
	 *            the auxiliary nominal
	 * @return the requested individual or <code>null</code> if the auxiliary
	 *         nominal does not have any related individual.
	 */
	public Integer getIndividual(Integer auxNominal);

	/**
	 * Returns the set of individuals.
	 * 
	 * @return the set of individuals
	 */
	public Set<Integer> getIndividuals();

	/**
	 * Returns the next generated class identifier.
	 * 
	 * @return the next generated class identifier
	 */
	public Integer getNextClassId();

	/**
	 * Returns the next generated object property identifier.
	 * 
	 * @return the next generated object property identifier
	 */
	public Integer getNextObjectPropertyId();

	/**
	 * Proposes the association of an object property to another object property
	 * as one the inverse object property of the other one. This association is
	 * accepted if and only if none of both object properties has already
	 * another inverse object property.
	 * 
	 * @param firstProperty
	 *            an object property
	 * @param secondProperty
	 *            an object property
	 * @return <code>true</code> if and only if the proposal was accepted
	 */
	public boolean proposeInverseObjectPropertyOf(Integer firstProperty,
			Integer secondProperty) throws IndexOutOfBoundsException;

}
