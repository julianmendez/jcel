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
 * This interface models a subsumer entry and can be used by a subsumer
 * observer.
 * 
 * @see SObserverRule
 * 
 * @author Julian Mendez
 */
public interface SEntry extends XEntry {

	/**
	 * Returns the subclass of this entry.
	 * 
	 * @return the subclass of this entry
	 */
	public Integer getSubClass();

	/**
	 * Returns the superclass of this entry.
	 * 
	 * @return the superclass of this entry
	 */
	public Integer getSuperClass();

}
