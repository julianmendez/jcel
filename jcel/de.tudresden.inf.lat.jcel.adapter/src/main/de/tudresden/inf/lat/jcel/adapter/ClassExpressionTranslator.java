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

package de.tudresden.inf.lat.jcel.adapter;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * An object of this class is a visitor used to translate an
 * <code>OWLDescription</code> in the OWL API 2 into an
 * <code>OWLClassExpression</code> in the OWL API 3. This is an auxiliary class
 * used by <code>SimpleTranslator</code>.
 * 
 * @author Julian Mendez
 * 
 */
public class ClassExpressionTranslator implements
		org.semanticweb.owl.model.OWLDescriptionVisitorEx<OWLClassExpression> {

	private SimpleTranslator translator = null;

	public ClassExpressionTranslator(SimpleTranslator trans) {
		this.translator = trans;
	}

	public OWLDataFactory getOWLDataFactory() {
		return this.translator.getOWLDataFactory();
	}

	public SimpleTranslator getTranslator() {
		return this.translator;
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLClass description) {
		return getTranslator().translate(description);
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLDataAllRestriction description) {
		return getOWLDataFactory().getOWLDataAllValuesFrom(
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLDataExactCardinalityRestriction description) {
		return getOWLDataFactory().getOWLDataExactCardinality(
				description.getCardinality(),
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLDataMaxCardinalityRestriction description) {
		return getOWLDataFactory().getOWLDataMaxCardinality(
				description.getCardinality(),
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLDataMinCardinalityRestriction description) {
		return getOWLDataFactory().getOWLDataMinCardinality(
				description.getCardinality(),
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLDataSomeRestriction description) {
		return getOWLDataFactory().getOWLDataSomeValuesFrom(
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLDataValueRestriction description) {
		return getOWLDataFactory().getOWLDataHasValue(
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getValue()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectAllRestriction description) {
		return getOWLDataFactory().getOWLObjectAllValuesFrom(
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectComplementOf description) {
		return getOWLDataFactory().getOWLObjectComplementOf(
				getTranslator().translate(description.getOperand()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectExactCardinalityRestriction description) {
		return getOWLDataFactory().getOWLObjectExactCardinality(
				description.getCardinality(),
				getTranslator().translate(description.getProperty()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectIntersectionOf description) {
		return getOWLDataFactory().getOWLObjectIntersectionOf(
				getTranslator().translateToClassExpressions(description.getOperands()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectMaxCardinalityRestriction description) {
		return getOWLDataFactory().getOWLObjectMaxCardinality(
				description.getCardinality(),
				getTranslator().translate(description.getProperty()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectMinCardinalityRestriction description) {
		return getOWLDataFactory().getOWLObjectMinCardinality(
				description.getCardinality(),
				getTranslator().translate(description.getProperty()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectOneOf description) {
		return getOWLDataFactory().getOWLObjectOneOf(
				getTranslator().translateToNamedIndividuals(description.getIndividuals()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectSelfRestriction description) {
		return getOWLDataFactory().getOWLObjectHasSelf(
				getTranslator().translate(description.getProperty()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectSomeRestriction description) {
		return getOWLDataFactory().getOWLObjectSomeValuesFrom(
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getFiller()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectUnionOf description) {
		return getOWLDataFactory().getOWLObjectUnionOf(
				getTranslator().translateToClassExpressions(description.getOperands()));
	}

	@Override
	public OWLClassExpression visit(
			org.semanticweb.owl.model.OWLObjectValueRestriction description) {
		return getOWLDataFactory().getOWLObjectHasValue(
				getTranslator().translate(description.getProperty()),
				getTranslator().translate(description.getValue()));
	}
}
