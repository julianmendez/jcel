/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class is a factory to create any complex axiom.
 * 
 * @author Julian Mendez
 */
public class ComplexIntegerAxiomFactoryImpl implements
		ComplexIntegerAxiomFactory {

	/**
	 * Constructs a new complex axiom factory.
	 */
	public ComplexIntegerAxiomFactoryImpl() {
	}

	@Override
	public IntegerClassAssertionAxiom createClassAssertionAxiom(
			IntegerClassExpression classExpr, int individualId) {
		return new IntegerClassAssertionAxiom(classExpr, individualId);
	}

	@Override
	public IntegerClassDeclarationAxiom createClassDeclarationAxiom(
			int declaredEntity) {
		return new IntegerClassDeclarationAxiom(declaredEntity);
	}

	@Override
	public IntegerDataPropertyAssertionAxiom createDataPropertyAssertionAxiom(
			int objectProp, int subjectInd, int objectInd) {
		return new IntegerDataPropertyAssertionAxiom(objectProp, subjectInd,
				objectInd);
	}

	@Override
	public IntegerDataPropertyDeclarationAxiom createDataPropertyDeclarationAxiom(
			int declaredEntity) {
		return new IntegerDataPropertyDeclarationAxiom(declaredEntity);
	}

	@Override
	public IntegerDifferentIndividualsAxiom createDifferentIndividualsAxiom(
			Set<Integer> individualSet) {
		return new IntegerDifferentIndividualsAxiom(individualSet);
	}

	@Override
	public IntegerDisjointClassesAxiom createDisjointClassesAxiom(
			Set<IntegerClassExpression> descSet) {
		return new IntegerDisjointClassesAxiom(descSet);
	}

	@Override
	public IntegerEquivalentClassesAxiom createEquivalentClassesAxiom(
			Set<IntegerClassExpression> descSet) {
		return new IntegerEquivalentClassesAxiom(descSet);
	}

	@Override
	public IntegerEquivalentObjectPropertiesAxiom createEquivalentObjectPropertiesAxiom(
			Set<IntegerObjectPropertyExpression> propSet) {
		return new IntegerEquivalentObjectPropertiesAxiom(propSet);
	}

	@Override
	public IntegerFunctionalObjectPropertyAxiom createFunctionalObjectPropertyAxiom(
			IntegerObjectPropertyExpression property) {
		return new IntegerFunctionalObjectPropertyAxiom(property);
	}

	@Override
	public IntegerInverseFunctionalObjectPropertyAxiom createInverseFunctionalObjectPropertyAxiom(
			IntegerObjectPropertyExpression property) {
		return new IntegerInverseFunctionalObjectPropertyAxiom(property);
	}

	@Override
	public IntegerInverseObjectPropertiesAxiom createInverseObjectPropertiesAxiom(
			IntegerObjectPropertyExpression first,
			IntegerObjectPropertyExpression second) {
		return new IntegerInverseObjectPropertiesAxiom(first, second);
	}

	@Override
	public IntegerNamedIndividualDeclarationAxiom createNamedIndividualDeclarationAxiom(
			int declaredEntity) {
		return new IntegerNamedIndividualDeclarationAxiom(declaredEntity);
	}

	@Override
	public IntegerNegativeObjectPropertyAssertionAxiom createNegativeObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, int subjectInd,
			int objectInd) {
		return new IntegerNegativeObjectPropertyAssertionAxiom(objectProp,
				subjectInd, objectInd);
	}

	@Override
	public IntegerObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, int subjectInd,
			int objectInd) {
		return new IntegerObjectPropertyAssertionAxiom(objectProp, subjectInd,
				objectInd);
	}

	@Override
	public IntegerObjectPropertyDeclarationAxiom createObjectPropertyDeclarationAxiom(
			int declaredEntity) {
		return new IntegerObjectPropertyDeclarationAxiom(declaredEntity);
	}

	@Override
	public IntegerPropertyRangeAxiom createPropertyRangeAxiom(
			IntegerObjectPropertyExpression prop, IntegerClassExpression clExpr) {
		return new IntegerPropertyRangeAxiom(prop, clExpr);
	}

	@Override
	public IntegerReflexiveObjectPropertyAxiom createReflexiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression property) {
		return new IntegerReflexiveObjectPropertyAxiom(property);
	}

	@Override
	public IntegerSameIndividualAxiom createSameIndividualAxiom(
			Set<Integer> individualSet) {
		return new IntegerSameIndividualAxiom(individualSet);
	}

	@Override
	public IntegerSubClassOfAxiom createSubClassOfAxiom(
			IntegerClassExpression subClExpr, IntegerClassExpression superClExpr) {
		return new IntegerSubClassOfAxiom(subClExpr, superClExpr);
	}

	@Override
	public IntegerSubObjectPropertyOfAxiom createSubObjectPropertyOfAxiom(
			IntegerObjectPropertyExpression subPropExpr,
			IntegerObjectPropertyExpression superPropExpr) {
		return new IntegerSubObjectPropertyOfAxiom(subPropExpr, superPropExpr);
	}

	@Override
	public IntegerSubPropertyChainOfAxiom createSubPropertyChainOfAxiom(
			List<IntegerObjectPropertyExpression> chain,
			IntegerObjectPropertyExpression superProp) {
		return new IntegerSubPropertyChainOfAxiom(chain, superProp);
	}

	@Override
	public IntegerTransitiveObjectPropertyAxiom createTransitiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression prop) {
		return new IntegerTransitiveObjectPropertyAxiom(prop);
	}

}
