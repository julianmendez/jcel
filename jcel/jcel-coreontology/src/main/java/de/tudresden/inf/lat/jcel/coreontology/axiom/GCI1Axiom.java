/*
 *
 * Copyright 2009-2013 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>A<sub>1</sub> &#8851; A<sub>2</sub> &#8849; B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI1Axiom implements NormalizedIntegerAxiom {

	private final int hashCode;
	private final int leftSubClass;
	private final int rightSubClass;
	private final int superClass;

	/**
	 * Constructs a new GCI-1 axiom.
	 * 
	 * @param leftSubCl
	 *            left subclass in the axiom
	 * @param rightSubCl
	 *            right subclass in the axiom
	 * @param rightCl
	 *            superclass in the axiom
	 */
	protected GCI1Axiom(int leftSubCl, int rightSubCl, int rightCl) {
		this.leftSubClass = leftSubCl;
		this.rightSubClass = rightSubCl;
		this.superClass = rightCl;
		this.hashCode = this.leftSubClass
				+ (31 * this.rightSubClass + (31 * this.superClass));
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
			ret = (this.leftSubClass == other.leftSubClass)
					&& (this.rightSubClass == other.rightSubClass)
					&& (this.superClass == other.superClass);
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(this.leftSubClass);
		ret.add(this.rightSubClass);
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

	/**
	 * Returns the left subclass in the axiom.
	 * 
	 * @return the left subclass in the axiom
	 */
	public int getLeftSubClass() {
		return this.leftSubClass;
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.emptySet();
	}

	/**
	 * Returns the right subclass in the axiom.
	 * 
	 * @return the right subclass in the axiom
	 */
	public int getRightSubClass() {
		return this.rightSubClass;
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
		sbuf.append(getLeftSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getRightSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
