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

package de.tudresden.inf.lat.jcel.core.completion.basic;

import java.util.Objects;
import java.util.function.Function;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.CompletionRuleMonitor;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.ExtendedOntology;

/**
 * 
 * <ul>
 * <li>CR-5 : <b>if</b> r \u2291 s &isin; <i>T</i>, <u>(r, x, y) &isin; R</u>
 * <br>
 * <b>then</b> R := R &cup; {(s, x, y)}</li>
 * </ul>
 * <br>
 * 
 * Previous form:
 * <ul>
 * <li>CR5 : <b>if</b> (X, Y) &isin; R(r) <b>and</b> r \u2291 s &isin; O
 * <b>and</b> (X, Y) &notin; R(s) <br>
 * <b>then</b> R(s) := R(s) &cup; {(X, Y)}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CR5RRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-5.
	 */
	public CR5RRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass, int rightClass) {
		Objects.requireNonNull(status);
		return applyRule(status, property, leftClass, rightClass);
	}

	private boolean applyRule(ClassifierStatus status, int property, int leftClass, int rightClass) {
		return applyRule(status.getExtendedOntology(),
				(Integer s) -> (Integer x) -> (Integer y) -> status.addNewREntry(s, x, y), property, leftClass,
				rightClass);
	}

	private boolean applyRule(ExtendedOntology ontology,
			Function<Integer, Function<Integer, Function<Integer, Boolean>>> queue, int r, int x, int y) {
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		ontology.getRI2rAxioms(r).forEach(axiom -> {
			int s = axiom.getSuperProperty();
			ret.or(queue.apply(s).apply(x).apply(y));
		});
		return ret.get();
	}

	@Override
	public boolean equals(Object o) {
		return (Objects.nonNull(o)) && getClass().equals(o.getClass());
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
