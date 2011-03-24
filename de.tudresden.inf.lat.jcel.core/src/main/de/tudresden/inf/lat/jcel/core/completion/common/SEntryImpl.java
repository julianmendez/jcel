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
 * @see SObserverRule
 * 
 * @author Julian Mendez
 */
public class SEntryImpl implements SEntry, Comparable<SEntryImpl> {

	private Integer subClass = null;
	private Integer superClass = null;

	public SEntryImpl(Integer subCl, Integer superCl) {
		if (subCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subClass = subCl;
		this.superClass = superCl;
	}

	@Override
	public int compareTo(SEntryImpl other) {
		if (other == null) {
			throw new NullPointerException("Null argument.");
		}

		int ret = this.subClass - other.subClass;
		if (ret == 0) {
			ret = this.superClass - other.superClass;
		}
		return ret;
	}

	@Override
	public Integer getSubClass() {
		return this.subClass;
	}

	@Override
	public Integer getSuperClass() {
		return this.superClass;
	}

	@Override
	public boolean isREntry() {
		return false;
	}

	@Override
	public boolean isSEntry() {
		return true;
	}

}
