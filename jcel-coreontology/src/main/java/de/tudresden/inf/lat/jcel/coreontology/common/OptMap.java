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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * An object implementing this interface wraps a map with some conditions:
 * <ul>
 * <li>no <code>null</code> value is accepted nor returned</li>
 * <li>the methods that verify containment accept only objects of type K or
 * V</li>
 * </ul>
 * 
 * @author Julian Mendez
 * @param <K>
 *            type of keys in this map
 * @param <V>
 *            type of mapped values
 */
public interface OptMap<K, V> {

	/**
	 * Returns the size of this map.
	 * 
	 * @return the size of this map
	 */
	int size();

	/**
	 * Returns <code>true</code> if and only if this map is empty.
	 * 
	 * @return <code>true</code> if and only if this map is empty
	 */
	boolean isEmpty();

	/**
	 * Returns <code>true</code> if and only if this map contains a mapping for
	 * the given key. This method replaces {@link Map#containsKey(Object)}.
	 * 
	 * @param key
	 *            key
	 * @return <code>true</code> if and only if this map contains a mapping for
	 *         the given key
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	boolean containsKey(K key);

	/**
	 * Returns <code>true</code> if and only if this map associates one or more
	 * keys to the given value. This method replaces
	 * {@link Map#containsValue(Object)}.
	 * 
	 * @param value
	 *            value
	 * @return <code>true</code> if and only if this map associates one or more
	 *         keys to the given value
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	boolean containsValue(V value);

	/**
	 * Returns an optional containing the value associated to the given key, is
	 * this association exists, or an empty optional otherwise. This method
	 * replaces {@link Map#get(Object)}.
	 * 
	 * @param key
	 *            key
	 * @return an optional containing the value associated to the given key, is
	 *         this association exists, or an empty optional otherwise
	 */
	Optional<V> get(K key);

	/**
	 * Associates the given value with the given key. This method replaces
	 * {@link Map#put put(K, V)}.
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return an optional containing the previous associated value for the
	 *         given key, or an empty optional if there was no mapping for that
	 *         key
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	Optional<V> put(K key, V value);

	/**
	 * Removes the mapping for the given key. This method replaces
	 * {@link Map#remove(Object)}.
	 * 
	 * @param key
	 *            key
	 * @return an optional containing the previous associated value for the
	 *         given key, or an empty optional if there was no mapping for that
	 *         key
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	public Optional<V> remove(K key);

	/**
	 * Clears this map.
	 */
	void clear();

	/**
	 * Adds all the associations given in the specified map.
	 * 
	 * @param m
	 *            map containing associations
	 */
	void putAll(Map<? extends K, ? extends V> m);

	/**
	 * Returns the set of keys.
	 * 
	 * @return the set of keys
	 */
	Set<K> keySet();

	/**
	 * Returns the collection of values.
	 * 
	 * @return the collection of values
	 */
	Collection<V> values();

	/**
	 * Returns a set of associations.
	 * 
	 * @return a set of associations
	 */
	Set<Entry<K, V>> entrySet();

	/**
	 * Returns this as a {@link Map}.
	 * 
	 * @return this as a <code>Map</code>
	 */
	Map<K, V> asMap();

}
