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
import java.util.Iterator;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 1-5 : C &#8801; D &rarr; &rarr; C &sube; D, D &sube; C</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_5 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(
			IntegerEquivalentClassesAxiom equivalentAxiom) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<IntegerClassExpression> classExpressionSet = equivalentAxiom
				.getClassExpressions();
		for (Iterator<IntegerClassExpression> firstIt = classExpressionSet
				.iterator(); firstIt.hasNext();) {
			IntegerClassExpression firstClassExpression = firstIt.next();
			for (Iterator<IntegerClassExpression> secondIt = classExpressionSet
					.iterator(); secondIt.hasNext();) {
				IntegerClassExpression secondClassExpression = secondIt.next();
				if (!firstClassExpression.equals(secondClassExpression)) {
					IntegerSubClassOfAxiom subClassAxiom = new IntegerSubClassOfAxiom(
							firstClassExpression, secondClassExpression);
					ret.add(subClassAxiom);
				}
			}
		}
		return ret;
	}

	public NormalizerNR1_5() {
	}
}
