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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.Collections;
import java.util.Set;

/**
 * This class models an axiom saying that two or more individuals are the same.
 * There is no unique name assumption.
 * 
 * @author Julian Mendez
 */
public class IntegerSameIndividualAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> individuals;

	/**
	 * Constructs a new same individual axiom.
	 * 
	 * @param individualSet
	 *            set of individuals declared to be the same
	 */
	protected IntegerSameIndividualAxiom(Set<Integer> individualSet) {
		if (individualSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.individuals = Collections.unmodifiableSet(individualSet);
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerSameIndividualAxiom) {
			IntegerSameIndividualAxiom other = (IntegerSameIndividualAxiom) o;
			ret = getIndividuals().equals(other.getIndividuals());
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

	/**
	 * Returns the set of individuals in this axiom.
	 * 
	 * @return the set of individuals in this axiom
	 */
	public Set<Integer> getIndividuals() {
		return Collections.unmodifiableSet(this.individuals);
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.unmodifiableSet(this.individuals);
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public int hashCode() {
		return getIndividuals().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.SameIndividual);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		Set<Integer> individualSet = getIndividuals();
		for (Integer individual : individualSet) {
			sbuf.append(individual.toString());
			sbuf.append(ComplexIntegerAxiomConstant.sp);
		}
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
