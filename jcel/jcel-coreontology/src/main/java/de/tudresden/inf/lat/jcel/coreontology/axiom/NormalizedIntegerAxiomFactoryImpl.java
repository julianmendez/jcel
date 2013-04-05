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