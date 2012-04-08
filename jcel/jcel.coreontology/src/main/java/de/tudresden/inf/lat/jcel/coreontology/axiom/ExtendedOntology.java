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
	public void addClass(int classId);

	/**
	 * Adds an object property to the ontology.
	 * 
	 * @param objectPropertyId
	 *            object property to be added
	 */
	public void addObjectProperty(int objectPropertyId);

	/**
	 * Clears the ontology.
	 */
	public void clear();

	/**
	 * Returns the set of all classes in the ontology.
	 * 
	 * @return the set of all classes in the ontology
	 */
	public Set<Integer> getClassSet();

	/**
	 * Returns the set of all functional object properties in the ontology.
	 * 
	 * @return the set of all functional object properties in the ontology
	 */
	public Set<Integer> getFunctionalObjectProperties();

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
	public Set<GCI0Axiom> getGCI0Axioms(int classId);

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
	public Set<GCI1Axiom> getGCI1Axioms(int classId);

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
	public Set<GCI2Axiom> getGCI2Axioms(int classId);

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
	public Set<GCI3Axiom> getGCI3AAxioms(int classId);

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
	public Set<GCI3Axiom> getGCI3rAAxioms(int objectPropertyId, int leftClassId);

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
	public Set<GCI3Axiom> getGCI3rAxioms(int objectPropertyId);

	/**
	 * Returns the set of all object properties in the ontology.
	 * 
	 * @return the set of all object properties in the ontology
	 */
	public Set<Integer> getObjectPropertySet();

	/**
	 * Returns the set of all reflexive object properties in the ontology.
	 * 
	 * @return the set of all reflexive object properties in the ontology
	 */
	public Set<Integer> getReflexiveObjectProperties();

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
	public Set<RI2Axiom> getRI2rAxioms(int objectPropertyId);

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
	public Set<RI2Axiom> getRI2sAxioms(int objectPropertyId);

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
	public Set<RI3Axiom> getRI3AxiomsByLeft(int objectPropertyId);

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
	public Set<RI3Axiom> getRI3AxiomsByRight(int objectPropertyId);

	/**
	 * Returns the set of all transitive object properties in the ontology.
	 * 
	 * @return the set of all transitive object properties in the ontology
	 */
	public Set<Integer> getTransitiveObjectProperties();

	/**
	 * Loads a set of normalized axioms.
	 * 
	 * @param axiomSet
	 *            set of normalized axioms to be loaded
	 */
	public void load(Set<NormalizedIntegerAxiom> axiomSet);

}
