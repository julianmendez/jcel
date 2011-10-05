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
import java.util.Iterator;
import java.util.Set;

/**
 * This class models an intersection of several class expressions, this is:
 * C<sub>1</sub> &#8851; &hellip; &#8851; C<sub>n</sub>
 * 
 * @author Julian Mendez
 * 
 */
public class IntegerObjectIntersectionOf implements IntegerClassExpression {

	private final boolean isIntersectionOfLiterals;
	private final boolean normalized;
	private final Set<IntegerClassExpression> operands;
	private final boolean withBottom;

	/**
	 * Constructs an intersection of class expressions.
	 * 
	 * @param operands
	 *            set of class expressions
	 */
	public IntegerObjectIntersectionOf(Set<IntegerClassExpression> operands) {
		if (operands == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.operands = operands;
		boolean normalized = true;
		boolean isIntersectionOfLiterals = true;
		boolean withBottom = false;
		for (IntegerClassExpression elem : this.operands) {
			if (elem.containsBottom()) {
				withBottom = true;
			}
			if (!elem.isLiteral()) {
				normalized = false;
				isIntersectionOfLiterals = isIntersectionOfLiterals
						&& elem.isIntersectionOfLiterals();
			}
		}
		this.normalized = normalized;
		this.isIntersectionOfLiterals = isIntersectionOfLiterals;
		this.withBottom = withBottom;
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
		return this.withBottom;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectIntersectionOf) {
			IntegerObjectIntersectionOf other = (IntegerObjectIntersectionOf) o;
			ret = getOperands().equals(other.getOperands())
					&& (containsBottom() == other.containsBottom());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		for (IntegerClassExpression expression : getOperands()) {
			ret.addAll(expression.getClassesInSignature());
		}
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

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		for (IntegerClassExpression expression : getOperands()) {
			ret.addAll(expression.getObjectPropertiesInSignature());
		}
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Return the operands in this class expression.
	 * 
	 * @return the operands in this class expression
	 */
	public Set<IntegerClassExpression> getOperands() {
		return Collections.unmodifiableSet(this.operands);
	}

	@Override
	public int hashCode() {
		return getOperands().hashCode();
	}

	@Override
	public boolean hasOnlyLiterals() {
		return this.normalized;
	}

	@Override
	public boolean isIntersectionOfLiterals() {
		return this.isIntersectionOfLiterals;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.ObjectIntersectionOf);
		sbuf.append(IntegerClassExpressionWord.openPar);
		for (Iterator<IntegerClassExpression> it = getOperands().iterator(); it
				.hasNext();) {
			IntegerClassExpression classExpression = it.next();
			sbuf.append(classExpression.toString());
			if (it.hasNext()) {
				sbuf.append(" ");
			}
		}
		sbuf.append(IntegerClassExpressionWord.closePar);
		return sbuf.toString();
	}

}
