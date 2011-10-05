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
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;

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

	/**
	 * @param bottomClass
	 *            OWL class for 'nothing'
	 * @param topClass
	 *            OWL class for 'thing'
	 * @param bottomObjectProperty
	 *            OWL bottom object property
	 * @param topObjectProperty
	 *            OWL top object property
	 * @param bottomDataProperty
	 *            OWL bottom data property
	 * @param topDataProperty
	 *            OWL top data property
	 */
	public TranslationRepository(OWLClass bottomClass, OWLClass topClass,
			OWLObjectProperty bottomObjectProperty,
			OWLObjectProperty topObjectProperty,
			OWLDataProperty bottomDataProperty,
			OWLDataProperty topDataProperty, Set<OWLAxiom> axiomSet) {
		if (bottomClass == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topClass == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomObjectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topObjectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.bottomClass = bottomClass;
		this.topClass = topClass;
		this.bottomObjectProperty = bottomObjectProperty;
		this.topObjectProperty = topObjectProperty;
		this.bottomDataProperty = bottomDataProperty;
		this.topDataProperty = topDataProperty;
		Set<OWLClass> conceptNameSet = collectClasses(axiomSet);
		Set<OWLObjectProperty> objectPropertySet = collectObjectProperties(axiomSet);
		Set<OWLNamedIndividual> individualSet = collectIndividuals(axiomSet);
		Set<OWLDataProperty> dataPropertySet = collectDataProperties(axiomSet);
		Set<OWLLiteral> literalSet = collectLiterals(axiomSet);
		load(conceptNameSet, objectPropertySet, individualSet, dataPropertySet,
				literalSet);
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

	private Set<OWLClass> collectClasses(Set<OWLAxiom> axiomSet) {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		for (OWLAxiom axiom : axiomSet) {
			ret.addAll(axiom.getClassesInSignature());
		}
		ret.add(bottomClass);
		ret.add(topClass);
		return ret;
	}

	private Set<OWLDataProperty> collectDataProperties(Set<OWLAxiom> axiomSet) {
		Set<OWLDataProperty> ret = new HashSet<OWLDataProperty>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLDataProperty> entities = axiom
					.getDataPropertiesInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	private Set<OWLNamedIndividual> collectIndividuals(Set<OWLAxiom> axiomSet) {
		Set<OWLNamedIndividual> ret = new HashSet<OWLNamedIndividual>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLNamedIndividual> entities = axiom
					.getIndividualsInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	private Set<OWLLiteral> collectLiterals(Set<OWLAxiom> axiomSet) {
		Set<OWLLiteral> ret = new HashSet<OWLLiteral>();
		for (OWLAxiom axiom : axiomSet) {
			if ((axiom instanceof OWLDataPropertyAssertionAxiom)) {
				ret.add(((OWLDataPropertyAssertionAxiom) axiom).getObject());
			}
			if (axiom instanceof OWLNegativeDataPropertyAssertionAxiom) {
				ret.add(((OWLNegativeDataPropertyAssertionAxiom) axiom)
						.getObject());
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

		}
		return ret;
	}

	private Set<OWLObjectProperty> collectObjectProperties(
			Set<OWLAxiom> axiomSet) {
		Set<OWLObjectProperty> ret = new HashSet<OWLObjectProperty>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLObjectProperty> entities = axiom
					.getObjectPropertiesInSignature();
			ret.addAll(entities);
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
	private void load(Set<OWLClass> classSet,
			Set<OWLObjectProperty> objectPropertySet,
			Set<OWLNamedIndividual> individualSet,
			Set<OWLDataProperty> dataPropertySet, Set<OWLLiteral> literalSet) {

		if (classSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (objectPropertySet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (individualSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (dataPropertySet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (literalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (classSet != null) {
			Set<OWLClass> sorted = new TreeSet<OWLClass>();
			sorted.addAll(classSet);
			sorted.remove(this.bottomClass);
			sorted.remove(this.topClass);
			this.classList = new ArrayList<OWLClass>();
			this.classList.add(this.bottomClass); // 0
			this.classList.add(this.topClass); // 1
			this.classList.addAll(sorted);
		}

		if (objectPropertySet != null) {
			Set<OWLObjectProperty> sorted = new TreeSet<OWLObjectProperty>();
			sorted.addAll(objectPropertySet);
			this.objectPropertyList = new ArrayList<OWLObjectProperty>();
			this.objectPropertyList.add(this.bottomObjectProperty); // 0
			this.objectPropertyList.add(this.topObjectProperty); // 1
			this.objectPropertyList.addAll(sorted);
		}

		if (individualSet != null) {
			Set<OWLNamedIndividual> sorted = new TreeSet<OWLNamedIndividual>();
			sorted.addAll(individualSet);
			this.individualList = new ArrayList<OWLNamedIndividual>();
			this.individualList.addAll(sorted);
		}

		if (dataPropertySet != null) {
			Set<OWLDataProperty> sorted = new TreeSet<OWLDataProperty>();
			sorted.addAll(dataPropertySet);
			this.dataPropertyList = new ArrayList<OWLDataProperty>();
			this.dataPropertyList.add(this.bottomDataProperty); // 0
			this.dataPropertyList.add(this.topDataProperty); // 1
			this.dataPropertyList.addAll(sorted);
		}

		if (literalSet != null) {
			Set<OWLLiteral> sorted = new TreeSet<OWLLiteral>();
			sorted.addAll(literalSet);
			this.literalList = new ArrayList<OWLLiteral>();
			this.literalList.addAll(sorted);
		}

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
