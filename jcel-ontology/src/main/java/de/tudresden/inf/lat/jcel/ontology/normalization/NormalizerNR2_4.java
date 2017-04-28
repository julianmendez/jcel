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
import java.util.Optional;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityType;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;

/**
 * 
 * <ul>
 * <li>NR-2.4 : B \u2293 C' \u2291 D \u219D C' \u2291 A, B \u2293 A \u2291 D
 * </li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
public class NormalizerNR2_4 implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer rule NR-2.4.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR2_4(IntegerOntologyObjectFactory factory) {
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
		if (!subClass.isLiteral()) {
			IntegerClassExpression superClass = classAxiom.getSuperClass();
			if (subClass instanceof IntegerObjectIntersectionOf) {
				IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
				Set<IntegerClassExpression> operands = intersection.getOperands();
				if (operands.size() > 2) {
					Set<IntegerAxiom> newSet = applyRule(operands, superClass, classAxiom.getAnnotations());
					if (newSet.size() > 1) {
						ret = newSet;
					}
				}

			}
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(Set<IntegerClassExpression> operands, IntegerClassExpression superClass,
			Set<IntegerAnnotation> annotations) {
		Set<IntegerAxiom> ret = new HashSet<>();
		Optional<IntegerClassExpression> optional = operands.stream().filter(op -> op.isLiteral()).findAny();

		if (optional.isPresent()) {
			IntegerClassExpression aLiteral = optional.get();
			IntegerClass newClass = getOntologyObjectFactory().getDataTypeFactory().createClass(
					getOntologyObjectFactory().getEntityManager().createAnonymousEntity(IntegerEntityType.CLASS, true));

			Set<IntegerClassExpression> newOperands = new HashSet<>();
			newOperands.addAll(operands);
			newOperands.remove(aLiteral);
			IntegerObjectIntersectionOf newIntersection = getOntologyObjectFactory().getDataTypeFactory()
					.createObjectIntersectionOf(newOperands);
			ret.add(getOntologyObjectFactory().getComplexAxiomFactory().createSubClassOfAxiom(newIntersection, newClass,
					annotations));

			Set<IntegerClassExpression> pairOfLiterals = new HashSet<>();
			pairOfLiterals.add(aLiteral);
			pairOfLiterals.add(newClass);
			IntegerObjectIntersectionOf intersectionOfLiterals = getOntologyObjectFactory().getDataTypeFactory()
					.createObjectIntersectionOf(pairOfLiterals);
			ret.add(getOntologyObjectFactory().getComplexAxiomFactory().createSubClassOfAxiom(intersectionOfLiterals,
					superClass, annotations));
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}
