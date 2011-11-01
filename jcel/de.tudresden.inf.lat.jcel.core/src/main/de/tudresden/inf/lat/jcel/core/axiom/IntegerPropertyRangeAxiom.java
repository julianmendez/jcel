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

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;

/**
 * This class models an axiom stating that the range of a particular property is
 * included in a particular class. <br/>
 * This is: range(r) &sube; A
 * 
 * @author Julian Mendez
 */
public class IntegerPropertyRangeAxiom implements IntegerAxiom {

	private Integer property = null;
	private IntegerClassExpression superClassExpression = null;

	public IntegerPropertyRangeAxiom(Integer prop, IntegerClassExpression clExpr) {
		this.property = prop;
		this.superClassExpression = clExpr;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerPropertyRangeAxiom) {
			IntegerPropertyRangeAxiom other = (IntegerPropertyRangeAxiom) o;
			ret = getProperty().equals(other.getProperty())
					&& getRange().equals(other.getRange());
		}
		return ret;
	}

	public Integer getProperty() {
		return this.property;
	}

	public IntegerClassExpression getRange() {
		return this.superClassExpression;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode() + getRange().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.RangeAxiom);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getProperty());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getRange().toString());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}