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

import java.util.ArrayList;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR5 : <b>if</b> r &#8849; s &isin; T, <u>(r,x,y) &isin; R</u>,(s,x,y)
 * &notin; R <br />
 * <b>then</b> R := R &cup; {(s,x,y)}</li>
 * </ul>
 * </p>
 * 
 * Original form:
 * <ul>
 * <li>CR5 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> r &#8849; s &isin; O
 * <b>and</b> (X,Y) &notin; R(s) <br />
 * <b>then</b> R(s) := R(s) &cup; {(X,Y)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR5BasicRule implements RObserverRule {

	public CR5BasicRule() {
	}

	@Override
	public List<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass());
	}

	private List<XEntry> applyRule(ClassifierStatus status, Integer r,
			Integer x, Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (RI2Axiom axiom : status.getExtendedOntology().getRI2Axioms(r)) {
			Integer s = axiom.getSuperProperty();
			ret.add(new REntryImpl(s, x, y));
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		return getClass().equals(o.getClass());
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
