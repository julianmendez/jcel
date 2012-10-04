/*
 * Copyright 2009 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 * jcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.core.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class implements an efficient array. It is an array of <code>int</code>
 * with exponential growth.
 * 
 * @author Julian Mendez
 */
public class EfficientArray implements Collection<Integer> {

	private static final int exponentialGrowthFactor = 2;
	private static final int initialSize = 1;
	private static final int linearGrowthFactor = 0;

	private int[] array = null;
	private int size = 0;

	/**
	 * Constructs an empty efficient array.
	 */
	public EfficientArray() {
		clear();
	}

	@Override
	public boolean add(Integer elem) {
		if (elem == null) {
			throw new NullPointerException("Null argument.");
		}

		boolean ret = false;
		int pointer = Arrays.binarySearch(this.array, 0, this.size, elem);
		if (pointer < 0) {
			pointer = (-1) * (pointer + 1);
			ret = true;
			if (this.size >= this.array.length) {
				this.array = Arrays.copyOf(this.array, linearGrowthFactor
						+ (exponentialGrowthFactor * this.array.length));
			}

			for (int i = this.size - 1; i >= pointer; i--) {
				this.array[i + 1] = this.array[i];
			}
			this.array[pointer] = elem;
			this.size++;
		}
		return ret;
	}

	@Override
	public boolean addAll(Collection<? extends Integer> collection) {
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
	public void clear() {
		this.array = new int[initialSize];
		this.size = 0;
	}

	@Override
	public boolean contains(Object elem) {
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
	public boolean containsAll(Collection<?> collection) {
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
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof EfficientArray) {
			EfficientArray other = (EfficientArray) o;
			ret = (this.size == other.size);
			for (int index = 0; ret && index < this.size; index++) {
				ret = ret && (this.array[index] == other.array[index]);
			}
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return this.array.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new EfficientArrayIterator(this.array, this.size);
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Object[] toArray() {
		Object[] ret = new Object[size()];
		for (int index = 0; index < this.size; index++) {
			ret[index] = this.array[index];
		}
		return ret;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
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
