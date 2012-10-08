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

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;

/**
 * <p>
 * <ul>
 * <li>CR-5 : <b>if</b> s &#8728; s &#8849; s &isin; <i>T</i> ,
 * <u>(r<sub>1</sub>, x, y) &isin; R</u>, <u>(r<sub>2</sub>, y, z) &isin; R</u>,
 * r<sub>1</sub> &#8849;<sub><i>T</i></sub> s, r<sub>2</sub>
 * &#8849;<sub><i>T</i></sub> s <br />
 * <b>then</b> R := R &cup; {(s, x, z)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR5ExtRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-5.
	 */
	public CR5ExtRule() {
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

	private boolean apply1(ClassifierStatus status, int r1, int x, int y) {
		boolean ret = false;
		for (int s : status.getSuperObjectProperties(r1)) {
			if (status.getExtendedOntology().getTransitiveObjectProperties()
					.contains(s)) {
				for (int r2 : status.getSubObjectProperties(s)) {
					for (int z : status.getSecondByFirst(r2, y)) {
						ret |= status.addNewREntry(s, x, z);
					}
				}
			}
		}
		return ret;
	}

	private boolean apply2(ClassifierStatus status, int r2, int y, int z) {
		boolean ret = false;
		for (int s : status.getSuperObjectProperties(r2)) {
			if (status.getExtendedOntology().getTransitiveObjectProperties()
					.contains(s)) {
				for (int r1 : status.getSubObjectProperties(s)) {
					for (int x : status.getFirstBySecond(r1, y)) {
						ret |= status.addNewREntry(s, x, z);
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
