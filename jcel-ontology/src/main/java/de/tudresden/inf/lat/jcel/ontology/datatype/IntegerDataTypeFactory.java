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
	IntegerClass createClass(int classId);

	/**
	 * Creates a has-value class expression.
	 *
	 * @param dataPropertyId
	 *            data property expression
	 * @param val
	 *            value
	 * @return a has-value class expression
	 */
	IntegerDataHasValue createDataHasValue(int dataPropertyId, int val);

	/**
	 * Creates a data property.
	 *
	 * @param dataPropertyId
	 *            data property identifier
	 * @return a data property
	 */
	IntegerDataProperty createDataProperty(int dataPropertyId);

	/**
	 * Creates an existential restriction.
	 *
	 * @param dataPropertyId
	 *            data property identifier
	 * @param classExpression
	 *            class expression
	 * @return an existential restriction
	 */
	IntegerDataSomeValuesFrom createDataSomeValuesFrom(int dataPropertyId, IntegerClassExpression classExpression);

	/**
	 * Creates a named individual.
	 *
	 * @param individualId
	 *            named individual identifier
	 * @return a named individual
	 */
	IntegerNamedIndividual createNamedIndividual(int individualId);

	/**
	 * Creates an intersection of class expressions.
	 *
	 * @param operands
	 *            set of class expressions
	 * @return an intersection of class expressions
	 */
	IntegerObjectIntersectionOf createObjectIntersectionOf(Set<IntegerClassExpression> operands);

	/**
	 * Creates an inverse object property.
	 *
	 * @param objectPropertyId
	 *            object property to create the inverse
	 * @return an inverse object property
	 */
	IntegerObjectInverseOf createObjectInverseOf(IntegerObjectProperty objectPropertyId);

	/**
	 * Creates a nominal expression.
	 *
	 * @param individualId
	 *            individual identifier
	 * @return a nominal expression
	 */
	IntegerObjectOneOf createObjectOneOf(int individualId);

	/**
	 * Creates an object property.
	 *
	 * @param objectPropertyId
	 *            object property identifier
	 * @return an object property
	 */
	IntegerObjectProperty createObjectProperty(int objectPropertyId);

	/**
	 * Creates an existential restriction.
	 *
	 * @param propertyExpression
	 *            property expression
	 * @param classExpression
	 *            class expression
	 * @return an existential restriction
	 */
	IntegerObjectSomeValuesFrom createObjectSomeValuesFrom(IntegerObjectPropertyExpression propertyExpression,
			IntegerClassExpression classExpression);

	/**
	 * Returns the bottom class.
	 *
	 * @return the bottom class
	 */
	IntegerClass getBottomClass();

	/**
	 * Returns the bottom data property.
	 *
	 * @return the bottom data property
	 */
	IntegerDataProperty getBottomDataProperty();

	/**
	 * Returns the bottom object property.
	 *
	 * @return the bottom object property
	 */
	IntegerObjectProperty getBottomObjectProperty();

	/**
	 * Returns the top class.
	 *
	 * @return the top class
	 */
	IntegerClass getTopClass();

	/**
	 * Returns the top data property.
	 *
	 * @return the top data property
	 */
	IntegerDataProperty getTopDataProperty();

	/**
	 * Returns the top object property.
	 *
	 * @return the top object property
	 */
	IntegerObjectProperty getTopObjectProperty();

}
