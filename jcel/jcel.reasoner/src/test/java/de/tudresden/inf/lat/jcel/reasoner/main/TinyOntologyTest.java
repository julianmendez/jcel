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

package de.tudresden.inf.lat.jcel.reasoner.main;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Set of tests using tiny ontologies.
 * 
 * @see RuleBasedReasoner
 * 
 * @author Julian Mendez
 */
public class TinyOntologyTest extends TestCase {

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public TinyOntologyTest() {
	}

	private IntegerClass createNewClass(IntegerOntologyObjectFactory factory,
			String name) {
		return factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, name, false));
	}

	private IntegerObjectProperty createNewObjectProperty(
			IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory().createObjectProperty(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.OBJECT_PROPERTY, name, false));
	}

	private Set<IntegerClass> flatten(Set<Set<IntegerClass>> originalSet) {
		Set<IntegerClass> ret = new TreeSet<IntegerClass>();
		for (Set<IntegerClass> set : originalSet) {
			ret.addAll(set);
		}
		return ret;
	}

	/**
	 * <ol>
	 * <li>A &#x2291; B,</li>
	 * <li>B &#x2291; C</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &#x2291; C</li>
	 * </ul>
	 */
	public void testTinyOntology0() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				b));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b,
				c));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> superClassesOfA = flatten(reasoner.getSuperClasses(a,
				false));
		assertTrue(superClassesOfA.contains(c));

		Set<IntegerClass> subClassesOfC = flatten(reasoner.getSubClasses(c,
				false));
		assertTrue(subClassesOfC.contains(a));
	}

	/**
	 * <ol>
	 * <li>A &#x2291; &exist; r <i>.</i> A ,</li>
	 * <li>A &#x2291; B ,</li>
	 * <li>&exist; r <i>.</i> B &#x2291; C</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &#x2291; C</li>
	 * </ul>
	 */
	public void testTinyOntology1() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, a)));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				b));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b),
				c));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> superClassesOfA = flatten(reasoner.getSuperClasses(a,
				false));
		assertTrue(superClassesOfA.contains(c));

		Set<IntegerClass> subClassesOfC = flatten(reasoner.getSubClasses(c,
				false));
		assertTrue(subClassesOfC.contains(a));
	}

	/**
	 * <ol>
	 * <li>A &#x2291; B ,</li>
	 * <li>B &#x2291; A ,</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * </ul>
	 */
	public void testTinyOntology2() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				b));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b,
				a));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivalentsOfA = reasoner.getEquivalentClasses(a);
		assertTrue(equivalentsOfA.contains(b));

		Set<IntegerClass> equivalentsOfB = reasoner.getEquivalentClasses(b);
		assertTrue(equivalentsOfB.contains(a));
	}

	/**
	 * <ol>
	 * <li>&#x22a4; &#x2291; A ,</li>
	 * <li>A &#x2291; B</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * <li>B &equiv; &#x22a4;</li>
	 * </ul>
	 */
	public void testTinyOntology3() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().getTopClass(), a));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				b));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivalentsOfA = reasoner.getEquivalentClasses(a);
		assertTrue(equivalentsOfA.contains(b));

		Set<IntegerClass> equivalentsOfB = reasoner.getEquivalentClasses(b);
		assertTrue(equivalentsOfB.contains(a));
		assertTrue(equivalentsOfB.contains(factory.getDataTypeFactory()
				.getTopClass()));

		Set<IntegerClass> equivalentsOfTop = reasoner
				.getEquivalentClasses(factory.getDataTypeFactory()
						.getTopClass());
		assertTrue(equivalentsOfTop.contains(b));
	}

	/**
	 * <ol>
	 * <li>A &#x2291; &#x22a5; ,</li>
	 * <li>B &#x2291; A</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * <li>B &equiv; &#x22a5;</li>
	 * </ul>
	 */
	public void testTinyOntology4() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().getBottomClass()));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b,
				a));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Set<IntegerClass> equivalentsOfA = reasoner.getEquivalentClasses(a);
		assertTrue(equivalentsOfA.contains(b));

		Set<IntegerClass> equivalentsOfB = reasoner.getEquivalentClasses(b);
		assertTrue(equivalentsOfB.contains(a));
		assertTrue(equivalentsOfB.contains(factory.getDataTypeFactory()
				.getBottomClass()));

		Set<IntegerClass> equivalentsOfBottom = reasoner
				.getEquivalentClasses(factory.getDataTypeFactory()
						.getBottomClass());
		assertTrue(equivalentsOfBottom.contains(b));
	}

}
