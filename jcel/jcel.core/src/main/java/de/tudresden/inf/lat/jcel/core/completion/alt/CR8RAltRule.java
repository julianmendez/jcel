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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-8 : <b>if</b> A &#8849; &exist; s<sup>-</sup> <i>.</i> B &isin;
 * <i>T</i> , <u>(r, x, y) &isin; R</u>, (y, A) &isin; S, s &#8849; r &isin;
 * <i>T</i> , f(r<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8RAltRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-8 (R).
	 */
	public CR8RAltRule() {
	}

	@Override
	public Collection<XEntry> apply(ClassifierStatus status, int property,
			int leftClass, int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableCollection(applyRule(status, property,
				leftClass, rightClass));
	}

	private Collection<XEntry> applyRule(ClassifierStatus status, int r, int x,
			int y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		int rMinus = status.getInverseObjectPropertyOf(r);
		if (status.getExtendedOntology().getFunctionalObjectProperties()
				.contains(rMinus)) {
			for (int a : status.getSubsumers(y)) {
				for (GCI2Axiom axiom : status.getExtendedOntology()
						.getGCI2Axioms(a)) {
					int sMinus = axiom.getPropertyInSuperClass();
					int s = status.getInverseObjectPropertyOf(sMinus);
					Set<RI2Axiom> axiomSet = new HashSet<RI2Axiom>();
					axiomSet.addAll(status.getExtendedOntology().getRI2rAxioms(
							s));
					axiomSet.retainAll(status.getExtendedOntology()
							.getRI2sAxioms(r));
					if (!axiomSet.isEmpty()) {
						int b = axiom.getClassInSuperClass();
						ret.add(new SEntryImpl(x, b));
					}
				}
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
