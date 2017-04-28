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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Set of tests for the ontology entailment checker.
 * 
 * @see OntologyEntailmentChecker
 * 
 * @author Julian Mendez
 */
public class OntologyEntailmentCheckerTest {

	public OntologyEntailmentCheckerTest() {
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
	 * <li>A \u2291 &exist; r <i>.</i> B ,</li>
	 * <li>&exist; r <i>.</i> C \u2291 D ,</li>
	 * <li>B \u2291 C</li>
	 * </ol>
	 * &vDash;
	 * <ul>
	 * <li>A \u2291 &exist; r <i>.</i> B ,</li>
	 * <li>&exist; r <i>.</i> C \u2291 D ,</li>
	 * <li>B \u2291 C ,</li>
	 * <li>A \u2291 D</li>
	 * </ul>
	 */
	@Test
	public void testOntology0() {
		Set<IntegerAnnotation> annotations = new TreeSet<>();
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");

		ComplexIntegerAxiom axiom1 = factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b), annotations);
		ontology.add(axiom1);

		ComplexIntegerAxiom axiom2 = factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c), d, annotations);
		ontology.add(axiom2);

		ComplexIntegerAxiom axiom3 = factory.getComplexAxiomFactory().createSubClassOfAxiom(b, c, annotations);
		ontology.add(axiom3);

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		Assert.assertTrue(reasoner.isEntailed(axiom1));
		Assert.assertTrue(reasoner.isEntailed(axiom2));
		Assert.assertTrue(reasoner.isEntailed(axiom3));

		boolean isEntailed = reasoner
				.isEntailed(factory.getComplexAxiomFactory().createSubClassOfAxiom(b, c, annotations));

		Assert.assertTrue(isEntailed);
	}

}
