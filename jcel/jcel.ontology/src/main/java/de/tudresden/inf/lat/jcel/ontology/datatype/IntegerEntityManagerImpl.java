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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An object of this class generates new identification numbers for object
 * properties and classes.
 * 
 * @author Julian Mendez
 */
public class IntegerEntityManagerImpl implements IntegerEntityManager {

	public static final String anonymousEntity = "AnonymousEntity";
	public static final String auxiliaryEntity = "AuxiliaryEntity";

	private Map<IntegerEntityType, Set<Integer>> auxEntityMap = new HashMap<IntegerEntityType, Set<Integer>>();
	private Set<Integer> auxEntitySet = new HashSet<Integer>();
	private Set<Integer> auxInverseObjectPropertySet = new HashSet<Integer>();
	private Map<Integer, Integer> auxNominalInvMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> auxNominalMap = new HashMap<Integer, Integer>();
	private int entityCounter = firstUsableIdentifier;
	private Map<Integer, IntegerEntityType> entityTypeMap = new HashMap<Integer, IntegerEntityType>();
	private Map<Integer, Integer> inverseObjectPropertyMap = new HashMap<Integer, Integer>();
	private Map<Integer, String> nameMap = new HashMap<Integer, String>();
	private Map<IntegerEntityType, Set<Integer>> nonAuxEntityMap = new HashMap<IntegerEntityType, Set<Integer>>();

	/**
	 * Constructs a new identifier generator.
	 */
	public IntegerEntityManagerImpl() {
		registerProperty(bottomClassId, IntegerEntityType.CLASS, false);
		registerProperty(topClassId, IntegerEntityType.CLASS, false);
		registerProperty(bottomObjectPropertyId,
				IntegerEntityType.OBJECT_PROPERTY, false);
		registerProperty(topObjectPropertyId,
				IntegerEntityType.OBJECT_PROPERTY, false);
		registerProperty(bottomDataPropertyId, IntegerEntityType.DATA_PROPERTY,
				false);
		registerProperty(topDataPropertyId, IntegerEntityType.DATA_PROPERTY,
				false);
	}

	@Override
	public Integer createAnonymousEntity(IntegerEntityType type,
			boolean auxiliary) {
		if (type == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.entityCounter;
		this.entityCounter++;
		registerProperty(ret, type, auxiliary);
		this.entityTypeMap.put(ret, type);
		return ret;
	}

	@Override
	public Integer createNamedEntity(IntegerEntityType type, String name,
			boolean auxiliary) {
		if (type == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (name == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = createAnonymousEntity(type, auxiliary);
		this.nameMap.put(ret, name);
		return ret;
	}

	@Override
	public Integer createOrGetClassIdForIndividual(Integer individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.auxNominalMap.get(individual);
		if (ret == null) {
			ret = createAnonymousEntity(IntegerEntityType.CLASS, true);
			this.auxNominalInvMap.put(ret, individual);
			this.auxNominalMap.put(individual, ret);
		}
		return ret;
	}

	@Override
	public Integer createOrGetInverseObjectPropertyOf(Integer propertyId)
			throws IndexOutOfBoundsException {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.inverseObjectPropertyMap.get(propertyId);
		if (ret == null) {
			ret = createAnonymousEntity(IntegerEntityType.OBJECT_PROPERTY, true);
			this.auxInverseObjectPropertySet.add(ret);
			this.inverseObjectPropertyMap.put(propertyId, ret);
			this.inverseObjectPropertyMap.put(ret, propertyId);
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof IntegerEntityManagerImpl) {
			IntegerEntityManagerImpl other = (IntegerEntityManagerImpl) o;
			ret = this.entityCounter == other.entityCounter
					&& this.auxEntityMap.equals(other.auxEntityMap)
					&& this.nonAuxEntityMap.equals(other.nonAuxEntityMap)
					&& this.auxNominalMap.equals(other.auxNominalMap)
					&& this.auxNominalInvMap.equals(other.auxNominalInvMap)
					&& this.auxInverseObjectPropertySet
							.equals(other.inverseObjectPropertyMap)
					&& this.nameMap.equals(other.nameMap)
					&& this.inverseObjectPropertyMap
							.equals(other.inverseObjectPropertyMap);
		}

		return ret;
	}

	@Override
	public Set<Integer> getAuxiliaryInverseObjectProperties() {
		return Collections.unmodifiableSet(this.auxInverseObjectPropertySet);
	}

	@Override
	public Integer getAuxiliaryNominal(Integer individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.auxNominalMap.get(individual);
	}

	@Override
	public Set<Integer> getAuxiliaryNominals() {
		return Collections.unmodifiableSet(this.auxNominalInvMap.keySet());
	}

	@Override
	public Set<Integer> getEntities(IntegerEntityType type, boolean auxiliary) {
		if (type == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> ret;
		if (auxiliary) {
			ret = this.auxEntityMap.get(type);
		} else {
			ret = this.nonAuxEntityMap.get(type);
		}
		if (ret == null) {
			ret = Collections.emptySet();
		} else {
			ret = Collections.unmodifiableSet(ret);
		}
		return ret;
	}

	@Override
	public Integer getIndividual(Integer auxNominal) {
		if (auxNominal == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.auxNominalInvMap.get(auxNominal);
	}

	@Override
	public Set<Integer> getIndividuals() {
		return Collections.unmodifiableSet(this.auxNominalMap.keySet());
	}

	@Override
	public String getName(Integer identifier) {
		if (identifier == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (!this.entityTypeMap.containsKey(identifier)) {
			throw new IndexOutOfBoundsException("Invalid identifier : "
					+ identifier);
		}

		String ret = this.nameMap.get(identifier);
		if (ret == null) {
			if (this.auxEntitySet.contains(identifier)) {
				ret = auxiliaryEntity + identifier;
			} else {
				ret = anonymousEntity + identifier;
			}
		}
		return ret;
	}

	@Override
	public IntegerEntityType getType(Integer identifier) {
		if (identifier == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerEntityType ret = this.entityTypeMap.get(identifier);
		if (ret == null) {
			throw new IndexOutOfBoundsException("Invalid identifier : "
					+ identifier);
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return this.entityCounter + 31 * this.nonAuxEntityMap.hashCode();
	}

	@Override
	public boolean isAuxiliary(Integer identifier) {
		if (identifier == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (!this.entityTypeMap.containsKey(identifier)) {
			throw new IndexOutOfBoundsException("Invalid identifier : "
					+ identifier);
		}
		return this.auxEntitySet.contains(identifier);
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

	private void registerProperty(Integer identifier, IntegerEntityType type,
			boolean auxiliary) {
		if (auxiliary) {
			this.auxEntitySet.add(identifier);
			Set<Integer> set = this.auxEntityMap.get(type);
			if (set == null) {
				set = new HashSet<Integer>();
				this.auxEntityMap.put(type, set);
			}
			set.add(identifier);
		} else {
			Set<Integer> set = this.nonAuxEntityMap.get(type);
			if (set == null) {
				set = new HashSet<Integer>();
				this.nonAuxEntityMap.put(type, set);
			}
			set.add(identifier);
		}
		this.entityTypeMap.put(identifier, type);
	}

	@Override
	public int size() {
		return this.entityCounter;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[\n  entities: ");
		sbuf.append(this.entityCounter);
		sbuf.append("\n  names: ");
		sbuf.append(this.nameMap);
		sbuf.append("\n  non-auxiliary entities: ");
		sbuf.append(this.nonAuxEntityMap);
		sbuf.append("\n  auxiliary entities: ");
		sbuf.append(this.auxEntityMap);
		sbuf.append("\n  auxiliary inverse object properties: ");
		sbuf.append(this.auxInverseObjectPropertySet);
		sbuf.append("\n]\n");
		return sbuf.toString();
	}

}
