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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.Objects;

import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;

/**
 * An object of this class is a subsumer entry and can be used by a subsumer
 * observer.
 * 
 * @author Julian Mendez
 * 
 * @see SObserverRule
 */
public class SEntryImpl implements SEntry, Comparable<SEntry> {

	private final int hashCode;
	private final int subClass;
	private final int superClass;

	/**
	 * Constructs a new subsumer entry (S-entry).
	 * 
	 * @param subCl
	 *            subclass identifier
	 * @param superCl
	 *            superclass identifier
	 */
	public SEntryImpl(int subCl, int superCl) {
		this.subClass = subCl;
		this.superClass = superCl;
		this.hashCode = this.subClass + (31 * this.superClass);
	}

	@Override
	public int compareTo(SEntry other) {
		Objects.requireNonNull(other);
		int ret = this.subClass - other.getSubClass();
		if (ret == 0) {
			ret = this.superClass - other.getSuperClass();
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof SEntry)) {
			SEntry other = (SEntry) o;
			ret = (this.subClass == other.getSubClass()) && (this.superClass == other.getSuperClass());
		}
		return ret;
	}

	@Override
	public int getSubClass() {
		return this.subClass;
	}

	@Override
	public int getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean isREntry() {
		return false;
	}

	@Override
	public boolean isSEntry() {
		return true;
	}

	@Override
	public String toString() {
		return "(" + getSubClass() + " " + getSuperClass() + ")";
	}

}
