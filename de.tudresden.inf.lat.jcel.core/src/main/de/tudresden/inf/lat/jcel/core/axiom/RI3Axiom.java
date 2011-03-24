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

/**
 * Axiom of the form:
 * <ul>
 * <li>r <sub><sup>o</sup></sub> s &sube; t</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RI3Axiom implements NormalizedIntegerAxiom {

	private Integer leftLeftProperty = null;
	private Integer leftRightProperty = null;
	private Integer rightProperty = null;

	public RI3Axiom(Integer leftLeftProp, Integer leftRightProp,
			Integer rightProp) {
		this.leftLeftProperty = leftLeftProp;
		this.leftRightProperty = leftRightProp;
		this.rightProperty = rightProp;
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
		if (o instanceof RI3Axiom) {
			RI3Axiom other = (RI3Axiom) o;
			ret = getLeftSubProperty().equals(other.getLeftSubProperty())
					&& getRightSubProperty()
							.equals(other.getRightSubProperty())
					&& getSuperProperty().equals(other.getSuperProperty());
		}
		return ret;
	}

	public Integer getLeftSubProperty() {
		return this.leftLeftProperty;
	}

	public Integer getRightSubProperty() {
		return this.leftRightProperty;
	}

	public Integer getSuperProperty() {
		return this.rightProperty;
	}

	@Override
	public int hashCode() {
		return getLeftSubProperty().hashCode()
				+ getRightSubProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.RI3);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getLeftSubProperty());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getRightSubProperty());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
