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
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-5 : <b>if</b> r &#8849; s &isin; <i>T</i>, <u>(r, x, y) &isin; R</u> <br />
 * <b>then</b> R := R &cup; {(s, x, y)}</li>
 * </ul>
 * </p>
 * 
 * Previous form:
 * <ul>
 * <li>CR5 : <b>if</b> (X, Y) &isin; R(r) <b>and</b> r &#8849; s &isin; O
 * <b>and</b> (X, Y) &notin; R(s) <br />
 * <b>then</b> R(s) := R(s) &cup; {(X, Y)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR5RRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-5.
	 */
	public CR5RRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass,
			int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, property, leftClass, rightClass);
	}

	private boolean applyRule(ClassifierStatus status, int r, int x, int y) {
		boolean ret = false;
		for (RI2Axiom axiom : status.getExtendedOntology().getRI2rAxioms(r)) {
			int s = axiom.getSuperProperty();
			ret |= status.addNewREntry(s, x, y);
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
