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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * <p>
 * <ul>
 * <li>NR-2.3 : &exist; r <i>.</i> C' &#8849; D &#8605; C' &#8849; A, &exist; r
 * <i>.</i> A &#8849; D</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_3 implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer rule NR-2.3.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR2_3(IntegerOntologyObjectFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.ontologyObjectFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubClassOfAxiom) {
			ret = applyRule((IntegerSubClassOfAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(IntegerSubClassOfAxiom classAxiom) {
		Set<IntegerAxiom> ret = Collections.emptySet();
		IntegerClassExpression subClass = classAxiom.getSubClass();
		IntegerClassExpression superClass = classAxiom.getSuperClass();
		if (subClass instanceof IntegerObjectSomeValuesFrom) {
			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			IntegerObjectPropertyExpression propertyExpression = restriction
					.getProperty();
			IntegerClassExpression filler = restriction.getFiller();
			if (!filler.isLiteral()) {
				ret = new HashSet<IntegerAxiom>();
				IntegerClass newClass = getOntologyObjectFactory()
						.getDataTypeFactory().createClass(
								getOntologyObjectFactory().getEntityManager()
										.createAnonymousEntity(
												IntegerEntityType.CLASS, true));
				IntegerObjectSomeValuesFrom newExistential = getOntologyObjectFactory()
						.getDataTypeFactory().createObjectSomeValuesFrom(
								propertyExpression, newClass);
				ret.add(getOntologyObjectFactory().getComplexAxiomFactory()
						.createSubClassOfAxiom(filler, newClass));
				ret.add(getOntologyObjectFactory().getComplexAxiomFactory()
						.createSubClassOfAxiom(newExistential, superClass));
			}
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
