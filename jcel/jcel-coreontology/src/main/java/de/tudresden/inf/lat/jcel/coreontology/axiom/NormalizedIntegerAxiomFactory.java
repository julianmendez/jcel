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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

/**
 * An object implementing this interface is a factory to create normalized
 * axioms.
 *
 * @author Julian Mendez
 */
public interface NormalizedIntegerAxiomFactory {

	/**
	 * Constructs a new functional object property axiom.
	 *
	 * @param prop
	 *            object property
	 * @return a new functional object property axiom
	 */
	FunctObjectPropAxiom createFunctObjectPropAxiom(int prop);

	/**
	 * Constructs a new GCI-0 axiom.
	 *
	 * @param subCl
	 *            subclass identifier
	 * @param superCl
	 *            superclass identifier
	 * @return a new GCI-0 axiom
	 */
	GCI0Axiom createGCI0Axiom(int subCl, int superCl);

	/**
	 * Constructs a new GCI-1 axiom.
	 *
	 * @param leftSubCl
	 *            left subclass in the axiom
	 * @param rightSubCl
	 *            right subclass in the axiom
	 * @param superCl
	 *            superclass in the axiom
	 * @return a new GCI-1 axiom
	 */
	GCI1Axiom createGCI1Axiom(int leftSubCl, int rightSubCl, int superCl);

	/**
	 * Constructs a new GCI-2 axiom.
	 *
	 * @param leftCl
	 *            subclass identifier
	 * @param rightProp
	 *            object property identifier
	 * @param rightCl
	 *            class identifier for the right-hand part
	 * @return a new GCI-2 axiom
	 */
	GCI2Axiom createGCI2Axiom(int leftCl, int rightProp, int rightCl);

	/**
	 * Constructs a new GCI-3 axiom.
	 *
	 * @param leftProp
	 *            object property identifier for the left-hand part
	 * @param leftCl
	 *            class identifier for the left-hand part
	 * @param rightCl
	 *            superclass identifier
	 * @return a new GCI-3 axiom
	 */
	GCI3Axiom createGCI3Axiom(int leftProp, int leftCl, int rightCl);

	/**
	 * Constructs a new nominal axiom.
	 *
	 * @param classId
	 *            class identifier in the axiom
	 * @param individualId
	 *            individual identifier in the axiom
	 * @return a new nominal axiom
	 */
	NominalAxiom createNominalAxiom(int classId, int individualId);

	/**
	 * Constructs a new range axiom.
	 *
	 * @param prop
	 *            object property identifier
	 * @param cl
	 *            class identifier
	 * @return a new range axiom
	 */
	RangeAxiom createRangeAxiom(int prop, int cl);

	/**
	 * Constructs a new axiom RI-1.
	 *
	 * @param prop
	 *            object property identifier
	 * @return a new axiom RI-1
	 */
	RI1Axiom createRI1Axiom(int prop);

	/**
	 * Constructs a new axiom RI-2.
	 *
	 * @param leftProp
	 *            object property identifier for the left-hand part of the axiom
	 * @param rightProp
	 *            object property identifier for the right-hand part of the
	 *            axiom
	 * @return a new axiom RI-2
	 */
	RI2Axiom createRI2Axiom(int leftProp, int rightProp);

	/**
	 * Constructs a new RI-3 axiom.
	 *
	 * @param leftLeftProp
	 *            object property identifier for the left-hand object property
	 *            on the composition
	 * @param leftRightProp
	 *            object property identifier for the right-hand object property
	 *            on the composition
	 * @param rightProp
	 *            object property identifier for super object property
	 * @return a new RI-3 axiom
	 */
	RI3Axiom createRI3Axiom(int leftLeftProp, int leftRightProp, int rightProp);

}
