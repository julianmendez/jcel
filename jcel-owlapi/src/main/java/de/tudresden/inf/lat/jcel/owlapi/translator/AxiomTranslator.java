/*
 *
 * Copyright (C) 2009-2015 Julian Mendez
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClass;
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
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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

import com.google.common.base.Optional;

import de.tudresden.inf.lat.jcel.coreontology.axiom.Annotation;
import de.tudresden.inf.lat.jcel.coreontology.axiom.AnnotationImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class is a visitor use to translate an owl axiom into an
 * integer axiom.
 * 
 * @author Julian Mendez
 */
public class AxiomTranslator implements OWLAxiomVisitorEx<Set<ComplexIntegerAxiom>> {

	private final ClassExpressionTranslator classExpressionTranslator;
	private final IntegerOntologyObjectFactory factory;

	public AxiomTranslator(ClassExpressionTranslator translator, IntegerOntologyObjectFactory factory) {
		Objects.requireNonNull(translator);
		Objects.requireNonNull(factory);
		this.factory = factory;
		this.classExpressionTranslator = translator;
	}

	private OWLDataProperty asOWLDataProperty(OWLDataPropertyExpression expression) {
		if (!(expression instanceof OWLDataProperty)) {
			throw new TranslationException("Data property expression cannot be translated: '" + expression + "'.");
		}
		return expression.asOWLDataProperty();
	}

	private OWLObjectProperty asOWLObjectProperty(OWLObjectPropertyExpression expression) {
		if (!(expression instanceof OWLObjectProperty)) {
			throw new TranslationException("Object property expression cannot be translated: '" + expression + "'.");
		}
		return expression.asOWLObjectProperty();
	}

	private ComplexIntegerAxiomFactory getAxiomFactory() {
		return this.factory.getComplexAxiomFactory();
	}

	public ClassExpressionTranslator getClassExpressionTranslator() {
		return this.classExpressionTranslator;
	}

	public IntegerDataTypeFactory getDataTypeFactory() {
		return this.factory.getDataTypeFactory();
	}

	public TranslationRepository getTranslationRepository() {
		return this.classExpressionTranslator.getTranslationRepository();
	}

	public IntegerClassExpression translate(OWLClassExpression owlClassExpression) throws TranslationException {
		return owlClassExpression.accept(getClassExpressionTranslator());
	}

	public Annotation translateAnnotation(OWLAnnotation owlAnnotation) {
		URI property = owlAnnotation.getProperty().getIRI().toURI();
		Optional<IRI> optIRI = owlAnnotation.getValue().asIRI();
		Optional<OWLLiteral> optLiteral = owlAnnotation.getValue().asLiteral();
		String value = null;
		if (optIRI.isPresent()) {
			value = optIRI.get().toString();
		} else if (optLiteral.isPresent()) {
			value = optLiteral.get().getLiteral();
		} else {
			value = "";
		}
		Annotation ret = new AnnotationImpl(property, value);
		return ret;
	}

	public Set<Annotation> translateAnnotations(Set<OWLAnnotation> owlAnnotations) {
		Set<Annotation> ret = new TreeSet<>();
		owlAnnotations.forEach(owlAnnotation -> ret.add(translateAnnotation(owlAnnotation)));
		return ret;
	}

	public IntegerObjectPropertyExpression translateObjectPropertyExpression(
			OWLObjectPropertyExpression objectPropertyExpr) throws TranslationException {
		return objectPropertyExpr.accept(getClassExpressionTranslator().getObjectPropertyExpressionTranslator());
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAnnotationAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAnnotationPropertyDomainAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAnnotationPropertyRangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLClassAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		Integer individualId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getIndividual());
		ComplexIntegerAxiom ret = getAxiomFactory().createClassAssertionAxiom(translate(axiom.getClassExpression()),
				individualId, translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDataPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLDataProperty property = asOWLDataProperty(axiom.getProperty());
		Integer propertyId = getClassExpressionTranslator().getObjectPropertyExpressionTranslator()
				.getTranslationRepository().getId(property);
		Integer subjectId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getSubject());
		Integer objectId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getObject());
		ComplexIntegerAxiom ret = getAxiomFactory().createDataPropertyAssertionAxiom(propertyId, subjectId, objectId,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDataPropertyDomainAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDataPropertyRangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDatatypeDefinitionAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	private Set<ComplexIntegerAxiom> declare(OWLClass owlClass, Set<OWLAnnotation> annotations) {
		getTranslationRepository().addClass(owlClass);
		ComplexIntegerAxiom elem = getAxiomFactory().createClassDeclarationAxiom(
				getTranslationRepository().getId(owlClass), translateAnnotations(annotations));
		return Collections.singleton(elem);

	}

	private Set<ComplexIntegerAxiom> declare(OWLObjectProperty owlObjectProperty, Set<OWLAnnotation> annotations) {
		getTranslationRepository().addObjectProperty(owlObjectProperty);
		ComplexIntegerAxiom elem = getAxiomFactory().createObjectPropertyDeclarationAxiom(
				getTranslationRepository().getId(owlObjectProperty), translateAnnotations(annotations));
		return Collections.singleton(elem);
	}

	private Set<ComplexIntegerAxiom> declare(OWLNamedIndividual owlNamedIndividual, Set<OWLAnnotation> annotations) {
		getTranslationRepository().addNamedIndividual(owlNamedIndividual);
		ComplexIntegerAxiom elem = getAxiomFactory().createNamedIndividualDeclarationAxiom(
				getTranslationRepository().getId(owlNamedIndividual), translateAnnotations(annotations));
		return Collections.singleton(elem);
	}

	private Set<ComplexIntegerAxiom> declare(OWLDataProperty owlDataProperty, Set<OWLAnnotation> annotations) {
		getTranslationRepository().addDataProperty(owlDataProperty);
		ComplexIntegerAxiom elem = getAxiomFactory().createDataPropertyDeclarationAxiom(
				getTranslationRepository().getId(owlDataProperty), translateAnnotations(annotations));
		return Collections.singleton(elem);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLEntity entity = axiom.getEntity();
		if (entity.isOWLClass()) {
			return declare(entity.asOWLClass(), axiom.getAnnotations());

		} else if (entity.isOWLObjectProperty()) {
			return declare(entity.asOWLObjectProperty(), axiom.getAnnotations());

		} else if (entity.isOWLNamedIndividual()) {
			return declare(entity.asOWLNamedIndividual(), axiom.getAnnotations());

		} else if (entity.isOWLDataProperty()) {
			return declare(entity.asOWLDataProperty(), axiom.getAnnotations());

		} else if (entity.isOWLAnnotationProperty()) {
			// FIXME add annotation property
			return Collections.emptySet();

		} else {
			throw TranslationException.newUnsupportedAxiomException(axiom);

		}
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDifferentIndividualsAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<Integer> individualIdSet = new HashSet<>();
		individualSet.forEach(individual -> individualIdSet
				.add(getClassExpressionTranslator().getTranslationRepository().getId(individual)));
		ComplexIntegerAxiom ret = getAxiomFactory().createDifferentIndividualsAxiom(individualIdSet,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointClassesAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		Set<OWLClassExpression> classExpressionSet = axiom.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<>();
		classExpressionSet.forEach(classExpression -> classIdSet.add(translate(classExpression)));
		ComplexIntegerAxiom ret = getAxiomFactory().createDisjointClassesAxiom(classIdSet,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointDataPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointObjectPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLDisjointUnionAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLEquivalentClassesAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		Set<OWLClassExpression> classExpressionSet = axiom.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<>();
		classExpressionSet.forEach(classExpression -> classIdSet.add(translate(classExpression)));
		ComplexIntegerAxiom ret = getAxiomFactory().createEquivalentClassesAxiom(classIdSet,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLEquivalentDataPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLEquivalentObjectPropertiesAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		Set<OWLObjectPropertyExpression> propertySet = axiom.getProperties();
		Set<IntegerObjectPropertyExpression> propertyExprSet = new HashSet<>();
		propertySet.forEach(propertyExpr -> {
			if (propertyExpr instanceof OWLObjectProperty) {
				propertyExprSet.add(getDataTypeFactory()
						.createObjectProperty(getClassExpressionTranslator().getObjectPropertyExpressionTranslator()
								.getTranslationRepository().getId(propertyExpr.asOWLObjectProperty())));
			} else {
				throw new IllegalStateException();
			}
		});
		ComplexIntegerAxiom ret = getAxiomFactory().createEquivalentObjectPropertiesAxiom(propertyExprSet,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLFunctionalDataPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLFunctionalObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		IntegerObjectPropertyExpression propExpr = this.getDataTypeFactory()
				.createObjectProperty(getClassExpressionTranslator().getObjectPropertyExpressionTranslator()
						.getTranslationRepository().getId(property));
		ComplexIntegerAxiom ret = getAxiomFactory().createFunctionalObjectPropertyAxiom(propExpr,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLHasKeyAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		IntegerObjectProperty propExpr = getDataTypeFactory().createObjectProperty(getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getTranslationRepository().getId(property));
		ComplexIntegerAxiom ret = getAxiomFactory().createInverseFunctionalObjectPropertyAxiom(propExpr,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLInverseObjectPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty firstProperty = asOWLObjectProperty(axiom.getFirstProperty());
		OWLObjectProperty secondProperty = asOWLObjectProperty(axiom.getSecondProperty());

		IntegerObjectProperty firstPropertyExpr = getDataTypeFactory()
				.createObjectProperty(getClassExpressionTranslator().getObjectPropertyExpressionTranslator()
						.getTranslationRepository().getId(firstProperty));
		IntegerObjectProperty secondPropertyExpr = getDataTypeFactory()
				.createObjectProperty(getClassExpressionTranslator().getObjectPropertyExpressionTranslator()
						.getTranslationRepository().getId(secondProperty));
		ComplexIntegerAxiom ret = getAxiomFactory().createInverseObjectPropertiesAxiom(firstPropertyExpr,
				secondPropertyExpr, translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		IntegerObjectPropertyExpression propertyExpr = translateObjectPropertyExpression(axiom.getProperty());
		Integer subjectId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getSubject());
		Integer objectId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getObject());
		ComplexIntegerAxiom ret = getAxiomFactory().createNegativeObjectPropertyAssertionAxiom(propertyExpr, subjectId,
				objectId, translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLObjectPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		IntegerObjectPropertyExpression propertyExpr = translateObjectPropertyExpression(axiom.getProperty());
		Integer subjectId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getSubject());
		Integer objectId = getClassExpressionTranslator().getTranslationRepository().getId(axiom.getObject());
		ComplexIntegerAxiom ret = getAxiomFactory().createObjectPropertyAssertionAxiom(propertyExpr, subjectId,
				objectId, translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLObjectPropertyDomainAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		IntegerObjectPropertyExpression propertyExpr = translateObjectPropertyExpression(axiom.getProperty());
		OWLClassExpression classExpression = axiom.getDomain();
		IntegerClassExpression superClassExpression = translate(classExpression);
		IntegerClassExpression subClassExpression = getDataTypeFactory().createObjectSomeValuesFrom(propertyExpr,
				getDataTypeFactory().createClass(IntegerEntityManager.topClassId));
		ComplexIntegerAxiom ret = getAxiomFactory().createSubClassOfAxiom(subClassExpression, superClassExpression,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLObjectPropertyRangeAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		OWLClassExpression classExpression = axiom.getRange();
		IntegerObjectProperty propExpr = getDataTypeFactory().createObjectProperty(getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getTranslationRepository().getId(property));
		ComplexIntegerAxiom ret = getAxiomFactory().createPropertyRangeAxiom(propExpr, translate(classExpression),
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLReflexiveObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty property = asOWLObjectProperty(axiom.getProperty());
		IntegerObjectProperty propertyExpr = getDataTypeFactory().createObjectProperty(getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getTranslationRepository().getId(property));
		ComplexIntegerAxiom ret = getAxiomFactory().createReflexiveObjectPropertyAxiom(propertyExpr,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSameIndividualAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<Integer> individualIdSet = new HashSet<>();
		individualSet.forEach(individual -> individualIdSet
				.add(getClassExpressionTranslator().getTranslationRepository().getId(individual)));
		ComplexIntegerAxiom ret = getAxiomFactory().createSameIndividualAxiom(individualIdSet,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubClassOfAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		OWLClassExpression owlSubClass = axiom.getSubClass();
		OWLClassExpression owlSuperClass = axiom.getSuperClass();
		IntegerClassExpression leftDescription = translate(owlSubClass);
		IntegerClassExpression rightDescription = translate(owlSuperClass);
		ComplexIntegerAxiom ret = getAxiomFactory().createSubClassOfAxiom(leftDescription, rightDescription,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubDataPropertyOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubObjectPropertyOfAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		OWLObjectPropertyExpression leftPropExpr = axiom.getSubProperty();
		OWLObjectPropertyExpression rightPropExpr = axiom.getSuperProperty();
		ComplexIntegerAxiom ret = getAxiomFactory().createSubObjectPropertyOfAxiom(
				translateObjectPropertyExpression(leftPropExpr), translateObjectPropertyExpression(rightPropExpr),
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSubPropertyChainOfAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		List<OWLObjectPropertyExpression> propChain = axiom.getPropertyChain();
		OWLObjectPropertyExpression superProperty = axiom.getSuperProperty();
		List<IntegerObjectPropertyExpression> chain = new ArrayList<>();
		propChain.forEach(property -> chain.add(translateObjectPropertyExpression(property)));
		ComplexIntegerAxiom ret = getAxiomFactory().createSubPropertyChainOfAxiom(chain,
				translateObjectPropertyExpression(superProperty), translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLSymmetricObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(OWLTransitiveObjectPropertyAxiom axiom) throws TranslationException {
		Objects.requireNonNull(axiom);
		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		IntegerObjectProperty propExpr = getDataTypeFactory().createObjectProperty(getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator().getTranslationRepository().getId(property));
		ComplexIntegerAxiom ret = getAxiomFactory().createTransitiveObjectPropertyAxiom(propExpr,
				translateAnnotations(axiom.getAnnotations()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<ComplexIntegerAxiom> visit(SWRLRule axiom) {
		Objects.requireNonNull(axiom);
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

}
