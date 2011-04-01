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

package de.tudresden.inf.lat.jcel.core.axiom.extension;

import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiom;

/**
 * An object implementing this class can detect the expressivity of a given set
 * of complex axioms.
 * 
 * @author Julian Mendez
 */
public class ExpressivityDetector {

	private ComplexIntegerAxiomAnalyzer axiomAnalyzer = null;

	/**
	 * Constructs a new expressivity detector.
	 * 
	 * @param axiomSet
	 *            set of axioms to detect the expressivity
	 */
	public ExpressivityDetector(Set<ComplexIntegerAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.axiomAnalyzer = new ComplexIntegerAxiomAnalyzer();
		for (ComplexIntegerAxiom axiom : axiomSet) {
			axiom.accept(this.axiomAnalyzer);
		}
	}

	private ComplexIntegerAxiomAnalyzer getAxiomDetector() {
		return this.axiomAnalyzer;
	}

	/**
	 * Returns the description logic name for the detected axioms.
	 * 
	 * @return the description logic name for the detected axioms
	 */
	public String getName() {
		StringBuffer sbuf = new StringBuffer();
		if (isS()) {
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
		if (isH()) {
			sbuf.append("H");
		}
		if (isO()) {
			sbuf.append("O");
		}
		if (isI()) {
			sbuf.append("I");
		}
		if (isQ()) {
			sbuf.append("Q");
		} else if (isN()) {
			sbuf.append("N");
		}
		if (isF()) {
			sbuf.append("F");
		}
		if (isR()) {
			sbuf.append("R");
		}
		if (isD()) {
			sbuf.append("(D)");
		}

		if (isTransitive()) {
			sbuf.append(" [transitive]");
		}
		if (hasBottom()) {
			sbuf.append(" [bottom]");
		}
		if (hasIndividual()) {
			sbuf.append(" [individual]");
		}
		return sbuf.toString();

	}

	/**
	 * Tells whether the detector found class bottom.
	 * 
	 * @return <code>true</code> if and only if the detector found class bottom
	 */
	public boolean hasBottom() {
		return getAxiomDetector().hasBottom();
	}

	/**
	 * Tells whether the detector found individuals
	 * 
	 * @return <code>true</code> if and only if the detector found individuals
	 */
	public boolean hasIndividual() {
		return getAxiomDetector().hasIndividual();
	}

	/**
	 * Tells whether the axioms require the AL logic.
	 * 
	 * @return <code>true</code> if and only if the axioms require the AL logic
	 */
	public boolean isAL() {
		return false;
	}

	/**
	 * Tells whether the axioms use complement.
	 * 
	 * @return <code>true</code> if and only if the axioms use complement
	 */
	public boolean isC() {
		return false;
	}

	/**
	 * Tells whether the axioms use data types.
	 * 
	 * @return <code>true</code> if and only if the axioms use data types
	 */
	public boolean isD() {
		return getAxiomDetector().hasDatatype();
	}

	/**
	 * Tells whether the axioms use full existential qualification.
	 * 
	 * @return <code>true</code> if and only if the axioms use full existential
	 *         qualification
	 */
	public boolean isE() {
		return true;
	}

	/**
	 * Tells whether the axioms require the EL logic.
	 * 
	 * @return <code>true</code> if and only if the axioms require the EL logic
	 */
	public boolean isEL() {
		return true;
	}

	/**
	 * Tells whether the axioms use functional object properties.
	 * 
	 * @return <code>true</code> if and only if the axioms use functional object
	 *         properties
	 */
	public boolean isF() {
		return getAxiomDetector().hasFunctionalObjectProperty();
	}

	/**
	 * Tells whether the axioms use object property hierarchies.
	 * 
	 * @return <code>true</code> if and only if the axioms use object property
	 *         hierarchies
	 */
	public boolean isH() {
		return getAxiomDetector().hasSubObjectPropertyOf();
	}

	/**
	 * Tells whether the axioms use inverse object properties.
	 * 
	 * @return <code>true</code> if and only if the axioms use inverse object
	 *         properties
	 */
	public boolean isI() {
		return getAxiomDetector().hasInverseObjectProperty();
	}

	/**
	 * Tells whether the axioms use cardinality restrictions. In this method,
	 * functional object properties are not considered cardinality restrictions.
	 * 
	 * @return <code>true</code> if and only if the axioms use number
	 *         restrictions
	 */
	public boolean isN() {
		return false;
	}

	/**
	 * Tells whether the axioms use nominals.
	 * 
	 * @return <code>true</code> if and only if the axioms use nominals
	 */
	public boolean isO() {
		return getAxiomDetector().hasNominal();
	}

	/**
	 * Tells whether the axioms use qualified cardinality restrictions.
	 * 
	 * @return <code>true</code> if and only if the axioms use qualified
	 *         cardinality restrictions
	 */
	public boolean isQ() {
		return false;
	}

	/**
	 * Tells whether the axioms use complex object properties or reflexive
	 * object properties.
	 * 
	 * @return <code>true</code> if and only if the axioms use complex object
	 *         properties or reflexive object properties.
	 */
	public boolean isR() {
		return getAxiomDetector().hasSubPropertyChainOf()
				|| getAxiomDetector().hasReflexiveObjectProperty();

	}

	/**
	 * Tells whether the axioms require the S logic.
	 * 
	 * @return <code>true</code> if and only if the axioms require the S logic
	 */
	public boolean isS() {
		return isAL() && isC() && isTransitive();
	}

	/**
	 * Tells whether the axioms use transitive object properties.
	 * 
	 * @return <code>true</code> if and only if the axioms use transitive object
	 *         properties
	 */
	public boolean isTransitive() {
		return getAxiomDetector().hasTransitiveObjectProperty();
	}

	/**
	 * Tells whether the axioms use class union.
	 * 
	 * @return <code>true</code> if and only if the axioms use class union
	 */
	public boolean isU() {
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

}
