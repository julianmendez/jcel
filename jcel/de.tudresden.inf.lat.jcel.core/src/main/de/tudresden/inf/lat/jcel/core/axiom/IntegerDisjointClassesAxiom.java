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

import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;

/**
 * This class models an axiom stating that the contained classes are pairwise
 * disjoint. <br />
 * This is: A<sub>1</sub> &cap; A<sub>2</sub> &sube; &#8869;, A<sub>1</sub>
 * &cap; A<sub>3</sub> &sube; &#8869;, A<sub>2</sub> &cap; A<sub>3</sub> &sube;
 * &#8869;, &hellip; , A<sub>n-1</sub> &cap; A<sub>n</sub> &sube; &#8869;
 * 
 * @author Julian Mendez
 */
public class IntegerDisjointClassesAxiom implements IntegerAxiom {

	private Set<IntegerDescription> descriptionSet = null;

	public IntegerDisjointClassesAxiom(Set<IntegerDescription> descSet) {
		if (descSet == null) {
			throw new IllegalArgumentException("Null parameters received.");
		}
		this.descriptionSet = descSet;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerDisjointClassesAxiom) {
			IntegerDisjointClassesAxiom other = (IntegerDisjointClassesAxiom) o;
			ret = getDescriptions().equals(other.getDescriptions());
		}
		return ret;
	}

	public Set<IntegerDescription> getDescriptions() {
		return this.descriptionSet;
	}

	@Override
	public int hashCode() {
		return getDescriptions().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.DisjointClasses);
		sbuf.append(IntegerAxiomConstant.openPar);
		Set<IntegerDescription> descriptionSet = getDescriptions();
		for (IntegerDescription description : descriptionSet) {
			sbuf.append(description.toString());
			sbuf.append(IntegerAxiomConstant.sp);
		}
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
