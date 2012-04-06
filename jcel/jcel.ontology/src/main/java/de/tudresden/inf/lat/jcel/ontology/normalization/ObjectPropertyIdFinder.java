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

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectInverseOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpressionVisitor;

/**
 * An object of this class is a normalizer for inverse object properties.
 * 
 * @author Julian Mendez
 */
public class ObjectPropertyIdFinder implements
		IntegerObjectPropertyExpressionVisitor<Integer> {

	private final IntegerEntityManager idGenerator;
	private Set<NormalizedIntegerAxiom> requiredAxioms = new HashSet<NormalizedIntegerAxiom>();

	/**
	 * Constructs a new normalizer for inverse object properties. During the
	 * process of normalization, some auxiliary inverse object properties may be
	 * created.
	 * 
	 * @param manager
	 *            entity manager
	 */
	public ObjectPropertyIdFinder(IntegerEntityManager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.idGenerator = manager;
	}

	public IntegerEntityManager getIdGenerator() {
		return this.idGenerator;
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
