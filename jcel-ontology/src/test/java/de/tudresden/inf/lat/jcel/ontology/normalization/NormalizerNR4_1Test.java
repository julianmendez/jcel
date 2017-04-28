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
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectOneOf;

/**
 * B \u2291 {a} \u219D B \u2291 A , A &equiv; {a}
 * 
 * @author Julian Mendez
 * 
 */
public class NormalizerNR4_1Test {

	@Test
	public void testRule() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR4_1 normalizer = new NormalizerNR4_1(factory);

		int indivAId = factory.getEntityManager().createNamedEntity(IntegerEntityType.INDIVIDUAL, "a", false);
		int classBId = factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "B", false);

		// IntegerNamedIndividualDeclarationAxiom axiom0 =
		// factory.getComplexAxiomFactory()
		// .createNamedIndividualDeclarationAxiom(a0, annotations);

		IntegerObjectOneOf oneOf = factory.getDataTypeFactory().createObjectOneOf(indivAId);
		IntegerClass classB = factory.getDataTypeFactory().createClass(classBId);

		IntegerSubClassOfAxiom axiom = factory.getComplexAxiomFactory().createSubClassOfAxiom(classB, oneOf,
				annotations);

		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(axiom);

		Set<IntegerAxiom> expectedAxioms = new HashSet<>();

		Optional<Integer> optClassA = factory.getEntityManager().getAuxiliaryNominal(indivAId);
		Assert.assertTrue(optClassA.isPresent());

		int classAId = optClassA.get();
		expectedAxioms.add(factory.getNormalizedAxiomFactory().createNominalAxiom(classAId, indivAId, annotations));
		expectedAxioms.add(factory.getNormalizedAxiomFactory().createGCI0Axiom(classBId, classAId, annotations));
		Assert.assertEquals(expectedAxioms, normalizedAxioms);
	}

}
