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

package de.tudresden.inf.lat.jcel.core.axiom.normalized;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Axiom of the form:
 * <ul>
 * <li>A &#8849; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI0Axiom implements NormalizedIntegerAxiom {

	private Integer subClass = null;
	private Integer superClass = null;

	public GCI0Axiom(Integer subCl, Integer superCl) {
		if (subCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subClass = subCl;
		this.superClass = superCl;
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
		if (o instanceof GCI0Axiom) {
			GCI0Axiom other = (GCI0Axiom) o;
			ret = getSubClass().equals(other.getSubClass())
					&& getSuperClass().equals(other.getSuperClass());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(this.subClass);
		ret.add(this.superClass);
		return Collections.unmodifiableSet(ret);
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
		return Collections.emptySet();
	}

	public Integer getSubClass() {
		return this.subClass;
	}

	public Integer getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return this.subClass.hashCode() + 31 * this.superClass.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.GCI0);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
