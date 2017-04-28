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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class can translate a class expression of the OWL API into
 * the integer-based representation.
 * 
 * @author Julian Mendez
 */
public class ClassExpressionTranslator implements OWLClassExpressionVisitorEx<IntegerClassExpression> {

	private final ObjectPropertyExpressionTranslator objectPropertyExpressionTranslator;

	/**
	 * Constructs a new class expression translator.
	 * 
	 * @param translator
	 *            object property expression translator
	 */
	public ClassExpressionTranslator(ObjectPropertyExpressionTranslator translator) {
		Objects.requireNonNull(translator);
		this.objectPropertyExpressionTranslator = translator;
	}

	public IntegerDataTypeFactory getDataTypeFactory() {
		return this.objectPropertyExpressionTranslator.getDataTypeFactory();
	}

	public ObjectPropertyExpressionTranslator getObjectPropertyExpressionTranslator() {
		return this.objectPropertyExpressionTranslator;
	}

	public TranslationRepository getTranslationRepository() {
		return this.objectPropertyExpressionTranslator.getTranslationRepository();
	}

	public Integer translateDataProperty(OWLDataProperty owlDataProperty) {
		Objects.requireNonNull(owlDataProperty);
		getTranslationRepository().addDataProperty(owlDataProperty);
		return getTranslationRepository().getId(owlDataProperty);
	}

	public Integer translateIndividual(OWLIndividual owlIndividual) throws TranslationException {
		Objects.requireNonNull(owlIndividual);
		getTranslationRepository().addNamedIndividual(owlIndividual.asOWLNamedIndividual());
		return getTranslationRepository().getId(owlIndividual);
	}

	public Integer translateLiteral(OWLLiteral owlLiteral) throws TranslationException {
		Objects.requireNonNull(owlLiteral);
		getTranslationRepository().addLiteral(owlLiteral);
		return getTranslationRepository().getId(owlLiteral);
	}

	@Override
	public IntegerClassExpression visit(OWLClass owlClassExpression) {
		Objects.requireNonNull(owlClassExpression);
		getTranslationRepository().addClass(owlClassExpression);
		return getDataTypeFactory().createClass(getTranslationRepository().getId(owlClassExpression));
	}

	@Override
	public IntegerClassExpression visit(OWLDataAllValuesFrom ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataExactCardinality ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataHasValue ce) {
		Objects.requireNonNull(ce);
		Integer dataPropertyId = getObjectPropertyExpressionTranslator().getTranslationRepository()
				.getId(ce.getProperty().asOWLDataProperty());
		Integer literalId = getTranslationRepository().getId(ce.getFiller());
		return getDataTypeFactory().createDataHasValue(dataPropertyId, literalId);
	}

	@Override
	public IntegerClassExpression visit(OWLDataMaxCardinality ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataMinCardinality ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataSomeValuesFrom ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectAllValuesFrom ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectComplementOf ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectExactCardinality ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectHasSelf ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectHasValue ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectIntersectionOf ce) {
		Objects.requireNonNull(ce);
		Set<OWLClassExpression> operands = ce.getOperands();
		Set<IntegerClassExpression> classExpressionList = new HashSet<>();
		operands.forEach(elem -> {
			classExpressionList.add(elem.accept(this));
		});
		return getDataTypeFactory().createObjectIntersectionOf(classExpressionList);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectMaxCardinality ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectMinCardinality ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectOneOf ce) {
		Objects.requireNonNull(ce);
		Set<OWLIndividual> indivSet = ce.getIndividuals();
		if (indivSet.isEmpty()) {
			throw new TranslationException("It is not possible to translate ObjectOneOf with empty set.");
		}
		if (indivSet.size() > 1) {
			throw new TranslationException(
					"It is not possible to translate ObjectOneOf with multiple individuals: '" + indivSet + "'.");
		}
		return getDataTypeFactory().createObjectOneOf(translateIndividual(indivSet.iterator().next()));
	}

	@Override
	public IntegerClassExpression visit(OWLObjectSomeValuesFrom ce) {
		Objects.requireNonNull(ce);
		IntegerObjectPropertyExpression propertyExpr = ce.getProperty().accept(getObjectPropertyExpressionTranslator());
		OWLClassExpression desc = ce.getFiller();
		IntegerClassExpression classExpression = desc.accept(this);
		return getDataTypeFactory().createObjectSomeValuesFrom(propertyExpr, classExpression);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectUnionOf ce) {
		Objects.requireNonNull(ce);
		throw TranslationException.newUnsupportedTranslationException(ce);
	}

}
