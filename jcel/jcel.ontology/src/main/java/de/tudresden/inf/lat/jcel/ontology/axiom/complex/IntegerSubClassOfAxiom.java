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

/**
 * This class models an axiom stating that one class is a subclass of another
 * one. <br />
 * This is : A &#8849; B
 * 
 * @author Julian Mendez
 */
public class IntegerSubClassOfAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> classesInSignature;
	private final Set<Integer> objectPropertiesInSignature;
	private final IntegerClassExpression subClass;
	private final IntegerClassExpression superClass;

	/**
	 * Constructs a new subclass axiom.
	 * 
	 * @param subClExpr
	 *            subclass
	 * @param superClExpr
	 *            superclass
	 */
	protected IntegerSubClassOfAxiom(IntegerClassExpression subClExpr,
			IntegerClassExpression superClExpr) {
		if (subClExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superClExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subClass = subClExpr;
		this.superClass = superClExpr;

		Set<Integer> classesInSignature = new HashSet<Integer>();
		classesInSignature.addAll(this.subClass.getClassesInSignature());
		classesInSignature.addAll(this.superClass.getClassesInSignature());
		this.classesInSignature = Collections
				.unmodifiableSet(classesInSignature);

		Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
		objectPropertiesInSignature.addAll(this.subClass
				.getObjectPropertiesInSignature());
		objectPropertiesInSignature.addAll(this.superClass
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
		if (!ret && o instanceof IntegerSubClassOfAxiom) {
			IntegerSubClassOfAxiom other = (IntegerSubClassOfAxiom) o;
			ret = getSubClass().equals(other.getSubClass())
					&& getSuperClass().equals(other.getSuperClass());
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
	 * Returns the subclass in this axiom.
	 * 
	 * @return the subclass in this axiom
	 */
	public IntegerClassExpression getSubClass() {
		return this.subClass;
	}

	/**
	 * Returns the superclass in this axiom.
	 * 
	 * @return the superclass in this axiom
	 */
	public IntegerClassExpression getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return getSubClass().hashCode() + 31 * getSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.SubClassOf);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getSubClass().toString());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getSuperClass().toString());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
