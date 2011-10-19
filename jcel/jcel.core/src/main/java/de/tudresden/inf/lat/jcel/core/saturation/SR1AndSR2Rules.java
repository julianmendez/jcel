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

import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IdGenerator;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;

/**
 * <ul>
 * <li>SR-1 : r &#8849; s &#8605; r<sup>-</sup> &#8849; s<sup>-</sup></li>
 * <li>SR-2 : r &#8849; s, s &#8849; t &#8605; r &#8849; t</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class SR1AndSR2Rules implements SaturationRule {

	private IntegerOntologyObjectFactory factory;
	private SaturationRuleHelper helper = new SaturationRuleHelper();

	/**
	 * Constructs a new composite rule of SR-1 and SR-2.
	 * 
	 * @param factory
	 *            factory
	 */
	public SR1AndSR2Rules(IntegerOntologyObjectFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.factory = factory;
	}

	@Override
	public Set<NormalizedIntegerAxiom> apply(
			Set<NormalizedIntegerAxiom> originalSet) {
		if (originalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(originalSet);

		Set<RI2Axiom> accumulatedSet = new HashSet<RI2Axiom>();
		Set<RI2Axiom> currentSet = new HashSet<RI2Axiom>();
		currentSet.addAll(this.helper.getRI2Axioms(originalSet));
		while (!currentSet.isEmpty()) {
			accumulatedSet.addAll(currentSet);
			currentSet = new HashSet<RI2Axiom>();
			currentSet.addAll(applyRule1(accumulatedSet));
			currentSet.addAll(applyRule2(accumulatedSet));
			currentSet.removeAll(accumulatedSet);
		}
		ret.addAll(accumulatedSet);

		return Collections.unmodifiableSet(ret);
	}

	private Set<RI2Axiom> applyRule1(Set<RI2Axiom> axiomSet) {
		Set<RI2Axiom> ret = new HashSet<RI2Axiom>();
		for (RI2Axiom axiom : axiomSet) {
			Integer invSubProperty = getIdGenerator()
					.createOrGetInverseObjectPropertyOf(axiom.getSubProperty());
			Integer invSuperProperty = getIdGenerator()
					.createOrGetInverseObjectPropertyOf(
							axiom.getSuperProperty());
			ret.add(this.factory.getNormalizedAxiomFactory().createRI2Axiom(
					invSubProperty, invSuperProperty));
		}
		return ret;
	}

	private Set<RI2Axiom> applyRule2(Set<RI2Axiom> axiomSet) {
		Set<RI2Axiom> ret = new HashSet<RI2Axiom>();
		Map<Integer, Set<Integer>> mapBySubProp = this.helper
				.getMapBySubObjectProperty(axiomSet);
		for (Integer firstProp : mapBySubProp.keySet()) {
			Set<Integer> secondPropSet = mapBySubProp.get(firstProp);
			if (secondPropSet != null) {
				for (Integer secondProp : secondPropSet) {
					Set<Integer> thirdPropSet = mapBySubProp.get(secondProp);
					if (thirdPropSet != null) {
						for (Integer thirdProp : thirdPropSet) {
							if (!secondPropSet.contains(thirdProp)) {
								ret.add(this.factory
										.getNormalizedAxiomFactory()
										.createRI2Axiom(firstProp, thirdProp));
							}
						}
					}
				}
			}
		}
		return ret;
	}

	private IdGenerator getIdGenerator() {
		return this.factory.getIdGenerator();
	}

}
