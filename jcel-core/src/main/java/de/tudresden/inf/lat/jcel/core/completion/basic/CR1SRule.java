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
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.coreontology.axiom.ExtendedOntology;

/**
 * 
 * <ul>
 * <li>CR-1 : <b>if</b> A \u2291 B &isin; <i>T</i>, <u>(x, A) &isin; S</u> <br>
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * <br>
 * 
 * Previous form:
 * <ul>
 * <li>CR0 : <b>if</b> A &isin; S(X) <b>and</b> A \u2291 B &isin; O <br>
 * <b>then</b> S(X) := S(X) &cup; {B}</li>
 * </ul>
 * 
 * This rule was not present in the original CEL algorithm. <br>
 * 
 * 
 * @author Julian Mendez
 */
public class CR1SRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-1.
	 */
	public CR1SRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		Objects.requireNonNull(status);
		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int subClass, int superClass) {
		return applyRule(status.getExtendedOntology(), (Integer x) -> (Integer b) -> status.addNewSEntry(x, b),
				subClass, superClass);
	}

	private boolean applyRule(ExtendedOntology ontology, Function<Integer, Function<Integer, Boolean>> queue, int x,
			int a) {
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		ontology.getGCI0Axioms(a).forEach(axiom -> {
			int b = axiom.getSuperClass();
			ret.or(queue.apply(x).apply(b));
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
