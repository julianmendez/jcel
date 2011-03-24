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

package de.tudresden.inf.lat.jcel.owlapi.main;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.reasoner.AxiomNotInProfileException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ClassExpressionNotInProfileException;
import org.semanticweb.owlapi.reasoner.FreshEntitiesException;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.UnsupportedEntailmentTypeException;
import org.semanticweb.owlapi.util.ProgressMonitor;
import org.semanticweb.owlapi.util.Version;

/**
 * This class is the connection with the OWL API. It implements some functions,
 * and throws an exception for the unimplemented ones.
 * 
 * @author Julian Mendez
 */
public class JcelReasoner implements OWLReasoner {

	private static final Logger logger = Logger.getLogger(JcelReasoner.class
			.getName());

	private JcelReasonerProcessor reasonerProcessor = null;

	private Date start = new Date();

	public JcelReasoner(OWLOntology rootOntology) {
		this.reasonerProcessor = new JcelReasonerProcessor(rootOntology);
	}

	public JcelReasoner(OWLOntology rootOntology, ProgressMonitor monitor) {
		this.reasonerProcessor = new JcelReasonerProcessor(rootOntology,
				monitor);
	}

	@Override
	public void dispose() {
		logger.finer("dispose()");
		getProcessor().dispose();
	}

	@Override
	public void flush() {
		logger.finer("flush()");
		// does nothing
	}

	@Override
	public Node<OWLClass> getBottomClassNode() {
		logger.finer("getBottomClassNode()");
		Node<OWLClass> ret = getProcessor().getBottomClassNode();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLDataProperty> getBottomDataPropertyNode() {
		logger.finer("getBottomDataPropertyNode()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getBottomDataPropertyNode()");
	}

	@Override
	public Node<OWLObjectProperty> getBottomObjectPropertyNode() {
		logger.finer("getBottomObjectPropertyNode()");
		Node<OWLObjectProperty> ret = getProcessor()
				.getBottomObjectPropertyNode();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public BufferingMode getBufferingMode() {
		logger.finer("getBufferingMode()");
		BufferingMode ret = BufferingMode.NON_BUFFERING;
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty property,
			boolean direct) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		logger
				.finer("getDataPropertyDomains(" + property + ", " + direct
						+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getDataPropertyDomains(OWLDataProperty, boolean)");
	}

	@Override
	public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual individual,
			OWLDataProperty property) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		logger.finer("getDataPropertyValues(" + individual + ", " + property
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getDataPropertyValues(OWLNamedIndividual, OWLDataProperty)");
	}

	@Override
	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(
			OWLNamedIndividual individual)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getDifferentIndividuals(" + individual + ")");
		NodeSet<OWLNamedIndividual> ret = getProcessor()
				.getDifferentIndividuals(individual);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression clsExpr,
			boolean direct) {
		logger.finer("getDisjointClasses(" + clsExpr + ", " + direct + ")");
		NodeSet<OWLClass> ret = getProcessor().getDisjointClasses(clsExpr,
				direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLDataProperty> getDisjointDataProperties(
			OWLDataPropertyExpression property, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getDisjointDataProperties(" + property + ", " + direct
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getDisjointDataProperties(OWLDataPropertyExpression, boolean)");
	}

	@Override
	public NodeSet<OWLObjectProperty> getDisjointObjectProperties(
			OWLObjectPropertyExpression propExpr, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getDisjointObjectProperties(" + propExpr + ", " + direct
				+ ")");
		NodeSet<OWLObjectProperty> ret = getProcessor()
				.getDisjointObjectProperties(propExpr, direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLClass> getEquivalentClasses(
			OWLClassExpression classExpression) {
		logger.finer("getEquivalentClasses(" + classExpression + ")");
		Node<OWLClass> ret = getProcessor().getEquivalentClasses(
				classExpression);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLDataProperty> getEquivalentDataProperties(
			OWLDataProperty property) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		logger.finer("getEquivalentDataProperties(" + property + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getEquivalentDataProperties(OWLDataProperty)");
	}

	@Override
	public Node<OWLObjectProperty> getEquivalentObjectProperties(
			OWLObjectPropertyExpression property)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getEquivalentObjectProperties(" + property + ")");
		Node<OWLObjectProperty> ret = getProcessor()
				.getEquivalentObjectProperties(property);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public FreshEntityPolicy getFreshEntityPolicy() {
		logger.finer("getFreshEntityPolicy()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getFreshEntityPolicy()");
	}

	@Override
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		logger.finer("getIndividualNodeSetPolicy()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getIndividualNodeSetPolicy()");
	}

	@Override
	public NodeSet<OWLNamedIndividual> getInstances(
			OWLClassExpression classExpression, boolean direct)
			throws InconsistentOntologyException,
			ClassExpressionNotInProfileException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getInstances(" + classExpression + ", " + direct + ")");
		NodeSet<OWLNamedIndividual> ret = getProcessor().getInstances(
				classExpression, direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLObjectProperty> getInverseObjectProperties(
			OWLObjectPropertyExpression propertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getInverseObjectProperties(" + propertyExpression + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getInverseObjectProperties(OWLObjectPropertyExpression)");
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyDomains(
			OWLObjectPropertyExpression propertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getObjectPropertyDomains(" + propertyExpression + ", "
				+ direct + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getObjectPropertyDomains(OWLObjectPropertyExpression, boolean)");
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyRanges(
			OWLObjectPropertyExpression propertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getObjectPropertyRanges(" + propertyExpression + ", "
				+ direct + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getObjectPropertyRanges(OWLObjectPropertyExpression, boolean)");
	}

	@Override
	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(
			OWLNamedIndividual individual,
			OWLObjectPropertyExpression propertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getObjectPropertyValues(" + individual + ", "
				+ propertyExpression + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getObjectPropertyValues(OWLNamedIndividual, OWLObjectPropertyExpression)");
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomAdditions() {
		logger.finer("getPendingAxiomAdditions()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getPendingAxiomAdditions()");
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomRemovals() {
		logger.finer("getPendingAxiomRemovals()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getPendingAxiomRemovals()");
	}

	@Override
	public List<OWLOntologyChange> getPendingChanges() {
		logger.finer("getPendingChanges()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getPendingChanges()");
	}

	public JcelReasonerProcessor getProcessor() {
		return this.reasonerProcessor;
	}

	public ProgressMonitor getProgressMonitor() {
		return getProcessor().getProgressMonitor();
	}

	@Override
	public String getReasonerName() {
		logger.finer("getReasonerName()");
		String ret = getProcessor().getReasonerName();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Version getReasonerVersion() {
		logger.finer("getReasonerVersion()");
		Version ret = getProcessor().getReasonerVersion();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public OWLOntology getRootOntology() {
		logger.finer("getRootOntology()");
		OWLOntology ret = getProcessor().getRootOntology();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLNamedIndividual> getSameIndividuals(
			OWLNamedIndividual individual)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getSameIndividuals(" + individual + ")");
		Node<OWLNamedIndividual> ret = getProcessor().getSameIndividuals(
				individual);
		logger.finer("" + ret);
		return ret;
	}

	public Date getStartTime() {
		return this.start;
	}

	@Override
	public NodeSet<OWLClass> getSubClasses(OWLClassExpression classExpression,
			boolean direct) {
		logger.finer("getSubClasses(" + classExpression + ", " + direct + ")");
		NodeSet<OWLClass> ret = getProcessor().getSubClasses(classExpression,
				direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLDataProperty> getSubDataProperties(
			OWLDataProperty property, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getSubDataProperties(" + property + ", " + direct + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getSubDataProperties(OWLDataProperty, boolean)");
	}

	@Override
	public NodeSet<OWLObjectProperty> getSubObjectProperties(
			OWLObjectPropertyExpression propertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getSubObjectProperties(" + propertyExpression + ", "
				+ direct + ")");
		NodeSet<OWLObjectProperty> ret = getProcessor().getSubProperties(
				propertyExpression, direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getSuperClasses(
			OWLClassExpression classExpression, boolean direct)
			throws InconsistentOntologyException,
			ClassExpressionNotInProfileException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger
				.finer("getSuperClasses(" + classExpression + ", " + direct
						+ ")");
		NodeSet<OWLClass> ret = getProcessor().getSuperClasses(classExpression,
				direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLDataProperty> getSuperDataProperties(
			OWLDataProperty propertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getSuperDataProperties(" + propertyExpression + ", "
				+ direct + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getSuperDataProperties(OWLDataProperty, boolean)");
	}

	@Override
	public NodeSet<OWLObjectProperty> getSuperObjectProperties(
			OWLObjectPropertyExpression propertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		logger.finer("getSuperObjectProperties(" + propertyExpression + ", "
				+ direct + ")");
		NodeSet<OWLObjectProperty> ret = getProcessor().getSuperProperties(
				propertyExpression, direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public long getTimeOut() {
		logger.finer("getTimeOut()");
		long ret = getProcessor().getTimeOut();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLClass> getTopClassNode() {
		logger.finer("getTopClassNode()");
		Node<OWLClass> ret = getProcessor().getTopClassNode();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLDataProperty> getTopDataPropertyNode() {
		logger.finer("getObjectPropertyRanges()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getTopDataPropertyNode()");
	}

	@Override
	public Node<OWLObjectProperty> getTopObjectPropertyNode() {
		logger.finer("getTopObjectPropertyNode()");
		Node<OWLObjectProperty> ret = getProcessor().getTopObjectPropertyNode();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getTypes(OWLNamedIndividual individual,
			boolean direct) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		logger.finer("getTypes(" + individual + ", " + direct + ")");
		NodeSet<OWLClass> ret = getProcessor().getTypes(individual, direct);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLClass> getUnsatisfiableClasses()
			throws ReasonerInterruptedException, TimeOutException {
		logger.finer("getUnsatisfiableClasses()");
		Node<OWLClass> ret = getProcessor().getUnsatisfiableClasses();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public void interrupt() {
		logger.finer("interrupt()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : interrupt()");
	}

	@Override
	public boolean isConsistent() throws ReasonerInterruptedException,
			TimeOutException {
		logger.finer("isConsistent()");
		boolean ret = getProcessor().isConsistent();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isEntailed(OWLAxiom axiom)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {
		logger.finer("isEntailed((OWLAxiom) " + axiom + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : isEntailed(OWLAxiom)");
	}

	@Override
	public boolean isEntailed(Set<? extends OWLAxiom> axiom)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {
		logger.finer("isEntailed((Set<? extends OWLAxiom>) " + axiom + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : isEntailed(Set<? extends OWLAxiom>)");
	}

	@Override
	public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
		logger.finer("isEntailmentCheckingSupported(" + axiomType + ")");
		boolean ret = getProcessor().isEntailmentCheckingSupported(axiomType);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isSatisfiable(OWLClassExpression description) {
		logger.finer("isSatisfiable(" + description + ")");
		boolean ret = getProcessor().isSatisfiable(description);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public void prepareReasoner() throws ReasonerInterruptedException,
			TimeOutException {
		logger.finer("prepareReasoner()");
		getProcessor().prepareReasoner();
	}
}
