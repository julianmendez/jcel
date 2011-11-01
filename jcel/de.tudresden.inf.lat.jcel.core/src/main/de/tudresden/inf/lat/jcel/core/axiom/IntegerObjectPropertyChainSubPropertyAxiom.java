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

package de.tudresden.inf.lat.jcel.core.axiom;

import java.util.Collections;
import java.util.List;

/**
 * This class models an axiom stating that the contained properties form a
 * subsumption chain. This is: r<sub>1</sub> <sub><sup>o</sup></sub>
 * r<sub>2</sub> <sub><sup>o</sup></sub>&hellip; r<sub>n-1</sub> &equiv;
 * <sub><sup>o</sup></sub> r<sub>n</sub> &sube; s
 * 
 * @author Julian Mendez
 */
public class IntegerObjectPropertyChainSubPropertyAxiom implements IntegerAxiom {

	private List<Integer> propertyChain = null;
	private Integer superProperty = null;

	public IntegerObjectPropertyChainSubPropertyAxiom(List<Integer> chain,
			Integer superProp) {
		if (chain == null || superProp == null) {
			throw new IllegalArgumentException("Null values used.");
		}
		this.propertyChain = chain;
		this.superProperty = superProp;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectPropertyChainSubPropertyAxiom) {
			IntegerObjectPropertyChainSubPropertyAxiom other = (IntegerObjectPropertyChainSubPropertyAxiom) o;
			ret = getPropertyChain().equals(other.getPropertyChain())
					&& getSuperProperty().equals(other.getSuperProperty());
		}
		return ret;
	}

	public List<Integer> getPropertyChain() {
		return Collections.unmodifiableList(this.propertyChain);
	}

	public Integer getSuperProperty() {
		return this.superProperty;
	}

	@Override
	public int hashCode() {
		return getPropertyChain().hashCode() + getSuperProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.ObjectPropertyChain);
		sbuf.append(IntegerAxiomConstant.openPar);
		List<Integer> propertyList = getPropertyChain();
		for (Integer property : propertyList) {
			sbuf.append(property);
			sbuf.append(IntegerAxiomConstant.sp);
		}
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
