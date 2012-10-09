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

import java.util.Calendar;
import java.util.Date;

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
		if (rule == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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
		if (rule == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.sListener = rule;
		this.rListener = null;
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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
	public boolean apply(ClassifierStatus status, int property, int leftClass,
			int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		long start = Calendar.getInstance().getTimeInMillis();
		boolean ret = this.rListener.apply(status, property, leftClass,
				rightClass);
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
		if (this.rListener != null) {
			sbuf.append(this.rListener.toString());
		}
		if (this.sListener != null) {
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