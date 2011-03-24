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
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * <p>
 * <ul>
 * <li>NR-1.6 : C<sup>&#8869;</sup> &#8849; D &#8605;</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_6 implements NormalizationRule {

	/**
	 * Constructs a new normalizer of rule NR-1.6.
	 */
	public NormalizerNR1_6() {
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	/**
	 * Tells whether this rule can be applied to the specified subclass axiom.
	 * 
	 * @param classAxiom
	 *            subclass axiom
	 * @return <code>true</code> if and only if this rule can be applied to the
	 *         specified subclass axiom
	 */
	public boolean canBeApplied(IntegerSubClassOfAxiom classAxiom) {
		if (classAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return classAxiom.getSubClass().containsBottom();
	}

}
