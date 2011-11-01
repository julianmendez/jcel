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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is an iterator for an efficient array.
 * 
 * @see EfficientArray
 * 
 * @author Julian Mendez
 */
public class EfficientArrayIterator implements Iterator<Integer> {

	private int[] array = null;
	private int pointer = 0;
	private int size = 0;

	/**
	 * Constructs an iterator for an efficient array.
	 * 
	 * @param a
	 *            array of <code>int</code> of the efficient array
	 * @param s
	 *            number of elements to consider in the array
	 */
	public EfficientArrayIterator(int[] a, int s) {
		if (a == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.array = a;
		this.size = s;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof EfficientArrayIterator) {
			EfficientArrayIterator other = (EfficientArrayIterator) o;
			ret = (this.size == other.size) && (this.pointer == other.pointer);
			for (int index = 0; ret && index < size; index++) {
				ret = ret && (this.array[index] == other.array[index]);
			}
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return this.pointer + 31 * this.array.hashCode();
	}

	@Override
	public boolean hasNext() {
		return this.pointer < this.size;
	}

	@Override
	public Integer next() {
		if (this.pointer >= this.size) {
			throw new NoSuchElementException();
		}
		return this.array[this.pointer++];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
