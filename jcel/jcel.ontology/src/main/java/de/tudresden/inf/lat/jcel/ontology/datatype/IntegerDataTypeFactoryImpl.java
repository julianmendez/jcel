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
 * An object implementing this class can create basic ontology objects.
 * 
 * @author Julian Mendez
 */
public class IntegerDataTypeFactoryImpl implements IntegerDataTypeFactory {

	private final IntegerClass bottomClass = new IntegerClass(
			IntegerEntityManager.bottomClassId);
	private final IntegerDataProperty bottomDataProperty = new IntegerDataProperty(
			IntegerEntityManager.bottomDataPropertyId);
	private final IntegerObjectProperty bottomObjectProperty = new IntegerObjectProperty(
			IntegerEntityManager.bottomObjectPropertyId);
	private final IntegerClass topClass = new IntegerClass(
			IntegerEntityManager.topClassId);
	private final IntegerDataProperty topDataProperty = new IntegerDataProperty(
			IntegerEntityManager.topDataPropertyId);
	private final IntegerObjectProperty topObjectProperty = new IntegerObjectProperty(
			IntegerEntityManager.topObjectPropertyId);

	/**
	 * Constructs a new data type factory.
	 */
	public IntegerDataTypeFactoryImpl() {
	}

	@Override
	public IntegerClass createClass(Integer n) {
		if (n == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerClass(n);
	}

	@Override
	public IntegerDataHasValue createDataHasValue(Integer propertyId,
			Integer val) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (val == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerDataHasValue(propertyId, val);
	}

	@Override
	public IntegerDataProperty createDataProperty(Integer n) {
		if (n == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerDataProperty(n);
	}

	@Override
	public IntegerDataSomeValuesFrom createDataSomeValuesFrom(
			Integer propertyId, IntegerClassExpression classExpression) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerDataSomeValuesFrom(propertyId, classExpression);
	}

	@Override
	public IntegerInverseDataProperty createInverseDataProperty(Integer n) {
		if (n == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerInverseDataProperty(n);
	}

	@Override
	public IntegerInverseObjectProperty createInverseObjectProperty(Integer n) {
		if (n == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerInverseObjectProperty(n);
	}

	@Override
	public IntegerNamedIndividual createNamedIndividual(Integer n) {
		if (n == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerNamedIndividual(n);
	}

	@Override
	public IntegerObjectIntersectionOf createObjectIntersectionOf(
			Set<IntegerClassExpression> operands) {
		if (operands == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerObjectIntersectionOf(operands);
	}

	@Override
	public IntegerObjectOneOf createObjectOneOf(Integer individualId) {
		if (individualId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerObjectOneOf(individualId);
	}

	@Override
	public IntegerObjectProperty createObjectProperty(Integer n) {
		if (n == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerObjectProperty(n);
	}

	@Override
	public IntegerObjectSomeValuesFrom createObjectSomeValuesFrom(
			IntegerObjectPropertyExpression propertyExpression,
			IntegerClassExpression classExpression) {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new IntegerObjectSomeValuesFrom(propertyExpression,
				classExpression);
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
