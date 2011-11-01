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

import de.tudresden.inf.lat.jcel.core.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerObjectPropertyChainSubPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RangeAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeRestriction;

/**
 * An object of this class models is a visitor used to normalize axioms. This is
 * an auxiliary class used by <code>OntologyNormalizer</code>.
 * 
 * @author Julian Mendez
 * 
 * @see OntologyNormalizer
 */
class SimpleNormalizer implements IntegerAxiomVisitor<Set<IntegerAxiom>> {

	private IdGenerator nameGenerator = null;

	public SimpleNormalizer(IdGenerator nameGen) {
		this.nameGenerator = nameGen;
	}

	protected IdGenerator getNameGenerator() {
		return this.nameGenerator;
	}

	public Set<IntegerAxiom> normalize(IntegerAxiom axiom) {
		return axiom.accept(this);
	}

	/**
	 * Returns a list of classes occurring in a set of descriptions.
	 * 
	 * @param operands
	 *            the set of descriptions
	 * @return a list of classes occurring in a set of descriptions
	 * @throws IllegalStateException
	 *             if there is any invalid description
	 */
	protected List<Integer> convertToClassList(Set<IntegerDescription> operands) {
		List<Integer> ret = new ArrayList<Integer>();
		for (IntegerDescription description : operands) {
			if (description.isLiteral()) {
				IntegerClass c = (IntegerClass) description;
				ret.add(c.getId());
			} else {
				throw new IllegalStateException(
						" Invalid state in description set.");
			}
		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(
			IntegerObjectPropertyChainSubPropertyAxiom axiom) {
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

	protected NormalizedIntegerAxiom simplify(IntegerRangeAxiom axiom) {
		NormalizedIntegerAxiom ret = null;
		Integer property = axiom.getProperty();
		IntegerDescription range = axiom.getRange();
		if (range.isLiteral()) {
			ret = new RangeAxiom(property, ((IntegerClass) range).getId());
		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(IntegerSubClassAxiom axiom) {
		NormalizedIntegerAxiom ret = null;
		IntegerDescription subClass = axiom.getSubClass();
		IntegerDescription superClass = axiom.getSuperClass();

		if (subClass.isLiteral() && superClass.isLiteral()) {
			ret = new GCI0Axiom(((IntegerClass) subClass).getId(),
					((IntegerClass) superClass).getId());

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectIntersectionOf)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
			Set<IntegerDescription> operands = intersection.getOperands();
			ret = new GCI1Axiom(convertToClassList(operands),
					((IntegerClass) superClass).getId());

		} else if (subClass.isLiteral() && !superClass.isLiteral()
				&& (superClass instanceof IntegerObjectSomeRestriction)
				&& superClass.hasOnlyLiterals()) {

			IntegerObjectSomeRestriction restriction = (IntegerObjectSomeRestriction) superClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			ret = new GCI2Axiom(((IntegerClass) subClass).getId(), restriction
					.getProperty(), filler.getId());

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectSomeRestriction)
				&& subClass.hasOnlyLiterals()) {

			IntegerObjectSomeRestriction restriction = (IntegerObjectSomeRestriction) subClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			ret = new GCI3Axiom(restriction.getProperty(), filler.getId(),
					((IntegerClass) superClass).getId());

		}
		return ret;
	}

	protected NormalizedIntegerAxiom simplify(
			IntegerTransitiveObjectPropertyAxiom axiom) {
		Integer property = axiom.getProperty();
		return new RI3Axiom(property, property, property);
	}

	@Override
	public Set<IntegerAxiom> visit(GCI0Axiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(GCI1Axiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(GCI2Axiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(GCI3Axiom axiom) {
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
	public Set<IntegerAxiom> visit(
			IntegerObjectPropertyChainSubPropertyAxiom axiom) {
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
	public Set<IntegerAxiom> visit(IntegerRangeAxiom axiom) {
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
	public Set<IntegerAxiom> visit(IntegerSubClassAxiom axiom) {
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
	public Set<IntegerAxiom> visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		Set<IntegerAxiom> ret = null;
		NormalizedIntegerAxiom normAxiom = simplify(axiom);
		if (normAxiom != null) {
			ret = new HashSet<IntegerAxiom>();
			ret.add(normAxiom);
		}
		return ret;
	}

	@Override
	public Set<IntegerAxiom> visit(RangeAxiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(RI1Axiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(RI2Axiom axiom) {
		return null;
	}

	@Override
	public Set<IntegerAxiom> visit(RI3Axiom axiom) {
		return null;
	}
}
