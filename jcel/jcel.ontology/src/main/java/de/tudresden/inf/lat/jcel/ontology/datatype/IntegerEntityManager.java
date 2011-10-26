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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Set;

/**
 * An object implementing this interface manages entities and creates new
 * identification numbers for auxiliary entities.
 * 
 * @author Julian Mendez
 */
public interface IntegerEntityManager {

	public static final Integer bottomClassId = 0;
	public static final Integer bottomDataPropertyId = 4;
	public static final Integer bottomObjectPropertyId = 2;
	public static final Integer firstUsableIdentifier = 6;
	public static final Integer topClassId = 1;
	public static final Integer topDataPropertyId = 5;
	public static final Integer topObjectPropertyId = 3;

	/**
	 * Creates a new entity of a certain type.
	 * 
	 * @param type
	 *            type of the entity
	 * @param auxiliary
	 *            <code>true</code> if and only if the created entity is an
	 *            auxiliary entity
	 * @return a new auxiliary entity of a certain type
	 */
	public Integer createAnonymousEntity(IntegerEntityType type,
			boolean auxiliary);

	/**
	 * Creates a new entity of a certain type.
	 * 
	 * @param type
	 *            type of the entity
	 * @param auxiliary
	 *            <code>true</code> if and only if the created entity is an
	 *            auxiliary entity
	 * @param name
	 *            name for this entity
	 * @return a new auxiliary entity of a certain type
	 */
	public Integer createNamedEntity(IntegerEntityType type, String name,
			boolean auxiliary);

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
	 * Returns the set of identifiers of auxiliary entities of a certain type.
	 * 
	 * @param type
	 *            type of the entity
	 * @param auxiliary
	 *            <code>true</code> for auxiliary entities, <code>false</code>
	 *            for non-auxiliary entities
	 * @return the set of identifiers of auxiliary entities of a certain type
	 */
	public Set<Integer> getEntities(IntegerEntityType type, boolean auxiliary);

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
	 * Returns the name defined for a certain entity.
	 * 
	 * @param identifier
	 *            entity to get the name
	 * @return the name defined for a certain entity
	 */
	public String getName(Integer identifier);

	/**
	 * Returns the entity type for the given identifier
	 * 
	 * @param identifier
	 *            entity identifier
	 * @return the entity type for the given identifier
	 */
	public IntegerEntityType getType(Integer identifier);

	/**
	 * Tells whether the given identifier corresponds to an auxiliary entity.
	 * 
	 * @return <code>true</code> if and only if the given identifier corresponds
	 *         to an auxiliary entity
	 */
	public boolean isAuxiliary(Integer identifier);

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

	/**
	 * Returns the number of created entities, either auxiliary or
	 * non-auxiliary.
	 * 
	 * @return the number of created entities, either auxiliary or
	 *         non-auxiliary.
	 */
	public int size();

}
