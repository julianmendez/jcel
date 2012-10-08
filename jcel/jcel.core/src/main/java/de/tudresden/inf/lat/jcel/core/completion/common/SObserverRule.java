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

package de.tudresden.inf.lat.jcel.core.completion.common;

/**
 * This is the interface of a subsumption observer. A subsumption observer is a
 * completion rule that triggers only when a new subsumption is added to the
 * classifier status. Applying this rule may trigger other completion rules for
 * a given classifier status.
 * 
 * @author Julian Mendez
 */
public interface SObserverRule {

	/**
	 * Applies the completion rule considering the specified subsumption entry.
	 * 
	 * @param status
	 *            classifier status
	 * @param subClass
	 *            sub class to be applied
	 * @param superClass
	 *            super class to be applied
	 * @return <code>true</code> if and only if the rule was applied
	 */
	public boolean apply(ClassifierStatus status, int subClass, int superClass);

}
