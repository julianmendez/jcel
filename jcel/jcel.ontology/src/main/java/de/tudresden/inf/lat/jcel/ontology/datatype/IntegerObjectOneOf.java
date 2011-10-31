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

/**
 * This class models the nominal constructor. <br />
 * This is: {a}, where a is an individual.
 * 
 * @author Julian Mendez
 */
public class IntegerObjectOneOf implements IntegerClassExpression {

	private final Integer individual;

	/**
	 * Constructs a nominal expression.
	 * 
	 * @param individualId
	 *            individual identifier
	 */
	protected IntegerObjectOneOf(Integer individualId) {
		if (individualId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.individual = individualId;
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
	public boolean containsOnlyOneClass() {
		return false;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectOneOf) {
			IntegerObjectOneOf other = (IntegerObjectOneOf) o;
			ret = getIndividual().equals(other.getIndividual());
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

	/**
	 * Returns the individual in this class expression.
	 * 
	 * @return the individual in this class expression
	 */
	public Integer getIndividual() {
		return this.individual;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.singleton(getIndividual());
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public int hashCode() {
		return getIndividual().hashCode();
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.ObjectOneOf);
		sbuf.append(IntegerClassExpressionWord.openPar);
		sbuf.append(getIndividual());
		sbuf.append(IntegerClassExpressionWord.closePar);
		return sbuf.toString();
	}

}
