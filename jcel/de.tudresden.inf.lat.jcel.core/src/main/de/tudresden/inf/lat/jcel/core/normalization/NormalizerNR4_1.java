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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI0Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.NominalAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectOneOf;

/**
 * <p>
 * <ul>
 * <li>NR-4.1 : B &#8849; {a} &#8605; B &#8849; A , A &equiv; {a}</li>
 * </ul>
 * </p>
 * 
 * This rule is not part of the original set of rules.
 * 
 * @author Julian Mendez
 */
class NormalizerNR4_1 implements NormalizationRule {

	private IdGenerator nameGenerator = null;

	/**
	 * Constructs a new normalizer rule NR-4.1.
	 * 
	 * @param generator
	 *            an identifier generator
	 */
	public NormalizerNR4_1(IdGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.nameGenerator = generator;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubClassOfAxiom) {
			ret = applyRule((IntegerSubClassOfAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(IntegerSubClassOfAxiom classAxiom) {
		Set<IntegerAxiom> ret = Collections.emptySet();
		IntegerClassExpression subClass = classAxiom.getSubClass();
		IntegerClassExpression superClass = classAxiom.getSuperClass();
		if (subClass.isLiteral() && superClass instanceof IntegerObjectOneOf) {
			IntegerObjectOneOf oneOf = (IntegerObjectOneOf) superClass;
			ret = new HashSet<IntegerAxiom>();
			Integer individual = oneOf.getIndividual();
			Integer auxiliaryClass = this.nameGenerator
					.createOrGetClassIdForIndividual(individual);
			ret.add(new NominalAxiom(auxiliaryClass, individual));
			ret.add(new GCI0Axiom(((IntegerClass) subClass).getId(),
					auxiliaryClass));
		}
		return ret;
	}

}
