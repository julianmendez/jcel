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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;

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
	public Map<Integer, Set<Integer>> getMapBySubObjectProperty(
			Set<RI2Axiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (RI2Axiom axiom : axiomSet) {
			Set<Integer> relatedElemSet = ret.get(axiom.getSubProperty());
			if (relatedElemSet == null) {
				relatedElemSet = new HashSet<Integer>();
				ret.put(axiom.getSubProperty(), relatedElemSet);
			}
			relatedElemSet.add(axiom.getSuperProperty());
		}
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
	public Map<Integer, Set<Integer>> getMapBySuperObjectProperty(
			Set<RI2Axiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (RI2Axiom axiom : axiomSet) {
			Set<Integer> relatedElemSet = ret.get(axiom.getSuperProperty());
			if (relatedElemSet == null) {
				relatedElemSet = new HashSet<Integer>();
				ret.put(axiom.getSuperProperty(), relatedElemSet);
			}
			relatedElemSet.add(axiom.getSubProperty());
		}
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
	public Set<Integer> getReachable(Integer first,
			Map<Integer, Set<Integer>> map) {
		if (first == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (map == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> toVisit = new HashSet<Integer>();
		toVisit.add(first);
		while (!toVisit.isEmpty()) {
			Integer elem = toVisit.iterator().next();
			toVisit.remove(elem);
			ret.add(elem);
			Set<Integer> set = map.get(elem);
			if (set != null) {
				for (Integer newElem : set) {
					if (!ret.contains(newElem)) {
						toVisit.add(newElem);
					}
				}
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
		if (originalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<RI2Axiom> ret = new HashSet<RI2Axiom>();
		for (NormalizedIntegerAxiom axiom : originalSet) {
			if (axiom instanceof RI2Axiom) {
				ret.add((RI2Axiom) axiom);
			}
		}
		return Collections.unmodifiableSet(ret);
	}

}
