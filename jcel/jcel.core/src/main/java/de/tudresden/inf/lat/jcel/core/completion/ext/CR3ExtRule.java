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
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.GCI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-3 : <b>if</b> A &#8849; &exist; r <i>.</i> B &isin; <i>T</i> , <u>(x,
 * A) &isin; S</u> <br />
 * <b>then</b> <b>if</b> f(r) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>then</b> v := (&#8868; , {&exist; r<sup>-</sup>
 * <i>.</i> A}) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V
 * <b>then</b> V := V &cup; {v} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} &cup;
 * {(v, &#8868;)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>else</b> y := (B, &empty;) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, y)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR3ExtRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-3.
	 */
	public CR3ExtRule() {
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
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
			int r = axiom.getPropertyInSuperClass();
			int b = axiom.getClassInSuperClass();
			if (status.getExtendedOntology().getFunctionalObjectProperties()
					.contains(r)) {
				VNodeImpl newNode = new VNodeImpl(status.getClassTopElement());
				int rMinus = status.getInverseObjectPropertyOf(r);
				newNode.addExistential(rMinus, a);
				int v = status.createOrGetNodeId(newNode);
				ret.add(new SEntryImpl(v, b));
				ret.add(new SEntryImpl(v, status.getClassTopElement()));
				ret.add(new REntryImpl(r, x, v));
			} else {
				int y = status.createOrGetNodeId(new VNodeImpl(b));
				ret.add(new REntryImpl(r, x, y));
			}
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
