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

package de.tudresden.inf.lat.jcel.protege.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.TreeSet;

import org.coode.xml.XMLWriter;
import org.coode.xml.XMLWriterImpl;
import org.coode.xml.XMLWriterNamespaceManager;

/**
 * This class makes an XML representation of the inferred data in an
 * org.semanticweb.owl.inference.OWLReasoner.
 * 
 * @author Julian Mendez
 */
public class OWLReasonerXMLOutput {

	private static final String attIRI = "IRI";
	private static final String CLASS = "Class";
	private static final String CLASS_ASSERTION = "ClassAssertion";
	private static final String DECLARATION = "Declaration";
	private static final String EQUIVALENT_CLASSES = "EquivalentClasses";
	private static final String EQUIVALENT_OBJECT_PROPERTIES = "EquivalentObjectProperties";
	private static final String NAMED_INDIVIDUAL = "NamedIndividual";
	private static final String OBJECT_PROPERTY = "ObjectProperty";
	private static final String OBJECT_PROPERTY_ASSERTION = "ObjectPropertyAssertion";
	private static final String ONTOLOGY = "Ontology";
	private static final String SUB_CLASS_OF = "SubClassOf";
	private static final String SUB_OBJECT_PROPERTY_OF = "SubObjectPropertyOf";
	private static final String xmlns = "http://www.w3.org/2002/07/owl#";
	private static final String xmlnsRdfKey = "rdf";
	private static final String xmlnsRdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String xmlnsRdfsKey = "rdfs";
	private static final String xmlnsRdfsPrefix = "http://www.w3.org/2000/01/rdf-schema#";
	private static final String xmlnsXmlKey = "xml";
	private static final String xmlnsXmlPrefix = "http://www.w3.org/XML/1998/namespace";
	private static final String xmlnsXsdKey = "xsd";
	private static final String xmlnsXsdPrefix = "http://www.w3.org/2001/XMLSchema#";

	private org.semanticweb.owl.inference.OWLReasoner reasoner = null;
	private XMLWriter writer = null;

	public OWLReasonerXMLOutput(
			org.semanticweb.owl.inference.OWLReasoner reasoner) {
		if (reasoner == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.reasoner = reasoner;
	}

	private Set<org.semanticweb.owl.model.OWLClass> flattenClasses(
			Set<Set<org.semanticweb.owl.model.OWLClass>> setOfSets) {
		Set<org.semanticweb.owl.model.OWLClass> ret = new TreeSet<org.semanticweb.owl.model.OWLClass>();
		for (Set<org.semanticweb.owl.model.OWLClass> set : setOfSets) {
			ret.addAll(set);
		}
		return ret;
	}

	private Set<org.semanticweb.owl.model.OWLObjectProperty> flattenProperties(
			Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> setOfSets) {
		Set<org.semanticweb.owl.model.OWLObjectProperty> ret = new TreeSet<org.semanticweb.owl.model.OWLObjectProperty>();
		for (Set<org.semanticweb.owl.model.OWLObjectProperty> set : setOfSets) {
			ret.addAll(set);
		}
		return ret;
	}

	private void render()
			throws org.semanticweb.owl.inference.OWLReasonerException {
		try {

			org.semanticweb.owl.model.OWLOntology ontology = this.reasoner
					.getLoadedOntologies().iterator().next();
			Set<org.semanticweb.owl.model.OWLClass> classSet = new TreeSet<org.semanticweb.owl.model.OWLClass>();
			classSet.addAll(ontology.getClassesInSignature());
			Set<org.semanticweb.owl.model.OWLObjectProperty> propertySet = new TreeSet<org.semanticweb.owl.model.OWLObjectProperty>();
			propertySet.addAll(ontology.getObjectPropertiesInSignature());
			Set<org.semanticweb.owl.model.OWLIndividual> individualSet = new TreeSet<org.semanticweb.owl.model.OWLIndividual>();
			individualSet.addAll(ontology.getIndividualsInSignature());

			renderDeclaration(classSet);
			renderDeclaration(propertySet);
			renderDeclaration(individualSet);

			Set<org.semanticweb.owl.model.OWLClass> classesToVisit = new TreeSet<org.semanticweb.owl.model.OWLClass>();
			classesToVisit.addAll(classSet);
			while (!classesToVisit.isEmpty()) {
				org.semanticweb.owl.model.OWLClass cls = classesToVisit
						.iterator().next();
				classesToVisit.remove(cls);
				Set<org.semanticweb.owl.model.OWLClass> equivClasses = this.reasoner
						.getEquivalentClasses(cls);
				renderEquivalentClasses(equivClasses);
				classesToVisit.removeAll(equivClasses);
			}

			Set<org.semanticweb.owl.model.OWLObjectProperty> propertiesToVisit = new TreeSet<org.semanticweb.owl.model.OWLObjectProperty>();
			propertiesToVisit.addAll(propertySet);
			while (!propertiesToVisit.isEmpty()) {
				org.semanticweb.owl.model.OWLObjectProperty property = propertiesToVisit
						.iterator().next();
				propertiesToVisit.remove(property);
				Set<org.semanticweb.owl.model.OWLObjectProperty> equivProperties = this.reasoner
						.getEquivalentProperties(property);
				renderEquivalentObjectProperties(equivProperties);
				propertiesToVisit.removeAll(equivProperties);
			}

			for (org.semanticweb.owl.model.OWLClass subClass : classSet) {
				Set<org.semanticweb.owl.model.OWLClass> superClasses = new TreeSet<org.semanticweb.owl.model.OWLClass>();
				superClasses.addAll(flattenClasses(this.reasoner
						.getSuperClasses(subClass)));
				for (org.semanticweb.owl.model.OWLClass superClass : superClasses) {
					renderSubClassOf(subClass, superClass);
				}
			}

			for (org.semanticweb.owl.model.OWLObjectProperty subProperty : propertySet) {
				Set<org.semanticweb.owl.model.OWLObjectProperty> superProperties = new TreeSet<org.semanticweb.owl.model.OWLObjectProperty>();
				superProperties.addAll(flattenProperties(this.reasoner
						.getSuperProperties(subProperty)));
				for (org.semanticweb.owl.model.OWLObjectProperty superProperty : superProperties) {
					renderSubObjectPropertyOf(subProperty, superProperty);
				}
			}

			for (org.semanticweb.owl.model.OWLClass cls : classSet) {
				Set<org.semanticweb.owl.model.OWLIndividual> instances = new TreeSet<org.semanticweb.owl.model.OWLIndividual>();
				instances.addAll(this.reasoner.getIndividuals(cls, true));
				for (org.semanticweb.owl.model.OWLIndividual individual : instances) {
					renderClassAssertion(cls, individual);
				}
			}

			for (org.semanticweb.owl.model.OWLObjectProperty property : propertySet) {
				for (org.semanticweb.owl.model.OWLIndividual individual : individualSet) {
					Set<org.semanticweb.owl.model.OWLIndividual> propertyValues = new TreeSet<org.semanticweb.owl.model.OWLIndividual>();
					propertyValues.addAll(this.reasoner.getRelatedIndividuals(
							individual, property.asOWLObjectProperty()));
					for (org.semanticweb.owl.model.OWLIndividual otherIndividual : propertyValues) {
						renderObjectPropertyAssertion(property, individual,
								otherIndividual);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	private void renderClassAssertion(org.semanticweb.owl.model.OWLClass cls,
			org.semanticweb.owl.model.OWLIndividual individual)
			throws IOException {
		this.writer.writeStartElement(CLASS_ASSERTION);
		renderEntity(cls);
		renderEntity(individual);
		this.writer.writeEndElement();

	}

	private void renderDeclaration(
			Set<? extends org.semanticweb.owl.model.OWLEntity> entities)
			throws IOException {
		for (org.semanticweb.owl.model.OWLEntity elem : entities) {
			this.writer.writeStartElement(DECLARATION);
			renderEntity(elem);
			this.writer.writeEndElement();
		}
	}

	private void renderEntity(org.semanticweb.owl.model.OWLEntity entity)
			throws IOException {
		if (entity instanceof org.semanticweb.owl.model.OWLClass) {
			this.writer.writeStartElement(CLASS);
		} else if (entity instanceof org.semanticweb.owl.model.OWLObjectProperty) {
			this.writer.writeStartElement(OBJECT_PROPERTY);
		} else if (entity instanceof org.semanticweb.owl.model.OWLIndividual) {
			this.writer.writeStartElement(NAMED_INDIVIDUAL);
		} else {
			throw new IllegalStateException("Entity cannot be rendered : '"
					+ entity + "'.");
		}
		this.writer.writeAttribute(attIRI, entity.getURI().toString());
		this.writer.writeEndElement();
	}

	private void renderEntitySet(
			Set<? extends org.semanticweb.owl.model.OWLEntity> entitySet)
			throws IOException {
		Set<org.semanticweb.owl.model.OWLEntity> set = new TreeSet<org.semanticweb.owl.model.OWLEntity>();
		set.addAll(entitySet);
		for (org.semanticweb.owl.model.OWLEntity entity : set) {
			renderEntity(entity);
		}
	}

	private void renderEquivalentClasses(
			Set<org.semanticweb.owl.model.OWLClass> classSet)
			throws IOException {
		this.writer.writeStartElement(EQUIVALENT_CLASSES);
		renderEntitySet(classSet);
		this.writer.writeEndElement();
	}

	private void renderEquivalentObjectProperties(
			Set<org.semanticweb.owl.model.OWLObjectProperty> propertySet)
			throws IOException {
		this.writer.writeStartElement(EQUIVALENT_OBJECT_PROPERTIES);
		renderEntitySet(propertySet);
		this.writer.writeEndElement();
	}

	private void renderObjectPropertyAssertion(
			org.semanticweb.owl.model.OWLObjectProperty property,
			org.semanticweb.owl.model.OWLIndividual subject,
			org.semanticweb.owl.model.OWLIndividual object) throws IOException {
		this.writer.writeStartElement(OBJECT_PROPERTY_ASSERTION);
		renderEntity(property);
		renderEntity(subject);
		renderEntity(object);
		this.writer.writeEndElement();
	}

	private void renderSubClassOf(org.semanticweb.owl.model.OWLClass subClass,
			org.semanticweb.owl.model.OWLClass superClass) throws IOException {
		this.writer.writeStartElement(SUB_CLASS_OF);
		renderEntity(subClass);
		renderEntity(superClass);
		this.writer.writeEndElement();
	}

	private void renderSubObjectPropertyOf(
			org.semanticweb.owl.model.OWLObjectProperty subProperty,
			org.semanticweb.owl.model.OWLObjectProperty superProperty)
			throws IOException {
		this.writer.writeStartElement(SUB_OBJECT_PROPERTY_OF);
		renderEntity(subProperty);
		renderEntity(superProperty);
		this.writer.writeEndElement();
	}

	public void toXML(OutputStream out) {
		if (out == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		try {
			XMLWriterNamespaceManager manager = new XMLWriterNamespaceManager(
					xmlns);
			OutputStreamWriter output = new OutputStreamWriter(out);

			manager.setPrefix(xmlnsRdfsKey, xmlnsRdfsPrefix);
			manager.setPrefix(xmlnsXsdKey, xmlnsXsdPrefix);
			manager.setPrefix(xmlnsRdfKey, xmlnsRdfPrefix);
			manager.setPrefix(xmlnsXmlKey, xmlnsXmlPrefix);

			this.writer = new XMLWriterImpl(output, manager);
			this.writer.setEncoding("UTF-8");
			this.writer.setWrapAttributes(false);
			this.writer.startDocument(ONTOLOGY);
			render();
			output.flush();
			this.writer.endDocument();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (org.semanticweb.owl.inference.OWLReasonerException e) {
			throw new RuntimeException(e);
		}
	}
}
