/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.core.graph;

/**
 * This class implements an existential expression.
 * 
 * @author Julian Mendez
 * 
 * @see VNodeObjectSomeValuesFrom
 */
public class VNodeObjectSomeValuesFromImpl implements VNodeObjectSomeValuesFrom {

	private final int classId;
	private final int hashCode;
	private final int objectPropertyId;

	/**
	 * Constructs a new existential expression.
	 * 
	 * @param objectProp
	 *            object property identifier
	 * @param cls
	 *            class identifier
	 */
	public VNodeObjectSomeValuesFromImpl(int objectProp, int cls) {
		this.objectPropertyId = objectProp;
		this.classId = cls;
		this.hashCode = this.objectPropertyId + (31 * this.classId);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof VNodeObjectSomeValuesFrom)) {
			VNodeObjectSomeValuesFrom other = (VNodeObjectSomeValuesFrom) o;
			ret = (this.objectPropertyId == other.getObjectPropertyId()) && (this.classId == other.getClassId());
		}
		return ret;
	}

	@Override
	public int getClassId() {
		return this.classId;
	}

	@Override
	public int getObjectPropertyId() {
		return this.objectPropertyId;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
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
