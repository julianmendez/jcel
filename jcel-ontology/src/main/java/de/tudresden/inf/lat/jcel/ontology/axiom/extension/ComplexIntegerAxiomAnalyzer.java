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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;
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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * An object implementing this class analyzes an complex axiom to detect what
 * constructors are used in it.
 * 
 * @author Julian Mendez
 */
public class ComplexIntegerAxiomAnalyzer implements ComplexIntegerAxiomVisitor<Boolean>, OntologyExpressivity {

	private final IntegerExpressionAnalyzer expressionAnalyzer = new IntegerExpressionAnalyzer();
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
	 * Constructs a new analyzer of complex axioms.
	 */
	public ComplexIntegerAxiomAnalyzer() {
	}

	@Override
	public boolean hasBottom() {
		return this.hasBottom || this.expressionAnalyzer.hasBottom();
	}

	@Override
	public boolean hasDatatype() {
		return this.hasDatatype || this.expressionAnalyzer.hasDatatype();
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
		return this.hasInverseObjectProperty || this.expressionAnalyzer.hasInverseObjectProperty();
	}

	@Override
	public boolean hasNominal() {
		return this.expressionAnalyzer.hasNominal();
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
	public Boolean visit(IntegerClassAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasIndividual = true;
		return axiom.getClassExpression().accept(this.expressionAnalyzer);
	}

	@Override
	public Boolean visit(IntegerClassDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		return true;
	}

	@Override
	public Boolean visit(IntegerDataPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasDatatype = true;
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDataPropertyDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasDatatype = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDifferentIndividualsAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom = true;
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDisjointClassesAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom = true;
		return this.expressionAnalyzer.visit(axiom.getClassExpressions());
	}

	@Override
	public Boolean visit(IntegerEquivalentClassesAxiom axiom) {
		Objects.requireNonNull(axiom);
		return this.expressionAnalyzer.visit(axiom.getClassExpressions());
	}

	@Override
	public Boolean visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		return true;
	}

	@Override
	public Boolean visit(IntegerFunctionalObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasFunctionalObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerInverseFunctionalObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasFunctionalObjectProperty = true;
		this.hasInverseObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerInverseObjectPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasInverseObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerNamedIndividualDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerNegativeObjectPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasBottom = true;
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectPropertyDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		return true;
	}

	@Override
	public Boolean visit(IntegerPropertyRangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		return axiom.getRange().accept(this.expressionAnalyzer);
	}

	@Override
	public Boolean visit(IntegerReflexiveObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasReflexiveObjectProperty = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerSameIndividualAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasIndividual = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerSubClassOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerClassExpression> set = new HashSet<>();
		set.add(axiom.getSubClass());
		set.add(axiom.getSuperClass());
		return this.expressionAnalyzer.visit(set);

	}

	@Override
	public Boolean visit(IntegerSubObjectPropertyOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasSubObjectPropertyOf = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerSubPropertyChainOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasSubPropertyChainOf = true;
		this.hasSubObjectPropertyOf = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.hasTransitiveObjectProperty = true;
		return true;
	}

}
