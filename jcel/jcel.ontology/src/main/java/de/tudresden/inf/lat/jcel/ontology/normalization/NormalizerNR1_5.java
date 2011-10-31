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
import java.util.Iterator;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * <p>
 * <ul>
 * <li>NR-1.5 : C &#8801; D &#8605; C &#8849; D, D &#8849; C</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_5 implements NormalizationRule {

	private IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer of rule NR-1.5.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR1_5(IntegerOntologyObjectFactory factory) {
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
		if (axiom instanceof IntegerEquivalentClassesAxiom) {
			ret = applyRule((IntegerEquivalentClassesAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(
			IntegerEquivalentClassesAxiom equivalentAxiom) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<IntegerClassExpression> classExpressionSet = equivalentAxiom
				.getClassExpressions();
		for (Iterator<IntegerClassExpression> firstIt = classExpressionSet
				.iterator(); firstIt.hasNext();) {
			IntegerClassExpression firstClassExpression = firstIt.next();
			for (Iterator<IntegerClassExpression> secondIt = classExpressionSet
					.iterator(); secondIt.hasNext();) {
				IntegerClassExpression secondClassExpression = secondIt.next();
				if (!firstClassExpression.equals(secondClassExpression)) {
					IntegerSubClassOfAxiom subClassAxiom = getOntologyObjectFactory()
							.getComplexAxiomFactory()
							.createSubClassOfAxiom(firstClassExpression,
									secondClassExpression);
					ret.add(subClassAxiom);
				}
			}
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
