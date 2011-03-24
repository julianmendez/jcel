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

/**
 * This class models a visitor of <code>ComplexIntegerAxiom</code>.
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
	public T visit(IntegerClassAssertionAxiom axiom);

	/**
	 * Visits a class declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerClassDeclarationAxiom axiom);

	/**
	 * Visits a data property assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerDataPropertyAssertionAxiom axiom);

	/**
	 * Visits a data property declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerDataPropertyDeclarationAxiom axiom);

	/**
	 * Visits a different individuals axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerDifferentIndividualsAxiom axiom);

	/**
	 * Visits a disjoint classes axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerDisjointClassesAxiom axiom);

	/**
	 * Visits an equivalent classes axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerEquivalentClassesAxiom axiom);

	/**
	 * Visits an equivalent object properties axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerEquivalentObjectPropertiesAxiom axiom);

	/**
	 * Visits a functional object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerFunctionalObjectPropertyAxiom axiom);

	/**
	 * Visits an inverse functional object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerInverseFunctionalObjectPropertyAxiom axiom);

	/**
	 * Visits an inverse object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerInverseObjectPropertiesAxiom axiom);

	/**
	 * Visits a named individual declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerNamedIndividualDeclarationAxiom axiom);

	/**
	 * Visits a negative object property assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerNegativeObjectPropertyAssertionAxiom axiom);

	/**
	 * Visits an object property assertion axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerObjectPropertyAssertionAxiom axiom);

	/**
	 * Visits an object property declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerObjectPropertyDeclarationAxiom axiom);

	/**
	 * Visits property range axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerPropertyRangeAxiom axiom);

	/**
	 * Visits a reflexive object property declaration axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerReflexiveObjectPropertyAxiom axiom);

	/**
	 * Visits a same individual axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerSameIndividualAxiom axiom);

	/**
	 * Visits a subclass axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerSubClassOfAxiom axiom);

	/**
	 * Visits a sub object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerSubObjectPropertyOfAxiom axiom);

	/**
	 * Visits a sub object property chain axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerSubPropertyChainOfAxiom axiom);

	/**
	 * Visits a transitive object property axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the visit result
	 */
	public T visit(IntegerTransitiveObjectPropertyAxiom axiom);

}
