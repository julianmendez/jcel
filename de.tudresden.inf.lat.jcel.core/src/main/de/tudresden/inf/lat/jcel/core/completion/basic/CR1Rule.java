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
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI0Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR-1 : <b>if</b> A &#8849; B &isin; <i>T</i>, <u>(x, A) &isin; S</u> <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * Previous form:
 * <ul>
 * <li>CR0 : <b>if</b> A &isin; S(X) <b>and</b> A &#8849; B &isin; O <br />
 * <b>then</b> S(X) := S(X) &cup; {B}</li>
 * </ul>
 * 
 * This rule was not present in the original CEL algorithm. <br />
 * 
 * 
 * @author Julian Mendez
 */
public class CR1Rule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-1.
	 */
	public CR1Rule() {
	}

	@Override
	public List<XEntry> apply(ClassifierStatus status, SEntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableList(applyRule(status,
				entry.getSubClass(), entry.getSuperClass()));
	}

	private List<XEntry> applyRule(ClassifierStatus status, Integer x, Integer a) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (GCI0Axiom axiom : status.getExtendedOntology().getGCI0Axioms(a)) {
			Integer b = axiom.getSuperClass();
			ret.add(new SEntryImpl(x, b));
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
