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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;

/**
 * An object of this class normalizes an ontology according to the following
 * normalization rules:
 * 
 * <ul>
 * <li>NR-1.1 : domain(r) \u2291 C \u219D &exist;r <i>.</i> \u22A4 \u2291 C</li>
 * <li>NR-1.2 : range(r) \u2291 C \u219D range(r) \u2291 A, A \u2291 C</li>
 * <li>NR-1.3 : reflexive(r) \u219D \u03B5 \u2291 r</li>
 * <li>NR-1.4 : transitive(r) \u219D r \u2218 r \u2291 r</li>
 * <li>NR-1.5 : C &equiv; D \u219D C \u2291 D, D \u2291 C</li>
 * <li>NR-1.6 : C<sup>&perp;</sup> \u2291 D \u219D</li>
 * <li>NR-1.7 : C \u2291 D<sup>&perp;</sup> \u219D C \u2291 &perp;</li>
 * <li>NR-2.1 : r<sub>1</sub> \u2218 &hellip; \u2218 r<sub>k</sub> \u2291 s
 * \u219D r<sub>1</sub> \u2218 &hellip; \u2218 r<sub>k-1</sub> \u2291 u, u
 * \u2218 r<sub>k</sub> \u2291 s</li>
 * <li>NR-2.2 : C<sub>1</sub> \u2293 &hellip; \u2293 C' \u2293 &hellip; \u2293 C
 * <sub>n</sub> \u2291 D \u219D C' \u2291 A, C<sub>1</sub> \u2293 &hellip;
 * \u2293 A \u2293 &hellip; \u2293 C<sub>n</sub> \u2291 D</li>
 * <li>NR-2.3 : &exist; r <i>.</i> C'\u2291 D \u219D C' \u2291 A, &exist; r
 * <i>.</i> A \u2291 D</li>
 * <li>NR-3.1 : C' \u2291 D' \u219D C' \u2291 A, A \u2291 D'</li>
 * <li>NR-3.2 : B \u2291 &exist; r <i>.</i> C' \u219D B \u2291 &exist; r
 * <i>.</i> A, A \u2291 C'</li>
 * <li>NR-3.3 : B \u2291 C \u2293 D \u219D B \u2291 C, B \u2291 D</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class OntologyNormalizer {

	/**
	 * Constructs a new ontology normalizer.
	 */
	public OntologyNormalizer() {
	}

	/**
	 * Normalizes a set of axioms.
	 * 
	 * @param originalAxiomSet
	 *            set of axioms to be normalized
	 * @param factory
	 *            factory
	 * @return the normalized set of axioms according to the specified axioms
	 */
	public Set<NormalizedIntegerAxiom> normalize(Set<ComplexIntegerAxiom> originalAxiomSet,
			IntegerOntologyObjectFactory factory) {
		Objects.requireNonNull(originalAxiomSet);
		Objects.requireNonNull(factory);
		Set<NormalizedIntegerAxiom> ret = new HashSet<>();
		SimpleNormalizer normalizer = new SimpleNormalizer(factory);
		Set<Integer> objectPropIdSet = new HashSet<>();
		Set<IntegerAxiom> currentAxiomSet = new HashSet<>();

		for (ComplexIntegerAxiom axiom : originalAxiomSet) {
			objectPropIdSet.addAll(axiom.getObjectPropertiesInSignature());
			if (axiom instanceof IntegerInverseObjectPropertiesAxiom) {
				Set<IntegerAxiom> newSet = normalizer.normalize(axiom);
				currentAxiomSet.addAll(newSet);
			} else {
				currentAxiomSet.add(axiom);
			}
		}

		for (Integer propId : objectPropIdSet) {
			Integer inversePropId = factory.getEntityManager().createOrGetInverseObjectPropertyOf(propId);
			currentAxiomSet.addAll(normalizer.getAxiomsForInverseObjectProperties(propId, inversePropId));
		}

		while (currentAxiomSet.size() > 0) {
			Set<IntegerAxiom> nextAxiomSet = new HashSet<>();
			for (IntegerAxiom axiom : currentAxiomSet) {
				if (axiom instanceof NormalizedIntegerAxiom) {
					ret.add((NormalizedIntegerAxiom) axiom);
				} else {
					Set<IntegerAxiom> newSet = normalizer.normalize(axiom);
					nextAxiomSet.addAll(newSet);
				}
			}
			currentAxiomSet = nextAxiomSet;
		}

		return Collections.unmodifiableSet(ret);
	}

}
