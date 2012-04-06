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
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;

/**
 * <p>
 * <ul>
 * <li>NR-2.2 : C<sub>1</sub> &#8851; &hellip; &#8851; C' &#8851; &hellip;
 * &#8851; C<sub>n</sub> &#8849; D &#8605; C' &#8849; A, C<sub>1</sub> &#8851;
 * &hellip; &#8851; A &#8851; &hellip; &#8851; C<sub>n</sub> &#8849; D</li>
 * </ul>
 * </p>
 * 
 * This rule is applied to all non atomic concepts, which is slightly different
 * from the original rule that applies to one concept each time.
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_2 implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer rule NR-2.2.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR2_2(IntegerOntologyObjectFactory factory) {
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
		if (!subClass.isLiteral()) {
			IntegerClassExpression superClass = classAxiom.getSuperClass();
			if (subClass instanceof IntegerObjectIntersectionOf) {
				IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
				Set<IntegerClassExpression> operands = intersection
						.getOperands();
				Set<IntegerAxiom> newSet = applyRule(operands, superClass);
				if (newSet.size() > 1) {
					ret = newSet;
				}
			}
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(Set<IntegerClassExpression> operands,
			IntegerClassExpression superClass) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<IntegerClassExpression> newOperands = new HashSet<IntegerClassExpression>();
		boolean applied = false;
		for (IntegerClassExpression classExpression : operands) {
			if (classExpression.isLiteral()) {
				newOperands.add(classExpression);
			} else {
				applied = true;
				IntegerClass newClass = getOntologyObjectFactory()
						.getDataTypeFactory().createClass(
								getOntologyObjectFactory().getEntityManager()
										.createAnonymousEntity(
												IntegerEntityType.CLASS, true));
				ret.add(getOntologyObjectFactory().getComplexAxiomFactory()
						.createSubClassOfAxiom(classExpression, newClass));
				newOperands.add(newClass);
			}
		}
		if (applied) {
			IntegerObjectIntersectionOf newIntersection = getOntologyObjectFactory()
					.getDataTypeFactory().createObjectIntersectionOf(
							newOperands);
			ret.add(getOntologyObjectFactory().getComplexAxiomFactory()
					.createSubClassOfAxiom(newIntersection, superClass));
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
