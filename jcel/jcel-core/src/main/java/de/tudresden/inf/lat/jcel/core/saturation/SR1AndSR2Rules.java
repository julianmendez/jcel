/*
 *
 * Copyright 2009-2013 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.core.saturation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * <ul>
 * <li>SR-1 : r &#8849; s &#8605; r<sup>-</sup> &#8849; s<sup>-</sup></li>
 * <li>SR-2 : r &#8849; s, s &#8849; t &#8605; r &#8849; t</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class SR1AndSR2Rules implements SaturationRule {

	private final NormalizedIntegerAxiomFactory factory;
	private final SaturationRuleHelper helper = new SaturationRuleHelper();
	private final IntegerEntityManager idGenerator;

	/**
	 * Constructs a new composite rule of SR-1 and SR-2.
	 * 
	 * @param factory
	 *            factory
	 */
	public SR1AndSR2Rules(NormalizedIntegerAxiomFactory factory,
			IntegerEntityManager generator) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.factory = factory;
		this.idGenerator = generator;
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
			Integer invSubProperty = this.idGenerator
					.createOrGetInverseObjectPropertyOf(axiom.getSubProperty());
			Integer invSuperProperty = this.idGenerator
					.createOrGetInverseObjectPropertyOf(axiom
							.getSuperProperty());
			ret.add(this.factory.createRI2Axiom(invSubProperty,
					invSuperProperty));
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
								ret.add(this.factory.createRI2Axiom(firstProp,
										thirdProp));
							}
						}
					}
				}
			}
		}
		return ret;
	}

}
