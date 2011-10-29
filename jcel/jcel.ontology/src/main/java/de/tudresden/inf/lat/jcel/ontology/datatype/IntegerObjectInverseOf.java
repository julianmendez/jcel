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

import java.util.Collections;
import java.util.Set;

/**
 * This class models an inverse object property.
 * 
 * @author Julian Mendez
 */
public class IntegerObjectInverseOf implements IntegerObjectPropertyExpression,
		Comparable<IntegerObjectInverseOf> {

	private final IntegerObjectProperty invProperty;

	/**
	 * Constructs an inverse object property.
	 * 
	 * @param property
	 *            property to create the inverse
	 */
	protected IntegerObjectInverseOf(IntegerObjectProperty property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.invProperty = property;
	}

	@Override
	public int compareTo(IntegerObjectInverseOf o) {
		if (o == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getInverse().compareTo(o.getInverse());
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectInverseOf) {
			IntegerObjectInverseOf other = (IntegerObjectInverseOf) o;
			ret = getInverse().equals(other.getInverse());
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

	/**
	 * Returns the inverse used for this object property expression
	 * 
	 * @return the inverse used for this object property expression
	 */
	public IntegerObjectProperty getInverse() {
		return this.invProperty;
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return getInverse().getObjectPropertiesInSignature();
	}

	@Override
	public int hashCode() {
		return getInverse().hashCode();
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		return getInverse().toString();
	}

}
