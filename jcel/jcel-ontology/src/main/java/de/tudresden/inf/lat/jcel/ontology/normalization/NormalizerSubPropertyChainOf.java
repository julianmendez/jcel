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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * 
 * @author Julian Mendez
 */
class NormalizerSubPropertyChainOf implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	public NormalizerSubPropertyChainOf(IntegerOntologyObjectFactory factory) {
		this.ontologyObjectFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubPropertyChainOfAxiom) {
			ret = new HashSet<IntegerAxiom>();
			Collection<NormalizedIntegerAxiom> normalizedAxioms = simplify((IntegerSubPropertyChainOfAxiom) axiom);
			for (NormalizedIntegerAxiom normalizedAxiom : normalizedAxioms) {
				ret.add(normalizedAxiom);
			}
		}
		return ret;
	}

	private IntegerEntityManager getIdGenerator() {
		return getOntologyObjectFactory().getEntityManager();
	}

	private NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
		return getOntologyObjectFactory().getNormalizedAxiomFactory();
	}

	private Integer getObjectPropertyId(IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(new ObjectPropertyIdFinder(getIdGenerator()));
	}

	public IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

	private Collection<NormalizedIntegerAxiom> simplify(
			IntegerSubPropertyChainOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<NormalizedIntegerAxiom>();
		List<IntegerObjectPropertyExpression> propChain = axiom
				.getPropertyChain();
		IntegerObjectPropertyExpression rightPart = axiom.getSuperProperty();

		if (propChain.size() == 0) {

			ret.add(getNormalizedAxiomFactory().createRI1Axiom(
					getObjectPropertyId(rightPart)));

		} else if (propChain.size() == 1) {

			Iterator<IntegerObjectPropertyExpression> it = propChain.iterator();
			IntegerObjectPropertyExpression leftPropExpr = it.next();
			ret.add(getNormalizedAxiomFactory().createRI2Axiom(
					getObjectPropertyId(leftPropExpr),
					getObjectPropertyId(rightPart)));

		} else if (propChain.size() == 2) {

			Iterator<IntegerObjectPropertyExpression> it = propChain.iterator();
			IntegerObjectPropertyExpression leftLeftProp = it.next();
			IntegerObjectPropertyExpression leftRightProp = it.next();
			ret.add(getNormalizedAxiomFactory().createRI3Axiom(
					getObjectPropertyId(leftLeftProp),
					getObjectPropertyId(leftRightProp),
					getObjectPropertyId(rightPart)));

		}
		return ret;
	}

}
