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

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models a normalization rule that normalizes an axiom of equivalent
 * object properties.
 * 
 * @author Julian Mendez
 */
class NormalizerEquivProperties implements NormalizationRule {

	private NormalizerObjectInverseOf inversePropertyNormalizer;
	private IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer of equivalent object properties.
	 * 
	 * @param inversePropNormalizer
	 */
	public NormalizerEquivProperties(IntegerOntologyObjectFactory factory,
			NormalizerObjectInverseOf inversePropNormalizer) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (inversePropNormalizer == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.ontologyObjectFactory = factory;
		this.inversePropertyNormalizer = inversePropNormalizer;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerEquivalentObjectPropertiesAxiom) {
			ret = applyRule((IntegerEquivalentObjectPropertiesAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(
			IntegerEquivalentObjectPropertiesAxiom equivPropAxiom) {
		Set<IntegerObjectPropertyExpression> expressionSet = equivPropAxiom
				.getProperties();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		for (Iterator<IntegerObjectPropertyExpression> firstIt = expressionSet
				.iterator(); firstIt.hasNext();) {
			Integer firstExpression = getObjectPropertyId(firstIt.next());
			for (Iterator<IntegerObjectPropertyExpression> secondIt = expressionSet
					.iterator(); secondIt.hasNext();) {
				Integer secondExpression = getObjectPropertyId(secondIt.next());
				RI2Axiom subPropertyAxiom = getOntologyObjectFactory()
						.getNormalizedAxiomFactory().createRI2Axiom(
								firstExpression, secondExpression);
				ret.add(subPropertyAxiom);
			}
		}
		return ret;
	}

	private Integer getObjectPropertyId(IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(this.inversePropertyNormalizer);
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
