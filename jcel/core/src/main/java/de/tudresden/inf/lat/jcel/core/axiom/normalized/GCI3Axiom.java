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

package de.tudresden.inf.lat.jcel.core.axiom.normalized;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpressionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>&exist; r <i>.</i> A &#8849; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI3Axiom implements NormalizedIntegerAxiom {

	private Integer classInSubClass = null;
	private Integer propertyInSubClass = null;
	private Integer superClass = null;

	/**
	 * Constructs a new GCI-3 axiom.
	 * 
	 * @param leftProp
	 *            object property identifier for the left-hand part
	 * @param leftCl
	 *            class identifier for the left-hand part
	 * @param rightCl
	 *            superclass identifier
	 */
	public GCI3Axiom(Integer leftProp, Integer leftCl, Integer rightCl) {
		if (leftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (leftCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classInSubClass = leftCl;
		this.propertyInSubClass = leftProp;
		this.superClass = rightCl;
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof GCI3Axiom) {
			GCI3Axiom other = (GCI3Axiom) o;
			ret = getClassInSubClass().equals(other.getClassInSubClass())
					&& getPropertyInSubClass().equals(
							other.getPropertyInSubClass())
					&& getSuperClass().equals(other.getSuperClass());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(this.classInSubClass);
		ret.add(this.superClass);
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Returns the class on the left-hand part of the axiom.
	 * 
	 * @return the class on the left-hand part of the axiom
	 */
	public Integer getClassInSubClass() {
		return this.classInSubClass;
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
		return Collections.singleton(this.propertyInSubClass);
	}

	/**
	 * Returns the object property on the left-hand part of the axiom.
	 * 
	 * @return the object property on the left-hand part of the axiom
	 */
	public Integer getPropertyInSubClass() {
		return this.propertyInSubClass;
	}

	/**
	 * Returns the superclass in the axiom.
	 * 
	 * @return the superclass in the axiom
	 */
	public Integer getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return getClassInSubClass().hashCode() + 31
				* getSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.GCI3);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(IntegerClassExpressionWord.ObjectSomeValuesFrom);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getPropertyInSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getClassInSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
