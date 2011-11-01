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
import java.util.Iterator;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescriptionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>A<sub>1</sub> &cap; &hellip; &cap; A<sub>n</sub> &sube; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI1Axiom implements NormalizedIntegerAxiom {

	private List<Integer> leftClassList = null;
	private Integer superClass = null;

	public GCI1Axiom(List<Integer> leftClList, Integer rightCl) {
		this.leftClassList = leftClList;
		this.superClass = rightCl;
	}

	@Override
	public <T> T accept(IntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof GCI1Axiom) {
			GCI1Axiom other = (GCI1Axiom) o;
			ret = getOperands().equals(other.getOperands())
					&& getSuperClass().equals(other.getSuperClass());
		}
		return ret;
	}

	public List<Integer> getOperands() {
		return Collections.unmodifiableList(this.leftClassList);
	}

	public Integer getSuperClass() {
		return this.superClass;
	}

	@Override
	public int hashCode() {
		return getOperands().hashCode() + getSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerAxiomConstant.GCI1);
		sbuf.append(IntegerAxiomConstant.openPar);
		sbuf.append(IntegerDescriptionWord.ObjectIntersectionOf);
		sbuf.append(IntegerAxiomConstant.openPar);
		for (Iterator<Integer> it = getOperands().iterator(); it.hasNext();) {
			Integer currentId = it.next();
			sbuf.append(currentId);
			if (it.hasNext()) {
				sbuf.append(IntegerAxiomConstant.sp);
			}
		}
		sbuf.append(IntegerAxiomConstant.closePar);
		sbuf.append(IntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(IntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
