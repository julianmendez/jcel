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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.Set;

/**
 * This is the interface of an extended ontology provides methods to efficiently
 * retrieve axioms from the ontology.
 * 
 * @author Julian Mendez
 */
public interface ExtendedOntology {

	/**
	 * Adds a class to the ontology.
	 * 
	 * @param classId
	 *            class to be added
	 */
	void addClass(int classId);

	/**
	 * Adds an object property to the ontology.
	 * 
	 * @param objectPropertyId
	 *            object property to be added
	 */
	void addObjectProperty(int objectPropertyId);

	/**
	 * Clears the ontology.
	 */
	void clear();

	/**
	 * Returns the set of all classes in the ontology.
	 * 
	 * @return the set of all classes in the ontology
	 */
	Set<Integer> getClassSet();

	/**
	 * Returns the set of all functional object properties in the ontology.
	 * 
	 * @return the set of all functional object properties in the ontology
	 */
	Set<Integer> getFunctionalObjectProperties();

	/**
	 * Returns the set of all axioms GCI0 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI0 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
	Set<GCI0Axiom> getGCI0Axioms(int classId);

	/**
	 * Returns the set of all axioms GCI1 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI1 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
	Set<GCI1Axiom> getGCI1Axioms(int classId);

	/**
	 * Returns the set of all axioms GCI2 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI2 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
	Set<GCI2Axiom> getGCI2Axioms(int classId);

	/**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * class occurs in the left part of the axiom.
	 * 
	 * @param classId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         class occurs in the left part of the axiom
	 */
	Set<GCI3Axiom> getGCI3AAxioms(int classId);

	/**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * object property and the given class occur in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            axiom
	 * @param leftClassId
	 *            class identifier occurring in the left part of the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         object property and the given class occur in the left part of the
	 *         axiom
	 */
	Set<GCI3Axiom> getGCI3rAAxioms(int objectPropertyId, int leftClassId);

	/**
	 * Returns the set of all axioms GCI3 in the ontology such that the given
	 * object property occurs in the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the axiom
	 * 
	 * @return the set of all axioms GCI3 in the ontology such that the given
	 *         object property occurs in the axiom
	 */
	Set<GCI3Axiom> getGCI3rAxioms(int objectPropertyId);

	/**
	 * Returns the set of all object properties in the ontology.
	 * 
	 * @return the set of all object properties in the ontology
	 */
	Set<Integer> getObjectPropertySet();

	/**
	 * Returns the set of all reflexive object properties in the ontology.
	 * 
	 * @return the set of all reflexive object properties in the ontology
	 */
	Set<Integer> getReflexiveObjectProperties();

	/**
	 * Returns the set of all axioms RI2 in the ontology such that the given
	 * object property occurs in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            axiom
	 * 
	 * @return the set of all axioms RI2 in the ontology such that the given
	 *         object property occurs in the left part of the axiom
	 */
	Set<RI2Axiom> getRI2rAxioms(int objectPropertyId);

	/**
	 * Returns the set of all axioms RI2 in the ontology such that the given
	 * object property occurs in the right part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the right part of the
	 *            axiom
	 * 
	 * @return the set of all axioms RI2 in the ontology such that the given
	 *         object property occurs in the right part of the axiom
	 */
	Set<RI2Axiom> getRI2sAxioms(int objectPropertyId);

	/**
	 * Returns the set of all axioms RI3 in the ontology such that the given
	 * object property occurs in the left part of the object property
	 * composition, in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the left part of the
	 *            composition, in the left part of the axiom
	 * 
	 * @return the set of all axioms RI3 in the ontology such that the given
	 *         object property occurs in the left part of the object property
	 *         composition, in the left part of the axiom
	 */
	Set<RI3Axiom> getRI3AxiomsByLeft(int objectPropertyId);

	/**
	 * Returns the set of all axioms RI3 in the ontology such that the given
	 * object property occurs in the right part of the object property
	 * composition, in the left part of the axiom.
	 * 
	 * @param objectPropertyId
	 *            object property identifier occurring in the right part of the
	 *            composition, in the left part of the axiom
	 * 
	 * @return the set of all axioms RI3 in the ontology such that the given
	 *         object property occurs in the right part of the object property
	 *         composition, in the left part of the axiom
	 */
	Set<RI3Axiom> getRI3AxiomsByRight(int objectPropertyId);

	/**
	 * Returns the set of all transitive object properties in the ontology.
	 * 
	 * @return the set of all transitive object properties in the ontology
	 */
	Set<Integer> getTransitiveObjectProperties();

	/**
	 * Loads a set of normalized axioms.
	 * 
	 * @param axiomSet
	 *            set of normalized axioms to be loaded
	 */
	void load(Set<NormalizedIntegerAxiom> axiomSet);

}
