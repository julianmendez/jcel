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
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;

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
		Set<IntegerDescription> descriptionSet = equivalentAxiom
				.getDescriptions();
		for (Iterator<IntegerDescription> firstIt = descriptionSet.iterator(); firstIt
				.hasNext();) {
			IntegerDescription firstDescription = firstIt.next();
			for (Iterator<IntegerDescription> secondIt = descriptionSet
					.iterator(); secondIt.hasNext();) {
				IntegerDescription secondDescription = secondIt.next();
				if (!firstDescription.equals(secondDescription)) {
					IntegerSubClassAxiom subClassAxiom = new IntegerSubClassAxiom(
							firstDescription, secondDescription);
					ret.add(subClassAxiom);
				}
			}
		}
		return ret;
	}

	public NormalizerNR1_5() {
	}
}
