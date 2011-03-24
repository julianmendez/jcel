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

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 1-7 : C &sube; D<sup>&#8869;</sup> &rarr; &rarr; C &sube; &#8869;</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_7 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerSubClassAxiom classAxiom) {
		Set<IntegerAxiom> ret = null;
		IntegerDescription superClass = classAxiom.getSuperClass();
		if (superClass.containsBottom() && !superClass.isLiteral()) {
			ret = new HashSet<IntegerAxiom>();
			IntegerSubClassAxiom axiom = new IntegerSubClassAxiom(classAxiom
					.getSubClass(),
					new IntegerClass(IntegerDescription.NOTHING));
			ret.add(axiom);
		}
		return ret;
	}

	public NormalizerNR1_7() {
	}
}
