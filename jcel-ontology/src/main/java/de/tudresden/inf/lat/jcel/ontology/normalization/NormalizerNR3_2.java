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

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * 
 * <ul>
 * <li>NR-3.2 : B \u2291 &exist; r <i>.</i> C' \u219D B \u2291 &exist; r
 * <i>.</i> A, A \u2291 C'</li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
public class NormalizerNR3_2 implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer rule NR-3.2.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR3_2(IntegerOntologyObjectFactory factory) {
		Objects.requireNonNull(factory);
		this.ontologyObjectFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		Objects.requireNonNull(axiom);
		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubClassOfAxiom) {
			ret = applyRule((IntegerSubClassOfAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(IntegerSubClassOfAxiom classAxiom) {
		Set<IntegerAxiom> ret = Collections.emptySet();
		IntegerClassExpression subClass = classAxiom.getSubClass();
		IntegerClassExpression superClass = classAxiom.getSuperClass();
		if (subClass.isLiteral() && (superClass instanceof IntegerObjectSomeValuesFrom)) {
			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) superClass;
			IntegerObjectPropertyExpression propertyExpression = restriction.getProperty();
			IntegerClassExpression filler = restriction.getFiller();
			if (!filler.isLiteral()) {
				ret = new HashSet<>();
				IntegerClass newClass = getOntologyObjectFactory().getDataTypeFactory()
						.createClass(getOntologyObjectFactory().getEntityManager()
								.createAnonymousEntity(IntegerEntityType.CLASS, true));
				IntegerObjectSomeValuesFrom newExistential = getOntologyObjectFactory().getDataTypeFactory()
						.createObjectSomeValuesFrom(propertyExpression, newClass);
				ret.add(getOntologyObjectFactory().getComplexAxiomFactory().createSubClassOfAxiom(subClass,
						newExistential, classAxiom.getAnnotations()));
				ret.add(getOntologyObjectFactory().getComplexAxiomFactory().createSubClassOfAxiom(newClass, filler,
						classAxiom.getAnnotations()));
			}
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
