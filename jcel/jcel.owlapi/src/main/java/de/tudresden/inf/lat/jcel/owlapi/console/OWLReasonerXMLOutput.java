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

	private OWLReasoner reasoner = null;
	private OWLXMLWriter writer = null;

	public OWLReasonerXMLOutput(OWLReasoner reasoner) {
		if (reasoner == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.reasoner = reasoner;
	}

	private void render() {
		Set<OWLClass> classSet = new TreeSet<OWLClass>();
		classSet.addAll(this.reasoner.getRootOntology().getClassesInSignature());
		Set<OWLObjectProperty> objectPropertySet = new TreeSet<OWLObjectProperty>();
		objectPropertySet.addAll(this.reasoner.getRootOntology()
				.getObjectPropertiesInSignature());
		Set<OWLDataProperty> dataPropertySet = new TreeSet<OWLDataProperty>();
		dataPropertySet.addAll(this.reasoner.getRootOntology()
				.getDataPropertiesInSignature());
		Set<OWLNamedIndividual> individualSet = new TreeSet<OWLNamedIndividual>();
		individualSet.addAll(this.reasoner.getRootOntology()
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
				Set<OWLClass> equivClasses = this.reasoner
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
				Set<OWLObjectPropertyExpression> equivProperties = this.reasoner
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
				Set<OWLDataProperty> equivProperties = this.reasoner
						.getEquivalentDataProperties(property).getEntities();
				if (equivProperties.size() > 1) {
					renderEquivalentDataProperties(equivProperties);
				}
				dataPropertiesToVisit.removeAll(equivProperties);
			}
		}

		for (OWLClass subClass : classSet) {
			Set<OWLClass> superClasses = new TreeSet<OWLClass>();
			superClasses.addAll(this.reasoner.getSuperClasses(subClass, true)
					.getFlattened());
			for (OWLClass superClass : superClasses) {
				renderSubClassOf(subClass, superClass);
			}
		}

		for (OWLObjectProperty subProperty : objectPropertySet) {
			Set<OWLObjectPropertyExpression> superProperties = new TreeSet<OWLObjectPropertyExpression>();
			superProperties.addAll(this.reasoner.getSuperObjectProperties(
					subProperty, true).getFlattened());
			for (OWLObjectPropertyExpression superProperty : superProperties) {
				renderSubObjectPropertyOf(subProperty.asOWLObjectProperty(),
						superProperty.asOWLObjectProperty());
			}
		}

		for (OWLDataProperty subProperty : dataPropertySet) {
			Set<OWLDataPropertyExpression> superProperties = new TreeSet<OWLDataPropertyExpression>();
			superProperties.addAll(this.reasoner.getSuperDataProperties(
					subProperty, true).getFlattened());
			for (OWLDataPropertyExpression superProperty : superProperties) {
				renderSubDataPropertyOf(subProperty.asOWLDataProperty(),
						superProperty.asOWLDataProperty());
			}
		}

		for (OWLClass cls : classSet) {
			Set<OWLNamedIndividual> instances = new TreeSet<OWLNamedIndividual>();
			instances.addAll(this.reasoner.getInstances(cls, true)
					.getFlattened());
			for (OWLNamedIndividual individual : instances) {
				renderClassAssertion(cls, individual);
			}
		}

		for (OWLObjectProperty property : objectPropertySet) {
			for (OWLNamedIndividual individual : individualSet) {
				Set<OWLNamedIndividual> propertyValues = new TreeSet<OWLNamedIndividual>();
				propertyValues.addAll(this.reasoner.getObjectPropertyValues(
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
				propertyValues.addAll(this.reasoner.getDataPropertyValues(
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
		this.writer.writeStartElement(OWLXMLVocabulary.CLASS_ASSERTION);
		renderEntity(cls);
		renderEntity(individual);
		this.writer.writeEndElement();

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
		this.writer.writeStartElement(OWLXMLVocabulary.DATA_PROPERTY_ASSERTION);
		renderEntity(property);
		renderEntity(subject);
		renderEntity(object);
		this.writer.writeEndElement();
	}

	private void renderDeclarationC(Set<OWLClass> entities) {
		for (OWLClass elem : entities) {
			this.writer.writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			this.writer.writeEndElement();
		}
	}

	private void renderDeclarationDP(Set<OWLDataProperty> entities) {
		for (OWLDataProperty elem : entities) {
			this.writer.writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			this.writer.writeEndElement();
		}
	}

	private void renderDeclarationI(Set<OWLNamedIndividual> entities) {
		for (OWLNamedIndividual elem : entities) {
			this.writer.writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			this.writer.writeEndElement();
		}
	}

	private void renderDeclarationOP(Set<OWLObjectProperty> entities) {
		for (OWLObjectProperty elem : entities) {
			this.writer.writeStartElement(OWLXMLVocabulary.DECLARATION);
			renderEntity(elem);
			this.writer.writeEndElement();
		}
	}

	private void renderEntity(OWLClass entity) {
		this.writer.writeStartElement(OWLXMLVocabulary.CLASS);
		this.writer.writeIRIAttribute(entity.getIRI());
		this.writer.writeEndElement();
	}

	private void renderEntity(OWLDataProperty entity) {
		this.writer.writeStartElement(OWLXMLVocabulary.DATA_PROPERTY);
		this.writer.writeIRIAttribute(entity.getIRI());
		this.writer.writeEndElement();
	}

	private void renderEntity(OWLLiteral entity) {
		this.writer.writeStartElement(OWLXMLVocabulary.LITERAL);
		this.writer.writeDatatypeAttribute(entity.getDatatype());
		this.writer.writeEndElement();
	}

	private void renderEntity(OWLNamedIndividual entity) {
		this.writer.writeStartElement(OWLXMLVocabulary.NAMED_INDIVIDUAL);
		this.writer.writeIRIAttribute(entity.getIRI());
		this.writer.writeEndElement();
	}

	private void renderEntity(OWLObjectProperty entity) {
		this.writer.writeStartElement(OWLXMLVocabulary.OBJECT_PROPERTY);
		this.writer.writeIRIAttribute(entity.getIRI());
		this.writer.writeEndElement();
	}

	private void renderEquivalentClasses(Set<OWLClass> classSet) {
		this.writer.writeStartElement(OWLXMLVocabulary.EQUIVALENT_CLASSES);
		renderClassSet(classSet);
		this.writer.writeEndElement();
	}

	private void renderEquivalentDataProperties(Set<OWLDataProperty> propertySet) {
		this.writer
				.writeStartElement(OWLXMLVocabulary.EQUIVALENT_DATA_PROPERTIES);
		Set<OWLDataProperty> set = new TreeSet<OWLDataProperty>();
		set.addAll(propertySet);
		for (OWLDataProperty property : set) {
			renderEntity(property.asOWLDataProperty());
		}
		this.writer.writeEndElement();
	}

	private void renderEquivalentObjectPropertyExpressions(
			Set<OWLObjectPropertyExpression> propertySet) {
		this.writer
				.writeStartElement(OWLXMLVocabulary.EQUIVALENT_OBJECT_PROPERTIES);
		Set<OWLObjectPropertyExpression> set = new TreeSet<OWLObjectPropertyExpression>();
		set.addAll(propertySet);
		for (OWLObjectPropertyExpression propertyExpression : set) {
			renderEntity(propertyExpression.asOWLObjectProperty());
		}
		this.writer.writeEndElement();
	}

	private void renderObjectPropertyAssertion(OWLObjectProperty property,
			OWLNamedIndividual subject, OWLNamedIndividual object) {
		this.writer
				.writeStartElement(OWLXMLVocabulary.OBJECT_PROPERTY_ASSERTION);
		renderEntity(property);
		renderEntity(subject);
		renderEntity(object);
		this.writer.writeEndElement();
	}

	private void renderSubClassOf(OWLClass subClass, OWLClass superClass) {
		this.writer.writeStartElement(OWLXMLVocabulary.SUB_CLASS_OF);
		renderEntity(subClass);
		renderEntity(superClass);
		this.writer.writeEndElement();
	}

	private void renderSubDataPropertyOf(OWLDataProperty subProperty,
			OWLDataProperty superProperty) {
		this.writer.writeStartElement(OWLXMLVocabulary.SUB_DATA_PROPERTY_OF);
		renderEntity(subProperty);
		renderEntity(superProperty);
		this.writer.writeEndElement();
	}

	private void renderSubObjectPropertyOf(OWLObjectProperty subProperty,
			OWLObjectProperty superProperty) {
		this.writer.writeStartElement(OWLXMLVocabulary.SUB_OBJECT_PROPERTY_OF);
		renderEntity(subProperty);
		renderEntity(superProperty);
		this.writer.writeEndElement();
	}

	public void toXML(OutputStream out) throws OWLRendererException {
		if (out == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLOntology ontology = this.reasoner.getRootOntology();
		Writer writer = new OutputStreamWriter(out);
		this.writer = new OWLXMLWriter(writer, ontology);
		this.writer.startDocument(ontology);
		render();
		this.writer.endDocument();
	}

}
