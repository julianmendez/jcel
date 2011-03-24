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

package de.tudresden.inf.lat.jcel.core.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;

/**
 * This class models an axiom stating that the contained classes are equivalent. <br />
 * This is: A<sub>1</sub> &equiv; A<sub>2</sub>, A<sub>2</sub> &equiv;
 * A<sub>3</sub>, &hellip; , A<sub>n-1</sub> &equiv; A<sub>n</sub>
 * 
 * @author Julian Mendez
 */
public class IntegerEquivalentClassesAxiom implements ComplexIntegerAxiom {

	private Set<Integer> classesInSignature = null;
	private Set<IntegerClassExpression> classExpressions = null;
	private Set<Integer> objectPropertiesInSignature = null;

	public IntegerEquivalentClassesAxiom(Set<IntegerClassExpression> descSet) {
		if (descSet == null) {
			throw new IllegalArgumentException("Null parameter received.");
		}
		this.classExpressions = descSet;

		this.classesInSignature = new HashSet<Integer>();
		for (IntegerClassExpression expression : this.classExpressions) {
			this.classesInSignature.addAll(expression.getClassesInSignature());
		}

		this.objectPropertiesInSignature = new HashSet<Integer>();
		for (IntegerClassExpression expression : this.classExpressions) {
			this.objectPropertiesInSignature.addAll(expression
					.getObjectPropertiesInSignature());
		}
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerEquivalentClassesAxiom) {
			IntegerEquivalentClassesAxiom other = (IntegerEquivalentClassesAxiom) o;
			ret = getClassExpressions().equals(other.getClassExpressions());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.unmodifiableSet(this.classesInSignature);
	}

	public Set<IntegerClassExpression> getClassExpressions() {
		return this.classExpressions;
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

	@Override
	public int hashCode() {
		return getClassExpressions().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.EquivalentClasses);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		Set<IntegerClassExpression> classExpressionSet = getClassExpressions();
		for (IntegerClassExpression classExpression : classExpressionSet) {
			sbuf.append(classExpression.toString());
			sbuf.append(ComplexIntegerAxiomConstant.sp);
		}
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
