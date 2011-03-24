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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI1Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR1 : <b>if</b> A<sub>1</sub> &#8851; &hellip; &#8851; A<sub>n</sub>
 * &#8849; B &isin; T, <u>(x,A<sub>1</sub>) &isin; S</u>, (x,A<sub>2</sub>)
 * &isin; S, &hellip; , (x,A<sub>n</sub>) &isin; S, (x,B) &notin; S <br />
 * <b>then</b> S := S &cup; {(x,B)}</li>
 * </ul>
 * </p>
 * 
 * Original form:
 * <ul>
 * <li>CR1 : <b>if</b> A<sub>1</sub>, &hellip; , A<sub>n</sub> &isin; S(X)
 * <b>and</b> A<sub>1</sub> &#8851; &hellip; &#8851; A<sub>n</sub> &#8849; B
 * &isin; O <b>and</b> B &notin; S(X) <br />
 * <b>then</b> S(X) := S(X) &cup; {B}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR1BasicRule implements SObserverRule {

	public CR1BasicRule() {
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
		Collection<Integer> subsumers = status.getSubsumers(x);
		for (GCI1Axiom axiom : status.getExtendedOntology().getGCI1Axioms(a)) {
			boolean valid = true;
			for (Iterator<Integer> it = axiom.getOperands().iterator(); valid
					&& it.hasNext();) {
				valid = valid && subsumers.contains(it.next());
			}
			if (valid) {
				ret.add(new SEntryImpl(x, axiom.getSuperClass()));
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
