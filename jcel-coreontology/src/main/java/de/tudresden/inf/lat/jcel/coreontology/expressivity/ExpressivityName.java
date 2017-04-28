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

package de.tudresden.inf.lat.jcel.coreontology.expressivity;

import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;

/**
 * An object of this class can name the expressivity used in an ontology.
 * 
 * @author Julian Mendez
 */
public class ExpressivityName {

	/**
	 * Constructs a new object that can give the description logic name for a
	 * given expressivity.
	 */
	public ExpressivityName() {
	}

	/**
	 * Returns the description logic name for a specified expressivity.
	 * 
	 * @param expr
	 *            ontology expressivity to get the name
	 * @return the description logic name for a specified expressivity
	 */
	public String getName(OntologyExpressivity expr) {
		StringBuffer sbuf = new StringBuffer();
		if (isAL() && isC() && expr.hasTransitiveObjectProperty()) {
			sbuf.append("S");
		} else {
			if (isAL()) {
				sbuf.append("AL");
			} else if (isEL()) {
				sbuf.append("EL");
			}
			if (isC()) {
				sbuf.append("C");
			} else {
				if (isU()) {
					sbuf.append("U");
				}
				if (!isEL() && isE()) {
					sbuf.append("E");
				}
			}
		}
		if (expr.hasSubObjectPropertyOf()) {
			sbuf.append("H");
		}
		if (expr.hasNominal()) {
			sbuf.append("O");
		}
		if (expr.hasInverseObjectProperty()) {
			sbuf.append("I");
		}
		if (isQ()) {
			sbuf.append("Q");
		} else if (isN()) {
			sbuf.append("N");
		}
		if (expr.hasFunctionalObjectProperty()) {
			sbuf.append("F");
		}
		if (expr.hasSubPropertyChainOf()) {
			sbuf.append("R");
		}
		if (expr.hasDatatype()) {
			sbuf.append("(D)");
		}
		if (expr.hasTransitiveObjectProperty()) {
			sbuf.append(" [transitive]");
		}
		if (expr.hasBottom()) {
			sbuf.append(" [bottom]");
		}
		if (expr.hasIndividual()) {
			sbuf.append(" [individual]");
		}
		return sbuf.toString();

	}

	/**
	 * Tells whether the axioms require the AL logic.
	 * 
	 * @return <code>true</code> if and only if the axioms require the AL logic
	 */
	private boolean isAL() {
		return false;
	}

	/**
	 * Tells whether the axioms use complement.
	 * 
	 * @return <code>true</code> if and only if the axioms use complement
	 */
	private boolean isC() {
		return false;
	}

	/**
	 * Tells whether the axioms use full existential qualification.
	 * 
	 * @return <code>true</code> if and only if the axioms use full existential
	 *         qualification
	 */
	private boolean isE() {
		return true;
	}

	/**
	 * Tells whether the axioms require the EL logic.
	 * 
	 * @return <code>true</code> if and only if the axioms require the EL logic
	 */
	private boolean isEL() {
		return true;
	}

	/**
	 * Tells whether the axioms use cardinality restrictions. In this method,
	 * functional object properties are not considered cardinality restrictions.
	 * 
	 * @return <code>true</code> if and only if the axioms use number
	 *         restrictions
	 */
	private boolean isN() {
		return false;
	}

	/**
	 * Tells whether the axioms use qualified cardinality restrictions.
	 * 
	 * @return <code>true</code> if and only if the axioms use qualified
	 *         cardinality restrictions
	 */
	private boolean isQ() {
		return false;
	}

	/**
	 * Tells whether the axioms use class union.
	 * 
	 * @return <code>true</code> if and only if the axioms use class union
	 */
	private boolean isU() {
		return false;
	}

}
