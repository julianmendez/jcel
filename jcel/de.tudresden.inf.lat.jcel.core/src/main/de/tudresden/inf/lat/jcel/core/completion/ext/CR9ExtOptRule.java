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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;

/**
 * <p>
 * <ul>
 * <li>CR-9 (optimized) : <b>if</b> <u>(r<sub>1</sub>, x, y<sub>1</sub>) &isin;
 * R</u>, (r<sub>2</sub>, x, y<sub>2</sub>) &isin; R, &hellip;, (r<sub>n</sub>,
 * x, y<sub>n</sub>) &isin; R, <br />
 * r<sub>1</sub> &#8849;<sub><i>T</i></sub> s, r<sub>2</sub>
 * &#8849;<sub><i>T</i></sub> s, &hellip;, r<sub>n</sub>
 * &#8849;<sub><i>T</i></sub> s, y<sub>i</sub> = (&#8868; , &psi;<sub>i</sub>)
 * for 1 &le; i &le; n, y<sub>i</sub> &ne; y<sub>j</sub> for 1 &le; i < j &le;
 * n, f(s) <br />
 * <b>then</b> v := (&#8868; , &psi;<sub>1</sub> &cup; &hellip; &cup
 * &psi;<sub>n</sub>) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, k) | (y<sub>1</sub>, k) &isin; S}
 * &cup; &hellip; &cup; {(v, k) | (y<sub>n</sub>, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r<sub>1</sub>, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR9ExtOptRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-9.
	 */
	public CR9ExtOptRule() {
	}

	@Override
	public Collection<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableCollection(applyRule(status,
				entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
	}

	private Collection<XEntry> applyRule(ClassifierStatus status, Integer r1,
			Integer x, Integer y) {
		Set<XEntry> ret = new HashSet<XEntry>();
		if (status.getNode(y).getClassId().equals(status.getClassTopElement())) {

			Set<Integer> valid = new HashSet<Integer>();
			valid.add(y);
			for (Integer r2 : status
					.getObjectPropertiesWithFunctionalAncestor(r1)) {
				for (Integer yi : status.getSecondByFirst(r2, x)) {
					if (status.getNode(yi).getClassId()
							.equals(status.getClassTopElement())) {
						valid.add(yi);
					}
				}
			}

			if (valid.size() > 1) {
				VNodeImpl newNode = new VNodeImpl(status.getClassTopElement());
				for (Integer yi : valid) {
					newNode.addExistentialsOf(status.getNode(yi));
				}
				Integer v = status.createOrGetNodeId(newNode);
				for (Integer yi : valid) {
					for (Integer p : status.getSubsumers(yi)) {
						ret.add(new SEntryImpl(v, p));
					}
					ret.add(new REntryImpl(r1, x, v));
				}
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
