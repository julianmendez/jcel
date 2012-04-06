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

package de.tudresden.inf.lat.jcel.core.reasoner;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Set of tests for the rule based reasoner.
 * 
 * @see RuleBasedReasoner
 * 
 * @author Julian Mendez
 */
public class RuleBasedReasonerTest extends TestCase {

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public RuleBasedReasonerTest() {
	}

	private void assertUniqueDirectSubClass(IntegerReasoner reasoner,
			IntegerClass subClass, IntegerClass superClass) {
		Set<Set<IntegerClass>> subClassesOfSecond = reasoner.getSubClasses(
				superClass, true);
		assertEquals(subClassesOfSecond.size(), 1);
		Set<IntegerClass> subElemSet = subClassesOfSecond.iterator().next();
		assertEquals(subElemSet.size(), 1);
		IntegerClass x1 = subElemSet.iterator().next();
		assertEquals(subClass, x1);

		Set<Set<IntegerClass>> superClassesOfFirst = reasoner.getSuperClasses(
				subClass, true);
		assertEquals(superClassesOfFirst.size(), 1);
		Set<IntegerClass> superElemSet = superClassesOfFirst.iterator().next();
		assertEquals(superElemSet.size(), 1);
		IntegerClass x2 = superElemSet.iterator().next();
		assertEquals(superClass, x2);
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

	/**
	 * <ol>
	 * <li>L &#8849; &exist; s <i>.</i> &#8868; ,</li>
	 * <li>&exist; s <sup>-</sup> <i>.</i> L &#8849; K ,</li>
	 * <li>&exist; s <i>.</i> K &#8849; E ,</li>
	 * <li>E &#8849; &exist; t <i>.</i> &exist; t <i>.</i> D ,</li>
	 * <li>&exist; t <i>.</i> D &#8849; C ,</li>
	 * <li>C &#8849; &exist; r<sub>1</sub> <i>.</i> G ,</li>
	 * <li>C &#8849; &exist; r<sub>2</sub> <i>.</i> H ,</li>
	 * <li>&exist; r <i>.</i> (G &#8851; H) &#8849; J ,</li>
	 * <li>r<sub>1</sub> &#8849; r ,</li>
	 * <li>r<sub>2</sub> &#8849; r ,</li>
	 * <li><i>f</i>(r) ,</li>
	 * <li>t &#8728; t &#8849; t</li>
	 * </ol>
	 * &#8872;
	 * <ul>
	 * <li>L &#8849; E ,</li>
	 * <li>E &#8849; C ,</li>
	 * <li>C &#8849; J</li>
	 * </ul>
	 */
	public void testOntology0() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
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
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(sMinus, s));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				l,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s,
						factory.getDataTypeFactory().getTopClass())));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(sMinus,
						l), k));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s, k),
				e));

		// 4
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				e,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(
						t,
						factory.getDataTypeFactory()
								.createObjectSomeValuesFrom(t, d))));

		// 5
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(t, d),
				c));

		// 6
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(c,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r1, g)));

		// 7
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(c,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r2, h)));

		// 8
		Set<IntegerClassExpression> g_h = new HashSet<IntegerClassExpression>();
		g_h.add(g);
		g_h.add(h);
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(
						r,
						factory.getDataTypeFactory()
								.createObjectIntersectionOf(g_h)), j));

		// 9
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r1, r));

		// 10
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r2, r));

		// 11
		ontology.add(factory.getComplexAxiomFactory()
				.createFunctionalObjectPropertyAxiom(r));

		// 12
		ontology.add(factory.getComplexAxiomFactory()
				.createTransitiveObjectPropertyAxiom(t));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory,
				false);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, l, e);
		assertUniqueDirectSubClass(reasoner, e, c);
		assertUniqueDirectSubClass(reasoner, c, j);
	}

	/**
	 * <ol>
	 * <li>A &#8849; &exist; r <i>.</i> B ,</li>
	 * <li>&exist; r<sup>-</sup> <i>.</i> A &#8849; C ,</li>
	 * <li>&exist; r <i>.</i> C &#8849; D</li>
	 * </ol>
	 * &#8872;
	 * <ul>
	 * <li>A &#8849; D</li>
	 * </ul>
	 */
	public void testOntology1() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(rMinus, r));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b)));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus,
						a), c));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c),
				d));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory,
				false);
		reasoner.classify();

		// test
		{
			Set<Set<IntegerClass>> subClassesOfB = reasoner.getSubClasses(b,
					true);
			assertEquals(subClassesOfB.size(), 1);
			Set<IntegerClass> subElemSet = subClassesOfB.iterator().next();
			assertEquals(subElemSet.size(), 1);
			IntegerClass x = subElemSet.iterator().next();
			assertEquals(factory.getDataTypeFactory().getBottomClass(), x);
		}

		assertUniqueDirectSubClass(reasoner, a, d);
	}

	/**
	 * <ol>
	 * <li>A &#8849; &exist; r <i>.</i> B ,</li>
	 * <li>D &#8849; &exist; r<sup>-</sup> <i>.</i> E ,</li>
	 * <li>&exist; r<sup>-</sup> <i>.</i> A &#8849; D ,</li>
	 * <li>&exist; r <i>.</i> C &#8849; D ,</li>
	 * <li><i>f</i>(r<sup>-</sup>) ,</li>
	 * <li><i>f</i>(s) ,</li>
	 * <li>t &#8728; t &#8849; t</li>
	 * </ol>
	 * &#8872;
	 * <ul>
	 * <li>A &#8849; E</li>
	 * </ul>
	 */
	public void testOntology2() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerClass e = createNewClass(factory, "E");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");
		IntegerObjectProperty s = createNewObjectProperty(factory, "s");
		IntegerObjectProperty t = createNewObjectProperty(factory, "t");
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(rMinus, r));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b)));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				d,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus,
						e)));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus,
						a), d));

		// 4
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c),
				d));

		// 5
		ontology.add(factory.getComplexAxiomFactory()
				.createFunctionalObjectPropertyAxiom(rMinus));

		// 6
		ontology.add(factory.getComplexAxiomFactory()
				.createFunctionalObjectPropertyAxiom(s));

		// 7
		ontology.add(factory.getComplexAxiomFactory()
				.createTransitiveObjectPropertyAxiom(t));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory,
				false);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, e);
	}

	/**
	 * <ol>
	 * <li>A &#8849; &exist; r<sub>1</sub> <i>.</i> B ,</li>
	 * <li>B &#8849; &exist; r<sub>2</sub> <i>.</i> C ,</li>
	 * <li>&exist; s <i>.</i> D &#8849; E ,</li>
	 * <li>&exist; s<sup>-</sup> <i>.</i> A &#8849; D ,</li>
	 * <li>r &#8849; s ,</li>
	 * <li>r<sub>1</sub> &#8849; r ,</li>
	 * <li>r<sub>2</sub> &#8849; r ,</li>
	 * <li>r &#8728; r &#8849; r</li>
	 * </ol>
	 * &#8872;
	 * <ul>
	 * <li>A &#8849; E</li>
	 * </ul>
	 */
	public void testOntology3() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
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
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(rMinus, r));
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(sMinus, s));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r1, b)));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(b,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r2, c)));

		// 3
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s, d),
				e));

		// 4
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(sMinus,
						a), d));

		// 5
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r, s));

		// 6
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r1, r));

		// 7
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r2, r));

		// 8
		ontology.add(factory.getComplexAxiomFactory()
				.createTransitiveObjectPropertyAxiom(r));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory,
				false);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, e);
	}

	/**
	 * <ol>
	 * <li>A &#8849; &exist; s<sub>1</sub> <i>.</i> B ,</li>
	 * <li>B &#8849; &exist; s<sub>2</sub><sup>-</sup> <i>.</i> D ,</li>
	 * <li>D &#8849; &exist; r<sub>1</sub> <i>.</i> B<sub>1</sub> ,</li>
	 * <li>D &#8849; &exist; r<sub>2</sub> <i>.</i> B<sub>2</sub> ,</li>
	 * <li>&exist; r <i>.</i> C &#8849; E ,</li>
	 * <li>B<sub>1</sub> &#8851; B<sub>2</sub> &#8849; C ,</li>
	 * <li>r<sub>1</sub> &#8849; r ,</li>
	 * <li>r<sub>2</sub> &#8849; r ,</li>
	 * <li>s<sub>1</sub> &#8849; s ,</li>
	 * <li>s<sub>1</sub> &#8849; t ,</li>
	 * <li>s<sub>2</sub> &#8849; s ,</li>
	 * <li>s<sub>2</sub><sup>-</sup> &#8849; t ,</li>
	 * <li><i>f</i>(r) ,</li>
	 * <li><i>f</i>(s<sup>-</sup>) ,</li>
	 * <li>t &#8728; t &#8849; t</li>
	 * </ol>
	 * &#8872;
	 * <ul>
	 * <li>A &#8849; D ,</li>
	 * <li>D &#8849; E</li>
	 * </ul>
	 */
	public void testOntology4() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
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
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(sMinus, s));
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(s2Minus, s2));

		// 1
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(s1, b)));

		// 2
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				b,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(
						s2Minus, d)));

		// 3
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(
						d,
						factory.getDataTypeFactory()
								.createObjectSomeValuesFrom(r1, b1)));

		// 4
		ontology.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(
						d,
						factory.getDataTypeFactory()
								.createObjectSomeValuesFrom(r2, b2)));

		// 5
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c),
				e));

		// 6
		Set<IntegerClassExpression> b1_b2 = new HashSet<IntegerClassExpression>();
		b1_b2.add(b1);
		b1_b2.add(b2);
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectIntersectionOf(b1_b2),
				c));

		// 7
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r1, r));

		// 8
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(r2, r));

		// 9
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(s1, s));

		// 10
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(s1, t));

		// 11
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(s2, s));

		// 12
		ontology.add(factory.getComplexAxiomFactory()
				.createSubObjectPropertyOfAxiom(s2Minus, t));

		// 13
		ontology.add(factory.getComplexAxiomFactory()
				.createFunctionalObjectPropertyAxiom(r));

		// 14
		ontology.add(factory.getComplexAxiomFactory()
				.createFunctionalObjectPropertyAxiom(sMinus));

		// 15
		ontology.add(factory.getComplexAxiomFactory()
				.createTransitiveObjectPropertyAxiom(t));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory,
				false);
		reasoner.classify();

		assertUniqueDirectSubClass(reasoner, a, d);
		assertUniqueDirectSubClass(reasoner, d, e);
	}

}
