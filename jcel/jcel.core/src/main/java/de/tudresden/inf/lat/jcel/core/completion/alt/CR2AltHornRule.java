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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-2 : <b>if</b> A<sub>1</sub> &#8851; &hellip; &#8851; A<sub>i</sub>
 * &#8851; &hellip; &#8851; A<sub>n</sub> &#8849; B &isin; <i>T</i>, <u>(x,
 * A<sub>1</sub>) &isin; S</u>, &hellip; , <u>(x, A<sub>i</sub>) &isin; S</u>,
 * &hellip; , <u>(x, A<sub>n</sub>) &isin; S</u> <br />
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
 * <br />
 * 
 * This rule uses a technique of Horn clauses and must be used during the entire
 * classification process and disposed after that. <br />
 * 
 * @author Julian Mendez
 */
public class CR2AltHornRule implements SObserverRule {

	private Map<Integer, Map<GCI1Axiom, Integer>> counters = new HashMap<Integer, Map<GCI1Axiom, Integer>>();

	/**
	 * Constructs a new completion rule CR-2 using a Horn-clause technique.
	 */
	public CR2AltHornRule() {
	}

	@Override
	public Collection<XEntry> apply(ClassifierStatus status, SEntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableCollection(applyRule(status,
				entry.getSubClass(), entry.getSuperClass()));
	}

	private Collection<XEntry> applyRule(ClassifierStatus status, int x, int a) {
		List<XEntry> ret = new ArrayList<XEntry>();
		Map<GCI1Axiom, Integer> maps = this.counters.get(x);
		if (maps == null) {
			maps = new HashMap<GCI1Axiom, Integer>();
			this.counters.put(x, maps);
		}
		for (GCI1Axiom axiom : status.getExtendedOntology().getGCI1Axioms(a)) {
			Integer count = maps.get(axiom);
			if (count == null) {
				count = axiom.getOperands().size();
			}
			count--;
			if (count.equals(0)) {
				maps.remove(axiom);
				if (maps.isEmpty()) {
					this.counters.remove(x);
				}
				int b = axiom.getSuperClass();
				ret.add(new SEntryImpl(x, b));
			}
			maps.put(axiom, count);
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
