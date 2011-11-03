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

package de.tudresden.inf.lat.jcel.core.algorithm.cel;

import java.util.Collections;
import java.util.Set;

/**
 * This is an auxiliary class which corresponds to an implication entry in the
 * extended ontology.
 * 
 * @author Julian Mendez
 */
class ImplicationEntry implements ExtensionEntry {

	private final int hashcode;
	private final Set<Integer> leftPart;
	private final Integer superClass;

	/**
	 * Creates an implication entry.
	 * 
	 * @param left
	 *            set of class identifiers
	 * @param right
	 *            class identifier
	 */
	public ImplicationEntry(Set<Integer> left, Integer right) {
		if (left == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (right == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.leftPart = left;
		this.superClass = right;
		this.hashcode = 31 * this.leftPart.hashCode()
				+ this.superClass.hashCode();
	}

	@Override
	public ExistentialEntry asExistential() {
		return null;
	}

	@Override
	public ImplicationEntry asImplication() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ImplicationEntry) {
			ImplicationEntry other = (ImplicationEntry) o;
			ret = getOperands().equals(other.getOperands())
					&& getSuperClass().equals(other.getSuperClass());
		}
		return ret;
	}

	/**
	 * Returns the operands in this entry.
	 * 
	 * @return the operands in this entry
	 */
	public Set<Integer> getOperands() {
		return Collections.unmodifiableSet(this.leftPart);
	}

	public Integer getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return this.hashcode;
	}

	@Override
	public boolean isExistential() {
		return false;
	}

	@Override
	public boolean isImplication() {
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(");
		sbuf.append(getOperands());
		sbuf.append(" --> ");
		sbuf.append(getSuperClass());
		sbuf.append(")");
		return sbuf.toString();
	}

}
