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
import java.util.Objects;
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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * @author Julian Mendez
 * 
 */
public class NormalizerNR2_3Test {

	/**
	 * &exist; r <i>.</i> &exist; s<sub>1</sub> <i>.</i> C<sub>1</sub> \u2291
	 * &exist; s<sub>2</sub> <i>.</i> C<sub>2</sub> \u219D &exist; s<sub>1</sub>
	 * <i>.</i> C<sub>1</sub> \u2291 A, &exist; r <i>.</i> A \u2291 &exist; s
	 * <sub>2</sub> <i>.</i> C<sub>2</sub>
	 */
	@Test
	public void testRule() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR2_3 normalizer = new NormalizerNR2_3(factory);

		IntegerObjectProperty r = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r", false));

		IntegerClass c1 = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "C1", false));

		IntegerObjectProperty s1 = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "s1", false));

		IntegerClass c2 = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "C2", false));

		IntegerObjectProperty s2 = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "s2", false));

		IntegerClassExpression cPrime = factory.getDataTypeFactory().createObjectSomeValuesFrom(s1, c1);
		IntegerClassExpression c = factory.getDataTypeFactory().createObjectSomeValuesFrom(r, cPrime);
		IntegerClassExpression d = factory.getDataTypeFactory().createObjectSomeValuesFrom(s2, c2);
		IntegerSubClassOfAxiom axiom = factory.getComplexAxiomFactory().createSubClassOfAxiom(c, d, annotations);

		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(axiom);

		IntegerClassExpression a = null;
		for (IntegerAxiom normAxiom : normalizedAxioms) {
			if (normAxiom instanceof IntegerSubClassOfAxiom) {
				IntegerSubClassOfAxiom currentAxiom = (IntegerSubClassOfAxiom) normAxiom;
				IntegerClassExpression leftExpr = currentAxiom.getSubClass();
				if (leftExpr instanceof IntegerObjectSomeValuesFrom) {
					IntegerObjectSomeValuesFrom expr = (IntegerObjectSomeValuesFrom) leftExpr;
					if (expr.getProperty().equals(r)) {
						Assert.assertTrue(Objects.isNull(a));
						a = expr.getFiller();
					}
				}
			}
		}

		Assert.assertTrue(Objects.nonNull(a));
		Assert.assertTrue(a.isLiteral());

		Set<IntegerAxiom> expectedAxioms = new HashSet<>();
		expectedAxioms.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(cPrime, a, annotations));

		IntegerClassExpression newExpr = factory.getDataTypeFactory().createObjectSomeValuesFrom(r, a);
		expectedAxioms.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(newExpr, d, annotations));

		Assert.assertEquals(expectedAxioms, normalizedAxioms);
	}

}
