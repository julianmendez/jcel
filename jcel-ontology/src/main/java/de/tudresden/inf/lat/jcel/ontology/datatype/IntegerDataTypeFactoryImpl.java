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

import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * An object implementing this class can create basic ontology objects.
 * 
 * @author Julian Mendez
 */
public class IntegerDataTypeFactoryImpl implements IntegerDataTypeFactory {

	private final IntegerClass bottomClass = new IntegerClass(IntegerEntityManager.bottomClassId);
	private final IntegerDataProperty bottomDataProperty = new IntegerDataProperty(
			IntegerEntityManager.bottomDataPropertyId);
	private final IntegerObjectProperty bottomObjectProperty = new IntegerObjectProperty(
			IntegerEntityManager.bottomObjectPropertyId);
	private final IntegerClass topClass = new IntegerClass(IntegerEntityManager.topClassId);
	private final IntegerDataProperty topDataProperty = new IntegerDataProperty(IntegerEntityManager.topDataPropertyId);
	private final IntegerObjectProperty topObjectProperty = new IntegerObjectProperty(
			IntegerEntityManager.topObjectPropertyId);

	/**
	 * Constructs a new data type factory.
	 */
	public IntegerDataTypeFactoryImpl() {
	}

	@Override
	public IntegerClass createClass(int classId) {
		return new IntegerClass(classId);
	}

	@Override
	public IntegerDataHasValue createDataHasValue(int dataPropertyId, int val) {
		return new IntegerDataHasValue(dataPropertyId, val);
	}

	@Override
	public IntegerDataProperty createDataProperty(int dataPropertyId) {
		return new IntegerDataProperty(dataPropertyId);
	}

	@Override
	public IntegerDataSomeValuesFrom createDataSomeValuesFrom(int dataPropertyId,
			IntegerClassExpression classExpression) {
		Objects.requireNonNull(classExpression);
		return new IntegerDataSomeValuesFrom(dataPropertyId, classExpression);
	}

	@Override
	public IntegerNamedIndividual createNamedIndividual(int individualId) {
		return new IntegerNamedIndividual(individualId);
	}

	@Override
	public IntegerObjectIntersectionOf createObjectIntersectionOf(Set<IntegerClassExpression> operands) {
		Objects.requireNonNull(operands);
		return new IntegerObjectIntersectionOf(operands);
	}

	@Override
	public IntegerObjectInverseOf createObjectInverseOf(IntegerObjectProperty property) {
		Objects.requireNonNull(property);
		return new IntegerObjectInverseOf(property);
	}

	@Override
	public IntegerObjectOneOf createObjectOneOf(int individualId) {
		return new IntegerObjectOneOf(individualId);
	}

	@Override
	public IntegerObjectProperty createObjectProperty(int objectPropertyId) {
		return new IntegerObjectProperty(objectPropertyId);
	}

	@Override
	public IntegerObjectSomeValuesFrom createObjectSomeValuesFrom(IntegerObjectPropertyExpression propertyExpression,
			IntegerClassExpression classExpression) {
		Objects.requireNonNull(propertyExpression);
		Objects.requireNonNull(classExpression);
		return new IntegerObjectSomeValuesFrom(propertyExpression, classExpression);
	}

	@Override
	public IntegerClass getBottomClass() {
		return this.bottomClass;
	}

	@Override
	public IntegerDataProperty getBottomDataProperty() {
		return this.bottomDataProperty;
	}

	@Override
	public IntegerObjectProperty getBottomObjectProperty() {
		return this.bottomObjectProperty;
	}

	@Override
	public IntegerClass getTopClass() {
		return this.topClass;
	}

	@Override
	public IntegerDataProperty getTopDataProperty() {
		return this.topDataProperty;
	}

	@Override
	public IntegerObjectProperty getTopObjectProperty() {
		return this.topObjectProperty;
	}

}
