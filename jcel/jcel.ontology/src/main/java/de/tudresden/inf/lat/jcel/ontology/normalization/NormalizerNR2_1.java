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
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

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

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer of rule NR-2.1.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR2_1(IntegerOntologyObjectFactory factory) {
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
			Integer newPropertyName = getOntologyObjectFactory()
					.getIdGenerator().createAnonymousEntity(
							IntegerEntityType.OBJECT_PROPERTY, true);
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI3Axiom(newPropertyName, lastPropertyName,
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
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI3Axiom(first, second, superProperty));
			ret.add(createRI3AxiomWithInverse(first, second, superProperty));
		} else if (propertyList.size() == 1) {
			Integer subProperty = getNormalizedObjectProperty(propertyList
					.iterator().next());
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI2Axiom(subProperty, superProperty));
			ret.add(createRI2AxiomWithInverse(subProperty, superProperty));
		} else if (propertyList.size() == 0) {
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI1Axiom(superProperty));
		}
		return ret;
	}

	private RI2Axiom createRI2AxiomWithInverse(Integer subProperty,
			Integer superProperty) {
		Integer inverseSubProperty = getOntologyObjectFactory()
				.getIdGenerator().createOrGetInverseObjectPropertyOf(
						subProperty);
		Integer inverseSuperProperty = getOntologyObjectFactory()
				.getIdGenerator().createOrGetInverseObjectPropertyOf(
						superProperty);
		return getOntologyObjectFactory().getNormalizedAxiomFactory()
				.createRI2Axiom(inverseSubProperty, inverseSuperProperty);
	}

	private RI3Axiom createRI3AxiomWithInverse(Integer firstProperty,
			Integer secondProperty, Integer superProperty) {
		Integer inverseFirstProperty = getOntologyObjectFactory()
				.getIdGenerator().createOrGetInverseObjectPropertyOf(
						firstProperty);
		Integer inverseLastProperty = getOntologyObjectFactory()
				.getIdGenerator().createOrGetInverseObjectPropertyOf(
						secondProperty);
		Integer inverseSuperProperty = getOntologyObjectFactory()
				.getIdGenerator().createOrGetInverseObjectPropertyOf(
						superProperty);
		return getOntologyObjectFactory().getNormalizedAxiomFactory()
				.createRI3Axiom(inverseLastProperty, inverseFirstProperty,
						inverseSuperProperty);
	}

	private Integer getNormalizedObjectProperty(
			IntegerObjectPropertyExpression propExpr) {
		Integer ret = null;
		if (propExpr instanceof IntegerObjectProperty) {
			ret = propExpr.getId();
		} else {
			ret = getOntologyObjectFactory().getIdGenerator()
					.createOrGetInverseObjectPropertyOf(propExpr.getId());
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
