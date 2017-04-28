/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.coreontology.datatype;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * An object of this class generates new identification numbers for object
 * properties and classes.
 * 
 * @author Julian Mendez
 */
public class IntegerEntityManagerImpl implements IntegerEntityManager {

	public static final String anonymousEntity = "AnonymousEntity";
	public static final String auxiliaryEntity = "AuxiliaryEntity";

	private final Map<IntegerEntityType, Set<Integer>> auxEntityMap = new HashMap<>();
	private final Set<Integer> auxEntitySet = new HashSet<>();
	private final Set<Integer> auxInverseObjectPropertySet = new HashSet<>();
	private final Map<Integer, Integer> auxNominalInvMap = new HashMap<>();
	private final Map<Integer, Integer> auxNominalMap = new HashMap<>();
	private int entityCounter = firstUsableIdentifier;
	private final Map<Integer, IntegerEntityType> entityTypeMap = new HashMap<>();
	private final Map<Integer, Integer> inverseObjectPropertyMap = new HashMap<>();
	private final Map<Integer, String> nameMap = new HashMap<>();
	private final Map<IntegerEntityType, Set<Integer>> nonAuxEntityMap = new HashMap<>();

	/**
	 * Constructs a new identifier generator.
	 */
	public IntegerEntityManagerImpl() {
		registerProperty(bottomClassId, IntegerEntityType.CLASS, false);
		registerProperty(topClassId, IntegerEntityType.CLASS, false);
		registerProperty(bottomObjectPropertyId, IntegerEntityType.OBJECT_PROPERTY, false);
		registerProperty(topObjectPropertyId, IntegerEntityType.OBJECT_PROPERTY, false);
		registerProperty(bottomDataPropertyId, IntegerEntityType.DATA_PROPERTY, false);
		registerProperty(topDataPropertyId, IntegerEntityType.DATA_PROPERTY, false);
	}

	@Override
	public Integer createAnonymousEntity(IntegerEntityType type, boolean auxiliary) {
		Objects.requireNonNull(type);
		Integer ret = this.entityCounter;
		this.entityCounter++;
		registerProperty(ret, type, auxiliary);
		this.entityTypeMap.put(ret, type);
		return ret;
	}

	@Override
	public Integer createNamedEntity(IntegerEntityType type, String name, boolean auxiliary) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		Integer ret = createAnonymousEntity(type, auxiliary);
		this.nameMap.put(ret, name);
		return ret;
	}

	@Override
	public Integer createOrGetClassIdForIndividual(Integer individual) {
		Objects.requireNonNull(individual);
		Integer ret = this.auxNominalMap.get(individual);
		if (Objects.isNull(ret)) {
			ret = createAnonymousEntity(IntegerEntityType.CLASS, true);
			this.auxNominalInvMap.put(ret, individual);
			this.auxNominalMap.put(individual, ret);
		}
		return ret;
	}

	@Override
	public Integer createOrGetInverseObjectPropertyOf(Integer propertyId) throws IndexOutOfBoundsException {
		Objects.requireNonNull(propertyId);
		Integer ret = this.inverseObjectPropertyMap.get(propertyId);
		if (Objects.isNull(ret)) {
			ret = createAnonymousEntity(IntegerEntityType.OBJECT_PROPERTY, true);
			this.auxInverseObjectPropertySet.add(ret);
			this.inverseObjectPropertyMap.put(propertyId, ret);
			this.inverseObjectPropertyMap.put(ret, propertyId);
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerEntityManagerImpl)) {
			IntegerEntityManagerImpl other = (IntegerEntityManagerImpl) o;
			ret = (this.entityCounter == other.entityCounter) && this.auxEntityMap.equals(other.auxEntityMap)
					&& this.nonAuxEntityMap.equals(other.nonAuxEntityMap)
					&& this.auxNominalMap.equals(other.auxNominalMap)
					&& this.auxNominalInvMap.equals(other.auxNominalInvMap)
					&& this.auxInverseObjectPropertySet.equals(other.inverseObjectPropertyMap)
					&& this.nameMap.equals(other.nameMap)
					&& this.inverseObjectPropertyMap.equals(other.inverseObjectPropertyMap);
		}

		return ret;
	}

	@Override
	public Set<Integer> getAuxiliaryInverseObjectProperties() {
		return Collections.unmodifiableSet(this.auxInverseObjectPropertySet);
	}

	@Override
	public Optional<Integer> getAuxiliaryNominal(Integer individual) {
		Objects.requireNonNull(individual);
		return Optional.ofNullable(this.auxNominalMap.get(individual));
	}

	@Override
	public Set<Integer> getAuxiliaryNominals() {
		return Collections.unmodifiableSet(this.auxNominalInvMap.keySet());
	}

	@Override
	public Set<Integer> getEntities(IntegerEntityType type) {
		Objects.requireNonNull(type);
		Set<Integer> ret = new TreeSet<>();
		if (Objects.nonNull(this.nonAuxEntityMap.get(type))) {
			ret.addAll(this.nonAuxEntityMap.get(type));
		}
		if (Objects.nonNull(this.auxEntityMap.get(type))) {
			ret.addAll(this.auxEntityMap.get(type));
		}
		ret = Collections.unmodifiableSet(ret);
		return ret;
	}

	@Override
	public Set<Integer> getEntities(IntegerEntityType type, boolean auxiliary) {
		Objects.requireNonNull(type);
		Set<Integer> ret;
		if (auxiliary) {
			ret = this.auxEntityMap.get(type);
		} else {
			ret = this.nonAuxEntityMap.get(type);
		}
		if (Objects.isNull(ret)) {
			ret = Collections.emptySet();
		} else {
			ret = Collections.unmodifiableSet(ret);
		}
		return ret;
	}

	@Override
	public Optional<Integer> getIndividual(Integer auxNominal) {
		Objects.requireNonNull(auxNominal);
		return Optional.ofNullable(this.auxNominalInvMap.get(auxNominal));
	}

	@Override
	public Set<Integer> getIndividuals() {
		return Collections.unmodifiableSet(this.auxNominalMap.keySet());
	}

	@Override
	public String getName(Integer identifier) {
		Objects.requireNonNull(identifier);
		if (!this.entityTypeMap.containsKey(identifier)) {
			throw new IndexOutOfBoundsException("Invalid identifier : " + identifier);
		}

		String ret = this.nameMap.get(identifier);
		if (Objects.isNull(ret)) {
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
		Objects.requireNonNull(identifier);
		IntegerEntityType ret = this.entityTypeMap.get(identifier);
		if (Objects.isNull(ret)) {
			throw new IndexOutOfBoundsException("Invalid identifier : " + identifier);
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return this.entityCounter + (31 * this.nonAuxEntityMap.hashCode());
	}

	@Override
	public boolean isAuxiliary(Integer identifier) {
		Objects.requireNonNull(identifier);
		if (!this.entityTypeMap.containsKey(identifier)) {
			throw new IndexOutOfBoundsException("Invalid identifier : " + identifier);
		}
		return this.auxEntitySet.contains(identifier);
	}

	@Override
	public boolean isEntity(Integer identifier) {
		Objects.requireNonNull(identifier);
		return this.entityTypeMap.containsKey(identifier);
	}

	@Override
	public boolean proposeInverseObjectPropertyOf(Integer firstProperty, Integer secondProperty) {
		Objects.requireNonNull(firstProperty);
		Objects.requireNonNull(secondProperty);
		boolean ret = false;
		Integer invFirstProperty = this.inverseObjectPropertyMap.get(firstProperty);
		Integer invSecondProperty = this.inverseObjectPropertyMap.get(secondProperty);
		if ((Objects.isNull(invFirstProperty)) && (Objects.isNull(invSecondProperty))) {
			this.inverseObjectPropertyMap.put(firstProperty, secondProperty);
			this.inverseObjectPropertyMap.put(secondProperty, firstProperty);
			ret = true;
		}
		return ret;
	}

	private void registerProperty(Integer identifier, IntegerEntityType type, boolean auxiliary) {
		if (auxiliary) {
			this.auxEntitySet.add(identifier);
			Set<Integer> set = this.auxEntityMap.get(type);
			if (Objects.isNull(set)) {
				set = new HashSet<>();
				this.auxEntityMap.put(type, set);
			}
			set.add(identifier);
		} else {
			Set<Integer> set = this.nonAuxEntityMap.get(type);
			if (Objects.isNull(set)) {
				set = new HashSet<>();
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
