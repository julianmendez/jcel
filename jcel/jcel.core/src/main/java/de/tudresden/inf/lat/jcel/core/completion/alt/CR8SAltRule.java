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

package de.tudresden.inf.lat.jcel.core.completion.alt;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-8 : <b>if</b> A &#8849; &exist; s<sup>-</sup> <i>.</i> B &isin;
 * <i>T</i> , (r, x, y) &isin; R, <u>(y, A) &isin; S</u>, s &#8849; r &isin;
 * <i>T</i>, f(r<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8SAltRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-8 (S).
	 */
	public CR8SAltRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int y, int a) {
		boolean ret = false;
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
			int sMinus = axiom.getPropertyInSuperClass();
			int s = status.getInverseObjectPropertyOf(sMinus);
			int b = axiom.getClassInSuperClass();
			for (RI2Axiom riAxiom : status.getExtendedOntology().getRI2rAxioms(
					s)) {
				int r = riAxiom.getSuperProperty();
				int rMinus = status.getInverseObjectPropertyOf(r);
				if (status.getExtendedOntology()
						.getFunctionalObjectProperties().contains(rMinus)) {
					for (int x : status.getFirstBySecond(r, y)) {
						ret |= status.addNewSEntry(x, b);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		return (o != null) && getClass().equals(o.getClass());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
