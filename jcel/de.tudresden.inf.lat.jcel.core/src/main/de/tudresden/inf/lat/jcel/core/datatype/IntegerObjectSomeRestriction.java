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
 * This class models an existential restriction, this is: &exist; r <i>.</i> A
 * 
 * @author Julian Mendez
 */
public class IntegerObjectSomeRestriction implements IntegerDescription {

	private IntegerDescription integerDescription = null;
	private boolean normalized = false;
	private Integer property = null;

	public IntegerObjectSomeRestriction(Integer propertyId,
			IntegerDescription description) {
		if (propertyId == null || description == null) {
			throw new IllegalArgumentException("Null values used.");
		}
		this.property = propertyId;
		this.integerDescription = description;
		this.normalized = description.isLiteral();
	}

	@Override
	public <T> T accept(IntegerDescriptionVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean containsBottom() {
		return this.integerDescription.containsBottom();
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectSomeRestriction) {
			IntegerObjectSomeRestriction other = (IntegerObjectSomeRestriction) o;
			ret = getProperty().equals(other.getProperty())
					&& getFiller().equals(other.getFiller());
		}
		return ret;
	}

	public IntegerDescription getFiller() {
		return this.integerDescription;
	}

	public Integer getProperty() {
		return this.property;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode() + getFiller().hashCode();
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
		sbuf.append(IntegerDescriptionWord.ObjectSomeValuesFrom);
		sbuf.append(IntegerDescriptionWord.openPar);
		sbuf.append(getProperty());
		sbuf.append(IntegerDescriptionWord.sp);
		sbuf.append(getFiller().toString());
		sbuf.append(IntegerDescriptionWord.closePar);
		return sbuf.toString();
	}
}
