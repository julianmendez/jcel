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

package de.tudresden.inf.lat.jcel.core.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class models an assertion that relates a data property and a pair of
 * individuals. <br />
 * This is: r(a,b)
 * 
 * @author Julian Mendez
 */
public class IntegerDataPropertyAssertionAxiom implements ComplexIntegerAxiom {

	private Set<Integer> individualsInSignature = null;
	private Integer object = null;
	private Integer property = null;
	private Integer subject = null;

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
	public IntegerDataPropertyAssertionAxiom(Integer objectProp,
			Integer subjectInd, Integer objectInd) {
		if (objectProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (subjectInd == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (objectInd == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.property = objectProp;
		this.subject = subjectInd;
		this.object = objectInd;

		this.individualsInSignature = new HashSet<Integer>();
		this.individualsInSignature.add(this.subject);
		this.individualsInSignature.add(this.object);
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerObjectPropertyAssertionAxiom) {
			IntegerObjectPropertyAssertionAxiom other = (IntegerObjectPropertyAssertionAxiom) o;
			ret = getProperty().equals(other.getProperty())
					&& getSubject().equals(other.getSubject())
					&& getObject().equals(other.getObject());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.unmodifiableSet(this.individualsInSignature);
	}

	/**
	 * Returns the individual representing the object.
	 * 
	 * @return the individual representing the object
	 */
	public Integer getObject() {
		return this.object;
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.singleton(getProperty());
	}

	/**
	 * Returns the data property in the axiom.
	 * 
	 * @return the data property in the axiom
	 */
	public Integer getProperty() {
		return this.property;
	}

	/**
	 * Returns the individual representing the subject.
	 * 
	 * @return the individual representing the subject
	 */
	public Integer getSubject() {
		return this.subject;
	}

	@Override
	public int hashCode() {
		return getProperty().hashCode() + 31 * getSubject().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.DataPropertyAssertion);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getProperty().toString());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getSubject().toString());
		sbuf.append(ComplexIntegerAxiomConstant.sp);
		sbuf.append(getObject().toString());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}