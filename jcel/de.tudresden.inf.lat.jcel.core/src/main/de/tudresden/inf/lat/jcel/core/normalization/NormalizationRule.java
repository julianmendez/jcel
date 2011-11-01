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

import java.util.Set;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * This interface is implemented by normalization rules. A normalization rule
 * transforms a non-normalized axiom into one or more normalized axioms.
 * 
 * @author Julian Mendez
 */
public interface NormalizationRule {

	/**
	 * Applies the normalization rule to a specified axiom.
	 * 
	 * @param axiom
	 *            axiom to be processed
	 * @return the set of axioms after the application of the normalization rule
	 */
	public Set<IntegerAxiom> apply(IntegerAxiom axiom);

}
