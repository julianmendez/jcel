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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
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
class SimpleNormalizer implements ComplexIntegerAxiomVisitor<Set<IntegerAxiom>> {

	private List<NormalizationRule> norChainOfSubClass = null;
	private NormalizationRule norDisjoint = null;
	private NormalizationRule norEquivProperties = null;
	private NormalizationRule norNR1_2 = null;
	private NormalizationRule norNR1_5 = null;
	private NormalizerNR1_6 norNR1_6 = null;
	private NormalizationRule norNR2_1 = null;
	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer.
	 * 
	 * @param factory
	 *            factory
	 */
	public SimpleNormalizer(IntegerOntologyObjectFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.ontologyObjectFactory = factory;
		reset();
	}

	/**
	 * Returns a list of classes occurring in a set of class expressions.
	 * 
	 * @param operands
	 *            the set of class expressions
	 * @return a list of classes occurring in a set of class expressions
	 * @throws IllegalStateException
	 *             if there is any invalid class expression
	 */
	private List<Integer> convertToClassList(
			Set<IntegerClassExpression> operands) {
		List<Integer> ret = new ArrayList<Integer>();
		for (IntegerClassExpression classExpression : operands) {
			if (classExpression.isLiteral()) {
				IntegerClass c = (IntegerClass) classExpression;
				ret.add(c.getId());
			} else {
				throw new IllegalStateException(
						" Invalid state in class expression set.");
			}
		}
		return ret;
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
	public Set<NormalizedIntegerAxiom> getAxiomsForInverseObjectProperties(
			Integer firstProperty, Integer secondProperty) {
		if (firstProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (secondProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		Integer inverseFirstProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(firstProperty);
		Integer inverseSecondProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(secondProperty);
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(firstProperty,
				inverseSecondProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(
				inverseSecondProperty, firstProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(secondProperty,
				inverseFirstProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(
				inverseFirstProperty, secondProperty));
		return Collections.unmodifiableSet(ret);

	}

	private ComplexIntegerAxiomFactory getComplexAxiomFactory() {
		return getOntologyObjectFactory().getComplexAxiomFactory();
	}

	private IntegerEntityManager getIdGenerator() {
		return getOntologyObjectFactory().getIdGenerator();
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
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof ComplexIntegerAxiom) {
			ComplexIntegerAxiom complexAxiom = (ComplexIntegerAxiom) axiom;
			ret = complexAxiom.accept(this);
		}
		return ret;
	}

	private void reset() {

		this.norChainOfSubClass = new ArrayList<NormalizationRule>();
		this.norChainOfSubClass.add(new NormalizerNR1_7(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR2_2(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR2_3(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR3_1(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR3_2(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR3_3(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR4_1(
				getOntologyObjectFactory()));
		this.norChainOfSubClass.add(new NormalizerNR4_2(
				getOntologyObjectFactory()));

		this.norDisjoint = new NormalizerDisjoint(getOntologyObjectFactory());
		this.norEquivProperties = new NormalizerEquivProperties(
				getOntologyObjectFactory());
		this.norNR1_5 = new NormalizerNR1_5(getOntologyObjectFactory());
		this.norNR1_2 = new NormalizerNR1_2(getOntologyObjectFactory());
		this.norNR2_1 = new NormalizerNR2_1(getOntologyObjectFactory());
		this.norNR1_6 = new NormalizerNR1_6();
	}

	private Collection<NormalizedIntegerAxiom> simplify(
			IntegerSubClassOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<NormalizedIntegerAxiom>();
		IntegerClassExpression subClass = axiom.getSubClass();
		IntegerClassExpression superClass = axiom.getSuperClass();

		if (subClass.isLiteral() && superClass.isLiteral()) {
			ret.add(getNormalizedAxiomFactory().createGCI0Axiom(
					((IntegerClass) subClass).getId(),
					((IntegerClass) superClass).getId()));

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectIntersectionOf)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
			Set<IntegerClassExpression> operands = intersection.getOperands();
			ret.add(getNormalizedAxiomFactory().createGCI1Axiom(
					convertToClassList(operands),
					((IntegerClass) superClass).getId()));

		} else if (subClass.isLiteral() && !superClass.isLiteral()
				&& (superClass instanceof IntegerObjectSomeValuesFrom)
				&& superClass.hasOnlyLiterals()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) superClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			Integer property = getObjectPropertyId(restriction.getProperty());
			ret.add(getNormalizedAxiomFactory()
					.createGCI2Axiom(((IntegerClass) subClass).getId(),
							property, filler.getId()));

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectSomeValuesFrom)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			Integer property = getObjectPropertyId(restriction.getProperty());
			ret.add(getNormalizedAxiomFactory().createGCI3Axiom(property,
					filler.getId(), ((IntegerClass) superClass).getId()));

		}
		return ret;
	}

	private Collection<NormalizedIntegerAxiom> simplify(
			IntegerSubPropertyChainOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<NormalizedIntegerAxiom>();
		List<IntegerObjectPropertyExpression> propChain = axiom
				.getPropertyChain();
		IntegerObjectPropertyExpression rightPart = axiom.getSuperProperty();

		if (propChain.size() == 0) {

			ret.add(getNormalizedAxiomFactory().createRI1Axiom(
					getObjectPropertyId(rightPart)));

		} else if (propChain.size() == 1) {

			Iterator<IntegerObjectPropertyExpression> it = propChain.iterator();
			IntegerObjectPropertyExpression leftPropExpr = it.next();
			ret.add(getNormalizedAxiomFactory().createRI2Axiom(
					getObjectPropertyId(leftPropExpr),
					getObjectPropertyId(rightPart)));

		} else if (propChain.size() == 2) {

			Iterator<IntegerObjectPropertyExpression> it = propChain.iterator();
			IntegerObjectPropertyExpression leftLeftProp = it.next();
			IntegerObjectPropertyExpression leftRightProp = it.next();
			ret.add(getNormalizedAxiomFactory().createRI3Axiom(
					getObjectPropertyId(leftLeftProp),
					getObjectPropertyId(leftRightProp),
					getObjectPropertyId(rightPart)));

		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerClassAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer individual = axiom.getIndividual();
		IntegerClassExpression classExpression = axiom.getClassExpression();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer classId = getIdGenerator().createOrGetClassIdForIndividual(
				individual);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(classId,
				individual));
		ret.add(getComplexAxiomFactory().createSubClassOfAxiom(
				getOntologyObjectFactory().getDataTypeFactory().createClass(
						classId), classExpression));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerClassDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		// TODO Auto-generated method stub
		// FIXME not implemented yet

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDataPropertyDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDifferentIndividualsAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<Integer> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classExprSet = new HashSet<IntegerClassExpression>();
		for (Integer individual : individualSet) {
			Integer classForIndiv = getIdGenerator()
					.createOrGetClassIdForIndividual(individual);
			ret.add(getNormalizedAxiomFactory().createNominalAxiom(
					classForIndiv, individual));
			classExprSet.add(getOntologyObjectFactory().getDataTypeFactory()
					.createClass(classForIndiv));
		}
		ret.add(getComplexAxiomFactory().createDisjointClassesAxiom(
				classExprSet));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDisjointClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.norDisjoint.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.norNR1_5.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.norEquivProperties.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		IntegerAxiom newAxiom = getNormalizedAxiomFactory()
				.createFunctObjectPropAxiom(
						getObjectPropertyId(axiom.getProperty()));
		ret.add(newAxiom);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(
			IntegerInverseFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerAxiom newAxiom = getNormalizedAxiomFactory()
				.createFunctObjectPropAxiom(
						getIdGenerator().createOrGetInverseObjectPropertyOf(
								getObjectPropertyId(axiom.getProperty())));
		return Collections.singleton(newAxiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerInverseObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer firstProperty = getObjectPropertyId(axiom.getFirstProperty());
		Integer secondProperty = getObjectPropertyId(axiom.getSecondProperty());
		getIdGenerator().proposeInverseObjectPropertyOf(firstProperty,
				secondProperty);
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		ret.addAll(getAxiomsForInverseObjectProperties(firstProperty,
				secondProperty));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerNamedIndividualDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(
			IntegerNegativeObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");

		}

		Integer object = axiom.getObject();
		Integer subject = axiom.getSubject();
		IntegerObjectPropertyExpression property = axiom.getProperty();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer classIdForObject = getIdGenerator()
				.createOrGetClassIdForIndividual(object);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(
				classIdForObject, object));
		Integer classIdForSubject = getIdGenerator()
				.createOrGetClassIdForIndividual(subject);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(
				classIdForObject, subject));
		IntegerObjectSomeValuesFrom restriction = getOntologyObjectFactory()
				.getDataTypeFactory().createObjectSomeValuesFrom(
						property,
						getOntologyObjectFactory().getDataTypeFactory()
								.createClass(classIdForObject));
		Set<IntegerClassExpression> classExpressionSet = new HashSet<IntegerClassExpression>();
		classExpressionSet.add(getOntologyObjectFactory().getDataTypeFactory()
				.createClass(classIdForSubject));
		classExpressionSet.add(restriction);
		IntegerObjectIntersectionOf intersection = getOntologyObjectFactory()
				.getDataTypeFactory().createObjectIntersectionOf(
						classExpressionSet);
		ret.add(getComplexAxiomFactory().createSubClassOfAxiom(
				intersection,
				getOntologyObjectFactory().getDataTypeFactory().createClass(
						IntegerEntityManager.bottomClassId)));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer object = axiom.getObject();
		Integer subject = axiom.getSubject();
		IntegerObjectPropertyExpression property = axiom.getProperty();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer classIdForObject = getIdGenerator()
				.createOrGetClassIdForIndividual(object);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(
				classIdForObject, object));
		Integer classIdForSubject = getIdGenerator()
				.createOrGetClassIdForIndividual(subject);
		ret.add(getNormalizedAxiomFactory().createNominalAxiom(
				classIdForObject, subject));
		IntegerObjectSomeValuesFrom restriction = getOntologyObjectFactory()
				.getDataTypeFactory().createObjectSomeValuesFrom(
						property,
						getOntologyObjectFactory().getDataTypeFactory()
								.createClass(classIdForObject));
		ret.add(getComplexAxiomFactory().createSubClassOfAxiom(
				getOntologyObjectFactory().getDataTypeFactory().createClass(
						classIdForSubject), restriction));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerObjectPropertyDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		IntegerClassExpression range = axiom.getRange();
		if (range.isLiteral()) {
			Integer property = getObjectPropertyId(axiom.getProperty());
			NormalizedIntegerAxiom normAxiom = getNormalizedAxiomFactory()
					.createRangeAxiom(property, ((IntegerClass) range).getId());
			ret = new HashSet<IntegerAxiom>();
			ret.add(normAxiom);
		} else {
			ret = this.norNR1_2.apply(axiom);
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerReflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		IntegerAxiom newAxiom = getNormalizedAxiomFactory().createRI1Axiom(
				getObjectPropertyId(axiom.getProperty()));
		ret.add(newAxiom);
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSameIndividualAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<Integer> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classExprSet = new HashSet<IntegerClassExpression>();
		for (Integer individual : individualSet) {
			Integer classForIndiv = getIdGenerator()
					.createOrGetClassIdForIndividual(individual);
			ret.add(getNormalizedAxiomFactory().createNominalAxiom(
					classForIndiv, individual));
			classExprSet.add(getOntologyObjectFactory().getDataTypeFactory()
					.createClass(classForIndiv));
		}
		ret.add(getComplexAxiomFactory().createEquivalentClassesAxiom(
				classExprSet));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubClassOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		ret.addAll(simplify(axiom));
		if (ret.isEmpty() && !this.norNR1_6.canBeApplied(axiom)) {
			for (Iterator<NormalizationRule> it = this.norChainOfSubClass
					.iterator(); it.hasNext() && ret.isEmpty();) {
				ret = it.next().apply(axiom);
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubObjectPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer subProperty = getObjectPropertyId(axiom.getSubProperty());
		Integer superProperty = getObjectPropertyId(axiom.getSuperProperty());
		Integer inverseSubProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(subProperty);
		Integer inverseSuperProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(superProperty);
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(subProperty,
				superProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(inverseSubProperty,
				inverseSuperProperty));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubPropertyChainOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		ret.addAll(simplify(axiom));
		if (ret.isEmpty()) {
			ret = this.norNR2_1.apply(axiom);
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer property = getObjectPropertyId(axiom.getProperty());
		NormalizedIntegerAxiom normAxiom = getNormalizedAxiomFactory()
				.createRI3Axiom(property, property, property);
		ret.add(normAxiom);
		return Collections.unmodifiableSet(ret);
	}

}
