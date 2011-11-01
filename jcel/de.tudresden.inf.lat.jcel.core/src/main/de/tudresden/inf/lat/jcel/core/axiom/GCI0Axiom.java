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
 * <li>A &sube; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI0Axiom implements NormalizedIntegerAxiom {

	private Integer subClass = null;
	private Integer superClass = null;

	public GCI0Axiom(Integer subCl, Integer superCl) {
		this.subClass = subCl;
		this.superClass = superCl;
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
		if (o instanceof GCI0Axiom) {
			GCI0Axiom other = (GCI0Axiom) o;
			ret = getSubClass().equals(other.getSubClass())
					&& getSuperClass().equals(other.getSuperClass());
		}
		return ret;
	}

	public Integer getSubClass() {
		return this.subClass;
	}

	public Integer getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return this.subClass.hashCode() + this.superClass.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.GCI0);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(getSubClass());
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
