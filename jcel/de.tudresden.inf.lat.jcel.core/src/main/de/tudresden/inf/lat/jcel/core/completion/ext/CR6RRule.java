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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;

/**
 * <p>
 * <ul>
 * <li>CR6 : <b>if</b> &exist; s<sup>-</sup> <i>.</i> A &#8849; B &isin;
 * <i>T</i> , r &#8849; <sub><i>T</i></sub> s , <u>(r, x, y) &isin; R</u>, y =
 * (B, &psi; ) , (x, A) &isin; S <br />
 * <b>then</b> v := (B, &psi; &cup; { &exist; r <sup>-</sup> <i>.</i> A }) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; { v }
 * , S := S &cup; {(v, k) | (y, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR6RRule implements RObserverRule {

	public CR6RRule() {
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
		Collection<Integer> superProps = status.getSuperObjectProperties(r);
		Integer b = status.getNode(y).getClassId();
		VNode psiNode = status.getNode(y);
		psiNode.setClassId(b);
		for (Integer a : status.getSubsumers(x)) {
			for (GCI3Axiom axiom : status.getExtendedOntology()
					.getGCI3ABAxioms(a, b)) {
				Integer sMinus = axiom.getPropertyInSubClass();
				Integer s = status.getInverseObjectPropertyOf(sMinus);

				if (superProps.contains(s)) {
					boolean inV = status.contains(psiNode);
					Integer v = status.createOrGetNodeId(psiNode);
					if (!inV) {
						for (Integer p : status.getSubsumers(y)) {
							ret.add(new SEntryImpl(v, p));
						}
					}
					ret.add(new SEntryImpl(v, b));
					ret.add(new REntryImpl(r, x, v));
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
