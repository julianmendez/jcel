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

/**
 * This is a visitor for an <code>IntegerAxiom</code>.
 * 
 * @param <T>
 *            Type of the returning value of visit functions.
 * 
 * @author Julian Mendez
 * 
 * @see IntegerAxiom
 */
public interface IntegerAxiomVisitor<T> {

	public T visit(GCI0Axiom axiom);

	public T visit(GCI1Axiom axiom);

	public T visit(GCI2Axiom axiom);

	public T visit(GCI3Axiom axiom);

	public T visit(IntegerDisjointClassesAxiom axiom);

	public T visit(IntegerEquivalentClassesAxiom axiom);

	public T visit(IntegerEquivalentObjectPropertiesAxiom axiom);

	public T visit(IntegerObjectPropertyChainSubPropertyAxiom axiom);

	public T visit(IntegerRangeAxiom axiom);

	public T visit(IntegerSubClassAxiom axiom);

	public T visit(IntegerTransitiveObjectPropertyAxiom axiom);

	public T visit(RangeAxiom axiom);

	public T visit(RI1Axiom axiom);

	public T visit(RI2Axiom axiom);

	public T visit(RI3Axiom axiom);
}
