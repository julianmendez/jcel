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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI0Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI3Axiom;
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
	protected List<Integer> convertToClassList(
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

	protected IdGenerator getNameGenerator() {
		return this.nameGenerator;
	}

	public Set<IntegerAxiom> normalize(IntegerAxiom axiom) {
		Set<IntegerAxiom> ret = null;
		if (axiom instanceof ComplexIntegerAxiom) {
			ComplexIntegerAxiom complexAxiom = (ComplexIntegerAxiom) axiom;
			ret = complexAxiom.accept(this);
		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(IntegerPropertyRangeAxiom axiom) {
		NormalizedIntegerAxiom ret = null;
		Integer property = axiom.getProperty();
		IntegerClassExpression range = axiom.getRange();
		if (range.isLiteral()) {
			ret = new RangeAxiom(property, ((IntegerClass) range).getId());
		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(IntegerSubClassOfAxiom axiom) {
		NormalizedIntegerAxiom ret = null;
		IntegerClassExpression subClass = axiom.getSubClass();
		IntegerClassExpression superClass = axiom.getSuperClass();

		if (subClass.isLiteral() && superClass.isLiteral()) {
			ret = new GCI0Axiom(((IntegerClass) subClass).getId(),
					((IntegerClass) superClass).getId());

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectIntersectionOf)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
			Set<IntegerClassExpression> operands = intersection.getOperands();
			ret = new GCI1Axiom(convertToClassList(operands),
					((IntegerClass) superClass).getId());

		} else if (subClass.isLiteral() && !superClass.isLiteral()
				&& (superClass instanceof IntegerObjectSomeValuesFrom)
				&& superClass.hasOnlyLiterals()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) superClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			ret = new GCI2Axiom(((IntegerClass) subClass).getId(), restriction
					.getProperty(), filler.getId());

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectSomeValuesFrom)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			ret = new GCI3Axiom(restriction.getProperty(), filler.getId(),
					((IntegerClass) superClass).getId());

		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(
			IntegerSubPropertyChainOfAxiom axiom) {
		NormalizedIntegerAxiom ret = null;
		List<Integer> propChain = axiom.getPropertyChain();
		Integer rightPart = axiom.getSuperProperty();

		if (propChain.size() == 0) {

			ret = new RI1Axiom(rightPart);

		} else if (propChain.size() == 1) {

			Iterator<Integer> it = propChain.iterator();
			Integer leftPropExpr = it.next();
			ret = new RI2Axiom(leftPropExpr, rightPart);

		} else if (propChain.size() == 2) {

			Iterator<Integer> it = propChain.iterator();
			Integer leftLeftProp = it.next();
			Integer leftRightProp = it.next();
			ret = new RI3Axiom(leftLeftProp, leftRightProp, rightPart);

		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(
			IntegerTransitiveObjectPropertyAxiom axiom) {
		Integer property = axiom.getProperty();
		return new RI3Axiom(property, property, property);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerClassDeclarationAxiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerDisjointClassesAxiom axiom) {
		return NormalizerDisjoint.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentClassesAxiom axiom) {
		return NormalizerNR1_5.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		return NormalizerEquivProperties.apply(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerObjectPropertyDeclarationAxiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerPropertyRangeAxiom axiom) {
		Set<IntegerAxiom> ret = null;
		NormalizedIntegerAxiom normAxiom = simplify(axiom);
		if (normAxiom != null) {
			ret = new HashSet<IntegerAxiom>();
			ret.add(normAxiom);
		} else {
			ret = NormalizerNR1_2.apply(axiom, getNameGenerator());
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubClassOfAxiom axiom) {
		Set<IntegerAxiom> ret = null;
		if (ret == null) {
			NormalizedIntegerAxiom normAxiom = simplify(axiom);
			if (normAxiom != null) {
				ret = new HashSet<IntegerAxiom>();
				ret.add(normAxiom);
			}
		}
		if (ret == null) {
			ret = NormalizerNR1_6.apply(axiom);
		}
		if (ret == null) {
			ret = NormalizerNR1_7.apply(axiom);
		}
		if (ret == null) {
			ret = NormalizerNR2_2.apply(axiom, this.nameGenerator);
		}
		if (ret == null) {
			ret = NormalizerNR2_3.apply(axiom, this.nameGenerator);
		}
		if (ret == null) {
			ret = NormalizerNR3_1.apply(axiom, this.nameGenerator);
		}
		if (ret == null) {
			ret = NormalizerNR3_2.apply(axiom, this.nameGenerator);
		}
		if (ret == null) {
			ret = NormalizerNR3_3.apply(axiom);
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerSubPropertyChainOfAxiom axiom) {
		Set<IntegerAxiom> ret = null;
		NormalizedIntegerAxiom normAxiom = simplify(axiom);
		if (normAxiom != null) {
			ret = new HashSet<IntegerAxiom>();
			ret.add(normAxiom);
		} else {
			ret = NormalizerNR2_1.apply(axiom, getNameGenerator());
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		Set<IntegerAxiom> ret = null;
		NormalizedIntegerAxiom normAxiom = simplify(axiom);
		if (normAxiom != null) {
			ret = new HashSet<IntegerAxiom>();
			ret.add(normAxiom);
		}
		return ret;
	}
}
