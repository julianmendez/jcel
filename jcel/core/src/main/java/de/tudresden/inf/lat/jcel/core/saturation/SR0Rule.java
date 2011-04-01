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

package de.tudresden.inf.lat.jcel.core.saturation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;

/**
 * For each object property r, this rule adds r &#8849; r.
 * 
 * @author Julian Mendez
 */
public class SR0Rule implements SaturationRule {

	/**
	 * Constructs a new SR-0 rule.
	 */
	public SR0Rule() {
	}

	@Override
	public Set<NormalizedIntegerAxiom> apply(
			Set<NormalizedIntegerAxiom> originalSet) {
		if (originalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(originalSet);

		Set<Integer> objectPropertySet = new HashSet<Integer>();
		for (NormalizedIntegerAxiom axiom : originalSet) {
			objectPropertySet.addAll(axiom.getObjectPropertiesInSignature());
		}

		for (Integer objectProperty : objectPropertySet) {
			ret.add(new RI2Axiom(objectProperty, objectProperty));
		}

		return Collections.unmodifiableSet(ret);
	}

}
