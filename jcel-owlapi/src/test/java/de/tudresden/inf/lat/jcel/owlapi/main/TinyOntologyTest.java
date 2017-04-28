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

package de.tudresden.inf.lat.jcel.owlapi.main;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;

/**
 * Set of tests using tiny ontologies.
 * 
 * @author Julian Mendez
 */
public class TinyOntologyTest {

	public static final String url = "https://lat.inf.tu-dresden.de/jcel/";

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public TinyOntologyTest() {
	}

	private OWLClass createNewClass(OWLDataFactory factory, String name) {
		return factory.getOWLClass(IRI.create(url + name));
	}

	private OWLObjectProperty createNewObjectProperty(OWLDataFactory factory, String name) {
		return factory.getOWLObjectProperty(IRI.create(url + name));
	}

	private Set<OWLClass> flatten(NodeSet<OWLClass> originalSet) {
		Set<OWLClass> ret = new TreeSet<>();
		originalSet.getNodes().forEach(set -> ret.addAll(set.getEntities()));
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
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology0() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");
		OWLClass c = createNewClass(factory, "C");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, c));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> superClassesOfA = flatten(reasoner.getSuperClasses(a, false));
		Assert.assertTrue(superClassesOfA.contains(c));

		Set<OWLClass> subClassesOfC = flatten(reasoner.getSubClasses(c, false));
		Assert.assertTrue(subClassesOfC.contains(a));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology1() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");
		OWLClass c = createNewClass(factory, "C");
		OWLObjectProperty r = createNewObjectProperty(factory, "r");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, factory.getOWLObjectSomeValuesFrom(r, a)));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		// 3
		axiomSet.add(factory.getOWLSubClassOfAxiom(factory.getOWLObjectSomeValuesFrom(r, b), c));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> superClassesOfA = flatten(reasoner.getSuperClasses(a, false));
		Assert.assertTrue(superClassesOfA.contains(c));

		Set<OWLClass> subClassesOfC = flatten(reasoner.getSubClasses(c, false));
		Assert.assertTrue(subClassesOfC.contains(a));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology2() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, a));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a).getEntities();
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b).getEntities();
		Assert.assertTrue(equivalentsOfB.contains(a));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology3() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(factory.getOWLThing(), a));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a).getEntities();
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b).getEntities();
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(factory.getOWLThing()));

		Set<OWLClass> equivalentsOfTop = reasoner.getEquivalentClasses(factory.getOWLThing()).getEntities();
		Assert.assertTrue(equivalentsOfTop.contains(b));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology4() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, factory.getOWLNothing()));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, a));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a).getEntities();
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b).getEntities();
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(factory.getOWLNothing()));

		Set<OWLClass> equivalentsOfBottom = reasoner.getEquivalentClasses(factory.getOWLNothing()).getEntities();
		Assert.assertTrue(equivalentsOfBottom.contains(b));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology5() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a1 = createNewClass(factory, "A1");
		OWLClass a2 = createNewClass(factory, "A2");
		OWLClass a3 = createNewClass(factory, "A3");
		OWLClass a4 = createNewClass(factory, "A4");
		OWLClass c = createNewClass(factory, "C");
		OWLClass d = createNewClass(factory, "D");

		{
			Set<OWLClassExpression> conjunction = new HashSet<>();
			conjunction.add(a1);
			conjunction.add(a2);
			conjunction.add(a3);
			OWLClassExpression defOfC = factory.getOWLObjectIntersectionOf(conjunction);
			Set<OWLClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(c);
			equivClasses.add(defOfC);

			// 1
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		{
			Set<OWLClassExpression> conjunction = new HashSet<>();
			conjunction.add(a2);
			conjunction.add(a3);
			conjunction.add(a4);
			OWLClassExpression defOfD = factory.getOWLObjectIntersectionOf(conjunction);
			Set<OWLClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(d);
			equivClasses.add(defOfD);

			// 2
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		{

			Set<OWLClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(a1);
			equivClasses.add(factory.getOWLThing());

			// 3
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		{

			Set<OWLClassExpression> equivClasses = new HashSet<>();
			equivClasses.add(a4);
			equivClasses.add(factory.getOWLThing());

			// 4
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivToC = reasoner.getEquivalentClasses(c).getEntities();
		Assert.assertTrue(equivToC.contains(d));

		Set<OWLClass> equivToD = reasoner.getEquivalentClasses(d).getEntities();
		Assert.assertTrue(equivToD.contains(c));

		verifyBottomAndTop(reasoner);

	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; r <i>.</i> B</li>
	 * <li>B \u2291 &exist; s <i>.</i> &perp;</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A &equiv; &perp;</li>
	 * <li>B &equiv; &perp;</li>
	 * </ul>
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology6() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");
		OWLObjectProperty r = createNewObjectProperty(factory, "r");
		OWLObjectProperty s = createNewObjectProperty(factory, "s");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, factory.getOWLObjectSomeValuesFrom(r, b)));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, factory.getOWLObjectSomeValuesFrom(s, factory.getOWLNothing())));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a).getEntities();
		Assert.assertTrue(equivalentsOfA.contains(b));
		Assert.assertTrue(equivalentsOfA.contains(factory.getOWLNothing()));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b).getEntities();
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(factory.getOWLNothing()));

		Set<OWLClass> equivalentsOfBottom = reasoner.getEquivalentClasses(factory.getOWLNothing()).getEntities();
		Assert.assertTrue(equivalentsOfBottom.contains(a));
		Assert.assertTrue(equivalentsOfBottom.contains(b));

		verifyBottomAndTop(reasoner);

	}

	/**
	 * <ol>
	 * <li>A \u2291 &exist; r <i>.</i> B</li>
	 * <li>B \u2291 &exist; r <i>.</i> C</li>
	 * <li>C \u2291 &exist; s <i>.</i> D</li>
	 * <li>D \u2291 &exist; s <i>.</i> E</li>
	 * <li>E \u2291 &exist; s <i>.</i> &perp;</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A &equiv; &perp;</li>
	 * <li>B &equiv; &perp;</li>
	 * <li>C &equiv; &perp;</li>
	 * <li>D &equiv; &perp;</li>
	 * <li>E &equiv; &perp;</li>
	 * </ul>
	 * 
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology7() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");
		OWLClass c = createNewClass(factory, "C");
		OWLClass d = createNewClass(factory, "D");
		OWLClass e = createNewClass(factory, "E");
		OWLObjectProperty r = createNewObjectProperty(factory, "r");
		OWLObjectProperty s = createNewObjectProperty(factory, "s");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, factory.getOWLObjectSomeValuesFrom(r, b)));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, factory.getOWLObjectSomeValuesFrom(r, c)));

		// 3
		axiomSet.add(factory.getOWLSubClassOfAxiom(c, factory.getOWLObjectSomeValuesFrom(s, d)));

		// 4
		axiomSet.add(factory.getOWLSubClassOfAxiom(d, factory.getOWLObjectSomeValuesFrom(s, e)));

		// 5
		axiomSet.add(factory.getOWLSubClassOfAxiom(e, factory.getOWLObjectSomeValuesFrom(s, factory.getOWLNothing())));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> elementsToTest = new TreeSet<>();
		elementsToTest.add(a);
		elementsToTest.add(b);
		elementsToTest.add(c);
		elementsToTest.add(d);
		elementsToTest.add(e);
		elementsToTest.add(factory.getOWLNothing());

		elementsToTest.forEach(element -> {
			Set<OWLClass> equivalents = reasoner.getEquivalentClasses(element).getEntities();
			Assert.assertTrue(equivalents.contains(a));
			Assert.assertTrue(equivalents.contains(b));
			Assert.assertTrue(equivalents.contains(c));
			Assert.assertTrue(equivalents.contains(d));
			Assert.assertTrue(equivalents.contains(e));
			Assert.assertTrue(equivalents.contains(factory.getOWLNothing()));
		});

		verifyBottomAndTop(reasoner);

	}

	/**
	 * @throws OWLOntologyCreationException
	 *             if something goes wrong with the ontology creation
	 */
	@Test
	public void testTinyOntology8() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");
		OWLClass ab = createNewClass(factory, "AB");

		Set<OWLClass> aAndBSet = new HashSet<>();
		aAndBSet.add(a);
		aAndBSet.add(b);
		OWLClassExpression aAndB = factory.getOWLObjectIntersectionOf(aAndBSet);
		axiomSet.add(factory.getOWLEquivalentClassesAxiom(ab, aAndB));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		Set<OWLClass> expectedSet = new HashSet<>();
		expectedSet.add(ab);
		Node<OWLClass> expected = new OWLClassNode(expectedSet);
		Assert.assertEquals(expected, reasoner.getEquivalentClasses(ab));
		Assert.assertEquals(expected, reasoner.getEquivalentClasses(aAndB));
	}

	private void verifyBottomAndTop(OWLReasoner reasoner) {
		OWLClass top = reasoner.getRootOntology().getOWLOntologyManager().getOWLDataFactory().getOWLThing();
		OWLClass bottom = reasoner.getRootOntology().getOWLOntologyManager().getOWLDataFactory().getOWLNothing();

		Assert.assertTrue(reasoner.getSubClasses(bottom, true).isEmpty());
		Assert.assertTrue(reasoner.getSubClasses(bottom, false).isEmpty());
		Assert.assertTrue(reasoner.getSuperClasses(top, true).isEmpty());
		Assert.assertTrue(reasoner.getSuperClasses(top, false).isEmpty());
	}

}
