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
 * <li>&#1013; &#8849; r</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RI1Axiom implements NormalizedIntegerAxiom {

	private final Integer superProperty;

	/**
	 * Constructs a new axiom RI-1.
	 * 
	 * @param prop
	 *            object property identifier
	 */
	protected RI1Axiom(Integer prop) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.superProperty = prop;
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
		boolean ret = false;
		if (o instanceof RI1Axiom) {
			RI1Axiom other = (RI1Axiom) o;
			ret = getSuperProperty().equals(other.getSuperProperty());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.emptySet();
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
		return Collections.singleton(this.superProperty);
	}

	/**
	 * Returns the object property in the axiom.
	 * 
	 * @return the object property in the axiom
	 */
	public Integer getSuperProperty() {
		return this.superProperty;
	}

	@Override
	public int hashCode() {
		return getSuperProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.RI1);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(NormalizedIntegerAxiomConstant.emptyProp);
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
