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

package de.tudresden.inf.lat.jcel.core.completion.ext;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-8 : <b>if</b> A &#8849; &exist; r<sub>2</sub><sup>-</sup> <i>.</i> B
 * &isin; <i>T</i> , <u>(r<sub>1</sub>, x, y) &isin; R</u>, (y, A) &isin; S,
 * r<sub>1</sub> &#8849;<sub><i>T</i></sub> s, r<sub>2</sub>
 * &#8849;<sub><i>T</i></sub> s, f(s<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8RExtRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-8 (R).
	 */
	public CR8RExtRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass,
			int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, property, leftClass, rightClass);
	}

	private boolean applyRule(ClassifierStatus status, int r1, int x, int y) {
		boolean ret = false;
		for (int s : status.getSuperObjectProperties(r1)) {
			int sMinus = status.getInverseObjectPropertyOf(s);
			if (status.getExtendedOntology().getFunctionalObjectProperties()
					.contains(sMinus)) {
				for (int a : status.getSubsumers(y)) {
					for (GCI2Axiom axiom : status.getExtendedOntology()
							.getGCI2Axioms(a)) {
						int r2Minus = axiom.getPropertyInSuperClass();
						int r2 = status.getInverseObjectPropertyOf(r2Minus);
						if (status.getSubObjectProperties(s).contains(r2)) {
							int b = axiom.getClassInSuperClass();
							ret |= status.addNewSEntry(x, b);
						}
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
