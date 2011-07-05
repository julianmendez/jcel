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

import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.FunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.GCI0Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.GCI1Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.GCI3Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NominalAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI1Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RangeAxiom;

/**
 * An object implementing this class analyzes an complex axiom to detect what
 * constructors are used in it.
 * 
 * @author Julian Mendez
 */
class NormalizedIntegerAxiomAnalyzer implements
		NormalizedIntegerAxiomVisitor<Boolean>, OntologyExpressivity {

	private boolean hasBottom = false;
	private boolean hasDatatype = false;
	private boolean hasFunctionalObjectProperty = false;
	private boolean hasIndividual = false;
	private boolean hasInverseObjectProperty = false;
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
	public Boolean visit(FunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasFunctionalObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(GCI0Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(GCI1Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(GCI2Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(GCI3Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(NominalAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasNominal = true;
		return true;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasReflexiveObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasSubObjectPropertyOf = true;
		return true;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (axiom.getSuperProperty().equals(axiom.getLeftSubProperty())
				&& axiom.getSuperProperty().equals(axiom.getRightSubProperty())) {
			this.hasTransitiveObjectProperty = true;
		} else {
			this.hasSubPropertyChainOf = true;
		}
		return true;
	}

}
