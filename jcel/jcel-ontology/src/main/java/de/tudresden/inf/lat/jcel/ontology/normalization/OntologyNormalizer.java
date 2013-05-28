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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.Collections;
import java.util.HashSet;
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
 * <li>NR-1.1 : domain(r) &sqsube; C &rarrw; &exist;r <i>.</i> &top; &sqsube; C</li>
 * <li>NR-1.2 : range(r) &sqsube; C &rarrw; range(r) &sqsube; A, A &sqsube; C</li>
 * <li>NR-1.3 : reflexive(r) &rarrw; &varepsilon; &sqsube; r</li>
 * <li>NR-1.4 : transitive(r) &rarrw; r &compfn; r &sqsube; r</li>
 * <li>NR-1.5 : C &equiv; D &rarrw; C &sqsube; D, D &sqsube; C</li>
 * <li>NR-1.6 : C<sup>&perp;</sup> &sqsube; D &rarrw;</li>
 * <li>NR-1.7 : C &sqsube; D<sup>&perp;</sup> &rarrw; C &sqsube; &perp;</li>
 * <li>NR-2.1 : r<sub>1</sub> &compfn; &hellip; &compfn; r<sub>k</sub> &sqsube;
 * s &rarrw; r<sub>1</sub> &compfn; &hellip; &compfn; r<sub>k-1</sub> &sqsube;
 * u, u &compfn; r<sub>k</sub> &sqsube; s</li>
 * <li>NR-2.2 : C<sub>1</sub> &sqcap; &hellip; &sqcap; C' &sqcap; &hellip;
 * &sqcap; C<sub>n</sub> &sqsube; D &rarrw; C' &sqsube; A, C<sub>1</sub> &sqcap;
 * &hellip; &sqcap; A &sqcap; &hellip; &sqcap; C<sub>n</sub> &sqsube; D</li>
 * <li>NR-2.3 : &exist; r <i>.</i> C'&sqsube; D &rarrw; C' &sqsube; A, &exist; r
 * <i>.</i> A &sqsube; D</li>
 * <li>NR-3.1 : C' &sqsube; D' &rarrw; C' &sqsube; A, A &sqsube; D'</li>
 * <li>NR-3.2 : B &sqsube; &exist; r <i>.</i> C' &rarrw; B &sqsube; &exist; r
 * <i>.</i> A, A &sqsube; C'</li>
 * <li>NR-3.3 : B &sqsube; C &sqcap; D &rarrw; B &sqsube; C, B &sqsube; D</li>
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
	public Set<NormalizedIntegerAxiom> normalize(
			Set<ComplexIntegerAxiom> originalAxiomSet,
			IntegerOntologyObjectFactory factory) {
		if (originalAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		SimpleNormalizer normalizer = new SimpleNormalizer(factory);
		Set<Integer> objectPropIdSet = new HashSet<Integer>();
		Set<IntegerAxiom> currentAxiomSet = new HashSet<IntegerAxiom>();

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
			Integer inversePropId = factory.getEntityManager()
					.createOrGetInverseObjectPropertyOf(propId);
			currentAxiomSet
					.addAll(normalizer.getAxiomsForInverseObjectProperties(
							propId, inversePropId));
		}

		while (currentAxiomSet.size() > 0) {
			Set<IntegerAxiom> nextAxiomSet = new HashSet<IntegerAxiom>();
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
