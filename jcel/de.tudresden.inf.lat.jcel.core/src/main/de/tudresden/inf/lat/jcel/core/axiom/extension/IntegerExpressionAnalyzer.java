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

package de.tudresden.inf.lat.jcel.core.axiom.extension;

import java.util.Set;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpressionVisitor;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDataHasValue;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDataSomeValuesFrom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerInverseObjectProperty;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectOneOf;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectPropertyExpressionVisitor;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object implementing this class analyzes an object property expression or a
 * class expression to detect what constructors are used in it.
 * 
 * @author Julian Mendez
 */
class IntegerExpressionAnalyzer implements
		IntegerClassExpressionVisitor<Boolean>,
		IntegerObjectPropertyExpressionVisitor<Boolean> {

	private boolean hasDatatype = false;
	private boolean hasInverseObjectProperty = false;
	private boolean hasNominal = false;

	public IntegerExpressionAnalyzer() {
	}

	public boolean hasDatatype() {
		return this.hasDatatype;
	}

	public boolean hasInverseObjectProperty() {
		return this.hasInverseObjectProperty;
	}

	public boolean hasNominal() {
		return this.hasNominal;
	}

	@Override
	public Boolean visit(IntegerClass classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerDataHasValue classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasDatatype = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDataSomeValuesFrom classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasDatatype = true;
		return classExpression.getFiller().accept(this);
	}

	@Override
	public Boolean visit(IntegerInverseObjectProperty objectPropertyExpression) {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasInverseObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectIntersectionOf classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visit(classExpression.getOperands());
	}

	@Override
	public Boolean visit(IntegerObjectOneOf classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasNominal = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectProperty objectPropertyExpression) {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerObjectSomeValuesFrom classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean fillerAcc = classExpression.getFiller().accept(this);
		boolean propAcc = classExpression.getProperty().accept(this);
		return fillerAcc && propAcc;
	}

	protected Boolean visit(Set<IntegerClassExpression> classSet) {
		boolean ret = true;
		for (IntegerClassExpression expr : classSet) {
			boolean accepted = expr.accept(this);
			ret = ret && accepted;
		}
		return ret;
	}

}
