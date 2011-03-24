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

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * This class models an intersection of several classes, this is: A <sub>1</sub>
 * &cap; &hellip; &cap; A<sub>n</sub>
 * 
 * @author Julian Mendez
 * 
 */
public class IntegerObjectIntersectionOf implements IntegerDescription {

	private boolean normalized = false;
	private Set<IntegerDescription> operandSet = null;
	private boolean withBottom = false;

	public IntegerObjectIntersectionOf(Set<IntegerDescription> operands) {
		if (operands == null) {
			throw new IllegalArgumentException("Null values used.");
		}
		this.operandSet = operands;
		this.normalized = true;
		for (IntegerDescription elem : this.operandSet) {
			if (elem.containsBottom()) {
				this.withBottom = true;
			}
			if (!elem.isLiteral()) {
				this.normalized = false;
			}
		}
	}

	@Override
	public <T> T accept(IntegerDescriptionVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean containsBottom() {
		return this.withBottom;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectIntersectionOf) {
			IntegerObjectIntersectionOf other = (IntegerObjectIntersectionOf) o;
			ret = getOperands().equals(other.getOperands())
					&& (containsBottom() == other.containsBottom());
		}
		return ret;
	}

	public Set<IntegerDescription> getOperands() {
		return Collections.unmodifiableSet(this.operandSet);
	}

	@Override
	public int hashCode() {
		return getOperands().hashCode();
	}

	@Override
	public boolean hasOnlyLiterals() {
		return this.normalized;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerDescriptionWord.ObjectIntersectionOf);
		sbuf.append(IntegerDescriptionWord.openPar);
		for (Iterator<IntegerDescription> it = getOperands().iterator(); it
				.hasNext();) {
			IntegerDescription description = it.next();
			sbuf.append(description.toString());
			if (it.hasNext()) {
				sbuf.append(" ");
			}
		}
		sbuf.append(IntegerDescriptionWord.closePar);
		return sbuf.toString();
	}
}
