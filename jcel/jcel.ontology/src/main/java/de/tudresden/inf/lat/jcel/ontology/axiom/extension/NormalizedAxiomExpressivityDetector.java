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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;

/**
 * An object implementing this class can detect the expressivity of a given set
 * of normalized axioms.
 * 
 * @author Julian Mendez
 */
public class NormalizedAxiomExpressivityDetector implements
		OntologyExpressivity {

	private NormalizedIntegerAxiomAnalyzer axiomAnalyzer = new NormalizedIntegerAxiomAnalyzer();
	private final String name;

	/**
	 * Constructs a new expressivity detector.
	 * 
	 * @param axiomSet
	 *            set of axioms to detect the expressivity
	 */
	public NormalizedAxiomExpressivityDetector(
			Set<NormalizedIntegerAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		for (NormalizedIntegerAxiom axiom : axiomSet) {
			axiom.accept(this.axiomAnalyzer);
		}
		this.name = (new ExpressivityName()).getName(this);
	}

	private NormalizedIntegerAxiomAnalyzer getAnalyzer() {
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
