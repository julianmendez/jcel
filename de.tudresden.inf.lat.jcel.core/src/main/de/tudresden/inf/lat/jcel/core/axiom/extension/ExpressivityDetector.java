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

	public boolean hasBottom() {
		return getAxiomDetector().hasBottom();
	}

	public boolean hasIndividual() {
		return getAxiomDetector().hasIndividual();
	}

	public boolean isAL() {
		return false;
	}

	public boolean isC() {
		return false;
	}

	public boolean isD() {
		return getAxiomDetector().hasDatatype();
	}

	public boolean isE() {
		return true;
	}

	public boolean isEL() {
		return true;
	}

	public boolean isF() {
		return getAxiomDetector().hasFunctionalObjectProperty();
	}

	public boolean isH() {
		return getAxiomDetector().hasSubObjectPropertyOf();
	}

	public boolean isI() {
		return getAxiomDetector().hasInverseObjectProperty();
	}

	public boolean isN() {
		return false;
	}

	public boolean isO() {
		return getAxiomDetector().hasNominal();
	}

	public boolean isQ() {
		return false;
	}

	public boolean isR() {
		return getAxiomDetector().hasSubPropertyChainOf()
				|| getAxiomDetector().hasReflexiveObjectProperty();

	}

	public boolean isS() {
		return isAL() && isC() && isTransitive();
	}

	public boolean isTransitive() {
		return getAxiomDetector().hasTransitiveObjectProperty();
	}

	public boolean isU() {
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

}
