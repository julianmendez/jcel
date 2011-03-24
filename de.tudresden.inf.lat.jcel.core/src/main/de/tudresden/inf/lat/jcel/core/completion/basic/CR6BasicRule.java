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

import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR6 : <b>if</b> r &#8728; s &#8849; t &isin; T, <u>(r,x,y) &isin; R</u>,
 * <u>(s,y,z) &isin; R</u>, (t,x,z) &notin; R <br />
 * <b>then</b> R := R &cup; {(t,x,z)}</li>
 * </ul>
 * </p>
 * 
 * Original form:
 * <ul>
 * <li>CR6 : <b>if</b> (X,Y) &isin; R(r) <b>and</b> (Y,Z) &isin; R(s) <b>and</b>
 * r &#8728; s &#8849; t &isin; O <b>and</b> (X,Z) &notin; R(t) <br />
 * <b>then</b> R(t) := R(t) &cup; {(X,Z)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR6BasicRule implements RObserverRule {

	public CR6BasicRule() {
	}

	@Override
	public List<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<XEntry> ret = new ArrayList<XEntry>();
		ret.addAll(apply1(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
		ret.addAll(apply2(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
		return ret;
	}

	private List<XEntry> apply1(ClassifierStatus status, Integer s, Integer y,
			Integer z) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (RI3Axiom axiom : status.getExtendedOntology().getRI3AxiomsByRight(
				s)) {
			Integer r = axiom.getLeftSubProperty();
			Integer t = axiom.getSuperProperty();
			for (Integer x : status.getRelationBySecond(r, y)) {
				ret.add(new REntryImpl(t, x, z));
			}
		}
		return ret;
	}

	private List<XEntry> apply2(ClassifierStatus status, Integer r, Integer x,
			Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (RI3Axiom axiom : status.getExtendedOntology()
				.getRI3AxiomsByLeft(r)) {
			Integer s = axiom.getRightSubProperty();
			Integer t = axiom.getSuperProperty();
			for (Integer z : status.getRelationByFirst(s, y)) {
				ret.add(new REntryImpl(t, x, z));
			}
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
