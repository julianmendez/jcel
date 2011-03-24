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

import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;

/**
 * This rule is applied to all non atomic concepts. This is slightly different
 * from the original rule, which applies to one concept each time. The original
 * rule is:
 * 
 * <ul>
 * <li>NR 2-2 : C<sub>1</sub> &cap; &hellip; &cap; C' &cap; &hellip; &cap;
 * C<sub>n</sub> &sube; D &rarr; &rarr; C' &sube; A, C<sub>1</sub> &cap;
 * &hellip; &cap; A &cap; &hellip; &cap; C<sub>n</sub> &sube; D</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_2 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerSubClassAxiom classAxiom,
			IdGenerator nameGenerator) {
		Set<IntegerAxiom> ret = null;

		IntegerDescription subClass = classAxiom.getSubClass();
		if (!subClass.isLiteral()) {
			IntegerDescription superClass = classAxiom.getSuperClass();
			if (subClass instanceof IntegerObjectIntersectionOf) {
				IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
				Set<IntegerDescription> operands = intersection.getOperands();
				Set<IntegerAxiom> newSet = applyRule(operands, superClass,
						nameGenerator);
				if (newSet.size() > 1) {
					ret = newSet;
				}
			}
		}
		return ret;
	}

	protected static Set<IntegerAxiom> applyRule(
			Set<IntegerDescription> operands, IntegerDescription superClass,
			IdGenerator nameGenerator) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<IntegerDescription> newOperands = new HashSet<IntegerDescription>();
		boolean applied = false;
		for (IntegerDescription description : operands) {
			if (description.isLiteral()) {
				newOperands.add(description);
			} else {
				applied = true;
				IntegerClass newClass = new IntegerClass(nameGenerator
						.createNewClassId());
				ret.add(new IntegerSubClassAxiom(description, newClass));
				newOperands.add(newClass);
			}
		}
		if (applied) {
			IntegerObjectIntersectionOf newIntersection = new IntegerObjectIntersectionOf(
					newOperands);
			ret.add(new IntegerSubClassAxiom(newIntersection, superClass));
		}
		return ret;
	}

	public NormalizerNR2_2() {
	}
}
