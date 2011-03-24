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
import java.util.List;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR-8 : <b>if</b> A &#8849; &exist; s<sup>-</sup> <i>.</i> B &isin;
 * <i>T</i> , (r, x, y) &isin; R, <u>(y, A) &isin; S</u>, s &#8849; r &isin;
 * <i>T</i>, f(r<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8SAltRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-8 (S).
	 */
	public CR8SAltRule() {
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

	private Collection<XEntry> applyRule(ClassifierStatus status, Integer y,
			Integer a) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
			Integer sMinus = axiom.getPropertyInSuperClass();
			Integer s = status.getInverseObjectPropertyOf(sMinus);
			Integer b = axiom.getClassInSuperClass();
			for (RI2Axiom riAxiom : status.getExtendedOntology().getRI2rAxioms(
					s)) {
				Integer r = riAxiom.getSuperProperty();
				Integer rMinus = status.getInverseObjectPropertyOf(r);
				if (status.getExtendedOntology()
						.getFunctionalObjectProperties().contains(rMinus)) {
					for (Integer x : status.getFirstBySecond(r, y)) {
						ret.add(new SEntryImpl(x, b));
					}
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
