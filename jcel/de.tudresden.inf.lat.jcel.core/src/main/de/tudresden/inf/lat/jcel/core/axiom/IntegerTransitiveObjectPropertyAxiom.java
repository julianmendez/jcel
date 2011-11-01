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
 * This class models an axiom stating that the contained properties are
 * transitive. <br />
 * This is : r <sub><sup>o</sup></sub> r &sube; r
 * 
 * @author Julian Mendez
 */
public class IntegerTransitiveObjectPropertyAxiom implements IntegerAxiom {

	private Integer property = null;

	public IntegerTransitiveObjectPropertyAxiom(Integer prop) {
		this.property = prop;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerRangeAxiom) {
			IntegerRangeAxiom other = (IntegerRangeAxiom) o;
			ret = getProperty().equals(other.getProperty());
		}
		return ret;
	}

	public Integer getProperty() {
		return this.property;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.TransitiveAxiom);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getProperty());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}