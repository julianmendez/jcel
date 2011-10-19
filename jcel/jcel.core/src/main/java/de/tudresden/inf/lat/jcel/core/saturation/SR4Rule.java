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
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.FunctObjectPropAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;

/**
 * <ul>
 * <li>SR-4 : r &#8849; s, f(s) &#8605; f(r)</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class SR4Rule implements SaturationRule {

	private final IntegerOntologyObjectFactory factory;
	private SaturationRuleHelper helper = new SaturationRuleHelper();

	/**
	 * Constructs a new SR-4 rule.
	 */
	public SR4Rule(IntegerOntologyObjectFactory f) {
		if (f == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.factory = f;
	}

	@Override
	public Set<NormalizedIntegerAxiom> apply(
			Set<NormalizedIntegerAxiom> originalSet) {
		if (originalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Map<Integer, Set<Integer>> mapBySuperProp = this.helper
				.getMapBySuperObjectProperty(this.helper
						.getRI2Axioms(originalSet));
		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(originalSet);

		for (NormalizedIntegerAxiom normalizedAxiom : originalSet) {
			if (normalizedAxiom instanceof FunctObjectPropAxiom) {
				FunctObjectPropAxiom axiom = (FunctObjectPropAxiom) normalizedAxiom;
				Integer functionalProperty = axiom.getProperty();
				Set<Integer> newFunctionalPropertySet = this.helper
						.getReachable(functionalProperty, mapBySuperProp);
				for (Integer newFunctionalProperty : newFunctionalPropertySet) {
					ret.add(this.factory.getNormalizedAxiomFactory()
							.createFunctObjectPropAxiom(newFunctionalProperty));
				}
			}
		}
		return Collections.unmodifiableSet(ret);
	}

}
