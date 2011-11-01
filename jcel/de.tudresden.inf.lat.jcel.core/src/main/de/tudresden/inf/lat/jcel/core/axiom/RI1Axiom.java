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
 * <li>&epsilon; &sube; r</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RI1Axiom implements NormalizedIntegerAxiom {

	private Integer property = null;

	public RI1Axiom(Integer prop) {
		this.property = prop;
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
		if (o instanceof RI1Axiom) {
			RI1Axiom other = (RI1Axiom) o;
			ret = getSuperProperty().equals(other.getSuperProperty());
		}
		return ret;
	}

	public Integer getSuperProperty() {
		return this.property;
	}

	@Override
	public int hashCode() {
		return getSuperProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.RI1);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(IntegerAxiomConstant.emptyProp);
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
