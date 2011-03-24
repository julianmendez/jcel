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
 * This interface models a relation entry and can be used by a relation
 * observer.
 * 
 * @see RObserverRule
 * 
 * @author Julian Mendez
 */
public interface REntry extends XEntry {

	/**
	 * Returns the class in the left part of this entry.
	 * 
	 * @return the class in the left part of this entry
	 */
	public Integer getLeftClass();

	/**
	 * Returns the class in the left part of this entry.
	 * 
	 * @return the class in the left part of this entry
	 */
	public Integer getProperty();

	/**
	 * Returns the object property of this entry.
	 * 
	 * @return the object property of this entry
	 */
	public Integer getRightClass();

}
