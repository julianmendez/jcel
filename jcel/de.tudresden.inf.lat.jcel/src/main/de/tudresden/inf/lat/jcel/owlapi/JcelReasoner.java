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

package de.tudresden.inf.lat.jcel.owlapi;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owl.inference.MonitorableOWLReasoner;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.ProgressMonitor;

/**
 * This class is the connection with the OWL API. It implements some functions,
 * and throws an exception for the unimplemented ones.
 * 
 * @author Julian Mendez
 */
public class JcelReasoner implements OWLReasoner, MonitorableOWLReasoner {

	private static final Logger logger = Logger.getLogger(JcelReasoner.class
			.getName());

	private JcelReasonerProcessor reasonerProcessor = null;

	private Date start = new Date();

	public JcelReasoner(OWLOntologyManager manager) {
		this.reasonerProcessor = new JcelReasonerProcessor(manager);
	}

	public JcelReasoner(OWLOntologyManager manager, ProgressMonitor monitor) {
		this.reasonerProcessor = new JcelReasonerProcessor(manager, monitor);
	}

	@Override
	public void classify() throws OWLReasonerException {
		logger.finer("(classify)");
		getProcessor().classify();
	}

	@Override
	public void clearOntologies() throws OWLReasonerException {
		logger.finer("(clearOntologies)");
		getProcessor().clearOntologies();
	}

	@Override
	public void dispose() throws OWLReasonerException {
		logger.finer("(dispose)");
		getProcessor().dispose();
	}

	@Override
	public Set<Set<OWLClass>> getAncestorClasses(OWLDescription description)
			throws OWLReasonerException {
		logger.finer("(getAncestorClasses) " + description);
		return getProcessor().getAncestorClasses(description);
	}

	@Override
	public Set<Set<OWLDataProperty>> getAncestorProperties(
			OWLDataProperty dataProperty) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<Set<OWLObjectProperty>> getAncestorProperties(
			OWLObjectProperty property) throws OWLReasonerException {
		logger.finer("(getAncestorProperties) " + property.getURI());
		return getProcessor().getAncestorProperties(property);
	}

	@Override
	public OWLEntity getCurrentEntity() {
		return null;
	}

	@Override
	public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(
			OWLIndividual individual) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<Set<OWLClass>> getDescendantClasses(OWLDescription description)
			throws OWLReasonerException {
		logger.finer("(getDescendantClasses) " + description.toString());
		return getProcessor().getDescendantClasses(description);
	}

	@Override
	public Set<Set<OWLDataProperty>> getDescendantProperties(
			OWLDataProperty dataProperty) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<Set<OWLObjectProperty>> getDescendantProperties(
			OWLObjectProperty property) throws OWLReasonerException {
		logger.finer("(getDescendantProperties)" + property.getURI());
		return getProcessor().getDescendantProperties(property);
	}

	@Override
	public Set<Set<OWLDescription>> getDomains(OWLDataProperty dataProperty)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<Set<OWLDescription>> getDomains(OWLObjectProperty property)
			throws OWLReasonerException {
		logger.finer("(getDomains) " + property.getURI());
		return getProcessor().getDomains(property);
	}

	@Override
	public Set<OWLClass> getEquivalentClasses(OWLDescription description)
			throws OWLReasonerException {
		logger.finer("(getEquivalentClasses) " + description);
		return getProcessor().getEquivalentClasses(description);
	}

	@Override
	public Set<OWLDataProperty> getEquivalentProperties(
			OWLDataProperty dataProperty) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<OWLObjectProperty> getEquivalentProperties(
			OWLObjectProperty property) throws OWLReasonerException {
		logger.finer("(getEquivalentProperties) " + property.getURI());
		return getProcessor().getEquivalentProperties(property);
	}

	@Override
	public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
		logger.finer("(getInconsistentClasses)");
		return getProcessor().getInconsistentClasses();
	}

	@Override
	public Set<OWLIndividual> getIndividuals(OWLDescription description,
			boolean arg1) throws OWLReasonerException {
		logger.finer("(getIndividuals) " + description);
		return getProcessor().getIndividuals(description, arg1);
	}

	@Override
	public Set<Set<OWLObjectProperty>> getInverseProperties(
			OWLObjectProperty property) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<OWLOntology> getLoadedOntologies() {
		logger.finer("(getLoadedOntologies)");
		return getProcessor().getLoadedOntologies();
	}

	@Override
	public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(
			OWLIndividual individual) throws OWLReasonerException {
		logger.finer("(getObjectPropertyRelationships) " + individual.getURI());
		return getProcessor().getObjectPropertyRelationships(individual);
	}

	public JcelReasonerProcessor getProcessor() {
		return this.reasonerProcessor;
	}

	public ProgressMonitor getProgressMonitor() {
		return getProcessor().getProgressMonitor();
	}

	@Override
	public Set<OWLDataRange> getRanges(OWLDataProperty dataProperty)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<OWLDescription> getRanges(OWLObjectProperty property)
			throws OWLReasonerException {
		logger.finer("(getRanges)");
		return getProcessor().getRanges(property);
	}

	@Override
	public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual individual,
			OWLObjectPropertyExpression propertyExpression)
			throws OWLReasonerException {
		logger.finer("(getRelatedIndividuals) " + individual.getURI() + " "
				+ propertyExpression);
		return getProcessor().getRelatedIndividuals(individual,
				propertyExpression);
	}

	@Override
	public Set<OWLConstant> getRelatedValues(OWLIndividual individual,
			OWLDataPropertyExpression dataProperty) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	public Date getStartTime() {
		return this.start;
	}

	@Override
	public Set<Set<OWLClass>> getSubClasses(OWLDescription description)
			throws OWLReasonerException {
		logger.finer("(getSubClasses) " + description);
		return getProcessor().getSubClasses(description);
	}

	@Override
	public Set<Set<OWLDataProperty>> getSubProperties(
			OWLDataProperty dataProperty) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<Set<OWLObjectProperty>> getSubProperties(
			OWLObjectProperty property) throws OWLReasonerException {
		logger.finer("(getSubProperties) " + property.getURI());
		return getProcessor().getSubProperties(property);
	}

	@Override
	public Set<Set<OWLClass>> getSuperClasses(OWLDescription description)
			throws OWLReasonerException {
		logger.finer("(getSuperClasses) " + description);
		return getProcessor().getSuperClasses(description);
	}

	@Override
	public Set<Set<OWLDataProperty>> getSuperProperties(
			OWLDataProperty dataProperty) throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public Set<Set<OWLObjectProperty>> getSuperProperties(
			OWLObjectProperty property) throws OWLReasonerException {
		logger.finer("(getSuperProperties) " + property.getURI());
		return getProcessor().getSuperProperties(property);
	}

	@Override
	public Set<Set<OWLClass>> getTypes(OWLIndividual individual, boolean direct)
			throws OWLReasonerException {
		logger.finer("(getTypes)");
		return getProcessor().getTypes(individual, direct);
	}

	@Override
	public boolean hasDataPropertyRelationship(OWLIndividual subject,
			OWLDataPropertyExpression property, OWLConstant object)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean hasObjectPropertyRelationship(OWLIndividual subject,
			OWLObjectPropertyExpression property, OWLIndividual object)
			throws OWLReasonerException {
		logger.finer("(hasObjectPropertyRelationship) " + subject.getURI()
				+ " " + property + " " + object.getURI());
		return getProcessor().hasObjectPropertyRelationship(subject, property,
				object);
	}

	@Override
	public boolean hasType(OWLIndividual individual, OWLDescription type,
			boolean direct) throws OWLReasonerException {
		logger.finer("(hasType) " + individual.getURI() + " " + type);
		return getProcessor().hasType(individual, type, direct);
	}

	@Override
	public boolean isAntiSymmetric(OWLObjectProperty property)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isClassified() throws OWLReasonerException {
		logger.finer("(isClassified)");
		return getProcessor().isClassified();
	}

	@Override
	public boolean isConsistent(OWLOntology ontology)
			throws OWLReasonerException {
		logger.finer("(isConsistent) " + ontology.getURI());
		return getProcessor().isConsistent(ontology);
	}

	@Override
	public boolean isDefined(OWLClass cls) throws OWLReasonerException {
		logger.finer("(isDefined) OWLClass " + cls.getURI());
		return getProcessor().isDefined(cls);
	}

	@Override
	public boolean isDefined(OWLDataProperty property)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isDefined(OWLIndividual individual)
			throws OWLReasonerException {
		logger.finer("(isDefined) OWLIndividual " + individual.getURI());
		return getProcessor().isDefined(individual);
	}

	@Override
	public boolean isDefined(OWLObjectProperty property)
			throws OWLReasonerException {
		logger.finer("(isDefined) OWLObjectProperty " + property.getURI());
		return getProcessor().isDefined(property);
	}

	@Override
	public boolean isEquivalentClass(OWLDescription description0,
			OWLDescription description1) throws OWLReasonerException {
		logger
				.finer("(isEquivalentClass) " + description0 + " "
						+ description1);
		return getProcessor().isEquivalentClass(description0, description1);
	}

	@Override
	public boolean isFunctional(OWLDataProperty dataProperty)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isFunctional(OWLObjectProperty property)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isInverseFunctional(OWLObjectProperty property)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isIrreflexive(OWLObjectProperty property)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isRealised() throws OWLReasonerException {
		logger.finer("(isRealised)");
		return getProcessor().isRealised();
	}

	@Override
	public boolean isReflexive(OWLObjectProperty property)
			throws OWLReasonerException {
		logger.finer("(isReflexive) " + property.getURI());
		return getProcessor().isReflexive(property);
	}

	@Override
	public boolean isSatisfiable(OWLDescription description)
			throws OWLReasonerException {
		logger.finer("(isSatisfiable) " + description);
		return getProcessor().isSatisfiable(description);
	}

	@Override
	public boolean isSubClassOf(OWLDescription description0,
			OWLDescription description1) throws OWLReasonerException {
		logger.finer("(isSubClassOf) " + description0 + " " + description1);
		return getProcessor().isSubClassOf(description0, description1);
	}

	@Override
	public boolean isSymmetric(OWLObjectProperty property)
			throws OWLReasonerException {
		throw new UnsupportedReasonerOperationInJcelException();
	}

	@Override
	public boolean isTransitive(OWLObjectProperty property)
			throws OWLReasonerException {
		logger.finer("(isTransitive) " + property.getURI());
		return getProcessor().isTransitive(property);
	}

	@Override
	public void loadOntologies(Set<OWLOntology> ontologySet)
			throws OWLReasonerException {
		logger.finer("(loadOntologies)");
		getProcessor().loadOntologies(ontologySet);
	}

	@Override
	public void realise() throws OWLReasonerException {
		logger.finer("(realise)");
		getProcessor().realise();
	}

	@Override
	public void setProgressMonitor(ProgressMonitor monitor) {
		getProcessor().setProgressMonitor(monitor);
	}

	@Override
	public void unloadOntologies(Set<OWLOntology> ontologySet)
			throws OWLReasonerException {
		logger.finer("(unloadOntologies)");
		getProcessor().unloadOntologies(ontologySet);
	}
}
