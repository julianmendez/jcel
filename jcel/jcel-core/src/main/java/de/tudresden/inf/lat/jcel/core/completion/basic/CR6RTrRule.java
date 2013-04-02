/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core.completion.basic;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;

/**
 * <p>
 * <ul>
 * <li>CR-6 : <b>if</b> r &#8728; r &#8849; r &isin; <i>T</i>, <u>(r, x, y)
 * &isin; R</u>, <u>(r, y, z) &isin; R</u> <br />
 * <b>then</b> R := R &cup; {(r, x, z)}</li>
 * </ul>
 * </p>
 * 
 * This is a particular case of role composition.
 * 
 * Previous forms:
 * <ul>
 * <li>CR-6 : <b>if</b> r &#8728; s &#8849; t &isin; <i>T</i>, <u>(r, x, y)
 * &isin; R</u>, <u>(s, y, z) &isin; R</u> <br />
 * <b>then</b> R := R &cup; {(t, x, z)}</li>
 * </ul>
 * 
 * <ul>
 * <li>CR6 : <b>if</b> (X, Y) &isin; R(r) <b>and</b> (Y,Z) &isin; R(s)
 * <b>and</b> r &#8728; s &#8849; t &isin; O <b>and</b> (X,Z) &notin; R(t) <br />
 * <b>then</b> R(t) := R(t) &cup; {(X, Z)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR6RTrRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-6 transitive.
	 */
	public CR6RTrRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass,
			int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		ret |= apply1(status, property, leftClass, rightClass);
		ret |= apply2(status, property, leftClass, rightClass);
		return ret;
	}

	private boolean apply1(ClassifierStatus status, int r, int x, int y) {
		boolean ret = false;
		if (status.getExtendedOntology().getTransitiveObjectProperties()
				.contains(r)) {
			for (int z : status.getSecondByFirst(r, y)) {
				ret |= status.addNewREntry(r, x, z);
			}
		}
		return ret;
	}

	private boolean apply2(ClassifierStatus status, int r, int y, int z) {
		boolean ret = false;
		if (status.getExtendedOntology().getTransitiveObjectProperties()
				.contains(r)) {
			for (int x : status.getFirstBySecond(r, y)) {
				ret |= status.addNewREntry(r, x, z);
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
