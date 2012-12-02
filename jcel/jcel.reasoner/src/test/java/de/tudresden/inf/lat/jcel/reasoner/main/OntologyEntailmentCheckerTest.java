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

import junit.framework.TestCase;
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
public class OntologyEntailmentCheckerTest extends TestCase {

	public OntologyEntailmentCheckerTest() {
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
	 * <li>A &#8849; &exist; r <i>.</i> B ,</li>
	 * <li>&exist; r <i>.</i> C &#8849; D ,</li>
	 * <li>B &#8849; C</li>
	 * </ol>
	 * &#8872;
	 * <ul>
	 * <li>A &#8849; &exist; r <i>.</i> B ,</li>
	 * <li>&exist; r <i>.</i> C &#8849; D ,</li>
	 * <li>B &#8849; C ,</li>
	 * <li>A &#8849; D</li>
	 * </ul>
	 */
	public void testOntology0() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");

		ComplexIntegerAxiom axiom1 = factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(
						a,
						factory.getDataTypeFactory()
								.createObjectSomeValuesFrom(r, b));
		ontology.add(axiom1);

		ComplexIntegerAxiom axiom2 = factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(
						factory.getDataTypeFactory()
								.createObjectSomeValuesFrom(r, c), d);
		ontology.add(axiom2);

		ComplexIntegerAxiom axiom3 = factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(b, c);
		ontology.add(axiom3);

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory);
		reasoner.classify();

		assertTrue(reasoner.isEntailed(axiom1));
		assertTrue(reasoner.isEntailed(axiom2));
		assertTrue(reasoner.isEntailed(axiom3));

		boolean isEntailed = reasoner.isEntailed(factory
				.getComplexAxiomFactory().createSubClassOfAxiom(b, c));

		assertTrue(isEntailed);
	}

}
