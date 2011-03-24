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

package de.tudresden.inf.lat.jcel.core.normalization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IdGenerator;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectPropertyExpression;

/**
 * <p>
 * <ul>
 * <li>NR 2-1 : r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k</sub> &#8849; s
 * &#8605; r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k-1</sub> &#8849; u, u
 * &#8728; r<sub>k</sub> &#8849; s</li>
 * </ul>
 * </p>
 * 
 * This rule was slightly modified to return only normalized axioms. <br />
 * 
 * This rule uses the following properties: <br />
 * 
 * <ul>
 * <li>r &#8849; s &hArr; r<sup>-</sup> &#8849; s<sup>-</sup></li>
 * <li>(r &#8728; s)<sup>-</sup> &equiv; s<sup>-</sup> &#8728; r<sup>-</sup></li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_1 implements NormalizationRule {

	private IdGenerator idGenerator = null;

	/**
	 * Constructs a new normalizer of rule NR-2.1.
	 * 
	 * @param generator
	 *            an identifier generator
	 */
	public NormalizerNR2_1(IdGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.idGenerator = generator;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubPropertyChainOfAxiom) {
			ret = applyRule((IntegerSubPropertyChainOfAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(
			IntegerSubPropertyChainOfAxiom propertyAxiom) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		List<IntegerObjectPropertyExpression> propertyList = propertyAxiom
				.getPropertyChain();
		Integer superProperty = getNormalizedObjectProperty(propertyAxiom
				.getSuperProperty());
		while (propertyList.size() > 2) {
			int lastPos = propertyList.size() - 1;
			Integer lastPropertyName = getNormalizedObjectProperty(propertyList
					.get(lastPos));
			Integer newPropertyName = this.idGenerator
					.createNewObjectPropertyId();
			ret.add(new RI3Axiom(newPropertyName, lastPropertyName,
					superProperty));
			ret.add(createRI3AxiomWithInverse(newPropertyName,
					lastPropertyName, superProperty));
			propertyList = propertyList.subList(0, lastPos);
			superProperty = newPropertyName;
		}
		if (propertyList.size() == 2) {
			Iterator<IntegerObjectPropertyExpression> it = propertyList
					.iterator();
			Integer first = getNormalizedObjectProperty(it.next());
			Integer second = getNormalizedObjectProperty(it.next());
			ret.add(new RI3Axiom(first, second, superProperty));
			ret.add(createRI3AxiomWithInverse(first, second, superProperty));
		} else if (propertyList.size() == 1) {
			Integer subProperty = getNormalizedObjectProperty(propertyList
					.iterator().next());
			ret.add(new RI2Axiom(subProperty, superProperty));
			ret.add(createRI2AxiomWithInverse(subProperty, superProperty));
		} else if (propertyList.size() == 0) {
			ret.add(new RI1Axiom(superProperty));
		}
		return ret;
	}

	private RI2Axiom createRI2AxiomWithInverse(Integer subProperty,
			Integer superProperty) {
		Integer inverseSubProperty = this.idGenerator
				.createOrGetInverseObjectPropertyOf(subProperty);
		Integer inverseSuperProperty = this.idGenerator
				.createOrGetInverseObjectPropertyOf(superProperty);
		return new RI2Axiom(inverseSubProperty, inverseSuperProperty);
	}

	private RI3Axiom createRI3AxiomWithInverse(Integer firstProperty,
			Integer secondProperty, Integer superProperty) {
		Integer inverseFirstProperty = this.idGenerator
				.createOrGetInverseObjectPropertyOf(firstProperty);
		Integer inverseLastProperty = this.idGenerator
				.createOrGetInverseObjectPropertyOf(secondProperty);
		Integer inverseSuperProperty = this.idGenerator
				.createOrGetInverseObjectPropertyOf(superProperty);
		return new RI3Axiom(inverseLastProperty, inverseFirstProperty,
				inverseSuperProperty);
	}

	private Integer getNormalizedObjectProperty(
			IntegerObjectPropertyExpression propExpr) {
		Integer ret = null;
		if (propExpr instanceof IntegerObjectProperty) {
			ret = propExpr.getId();
		} else {
			ret = this.idGenerator.createOrGetInverseObjectPropertyOf(propExpr
					.getId());
		}
		return ret;
	}

}
