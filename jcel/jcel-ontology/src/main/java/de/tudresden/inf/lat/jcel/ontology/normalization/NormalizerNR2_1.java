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

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
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
		Integer superProperty = getObjectPropertyId(propertyAxiom
				.getSuperProperty());
		while (propertyList.size() > 2) {
			int lastPos = propertyList.size() - 1;
			Integer lastPropertyName = getObjectPropertyId(propertyList
					.get(lastPos));
			Integer newPropertyName = getOntologyObjectFactory()
					.getEntityManager().createAnonymousEntity(
							IntegerEntityType.OBJECT_PROPERTY, true);
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI3Axiom(newPropertyName, lastPropertyName,
							superProperty));
			propertyList = propertyList.subList(0, lastPos);
			superProperty = newPropertyName;
		}
		if (propertyList.size() == 2) {
			Iterator<IntegerObjectPropertyExpression> it = propertyList
					.iterator();
			Integer first = getObjectPropertyId(it.next());
			Integer second = getObjectPropertyId(it.next());
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI3Axiom(first, second, superProperty));
		} else if (propertyList.size() == 1) {
			Integer subProperty = getObjectPropertyId(propertyList.iterator()
					.next());
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI2Axiom(subProperty, superProperty));
		} else if (propertyList.size() == 0) {
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRI1Axiom(superProperty));
		}
		return ret;
	}

	private Integer getObjectPropertyId(IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(new ObjectPropertyIdFinder(
				getOntologyObjectFactory().getEntityManager()));
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
