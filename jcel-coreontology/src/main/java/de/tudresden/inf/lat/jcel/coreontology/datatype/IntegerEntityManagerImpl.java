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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * An object of this class generates new identification numbers for object
 * properties and classes.
 * 
 * @author Julian Mendez
 */
public class IntegerEntityManagerImpl implements IntegerEntityManager {

	public static final String anonymousEntity = "AnonymousEntity";
	public static final String auxiliaryEntity = "AuxiliaryEntity";

	private final OptMap<IntegerEntityType, Set<Integer>> auxEntityMap = new OptMapImpl<>(new HashMap<>());
	private final Set<Integer> auxEntitySet = new HashSet<>();
	private final Set<Integer> auxInverseObjectPropertySet = new HashSet<>();
	private final OptMap<Integer, Integer> auxNominalInvMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Integer> auxNominalMap = new OptMapImpl<>(new HashMap<>());
	private int entityCounter = firstUsableIdentifier;
	private final OptMap<Integer, IntegerEntityType> entityTypeMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Integer> inverseObjectPropertyMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, String> nameMap = new OptMapImpl<>(new HashMap<>());
	private final OptMap<IntegerEntityType, Set<Integer>> nonAuxEntityMap = new OptMapImpl<>(new HashMap<>());

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
		Optional<Integer> optId = this.auxNominalMap.get(individual);
		if (!optId.isPresent()) {
			optId = Optional.of(createAnonymousEntity(IntegerEntityType.CLASS, true));
			this.auxNominalInvMap.put(optId.get(), individual);
			this.auxNominalMap.put(individual, optId.get());
		}
		return optId.get();
	}

	@Override
	public Integer createOrGetInverseObjectPropertyOf(Integer propertyId) throws IndexOutOfBoundsException {
		Objects.requireNonNull(propertyId);
		Optional<Integer> optId = this.inverseObjectPropertyMap.get(propertyId);
		if (!optId.isPresent()) {
			optId = Optional.of(createAnonymousEntity(IntegerEntityType.OBJECT_PROPERTY, true));
			this.auxInverseObjectPropertySet.add(optId.get());
			this.inverseObjectPropertyMap.put(propertyId, optId.get());
			this.inverseObjectPropertyMap.put(optId.get(), propertyId);
		}
		return optId.get();
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
		return this.auxNominalMap.get(individual);
	}

	@Override
	public Set<Integer> getAuxiliaryNominals() {
		return Collections.unmodifiableSet(this.auxNominalInvMap.keySet());
	}

	@Override
	public Set<Integer> getEntities(IntegerEntityType type) {
		Objects.requireNonNull(type);
		Set<Integer> ret = new TreeSet<>();
		if (this.nonAuxEntityMap.get(type).isPresent()) {
			ret.addAll(this.nonAuxEntityMap.get(type).get());
		}
		if (this.auxEntityMap.get(type).isPresent()) {
			ret.addAll(this.auxEntityMap.get(type).get());
		}
		ret = Collections.unmodifiableSet(ret);
		return ret;
	}

	@Override
	public Set<Integer> getEntities(IntegerEntityType type, boolean auxiliary) {
		Objects.requireNonNull(type);
		Optional<Set<Integer>> optSet;
		if (auxiliary) {
			optSet = this.auxEntityMap.get(type);
		} else {
			optSet = this.nonAuxEntityMap.get(type);
		}
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		} else {
			optSet = Optional.of(Collections.unmodifiableSet(optSet.get()));
		}
		return optSet.get();
	}

	@Override
	public Optional<Integer> getIndividual(Integer auxNominal) {
		Objects.requireNonNull(auxNominal);
		return this.auxNominalInvMap.get(auxNominal);
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

		Optional<String> optName = this.nameMap.get(identifier);
		if (!optName.isPresent()) {
			if (this.auxEntitySet.contains(identifier)) {
				optName = Optional.of(auxiliaryEntity + identifier);
			} else {
				optName = Optional.of(anonymousEntity + identifier);
			}
		}
		return optName.get();
	}

	@Override
	public IntegerEntityType getType(Integer identifier) {
		Objects.requireNonNull(identifier);
		Optional<IntegerEntityType> optType = this.entityTypeMap.get(identifier);
		if (!optType.isPresent()) {
			throw new IndexOutOfBoundsException("Invalid identifier : " + identifier);
		}
		return optType.get();
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
		Optional<Integer> optInvFirstProperty = this.inverseObjectPropertyMap.get(firstProperty);
		Optional<Integer> optInvSecondProperty = this.inverseObjectPropertyMap.get(secondProperty);
		if (!optInvFirstProperty.isPresent() && !optInvSecondProperty.isPresent()) {
			this.inverseObjectPropertyMap.put(firstProperty, secondProperty);
			this.inverseObjectPropertyMap.put(secondProperty, firstProperty);
			ret = true;
		}
		return ret;
	}

	private void registerProperty(Integer identifier, IntegerEntityType type, boolean auxiliary) {
		if (auxiliary) {
			this.auxEntitySet.add(identifier);
			Optional<Set<Integer>> optSet = this.auxEntityMap.get(type);
			if (!optSet.isPresent()) {
				optSet = Optional.of(new HashSet<>());
				this.auxEntityMap.put(type, optSet.get());
			}
			optSet.get().add(identifier);
		} else {
			Optional<Set<Integer>> optSet = this.nonAuxEntityMap.get(type);
			if (!optSet.isPresent()) {
				optSet = Optional.of(new HashSet<>());
				this.nonAuxEntityMap.put(type, optSet.get());
			}
			optSet.get().add(identifier);
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
