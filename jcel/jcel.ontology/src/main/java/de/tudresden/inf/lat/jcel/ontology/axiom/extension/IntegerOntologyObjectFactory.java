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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;

/**
 * An object implementing this interface can create all the objects in an
 * ontology.
 * 
 * @author Julian Mendez
 */
public interface IntegerOntologyObjectFactory {

	/**
	 * Returns the complex axiom factory.
	 * 
	 * @return the complex axiom factory
	 */
	public ComplexIntegerAxiomFactory getComplexAxiomFactory();

	/**
	 * Returns the data type factory.
	 * 
	 * @return the data type factory
	 */
	public IntegerDataTypeFactory getDataTypeFactory();

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	public IntegerEntityManager getEntityManager();

	/**
	 * Returns the normalized axiom factory.
	 * 
	 * @return the normalized axiom factory
	 */
	public NormalizedIntegerAxiomFactory getNormalizedAxiomFactory();

}
