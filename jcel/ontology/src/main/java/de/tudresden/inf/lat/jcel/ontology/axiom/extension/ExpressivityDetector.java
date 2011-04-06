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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;

/**
 * An object implementing this class can detect the expressivity of a given set
 * of complex axioms.
 * 
 * @author Julian Mendez
 */
public class ExpressivityDetector implements OntologyExpressivity {

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

	private ComplexIntegerAxiomAnalyzer getAnalyzer() {
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
		if (hasSubObjectPropertyOf()) {
			sbuf.append("H");
		}
		if (hasNominal()) {
			sbuf.append("O");
		}
		if (hasInverseObjectProperty()) {
			sbuf.append("I");
		}
		if (isQ()) {
			sbuf.append("Q");
		} else if (isN()) {
			sbuf.append("N");
		}
		if (hasFunctionalObjectProperty()) {
			sbuf.append("F");
		}
		if (hasSubPropertyChainOf()) {
			sbuf.append("R");
		}
		if (hasDatatype()) {
			sbuf.append("(D)");
		}

		if (hasTransitiveObjectProperty()) {
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

	@Override
	public boolean hasBottom() {
		return getAnalyzer().hasBottom();
	}

	@Override
	public boolean hasDatatype() {
		return getAnalyzer().hasDatatype();
	}

	@Override
	public boolean hasFunctionalObjectProperty() {
		return getAnalyzer().hasFunctionalObjectProperty();
	}

	@Override
	public boolean hasIndividual() {
		return getAnalyzer().hasIndividual();
	}

	@Override
	public boolean hasInverseObjectProperty() {
		return getAnalyzer().hasInverseObjectProperty();
	}

	@Override
	public boolean hasNominal() {
		return getAnalyzer().hasNominal();
	}

	@Override
	public boolean hasReflexiveObjectProperty() {
		return getAnalyzer().hasReflexiveObjectProperty();
	}

	@Override
	public boolean hasSubObjectPropertyOf() {
		return getAnalyzer().hasSubObjectPropertyOf();
	}

	@Override
	public boolean hasSubPropertyChainOf() {
		return getAnalyzer().hasSubPropertyChainOf();

	}

	@Override
	public boolean hasTransitiveObjectProperty() {
		return getAnalyzer().hasTransitiveObjectProperty();
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
	 * Tells whether the axioms use qualified cardinality restrictions.
	 * 
	 * @return <code>true</code> if and only if the axioms use qualified
	 *         cardinality restrictions
	 */
	public boolean isQ() {
		return false;
	}

	/**
	 * Tells whether the axioms require the S logic.
	 * 
	 * @return <code>true</code> if and only if the axioms require the S logic
	 */
	public boolean isS() {
		return isAL() && isC() && hasTransitiveObjectProperty();
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
