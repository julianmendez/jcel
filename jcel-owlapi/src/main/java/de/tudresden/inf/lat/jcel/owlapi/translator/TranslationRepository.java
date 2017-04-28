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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;

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
	private final Map<OWLClass, Integer> classInvMap = new HashMap<>();
	private final Map<Integer, OWLClass> classMap = new HashMap<>();
	private final Map<OWLDataProperty, Integer> dataPropertyInvMap = new HashMap<>();
	private final Map<Integer, OWLDataProperty> dataPropertyMap = new HashMap<>();
	private final IntegerEntityManager entityManager;
	private final Map<OWLNamedIndividual, Integer> individualInvMap = new HashMap<>();
	private final Map<Integer, OWLNamedIndividual> individualMap = new HashMap<>();
	private final Map<OWLLiteral, Integer> literalInvMap = new HashMap<>();
	private final Map<Integer, OWLLiteral> literalMap = new HashMap<>();
	private final Map<OWLObjectProperty, Integer> objectPropertyInvMap = new HashMap<>();
	private final Map<Integer, OWLObjectProperty> objectPropertyMap = new HashMap<>();
	private final Map<OWLAnnotationProperty, Integer> annotationPropertyInvMap = new HashMap<>();
	private final Map<Integer, OWLAnnotationProperty> annotationPropertyMap = new HashMap<>();
	private final Map<OWLAnnotationValue, Integer> annotationValueInvMap = new HashMap<>();
	private final Map<Integer, OWLAnnotationValue> annotationValueMap = new HashMap<>();
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
	public TranslationRepository(OWLDataFactory dataFactory, IntegerEntityManager manager) {
		Objects.requireNonNull(dataFactory);
		Objects.requireNonNull(manager);
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
	 * Adds the entities of an ontology to the repository.
	 * 
	 * @param ontology
	 *            OWL ontology
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addAxiomEntities(OWLOntology ontology) {
		Objects.requireNonNull(ontology);
		boolean ret = false;

		ret = ontology.getClassesInSignature().stream().map(cls -> addClass(cls)) //
				.reduce(ret, (accum, elem) -> (accum || elem));

		ret = ontology.getObjectPropertiesInSignature().stream().map(objProp -> addObjectProperty(objProp)) //
				.reduce(ret, (accum, elem) -> (accum || elem));

		ret = ontology.getIndividualsInSignature().stream().map(indiv -> addNamedIndividual(indiv)) //
				.reduce(ret, (accum, elem) -> (accum || elem));

		ret = ontology.getDataPropertiesInSignature().stream().map(dataProp -> addDataProperty(dataProp)) //
				.reduce(ret, (accum, elem) -> (accum || elem));

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
		Objects.requireNonNull(cls);
		boolean ret = false;
		if (!this.classInvMap.containsKey(cls)) {
			Integer id = this.entityManager.createNamedEntity(IntegerEntityType.CLASS, cls.toStringID(), false);
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
		Objects.requireNonNull(dataProp);
		boolean ret = false;
		if (!this.dataPropertyInvMap.containsKey(dataProp)) {
			Integer id = this.entityManager.createNamedEntity(IntegerEntityType.DATA_PROPERTY, dataProp.toStringID(),
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
		Objects.requireNonNull(lit);
		boolean ret = false;
		if (!this.literalInvMap.containsKey(lit)) {
			Integer id = this.entityManager.createNamedEntity(IntegerEntityType.LITERAL, lit.getLiteral(), false);
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
		Objects.requireNonNull(indiv);
		boolean ret = false;
		if (!this.individualInvMap.containsKey(indiv)) {
			Integer id = this.entityManager.createNamedEntity(IntegerEntityType.INDIVIDUAL, indiv.toStringID(), false);
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
		Objects.requireNonNull(objProp);
		boolean ret = false;
		if (!this.objectPropertyInvMap.containsKey(objProp)) {
			Integer id = this.entityManager.createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, objProp.toStringID(),
					false);
			this.objectPropertyMap.put(id, objProp);
			this.objectPropertyInvMap.put(objProp, id);
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds an annotation property to the repository.
	 * 
	 * @param annProp
	 *            OWL annotation property
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addAnnotationProperty(OWLAnnotationProperty annProp) {
		Objects.requireNonNull(annProp);
		boolean ret = false;
		if (!this.annotationPropertyInvMap.containsKey(annProp)) {
			Integer id = this.entityManager.createNamedEntity(IntegerEntityType.ANNOTATION_PROPERTY,
					annProp.toStringID(), false);
			this.annotationPropertyMap.put(id, annProp);
			this.annotationPropertyInvMap.put(annProp, id);
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds an annotation value to the repository.
	 * 
	 * @param annValue
	 *            OWL annotation value
	 * @return <code>true</code> if and only if the repository has changed
	 */
	public boolean addAnnotationValue(OWLAnnotationValue annValue) {
		Objects.requireNonNull(annValue);
		boolean ret = false;
		if (!this.annotationValueInvMap.containsKey(annValue)) {
			if (annValue.asLiteral().isPresent()) {
				Integer id = this.entityManager.createNamedEntity(IntegerEntityType.ANNOTATION_VALUE,
						annValue.asLiteral().get().getLiteral(), false);
				this.annotationValueMap.put(id, annValue);
				this.annotationValueInvMap.put(annValue, id);
				ret = true;
			}
		}
		return ret;
	}

	public Integer getId(OWLClass owlClass) {
		Objects.requireNonNull(owlClass);
		Integer ret = this.classInvMap.get(owlClass);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(owlClass.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLDataProperty owlDataProperty) {
		Objects.requireNonNull(owlDataProperty);
		Integer ret = this.dataPropertyInvMap.get(owlDataProperty);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(owlDataProperty.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLIndividual individual) {
		Objects.requireNonNull(individual);
		Integer ret = this.individualInvMap.get(individual);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(individual.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLLiteral owlLiteral) {
		Objects.requireNonNull(owlLiteral);
		Integer ret = this.literalInvMap.get(owlLiteral);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(owlLiteral.getLiteral());
		}
		return ret;
	}

	public Integer getId(OWLObjectProperty owlObjectProperty) {
		Objects.requireNonNull(owlObjectProperty);
		Integer ret = this.objectPropertyInvMap.get(owlObjectProperty);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(owlObjectProperty.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLAnnotationProperty owlAnnotationProperty) {
		Objects.requireNonNull(owlAnnotationProperty);
		Integer ret = this.annotationPropertyInvMap.get(owlAnnotationProperty);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(owlAnnotationProperty.toStringID());
		}
		return ret;
	}

	public Integer getId(OWLAnnotationValue owlAnnotationValue) {
		Objects.requireNonNull(owlAnnotationValue);
		Integer ret = this.annotationValueInvMap.get(owlAnnotationValue);
		if (Objects.isNull(ret)) {
			String msg = null;
			if (owlAnnotationValue.asLiteral().isPresent()) {
				msg = owlAnnotationValue.asLiteral().get().getLiteral();
			} else {
				msg = owlAnnotationValue.toString();
			}
			throw TranslationException.newIncompleteMapException(msg);
		}
		return ret;
	}

	public OWLClass getOWLClass(Integer index) {
		Objects.requireNonNull(index);
		OWLClass ret = this.classMap.get(index);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret;
	}

	public OWLDataProperty getOWLDataProperty(Integer index) {
		Objects.requireNonNull(index);
		OWLDataProperty ret = this.dataPropertyMap.get(index);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret;
	}

	public OWLNamedIndividual getOWLNamedIndividual(Integer index) {
		Objects.requireNonNull(index);
		OWLNamedIndividual ret = this.individualMap.get(index);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret;
	}

	public OWLObjectProperty getOWLObjectProperty(Integer index) {
		Objects.requireNonNull(index);
		OWLObjectProperty ret = this.objectPropertyMap.get(index);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret;
	}

	public OWLAnnotationProperty getOWLAnnotationProperty(Integer index) {
		Objects.requireNonNull(index);
		OWLAnnotationProperty ret = this.annotationPropertyMap.get(index);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret;
	}

	public OWLAnnotationValue getOWLAnnotationValue(Integer index) {
		Objects.requireNonNull(index);
		OWLAnnotationValue ret = this.annotationValueMap.get(index);
		if (Objects.isNull(ret)) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret;
	}

	public Optional<Integer> getOptId(OWLClass owlClass) {
		Objects.requireNonNull(owlClass);
		return Optional.ofNullable(this.classInvMap.get(owlClass));
	}

	public Optional<Integer> getOptId(OWLDataProperty owlDataProperty) {
		Objects.requireNonNull(owlDataProperty);
		return Optional.ofNullable(this.dataPropertyInvMap.get(owlDataProperty));
	}

	public Optional<Integer> getOptId(OWLIndividual individual) {
		Objects.requireNonNull(individual);
		return Optional.ofNullable(this.individualInvMap.get(individual));
	}

	public Optional<Integer> getOptId(OWLLiteral owlLiteral) {
		Objects.requireNonNull(owlLiteral);
		return Optional.ofNullable(this.literalInvMap.get(owlLiteral));
	}

	public Optional<Integer> getOptId(OWLObjectProperty owlObjectProperty) {
		Objects.requireNonNull(owlObjectProperty);
		return Optional.ofNullable(this.objectPropertyInvMap.get(owlObjectProperty));
	}

	public Optional<Integer> getOptId(OWLAnnotationProperty owlAnnotationProperty) {
		Objects.requireNonNull(owlAnnotationProperty);
		return Optional.ofNullable(this.annotationPropertyInvMap.get(owlAnnotationProperty));
	}

	public Optional<Integer> getOptId(OWLAnnotationValue owlAnnotationValue) {
		Objects.requireNonNull(owlAnnotationValue);
		Integer ret = this.annotationValueInvMap.get(owlAnnotationValue);
		if (Objects.isNull(ret)) {
			String msg = null;
			if (owlAnnotationValue.asLiteral().isPresent()) {
				msg = owlAnnotationValue.asLiteral().get().getLiteral();
			} else {
				msg = owlAnnotationValue.toString();
			}
		}
		return Optional.ofNullable(ret);
	}

	public Optional<OWLClass> getOptOWLClass(Integer index) {
		Objects.requireNonNull(index);
		return Optional.ofNullable(this.classMap.get(index));
	}

	public Optional<OWLDataProperty> getOptOWLDataProperty(Integer index) {
		Objects.requireNonNull(index);
		return Optional.ofNullable(this.dataPropertyMap.get(index));
	}

	public Optional<OWLNamedIndividual> getOptOWLNamedIndividual(Integer index) {
		Objects.requireNonNull(index);
		return Optional.ofNullable(this.individualMap.get(index));
	}

	public Optional<OWLObjectProperty> getOptOWLObjectProperty(Integer index) {
		Objects.requireNonNull(index);
		return Optional.ofNullable(this.objectPropertyMap.get(index));
	}

	public Optional<OWLAnnotationProperty> getOptOWLAnnotationProperty(Integer index) {
		Objects.requireNonNull(index);
		return Optional.ofNullable(this.annotationPropertyMap.get(index));
	}

	public Optional<OWLAnnotationValue> getOptOWLAnnotationValue(Integer index) {
		Objects.requireNonNull(index);
		return Optional.ofNullable(this.annotationValueMap.get(index));
	}

	private void initializeMaps() {

		this.classMap.put(IntegerEntityManager.bottomClassId, this.bottomClass);
		this.classInvMap.put(this.bottomClass, IntegerEntityManager.bottomClassId);
		this.classMap.put(IntegerEntityManager.topClassId, this.topClass);
		this.classInvMap.put(this.topClass, IntegerEntityManager.topClassId);

		this.objectPropertyMap.put(IntegerEntityManager.bottomObjectPropertyId, this.bottomObjectProperty);
		this.objectPropertyInvMap.put(this.bottomObjectProperty, IntegerEntityManager.bottomObjectPropertyId);
		this.objectPropertyMap.put(IntegerEntityManager.topObjectPropertyId, this.topObjectProperty);
		this.objectPropertyInvMap.put(this.topObjectProperty, IntegerEntityManager.topObjectPropertyId);

		this.dataPropertyMap.put(IntegerEntityManager.bottomDataPropertyId, this.bottomDataProperty);
		this.dataPropertyInvMap.put(this.bottomDataProperty, IntegerEntityManager.bottomDataPropertyId);
		this.dataPropertyMap.put(IntegerEntityManager.topDataPropertyId, this.topDataProperty);
		this.dataPropertyInvMap.put(this.topDataProperty, IntegerEntityManager.topDataPropertyId);
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
		sbuf.append(this.annotationPropertyMap.toString());
		sbuf.append("\n");
		sbuf.append(this.annotationValueMap.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}

}
