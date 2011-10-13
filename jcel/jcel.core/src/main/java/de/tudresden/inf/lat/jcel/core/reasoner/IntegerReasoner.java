/*
 * Copyright 2009 Julian Mendez
 *
 *
 * Integerhis file is part of jcel.
 *
 * jcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcel is distributed in the hope that it will be useful,
 * but WIIntegerHOUInteger ANY WARRANIntegerY; without even the implied warranty of
 * MERCHANIntegerABILIIntegerY or FIIntegerNESS FOR A PARIntegerICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.core.reasoner;

import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataProperty;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * 
 * @author Julian Mendez
 */
public interface IntegerReasoner {

	public void classify();

	public void dispose();

	public void flush();

	public Set<IntegerClass> getBottomClassNode();

	public Set<IntegerDataProperty> getBottomDataPropertyNode();

	public Set<IntegerObjectPropertyExpression> getBottomObjectPropertyNode();

	public boolean getBufferingMode();

	public Set<Set<IntegerClass>> getDataPropertyDomains(
			IntegerDataProperty pe, boolean direct);

	public Set<IntegerClass> getDataPropertyValues(IntegerNamedIndividual ind,
			IntegerDataProperty pe);

	public Set<Set<IntegerNamedIndividual>> getDifferentIndividuals(
			IntegerNamedIndividual ind);

	public Set<Set<IntegerClass>> getDisjointClasses(IntegerClassExpression ce);

	public Set<Set<IntegerDataProperty>> getDisjointDataProperties(
			IntegerDataPropertyExpression pe);

	public Set<Set<IntegerObjectPropertyExpression>> getDisjointObjectProperties(
			IntegerObjectPropertyExpression pe);

	public Set<IntegerClass> getEquivalentClasses(IntegerClassExpression ce);

	public Set<IntegerDataProperty> getEquivalentDataProperties(
			IntegerDataProperty pe);

	public Set<IntegerObjectPropertyExpression> getEquivalentObjectProperties(
			IntegerObjectPropertyExpression pe);

	public Set<Set<IntegerNamedIndividual>> getInstances(
			IntegerClassExpression ce, boolean direct);

	public Set<IntegerObjectPropertyExpression> getInverseObjectProperties(
			IntegerObjectPropertyExpression pe);

	public Set<Set<IntegerClass>> getObjectPropertyDomains(
			IntegerObjectPropertyExpression pe, boolean direct);

	public Set<Set<IntegerClass>> getObjectPropertyRanges(
			IntegerObjectPropertyExpression pe, boolean direct);

	public Set<Set<IntegerNamedIndividual>> getObjectPropertyValues(
			IntegerNamedIndividual ind, IntegerObjectPropertyExpression pe);

	public Set<ComplexIntegerAxiom> getPendingAxiomAdditions();

	public Set<ComplexIntegerAxiom> getPendingAxiomRemovals();

	public String getReasonerName();

	public String getReasonerVersion();

	public Set<ComplexIntegerAxiom> getRootOntology();

	public Set<IntegerNamedIndividual> getSameIndividuals(
			IntegerNamedIndividual ind);

	public Set<Set<IntegerClass>> getSubClasses(IntegerClassExpression ce,
			boolean direct);

	public Set<Set<IntegerDataProperty>> getSubDataProperties(
			IntegerDataProperty pe, boolean direct);

	public Set<Set<IntegerObjectPropertyExpression>> getSubObjectProperties(
			IntegerObjectPropertyExpression pe, boolean direct);

	public Set<Set<IntegerClass>> getSuperClasses(IntegerClassExpression ce,
			boolean direct);

	public Set<Set<IntegerDataProperty>> getSuperDataProperties(
			IntegerDataProperty pe, boolean direct);

	public Set<Set<IntegerObjectPropertyExpression>> getSuperObjectProperties(
			IntegerObjectPropertyExpression pe, boolean direct);

	public long getTimeOut();

	public Set<IntegerClass> getTopClassNode();

	public Set<IntegerDataProperty> getTopDataPropertyNode();

	public Set<IntegerObjectPropertyExpression> getTopObjectPropertyNode();

	public Set<Set<IntegerClass>> getTypes(IntegerNamedIndividual ind,
			boolean direct);

	public Set<IntegerClass> getUnsatisfiableClasses();

	public void interrupt();

	public boolean isConsistent();

	public boolean isEntailed(IntegerAxiom axiom);

	public boolean isEntailed(Set<? extends IntegerAxiom> axioms);

	public boolean isSatisfiable(IntegerClassExpression classExpression);

}
