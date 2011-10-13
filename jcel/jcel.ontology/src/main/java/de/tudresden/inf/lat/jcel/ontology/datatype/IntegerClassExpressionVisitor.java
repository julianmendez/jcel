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

/**
 * This class models a visitor of <code>IntegerClassExpression</code>.
 * 
 * @param <T>
 *            Type of the returning value of visit functions.
 * 
 * @author Julian Mendez
 * 
 * @see IntegerClassExpression
 */
public interface IntegerClassExpressionVisitor<T> {

	/**
	 * Visits an integer class.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return the visit result
	 */
	public T visit(IntegerClass classExpression);

	/**
	 * Visits a has-value class expression.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return the visit result
	 */
	public T visit(IntegerDataHasValue classExpression);

	/**
	 * Visits an existential restriction with data properties.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return the visit result
	 */
	public T visit(IntegerDataSomeValuesFrom classExpression);

	/**
	 * Visits a named individual.
	 * 
	 * @param namedIndividual
	 *            named individual
	 * @return the visit result
	 */
	public T visit(IntegerNamedIndividual namedIndividual);

	/**
	 * Visits an intersection of several class expressions.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return the visit result
	 */
	public T visit(IntegerObjectIntersectionOf classExpression);

	/**
	 * Visits a nominal constructor.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return the visit result
	 */
	public T visit(IntegerObjectOneOf classExpression);

	/**
	 * Visits an existential restriction with object properties.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return the visit result
	 */
	public T visit(IntegerObjectSomeValuesFrom classExpression);

}
