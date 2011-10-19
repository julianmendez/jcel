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

	/**
	 * Constructs a new class assertion axiom.
	 * 
	 * @param classExpr
	 *            class expression of the assertion
	 * @param individualId
	 *            individual of the assertion
	 */
	public IntegerClassAssertionAxiom createClassAssertionAxiom(
			IntegerClassExpression classExpr, Integer individualId) {
		return new IntegerClassAssertionAxiom(classExpr, individualId);
	}

	/**
	 * Constructs a new class declaration axiom.
	 * 
	 * @param declaredEntity
	 *            class identifier
	 */
	public IntegerClassDeclarationAxiom createClassDeclarationAxiom(
			Integer declaredEntity) {
		return new IntegerClassDeclarationAxiom(declaredEntity);
	}

	/**
	 * Constructs a new data property assertion axiom.
	 * 
	 * @param objectProp
	 *            data property
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 */
	public IntegerDataPropertyAssertionAxiom createDataPropertyAssertionAxiom(
			Integer objectProp, Integer subjectInd, Integer objectInd) {
		return new IntegerDataPropertyAssertionAxiom(objectProp, subjectInd,
				objectInd);
	}

	/**
	 * Constructs a new data property declaration axiom.
	 * 
	 * @param declaredEntity
	 *            data property
	 */
	public IntegerDataPropertyDeclarationAxiom createDataPropertyDeclarationAxiom(
			Integer declaredEntity) {
		return new IntegerDataPropertyDeclarationAxiom(declaredEntity);
	}

	/**
	 * Constructs a new different individuals axiom
	 * 
	 * @param individualSet
	 *            set of individuals declared to be different
	 */
	public IntegerDifferentIndividualsAxiom createDifferentIndividualsAxiom(
			Set<Integer> individualSet) {
		return new IntegerDifferentIndividualsAxiom(individualSet);
	}

	/**
	 * Constructs a new disjoint classes axiom.
	 * 
	 * @param descSet
	 *            set of classes declared to be disjoint
	 */
	public IntegerDisjointClassesAxiom createDisjointClassesAxiom(
			Set<IntegerClassExpression> descSet) {
		return new IntegerDisjointClassesAxiom(descSet);
	}

	/**
	 * Constructs a new equivalent classes axiom.
	 * 
	 * @param descSet
	 *            set of classes declared to be equivalent
	 */
	public IntegerEquivalentClassesAxiom createEquivalentClassesAxiom(
			Set<IntegerClassExpression> descSet) {
		return new IntegerEquivalentClassesAxiom(descSet);
	}

	/**
	 * Constructs an equivalent object properties axiom.
	 * 
	 * @param propSet
	 *            set of object properties declared to be equivalent
	 */
	public IntegerEquivalentObjectPropertiesAxiom createEquivalentObjectPropertiesAxiom(
			Set<Integer> propSet) {
		return new IntegerEquivalentObjectPropertiesAxiom(propSet);
	}

	/**
	 * Constructs a new functional object property axiom.
	 * 
	 * @param property
	 *            object property declared functional
	 */
	public IntegerFunctionalObjectPropertyAxiom createFunctionalObjectPropertyAxiom(
			Integer property) {
		return new IntegerFunctionalObjectPropertyAxiom(property);
	}

	/**
	 * Constructs a new inverse functional object property axiom.
	 * 
	 * @param property
	 *            object property which inverse is declared functional
	 */
	public IntegerInverseFunctionalObjectPropertyAxiom createInverseFunctionalObjectPropertyAxiom(
			Integer property) {
		return new IntegerInverseFunctionalObjectPropertyAxiom(property);
	}

	/**
	 * Constructs a new inverse object property axiom, declaring that one object
	 * property is the inverse of another one.
	 * 
	 * @param first
	 *            object property
	 * @param second
	 *            object property
	 */
	public IntegerInverseObjectPropertiesAxiom createInverseObjectPropertiesAxiom(
			Integer first, Integer second) {
		return new IntegerInverseObjectPropertiesAxiom(first, second);
	}

	/**
	 * Constructs a new named individual declaration axiom.
	 * 
	 * @param declaredEntity
	 *            named individual
	 */
	public IntegerNamedIndividualDeclarationAxiom createNamedIndividualDeclarationAxiom(
			Integer declaredEntity) {
		return new IntegerNamedIndividualDeclarationAxiom(declaredEntity);
	}

	/**
	 * Constructs a new negative object property axiom.
	 * 
	 * @param objectProp
	 *            object property expression
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 */
	public IntegerNegativeObjectPropertyAssertionAxiom createNegativeObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, Integer subjectInd,
			Integer objectInd) {
		return new IntegerNegativeObjectPropertyAssertionAxiom(objectProp,
				subjectInd, objectInd);
	}

	/**
	 * Constructs a new object property assertion axiom.
	 * 
	 * @param objectProp
	 *            object property
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 */
	public IntegerObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(
			IntegerObjectPropertyExpression objectProp, Integer subjectInd,
			Integer objectInd) {
		return new IntegerObjectPropertyAssertionAxiom(objectProp, subjectInd,
				objectInd);
	}

	/**
	 * Constructs a new object property declaration axiom.
	 * 
	 * @param declaredEntity
	 *            object property
	 */
	public IntegerObjectPropertyDeclarationAxiom createObjectPropertyDeclarationAxiom(
			Integer declaredEntity) {
		return new IntegerObjectPropertyDeclarationAxiom(declaredEntity);
	}

	/**
	 * Constructs a new object property range axiom.
	 * 
	 * @param prop
	 *            object property
	 * @param clExpr
	 *            class expression
	 */
	public IntegerPropertyRangeAxiom createPropertyRangeAxiom(Integer prop,
			IntegerClassExpression clExpr) {
		return new IntegerPropertyRangeAxiom(prop, clExpr);
	}

	/**
	 * Constructs a new reflexive object property axiom.
	 * 
	 * @param property
	 *            object property
	 */
	public IntegerReflexiveObjectPropertyAxiom createReflexiveObjectPropertyAxiom(
			Integer property) {
		return new IntegerReflexiveObjectPropertyAxiom(property);
	}

	/**
	 * Constructs a new same individual axiom.
	 * 
	 * @param individualSet
	 *            set of individuals declared to be the same
	 */
	public IntegerSameIndividualAxiom createSameIndividualAxiom(
			Set<Integer> individualSet) {
		return new IntegerSameIndividualAxiom(individualSet);
	}

	/**
	 * Constructs a new subclass axiom.
	 * 
	 * @param subClExpr
	 *            subclass
	 * @param superClExpr
	 *            superclass
	 */
	public IntegerSubClassOfAxiom createSubClassOfAxiom(
			IntegerClassExpression subClExpr, IntegerClassExpression superClExpr) {
		return new IntegerSubClassOfAxiom(subClExpr, superClExpr);
	}

	/**
	 * Constructs a new sub object property axiom.
	 * 
	 * @param subPropExpr
	 *            sub object property
	 * @param superPropExpr
	 *            super object property
	 */
	public IntegerSubObjectPropertyOfAxiom createSubObjectPropertyOfAxiom(
			IntegerObjectPropertyExpression subPropExpr,
			IntegerObjectPropertyExpression superPropExpr) {
		return new IntegerSubObjectPropertyOfAxiom(subPropExpr, superPropExpr);
	}

	/**
	 * Constructs a new sub object property chain axiom.
	 * 
	 * @param chain
	 *            list of object property expressions in the chain
	 * @param superProp
	 *            super object property expression
	 */
	public IntegerSubPropertyChainOfAxiom createSubPropertyChainOfAxiom(
			List<IntegerObjectPropertyExpression> chain,
			IntegerObjectPropertyExpression superProp) {
		return new IntegerSubPropertyChainOfAxiom(chain, superProp);
	}

	/**
	 * Constructs a new transitive object property axiom.
	 * 
	 * @param prop
	 *            object property
	 */
	public IntegerTransitiveObjectPropertyAxiom createTransitiveObjectPropertyAxiom(
			Integer prop) {
		return new IntegerTransitiveObjectPropertyAxiom(prop);
	}

}
