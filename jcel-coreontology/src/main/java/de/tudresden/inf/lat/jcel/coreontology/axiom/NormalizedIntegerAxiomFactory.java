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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.Set;

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
	 * @param propertyId
	 *            object property identifier
	 * @param annotations
	 *            annotations
	 * @return a new functional object property axiom
	 */
	FunctObjectPropAxiom createFunctObjectPropAxiom(int propertyId, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new GCI-0 axiom.
	 *
	 * @param subClassId
	 *            subclass identifier
	 * @param superClassId
	 *            superclass identifier
	 * @param annotations
	 *            annotations
	 * @return a new GCI-0 axiom
	 */
	GCI0Axiom createGCI0Axiom(int subClassId, int superClassId, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new GCI-1 axiom.
	 *
	 * @param leftSubClassId
	 *            left subclass in the axiom
	 * @param rightSubClassId
	 *            right subclass in the axiom
	 * @param superClassId
	 *            superclass in the axiom
	 * @param annotations
	 *            annotations
	 * @return a new GCI-1 axiom
	 */
	GCI1Axiom createGCI1Axiom(int leftSubClassId, int rightSubClassId, int superClassId,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new GCI-2 axiom.
	 *
	 * @param leftClassId
	 *            subclass identifier
	 * @param rightPropertyId
	 *            object property identifier
	 * @param rightClassId
	 *            class identifier for the right-hand part
	 * @param annotations
	 *            annotations
	 * @return a new GCI-2 axiom
	 */
	GCI2Axiom createGCI2Axiom(int leftClassId, int rightPropertyId, int rightClassId,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new GCI-3 axiom.
	 *
	 * @param leftPropertyId
	 *            object property identifier for the left-hand part
	 * @param leftClassId
	 *            class identifier for the left-hand part
	 * @param rightClassId
	 *            superclass identifier
	 * @param annotations
	 *            annotations
	 * @return a new GCI-3 axiom
	 */
	GCI3Axiom createGCI3Axiom(int leftPropertyId, int leftClassId, int rightClassId,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new nominal axiom.
	 *
	 * @param classId
	 *            class identifier in the axiom
	 * @param individualId
	 *            individual identifier in the axiom
	 * @param annotations
	 *            annotations
	 * @return a new nominal axiom
	 */
	NominalAxiom createNominalAxiom(int classId, int individualId, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new range axiom.
	 *
	 * @param propertyId
	 *            object property identifier
	 * @param classId
	 *            class identifier
	 * @param annotations
	 *            annotations
	 * @return a new range axiom
	 */
	RangeAxiom createRangeAxiom(int propertyId, int classId, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new axiom RI-1.
	 *
	 * @param propertyId
	 *            object property identifier
	 * @param annotations
	 *            annotations
	 * @return a new axiom RI-1
	 */
	RI1Axiom createRI1Axiom(int propertyId, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new axiom RI-2.
	 *
	 * @param leftPropertyId
	 *            object property identifier for the left-hand part of the axiom
	 * @param rightPropertyId
	 *            object property identifier for the right-hand part of the
	 *            axiom
	 * @param annotations
	 *            annotations
	 * @return a new axiom RI-2
	 */
	RI2Axiom createRI2Axiom(int leftPropertyId, int rightPropertyId, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new RI-3 axiom.
	 *
	 * @param leftLeftPropertyId
	 *            object property identifier for the left-hand object property
	 *            on the composition
	 * @param leftRightPropertyId
	 *            object property identifier for the right-hand object property
	 *            on the composition
	 * @param rightPropertyId
	 *            object property identifier for super object property
	 * @param annotations
	 *            annotations
	 * @return a new RI-3 axiom
	 */
	RI3Axiom createRI3Axiom(int leftLeftPropertyId, int leftRightPropertyId, int rightPropertyId,
			Set<IntegerAnnotation> annotations);

}
