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
import java.util.List;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;

/**
 * <p>
 * <ul>
 * <li>CR8 : <b>if</b> A &#8849; &exist; r <i>.</i> B &isin; <i>T</i> , <u>(x,
 * A) &isin; S</u>, y = (B, &empty; ) , z = (&#8868; , {&exist; r<sup>-</sup>
 * <i>.</i> A}) <br />
 * <b>then</b> <b>if</b> f(r) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>then</b> R := R &cup; {(r, x, z)} , S := S &cup;
 * {(z, B)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>else</b> R := R &cup; { (r, x, y) }</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8Rule implements SObserverRule {

	public CR8Rule() {
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
			Integer r = axiom.getPropertyInSuperClass();
			Integer b = axiom.getClassInSuperClass();
			if (status.getExtendedOntology().getFunctionalObjectProperties()
					.contains(r)) {
				VNode node = new VNodeImpl(status.getClassTopElement());
				Integer rMinus = status.getInverseObjectPropertyOf(r);
				node.addExistential(rMinus, a);
				Integer z = status.createOrGetNodeId(node);
				ret.add(new REntryImpl(r, x, z));
				ret.add(new SEntryImpl(z, b));
			} else {
				Integer y = status.createOrGetNodeId(new VNodeImpl(b));
				ret.add(new REntryImpl(r, x, y));
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
