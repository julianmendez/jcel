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

package de.tudresden.inf.lat.jcel.core.normalization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An object of this class generates new identification numbers for object
 * properties and classes.
 * 
 * @author Julian Mendez
 * 
 */
public class IdGenerator {

	// private static final Logger logger = Logger.getLogger(NameGenerator.class
	// .getName());

	private Map<Integer, Integer> auxiliaryNominalMap = null;
	private Integer firstClassId = null;
	private Integer firstObjectPropertyId = null;
	private Map<Integer, Integer> invAuxiliaryNominalMap = null;
	private Map<Integer, Integer> inverseObjectPropertyMap = null;
	private Integer nextClassId = null;
	private Integer nextObjectPropertyId = null;

	public IdGenerator(Integer classOffset, Integer propertyOffset) {
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
		this.auxiliaryNominalMap = new HashMap<Integer, Integer>();
		this.invAuxiliaryNominalMap = new HashMap<Integer, Integer>();
		this.inverseObjectPropertyMap = new HashMap<Integer, Integer>();
	}

	public Integer createNewClassId() {
		Integer ret = getNextClassId();
		this.nextClassId++;
		return ret;
	}

	public Integer createNewObjectPropertyId() {
		Integer ret = getNextObjectPropertyId();
		this.nextObjectPropertyId++;
		return ret;
	}

	/**
	 * Returns the class identifier corresponding to the given individual. If
	 * that class does not exist, it creates a new auxiliary class.
	 * 
	 * @param individual
	 *            individual to get the class
	 * @return the class identifier for the given individual
	 */
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

	/**
	 * Returns the inverse object property of the given object property. If this
	 * property does not exist, it creates a new auxiliary object property.
	 * 
	 * @param propertyId
	 *            property identifier to create an inverse object property
	 * @return the inverse object property of the given object property
	 * @throws IndexOutOfBoundsException
	 *             if the property identifier is greater than the last auxiliary
	 *             property created
	 */
	public Integer createOrGetInverseObjectPropertyOf(Integer propertyId)
			throws IndexOutOfBoundsException {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (propertyId >= getNextObjectPropertyId()) {
			throw new IndexOutOfBoundsException(
					"Object property identifier ("
							+ propertyId
							+ ") is greater than or equal to the next auxiliary object property ("
							+ getNextObjectPropertyId() + ").");
		}

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
		if (o instanceof IdGenerator) {
			IdGenerator other = (IdGenerator) o;
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

	/**
	 * This method gives the auxiliary nominal related to a specific individual.
	 * 
	 * @param individual
	 *            the individual
	 * @return the requested class or <code>null</code> if the individual does
	 *         not have any related auxiliary nominal.
	 */
	public Integer getAuxiliaryNominal(Integer individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.auxiliaryNominalMap.get(individual);
	}

	public Set<Integer> getAuxiliaryNominals() {
		return Collections
				.unmodifiableSet(this.invAuxiliaryNominalMap.keySet());
	}

	public Integer getFirstClassId() {
		return this.firstClassId;
	}

	public Integer getFirstObjectPropertyId() {
		return this.firstObjectPropertyId;
	}

	/**
	 * This method gives the individual related to a specific auxiliary nominal.
	 * 
	 * @param auxNominal
	 *            the auxiliary nominal
	 * @return the requested individual or <code>null</code> if the auxiliary
	 *         nominal does not have any related individual.
	 */
	public Integer getIndividual(Integer auxNominal) {
		if (auxNominal == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.invAuxiliaryNominalMap.get(auxNominal);
	}

	public Set<Integer> getIndividuals() {
		return Collections.unmodifiableSet(this.auxiliaryNominalMap.keySet());
	}

	public Integer getNextClassId() {
		return this.nextClassId;
	}

	public Integer getNextObjectPropertyId() {
		return this.nextObjectPropertyId;
	}

	@Override
	public int hashCode() {
		return this.firstClassId + 31 * this.nextClassId;
	}

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
