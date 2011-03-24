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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.FunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;

/**
 * This class models an ontology that is saturated of object property
 * inclusions. The saturation is performed by applying the following rules:
 * 
 * <ul>
 * <li>SR-1 : r &#8849; s &#8605; r<sup>-</sup> &#8849; s<sup>-</sup></li>
 * <li>SR-2 : r &#8849; s, s &#8849; t &#8605; r &#8849; t</li>
 * <li>SR-3 : r &#8728; s &#8849; t &#8605; s<sup>-</sup> &#8728; r<sup>-</sup>
 * &#8849; t<sup>-</sup></li>
 * <li>SR-4 : r &#8849; s, f(s) &#8605; f(r)</li>
 * </ul>
 * 
 * @author Julian Mendez
 * 
 */
public class SubPropertyNormalizer {

	private IdGenerator idGenerator = null;

	/**
	 * Constructs a new normalizer of sub object properties.
	 * 
	 * @param generator
	 *            an identifier generator
	 */
	public SubPropertyNormalizer(IdGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.idGenerator = generator;
	}

	private Set<RI2Axiom> applyRule1(Set<RI2Axiom> axiomSet) {
		Set<RI2Axiom> ret = new HashSet<RI2Axiom>();
		for (RI2Axiom axiom : axiomSet) {
			Integer invSubProperty = getIdGenerator()
					.createOrGetInverseObjectPropertyOf(axiom.getSubProperty());
			Integer invSuperProperty = getIdGenerator()
					.createOrGetInverseObjectPropertyOf(
							axiom.getSuperProperty());
			ret.add(new RI2Axiom(invSubProperty, invSuperProperty));
		}
		return ret;
	}

	private Set<RI2Axiom> applyRule2(Set<RI2Axiom> axiomSet) {
		Set<RI2Axiom> ret = new HashSet<RI2Axiom>();
		Map<Integer, Set<Integer>> mapBySubProp = getMapBySubObjectProperty(axiomSet);
		for (Integer firstProp : mapBySubProp.keySet()) {
			Set<Integer> secondPropSet = mapBySubProp.get(firstProp);
			if (secondPropSet != null) {
				for (Integer secondProp : secondPropSet) {
					Set<Integer> thirdPropSet = mapBySubProp.get(secondProp);
					if (thirdPropSet != null) {
						for (Integer thirdProp : thirdPropSet) {
							if (!secondPropSet.contains(thirdProp)) {
								ret.add(new RI2Axiom(firstProp, thirdProp));
							}
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	public IdGenerator getIdGenerator() {
		return this.idGenerator;
	}

	private Map<Integer, Set<Integer>> getMapBySubObjectProperty(
			Set<RI2Axiom> axiomSet) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (RI2Axiom axiom : axiomSet) {
			Set<Integer> relatedElemSet = ret.get(axiom.getSubProperty());
			if (relatedElemSet == null) {
				relatedElemSet = new HashSet<Integer>();
				ret.put(axiom.getSubProperty(), relatedElemSet);
			}
			relatedElemSet.add(axiom.getSuperProperty());
		}
		return ret;
	}

	private Map<Integer, Set<Integer>> getMapBySuperObjectProperty(
			Set<RI2Axiom> axiomSet) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (RI2Axiom axiom : axiomSet) {
			Set<Integer> relatedElemSet = ret.get(axiom.getSuperProperty());
			if (relatedElemSet == null) {
				relatedElemSet = new HashSet<Integer>();
				ret.put(axiom.getSuperProperty(), relatedElemSet);
			}
			relatedElemSet.add(axiom.getSubProperty());
		}
		return ret;
	}

	private Set<Integer> getReachable(Integer first,
			Map<Integer, Set<Integer>> map) {
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
		return ret;
	}

	private Set<RI2Axiom> getRI2Axioms(Set<NormalizedIntegerAxiom> originalSet) {
		Set<RI2Axiom> ret = new HashSet<RI2Axiom>();
		for (NormalizedIntegerAxiom axiom : originalSet) {
			if (axiom instanceof RI2Axiom) {
				ret.add((RI2Axiom) axiom);
			}
		}
		return ret;
	}

	/**
	 * Saturates a set of normalized axioms by applying rules SR-1, SR-2, SR-3
	 * and SR-4.
	 * 
	 * @param axiomSet
	 *            set of normalized axioms to be saturated
	 * @return the saturated set of normalized axioms
	 */
	public Set<NormalizedIntegerAxiom> saturate(
			Set<NormalizedIntegerAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections
				.unmodifiableSet(saturateRule4(saturateRule3(saturateRule1AndRule2(axiomSet))));
	}

	private Set<NormalizedIntegerAxiom> saturateRule1AndRule2(
			Set<NormalizedIntegerAxiom> axiomSet) {
		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(axiomSet);

		Set<RI2Axiom> accumulatedSet = new HashSet<RI2Axiom>();
		Set<RI2Axiom> currentSet = getRI2Axioms(axiomSet);
		while (!currentSet.isEmpty()) {
			accumulatedSet.addAll(currentSet);
			currentSet = new HashSet<RI2Axiom>();
			currentSet.addAll(applyRule1(accumulatedSet));
			currentSet.addAll(applyRule2(accumulatedSet));
			currentSet.removeAll(accumulatedSet);
		}
		ret.addAll(accumulatedSet);

		return ret;
	}

	private Set<NormalizedIntegerAxiom> saturateRule3(
			Set<NormalizedIntegerAxiom> originalSet) {
		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(originalSet);
		for (NormalizedIntegerAxiom normalizedAxiom : originalSet) {
			if (normalizedAxiom instanceof RI3Axiom) {
				RI3Axiom axiom = (RI3Axiom) normalizedAxiom;
				Integer invLeftSubProp = getIdGenerator()
						.createOrGetInverseObjectPropertyOf(
								axiom.getLeftSubProperty());
				Integer invRightSubProp = getIdGenerator()
						.createOrGetInverseObjectPropertyOf(
								axiom.getRightSubProperty());
				Integer invSuperProp = getIdGenerator()
						.createOrGetInverseObjectPropertyOf(
								axiom.getSuperProperty());
				RI3Axiom newAxiom = new RI3Axiom(invRightSubProp,
						invLeftSubProp, invSuperProp);
				ret.add(newAxiom);
			}
		}
		return ret;
	}

	private Set<NormalizedIntegerAxiom> saturateRule4(
			Set<NormalizedIntegerAxiom> originalSet) {
		Map<Integer, Set<Integer>> mapBySuperProp = getMapBySuperObjectProperty(getRI2Axioms(originalSet));
		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(originalSet);

		for (NormalizedIntegerAxiom normalizedAxiom : originalSet) {
			if (normalizedAxiom instanceof FunctionalObjectPropertyAxiom) {
				FunctionalObjectPropertyAxiom axiom = (FunctionalObjectPropertyAxiom) normalizedAxiom;
				Integer functionalProperty = axiom.getProperty();
				Set<Integer> newFunctionalPropertySet = getReachable(
						functionalProperty, mapBySuperProp);
				for (Integer newFunctionalProperty : newFunctionalPropertySet) {
					ret.add(new FunctionalObjectPropertyAxiom(
							newFunctionalProperty));
				}
			}
		}
		return ret;
	}

}
