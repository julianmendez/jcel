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

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * This class models a transformation rule that normalizes an axiom of
 * equivalent properties.
 * 
 * @author Julian Mendez
 */
class NormalizerEquivProperties implements NormalizationRule {

	public static Set<IntegerAxiom> apply(
			IntegerEquivalentObjectPropertiesAxiom equivPropAxiom) {
		if (equivPropAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> expressionSet = equivPropAxiom.getProperties();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		for (Iterator<Integer> firstIt = expressionSet.iterator(); firstIt
				.hasNext();) {
			Integer firstExpression = firstIt.next();
			for (Iterator<Integer> secondIt = expressionSet.iterator(); secondIt
					.hasNext();) {
				Integer secondExpression = secondIt.next();
				RI2Axiom subPropertyAxiom = new RI2Axiom(firstExpression,
						secondExpression);
				ret.add(subPropertyAxiom);
			}
		}
		return ret;
	}

	public NormalizerEquivProperties() {
	}
}
