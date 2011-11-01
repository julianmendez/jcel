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
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR9 : <b>if</b> B &#8849; &exist; r<sub>2</sub><sup>-</sup> <i>.</i> A
 * &isin; <i>T</i> , (r<sub>1</sub>, x, y) &isin; R, <u>(y, B) &isin; S</u>, <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; r<sub>1</sub> &#8849; <sub><i>T</i></sub> s ,
 * r<sub>2</sub> &#8849; <sub><i>T</i></sub> s , f(s<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, A)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR9SRule implements SObserverRule {

	public CR9SRule() {
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

	private List<XEntry> applyRule(ClassifierStatus status, Integer y, Integer b) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(b)) {
			Integer r2Minus = axiom.getPropertyInSuperClass();
			Integer r2 = status.getInverseObjectPropertyOf(r2Minus);
			Integer a = axiom.getClassInSuperClass();
			for (Integer s : status.getSuperObjectProperties(r2)) {
				Integer sMinus = status.getInverseObjectPropertyOf(s);
				if (status.getExtendedOntology()
						.getFunctionalObjectProperties().contains(sMinus)) {
					for (Integer r1 : status.getSubObjectProperties(s)) {
						for (Integer x : status.getRelationBySecond(r1, y)) {
							ret.add(new SEntryImpl(x, a));
						}
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
