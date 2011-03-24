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

package de.tudresden.inf.lat.jcel.core.datatype;

/**
 * This models a class, this is : A
 * 
 * @author Julian Mendez
 * 
 */
public class IntegerClass implements IntegerClassExpression,
		Comparable<IntegerClass> {

	private Integer id = null;

	public IntegerClass(Integer n) {
		this.id = n;
	}

	@Override
	public <T> T accept(IntegerClassExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(IntegerClass o) {
		return getId().compareTo(o.getId());
	}

	@Override
	public boolean containsBottom() {
		return this.id.equals(IntegerClassExpression.BOTTOM);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerClass) {
			IntegerClass other = (IntegerClass) o;
			ret = getId().equals(other.getId());
		}
		return ret;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean hasOnlyLiterals() {
		return true;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public String toString() {
		return getId().toString();
	}
}
