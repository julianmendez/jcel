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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for efficient arrays.
 * 
 * @author Julian Mendez
 */
public class HashArraySetTest {

	public HashArraySetTest() {
	}

	private boolean assertInclusion(Collection<Integer> c1, Collection<Integer> c2) {
		boolean ret = true;
		c1.forEach(e -> {
			Assert.assertTrue(c2.contains(e));
		});
		return ret;
	}

	@Test
	public void testInsertion() {

		HashArraySet arraySet = new HashArraySet();
		ArrayList<Integer> arrayList = new ArrayList<>();
		Set<Integer> treeSet = new TreeSet<>();

		IntStream.range(0, 0x1000).forEach(i -> {
			int element = i % 0xf0;
			treeSet.add(element);
			arrayList.add(element);
			arraySet.add(element);
			Assert.assertEquals(treeSet.size(), arraySet.size());
		});

		IntStream.range(0x1000, 0x10000).forEach(i -> {
			int element = i % 0xFF;
			boolean b1 = treeSet.contains(element);
			boolean b2 = arrayList.contains(element);
			boolean b3 = arraySet.contains(element);
			Assert.assertEquals(b1, b3);
			Assert.assertEquals(b2, b3);
		});
	}

	@Test
	public void testIteration() {
		Set<Integer> treeSet = new TreeSet<>();
		HashArraySet arraySet = new HashArraySet();

		IntStream.range(0, 0x1000).forEach(i -> {
			int element = i % 0xF0;
			treeSet.add(element);
			arraySet.add(element);
			Assert.assertEquals(treeSet.size(), arraySet.size());
		});

		assertInclusion(treeSet, arraySet);
		assertInclusion(arraySet, treeSet);

		IntStream.range(0, 0x618).map(x -> (x * 0x2A)).forEach(i -> {
			int element = i % 0xF08;
			treeSet.add(element);
			arraySet.add(element);
			Assert.assertEquals(treeSet.size(), arraySet.size());
		});

		assertInclusion(treeSet, arraySet);
		assertInclusion(arraySet, treeSet);

		IntStream.range(0, 0x1000).forEach(i -> {
			int element = i % 0xF0;
			treeSet.add(element);
			arraySet.add(element);
			Assert.assertEquals(treeSet.size(), arraySet.size());
		});

		assertInclusion(treeSet, arraySet);
		assertInclusion(arraySet, treeSet);
	}

}
