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

package de.tudresden.inf.lat.jcel.core.datatype;

/**
 * This interface is implemented by classes that model descriptions with integer
 * numbers.
 * 
 * @author Julian Mendez
 */
public interface IntegerDescription {

	public static final Integer NOTHING = 0;
	public static final Integer THING = 1;

	public <T> T accept(IntegerDescriptionVisitor<T> visitor);

	/**
	 * Tell whether or not this description contains a bottom.
	 * 
	 * @return <code>true</code> if and only if this description contains a
	 *         bottom
	 */
	public boolean containsBottom();

	/**
	 * Tell whether or not this description contains only literals.
	 * 
	 * @return <code>true</code> if and only if this description contains only
	 *         literals
	 */
	public boolean hasOnlyLiterals();

	/**
	 * Tell whether or not this description is a literal.
	 * 
	 * @return <code>true</code> if and only if this description is a literal
	 */
	public boolean isLiteral();
}
