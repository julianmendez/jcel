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
		ArrayList<Integer> arrayList = new ArrayList<>();
		Set<Integer> treeSet = new TreeSet<>();

		for (int i = 0; i < 0x1000; i++) {
			int element = i % 0xFF;
			treeSet.add(element);
			arrayList.add(element);
			arraySet.add(element);
			assertEquals(treeSet.size(), arraySet.size());
		}

		for (int i = 0x1000; i < 0x10000; i++) {
			int element = i % 0xFF;
			boolean b1 = treeSet.contains(element);
			boolean b2 = arrayList.contains(element);
			boolean b3 = arraySet.contains(element);
			assertEquals(b1, b3);
			assertEquals(b2, b3);
		}
	}

	public void testIteration() {
		Set<Integer> treeSet = new TreeSet<>();
		ArraySet arraySet = new ArraySet();

		for (int i = 0; i < 0x1000; i++) {
			int element = i % 0xF0;
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
