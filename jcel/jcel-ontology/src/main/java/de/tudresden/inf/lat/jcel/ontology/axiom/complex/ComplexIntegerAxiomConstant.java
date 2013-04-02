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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * This interface is a collection of constants used by classes that implement an
 * <code>ComplexIntegerAxiom</code>.
 * 
 * @author Julian Mendez
 * 
 * @see ComplexIntegerAxiom
 */

public interface ComplexIntegerAxiomConstant {

	public static final String ClassAssertion = "ClassAssertion";
	public static final String ClassDeclaration = "Class";
	public static final String closePar = IntegerClassExpressionWord.closePar;
	public static final String DataPropertyAssertion = "DataPropertyAssertion";
	public static final String DataPropertyDeclaration = "DataProperty";
	public static final String Declaration = "Declaration";
	public static final String DifferentIndividuals = "DifferentIndividuals";
	public static final String DisjointClasses = "DisjointClasses";
	public static final String EquivalentClasses = "EquivalentClasses";
	public static final String EquivalentProperties = "EquivalentProperties";
	public static final String FunctionalObjectProperty = "FunctionalObjectProperty";
	public static final String InverseFunctionalObjectProperty = "InverseFunctionalObjectProperty";
	public static final String InverseObjectProperties = "InverseObjectProperties";
	public static final String NamedIndividualDeclaration = "NamedIndividual";
	public static final String NegativeObjectPropertyAssertion = "NegativeObjectPropertyAssertion";
	public static final String ObjectPropertyAssertion = "ObjectPropertyAssertion";
	public static final String ObjectPropertyChain = "ObjectPropertyChain";
	public static final String ObjectPropertyDeclaration = "ObjectProperty";
	public static final String openPar = IntegerClassExpressionWord.openPar;
	public static final String RangeAxiom = "RangeAxiom";
	public static final String ReflexiveObjectProperty = "ReflexiveObjectProperty";
	public static final String SameIndividual = "SameIndividual";
	public static final String sp = IntegerClassExpressionWord.sp;
	public static final String SubClassOf = "SubClassOf";
	public static final String SubObjectPropertyOf = "SubObjectPropertyOf";
	public static final String TransitiveObjectProperty = "TransitiveObjectProperty";

}
