/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.CompletionRuleMonitor;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;

/**
 * An object implementing this class is a completion rule chain for the set of
 * relations.
 * 
 * @author Julian Mendez
 */
public class RChain implements RObserverRule {

	private final List<RObserverRule> chain;

	/**
	 * Constructs a new chain for the set of relations.
	 * 
	 * @param ch
	 *            list of rules
	 */
	public RChain(List<RObserverRule> ch) {
		Objects.requireNonNull(ch);
		this.chain = ch;
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass, int rightClass) {
		Objects.requireNonNull(status);
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		this.chain.forEach(elem -> {
			ret.or(elem.apply(status, property, leftClass, rightClass));
		});
		return ret.get();
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
		getList().forEach(elem -> {
			sbuf.append(elem.toString());
			sbuf.append(" ");
		});
		sbuf.append(")");
		return sbuf.toString();
	}

}
