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

package de.tudresden.inf.lat.jcel.core.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * This class implements a set of integers using an array of <code>int</code>
 * with exponential growth. The elements are stored using a hash function.
 * 
 * @author Julian Mendez
 */
public class HashArraySet implements Set<Integer> {

	protected static final int EMPTY = Integer.MIN_VALUE;
	private static final int exponentialGrowthFactor = 2;
	private static final int initialSize = 0x10;
	private static final int linearGrowthFactor = 1;
	private static final int REHASH = Integer.MIN_VALUE;

	private int[] array = null;
	private int size = 0;

	/**
	 * Constructs an empty array set.
	 */
	public HashArraySet() {
		clear();
	}

	@Override
	public synchronized boolean add(Integer elem) {
		Objects.requireNonNull(elem);
		boolean ret = false;
		int pointer = find(elem, this.array);
		if ((pointer == REHASH) || (pointer < 0)) {
			ret = true;
			if (pointer != REHASH) {
				pointer = (-1) * (pointer + 1);
				this.array[pointer] = elem;
			} else {
				this.array = rehashAndAdd(this.array, elem);
			}
			this.size++;
		}
		return ret;
	}

	@Override
	public synchronized boolean addAll(Collection<? extends Integer> collection) {
		Objects.requireNonNull(collection);
		boolean ret = false;
		for (Integer elem : collection) {
			ret |= add(elem);
		}
		return ret;
	}

	@Override
	public synchronized void clear() {
		this.array = makeNewArray(initialSize);
		this.size = 0;
	}

	private int computeNewLength(int length) {
		int ret = linearGrowthFactor + (exponentialGrowthFactor * length);
		while (((ret % 2) == 0) || ((ret % 3) == 0) || ((ret % 5) == 0)) {
			ret++;
		}
		return ret;
	}

	@Override
	public synchronized boolean contains(Object elem) {
		Objects.requireNonNull(elem);
		boolean ret = false;
		int e = ((Integer) elem).intValue();
		int pointer = find(e, this.array);
		ret = (pointer >= 0) && (pointer != REHASH);
		return ret;
	}

	@Override
	public synchronized boolean containsAll(Collection<?> collection) {
		Objects.requireNonNull(collection);
		return collection.stream().allMatch(elem -> contains(elem));
	}

	@Override
	public synchronized boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof HashArraySet)) {
			HashArraySet other = (HashArraySet) o;
			ret = (this.size == other.size);

			ret = ret && IntStream.range(0, this.array.length).map(index -> this.array[index])
					.filter(current -> current != EMPTY).allMatch(current -> other.contains(current));

			ret = ret && IntStream.range(0, other.array.length).map(index -> other.array[index])
					.filter(current -> current != EMPTY).allMatch(current -> contains(current));
		}
		return ret;
	}

	private int find(int elem, int[] currentArray) {
		if (elem == EMPTY) {
			throw new IllegalArgumentException("Out of range: " + elem);
		}
		if (elem == REHASH) {
			throw new IllegalArgumentException("Out of range: " + elem);
		}

		boolean indexFound = false;
		int pointer = REHASH;
		int mod = currentArray.length;
		int dist = 1;
		int initial = elem;
		while (!indexFound && (dist < mod)) {
			pointer = Math.abs(initial + dist) % mod;
			int element = currentArray[pointer];
			if (element == EMPTY) {
				pointer = (-1) * (pointer + 1);
				indexFound = true;
			} else {
				if (elem == element) {
					indexFound = true;
				} else {
					dist *= 2;
				}
			}
		}
		if (!indexFound) {
			pointer = REHASH;
		}

		return pointer;
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
		return new HashArraySetIterator(this.array, this.size);
	}

	private int[] makeNewArray(int size) {
		int[] ret = new int[size];
		IntStream.range(0, size).forEach(i -> {
			ret[i] = EMPTY;
		});
		return ret;
	}

	private boolean rehash(int[] originalArray, int[] newArray) {
		boolean ret = true;
		for (int index = 0; ret && (index < originalArray.length); index++) {
			int element = originalArray[index];
			if (element != EMPTY) {
				int pointer = find(element, newArray);
				if (pointer == REHASH) {
					ret = false;
				} else if (pointer < 0) {
					pointer = (-1) * (pointer + 1);
					newArray[pointer] = element;
				}
			}
		}
		return ret;
	}

	private int[] rehashAndAdd(int[] originalArray, int elem) {
		int[] newArray = originalArray;
		boolean rehashDone = false;
		while (!rehashDone) {
			int newLength = computeNewLength(newArray.length);
			newArray = makeNewArray(newLength);
			int newPointer = find(elem, newArray);
			newPointer = (-1) * (newPointer + 1);
			newArray[newPointer] = elem;

			rehashDone = rehash(originalArray, newArray);
		}
		return newArray;
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
		ArrayList<Integer> ret = new ArrayList<>();
		IntStream.range(0, this.array.length).forEach(index -> {
			int element = this.array[index];
			if (element != EMPTY) {
				ret.add(element);
			}
		});
		return ret;
	}

	@Override
	public synchronized String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[ ");
		IntStream.range(0, this.array.length).forEach(index -> {
			int element = this.array[index];
			if (element == EMPTY) {
				sbuf.append(".");
			} else {
				sbuf.append(element);
			}
			sbuf.append(" ");
		});
		sbuf.append("]");
		return sbuf.toString();
	}

}
