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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR9 : <b>if</b> B &#8849; &exist; r<sub>2</sub><sup>-</sup> <i>.</i> A
 * &isin; <i>T</i> , <u>(r<sub>1</sub>, x, y) &isin; R</u>, (y, B) &isin; S, <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; r<sub>1</sub> &#8849; <sub><i>T</i></sub> s ,
 * r<sub>2</sub> &#8849; <sub><i>T</i></sub> s , f(s<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, A)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR9RRule implements RObserverRule {

	public CR9RRule() {
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

	private List<XEntry> applyRule(ClassifierStatus status, Integer r1,
			Integer x, Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		Set<Integer> superPropsOfR1 = new HashSet<Integer>();
		superPropsOfR1.addAll(status.getSuperObjectProperties(r1));
		for (Integer b : status.getSubsumers(y)) {
			for (GCI2Axiom axiom : status.getExtendedOntology()
					.getGCI2Axioms(b)) {
				Integer r2Minus = axiom.getPropertyInSuperClass();
				Integer r2 = status.getInverseObjectPropertyOf(r2Minus);
				Integer a = axiom.getClassInSuperClass();
				for (Integer s : status.getSuperObjectProperties(r2)) {
					if (superPropsOfR1.contains(s)) {
						Integer sMinus = status.getInverseObjectPropertyOf(s);
						if (status.getExtendedOntology()
								.getFunctionalObjectProperties()
								.contains(sMinus)) {
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
