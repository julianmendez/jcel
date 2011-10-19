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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class models an existential restriction with object properties, this is:
 * &exist; r <i>.</i> C , where r is an object property expression and C is a
 * class expression.
 * 
 * @author Julian Mendez
 */
public class IntegerObjectSomeValuesFrom implements IntegerClassExpression {

	private final IntegerClassExpression filler;
	private final boolean normalized;
	private final IntegerObjectPropertyExpression property;

	/**
	 * Constructs an existential restriction.
	 * 
	 * @param propertyExpression
	 *            property expression
	 * @param classExpression
	 *            class expression
	 */
	protected IntegerObjectSomeValuesFrom(
			IntegerObjectPropertyExpression propertyExpression,
			IntegerClassExpression classExpression) {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.property = propertyExpression;
		this.filler = classExpression;
		this.normalized = propertyExpression.isLiteral()
				&& classExpression.isLiteral();
	}

	@Override
	public <T> T accept(IntegerClassExpressionVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean containsBottom() {
		return this.filler.containsBottom();
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectSomeValuesFrom) {
			IntegerObjectSomeValuesFrom other = (IntegerObjectSomeValuesFrom) o;
			ret = getProperty().equals(other.getProperty())
					&& getFiller().equals(other.getFiller());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.addAll(getFiller().getClassesInSignature());
		return Collections.unmodifiableSet(ret);
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
	 * Returns the filler.
	 * 
	 * @return the filler
	 */
	public IntegerClassExpression getFiller() {
		return this.filler;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.addAll(getFiller().getObjectPropertiesInSignature());
		ret.addAll(getProperty().getObjectPropertiesInSignature());
		return Collections.unmodifiableSet(ret);
	}

	public IntegerObjectPropertyExpression getProperty() {
		return this.property;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode() + 31 * getFiller().hashCode();
	}

	@Override
	public boolean hasOnlyLiterals() {
		return this.normalized;
	}

	@Override
	public boolean isIntersectionOfLiterals() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.ObjectSomeValuesFrom);
		sbuf.append(IntegerClassExpressionWord.openPar);
		sbuf.append(getProperty().toString());
		sbuf.append(IntegerClassExpressionWord.sp);
		sbuf.append(getFiller().toString());
		sbuf.append(IntegerClassExpressionWord.closePar);
		return sbuf.toString();
	}

}
