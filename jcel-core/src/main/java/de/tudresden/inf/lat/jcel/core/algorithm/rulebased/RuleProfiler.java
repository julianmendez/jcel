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

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;

/**
 * An object implementing this class is a profiler for completion rules.
 * 
 * @author Julian Mendez
 */
public class RuleProfiler implements RObserverRule, SObserverRule {

	private final RObserverRule rListener;
	private final SObserverRule sListener;
	private long successful = 0;
	private long times = 0;
	private long totalTime = 0;

	/**
	 * Constructs a new profiler for an R-rule.
	 * 
	 * @param rule
	 *            R-rule to collect data
	 */
	public RuleProfiler(RObserverRule rule) {
		Objects.requireNonNull(rule);
		this.rListener = rule;
		this.sListener = null;
	}

	/**
	 * Constructs a new profiler for an S-rule.
	 * 
	 * @param rule
	 *            S-rule to collect data
	 */
	public RuleProfiler(SObserverRule rule) {
		Objects.requireNonNull(rule);
		this.sListener = rule;
		this.rListener = null;
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		Objects.requireNonNull(status);
		long start = (new Date()).getTime();
		boolean ret = this.sListener.apply(status, subClass, superClass);
		this.totalTime += ((new Date()).getTime() - start);
		this.times++;
		if (ret) {
			this.successful++;
		}
		return ret;
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass, int rightClass) {
		Objects.requireNonNull(status);
		long start = Calendar.getInstance().getTimeInMillis();
		boolean ret = this.rListener.apply(status, property, leftClass, rightClass);
		this.totalTime += (Calendar.getInstance().getTimeInMillis() - start);
		this.times++;
		if (ret) {
			this.successful++;
		}
		return ret;
	}

	/**
	 * Returns the number of successful applications of the completion rule. A
	 * successful application is when the returned set is not empty.
	 * 
	 * @return the number of successful applications of the completion rule
	 */
	public long getSuccessful() {
		return this.successful;
	}

	/**
	 * Returns the number of times that the completion rule was tried to be
	 * applied.
	 * 
	 * @return the number of times that the completion rule was tried to be
	 *         applied
	 */
	public long getTimes() {
		return this.times;
	}

	/**
	 * Returns the total time used to process all the entries.
	 * 
	 * @return the total time used to process all the entries
	 */
	public long getTotalTime() {
		return this.totalTime;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		if (Objects.nonNull(this.rListener)) {
			sbuf.append(this.rListener.toString());
		}
		if (Objects.nonNull(this.sListener)) {
			sbuf.append(this.sListener.toString());
		}
		sbuf.append(" ok=");
		sbuf.append(getSuccessful());
		sbuf.append(" all=");
		sbuf.append(getTimes());
		sbuf.append(" time=");
		sbuf.append(getTotalTime());
		sbuf.append(" ms \n");
		return sbuf.toString();
	}

}
