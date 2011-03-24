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

	public T visit(IntegerClassAssertionAxiom axiom);

	public T visit(IntegerClassDeclarationAxiom axiom);

	public T visit(IntegerDataPropertyAssertionAxiom axiom);

	public T visit(IntegerDataPropertyDeclarationAxiom axiom);

	public T visit(IntegerDifferentIndividualsAxiom axiom);

	public T visit(IntegerDisjointClassesAxiom axiom);

	public T visit(IntegerEquivalentClassesAxiom axiom);

	public T visit(IntegerEquivalentObjectPropertiesAxiom axiom);

	public T visit(IntegerFunctionalObjectPropertyAxiom axiom);

	public T visit(IntegerInverseFunctionalObjectPropertyAxiom axiom);

	public T visit(IntegerInverseObjectPropertiesAxiom axiom);

	public T visit(IntegerNamedIndividualDeclarationAxiom axiom);

	public T visit(IntegerNegativeObjectPropertyAssertionAxiom axiom);

	public T visit(IntegerObjectPropertyAssertionAxiom axiom);

	public T visit(IntegerObjectPropertyDeclarationAxiom axiom);

	public T visit(IntegerPropertyRangeAxiom axiom);

	public T visit(IntegerReflexiveObjectPropertyAxiom axiom);

	public T visit(IntegerSameIndividualAxiom axiom);

	public T visit(IntegerSubClassOfAxiom axiom);

	public T visit(IntegerSubObjectPropertyOfAxiom axiom);

	public T visit(IntegerSubPropertyChainOfAxiom axiom);

	public T visit(IntegerTransitiveObjectPropertyAxiom axiom);

}
