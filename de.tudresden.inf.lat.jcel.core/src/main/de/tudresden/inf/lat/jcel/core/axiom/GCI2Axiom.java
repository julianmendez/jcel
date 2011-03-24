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

import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescriptionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>A<sub>1</sub> &sube; &exist; r <i>.</i> A <sub>2</sub></li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI2Axiom implements NormalizedIntegerAxiom {

	private Integer classInSuperClass = null;
	private Integer propertyInSuperClass = null;
	private Integer subClass = null;

	public GCI2Axiom(Integer leftCl, Integer rightProp, Integer rightCl) {
		this.subClass = leftCl;
		this.propertyInSuperClass = rightProp;
		this.classInSuperClass = rightCl;
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
		if (o instanceof GCI2Axiom) {
			GCI2Axiom other = (GCI2Axiom) o;
			ret = getSubClass().equals(other.getSubClass())
					&& getClassInSuperClass().equals(
							other.getClassInSuperClass())
					&& getPropertyInSuperClass().equals(
							other.getPropertyInSuperClass());
		}
		return ret;
	}

	public Integer getClassInSuperClass() {
		return this.classInSuperClass;
	}

	public Integer getPropertyInSuperClass() {
		return this.propertyInSuperClass;
	}

	public Integer getSubClass() {
		return this.subClass;
	}

	@Override
	public int hashCode() {
		return getSubClass().hashCode() + getClassInSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.GCI2);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getSubClass());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(IntegerDescriptionWord.ObjectSomeValuesFrom);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getPropertyInSuperClass());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getClassInSuperClass());
		sbuf.append(IntegerAxiomConstant.closePar);
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
