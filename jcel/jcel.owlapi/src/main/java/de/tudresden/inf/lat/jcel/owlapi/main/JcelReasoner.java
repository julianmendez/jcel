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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyChangeVisitorEx;
import org.semanticweb.owlapi.reasoner.AxiomNotInProfileException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ClassExpressionNotInProfileException;
import org.semanticweb.owlapi.reasoner.FreshEntitiesException;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.UnsupportedEntailmentTypeException;
import org.semanticweb.owlapi.util.Version;

import de.tudresden.inf.lat.jcel.core.reasoner.IntegerReasoner;
import de.tudresden.inf.lat.jcel.core.reasoner.RuleBasedReasoner;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.owlapi.translator.Translator;

/**
 * This class is the connection with the OWL API. It implements some functions,
 * and throws an exception for the unimplemented ones.
 * 
 * @author Julian Mendez
 */
public class JcelReasoner implements OWLReasoner, OWLOntologyChangeListener {

	private static final Logger logger = Logger.getLogger(JcelReasoner.class
			.getName());

	private final RuleBasedReasoner jcelCore;
	private final OWLOntologyChangeVisitorEx<Boolean> ontologyChangeVisitor = new JcelOntologyChangeVisitorEx(
			this);
	private Set<OWLAxiom> pendingAxiomAdditions = new HashSet<OWLAxiom>();
	private Set<OWLAxiom> pendingAxiomRemovals = new HashSet<OWLAxiom>();
	private List<OWLOntologyChange> pendingChanges = new ArrayList<OWLOntologyChange>();
	private final OWLReasonerConfiguration reasonerConfiguration;
	private final OWLOntology rootOntology;
	private final Date start = new Date();
	private final Set<AxiomType<?>> supportedAxiomTypes;
	private final Translator translator;

	/**
	 * Constructs a new jcel reasoner.
	 * 
	 * @param rootOntology
	 *            root ontology
	 * @param buffering
	 *            <code>true</code> if and only if the reasoner is buffering
	 */
	public JcelReasoner(OWLOntology rootOntology, boolean buffering) {
		if (rootOntology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.rootOntology = rootOntology;
		this.translator = new Translator(rootOntology,
				new IntegerOntologyObjectFactoryImpl());
		this.jcelCore = new RuleBasedReasoner(this.translator.getOntology(),
				this.translator.getOntologyObjectFactory(), buffering);
		this.rootOntology.getOWLOntologyManager().addOntologyChangeListener(
				this);
		this.supportedAxiomTypes = getSupportedTypes();
		this.reasonerConfiguration = null;
	}

	/**
	 * Constructs a new jcel reasoner.
	 * 
	 * @param rootOntology
	 *            root ontology
	 * @param buffering
	 *            <code>true</code> if and only if the reasoner is buffering
	 * @param configuration
	 *            reasoner configuration
	 */
	public JcelReasoner(OWLOntology rootOntology, boolean buffering,
			OWLReasonerConfiguration configuration) {
		if (rootOntology == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (configuration == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.rootOntology = rootOntology;
		this.translator = new Translator(rootOntology,
				new IntegerOntologyObjectFactoryImpl());
		this.jcelCore = new RuleBasedReasoner(this.translator.getOntology(),
				this.translator.getOntologyObjectFactory(), buffering);
		this.reasonerConfiguration = configuration;
		this.rootOntology.getOWLOntologyManager().addOntologyChangeListener(
				this);
		this.supportedAxiomTypes = getSupportedTypes();
	}

	public boolean addAxiom(OWLAxiom axiom) {
		boolean ret = this.pendingAxiomAdditions.add(axiom);
		getTranslator().getTranslationRepository().addAxiom(axiom);
		Set<ComplexIntegerAxiom> axioms = getTranslator().translateSA(
				Collections.singleton(axiom));
		for (ComplexIntegerAxiom ax : axioms) {
			boolean changed = this.jcelCore.addAxiom(ax);
			ret = ret || changed;
		}
		return ret;
	}

	@Override
	public void dispose() {
		logger.finer("dispose()");
		getReasoner().dispose();
	}

	@Override
	public void flush() {
		logger.finer("flush()");
		getReasoner().flush();
		this.pendingAxiomAdditions.clear();
		this.pendingAxiomRemovals.clear();
		this.pendingChanges.clear();
	}

	@Override
	public Node<OWLClass> getBottomClassNode() {
		logger.finer("getBottomClassNode()");
		Node<OWLClass> ret = getTranslator().translateSC(
				getReasoner().getBottomClassNode());
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
	public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
		logger.finer("getBottomObjectPropertyNode()");
		Node<OWLObjectPropertyExpression> ret = getTranslator().translateSOPE(
				getReasoner().getBottomObjectPropertyNode());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public BufferingMode getBufferingMode() {
		logger.finer("getBufferingMode()");

		BufferingMode ret = getReasoner().getBufferingMode() ? BufferingMode.BUFFERING
				: BufferingMode.NON_BUFFERING;
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getDataPropertyDomains(
			OWLDataProperty dataProperty, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getDataPropertyDomains(" + dataProperty + ", " + direct
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getDataPropertyDomains(OWLDataProperty, boolean)");
	}

	@Override
	public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual individual,
			OWLDataProperty dataProperty) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getDataPropertyValues(" + individual + ", "
				+ dataProperty + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getDataPropertyValues(OWLNamedIndividual, OWLDataProperty)");
	}

	@Override
	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(
			OWLNamedIndividual individual)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getDifferentIndividuals(" + individual + ")");
		NodeSet<OWLNamedIndividual> ret = getTranslator().translateSSI(
				getReasoner().getDifferentIndividuals(
						getTranslator().translateI(individual)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getDisjointClasses(
			OWLClassExpression classExpression)
			throws ReasonerInterruptedException, TimeOutException,
			FreshEntitiesException, InconsistentOntologyException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getDisjointClasses(" + classExpression + ")");
		NodeSet<OWLClass> ret = getTranslator().translateSSC(
				getReasoner().getDisjointClasses(
						getTranslator().translateCE(classExpression)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLDataProperty> getDisjointDataProperties(
			OWLDataPropertyExpression dataPropertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (dataPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getDisjointDataProperties(" + dataPropertyExpression
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getDisjointDataProperties(OWLDataPropertyExpression)");
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(
			OWLObjectPropertyExpression objectPropertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getDisjointDataProperties(" + objectPropertyExpression
				+ ")");
		NodeSet<OWLObjectPropertyExpression> ret = getTranslator()
				.translateSSOPE(
						getReasoner().getDisjointObjectProperties(
								getTranslator().translateOPE(
										objectPropertyExpression)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLClass> getEquivalentClasses(
			OWLClassExpression classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getEquivalentClasses(" + classExpression + ")");
		Node<OWLClass> ret = getTranslator().translateSC(
				getReasoner().getEquivalentClasses(
						getTranslator().translateCE(classExpression)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLDataProperty> getEquivalentDataProperties(
			OWLDataProperty dataProperty) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getEquivalentDataProperties(" + dataProperty + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getEquivalentDataProperties(OWLDataProperty)");
	}

	@Override
	public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(
			OWLObjectPropertyExpression objectPropertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getEquivalentObjectProperties("
				+ objectPropertyExpression + ")");
		Node<OWLObjectPropertyExpression> ret = getTranslator()
				.translateSOPE(
						getReasoner().getEquivalentObjectProperties(
								getTranslator().translateOPE(
										objectPropertyExpression)));
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
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getInstances(" + classExpression + ", " + direct + ")");
		NodeSet<OWLNamedIndividual> ret = getTranslator().translateSSI(
				getReasoner().getInstances(
						getTranslator().translateCE(classExpression), direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getInverseObjectProperties(
			OWLObjectPropertyExpression objectPropertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getInverseObjectProperties(" + objectPropertyExpression
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getInverseObjectProperties(OWLObjectPropertyExpression)");
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyDomains(
			OWLObjectPropertyExpression objectPropertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getObjectPropertyDomains(" + objectPropertyExpression
				+ ", " + direct + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getObjectPropertyDomains(OWLObjectPropertyExpression, boolean)");
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyRanges(
			OWLObjectPropertyExpression objectPropertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getObjectPropertyRanges(" + objectPropertyExpression
				+ ", " + direct + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getObjectPropertyRanges(OWLObjectPropertyExpression, boolean)");
	}

	@Override
	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(
			OWLNamedIndividual individual,
			OWLObjectPropertyExpression objectPropertyExpression)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getObjectPropertyValues(" + individual + ", "
				+ objectPropertyExpression + ")");
		NodeSet<OWLNamedIndividual> ret = getTranslator()
				.translateSSI(
						getReasoner().getObjectPropertyValues(
								getTranslator().translateI(individual),
								getTranslator().translateOPE(
										objectPropertyExpression)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomAdditions() {
		logger.finer("getPendingAxiomAdditions()");
		Set<OWLAxiom> ret = this.pendingAxiomAdditions;
		logger.finer("" + ret);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomRemovals() {
		logger.finer("getPendingAxiomRemovals()");
		Set<OWLAxiom> ret = this.pendingAxiomRemovals;
		logger.finer("" + ret);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public List<OWLOntologyChange> getPendingChanges() {
		logger.finer("getPendingChanges()");
		List<OWLOntologyChange> ret = this.pendingChanges;
		logger.finer("" + ret);
		return Collections.unmodifiableList(ret);
	}

	@Override
	public Set<InferenceType> getPrecomputableInferenceTypes() {
		logger.finer("getPrecomputableInferenceTypes()");
		Set<InferenceType> ret = Collections.emptySet();
		logger.finer("" + ret);
		return ret;
	}

	public IntegerReasoner getReasoner() {
		return this.jcelCore;
	}

	public OWLReasonerConfiguration getReasonerConfiguration() {
		return this.reasonerConfiguration;
	}

	@Override
	public String getReasonerName() {
		logger.finer("getReasonerName()");
		String ret = getReasoner().getReasonerName();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Version getReasonerVersion() {
		logger.finer("getReasonerVersion()");
		Version ret = new Version(0, 0, 0, 0);
		String versionId = getReasoner().getReasonerVersion();
		if (versionId != null) {
			StringTokenizer stok = new StringTokenizer(versionId, ".");
			int major = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			int minor = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			int patch = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			int build = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			ret = new Version(major, minor, patch, build);
		}
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public OWLOntology getRootOntology() {
		logger.finer("getRootOntology()");
		OWLOntology ret = this.rootOntology;
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLNamedIndividual> getSameIndividuals(
			OWLNamedIndividual individual)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSameIndividuals(" + individual + ")");
		Node<OWLNamedIndividual> ret = getTranslator().translateSI(
				getReasoner().getSameIndividuals(
						getTranslator().translateI(individual)));
		logger.finer("" + ret);
		return ret;
	}

	public Date getStartTime() {
		return this.start;
	}

	@Override
	public NodeSet<OWLClass> getSubClasses(OWLClassExpression classExpression,
			boolean direct) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSubClasses(" + classExpression + ", " + direct + ")");
		NodeSet<OWLClass> ret = getTranslator().translateSSC(
				getReasoner().getSubClasses(
						getTranslator().translateCE(classExpression), direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLDataProperty> getSubDataProperties(
			OWLDataProperty dataProperty, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSubDataProperties(" + dataProperty + ", " + direct
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getSubDataProperties(OWLDataProperty, boolean)");
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(
			OWLObjectPropertyExpression objectPropertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSubObjectProperties(" + objectPropertyExpression
				+ ", " + direct + ")");
		NodeSet<OWLObjectPropertyExpression> ret = getTranslator()
				.translateSSOPE(
						getReasoner().getSubObjectProperties(
								getTranslator().translateOPE(
										objectPropertyExpression), direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLClass> getSuperClasses(
			OWLClassExpression classExpression, boolean direct)
			throws InconsistentOntologyException,
			ClassExpressionNotInProfileException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSuperClasses(" + classExpression + ", " + direct + ")");
		NodeSet<OWLClass> ret = getTranslator().translateSSC(
				getReasoner().getSuperClasses(
						getTranslator().translateCE(classExpression), direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public NodeSet<OWLDataProperty> getSuperDataProperties(
			OWLDataProperty dataProperty, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSuperDataProperties(" + dataProperty + ", " + direct
				+ ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getSuperDataProperties(OWLDataProperty, boolean)");
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(
			OWLObjectPropertyExpression objectPropertyExpression, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getSuperObjectProperties(" + objectPropertyExpression
				+ ", " + direct + ")");
		NodeSet<OWLObjectPropertyExpression> ret = getTranslator()
				.translateSSOPE(
						getReasoner().getSuperObjectProperties(
								getTranslator().translateOPE(
										objectPropertyExpression), direct));
		logger.finer("" + ret);
		return ret;
	}

	private Set<AxiomType<?>> getSupportedTypes() {
		Set<AxiomType<?>> ret = new HashSet<AxiomType<?>>();
		ret.add(AxiomType.EQUIVALENT_CLASSES);
		ret.add(AxiomType.SUBCLASS_OF);
		ret.add(AxiomType.SUB_OBJECT_PROPERTY);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public long getTimeOut() {
		logger.finer("getTimeOut()");
		long ret = getReasoner().getTimeOut();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLClass> getTopClassNode() {
		logger.finer("getTopClassNode()");
		Node<OWLClass> ret = getTranslator().translateSC(
				getReasoner().getTopClassNode());
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLDataProperty> getTopDataPropertyNode() {
		logger.finer("getTopDataPropertyNode()");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : getTopDataPropertyNode()");
	}

	@Override
	public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
		logger.finer("getTopObjectPropertyNode()");
		Node<OWLObjectPropertyExpression> ret = getTranslator().translateSOPE(
				getReasoner().getTopObjectPropertyNode());
		logger.finer("" + ret);
		return ret;
	}

	public Translator getTranslator() {
		return this.translator;
	}

	@Override
	public NodeSet<OWLClass> getTypes(OWLNamedIndividual individual,
			boolean direct) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("getTypes(" + individual + ", " + direct + ")");
		NodeSet<OWLClass> ret = getTranslator().translateSSC(
				getReasoner().getTypes(getTranslator().translateI(individual),
						direct));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public Node<OWLClass> getUnsatisfiableClasses()
			throws ReasonerInterruptedException, TimeOutException {
		logger.finer("getUnsatisfiableClasses()");
		Node<OWLClass> ret = getTranslator().translateSC(
				getReasoner().getUnsatisfiableClasses());
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
		boolean ret = getReasoner().isConsistent();
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isEntailed(OWLAxiom axiom)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("isEntailed((OWLAxiom) " + axiom + ")");
		boolean ret = getReasoner().isEntailed(
				getTranslator().translateSA(Collections.singleton(axiom)));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isEntailed(Set<? extends OWLAxiom> axiomSet)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("isEntailed((Set<? extends OWLAxiom>) " + axiomSet + ")");
		Set<OWLAxiom> set = new HashSet<OWLAxiom>();
		for (OWLAxiom axiom : axiomSet) {
			set.add(axiom);
		}
		boolean ret = getReasoner()
				.isEntailed(getTranslator().translateSA(set));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
		if (axiomType == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("isEntailmentCheckingSupported(" + axiomType + ")");
		boolean ret = this.supportedAxiomTypes.contains(axiomType);
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public boolean isPrecomputed(InferenceType inferenceType) {
		if (inferenceType == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("isPrecomputed(" + inferenceType + ")");
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : isPrecomputed()");
	}

	@Override
	public boolean isSatisfiable(OWLClassExpression classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("isSatisfiable(" + classExpression + ")");
		boolean ret = getReasoner().isSatisfiable(
				getTranslator().translateCE(classExpression));
		logger.finer("" + ret);
		return ret;
	}

	@Override
	public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
			throws OWLException {
		this.pendingChanges.addAll(changes);
		for (OWLOntologyChange change : changes) {
			change.accept(this.ontologyChangeVisitor);
		}
	}

	@Override
	public void precomputeInferences(InferenceType... inferenceTypes)
			throws ReasonerInterruptedException, TimeOutException,
			InconsistentOntologyException {
		if (inferenceTypes == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.finer("precomputeInferences(" + inferenceTypes + ")");
		getReasoner().classify();
	}

	public boolean removeAxiom(OWLAxiom axiom) {
		boolean ret = this.pendingAxiomRemovals.add(axiom);
		Set<ComplexIntegerAxiom> axioms = getTranslator().translateSA(
				Collections.singleton(axiom));
		for (ComplexIntegerAxiom ax : axioms) {
			boolean changed = this.jcelCore.removeAxiom(ax);
			ret = ret || changed;
		}
		return ret;
	}

}
