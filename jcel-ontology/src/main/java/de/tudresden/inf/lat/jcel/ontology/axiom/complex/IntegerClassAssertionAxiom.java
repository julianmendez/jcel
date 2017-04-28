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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * This class models an assertion axiom that relates a class with an individual.
 * <br>
 * This is: A(b)
 * 
 * @author Julian Mendez
 */
public class IntegerClassAssertionAxiom implements ComplexIntegerAxiom {

	private final IntegerClassExpression classExpression;
	private final int individual;
	private final Set<IntegerAnnotation> annotations;
	private final int hashCode;

	/**
	 * Constructs a new class assertion axiom.
	 * 
	 * @param classExpr
	 *            class expression of the assertion
	 * @param individualId
	 *            individual of the assertion
	 * @param annotations
	 *            annotations
	 */
	IntegerClassAssertionAxiom(IntegerClassExpression classExpr, int individualId, Set<IntegerAnnotation> annotations) {
		Objects.requireNonNull(classExpr);
		Objects.requireNonNull(annotations);
		this.classExpression = classExpr;
		this.individual = individualId;
		this.annotations = annotations;
		this.hashCode = this.classExpression.hashCode() + 0x1F * (this.individual + 0x1F * this.annotations.hashCode());
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = (this == obj);
		if (!ret && (obj instanceof IntegerClassAssertionAxiom)) {
			IntegerClassAssertionAxiom other = (IntegerClassAssertionAxiom) obj;
			ret = getClassExpression().equals(other.getClassExpression())
					&& getIndividual().equals(other.getIndividual()) && getAnnotations().equals(other.getAnnotations());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return this.classExpression.getClassesInSignature();
	}

	/**
	 * Returns the class expression in the axiom.
	 * 
	 * @return the class expression in the axiom
	 */
	public IntegerClassExpression getClassExpression() {
		return this.classExpression;
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	/**
	 * Returns the individual in the axiom.
	 * 
	 * @return the individual in the axiom
	 */
	public Integer getIndividual() {
		return this.individual;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.singleton(this.individual);
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.emptySet();
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
		sbuf.append(ComplexIntegerAxiomConstant.ClassAssertion);
		sbuf.append(ComplexIntegerAxiomConstant.LEFT_PAR);
		sbuf.append(getClassExpression());
		sbuf.append(ComplexIntegerAxiomConstant.SP);
		sbuf.append(getIndividual());
		sbuf.append(ComplexIntegerAxiomConstant.RIGHT_PAR);
		return sbuf.toString();
	}

}
