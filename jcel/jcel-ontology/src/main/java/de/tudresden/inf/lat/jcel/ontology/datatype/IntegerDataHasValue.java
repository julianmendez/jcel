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
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * This class models a has-value class expression, this is: &exist; r <i>.</i>
 * {v} , where r is data property expression and v is a value.
 * 
 * @author Julian Mendez
 */
public class IntegerDataHasValue implements IntegerClassExpression {

	private final int hashCode;
	private final boolean normalized = false;
	private final int property;
	private final int value;

	/**
	 * Constructs a has-value class expression.
	 * 
	 * @param propertyId
	 *            data property expression
	 * @param val
	 *            value
	 */
	protected IntegerDataHasValue(int propertyId, int val) {
		this.property = propertyId;
		this.value = val;
		this.hashCode = propertyId + 31 * val;
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
		return false;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerDataHasValue) {
			IntegerDataHasValue other = (IntegerDataHasValue) o;
			ret = (getProperty() == other.getProperty())
					&& (getValue() == other.getValue());
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
		return Collections.emptySet();
	}

	/**
	 * Returns the data property in this class expression.
	 * 
	 * @return the data property in this class expression
	 */
	public int getProperty() {
		return this.property;
	}

	/**
	 * Returns the value in this class expression.
	 * 
	 * @return the value in this class expression
	 */
	public int getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean hasOnlyClasses() {
		return this.normalized;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.DataHasValue);
		sbuf.append(IntegerClassExpressionWord.openPar);
		sbuf.append(getProperty());
		sbuf.append(IntegerClassExpressionWord.sp);
		sbuf.append(getValue());
		sbuf.append(IntegerClassExpressionWord.closePar);
		return sbuf.toString();
	}

}
