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

package de.tudresden.inf.lat.jcel.coreontology.datatype;

import java.util.Optional;
import java.util.Set;

/**
 * An object implementing this interface manages entities and creates new
 * identification numbers for auxiliary entities.
 *
 * @author Julian Mendez
 */
public interface IntegerEntityManager {

	Integer bottomClassId = 0;
	Integer bottomDataPropertyId = 4;
	Integer bottomObjectPropertyId = 2;
	Integer firstUsableIdentifier = 6;
	Integer topClassId = 1;
	Integer topDataPropertyId = 5;
	Integer topObjectPropertyId = 3;

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
	Integer createAnonymousEntity(IntegerEntityType type, boolean auxiliary);

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
	Integer createNamedEntity(IntegerEntityType type, String name, boolean auxiliary);

	/**
	 * Returns the class identifier corresponding to the given individual. If
	 * that class does not exist, it creates a new auxiliary class.
	 *
	 * @param individual
	 *            individual to get the class
	 * @return the class identifier for the given individual
	 */
	Integer createOrGetClassIdForIndividual(Integer individual);

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
	Integer createOrGetInverseObjectPropertyOf(Integer propertyId) throws IndexOutOfBoundsException;

	/**
	 * Returns the auxiliary inverse object properties.
	 *
	 * @return the auxiliary inverse object properties
	 */
	Set<Integer> getAuxiliaryInverseObjectProperties();

	/**
	 * Returns an optional containing the auxiliary nominal related to a
	 * specific individual, or an empty optional if the individual does not have
	 * any related auxiliary nominal.
	 *
	 * @param individual
	 *            the individual
	 * @return an optional containing the auxiliary nominal related to a
	 *         specific individual, or an empty optional if the individual does
	 *         not have any related auxiliary nominal
	 */
	Optional<Integer> getAuxiliaryNominal(Integer individual);

	/**
	 * Returns the set of auxiliary nominals.
	 *
	 * @return the set of auxiliary nominals
	 */
	Set<Integer> getAuxiliaryNominals();

	/**
	 * Returns the set of identifiers of entities of a certain type.
	 *
	 * @param type
	 *            type of the entity
	 * 
	 * @return the set of identifiers of entities of a certain type
	 */
	Set<Integer> getEntities(IntegerEntityType type);

	/**
	 * Returns either the set of identifiers of auxiliary entities or the set of
	 * identifiers of non-auxiliary entities.
	 *
	 * @param type
	 *            type of the entity
	 * @param auxiliary
	 *            <code>true</code> for auxiliary entities only,
	 *            <code>false</code> for non-auxiliary entities only
	 * @return either the set of identifiers of auxiliary entities or the set of
	 *         identifiers of non-auxiliary entities
	 */
	Set<Integer> getEntities(IntegerEntityType type, boolean auxiliary);

	/**
	 * Returns an optional containing the individual related to a specific
	 * auxiliary nominal, or an empty optional if the auxiliary nominal does not
	 * have any related individual.
	 *
	 * @param auxNominal
	 *            the auxiliary nominal
	 * @return an optional containing the individual related to a specific
	 *         auxiliary nominal, or an empty optional if the auxiliary nominal
	 *         does not have any related individual.
	 */
	Optional<Integer> getIndividual(Integer auxNominal);

	/**
	 * Returns the set of individuals.
	 *
	 * @return the set of individuals
	 */
	Set<Integer> getIndividuals();

	/**
	 * Returns the name defined for a certain entity.
	 *
	 * @param identifier
	 *            entity to get the name
	 * @return the name defined for a certain entity
	 */
	String getName(Integer identifier);

	/**
	 * Returns the entity type for the given identifier
	 *
	 * @param identifier
	 *            entity identifier
	 * @return the entity type for the given identifier
	 */
	IntegerEntityType getType(Integer identifier);

	/**
	 * Tells whether the given identifier corresponds to an auxiliary entity.
	 *
	 * @param identifier
	 *            entity identifier
	 * @return <code>true</code> if and only if the given identifier corresponds
	 *         to an auxiliary entity
	 */
	boolean isAuxiliary(Integer identifier);

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
	boolean proposeInverseObjectPropertyOf(Integer firstProperty, Integer secondProperty);

	/**
	 * Returns the number of created entities, either auxiliary or
	 * non-auxiliary.
	 *
	 * @return the number of created entities, either auxiliary or
	 *         non-auxiliary.
	 */
	int size();

	/**
	 * Tell whether the given identifier corresponds to a registered entity
	 * 
	 * @param identifier
	 *            entity identifier
	 * @return <code>true</code>if and only if the given identifier corresponds
	 *         to a registered entity
	 */
	boolean isEntity(Integer identifier);

}
