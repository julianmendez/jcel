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
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * <p>
 * <ul>
 * <li>CR-6 : <b>if</b> r &#8728; r &#8849; r &isin; <i>T</i> , <u>(r, x, y)
 * &isin; R</u>, <u>(r, y, z) &isin; R</u> <br />
 * <b>then</b> R := R &cup; {(r, x, z)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR6ExtRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-6.
	 */
	public CR6ExtRule() {
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
		return Collections.unmodifiableList(ret);
	}

	private List<XEntry> apply1(ClassifierStatus status, Integer r, Integer x,
			Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		if (status.getExtendedOntology().getTransitiveObjectProperties()
				.contains(r)) {
			for (Integer z : status.getSecondByFirst(r, y)) {
				ret.add(new REntryImpl(r, x, z));
			}
		}
		return ret;
	}

	private List<XEntry> apply2(ClassifierStatus status, Integer r, Integer y,
			Integer z) {
		List<XEntry> ret = new ArrayList<XEntry>();
		if (status.getExtendedOntology().getTransitiveObjectProperties()
				.contains(r)) {
			for (Integer x : status.getFirstBySecond(r, y)) {
				ret.add(new REntryImpl(r, x, z));
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
