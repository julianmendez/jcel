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

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR5 : <b>if</b> s &#8728; s &#8849; s &isin; <i>T</i> ,
 * <u>(r<sub>1</sub>, x, y) &isin; R</u>, <u>(r<sub>2</sub>, y, z) &isin; R</u>,
 * r<sub>1</sub> &#8849; <sub><i>T</i></sub> s , r<sub>2</sub> &#8849;
 * <sub><i>T</i></sub> s <br />
 * <b>then</b> R := R &cup; { (s, x, z) }</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR5Rule implements RObserverRule {

	public CR5Rule() {
	}

	@Override
	public List<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<XEntry> ret = new ArrayList<XEntry>();
		ret.addAll(apply1(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
		ret.addAll(apply2(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
		return ret;
	}

	private List<XEntry> apply1(ClassifierStatus status, Integer r1, Integer x,
			Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (Integer s : status.getSuperObjectProperties(r1)) {
			if (status.getExtendedOntology().getTransitiveObjectProperties()
					.contains(s)) {
				for (Integer r2 : status.getSubObjectProperties(s)) {
					for (Integer z : status.getRelationByFirst(r2, y)) {
						ret.add(new REntryImpl(s, x, z));
					}
				}
			}
		}
		return ret;
	}

	private List<XEntry> apply2(ClassifierStatus status, Integer r2, Integer y,
			Integer z) {
		List<XEntry> ret = new ArrayList<XEntry>();
		for (Integer s : status.getSuperObjectProperties(r2)) {
			if (status.getExtendedOntology().getTransitiveObjectProperties()
					.contains(s)) {
				for (Integer r1 : status.getSubObjectProperties(s)) {
					for (Integer x : status.getRelationBySecond(r1, z)) {
						ret.add(new REntryImpl(s, x, z));
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
