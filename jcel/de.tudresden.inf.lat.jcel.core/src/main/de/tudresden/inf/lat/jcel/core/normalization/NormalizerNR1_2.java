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
import de.tudresden.inf.lat.jcel.core.axiom.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.RangeAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 1-2 : range(r) &sube; C &rarr;&rarr; range(r) &sube; A, A &sube; C</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_2 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerPropertyRangeAxiom rangeAxiom,
			IdGenerator nameGenerator) {
		Set<IntegerAxiom> ret = null;
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
