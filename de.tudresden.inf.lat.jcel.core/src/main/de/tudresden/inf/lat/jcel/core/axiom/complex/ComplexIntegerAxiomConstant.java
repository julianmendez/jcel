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

package de.tudresden.inf.lat.jcel.core.axiom.complex;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpressionWord;

/**
 * This interface is a collection of constants used by classes that implement an
 * <code>ComplexIntegerAxiom</code>.
 * 
 * @author Julian Mendez
 * 
 * @see ComplexIntegerAxiom
 */

public interface ComplexIntegerAxiomConstant {
	public static final String ClassDeclaration = "Class";
	public static final String closePar = IntegerClassExpressionWord.closePar;
	public static final String Declaration = "Declaration";
	public static final String DisjointClasses = "DisjointClasses";
	public static final String EquivalentClasses = "EquivalentClasses";
	public static final String EquivalentProperties = "EquivalentProperties";
	public static final String ObjectPropertyChain = "ObjectPropertyChain";
	public static final String ObjectPropertyDeclaration = "ObjectProperty";
	public static final String openPar = IntegerClassExpressionWord.openPar;
	public static final String RangeAxiom = "RangeAxiom";
	public static final String sp = IntegerClassExpressionWord.sp;
	public static final String SubClassOf = "SubClassOf";
	public static final String TransitiveObjectProperty = "TransitiveObjectProperty";
}
