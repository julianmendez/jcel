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

import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectInverseOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpressionVisitor;

/**
 * An object of this class is a normalizer for inverse object properties.
 * 
 * @author Julian Mendez
 */
class NormalizerObjectInverseOf implements
		IntegerObjectPropertyExpressionVisitor<Integer> {

	private final NormalizedIntegerAxiomFactory factory;
	private final IntegerEntityManager idGenerator;
	private Set<NormalizedIntegerAxiom> requiredAxioms = new HashSet<NormalizedIntegerAxiom>();

	/**
	 * Constructs a new normalizer for inverse object properties. During the
	 * process of normalization, some auxiliary inverse object properties may be
	 * created. A set of required axioms has a set of axioms to relate
	 * previously existent and created object properties.
	 * 
	 * @param manager
	 *            entity manager
	 * @param factory
	 *            factory of normalized axioms
	 */
	public NormalizerObjectInverseOf(IntegerEntityManager manager,
			NormalizedIntegerAxiomFactory factory) {
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.idGenerator = manager;
		this.factory = factory;
	}

	/**
	 * Returns a set of normalized axioms that relates two object properties
	 * such that one is the inverse of the other one.
	 * 
	 * @param firstProperty
	 *            first object property
	 * @param secondProperty
	 *            second object property, which is the inverse of the first one
	 * @return a set of normalized axioms that relates two object properties
	 *         such that one is the inverse of the other one
	 */
	public Set<NormalizedIntegerAxiom> getAxiomsForInverseObjectProperties(
			Integer firstProperty, Integer secondProperty) {
		if (firstProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (secondProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		Integer inverseFirstProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(firstProperty);
		Integer inverseSecondProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(secondProperty);
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(firstProperty,
				inverseSecondProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(
				inverseSecondProperty, firstProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(secondProperty,
				inverseFirstProperty));
		ret.add(getNormalizedAxiomFactory().createRI2Axiom(
				inverseFirstProperty, secondProperty));
		return Collections.unmodifiableSet(ret);

	}

	public IntegerEntityManager getIdGenerator() {
		return this.idGenerator;
	}

	public NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
		return this.factory;
	}

	public Set<NormalizedIntegerAxiom> getRequiredAxioms() {
		return Collections.unmodifiableSet(this.requiredAxioms);
	}

	@Override
	public Integer visit(IntegerObjectInverseOf objectPropertyExpression) {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		Integer firstProperty = objectPropertyExpression.getInverse().getId();
		Integer secondProperty = getIdGenerator()
				.createOrGetInverseObjectPropertyOf(firstProperty);
		this.requiredAxioms.addAll(this.getAxiomsForInverseObjectProperties(
				firstProperty, secondProperty));
		return secondProperty;
	}

	@Override
	public Integer visit(IntegerObjectProperty objectPropertyExpression) {
		if (objectPropertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return objectPropertyExpression.getId();
	}

}
