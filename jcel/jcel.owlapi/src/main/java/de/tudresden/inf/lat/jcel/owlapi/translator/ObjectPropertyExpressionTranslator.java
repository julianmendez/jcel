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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class can translate an object property expression of the
 * OWL API into the integer-based representation.
 * 
 * @author Julian Mendez
 */
public class ObjectPropertyExpressionTranslator implements
		OWLPropertyExpressionVisitorEx<IntegerObjectPropertyExpression> {

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
	public ObjectPropertyExpressionTranslator(IntegerDataTypeFactory factory,
			TranslationRepository repository) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (repository == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(property);
	}

	@Override
	public IntegerObjectPropertyExpression visit(OWLObjectInverseOf property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(property);
	}

	@Override
	public IntegerObjectPropertyExpression visit(OWLObjectProperty property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getDataTypeFactory().createObjectProperty(
				getTranslationRepository().getId(property));
	}

}
