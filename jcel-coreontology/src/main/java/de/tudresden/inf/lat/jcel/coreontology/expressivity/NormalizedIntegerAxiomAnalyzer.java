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

package de.tudresden.inf.lat.jcel.coreontology.expressivity;

import java.util.Objects;

import de.tudresden.inf.lat.jcel.coreontology.axiom.FunctObjectPropAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NominalAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RangeAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;

/**
 * An object implementing this class analyzes an complex axiom to detect what
 * constructors are used in it.
 * 
 * @author Julian Mendez
 */
class NormalizedIntegerAxiomAnalyzer implements NormalizedIntegerAxiomVisitor<Boolean>, OntologyExpressivity {

	private boolean hasBottom = false;
	private final boolean hasDatatype = false;
	private boolean hasFunctionalObjectProperty = false;
	private final boolean hasIndividual = false;
	private final boolean hasInverseObjectProperty = false;
	private boolean hasNominal = false;
	private boolean hasReflexiveObjectProperty = false;
	private boolean hasSubObjectPropertyOf = false;
	private boolean hasSubPropertyChainOf = false;
	private boolean hasTransitiveObjectProperty = false;

	/**
	 * Constructs a new analyzer of normalized axioms.
	 */
	public NormalizedIntegerAxiomAnalyzer() {
	}

	@Override
	public boolean hasBottom() {
		return this.hasBottom;
	}

	@Override
	public boolean hasDatatype() {
		return this.hasDatatype;
	}

	@Override
	public boolean hasFunctionalObjectProperty() {
		return this.hasFunctionalObjectProperty;
	}

	@Override
	public boolean hasIndividual() {
		return this.hasIndividual;
	}

	@Override
	public boolean hasInverseObjectProperty() {
		return this.hasInverseObjectProperty;
	}

	@Override
	public boolean hasNominal() {
		return this.hasNominal;
	}

	@Override
	public boolean hasReflexiveObjectProperty() {
		return this.hasReflexiveObjectProperty;
	}

	@Override
	public boolean hasSubObjectPropertyOf() {
		return this.hasSubObjectPropertyOf;
	}

	@Override
	public boolean hasSubPropertyChainOf() {
		return this.hasSubPropertyChainOf;
	}

	@Override
	public boolean hasTransitiveObjectProperty() {
		return this.hasTransitiveObjectProperty;
	}

	@Override
	public Boolean visit(FunctObjectPropAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasFunctionalObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(GCI0Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom |= (axiom.getSubClass() == IntegerEntityManager.bottomClassId)
				|| (axiom.getSuperClass() == IntegerEntityManager.bottomClassId);
		return true;
	}

	@Override
	public Boolean visit(GCI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom |= (axiom.getLeftSubClass() == IntegerEntityManager.bottomClassId)
				|| (axiom.getRightSubClass() == IntegerEntityManager.bottomClassId)
				|| (axiom.getSuperClass() == IntegerEntityManager.bottomClassId);
		return true;
	}

	@Override
	public Boolean visit(GCI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom |= (axiom.getSubClass() == IntegerEntityManager.bottomClassId)
				|| (axiom.getClassInSuperClass() == IntegerEntityManager.bottomClassId);
		return true;
	}

	@Override
	public Boolean visit(GCI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom |= (axiom.getClassInSubClass() == IntegerEntityManager.bottomClassId)
				|| (axiom.getSuperClass() == IntegerEntityManager.bottomClassId);
		return true;
	}

	@Override
	public Boolean visit(NominalAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasNominal = true;
		return true;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom |= (axiom.getRange() == IntegerEntityManager.bottomClassId);
		return true;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasReflexiveObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasSubObjectPropertyOf = true;
		return true;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		if ((axiom.getSuperProperty() == axiom.getLeftSubProperty())
				&& (axiom.getSuperProperty() == axiom.getRightSubProperty())) {
			this.hasTransitiveObjectProperty = true;
		} else {
			this.hasSubPropertyChainOf = true;
		}
		return true;
	}

}
