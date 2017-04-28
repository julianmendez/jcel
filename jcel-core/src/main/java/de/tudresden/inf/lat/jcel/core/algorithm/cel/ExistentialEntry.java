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

package de.tudresden.inf.lat.jcel.core.algorithm.cel;

import java.util.Objects;
import java.util.Optional;

/**
 * This is an auxiliary class which corresponds to an existential entry in the
 * extended ontology.
 * 
 * @author Julian Mendez
 */
class ExistentialEntry implements ExtensionEntry {

	private final Integer classId;
	private final int hashcode;
	private final Integer propertyId;

	/**
	 * Constructs a new existential entry.
	 * 
	 * @param property
	 *            object property identifier
	 * @param cls
	 *            class identifier
	 */
	public ExistentialEntry(Integer property, Integer cls) {
		Objects.requireNonNull(property);
		Objects.requireNonNull(cls);
		this.propertyId = property;
		this.classId = cls;
		this.hashcode = (31 * this.classId.hashCode()) + this.propertyId.hashCode();
	}

	@Override
	public Optional<ExistentialEntry> asExistential() {
		return Optional.of(this);
	}

	@Override
	public Optional<ImplicationEntry> asImplication() {
		return Optional.empty();
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ExistentialEntry) {
			ExistentialEntry other = (ExistentialEntry) o;
			ret = getClassId().equals(other.getClassId()) && getPropertyId().equals(other.getPropertyId());
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
		return this.hashcode;
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
