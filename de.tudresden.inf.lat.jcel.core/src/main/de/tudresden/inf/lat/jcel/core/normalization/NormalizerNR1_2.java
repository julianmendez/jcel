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

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RangeAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 1-2 : range(r) &#8849; C &#8605; range(r) &#8849; A, A &#8849; C</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_2 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerPropertyRangeAxiom rangeAxiom,
			IdGenerator nameGenerator) {
		if (rangeAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (nameGenerator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (!rangeAxiom.getRange().isLiteral()) {
			ret = new HashSet<IntegerAxiom>();
			Integer newClassId = nameGenerator.createNewClassId();
			ret.add(new RangeAxiom(rangeAxiom.getProperty(), newClassId));
			ret.add(new IntegerSubClassOfAxiom(new IntegerClass(newClassId),
					rangeAxiom.getRange()));
		}
		return ret;

	}

	public NormalizerNR1_2() {
	}
}
