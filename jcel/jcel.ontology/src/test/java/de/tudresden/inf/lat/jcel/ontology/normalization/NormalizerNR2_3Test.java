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
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * @author Julian Mendez
 * 
 * @see NormalizerNR2_3
 */
public class NormalizerNR2_3Test extends TestCase {

	/**
	 * &exist; r <i>.</i> &exist; s<sub>1</sub> <i>.</i> C<sub>1</sub> &#8849;
	 * &exist; s<sub>2</sub> <i>.</i> C<sub>2</sub> &#8605; &exist;
	 * s<sub>1</sub> <i>.</i> C<sub>1</sub> &#8849; A, &exist; r <i>.</i> A
	 * &#8849; &exist; s<sub>2</sub> <i>.</i> C<sub>2</sub>
	 */
	public void testRule() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR2_3 normalizer = new NormalizerNR2_3(factory);

		IntegerObjectProperty r = factory.getDataTypeFactory()
				.createObjectProperty(
						factory.getEntityManager().createNamedEntity(
								IntegerEntityType.OBJECT_PROPERTY, "r", false));

		IntegerClass c1 = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "A1", false));

		IntegerObjectProperty s1 = factory
				.getDataTypeFactory()
				.createObjectProperty(
						factory.getEntityManager().createNamedEntity(
								IntegerEntityType.OBJECT_PROPERTY, "r1", false));

		IntegerClass c2 = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "A2", false));

		IntegerObjectProperty s2 = factory
				.getDataTypeFactory()
				.createObjectProperty(
						factory.getEntityManager().createNamedEntity(
								IntegerEntityType.OBJECT_PROPERTY, "r2", false));

		IntegerClassExpression cPrime = factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(s1, c1);
		IntegerClassExpression c = factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(r, cPrime);
		IntegerClassExpression d = factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(s2, c2);
		IntegerSubClassOfAxiom axiom = factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(c, d);

		Set<IntegerAxiom> normalizedAxioms = normalizer.apply(axiom);

		IntegerClassExpression a = null;
		for (IntegerAxiom normAxiom : normalizedAxioms) {
			if (normAxiom instanceof IntegerSubClassOfAxiom) {
				IntegerSubClassOfAxiom currentAxiom = (IntegerSubClassOfAxiom) normAxiom;
				IntegerClassExpression leftExpr = currentAxiom.getSubClass();
				if (leftExpr instanceof IntegerObjectSomeValuesFrom) {
					IntegerObjectSomeValuesFrom expr = (IntegerObjectSomeValuesFrom) leftExpr;
					if (expr.getProperty().equals(r)) {
						assertTrue(a == null);
						a = expr.getFiller();
					}
				}
			}
		}

		assertTrue(a != null);
		assertTrue(a.isLiteral());

		Set<IntegerAxiom> expectedAxioms = new HashSet<IntegerAxiom>();
		expectedAxioms.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(cPrime, a));

		IntegerClassExpression newExpr = factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(r, a);
		expectedAxioms.add(factory.getComplexAxiomFactory()
				.createSubClassOfAxiom(newExpr, d));

		assertEquals(expectedAxioms, normalizedAxioms);
	}

}
