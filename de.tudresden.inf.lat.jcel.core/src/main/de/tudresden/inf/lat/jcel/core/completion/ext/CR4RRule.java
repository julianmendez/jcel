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

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR4 : <b>if</b> &exist; s <i>.</i> B &#8849; A &isin; <i>T</i> , <u>(r,
 * x, y) &isin; R</u>, (y, B) &isin; S, r &#8849; <sub><i>T</i> </sub> s <br />
 * <b>then</b> S := S &cup; {(x, A)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR4RRule implements RObserverRule {

	public CR4RRule() {
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
		List<Integer> list = new ArrayList<Integer>();
		for (Integer cBprime : status.getSubsumers(y)) {
			for (GCI3Axiom axiom : status.getExtendedOntology()
					.getGCI3rAAxioms(r, cBprime)) {
				list.add(axiom.getSuperClass());
			}
		}
		for (Integer elem : list) {
			ret.add(new SEntryImpl(x, elem));
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
