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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * This class models an intersection of several class expressions, this is: C
 * <sub>1</sub> \u2293 &hellip; \u2293 C<sub>n</sub>
 * 
 * @author Julian Mendez
 * 
 */
public class IntegerObjectIntersectionOf implements IntegerClassExpression {

	private final int hashCode;
	private final boolean normalized;
	private final Set<IntegerClassExpression> operands;
	private final boolean withBottom;

	/**
	 * Constructs an intersection of class expressions.
	 * 
	 * @param operands
	 *            set of class expressions
	 */
	protected IntegerObjectIntersectionOf(Set<IntegerClassExpression> operands) {
		Objects.requireNonNull(operands);
		this.operands = operands;
		this.hashCode = operands.hashCode();
		boolean normalized = true;
		boolean withBottom = false;
		for (IntegerClassExpression elem : this.operands) {
			if (elem.containsBottom()) {
				withBottom = true;
			}
			if (!elem.isLiteral()) {
				normalized = false;
			}
		}
		this.normalized = normalized;
		this.withBottom = withBottom;
	}

	@Override
	public <T> T accept(IntegerClassExpressionVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean containsBottom() {
		return this.withBottom;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerObjectIntersectionOf)) {
			IntegerObjectIntersectionOf other = (IntegerObjectIntersectionOf) o;
			ret = getOperands().equals(other.getOperands()) && (containsBottom() == other.containsBottom());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<>();
		getOperands().forEach(expression -> ret.addAll(expression.getClassesInSignature()));
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
		Set<Integer> ret = new HashSet<>();
		getOperands().forEach(expression -> ret.addAll(expression.getObjectPropertiesInSignature()));
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Return the operands in this class expression.
	 * 
	 * @return the operands in this class expression
	 */
	public Set<IntegerClassExpression> getOperands() {
		return Collections.unmodifiableSet(this.operands);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean hasOnlyClasses() {
		return this.normalized;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.ObjectIntersectionOf);
		sbuf.append(IntegerClassExpressionWord.LEFT_PAR);
		for (Iterator<IntegerClassExpression> it = getOperands().iterator(); it.hasNext();) {
			IntegerClassExpression classExpression = it.next();
			sbuf.append(classExpression.toString());
			if (it.hasNext()) {
				sbuf.append(" ");
			}
		}
		sbuf.append(IntegerClassExpressionWord.RIGHT_PAR);
		return sbuf.toString();
	}

}
