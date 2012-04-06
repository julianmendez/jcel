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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityType;

/**
 * An object of this class is a repository used for the translation between OWL
 * API objects and the integer numbers. Each entity is identified by an integer
 * number.
 * 
 * @author Julian Mendez
 */
public class TranslationRepository {

	private final OWLClass bottomClass;
	private final OWLDataProperty bottomDataProperty;
	private final OWLObjectProperty bottomObjectProperty;
	private final Map<OWLClass, Integer> classInvMap = new HashMap<OWLClass, Integer>();
	private final Map<Integer, OWLClass> classMap = new HashMap<Integer, OWLClass>();
	private final Map<OWLDataProperty, Integer> dataPropertyInvMap = new HashMap<OWLDataProperty, Integer>();
	private final Map<Integer, OWLDataProperty> dataPropertyMap = new HashMap<Integer, OWLDataProperty>();
	private final IntegerEntityManager entityManager;
	private final Map<OWLNamedIndividual, Integer> individualInvMap = new HashMap<OWLNamedIndividual, Integer>();
	private final Map<Integer, OWLNamedIndividual> individualMap = new HashMap<Integer, OWLNamedIndividual>();
	private final Map<OWLLiteral, Integer> literalInvMap = new HashMap<OWLLiteral, Integer>();
	private final Map<Integer, OWLLiteral> literalMap = new HashMap<Integer, OWLLiteral>();
	private final Map<OWLObjectProperty, Integer> objectPropertyInvMap = new HashMap<OWLObjectProperty, Integer>();
	private final Map<Integer, OWLObjectProperty> objectPropertyMap = new HashMap<Integer, OWLObjectProperty>();
	private final OWLClass topClass;
	private final OWLDataProperty topDataProperty;
	private final OWLObjectProperty topObjectProperty;

	/**
	 * Constructs a new translation repository.
	 * 
	 * @param dataFactory
	 *            OWL ontology
	 * @param manager
	 *            entity manager
	 */
	public TranslationRepository(OWLDataFactory dataFactory,
			IntegerEntityManager manager) {
		if (dataFactory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.entityManager = manager;

		this.bottomClass = dataFactory.getOWLNothing();
		this.topClass = dataFactory.getOWLThing();
		this.bottomObjectProperty = dataFactory.getOWLBottomObjectProperty();
		this.topObjectProperty = dataFactory.getOWLTopObjectProperty();
		this.bottomDataProperty = dataFactory.getOWLBottomDataProperty();
		this.topDataProperty = dataFactory.getOWLTopDataProperty();

		initializeMaps();
	}

	/**
	 * Adds the entities of an axiom to the repository.
	 * 
	 * @param axiom
	 *            OWL axiom
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addAxiomEntities(OWLAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;

		for (OWLClass cls : axiom.getClassesInSignature()) {
			boolean changed = addClass(cls);
			ret = ret || changed;
		}
		for (OWLObjectProperty objProp : axiom.getObjectPropertiesInSignature()) {
			boolean changed = addObjectProperty(objProp);
			ret = ret || changed;
		}
		for (OWLNamedIndividual indiv : axiom.getIndividualsInSignature()) {
			boolean changed = addNamedIndividual(indiv);
			ret = ret || changed;
		}
		for (OWLDataProperty dataProp : axiom.getDataPropertiesInSignature()) {
			boolean changed = addDataProperty(dataProp);
			ret = ret || changed;
		}
		for (OWLLiteral lit : collectLiterals(axiom)) {
			boolean changed = addLiteral(lit);
			ret = ret || changed;
		}
		return ret;
	}

	/**
	 * Adds a class to the repository.
	 * 
	 * @param cls
	 *            OWL class
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addClass(OWLClass cls) {
		if (cls == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.classInvMap.containsKey(cls)) {
			Integer id = this.entityManager.createNamedEntity(
					IntegerEntityType.CLASS, cls.toStringID(), false);
			this.classMap.put(id, cls);
			this.classInvMap.put(cls, id);
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds a data property to the repository.
	 * 
	 * @param dataProp
	 *            OWL data property
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addDataProperty(OWLDataProperty dataProp) {
		if (dataProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.dataPropertyInvMap.containsKey(dataProp)) {
			Integer id = this.entityManager.createNamedEntity(
					IntegerEntityType.DATA_PROPERTY, dataProp.toStringID(),
					false);
			this.dataPropertyMap.put(id, dataProp);
			this.dataPropertyInvMap.put(dataProp, id);
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds a literal to the repository.
	 * 
	 * @param lit
	 *            OWL literal
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addLiteral(OWLLiteral lit) {
		if (lit == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.literalInvMap.containsKey(lit)) {
			Integer id = this.entityManager.createNamedEntity(
					IntegerEntityType.LITERAL, lit.getLiteral(), false);
			this.literalMap.put(id, lit);
			this.literalInvMap.put(lit, id);
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds an named individual to the repository.
	 * 
	 * @param indiv
	 *            OWL named individual
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addNamedIndividual(OWLNamedIndividual indiv) {
		if (indiv == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.individualInvMap.containsKey(indiv)) {
			Integer id = this.entityManager.createNamedEntity(
					IntegerEntityType.INDIVIDUAL, indiv.toStringID(), false);
			this.individualMap.put(id, indiv);
			this.individualInvMap.put(indiv, id);
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds an object property to the repository.
	 * 
	 * @param objProp
	 *            OWL object property
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addObjectProperty(OWLObjectProperty objProp) {
		if (objProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.objectPropertyInvMap.containsKey(objProp)) {
			Integer id = this.entityManager.createNamedEntity(
					IntegerEntityType.OBJECT_PROPERTY, objProp.toStringID(),
					false);
			this.objectPropertyMap.put(id, objProp);
			this.objectPropertyInvMap.put(objProp, id);
			ret = true;
		}
		return ret;
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

	public Integer getId(OWLClass owlClass) {
		if (owlClass == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.classInvMap.get(owlClass);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(owlClass
					.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLDataProperty owlDataProperty) {
		if (owlDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.dataPropertyInvMap.get(owlDataProperty);
		if (ret == null) {
			throw TranslationException
					.newIncompleteMapException(owlDataProperty.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.individualInvMap.get(individual);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(individual
					.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLLiteral owlLiteral) {
		if (owlLiteral == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.literalInvMap.get(owlLiteral);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(owlLiteral
					.getLiteral());
		}
		return ret;
	}

	public Integer getId(OWLObjectProperty owlObjectProperty) {
		if (owlObjectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.objectPropertyInvMap.get(owlObjectProperty);
		if (ret == null) {
			throw TranslationException
					.newIncompleteMapException(owlObjectProperty.toStringID());
		}
		return ret;
	}

	public OWLClass getOWLClass(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLClass ret = this.classMap.get(index);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(index
					.toString());
		}
		return ret;
	}

	public OWLDataProperty getOWLDataProperty(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLDataProperty ret = this.dataPropertyMap.get(index);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(index
					.toString());
		}
		return ret;
	}

	public OWLNamedIndividual getOWLNamedIndividual(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLNamedIndividual ret = this.individualMap.get(index);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(index
					.toString());
		}
		return ret;
	}

	public OWLObjectProperty getOWLObjectProperty(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty ret = this.objectPropertyMap.get(index);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(index
					.toString());
		}
		return ret;
	}

	private void initializeMaps() {

		this.classMap.put(IntegerEntityManager.bottomClassId, this.bottomClass);
		this.classInvMap.put(this.bottomClass,
				IntegerEntityManager.bottomClassId);
		this.classMap.put(IntegerEntityManager.topClassId, this.topClass);
		this.classInvMap.put(this.topClass, IntegerEntityManager.topClassId);

		this.objectPropertyMap.put(IntegerEntityManager.bottomObjectPropertyId,
				this.bottomObjectProperty);
		this.objectPropertyInvMap.put(this.bottomObjectProperty,
				IntegerEntityManager.bottomObjectPropertyId);
		this.objectPropertyMap.put(IntegerEntityManager.topObjectPropertyId,
				this.topObjectProperty);
		this.objectPropertyInvMap.put(this.topObjectProperty,
				IntegerEntityManager.topObjectPropertyId);

		this.dataPropertyMap.put(IntegerEntityManager.bottomDataPropertyId,
				this.bottomDataProperty);
		this.dataPropertyInvMap.put(this.bottomDataProperty,
				IntegerEntityManager.bottomDataPropertyId);
		this.dataPropertyMap.put(IntegerEntityManager.topDataPropertyId,
				this.topDataProperty);
		this.dataPropertyInvMap.put(this.topDataProperty,
				IntegerEntityManager.topDataPropertyId);
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n");
		sbuf.append(this.classMap.toString());
		sbuf.append("\n");
		sbuf.append(this.objectPropertyMap.toString());
		sbuf.append("\n");
		sbuf.append(this.individualMap.toString());
		sbuf.append("\n");
		sbuf.append(this.dataPropertyMap.toString());
		sbuf.append("\n");
		sbuf.append(this.literalMap.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}

}
