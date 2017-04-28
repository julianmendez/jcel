/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
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

	public Translator(OWLDataFactory dataFactory, IntegerOntologyObjectFactory factory) {
		Objects.requireNonNull(dataFactory);
		Objects.requireNonNull(factory);
		TranslationRepository repository = new TranslationRepository(dataFactory, factory.getEntityManager());
		this.factory = factory;
		ObjectPropertyExpressionTranslator objPropExprTranslator = new ObjectPropertyExpressionTranslator(
				this.factory.getDataTypeFactory(), repository);
		ClassExpressionTranslator cet = new ClassExpressionTranslator(objPropExprTranslator);

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
		Objects.requireNonNull(integerObject);
		return getTranslationRepository().getOWLClass(integerObject.getId());
	}

	public IntegerClass translateC(OWLClass owlObject) {
		Objects.requireNonNull(owlObject);
		return getOntologyObjectFactory().getDataTypeFactory()
				.createClass(getAxiomTranslator().getTranslationRepository().getId(owlObject));
	}

	public IntegerClassExpression translateCE(OWLClassExpression owlObject) {
		Objects.requireNonNull(owlObject);
		return owlObject.accept(getAxiomTranslator().getClassExpressionTranslator());
	}

	public OWLNamedIndividual translateI(IntegerNamedIndividual integerObject) {
		Objects.requireNonNull(integerObject);
		return getTranslationRepository().getOWLNamedIndividual(integerObject.getId());
	}

	public IntegerNamedIndividual translateI(OWLNamedIndividual owlObject) {
		Objects.requireNonNull(owlObject);
		return getOntologyObjectFactory().getDataTypeFactory()
				.createNamedIndividual(getAxiomTranslator().getTranslationRepository().getId(owlObject));
	}

	public OWLObjectPropertyExpression translateOPE(IntegerObjectPropertyExpression integerObject) {
		Objects.requireNonNull(integerObject);
		if (!(integerObject instanceof IntegerObjectProperty)) {
			TranslationException.newUnsupportedTranslationException(integerObject);
		}

		IntegerObjectProperty property = (IntegerObjectProperty) integerObject;
		return getTranslationRepository().getOWLObjectProperty(property.getId());
	}

	public IntegerObjectPropertyExpression translateOPE(OWLObjectPropertyExpression owlObject) {
		Objects.requireNonNull(owlObject);
		return owlObject
				.accept(getAxiomTranslator().getClassExpressionTranslator().getObjectPropertyExpressionTranslator());
	}

	public Set<ComplexIntegerAxiom> translateSA(Set<OWLAxiom> owlObject) {
		Objects.requireNonNull(owlObject);
		Set<ComplexIntegerAxiom> ret = new HashSet<>();
		owlObject.forEach(axiom -> {
			ret.addAll(axiom.accept(axiomTranslator));
		});
		return ret;
	}

	public Set<IntegerClass> translateSC(Node<OWLClass> owlObject) {
		Objects.requireNonNull(owlObject);
		Set<IntegerClass> ret = new HashSet<>();
		owlObject.forEach(cls -> {
			ret.add(translateC(cls));
		});
		return Collections.unmodifiableSet(ret);
	}

	public Node<OWLClass> translateSC(Set<IntegerClass> integerObject) {
		Objects.requireNonNull(integerObject);
		Set<OWLClass> set = new HashSet<>();
		integerObject.forEach(cls -> {
			set.add(translateC(cls));
		});
		return NodeFactory.getOWLClassNode(set);
	}

	public Set<IntegerNamedIndividual> translateSI(Node<OWLNamedIndividual> owlObject) {
		Set<IntegerNamedIndividual> ret = new HashSet<>();
		owlObject.getEntities().forEach(indiv -> {
			ret.add(translateI(indiv));
		});
		return Collections.unmodifiableSet(ret);
	}

	public Node<OWLNamedIndividual> translateSI(Set<IntegerNamedIndividual> integerObject) {
		Objects.requireNonNull(integerObject);
		Set<OWLNamedIndividual> set = new HashSet<>();
		integerObject.forEach(indiv -> {
			set.add(translateI(indiv));
		});
		return NodeFactory.getOWLNamedIndividualNode(set);
	}

	public Set<IntegerObjectPropertyExpression> translateSOPE(Node<OWLObjectPropertyExpression> owlObject) {
		Objects.requireNonNull(owlObject);
		Set<IntegerObjectPropertyExpression> ret = new HashSet<>();
		owlObject.getEntities().forEach(prop -> {
			ret.add(translateOPE(prop));
		});
		return Collections.unmodifiableSet(ret);
	}

	public Node<OWLObjectPropertyExpression> translateSOPE(Set<IntegerObjectPropertyExpression> integerObject) {
		Objects.requireNonNull(integerObject);
		Set<OWLObjectPropertyExpression> set = new HashSet<>();
		integerObject.forEach(prop -> {
			set.add(translateOPE(prop));
		});
		return NodeFactory.getOWLObjectPropertyNode(set);
	}

	public Set<Set<IntegerClass>> translateSSC(NodeSet<OWLClass> owlObject) {
		Objects.requireNonNull(owlObject);
		Set<Set<IntegerClass>> ret = new HashSet<>();
		owlObject.getNodes().forEach(node -> {
			ret.add(translateSC(node));
		});
		return Collections.unmodifiableSet(ret);
	}

	public NodeSet<OWLClass> translateSSC(Set<Set<IntegerClass>> integerObject) {
		Objects.requireNonNull(integerObject);
		Set<Node<OWLClass>> setOfNodes = new HashSet<>();
		integerObject.forEach(intSet -> {
			setOfNodes.add(translateSC(intSet));
		});
		return new OWLClassNodeSet(setOfNodes);
	}

	public Set<Set<IntegerNamedIndividual>> translateSSI(NodeSet<OWLNamedIndividual> owlObject) {
		Objects.requireNonNull(owlObject);
		Set<Set<IntegerNamedIndividual>> ret = new HashSet<>();
		owlObject.getNodes().forEach(node -> {
			ret.add(translateSI(node));
		});
		return Collections.unmodifiableSet(ret);
	}

	public NodeSet<OWLNamedIndividual> translateSSI(Set<Set<IntegerNamedIndividual>> integerObject) {
		Objects.requireNonNull(integerObject);
		Set<Node<OWLNamedIndividual>> setOfNodes = new HashSet<>();
		integerObject.forEach(intSet -> {
			setOfNodes.add(translateSI(intSet));
		});
		return new OWLNamedIndividualNodeSet(setOfNodes);
	}

	public Set<Set<IntegerObjectPropertyExpression>> translateSSOPE(NodeSet<OWLObjectPropertyExpression> owlObject) {
		Objects.requireNonNull(owlObject);
		Set<Set<IntegerObjectPropertyExpression>> ret = new HashSet<>();
		owlObject.getNodes().forEach(node -> {
			ret.add(translateSOPE(node));
		});
		return Collections.unmodifiableSet(ret);
	}

	public NodeSet<OWLObjectPropertyExpression> translateSSOPE(
			Set<Set<IntegerObjectPropertyExpression>> integerObject) {
		Objects.requireNonNull(integerObject);
		Set<Node<OWLObjectPropertyExpression>> setOfNodes = new HashSet<>();
		integerObject.forEach(intSet -> {
			setOfNodes.add(translateSOPE(intSet));
		});
		return new OWLObjectPropertyNodeSet(setOfNodes);
	}

}
