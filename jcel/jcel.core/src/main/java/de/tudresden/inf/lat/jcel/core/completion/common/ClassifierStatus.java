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

package de.tudresden.inf.lat.jcel.core.completion.common;

import java.util.Collection;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.ExtendedOntology;

/**
 * An class implementing this interface contains the axioms of the ontology and
 * the main sets used in the classification process. These set are S, R and V.
 * 
 * @author Julian Mendez
 */
public interface ClassifierStatus {

	/**
	 * Returns whether a particular node belongs to the set V.
	 * 
	 * @param node
	 *            node to test membership
	 * @return <tt>true</tt> if and only if the node belongs to the set V
	 */
	public boolean contains(VNode node);

	/**
	 * Gets the identifier of a node, or creates a new one.
	 * 
	 * @param node
	 *            node to get the identifier
	 * @return the identifier of the given node
	 */
	public int createOrGetNodeId(VNode node);

	/**
	 * Returns the object property bottom element.
	 * 
	 * @return the object property bottom element
	 */
	public int geObjectPropertyBottomElement();

	/**
	 * Returns the class bottom element.
	 * 
	 * @return the class bottom element
	 */
	public int getClassBottomElement();

	/**
	 * Returns the class top element.
	 * 
	 * @return the class top element
	 */
	public int getClassTopElement();

	/**
	 * Return an ontology with convenience methods to get its axioms.
	 * 
	 * @return an ontology with convenience methods to get its axioms
	 */
	public ExtendedOntology getExtendedOntology();

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
	public Collection<Integer> getFirstBySecond(int objectProperty,
			int rightClass);

	/**
	 * Returns the inverse object property of a particular object property.
	 * Applying this method twice must return the identity.
	 * 
	 * @param propertyId
	 *            object property
	 * @return the inverse object property of the given object property
	 */
	public int getInverseObjectPropertyOf(int propertyId);

	/**
	 * Returns the node for a given node identifier.
	 * 
	 * @param nodeId
	 *            node identifier
	 * @return the node for the given node identifier
	 */
	public VNode getNode(int nodeId);

	/**
	 * Returns the set of object properties related with a certain class as a
	 * first component.
	 * 
	 * @param className
	 *            the class name
	 * @return the set of properties used by a certain class
	 */
	public Collection<Integer> getObjectPropertiesByFirst(int className);

	/**
	 * Returns the set of object properties related with a certain class as a
	 * second component.
	 * 
	 * @param className
	 *            the class name
	 * @return the set of properties used by a certain class
	 */
	public Collection<Integer> getObjectPropertiesBySecond(int className);

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
	public Set<Integer> getObjectPropertiesWithFunctionalAncestor(
			int objectProperty);

	/**
	 * Returns the object property top element.
	 * 
	 * @return the object property top element
	 */
	public int getObjectPropertyTopElement();

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
	public Collection<Integer> getSecondByFirst(int objectProperty,
			int leftClass);

	/**
	 * Returns all the sub object properties of an object property.
	 * 
	 * @param objectProperty
	 *            object property
	 * @return all the sub object properties of an object property
	 */
	public Collection<Integer> getSubObjectProperties(int objectProperty);

	/**
	 * Returns all the super classes (subsumers) of a class.
	 * 
	 * @param subClass
	 *            class to get the subsumers
	 * @return all the subsumers
	 */
	public Collection<Integer> getSubsumers(int subClass);

	/**
	 * Returns all the super object properties of an object property.
	 * 
	 * @param objectProperty
	 *            object property
	 * @return all the super object properties of an object property
	 */
	public Collection<Integer> getSuperObjectProperties(int objectProperty);

}
