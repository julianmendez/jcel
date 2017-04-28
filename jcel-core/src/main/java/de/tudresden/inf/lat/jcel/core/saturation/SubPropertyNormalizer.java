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

package de.tudresden.inf.lat.jcel.core.saturation;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * This class models a normalizer that saturates an ontology with object
 * property inclusions. The saturation adds an axiom r \u2291 r for each object
 * property r, and then applies the following rules:
 *
 * <ul>
 * <li>SR-1 : r \u2291 s \u219D r<sup>-</sup> \u2291 s<sup>-</sup></li>
 * <li>SR-2 : r \u2291 s, s \u2291 t \u219D r \u2291 t</li>
 * <li>SR-3 : r \u2218 s \u2291 t \u219D s<sup>-</sup> \u2218 r<sup>-</sup>
 * \u2291 t<sup>-</sup></li>
 * <li>SR-4 : r \u2291 s, f(s) \u219D f(r)</li>
 * </ul>
 *
 * @author Julian Mendez
 *
 */
public class SubPropertyNormalizer implements SaturationRule {

	private final SaturationRule sr0;
	private final SaturationRule sr1sr2;
	private final SaturationRule sr3;
	private final SaturationRule sr4;

	/**
	 * Constructs a new normalizer of sub object properties.
	 *
	 * @param factory
	 *            factory
	 * @param entityManager
	 *            entity manager
	 */
	public SubPropertyNormalizer(NormalizedIntegerAxiomFactory factory, IntegerEntityManager entityManager) {
		Objects.requireNonNull(factory);
		this.sr0 = new SR0Rule(factory);
		this.sr1sr2 = new SR1AndSR2Rules(factory, entityManager);
		this.sr3 = new SR3Rule(factory, entityManager);
		this.sr4 = new SR4Rule(factory);
	}

	/**
	 * Saturates a set of normalized axioms by applying rules SR-1, SR-2, SR-3,
	 * SR-4 and SR-5.
	 *
	 * @param originalSet
	 *            set of normalized axioms to be saturated
	 * @return the saturated set of normalized axioms
	 */
	@Override
	public Set<NormalizedIntegerAxiom> apply(Set<NormalizedIntegerAxiom> originalSet) {
		Objects.requireNonNull(originalSet);
		return Collections
				.unmodifiableSet(this.sr4.apply(this.sr3.apply(this.sr1sr2.apply(this.sr0.apply(originalSet)))));
	}

}
