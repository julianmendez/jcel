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

package de.tudresden.inf.lat.jcel.owlapi.main;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

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

/**
 * Set of tests using tiny ontologies.
 * 
 * @author Julian Mendez
 */
public class TinyOntologyTest extends TestCase {

	public static final String url = "http://lat.inf.tu-dresden.de/jcel/";

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public TinyOntologyTest() {
	}

	private OWLClass createNewClass(OWLDataFactory factory, String name) {
		return factory.getOWLClass(IRI.create(url + name));
	}

	private OWLObjectProperty createNewObjectProperty(OWLDataFactory factory,
			String name) {
		return factory.getOWLObjectProperty(IRI.create(url + name));
	}

	private Set<OWLClass> flatten(NodeSet<OWLClass> originalSet) {
		Set<OWLClass> ret = new TreeSet<OWLClass>();
		for (Node<OWLClass> set : originalSet.getNodes()) {
			ret.addAll(set.getEntities());
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
	 * 
	 * @throws OWLOntologyCreationException
	 */
	public void testTinyOntology0() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
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

		Set<OWLClass> superClassesOfA = flatten(reasoner.getSuperClasses(a,
				false));
		assertTrue(superClassesOfA.contains(c));

		Set<OWLClass> subClassesOfC = flatten(reasoner.getSubClasses(c, false));
		assertTrue(subClassesOfC.contains(a));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 */
	public void testTinyOntology1() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");
		OWLClass c = createNewClass(factory, "C");
		OWLObjectProperty r = createNewObjectProperty(factory, "r");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a,
				factory.getOWLObjectSomeValuesFrom(r, a)));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		// 3
		axiomSet.add(factory.getOWLSubClassOfAxiom(
				factory.getOWLObjectSomeValuesFrom(r, b), c));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> superClassesOfA = flatten(reasoner.getSuperClasses(a,
				false));
		assertTrue(superClassesOfA.contains(c));

		Set<OWLClass> subClassesOfC = flatten(reasoner.getSubClasses(c, false));
		assertTrue(subClassesOfC.contains(a));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 */
	public void testTinyOntology2() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, a));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a)
				.getEntities();
		assertTrue(equivalentsOfA.contains(b));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b)
				.getEntities();
		assertTrue(equivalentsOfB.contains(a));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 */
	public void testTinyOntology3() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(factory.getOWLThing(), a));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, b));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a)
				.getEntities();
		assertTrue(equivalentsOfA.contains(b));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b)
				.getEntities();
		assertTrue(equivalentsOfB.contains(a));
		assertTrue(equivalentsOfB.contains(factory.getOWLThing()));

		Set<OWLClass> equivalentsOfTop = reasoner.getEquivalentClasses(
				factory.getOWLThing()).getEntities();
		assertTrue(equivalentsOfTop.contains(b));

		verifyBottomAndTop(reasoner);

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
	 * 
	 * @throws OWLOntologyCreationException
	 */
	public void testTinyOntology4() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
		OWLClass a = createNewClass(factory, "A");
		OWLClass b = createNewClass(factory, "B");

		// 1
		axiomSet.add(factory.getOWLSubClassOfAxiom(a, factory.getOWLNothing()));

		// 2
		axiomSet.add(factory.getOWLSubClassOfAxiom(b, a));

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivalentsOfA = reasoner.getEquivalentClasses(a)
				.getEntities();
		assertTrue(equivalentsOfA.contains(b));

		Set<OWLClass> equivalentsOfB = reasoner.getEquivalentClasses(b)
				.getEntities();
		assertTrue(equivalentsOfB.contains(a));
		assertTrue(equivalentsOfB.contains(factory.getOWLNothing()));

		Set<OWLClass> equivalentsOfBottom = reasoner.getEquivalentClasses(
				factory.getOWLNothing()).getEntities();
		assertTrue(equivalentsOfBottom.contains(b));

		verifyBottomAndTop(reasoner);

	}

	/**
	 * <ol>
	 * <li>C &equiv; A<sub>1</sub> &#x2293; A<sub>2</sub> &#x2293;
	 * A<sub>3</sub>,</li>
	 * <li>D &equiv; A<sub>2</sub> &#x2293; A<sub>3</sub> &#x2293;
	 * A<sub>4</sub>,</li>
	 * <li>A<sub>1</sub> &equiv; &#x22a4;</li>
	 * <li>A<sub>4</sub> &equiv; &#x22a4;</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>C &equiv; D</li>
	 * </ul>
	 * 
	 * @throws OWLOntologyCreationException
	 */
	public void testTinyOntology5() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
		OWLClass a1 = createNewClass(factory, "A1");
		OWLClass a2 = createNewClass(factory, "A2");
		OWLClass a3 = createNewClass(factory, "A3");
		OWLClass a4 = createNewClass(factory, "A4");
		OWLClass c = createNewClass(factory, "C");
		OWLClass d = createNewClass(factory, "D");

		{
			Set<OWLClassExpression> conjunction = new HashSet<OWLClassExpression>();
			conjunction.add(a1);
			conjunction.add(a2);
			conjunction.add(a3);
			OWLClassExpression defOfC = factory
					.getOWLObjectIntersectionOf(conjunction);
			Set<OWLClassExpression> equivClasses = new HashSet<OWLClassExpression>();
			equivClasses.add(c);
			equivClasses.add(defOfC);

			// 1
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		{
			Set<OWLClassExpression> conjunction = new HashSet<OWLClassExpression>();
			conjunction.add(a2);
			conjunction.add(a3);
			conjunction.add(a4);
			OWLClassExpression defOfD = factory
					.getOWLObjectIntersectionOf(conjunction);
			Set<OWLClassExpression> equivClasses = new HashSet<OWLClassExpression>();
			equivClasses.add(d);
			equivClasses.add(defOfD);

			// 2
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		{

			Set<OWLClassExpression> equivClasses = new HashSet<OWLClassExpression>();
			equivClasses.add(a1);
			equivClasses.add(factory.getOWLThing());

			// 3
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		{

			Set<OWLClassExpression> equivClasses = new HashSet<OWLClassExpression>();
			equivClasses.add(a4);
			equivClasses.add(factory.getOWLThing());

			// 4
			axiomSet.add(factory.getOWLEquivalentClassesAxiom(equivClasses));
		}

		OWLOntology ontology = manager.createOntology(axiomSet);
		JcelReasonerFactory reasonerFactory = new JcelReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		Set<OWLClass> equivToC = reasoner.getEquivalentClasses(c).getEntities();
		assertTrue(equivToC.contains(d));

		Set<OWLClass> equivToD = reasoner.getEquivalentClasses(d).getEntities();
		assertTrue(equivToD.contains(c));

		verifyBottomAndTop(reasoner);

	}

	private void verifyBottomAndTop(OWLReasoner reasoner) {
		OWLClass top = reasoner.getRootOntology().getOWLOntologyManager()
				.getOWLDataFactory().getOWLThing();
		OWLClass bottom = reasoner.getRootOntology().getOWLOntologyManager()
				.getOWLDataFactory().getOWLNothing();

		assertTrue(reasoner.getSubClasses(bottom, true).isEmpty());
		assertTrue(reasoner.getSubClasses(bottom, false).isEmpty());
		assertTrue(reasoner.getSuperClasses(top, true).isEmpty());
		assertTrue(reasoner.getSuperClasses(top, false).isEmpty());
	}

}
