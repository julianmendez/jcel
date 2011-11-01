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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.ProgressMonitor;

import de.tudresden.inf.lat.jcel.classifier.JcelClassifierImpl;
import de.tudresden.inf.lat.jcel.classifier.JcelModel;
import de.tudresden.inf.lat.jcel.classifier.ModelConstructionException;
import de.tudresden.inf.lat.jcel.classifier.ModelConstructor;

/**
 * This class implements the available functionality of a
 * <code>JcelReasoner</code>. This is an auxiliary class used by
 * <code>JcelReasoner</code>.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
class JcelReasonerProcessor {

	// private static final Logger logger = Logger
	// .getLogger(JcelReasonerProcessor.class.getName());

	private JcelModel model = null;
	private ModelConstructor modelConstructor = null;
	private Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
	private OWLOntologyManager ontologyManager = null;

	public JcelReasonerProcessor(OWLOntologyManager manager) {
		this.ontologyManager = manager;
		this.modelConstructor = new ModelConstructor(new JcelClassifierImpl(
				manager));
	}

	public JcelReasonerProcessor(OWLOntologyManager manager,
			ProgressMonitor monitor) {
		this.ontologyManager = manager;
		this.modelConstructor = new ModelConstructor(new JcelClassifierImpl(
				manager, monitor));

	}

	protected void assertClassification() throws JcelReasonerException {
		if (!isClassified()) {
			throw new JcelReasonerException(
					"The ontology has not been classified!");
		}
	}

	public void classify() throws JcelReasonerException {
		try {
			this.model = this.modelConstructor.createModel(getOntologies());
		} catch (ModelConstructionException e) {
			throw new JcelReasonerException(e);
		}
	}

	public void clearOntologies() throws JcelReasonerException {
		this.ontologies = new HashSet<OWLOntology>();
	}

	protected Set<Set<OWLClass>> convertToOWLClass(Set<Set<OWLEntity>> setOfSets) {
		Set<Set<OWLClass>> ret = new HashSet<Set<OWLClass>>();
		for (Set<OWLEntity> elem : setOfSets) {
			ret.add(pickOWLClasses(elem));
		}
		return ret;
	}

	public void dispose() throws JcelReasonerException {
		this.ontologies = new HashSet<OWLOntology>();
	}

	public Set<OWLEntity> flatten(Set<Set<OWLEntity>> setOfSets) {
		Set<OWLEntity> ret = new HashSet<OWLEntity>();
		for (Set<OWLEntity> set : setOfSets) {
			for (OWLEntity elem : set) {
				ret.add(elem);
			}
		}
		return ret;
	}

	public Set<Set<OWLClass>> getAncestorClasses(OWLDescription description)
			throws JcelReasonerException {
		return convertToOWLClass(getModel().getOWLEntityGraph().getEqAncestors(
				description.asOWLClass()));
	}

	public Set<Set<OWLObjectProperty>> getAncestorProperties(
			OWLObjectProperty property) throws JcelReasonerException {
		return getModel().getOWLObjectPropertyGraph().getEqAncestors(property);
	}

	public OWLEntity getCurrentEntity() {
		return null;
	}

	public Set<Set<OWLClass>> getDescendantClasses(OWLDescription description)
			throws JcelReasonerException {
		return convertToOWLClass(getModel().getOWLEntityGraph()
				.getEqDescendants(description.asOWLClass()));
	}

	public Set<Set<OWLObjectProperty>> getDescendantProperties(
			OWLObjectProperty property) throws JcelReasonerException {
		return getModel().getOWLObjectPropertyGraph()
				.getEqDescendants(property);
	}

	public Set<Set<OWLDescription>> getDomains(OWLObjectProperty property)
			throws JcelReasonerException {
		throw new JcelReasonerException(
				"Not implemented: getDomains(OWLObjectProperty) .");
	}

	public Set<OWLClass> getEquivalentClasses(OWLDescription description)
			throws JcelReasonerException {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		if (isClassified()) {
			ret = pickOWLClasses(getModel().getOWLEntityGraph().getEquivalents(
					description.asOWLClass()));
		}
		return ret;
	}

	public Set<OWLObjectProperty> getEquivalentProperties(
			OWLObjectProperty property) throws JcelReasonerException {
		Set<OWLObjectProperty> ret = new HashSet<OWLObjectProperty>();
		if (isClassified()) {
			ret = getModel().getOWLObjectPropertyGraph().getEquivalents(
					property);
		}
		return ret;
	}

	protected ModelConstructor getExtraReasoner() {
		return this.modelConstructor;
	}

	public Set<OWLClass> getInconsistentClasses() throws JcelReasonerException {
		Set<OWLClass> ret = getEquivalentClasses(getOWLNothing());
		if (ret.size() == 1) {
			ret = new HashSet<OWLClass>();
		}
		return ret;
	}

	public Set<OWLIndividual> getIndividuals(OWLDescription description,
			boolean direct) throws JcelReasonerException {
		Set<OWLIndividual> ret = new HashSet<OWLIndividual>();
		if (description instanceof OWLClass) {
			OWLClass type = description.asOWLClass();
			Set<OWLEntity> entitySet = null;
			if (direct) {
				entitySet = flatten(getModel().getOWLEntityGraph()
						.getEqChildren(type));
			} else {
				entitySet = flatten(getModel().getOWLEntityGraph()
						.getEqChildren(type));
			}
			ret = pickOWLIndividuals(entitySet);
		}
		return ret;
	}

	public Set<OWLOntology> getLoadedOntologies() {
		return this.ontologies;
	}

	protected JcelModel getModel() {
		return this.model;
	}

	public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(
			OWLIndividual individual) throws JcelReasonerException {
		throw new JcelReasonerException(
				"Not implemented: getObjectPropertyRelationships(OWLIndividual) .");
	}

	protected Set<OWLOntology> getOntologies() {
		return this.ontologies;
	}

	public OWLClass getOWLNothing() {
		return this.ontologyManager.getOWLDataFactory().getOWLNothing();
	}

	public OWLClass getOWLThing() {
		return this.ontologyManager.getOWLDataFactory().getOWLThing();
	}

	public ProgressMonitor getProgressMonitor() {
		return this.modelConstructor.getClassifier().getProgressMonitor();
	}

	public Set<OWLDescription> getRanges(OWLObjectProperty property)
			throws JcelReasonerException {
		throw new JcelReasonerException(
				"Not implemented: getRanges(OWLObjectProperty) .");
	}

	public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual individual,
			OWLObjectPropertyExpression propertyExpression)
			throws JcelReasonerException {
		throw new JcelReasonerException(
				"Not implemented: getRelatedIndividuals(OWLIndividual, OWLObjectPropertyExpression) .");
	}

	public Set<Set<OWLClass>> getSubClasses(OWLDescription description)
			throws JcelReasonerException {
		Set<Set<OWLClass>> ret = new HashSet<Set<OWLClass>>();
		if (isClassified()) {
			ret = convertToOWLClass(getModel().getOWLEntityGraph()
					.getEqChildren(description.asOWLClass()));
		}
		return ret;
	}

	public Set<Set<OWLObjectProperty>> getSubProperties(
			OWLObjectProperty property) throws JcelReasonerException {
		Set<Set<OWLObjectProperty>> ret = new HashSet<Set<OWLObjectProperty>>();
		if (isClassified()) {
			ret = getModel().getOWLObjectPropertyGraph()
					.getEqChildren(property);
		}
		return ret;
	}

	public Set<Set<OWLClass>> getSuperClasses(OWLDescription description)
			throws JcelReasonerException {
		Set<Set<OWLClass>> ret = new HashSet<Set<OWLClass>>();
		if (isClassified()) {
			ret = convertToOWLClass(getModel().getOWLEntityGraph()
					.getEqParents(description.asOWLClass()));
		}
		return ret;
	}

	public Set<Set<OWLObjectProperty>> getSuperProperties(
			OWLObjectProperty property) throws JcelReasonerException {
		Set<Set<OWLObjectProperty>> ret = new HashSet<Set<OWLObjectProperty>>();
		if (isClassified()) {
			ret = getModel().getOWLObjectPropertyGraph().getEqParents(property);
		}
		return ret;
	}

	public Set<Set<OWLClass>> getTypes(OWLIndividual individual, boolean direct)
			throws JcelReasonerException {
		Set<Set<OWLClass>> ret = new HashSet<Set<OWLClass>>();
		if (isClassified()) {
			Set<Set<OWLEntity>> types = null;
			if (direct) {
				types = getModel().getOWLEntityGraph().getEqParents(individual);
			} else {
				types = getModel().getOWLEntityGraph().getEqAncestors(
						individual);
			}
			ret = convertToOWLClass(types);
		}
		return ret;
	}

	public boolean hasObjectPropertyRelationship(OWLIndividual individual0,
			OWLObjectPropertyExpression property, OWLIndividual individual1)
			throws JcelReasonerException {
		throw new JcelReasonerException(
				"Not implemented: hasObjectPropertyRelationship(OWLIndividual, OWLObjectPropertyExpression, OWLIndividual) .");
	}

	public boolean hasType(OWLIndividual individual, OWLDescription type,
			boolean direct) throws JcelReasonerException {
		boolean ret = false;
		if (type instanceof OWLClass) {
			if (direct) {
				ret = flatten(
						getModel().getOWLEntityGraph().getEqParents(individual))
						.contains(type.asOWLClass());
			} else {
				ret = flatten(
						getModel().getOWLEntityGraph().getEqAncestors(
								individual)).contains(type.asOWLClass());
			}
		} else {
			throw new JcelReasonerException("Function not supported: hasType('"
					+ individual + "', '" + type + "', " + direct + ").");
		}
		return ret;
	}

	public boolean isClassified() throws JcelReasonerException {
		return this.model != null;
	}

	public boolean isConsistent(OWLOntology ontology)
			throws JcelReasonerException {
		return !isEquivalentClass(getOWLNothing(), getOWLThing())
				&& pickOWLIndividuals(
						getModel().getOWLEntityGraph().getEquivalents(
								getOWLNothing())).isEmpty();
	}

	/**
	 * Returns <code>true</code> when an OWL class belongs to the ontology.
	 * 
	 * @return <code>true</code> if the OWL class is defined.
	 */
	public boolean isDefined(OWLClass cls) {
		boolean ret = false;
		if (cls.equals(getOWLNothing()) || cls.equals(getOWLThing())) {
			ret = true;
		} else {
			for (OWLOntology currentOntology : getOntologies()) {
				ret = ret || (currentOntology != null)
						&& currentOntology.containsClassReference(cls.getURI());
			}
		}
		return ret;
	}

	/**
	 * Returns <code>true</code> when an individual belongs to the ontology.
	 * 
	 * @return <code>true</code> if the individual is defined
	 */
	public boolean isDefined(OWLIndividual individual) {
		boolean ret = false;
		for (OWLOntology currentOntology : getOntologies()) {
			ret = ret
					|| (currentOntology != null && currentOntology
							.containsIndividualReference(individual.getURI()));
		}

		return ret;
	}

	/**
	 * Returns true when an property belongs to the ontology.
	 * 
	 * @return <code>true</code> if the property is defined
	 */
	public boolean isDefined(OWLObjectProperty property) {
		boolean ret = false;
		for (OWLOntology currentOntology : getOntologies()) {
			ret = ret
					|| (currentOntology != null && currentOntology
							.containsObjectPropertyReference(property.getURI()));
		}
		return ret;
	}

	public boolean isEquivalentClass(OWLDescription clsC, OWLDescription clsD)
			throws JcelReasonerException {
		assertClassification();
		return getModel().getOWLEntityGraph().getEquivalents(clsC.asOWLClass())
				.contains(clsD.asOWLClass());
	}

	public boolean isRealised() throws JcelReasonerException {
		throw new JcelReasonerException("Not implemented: isRealised() .");
	}

	public boolean isReflexive(OWLObjectProperty property)
			throws JcelReasonerException {
		return getModel().getReflexiveOWLObjectProperties().contains(property);
	}

	public boolean isSatisfiable(OWLDescription description)
			throws JcelReasonerException {
		boolean ret = true;
		if (isClassified()) {
			if (description.isLiteral()) {
				ret = !isEquivalentClass(description.asOWLClass(),
						getOWLNothing());
			} else {
				Set<OWLEntity> referredEntities = description.getSignature();
				Set<OWLClass> inconsistentClasses = getInconsistentClasses();
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

	public boolean isSubClassOf(OWLDescription clsC, OWLDescription clsD)
			throws JcelReasonerException {
		return getModel().getOWLEntityGraph().getEquivalents(clsC.asOWLClass())
				.contains(clsD.asOWLClass())
				|| flatten(
						getModel().getOWLEntityGraph().getEqAncestors(
								clsC.asOWLClass())).contains(clsD.asOWLClass());
	}

	public boolean isTransitive(OWLObjectProperty property)
			throws JcelReasonerException {
		return getModel().getTransitiveOWLObjectProperties().contains(property);
	}

	/**
	 * Loads the ontology.
	 * 
	 * @throws JcelReasonerException
	 */
	public void loadOntologies(Set<OWLOntology> setOfOntologies)
			throws JcelReasonerException {
		this.ontologies = setOfOntologies;
		this.model = null;
	}

	protected Set<OWLClass> pickOWLClasses(Set<OWLEntity> set) {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		for (OWLEntity entity : set) {
			if (entity instanceof OWLClass) {
				ret.add(entity.asOWLClass());
			}
		}
		return ret;
	}

	protected Set<OWLIndividual> pickOWLIndividuals(Set<OWLEntity> set) {
		Set<OWLIndividual> ret = new HashSet<OWLIndividual>();
		for (OWLEntity entity : set) {
			if (entity instanceof OWLIndividual) {
				ret.add(entity.asOWLIndividual());
			}
		}
		return ret;
	}

	public void realise() throws JcelReasonerException {
		throw new JcelReasonerException("Not implemented: realise() .");
	}

	public void setProgressMonitor(ProgressMonitor pMonitor) {
		this.modelConstructor.getClassifier().setProgressMonitor(pMonitor);
	}

	public void unloadOntologies(Set<OWLOntology> ontologySet)
			throws JcelReasonerException {
		this.ontologies.removeAll(ontologySet);
	}
}
