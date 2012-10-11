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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Collections;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * This models a class, this is : A
 * 
 * @author Julian Mendez
 */
public class IntegerClass implements IntegerClassExpression,
		Comparable<IntegerClass> {

	private final int id;

	/**
	 * Constructs a new class.
	 * 
	 * @param n
	 *            class identifier
	 */
	protected IntegerClass(int n) {
		this.id = n;
	}

	@Override
	public <T> T accept(IntegerClassExpressionVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public int compareTo(IntegerClass o) {
		if (o == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getId() - o.getId();
	}

	@Override
	public boolean containsBottom() {
		return IntegerEntityManager.bottomClassId.equals(this.id);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerClass) {
			IntegerClass other = (IntegerClass) o;
			ret = getId() == other.getId();
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.singleton(this.id);
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	/**
	 * Returns the class identifier.
	 * 
	 * @return the class identifier
	 */
	public int getId() {
		return this.id;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean hasOnlyClasses() {
		return true;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public String toString() {
		return ((Integer) getId()).toString();
	}

}
