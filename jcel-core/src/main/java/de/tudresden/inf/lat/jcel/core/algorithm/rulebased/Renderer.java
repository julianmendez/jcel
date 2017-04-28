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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import de.tudresden.inf.lat.jcel.core.graph.IntegerBinaryRelation;
import de.tudresden.inf.lat.jcel.core.graph.IntegerRelationMap;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * An object of this class renders the important data structures used in the
 * classification.
 *
 * @author Julian Mendez
 */
public class Renderer {

	private class PairIdName implements Comparable<PairIdName> {

		private final int id;
		private final String name;

		public PairIdName(int id, String name) {
			Objects.requireNonNull(name);
			this.id = id;
			this.name = name;
		}

		@Override
		public int compareTo(PairIdName other) {
			Objects.requireNonNull(other);
			int ret = getName().compareTo(other.getName());
			if (ret == 0) {
				ret = getId() - other.getId();
			}
			return ret;
		}

		public int getId() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}

	}

	public static final String subClassOf = "isA";

	private Set<String> getNames(Collection<Integer> identifiers, IntegerEntityManager entityManager) {
		Set<String> ret = new TreeSet<>();
		identifiers.forEach(id -> ret.add(entityManager.getName(id)));
		return ret;
	}

	/**
	 * Renders set R using Turtle syntax.
	 *
	 * @param output
	 *            writer
	 * @param entityManager
	 *            entity manager
	 * @param setR
	 *            set R
	 * @return <code>true</code> if and only if it renders at least one triple
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public boolean renderWithNames(Writer output, IntegerEntityManager entityManager, IntegerRelationMap setR)
			throws IOException {
		Objects.requireNonNull(output);
		Objects.requireNonNull(entityManager);
		Objects.requireNonNull(setR);

		boolean ret = false;
		TurtleRenderer renderer = new TurtleRenderer(output);
		renderer.loadPrefixes(getNames(setR.getElements(), entityManager));
		renderer.renderPrefixes();

		List<Integer> listOfPropertyIds = sortByName(setR.getElements(), entityManager);
		for (int propertyId : listOfPropertyIds) {

			IntegerBinaryRelation relation = setR.get(propertyId);
			String propertyName = entityManager.getName(propertyId);
			List<Integer> listOfLeftClassIds = sortByName(relation.getElements(), entityManager);
			for (int leftClassId : listOfLeftClassIds) {

				String leftClassName = entityManager.getName(leftClassId);
				List<Integer> listOfRightClassIds = sortByName(relation.getByFirst(leftClassId), entityManager);
				for (int rightClassId : listOfRightClassIds) {

					String rightClassName = entityManager.getName(rightClassId);
					renderer.renderTriple(propertyName, leftClassName, rightClassName);

					ret = true;
				}
			}
		}
		return ret;
	}

	/**
	 * Renders set S using Turtle syntax.
	 *
	 * @param output
	 *            writer
	 * @param entityManager
	 *            entity manager
	 * @param setS
	 *            set S
	 * @return <code>true</code> if and only if it renders at least one triple
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public boolean renderWithNames(Writer output, IntegerEntityManager entityManager, IntegerSubsumerGraph setS)
			throws IOException {
		Objects.requireNonNull(output);
		Objects.requireNonNull(entityManager);
		Objects.requireNonNull(setS);

		boolean ret = false;
		TurtleRenderer renderer = new TurtleRenderer(output);
		renderer.loadPrefixes(getNames(setS.getElements(), entityManager));
		renderer.renderPrefixes();

		List<Integer> listOfSubClassIds = sortByName(setS.getElements(), entityManager);
		for (int subClassId : listOfSubClassIds) {

			String subClassName = entityManager.getName(subClassId);
			List<Integer> listOfSuperClassIds = sortByName(setS.getSubsumers(subClassId), entityManager);
			for (int superClassId : listOfSuperClassIds) {

				String superClassName = entityManager.getName(superClassId);
				renderer.renderTriple(subClassOf, subClassName, superClassName);

				ret = true;
			}
		}
		return ret;
	}

	private List<Integer> sortByName(Collection<Integer> identifiers, IntegerEntityManager entityManager) {
		List<PairIdName> list = new ArrayList<>();
		identifiers.forEach(id -> list.add(new PairIdName(id, entityManager.getName(id))));
		Collections.sort(list);
		List<Integer> ret = new ArrayList<>();
		list.forEach(pair -> ret.add(pair.getId()));
		return ret;
	}

}
