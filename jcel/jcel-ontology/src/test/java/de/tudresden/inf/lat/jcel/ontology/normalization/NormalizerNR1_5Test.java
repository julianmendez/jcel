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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * 
 * @author Julian Mendez
 * 
 * @see NormalizerNR1_5
 */
public class NormalizerNR1_5Test extends TestCase {

	/**
	 * C &#8801; D &#8605; C &#8849; D, D &#8849; C
	 */
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

	/**
	 * &exist; r <i>.</i> C<sub>1</sub> &#8801; C<sub>2</sub> &#8851;
	 * C<sub>3</sub> &#8605; &exist; r <i>.</i> C<sub>1</sub> &#8849;
	 * C<sub>2</sub> &#8851; C<sub>3</sub>, C<sub>2</sub> &#8851; C<sub>3</sub>
	 * &#8849; &exist; r <i>.</i> C<sub>1</sub>
	 */
	public void testUsingClassExpressions() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();
		NormalizerNR1_5 normalizer = new NormalizerNR1_5(factory);

		IntegerObjectProperty r = factory.getDataTypeFactory()
				.createObjectProperty(
						factory.getEntityManager().createNamedEntity(
								IntegerEntityType.OBJECT_PROPERTY, "r", false));
		IntegerClass c1 = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "C1", false));
		IntegerClass c2 = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "C2", false));
		IntegerClass c3 = factory.getDataTypeFactory().createClass(
				factory.getEntityManager().createNamedEntity(
						IntegerEntityType.CLASS, "C3", false));

		IntegerClassExpression c = factory.getDataTypeFactory()
				.createObjectSomeValuesFrom(r, c1);
		Set<IntegerClassExpression> parameter = new HashSet<IntegerClassExpression>();
		parameter.add(c2);
		parameter.add(c3);
		IntegerClassExpression d = factory.getDataTypeFactory()
				.createObjectIntersectionOf(parameter);

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
