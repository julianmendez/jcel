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
	 * Constructs a new class.
	 * 
	 * @param n
	 *            class identifier
	 */
	public IntegerClass createClass(Integer n);

	/**
	 * Constructs a has-value class expression.
	 * 
	 * @param propertyId
	 *            data property expression
	 * @param val
	 *            value
	 */
	public IntegerDataHasValue createDataHasValue(Integer propertyId,
			Integer val);

	/**
	 * Constructs a data property.
	 * 
	 * @param n
	 *            data property identifier
	 */
	public IntegerDataProperty createDataProperty(Integer n);

	/**
	 * Constructs an existential restriction.
	 * 
	 * @param propertyId
	 * @param classExpression
	 */
	public IntegerDataSomeValuesFrom createDataSomeValuesFrom(
			Integer propertyId, IntegerClassExpression classExpression);

	/**
	 * Constructs an inverse object property.
	 * 
	 * @param n
	 *            inverse object property identifier
	 */
	public IntegerObjectInverseOf createInverseObjectProperty(Integer n);

	/**
	 * Constructs a new named individual.
	 * 
	 * @param n
	 *            named individual identifier
	 */
	public IntegerNamedIndividual createNamedIndividual(Integer n);

	/**
	 * Constructs an intersection of class expressions.
	 * 
	 * @param operands
	 *            set of class expressions
	 */
	public IntegerObjectIntersectionOf createObjectIntersectionOf(
			Set<IntegerClassExpression> operands);

	/**
	 * Constructs a nominal expression.
	 * 
	 * @param individualId
	 *            individual identifier
	 */
	public IntegerObjectOneOf createObjectOneOf(Integer individualId);

	/**
	 * Constructs an object property.
	 * 
	 * @param n
	 *            object property identifier
	 */
	public IntegerObjectProperty createObjectProperty(Integer n);

	/**
	 * Constructs an existential restriction.
	 * 
	 * @param propertyExpression
	 *            property expression
	 * @param classExpression
	 *            class expression
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
