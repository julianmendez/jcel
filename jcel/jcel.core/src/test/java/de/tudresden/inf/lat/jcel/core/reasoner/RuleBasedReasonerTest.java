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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Test for rule based reasoner.
 * 
 * @see RuleBasedReasoner
 * 
 * @author Julian Mendez
 */
public class RuleBasedReasonerTest extends TestCase {

	private IntegerClass createNewClass(IntegerOntologyObjectFactory factory,
			String name) {
		return factory.getDataTypeFactory().createClass(
				factory.getIdGenerator().createNamedEntity(
						IntegerEntityType.CLASS, name, false));
	}

	private IntegerObjectProperty createNewObjectProperty(
			IntegerOntologyObjectFactory factory, String name) {
		return factory.getDataTypeFactory().createObjectProperty(
				factory.getIdGenerator().createNamedEntity(
						IntegerEntityType.OBJECT_PROPERTY, name, false));
	}

	public void testOntology1() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerClass c = createNewClass(factory, "C");
		IntegerClass d = createNewClass(factory, "D");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "R");
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(rMinus.getId(), r.getId()));
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(a,
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, b)));
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus,
						a), c));
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(r, c),
				d));

		IntegerReasoner reasoner = new RuleBasedReasoner(ontology, factory,
				false);
		reasoner.classify();
		{
			Set<Set<IntegerClass>> subClasses = reasoner.getSubClasses(b, true);
			assertEquals(subClasses.size(), 1);
			Set<IntegerClass> subElemSet = subClasses.iterator().next();
			assertEquals(subElemSet.size(), 1);
			IntegerClass x = subElemSet.iterator().next();
			assertEquals(factory.getDataTypeFactory().getBottomClass(), x);
		}
		{

			Set<Set<IntegerClass>> subClasses = reasoner.getSubClasses(d, true);
			assertEquals(subClasses.size(), 1);
			Set<IntegerClass> subElemSet = subClasses.iterator().next();
			assertEquals(subElemSet.size(), 1);
			IntegerClass x = subElemSet.iterator().next();
			assertEquals(a, x);
		}
	}

}
