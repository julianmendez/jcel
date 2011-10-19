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
import java.util.HashSet;
import java.util.Set;

/**
 * Axiom of the form:
 * <ul>
 * <li>r &#8728; s &#8849; t</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RI3Axiom implements NormalizedIntegerAxiom {

	private final Integer leftSubProperty;
	private final Integer rightSubProperty;
	private final Integer superProperty;

	/**
	 * Constructs a new RI-3 axiom
	 * 
	 * @param leftLeftProp
	 *            object property identifier for the left-hand object property
	 *            on the composition
	 * @param leftRightProp
	 *            object property identifier for the right-hand object property
	 *            on the composition
	 * @param rightProp
	 *            object property identifier for super object property
	 */
	protected RI3Axiom(Integer leftLeftProp, Integer leftRightProp,
			Integer rightProp) {
		if (leftLeftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (leftRightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.leftSubProperty = leftLeftProp;
		this.rightSubProperty = leftRightProp;
		this.superProperty = rightProp;
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
		if (o instanceof RI3Axiom) {
			RI3Axiom other = (RI3Axiom) o;
			ret = getLeftSubProperty().equals(other.getLeftSubProperty())
					&& getRightSubProperty()
							.equals(other.getRightSubProperty())
					&& getSuperProperty().equals(other.getSuperProperty());
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

	/**
	 * Returns the object property on the left-hand part of the composition.
	 * 
	 * @return the object property on the left-hand part of the composition
	 */
	public Integer getLeftSubProperty() {
		return this.leftSubProperty;
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(this.leftSubProperty);
		ret.add(this.rightSubProperty);
		ret.add(this.superProperty);
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Returns the object property on the right-hand part of the composition.
	 * 
	 * @return the object property on the right-hand part of the composition
	 */
	public Integer getRightSubProperty() {
		return this.rightSubProperty;
	}

	/**
	 * Returns the super object property.
	 * 
	 * @return the super object property
	 */
	public Integer getSuperProperty() {
		return this.superProperty;
	}

	@Override
	public int hashCode() {
		return getLeftSubProperty().hashCode() + 31
				* getRightSubProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.RI3);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getLeftSubProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getRightSubProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
