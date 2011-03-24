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

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR2 : <b>if</b> A &#8849; &exist; r <i>.</i> B &isin; T, <u>(x,A) &isin;
 * S</u>, (r,x,B) &notin; R <br />
 * <b>then</b> R := R &cup;{(r,x,B)}</li>
 * </ul>
 * </p>
 * 
 * Original form:
 * <ul>
 * <li>CR2 : <b>if</b> A &isin; S(X) <b>and</b> A &#8849; &exist; r <i>.</i> B
 * &isin; O <b>and</b> (X,B) &notin; R(r) <br />
 * <b>then</b> R(r) := R(r) &cup;{(X,B)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR2BasicRule implements SObserverRule {

	public CR2BasicRule() {
	}

	@Override
	public List<XEntry> apply(ClassifierStatus status, SEntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, entry.getSubClass(), entry.getSuperClass());
	}

	private List<XEntry> applyRule(ClassifierStatus status, Integer x, Integer a) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
			ret.add(new REntryImpl(axiom.getPropertyInSuperClass(), x, axiom
					.getClassInSuperClass()));
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
