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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.CompletionRuleMonitor;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * 
 * <ul>
 * <li>CR-9 (optimized) : <b>if</b> <u>(r<sub>1</sub>, x, y<sub>1</sub>) &isin;
 * R</u>, (r<sub>2</sub>, x, y<sub>2</sub>) &isin; R, &hellip;, (r<sub>n</sub>,
 * x, y<sub>n</sub>) &isin; R, <br>
 * r<sub>1</sub> \u2291<sub><i>T</i></sub> s, r<sub>2</sub> \u2291
 * <sub><i>T</i></sub> s, &hellip;, r<sub>n</sub> \u2291<sub><i>T</i></sub> s, y
 * <sub>i</sub> = (\u22A4 , &psi;<sub>i</sub>) for 1 &le; i &le; n, y
 * <sub>i</sub> &ne; y<sub>j</sub> for 1 &le; i &lt; j &le; n, f(s) <br>
 * <b>then</b> v := (\u22A4 , &psi;<sub>1</sub> &cup; &hellip; &cup; &psi;
 * <sub>n</sub>) <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v}
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, k) | (y<sub>1</sub>, k) &isin; S}
 * &cup; &hellip; &cup; {(v, k) | (y<sub>n</sub>, k) &isin; S} <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r<sub>1</sub>, x, v)}</li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
public class CR9RExtOptRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-9.
	 */
	public CR9RExtOptRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass, int rightClass) {
		Objects.requireNonNull(status);
		return applyRule(status, property, leftClass, rightClass);
	}

	private boolean applyRule(ClassifierStatus status, int r1, int x, int y) {
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		if (status.getNode(y).getClassId() == IntegerEntityManager.topClassId) {

			Set<Integer> valid = new HashSet<>();
			valid.add(y);
			status.getObjectPropertiesWithFunctionalAncestor(r1).forEach(r2 -> {
				status.getSecondByFirst(r2, x).forEach(yi -> {
					if (status.getNode(yi).getClassId() == IntegerEntityManager.topClassId) {
						valid.add(yi);
					}
				});
			});

			if (valid.size() > 1) {
				VNodeImpl newNode = new VNodeImpl(IntegerEntityManager.topClassId);
				valid.forEach(yi -> {
					newNode.addExistentialsOf(status.getNode(yi));
				});
				int v = status.createOrGetNodeId(newNode);
				valid.forEach(yi -> {

					status.getSubsumers(yi).forEach(p -> {
						ret.or(status.addNewSEntry(v, p));
					});

					ret.or(status.addNewREntry(r1, x, v));
				});
			}

		}
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
