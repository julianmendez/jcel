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

import java.util.Collection;

/**
 * This is the interface of a relation observer. A relation observer is a
 * completion rule that triggers only when a new relation is added to the
 * classifier status. Applying this rule may trigger other completion rules for
 * a given classifier status.
 * 
 * @author Julian Mendez
 */
public interface RObserverRule {

	/**
	 * Applies the completion rule considering the specified relation entry.
	 * 
	 * @param status
	 *            classifier status
	 * @param property
	 *            property to be applied
	 * @param leftClass
	 *            left class to be applied
	 * @param rightClass
	 *            right class to be applied
	 * @return the result of applying the rule to the specified relation entry
	 */
	public Collection<XEntry> apply(ClassifierStatus status, int property,
			int leftClass, int rightClass);

}
