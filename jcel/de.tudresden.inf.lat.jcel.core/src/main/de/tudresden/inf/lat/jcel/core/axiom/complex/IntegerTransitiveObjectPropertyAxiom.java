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

package de.tudresden.inf.lat.jcel.core.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class models an axiom stating that the contained properties are
 * transitive. <br />
 * This is : r <sub><sup>o</sup></sub> r &#8849; r
 * 
 * @author Julian Mendez
 */
public class IntegerTransitiveObjectPropertyAxiom implements
		ComplexIntegerAxiom {

	private Set<Integer> objectPropertiesInSignature = null;
	private Integer property = null;

	public IntegerTransitiveObjectPropertyAxiom(Integer prop) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.property = prop;

		this.objectPropertiesInSignature = new HashSet<Integer>();
		this.objectPropertiesInSignature.add(this.property);
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
		if (o instanceof IntegerTransitiveObjectPropertyAxiom) {
			IntegerTransitiveObjectPropertyAxiom other = (IntegerTransitiveObjectPropertyAxiom) o;
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
		return Collections.unmodifiableSet(this.objectPropertiesInSignature);
	}

	public Integer getProperty() {
		return this.property;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.TransitiveObjectProperty);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getProperty());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}