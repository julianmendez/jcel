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
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that the contained properties form a
 * subsumption chain. This is: r<sub>1</sub> &compfn; r<sub>2</sub>
 * &compfn;&hellip; &compfn; r<sub>n</sub> &sqsube; s
 * 
 * @author Julian Mendez
 */
public class IntegerSubPropertyChainOfAxiom implements ComplexIntegerAxiom {

	private final int hashCode;
	private final Set<Integer> objectPropertiesInSignature;
	private final List<IntegerObjectPropertyExpression> propertyChain;
	private final IntegerObjectPropertyExpression superProperty;

	/**
	 * Constructs a new sub object property chain axiom.
	 * 
	 * @param chain
	 *            list of object property expressions in the chain
	 * @param superProp
	 *            super object property expression
	 */
	protected IntegerSubPropertyChainOfAxiom(
			List<IntegerObjectPropertyExpression> chain,
			IntegerObjectPropertyExpression superProp) {
		if (chain == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.propertyChain = chain;
		this.superProperty = superProp;
		this.hashCode = superProp.hashCode() + 31 * chain.hashCode();

		Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
		for (IntegerObjectPropertyExpression propertyExpr : getPropertyChain()) {
			objectPropertiesInSignature.addAll(propertyExpr
					.getObjectPropertiesInSignature());
		}
		objectPropertiesInSignature.addAll(getSuperProperty()
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
		if (!ret && o instanceof IntegerSubPropertyChainOfAxiom) {
			IntegerSubPropertyChainOfAxiom other = (IntegerSubPropertyChainOfAxiom) o;
			ret = getPropertyChain().equals(other.getPropertyChain())
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

	public List<IntegerObjectPropertyExpression> getPropertyChain() {
		return Collections.unmodifiableList(this.propertyChain);
	}

	/**
	 * Returns the super object property expression.
	 * 
	 * @return the super object property expression
	 */
	public IntegerObjectPropertyExpression getSuperProperty() {
		return this.superProperty;
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
		sbuf.append(ComplexIntegerAxiomConstant.ObjectPropertyChain);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		List<IntegerObjectPropertyExpression> propertyList = getPropertyChain();
		for (IntegerObjectPropertyExpression property : propertyList) {
			sbuf.append(property.toString());
			sbuf.append(ComplexIntegerAxiomConstant.sp);
		}
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
