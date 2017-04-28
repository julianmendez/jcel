/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * This is the default implementation of {@link GCI3Axiom}.
 * 
 * @author Julian Mendez
 * 
 */
public class GCI3AxiomImpl implements GCI3Axiom {

	private final int classInSubClass;
	private final int propertyInSubClass;
	private final int superClass;
	private final Set<IntegerAnnotation> annotations;
	private final int hashCode;

	/**
	 * Constructs a new GCI-3 axiom.
	 * 
	 * @param leftPropertyId
	 *            object property identifier for the left-hand part
	 * @param leftClassId
	 *            class identifier for the left-hand part
	 * @param rightClassId
	 *            superclass identifier
	 * @param annotations
	 *            annotations
	 */
	GCI3AxiomImpl(int leftPropertyId, int leftClassId, int rightClassId, Set<IntegerAnnotation> annotations) {
		Objects.requireNonNull(annotations);
		this.classInSubClass = leftClassId;
		this.propertyInSubClass = leftPropertyId;
		this.superClass = rightClassId;
		this.annotations = annotations;
		this.hashCode = this.classInSubClass
				+ 0x1F * (this.propertyInSubClass + 0x1F * (this.superClass + 0x1F * this.annotations.hashCode()));
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof GCI3Axiom)) {
			return false;
		} else {
			GCI3Axiom other = (GCI3Axiom) obj;
			return (getClassInSubClass() == other.getClassInSubClass())
					&& (getPropertyInSubClass() == other.getPropertyInSubClass())
					&& (getSuperClass() == other.getSuperClass()) && getAnnotations().equals(other.getAnnotations());
		}
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<>();
		ret.add(this.classInSubClass);
		ret.add(this.superClass);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public int getClassInSubClass() {
		return this.classInSubClass;
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
		return Collections.singleton(this.propertyInSubClass);
	}

	@Override
	public int getPropertyInSubClass() {
		return this.propertyInSubClass;
	}

	@Override
	public int getSuperClass() {
		return this.superClass;
	}

	@Override
	public Set<IntegerAnnotation> getAnnotations() {
		return Collections.unmodifiableSet(this.annotations);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.GCI3);
		sbuf.append(NormalizedIntegerAxiomConstant.LEFT_PAR);
		sbuf.append(IntegerClassExpressionWord.ObjectSomeValuesFrom);
		sbuf.append(NormalizedIntegerAxiomConstant.LEFT_PAR);
		sbuf.append(getPropertyInSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.SP);
		sbuf.append(getClassInSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.RIGHT_PAR);
		sbuf.append(NormalizedIntegerAxiomConstant.SP);
		sbuf.append(getSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.RIGHT_PAR);
		return sbuf.toString();
	}

}
