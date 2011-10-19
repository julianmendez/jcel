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
 * An object of this class is a factory to create any normalized axiom.
 * 
 * @author Julian Mendez
 */
public class NormalizedIntegerAxiomFactoryImpl implements
		NormalizedIntegerAxiomFactory {

	/**
	 * Constructs a new normalized axiom factory.
	 */
	public NormalizedIntegerAxiomFactoryImpl() {
	}

	/**
	 * Constructs a new functional object property axiom.
	 * 
	 * @param prop
	 *            object property
	 */
	public FunctObjectPropAxiom createFunctObjectPropAxiom(Integer prop) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new FunctObjectPropAxiom(prop);
	}

	/**
	 * Constructs a new GCI-0 axiom.
	 * 
	 * @param subCl
	 *            subclass identifier
	 * @param superCl
	 *            superclass identifier
	 */
	public GCI0Axiom createGCI0Axiom(Integer subCl, Integer superCl) {
		if (subCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new GCI0Axiom(subCl, superCl);
	}

	/**
	 * Constructs a new GCI-1 axiom.
	 * 
	 * @param leftClList
	 *            list of class identifier for the part of the left-hand side
	 * @param rightCl
	 *            superclass in the axiom
	 */
	public GCI1Axiom createGCI1Axiom(List<Integer> leftClList, Integer rightCl) {
		if (leftClList == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new GCI1Axiom(leftClList, rightCl);
	}

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
			Integer rightCl) {
		if (leftCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new GCI2Axiom(leftCl, rightProp, rightCl);
	}

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
			Integer rightCl) {
		if (leftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (leftCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new GCI3Axiom(leftProp, leftCl, rightCl);
	}

	/**
	 * Constructs a new nominal axiom.
	 * 
	 * @param classId
	 *            class identifier in the axiom
	 * @param individualId
	 *            individual identifier in the axiom
	 */
	public NominalAxiom createNominalAxiom(Integer classId, Integer individualId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (individualId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new NominalAxiom(classId, individualId);
	}

	/**
	 * Constructs a new range axiom.
	 * 
	 * @param prop
	 *            object property identifier
	 * @param cl
	 *            class identifier
	 */
	public RangeAxiom createRangeAxiom(Integer prop, Integer cl) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (cl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RangeAxiom(prop, cl);
	}

	/**
	 * Constructs a new axiom RI-1.
	 * 
	 * @param prop
	 *            object property identifier
	 */
	public RI1Axiom createRI1Axiom(Integer prop) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RI1Axiom(prop);
	}

	/**
	 * Constructs a new axiom RI-2.
	 * 
	 * @param leftProp
	 *            object property identifier for the left-hand part of the axiom
	 * @param rightProp
	 *            object property identifier for the right-hand part of the
	 *            axiom
	 */
	public RI2Axiom createRI2Axiom(Integer leftProp, Integer rightProp) {
		if (leftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RI2Axiom(leftProp, rightProp);
	}

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
			Integer rightProp) {
		if (leftLeftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (leftRightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RI3Axiom(leftLeftProp, leftRightProp, rightProp);
	}

}
