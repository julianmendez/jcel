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

import java.util.Collection;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-2 : <b>if</b> A<sub>1</sub> &#8851; A<sub>2</sub> &#8849; B &isin;
 * <i>T</i>, <u>(x, A<sub>1</sub>) &isin; S</u>, <u>(x, A<sub>2</sub>) &isin;
 * S</u> <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * Previous forms:
 * 
 * <ul>
 * <li>CR-2 : <b>if</b> A<sub>1</sub> &#8851; &hellip; &#8851; A<sub>i</sub>
 * &#8851; &hellip; &#8851; A<sub>n</sub> &#8849; B &isin; <i>T</i>, (x,
 * A<sub>1</sub>) &isin; S, &hellip; <u>(x, A<sub>i</sub>) &isin; S</u>,
 * &hellip; , (x, A<sub>n</sub>) &isin; S <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * 
 * <ul>
 * <li>CR1 : <b>if</b> A<sub>1</sub>, &hellip; , A<sub>n</sub> &isin; S(X)
 * <b>and</b> A<sub>1</sub> &#8851; &hellip; &#8851; A<sub>n</sub> &#8849; B
 * &isin; O <b>and</b> B &notin; S(X) <br />
 * <b>then</b> S(X) := S(X) &cup; {B}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR2Rule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-2.
	 */
	public CR2Rule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int x, int a) {
		boolean ret = false;
		Collection<Integer> subsumersOfX = status.getSubsumers(x);
		for (GCI1Axiom axiom : status.getExtendedOntology().getGCI1Axioms(a)) {
			boolean valid = true;
			if (a == axiom.getRightSubClass()) {
				valid = valid && subsumersOfX.contains(axiom.getLeftSubClass());
			} else {
				valid = valid
						&& subsumersOfX.contains(axiom.getRightSubClass());
			}
			if (valid) {
				int b = axiom.getSuperClass();
				ret |= status.addNewSEntry(x, b);
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
