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

package de.tudresden.inf.lat.jcel.core.graph;

/**
 * This class implements an existential expression.
 * 
 * @see VNodeObjectSomeValuesFrom
 * 
 * @author Julian Mendez
 */
public class VNodeObjectSomeValuesFromImpl implements VNodeObjectSomeValuesFrom {

	private final Integer classId;
	private final Integer objectPropertyId;

	/**
	 * Constructs a new existential expression.
	 * 
	 * @param objectProp
	 *            object property identifier
	 * @param cls
	 *            class identifier
	 */
	public VNodeObjectSomeValuesFromImpl(Integer objectProp, Integer cls) {
		if (objectProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (cls == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.objectPropertyId = objectProp;
		this.classId = cls;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof VNodeObjectSomeValuesFrom) {
			VNodeObjectSomeValuesFrom other = (VNodeObjectSomeValuesFrom) o;
			ret = getObjectPropertyId().equals(other.getObjectPropertyId())
					&& getClassId().equals(other.getClassId());
		}
		return ret;
	}

	@Override
	public Integer getClassId() {
		return this.classId;
	}

	@Override
	public Integer getObjectPropertyId() {
		return this.objectPropertyId;
	}

	@Override
	public int hashCode() {
		return this.objectPropertyId + 31 * this.classId;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("exists ");
		ret.append(getObjectPropertyId());
		ret.append(".");
		ret.append(getClassId());
		return ret.toString();
	}

}
