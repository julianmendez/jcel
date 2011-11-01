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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.NormalizedIntegerAxiom;

/**
 * An object of this class normalizes an ontology according to the CEL
 * normalization rules. The rules are:
 * 
 * <ul>
 * <li>NR 1-1 : domain(r) &sube; C &rarr;&rarr; &exist;r <i>.</i> &#8868; &sube;
 * C</li>
 * <li>NR 1-2 : range(r) &sube; C &rarr;&rarr; range(r) &sube; A, A &sube; C</li>
 * <li>NR 1-3 : reflexive(r) &rarr;&rarr; &epsilon; &sube; r</li>
 * <li>NR 1-4 : transitive(r) &rarr; &rarr; r &#8728; r &sube; r</li>
 * <li>NR 1-5 : C &#8801; D &rarr; &rarr; C &sube; D, D &sube; C</li>
 * <li>NR 1-6 : C<sup>&#8869;</sup> &sube; D &rarr; &rarr;</li>
 * <li>NR 1-7 : C &sube; D<sup>&#8869;</sup> &rarr; &rarr; C &sube; &#8869;</li>
 * <li>NR 2-1 : r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k</sub> &sube; s
 * &rarr; &rarr; r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k-1</sub> &sube;
 * u, u &#8728; r<sub>k</sub> &sube; s</li>
 * <li>NR 2-2 : C<sub>1</sub> &cap; &hellip; &cap; C' &cap; &hellip; &cap;
 * C<sub>n</sub> &sube; D &rarr; &rarr; C' &sube; A, C<sub>1</sub> &cap;
 * &hellip; &cap; A &cap; &hellip; &cap; C<sub>n</sub> &sube; D</li>
 * <li>NR 2-3 : &exist; r <i>.</i> C'&sube; D &rarr; &rarr; C' &sube; A, &exist;
 * r <i>.</i> A &sube; D</li>
 * <li>NR 3-1 : C' &sube; D' &rarr; &rarr; C' &sube; A, A &sube; D'</li>
 * <li>NR 3-2 : B &sube; &exist; r <i>.</i> C' &rarr; &rarr; B &sube; &exist; r
 * <i>.</i> A, A &sube; C'</li>
 * <li>NR 3-3 : B &sube; C &cap; D &rarr; &rarr; B &sube; C, B &sube; D</li>
 * </ul>
 * 
 * (Due to technical restrictions, squared symbols were replaced by rounded
 * ones)
 * 
 * @author Julian Mendez
 * 
 */
public class OntologyNormalizer {

	private static final Logger logger = Logger
			.getLogger(OntologyNormalizer.class.getName());

	private IdGenerator nameGenerator = null;

	public OntologyNormalizer(IdGenerator generator) {
		this.nameGenerator = generator;
	}

	public IdGenerator getNameGenerator() {
		return this.nameGenerator;
	}

	public Set<NormalizedIntegerAxiom> normalize(
			Set<IntegerAxiom> originalAxiomSet) {
		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		SimpleNormalizer normalizer = new SimpleNormalizer(this.nameGenerator);
		Set<IntegerAxiom> currentAxiomSet = new HashSet<IntegerAxiom>();
		currentAxiomSet.addAll(originalAxiomSet);
		while (currentAxiomSet.size() > 0) {
			Set<IntegerAxiom> nextAxiomSet = new HashSet<IntegerAxiom>();
			for (IntegerAxiom axiom : currentAxiomSet) {
				if (axiom instanceof NormalizedIntegerAxiom) {
					ret.add((NormalizedIntegerAxiom) axiom);
				} else {
					Set<IntegerAxiom> newSet = normalizer.normalize(axiom);
					if (newSet != null) {
						nextAxiomSet.addAll(newSet);
					} else {
						logger.fine("Not normalizing '" + axiom + "'");
					}
				}
			}
			currentAxiomSet = nextAxiomSet;
		}
		return ret;
	}
}
