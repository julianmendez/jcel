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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
			if (name == null) {
				throw new NullPointerException("Null argument.");
			}

			this.id = id;
			this.name = name;
		}

		@Override
		public int compareTo(PairIdName other) {
			if (other == null) {
				throw new NullPointerException("Null argument.");
			}

			int ret = getName().compareTo(other.getName());
			if (ret == 0) {
				ret = getId() - other.getId();
			}
			return ret;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

	}

	public static final String subClassOf = "isA";

	private Set<String> getNames(Collection<Integer> identifiers,
			IntegerEntityManager entityManager) throws IOException {
		Set<String> ret = new TreeSet<String>();
		for (int id : identifiers) {
			ret.add(entityManager.getName(id));
		}
		return ret;
	}

	public boolean renderWithNames(Writer output,
			IntegerEntityManager entityManager, IntegerRelationMap setR)
			throws IOException {
		if (output == null || entityManager == null || setR == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		TurtleRenderer renderer = new TurtleRenderer(output);
		renderer.loadPrefixes(getNames(setR.getElements(), entityManager));
		renderer.renderPrefixes();

		List<Integer> listOfPropertyIds = sortByName(setR.getElements(),
				entityManager);
		for (int propertyId : listOfPropertyIds) {

			IntegerBinaryRelation relation = setR.get(propertyId);
			String propertyName = entityManager.getName(propertyId);
			List<Integer> listOfLeftClassIds = sortByName(
					relation.getElements(), entityManager);
			for (int leftClassId : listOfLeftClassIds) {

				String leftClassName = entityManager.getName(leftClassId);
				List<Integer> listOfRightClassIds = sortByName(
						relation.getByFirst(leftClassId), entityManager);
				for (int rightClassId : listOfRightClassIds) {

					String rightClassName = entityManager.getName(rightClassId);
					renderer.renderTriple(propertyName, leftClassName,
							rightClassName);

					ret = true;
				}
			}
		}
		return ret;
	}

	public boolean renderWithNames(Writer output,
			IntegerEntityManager entityManager, IntegerSubsumerGraph setS)
			throws IOException {
		if (output == null || entityManager == null || setS == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		TurtleRenderer renderer = new TurtleRenderer(output);
		renderer.loadPrefixes(getNames(setS.getElements(), entityManager));
		renderer.renderPrefixes();

		List<Integer> listOfSubClassIds = sortByName(setS.getElements(),
				entityManager);
		for (int subClassId : listOfSubClassIds) {

			String subClassName = entityManager.getName(subClassId);
			List<Integer> listOfSuperClassIds = sortByName(
					setS.getSubsumers(subClassId), entityManager);
			for (int superClassId : listOfSuperClassIds) {

				String superClassName = entityManager.getName(superClassId);
				renderer.renderTriple(subClassOf, subClassName, superClassName);

				ret = true;
			}
		}
		return ret;
	}

	private List<Integer> sortByName(Collection<Integer> identifiers,
			IntegerEntityManager entityManager) {
		List<PairIdName> list = new ArrayList<PairIdName>();
		for (int id : identifiers) {
			list.add(new PairIdName(id, entityManager.getName(id)));
		}
		Collections.sort(list);
		List<Integer> ret = new ArrayList<Integer>();
		for (PairIdName pair : list) {
			ret.add(pair.getId());
		}
		return ret;
	}

}
