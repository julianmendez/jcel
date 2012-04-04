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
 * An object of this class is a relation entry and can be used by a relation
 * observer.
 * 
 * @author Julian Mendez
 * 
 * @see RObserverRule
 */
public class REntryImpl implements REntry, Comparable<REntryImpl> {

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
		this.hashCode = this.property + 31
				* (this.leftClass + 31 * this.rightClass);
	}

	@Override
	public int compareTo(REntryImpl other) {
		if (other == null) {
			throw new NullPointerException("Null argument.");
		}

		int ret = this.property - other.property;
		if (ret == 0) {
			ret = this.leftClass - other.leftClass;
		}
		if (ret == 0) {
			ret = this.rightClass - other.rightClass;
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof REntry) {
			REntry other = (REntry) o;
			ret = (this.property == other.getProperty())
					&& (this.leftClass == other.getLeftClass())
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
		return "(" + getProperty() + " " + getLeftClass() + " "
				+ getRightClass() + ")";
	}

}
