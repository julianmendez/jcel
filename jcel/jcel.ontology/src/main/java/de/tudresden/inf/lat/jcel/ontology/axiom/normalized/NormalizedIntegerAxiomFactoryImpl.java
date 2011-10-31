/*
 * Copyright 2009 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 * jcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General @Override public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General @Override public License for more details.
 *
 * You should have received a copy of the GNU Lesser General @Override public License
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

	@Override
	public FunctObjectPropAxiom createFunctObjectPropAxiom(Integer prop) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new FunctObjectPropAxiom(prop);
	}

	@Override
	public GCI0Axiom createGCI0Axiom(Integer subCl, Integer superCl) {
		if (subCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (superCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new GCI0Axiom(subCl, superCl);
	}

	@Override
	public GCI1Axiom createGCI1Axiom(List<Integer> leftClList, Integer rightCl) {
		if (leftClList == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new GCI1Axiom(leftClList, rightCl);
	}

	@Override
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

	@Override
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

	@Override
	public NominalAxiom createNominalAxiom(Integer classId, Integer individualId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (individualId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new NominalAxiom(classId, individualId);
	}

	@Override
	public RangeAxiom createRangeAxiom(Integer prop, Integer cl) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (cl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RangeAxiom(prop, cl);
	}

	@Override
	public RI1Axiom createRI1Axiom(Integer prop) {
		if (prop == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RI1Axiom(prop);
	}

	@Override
	public RI2Axiom createRI2Axiom(Integer leftProp, Integer rightProp) {
		if (leftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return new RI2Axiom(leftProp, rightProp);
	}

	@Override
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
