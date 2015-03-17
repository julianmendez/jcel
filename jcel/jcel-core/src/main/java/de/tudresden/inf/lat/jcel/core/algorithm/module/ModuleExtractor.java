/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core.algorithm.module;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;

/**
 * An object of this class is a module extractor, i.e. it can extract a subset
 * of axioms that are relevant to answer a query.
 * 
 * @author Julian Mendez
 */
public class ModuleExtractor {

	/**
	 * Constructs a new module extractor.
	 */
	public ModuleExtractor() {
	}

	/**
	 * Returns the class identifiers found on the left-hand side of the given
	 * axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the class identifiers found on the left-hand side of the given
	 *         axiom
	 */
	Set<Integer> getClassesOnTheLeft(NormalizedIntegerAxiom axiom) {
		// FIXME
		return new TreeSet<Integer>();
	}

	/**
	 * Returns the class identifiers found on the right-hand side of the given
	 * axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the class identifiers found on the right-hand side of the given
	 *         axiom
	 */
	Set<Integer> getClassesOnTheRight(NormalizedIntegerAxiom axiom) {
		// FIXME
		return new TreeSet<Integer>();
	}

	/**
	 * Returns the object property identifiers found on the left-hand side of
	 * the given axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the object property identifiers found on the left-hand side of
	 *         the given axiom
	 */
	Set<Integer> getObjectPropertiesOnTheLeft(NormalizedIntegerAxiom axiom) {
		// FIXME
		return new TreeSet<Integer>();
	}

	/**
	 * Returns the object property found on the right-hand side of the given
	 * axiom.
	 * 
	 * @param axiom
	 *            axiom
	 * @return the object property identifiers found on the right-hand side of
	 *         the given axiom
	 */
	Set<Integer> getObjectPropertiesOnTheRight(NormalizedIntegerAxiom axiom) {
		// FIXME
		return new TreeSet<Integer>();
	}

	/**
	 * Tells whether the given sets have an element in common.
	 * 
	 * @param set0
	 *            set (expected to be smaller)
	 * @param set1
	 *            set (expected to be larger)
	 * @return <code>true</code> if and only if the given set have an element in
	 *         common
	 */
	boolean nonEmptyIntersection(Set<Integer> set0, Set<Integer> set1) {
		boolean ret = false;
		Iterator<Integer> it = set0.iterator();
		while (!ret && it.hasNext()) {
			ret |= set1.contains(it.hasNext());
		}
		return ret;
	}

	/**
	 * Returns a module, i.e. a subset of axioms relevant to answer a query.
	 * 
	 * @param setOfAxioms
	 *            set of axioms
	 * @param setOfClasses
	 *            set of classes
	 * @param setOfObjectProperties
	 *            set of object properties
	 * @return a module, i.e. a subset of axioms relevant to answer a query
	 */
	public Set<NormalizedIntegerAxiom> extractModule(
			Collection<NormalizedIntegerAxiom> setOfAxioms,
			Set<Integer> setOfClasses, Set<Integer> setOfObjectProperties) {

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();

		Set<Integer> classes = new HashSet<Integer>();
		classes.addAll(setOfClasses);
		Set<Integer> objectProperties = new HashSet<Integer>();
		objectProperties.addAll(setOfObjectProperties);

		boolean found = true;
		while (found) {
			found = false;
			for (NormalizedIntegerAxiom axiom : setOfAxioms) {
				Set<Integer> classesOnTheLeft = getClassesOnTheLeft(axiom);
				Set<Integer> objectPropertiesOnTheLeft = getObjectPropertiesOnTheLeft(axiom);

				if (nonEmptyIntersection(classesOnTheLeft, classes)
						|| (classesOnTheLeft.isEmpty() && nonEmptyIntersection(
								objectPropertiesOnTheLeft, objectProperties))) {
					found = true;
					classes.addAll(getClassesOnTheRight(axiom));
					objectProperties
							.addAll(getObjectPropertiesOnTheRight(axiom));
					ret.add(axiom);
				}

			}
		}
		return ret;
	}

}
