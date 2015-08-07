/*
 *
 * Copyright (C) 2009-2015 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.core.completion.ext;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;

/**
 * 
 * <ul>
 * <li>CR-8 : <b>if</b> A \u2291 &exist; r<sub>2</sub><sup>-</sup> <i>.</i> B
 * &isin; <i>T</i> , (r<sub>1</sub>, x, y) &isin; R, <u>(y, A) &isin; S</u>,
 * r<sub>1</sub> \u2291<sub><i>T</i></sub> s, r<sub>2</sub>
 * \u2291<sub><i>T</i></sub> s, f(s<sup>-</sup>) <br>
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
public class CR8SExtRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-8 (S).
	 */
	public CR8SExtRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int y, int a) {
		boolean ret = false;
		for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
			int r2Minus = axiom.getPropertyInSuperClass();
			int r2 = status.getInverseObjectPropertyOf(r2Minus);
			for (int s : status.getSuperObjectProperties(r2)) {
				int sMinus = status.getInverseObjectPropertyOf(s);
				if (status.getExtendedOntology()
						.getFunctionalObjectProperties().contains(sMinus)) {
					int b = axiom.getClassInSuperClass();
					for (int r1 : status.getSubObjectProperties(s)) {
						for (int x : status.getFirstBySecond(r1, y)) {
							ret |= status.addNewSEntry(x, b);
						}
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
