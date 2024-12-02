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

//import java.util.HashMap;
//import java.util.Objects;
//import java.util.Optional;

import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;

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
import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

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
	private final OptMap<OWLClass, Integer> classInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLClass> classMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<OWLDataProperty, Integer> dataPropertyInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLDataProperty> dataPropertyMap = new OptMapImpl<>(new HashMap<>());
	private final IntegerEntityManager entityManager;
	private final OptMap<OWLNamedIndividual, Integer> individualInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLNamedIndividual> individualMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<OWLLiteral, Integer> literalInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLLiteral> literalMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<OWLObjectProperty, Integer> objectPropertyInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLObjectProperty> objectPropertyMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<OWLAnnotationProperty, Integer> annotationPropertyInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLAnnotationProperty> annotationPropertyMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<OWLAnnotationValue, Integer> annotationValueInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OWLAnnotationValue> annotationValueMap = new OptMapImpl<>(new HashMap<>());
	private final OWLClass topClass;
	private final OWLDataProperty topDataProperty;
	private final OWLObjectProperty topObjectProperty;
	private final Set<String> newClasses = new HashSet<>();
	private final Set<String> newDataProperties = new HashSet<>();
	// private final Set<String> newLiterals = new HashSet<>();
	private final Set<String> newNamedIndividuals = new HashSet<>();
	private final Set<String> newObjectProperties = new HashSet<>();
	private final Set<String> newAnnotationProperties = new HashSet<>();
	// private final Set<String> newAnnotationValues = new HashSet<>();

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
		Optional<Integer> ret = this.classInvMap.get(owlClass);
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(owlClass.toStringID());
		}
		return ret.get();
	}

	public Integer getId(OWLDataProperty owlDataProperty) {
		Objects.requireNonNull(owlDataProperty);
		Optional<Integer> ret = this.dataPropertyInvMap.get(owlDataProperty);
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(owlDataProperty.toStringID());
		}
		return ret.get();
	}

	public Integer getId(OWLIndividual individual) {
		Objects.requireNonNull(individual);
		Optional<Integer> ret = this.individualInvMap.get(individual.asOWLNamedIndividual());
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(individual.toStringID());
		}
		return ret.get();
	}

	public Integer getId(OWLLiteral owlLiteral) {
		Objects.requireNonNull(owlLiteral);
		Optional<Integer> ret = this.literalInvMap.get(owlLiteral);
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(owlLiteral.getLiteral());
		}
		return ret.get();
	}

	public Integer getId(OWLObjectProperty owlObjectProperty) {
		Objects.requireNonNull(owlObjectProperty);
		Optional<Integer> ret = this.objectPropertyInvMap.get(owlObjectProperty);
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(owlObjectProperty.toStringID());
		}
		return ret.get();
	}

	public Integer getId(OWLAnnotationProperty owlAnnotationProperty) {
		Objects.requireNonNull(owlAnnotationProperty);
		Optional<Integer> ret = this.annotationPropertyInvMap.get(owlAnnotationProperty);
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(owlAnnotationProperty.toStringID());
		}
		return ret.get();
	}

	public Integer getId(OWLAnnotationValue owlAnnotationValue) {
		Objects.requireNonNull(owlAnnotationValue);
		Optional<Integer> ret = this.annotationValueInvMap.get(owlAnnotationValue);
		if (!ret.isPresent()) {
			String msg = null;
			if (owlAnnotationValue.asLiteral().isPresent()) {
				msg = owlAnnotationValue.asLiteral().get().getLiteral();
			} else {
				msg = owlAnnotationValue.toString();
			}
			throw TranslationException.newIncompleteMapException(msg);
		}
		return ret.get();
	}

	public OWLClass getOWLClass(Integer index) {
		Objects.requireNonNull(index);
		Optional<OWLClass> ret = this.classMap.get(index);
		if (!ret.isPresent()) {
			// throw TranslationException.newIncompleteMapException(index.toString());
			while (true)
			{
				String randomClassPostfix = RandomStringUtils.randomAlphanumeric(5);
				String prefix = "http://";
				String randomClassName = prefix.concat(randomClassPostfix);
				if (!newClasses.contains(randomClassName)) {
					newClasses.add(randomClassName);
					OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
					OWLClass cls = dataFactory.getOWLClass(IRI.create(randomClassName));
					this.classMap.put(index, cls);
					this.classInvMap.put(cls, index);
					Optional<OWLClass> ret2 = this.classMap.get(index);
					return ret2.get();
				}
			}
		}
		return ret.get();
	}

	public OWLDataProperty getOWLDataProperty(Integer index) {
		Objects.requireNonNull(index);
		Optional<OWLDataProperty> ret = this.dataPropertyMap.get(index);
		if (!ret.isPresent()) {
			// throw TranslationException.newIncompleteMapException(index.toString());
			while (true)
			{
				String randomDataPostfix = RandomStringUtils.randomAlphanumeric(6);
				String prefix = "http://";
				String randomDataProperty = prefix.concat(randomDataPostfix);
				if (!newDataProperties.contains(randomDataProperty)) {
					newDataProperties.add(randomDataProperty);
					OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
					OWLDataProperty dpr = dataFactory.getOWLDataProperty(IRI.create(randomDataProperty));
					this.dataPropertyMap.put(index, dpr);
					this.dataPropertyInvMap.put(dpr, index);
					Optional<OWLDataProperty> ret2 = this.dataPropertyMap.get(index);
					return ret2.get();
				}
			}
		}
		return ret.get();
	}

	public OWLNamedIndividual getOWLNamedIndividual(Integer index) {
		Objects.requireNonNull(index);
		Optional<OWLNamedIndividual> ret = this.individualMap.get(index);
		if (!ret.isPresent()) {
			// throw TranslationException.newIncompleteMapException(index.toString());
			while (true)
			{
				String randomNamedIndividualPostfix = RandomStringUtils.randomAlphanumeric(7);
				String prefix = "http://";
				String randomNamedIndividual = prefix.concat(randomNamedIndividualPostfix);
				if (!newNamedIndividuals.contains(randomNamedIndividual)) {
					newNamedIndividuals.add(randomNamedIndividual);
					OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
					OWLNamedIndividual ni = dataFactory.getOWLNamedIndividual(IRI.create(randomNamedIndividual));
					this.individualMap.put(index, ni);
					this.individualInvMap.put(ni, index);
					Optional<OWLNamedIndividual> ret2 = this.individualMap.get(index);
					return ret2.get();
				}
			}
		}
		return ret.get();
	}

	public OWLObjectProperty getOWLObjectProperty(Integer index) {
		Objects.requireNonNull(index);
		Optional<OWLObjectProperty> ret = this.objectPropertyMap.get(index);
		if (!ret.isPresent()) {
			// throw TranslationException.newIncompleteMapException(index.toString());
			while (true)
			{
				String randomObjectPropertyPostfix = RandomStringUtils.randomAlphanumeric(8);
				String prefix = "http://";
				String randomObjectProperty = prefix.concat(randomObjectPropertyPostfix);
				if (!newObjectProperties.contains(randomObjectProperty)) {
					newObjectProperties.add(randomObjectProperty);
					OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
					OWLObjectProperty op = dataFactory.getOWLObjectProperty(IRI.create(randomObjectProperty));
					this.objectPropertyMap.put(index, op);
					this.objectPropertyInvMap.put(op, index);
					Optional<OWLObjectProperty> ret2 = this.objectPropertyMap.get(index);
					return ret2.get();
				}
			}
		}
		return ret.get();
	}

	public OWLAnnotationProperty getOWLAnnotationProperty(Integer index) {
		Objects.requireNonNull(index);
		Optional<OWLAnnotationProperty> ret = this.annotationPropertyMap.get(index);
		if (!ret.isPresent()) {
			// throw TranslationException.newIncompleteMapException(index.toString());
			while (true)
			{
				String randomAnnotationPropertyPostfix = RandomStringUtils.randomAlphanumeric(9);
				String prefix = "http://";
				String randomAnnotationProperty = prefix.concat(randomAnnotationPropertyPostfix);
				if (!newAnnotationProperties.contains(randomAnnotationProperty)) {
					newAnnotationProperties.add(randomAnnotationProperty);
					OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
					OWLAnnotationProperty ap = dataFactory.getOWLAnnotationProperty(IRI
							.create(randomAnnotationProperty));
					this.annotationPropertyMap.put(index, ap);
					this.annotationPropertyInvMap.put(ap, index);
					Optional<OWLAnnotationProperty> ret2 = this.annotationPropertyMap.get(index);
					return ret2.get();
				}
			}
		}
		return ret.get();
	}

	public OWLAnnotationValue getOWLAnnotationValue(Integer index) {
		Objects.requireNonNull(index);
		Optional<OWLAnnotationValue> ret = this.annotationValueMap.get(index);
		if (!ret.isPresent()) {
			throw TranslationException.newIncompleteMapException(index.toString());
		}
		return ret.get();
	}

	public Optional<Integer> getOptId(OWLClass owlClass) {
		Objects.requireNonNull(owlClass);
		return this.classInvMap.get(owlClass);
	}

	public Optional<Integer> getOptId(OWLDataProperty owlDataProperty) {
		Objects.requireNonNull(owlDataProperty);
		return this.dataPropertyInvMap.get(owlDataProperty);
	}

	public Optional<Integer> getOptId(OWLIndividual individual) {
		Objects.requireNonNull(individual);
		return this.individualInvMap.get(individual.asOWLNamedIndividual());
	}

	public Optional<Integer> getOptId(OWLLiteral owlLiteral) {
		Objects.requireNonNull(owlLiteral);
		return this.literalInvMap.get(owlLiteral);
	}

	public Optional<Integer> getOptId(OWLObjectProperty owlObjectProperty) {
		Objects.requireNonNull(owlObjectProperty);
		return this.objectPropertyInvMap.get(owlObjectProperty);
	}

	public Optional<Integer> getOptId(OWLAnnotationProperty owlAnnotationProperty) {
		Objects.requireNonNull(owlAnnotationProperty);
		return this.annotationPropertyInvMap.get(owlAnnotationProperty);
	}

	public Optional<Integer> getOptId(OWLAnnotationValue owlAnnotationValue) {
		Objects.requireNonNull(owlAnnotationValue);
		Optional<Integer> ret = this.annotationValueInvMap.get(owlAnnotationValue);
		if (!ret.isPresent()) {
			String msg = null; // FIXME this variable is never used
			if (owlAnnotationValue.asLiteral().isPresent()) {
				msg = owlAnnotationValue.asLiteral().get().getLiteral();
			} else {
				msg = owlAnnotationValue.toString();
			}
		}
		return ret;
	}

	public Optional<OWLClass> getOptOWLClass(Integer index) {
		Objects.requireNonNull(index);
		return this.classMap.get(index);
	}

	public Optional<OWLDataProperty> getOptOWLDataProperty(Integer index) {
		Objects.requireNonNull(index);
		return this.dataPropertyMap.get(index);
	}

	public Optional<OWLNamedIndividual> getOptOWLNamedIndividual(Integer index) {
		Objects.requireNonNull(index);
		return this.individualMap.get(index);
	}

	public Optional<OWLObjectProperty> getOptOWLObjectProperty(Integer index) {
		Objects.requireNonNull(index);
		return this.objectPropertyMap.get(index);
	}

	public Optional<OWLAnnotationProperty> getOptOWLAnnotationProperty(Integer index) {
		Objects.requireNonNull(index);
		return this.annotationPropertyMap.get(index);
	}

	public Optional<OWLAnnotationValue> getOptOWLAnnotationValue(Integer index) {
		Objects.requireNonNull(index);
		return this.annotationValueMap.get(index);
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
