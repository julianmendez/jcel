/*
 *
 * Copyright 2009-2013 Julian Mendez
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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import org.coode.owlapi.owlxml.renderer.OWLXMLWriter;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.vocab.OWLXMLVocabulary;

/**
 * This class makes an XML representation of the inferred data by an
 * OWLReasoner.
 * 
 * @author Julian Mendez
 */
public class OWLReasonerXMLOutput {

	private final OWLReasoner reasoner;
	private OWLXMLWriter writer = null;

	public OWLReasonerXMLOutput(OWLReasoner reasoner) {
		if (reasoner == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.reasoner = reasoner;
	}

	private OWLReasoner getReasoner() {
		return this.reasoner;
	}

	private OWLXMLWriter getWriter() {
		return this.writer;
	}

	private void render() {
		Set<OWLClass> classSet = new TreeSet<OWLClass>();
		classSet.addAll(getReasoner().getRootOntology().getClassesInSignature());
		Set<OWLObjectProperty> objectPropertySet = new TreeSet<OWLObjectProperty>();
		objectPropertySet.addAll(getReasoner().getRootOntology()
				.getObjectPropertiesInSignature());
		Set<OWLDataProperty> dataPropertySet = new TreeSet<OWLDataProperty>();
		dataPropertySet.addAll(getReasoner().getRootOntology()
				.getDataPropertiesInSignature());
		Set<OWLNamedIndividual> individualSet = new TreeSet<OWLNamedIndividual>();
		individualSet.addAll(getReasoner().getRootOntology()
				.getIndividualsInSignature());

		renderDeclarationC(classSet);
		renderDeclarationOP(objectPropertySet);
		renderDeclarationDP(dataPropertySet);
		renderDeclarationI(individualSet);

		{
			Set<OWLClass> classesToVisit = new TreeSet<OWLClass>();
			classesToVisit.addAll(classSet);
			while (!classesToVisit.isEmpty()) {
				OWLClass cls = classesToVisit.iterator().next();
				classesToVisit.remove(cls);
				Set<OWLClass> equivClasses = getReasoner()
						.getEquivalentClasses(cls).getEntities();
				if (equivClasses.size() > 1) {
					renderEquivalentClasses(equivClasses);
				}
				classesToVisit.removeAll(equivClasses);
			}
		}

		{
			Set<OWLObjectProperty> objectPropertiesToVisit = new TreeSet<OWLObjectProperty>();
			objectPropertiesToVisit.addAll(objectPropertySet);
			while (!objectPropertiesToVisit.isEmpty()) {
				OWLObjectProperty property = objectPropertiesToVisit.iterator()
						.next();
				objectPropertiesToVisit.remove(property);
				Set<OWLObjectPropertyExpression> equivProperties = getReasoner()
						.getEquivalentObjectProperties(property).getEntities();
				if (equivProperties.size() > 1) {
					renderEquivalentObjectPropertyExpressions(equivProperties);
				}
				objectPropertiesToVisit.removeAll(equivProperties);
			}
		}

		{
			Set<OWLDataProperty> dataPropertiesToVisit = new TreeSet<OWLDataProperty>();
			dataPropertiesToVisit.addAll(dataPropertySet);
			while (!dataPropertiesToVisit.isEmpty()) {
				OWLDataProperty property = dataPropertiesToVisit.iterator()
						.next();
				dataPropertiesToVisit.remove(property);
				Set<OWLDataProperty> equivProperties = getReasoner()
						.getEquivalentDataProperties(property).getEntities();
				if (equivProperties.size() > 1) {
					renderEquivalentDataProperties(equivProperties);
				}
				dataPropertiesToVisit.removeAll(equivProperties);
			}
		}

		for (OWLClass subClass : classSet) {
			Set<OWLClass> superClasses = new TreeSet<OWLClass>();
			superClasses.addAll(getReasoner().getSuperClasses(subClass, true)
					.getFlattened());
			for (OWLClass superClass : superClasses) {
				renderSubClassOf(subClass, superClass);
			}
		}

		for (OWLObjectProperty subProperty : objectPropertySet) {
			Set<OWLObjectPropertyExpression> superProperties = new TreeSet<OWLObjectPropertyExpression>();
			superProperties.addAll(getReasoner().getSuperObjectProperties(
					subProperty, true).getFlattened());
			for (OWLObjectPropertyExpression superProperty : superProperties) {
				renderSubObjectPropertyOf(subProperty.asOWLObjectProperty(),
						superProperty.asOWLObjectProperty());
			}
		}

		for (OWLDataProperty subProperty : dataPropertySet) {
			Set<OWLDataPropertyExpression> superProperties = new TreeSet<OWLDataPropertyExpression>();
			superProperties.addAll(getReasoner().getSuperDataProperties(
					subProperty, true).getFlattened());
			for (OWLDataPropertyExpression superProperty : superProperties) {
				renderSubDataPropertyOf(subProperty.asOWLDataProperty(),
						superProperty.asOWLDataProperty());
			}
		}

		for (OWLClass cls : classSet) {
			Set<OWLNamedIndividual> instances = new TreeSet<OWLNamedIndividual>();
			instances.addAll(getReasoner().getInstances(cls, true)
					.getFlattened());
			for (OWLNamedIndividual individual : instances) {
				renderClassAssertion(cls, individual);
			}
		}

		for (OWLObjectProperty property : objectPropertySet) {
			for (OWLNamedIndividual individual : individualSet) {
				Set<OWLNamedIndividual> propertyValues = new TreeSet<OWLNamedIndividual>();
				propertyValues.addAll(getReasoner().getObjectPropertyValues(
						individual, property.asOWLObjectProperty())
						.getFlattened());
				for (OWLNamedIndividual otherIndividual : propertyValues) {
					renderObjectPropertyAssertion(property, individual,
							otherIndividual);
				}
			}
		}

		for (OWLDataProperty property : dataPropertySet) {
			for (OWLNamedIndividual individual : individualSet) {
				Set<OWLLiteral> propertyValues = new TreeSet<OWLLiteral>();
				propertyValues.addAll(getReasoner().getDataPropertyValues(
						individual, property.asOWLDataProperty()));
				for (OWLLiteral otherIndividual : propertyValues) {
					renderDataPropertyAssertion(property, individual,
							otherIndividual);
				}
			}
		}
	}

	private void renderClassAssertion(OWLClass cls,
			OWLNamedIndividual individual) {
		getWriter().writeStartElement(OWLXMLVocabulary.CLASS_ASSERTION);
		renderEntity(cls);
		renderEntity(individual);
		getWriter().writeEndElement();

	}

	private void renderClassSet(Set<OWLClass> entitySet) {
		Set<OWLClass> set = new TreeSet<OWLClass>();
		set.addAll(entitySet);
		for (OWLClass entity : set) {
			renderEntity(entity);
		}
	}

	private void renderDataPropertyAssertion(OWLDataProperty property,
			OWLNamedIndividual subject, OWLLiteral object) {
		getWriter().writeStartElement(OWLXMLVocabulary.DATA_PROPERTY_ASSERTION);
		renderEntity(property);
		renderEntity(subject);
		renderEntity(object);
		getWriter().writeEndElement();
	}

	private void renderDeclarationC(Set<OWLClass> entities) {
		for (OWLClass elem : entities) {
			getWriter().writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			getWriter().writeEndElement();
		}
	}

	private void renderDeclarationDP(Set<OWLDataProperty> entities) {
		for (OWLDataProperty elem : entities) {
			getWriter().writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			getWriter().writeEndElement();
		}
	}

	private void renderDeclarationI(Set<OWLNamedIndividual> entities) {
		for (OWLNamedIndividual elem : entities) {
			getWriter().writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			getWriter().writeEndElement();
		}
	}

	private void renderDeclarationOP(Set<OWLObjectProperty> entities) {
		for (OWLObjectProperty elem : entities) {
			getWriter().writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			getWriter().writeEndElement();
		}
	}

	private void renderEntity(OWLClass entity) {
		getWriter().writeStartElement(OWLXMLVocabulary.CLASS);
		getWriter().writeIRIAttribute(entity.getIRI());
		getWriter().writeEndElement();
	}

	private void renderEntity(OWLDataProperty entity) {
		getWriter().writeStartElement(OWLXMLVocabulary.DATA_PROPERTY);
		getWriter().writeIRIAttribute(entity.getIRI());
		getWriter().writeEndElement();
	}

	private void renderEntity(OWLLiteral entity) {
		getWriter().writeStartElement(OWLXMLVocabulary.LITERAL);
		getWriter().writeDatatypeAttribute(entity.getDatatype());
		getWriter().writeEndElement();
	}

	private void renderEntity(OWLNamedIndividual entity) {
		getWriter().writeStartElement(OWLXMLVocabulary.NAMED_INDIVIDUAL);
		getWriter().writeIRIAttribute(entity.getIRI());
		getWriter().writeEndElement();
	}

	private void renderEntity(OWLObjectProperty entity) {
		getWriter().writeStartElement(OWLXMLVocabulary.OBJECT_PROPERTY);
		getWriter().writeIRIAttribute(entity.getIRI());
		getWriter().writeEndElement();
	}

	private void renderEquivalentClasses(Set<OWLClass> classSet) {
		getWriter().writeStartElement(OWLXMLVocabulary.EQUIVALENT_CLASSES);
		renderClassSet(classSet);
		getWriter().writeEndElement();
	}

	private void renderEquivalentDataProperties(Set<OWLDataProperty> propertySet) {
		getWriter().writeStartElement(
				OWLXMLVocabulary.EQUIVALENT_DATA_PROPERTIES);
		Set<OWLDataProperty> set = new TreeSet<OWLDataProperty>();
		set.addAll(propertySet);
		for (OWLDataProperty property : set) {
			renderEntity(property.asOWLDataProperty());
		}
		getWriter().writeEndElement();
	}

	private void renderEquivalentObjectPropertyExpressions(
			Set<OWLObjectPropertyExpression> propertySet) {
		getWriter().writeStartElement(
				OWLXMLVocabulary.EQUIVALENT_OBJECT_PROPERTIES);
		Set<OWLObjectPropertyExpression> set = new TreeSet<OWLObjectPropertyExpression>();
		set.addAll(propertySet);
		for (OWLObjectPropertyExpression propertyExpression : set) {
			renderEntity(propertyExpression.asOWLObjectProperty());
		}
		getWriter().writeEndElement();
	}

	private void renderObjectPropertyAssertion(OWLObjectProperty property,
			OWLNamedIndividual subject, OWLNamedIndividual object) {
		getWriter().writeStartElement(
				OWLXMLVocabulary.OBJECT_PROPERTY_ASSERTION);
		renderEntity(property);
		renderEntity(subject);
		renderEntity(object);
		getWriter().writeEndElement();
	}

	private void renderSubClassOf(OWLClass subClass, OWLClass superClass) {
		getWriter().writeStartElement(OWLXMLVocabulary.SUB_CLASS_OF);
		renderEntity(subClass);
		renderEntity(superClass);
		getWriter().writeEndElement();
	}

	private void renderSubDataPropertyOf(OWLDataProperty subProperty,
			OWLDataProperty superProperty) {
		getWriter().writeStartElement(OWLXMLVocabulary.SUB_DATA_PROPERTY_OF);
		renderEntity(subProperty);
		renderEntity(superProperty);
		getWriter().writeEndElement();
	}

	private void renderSubObjectPropertyOf(OWLObjectProperty subProperty,
			OWLObjectProperty superProperty) {
		getWriter().writeStartElement(OWLXMLVocabulary.SUB_OBJECT_PROPERTY_OF);
		renderEntity(subProperty);
		renderEntity(superProperty);
		getWriter().writeEndElement();
	}

	public void toXML(OutputStream out) throws OWLRendererException {
		if (out == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLOntology ontology = getReasoner().getRootOntology();
		Writer writer = new OutputStreamWriter(out);
		this.writer = new OWLXMLWriter(writer, ontology);
		getWriter().startDocument(ontology);
		render();
		getWriter().endDocument();
	}

}
