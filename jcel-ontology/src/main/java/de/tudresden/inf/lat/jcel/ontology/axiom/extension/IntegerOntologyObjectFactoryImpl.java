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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManagerImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataTypeFactoryImpl;

/**
 * An object of this class can create all the objects in an ontology.
 * 
 * @author Julian Mendez
 */
public class IntegerOntologyObjectFactoryImpl implements IntegerOntologyObjectFactory {

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
	public IntegerEntityManager getEntityManager() {
		return this.idGenerator;
	}

	@Override
	public NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
		return this.normalizedAxiomFactory;
	}

}
