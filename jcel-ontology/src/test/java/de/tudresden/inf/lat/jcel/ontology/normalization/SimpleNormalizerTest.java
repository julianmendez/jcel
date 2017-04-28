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
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * 
 * A(i0), B(i1), r(i0, i1) \u219D I0 &equiv; {i0}, I1 &equiv; {i1}, I0 \u2291 A,
 * I1 \u2291 B, I0 \u2291 &exist; r . I1
 * 
 * @author Julian Mendez
 * 
 */
public class SimpleNormalizerTest {

	@Test
	public void testSimpleNormalizer() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		SimpleNormalizer normalizer = new SimpleNormalizer(factory);

		int classAId = factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "a", false);
		IntegerClass classA = factory.getDataTypeFactory().createClass(classAId);

		int classBId = factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "b", false);
		IntegerClass classB = factory.getDataTypeFactory().createClass(classBId);

		int propertyRId = factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r", false);
		IntegerObjectProperty propertyR = factory.getDataTypeFactory().createObjectProperty(propertyRId);

		int indivI0Id = factory.getEntityManager().createNamedEntity(IntegerEntityType.INDIVIDUAL, "i0", false);
		int indivI1Id = factory.getEntityManager().createNamedEntity(IntegerEntityType.INDIVIDUAL, "i1", false);

		IntegerClassAssertionAxiom axiom0 = factory.getComplexAxiomFactory().createClassAssertionAxiom(classA,
				indivI0Id, annotations);
		IntegerClassAssertionAxiom axiom1 = factory.getComplexAxiomFactory().createClassAssertionAxiom(classB,
				indivI1Id, annotations);
		IntegerObjectPropertyAssertionAxiom axiom2 = factory.getComplexAxiomFactory()
				.createObjectPropertyAssertionAxiom(propertyR, indivI0Id, indivI1Id, annotations);

		Set<IntegerAxiom> firstIterationAxioms = new HashSet<>();
		firstIterationAxioms.addAll(normalizer.normalize(axiom0));
		firstIterationAxioms.addAll(normalizer.normalize(axiom1));
		firstIterationAxioms.addAll(normalizer.normalize(axiom2));

		Set<IntegerAxiom> normalizedAxioms = new HashSet<>();
		firstIterationAxioms.forEach(axiom -> normalizedAxioms.addAll(normalizer.normalize(axiom)));

		Set<IntegerAxiom> expectedAxioms = new HashSet<>();

		Optional<Integer> optClassI0 = factory.getEntityManager().getAuxiliaryNominal(indivI0Id);
		Assert.assertTrue(optClassI0.isPresent());

		Optional<Integer> optClassI1 = factory.getEntityManager().getAuxiliaryNominal(indivI1Id);
		Assert.assertTrue(optClassI1.isPresent());

		int classI0Id = optClassI0.get();
		int classI1Id = optClassI1.get();
		expectedAxioms.add(factory.getNormalizedAxiomFactory().createNominalAxiom(classI0Id, indivI0Id, annotations));
		expectedAxioms.add(factory.getNormalizedAxiomFactory().createGCI0Axiom(classI0Id, classAId, annotations));
		expectedAxioms.add(factory.getNormalizedAxiomFactory().createNominalAxiom(classI1Id, indivI1Id, annotations));
		expectedAxioms.add(factory.getNormalizedAxiomFactory().createGCI0Axiom(classI1Id, classBId, annotations));
		expectedAxioms.add(
				factory.getNormalizedAxiomFactory().createGCI2Axiom(classI0Id, propertyRId, classI1Id, annotations));

		Assert.assertEquals(expectedAxioms, normalizedAxioms);

	}

}
