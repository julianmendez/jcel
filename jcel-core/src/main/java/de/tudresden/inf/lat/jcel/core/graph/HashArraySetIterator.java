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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This class implements an iterator for a hash array set.
 * 
 * @see HashArraySet
 * 
 * @author Julian Mendez
 */
public class HashArraySetIterator implements Iterator<Integer> {

	private final int[] array;
	private int count = 0;
	private int pointer = 0;
	private final int size;

	/**
	 * Constructs an iterator for an array set.
	 * 
	 * @param a
	 *            array of <code>int</code> of the array set
	 * @param s
	 *            number of elements to consider in the array
	 */
	public HashArraySetIterator(int[] a, int s) {
		Objects.requireNonNull(a);
		this.array = a;
		this.size = s;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof HashArraySetIterator)) {
			HashArraySetIterator other = (HashArraySetIterator) o;
			ret = (this.size == other.size) && (this.pointer == other.pointer) && (this.count == other.count);
			ret = ret && IntStream.range(0, this.size).allMatch(index -> (this.array[index] == other.array[index]));
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return this.pointer + (31 * this.array.hashCode());
	}

	@Override
	public boolean hasNext() {
		return this.count < this.size;
	}

	@Override
	public Integer next() {
		if (this.count >= this.size) {
			throw new NoSuchElementException();
		}
		while ((this.pointer < this.array.length) && (this.array[this.pointer] == HashArraySet.EMPTY)) {
			this.pointer++;
		}
		if (this.pointer == this.array.length) {
			throw new IllegalStateException("Illegal state: " + toString());
		}
		int ret = this.array[this.pointer];
		this.pointer++;
		this.count++;
		return ret;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("at ");
		sbuf.append(this.count);
		sbuf.append("/");
		sbuf.append(this.size);
		sbuf.append(" (");
		sbuf.append(this.pointer);
		sbuf.append("/");
		sbuf.append(this.array.length);
		sbuf.append(")   ");
		sbuf.append("[ ");
		IntStream.range(0, this.array.length).forEach(index -> {
			int element = this.array[index];
			if (index == this.pointer) {
				sbuf.append("^");
			}
			if (element == HashArraySet.EMPTY) {
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
