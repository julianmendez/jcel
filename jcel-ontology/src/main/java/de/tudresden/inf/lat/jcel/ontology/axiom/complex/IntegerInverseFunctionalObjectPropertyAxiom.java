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
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that the inverse of an object property is
 * functional.
 * 
 * @author Julian Mendez
 */
public class IntegerInverseFunctionalObjectPropertyAxiom implements ComplexIntegerAxiom {

	private final IntegerObjectPropertyExpression objectProperty;
	private final Set<IntegerAnnotation> annotations;
	private final int hashCode;

	/**
	 * Constructs a new inverse functional object property axiom.
	 * 
	 * @param property
	 *            object property which inverse is declared functional
	 * @param annotations
	 *            annotations
	 */
	IntegerInverseFunctionalObjectPropertyAxiom(IntegerObjectPropertyExpression property,
			Set<IntegerAnnotation> annotations) {
		Objects.requireNonNull(property);
		Objects.requireNonNull(annotations);
		this.objectProperty = property;
		this.annotations = annotations;
		this.hashCode = this.objectProperty.hashCode() + 0x1F * this.annotations.hashCode();
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = (this == obj);
		if (!ret && (obj instanceof IntegerInverseFunctionalObjectPropertyAxiom)) {
			IntegerInverseFunctionalObjectPropertyAxiom other = (IntegerInverseFunctionalObjectPropertyAxiom) obj;
			ret = getProperty().equals(other.getProperty()) && getAnnotations().equals(other.getAnnotations());
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
		sbuf.append(ComplexIntegerAxiomConstant.InverseFunctionalObjectProperty);
		sbuf.append(ComplexIntegerAxiomConstant.LEFT_PAR);
		sbuf.append(getProperty());
		sbuf.append(ComplexIntegerAxiomConstant.RIGHT_PAR);
		return sbuf.toString();
	}

}
