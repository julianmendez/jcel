/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.rulebased.RuleBasedProcessor;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactoryImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManagerImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.coreontology.expressivity.NormalizedAxiomExpressivityDetector;

/**
 * Set of tests using tiny ontologies.
 * 
 * @see RuleBasedProcessor
 * 
 * @author Julian Mendez
 */
public class TinyOntologyTest extends TestCase {

	/**
	 * Constructs a new set of tests for the rule based reasoner.
	 */
	public TinyOntologyTest() {
	}

	private void classify(Processor processor) {
		while (!processor.isReady()) {
			processor.process();
		}
	}

	private Integer createNewClass(IntegerEntityManager entityManager,
			String name) {
		return entityManager.createNamedEntity(IntegerEntityType.CLASS, name,
				false);
	}

	private Integer createNewObjectProperty(IntegerEntityManager entityManager,
			String name) {
		return entityManager.createNamedEntity(
				IntegerEntityType.OBJECT_PROPERTY, name, false);
	}

	private Processor createProcessor(
			Set<NormalizedIntegerAxiom> normalizedAxiomSet,
			IntegerEntityManager entityManager,
			NormalizedIntegerAxiomFactory factory) {
		Set<Integer> originalObjectProperties = entityManager.getEntities(
				IntegerEntityType.OBJECT_PROPERTY, false);
		Set<Integer> originalClasses = entityManager.getEntities(
				IntegerEntityType.CLASS, false);
		NormalizedAxiomExpressivityDetector detector = new NormalizedAxiomExpressivityDetector(
				normalizedAxiomSet);
		Processor ret = new RuleBasedProcessor(originalObjectProperties,
				originalClasses, normalizedAxiomSet, detector, factory,
				entityManager);
		return ret;
	}

	/**
	 * <ol>
	 * <li>A &#x2291; B ,</li>
	 * <li>B &#x2291; C</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &#x2291; C</li>
	 * </ul>
	 */
	public void testTinyOntology0() {
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");
		Integer c = createNewClass(entityManager, "C");

		// 1
		ontology.add(factory.createGCI0Axiom(a, b));

		// 2
		ontology.add(factory.createGCI0Axiom(b, c));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> superClassesOfA = processor.getClassHierarchy()
				.getAncestors(a);
		assertTrue(superClassesOfA.contains(c));

		Set<Integer> subClassesOfC = processor.getClassHierarchy()
				.getDescendants(c);
		assertTrue(subClassesOfC.contains(a));

		Set<Integer> intermediateSet = new HashSet<Integer>();
		intermediateSet.add(a);
		intermediateSet.add(b);
		intermediateSet.add(c);
		verifyOntology(processor, intermediateSet);
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
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		Integer r = createNewObjectProperty(entityManager, "r");
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");
		Integer c = createNewClass(entityManager, "C");

		// 1
		ontology.add(factory.createGCI2Axiom(a, r, a));

		// 2
		ontology.add(factory.createGCI0Axiom(a, b));

		// 3
		ontology.add(factory.createGCI3Axiom(r, b, c));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> superClassesOfA = processor.getClassHierarchy()
				.getAncestors(a);
		assertTrue(superClassesOfA.contains(c));

		Set<Integer> subClassesOfC = processor.getClassHierarchy()
				.getDescendants(c);
		assertTrue(subClassesOfC.contains(a));

		Set<Integer> intermediateSet = new HashSet<Integer>();
		intermediateSet.add(a);
		intermediateSet.add(b);
		intermediateSet.add(c);
		verifyOntology(processor, intermediateSet);
	}

	/**
	 * <ol>
	 * <li>A &#x2291; B ,</li>
	 * <li>B &#x2291; A</li>
	 * </ol>
	 * &#x22a8;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * </ul>
	 */
	public void testTinyOntology2() {
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");

		// 1
		ontology.add(factory.createGCI0Axiom(a, b));

		// 2
		ontology.add(factory.createGCI0Axiom(b, a));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> equivalentsOfA = processor.getClassHierarchy()
				.getEquivalents(a);
		assertTrue(equivalentsOfA.contains(b));

		Set<Integer> equivalentsOfB = processor.getClassHierarchy()
				.getEquivalents(b);
		assertTrue(equivalentsOfB.contains(a));

		Set<Integer> intermediateSet = new HashSet<Integer>();
		intermediateSet.add(a);
		intermediateSet.add(b);
		verifyOntology(processor, intermediateSet);
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
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		Integer top = IntegerEntityManager.topClassId;
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");

		// 1
		ontology.add(factory.createGCI0Axiom(top, a));

		// 2
		ontology.add(factory.createGCI0Axiom(a, b));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> equivalentsOfA = processor.getClassHierarchy()
				.getEquivalents(a);
		assertTrue(equivalentsOfA.contains(b));

		Set<Integer> equivalentsOfB = processor.getClassHierarchy()
				.getEquivalents(b);
		assertTrue(equivalentsOfB.contains(a));
		assertTrue(equivalentsOfB.contains(top));

		Set<Integer> equivalentsOfTop = processor.getClassHierarchy()
				.getEquivalents(top);
		assertTrue(equivalentsOfTop.contains(b));

		Set<Integer> equivToTop = new HashSet<Integer>();
		equivToTop.add(processor.getClassHierarchy().getTopElement());
		equivToTop.add(a);
		equivToTop.add(b);

		assertEquals(
				Collections.emptySet(),
				processor.getClassHierarchy().getDescendants(
						processor.getClassHierarchy().getBottomElement()));

		Set<Integer> descendantsOfTop = new HashSet<Integer>();
		descendantsOfTop.add(processor.getClassHierarchy().getBottomElement());
		assertEquals(descendantsOfTop, processor.getClassHierarchy()
				.getDescendants(processor.getClassHierarchy().getTopElement()));

		assertEquals(
				equivToTop,
				processor.getClassHierarchy().getEquivalents(
						processor.getClassHierarchy().getTopElement()));

		assertEquals(Collections.emptySet(), processor.getClassHierarchy()
				.getAncestors(processor.getClassHierarchy().getTopElement()));
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
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<NormalizedIntegerAxiom>();
		Integer bottom = IntegerEntityManager.bottomClassId;
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");

		// 1
		ontology.add(factory.createGCI0Axiom(a, bottom));

		// 2
		ontology.add(factory.createGCI0Axiom(b, a));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> equivalentsOfA = processor.getClassHierarchy()
				.getEquivalents(a);
		assertTrue(equivalentsOfA.contains(b));

		Set<Integer> equivalentsOfB = processor.getClassHierarchy()
				.getEquivalents(b);
		assertTrue(equivalentsOfB.contains(a));
		assertTrue(equivalentsOfB.contains(bottom));

		Set<Integer> equivalentsOfBottom = processor.getClassHierarchy()
				.getEquivalents(bottom);
		assertTrue(equivalentsOfBottom.contains(b));

		Set<Integer> equivToBottom = new HashSet<Integer>();
		equivToBottom.add(processor.getClassHierarchy().getBottomElement());
		equivToBottom.add(a);
		equivToBottom.add(b);

		assertEquals(
				Collections.emptySet(),
				processor.getClassHierarchy().getDescendants(
						processor.getClassHierarchy().getBottomElement()));
		assertEquals(
				equivToBottom,
				processor.getClassHierarchy().getEquivalents(
						processor.getClassHierarchy().getBottomElement()));

		Set<Integer> ancestorsOfBottom = new HashSet<Integer>();
		ancestorsOfBottom.add(processor.getClassHierarchy().getTopElement());
		assertEquals(ancestorsOfBottom, processor.getClassHierarchy()
				.getAncestors(processor.getClassHierarchy().getBottomElement()));

		assertEquals(Collections.emptySet(), processor.getClassHierarchy()
				.getAncestors(processor.getClassHierarchy().getTopElement()));

	}

	private void verifyOntology(Processor processor,
			Set<Integer> intermediateSet) {
		assertEquals(
				Collections.emptySet(),
				processor.getClassHierarchy().getDescendants(
						processor.getClassHierarchy().getBottomElement()));

		assertEquals(Collections.emptySet(), processor.getClassHierarchy()
				.getAncestors(processor.getClassHierarchy().getTopElement()));

		{
			Set<Integer> equivToBottom = new HashSet<Integer>();
			equivToBottom.add(processor.getClassHierarchy().getBottomElement());
			assertEquals(
					equivToBottom,
					processor.getClassHierarchy().getEquivalents(
							processor.getClassHierarchy().getBottomElement()));
		}

		{
			Set<Integer> ancestorsOfBottom = new HashSet<Integer>();
			ancestorsOfBottom
					.add(processor.getClassHierarchy().getTopElement());
			ancestorsOfBottom.addAll(intermediateSet);
			assertEquals(
					ancestorsOfBottom,
					processor.getClassHierarchy().getAncestors(
							processor.getClassHierarchy().getBottomElement()));
		}
		{
			Set<Integer> equivToTop = new HashSet<Integer>();
			equivToTop.add(processor.getClassHierarchy().getTopElement());
			assertEquals(
					equivToTop,
					processor.getClassHierarchy().getEquivalents(
							processor.getClassHierarchy().getTopElement()));

		}

		{
			Set<Integer> descendantsOfTop = new HashSet<Integer>();
			descendantsOfTop.add(processor.getClassHierarchy()
					.getBottomElement());
			descendantsOfTop.addAll(intermediateSet);
			assertEquals(
					descendantsOfTop,
					processor.getClassHierarchy().getDescendants(
							processor.getClassHierarchy().getTopElement()));

		}
	}

}
