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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * This class provides the functionality of an <code>OWLReasoner</code> of the
 * OWL API 2. It is prepared to work with an internal <code>OWLReasoner</code>
 * of the OWL API 3.
 * 
 * @author Julian Mendez
 */
public abstract class OWLReasonerAdapter implements
		org.semanticweb.owl.inference.OWLReasoner {

	private static final Logger logger = Logger
			.getLogger(OWLReasonerAdapter.class.getName());

	private SimpleInvTranslator invTranslator = null;
	private boolean isClassified = false;
	private Set<org.semanticweb.owl.model.OWLOntology> ontologies = null;
	private org.semanticweb.owl.model.OWLOntologyManager ontologyManager = null;
	private OWLReasoner reasoner = null;
	private SimpleTranslator translator = new SimpleTranslator(OWLManager
			.createOWLOntologyManager().getOWLDataFactory());

	@Override
	public void classify()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("classify()");
		getOWLReasoner().prepareReasoner();
		this.isClassified = true;
	}

	@Override
	public void clearOntologies()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("clearOntologies()");
		this.isClassified = false;
		loadAxiomSet(new HashSet<org.semanticweb.owl.model.OWLAxiom>());
	}

	/**
	 * Creates a new <code>OWLReasoner</code> of the OWL API 3.
	 * 
	 * @param ontology
	 *            the root ontology using the OWL API 3
	 * @return an OWLReasoner
	 */
	protected abstract OWLReasoner createReasoner(OWLOntology ontology);

	@Override
	public void dispose()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("dispose()");
		getOWLReasoner().dispose();
	}

	protected Set<org.semanticweb.owl.model.OWLClass> flatten(
			Set<Set<org.semanticweb.owl.model.OWLClass>> setOfSets) {
		Set<org.semanticweb.owl.model.OWLClass> ret = new HashSet<org.semanticweb.owl.model.OWLClass>();
		for (Set<org.semanticweb.owl.model.OWLClass> set : setOfSets) {
			ret.addAll(set);
		}
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLClass>> getAncestorClasses(
			org.semanticweb.owl.model.OWLDescription description)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getAncestorClasses(" + description + ")");
		Set<Set<org.semanticweb.owl.model.OWLClass>> ret = getInvTranslator()
				.translateToClasses(
						getOWLReasoner().getSuperClasses(
								getTranslator().translate(description), false));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLDataProperty>> getAncestorProperties(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getAncestorProperties((OWLDataProperty) " + dataProperty
				+ ")");
		Set<Set<org.semanticweb.owl.model.OWLDataProperty>> ret = getInvTranslator()
				.translateToDataProperties(
						getOWLReasoner().getSuperDataProperties(
								getTranslator().translate(dataProperty), false));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> getAncestorProperties(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getAncestorProperties((OWLObjectProperty) " + property
				+ ")");
		Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> ret = getInvTranslator()
				.translateToObjectProperties(
						getOWLReasoner().getSuperObjectProperties(
								getTranslator().translate(property), false));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Map<org.semanticweb.owl.model.OWLDataProperty, Set<org.semanticweb.owl.model.OWLConstant>> getDataPropertyRelationships(
			org.semanticweb.owl.model.OWLIndividual individual)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getDataPropertyRelationships(" + individual + ")");
		throw new TranslationException(
				"Unsupported translation : getDataPropertyRelationships(OWLIndividual)");
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLClass>> getDescendantClasses(
			org.semanticweb.owl.model.OWLDescription description)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getDescendantClasses(" + description + ")");
		Set<Set<org.semanticweb.owl.model.OWLClass>> ret = getInvTranslator()
				.translateToClasses(
						getOWLReasoner().getSubClasses(
								getTranslator().translate(description), false));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLDataProperty>> getDescendantProperties(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getDescendantProperties(" + dataProperty + "");
		Set<Set<org.semanticweb.owl.model.OWLDataProperty>> ret = getInvTranslator()
				.translateToDataProperties(
						getOWLReasoner().getSubDataProperties(
								getTranslator().translate(dataProperty), false));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> getDescendantProperties(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getDescendantProperties(" + property + ")");
		Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> ret = getInvTranslator()
				.translateToObjectProperties(
						getOWLReasoner().getSubObjectProperties(
								getTranslator().translate(property), false));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLDescription>> getDomains(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getDomains(" + dataProperty + ")");
		throw new TranslationException(
				"Unsupported translation : getDomains(OWLDataProperty)");
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLDescription>> getDomains(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getDomains(" + property + ")");
		throw new TranslationException(
				"Unsupported translation : getDomains(OWLObjectProperty)");
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLClass> getEquivalentClasses(
			org.semanticweb.owl.model.OWLDescription description)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getEquivalentClasses(" + description + ")");
		Set<org.semanticweb.owl.model.OWLClass> ret = getInvTranslator()
				.translateToClasses(
						getOWLReasoner().getEquivalentClasses(
								getTranslator().translate(
										description.asOWLClass())));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLDataProperty> getEquivalentProperties(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getEquivalentProperties((OWLDataProperty) "
				+ dataProperty + ")");
		Set<org.semanticweb.owl.model.OWLDataProperty> ret = getInvTranslator()
				.translateToDataProperties(
						getOWLReasoner().getEquivalentDataProperties(
								getTranslator().translate(dataProperty)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLObjectProperty> getEquivalentProperties(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getEquivalentProperties((OWLDataProperty) " + property
				+ ")");
		Set<org.semanticweb.owl.model.OWLObjectProperty> ret = getInvTranslator()
				.translateToObjectProperties(
						getOWLReasoner().getEquivalentObjectProperties(
								getTranslator().translate(property)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLClass> getInconsistentClasses()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getInconsistentClasses()");
		Set<org.semanticweb.owl.model.OWLClass> ret = getInvTranslator()
				.translateToClasses(getOWLReasoner().getUnsatisfiableClasses());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLIndividual> getIndividuals(
			org.semanticweb.owl.model.OWLDescription description, boolean direct)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getIndividuals(" + description + ", " + direct + ")");
		Set<org.semanticweb.owl.model.OWLIndividual> ret = getInvTranslator()
				.translateToIndividuals(
						getOWLReasoner().getInstances(
								getTranslator().translate(
										description.asOWLClass()), direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> getInverseProperties(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getInverseProperties(" + property + ")");
		throw new TranslationException(
				"Unsupported translation : getInverseProperties(OWLObjectProperty)");
	}

	public SimpleInvTranslator getInvTranslator() {
		return this.invTranslator;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLOntology> getLoadedOntologies() {
		logger.finer("getLoadedOntologies()");
		Set<org.semanticweb.owl.model.OWLOntology> ret = this.ontologies;
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Map<org.semanticweb.owl.model.OWLObjectProperty, Set<org.semanticweb.owl.model.OWLIndividual>> getObjectPropertyRelationships(
			org.semanticweb.owl.model.OWLIndividual individual)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getObjectPropertyRelationships(" + individual + ")");
		throw new TranslationException(
				"Unsupported translation : getObjectPropertyRelationships(OWLIndividual)");
	}

	public org.semanticweb.owl.model.OWLOntologyManager getOWLOntologyManager() {
		return this.ontologyManager;
	}

	public OWLReasoner getOWLReasoner() {
		return this.reasoner;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLDataRange> getRanges(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getRanges(" + dataProperty + ")");
		throw new TranslationException(
				"Unsupported translation : getRanges(OWLDataProperty)");
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLDescription> getRanges(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getRanges(" + property + ")");
		throw new TranslationException(
				"Unsupported translation : getRanges(OWLObjectProperty)");
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLIndividual> getRelatedIndividuals(
			org.semanticweb.owl.model.OWLIndividual individual,
			org.semanticweb.owl.model.OWLObjectPropertyExpression propertyExpression)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getRelatedIndividuals(" + individual + ", "
				+ propertyExpression + ")");
		Set<org.semanticweb.owl.model.OWLIndividual> ret = getInvTranslator()
				.translateToIndividuals(
						getOWLReasoner().getObjectPropertyValues(
								getTranslator().translate(individual),
								getTranslator().translate(propertyExpression)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<org.semanticweb.owl.model.OWLConstant> getRelatedValues(
			org.semanticweb.owl.model.OWLIndividual individual,
			org.semanticweb.owl.model.OWLDataPropertyExpression dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getRelatedValues(" + individual + ", " + dataProperty
				+ ")");
		throw new TranslationException(
				"Unsupported translation : getRelatedValues(OWLIndividual, OWLDataPropertyExpression)");
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLClass>> getSubClasses(
			org.semanticweb.owl.model.OWLDescription description)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getSubClasses(" + description + ")");
		Set<Set<org.semanticweb.owl.model.OWLClass>> ret = getInvTranslator()
				.translateToClasses(
						getOWLReasoner().getSubClasses(
								getTranslator().translate(description), true));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLDataProperty>> getSubProperties(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger
				.finer("getSubProperties((OWLDataProperty) " + dataProperty
						+ ")");
		Set<Set<org.semanticweb.owl.model.OWLDataProperty>> ret = getInvTranslator()
				.translateToDataProperties(
						getOWLReasoner().getSubDataProperties(
								getTranslator().translate(dataProperty), true));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> getSubProperties(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getSubProperties((OWLObjectProperty) " + property + ")");
		Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> ret = getInvTranslator()
				.translateToObjectProperties(
						getOWLReasoner().getSubObjectProperties(
								getTranslator().translate(property), true));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLClass>> getSuperClasses(
			org.semanticweb.owl.model.OWLDescription description)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getSuperClasses(" + description + ")");
		Set<Set<org.semanticweb.owl.model.OWLClass>> ret = getInvTranslator()
				.translateToClasses(
						getOWLReasoner().getSuperClasses(
								getTranslator().translate(description), true));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLDataProperty>> getSuperProperties(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getSuperProperties((OWLDataProperty) " + dataProperty
				+ ")");
		Set<Set<org.semanticweb.owl.model.OWLDataProperty>> ret = getInvTranslator()
				.translateToDataProperties(
						getOWLReasoner().getSuperDataProperties(
								getTranslator().translate(dataProperty), true));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> getSuperProperties(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger
				.finer("getSuperProperties((OWLObjectProperty) " + property
						+ ")");
		Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> ret = getInvTranslator()
				.translateToObjectProperties(
						getOWLReasoner().getSuperObjectProperties(
								getTranslator().translate(property), true));
		logger.finer("" + ret);
		return ret;
	}

	public SimpleTranslator getTranslator() {
		return this.translator;
	}

	@Override
	public Set<Set<org.semanticweb.owl.model.OWLClass>> getTypes(
			org.semanticweb.owl.model.OWLIndividual individual, boolean direct)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("getTypes(" + individual + ", " + direct + ")");
		Set<Set<org.semanticweb.owl.model.OWLClass>> ret = getInvTranslator()
				.translateToClasses(
						getOWLReasoner().getTypes(
								getTranslator().translate(individual), direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean hasDataPropertyRelationship(
			org.semanticweb.owl.model.OWLIndividual subject,
			org.semanticweb.owl.model.OWLDataPropertyExpression dataProperty,
			org.semanticweb.owl.model.OWLConstant object)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("hasDataPropertyRelationship(" + subject + ",  "
				+ dataProperty + ", " + object + ")");
		boolean ret = getOWLReasoner().getDataPropertyValues(
				getTranslator().translate(subject),
				getTranslator().translate(dataProperty.asOWLDataProperty()))
				.contains(getTranslator().translate(object));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean hasObjectPropertyRelationship(
			org.semanticweb.owl.model.OWLIndividual subject,
			org.semanticweb.owl.model.OWLObjectPropertyExpression property,
			org.semanticweb.owl.model.OWLIndividual object)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("hasObjectPropertyRelationship(" + subject + ", "
				+ property + ", " + object + ")");
		boolean ret = getOWLReasoner().getObjectPropertyValues(
				getTranslator().translate(subject),
				getTranslator().translate(property)).containsEntity(
				getTranslator().translate(object));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean hasType(org.semanticweb.owl.model.OWLIndividual individual,
			org.semanticweb.owl.model.OWLDescription type, boolean direct)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("hasType(" + individual + ", " + type + ", " + direct
				+ ")");
		boolean ret = flatten(getTypes(individual, direct)).contains(type);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isAntiSymmetric(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isAntiSymmetric(" + property + ")");
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getAsymmetricObjectPropertyAxioms(
						getTranslator().translate(property)).isEmpty());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isClassified()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isClassified()");
		boolean ret = this.isClassified;
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isConsistent(org.semanticweb.owl.model.OWLOntology ontology)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isConsistent(" + ontology + ")");
		boolean ret = getOWLReasoner().isConsistent();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isDefined(org.semanticweb.owl.model.OWLClass cls)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isDefined((OWLClass) " + cls + ")");
		boolean ret = getOWLReasoner().getRootOntology()
				.containsClassInSignature(
						getTranslator().translate(cls).getIRI());
		logger.finer("" + ret);
		return ret;

	}

	@Override
	public boolean isDefined(org.semanticweb.owl.model.OWLDataProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isDefined((OWLDataProperty) " + property + ")");
		boolean ret = getOWLReasoner().getRootOntology()
				.containsDataPropertyInSignature(
						getTranslator().translate(property).getIRI());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isDefined(org.semanticweb.owl.model.OWLIndividual individual)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isDefined((OWLIndividual) " + individual + ")");
		boolean ret = getOWLReasoner().getRootOntology()
				.containsIndividualInSignature(
						getTranslator().translate(individual).getIRI());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isDefined(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isDefined((OWLObjectProperty) " + property + ")");
		boolean ret = getOWLReasoner().getRootOntology()
				.containsObjectPropertyInSignature(
						getTranslator().translate(property).getIRI());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isEquivalentClass(
			org.semanticweb.owl.model.OWLDescription description0,
			org.semanticweb.owl.model.OWLDescription description1)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isEquivalentClass(" + description0 + ", " + description1
				+ ")");
		boolean ret = getOWLReasoner().getEquivalentClasses(
				getTranslator().translate(description0.asOWLClass())).contains(
				getTranslator().translate(description1.asOWLClass()));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isFunctional(
			org.semanticweb.owl.model.OWLDataProperty dataProperty)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isFunctional((OWLDataProperty) " + dataProperty + ")");
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getFunctionalDataPropertyAxioms(
						getTranslator().translate(dataProperty)).isEmpty());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isFunctional(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isFunctional((OWLObjectProperty) " + property + ")");
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getFunctionalObjectPropertyAxioms(
						getTranslator().translate(property)).isEmpty());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isInverseFunctional(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isInverseFunctional(" + property + ")");
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getInverseFunctionalObjectPropertyAxioms(
						getTranslator().translate(property)).isEmpty());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isIrreflexive(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isIrreflexive(" + property + ")");
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getIrreflexiveObjectPropertyAxioms(
						getTranslator().translate(property)).isEmpty());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isRealised()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isRealised()");
		throw new TranslationException("Unsupported translation : isRealised()");
	}

	@Override
	public boolean isReflexive(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isReflexive(" + property + ")");
		OWLObjectProperty translatedProperty = getTranslator().translate(
				property);
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getReflexiveObjectPropertyAxioms(translatedProperty).isEmpty());
		if (!ret) {
			Set<OWLSubPropertyChainOfAxiom> axiomSet = getOWLReasoner()
					.getRootOntology().getAxioms(
							AxiomType.SUB_PROPERTY_CHAIN_OF);
			for (OWLSubPropertyChainOfAxiom axiom : axiomSet) {
				if (translatedProperty.equals(axiom.getSuperProperty())
						&& axiom.getPropertyChain().isEmpty()) {
					ret = true;
				}
			}
		}
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isSatisfiable(
			org.semanticweb.owl.model.OWLDescription description)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isSatisfiable(" + description + ")");
		boolean ret = getOWLReasoner().isSatisfiable(
				getTranslator().translate(description));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isSubClassOf(
			org.semanticweb.owl.model.OWLDescription description0,
			org.semanticweb.owl.model.OWLDescription description1)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger
				.finer("isSubClassOf(" + description0 + ", " + description1
						+ ")");
		Set<OWLClass> subsumers = new HashSet<OWLClass>();
		subsumers.addAll(getOWLReasoner().getSuperClasses(
				getTranslator().translate(description0), false).getFlattened());
		subsumers.addAll(getOWLReasoner().getEquivalentClasses(
				getTranslator().translate(description0)).getEntities());
		boolean ret = subsumers.contains((getTranslator()
				.translate(description1.asOWLClass())));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isSymmetric(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isSymmetric(" + property + ")");
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getSymmetricObjectPropertyAxioms(
						getTranslator().translate(property)).isEmpty());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isTransitive(
			org.semanticweb.owl.model.OWLObjectProperty property)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("isTransitive(" + property + ")");
		OWLObjectProperty translatedProperty = getTranslator().translate(
				property);
		boolean ret = !(getOWLReasoner().getRootOntology()
				.getTransitiveObjectPropertyAxioms(translatedProperty)
				.isEmpty());
		if (!ret) {
			Set<OWLSubPropertyChainOfAxiom> axiomSet = getOWLReasoner()
					.getRootOntology().getAxioms(
							AxiomType.SUB_PROPERTY_CHAIN_OF);
			for (OWLSubPropertyChainOfAxiom axiom : axiomSet) {
				if (translatedProperty.equals(axiom.getSuperProperty())) {
					List<OWLObjectPropertyExpression> propertyChain = axiom
							.getPropertyChain();
					if ((propertyChain.size() == 2)
							&& translatedProperty.equals(propertyChain.get(0))
							&& translatedProperty.equals(propertyChain.get(1))) {
						ret = true;
					}
				}
			}
		}
		logger.finer("" + ret);
		return ret;
	}

	public void loadAxiomSet(
			Set<org.semanticweb.owl.model.OWLAxiom> originalAxiomSet)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		OWLOntologyManager newOntologyManager = OWLManager
				.createOWLOntologyManager();
		this.translator = new SimpleTranslator(newOntologyManager
				.getOWLDataFactory());
		Set<OWLAxiom> axiomSet = getTranslator().translateToAxioms(
				getTranslator().processImportsDeclarations(
						getOWLOntologyManager(), originalAxiomSet));
		try {
			this.reasoner = createReasoner(newOntologyManager
					.createOntology(axiomSet));
		} catch (OWLOntologyCreationException e) {
			throw new TranslationException(e);
		}
	}

	@Override
	public void loadOntologies(
			Set<org.semanticweb.owl.model.OWLOntology> ontologySet)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("loadOntologies(" + ontologySet + ")");
		this.isClassified = false;
		this.ontologies = ontologySet;
		if (ontologySet != null && !ontologySet.isEmpty()) {
			loadAxiomSet(ontologySet.iterator().next().getAxioms());
		}
	}

	@Override
	public void realise()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("realise()");
		getOWLReasoner().flush();
	}

	protected void setOWLOntologyManager(
			org.semanticweb.owl.model.OWLOntologyManager manager) {
		this.isClassified = false;
		this.ontologyManager = manager;
		this.invTranslator = new SimpleInvTranslator(manager
				.getOWLDataFactory());
	}

	@Override
	public void unloadOntologies(
			Set<org.semanticweb.owl.model.OWLOntology> ontologySet)
			throws org.semanticweb.owl.inference.OWLReasonerException {
		logger.finer("unloadOntologies(" + ontologySet + ")");
		throw new TranslationException(
				"Unsupported translation : unloadOntologies(Set<org.semanticweb.owl.model.OWLOntology>)");
	}
}
