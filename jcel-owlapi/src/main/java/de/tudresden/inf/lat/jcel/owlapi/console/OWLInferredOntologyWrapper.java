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

package de.tudresden.inf.lat.jcel.owlapi.console;

import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * This class contains the ontology inferred by a reasoner.
 * 
 * @author Julian Mendez
 */
public class OWLInferredOntologyWrapper {

	private final OWLOntology ontology;
	private final OWLReasoner reasoner;

	public OWLInferredOntologyWrapper(OWLReasoner reasoner) throws OWLOntologyCreationException {
		if (reasoner == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.reasoner = reasoner;

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		this.ontology = manager.createOntology(reasoner.getRootOntology().getOntologyID());

		process();

	}

	private void addAxiom(OWLAxiom axiom) {
		this.ontology.getOWLOntologyManager().addAxiom(this.ontology, axiom);
	}

	public OWLOntology getOWLOntology() {
		return this.ontology;
	}

	private OWLReasoner getReasoner() {
		return this.reasoner;
	}

	private void process() {
		Set<OWLClass> classSet = new TreeSet<OWLClass>();
		classSet.addAll(getReasoner().getRootOntology().getClassesInSignature());
		Set<OWLObjectProperty> objectPropertySet = new TreeSet<OWLObjectProperty>();
		objectPropertySet.addAll(getReasoner().getRootOntology().getObjectPropertiesInSignature());
		Set<OWLDataProperty> dataPropertySet = new TreeSet<OWLDataProperty>();
		dataPropertySet.addAll(getReasoner().getRootOntology().getDataPropertiesInSignature());
		Set<OWLNamedIndividual> individualSet = new TreeSet<OWLNamedIndividual>();
		individualSet.addAll(getReasoner().getRootOntology().getIndividualsInSignature());

		processDeclarationC(classSet);
		processDeclarationOP(objectPropertySet);
		processDeclarationDP(dataPropertySet);
		processDeclarationI(individualSet);

		{
			Set<OWLClass> classesToVisit = new TreeSet<OWLClass>();
			classesToVisit.addAll(classSet);
			while (!classesToVisit.isEmpty()) {
				OWLClass cls = classesToVisit.iterator().next();
				classesToVisit.remove(cls);
				Set<OWLClass> equivClasses = getReasoner().getEquivalentClasses(cls).getEntities();
				if (equivClasses.size() > 1) {
					processEquivalentClasses(equivClasses);
				}
				classesToVisit.removeAll(equivClasses);
			}
		}

		{
			Set<OWLObjectProperty> objectPropertiesToVisit = new TreeSet<OWLObjectProperty>();
			objectPropertiesToVisit.addAll(objectPropertySet);
			while (!objectPropertiesToVisit.isEmpty()) {
				OWLObjectProperty property = objectPropertiesToVisit.iterator().next();
				objectPropertiesToVisit.remove(property);
				Set<OWLObjectPropertyExpression> equivProperties = getReasoner().getEquivalentObjectProperties(property)
						.getEntities();
				if (equivProperties.size() > 1) {
					processEquivalentObjectProperties(equivProperties);
				}
				objectPropertiesToVisit.removeAll(equivProperties);
			}
		}

		{
			Set<OWLDataProperty> dataPropertiesToVisit = new TreeSet<OWLDataProperty>();
			dataPropertiesToVisit.addAll(dataPropertySet);
			while (!dataPropertiesToVisit.isEmpty()) {
				OWLDataProperty property = dataPropertiesToVisit.iterator().next();
				dataPropertiesToVisit.remove(property);
				Set<OWLDataProperty> equivProperties = getReasoner().getEquivalentDataProperties(property)
						.getEntities();
				if (equivProperties.size() > 1) {
					processEquivalentDataProperties(equivProperties);
				}
				dataPropertiesToVisit.removeAll(equivProperties);
			}
		}

		for (OWLClass subClass : classSet) {
			Set<OWLClass> superClasses = new TreeSet<OWLClass>();
			superClasses.addAll(getReasoner().getSuperClasses(subClass, true).getFlattened());
			for (OWLClass superClass : superClasses) {
				processSubClassOf(subClass, superClass);
			}
		}

		for (OWLObjectProperty subProperty : objectPropertySet) {
			Set<OWLObjectPropertyExpression> superProperties = new TreeSet<OWLObjectPropertyExpression>();
			superProperties.addAll(getReasoner().getSuperObjectProperties(subProperty, true).getFlattened());
			for (OWLObjectPropertyExpression superProperty : superProperties) {
				processSubObjectPropertyOf(subProperty.asOWLObjectProperty(), superProperty.asOWLObjectProperty());
			}
		}

		for (OWLDataProperty subProperty : dataPropertySet) {
			Set<OWLDataPropertyExpression> superProperties = new TreeSet<OWLDataPropertyExpression>();
			superProperties.addAll(getReasoner().getSuperDataProperties(subProperty, true).getFlattened());
			for (OWLDataPropertyExpression superProperty : superProperties) {
				processSubDataPropertyOf(subProperty.asOWLDataProperty(), superProperty.asOWLDataProperty());
			}
		}

		for (OWLClass cls : classSet) {
			Set<OWLNamedIndividual> instances = new TreeSet<OWLNamedIndividual>();
			instances.addAll(getReasoner().getInstances(cls, true).getFlattened());
			for (OWLNamedIndividual individual : instances) {
				processClassAssertion(cls, individual);
			}
		}

		for (OWLObjectProperty property : objectPropertySet) {
			for (OWLNamedIndividual individual : individualSet) {
				Set<OWLNamedIndividual> propertyValues = new TreeSet<OWLNamedIndividual>();
				propertyValues.addAll(getReasoner().getObjectPropertyValues(individual, property.asOWLObjectProperty())
						.getFlattened());
				for (OWLNamedIndividual otherIndividual : propertyValues) {
					processObjectPropertyAssertion(property, individual, otherIndividual);
				}
			}
		}

		for (OWLDataProperty property : dataPropertySet) {
			for (OWLNamedIndividual individual : individualSet) {
				Set<OWLLiteral> propertyValues = new TreeSet<OWLLiteral>();
				propertyValues.addAll(getReasoner().getDataPropertyValues(individual, property.asOWLDataProperty()));
				for (OWLLiteral otherIndividual : propertyValues) {
					processDataPropertyAssertion(property, individual, otherIndividual);
				}
			}
		}
	}

	private void processClassAssertion(OWLClass cls, OWLNamedIndividual individual) {
		processEntity(cls);
		processEntity(individual);
	}

	private void processDataPropertyAssertion(OWLDataProperty property, OWLNamedIndividual subject, OWLLiteral object) {
		processEntity(property);
		processEntity(subject);
		// processEntity(object);
	}

	private void processDeclarationC(Set<OWLClass> entities) {
		for (OWLClass elem : entities) {
			processEntity(elem);
		}
	}

	private void processDeclarationDP(Set<OWLDataProperty> entities) {
		for (OWLDataProperty elem : entities) {
			processEntity(elem);
		}
	}

	private void processDeclarationI(Set<OWLNamedIndividual> entities) {
		for (OWLNamedIndividual elem : entities) {
			processEntity(elem);
		}
	}

	private void processDeclarationOP(Set<OWLObjectProperty> entities) {
		for (OWLObjectProperty elem : entities) {
			processEntity(elem);
		}
	}

	private void processEntity(OWLClass entity) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLDeclarationAxiom(entity));
	}

	private void processEntity(OWLDataProperty entity) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLDeclarationAxiom(entity));
	}

	private void processEntity(OWLNamedIndividual entity) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLDeclarationAxiom(entity));
	}

	private void processEntity(OWLObjectProperty entity) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLDeclarationAxiom(entity));
	}

	private void processEquivalentClasses(Set<OWLClass> classSet) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLEquivalentClassesAxiom(classSet));
	}

	private void processEquivalentDataProperties(Set<OWLDataProperty> propertySet) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory()
				.getOWLEquivalentDataPropertiesAxiom(propertySet));
	}

	private void processEquivalentObjectProperties(Set<OWLObjectPropertyExpression> propertySet) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory()
				.getOWLEquivalentObjectPropertiesAxiom(propertySet));
	}

	private void processObjectPropertyAssertion(OWLObjectProperty property, OWLNamedIndividual subject,
			OWLNamedIndividual object) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(property,
				subject, object));
	}

	private void processSubClassOf(OWLClass subClass, OWLClass superClass) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLSubClassOfAxiom(subClass, superClass));
	}

	private void processSubDataPropertyOf(OWLDataProperty subProperty, OWLDataProperty superProperty) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLSubDataPropertyOfAxiom(subProperty,
				superProperty));
	}

	private void processSubObjectPropertyOf(OWLObjectProperty subProperty, OWLObjectProperty superProperty) {
		addAxiom(this.ontology.getOWLOntologyManager().getOWLDataFactory().getOWLSubObjectPropertyOfAxiom(subProperty,
				superProperty));
	}

}
