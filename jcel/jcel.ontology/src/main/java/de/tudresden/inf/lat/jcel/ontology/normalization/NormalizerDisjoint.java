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

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;

/**
 * This class models a normalization rule that normalizes an axiom of disjoint
 * classes.
 * 
 * @author Julian Mendez
 */
class NormalizerDisjoint implements NormalizationRule {

	private IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer of disjoint classes.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerDisjoint(IntegerOntologyObjectFactory factory) {
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
		if (axiom instanceof IntegerDisjointClassesAxiom) {
			ret = applyRule((IntegerDisjointClassesAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(
			IntegerDisjointClassesAxiom disjointAxiom) {
		Set<IntegerClassExpression> classExpressionSet = disjointAxiom
				.getClassExpressions();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		for (Iterator<IntegerClassExpression> firstIt = classExpressionSet
				.iterator(); firstIt.hasNext();) {
			IntegerClassExpression firstClassExpression = firstIt.next();
			for (Iterator<IntegerClassExpression> secondIt = classExpressionSet
					.iterator(); secondIt.hasNext();) {
				IntegerClassExpression secondClassExpression = secondIt.next();
				if (!firstClassExpression.equals(secondClassExpression)) {
					Set<IntegerClassExpression> pair = new HashSet<IntegerClassExpression>();
					pair.add(firstClassExpression);
					pair.add(secondClassExpression);
					IntegerObjectIntersectionOf intersection = getOntologyObjectFactory()
							.getDataTypeFactory().createObjectIntersectionOf(
									pair);
					IntegerSubClassOfAxiom subClassAxiom = getOntologyObjectFactory()
							.getComplexAxiomFactory()
							.createSubClassOfAxiom(
									intersection,
									getOntologyObjectFactory()
											.getDataTypeFactory()
											.createClass(
													IntegerEntityManager.bottomClassId));
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
