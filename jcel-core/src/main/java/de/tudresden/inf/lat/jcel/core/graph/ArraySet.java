/*
 *
 * Copyright (C) 2009-2015 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements a set of integers using a sorted array of
 * <code>int</code> with exponential growth.
 * 
 * @author Julian Mendez
 */
public class ArraySet implements Set<Integer> {

	private static final int exponentialGrowthFactor = 2;
	private static final int initialSize = 1;
	private static final int linearGrowthFactor = 1;

	private int[] array = null;
	private int size = 0;

	/**
	 * Constructs an empty array set.
	 */
	public ArraySet() {
		clear();
	}

	@Override
	public synchronized boolean add(Integer elem) {
		if (elem == null) {
			throw new NullPointerException("Null argument.");
		}

		boolean ret = false;
		int pointer = Arrays.binarySearch(this.array, 0, this.size, elem);
		if (pointer < 0) {
			pointer = (-1) * (pointer + 1);
			ret = true;
			if (this.size >= this.array.length) {
				int[] newArray = new int[linearGrowthFactor + (exponentialGrowthFactor * this.array.length)];
				System.arraycopy(this.array, 0, newArray, 0, pointer);
				System.arraycopy(this.array, pointer, newArray, pointer + 1, this.size - pointer);
				this.array = newArray;
			} else {
				System.arraycopy(this.array, pointer, this.array, pointer + 1, this.size - pointer);
			}
			this.array[pointer] = elem;
			this.size++;
		}
		return ret;
	}

	@Override
	public synchronized boolean addAll(Collection<? extends Integer> collection) {
		if (collection == null) {
			throw new NullPointerException("Null argument.");
		}

		boolean ret = false;
		for (Integer elem : collection) {
			ret |= add(elem);
		}
		return ret;
	}

	@Override
	public synchronized void clear() {
		this.array = new int[initialSize];
		this.size = 0;
	}

	@Override
	public synchronized boolean contains(Object elem) {
		if (elem == null) {
			throw new NullPointerException("Null argument.");
		}

		boolean ret = false;
		int e = ((Integer) elem).intValue();
		int pointer = Arrays.binarySearch(this.array, 0, this.size, e);
		ret = (pointer >= 0);
		return ret;
	}

	@Override
	public synchronized boolean containsAll(Collection<?> collection) {
		if (collection == null) {
			throw new NullPointerException("Null argument.");
		}

		boolean ret = true;
		for (Iterator<?> it = collection.iterator(); ret && it.hasNext();) {
			ret = ret && contains(it.next());
		}
		return ret;
	}

	@Override
	public synchronized boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof ArraySet)) {
			ArraySet other = (ArraySet) o;
			ret = (this.size == other.size);
			for (int index = 0; ret && (index < this.size); index++) {
				ret = ret && (this.array[index] == other.array[index]);
			}
		}
		return ret;
	}

	@Override
	public synchronized int hashCode() {
		return this.array.hashCode();
	}

	@Override
	public synchronized boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public synchronized Iterator<Integer> iterator() {
		return new ArraySetIterator(this.array, this.size);
	}

	@Override
	public synchronized boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized int size() {
		return this.size;
	}

	@Override
	public synchronized Object[] toArray() {
		return toArrayList().toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return toArrayList().toArray(a);
	}

	private synchronized ArrayList<Integer> toArrayList() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (int index = 0; index < this.size; index++) {
			ret.add(this.array[index]);
		}
		return ret;
	}

	@Override
	public synchronized String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[ ");
		for (int index = 0; index < size(); index++) {
			sbuf.append(this.array[index]);
			sbuf.append(" ");
		}
		sbuf.append("]");
		return sbuf.toString();
	}

}
