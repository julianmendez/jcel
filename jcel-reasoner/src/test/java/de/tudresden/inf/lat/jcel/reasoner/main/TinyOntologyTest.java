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
 * Set of tests using tiny ontologies.
 * 
 * @see RuleBasedReasoner
 * 
 * @author Julian Mendez
 */
public class TinyOntologyTest {

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public TinyOntologyTest() {
	}

	private IntegerClass createNewClass(IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory()
				.createClass(factory.getEntityManager().createNamedEntity(IntegerEntityType.CLASS, name, false));
	}

	private IntegerObjectProperty createNewObjectProperty(IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, name, false));
	}

	private Set<IntegerClass> flatten(Set<Set<IntegerClass>> originalSet) {
		Set<IntegerClass> ret = new TreeSet<>();
		originalSet.forEach(set -> ret.addAll(set));
		return ret;
	}

	/**
	 * <ol>
	 * <li>A \u2291 B,</li>
	 * <li>B \u2291 C</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 C</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology0() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a, b, annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b, c, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> superClassesOfA = flatten(reasoner.getSuperClasses(a, false));
		Assert.assertTrue(superClassesOfA.contains(c));

		Set<IntegerClass> subClassesOfC = flatten(reasoner.getSubClasses(c, false));
		Assert.assertTrue(subClassesOfC.contains(a));

		verifyBottomAndTop(factory, reasoner);
	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; r <i>.</i> A ,</li>
	 * <li>A \u2291 B ,</li>
	 * <li>&exist; r <i>.</i> B \u2291 C</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 C</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology1() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, a), annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a, b, annotations));

		// 3
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b), c, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> superClassesOfA = flatten(reasoner.getSuperClasses(a, false));
		Assert.assertTrue(superClassesOfA.contains(c));

		Set<IntegerClass> subClassesOfC = flatten(reasoner.getSubClasses(c, false));
		Assert.assertTrue(subClassesOfC.contains(a));

		verifyBottomAndTop(factory, reasoner);
	}

	/**
	 * <ol>
	 * <li>A \u2291 B ,</li>
	 * <li>B \u2291 A ,</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology2() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a, b, annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b, a, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivalentsOfA = reasoner.getEquivalentClasses(a);
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<IntegerClass> equivalentsOfB = reasoner.getEquivalentClasses(b);
		Assert.assertTrue(equivalentsOfB.contains(a));

		verifyBottomAndTop(factory, reasoner);
	}

	/**
	 * <ol>
	 * <li>\u22A4 \u2291 A ,</li>
	 * <li>A \u2291 B</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * <li>B &equiv; \u22A4</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology3() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(factory.getDataTypeFactory().getTopClass(),
				a, annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a, b, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivalentsOfA = reasoner.getEquivalentClasses(a);
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<IntegerClass> equivalentsOfB = reasoner.getEquivalentClasses(b);
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(factory.getDataTypeFactory().getTopClass()));

		Set<IntegerClass> equivalentsOfTop = reasoner.getEquivalentClasses(factory.getDataTypeFactory().getTopClass());
		Assert.assertTrue(equivalentsOfTop.contains(b));

		verifyBottomAndTop(factory, reasoner);
	}

	/**
	 * <ol>
	 * <li>A \u2291 &perp; ,</li>
	 * <li>B \u2291 A</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * <li>B &equiv; &perp;</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology4() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().getBottomClass(), annotations));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b, a, annotations));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivalentsOfA = reasoner.getEquivalentClasses(a);
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<IntegerClass> equivalentsOfB = reasoner.getEquivalentClasses(b);
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(factory.getDataTypeFactory().getBottomClass()));

		Set<IntegerClass> equivalentsOfBottom = reasoner
				.getEquivalentClasses(factory.getDataTypeFactory().getBottomClass());
		Assert.assertTrue(equivalentsOfBottom.contains(b));

		verifyBottomAndTop(factory, reasoner);
	}

	/**
	 * <ol>
	 * <li>C &equiv; A<sub>1</sub> \u2293 A<sub>2</sub> \u2293 A<sub>3</sub>,
	 * </li>
	 * <li>D &equiv; A<sub>2</sub> \u2293 A<sub>3</sub> \u2293 A<sub>4</sub>,
	 * </li>
	 * <li>A<sub>1</sub> &equiv; \u22A4</li>
	 * <li>A<sub>4</sub> &equiv; \u22A4</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>C &equiv; D</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology5() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a1 = createNewClass(factory, "A1");
		IntegerClass a2 = createNewClass(factory, "A2");
		IntegerClass a3 = createNewClass(factory, "A3");
		IntegerClass a4 = createNewClass(factory, "A4");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");

		{
			Set<IntegerClassExpression> conjunction = new HashSet<>();
			conjunction.add(a1);
			conjunction.add(a2);
			conjunction.add(a3);
			IntegerClassExpression defOfC = factory.getDataTypeFactory().createObjectIntersectionOf(conjunction);
			Set<IntegerClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(c);
			equivClasses.add(defOfC);

			// 1
			ontology.add(factory.getComplexAxiomFactory().createEquivalentClassesAxiom(equivClasses, annotations));
		}

		{
			Set<IntegerClassExpression> conjunction = new HashSet<>();
			conjunction.add(a2);
			conjunction.add(a3);
			conjunction.add(a4);
			IntegerClassExpression defOfD = factory.getDataTypeFactory().createObjectIntersectionOf(conjunction);
			Set<IntegerClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(d);
			equivClasses.add(defOfD);

			// 2
			ontology.add(factory.getComplexAxiomFactory().createEquivalentClassesAxiom(equivClasses, annotations));
		}

		{

			Set<IntegerClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(a1);
			equivClasses.add(factory.getDataTypeFactory().getTopClass());

			// 3
			ontology.add(factory.getComplexAxiomFactory().createEquivalentClassesAxiom(equivClasses, annotations));
		}

		{

			Set<IntegerClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(a4);
			equivClasses.add(factory.getDataTypeFactory().getTopClass());

			// 4
			ontology.add(factory.getComplexAxiomFactory().createEquivalentClassesAxiom(equivClasses, annotations));
		}

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivToC = reasoner.getEquivalentClasses(c);
		Assert.assertTrue(equivToC.contains(d));

		Set<IntegerClass> equivToD = reasoner.getEquivalentClasses(d);
		Assert.assertTrue(equivToD.contains(c));

		verifyBottomAndTop(factory, reasoner);
	}

	private void verifyBottomAndTop(IntegerOntologyObjectFactory factory, IntegerReasoner reasoner) {

		IntegerClass top = factory.getDataTypeFactory().getTopClass();
		IntegerClass bottom = factory.getDataTypeFactory().getBottomClass();

		Assert.assertTrue(reasoner.getSubClasses(bottom, true).isEmpty());
		Assert.assertTrue(reasoner.getSubClasses(bottom, false).isEmpty());
		Assert.assertTrue(reasoner.getSuperClasses(top, true).isEmpty());
		Assert.assertTrue(reasoner.getSuperClasses(top, false).isEmpty());
	}

}
