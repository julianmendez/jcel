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

import java.util.Set;

/**
 * This class models an axiom stating that the contained properties are
 * equivalent. <br/>
 * This is: r<sub>1</sub> &equiv; r<sub>2</sub>, r<sub>2</sub> &equiv;
 * r<sub>3</sub>, &hellip; r<sub>n-1</sub> &equiv; r<sub>n</sub>
 * 
 * 
 * @author Julian Mendez
 */
public class IntegerEquivalentObjectPropertiesAxiom implements IntegerAxiom {

	private Set<Integer> propertySet = null;

	public IntegerEquivalentObjectPropertiesAxiom(Set<Integer> propSet) {
		if (propSet == null) {
			throw new IllegalArgumentException("Null parameters received.");
		}
		this.propertySet = propSet;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerEquivalentObjectPropertiesAxiom) {
			IntegerEquivalentObjectPropertiesAxiom other = (IntegerEquivalentObjectPropertiesAxiom) o;
			ret = getProperties().equals(other.getProperties());
		}
		return ret;
	}

	public Set<Integer> getProperties() {
		return this.propertySet;
	}

	@Override
	public int hashCode() {
		return getProperties().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.EquivalentProperties);
		sbuf.append(IntegerAxiomConstant.openPar);
		Set<Integer> propertySet = getProperties();
		for (Integer property : propertySet) {
			sbuf.append(property);
			sbuf.append(IntegerAxiomConstant.sp);
		}
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
