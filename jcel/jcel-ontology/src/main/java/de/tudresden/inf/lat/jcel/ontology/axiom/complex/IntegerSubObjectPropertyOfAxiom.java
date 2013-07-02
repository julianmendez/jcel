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

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that one object property is a subproperty
 * of another one. <br />
 * This is : r &sqsube; s
 * 
 * @author Julian Mendez
 */
public class IntegerSubObjectPropertyOfAxiom implements ComplexIntegerAxiom {

	private final int hashCode;
	private final Set<Integer> objectPropertiesInSignature;
	private final IntegerObjectPropertyExpression subObjectProperty;
	private final IntegerObjectPropertyExpression superObjectProperty;

	/**
	 * Constructs a new sub object property axiom.
	 * 
	 * @param subPropExpr
	 *            sub object property
	 * @param superPropExpr
	 *            super object property
	 */
	protected IntegerSubObjectPropertyOfAxiom(
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
		this.hashCode = subPropExpr.hashCode()
				+ (31 * superPropExpr.hashCode());

		Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
		objectPropertiesInSignature.addAll(this.subObjectProperty
				.getObjectPropertiesInSignature());
		objectPropertiesInSignature.addAll(this.superObjectProperty
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
		if (!ret && (o instanceof IntegerSubObjectPropertyOfAxiom)) {
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
		return this.hashCode;
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
