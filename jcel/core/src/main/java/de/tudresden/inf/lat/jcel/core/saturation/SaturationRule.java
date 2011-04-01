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

import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;

/**
 * This interface is implemented by saturation rules. A saturation rule
 * saturates a set of normalized axioms.
 * 
 * @author Julian Mendez
 */
public interface SaturationRule {

	/**
	 * Applies the saturation rule to a specified set of normalized axioms.
	 * 
	 * @param originalSet
	 *            set of normalized axioms
	 * @return the set of normalized axioms after the application of the
	 *         saturation rule
	 */
	public Set<NormalizedIntegerAxiom> apply(
			Set<NormalizedIntegerAxiom> originalSet);

}
