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
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that the range of a particular object
 * property is included in a particular class expression. <br />
 * This is: range(r) &#8849; C
 * 
 * @author Julian Mendez
 */
public class IntegerPropertyRangeAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> classesInSignature;
	private final Set<Integer> objectPropertiesInSignature;
	private final IntegerObjectPropertyExpression property;
	private final IntegerClassExpression range;

	/**
	 * Constructs a new object property range axiom.
	 * 
	 * @param prop
	 *            object property
	 * @param clExpr
	 *            class expression
	 */
	protected IntegerPropertyRangeAxiom(IntegerObjectPropertyExpression prop,
			IntegerClassExpression clExpr) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (clExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.property = prop;
		this.range = clExpr;

		Set<Integer> classesInSignature = new HashSet<Integer>();
		classesInSignature.addAll(this.range.getClassesInSignature());
		this.classesInSignature = Collections
				.unmodifiableSet(classesInSignature);

		Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
		objectPropertiesInSignature.addAll(this.range
				.getObjectPropertiesInSignature());
		objectPropertiesInSignature.addAll(this.property
				.getObjectPropertiesInSignature());
		this.objectPropertiesInSignature = Collections
				.unmodifiableSet(objectPropertiesInSignature);
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
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerPropertyRangeAxiom) {
			IntegerPropertyRangeAxiom other = (IntegerPropertyRangeAxiom) o;
			ret = getProperty().equals(other.getProperty())
					&& getRange().equals(other.getRange());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.unmodifiableSet(this.classesInSignature);
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
	 * Returns the object property in this axiom.
	 * 
	 * @return the object property in this axiom
	 */
	public IntegerObjectPropertyExpression getProperty() {
		return this.property;
	}

	/**
	 * Returns the class expression in this axiom.
	 * 
	 * @return the class expression in this axiom
	 */
	public IntegerClassExpression getRange() {
		return this.range;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode() + getRange().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.RangeAxiom);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getProperty());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getRange().toString());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
