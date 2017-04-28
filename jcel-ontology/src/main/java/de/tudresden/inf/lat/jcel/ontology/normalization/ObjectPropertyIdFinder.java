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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
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
public class ObjectPropertyIdFinder implements IntegerObjectPropertyExpressionVisitor<Integer> {

	private final IntegerEntityManager idGenerator;
	private final Set<NormalizedIntegerAxiom> requiredAxioms = new HashSet<>();

	/**
	 * Constructs a new normalizer for inverse object properties. During the
	 * process of normalization, some auxiliary inverse object properties may be
	 * created.
	 * 
	 * @param manager
	 *            entity manager
	 */
	public ObjectPropertyIdFinder(IntegerEntityManager manager) {
		Objects.requireNonNull(manager);
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
		Objects.requireNonNull(objectPropertyExpression);
		Integer firstProperty = objectPropertyExpression.getInverse().getId();
		Integer secondProperty = getIdGenerator().createOrGetInverseObjectPropertyOf(firstProperty);
		return secondProperty;
	}

	@Override
	public Integer visit(IntegerObjectProperty objectPropertyExpression) {
		Objects.requireNonNull(objectPropertyExpression);
		return objectPropertyExpression.getId();
	}

}
