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

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeValuesFrom;

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 2-3 : &exist; r <i>.</i> C' &#8849; D &#8605; C' &#8849; A, &exist; r
 * <i>.</i> A &#8849; D</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_3 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerSubClassOfAxiom classAxiom,
			IdGenerator nameGenerator) {
		Set<IntegerAxiom> ret = null;
		IntegerClassExpression subClass = classAxiom.getSubClass();
		IntegerClassExpression superClass = classAxiom.getSuperClass();
		if (subClass instanceof IntegerObjectSomeValuesFrom) {
			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			Integer property = restriction.getProperty();
			IntegerClassExpression filler = restriction.getFiller();
			if (!filler.isLiteral()) {
				ret = new HashSet<IntegerAxiom>();
				IntegerClass newClass = new IntegerClass(nameGenerator
						.createNewClassId());
				IntegerObjectSomeValuesFrom newExistential = new IntegerObjectSomeValuesFrom(
						property, newClass);
				ret.add(new IntegerSubClassOfAxiom(filler, newClass));
				ret.add(new IntegerSubClassOfAxiom(newExistential, superClass));
			}
		}
		return ret;
	}

	public NormalizerNR2_3() {
	}
}
