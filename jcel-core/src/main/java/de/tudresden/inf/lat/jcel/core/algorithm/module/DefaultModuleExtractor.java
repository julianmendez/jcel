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

package de.tudresden.inf.lat.jcel.core.algorithm.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;

/**
 * An object of this class is a module extractor, i.e. it can extract a subset
 * of axioms that are relevant to answer a query.
 * 
 * @author Julian Mendez
 */
public class DefaultModuleExtractor {

	/**
	 * Constructs a new module extractor.
	 */
	public DefaultModuleExtractor() {
	}

	/**
	 * Returns a map that relates a class with the set of axioms where this
	 * class occurs on the left side of the axiom
	 * 
	 * @param normalizedAxioms
	 *            normalized axioms
	 * @return a map that relates a class with the set of axioms where this
	 *         class occurs on the left side of the axiom
	 */
	Map<Integer, Set<DefaultIdentifierCollector>> buildMapOfAxioms(Set<DefaultIdentifierCollector> normalizedAxioms) {
		Map<Integer, Set<DefaultIdentifierCollector>> map = new HashMap<>();
		normalizedAxioms.forEach(axiom -> {
			Set<Integer> classesOnTheLeft = axiom.getClassesOnTheLeft();
			classesOnTheLeft.forEach(classId -> {
				Set<DefaultIdentifierCollector> value = map.get(classId);
				if (Objects.isNull(value)) {
					value = new HashSet<>();
					map.put(classId, value);
				}
				value.add(axiom);
			});
		});
		return map;
	}

	Set<NormalizedIntegerAxiom> getAxiomsWithoutEntitiesOnTheLeft(Set<DefaultIdentifierCollector> axioms) {
		Set<NormalizedIntegerAxiom> ret = new HashSet<>();
		axioms.forEach(axiom -> {
			if (axiom.getClassesOnTheLeft().isEmpty() && axiom.getObjectPropertiesOnTheLeft().isEmpty()) {
				ret.add(axiom.getAxiom());
			}
		});
		return ret;
	}

	Set<DefaultIdentifierCollector> getAxiomsWithClassesOnTheLeft(Set<Integer> classesToVisit,
			Map<Integer, Set<DefaultIdentifierCollector>> map) {
		Set<DefaultIdentifierCollector> ret = new HashSet<>();
		classesToVisit.forEach(classId -> {
			Set<DefaultIdentifierCollector> newAxioms = map.get(classId);
			if (Objects.nonNull(newAxioms)) {
				ret.addAll(newAxioms);
			}
		});
		return ret;
	}

	Set<Integer> getEntities(IntegerAxiom axiom) {
		Set<Integer> ret = new TreeSet<>();
		ret.addAll(axiom.getClassesInSignature());
		ret.addAll(axiom.getObjectPropertiesInSignature());
		ret.addAll(axiom.getIndividualsInSignature());
		ret.addAll(axiom.getDataPropertiesInSignature());
		ret.addAll(axiom.getDatatypesInSignature());
		return ret;
	}

	/**
	 * Returns a module, i.e. a subset of axioms relevant to answer a query.
	 * 
	 * @param setOfAxioms
	 *            set of axioms
	 * @param setOfClasses
	 *            set of classes
	 * @return a module, i.e. a subset of axioms relevant to answer a query
	 */
	public Module extractModule(Collection<NormalizedIntegerAxiom> setOfAxioms, Set<Integer> setOfClasses) {

		Set<NormalizedIntegerAxiom> newAxioms = new HashSet<>();

		Set<DefaultIdentifierCollector> axioms = new HashSet<>();
		setOfAxioms.forEach(axiom -> axioms.add(new DefaultIdentifierCollector(axiom)));

		newAxioms.addAll(getAxiomsWithoutEntitiesOnTheLeft(axioms));

		Map<Integer, Set<DefaultIdentifierCollector>> map = buildMapOfAxioms(axioms);

		Set<Integer> visitedClasses = new TreeSet<>();
		Set<Integer> classesToVisit = new TreeSet<>();
		classesToVisit.addAll(setOfClasses);
		int resultSize = -1;
		while (newAxioms.size() > resultSize) {
			resultSize = newAxioms.size();

			Set<DefaultIdentifierCollector> axiomsToVisit = getAxiomsWithClassesOnTheLeft(classesToVisit, map);
			visitedClasses.addAll(classesToVisit);
			classesToVisit.clear();

			axiomsToVisit.forEach(axiom -> {
				classesToVisit.addAll(axiom.getClassesOnTheRight());
				newAxioms.add(axiom.getAxiom());
			});
			classesToVisit.removeAll(visitedClasses);
		}

		Set<Integer> entities = new TreeSet<>();
		entities.addAll(visitedClasses);
		newAxioms.forEach(axiom -> entities.addAll(getEntities(axiom)));
		return new Module(entities, newAxioms);
	}

}
