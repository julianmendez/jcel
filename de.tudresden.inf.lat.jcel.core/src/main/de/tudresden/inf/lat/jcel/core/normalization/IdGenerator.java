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
 * An object of this class generates new identification numbers for properties
 * and classes.
 * 
 * @author Julian Mendez
 * 
 */
public class IdGenerator {

	// private static final Logger logger = Logger.getLogger(NameGenerator.class
	// .getName());

	private Map<Integer, Integer> auxiliaryNominalMap = null;
	private Integer firstClassId = null;
	private Integer firstPropertyId = null;
	private Map<Integer, Integer> invAuxiliaryNominalMap = null;
	private Integer nextClassId = null;;
	private Integer nextPropertyId = null;;

	public IdGenerator(Integer classOffset, Integer propertyOffset) {
		if (classOffset == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (propertyOffset == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.firstClassId = classOffset;
		this.firstPropertyId = propertyOffset;
		this.nextClassId = this.firstClassId;
		this.nextPropertyId = this.firstPropertyId;
		this.auxiliaryNominalMap = new HashMap<Integer, Integer>();
		this.invAuxiliaryNominalMap = new HashMap<Integer, Integer>();
	}

	public Integer createNewClassId() {
		Integer ret = getNextClassId();
		this.nextClassId++;
		return ret;
	}

	public Integer createNewPropertyId() {
		Integer ret = getNextPropertyId();
		this.nextPropertyId++;
		return ret;
	}

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
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IdGenerator) {
			IdGenerator other = (IdGenerator) o;
			ret = getFirstClassId().equals(other.getFirstClassId())
					&& getFirstPropertyId().equals(other.getFirstPropertyId())
					&& getNextClassId().equals(other.getNextClassId())
					&& getNextPropertyId().equals(other.getNextPropertyId());
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

	public Integer getFirstPropertyId() {
		return this.firstPropertyId;
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

	public Integer getNextPropertyId() {
		return this.nextPropertyId;
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
		sbuf.append(getFirstPropertyId());
		sbuf.append("-");
		sbuf.append(getNextPropertyId());
		sbuf.append(")");
		return sbuf.toString();
	}
}
