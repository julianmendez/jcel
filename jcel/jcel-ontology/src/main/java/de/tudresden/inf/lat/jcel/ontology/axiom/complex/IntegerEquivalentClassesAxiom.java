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
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * This class models an axiom stating that the contained classes are equivalent. <br />
 * This is: A<sub>1</sub> &equiv; A<sub>2</sub>, A<sub>2</sub> &equiv;
 * A<sub>3</sub>, &hellip; , A<sub>n-1</sub> &equiv; A<sub>n</sub>
 * 
 * @author Julian Mendez
 */
public class IntegerEquivalentClassesAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> classesInSignature;
	private final Set<IntegerClassExpression> classExpressions;
	private final int hashCode;
	private final Set<Integer> objectPropertiesInSignature;

	/**
	 * Constructs a new equivalent classes axiom.
	 * 
	 * @param descSet
	 *            set of classes declared to be equivalent
	 */
	protected IntegerEquivalentClassesAxiom(Set<IntegerClassExpression> descSet) {
		if (descSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classExpressions = Collections.unmodifiableSet(descSet);
		this.hashCode = descSet.hashCode();

		Set<Integer> classesInSignature = new HashSet<Integer>();
		for (IntegerClassExpression expression : this.classExpressions) {
			classesInSignature.addAll(expression.getClassesInSignature());
		}
		this.classesInSignature = Collections
				.unmodifiableSet(classesInSignature);

		Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
		for (IntegerClassExpression expression : this.classExpressions) {
			objectPropertiesInSignature.addAll(expression
					.getObjectPropertiesInSignature());
		}
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
		if (!ret && o instanceof IntegerEquivalentClassesAxiom) {
			IntegerEquivalentClassesAxiom other = (IntegerEquivalentClassesAxiom) o;
			ret = getClassExpressions().equals(other.getClassExpressions());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.unmodifiableSet(this.classesInSignature);
	}

	/**
	 * Returns the set of equivalent classes in this axiom.
	 * 
	 * @return the set of equivalent classes in this axiom
	 */
	public Set<IntegerClassExpression> getClassExpressions() {
		return Collections.unmodifiableSet(this.classExpressions);
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
		return this.hashCode;
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
