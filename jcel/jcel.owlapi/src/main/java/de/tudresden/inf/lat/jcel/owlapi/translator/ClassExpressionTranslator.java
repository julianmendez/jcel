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

import java.util.HashSet;
import java.util.Map;
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
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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
 * 
 * @author Julian Mendez
 */
public class ClassExpressionTranslator implements
		OWLClassExpressionVisitorEx<IntegerClassExpression> {

	private Map<OWLClass, Integer> classMap = null;
	private Map<OWLNamedIndividual, Integer> individualMap = null;
	private Map<OWLLiteral, Integer> literalMap = null;
	private ObjectPropertyExpressionTranslator objectPropertyExpressionTranslator;

	public ClassExpressionTranslator(Map<OWLClass, Integer> clMap,
			Map<OWLNamedIndividual, Integer> indivMap,
			Map<OWLLiteral, Integer> litMap,
			ObjectPropertyExpressionTranslator translator) {
		if (translator == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (clMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (indivMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (litMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.objectPropertyExpressionTranslator = translator;
		this.classMap = clMap;
		this.individualMap = indivMap;
		this.literalMap = litMap;
	}

	public IntegerDataTypeFactory getDataTypeFactory() {
		return this.objectPropertyExpressionTranslator.getDataTypeFactory();
	}

	public Integer getId(OWLClass owlClass) throws TranslationException {
		if (owlClass == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.classMap.get(owlClass);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(owlClass
					.toString());
		}
		return ret;
	}

	public Integer getId(OWLIndividual individual) throws TranslationException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.individualMap.get(individual);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(individual
					.toString());
		}
		return ret;
	}

	public Integer getId(OWLLiteral owlLiteral) {
		if (owlLiteral == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.literalMap.get(owlLiteral);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(owlLiteral
					.toString());
		}
		return ret;
	}

	public ObjectPropertyExpressionTranslator getObjectPropertyExpressionTranslator() {
		return this.objectPropertyExpressionTranslator;
	}

	public Integer translateDataProperty(OWLDataProperty owlDataProperty) {
		if (owlDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getObjectPropertyExpressionTranslator().getId(owlDataProperty);
	}

	public Integer translateIndividual(OWLIndividual owlIndividual)
			throws TranslationException {
		if (owlIndividual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getId(owlIndividual);
	}

	public Integer translateLiteral(OWLLiteral owlLiteral)
			throws TranslationException {
		if (owlLiteral == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getId(owlLiteral);
	}

	@Override
	public IntegerClassExpression visit(OWLClass ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getDataTypeFactory().createClass(getId(ce));
	}

	@Override
	public IntegerClassExpression visit(OWLDataAllValuesFrom ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataExactCardinality ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataHasValue ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer dataPropertyId = getObjectPropertyExpressionTranslator().getId(
				ce.getProperty().asOWLDataProperty());
		Integer literalId = getId(ce.getValue());
		return getDataTypeFactory().createDataHasValue(dataPropertyId,
				literalId);
	}

	@Override
	public IntegerClassExpression visit(OWLDataMaxCardinality ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataMinCardinality ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLDataSomeValuesFrom ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectAllValuesFrom ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectComplementOf ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectExactCardinality ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectHasSelf ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectHasValue ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectIntersectionOf ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClassExpression> operands = ce.getOperands();
		Set<IntegerClassExpression> classExpressionList = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression elem : operands) {
			classExpressionList.add(elem.accept(this));
		}
		return getDataTypeFactory().createObjectIntersectionOf(
				classExpressionList);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectMaxCardinality ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectMinCardinality ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectOneOf ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLIndividual> indivSet = ce.getIndividuals();
		if (indivSet.isEmpty()) {
			throw new TranslationException(
					"It is not possible to translate ObjectOneOf with empty set.");
		}
		if (indivSet.size() > 1) {
			throw new TranslationException(
					"It is not possible to translate ObjectOneOf with multiple individuals: '"
							+ indivSet + "'.");
		}
		return getDataTypeFactory().createObjectOneOf(
				translateIndividual(indivSet.iterator().next()));
	}

	@Override
	public IntegerClassExpression visit(OWLObjectSomeValuesFrom ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerObjectPropertyExpression propertyExpr = ce.getProperty().accept(
				getObjectPropertyExpressionTranslator());
		OWLClassExpression desc = ce.getFiller();
		IntegerClassExpression classExpression = desc.accept(this);
		return getDataTypeFactory().createObjectSomeValuesFrom(propertyExpr,
				classExpression);
	}

	@Override
	public IntegerClassExpression visit(OWLObjectUnionOf ce) {
		if (ce == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedTranslationException(ce);
	}

}
