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

package de.tudresden.inf.lat.jcel.core.completion.ext;

import java.util.Objects;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.CompletionRuleMonitor;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;

/**
 * 
 * <ul>
 * <li>CR-6 : <b>if</b> &exist; s<sup>-</sup> <i>.</i> A \u2291 B &isin;
 * <i>T</i>, r \u2291<sub><i>T</i></sub> s, (r, x, y) &isin; R, <u>(x, A) &isin;
 * S</u> , (y, B) &notin; S, y = (B', &psi;) <br>
 * <b>then</b> v := (B', &psi; &cup; {&exist; r <sup>-</sup> <i>.</i> A}) <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} ,
 * S := S &cup; {(v, k) | (y, k) &isin; S} <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
public class CR6SExtRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-6 (S).
	 */
	public CR6SExtRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		Objects.requireNonNull(status);
		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int x, int a) {
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		status.getExtendedOntology().getGCI3AAxioms(a).forEach(axiom -> {
			int sMinus = axiom.getPropertyInSubClass();
			int s = status.getInverseObjectPropertyOf(sMinus);
			status.getSubObjectProperties(s).forEach(r -> {
				int rMinus = status.getInverseObjectPropertyOf(r);
				int b = axiom.getSuperClass();
				status.getSecondByFirst(r, x).forEach(y -> {
					if (!status.getSubsumers(y).contains(b)) {
						VNode psiNode = status.getNode(y);
						VNodeImpl newNode = new VNodeImpl(psiNode.getClassId());
						newNode.addExistentialsOf(psiNode);
						newNode.addExistential(rMinus, a);
						boolean inV = status.contains(newNode);
						int v = status.createOrGetNodeId(newNode);
						if (!inV) {
							status.getSubsumers(y).forEach(p -> {
								ret.or(status.addNewSEntry(v, p));
							});
						}
						ret.or(status.addNewSEntry(v, b));
						ret.or(status.addNewREntry(r, x, v));
					}
				});
			});
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
