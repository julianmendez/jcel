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

package de.tudresden.inf.lat.jcel.core.axiom.normalized;

import java.util.Collections;
import java.util.Set;

/**
 * Axiom of the form:
 * <ul>
 * <li>{a} &equiv; A</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class NominalAxiom implements NormalizedIntegerAxiom {

	private Integer classExpression = null;
	private Integer individual = null;

	/**
	 * Constructs a new nominal axiom.
	 * 
	 * @param classId
	 *            class identifier in the axiom
	 * @param individualId
	 *            individual identifier in the axiom
	 */
	public NominalAxiom(Integer classId, Integer individualId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (individualId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classExpression = classId;
		this.individual = individualId;
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof NominalAxiom) {
			NominalAxiom other = (NominalAxiom) o;
			ret = getClassExpression().equals(other.getClassExpression())
					&& getIndividual().equals(other.getIndividual());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.singleton(getClassExpression());
	}

	public Integer getClassExpression() {
		return this.classExpression;
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	public Integer getIndividual() {
		return this.individual;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.singleton(getIndividual());
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public int hashCode() {
		return getClassExpression().hashCode() + 31
				* getIndividual().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.NominalAxiom);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getClassExpression());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getIndividual());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
