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

package de.tudresden.inf.lat.jcel.core.algorithm.cel;

/**
 * This is an auxiliary class which corresponds to an existential entry in the
 * extended ontology.
 * 
 * @author Julian Mendez
 */
class ExistentialEntry implements ExtensionEntry {

	private Integer classId = null;
	private int hashcode = 0;
	private Integer propertyId = null;

	/**
	 * Constructs a new existential entry.
	 * 
	 * @param property
	 *            object property identifier
	 * @param cls
	 *            class identifier
	 */
	public ExistentialEntry(Integer property, Integer cls) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (cls == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.propertyId = property;
		this.classId = cls;
		this.hashcode = 31 * this.classId.hashCode()
				+ this.propertyId.hashCode();
	}

	@Override
	public ExistentialEntry asExistential() {
		return this;
	}

	@Override
	public ImplicationEntry asImplication() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ExistentialEntry) {
			ExistentialEntry other = (ExistentialEntry) o;
			ret = getClassId().equals(other.getClassId())
					&& getPropertyId().equals(other.getPropertyId());
		}
		return ret;
	}

	/**
	 * Returns the class in this entry.
	 * 
	 * @return the class in this entry
	 */
	public Integer getClassId() {
		return this.classId;
	}

	/**
	 * Returns the object property in this entry.
	 * 
	 * @return the object property in this entry
	 */
	public Integer getPropertyId() {
		return this.propertyId;
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean isExistential() {
		return true;
	}

	@Override
	public boolean isImplication() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(E ");
		sbuf.append(getPropertyId());
		sbuf.append(".");
		sbuf.append(getClassId());
		sbuf.append(")");
		return sbuf.toString();
	}

}
