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

package de.tudresden.inf.lat.jcel.core.completion.basic;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-3 : <b>if</b> A &#8849; &exist; r <i>.</i> B &isin; <i>T</i>, <u>(x,
 * A) &isin; S</u> <br />
 * <b>then</b> R := R &cup;{(r, x, B)}</li>
 * </ul>
 * </p>
 * 
 * Previous form:
 * <ul>
 * <li>CR2 : <b>if</b> A &isin; S(X) <b>and</b> A &#8849; &exist; r <i>.</i> B
 * &isin; O <b>and</b> (X, B) &notin; R(r) <br />
 * <b>then</b> R(r) := R(r) &cup;{(X, B)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR3Rule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-3.
	 */
	public CR3Rule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int x, int a) {
		boolean ret = false;
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
			int r = axiom.getPropertyInSuperClass();
			int b = axiom.getClassInSuperClass();
			ret |= status.addNewREntry(r, x, b);
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
