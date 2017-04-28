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

import java.util.Objects;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectInverseOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class can translate an object property expression of the
 * OWL API into the integer-based representation.
 * 
 * @author Julian Mendez
 */
public class ObjectPropertyExpressionTranslator
		implements OWLPropertyExpressionVisitorEx<IntegerObjectPropertyExpression> {

	private final IntegerDataTypeFactory factory;
	private final TranslationRepository repository;

	/**
	 * Constructs a new object property expression translator.
	 * 
	 * @param factory
	 *            data type factory
	 * @param repository
	 *            translation repository
	 */
	public ObjectPropertyExpressionTranslator(IntegerDataTypeFactory factory, TranslationRepository repository) {
		Objects.requireNonNull(factory);
		Objects.requireNonNull(repository);
		this.factory = factory;
		this.repository = repository;
	}

	public IntegerDataTypeFactory getDataTypeFactory() {
		return this.factory;
	}

	public TranslationRepository getTranslationRepository() {
		return this.repository;
	}

	@Override
	public IntegerObjectPropertyExpression visit(OWLDataProperty property) {
		Objects.requireNonNull(property);
		throw TranslationException.newUnsupportedTranslationException(property);
	}

	@Override
	public IntegerObjectPropertyExpression visit(OWLObjectInverseOf property) {
		Objects.requireNonNull(property);
		IntegerObjectPropertyExpression propExpr = property.getInverse().accept(this);
		IntegerObjectPropertyExpression ret;
		if (propExpr instanceof IntegerObjectProperty) {
			ret = getDataTypeFactory().createObjectInverseOf((IntegerObjectProperty) propExpr);
		} else if (propExpr instanceof IntegerObjectInverseOf) {
			ret = ((IntegerObjectInverseOf) propExpr).getInverse();
		} else {
			throw new IllegalArgumentException("Object property expression cannot be translated: " + property);
		}

		return ret;
	}

	@Override
	public IntegerObjectPropertyExpression visit(OWLObjectProperty owlObjectProperty) {
		Objects.requireNonNull(owlObjectProperty);
		getTranslationRepository().addObjectProperty(owlObjectProperty);
		return getDataTypeFactory().createObjectProperty(getTranslationRepository().getId(owlObjectProperty));
	}

	@Override
	public IntegerObjectPropertyExpression visit(OWLAnnotationProperty owlAnnotationProperty) {
		Objects.requireNonNull(owlAnnotationProperty);
		// TODO Auto-generated method stub
		return null;
	}

}
