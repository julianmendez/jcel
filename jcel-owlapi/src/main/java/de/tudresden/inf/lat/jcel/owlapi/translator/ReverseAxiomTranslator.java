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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import de.tudresden.inf.lat.jcel.coreontology.axiom.FunctObjectPropAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NominalAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RangeAxiom;

/**
 * An object of this class translates normalized integer axioms into OWL axioms.
 * 
 * @author Julian Mendez
 *
 */
public class ReverseAxiomTranslator implements NormalizedIntegerAxiomVisitor<OWLAxiom> {

	private final Translator translator;
	private final OWLOntology ontology;

	/**
	 * Creates a new reverse axiom translator
	 * 
	 * @param translator
	 *            translator
	 * @param ontology
	 *            ontology
	 */
	public ReverseAxiomTranslator(Translator translator, OWLOntology ontology) {
		Objects.requireNonNull(translator);
		Objects.requireNonNull(ontology);
		this.translator = translator;
		this.ontology = ontology;
	}

	@Override
	public OWLAxiom visit(FunctObjectPropAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty owlProperty = translator.getTranslationRepository().getOWLObjectProperty(axiom.getProperty());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		return dataFactory.getOWLFunctionalObjectPropertyAxiom(owlProperty, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(GCI0Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLClass owlSubClass = translator.getTranslationRepository().getOWLClass(axiom.getSubClass());
		OWLClass owlSuperClass = translator.getTranslationRepository().getOWLClass(axiom.getSuperClass());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		return dataFactory.getOWLSubClassOfAxiom(owlSubClass, owlSuperClass, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(GCI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLClass owlLeftSubClass = translator.getTranslationRepository().getOWLClass(axiom.getLeftSubClass());
		OWLClass owlRightSubClass = translator.getTranslationRepository().getOWLClass(axiom.getRightSubClass());
		OWLClass owlSuperClass = translator.getTranslationRepository().getOWLClass(axiom.getSuperClass());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		Set<OWLClass> set = new HashSet<>();
		set.add(owlLeftSubClass);
		set.add(owlRightSubClass);
		OWLClassExpression owlObjectIntersectionOf = dataFactory.getOWLObjectIntersectionOf(set);
		return dataFactory.getOWLSubClassOfAxiom(owlObjectIntersectionOf, owlSuperClass, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(GCI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLClass owlSubClass = translator.getTranslationRepository().getOWLClass(axiom.getSubClass());
		OWLClass owlClassInSuperClass = translator.getTranslationRepository().getOWLClass(axiom.getClassInSuperClass());
		OWLObjectProperty owlObjectProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getPropertyInSuperClass());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLClassExpression owlObjectSomeValuesFrom = dataFactory.getOWLObjectSomeValuesFrom(owlObjectProperty,
				owlClassInSuperClass);
		return dataFactory.getOWLSubClassOfAxiom(owlSubClass, owlObjectSomeValuesFrom, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(GCI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLClass owlSuperClass = translator.getTranslationRepository().getOWLClass(axiom.getSuperClass());
		OWLClass owlClassInSubClass = translator.getTranslationRepository().getOWLClass(axiom.getClassInSubClass());
		OWLObjectProperty owlObjectProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getPropertyInSubClass());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLClassExpression owlObjectSomeValuesFrom = dataFactory.getOWLObjectSomeValuesFrom(owlObjectProperty,
				owlClassInSubClass);
		return dataFactory.getOWLSubClassOfAxiom(owlObjectSomeValuesFrom, owlSuperClass, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(NominalAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLNamedIndividual owlIndividual = translator.getTranslationRepository()
				.getOWLNamedIndividual(axiom.getIndividual());
		OWLClass owlClass = translator.getTranslationRepository().getOWLClass(axiom.getClassExpression());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLObjectOneOf owlObjectOneOf = dataFactory.getOWLObjectOneOf(owlIndividual);
		Set<OWLClassExpression> owlClassExpressions = new HashSet<>();
		owlClassExpressions.add(owlObjectOneOf);
		owlClassExpressions.add(owlClass);
		return dataFactory.getOWLEquivalentClassesAxiom(owlClassExpressions, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(RangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty owlObjectProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getProperty());
		OWLClass owlClass = translator.getTranslationRepository().getOWLClass(axiom.getRange());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		return dataFactory.getOWLObjectPropertyRangeAxiom(owlObjectProperty, owlClass, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(RI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty owlSuperProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getSuperProperty());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		List<OWLObjectProperty> owlPropertyList = new ArrayList<>();
		return dataFactory.getOWLSubPropertyChainOfAxiom(owlPropertyList, owlSuperProperty, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(RI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty owlSubProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getSubProperty());
		OWLObjectProperty owlSuperProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getSuperProperty());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		return dataFactory.getOWLSubObjectPropertyOfAxiom(owlSubProperty, owlSuperProperty, owlAnnotations);
	}

	@Override
	public OWLAxiom visit(RI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		OWLObjectProperty owlLeftSubProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getLeftSubProperty());
		OWLObjectProperty owlRightSubProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getRightSubProperty());
		OWLObjectProperty owlSuperProperty = translator.getTranslationRepository()
				.getOWLObjectProperty(axiom.getSuperProperty());
		Set<OWLAnnotation> owlAnnotations = translateAnnotations(axiom.getAnnotations());
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		List<OWLObjectProperty> owlPropertyList = new ArrayList<>();
		owlPropertyList.add(owlLeftSubProperty);
		owlPropertyList.add(owlRightSubProperty);
		return dataFactory.getOWLSubPropertyChainOfAxiom(owlPropertyList, owlSuperProperty, owlAnnotations);
	}

	OWLAnnotation translateAnnotation(IntegerAnnotation annotation) {
		Objects.requireNonNull(annotation);
		OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLAnnotationProperty owlAnnotationProperty = translator.getTranslationRepository()
				.getOWLAnnotationProperty(annotation.getAnnotationProperty());
		OWLAnnotationValue owlAnnotationValue = translator.getTranslationRepository()
				.getOWLAnnotationValue(annotation.getAnnotationProperty());
		OWLAnnotation owlAnnotation = factory.getOWLAnnotation(owlAnnotationProperty, owlAnnotationValue);
		return owlAnnotation;
	}

	Set<OWLAnnotation> translateAnnotations(Set<IntegerAnnotation> annotations) {
		Objects.requireNonNull(annotations);
		Set<OWLAnnotation> owlAnnotations = new HashSet<>();
		annotations.forEach(annotation -> owlAnnotations.add(translateAnnotation(annotation)));
		return owlAnnotations;
	}

}
