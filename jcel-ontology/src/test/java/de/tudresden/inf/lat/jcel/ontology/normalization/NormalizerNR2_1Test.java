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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * 
 * @author Julian Mendez
 * 
 */
public class NormalizerNR2_1Test {

	/**
	 * r<sub>1</sub> \u2218 r<sub>2</sub> \u2218 r<sub>3</sub> \u2218 r
	 * <sub>4</sub> \u2291 s \u219D r<sub>1</sub> \u2218 r<sub>2</sub> \u2291 u
	 * <sub>1</sub>, u<sub>1</sub> \u2218 r<sub>3</sub> \u2291 u<sub>2</sub>, u
	 * <sub>2</sub> \u2218 r<sub>4</sub> \u2291 s
	 */
	@Test
	public void testRule() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR2_1 normalizer = new NormalizerNR2_1(factory);

		IntegerObjectProperty r1 = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r1", false));

		IntegerObjectProperty r2 = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r2", false));

		IntegerObjectProperty r3 = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r3", false));

		IntegerObjectProperty r4 = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r4", false));

		IntegerObjectProperty s = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "s", false));

		IntegerSubPropertyChainOfAxiom originalAxiom = null;
		{
			List<IntegerObjectPropertyExpression> list = new ArrayList<>();
			list.add(r1);
			list.add(r2);
			list.add(r3);
			list.add(r4);
			originalAxiom = factory.getComplexAxiomFactory().createSubPropertyChainOfAxiom(list, s, annotations);
		}
		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(originalAxiom);

		IntegerObjectProperty u1 = null;
		IntegerObjectProperty u2 = null;

		for (IntegerAxiom normAxiom : normalizedAxioms) {
			Assert.assertTrue(normAxiom instanceof RI3Axiom);
			if (normAxiom instanceof RI3Axiom) {
				RI3Axiom axiom = (RI3Axiom) normAxiom;

				if (axiom.getSuperProperty() == s.getId() && axiom.getRightSubProperty() == r4.getId()) {
					u2 = factory.getDataTypeFactory().createObjectProperty(axiom.getLeftSubProperty());
				}

				if (axiom.getLeftSubProperty() == r1.getId() && axiom.getRightSubProperty() == r2.getId()) {
					u1 = factory.getDataTypeFactory().createObjectProperty(axiom.getSuperProperty());
				}
			}

		}

		Set<IntegerAxiom> expectedAxioms = new HashSet<>();
		expectedAxioms.add(
				factory.getNormalizedAxiomFactory().createRI3Axiom(r1.getId(), r2.getId(), u1.getId(), annotations));
		expectedAxioms.add(
				factory.getNormalizedAxiomFactory().createRI3Axiom(u1.getId(), r3.getId(), u2.getId(), annotations));
		expectedAxioms.add(
				factory.getNormalizedAxiomFactory().createRI3Axiom(u2.getId(), r4.getId(), s.getId(), annotations));

		Assert.assertEquals(expectedAxioms, normalizedAxioms);
	}

}
