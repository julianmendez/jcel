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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;

/**
 * This class contains convenience methods used by saturation rules.
 * 
 * @author Julian Mendez
 */
class SaturationRuleHelper {

	/**
	 * Constructs a new helper.
	 */
	public SaturationRuleHelper() {
	}

	/**
	 * Returns a map that for each object property has a set of super object
	 * properties.
	 * 
	 * @param axiomSet
	 *            set of RI-2 axioms
	 * @return a map that for each object property has a set of super object
	 *         properties
	 */
	public Map<Integer, Set<Integer>> getMapBySubObjectProperty(Set<RI2Axiom> axiomSet) {
		Objects.requireNonNull(axiomSet);
		Map<Integer, Set<Integer>> ret = new HashMap<>();
		axiomSet.forEach(axiom -> {
			Set<Integer> relatedElemSet = ret.get(axiom.getSubProperty());
			if (Objects.isNull(relatedElemSet)) {
				relatedElemSet = new HashSet<>();
				ret.put(axiom.getSubProperty(), relatedElemSet);
			}
			relatedElemSet.add(axiom.getSuperProperty());
		});
		return Collections.unmodifiableMap(ret);
	}

	/**
	 * Returns a map that for each object property has a set of sub object
	 * properties.
	 * 
	 * @param axiomSet
	 *            set of RI2 axioms
	 * @return a map that for each object property has a set of sub object
	 *         properties
	 */
	public Map<Integer, Set<Integer>> getMapBySuperObjectProperty(Set<RI2Axiom> axiomSet) {
		Objects.requireNonNull(axiomSet);
		Map<Integer, Set<Integer>> ret = new HashMap<>();
		axiomSet.forEach(axiom -> {
			Set<Integer> relatedElemSet = ret.get(axiom.getSuperProperty());
			if (Objects.isNull(relatedElemSet)) {
				relatedElemSet = new HashSet<>();
				ret.put(axiom.getSuperProperty(), relatedElemSet);
			}
			relatedElemSet.add(axiom.getSubProperty());
		});
		return Collections.unmodifiableMap(ret);
	}

	/**
	 * Returns the set of all nodes reachable from a specified starting node,
	 * according to the given graph.
	 * 
	 * @param first
	 *            starting node
	 * @param map
	 *            map representing a graph
	 * @return the set of all nodes reachable from a specified starting node,
	 *         according to the given graph
	 */
	public Set<Integer> getReachable(Integer first, Map<Integer, Set<Integer>> map) {
		Objects.requireNonNull(first);
		Objects.requireNonNull(map);
		Set<Integer> ret = new HashSet<>();
		Set<Integer> toVisit = new HashSet<>();
		toVisit.add(first);
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> set = map.get(elem);
			if (Objects.nonNull(set)) {
				set.forEach(newElem -> {
					if (!ret.contains(newElem)) {
						toVisit.add(newElem);
					}
				});
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Returns all the RI-2 axioms in the specified set of normalized axioms.
	 * 
	 * @param originalSet
	 *            set of normalized axioms
	 * @return all the RI-2 axioms in the specified set of normalized axioms
	 */
	public Set<RI2Axiom> getRI2Axioms(Set<NormalizedIntegerAxiom> originalSet) {
		Objects.requireNonNull(originalSet);
		Set<RI2Axiom> ret = new HashSet<>();
		originalSet.forEach(axiom -> {
			if (axiom instanceof RI2Axiom) {
				ret.add((RI2Axiom) axiom);
			}
		});
		return Collections.unmodifiableSet(ret);
	}

}
