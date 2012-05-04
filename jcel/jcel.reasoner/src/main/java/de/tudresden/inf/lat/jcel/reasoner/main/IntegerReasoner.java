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

package de.tudresden.inf.lat.jcel.reasoner.main;

import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This interface models a reasoner.
 * 
 * @author Julian Mendez
 */
public interface IntegerReasoner {

	/**
	 * Performs classification only if it is needed.
	 */
	public void classify();

	/**
	 * Disposes this reasoner.
	 */
	public void dispose();

	/**
	 * Applies changes to the ontology when the reasoner is buffered.
	 */
	public void flush();

	/**
	 * Returns the bottom class node.
	 * 
	 * @return the bottom class node
	 */
	public Set<IntegerClass> getBottomClassNode();

	/**
	 * Returns the bottom data property node.
	 * 
	 * @return the bottom data property node
	 */
	public Set<IntegerDataProperty> getBottomDataPropertyNode();

	/**
	 * Returns the bottom object property node.
	 * 
	 * @return the bottom object property node
	 */
	public Set<IntegerObjectPropertyExpression> getBottomObjectPropertyNode();

	/**
	 * Returns the classes that are domains of the specified data property.
	 * 
	 * @param pe
	 *            data property
	 * @param direct
	 *            <code>true</code> to get only the direct domains
	 * @return the classes that are domains of the specified data property
	 */
	public Set<Set<IntegerClass>> getDataPropertyDomains(
			IntegerDataProperty pe, boolean direct);

	/**
	 * Returns the data property values for the specified individual and data
	 * property.
	 * 
	 * @param ind
	 *            individual
	 * @param pe
	 *            data property
	 * @return the data property values for the specified individual and data
	 *         property
	 */
	public Set<IntegerClass> getDataPropertyValues(IntegerNamedIndividual ind,
			IntegerDataProperty pe);

	/**
	 * Returns the individuals that are known to be different from the specified
	 * individual.
	 * 
	 * @param ind
	 *            individual
	 * @return the individuals that are known to be different from the specified
	 *         individual
	 */
	public Set<Set<IntegerNamedIndividual>> getDifferentIndividuals(
			IntegerNamedIndividual ind);

	/**
	 * Returns the classes that are disjoint with the specified class
	 * expression.
	 * 
	 * @param ce
	 *            class expression
	 * @return the classes that are disjoint with the specified class expression
	 */
	public Set<Set<IntegerClass>> getDisjointClasses(IntegerClassExpression ce);

	/**
	 * Returns the data properties that are disjoint with the specified data
	 * property expression.
	 * 
	 * @param pe
	 *            data property expression
	 * @return the data properties that are disjoint with the specified data
	 *         property expression
	 */
	public Set<Set<IntegerDataProperty>> getDisjointDataProperties(
			IntegerDataPropertyExpression pe);

	/**
	 * Returns the object properties that are disjoint with the specified object
	 * property expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @return the object properties that are disjoint with the specified object
	 *         property expression
	 */
	public Set<Set<IntegerObjectPropertyExpression>> getDisjointObjectProperties(
			IntegerObjectPropertyExpression pe);

	/**
	 * Returns the classes that are equivalent to the specified class
	 * expression.
	 * 
	 * @param ce
	 *            class expression
	 * @return the classes that are equivalent to the specified class expression
	 */
	public Set<IntegerClass> getEquivalentClasses(IntegerClassExpression ce);

	/**
	 * Returns the data properties that are equivalent to the specified data
	 * property expression.
	 * 
	 * @param pe
	 *            data property expression
	 * @return the data properties that are equivalent to the specified data
	 *         property expression
	 */
	public Set<IntegerDataProperty> getEquivalentDataProperties(
			IntegerDataProperty pe);

	/**
	 * Returns the object properties that are equivalent to the specified object
	 * property expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @return the object properties that are equivalent to the specified object
	 *         property expression
	 */
	public Set<IntegerObjectPropertyExpression> getEquivalentObjectProperties(
			IntegerObjectPropertyExpression pe);

	/**
	 * Returns the individuals that are instances of the specified class
	 * expression.
	 * 
	 * @param ce
	 *            class expression
	 * @param direct
	 *            <code>true</code> to get the direct instances
	 * @return the individuals that are instances of the specified class
	 *         expression
	 */
	public Set<Set<IntegerNamedIndividual>> getInstances(
			IntegerClassExpression ce, boolean direct);

	/**
	 * Returns the object property expressions that are the inverses of the
	 * specified object property expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @return the object property expressions that are the inverses of the
	 *         specified object property expression
	 */
	public Set<IntegerObjectPropertyExpression> getInverseObjectProperties(
			IntegerObjectPropertyExpression pe);

	/**
	 * Returns the classes that are domains of the specified object property
	 * expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @param direct
	 *            <code>true</code> to get only the direct domains
	 * @return the classes that are domains of the specified object property
	 *         expression
	 */
	public Set<Set<IntegerClass>> getObjectPropertyDomains(
			IntegerObjectPropertyExpression pe, boolean direct);

	/**
	 * Returns the classes that are ranges of the specified object property
	 * expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @param direct
	 *            <code>true</code> to get the direct ranges
	 * @return the classes that are ranges of the specified object property
	 *         expression
	 */
	public Set<Set<IntegerClass>> getObjectPropertyRanges(
			IntegerObjectPropertyExpression pe, boolean direct);

	/**
	 * Returns the object property values for the specified individual and
	 * object property expression
	 * 
	 * @param ind
	 *            individual
	 * @param pe
	 *            data property
	 * @return the object property values for the specified individual and
	 *         object property expression
	 */
	public Set<Set<IntegerNamedIndividual>> getObjectPropertyValues(
			IntegerNamedIndividual ind, IntegerObjectPropertyExpression pe);

	/**
	 * Returns the name of this reasoner.
	 * 
	 * @return the name of this reasoner
	 */
	public String getReasonerName();

	/**
	 * Returns the version of this reasoner.
	 * 
	 * @return the version of this reasoner
	 */
	public String getReasonerVersion();

	/**
	 * Returns the individuals that are equivalent to the specified individual.
	 * 
	 * @param ind
	 *            individual
	 * @return the individuals that are equivalent to the specified individual
	 */
	public Set<IntegerNamedIndividual> getSameIndividuals(
			IntegerNamedIndividual ind);

	/**
	 * Returns the sub classes of a specified class expression.
	 * 
	 * @param ce
	 *            class expression
	 * @param direct
	 *            <code>true</code> to get only the direct sub classes
	 * @return the sub classes of a specified class expression
	 */
	public Set<Set<IntegerClass>> getSubClasses(IntegerClassExpression ce,
			boolean direct);

	/**
	 * Returns the sub data properties of a specified data property.
	 * 
	 * @param pe
	 *            data property
	 * @param direct
	 *            <code>true</code> to get only the direct sub data properties
	 * @return the sub data properties of a specified data property
	 */
	public Set<Set<IntegerDataProperty>> getSubDataProperties(
			IntegerDataProperty pe, boolean direct);

	/**
	 * Returns the sub object properties of a specified object property
	 * expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @param direct
	 *            <code>true</code> to get only the direct sub object properties
	 * @return the sub object properties of a specified object property
	 *         expression
	 */
	public Set<Set<IntegerObjectPropertyExpression>> getSubObjectProperties(
			IntegerObjectPropertyExpression pe, boolean direct);

	/**
	 * Returns the super classes of a specified class expression.
	 * 
	 * @param ce
	 *            class expression
	 * @param direct
	 *            <code>true</code> to get only the direct super classes
	 * @return the super classes of a specified class expression
	 */
	public Set<Set<IntegerClass>> getSuperClasses(IntegerClassExpression ce,
			boolean direct);

	/**
	 * Returns the super data properties of a specified data property.
	 * 
	 * @param pe
	 *            data property
	 * @param direct
	 *            <code>true</code> to get only the direct super data properties
	 * @return the super data properties of a specified data property
	 */
	public Set<Set<IntegerDataProperty>> getSuperDataProperties(
			IntegerDataProperty pe, boolean direct);

	/**
	 * Returns the super object properties of a specified object property
	 * expression.
	 * 
	 * @param pe
	 *            object property expression
	 * @param direct
	 *            <code>true</code> to get only the direct super object
	 *            properties
	 * @return the super object properties of a specified object property
	 *         expression
	 */
	public Set<Set<IntegerObjectPropertyExpression>> getSuperObjectProperties(
			IntegerObjectPropertyExpression pe, boolean direct);

	/**
	 * Returns the time out.
	 * 
	 * @return the time out
	 */
	public long getTimeOut();

	/**
	 * Returns the top class node.
	 * 
	 * @return the top class node
	 */
	public Set<IntegerClass> getTopClassNode();

	/**
	 * Returns the top data property node.
	 * 
	 * @return the top data property node
	 */
	public Set<IntegerDataProperty> getTopDataPropertyNode();

	/**
	 * Returns the top object property node.
	 * 
	 * @return the top object property node
	 */
	public Set<IntegerObjectPropertyExpression> getTopObjectPropertyNode();

	/**
	 * Returns the types of a specified individual.
	 * 
	 * @param ind
	 *            individual
	 * @param direct
	 *            <code>true</code> to get only the direct types
	 * @return the types of a specified individual
	 */
	public Set<Set<IntegerClass>> getTypes(IntegerNamedIndividual ind,
			boolean direct);

	/**
	 * Returns the set of unsatisfiable classes.
	 * 
	 * @return the set of unsatisfiable classes
	 */
	public Set<IntegerClass> getUnsatisfiableClasses();

	/**
	 * Sends a request to the reasoner to stop its processing.
	 */
	public void interrupt();

	/**
	 * Tells whether the ontology is consistent.
	 * 
	 * @return <code>true</code> if and only if the ontology is consistent
	 */
	public boolean isConsistent();

	/**
	 * Tells whether the specified axiom is entailed by the ontology.
	 * 
	 * @param axiom
	 *            axiom to test entailment
	 * @return <code>true</code> if and only if the specified axiom is entailed
	 *         by the ontology
	 */
	public boolean isEntailed(ComplexIntegerAxiom axiom);

	/**
	 * Tells whether the specified set of axioms is entailed by the ontology.
	 * 
	 * @param axioms
	 *            set of axioms to test entailment
	 * @return <code>true</code> if and only if the specified set of axioms is
	 *         entailed by the ontology
	 */
	public boolean isEntailed(Set<ComplexIntegerAxiom> axioms);

	/**
	 * Tells whether the specified class expression is satisfiable.
	 * 
	 * @param classExpression
	 *            class expression
	 * @return <code>true</code> if and only if the specified class expression
	 *         is satisfiable
	 */
	public boolean isSatisfiable(IntegerClassExpression classExpression);

}
