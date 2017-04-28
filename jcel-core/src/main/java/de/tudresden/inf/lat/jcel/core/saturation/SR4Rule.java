/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.FunctObjectPropAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;

/**
 * <ul>
 * <li>SR-4 : r \u2291 s, f(s) \u219D f(r)</li>
 * </ul>
 *
 * @author Julian Mendez
 */
public class SR4Rule implements SaturationRule {

	private final NormalizedIntegerAxiomFactory factory;
	private final SaturationRuleHelper helper = new SaturationRuleHelper();

	/**
	 * Constructs a new SR-4 rule.
	 *
	 * @param factory
	 *            factory
	 */
	public SR4Rule(NormalizedIntegerAxiomFactory factory) {
		Objects.requireNonNull(factory);
		this.factory = factory;
	}

	@Override
	public Set<NormalizedIntegerAxiom> apply(Set<NormalizedIntegerAxiom> originalSet) {
		Objects.requireNonNull(originalSet);
		Map<Integer, Set<Integer>> mapBySuperProp = this.helper
				.getMapBySuperObjectProperty(this.helper.getRI2Axioms(originalSet));
		Set<NormalizedIntegerAxiom> ret = new HashSet<>();
		ret.addAll(originalSet);

		originalSet.forEach(normalizedAxiom -> {
			if (normalizedAxiom instanceof FunctObjectPropAxiom) {
				FunctObjectPropAxiom axiom = (FunctObjectPropAxiom) normalizedAxiom;
				Integer functionalProperty = axiom.getProperty();
				Set<Integer> newFunctionalPropertySet = this.helper.getReachable(functionalProperty, mapBySuperProp);
				newFunctionalPropertySet.forEach(newFunctionalProperty -> ret
						.add(this.factory.createFunctObjectPropAxiom(newFunctionalProperty, axiom.getAnnotations())));
			}
		});
		return Collections.unmodifiableSet(ret);
	}

}
