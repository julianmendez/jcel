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

import java.util.Collections;
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
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.impl.NodeFactory;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNodeSet;
import org.semanticweb.owlapi.util.Version;

import de.tudresden.inf.lat.jcel.owlapi.classifier.JcelClassifier;
import de.tudresden.inf.lat.jcel.owlapi.classifier.JcelClassifierImpl;
import de.tudresden.inf.lat.jcel.owlapi.classifier.JcelModel;

/**
 * This class implements the available functionality of a
 * <code>JcelReasoner</code>. This is an auxiliary class used by
 * <code>JcelReasoner</code>.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class JcelReasonerProcessor {

	private static final Logger logger = Logger
			.getLogger(JcelReasonerProcessor.class.getName());

	private JcelClassifier classifier = null;
	private JcelModel model = null;
	private OWLOntology rootOntology = null;
	private long timeOut = Long.MAX_VALUE;

	/**
	 * Constructs a new reasoner processor.
	 * 
	 * @param rootOnt
	 *            root ontology
	 */
	public JcelReasonerProcessor(OWLOntology rootOnt) {
		if (rootOnt == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.rootOntology = rootOnt;
		this.classifier = new JcelClassifierImpl(getOWLNothing(),
				getOWLThing(), getOWLBottomObjectProperty(),
				getOWLTopObjectProperty(), getOWLBottomDataProperty(),
				getOWLTopDataProperty());
	}

	/**
	 * Constructs a new reasoner processor.
	 * 
	 * @param rootOnt
	 *            root ontology
	 * @param configuration
	 *            reasoner
	 */
	public JcelReasonerProcessor(OWLOntology rootOnt,
			OWLReasonerConfiguration configuration) {
		if (rootOnt == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (configuration == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.rootOntology = rootOnt;
		this.classifier = new JcelClassifierImpl(getOWLNothing(),
				getOWLThing(), getOWLBottomObjectProperty(),
				getOWLTopObjectProperty(), getOWLBottomDataProperty(),
				getOWLTopDataProperty(), configuration);
		this.timeOut = configuration.getTimeOut();
	}

	private void assertClassification() throws JcelReasonerException {
		if (!isClassified()) {
			throw new JcelReasonerException(
					"The ontology has not been classified!");
		}
	}

	private NodeSet<OWLClass> convertToNodeSetOfOWLClass(
			Set<Set<OWLEntity>> setOfSets) {
		Set<Node<OWLClass>> nodeSet = new HashSet<Node<OWLClass>>();
		for (Set<OWLEntity> elem : setOfSets) {
			nodeSet.add(NodeFactory.getOWLClassNode(pickOWLClasses(elem)));
		}
		return new OWLClassNodeSet(nodeSet);
	}

	private NodeSet<OWLNamedIndividual> convertToNodeSetOfOWLNamedIndividual(
			Set<OWLNamedIndividual> individualSet) {

		Set<Node<OWLNamedIndividual>> nodeSet = new HashSet<Node<OWLNamedIndividual>>();
		for (OWLNamedIndividual individual : individualSet) {
			nodeSet.add(NodeFactory.getOWLNamedIndividualNode(individual));
		}
		return new OWLNamedIndividualNodeSet(nodeSet);
	}

	private NodeSet<OWLObjectPropertyExpression> convertToNodeSetOfOWLObjectPropertyExpression(
			Set<Set<OWLObjectProperty>> setOfSets) {
		Set<Node<OWLObjectPropertyExpression>> nodeSet = new HashSet<Node<OWLObjectPropertyExpression>>();
		for (Set<OWLObjectProperty> elem : setOfSets) {
			nodeSet.add(NodeFactory
					.getOWLObjectPropertyNode(convertToOWLObjectPropertyExpression(elem)));
		}
		return new OWLObjectPropertyNodeSet(nodeSet);
	}

	private Set<OWLObjectPropertyExpression> convertToOWLObjectPropertyExpression(
			Set<OWLObjectProperty> set) {
		Set<OWLObjectPropertyExpression> ret = new HashSet<OWLObjectPropertyExpression>();
		ret.addAll(set);
		return ret;

	}

	public void dispose() throws JcelReasonerException {
		this.rootOntology = null;
	}

	public Set<OWLEntity> flatten(Set<Set<OWLEntity>> setOfSets) {
		if (setOfSets == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLEntity> ret = new HashSet<OWLEntity>();
		for (Set<OWLEntity> set : setOfSets) {
			for (OWLEntity elem : set) {
				ret.add(elem);
			}
		}
		return ret;
	}

	public Node<OWLClass> getBottomClassNode() {
		return NodeFactory.getOWLClassNode(getEquivClasses(getOWLNothing()));
	}

	public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
		return getEquivalentObjectProperties(getOWLBottomObjectProperty());
	}

	public JcelClassifier getClassifier() {
		return this.classifier;
	}

	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(
			OWLNamedIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new JcelReasonerException(
				"Not implemented: getDifferentIndividuals(OWLNamedIndividual)");
	}

	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression clExp) {
		if (clExp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new JcelReasonerException(
				"Not implemented: getDisjointClasses(OWLClassExpression)");
	}

	public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(
			OWLObjectPropertyExpression propExpr) {
		if (propExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new JcelReasonerException(
				"Not implemented: getDisjointObjectProperties(OWLObjectPropertyExpression)");
	}

	public Node<OWLClass> getEquivalentClasses(
			OWLClassExpression classExpression) throws JcelReasonerException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Node<OWLClass> ret = null;
		if (isClassified()) {
			ret = NodeFactory.getOWLClassNode(pickOWLClasses(getModel()
					.getOWLEntityGraph().getEquivalents(
							classExpression.asOWLClass())));
		}
		return ret;
	}

	public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(
			OWLObjectPropertyExpression propertyExpression) {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = propertyExpression.asOWLObjectProperty();
		Node<OWLObjectPropertyExpression> ret = null;
		if (isClassified()) {
			ret = NodeFactory
					.getOWLObjectPropertyNode(convertToOWLObjectPropertyExpression(getModel()
							.getOWLObjectPropertyGraph().getEquivalents(
									property)));
		}
		return ret;
	}

	private Set<OWLClass> getEquivClasses(OWLClass cls)
			throws JcelReasonerException {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		if (isClassified()) {
			ret = pickOWLClasses(getModel().getOWLEntityGraph().getEquivalents(
					cls));
		}
		return ret;
	}

	public NodeSet<OWLNamedIndividual> getInstances(
			OWLClassExpression classExpression, boolean direct)
			throws JcelReasonerException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLClass type = classExpression.asOWLClass();
		Set<OWLEntity> entitySet = flatten(getModel().getOWLEntityGraph()
				.getSubElements(type, direct));
		return convertToNodeSetOfOWLNamedIndividual(pickOWLNamedIndividuals(entitySet));
	}

	private JcelModel getModel() {
		return this.model;
	}

	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(
			OWLNamedIndividual individual,
			OWLObjectPropertyExpression propertyExpression) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		NodeSet<OWLNamedIndividual> ret = null;
		if (isClassified()) {
			Set<OWLObjectPropertyAssertionAxiom> axiomSet = getRootOntology()
					.getObjectPropertyAssertionAxioms(individual);
			Set<OWLNamedIndividual> individualSet = new HashSet<OWLNamedIndividual>();
			for (OWLObjectPropertyAssertionAxiom axiom : axiomSet) {
				if (propertyExpression.equals(axiom.getProperty())) {
					individualSet.add(axiom.getObject().asOWLNamedIndividual());
				}
			}
			ret = convertToNodeSetOfOWLNamedIndividual(individualSet);
		}
		return ret;
	}

	public OWLDataProperty getOWLBottomDataProperty() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLBottomDataProperty();
	}

	public OWLObjectProperty getOWLBottomObjectProperty() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLBottomObjectProperty();
	}

	public OWLClass getOWLNothing() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLNothing();
	}

	public OWLClass getOWLThing() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLThing();
	}

	public OWLDataProperty getOWLTopDataProperty() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLTopDataProperty();
	}

	public OWLObjectProperty getOWLTopObjectProperty() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLTopObjectProperty();
	}

	public Set<OWLAxiom> getPendingAxiomAdditions() {
		return Collections.emptySet();
	}

	public Set<OWLAxiom> getPendingAxiomRemovals() {
		return Collections.emptySet();
	}

	public List<OWLOntologyChange> getPendingChanges() {
		return Collections.emptyList();
	}

	public Set<InferenceType> getPrecomputableInferenceTypes() {
		return Collections.singleton(InferenceType.CLASS_HIERARCHY);
	}

	public OWLReasonerConfiguration getReasonerConfiguration() {
		return getClassifier().getReasonerConfiguration();
	}

	public String getReasonerName() {
		return getClass().getPackage().getImplementationTitle();
	}

	public Version getReasonerVersion() {
		Version ret = new Version(0, 0, 0, 0);
		String versionId = getClass().getPackage().getImplementationVersion();
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
		return ret;
	}

	public OWLOntology getRootOntology() {
		return this.rootOntology;
	}

	public Node<OWLNamedIndividual> getSameIndividuals(
			OWLNamedIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Node<OWLNamedIndividual> ret = null;
		if (isClassified()) {
			ret = NodeFactory
					.getOWLNamedIndividualNode(pickOWLNamedIndividuals(getModel()
							.getOWLEntityGraph().getEquivalents(individual)));
		}
		return ret;
	}

	public NodeSet<OWLClass> getSubClasses(OWLClassExpression description,
			boolean direct) {
		if (description == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLClass(getModel().getOWLEntityGraph()
				.getSubElements(description.asOWLClass(), direct));
	}

	public NodeSet<OWLObjectPropertyExpression> getSubProperties(
			OWLObjectPropertyExpression propertyExpression, boolean direct)
			throws JcelReasonerException {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLObjectPropertyExpression(getModel()
				.getOWLObjectPropertyGraph().getSubElements(
						propertyExpression.asOWLObjectProperty(), direct));
	}

	public NodeSet<OWLClass> getSuperClasses(
			OWLClassExpression classExpression, boolean direct) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLClass(getModel().getOWLEntityGraph()
				.getSuperElements(classExpression.asOWLClass(), direct));
	}

	public NodeSet<OWLObjectPropertyExpression> getSuperProperties(
			OWLObjectPropertyExpression propertyExpression, boolean direct) {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLObjectPropertyExpression(getModel()
				.getOWLObjectPropertyGraph().getSuperElements(
						propertyExpression.asOWLObjectProperty(), direct));
	}

	public long getTimeOut() {
		return this.timeOut;
	}

	public Node<OWLClass> getTopClassNode() {
		return NodeFactory.getOWLClassNode(getEquivClasses(getOWLThing()));
	}

	public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
		return getEquivalentObjectProperties(getOWLTopObjectProperty());
	}

	public NodeSet<OWLClass> getTypes(OWLNamedIndividual individual,
			boolean direct) throws JcelReasonerException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		NodeSet<OWLClass> ret = null;
		if (isClassified()) {
			Set<Set<OWLEntity>> types = getModel().getOWLEntityGraph()
					.getSuperElements(individual, direct);
			ret = convertToNodeSetOfOWLClass(types);
		}
		return ret;
	}

	public Node<OWLClass> getUnsatisfiableClasses() {
		Set<OWLClass> classSet = getEquivClasses(getOWLNothing());
		if (classSet.size() == 1) {
			classSet = new HashSet<OWLClass>();
		}
		return NodeFactory.getOWLClassNode(classSet);
	}

	public boolean hasType(OWLNamedIndividual individual,
			OWLClassExpression type, boolean direct)
			throws JcelReasonerException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (type instanceof OWLClass) {
			ret = flatten(
					getModel().getOWLEntityGraph().getSuperElements(individual,
							direct)).contains(type.asOWLClass());
		} else {
			throw new JcelReasonerException(
					"Only implemented for classes: hasType('" + individual
							+ "', '" + type + "', " + direct + ")");
		}
		return ret;
	}

	public void interrupt() {
		getClassifier().interrupt();
	}

	public boolean isClassified() throws JcelReasonerException {
		return this.model != null;
	}

	public boolean isConsistent() throws JcelReasonerException {
		return !isEquivalentClass(getOWLNothing(), getOWLThing())
				&& pickOWLNamedIndividuals(
						getModel().getOWLEntityGraph().getEquivalents(
								getOWLNothing())).isEmpty();
	}

	public boolean isEntailed(OWLAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		assertClassification();
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : isEntailed(OWLAxiom)");
	}

	public boolean isEntailed(Set<? extends OWLAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		assertClassification();
		throw new UnsupportedReasonerOperationInJcelException(
				"Unsupported operation : isEntailed(Set<? extends OWLAxiom>)");
	}

	public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
		if (axiomType == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

	private boolean isEquivalentClass(OWLClassExpression clsC,
			OWLClassExpression clsD) throws JcelReasonerException {
		assertClassification();
		return getModel().getOWLEntityGraph().getEquivalents(clsC.asOWLClass())
				.contains(clsD.asOWLClass());
	}

	public boolean isSatisfiable(OWLClassExpression classExpression)
			throws JcelReasonerException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = true;
		if (isClassified()) {
			if (classExpression.isClassExpressionLiteral()) {
				ret = !isEquivalentClass(classExpression.asOWLClass(),
						getOWLNothing());
			} else {
				Set<OWLEntity> referredEntities = classExpression
						.getSignature();
				Set<OWLClass> inconsistentClasses = getEquivClasses(getOWLNothing());
				for (OWLEntity current : referredEntities) {
					if (current.isOWLClass()
							&& inconsistentClasses.contains(current)) {
						ret = false;
					}
				}
			}
		}
		return ret;
	}

	private Set<OWLClass> pickOWLClasses(Set<OWLEntity> set) {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		for (OWLEntity entity : set) {
			if (entity instanceof OWLClass) {
				ret.add(entity.asOWLClass());
			}
		}
		return ret;
	}

	private Set<OWLNamedIndividual> pickOWLNamedIndividuals(Set<OWLEntity> set) {
		Set<OWLNamedIndividual> ret = new HashSet<OWLNamedIndividual>();
		for (OWLEntity entity : set) {
			if (entity instanceof OWLNamedIndividual) {
				ret.add(entity.asOWLNamedIndividual());
			}
		}
		return ret;
	}

	public void precomputeInferences() {
		logger.fine("classifying " + getRootOntology().getOntologyID());
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
		for (OWLOntology ontology : getRootOntology().getImportsClosure()) {
			axiomSet.addAll(ontology.getAxioms());
		}
		getClassifier().resetAndLoad(axiomSet);
		this.model = new JcelModel(getClassifier().getRelationGraph(),
				getClassifier().getTypeGraph(), getClassifier()
						.getReflexiveProperties(), getClassifier()
						.getTransitiveProperties());
	}

}
