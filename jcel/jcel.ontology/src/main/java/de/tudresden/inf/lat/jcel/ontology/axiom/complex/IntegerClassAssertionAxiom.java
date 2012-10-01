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
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * This class models an assertion axiom that relates a class with an individual. <br />
 * This is: A(b)
 * 
 * @author Julian Mendez
 */
public class IntegerClassAssertionAxiom implements ComplexIntegerAxiom {

	private final IntegerClassExpression classExpression;
	private final int individual;

	/**
	 * Constructs a new class assertion axiom.
	 * 
	 * @param classExpr
	 *            class expression of the assertion
	 * @param individualId
	 *            individual of the assertion
	 */
	protected IntegerClassAssertionAxiom(IntegerClassExpression classExpr,
			int individualId) {
		if (classExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classExpression = classExpr;
		this.individual = individualId;
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
		if (!ret && o instanceof IntegerClassAssertionAxiom) {
			IntegerClassAssertionAxiom other = (IntegerClassAssertionAxiom) o;
			ret = getClassExpression().equals(other.getClassExpression())
					&& getIndividual().equals(other.getIndividual());
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
	public int hashCode() {
		return getClassExpression().hashCode() + 31
				* getIndividual().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.ClassAssertion);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getClassExpression());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getIndividual());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
