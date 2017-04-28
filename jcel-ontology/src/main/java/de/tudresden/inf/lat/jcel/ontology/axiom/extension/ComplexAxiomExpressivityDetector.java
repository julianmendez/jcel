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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;
import de.tudresden.inf.lat.jcel.coreontology.expressivity.ExpressivityName;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;

/**
 * An object implementing this class can detect the expressivity of a given set
 * of complex axioms.
 * 
 * @author Julian Mendez
 */
public class ComplexAxiomExpressivityDetector implements OntologyExpressivity {

	private final ComplexIntegerAxiomAnalyzer axiomAnalyzer = new ComplexIntegerAxiomAnalyzer();
	private final String name;

	/**
	 * Constructs a new expressivity detector.
	 * 
	 * @param axiomSet
	 *            set of axioms to detect the expressivity
	 */
	public ComplexAxiomExpressivityDetector(Set<ComplexIntegerAxiom> axiomSet) {
		Objects.requireNonNull(axiomSet);
		axiomSet.forEach(axiom -> axiom.accept(this.axiomAnalyzer));
		this.name = (new ExpressivityName()).getName(this);
	}

	private ComplexIntegerAxiomAnalyzer getAnalyzer() {
		return this.axiomAnalyzer;
	}

	@Override
	public boolean hasBottom() {
		return getAnalyzer().hasBottom();
	}

	@Override
	public boolean hasDatatype() {
		return getAnalyzer().hasDatatype();
	}

	@Override
	public boolean hasFunctionalObjectProperty() {
		return getAnalyzer().hasFunctionalObjectProperty();
	}

	@Override
	public boolean hasIndividual() {
		return getAnalyzer().hasIndividual();
	}

	@Override
	public boolean hasInverseObjectProperty() {
		return getAnalyzer().hasInverseObjectProperty();
	}

	@Override
	public boolean hasNominal() {
		return getAnalyzer().hasNominal();
	}

	@Override
	public boolean hasReflexiveObjectProperty() {
		return getAnalyzer().hasReflexiveObjectProperty();
	}

	@Override
	public boolean hasSubObjectPropertyOf() {
		return getAnalyzer().hasSubObjectPropertyOf();
	}

	@Override
	public boolean hasSubPropertyChainOf() {
		return getAnalyzer().hasSubPropertyChainOf();

	}

	@Override
	public boolean hasTransitiveObjectProperty() {
		return getAnalyzer().hasTransitiveObjectProperty();
	}

	@Override
	public String toString() {
		return this.name;
	}

}
