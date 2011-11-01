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

package de.tudresden.inf.lat.jcel.core.axiom.normalized;

/**
 * This class models a visitor of <code>NormalizedIntegerAxiom</code>.
 * 
 * @author Julian Mendez
 * 
 * @see NormalizedIntegerAxiom
 */
public interface NormalizedIntegerAxiomVisitor<T> {

	public T visit(GCI0Axiom axiom);

	public T visit(GCI1Axiom axiom);

	public T visit(GCI2Axiom axiom);

	public T visit(GCI3Axiom axiom);

	public T visit(RangeAxiom axiom);

	public T visit(RI1Axiom axiom);

	public T visit(RI2Axiom axiom);

	public T visit(RI3Axiom axiom);

}
