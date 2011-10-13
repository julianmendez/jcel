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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * An object of this class is a repository used for the translation between OWL
 * API objects and the integer numbers. Each entity is identified by an integer
 * number.
 * 
 * @author Julian Mendez
 */
public class TranslationRepository {

	public static final Integer versionNumber = 1;

	private OWLClass bottomClass = null;
	private OWLDataProperty bottomDataProperty;
	private OWLObjectProperty bottomObjectProperty = null;
	private List<OWLClass> classList = null;
	private List<OWLDataProperty> dataPropertyList = null;
	private List<OWLNamedIndividual> individualList = null;
	private List<OWLLiteral> literalList = null;
	private List<OWLObjectProperty> objectPropertyList = null;
	private OWLClass topClass = null;
	private OWLDataProperty topDataProperty;
	private OWLObjectProperty topObjectProperty = null;

	public TranslationRepository(OWLOntology rootOntology) {
		if (rootOntology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
				.getOWLDataFactory();

		this.bottomClass = dataFactory.getOWLNothing();
		this.topClass = dataFactory.getOWLThing();
		this.bottomObjectProperty = dataFactory.getOWLBottomObjectProperty();
		this.topObjectProperty = dataFactory.getOWLTopObjectProperty();
		this.bottomDataProperty = dataFactory.getOWLBottomDataProperty();
		this.topDataProperty = dataFactory.getOWLTopDataProperty();

		load(rootOntology.getAxioms());

	}

	/**
	 * Clears the repository.
	 */
	public void clear() {
		this.classList = new ArrayList<OWLClass>();
		this.objectPropertyList = new ArrayList<OWLObjectProperty>();
		this.individualList = new ArrayList<OWLNamedIndividual>();
		this.dataPropertyList = new ArrayList<OWLDataProperty>();
		this.literalList = new ArrayList<OWLLiteral>();
	}

	private Set<OWLLiteral> collectLiterals(OWLAxiom axiom) {
		Set<OWLLiteral> ret = new HashSet<OWLLiteral>();
		if ((axiom instanceof OWLDataPropertyAssertionAxiom)) {
			ret.add(((OWLDataPropertyAssertionAxiom) axiom).getObject());
		}
		if (axiom instanceof OWLNegativeDataPropertyAssertionAxiom) {
			ret.add(((OWLNegativeDataPropertyAssertionAxiom) axiom).getObject());
		}
		Set<OWLClassExpression> classExpressions = axiom
				.getNestedClassExpressions();
		for (OWLClassExpression classExpr : classExpressions) {
			if (classExpr instanceof OWLDataHasValue) {
				ret.add(((OWLDataHasValue) classExpr).getValue());
			}
			if (classExpr instanceof OWLDataOneOf) {
				ret.addAll(((OWLDataOneOf) classExpr).getValues());
			}
		}
		return ret;
	}

	public OWLClass getOWLClass(int index) {
		return this.classList.get(index);
	}

	public OWLClass getOWLClass(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (index >= this.classList.size()) {
			throw new IllegalArgumentException("Wrong index for OWLClass : '"
					+ index + "'.");
		}
		return this.classList.get(index);
	}

	public List<OWLClass> getOWLClassList() {
		return Collections.unmodifiableList(this.classList);
	}

	public OWLDataProperty getOWLDataProperty(int index) {
		return this.dataPropertyList.get(index);
	}

	public List<OWLDataProperty> getOWLDataPropertyList() {
		return Collections.unmodifiableList(this.dataPropertyList);
	}

	public List<OWLLiteral> getOWLLiteralList() {
		return Collections.unmodifiableList(this.literalList);
	}

	public OWLNamedIndividual getOWLNamedIndividual(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (index < this.classList.size()) {
			throw new IllegalArgumentException(
					"Wrong index for OWLNamedIndividual : '" + index + "'.");
		}

		return this.individualList.get(index - this.classList.size());
	}

	public List<OWLNamedIndividual> getOWLNamedIndividualList() {
		return Collections.unmodifiableList(this.individualList);
	}

	public OWLObjectProperty getOWLObjectProperty(int index) {
		return this.objectPropertyList.get(index);
	}

	public List<OWLObjectProperty> getOWLObjectPropertyList() {
		return Collections.unmodifiableList(this.objectPropertyList);
	}

	/**
	 * Loads the repository.
	 * 
	 * @param classSet
	 *            set of OWL classes
	 * @param objectPropertySet
	 *            set of object properties
	 * @param individualSet
	 *            set of individuals
	 * @param dataPropertySet
	 *            set of data properties
	 * @param literalSet
	 *            set of literals
	 */
	private void load(Set<OWLAxiom> axiomSet) {

		Set<OWLClass> classSet = new TreeSet<OWLClass>();
		Set<OWLObjectProperty> objectPropertySet = new TreeSet<OWLObjectProperty>();
		Set<OWLNamedIndividual> individualSet = new TreeSet<OWLNamedIndividual>();
		Set<OWLDataProperty> dataPropertySet = new TreeSet<OWLDataProperty>();
		Set<OWLLiteral> literalSet = new TreeSet<OWLLiteral>();

		this.classList = new ArrayList<OWLClass>();
		this.objectPropertyList = new ArrayList<OWLObjectProperty>();
		this.individualList = new ArrayList<OWLNamedIndividual>();
		this.dataPropertyList = new ArrayList<OWLDataProperty>();
		this.literalList = new ArrayList<OWLLiteral>();

		this.classList.add(this.bottomClass); // 0
		this.classList.add(this.topClass); // 1

		this.objectPropertyList.add(this.bottomObjectProperty); // 0
		this.objectPropertyList.add(this.topObjectProperty); // 1

		this.dataPropertyList.add(this.bottomDataProperty); // 0
		this.dataPropertyList.add(this.topDataProperty); // 1

		for (OWLAxiom axiom : axiomSet) {
			classSet.addAll(axiom.getClassesInSignature());
			objectPropertySet.addAll(axiom.getObjectPropertiesInSignature());
			individualSet.addAll(axiom.getIndividualsInSignature());
			dataPropertySet.addAll(axiom.getDataPropertiesInSignature());
			literalSet.addAll(collectLiterals(axiom));
		}

		classSet.remove(this.bottomClass);
		classSet.remove(this.topClass);
		this.classList.addAll(classSet);
		this.objectPropertyList.addAll(objectPropertySet);
		this.individualList.addAll(individualSet);
		this.dataPropertyList.addAll(dataPropertySet);
		this.literalList.addAll(literalSet);
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n");
		sbuf.append(this.classList.toString());
		sbuf.append("\n");
		sbuf.append(this.objectPropertyList.toString());
		sbuf.append("\n");
		sbuf.append(this.individualList.toString());
		sbuf.append("\n");
		sbuf.append(this.dataPropertyList.toString());
		sbuf.append("\n");
		sbuf.append(this.literalList.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}

}
