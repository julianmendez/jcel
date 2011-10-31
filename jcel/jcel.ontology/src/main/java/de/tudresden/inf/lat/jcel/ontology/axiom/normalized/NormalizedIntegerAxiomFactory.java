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

import java.util.List;

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
	 */
	public FunctObjectPropAxiom createFunctObjectPropAxiom(
			Integer prop);

	/**
	 * Constructs a new GCI-0 axiom.
	 * 
	 * @param subCl
	 *            subclass identifier
	 * @param superCl
	 *            superclass identifier
	 */
	public GCI0Axiom createGCI0Axiom(Integer subCl, Integer superCl);

	/**
	 * Constructs a new GCI-1 axiom.
	 * 
	 * @param leftClList
	 *            list of class identifier for the part of the left-hand side
	 * @param rightCl
	 *            superclass in the axiom
	 */
	public GCI1Axiom createGCI1Axiom(List<Integer> leftClList, Integer rightCl);

	/**
	 * Constructs a new GCI-2 axiom.
	 * 
	 * @param leftCl
	 *            subclass identifier
	 * @param rightProp
	 *            object property identifier
	 * @param rightCl
	 *            class identifier for the right-hand part
	 */
	public GCI2Axiom createGCI2Axiom(Integer leftCl, Integer rightProp,
			Integer rightCl);

	/**
	 * Constructs a new GCI-3 axiom.
	 * 
	 * @param leftProp
	 *            object property identifier for the left-hand part
	 * @param leftCl
	 *            class identifier for the left-hand part
	 * @param rightCl
	 *            superclass identifier
	 */
	public GCI3Axiom createGCI3Axiom(Integer leftProp, Integer leftCl,
			Integer rightCl);

	/**
	 * Constructs a new nominal axiom.
	 * 
	 * @param classId
	 *            class identifier in the axiom
	 * @param individualId
	 *            individual identifier in the axiom
	 */
	public NominalAxiom createNominalAxiom(Integer classId, Integer individualId);

	/**
	 * Constructs a new range axiom.
	 * 
	 * @param prop
	 *            object property identifier
	 * @param cl
	 *            class identifier
	 */
	public RangeAxiom createRangeAxiom(Integer prop, Integer cl);

	/**
	 * Constructs a new axiom RI-1.
	 * 
	 * @param prop
	 *            object property identifier
	 */
	public RI1Axiom createRI1Axiom(Integer prop);

	/**
	 * Constructs a new axiom RI-2.
	 * 
	 * @param leftProp
	 *            object property identifier for the left-hand part of the axiom
	 * @param rightProp
	 *            object property identifier for the right-hand part of the
	 *            axiom
	 */
	public RI2Axiom createRI2Axiom(Integer leftProp, Integer rightProp);

	/**
	 * Constructs a new RI-3 axiom
	 * 
	 * @param leftLeftProp
	 *            object property identifier for the left-hand object property
	 *            on the composition
	 * @param leftRightProp
	 *            object property identifier for the right-hand object property
	 *            on the composition
	 * @param rightProp
	 *            object property identifier for super object property
	 */
	public RI3Axiom createRI3Axiom(Integer leftLeftProp, Integer leftRightProp,
			Integer rightProp);

}
