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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Set;

/**
 * An object implementing this interface is an object used for data types based
 * on integer numbers.
 * 
 * @author Julian Mendez
 */
public interface IntegerDatatype {

	public static final Integer classBottomElement = 0;
	public static final Integer classTopElement = 1;
	public static final Integer propertyBottomElement = 0;
	public static final Integer propertyTopElement = 1;

	/**
	 * Returns the classes in the signature.
	 * 
	 * @return the classes in the signature.
	 */
	public Set<Integer> getClassesInSignature();

	/**
	 * Returns the data properties in the signature.
	 * 
	 * @return the data properties in the signature.
	 */
	public Set<Integer> getDataPropertiesInSignature();

	/**
	 * Returns the data types in the signature.
	 * 
	 * @return the data types in the signature.
	 */
	public Set<Integer> getDatatypesInSignature();

	/**
	 * Returns the individuals in the signature.
	 * 
	 * @return the individuals in the signature.
	 */
	public Set<Integer> getIndividualsInSignature();

	/**
	 * Returns the object properties in the signature.
	 * 
	 * @return the object properties in the signature.
	 */
	public Set<Integer> getObjectPropertiesInSignature();

}
