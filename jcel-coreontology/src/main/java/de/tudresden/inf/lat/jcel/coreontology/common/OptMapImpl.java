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

package de.tudresden.inf.lat.jcel.coreontology.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This is the default implementation of {@link OptMap}. This implementation
 * does not copy the map passed as an argument to create the instance, and it
 * uses the map itself instead. This means that the internal representation can
 * be modified externally, or it can be retrieved by using {@link #asMap()}.
 * 
 * @author Julian Mendez
 * 
 * @param <K>
 *            type of keys in this map
 * @param <V>
 *            type of mapped values
 */
public class OptMapImpl<K, V> implements OptMap<K, V> {

	private final Map<K, V> map;

	/**
	 * Constructs a new map. The default implementation structure is a
	 * {@link HashMap}.
	 */
	public OptMapImpl() {
		this.map = new HashMap<K, V>();
	}

	/**
	 * Constructs a new map using a specified {@link Map}.
	 * 
	 * @param map
	 *            map
	 */
	public OptMapImpl(Map<K, V> map) {
		Objects.requireNonNull(map);
		this.map = map;
	}

	/**
	 * Constructs a new map using another specified {@link OptMap}.
	 * 
	 * @param map
	 *            map
	 */
	public OptMapImpl(OptMap<K, V> map) {
		Objects.requireNonNull(map);
		this.map = map.asMap();
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey(K key) {
		Objects.requireNonNull(key);
		return this.map.containsKey(key);
	}

	@Override
	public boolean containsValue(V value) {
		Objects.requireNonNull(value);
		return this.map.containsValue(value);
	}

	@Override
	public Optional<V> get(K key) {
		Objects.requireNonNull(key);
		return Optional.ofNullable(this.map.get(key));
	}

	@Override
	public Optional<V> put(K key, V value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		return Optional.ofNullable(this.map.put(key, value));
	}

	@Override
	public Optional<V> remove(K key) {
		Objects.requireNonNull(key);
		return Optional.ofNullable(this.map.remove(key));
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Objects.requireNonNull(m);
		this.map.putAll(m);
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Set<K> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.map.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.map.entrySet();
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.map.equals(obj);
	}

	@Override
	public String toString() {
		return this.map.toString();
	}

	@Override
	public Map<K, V> asMap() {
		return this.map;
	}

}
