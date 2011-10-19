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

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;

/**
 * <p>
 * <ul>
 * <li>NR-1.2 : range(r) &#8849; C &#8605; range(r) &#8849; A, A &#8849; C</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_2 implements NormalizationRule {

	private IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer of rule NR-1.2.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR1_2(IntegerOntologyObjectFactory factory) {
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
		if (axiom instanceof IntegerPropertyRangeAxiom) {
			ret = applyRule((IntegerPropertyRangeAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(IntegerPropertyRangeAxiom rangeAxiom) {
		Set<IntegerAxiom> ret = Collections.emptySet();
		if (!rangeAxiom.getRange().isLiteral()) {
			ret = new HashSet<IntegerAxiom>();
			Integer newClassId = getOntologyObjectFactory().getIdGenerator()
					.createNewClassId();
			ret.add(getOntologyObjectFactory().getNormalizedAxiomFactory()
					.createRangeAxiom(rangeAxiom.getProperty(), newClassId));
			ret.add(getOntologyObjectFactory().getComplexAxiomFactory()
					.createSubClassOfAxiom(
							getOntologyObjectFactory().getDataTypeFactory()
									.createClass(newClassId),
							rangeAxiom.getRange()));
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
