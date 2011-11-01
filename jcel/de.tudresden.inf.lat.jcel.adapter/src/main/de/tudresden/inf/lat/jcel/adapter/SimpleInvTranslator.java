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

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 * An object of this class is used for the translation from the OWL API 3 to the
 * OWL API 2.
 * 
 * @author Julian Mendez
 */
public class SimpleInvTranslator {

	private org.semanticweb.owl.model.OWLDataFactory dataFactory = null;

	public SimpleInvTranslator(org.semanticweb.owl.model.OWLDataFactory manager) {
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.dataFactory = manager;
	}

	public org.semanticweb.owl.model.OWLDataFactory getOWLDataFactory() {
		return this.dataFactory;
	}

	public org.semanticweb.owl.model.OWLClass translate(OWLClass cls) {
		if (cls == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLClass(cls.getIRI().toURI());
	}

	public org.semanticweb.owl.model.OWLDataProperty translate(
			OWLDataProperty dataProperty) {
		if (dataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLDataProperty(
				dataProperty.getIRI().toURI());
	}

	public org.semanticweb.owl.model.OWLIndividual translate(
			OWLNamedIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory()
				.getOWLIndividual(individual.getIRI().toURI());
	}

	public org.semanticweb.owl.model.OWLObjectProperty translate(
			OWLObjectProperty objectProperty) {
		if (objectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOWLDataFactory().getOWLObjectProperty(
				objectProperty.getIRI().toURI());
	}

	public Set<org.semanticweb.owl.model.OWLClass> translateToClasses(
			Node<OWLClass> node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<org.semanticweb.owl.model.OWLClass> ret = new HashSet<org.semanticweb.owl.model.OWLClass>();
		Set<OWLClass> set = node.getEntities();
		for (OWLClass elem : set) {
			ret.add(translate(elem));
		}
		return ret;
	}

	public Set<Set<org.semanticweb.owl.model.OWLClass>> translateToClasses(
			NodeSet<OWLClass> nodeSet) {
		if (nodeSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<org.semanticweb.owl.model.OWLClass>> ret = new HashSet<Set<org.semanticweb.owl.model.OWLClass>>();
		Set<Node<OWLClass>> setOfSets = nodeSet.getNodes();
		for (Node<OWLClass> node : setOfSets) {
			Set<org.semanticweb.owl.model.OWLClass> current = new HashSet<org.semanticweb.owl.model.OWLClass>();
			Set<OWLClass> set = node.getEntities();
			for (OWLClass elem : set) {
				current.add(translate(elem));
			}
			ret.add(current);
		}
		return ret;
	}

	public Set<org.semanticweb.owl.model.OWLDataProperty> translateToDataProperties(
			Node<OWLDataProperty> node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<org.semanticweb.owl.model.OWLDataProperty> ret = new HashSet<org.semanticweb.owl.model.OWLDataProperty>();
		Set<OWLDataProperty> set = node.getEntities();
		for (OWLDataProperty elem : set) {
			ret.add(translate(elem));
		}
		return ret;
	}

	public Set<Set<org.semanticweb.owl.model.OWLDataProperty>> translateToDataProperties(
			NodeSet<OWLDataProperty> nodeSet) {
		if (nodeSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<org.semanticweb.owl.model.OWLDataProperty>> ret = new HashSet<Set<org.semanticweb.owl.model.OWLDataProperty>>();
		Set<Node<OWLDataProperty>> setOfSets = nodeSet.getNodes();
		for (Node<OWLDataProperty> node : setOfSets) {
			Set<org.semanticweb.owl.model.OWLDataProperty> current = new HashSet<org.semanticweb.owl.model.OWLDataProperty>();
			Set<OWLDataProperty> set = node.getEntities();
			for (OWLDataProperty elem : set) {
				current.add(translate(elem));
			}
			ret.add(current);
		}
		return ret;
	}

	public Set<org.semanticweb.owl.model.OWLIndividual> translateToIndividuals(
			NodeSet<OWLNamedIndividual> nodeSet) {
		if (nodeSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<org.semanticweb.owl.model.OWLIndividual> ret = new HashSet<org.semanticweb.owl.model.OWLIndividual>();
		Set<OWLNamedIndividual> set = nodeSet.getFlattened();
		for (OWLNamedIndividual elem : set) {
			ret.add(translate(elem));
		}
		return ret;
	}

	public Set<org.semanticweb.owl.model.OWLObjectProperty> translateToObjectProperties(
			Node<OWLObjectProperty> node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<org.semanticweb.owl.model.OWLObjectProperty> ret = new HashSet<org.semanticweb.owl.model.OWLObjectProperty>();
		Set<OWLObjectProperty> set = node.getEntities();
		for (OWLObjectProperty elem : set) {
			ret.add(translate(elem));
		}
		return ret;
	}

	public Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> translateToObjectProperties(
			NodeSet<OWLObjectProperty> nodeSet) {
		if (nodeSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<org.semanticweb.owl.model.OWLObjectProperty>> ret = new HashSet<Set<org.semanticweb.owl.model.OWLObjectProperty>>();
		Set<Node<OWLObjectProperty>> setOfSets = nodeSet.getNodes();
		for (Node<OWLObjectProperty> node : setOfSets) {
			Set<org.semanticweb.owl.model.OWLObjectProperty> current = new HashSet<org.semanticweb.owl.model.OWLObjectProperty>();
			Set<OWLObjectProperty> set = node.getEntities();
			for (OWLObjectProperty elem : set) {
				current.add(translate(elem));
			}
			ret.add(current);
		}
		return ret;
	}

}
