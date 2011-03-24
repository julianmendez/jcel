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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;

/**
 * An object implementing this class is a completion rule chain for the set of
 * relations.
 * 
 * @author Julian Mendez
 */
public class RChain implements RObserverRule {

	private List<RObserverRule> chain = null;

	/**
	 * Constructs a new chain for the set of relations.
	 * 
	 * @param ch
	 *            list of rules
	 */
	public RChain(List<RObserverRule> ch) {
		if (ch == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.chain = ch;
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
		for (RObserverRule elem : this.chain) {
			ret.addAll(elem.apply(status, entry));
		}
		return Collections.unmodifiableList(ret);
	}

	/**
	 * Returns the list of relation observers.
	 * 
	 * @return the list of relation observers
	 */
	public List<RObserverRule> getList() {
		return Collections.unmodifiableList(this.chain);
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(");
		for (RObserverRule elem : getList()) {
			sbuf.append(elem.toString());
			sbuf.append(" ");
		}
		sbuf.append(")");
		return sbuf.toString();
	}

}
