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

import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;

/**
 * An object of this class is a relation entry and can be used by a relation
 * observer.
 * 
 * @author Julian Mendez
 * 
 * @see RObserverRule
 */
public class REntryImpl implements REntry, Comparable<REntry> {

	private final int hashCode;
	private final int leftClass;
	private final int property;
	private final int rightClass;

	/**
	 * Constructs a new relation entry (R-entry).
	 * 
	 * @param prop
	 *            object property identifier
	 * @param leftCl
	 *            class identifier of left component
	 * @param rightCl
	 *            class identifier of right component
	 */
	public REntryImpl(int prop, int leftCl, int rightCl) {
		this.property = prop;
		this.leftClass = leftCl;
		this.rightClass = rightCl;
		this.hashCode = this.property + (31 * (this.leftClass + (31 * this.rightClass)));
	}

	@Override
	public int compareTo(REntry other) {
		Objects.requireNonNull(other);
		int ret = this.property - other.getProperty();
		if (ret == 0) {
			ret = this.leftClass - other.getLeftClass();
		}
		if (ret == 0) {
			ret = this.rightClass - other.getRightClass();
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof REntry)) {
			REntry other = (REntry) o;
			ret = (this.property == other.getProperty()) && (this.leftClass == other.getLeftClass())
					&& (this.rightClass == other.getRightClass());
		}
		return ret;
	}

	@Override
	public int getLeftClass() {
		return this.leftClass;
	}

	@Override
	public int getProperty() {
		return this.property;
	}

	@Override
	public int getRightClass() {
		return this.rightClass;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean isREntry() {
		return true;
	}

	@Override
	public boolean isSEntry() {
		return false;
	}

	@Override
	public String toString() {
		return "(" + getProperty() + " " + getLeftClass() + " " + getRightClass() + ")";
	}

}
