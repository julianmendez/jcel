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
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * 
 * @author Julian Mendez
 * 
 */
public class NormalizerNR1_5Test {

	/**
	 * C &equiv; D \u219D C \u2291 D, D \u2291 C
	 */
	@Test
	public void testUsingClasses() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR1_5 normalizer = new NormalizerNR1_5(factory);

		IntegerClass c = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "C", false));
		IntegerClass d = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "D", false));

		Set<IntegerClassExpression> set = new HashSet<>();
		set.add(c);
		set.add(d);
		IntegerEquivalentClassesAxiom axiom = factory.getComplexAxiomFactory().createEquivalentClassesAxiom(set,
				annotations);
		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(axiom);

		Set<IntegerAxiom> expectedAxioms = new HashSet<>();
		expectedAxioms.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(c, d, annotations));
		expectedAxioms.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(d, c, annotations));

		Assert.assertEquals(expectedAxioms, normalizedAxioms);
	}

	/**
	 * &exist; r <i>.</i> C<sub>1</sub> &equiv; C<sub>2</sub> \u2293 C
	 * <sub>3</sub> \u219D &exist; r <i>.</i> C<sub>1</sub> \u2291 C<sub>2</sub>
	 * \u2293 C<sub>3</sub>, C<sub>2</sub> \u2293 C<sub>3</sub> \u2291 &exist; r
	 * <i>.</i> C<sub>1</sub>
	 */
	@Test
	public void testUsingClassExpressions() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR1_5 normalizer = new NormalizerNR1_5(factory);

		IntegerObjectProperty r = factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, "r", false));
		IntegerClass c1 = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "C1", false));
		IntegerClass c2 = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "C2", false));
		IntegerClass c3 = factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, "C3", false));

		IntegerClassExpression c = factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c1);
		Set<IntegerClassExpression> parameter = new HashSet<>();
		parameter.add(c2);
		parameter.add(c3);
		IntegerClassExpression d = factory.getDataTypeFactory().createObjectIntersectionOf(parameter);

		Set<IntegerClassExpression> set = new HashSet<>();
		set.add(c);
		set.add(d);
		IntegerEquivalentClassesAxiom axiom = factory.getComplexAxiomFactory().createEquivalentClassesAxiom(set,
				annotations);
		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(axiom);

		Set<IntegerAxiom> expectedAxioms = new HashSet<>();
		expectedAxioms.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(c, d, annotations));
		expectedAxioms.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(d, c, annotations));

		Assert.assertEquals(expectedAxioms, normalizedAxioms);
	}

}
