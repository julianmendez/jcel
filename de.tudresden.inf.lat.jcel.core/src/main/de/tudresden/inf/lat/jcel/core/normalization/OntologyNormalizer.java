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
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerInverseObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * An object of this class normalizes an ontology according to the following
 * normalization rules:
 * 
 * <ul>
 * <li>NR-1.1 : domain(r) &#8849; C &#8605; &exist;r <i>.</i> &#8868; &#8849; C</li>
 * <li>NR-1.2 : range(r) &#8849; C &#8605; range(r) &#8849; A, A &#8849; C</li>
 * <li>NR-1.3 : reflexive(r) &#8605; &#1013; &#8849; r</li>
 * <li>NR-1.4 : transitive(r) &#8605; r &#8728; r &#8849; r</li>
 * <li>NR-1.5 : C &#8801; D &#8605; C &#8849; D, D &#8849; C</li>
 * <li>NR-1.6 : C<sup>&#8869;</sup> &#8849; D &#8605;</li>
 * <li>NR-1.7 : C &#8849; D<sup>&#8869;</sup> &#8605; C &#8849; &#8869;</li>
 * <li>NR-2.1 : r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k</sub> &#8849; s
 * &#8605; r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k-1</sub> &#8849; u, u
 * &#8728; r<sub>k</sub> &#8849; s</li>
 * <li>NR-2.2 : C<sub>1</sub> &#8851; &hellip; &#8851; C' &#8851; &hellip;
 * &#8851; C<sub>n</sub> &#8849; D &#8605; C' &#8849; A, C<sub>1</sub> &#8851;
 * &hellip; &#8851; A &#8851; &hellip; &#8851; C<sub>n</sub> &#8849; D</li>
 * <li>NR-2.3 : &exist; r <i>.</i> C'&#8849; D &#8605; C' &#8849; A, &exist; r
 * <i>.</i> A &#8849; D</li>
 * <li>NR-3.1 : C' &#8849; D' &#8605; C' &#8849; A, A &#8849; D'</li>
 * <li>NR-3.2 : B &#8849; &exist; r <i>.</i> C' &#8605; B &#8849; &exist; r
 * <i>.</i> A, A &#8849; C'</li>
 * <li>NR-3.3 : B &#8849; C &#8851; D &#8605; B &#8849; C, B &#8849; D</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class OntologyNormalizer {

	// private static final Logger logger = Logger
	// .getLogger(OntologyNormalizer.class.getName());

	private IdGenerator nameGenerator = null;

	/**
	 * Constructs a new ontology normalizer.
	 * 
	 * @param generator
	 *            an identifier generator
	 */
	public OntologyNormalizer(IdGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.nameGenerator = generator;
	}

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	public IdGenerator getNameGenerator() {
		return this.nameGenerator;
	}

	/**
	 * Normalizes a set of axioms.
	 * 
	 * @param originalAxiomSet
	 *            set of axioms to be normalized
	 * @return the normalized set of axioms according to the specified axioms
	 */
	public Set<NormalizedIntegerAxiom> normalize(
			Set<ComplexIntegerAxiom> originalAxiomSet) {
		if (originalAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		SimpleNormalizer normalizer = new SimpleNormalizer(this.nameGenerator);
		Set<IntegerAxiom> currentAxiomSet = new HashSet<IntegerAxiom>();

		for (ComplexIntegerAxiom axiom : originalAxiomSet) {
			if (axiom instanceof IntegerInverseObjectPropertiesAxiom) {
				Set<IntegerAxiom> newSet = normalizer.normalize(axiom);
				currentAxiomSet.addAll(newSet);
			} else {
				currentAxiomSet.add(axiom);
			}
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
