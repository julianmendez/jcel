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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

/**
 * This class models a visitor of <code>ComplexIntegerAxiom</code>.
 * 
 * @param <T>
 *            result type
 * 
 * @author Julian Mendez
 * 
 * @see ComplexIntegerAxiom
 */
public interface ComplexIntegerAxiomVisitor<T> {

	/**
	 * Visits a class assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerClassAssertionAxiom axiom);

	/**
	 * Visits a class declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerClassDeclarationAxiom axiom);

	/**
	 * Visits a data property assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerDataPropertyAssertionAxiom axiom);

	/**
	 * Visits a data property declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerDataPropertyDeclarationAxiom axiom);

	/**
	 * Visits a different individuals axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerDifferentIndividualsAxiom axiom);

	/**
	 * Visits a disjoint classes axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerDisjointClassesAxiom axiom);

	/**
	 * Visits an equivalent classes axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerEquivalentClassesAxiom axiom);

	/**
	 * Visits an equivalent object properties axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerEquivalentObjectPropertiesAxiom axiom);

	/**
	 * Visits a functional object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerFunctionalObjectPropertyAxiom axiom);

	/**
	 * Visits an inverse functional object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerInverseFunctionalObjectPropertyAxiom axiom);

	/**
	 * Visits an inverse object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerInverseObjectPropertiesAxiom axiom);

	/**
	 * Visits a named individual declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerNamedIndividualDeclarationAxiom axiom);

	/**
	 * Visits a negative object property assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerNegativeObjectPropertyAssertionAxiom axiom);

	/**
	 * Visits an object property assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerObjectPropertyAssertionAxiom axiom);

	/**
	 * Visits an object property declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerObjectPropertyDeclarationAxiom axiom);

	/**
	 * Visits property range axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerPropertyRangeAxiom axiom);

	/**
	 * Visits a reflexive object property declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerReflexiveObjectPropertyAxiom axiom);

	/**
	 * Visits a same individual axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerSameIndividualAxiom axiom);

	/**
	 * Visits a subclass axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerSubClassOfAxiom axiom);

	/**
	 * Visits a sub object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerSubObjectPropertyOfAxiom axiom);

	/**
	 * Visits a sub object property chain axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerSubPropertyChainOfAxiom axiom);

	/**
	 * Visits a transitive object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	T visit(IntegerTransitiveObjectPropertyAxiom axiom);

}
