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
 * This class models an axiom stating that the range of a particular property is
 * included in a particular class. <br/>
 * This is: range(r) &#8849; A
 * 
 * @author Julian Mendez
 */
public class IntegerInverseObjectPropertiesAxiom implements ComplexIntegerAxiom {

	private Integer firstProperty = null;
	private Set<Integer> objectPropertiesInSignature = null;
	private Integer secondProperty = null;

	public IntegerInverseObjectPropertiesAxiom(Integer first, Integer second) {
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (second == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.firstProperty = first;
		this.secondProperty = second;

		this.objectPropertiesInSignature = new HashSet<Integer>();
		this.objectPropertiesInSignature.add(this.firstProperty);
		this.objectPropertiesInSignature.add(this.secondProperty);
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
		if (o instanceof IntegerInverseObjectPropertiesAxiom) {
			IntegerInverseObjectPropertiesAxiom other = (IntegerInverseObjectPropertiesAxiom) o;
			ret = getFirstProperty().equals(other.getFirstProperty())
					&& getSecondProperty().equals(other.getSecondProperty());
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

	public Integer getFirstProperty() {
		return this.firstProperty;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.unmodifiableSet(this.objectPropertiesInSignature);
	}

	public Integer getSecondProperty() {
		return this.secondProperty;
	}

	@Override
	public int hashCode() {
		return getFirstProperty().hashCode() + 31
				* getSecondProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.InverseObjectProperties);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getFirstProperty());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getSecondProperty());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}