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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.Collections;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that an object property is reflexive.
 * 
 * @author Julian Mendez
 */
public class IntegerReflexiveObjectPropertyAxiom implements ComplexIntegerAxiom {

	private final IntegerObjectPropertyExpression objectProperty;

	/**
	 * Constructs a new reflexive object property axiom.
	 * 
	 * @param property
	 *            object property
	 */
	protected IntegerReflexiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.objectProperty = property;
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerReflexiveObjectPropertyAxiom) {
			IntegerReflexiveObjectPropertyAxiom other = (IntegerReflexiveObjectPropertyAxiom) o;
			ret = getProperty().equals(other.getProperty());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return getProperty().getObjectPropertiesInSignature();
	}

	/**
	 * Returns the object property in this axiom.
	 * 
	 * @return the object property in this axiom
	 */
	public IntegerObjectPropertyExpression getProperty() {
		return this.objectProperty;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode();

	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.ReflexiveObjectProperty);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getProperty());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
