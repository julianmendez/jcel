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

package de.tudresden.inf.lat.jcel.core.classifier;

/**
 * This class models an existential restriction with an atomic class, this is:
 * &exists r <i>.</i> A , where A is atomic. Objects of this class are used as
 * keys when the ontology is extended. This is an auxiliary class used by
 * <code>ExtendedOntology</code>.
 * 
 * @author Julian Mendez
 * 
 * @see ExtendedOntology
 */
class ExistentialKey {

	private Integer classId = null;
	private int hashcode = 0;
	private Integer propertyId = null;

	public ExistentialKey(Integer prop, Integer c) {
		this.classId = c;
		this.propertyId = prop;
		this.hashcode = 31 * this.classId.hashCode()
				+ this.propertyId.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ExistentialKey) {
			ExistentialKey other = (ExistentialKey) o;
			ret = getClassId().equals(other.getClassId())
					&& getPropertyId().equals(other.getPropertyId());
		}
		return ret;
	}

	public Integer getClassId() {
		return this.classId;
	}

	public Integer getPropertyId() {
		return this.propertyId;
	}

	@Override
	public int hashCode() {
		return this.hashcode;
	}

	@Override
	public String toString() {
		return getPropertyId().toString() + " " + getClassId().toString();
	}
}
