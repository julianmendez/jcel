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

package de.tudresden.inf.lat.jcel.core.normalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerClassAssertionAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDifferentIndividualsAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerNamedIndividualDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerNegativeObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSameIndividualAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI0Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.NominalAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RangeAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object of this class models is a visitor used to normalize axioms. This is
 * an auxiliary class used by <code>OntologyNormalizer</code>.
 * 
 * @author Julian Mendez
 * 
 * @see OntologyNormalizer
 */
class SimpleNormalizer implements ComplexIntegerAxiomVisitor<Set<IntegerAxiom>> {

	private IdGenerator nameGenerator = null;

	public SimpleNormalizer(IdGenerator nameGen) {
		if (nameGen == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.nameGenerator = nameGen;
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

	private IdGenerator getNameGenerator() {
		return this.nameGenerator;
	}

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

	private Collection<NormalizedIntegerAxiom> simplify(
			IntegerSubClassOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<NormalizedIntegerAxiom>();
		IntegerClassExpression subClass = axiom.getSubClass();
		IntegerClassExpression superClass = axiom.getSuperClass();

		if (subClass.isLiteral() && superClass.isLiteral()) {
			ret.add(new GCI0Axiom(((IntegerClass) subClass).getId(),
					((IntegerClass) superClass).getId()));

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectIntersectionOf)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
			Set<IntegerClassExpression> operands = intersection.getOperands();
			ret.add(new GCI1Axiom(convertToClassList(operands),
					((IntegerClass) superClass).getId()));

		} else if (subClass.isLiteral() && !superClass.isLiteral()
				&& (superClass instanceof IntegerObjectSomeValuesFrom)
				&& superClass.hasOnlyLiterals()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) superClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			ret.add(new GCI2Axiom(((IntegerClass) subClass).getId(),
					restriction.getProperty(), filler.getId()));

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectSomeValuesFrom)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			ret.add(new GCI3Axiom(restriction.getProperty(), filler.getId(),
					((IntegerClass) superClass).getId()));

		}
		return ret;
	}

	private Collection<NormalizedIntegerAxiom> simplify(
			IntegerSubPropertyChainOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<NormalizedIntegerAxiom>();
		List<Integer> propChain = axiom.getPropertyChain();
		Integer rightPart = axiom.getSuperProperty();

		if (propChain.size() == 0) {

			ret.add(new RI1Axiom(rightPart));

		} else if (propChain.size() == 1) {

			Iterator<Integer> it = propChain.iterator();
			Integer leftPropExpr = it.next();
			ret.add(new RI2Axiom(leftPropExpr, rightPart));

		} else if (propChain.size() == 2) {

			Iterator<Integer> it = propChain.iterator();
			Integer leftLeftProp = it.next();
			Integer leftRightProp = it.next();
			ret.add(new RI3Axiom(leftLeftProp, leftRightProp, rightPart));

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
		Integer classId = getNameGenerator().createOrGetClassIdForIndividual(
				individual);
		ret.add(new NominalAxiom(classId, individual));
		ret.add(new IntegerSubClassOfAxiom(new IntegerClass(classId),
				classExpression));
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
	public Set<IntegerAxiom> visit(IntegerDifferentIndividualsAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<Integer> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classExprSet = new HashSet<IntegerClassExpression>();
		for (Integer individual : individualSet) {
			Integer classForIndiv = getNameGenerator()
					.createOrGetClassIdForIndividual(individual);
			ret.add(new NominalAxiom(classForIndiv, individual));
			classExprSet.add(new IntegerClass(classForIndiv));
		}
		ret.add(new IntegerDisjointClassesAxiom(classExprSet));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDisjointClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return NormalizerDisjoint.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return NormalizerNR1_5.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return NormalizerEquivProperties.apply(axiom);
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
		Integer subject = axiom.getObject();
		Integer property = axiom.getProperty();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer classIdForObject = getNameGenerator()
				.createOrGetClassIdForIndividual(object);
		ret.add(new NominalAxiom(classIdForObject, object));
		Integer classIdForSubject = getNameGenerator()
				.createOrGetClassIdForIndividual(subject);
		ret.add(new NominalAxiom(classIdForObject, subject));
		IntegerObjectSomeValuesFrom restriction = new IntegerObjectSomeValuesFrom(
				property, new IntegerClass(classIdForObject));
		Set<IntegerClassExpression> classExpressionSet = new HashSet<IntegerClassExpression>();
		classExpressionSet.add(new IntegerClass(classIdForSubject));
		classExpressionSet.add(restriction);
		IntegerObjectIntersectionOf intersection = new IntegerObjectIntersectionOf(
				classExpressionSet);
		ret.add(new IntegerSubClassOfAxiom(intersection, new IntegerClass(
				IntegerClass.classBottomElement)));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer object = axiom.getObject();
		Integer subject = axiom.getObject();
		Integer property = axiom.getProperty();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer classIdForObject = getNameGenerator()
				.createOrGetClassIdForIndividual(object);
		ret.add(new NominalAxiom(classIdForObject, object));
		Integer classIdForSubject = getNameGenerator()
				.createOrGetClassIdForIndividual(subject);
		ret.add(new NominalAxiom(classIdForObject, subject));
		IntegerObjectSomeValuesFrom restriction = new IntegerObjectSomeValuesFrom(
				property, new IntegerClass(classIdForObject));
		ret.add(new IntegerSubClassOfAxiom(new IntegerClass(classIdForSubject),
				restriction));
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
			Integer property = axiom.getProperty();
			NormalizedIntegerAxiom normAxiom = new RangeAxiom(property,
					((IntegerClass) range).getId());
			ret = new HashSet<IntegerAxiom>();
			ret.add(normAxiom);
		} else {
			ret = NormalizerNR1_2.apply(axiom, getNameGenerator());
		}
		return ret;
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
			Integer classForIndiv = getNameGenerator()
					.createOrGetClassIdForIndividual(individual);
			ret.add(new NominalAxiom(classForIndiv, individual));
			classExprSet.add(new IntegerClass(classForIndiv));
		}
		ret.add(new IntegerEquivalentClassesAxiom(classExprSet));
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubClassOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		ret.addAll(simplify(axiom));
		if (ret.isEmpty() && !NormalizerNR1_6.canBeApplied(axiom)) {
			if (ret.isEmpty()) {
				ret = NormalizerNR1_7.apply(axiom);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR2_2.apply(axiom, this.nameGenerator);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR2_3.apply(axiom, this.nameGenerator);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR3_1.apply(axiom, this.nameGenerator);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR3_2.apply(axiom, this.nameGenerator);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR3_3.apply(axiom);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR4_1.apply(axiom, this.nameGenerator);
			}
			if (ret.isEmpty()) {
				ret = NormalizerNR4_2.apply(axiom, this.nameGenerator);
			}
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubPropertyChainOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		ret.addAll(simplify(axiom));
		if (ret.isEmpty()) {
			ret = NormalizerNR2_1.apply(axiom, getNameGenerator());
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Integer property = axiom.getProperty();
		NormalizedIntegerAxiom normAxiom = new RI3Axiom(property, property,
				property);
		ret.add(normAxiom);
		return ret;
	}
}
