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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.NodeFactory;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNodeSet;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class can translate a set of <code>OWLAxiom</code>s into a
 * set of <code>IntegerAxiom</code>s.
 * 
 * @author Julian Mendez
 */
public class Translator {

	private AxiomTranslator axiomTranslator;
	private Set<ComplexIntegerAxiom> ontology;
	private TranslationRepository repository;

	public Translator(OWLOntology rootOntology) {
		if (rootOntology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.repository = new TranslationRepository(rootOntology);
		Map<OWLObjectProperty, Integer> objectPropertyMap = createPropertyMap(repository);
		Map<OWLClass, Integer> classMap = createClassMap(repository);
		Map<OWLNamedIndividual, Integer> individualMap = createIndividualMap(
				classMap.size(), repository);
		Map<OWLDataProperty, Integer> dataPropertyMap = createDataPropertyMap(repository);
		Map<OWLLiteral, Integer> literalMap = createLiteralMap(repository);
		ObjectPropertyExpressionTranslator obet = new ObjectPropertyExpressionTranslator(
				objectPropertyMap, dataPropertyMap);
		ClassExpressionTranslator cet = new ClassExpressionTranslator(classMap,
				individualMap, literalMap, obet);

		this.axiomTranslator = new AxiomTranslator(cet);

		Set<OWLAxiom> owlAxiomSet = rootOntology.getAxioms();
		for (OWLOntology ont : rootOntology.getImportsClosure()) {
			owlAxiomSet.addAll(ont.getAxioms());
		}

		this.ontology = translateA(owlAxiomSet);
	}

	private Map<OWLClass, Integer> createClassMap(
			TranslationRepository repository) {
		Map<OWLClass, Integer> ret = new HashMap<OWLClass, Integer>();
		Iterator<OWLClass> it = repository.getOWLClassList().iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLClass current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLDataProperty, Integer> createDataPropertyMap(
			TranslationRepository repository) {
		Map<OWLDataProperty, Integer> ret = new HashMap<OWLDataProperty, Integer>();
		Iterator<OWLDataProperty> it = repository.getOWLDataPropertyList()
				.iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLDataProperty current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLNamedIndividual, Integer> createIndividualMap(
			Integer offset, TranslationRepository repository) {
		Map<OWLNamedIndividual, Integer> ret = new HashMap<OWLNamedIndividual, Integer>();
		Iterator<OWLNamedIndividual> it = repository
				.getOWLNamedIndividualList().iterator();
		for (int index = offset; it.hasNext(); index++) {
			OWLNamedIndividual current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLLiteral, Integer> createLiteralMap(
			TranslationRepository repository) {
		Map<OWLLiteral, Integer> ret = new HashMap<OWLLiteral, Integer>();
		Iterator<OWLLiteral> it = repository.getOWLLiteralList().iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLLiteral current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLObjectProperty, Integer> createPropertyMap(
			TranslationRepository repository) {
		Map<OWLObjectProperty, Integer> ret = new HashMap<OWLObjectProperty, Integer>();
		Iterator<OWLObjectProperty> it = repository.getOWLObjectPropertyList()
				.iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLObjectProperty current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	public AxiomTranslator getAxiomTranslator() {
		return this.axiomTranslator;
	}

	public Set<ComplexIntegerAxiom> getOntology() {
		return this.ontology;
	}

	public TranslationRepository getTranslationRepository() {
		return this.repository;
	}

	private Set<ComplexIntegerAxiom> translateA(Set<OWLAxiom> axiomSet)
			throws TranslationException {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<ComplexIntegerAxiom> ret = new HashSet<ComplexIntegerAxiom>();
		for (OWLAxiom axiom : axiomSet) {
			ret.addAll(axiom.accept(axiomTranslator));
		}
		return ret;
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

		return new IntegerClass(getAxiomTranslator()
				.getClassExpressionTranslator().getId(owlObject));
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

		return new IntegerNamedIndividual(getAxiomTranslator()
				.getClassExpressionTranslator().getId(owlObject));
	}

	public OWLObjectPropertyExpression translateOPE(
			IntegerObjectPropertyExpression integerObject) {
		if (integerObject == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectPropertyExpression ret = null;
		if (integerObject instanceof IntegerObjectProperty) {
			ret = getTranslationRepository().getOWLObjectProperty(
					integerObject.getId());
		} else {
			TranslationException
					.newUnsupportedTranslationException(integerObject);
		}
		return ret;
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
