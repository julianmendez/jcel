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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 * An object of this class is used for the translation from the OWL API 2 to the
 * OWL API 3.
 * 
 * @author Julian Mendez
 */
public class SimpleTranslator {

	private AxiomTranslator axiomTranslator = null;
	private ClassExpressionTranslator classExpressionTranslator = null;
	private OWLDataFactory dataFactory = null;
	private EntityTranslator entityTranslator = null;

	public SimpleTranslator(OWLDataFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.dataFactory = factory;
		this.axiomTranslator = new AxiomTranslator(this);
		this.classExpressionTranslator = new ClassExpressionTranslator(this);
		this.entityTranslator = new EntityTranslator(this);
	}

	public OWLDataFactory getOWLDataFactory() {
		return this.dataFactory;
	}

	public Set<org.semanticweb.owl.model.OWLAxiom> processImportsDeclarations(
			org.semanticweb.owl.model.OWLOntologyManager manager,
			Set<org.semanticweb.owl.model.OWLAxiom> axiomSet) {
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<org.semanticweb.owl.model.OWLAxiom> ret = new HashSet<org.semanticweb.owl.model.OWLAxiom>();
		Set<org.semanticweb.owl.model.OWLImportsDeclaration> toVisit = new HashSet<org.semanticweb.owl.model.OWLImportsDeclaration>();
		Set<org.semanticweb.owl.model.OWLImportsDeclaration> visited = new HashSet<org.semanticweb.owl.model.OWLImportsDeclaration>();
		for (org.semanticweb.owl.model.OWLAxiom axiom : axiomSet) {
			if (axiom instanceof org.semanticweb.owl.model.OWLImportsDeclaration) {
				toVisit
						.add((org.semanticweb.owl.model.OWLImportsDeclaration) axiom);
			} else {
				ret.add(axiom);
			}
		}
		while (!toVisit.isEmpty()) {
			org.semanticweb.owl.model.OWLImportsDeclaration declaration = toVisit
					.iterator().next();
			visited.add(declaration);
			org.semanticweb.owl.model.OWLOntology ontology = manager
					.getOntology(declaration.getImportedOntologyURI());
			if (ontology != null) {
				Set<org.semanticweb.owl.model.OWLAxiom> currentAxiomSet = ontology
						.getAxioms();
				for (org.semanticweb.owl.model.OWLAxiom axiom : currentAxiomSet) {
					if (axiom instanceof org.semanticweb.owl.model.OWLImportsDeclaration) {
						toVisit
								.add((org.semanticweb.owl.model.OWLImportsDeclaration) axiom);
					} else {
						ret.add(axiom);
					}
				}
			}
			toVisit.removeAll(visited);
		}
		return ret;
	}

	public List<OWLObjectPropertyExpression> translate(
			List<org.semanticweb.owl.model.OWLObjectPropertyExpression> propertyChain) {
		if (propertyChain == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<OWLObjectPropertyExpression> ret = new ArrayList<OWLObjectPropertyExpression>();
		for (org.semanticweb.owl.model.OWLObjectPropertyExpression property : propertyChain) {
			ret.add(translate(property));
		}
		return ret;
	}

	public OWLAxiom translate(org.semanticweb.owl.model.OWLAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return axiom.accept(this.axiomTranslator);
	}

	public OWLClass translate(org.semanticweb.owl.model.OWLClass cls) {
		if (cls == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLClass(IRI.create(cls.getURI()));
	}

	public OWLLiteral translate(org.semanticweb.owl.model.OWLConstant object) {
		if (object == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLStringLiteral(object.getLiteral());
	}

	public OWLDataProperty translate(
			org.semanticweb.owl.model.OWLDataProperty dataProperty) {
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLDataProperty(
				IRI.create(dataProperty.getURI()));
	}

	public OWLDataPropertyExpression translate(
			org.semanticweb.owl.model.OWLDataPropertyExpression property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return translate(property.asOWLDataProperty());
	}

	public OWLDataRange translate(org.semanticweb.owl.model.OWLDataRange range) {
		if (range == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new TranslationException("Unsupported translation : OWLDataRange");
	}

	public OWLDatatype translate(org.semanticweb.owl.model.OWLDataType dataType) {
		if (dataType == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new TranslationException("Unsupported translation : OWLDataType");
	}

	public OWLClassExpression translate(
			org.semanticweb.owl.model.OWLDescription description) {
		if (description == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return description.accept(this.classExpressionTranslator);
	}

	public OWLEntity translate(org.semanticweb.owl.model.OWLEntity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return entity.accept(this.entityTranslator);
	}

	public OWLNamedIndividual translate(
			org.semanticweb.owl.model.OWLIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLNamedIndividual(
				IRI.create(individual.getURI()));
	}

	public OWLObjectProperty translate(
			org.semanticweb.owl.model.OWLObjectProperty property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLObjectProperty(
				IRI.create(property.getURI()));
	}

	public OWLObjectPropertyExpression translate(
			org.semanticweb.owl.model.OWLObjectPropertyExpression property) {
		if (property == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return translate(property.asOWLObjectProperty());
	}

	public Set<OWLAxiom> translateToAxioms(
			Set<org.semanticweb.owl.model.OWLAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLAxiom> ret = new HashSet<OWLAxiom>();
		for (org.semanticweb.owl.model.OWLAxiom axiom : axiomSet) {
			OWLAxiom newAxiom = translate(axiom);
			if (newAxiom != null) {
				ret.add(newAxiom);
			}
		}
		return ret;
	}

	public Set<OWLClassExpression> translateToClassExpressions(
			Set<org.semanticweb.owl.model.OWLDescription> descriptionSet) {
		if (descriptionSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClassExpression> ret = new HashSet<OWLClassExpression>();
		for (org.semanticweb.owl.model.OWLDescription description : descriptionSet) {
			ret.add(translate(description));
		}
		return ret;
	}

	public Set<OWLDataPropertyExpression> translateToDataPropertyExpressions(
			Set<org.semanticweb.owl.model.OWLDataPropertyExpression> propertyExpressionSet) {
		if (propertyExpressionSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLDataPropertyExpression> ret = new HashSet<OWLDataPropertyExpression>();
		for (org.semanticweb.owl.model.OWLDataPropertyExpression propertyExpression : propertyExpressionSet) {
			ret.add(translate(propertyExpression));
		}
		return ret;
	}

	public Set<OWLNamedIndividual> translateToNamedIndividuals(
			Set<org.semanticweb.owl.model.OWLIndividual> individualSet) {
		if (individualSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLNamedIndividual> ret = new HashSet<OWLNamedIndividual>();
		for (org.semanticweb.owl.model.OWLIndividual individual : individualSet) {
			ret.add(translate(individual));
		}
		return ret;
	}

	public Set<OWLObjectPropertyExpression> translateToObjectPropertyExpressions(
			Set<org.semanticweb.owl.model.OWLObjectPropertyExpression> propertyExpressionSet) {
		if (propertyExpressionSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLObjectPropertyExpression> ret = new HashSet<OWLObjectPropertyExpression>();
		for (org.semanticweb.owl.model.OWLObjectPropertyExpression propertyExpression : propertyExpressionSet) {
			ret.add(translate(propertyExpression));
		}
		return ret;
	}
}
