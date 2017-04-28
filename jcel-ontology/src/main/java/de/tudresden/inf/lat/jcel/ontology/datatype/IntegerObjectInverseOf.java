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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * This class models an inverse object property.
 * 
 * @author Julian Mendez
 */
public class IntegerObjectInverseOf implements IntegerObjectPropertyExpression, Comparable<IntegerObjectInverseOf> {

	private final int hashCode;
	private final IntegerObjectProperty invProperty;

	/**
	 * Constructs an inverse object property.
	 * 
	 * @param property
	 *            property to create the inverse
	 */
	protected IntegerObjectInverseOf(IntegerObjectProperty property) {
		Objects.requireNonNull(property);
		this.invProperty = property;
		this.hashCode = property.hashCode();
	}

	@Override
	public <T> T accept(IntegerObjectPropertyExpressionVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public int compareTo(IntegerObjectInverseOf o) {
		Objects.requireNonNull(o);
		return getInverse().compareTo(o.getInverse());
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerObjectInverseOf)) {
			IntegerObjectInverseOf other = (IntegerObjectInverseOf) o;
			ret = getInverse().equals(other.getInverse());
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

	/**
	 * Returns the inverse used for this object property expression
	 * 
	 * @return the inverse used for this object property expression
	 */
	public IntegerObjectProperty getInverse() {
		return this.invProperty;
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return getInverse().getObjectPropertiesInSignature();
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.ObjectInverseOf);
		sbuf.append(IntegerClassExpressionWord.LEFT_PAR);
		sbuf.append(getInverse().toString());
		sbuf.append(IntegerClassExpressionWord.RIGHT_PAR);
		return sbuf.toString();
	}

}
