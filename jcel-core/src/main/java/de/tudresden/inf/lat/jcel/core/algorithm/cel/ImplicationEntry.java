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

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
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
		Objects.requireNonNull(left);
		Objects.requireNonNull(right);
		this.leftPart = left;
		this.superClass = right;
		this.hashcode = (31 * this.leftPart.hashCode()) + this.superClass.hashCode();
	}

	@Override
	public Optional<ExistentialEntry> asExistential() {
		return Optional.empty();
	}

	@Override
	public Optional<ImplicationEntry> asImplication() {
		return Optional.of(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ImplicationEntry) {
			ImplicationEntry other = (ImplicationEntry) o;
			ret = getOperands().equals(other.getOperands()) && getSuperClass().equals(other.getSuperClass());
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
