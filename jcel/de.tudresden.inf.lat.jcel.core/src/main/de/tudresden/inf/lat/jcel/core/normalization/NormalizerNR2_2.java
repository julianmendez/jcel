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
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;

/**
 * This rule is applied to all non atomic concepts. This is slightly different
 * from the original rule, which applies to one concept each time. The original
 * rule is:
 * 
 * <ul>
 * <li>NR 2-2 : C<sub>1</sub> &#8851; &hellip; &#8851; C' &#8851; &hellip;
 * &#8851; C<sub>n</sub> &#8849; D &#8605; C' &#8849; A, C<sub>1</sub> &#8851;
 * &hellip; &#8851; A &#8851; &hellip; &#8851; C<sub>n</sub> &#8849; D</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_2 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerSubClassOfAxiom classAxiom,
			IdGenerator nameGenerator) {
		if (classAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (nameGenerator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();

		IntegerClassExpression subClass = classAxiom.getSubClass();
		if (!subClass.isLiteral()) {
			IntegerClassExpression superClass = classAxiom.getSuperClass();
			if (subClass instanceof IntegerObjectIntersectionOf) {
				IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
				Set<IntegerClassExpression> operands = intersection
						.getOperands();
				Set<IntegerAxiom> newSet = applyRule(operands, superClass,
						nameGenerator);
				if (newSet.size() > 1) {
					ret = newSet;
				}
			}
		}
		return ret;
	}

	private static Set<IntegerAxiom> applyRule(
			Set<IntegerClassExpression> operands,
			IntegerClassExpression superClass, IdGenerator nameGenerator) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<IntegerClassExpression> newOperands = new HashSet<IntegerClassExpression>();
		boolean applied = false;
		for (IntegerClassExpression classExpression : operands) {
			if (classExpression.isLiteral()) {
				newOperands.add(classExpression);
			} else {
				applied = true;
				IntegerClass newClass = new IntegerClass(nameGenerator
						.createNewClassId());
				ret.add(new IntegerSubClassOfAxiom(classExpression, newClass));
				newOperands.add(newClass);
			}
		}
		if (applied) {
			IntegerObjectIntersectionOf newIntersection = new IntegerObjectIntersectionOf(
					newOperands);
			ret.add(new IntegerSubClassOfAxiom(newIntersection, superClass));
		}
		return ret;
	}

	public NormalizerNR2_2() {
	}
}
