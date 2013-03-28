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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

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
	public FunctObjectPropAxiom createFunctObjectPropAxiom(int prop) {
		return new FunctObjectPropAxiom(prop);
	}

	@Override
	public GCI0Axiom createGCI0Axiom(int subCl, int superCl) {
		return new GCI0Axiom(subCl, superCl);
	}

	@Override
	public GCI1Axiom createGCI1Axiom(int leftCl, int rightCl, int superCl) {
		return new GCI1Axiom(leftCl, rightCl, superCl);
	}

	@Override
	public GCI2Axiom createGCI2Axiom(int leftCl, int rightProp, int rightCl) {
		return new GCI2Axiom(leftCl, rightProp, rightCl);
	}

	@Override
	public GCI3Axiom createGCI3Axiom(int leftProp, int leftCl, int rightCl) {
		return new GCI3Axiom(leftProp, leftCl, rightCl);
	}

	@Override
	public NominalAxiom createNominalAxiom(int classId, int individualId) {
		return new NominalAxiom(classId, individualId);
	}

	@Override
	public RangeAxiom createRangeAxiom(int prop, int cl) {
		return new RangeAxiom(prop, cl);
	}

	@Override
	public RI1Axiom createRI1Axiom(int prop) {
		return new RI1Axiom(prop);
	}

	@Override
	public RI2Axiom createRI2Axiom(int leftProp, int rightProp) {
		return new RI2Axiom(leftProp, rightProp);
	}

	@Override
	public RI3Axiom createRI3Axiom(int leftLeftProp, int leftRightProp,
			int rightProp) {
		return new RI3Axiom(leftLeftProp, leftRightProp, rightProp);
	}

}
