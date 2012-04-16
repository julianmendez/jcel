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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.NodeFactory;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNodeSet;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class can translate a set of <code>OWLAxiom</code>s into a
 * set of integer axioms.
 * 
 * @see OWLAxiom
 * 
 * @author Julian Mendez
 */
public class Translator {

	private final AxiomTranslator axiomTranslator;
	private final IntegerOntologyObjectFactory factory;

	public Translator(OWLDataFactory dataFactory,
			IntegerOntologyObjectFactory factory) {
		if (dataFactory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		TranslationRepository repository = new TranslationRepository(
				dataFactory, factory.getEntityManager());
		this.factory = factory;
		ObjectPropertyExpressionTranslator objPropExprTranslator = new ObjectPropertyExpressionTranslator(
				this.factory.getDataTypeFactory(), repository);
		ClassExpressionTranslator cet = new ClassExpressionTranslator(
				objPropExprTranslator);

		this.axiomTranslator = new AxiomTranslator(cet, this.factory);
	}

	public AxiomTranslator getAxiomTranslator() {
		return this.axiomTranslator;
	}

	/**
	 * Returns the ontology object factory.
	 * 
	 * @return the ontology object factory
	 */
	public IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.factory;
	}

	public TranslationRepository getTranslationRepository() {
		return this.axiomTranslator.getTranslationRepository();
	}

	public OWLClass translateC(IntegerClass integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getTranslationRepository().getOWLClass(integerObject.getId());
	}

	public IntegerClass translateC(OWLClass owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOntologyObjectFactory().getDataTypeFactory().createClass(
				getAxiomTranslator().getTranslationRepository()
						.getId(owlObject));
	}

	public IntegerClassExpression translateCE(OWLClassExpression owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return owlObject.accept(getAxiomTranslator()
				.getClassExpressionTranslator());
	}

	public OWLNamedIndividual translateI(IntegerNamedIndividual integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getTranslationRepository().getOWLNamedIndividual(
				integerObject.getId());
	}

	public IntegerNamedIndividual translateI(OWLNamedIndividual owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getOntologyObjectFactory().getDataTypeFactory()
				.createNamedIndividual(
						getAxiomTranslator().getTranslationRepository().getId(
								owlObject));
	}

	public OWLObjectPropertyExpression translateOPE(
			IntegerObjectPropertyExpression integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (!(integerObject instanceof IntegerObjectProperty)) {
			TranslationException
					.newUnsupportedTranslationException(integerObject);
		}

		IntegerObjectProperty property = (IntegerObjectProperty) integerObject;
		return getTranslationRepository()
				.getOWLObjectProperty(property.getId());
	}

	public IntegerObjectPropertyExpression translateOPE(
			OWLObjectPropertyExpression owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return owlObject.accept(getAxiomTranslator()
				.getClassExpressionTranslator()
				.getObjectPropertyExpressionTranslator());
	}

	public Set<ComplexIntegerAxiom> translateSA(Set<OWLAxiom> owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<ComplexIntegerAxiom> ret = new HashSet<ComplexIntegerAxiom>();
		for (OWLAxiom axiom : owlObject) {
			ret.addAll(axiom.accept(axiomTranslator));
		}
		return ret;
	}

	public Set<IntegerClass> translateSC(Node<OWLClass> owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerClass> ret = new HashSet<IntegerClass>();
		for (OWLClass cls : owlObject) {
			ret.add(translateC(cls));
		}
		return Collections.unmodifiableSet(ret);
	}

	public Node<OWLClass> translateSC(Set<IntegerClass> integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClass> set = new HashSet<OWLClass>();
		for (IntegerClass cls : integerObject) {
			set.add(translateC(cls));
		}
		return NodeFactory.getOWLClassNode(set);
	}

	public Set<IntegerNamedIndividual> translateSI(
			Node<OWLNamedIndividual> owlObject) {
		Set<IntegerNamedIndividual> ret = new HashSet<IntegerNamedIndividual>();
		for (OWLNamedIndividual indiv : owlObject.getEntities()) {
			ret.add(translateI(indiv));
		}
		return Collections.unmodifiableSet(ret);
	}

	public Node<OWLNamedIndividual> translateSI(
			Set<IntegerNamedIndividual> integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLNamedIndividual> set = new HashSet<OWLNamedIndividual>();
		for (IntegerNamedIndividual indiv : integerObject) {
			set.add(translateI(indiv));
		}
		return NodeFactory.getOWLNamedIndividualNode(set);
	}

	public Set<IntegerObjectPropertyExpression> translateSOPE(
			Node<OWLObjectPropertyExpression> owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerObjectPropertyExpression> ret = new HashSet<IntegerObjectPropertyExpression>();
		for (OWLObjectPropertyExpression prop : owlObject.getEntities()) {
			ret.add(translateOPE(prop));
		}
		return Collections.unmodifiableSet(ret);
	}

	public Node<OWLObjectPropertyExpression> translateSOPE(
			Set<IntegerObjectPropertyExpression> integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLObjectPropertyExpression> set = new HashSet<OWLObjectPropertyExpression>();
		for (IntegerObjectPropertyExpression prop : integerObject) {
			set.add(translateOPE(prop));
		}
		return NodeFactory.getOWLObjectPropertyNode(set);
	}

	public Set<Set<IntegerClass>> translateSSC(NodeSet<OWLClass> owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<IntegerClass>> ret = new HashSet<Set<IntegerClass>>();
		for (Node<OWLClass> node : owlObject.getNodes()) {
			ret.add(translateSC(node));
		}
		return Collections.unmodifiableSet(ret);
	}

	public NodeSet<OWLClass> translateSSC(Set<Set<IntegerClass>> integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Node<OWLClass>> setOfNodes = new HashSet<Node<OWLClass>>();
		for (Set<IntegerClass> intSet : integerObject) {
			setOfNodes.add(translateSC(intSet));
		}
		return new OWLClassNodeSet(setOfNodes);
	}

	public Set<Set<IntegerNamedIndividual>> translateSSI(
			NodeSet<OWLNamedIndividual> owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<IntegerNamedIndividual>> ret = new HashSet<Set<IntegerNamedIndividual>>();
		for (Node<OWLNamedIndividual> node : owlObject.getNodes()) {
			ret.add(translateSI(node));
		}
		return Collections.unmodifiableSet(ret);
	}

	public NodeSet<OWLNamedIndividual> translateSSI(
			Set<Set<IntegerNamedIndividual>> integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Node<OWLNamedIndividual>> setOfNodes = new HashSet<Node<OWLNamedIndividual>>();
		for (Set<IntegerNamedIndividual> intSet : integerObject) {
			setOfNodes.add(translateSI(intSet));
		}
		return new OWLNamedIndividualNodeSet(setOfNodes);
	}

	public Set<Set<IntegerObjectPropertyExpression>> translateSSOPE(
			NodeSet<OWLObjectPropertyExpression> owlObject) {
		if (owlObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<IntegerObjectPropertyExpression>> ret = new HashSet<Set<IntegerObjectPropertyExpression>>();
		for (Node<OWLObjectPropertyExpression> node : owlObject.getNodes()) {
			ret.add(translateSOPE(node));
		}
		return Collections.unmodifiableSet(ret);
	}

	public NodeSet<OWLObjectPropertyExpression> translateSSOPE(
			Set<Set<IntegerObjectPropertyExpression>> integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Node<OWLObjectPropertyExpression>> setOfNodes = new HashSet<Node<OWLObjectPropertyExpression>>();
		for (Set<IntegerObjectPropertyExpression> intSet : integerObject) {
			setOfNodes.add(translateSOPE(intSet));
		}
		return new OWLObjectPropertyNodeSet(setOfNodes);
	}

}
