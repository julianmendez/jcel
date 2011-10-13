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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object of this class is a visitor use to translate an owl axiom into an
 * integer axiom.
 * 
 * @author Julian Mendez
 */
public class AxiomTranslator implements
		OWLAxiomVisitorEx<Set<ComplexIntegerAxiom>> {

	private ClassExpressionTranslator classExpressionTranslator;

	public AxiomTranslator(ClassExpressionTranslator translator) {
		if (translator == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.classExpressionTranslator = translator;
	}

	private OWLDataProperty asOWLDataProperty(
			OWLDataPropertyExpression expression) {
		if (!(expression instanceof OWLDataProperty)) {
			throw new TranslationException(
					"Data property expression cannot be translated: '"
							+ expression + "'.");
		}
		return expression.asOWLDataProperty();
	}

	private OWLObjectProperty asOWLObjectProperty(
			OWLObjectPropertyExpression expression) {
		if (!(expression instanceof OWLObjectProperty)) {
			throw new TranslationException(
					"Object property expression cannot be translated: '"
							+ expression + "'.");
		}
		return expression.asOWLObjectProperty();
	}

	public ClassExpressionTranslator getClassExpressionTranslator() {
		return this.classExpressionTranslator;
	}

	public IntegerClassExpression translate(
			OWLClassExpression owlClassExpression) throws TranslationException {
		return owlClassExpression.accept(getClassExpressionTranslator());
	}

	public IntegerObjectPropertyExpression translateObjectPropertyExpression(
			OWLObjectPropertyExpression objectPropertyExpr)
			throws TranslationException {
		return objectPropertyExpr.accept(getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator());
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAnnotationAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAnnotationPropertyDomainAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAnnotationPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLClassAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer individualId = getClassExpressionTranslator().getId(
				axiom.getIndividual());
		ComplexIntegerAxiom ret = new IntegerClassAssertionAxiom(
				translate(axiom.getClassExpression()), individualId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLDataProperty property = asOWLDataProperty(axiom.getProperty());
		Integer propertyId = getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getId(property);
		Integer subjectId = getClassExpressionTranslator().getId(
				axiom.getSubject());
		Integer objectId = getClassExpressionTranslator().getId(
				axiom.getObject());
		ComplexIntegerAxiom ret = new IntegerDataPropertyAssertionAxiom(
				propertyId, subjectId, objectId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDataPropertyDomainAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDataPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDatatypeDefinitionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLEntity entity = axiom.getEntity();
		Set<ComplexIntegerAxiom> ret = null;
		if (entity.isOWLClass()) {
			ComplexIntegerAxiom elem = new IntegerClassDeclarationAxiom(
					getClassExpressionTranslator().getId(entity.asOWLClass()));
			ret = Collections.singleton(elem);
		} else if (entity.isOWLObjectProperty()) {
			ComplexIntegerAxiom elem = new IntegerObjectPropertyDeclarationAxiom(
					getClassExpressionTranslator()
							.getObjectPropertyExpressionTranslator().getId(
									entity.asOWLObjectProperty()));
			ret = Collections.singleton(elem);
		} else if (entity.isOWLNamedIndividual()) {
			ComplexIntegerAxiom elem = new IntegerNamedIndividualDeclarationAxiom(
					getClassExpressionTranslator().getId(
							entity.asOWLNamedIndividual()));
			ret = Collections.singleton(elem);
		} else if (entity.isOWLDataProperty()) {
			ComplexIntegerAxiom elem = new IntegerDataPropertyDeclarationAxiom(
					getClassExpressionTranslator()
							.getObjectPropertyExpressionTranslator().getId(
									entity.asOWLDataProperty()));
			ret = Collections.singleton(elem);
		} else if (entity.isOWLAnnotationProperty()) {
			// it is ignored
			ret = Collections.emptySet();
		} else {
			throw TranslationException.newUnsupportedAxiomException(axiom);
		}
		return ret;
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDifferentIndividualsAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<Integer> individualIdSet = new HashSet<Integer>();
		for (OWLIndividual individual : individualSet) {
			individualIdSet.add(getClassExpressionTranslator()
					.getId(individual));
		}
		ComplexIntegerAxiom ret = new IntegerDifferentIndividualsAxiom(
				individualIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointClassesAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClassExpression> classExpressionSet = axiom
				.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression classExpression : classExpressionSet) {
			classIdSet.add(translate(classExpression));
		}
		ComplexIntegerAxiom ret = new IntegerDisjointClassesAxiom(classIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointDataPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointUnionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLEquivalentClassesAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClassExpression> classExpressionSet = axiom
				.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression classExpression : classExpressionSet) {
			classIdSet.add(translate(classExpression));
		}
		ComplexIntegerAxiom ret = new IntegerEquivalentClassesAxiom(classIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLEquivalentDataPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(
			OWLEquivalentObjectPropertiesAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLObjectPropertyExpression> propertySet = axiom.getProperties();
		Set<Integer> propertyIdSet = new HashSet<Integer>();
		for (OWLObjectPropertyExpression propertyExpr : propertySet) {
			if (propertyExpr instanceof OWLObjectProperty) {
				propertyIdSet.add(getClassExpressionTranslator()
						.getObjectPropertyExpressionTranslator().getId(
								propertyExpr.asOWLObjectProperty()));
			} else {
				throw new IllegalStateException();
			}

		}
		ComplexIntegerAxiom ret = new IntegerEquivalentObjectPropertiesAxiom(
				propertyIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLFunctionalDataPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		Integer propertyId = getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getId(property);
		ComplexIntegerAxiom ret = new IntegerFunctionalObjectPropertyAxiom(
				propertyId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLHasKeyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(
			OWLInverseFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		Integer propertyId = getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getId(property);
		ComplexIntegerAxiom ret = new IntegerInverseFunctionalObjectPropertyAxiom(
				propertyId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLInverseObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty firstProperty = asOWLObjectProperty(axiom
				.getFirstProperty());
		OWLObjectProperty secondProperty = asOWLObjectProperty(axiom
				.getSecondProperty());
		Integer firstPropertyId = getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getId(firstProperty);
		Integer secondPropertyId = getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getId(secondProperty);
		ComplexIntegerAxiom ret = new IntegerInverseObjectPropertiesAxiom(
				firstPropertyId, secondPropertyId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(
			OWLIrreflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(
			OWLNegativeDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(
			OWLNegativeObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerObjectPropertyExpression propertyExpr = translateObjectPropertyExpression(axiom
				.getProperty());
		Integer subjectId = getClassExpressionTranslator().getId(
				axiom.getSubject());
		Integer objectId = getClassExpressionTranslator().getId(
				axiom.getObject());
		ComplexIntegerAxiom ret = new IntegerNegativeObjectPropertyAssertionAxiom(
				propertyExpr, subjectId, objectId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerObjectPropertyExpression propertyExpr = translateObjectPropertyExpression(axiom
				.getProperty());
		Integer subjectId = getClassExpressionTranslator().getId(
				axiom.getSubject());
		Integer objectId = getClassExpressionTranslator().getId(
				axiom.getObject());
		ComplexIntegerAxiom ret = new IntegerObjectPropertyAssertionAxiom(
				propertyExpr, subjectId, objectId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLObjectPropertyDomainAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		ComplexIntegerAxiom ret = null;
		IntegerObjectPropertyExpression propertyExpr = translateObjectPropertyExpression(axiom
				.getProperty());
		OWLClassExpression classExpression = axiom.getDomain();
		IntegerClassExpression superClassExpression = translate(classExpression);
		IntegerClassExpression subClassExpression = new IntegerObjectSomeValuesFrom(
				propertyExpr, new IntegerClass(IntegerDatatype.classTopElement));
		ret = new IntegerSubClassOfAxiom(subClassExpression,
				superClassExpression);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLObjectPropertyRangeAxiom rangeAxiom)
			throws TranslationException {
		if (rangeAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = asOWLObjectProperty(rangeAxiom
				.getProperty());
		OWLClassExpression classExpression = rangeAxiom.getRange();
		ComplexIntegerAxiom ret = new IntegerPropertyRangeAxiom(
				getClassExpressionTranslator()
						.getObjectPropertyExpressionTranslator()
						.getId(property), translate(classExpression));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLReflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		Integer propertyId = getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getId(property);
		ComplexIntegerAxiom ret = new IntegerReflexiveObjectPropertyAxiom(
				propertyId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSameIndividualAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<Integer> individualIdSet = new HashSet<Integer>();
		for (OWLIndividual individual : individualSet) {
			individualIdSet.add(getClassExpressionTranslator()
					.getId(individual));
		}
		ComplexIntegerAxiom ret = new IntegerSameIndividualAxiom(
				individualIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubClassOfAxiom classAxiom)
			throws TranslationException {
		if (classAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLClassExpression owlSubClass = classAxiom.getSubClass();
		OWLClassExpression owlSuperClass = classAxiom.getSuperClass();
		IntegerClassExpression leftDescription = translate(owlSubClass);
		IntegerClassExpression rightDescription = translate(owlSuperClass);
		ComplexIntegerAxiom ret = new IntegerSubClassOfAxiom(leftDescription,
				rightDescription);
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubDataPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubObjectPropertyOfAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectPropertyExpression leftPropExpr = axiom.getSubProperty();
		OWLObjectPropertyExpression rightPropExpr = axiom.getSuperProperty();
		ComplexIntegerAxiom ret = new IntegerSubObjectPropertyOfAxiom(
				translateObjectPropertyExpression(leftPropExpr),
				translateObjectPropertyExpression(rightPropExpr));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubPropertyChainOfAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<OWLObjectPropertyExpression> propChain = axiom.getPropertyChain();
		OWLObjectPropertyExpression superProperty = axiom.getSuperProperty();
		List<IntegerObjectPropertyExpression> chain = new ArrayList<IntegerObjectPropertyExpression>();
		for (OWLObjectPropertyExpression property : propChain) {
			chain.add(translateObjectPropertyExpression(property));
		}
		ComplexIntegerAxiom ret = new IntegerSubPropertyChainOfAxiom(chain,
				translateObjectPropertyExpression(superProperty));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSymmetricObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLTransitiveObjectPropertyAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		ComplexIntegerAxiom ret = new IntegerTransitiveObjectPropertyAxiom(
				getClassExpressionTranslator()
						.getObjectPropertyExpressionTranslator()
						.getId(property));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(SWRLRule axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

}