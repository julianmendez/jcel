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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class is a factory to create any complex axiom.
 * 
 * @author Julian Mendez
 */
public class ComplexIntegerAxiomFactoryImpl implements ComplexIntegerAxiomFactory {

	/**
	 * Constructs a new complex axiom factory.
	 */
	public ComplexIntegerAxiomFactoryImpl() {
	}

	@Override
	public IntegerClassAssertionAxiom createClassAssertionAxiom(IntegerClassExpression classExpr, int individualId,
			Set<IntegerAnnotation> annotations) {
		return new IntegerClassAssertionAxiom(classExpr, individualId, annotations);
	}

	@Override
	public IntegerClassDeclarationAxiom createClassDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations) {
		return new IntegerClassDeclarationAxiom(declaredEntity, annotations);
	}

	@Override
	public IntegerDataPropertyAssertionAxiom createDataPropertyAssertionAxiom(int objectProp, int subjectInd,
			int objectInd, Set<IntegerAnnotation> annotations) {
		return new IntegerDataPropertyAssertionAxiom(objectProp, subjectInd, objectInd, annotations);
	}

	@Override
	public IntegerDataPropertyDeclarationAxiom createDataPropertyDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations) {
		return new IntegerDataPropertyDeclarationAxiom(declaredEntity, annotations);
	}

	@Override
	public IntegerDifferentIndividualsAxiom createDifferentIndividualsAxiom(Set<Integer> individualSet,
			Set<IntegerAnnotation> annotations) {
		return new IntegerDifferentIndividualsAxiom(individualSet, annotations);
	}

	@Override
	public IntegerDisjointClassesAxiom createDisjointClassesAxiom(Set<IntegerClassExpression> descSet,
			Set<IntegerAnnotation> annotations) {
		return new IntegerDisjointClassesAxiom(descSet, annotations);
	}

	@Override
	public IntegerEquivalentClassesAxiom createEquivalentClassesAxiom(Set<IntegerClassExpression> descSet,
			Set<IntegerAnnotation> annotations) {
		return new IntegerEquivalentClassesAxiom(descSet, annotations);
	}

	@Override
	public IntegerEquivalentObjectPropertiesAxiom createEquivalentObjectPropertiesAxiom(
			Set<IntegerObjectPropertyExpression> propSet, Set<IntegerAnnotation> annotations) {
		return new IntegerEquivalentObjectPropertiesAxiom(propSet, annotations);
	}

	@Override
	public IntegerFunctionalObjectPropertyAxiom createFunctionalObjectPropertyAxiom(
			IntegerObjectPropertyExpression property, Set<IntegerAnnotation> annotations) {
		return new IntegerFunctionalObjectPropertyAxiom(property, annotations);
	}

	@Override
	public IntegerInverseFunctionalObjectPropertyAxiom createInverseFunctionalObjectPropertyAxiom(
			IntegerObjectPropertyExpression property, Set<IntegerAnnotation> annotations) {
		return new IntegerInverseFunctionalObjectPropertyAxiom(property, annotations);
	}

	@Override
	public IntegerInverseObjectPropertiesAxiom createInverseObjectPropertiesAxiom(IntegerObjectPropertyExpression first,
			IntegerObjectPropertyExpression second, Set<IntegerAnnotation> annotations) {
		return new IntegerInverseObjectPropertiesAxiom(first, second, annotations);
	}

	@Override
	public IntegerNamedIndividualDeclarationAxiom createNamedIndividualDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations) {
		return new IntegerNamedIndividualDeclarationAxiom(declaredEntity, annotations);
	}

	@Override
	public IntegerNegativeObjectPropertyAssertionAxiom createNegativeObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, int subjectInd, int objectInd,
			Set<IntegerAnnotation> annotations) {
		return new IntegerNegativeObjectPropertyAssertionAxiom(objectProp, subjectInd, objectInd, annotations);
	}

	@Override
	public IntegerObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, int subjectInd, int objectInd,
			Set<IntegerAnnotation> annotations) {
		return new IntegerObjectPropertyAssertionAxiom(objectProp, subjectInd, objectInd, annotations);
	}

	@Override
	public IntegerObjectPropertyDeclarationAxiom createObjectPropertyDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations) {
		return new IntegerObjectPropertyDeclarationAxiom(declaredEntity, annotations);
	}

	@Override
	public IntegerPropertyRangeAxiom createPropertyRangeAxiom(IntegerObjectPropertyExpression prop,
			IntegerClassExpression clExpr, Set<IntegerAnnotation> annotations) {
		return new IntegerPropertyRangeAxiom(prop, clExpr, annotations);
	}

	@Override
	public IntegerReflexiveObjectPropertyAxiom createReflexiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression property, Set<IntegerAnnotation> annotations) {
		return new IntegerReflexiveObjectPropertyAxiom(property, annotations);
	}

	@Override
	public IntegerSameIndividualAxiom createSameIndividualAxiom(Set<Integer> individualSet,
			Set<IntegerAnnotation> annotations) {
		return new IntegerSameIndividualAxiom(individualSet, annotations);
	}

	@Override
	public IntegerSubClassOfAxiom createSubClassOfAxiom(IntegerClassExpression subClExpr,
			IntegerClassExpression superClExpr, Set<IntegerAnnotation> annotations) {
		return new IntegerSubClassOfAxiom(subClExpr, superClExpr, annotations);
	}

	@Override
	public IntegerSubObjectPropertyOfAxiom createSubObjectPropertyOfAxiom(IntegerObjectPropertyExpression subPropExpr,
			IntegerObjectPropertyExpression superPropExpr, Set<IntegerAnnotation> annotations) {
		return new IntegerSubObjectPropertyOfAxiom(subPropExpr, superPropExpr, annotations);
	}

	@Override
	public IntegerSubPropertyChainOfAxiom createSubPropertyChainOfAxiom(List<IntegerObjectPropertyExpression> chain,
			IntegerObjectPropertyExpression superProp, Set<IntegerAnnotation> annotations) {
		return new IntegerSubPropertyChainOfAxiom(chain, superProp, annotations);
	}

	@Override
	public IntegerTransitiveObjectPropertyAxiom createTransitiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression prop, Set<IntegerAnnotation> annotations) {
		return new IntegerTransitiveObjectPropertyAxiom(prop, annotations);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ComplexIntegerAxiomFactoryImpl);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
