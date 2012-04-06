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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpressionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>A<sub>1</sub> &#8851; &hellip; &#8851; A<sub>n</sub> &#8849; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI1Axiom implements NormalizedIntegerAxiom {

	private final int hashCode;
	private final List<Integer> operands;
	private final int superClass;

	/**
	 * Constructs a new GCI-1 axiom.
	 * 
	 * @param leftClList
	 *            list of class identifier for the part of the left-hand side
	 * @param rightCl
	 *            superclass in the axiom
	 */
	protected GCI1Axiom(List<Integer> leftClList, int rightCl) {
		if (leftClList == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.operands = leftClList;
		this.superClass = rightCl;
		this.hashCode = this.operands.hashCode() + 31 * this.superClass;
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
		if (!ret && o instanceof GCI1Axiom) {
			GCI1Axiom other = (GCI1Axiom) o;
			ret = (this.superClass == other.superClass)
					&& this.operands.equals(other.operands);
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.addAll(this.operands);
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

	/**
	 * Returns the list of operands on the left-hand side of the axiom.
	 * 
	 * @return the list of operands in the axiom.
	 */
	public List<Integer> getOperands() {
		return Collections.unmodifiableList(this.operands);
	}

	/**
	 * Returns the superclass in the axiom.
	 * 
	 * @return the superclass in the axiom
	 */
	public int getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.GCI1);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(IntegerClassExpressionWord.ObjectIntersectionOf);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		for (Iterator<Integer> it = getOperands().iterator(); it.hasNext();) {
			Integer currentId = it.next();
			sbuf.append(currentId);
			if (it.hasNext()) {
				sbuf.append(NormalizedIntegerAxiomConstant.sp);
			}
		}
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
