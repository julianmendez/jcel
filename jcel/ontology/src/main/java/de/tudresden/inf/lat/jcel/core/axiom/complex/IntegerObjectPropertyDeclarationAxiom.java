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
import java.util.Set;

/**
 * An object of this class is an axiom that declares an object property.
 * 
 * @author Julian Mendez
 */
public class IntegerObjectPropertyDeclarationAxiom implements
		IntegerDeclarationAxiom {

	private Integer entity = null;

	/**
	 * Constructs a new object property declaration axiom.
	 * 
	 * @param declaredEntity
	 *            object property
	 */
	public IntegerObjectPropertyDeclarationAxiom(Integer declaredEntity) {
		if (declaredEntity == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.entity = declaredEntity;
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
		if (o instanceof IntegerObjectPropertyDeclarationAxiom) {
			IntegerObjectPropertyDeclarationAxiom other = (IntegerObjectPropertyDeclarationAxiom) o;
			ret = getEntity().equals(other.getEntity());
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
	public Integer getEntity() {
		return this.entity;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.singleton(this.entity);
	}

	@Override
	public int hashCode() {
		return getEntity().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.Declaration);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(ComplexIntegerAxiomConstant.ObjectPropertyDeclaration);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		sbuf.append(getEntity().toString());
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
