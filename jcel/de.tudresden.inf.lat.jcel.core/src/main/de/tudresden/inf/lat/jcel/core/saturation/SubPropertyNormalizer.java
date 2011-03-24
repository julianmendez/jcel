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
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IdGenerator;

/**
 * This class models a normalizer that saturates an ontology with object
 * property inclusions. The saturation adds an axiom r &#8849; r for each object
 * property r, and then applies the following rules:
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
public class SubPropertyNormalizer implements SaturationRule {

	private SaturationRule sr0 = null;
	private SaturationRule sr1sr2 = null;
	private SaturationRule sr3 = null;
	private SaturationRule sr4 = null;

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

		this.sr0 = new SR0Rule();
		this.sr1sr2 = new SR1AndSR2Rules(generator);
		this.sr3 = new SR3Rule(generator);
		this.sr4 = new SR4Rule();
	}

	/**
	 * Saturates a set of normalized axioms by applying rules SR-1, SR-2, SR-3,
	 * SR-4 and SR-5.
	 * 
	 * @param originalSet
	 *            set of normalized axioms to be saturated
	 * @return the saturated set of normalized axioms
	 */
	public Set<NormalizedIntegerAxiom> apply(
			Set<NormalizedIntegerAxiom> originalSet) {
		if (originalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableSet(this.sr4.apply(this.sr3
				.apply(this.sr1sr2.apply(this.sr0.apply(originalSet)))));
	}

}
