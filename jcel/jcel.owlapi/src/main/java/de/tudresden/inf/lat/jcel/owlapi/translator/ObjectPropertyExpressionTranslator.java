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

import java.util.Map;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * 
 * @author Julian Mendez
 */
public class ObjectPropertyExpressionTranslator implements
		OWLPropertyExpressionVisitorEx<IntegerObjectPropertyExpression> {

	private Map<OWLDataProperty, Integer> dataPropertyMap = null;
	private Map<OWLObjectProperty, Integer> objectPropertyMap = null;
	private IntegerDataTypeFactory factory;

	public ObjectPropertyExpressionTranslator(
			Map<OWLObjectProperty, Integer> objPropMap,
			Map<OWLDataProperty, Integer> dataPropMap,
			IntegerDataTypeFactory factory) {
		if (objPropMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (dataPropMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.dataPropertyMap = dataPropMap;
		this.objectPropertyMap = objPropMap;
		this.factory = factory;
	}

	public Integer getId(OWLDataProperty owlDataProperty) {
		if (owlDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.dataPropertyMap.get(owlDataProperty);
		if (ret == null) {
			throw TranslationException
					.newIncompleteMapException(owlDataProperty.toString());
		}
		return ret;
	}

	public Integer getId(OWLObjectProperty owlObjectProperty)
			throws TranslationException {
		if (owlObjectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.objectPropertyMap.get(owlObjectProperty);
		if (ret == null) {
			throw TranslationException
					.newIncompleteMapException(owlObjectProperty.toString());
		}
		return ret;
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

		return getDataTypeFactory().createObjectProperty(getId(property));
	}

	public IntegerDataTypeFactory getDataTypeFactory() {
		return this.factory;
	}

}
