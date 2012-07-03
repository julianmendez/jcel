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

import junit.framework.TestCase;

/**
 * Tests for efficient arrays.
 * 
 * @author Julian Mendez
 */
public class EfficientArrayTest extends TestCase {

	public EfficientArrayTest() {
	}

	public void testInsertion() {

		EfficientArray efficientArray = new EfficientArray();
		ArrayList<Integer> arrayList = new ArrayList<Integer>();

		for (int i = 0; i < 0x1000; i++) {
			int element = i % 0xff;
			efficientArray.add(element);
			arrayList.add(element);
		}

		for (int i = 0x1000; i < 0x10000; i++) {
			int element = i % 0xff;
			boolean b1 = arrayList.contains(element);
			boolean b2 = efficientArray.contains(element);
			assertEquals(b1, b2);
		}
	}

}
