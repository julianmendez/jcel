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
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactoryImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * Test class for ontology normalizer.
 * 
 * @see OntologyNormalizer
 * 
 * @author Julian Mendez
 */
public class OntologyNormalizerTest extends TestCase {

	/**
	 * Constructs a new ontology normalizer.
	 */
	public OntologyNormalizerTest() {
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

	public void testGCI3InverseObjectProperties() {
		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");
		IntegerObjectProperty rMinus = createNewObjectProperty(factory, "r-");

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		ontology.add(factory.getComplexAxiomFactory()
				.createInverseObjectPropertiesAxiom(rMinus, r));

		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(rMinus,
						a), b));

		OntologyNormalizer normalizer = new OntologyNormalizer();

		Set<NormalizedIntegerAxiom> normalizedSet = normalizer.normalize(
				ontology, factory);

		Set<NormalizedIntegerAxiom> expectedSet = new HashSet<NormalizedIntegerAxiom>();
		expectedSet.add(factory.getNormalizedAxiomFactory().createGCI3Axiom(
				rMinus.getId(), a.getId(), b.getId()));

		assertEquals(expectedSet, normalizedSet);
	}

	public void testGCI3ObjectInverseOf() {

		IntegerOntologyObjectFactory factory = new IntegerOntologyObjectFactoryImpl();

		IntegerClass a = createNewClass(factory, "A");
		IntegerClass b = createNewClass(factory, "B");
		IntegerObjectProperty r = createNewObjectProperty(factory, "r");

		Set<ComplexIntegerAxiom> ontology = new HashSet<ComplexIntegerAxiom>();
		ontology.add(factory.getComplexAxiomFactory().createSubClassOfAxiom(
				factory.getDataTypeFactory().createObjectSomeValuesFrom(
						factory.getDataTypeFactory().createObjectInverseOf(r),
						a), b));

		OntologyNormalizer normalizer = new OntologyNormalizer();

		Set<NormalizedIntegerAxiom> normalizedSet = normalizer.normalize(
				ontology, factory);

		Set<NormalizedIntegerAxiom> expectedSet = new HashSet<NormalizedIntegerAxiom>();
		expectedSet.add(factory.getNormalizedAxiomFactory().createGCI3Axiom(
				factory.getEntityManager().createOrGetInverseObjectPropertyOf(
						r.getId()), a.getId(), b.getId()));

		assertEquals(expectedSet, normalizedSet);
	}

}
