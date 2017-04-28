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

package de.tudresden.inf.lat.jcel.reasoner.main;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Set of tests for the rule based reasoner.
 * 
 * @see RuleBasedReasoner
 * 
 * @author Julian Mendez
 */
public class RuleBasedReasonerTest {

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public RuleBasedReasonerTest() {
	}

	private void assertUniqueDirectSubClass(IntegerReasoner reasoner, IntegerClass subClass, IntegerClass superClass) {
		Set<Set<IntegerClass>> subClassesOfSecond = reasoner.getSubClasses(superClass, true);
		Assert.assertEquals(subClassesOfSecond.size(), 1);
		Set<IntegerClass> subElemSet = subClassesOfSecond.iterator().next();
		Assert.assertEquals(subElemSet.size(), 1);
		IntegerClass x1 = subElemSet.iterator().next();
		Assert.assertEquals(subClass, x1);

		Set<Set<IntegerClass>> superClassesOfFirst = reasoner.getSuperClasses(subClass, true);
		Assert.assertEquals(superClassesOfFirst.size(), 1);
		Set<IntegerClass> superElemSet = superClassesOfFirst.iterator().next();
		Assert.assertEquals(superElemSet.size(), 1);
		IntegerClass x2 = superElemSet.iterator().next();
		Assert.assertEquals(superClass, x2);
	}

	private IntegerClass createNewClass(IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, name, false));
	}

	private IntegerObjectProperty createNewObjectProperty(IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, name, false));
	}

	/**
	 * <ol>
	 * <li>L \u2291 &exist; s <i>.</i> \u22A4 ,</li>
	 * <li>&exist; s <sup>-</sup> <i>.</i> L \u2291 K ,</li>
	 * <li>&exist; s <i>.</i> K \u2291 E ,</li>
	 * <li>E \u2291 &exist; t <i>.</i> &exist; t <i>.</i> D ,</li>
	 * <li>&exist; t <i>.</i> D \u2291 C ,</li>
	 * <li>C \u2291 &exist; r<sub>1</sub> <i>.</i> G ,</li>
	 * <li>C \u2291 &exist; r<sub>2</sub> <i>.</i> H ,</li>
	 * <li>&exist; r <i>.</i> (G \u2293 H) \u2291 J ,</li>
	 * <li>r<sub>1</sub> \u2291 r ,</li>
	 * <li>r<sub>2</sub> \u2291 r ,</li>
	 * <li><i>f</i>(r) ,</li>
	 * <li>t \u2218 t \u2291 t</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>L \u2291 E ,</li>
	 * <li>E \u2291 C ,</li>
	 * <li>C \u2291 J</li>
	 * </ul>
	 */
	@Test
	public void testOntology0() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerClass e = createNewClass(factory, "E");
		IntegerClass g = createNewClass(factory, "G");
		IntegerClass h = createNewClass(factory, "H");
		IntegerClass j = createNewClass(factory, "J");
		IntegerClass k = createNewClass(factory, "K");
		IntegerClass l = createNewClass(factory, "L");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty r1 = createNewObjectProperty(factory, "r1");
		IntegerObjectProperty r2 = createNewObjectProperty(factory, "r2");
		IntegerObjectProperty s = createNewObjectProperty(factory, "s");
		IntegerObjectProperty sMinus = createNewObjectProperty(factory, "s-");
		IntegerObjectProperty t = createNewObjectProperty(factory, "t");
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(sMinus, s, annotations));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(l,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s, factory.getDataTypeFactory().getTopClass()),
				annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(sMinus, l), k, annotations));

		// 3
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(s, k), e, annotations));

		// 4
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(e, factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(t, factory.getDataTypeFactory().createObjectSomeValuesFrom(t, d)),
				annotations));

		// 5
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(t, d), c, annotations));

		// 6
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(c,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r1, g), annotations));

		// 7
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(c,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r2, h), annotations));

		// 8
		Set<IntegerClassExpression> g_h = new HashSet<>();
		g_h.add(g);
		g_h.add(h);
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(r, factory.getDataTypeFactory().createObjectIntersectionOf(g_h)), j,
				annotations));

		// 9
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r1, r, annotations));

		// 10
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r2, r, annotations));

		// 11
		ontology.add(factory.getComplexAxiomFactory().createFunctionalObjectPropertyAxiom(r, annotations));

		// 12
		ontology.add(factory.getComplexAxiomFactory().createTransitiveObjectPropertyAxiom(t, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, l, e);
		assertUniqueDirectSubClass(reasoner, e, c);
		assertUniqueDirectSubClass(reasoner, c, j);
	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; r <i>.</i> B ,</li>
	 * <li>&exist; r<sup>-</sup> <i>.</i> A \u2291 C ,</li>
	 * <li>&exist; r <i>.</i> C \u2291 D</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 D</li>
	 * </ul>
	 */
	@Test
	public void testOntology1() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(rMinus, r, annotations));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b), annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus, a), c, annotations));

		// 3
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c), d, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, d);
	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; r <i>.</i> B ,</li>
	 * <li>D \u2291 &exist; r<sup>-</sup> <i>.</i> E ,</li>
	 * <li>&exist; r<sup>-</sup> <i>.</i> A \u2291 D ,</li>
	 * <li>&exist; r <i>.</i> C \u2291 D ,</li>
	 * <li><i>f</i>(r<sup>-</sup>) ,</li>
	 * <li><i>f</i>(s) ,</li>
	 * <li>t \u2218 t \u2291 t</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 E</li>
	 * </ul>
	 */
	@Test
	public void testOntology2() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerClass e = createNewClass(factory, "E");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");
		IntegerObjectProperty s = createNewObjectProperty(factory, "s");
		IntegerObjectProperty t = createNewObjectProperty(factory, "t");
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(rMinus, r, annotations));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b), annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(d,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus, e), annotations));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus, a), d, annotations));

		// 4
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c), d, annotations));

		// 5
		ontology.add(factory.getComplexAxiomFactory().createFunctionalObjectPropertyAxiom(rMinus, annotations));

		// 6
		ontology.add(factory.getComplexAxiomFactory().createFunctionalObjectPropertyAxiom(s, annotations));

		// 7
		ontology.add(factory.getComplexAxiomFactory().createTransitiveObjectPropertyAxiom(t, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, e);
	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; r<sub>1</sub> <i>.</i> B ,</li>
	 * <li>B \u2291 &exist; r<sub>2</sub> <i>.</i> C ,</li>
	 * <li>&exist; s <i>.</i> D \u2291 E ,</li>
	 * <li>&exist; s<sup>-</sup> <i>.</i> A \u2291 D ,</li>
	 * <li>r \u2291 s ,</li>
	 * <li>r<sub>1</sub> \u2291 r ,</li>
	 * <li>r<sub>2</sub> \u2291 r ,</li>
	 * <li>r \u2218 r \u2291 r</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 E</li>
	 * </ul>
	 */
	@Test
	public void testOntology3() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerClass e = createNewClass(factory, "E");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty r1 = createNewObjectProperty(factory, "r1");
		IntegerObjectProperty r2 = createNewObjectProperty(factory, "r2");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");
		IntegerObjectProperty s = createNewObjectProperty(factory, "s");
		IntegerObjectProperty sMinus = createNewObjectProperty(factory, "s-");
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(rMinus, r, annotations));
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(sMinus, s, annotations));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r1, b), annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r2, c), annotations));

		// 3
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(s, d), e, annotations));

		// 4
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(sMinus, a), d, annotations));

		// 5
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r, s, annotations));

		// 6
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r1, r, annotations));

		// 7
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r2, r, annotations));

		// 8
		ontology.add(factory.getComplexAxiomFactory().createTransitiveObjectPropertyAxiom(r, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, e);
	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; s<sub>1</sub> <i>.</i> B ,</li>
	 * <li>B \u2291 &exist; s<sub>2</sub><sup>-</sup> <i>.</i> D ,</li>
	 * <li>D \u2291 &exist; r<sub>1</sub> <i>.</i> B<sub>1</sub> ,</li>
	 * <li>D \u2291 &exist; r<sub>2</sub> <i>.</i> B<sub>2</sub> ,</li>
	 * <li>&exist; r <i>.</i> C \u2291 E ,</li>
	 * <li>B<sub>1</sub> \u2293 B<sub>2</sub> \u2291 C ,</li>
	 * <li>r<sub>1</sub> \u2291 r ,</li>
	 * <li>r<sub>2</sub> \u2291 r ,</li>
	 * <li>s<sub>1</sub> \u2291 s ,</li>
	 * <li>s<sub>1</sub> \u2291 t ,</li>
	 * <li>s<sub>2</sub> \u2291 s ,</li>
	 * <li>s<sub>2</sub><sup>-</sup> \u2291 t ,</li>
	 * <li><i>f</i>(r) ,</li>
	 * <li><i>f</i>(s<sup>-</sup>) ,</li>
	 * <li>t \u2218 t \u2291 t</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 D ,</li>
	 * <li>D \u2291 E</li>
	 * </ul>
	 */
	@Test
	public void testOntology4() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass b1 = createNewClass(factory, "B1");
		IntegerClass b2 = createNewClass(factory, "B2");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerClass e = createNewClass(factory, "E");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty r1 = createNewObjectProperty(factory, "r1");
		IntegerObjectProperty r2 = createNewObjectProperty(factory, "r2");
		IntegerObjectProperty s = createNewObjectProperty(factory, "s");
		IntegerObjectProperty s1 = createNewObjectProperty(factory, "s1");
		IntegerObjectProperty s2 = createNewObjectProperty(factory, "s2");
		IntegerObjectProperty t = createNewObjectProperty(factory, "t");
		IntegerObjectProperty sMinus = createNewObjectProperty(factory, "s-");
		IntegerObjectProperty s2Minus = createNewObjectProperty(factory, "s2-");
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(sMinus, s, annotations));
		ontology.add(factory.getComplexAxiomFactory().createInverseObjectPropertiesAxiom(s2Minus, s2, annotations));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s1, b), annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s2Minus, d), annotations));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(d,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r1, b1), annotations));

		// 4
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(d,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r2, b2), annotations));

		// 5
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c), e, annotations));

		// 6
		Set<IntegerClassExpression> b1_b2 = new HashSet<>();
		b1_b2.add(b1);
		b1_b2.add(b2);
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectIntersectionOf(b1_b2), c, annotations));

		// 7
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r1, r, annotations));

		// 8
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(r2, r, annotations));

		// 9
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(s1, s, annotations));

		// 10
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(s1, t, annotations));

		// 11
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(s2, s, annotations));

		// 12
		ontology.add(factory.getComplexAxiomFactory().createSubObjectPropertyOfAxiom(s2Minus, t, annotations));

		// 13
		ontology.add(factory.getComplexAxiomFactory().createFunctionalObjectPropertyAxiom(r, annotations));

		// 14
		ontology.add(factory.getComplexAxiomFactory().createFunctionalObjectPropertyAxiom(sMinus, annotations));

		// 15
		ontology.add(factory.getComplexAxiomFactory().createTransitiveObjectPropertyAxiom(t, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, d);
		assertUniqueDirectSubClass(reasoner, d, e);
	}

}
