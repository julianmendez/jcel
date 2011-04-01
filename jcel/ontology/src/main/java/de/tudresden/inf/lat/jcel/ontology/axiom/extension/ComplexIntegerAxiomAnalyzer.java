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

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDataPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDataPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDifferentIndividualsAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerFunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseFunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerNamedIndividualDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerNegativeObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerReflexiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSameIndividualAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubObjectPropertyOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerTransitiveObjectPropertyAxiom;

/**
 * An object implementing this class analyzes an complex axiom to detect what
 * constructors are used in it.
 * 
 * @author Julian Mendez
 */
class ComplexIntegerAxiomAnalyzer implements
		ComplexIntegerAxiomVisitor<Boolean> {

	private IntegerExpressionAnalyzer expressionAnalyzer = null;
	private boolean hasBottom = false;
	private boolean hasDatatype = false;
	private boolean hasFunctionalObjectProperty = false;
	private boolean hasIndividual = false;
	private boolean hasInverseObjectProperty = false;
	private boolean hasReflexiveObjectProperty = false;
	private boolean hasSubObjectPropertyOf = false;
	private boolean hasSubPropertyChainOf = false;
	private boolean hasTransitiveObjectProperty = false;

	/**
	 * Constructs a new axiom analyzer.
	 */
	public ComplexIntegerAxiomAnalyzer() {
		this.expressionAnalyzer = new IntegerExpressionAnalyzer();
	}

	/**
	 * Tells whether the analyzer has found class bottom.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found class
	 *         bottom
	 */
	public boolean hasBottom() {
		return this.hasBottom;
	}

	/**
	 * Tells whether the analyzer has found data types.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found data
	 *         types
	 */
	public boolean hasDatatype() {
		return this.hasDatatype || this.expressionAnalyzer.hasDatatype();
	}

	/**
	 * Tells whether the analyzer has found functional object properties.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found
	 *         functional object properties
	 */
	public boolean hasFunctionalObjectProperty() {
		return this.hasFunctionalObjectProperty;
	}

	/**
	 * Tells whether the analyzer has found individuals.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found
	 *         individuals
	 */
	public boolean hasIndividual() {
		return this.hasIndividual;
	}

	/**
	 * Tells whether the analyzer has found inverse object properties.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found inverse
	 *         object properties
	 */
	public boolean hasInverseObjectProperty() {
		return this.hasInverseObjectProperty
				|| this.expressionAnalyzer.hasInverseObjectProperty();
	}

	/**
	 * Tells whether the analyzer has found nominals.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found nominals
	 */
	public boolean hasNominal() {
		return this.expressionAnalyzer.hasNominal();
	}

	/**
	 * Tells whether the analyzer has found reflexive object properties.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found reflexive
	 *         object properties
	 */
	public boolean hasReflexiveObjectProperty() {
		return this.hasReflexiveObjectProperty;
	}

	/**
	 * Tells whether the analyzer has found sub object properties.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found sub
	 *         object properties
	 */
	public boolean hasSubObjectPropertyOf() {
		return this.hasSubObjectPropertyOf;
	}

	/**
	 * Tells whether the analyzer has found sub object property chains.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found sub
	 *         object property chains
	 */
	public boolean hasSubPropertyChainOf() {
		return this.hasSubPropertyChainOf;
	}

	/**
	 * Tells whether the analyzer has found transitive object properties.
	 * 
	 * @return <code>true</code> if and only if the analyzer has found
	 *         transitive object properties
	 */
	public boolean hasTransitiveObjectProperty() {
		return this.hasTransitiveObjectProperty;
	}

	@Override
	public Boolean visit(IntegerClassAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasIndividual = true;
		return axiom.getClassExpression().accept(this.expressionAnalyzer);
	}

	@Override
	public Boolean visit(IntegerClassDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasDatatype = true;
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDataPropertyDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasDatatype = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDifferentIndividualsAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasBottom = true;
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDisjointClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasBottom = true;
		return this.expressionAnalyzer.visit(axiom.getClassExpressions());
	}

	@Override
	public Boolean visit(IntegerEquivalentClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.expressionAnalyzer.visit(axiom.getClassExpressions());
	}

	@Override
	public Boolean visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasFunctionalObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerInverseFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasFunctionalObjectProperty = true;
		this.hasInverseObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerInverseObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasInverseObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerNamedIndividualDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerNegativeObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasBottom = true;
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectPropertyDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return axiom.getRange().accept(this.expressionAnalyzer);
	}

	@Override
	public Boolean visit(IntegerReflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasReflexiveObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerSameIndividualAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerSubClassOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerSubObjectPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasSubObjectPropertyOf = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerSubPropertyChainOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasSubPropertyChainOf = true;
		this.hasSubObjectPropertyOf = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasTransitiveObjectProperty = true;
		return true;
	}

}
