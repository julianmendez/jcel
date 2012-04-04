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

package de.tudresden.inf.lat.jcel.core.completion.common;

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
		this.hashCode = this.subClass + 31 * this.superClass;
	}

	@Override
	public int compareTo(SEntry other) {
		if (other == null) {
			throw new NullPointerException("Null argument.");
		}

		int ret = this.subClass - other.getSubClass();
		if (ret == 0) {
			ret = this.superClass - other.getSuperClass();
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof SEntry) {
			SEntry other = (SEntry) o;
			ret = (this.subClass == other.getSubClass())
					&& (this.superClass == other.getSuperClass());
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
