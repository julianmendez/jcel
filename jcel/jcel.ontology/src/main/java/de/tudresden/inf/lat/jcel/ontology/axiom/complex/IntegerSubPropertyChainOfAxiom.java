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
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that the contained properties form a
 * subsumption chain. This is: r<sub>1</sub> &#8728; r<sub>2</sub>
 * &#8728;&hellip; &#8728; r<sub>n</sub> &#8849; s
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
		sbuf.append(ComplexIntegerAxiomConstant.ObjectPropertyChain);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		List<IntegerObjectPropertyExpression> propertyList = getPropertyChain();
		for (IntegerObjectPropertyExpression property : propertyList) {
			sbuf.append(property.toString());
			sbuf.append(ComplexIntegerAxiomConstant.sp);
		}
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
