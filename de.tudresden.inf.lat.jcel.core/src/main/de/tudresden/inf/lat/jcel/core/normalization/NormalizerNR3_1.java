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
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeValuesFrom;

/**
 * Modified version of NR3-1 . <br />
 * The original rule is:
 * 
 * <ul>
 * <li>NR 3-1 : C' &#8849; D' &#8605; C' &#8849; A, A &#8849; D'</li>
 * </ul>
 * 
 * This rule is:
 * 
 * <ul>
 * <li>NR 3-1 : C' &#8849; &exist; r <i>.</i> D &#8605; C' &#8849; A, A &#8849;
 * &exist; r <i>.</i> D</li>
 * </ul>
 * 
 * This rule could be considered a particular case of NR 2-2.
 * 
 * @author Julian Mendez
 */
class NormalizerNR3_1 implements NormalizationRule {

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
		IntegerClassExpression superClass = classAxiom.getSuperClass();
		if (!subClass.isLiteral()
				&& superClass instanceof IntegerObjectSomeValuesFrom) {
			ret = new HashSet<IntegerAxiom>();
			IntegerClass newClass = new IntegerClass(nameGenerator
					.createNewClassId());
			ret.add(new IntegerSubClassOfAxiom(subClass, newClass));
			ret.add(new IntegerSubClassOfAxiom(newClass, superClass));
		}
		return ret;
	}

	public NormalizerNR3_1() {
	}
}
