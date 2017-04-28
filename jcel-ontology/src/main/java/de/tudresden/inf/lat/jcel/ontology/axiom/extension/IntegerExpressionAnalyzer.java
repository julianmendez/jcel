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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpressionVisitor;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataHasValue;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataSomeValuesFrom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectInverseOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectOneOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object implementing this class analyzes an object property expression or a
 * class expression to detect what constructors are used in it.
 * 
 * @author Julian Mendez
 */
public class IntegerExpressionAnalyzer implements IntegerClassExpressionVisitor<Boolean> {

	private boolean hasBottom = false;
	private boolean hasDatatype = false;
	private boolean hasInverseObjectProperty = false;
	private boolean hasNominal = false;

	/**
	 * Constructs a new expression analyzer.
	 */
	public IntegerExpressionAnalyzer() {
	}

	/**
	 * Tells whether the expression analyzer has detected bottom.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected bottom
	 */
	public boolean hasBottom() {
		return this.hasBottom;
	}

	/**
	 * Tells whether the expression analyzer has detected data types.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected data types
	 */
	public boolean hasDatatype() {
		return this.hasDatatype;
	}

	/**
	 * Tells whether the expression analyzer has detected inverse object
	 * properties.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected inverse object properties
	 */
	public boolean hasInverseObjectProperty() {
		return this.hasInverseObjectProperty;
	}

	/**
	 * Tells whether the expression analyzer has detected nominals.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected nominals
	 */
	public boolean hasNominal() {
		return this.hasNominal;
	}

	@Override
	public Boolean visit(IntegerClass classExpression) {
		Objects.requireNonNull(classExpression);
		this.hasBottom |= (classExpression.getId() == IntegerEntityManager.bottomClassId);
		return true;
	}

	@Override
	public Boolean visit(IntegerDataHasValue classExpression) {
		Objects.requireNonNull(classExpression);
		this.hasDatatype = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDataSomeValuesFrom classExpression) {
		Objects.requireNonNull(classExpression);
		this.hasDatatype = true;
		return classExpression.getFiller().accept(this);
	}

	@Override
	public Boolean visit(IntegerNamedIndividual namedIndividual) {
		Objects.requireNonNull(namedIndividual);
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectIntersectionOf classExpression) {
		Objects.requireNonNull(classExpression);
		return visit(classExpression.getOperands());
	}

	@Override
	public Boolean visit(IntegerObjectOneOf classExpression) {
		Objects.requireNonNull(classExpression);
		this.hasNominal = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectSomeValuesFrom classExpression) {
		Objects.requireNonNull(classExpression);
		boolean fillerAcc = classExpression.getFiller().accept(this);
		IntegerObjectPropertyExpression prop = classExpression.getProperty();
		if (prop instanceof IntegerObjectInverseOf) {
			this.hasInverseObjectProperty = true;
		}
		return fillerAcc;
	}

	/**
	 * Visits a set of class expressions.
	 * 
	 * @param classSet
	 *            set of class expressions
	 * @return the visit result
	 */
	protected Boolean visit(Set<IntegerClassExpression> classSet) {
		Objects.requireNonNull(classSet);
		return classSet.stream().map(expr -> expr.accept(this)).reduce(true, (accum, elem) -> (accum && elem));
	}

}
