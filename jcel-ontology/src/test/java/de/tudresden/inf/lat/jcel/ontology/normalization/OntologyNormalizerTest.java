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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Test class for ontology normalizer.
 * 
 * @see OntologyNormalizer
 * 
 * @author Julian Mendez
 */
public class OntologyNormalizerTest {

	/**
	 * Constructs a new ontology normalizer.
	 */
	public OntologyNormalizerTest() {
	}

	private IntegerClass createNewClass(IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, name, false));
	}

	private IntegerObjectProperty createNewObjectProperty(IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, name, false));
	}

	@Test
	public void testGCI3InverseObjectProperties() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(rMinus, r, annotations));

		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus, a), b, annotations));

		OntologyNormalizer normalizer = new OntologyNormalizer();

		Set<NormalizedIntegerAxiom> normalizedSet = normalizer.normalize(ontology, factory);

		Set<NormalizedIntegerAxiom> expectedSet = new HashSet<>();
		expectedSet.add(
				factory.getNormalizedAxiomFactory().createGCI3Axiom(rMinus.getId(), a.getId(), b.getId(), annotations));

		Assert.assertEquals(expectedSet, normalizedSet);
	}

	@Test
	public void testGCI3ObjectInverseOf() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(factory.getDataTypeFactory().createObjectInverseOf(r), a), b, annotations));

		OntologyNormalizer normalizer = new OntologyNormalizer();

		Set<NormalizedIntegerAxiom> normalizedSet = normalizer.normalize(ontology, factory);

		Set<NormalizedIntegerAxiom> expectedSet = new HashSet<>();
		expectedSet.add(factory.getNormalizedAxiomFactory().createGCI3Axiom(
				factory.getEntityManager().createOrGetInverseObjectPropertyOf(r.getId()), a.getId(), b.getId(),
				annotations));

		Assert.assertEquals(expectedSet, normalizedSet);
	}

}
