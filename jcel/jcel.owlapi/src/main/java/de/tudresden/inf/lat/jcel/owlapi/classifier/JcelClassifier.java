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

package de.tudresden.inf.lat.jcel.owlapi.classifier;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;

/**
 * This interface models what functionality a classifier must provide.
 * 
 * @author Julian Mendez
 */
public interface JcelClassifier {

	/**
	 * Returns the set of equivalent classes or individuals of a given entity.
	 * 
	 * @param entity
	 *            entity
	 * @return the set of equivalent classes or individuals of a given entity
	 */
	public Set<OWLEntity> getEquivalentClassesAndIndividuals(OWLEntity entity);

	/**
	 * Returns the set of equivalent object properties of a given object
	 * property expression.
	 * 
	 * @param propExpr
	 *            object property expression
	 * @return the set of equivalent object properties of a given object
	 *         property expression
	 */
	public Set<OWLObjectProperty> getEquivalentObjectProperties(
			OWLObjectPropertyExpression propExpr);

	/**
	 * Returns the reasoner configuration.
	 * 
	 * @return the reasoner configuration
	 */
	public OWLReasonerConfiguration getReasonerConfiguration();

	/**
	 * Returns the set of reflexive properties.
	 * 
	 * @return the set of reflexive properties.
	 */
	public Set<OWLObjectProperty> getReflexiveProperties();

	/**
	 * Returns a set of sets of equivalent sub classes of a given class
	 * expression.
	 * 
	 * @param entity
	 *            class expression
	 * @param direct
	 *            <code>true</code> to get only direct sub classes
	 * @return a set of sets of equivalent sub classes of a given class
	 *         expression
	 */
	public Set<Set<OWLEntity>> getSubClassesAndIndividuals(OWLEntity entity,
			boolean direct);

	/**
	 * Returns a set of sets of equivalent object properties of a given object
	 * property expression.
	 * 
	 * @param propExpr
	 *            object property expression
	 * @param direct
	 *            <code>true</code> to get only direct sub object properties
	 * @return a set of sets of equivalent object properties of a given object
	 *         property expression.
	 */
	public Set<Set<OWLObjectProperty>> getSubObjectProperties(
			OWLObjectPropertyExpression propExpr, boolean direct);

	/**
	 * Returns a set of sets of equivalent super classes of a given class
	 * expression.
	 * 
	 * @param entity
	 *            class expression
	 * @param direct
	 *            <code>true</code> to get only direct super classes
	 * @return a set of sets of equivalent super classes of a given class
	 *         expression
	 */
	public Set<Set<OWLEntity>> getSuperClassesAndIndividuals(OWLEntity entity,
			boolean direct);

	/**
	 * Returns a set of sets of equivalent super object properties of a given
	 * object property expression.
	 * 
	 * @param propExpr
	 *            object property expression
	 * @param direct
	 *            <code>true</code> to get only direct super object properties
	 * @return a set of sets of equivalent super object properties of a given
	 *         object property expression
	 */
	public Set<Set<OWLObjectProperty>> getSuperObjectProperties(
			OWLObjectPropertyExpression propExpr, boolean direct);

	/**
	 * Returns the set of transitive properties.
	 * 
	 * @return the set of transitive properties.
	 */
	public Set<OWLObjectProperty> getTransitiveProperties();

	/**
	 * Interrupts the execution when running a classification.
	 */
	public void interrupt();

	/**
	 * Resets, loads an a set of axiom and performs the classification.
	 * 
	 * @param axioms
	 *            set of axioms to classify.
	 */
	public void resetAndLoad(Set<OWLAxiom> axioms);

	/**
	 * Defines a reasoner configuration.
	 * 
	 * @param config
	 *            the reasoner configuration.
	 */
	public void setReasonerConfiguration(OWLReasonerConfiguration config);

}
