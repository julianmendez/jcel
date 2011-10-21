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

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManagerImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactoryImpl;

/**
 * An object of this class can create all the objects in an ontology.
 * 
 * @author Julian Mendez
 */
public class IntegerOntologyObjectFactoryImpl implements
		IntegerOntologyObjectFactory {

	private final ComplexIntegerAxiomFactory complexAxiomFactory = new ComplexIntegerAxiomFactoryImpl();
	private final IntegerDataTypeFactory dataTypeFactory = new IntegerDataTypeFactoryImpl();
	private final IntegerEntityManager idGenerator = new IntegerEntityManagerImpl();
	private final NormalizedIntegerAxiomFactory normalizedAxiomFactory = new NormalizedIntegerAxiomFactoryImpl();

	/**
	 * Constructs a new ontology object factory.
	 */
	public IntegerOntologyObjectFactoryImpl() {
	}

	@Override
	public ComplexIntegerAxiomFactory getComplexAxiomFactory() {
		return this.complexAxiomFactory;
	}

	@Override
	public IntegerDataTypeFactory getDataTypeFactory() {
		return this.dataTypeFactory;
	}

	@Override
	public IntegerEntityManager getIdGenerator() {
		return this.idGenerator;
	}

	@Override
	public NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
		return this.normalizedAxiomFactory;
	}

}
