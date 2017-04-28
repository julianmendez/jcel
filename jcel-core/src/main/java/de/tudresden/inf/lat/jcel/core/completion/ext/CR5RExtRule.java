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
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;

/**
 * 
 * <ul>
 * <li>CR-5 : <b>if</b> s \u2218 s \u2291 s &isin; <i>T</i> , <u>(r<sub>1</sub>,
 * x, y) &isin; R</u>, <u>(r<sub>2</sub>, y, z) &isin; R</u>, r<sub>1</sub>
 * \u2291<sub><i>T</i></sub> s, r<sub>2</sub> \u2291<sub><i>T</i></sub> s <br>
 * <b>then</b> R := R &cup; {(s, x, z)}</li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
public class CR5RExtRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-5.
	 */
	public CR5RExtRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass, int rightClass) {
		Objects.requireNonNull(status);
		boolean ret = false;
		ret |= apply1(status, property, leftClass, rightClass);
		ret |= apply2(status, property, leftClass, rightClass);
		return ret;
	}

	private boolean apply1(ClassifierStatus status, int r1, int x, int y) {
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		status.getSuperObjectProperties(r1).forEach(s -> {
			if (status.getExtendedOntology().getTransitiveObjectProperties().contains(s)) {
				status.getSubObjectProperties(s).forEach(r2 -> {
					status.getSecondByFirst(r2, y).forEach(z -> {
						ret.or(status.addNewREntry(s, x, z));
					});
				});
			}
		});
		return ret.get();
	}

	private boolean apply2(ClassifierStatus status, int r2, int y, int z) {
		CompletionRuleMonitor ret = new CompletionRuleMonitor();
		status.getSuperObjectProperties(r2).forEach(s -> {
			if (status.getExtendedOntology().getTransitiveObjectProperties().contains(s)) {
				status.getSubObjectProperties(s).forEach(r1 -> {
					status.getFirstBySecond(r1, y).forEach(x -> {
						ret.or(status.addNewREntry(s, x, z));
					});
				});
			}
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
