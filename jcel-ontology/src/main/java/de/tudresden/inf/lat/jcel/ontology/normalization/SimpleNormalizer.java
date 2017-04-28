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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDataPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDataPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDifferentIndividualsAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerFunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseFunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerNamedIndividualDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerNegativeObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerReflexiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSameIndividualAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubObjectPropertyOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object of this class models is a visitor used to normalize axioms. This is
 * an auxiliary class used by <code>OntologyNormalizer</code>.
 * 
 * @author Julian Mendez
 * 
 * @see OntologyNormalizer
 */
public class SimpleNormalizer implements ComplexIntegerAxiomVisitor<Set<IntegerAxiom>> {

	private final List<NormalizationRule> norChainOfSubClass;
	private final NormalizationRule norDisjoint;
	private final NormalizationRule norEquivProperties;
	private final NormalizationRule norNR1_2;
	private final NormalizationRule norNR1_5;
	private final NormalizerNR1_6 norNR1_6;
	private final NormalizationRule norNR2_1;
	private final NormalizationRule norSubPropertyChainOf;
	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer.
	 * 
	 * @param factory
	 *            factory
	 */
	public SimpleNormalizer(IntegerOntologyObjectFactory factory) {
		Objects.requireNonNull(factory);
		this.ontologyObjectFactory = factory;

		this.norChainOfSubClass = new ArrayList<>();
		this.norChainOfSubClass.add(new NormalizerNR1_7(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR2_2(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR2_3(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR2_4(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR3_1(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR3_2(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR3_3(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR4_1(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR4_2(getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerSubClassOf(getOntologyObjectFactory()));

		this.norDisjoint = new NormalizerDisjoint(getOntologyObjectFactory());
		this.norEquivProperties = new NormalizerEquivProperties(getOntologyObjectFactory());
		this.norSubPropertyChainOf = new NormalizerSubPropertyChainOf(getOntologyObjectFactory());
		this.norNR1_5 = new NormalizerNR1_5(getOntologyObjectFactory());
		this.norNR1_2 = new NormalizerNR1_2(getOntologyObjectFactory());
		this.norNR2_1 = new NormalizerNR2_1(getOntologyObjectFactory());
		this.norNR1_6 = new NormalizerNR1_6();
	}

	/**
	 * Returns a set of normalized axioms that relates two object properties
	 * such that one is the inverse of the other one.
	 * 
	 * @param firstProperty
	 *            first object property
	 * @param secondProperty
	 *            second object property, which is the inverse of the first one
	 * @return a set of normalized axioms that relates two object properties
	 *         such that one is the inverse of the other one
	 */
	public Set<NormalizedIntegerAxiom> getAxiomsForInverseObjectProperties(Integer firstProperty,
			Integer secondProperty) {
		Objects.requireNonNull(firstProperty);
		Objects.requireNonNull(secondProperty);
		Set<IntegerAnnotation> annotations = Collections.emptySet();
		Set<NormalizedIntegerAxiom> ret = new HashSet<>();
		{
			Integer inverseSecondProperty = getIdGenerator().createOrGetInverseObjectPropertyOf(secondProperty);
			if (!inverseSecondProperty.equals(firstProperty)) {
				ret.add(getNormalizedAxiomFactory().createRI2Axiom(firstProperty, inverseSecondProperty, annotations));
				ret.add(getNormalizedAxiomFactory().createRI2Axiom(inverseSecondProperty, firstProperty, annotations));
			}
		}
		{
			Integer inverseFirstProperty = getIdGenerator().createOrGetInverseObjectPropertyOf(firstProperty);
			if (!inverseFirstProperty.equals(secondProperty)) {
				ret.add(getNormalizedAxiomFactory().createRI2Axiom(secondProperty, inverseFirstProperty, annotations));
				ret.add(getNormalizedAxiomFactory().createRI2Axiom(inverseFirstProperty, secondProperty, annotations));
			}
		}

		return Collections.unmodifiableSet(ret);

	}

	private ComplexIntegerAxiomFactory getComplexAxiomFactory() {
		return getOntologyObjectFactory().getComplexAxiomFactory();
	}

	private IntegerEntityManager getIdGenerator() {
		return getOntologyObjectFactory().getEntityManager();
	}

	private NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
		return getOntologyObjectFactory().getNormalizedAxiomFactory();
	}

	private Integer getObjectPropertyId(IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(new ObjectPropertyIdFinder(getIdGenerator()));
	}

	public IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

	/**
	 * Normalizes an axiom.
	 * 
	 * @param axiom
	 *            axiom to be normalized
	 * @return the normalized set of axioms according to the specified axiom
	 */
	public Set<IntegerAxiom> normalize(IntegerAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = null;
		if (axiom instanceof ComplexIntegerAxiom) {
			ComplexIntegerAxiom complexAxiom = (ComplexIntegerAxiom) axiom;
			ret = complexAxiom.accept(this);
		} else if (axiom instanceof NormalizedIntegerAxiom) {
			ret = new HashSet<>();
			ret.add(axiom);
		} else {
			throw new IllegalArgumentException("Axiom type is not recognized: '" + axiom + "'.");
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerClassAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		Integer individual = axiom.getIndividual();
		IntegerClassExpression classExpression = axiom.getClassExpression();
		Set<IntegerAxiom> ret = new HashSet<>();
		Integer classId = getIdGenerator().createOrGetClassIdForIndividual(individual);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(classId, individual, axiom.getAnnotations()));
		ret.add(getComplexAxiomFactory().createSubClassOfAxiom(
				getOntologyObjectFactory().getDataTypeFactory().createClass(classId), classExpression,
				axiom.getAnnotations()));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerClassDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDataPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		// TODO Auto-generated method stub
		// FIXME not implemented yet

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDataPropertyDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDifferentIndividualsAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		Set<Integer> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classExprSet = new HashSet<>();
		individualSet.forEach(individual -> {
			Integer classForIndiv = getIdGenerator().createOrGetClassIdForIndividual(individual);
			ret.add(getNormalizedAxiomFactory().createNominalAxiom(classForIndiv, individual, axiom.getAnnotations()));
			classExprSet.add(getOntologyObjectFactory().getDataTypeFactory().createClass(classForIndiv));
		});
		ret.add(getComplexAxiomFactory().createDisjointClassesAxiom(classExprSet, axiom.getAnnotations()));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDisjointClassesAxiom axiom) {
		Objects.requireNonNull(axiom);
		return this.norDisjoint.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentClassesAxiom axiom) {
		Objects.requireNonNull(axiom);
		return this.norNR1_5.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		return this.norEquivProperties.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerFunctionalObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		IntegerAxiom newAxiom = getNormalizedAxiomFactory()
				.createFunctObjectPropAxiom(getObjectPropertyId(axiom.getProperty()), axiom.getAnnotations());
		ret.add(newAxiom);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerInverseFunctionalObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		IntegerAxiom newAxiom = getNormalizedAxiomFactory().createFunctObjectPropAxiom(
				getIdGenerator().createOrGetInverseObjectPropertyOf(getObjectPropertyId(axiom.getProperty())),
				axiom.getAnnotations());
		return Collections.singleton(newAxiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerInverseObjectPropertiesAxiom axiom) {
		Objects.requireNonNull(axiom);
		Integer firstProperty = getObjectPropertyId(axiom.getFirstProperty());
		Integer secondProperty = getObjectPropertyId(axiom.getSecondProperty());
		getIdGenerator().proposeInverseObjectPropertyOf(firstProperty, secondProperty);
		Set<IntegerAxiom> ret = new HashSet<>();
		ret.addAll(getAxiomsForInverseObjectProperties(firstProperty, secondProperty));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerNamedIndividualDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerNegativeObjectPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		Integer subject = axiom.getSubject();
		Integer object = axiom.getObject();
		IntegerObjectPropertyExpression property = axiom.getProperty();
		Set<IntegerAxiom> ret = new HashSet<>();
		Integer classIdForSubject = getIdGenerator().createOrGetClassIdForIndividual(subject);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(classIdForSubject, subject, axiom.getAnnotations()));
		Integer classIdForObject = getIdGenerator().createOrGetClassIdForIndividual(object);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(classIdForObject, object, axiom.getAnnotations()));
		IntegerObjectSomeValuesFrom restriction = getOntologyObjectFactory().getDataTypeFactory()
				.createObjectSomeValuesFrom(property,
						getOntologyObjectFactory().getDataTypeFactory().createClass(classIdForObject));
		Set<IntegerClassExpression> classExpressionSet = new HashSet<>();
		classExpressionSet.add(getOntologyObjectFactory().getDataTypeFactory().createClass(classIdForSubject));
		classExpressionSet.add(restriction);
		IntegerObjectIntersectionOf intersection = getOntologyObjectFactory().getDataTypeFactory()
				.createObjectIntersectionOf(classExpressionSet);
		ret.add(getComplexAxiomFactory().createSubClassOfAxiom(intersection,
				getOntologyObjectFactory().getDataTypeFactory().createClass(IntegerEntityManager.bottomClassId),
				axiom.getAnnotations()));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerObjectPropertyAssertionAxiom axiom) {
		Objects.requireNonNull(axiom);
		Integer subject = axiom.getSubject();
		Integer object = axiom.getObject();
		IntegerObjectPropertyExpression property = axiom.getProperty();
		Set<IntegerAxiom> ret = new HashSet<>();
		Integer classIdForSubject = getIdGenerator().createOrGetClassIdForIndividual(subject);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(classIdForSubject, subject, axiom.getAnnotations()));
		Integer classIdForObject = getIdGenerator().createOrGetClassIdForIndividual(object);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(classIdForObject, object, axiom.getAnnotations()));
		IntegerObjectSomeValuesFrom restriction = getOntologyObjectFactory().getDataTypeFactory()
				.createObjectSomeValuesFrom(property,
						getOntologyObjectFactory().getDataTypeFactory().createClass(classIdForObject));
		ret.add(getComplexAxiomFactory().createSubClassOfAxiom(
				getOntologyObjectFactory().getDataTypeFactory().createClass(classIdForSubject), restriction,
				axiom.getAnnotations()));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerObjectPropertyDeclarationAxiom axiom) {
		Objects.requireNonNull(axiom);
		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerPropertyRangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = Collections.emptySet();
		IntegerClassExpression range = axiom.getRange();
		if (range.isLiteral()) {
			Integer property = getObjectPropertyId(axiom.getProperty());
			NormalizedIntegerAxiom normAxiom = getNormalizedAxiomFactory().createRangeAxiom(property,
					((IntegerClass) range).getId(), axiom.getAnnotations());
			ret = new HashSet<>();
			ret.add(normAxiom);
		} else {
			ret = this.norNR1_2.apply(axiom);
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerReflexiveObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		IntegerAxiom newAxiom = getNormalizedAxiomFactory().createRI1Axiom(getObjectPropertyId(axiom.getProperty()),
				axiom.getAnnotations());
		ret.add(newAxiom);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSameIndividualAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		Set<Integer> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classExprSet = new HashSet<>();
		individualSet.forEach(individual -> {
			Integer classForIndiv = getIdGenerator().createOrGetClassIdForIndividual(individual);
			ret.add(getNormalizedAxiomFactory().createNominalAxiom(classForIndiv, individual, axiom.getAnnotations()));
			classExprSet.add(getOntologyObjectFactory().getDataTypeFactory().createClass(classForIndiv));
		});
		ret.add(getComplexAxiomFactory().createEquivalentClassesAxiom(classExprSet, axiom.getAnnotations()));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubClassOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		if (!this.norNR1_6.canBeApplied(axiom)) {
			for (Iterator<NormalizationRule> it = this.norChainOfSubClass.iterator(); it.hasNext() && ret.isEmpty();) {
				ret = it.next().apply(axiom);
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubObjectPropertyOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		Integer subProperty = getObjectPropertyId(axiom.getSubProperty());
		Integer superProperty = getObjectPropertyId(axiom.getSuperProperty());
		Integer inverseSubProperty = getIdGenerator().createOrGetInverseObjectPropertyOf(subProperty);
		Integer inverseSuperProperty = getIdGenerator().createOrGetInverseObjectPropertyOf(superProperty);
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(subProperty, superProperty, axiom.getAnnotations()));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(inverseSubProperty, inverseSuperProperty,
				axiom.getAnnotations()));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubPropertyChainOfAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		if (ret.isEmpty()) {
			ret = this.norNR2_1.apply(axiom);
		}
		if (ret.isEmpty()) {
			ret = this.norSubPropertyChainOf.apply(axiom);
		}

		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		Integer property = getObjectPropertyId(axiom.getProperty());
		NormalizedIntegerAxiom normAxiom = getNormalizedAxiomFactory().createRI3Axiom(property, property, property,
				axiom.getAnnotations());
		ret.add(normAxiom);
		return Collections.unmodifiableSet(ret);
	}

}
