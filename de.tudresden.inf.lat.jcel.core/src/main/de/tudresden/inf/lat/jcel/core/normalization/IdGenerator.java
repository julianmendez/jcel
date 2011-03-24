/*
 * Copyright 2009 Julian Mendez
 *
 *
 *  This file is part of jcel.
 *
 *  jcel is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jcel is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.core.normalization;

/**
 * An object of this class generates new identification numbers for properties
 * and classes.
 * 
 * @author Julian Mendez
 * 
 */
public class IdGenerator {

	// private static final Logger logger = Logger.getLogger(NameGenerator.class
	// .getName());

	private Integer firstClassId = null;
	private Integer firstPropertyId = null;
	private Integer nextClassId = null;
	private Integer nextPropertyId = null;

	public IdGenerator(Integer propertyOffset, Integer classOffset) {
		this.firstPropertyId = propertyOffset;
		this.firstClassId = classOffset;
		this.nextPropertyId = this.firstPropertyId;
		this.nextClassId = this.firstClassId;
	}

	public Integer createNewClassId() {
		Integer ret = getNextClassId();
		this.nextClassId++;
		return ret;
	}

	public Integer createNewPropertyId() {
		Integer ret = getNextPropertyId();
		this.nextPropertyId++;
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IdGenerator) {
			IdGenerator other = (IdGenerator) o;
			ret = getFirstClassId().equals(other.getFirstClassId())
					&& getFirstPropertyId().equals(other.getFirstPropertyId())
					&& getNextClassId().equals(other.getNextClassId())
					&& getNextPropertyId().equals(other.getNextPropertyId());
		}
		return ret;
	}

	public Integer getFirstClassId() {
		return this.firstClassId;
	}

	public Integer getFirstPropertyId() {
		return this.firstPropertyId;
	}

	public Integer getNextClassId() {
		return this.nextClassId;
	}

	public Integer getNextPropertyId() {
		return this.nextPropertyId;
	}

	@Override
	public int hashCode() {
		return this.firstClassId + this.nextClassId;
	}

	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(");
		sbuf.append(getFirstClassId());
		sbuf.append("-");
		sbuf.append(getNextClassId());
		sbuf.append(" , ");
		sbuf.append(getFirstPropertyId());
		sbuf.append("-");
		sbuf.append(getNextPropertyId());
		sbuf.append(")");
		return sbuf.toString();
	}
}
