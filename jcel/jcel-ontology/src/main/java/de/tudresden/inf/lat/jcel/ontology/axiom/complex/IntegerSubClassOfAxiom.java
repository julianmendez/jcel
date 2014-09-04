/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * This class models an axiom stating that one class is a subclass of another
 * one. <br>
 * This is : A \u2291 B
 * 
 * @author Julian Mendez
 */
public class IntegerSubClassOfAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> classesInSignature;
	private final int hashCode;
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
		this.hashCode = subClExpr.hashCode() + (31 * superClExpr.hashCode());

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
		if (!ret && (o instanceof IntegerSubClassOfAxiom)) {
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
		return this.hashCode;
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
