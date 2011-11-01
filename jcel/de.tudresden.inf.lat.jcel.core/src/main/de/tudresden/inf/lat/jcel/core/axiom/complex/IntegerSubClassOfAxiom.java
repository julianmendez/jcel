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

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;

/**
 * This class models an axiom stating that one class is a subclass of another
 * one. <br />
 * This is : A &#8849; B
 * 
 * @author Julian Mendez
 */
public class IntegerSubClassOfAxiom implements ComplexIntegerAxiom {

	private Set<Integer> classesInSignature = null;
	private Set<Integer> objectPropertiesInSignature = null;
	private IntegerClassExpression subClass = null;
	private IntegerClassExpression superClass = null;

	public IntegerSubClassOfAxiom(IntegerClassExpression subClExpr,
			IntegerClassExpression superClExpr) {
		if (subClExpr == null || superClExpr == null) {
			throw new IllegalArgumentException("Null parameters received.");
		}
		this.subClass = subClExpr;
		this.superClass = superClExpr;

		this.classesInSignature = new HashSet<Integer>();
		this.classesInSignature.addAll(this.subClass.getClassesInSignature());
		this.classesInSignature.addAll(this.superClass.getClassesInSignature());

		this.objectPropertiesInSignature = new HashSet<Integer>();
		this.objectPropertiesInSignature.addAll(this.subClass
				.getObjectPropertiesInSignature());
		this.objectPropertiesInSignature.addAll(this.superClass
				.getObjectPropertiesInSignature());
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerSubClassOfAxiom) {
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

	public IntegerClassExpression getSubClass() {
		return this.subClass;
	}

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
