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
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeRestriction;

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 3-2 : B &sube; &exist; r <i>.</i> C' &rarr; &rarr; B &sube; &exist; r
 * <i>.</i> A, A &sube; C'</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR3_2 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerSubClassAxiom classAxiom,
			IdGenerator nameGenerator) {
		Set<IntegerAxiom> ret = null;
		IntegerDescription subClass = classAxiom.getSubClass();
		IntegerDescription superClass = classAxiom.getSuperClass();
		if (subClass.isLiteral()
				&& superClass instanceof IntegerObjectSomeRestriction) {
			IntegerObjectSomeRestriction restriction = (IntegerObjectSomeRestriction) superClass;
			Integer property = restriction.getProperty();
			IntegerDescription filler = restriction.getFiller();
			if (!filler.isLiteral()) {
				ret = new HashSet<IntegerAxiom>();
				IntegerClass newClass = new IntegerClass(nameGenerator
						.createNewClassId());
				IntegerObjectSomeRestriction newExistential = new IntegerObjectSomeRestriction(
						property, newClass);
				ret.add(new IntegerSubClassAxiom(subClass, newExistential));
				ret.add(new IntegerSubClassAxiom(newClass, filler));
			}
		}
		return ret;
	}

	public NormalizerNR3_2() {
	}
}
