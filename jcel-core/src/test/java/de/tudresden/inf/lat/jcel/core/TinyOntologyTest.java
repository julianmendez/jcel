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

package de.tudresden.inf.lat.jcel.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.rulebased.RuleBasedProcessor;
import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
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
public class TinyOntologyTest {

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

	private Integer createNewClass(IntegerEntityManager entityManager, String name) {
		return entityManager.createNamedEntity(IntegerEntityType.CLASS, name, false);
	}

	private Integer createNewObjectProperty(IntegerEntityManager entityManager, String name) {
		return entityManager.createNamedEntity(IntegerEntityType.OBJECT_PROPERTY, name, false);
	}

	private Processor createProcessor(Set<NormalizedIntegerAxiom> normalizedAxiomSet,
			IntegerEntityManager entityManager, NormalizedIntegerAxiomFactory factory) {
		Set<Integer> originalObjectProperties = entityManager.getEntities(IntegerEntityType.OBJECT_PROPERTY, false);
		Set<Integer> originalClasses = entityManager.getEntities(IntegerEntityType.CLASS, false);
		NormalizedAxiomExpressivityDetector detector = new NormalizedAxiomExpressivityDetector(normalizedAxiomSet);
		Processor ret = new RuleBasedProcessor(originalObjectProperties, originalClasses, normalizedAxiomSet, detector,
				factory, entityManager);
		return ret;
	}

	/**
	 * <ol>
	 * <li>A \u2291 B ,</li>
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
		NormalizedIntegerAxiomFactoryImpl factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<>();
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");
		Integer c = createNewClass(entityManager, "C");

		// 1
		ontology.add(factory.createGCI0Axiom(a, b, annotations));

		// 2
		ontology.add(factory.createGCI0Axiom(b, c, annotations));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> superClassesOfA = processor.getClassHierarchy().getAncestors(a);
		Assert.assertTrue(superClassesOfA.contains(c));

		Set<Integer> subClassesOfC = processor.getClassHierarchy().getDescendants(c);
		Assert.assertTrue(subClassesOfC.contains(a));

		Set<Integer> intermediateSet = new HashSet<>();
		intermediateSet.add(a);
		intermediateSet.add(b);
		intermediateSet.add(c);
		verifyOntology(processor, intermediateSet);
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
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<>();
		Integer r = createNewObjectProperty(entityManager, "r");
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");
		Integer c = createNewClass(entityManager, "C");

		// 1
		ontology.add(factory.createGCI2Axiom(a, r, a, annotations));

		// 2
		ontology.add(factory.createGCI0Axiom(a, b, annotations));

		// 3
		ontology.add(factory.createGCI3Axiom(r, b, c, annotations));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> superClassesOfA = processor.getClassHierarchy().getAncestors(a);
		Assert.assertTrue(superClassesOfA.contains(c));

		Set<Integer> subClassesOfC = processor.getClassHierarchy().getDescendants(c);
		Assert.assertTrue(subClassesOfC.contains(a));

		Set<Integer> intermediateSet = new HashSet<>();
		intermediateSet.add(a);
		intermediateSet.add(b);
		intermediateSet.add(c);
		verifyOntology(processor, intermediateSet);
	}

	/**
	 * <ol>
	 * <li>A \u2291 B ,</li>
	 * <li>B \u2291 A</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A &equiv; B</li>
	 * </ul>
	 */
	@Test
	public void testTinyOntology2() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<>();
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");

		// 1
		ontology.add(factory.createGCI0Axiom(a, b, annotations));

		// 2
		ontology.add(factory.createGCI0Axiom(b, a, annotations));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> equivalentsOfA = processor.getClassHierarchy().getEquivalents(a);
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<Integer> equivalentsOfB = processor.getClassHierarchy().getEquivalents(b);
		Assert.assertTrue(equivalentsOfB.contains(a));

		Set<Integer> intermediateSet = new HashSet<>();
		intermediateSet.add(a);
		intermediateSet.add(b);
		verifyOntology(processor, intermediateSet);
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
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<>();
		Integer top = IntegerEntityManager.topClassId;
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");

		// 1
		ontology.add(factory.createGCI0Axiom(top, a, annotations));

		// 2
		ontology.add(factory.createGCI0Axiom(a, b, annotations));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> equivalentsOfA = processor.getClassHierarchy().getEquivalents(a);
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<Integer> equivalentsOfB = processor.getClassHierarchy().getEquivalents(b);
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(top));

		Set<Integer> equivalentsOfTop = processor.getClassHierarchy().getEquivalents(top);
		Assert.assertTrue(equivalentsOfTop.contains(b));

		Set<Integer> equivToTop = new HashSet<>();
		equivToTop.add(processor.getClassHierarchy().getTopElement());
		equivToTop.add(a);
		equivToTop.add(b);

		Assert.assertEquals(Collections.emptySet(),
				processor.getClassHierarchy().getDescendants(processor.getClassHierarchy().getBottomElement()));

		Set<Integer> descendantsOfTop = new HashSet<>();
		descendantsOfTop.add(processor.getClassHierarchy().getBottomElement());
		Assert.assertEquals(descendantsOfTop,
				processor.getClassHierarchy().getDescendants(processor.getClassHierarchy().getTopElement()));

		Assert.assertEquals(equivToTop,
				processor.getClassHierarchy().getEquivalents(processor.getClassHierarchy().getTopElement()));

		Assert.assertEquals(Collections.emptySet(),
				processor.getClassHierarchy().getAncestors(processor.getClassHierarchy().getTopElement()));
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
		NormalizedIntegerAxiomFactory factory = new NormalizedIntegerAxiomFactoryImpl();
		IntegerEntityManager entityManager = new IntegerEntityManagerImpl();
		Set<NormalizedIntegerAxiom> ontology = new HashSet<>();
		Integer bottom = IntegerEntityManager.bottomClassId;
		Integer a = createNewClass(entityManager, "A");
		Integer b = createNewClass(entityManager, "B");

		// 1
		ontology.add(factory.createGCI0Axiom(a, bottom, annotations));

		// 2
		ontology.add(factory.createGCI0Axiom(b, a, annotations));

		Processor processor = createProcessor(ontology, entityManager, factory);
		classify(processor);

		Set<Integer> equivalentsOfA = processor.getClassHierarchy().getEquivalents(a);
		Assert.assertTrue(equivalentsOfA.contains(b));

		Set<Integer> equivalentsOfB = processor.getClassHierarchy().getEquivalents(b);
		Assert.assertTrue(equivalentsOfB.contains(a));
		Assert.assertTrue(equivalentsOfB.contains(bottom));

		Set<Integer> equivalentsOfBottom = processor.getClassHierarchy().getEquivalents(bottom);
		Assert.assertTrue(equivalentsOfBottom.contains(b));

		Set<Integer> equivToBottom = new HashSet<>();
		equivToBottom.add(processor.getClassHierarchy().getBottomElement());
		equivToBottom.add(a);
		equivToBottom.add(b);

		Assert.assertEquals(Collections.emptySet(),
				processor.getClassHierarchy().getDescendants(processor.getClassHierarchy().getBottomElement()));
		Assert.assertEquals(equivToBottom,
				processor.getClassHierarchy().getEquivalents(processor.getClassHierarchy().getBottomElement()));

		Set<Integer> ancestorsOfBottom = new HashSet<>();
		ancestorsOfBottom.add(processor.getClassHierarchy().getTopElement());
		Assert.assertEquals(ancestorsOfBottom,
				processor.getClassHierarchy().getAncestors(processor.getClassHierarchy().getBottomElement()));

		Assert.assertEquals(Collections.emptySet(),
				processor.getClassHierarchy().getAncestors(processor.getClassHierarchy().getTopElement()));

	}

	private void verifyOntology(Processor processor, Set<Integer> intermediateSet) {
		Assert.assertEquals(Collections.emptySet(),
				processor.getClassHierarchy().getDescendants(processor.getClassHierarchy().getBottomElement()));

		Assert.assertEquals(Collections.emptySet(),
				processor.getClassHierarchy().getAncestors(processor.getClassHierarchy().getTopElement()));

		{
			Set<Integer> equivToBottom = new HashSet<>();
			equivToBottom.add(processor.getClassHierarchy().getBottomElement());
			Assert.assertEquals(equivToBottom,
					processor.getClassHierarchy().getEquivalents(processor.getClassHierarchy().getBottomElement()));
		}

		{
			Set<Integer> ancestorsOfBottom = new HashSet<>();
			ancestorsOfBottom.add(processor.getClassHierarchy().getTopElement());
			ancestorsOfBottom.addAll(intermediateSet);
			Assert.assertEquals(ancestorsOfBottom,
					processor.getClassHierarchy().getAncestors(processor.getClassHierarchy().getBottomElement()));
		}
		{
			Set<Integer> equivToTop = new HashSet<>();
			equivToTop.add(processor.getClassHierarchy().getTopElement());
			Assert.assertEquals(equivToTop,
					processor.getClassHierarchy().getEquivalents(processor.getClassHierarchy().getTopElement()));

		}

		{
			Set<Integer> descendantsOfTop = new HashSet<>();
			descendantsOfTop.add(processor.getClassHierarchy().getBottomElement());
			descendantsOfTop.addAll(intermediateSet);
			Assert.assertEquals(descendantsOfTop,
					processor.getClassHierarchy().getDescendants(processor.getClassHierarchy().getTopElement()));

		}
	}

}
