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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

/**
 * Tests for efficient arrays.
 * 
 * @author Julian Mendez
 */
public class ArraySetTest extends TestCase {

	public ArraySetTest() {
	}

	public void testInsertion() {

		ArraySet arraySet = new ArraySet();
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		Set<Integer> treeSet = new TreeSet<Integer>();

		for (int i = 0; i < 0x1000; i++) {
			int element = i % 0xff;
			treeSet.add(element);
			arrayList.add(element);
			arraySet.add(element);
			assertEquals(treeSet.size(), arraySet.size());
		}

		for (int i = 0x1000; i < 0x10000; i++) {
			int element = i % 0xff;
			boolean b1 = treeSet.contains(element);
			boolean b2 = arrayList.contains(element);
			boolean b3 = arraySet.contains(element);
			assertEquals(b1, b3);
			assertEquals(b2, b3);
		}
	}

	public void testIteration() {
		Set<Integer> treeSet = new TreeSet<Integer>();
		ArraySet arraySet = new ArraySet();

		for (int i = 0; i < 0x1000; i++) {
			int element = i % 0xf0;
			treeSet.add(element);
			arraySet.add(element);
			assertEquals(treeSet.size(), arraySet.size());
		}

		Iterator<Integer> it = treeSet.iterator();
		for (Integer e : arraySet) {
			assertEquals(it.next(), e);
		}
	}

}
