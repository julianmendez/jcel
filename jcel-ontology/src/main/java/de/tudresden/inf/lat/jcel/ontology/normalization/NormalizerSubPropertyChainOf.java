/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
public class NormalizerSubPropertyChainOf implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	public NormalizerSubPropertyChainOf(IntegerOntologyObjectFactory factory) {
		this.ontologyObjectFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = new HashSet<>();
		if (axiom instanceof IntegerSubPropertyChainOfAxiom) {
			Collection<NormalizedIntegerAxiom> normalizedAxioms = simplify((IntegerSubPropertyChainOfAxiom) axiom);
			normalizedAxioms.forEach(normalizedAxiom -> {
				ret.add(normalizedAxiom);
			});
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

	private Collection<NormalizedIntegerAxiom> simplify(IntegerSubPropertyChainOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<>();
		List<IntegerObjectPropertyExpression> propChain = axiom.getPropertyChain();
		IntegerObjectPropertyExpression rightPart = axiom.getSuperProperty();

		if (propChain.size() == 0) {

			ret.add(getNormalizedAxiomFactory().createRI1Axiom(getObjectPropertyId(rightPart), axiom.getAnnotations()));

		} else if (propChain.size() == 1) {

			Iterator<IntegerObjectPropertyExpression> it = propChain.iterator();
			IntegerObjectPropertyExpression leftPropExpr = it.next();
			ret.add(getNormalizedAxiomFactory().createRI2Axiom(getObjectPropertyId(leftPropExpr),
					getObjectPropertyId(rightPart), axiom.getAnnotations()));

		} else if (propChain.size() == 2) {

			Iterator<IntegerObjectPropertyExpression> it = propChain.iterator();
			IntegerObjectPropertyExpression leftLeftProp = it.next();
			IntegerObjectPropertyExpression leftRightProp = it.next();
			ret.add(getNormalizedAxiomFactory().createRI3Axiom(getObjectPropertyId(leftLeftProp),
					getObjectPropertyId(leftRightProp), getObjectPropertyId(rightPart), axiom.getAnnotations()));

		}
		return ret;
	}

}
