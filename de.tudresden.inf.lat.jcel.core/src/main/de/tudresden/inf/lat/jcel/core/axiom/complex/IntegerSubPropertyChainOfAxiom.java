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

package de.tudresden.inf.lat.jcel.core.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class models an axiom stating that the contained properties form a
 * subsumption chain. This is: r<sub>1</sub> &#8728; r<sub>2</sub>
 * &#8728;&hellip; &#8728; r<sub>n</sub> &#8849; s
 * 
 * @author Julian Mendez
 */
public class IntegerSubPropertyChainOfAxiom implements ComplexIntegerAxiom {

	private Set<Integer> objectPropertiesInSignature = null;
	private List<Integer> propertyChain = null;
	private Integer superProperty = null;

	public IntegerSubPropertyChainOfAxiom(List<Integer> chain, Integer superProp) {
		if (chain == null || superProp == null) {
			throw new IllegalArgumentException("Null values used.");
		}
		this.propertyChain = chain;
		this.superProperty = superProp;

		this.objectPropertiesInSignature = new HashSet<Integer>();
		this.objectPropertiesInSignature.addAll(getPropertyChain());
		this.objectPropertiesInSignature.add(getSuperProperty());
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerSubPropertyChainOfAxiom) {
			IntegerSubPropertyChainOfAxiom other = (IntegerSubPropertyChainOfAxiom) o;
			ret = getPropertyChain().equals(other.getPropertyChain())
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

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.unmodifiableSet(this.objectPropertiesInSignature);
	}

	public List<Integer> getPropertyChain() {
		return Collections.unmodifiableList(this.propertyChain);
	}

	public Integer getSuperProperty() {
		return this.superProperty;
	}

	@Override
	public int hashCode() {
		return getPropertyChain().hashCode() + 31
				* getSuperProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.ObjectPropertyChain);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		List<Integer> propertyList = getPropertyChain();
		for (Integer property : propertyList) {
			sbuf.append(property);
			sbuf.append(ComplexIntegerAxiomConstant.sp);
		}
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
