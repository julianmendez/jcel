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

package de.tudresden.inf.lat.jcel.ontology.axiom.normalized;

import java.util.Collections;
import java.util.Set;

/**
 * Axiom of the form:
 * <ul>
 * <li>range(r) &#8849; A</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RangeAxiom implements NormalizedIntegerAxiom {

	private final int hashCode;
	private final int property;
	private final int range;

	/**
	 * Constructs a new range axiom.
	 * 
	 * @param prop
	 *            object property identifier
	 * @param cl
	 *            class identifier
	 */
	protected RangeAxiom(int prop, int cl) {
		this.property = prop;
		this.range = cl;
		this.hashCode = this.property + 31 * this.range;
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof RangeAxiom) {
			RangeAxiom other = (RangeAxiom) o;
			ret = (this.property == other.property)
					&& (this.range == other.range);
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.singleton(this.range);
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.singleton(this.property);
	}

	/**
	 * Returns the object property in the axiom.
	 * 
	 * @return the object property in the axiom
	 */
	public int getProperty() {
		return this.property;
	}

	/**
	 * Returns the class identifier in the axiom.
	 * 
	 * @return the class identifier in the axiom
	 */
	public int getRange() {
		return this.range;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.NormalizedRangeAxiom);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getRange());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
