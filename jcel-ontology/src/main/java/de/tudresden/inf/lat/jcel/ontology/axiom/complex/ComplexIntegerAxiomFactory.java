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
 * An object this interface is a factory to create complex axioms.
 *
 * @author Julian Mendez
 */
public interface ComplexIntegerAxiomFactory {

	/**
	 * Constructs a new class assertion axiom.
	 *
	 * @param classExpr
	 *            class expression of the assertion
	 * @param individualId
	 *            individual of the assertion
	 * @param annotations
	 *            annotations
	 * @return a new class assertion axiom
	 */
	public IntegerClassAssertionAxiom createClassAssertionAxiom(IntegerClassExpression classExpr, int individualId,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new class declaration axiom.
	 *
	 * @param declaredEntity
	 *            class identifier
	 * @param annotations
	 *            annotations
	 * @return a new class declaration axiom
	 */
	public IntegerClassDeclarationAxiom createClassDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new data property assertion axiom.
	 *
	 * @param objectProp
	 *            data property
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 * @param annotations
	 *            annotations
	 * @return a new data property assertion axiom
	 */
	public IntegerDataPropertyAssertionAxiom createDataPropertyAssertionAxiom(int objectProp, int subjectInd,
			int objectInd, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new data property declaration axiom.
	 *
	 * @param declaredEntity
	 *            data property
	 * @param annotations
	 *            annotations
	 * @return a new data property declaration axiom
	 */
	public IntegerDataPropertyDeclarationAxiom createDataPropertyDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new different individuals axiom
	 *
	 * @param individualSet
	 *            set of individuals declared to be different
	 * @param annotations
	 *            annotations
	 * @return a new different individuals axiom
	 */
	public IntegerDifferentIndividualsAxiom createDifferentIndividualsAxiom(Set<Integer> individualSet,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new disjoint classes axiom.
	 *
	 * @param descSet
	 *            set of classes declared to be disjoint
	 * @param annotations
	 *            annotations
	 * @return a new disjoint classes axiom
	 */
	public IntegerDisjointClassesAxiom createDisjointClassesAxiom(Set<IntegerClassExpression> descSet,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new equivalent classes axiom.
	 *
	 * @param descSet
	 *            set of classes declared to be equivalent
	 * @param annotations
	 *            annotations
	 * @return a new equivalent classes axiom
	 */
	public IntegerEquivalentClassesAxiom createEquivalentClassesAxiom(Set<IntegerClassExpression> descSet,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs an equivalent object properties axiom.
	 *
	 * @param propSet
	 *            set of object properties declared to be equivalent
	 * @param annotations
	 *            annotations
	 * @return an equivalent object properties axiom
	 */
	public IntegerEquivalentObjectPropertiesAxiom createEquivalentObjectPropertiesAxiom(
			Set<IntegerObjectPropertyExpression> propSet, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new functional object property axiom.
	 *
	 * @param property
	 *            object property declared functional
	 * @param annotations
	 *            annotations
	 * @return a new functional object property axiom
	 */
	public IntegerFunctionalObjectPropertyAxiom createFunctionalObjectPropertyAxiom(
			IntegerObjectPropertyExpression property, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new inverse functional object property axiom.
	 *
	 * @param property
	 *            object property which inverse is declared functional
	 * @param annotations
	 *            annotations
	 * @return a new inverse functional object property axiom
	 */
	public IntegerInverseFunctionalObjectPropertyAxiom createInverseFunctionalObjectPropertyAxiom(
			IntegerObjectPropertyExpression property, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new inverse object property axiom, declaring that one object
	 * property is the inverse of another one.
	 *
	 * @param first
	 *            object property
	 * @param second
	 *            object property
	 * @param annotations
	 *            annotations
	 * @return a new inverse object property axiom
	 */
	public IntegerInverseObjectPropertiesAxiom createInverseObjectPropertiesAxiom(IntegerObjectPropertyExpression first,
			IntegerObjectPropertyExpression second, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new named individual declaration axiom.
	 *
	 * @param declaredEntity
	 *            named individual
	 * @param annotations
	 *            annotations
	 * @return a new named individual declaration axiom
	 */
	public IntegerNamedIndividualDeclarationAxiom createNamedIndividualDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new negative object property axiom.
	 *
	 * @param objectProp
	 *            object property expression
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 * @param annotations
	 *            annotations
	 * @return a new negative object property axiom
	 */
	public IntegerNegativeObjectPropertyAssertionAxiom createNegativeObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, int subjectInd, int objectInd,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new object property assertion axiom.
	 *
	 * @param objectProp
	 *            object property
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 * @param annotations
	 *            annotations
	 * @return a new object property assertion axiom
	 */
	public IntegerObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, int subjectInd, int objectInd,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new object property declaration axiom.
	 *
	 * @param declaredEntity
	 *            object property
	 * @param annotations
	 *            annotations
	 * @return a new object property declaration axiom
	 */
	public IntegerObjectPropertyDeclarationAxiom createObjectPropertyDeclarationAxiom(int declaredEntity,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new object property range axiom.
	 *
	 * @param prop
	 *            object property
	 * @param clExpr
	 *            class expression
	 * @param annotations
	 *            annotations
	 * @return a new object property range axiom
	 */
	public IntegerPropertyRangeAxiom createPropertyRangeAxiom(IntegerObjectPropertyExpression prop,
			IntegerClassExpression clExpr, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new reflexive object property axiom.
	 *
	 * @param property
	 *            object property
	 * @param annotations
	 *            annotations
	 * @return a new reflexive object property axiom
	 */
	public IntegerReflexiveObjectPropertyAxiom createReflexiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression property, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new same individual axiom.
	 *
	 * @param individualSet
	 *            set of individuals declared to be the same
	 * @param annotations
	 *            annotations
	 * @return a new same individual axiom
	 */
	public IntegerSameIndividualAxiom createSameIndividualAxiom(Set<Integer> individualSet,
			Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new subclass axiom.
	 *
	 * @param subClExpr
	 *            subclass
	 * @param superClExpr
	 *            superclass
	 * @param annotations
	 *            annotations
	 * @return a new subclass axiom
	 */
	public IntegerSubClassOfAxiom createSubClassOfAxiom(IntegerClassExpression subClExpr,
			IntegerClassExpression superClExpr, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new sub object property axiom.
	 *
	 * @param subPropExpr
	 *            sub object property
	 * @param superPropExpr
	 *            super object property
	 * @param annotations
	 *            annotations
	 * @return a new sub object property axiom
	 */
	public IntegerSubObjectPropertyOfAxiom createSubObjectPropertyOfAxiom(IntegerObjectPropertyExpression subPropExpr,
			IntegerObjectPropertyExpression superPropExpr, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new sub object property chain axiom.
	 *
	 * @param chain
	 *            list of object property expressions in the chain
	 * @param superProp
	 *            super object property expression
	 * @param annotations
	 *            annotations
	 * @return a new sub object property chain axiom
	 */
	public IntegerSubPropertyChainOfAxiom createSubPropertyChainOfAxiom(List<IntegerObjectPropertyExpression> chain,
			IntegerObjectPropertyExpression superProp, Set<IntegerAnnotation> annotations);

	/**
	 * Constructs a new transitive object property axiom.
	 *
	 * @param prop
	 *            object property
	 * @param annotations
	 *            annotations
	 * @return a new transitive object property axiom
	 */
	public IntegerTransitiveObjectPropertyAxiom createTransitiveObjectPropertyAxiom(
			IntegerObjectPropertyExpression prop, Set<IntegerAnnotation> annotations);

}
