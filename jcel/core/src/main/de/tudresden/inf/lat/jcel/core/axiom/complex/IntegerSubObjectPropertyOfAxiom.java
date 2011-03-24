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

import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that one object property is a subproperty
 * of another one. <br />
 * This is : r &#8849; s
 * 
 * @author Julian Mendez
 */
public class IntegerSubObjectPropertyOfAxiom implements ComplexIntegerAxiom {

	private Set<Integer> objectPropertiesInSignature = null;
	private IntegerObjectPropertyExpression subObjectProperty = null;
	private IntegerObjectPropertyExpression superObjectProperty = null;

	/**
	 * Constructs a new sub object property axiom.
	 * 
	 * @param subPropExpr
	 *            sub object property
	 * @param superPropExpr
	 *            super object property
	 */
	public IntegerSubObjectPropertyOfAxiom(
			IntegerObjectPropertyExpression subPropExpr,
			IntegerObjectPropertyExpression superPropExpr) {
		if (subPropExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superPropExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subObjectProperty = subPropExpr;
		this.superObjectProperty = superPropExpr;

		this.objectPropertiesInSignature = new HashSet<Integer>();
		this.objectPropertiesInSignature.addAll(this.subObjectProperty
				.getObjectPropertiesInSignature());
		this.objectPropertiesInSignature.addAll(this.superObjectProperty
				.getObjectPropertiesInSignature());
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
		if (o instanceof IntegerSubObjectPropertyOfAxiom) {
			IntegerSubObjectPropertyOfAxiom other = (IntegerSubObjectPropertyOfAxiom) o;
			ret = getSubProperty().equals(other.getSubProperty())
					&& getSuperProperty().equals(other.getSuperProperty());
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

	/**
	 * Returns the sub object property in this axiom.
	 * 
	 * @return the sub object property in this axiom
	 */
	public IntegerObjectPropertyExpression getSubProperty() {
		return this.subObjectProperty;
	}

	/**
	 * Returns the super object property in this axiom.
	 * 
	 * @return the super object property in this axiom
	 */
	public IntegerObjectPropertyExpression getSuperProperty() {
		return this.superObjectProperty;
	}

	@Override
	public int hashCode() {
		return getSubProperty().hashCode() + 31 * getSuperProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.SubObjectPropertyOf);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getSubProperty().toString());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty().toString());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
