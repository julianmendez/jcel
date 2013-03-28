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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Set;

/**
 * An object implementing this interface can create basic ontology objects.
 * 
 * @author Julian Mendez
 */
public interface IntegerDataTypeFactory {

	/**
	 * Creates a class.
	 * 
	 * @param classId
	 *            class identifier
	 * @return a class
	 */
	public IntegerClass createClass(int classId);

	/**
	 * Creates a has-value class expression.
	 * 
	 * @param dataPropertyId
	 *            data property expression
	 * @param val
	 *            value
	 * @return a has-value class expression
	 */
	public IntegerDataHasValue createDataHasValue(int dataPropertyId, int val);

	/**
	 * Creates a data property.
	 * 
	 * @param dataPropertyId
	 *            data property identifier
	 * @return a data property
	 */
	public IntegerDataProperty createDataProperty(int dataPropertyId);

	/**
	 * Creates an existential restriction.
	 * 
	 * @param dataPropertyId
	 * @param classExpression
	 * @return an existential restriction
	 */
	public IntegerDataSomeValuesFrom createDataSomeValuesFrom(
			int dataPropertyId, IntegerClassExpression classExpression);

	/**
	 * Creates a named individual.
	 * 
	 * @param individualId
	 *            named individual identifier
	 * @return a named individual
	 */
	public IntegerNamedIndividual createNamedIndividual(int individualId);

	/**
	 * Creates an intersection of class expressions.
	 * 
	 * @param operands
	 *            set of class expressions
	 * @return an intersection of class expressions
	 */
	public IntegerObjectIntersectionOf createObjectIntersectionOf(
			Set<IntegerClassExpression> operands);

	/**
	 * Creates an inverse object property.
	 * 
	 * @param objectPropertyId
	 *            object property to create the inverse
	 * @return an inverse object property
	 */
	public IntegerObjectInverseOf createObjectInverseOf(
			IntegerObjectProperty objectPropertyId);

	/**
	 * Creates a nominal expression.
	 * 
	 * @param individualId
	 *            individual identifier
	 * @return a nominal expression
	 */
	public IntegerObjectOneOf createObjectOneOf(int individualId);

	/**
	 * Creates an object property.
	 * 
	 * @param objectPropertyId
	 *            object property identifier
	 * @return an object property
	 */
	public IntegerObjectProperty createObjectProperty(int objectPropertyId);

	/**
	 * Creates an existential restriction.
	 * 
	 * @param propertyExpression
	 *            property expression
	 * @param classExpression
	 *            class expression
	 * @return an existential restriction
	 */
	public IntegerObjectSomeValuesFrom createObjectSomeValuesFrom(
			IntegerObjectPropertyExpression propertyExpression,
			IntegerClassExpression classExpression);

	/**
	 * Returns the bottom class.
	 * 
	 * @return the bottom class
	 */
	public IntegerClass getBottomClass();

	/**
	 * Returns the bottom data property.
	 * 
	 * @return the bottom data property
	 */
	public IntegerDataProperty getBottomDataProperty();

	/**
	 * Returns the bottom object property.
	 * 
	 * @return the bottom object property
	 */
	public IntegerObjectProperty getBottomObjectProperty();

	/**
	 * Returns the top class.
	 * 
	 * @return the top class
	 */
	public IntegerClass getTopClass();

	/**
	 * Returns the top data property.
	 * 
	 * @return the top data property
	 */
	public IntegerDataProperty getTopDataProperty();

	/**
	 * Returns the top object property.
	 * 
	 * @return the top object property
	 */
	public IntegerObjectProperty getTopObjectProperty();

}
