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

package de.tudresden.inf.lat.jcel.core.axiom;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpressionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>&exist; r <i>.</i> A<sub>1</sub> &sube; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI3Axiom implements NormalizedIntegerAxiom {

	private Integer leftClass = null;
	private Integer leftProperty = null;
	private Integer rightClass = null;

	public GCI3Axiom(Integer leftProp, Integer leftCl, Integer rightCl) {
		this.leftClass = leftCl;
		this.leftProperty = leftProp;
		this.rightClass = rightCl;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
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

	public Integer getClassInSubClass() {
		return this.leftClass;
	}

	public Integer getPropertyInSubClass() {
		return this.leftProperty;
	}

	public Integer getSuperClass() {
		return this.rightClass;
	}

	@Override
	public int hashCode() {
		return getClassInSubClass().hashCode() + getSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.GCI3);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(IntegerClassExpressionWord.ObjectSomeValuesFrom);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getPropertyInSubClass());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getClassInSubClass());
		sbuf.append(IntegerAxiomConstant.closePar);
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
