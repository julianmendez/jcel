/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
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
	private final Set<Integer> objectPropertiesInSignature;
	private final IntegerClassExpression subClass;
	private final IntegerClassExpression superClass;
	private final Set<IntegerAnnotation> annotations;
	private final int hashCode;

	/**
	 * Constructs a new subclass axiom.
	 * 
	 * @param subClExpr
	 *            subclass
	 * @param superClExpr
	 *            superclass
	 * @param annotations
	 *            annotations
	 */
	IntegerSubClassOfAxiom(IntegerClassExpression subClExpr, IntegerClassExpression superClExpr,
			Set<IntegerAnnotation> annotations) {
		Objects.requireNonNull(subClExpr);
		Objects.requireNonNull(superClExpr);
		Objects.requireNonNull(annotations);
		this.subClass = subClExpr;
		this.superClass = superClExpr;

		Set<Integer> classesInSignature = new HashSet<>();
		classesInSignature.addAll(this.subClass.getClassesInSignature());
		classesInSignature.addAll(this.superClass.getClassesInSignature());
		this.classesInSignature = Collections.unmodifiableSet(classesInSignature);

		Set<Integer> objectPropertiesInSignature = new HashSet<>();
		objectPropertiesInSignature.addAll(this.subClass.getObjectPropertiesInSignature());
		objectPropertiesInSignature.addAll(this.superClass.getObjectPropertiesInSignature());
		this.objectPropertiesInSignature = Collections.unmodifiableSet(objectPropertiesInSignature);
		this.annotations = annotations;
		this.hashCode = this.subClass.hashCode()
				+ 0x1F * (this.superClass.hashCode() + 0x1F * this.annotations.hashCode());
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = (this == obj);
		if (!ret && (obj instanceof IntegerSubClassOfAxiom)) {
			IntegerSubClassOfAxiom other = (IntegerSubClassOfAxiom) obj;
			ret = getSubClass().equals(other.getSubClass()) && getSuperClass().equals(other.getSuperClass())
					&& getAnnotations().equals(other.getAnnotations());
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
	public Set<IntegerAnnotation> getAnnotations() {
		return Collections.unmodifiableSet(this.annotations);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.SubClassOf);
		sbuf.append(ComplexIntegerAxiomConstant.LEFT_PAR);
		sbuf.append(getSubClass().toString());
		sbuf.append(ComplexIntegerAxiomConstant.SP);
		sbuf.append(getSuperClass().toString());
		sbuf.append(ComplexIntegerAxiomConstant.RIGHT_PAR);
		return sbuf.toString();
	}

}
