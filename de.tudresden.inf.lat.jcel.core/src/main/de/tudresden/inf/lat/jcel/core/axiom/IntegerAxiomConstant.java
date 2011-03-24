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

import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpressionWord;

/**
 * This interface is a collection of constants used by classes that implement an
 * <code>IntegerAxiom</code>.
 * 
 * @author Julian Mendez
 * 
 * @see IntegerAxiom
 */

public interface IntegerAxiomConstant {
	public static final String closePar = IntegerClassExpressionWord.closePar;
	public static final Object DisjointClasses = "DisjointClasses";
	public static final Object emptyProp = "()";
	public static final String EquivalentClasses = "EquivalentClasses";
	public static final Object EquivalentProperties = "EquivalentProperties";
	public static final String GCI0 = "SubClassOf*";
	public static final String GCI1 = "SubClassOf*";
	public static final String GCI2 = "SubClassOf*";
	public static final String GCI3 = "SubClassOf*";
	public static final Object NormalizedRangeAxiom = "RangeAxiom*";
	public static final Object ObjectPropertyChain = "ObjectPropertyChain";
	public static final String openPar = IntegerClassExpressionWord.openPar;
	public static final Object RangeAxiom = "RangeAxiom";
	public static final String RI1 = "SubObjectPropertyOf*";
	public static final String RI2 = "SubObjectPropertyOf*";
	public static final String RI3 = "SubObjectPropertyOf*";
	public static final String sp = IntegerClassExpressionWord.sp;
	public static final String SubClassOf = "SubClassOf";
	public static final String SubObjectPropertyOf = "SubObjectPropertyOf";
	public static final Object TransitiveAxiom = "TransitiveObjectProperty";
}
