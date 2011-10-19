/*
 * Copyright 2009 Julian Mendez
 *
 *
 *  This file is part of jcel.
 *
 *  jcel is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jcel is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An object of this class generates new identification numbers for object
 * properties and classes.
 * 
 * @author Julian Mendez
 */
public class IdGeneratorImpl implements IdGenerator {

	private Map<Integer, Integer> auxiliaryNominalMap = new HashMap<Integer, Integer>();
	private Integer firstClassId = null;
	private Integer firstObjectPropertyId = null;
	private Map<Integer, Integer> invAuxiliaryNominalMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> inverseObjectPropertyMap = new HashMap<Integer, Integer>();
	private Integer nextClassId = null;
	private Integer nextObjectPropertyId = null;

	/**
	 * Constructs a new identifier generator.
	 */
	public IdGeneratorImpl() {
	}

	private void assertBounds(Integer propertyId)
			throws IndexOutOfBoundsException {
		if (propertyId >= getNextObjectPropertyId()) {
			throw new IndexOutOfBoundsException(
					"Object property identifier ("
							+ propertyId
							+ ") is greater than or equal to the next auxiliary object property ("
							+ getNextObjectPropertyId() + ").");
		}
	}

	@Override
	public Integer createNewClassId() {
		Integer ret = getNextClassId();
		this.nextClassId++;
		return ret;
	}

	@Override
	public Integer createNewObjectPropertyId() {
		Integer ret = getNextObjectPropertyId();
		this.nextObjectPropertyId++;
		return ret;
	}

	@Override
	public Integer createOrGetClassIdForIndividual(Integer individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.auxiliaryNominalMap.get(individual);
		if (ret == null) {
			ret = createNewClassId();
			this.invAuxiliaryNominalMap.put(ret, individual);
			this.auxiliaryNominalMap.put(individual, ret);
		}
		return ret;
	}

	@Override
	public Integer createOrGetInverseObjectPropertyOf(Integer propertyId)
			throws IndexOutOfBoundsException {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		assertBounds(propertyId);

		Integer ret = this.inverseObjectPropertyMap.get(propertyId);
		if (ret == null) {
			ret = createNewObjectPropertyId();
			this.inverseObjectPropertyMap.put(propertyId, ret);
			this.inverseObjectPropertyMap.put(ret, propertyId);
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IdGeneratorImpl) {
			IdGeneratorImpl other = (IdGeneratorImpl) o;
			ret = getFirstClassId().equals(other.getFirstClassId())
					&& getFirstObjectPropertyId().equals(
							other.getFirstObjectPropertyId())
					&& getNextClassId().equals(other.getNextClassId())
					&& getNextObjectPropertyId().equals(
							other.getNextObjectPropertyId())
					&& this.auxiliaryNominalMap
							.equals(other.auxiliaryNominalMap)
					&& this.invAuxiliaryNominalMap
							.equals(other.invAuxiliaryNominalMap)
					&& this.inverseObjectPropertyMap
							.equals(other.inverseObjectPropertyMap);
		}

		return ret;
	}

	@Override
	public Integer getAuxiliaryNominal(Integer individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.auxiliaryNominalMap.get(individual);
	}

	@Override
	public Set<Integer> getAuxiliaryNominals() {
		return Collections
				.unmodifiableSet(this.invAuxiliaryNominalMap.keySet());
	}

	@Override
	public Integer getFirstClassId() {
		return this.firstClassId;
	}

	@Override
	public Integer getFirstObjectPropertyId() {
		return this.firstObjectPropertyId;
	}

	@Override
	public Integer getIndividual(Integer auxNominal) {
		if (auxNominal == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.invAuxiliaryNominalMap.get(auxNominal);
	}

	@Override
	public Set<Integer> getIndividuals() {
		return Collections.unmodifiableSet(this.auxiliaryNominalMap.keySet());
	}

	@Override
	public Integer getNextClassId() {
		return this.nextClassId;
	}

	@Override
	public Integer getNextObjectPropertyId() {
		return this.nextObjectPropertyId;
	}

	@Override
	public int hashCode() {
		return this.firstClassId + 31 * this.nextClassId;
	}

	@Override
	public boolean proposeInverseObjectPropertyOf(Integer firstProperty,
			Integer secondProperty) throws IndexOutOfBoundsException {
		if (firstProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (secondProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		assertBounds(firstProperty);
		assertBounds(secondProperty);

		boolean ret = false;
		Integer invFirstProperty = this.inverseObjectPropertyMap
				.get(firstProperty);
		Integer invSecondProperty = this.inverseObjectPropertyMap
				.get(secondProperty);
		if (invFirstProperty == null && invSecondProperty == null) {
			this.inverseObjectPropertyMap.put(firstProperty, secondProperty);
			this.inverseObjectPropertyMap.put(secondProperty, firstProperty);
			ret = true;
		}
		return ret;
	}

	@Override
	public void resetTo(Integer classOffset, Integer propertyOffset) {
		if (classOffset == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (propertyOffset == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.firstClassId = classOffset;
		this.firstObjectPropertyId = propertyOffset;
		this.nextClassId = this.firstClassId;
		this.nextObjectPropertyId = this.firstObjectPropertyId;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(");
		sbuf.append(getFirstClassId());
		sbuf.append("-");
		sbuf.append(getNextClassId());
		sbuf.append(" , ");
		sbuf.append(getFirstObjectPropertyId());
		sbuf.append("-");
		sbuf.append(getNextObjectPropertyId());
		sbuf.append(")");
		return sbuf.toString();
	}

}
