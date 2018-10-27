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

package de.tudresden.inf.lat.jcel.core.completion.common;

import java.util.Collection;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.coreontology.axiom.ExtendedOntology;

/**
 * An class implementing this interface contains the axioms of the ontology and
 * the main sets used in the classification process. These set are S, R and V.
 * 
 * @author Julian Mendez
 */
public interface ClassifierStatus {

	/**
	 * Adds a new R-entry to the set of entries to be processed.
	 * 
	 * @param propertyId
	 *            property id
	 * @param leftClassId
	 *            left class id
	 * @param rightClassId
	 *            right class id
	 * @return <code>true</code> if and only if the entry was added
	 */
	boolean addNewREntry(int propertyId, int leftClassId, int rightClassId);

	/**
	 * Adds a new S-entry to the set of entries to be processed.
	 * 
	 * @param subClassId
	 *            subclass id
	 * @param superClassId
	 *            superclass id
	 * @return <code>true</code> if and only if the entry was added
	 */
	boolean addNewSEntry(int subClassId, int superClassId);

	/**
	 * Returns whether a particular node belongs to the set V.
	 * 
	 * @param node
	 *            node to test membership
	 * @return <code>true</code> if and only if the node belongs to the set V
	 */
	boolean contains(VNode node);

	/**
	 * Gets the identifier of a node, or creates a new one.
	 * 
	 * @param node
	 *            node to get the identifier
	 * @return the identifier of the given node
	 */
	int createOrGetNodeId(VNode node);

	/**
	 * Returns the monitor of set S.
	 * 
	 * @return the monitor of set S
	 */
	Object getClassGraphMonitor();

	/**
	 * Return an ontology with convenience methods to get its axioms.
	 * 
	 * @return an ontology with convenience methods to get its axioms
	 */
	ExtendedOntology getExtendedOntology();

	/**
	 * Returns all the classes that are related to a certain class using a
	 * certain object property
	 * 
	 * @param objectProperty
	 *            that relates the classes
	 * @param rightClass
	 *            the class that is on right part in the relation
	 * @return all the related classes
	 */
	Collection<Integer> getFirstBySecond(int objectProperty, int rightClass);

	/**
	 * Returns the inverse object property of a particular object property.
	 * Applying this method twice must return the identity.
	 * 
	 * @param propertyId
	 *            object property
	 * @return the inverse object property of the given object property
	 */
	int getInverseObjectPropertyOf(int propertyId);

	/**
	 * Returns the node for a given node identifier.
	 * 
	 * @param nodeId
	 *            node identifier
	 * @return the node for the given node identifier
	 */
	VNode getNode(int nodeId);

	/**
	 * Returns the set of object properties related with a certain class as a
	 * first component.
	 * 
	 * @param className
	 *            the class name
	 * @return the set of properties used by a certain class
	 */
	Collection<Integer> getObjectPropertiesByFirst(int className);

	/**
	 * Returns the set of object properties related with a certain class as a
	 * second component.
	 * 
	 * @param className
	 *            the class name
	 * @return the set of properties used by a certain class
	 */
	Collection<Integer> getObjectPropertiesBySecond(int className);

	/**
	 * Returns all the functional object properties that have a common
	 * functional object property ancestor of the specified object property. The
	 * result includes the specified object property when it is functional.
	 * 
	 * @param objectProperty
	 *            object property
	 * @return all the functional object properties that have a common
	 *         functional object property ancestor of the specified object
	 *         property
	 */
	Set<Integer> getObjectPropertiesWithFunctionalAncestor(int objectProperty);

	/**
	 * Returns the monitor of set R.
	 * 
	 * @return the monitor of set R
	 */
	Object getRelationSetMonitor();

	/**
	 * Returns all the classes that are related from a certain class using a
	 * certain object property
	 * 
	 * @param objectProperty
	 *            that relates the classes
	 * @param leftClass
	 *            the class that is on left part in the relation
	 * @return all the related classes
	 */
	Collection<Integer> getSecondByFirst(int objectProperty, int leftClass);

	/**
	 * Returns all the sub object properties of an object property.
	 * 
	 * @param objectProperty
	 *            object property
	 * @return all the sub object properties of an object property
	 */
	Collection<Integer> getSubObjectProperties(int objectProperty);

	/**
	 * Returns all the super classes (subsumers) of a class.
	 * 
	 * @param subClass
	 *            class to get the subsumers
	 * @return all the subsumers
	 */
	Collection<Integer> getSubsumers(int subClass);

	/**
	 * Returns all the super object properties of an object property.
	 * 
	 * @param objectProperty
	 *            object property
	 * @return all the super object properties of an object property
	 */
	Collection<Integer> getSuperObjectProperties(int objectProperty);

}
