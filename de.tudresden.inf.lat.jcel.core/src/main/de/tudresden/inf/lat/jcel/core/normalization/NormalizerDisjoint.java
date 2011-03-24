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
import de.tudresden.inf.lat.jcel.core.axiom.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;

/**
 * This class models a transformation rule that normalizes an axiom of disjoint
 * classes.
 * 
 * @author Julian Mendez
 */
class NormalizerDisjoint implements NormalizationRule {

	public static Set<IntegerAxiom> apply(
			IntegerDisjointClassesAxiom disjointAxiom) {
		Set<IntegerDescription> descriptionSet = disjointAxiom
				.getDescriptions();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		for (Iterator<IntegerDescription> firstIt = descriptionSet.iterator(); firstIt
				.hasNext();) {
			IntegerDescription firstDescription = firstIt.next();
			for (Iterator<IntegerDescription> secondIt = descriptionSet
					.iterator(); secondIt.hasNext();) {
				IntegerDescription secondDescription = secondIt.next();
				if (!firstDescription.equals(secondDescription)) {
					Set<IntegerDescription> pair = new HashSet<IntegerDescription>();
					pair.add(firstDescription);
					pair.add(secondDescription);
					IntegerObjectIntersectionOf intersection = new IntegerObjectIntersectionOf(
							pair);
					IntegerSubClassAxiom subClassAxiom = new IntegerSubClassAxiom(
							intersection, new IntegerClass(
									IntegerDescription.NOTHING));
					ret.add(subClassAxiom);
				}
			}
		}
		return ret;

	}

	public NormalizerDisjoint() {
	}
}
