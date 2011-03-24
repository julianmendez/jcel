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
 * This class models an axiom stating that one class is a subclass of another
 * one. <br />
 * This is : A &sube; B
 * 
 * @author Julian Mendez
 */
public class IntegerSubClassOfAxiom implements IntegerAxiom {

	private IntegerClassExpression subClassExpression = null;
	private IntegerClassExpression superClassExpression = null;

	public IntegerSubClassOfAxiom(IntegerClassExpression subClExpr,
			IntegerClassExpression superClExpr) {
		if (subClExpr == null || superClExpr == null) {
			throw new IllegalArgumentException("Null parameters received.");
		}
		this.subClassExpression = subClExpr;
		this.superClassExpression = superClExpr;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerSubClassOfAxiom) {
			IntegerSubClassOfAxiom other = (IntegerSubClassOfAxiom) o;
			ret = getSubClass().equals(other.getSubClass())
					&& getSuperClass().equals(other.getSuperClass());
		}
		return ret;
	}

	public IntegerClassExpression getSubClass() {
		return this.subClassExpression;
	}

	public IntegerClassExpression getSuperClass() {
		return this.superClassExpression;
	}

	@Override
	public int hashCode() {
		return getSubClass().hashCode() + getSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.SubClassOf);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getSubClass().toString());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getSuperClass().toString());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
