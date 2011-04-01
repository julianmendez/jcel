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

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpressionWord;

/**
 * This interface is a collection of constants used by classes that implement an
 * <code>NormalizedIntegerAxiom</code>.
 * 
 * @author Julian Mendez
 * 
 * @see NormalizedIntegerAxiom
 */

public interface NormalizedIntegerAxiomConstant {

	public static final String closePar = IntegerClassExpressionWord.closePar;
	public static final String emptyProp = "()";
	public static final String FunctionalObjectProperty = "FunctionalObjectProperty*";
	public static final String GCI0 = "SubClassOf*";
	public static final String GCI1 = "SubClassOf*";
	public static final String GCI2 = "SubClassOf*";
	public static final String GCI3 = "SubClassOf*";
	public static final String NominalAxiom = "NominalAxiom*";
	public static final String NormalizedRangeAxiom = "RangeAxiom*";
	public static final String openPar = IntegerClassExpressionWord.openPar;
	public static final String RI1 = "SubObjectPropertyOf*";
	public static final String RI2 = "SubObjectPropertyOf*";
	public static final String RI3 = "SubObjectPropertyOf*";
	public static final String sp = IntegerClassExpressionWord.sp;

}
