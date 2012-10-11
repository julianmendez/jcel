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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * 
 * @author Julian Mendez
 * 
 * @see NormalizerNR1_5
 */
public class NormalizerNR1_5Test extends TestCase {

	public void testUsingClasses() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR1_5 normalizer = new NormalizerNR1_5(factory);

		IntegerClass c = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "C", false));
		IntegerClass d = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "D", false));

		Set<IntegerClassExpression> set = new HashSet<IntegerClassExpression>();
		set.add(c);
		set.add(d);
		IntegerEquivalentClassesAxiom axiom = factory.getComplexAxiomFactory()
				.createEquivalentClassesAxiom(set);
		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(axiom);

		Set<IntegerAxiom> expectedAxioms = new HashSet<IntegerAxiom>();
		expectedAxioms.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(c, d));
		expectedAxioms.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(d, c));

		assertEquals(expectedAxioms, normalizedAxioms);
	}

}
