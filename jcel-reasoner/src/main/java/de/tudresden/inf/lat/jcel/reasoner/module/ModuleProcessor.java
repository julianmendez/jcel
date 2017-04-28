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

package de.tudresden.inf.lat.jcel.reasoner.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.common.UnclassifiedOntologyException;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;

/**
 * An object of this class classifies an ontology. It divides a set of axioms in
 * subsets such that they have disjoint sets of classes and properties. Then, it
 * uses the default processor to classify each subset. The resulting class and
 * property hierarchies are the union of the partial ones.
 * 
 * @author Julian Mendez
 */
public class ModuleProcessor implements Processor {

	private static final Logger logger = Logger.getLogger(ModuleProcessor.class.getName());

	private IntegerHierarchicalGraphImpl classHierarchy = null;
	private IntegerHierarchicalGraphImpl dataPropertyHierarchy = null;
	private Map<Integer, Set<Integer>> directTypes = null;
	private boolean isReady = false;
	private Integer moduleIndex = 0;
	private List<Set<ComplexIntegerAxiom>> moduleList = null;
	private IntegerHierarchicalGraphImpl objectPropertyHierarchy = null;
	private Processor processor = null;
	private final ModuleProcessorFactory processorFactory;
	private Map<Integer, Set<Integer>> sameIndividualMap = null;

	/**
	 * Constructs a new module processor. It uses an auxiliary processor to
	 * classify each module.
	 * 
	 * @param axiomSet
	 *            set of axioms
	 * @param factory
	 *            factory to create the auxiliary processor
	 */
	public ModuleProcessor(Set<ComplexIntegerAxiom> axiomSet, ModuleProcessorFactory factory) {
		Objects.requireNonNull(axiomSet);
		Objects.requireNonNull(factory);
		this.processorFactory = factory;
		preProcess(axiomSet);
	}

	private boolean containsAnyClass(Set<Integer> a, Set<Integer> b) {
		return b.stream()
				.filter(elem -> (!elem.equals(IntegerEntityManager.bottomClassId)
						&& !elem.equals(IntegerEntityManager.topClassId))) //
				.anyMatch(elem -> a.contains(elem));
	}

	private boolean containsAnyProperty(Set<Integer> a, Set<Integer> b) {
		return b.stream()
				.filter(elem -> (!elem.equals(IntegerEntityManager.bottomObjectPropertyId)
						&& !elem.equals(IntegerEntityManager.topObjectPropertyId))) //
				.anyMatch(elem -> a.contains(elem));
	}

	/**
	 * Divides a set of axioms in subsets that have disjoint sets of classes and
	 * properties.
	 * 
	 * @param axiomSet
	 *            the set of axioms
	 * @return the subsets with disjoint symbols
	 */
	private List<Set<ComplexIntegerAxiom>> findModules(Set<ComplexIntegerAxiom> axiomSet) {
		List<Set<ComplexIntegerAxiom>> ret = new ArrayList<>();
		Set<ComplexIntegerAxiom> toBeGrouped = new HashSet<>();
		toBeGrouped.addAll(axiomSet);
		while (!toBeGrouped.isEmpty()) {
			ComplexIntegerAxiom firstAxiom = toBeGrouped.iterator().next();
			toBeGrouped.remove(firstAxiom);
			Set<ComplexIntegerAxiom> group = new HashSet<>();
			group.add(firstAxiom);

			Set<Integer> classSet = new HashSet<>();
			classSet.addAll(firstAxiom.getClassesInSignature());
			Set<Integer> propertySet = new HashSet<>();
			propertySet.addAll(firstAxiom.getObjectPropertiesInSignature());

			boolean found = true;
			while (found) {
				found = false;
				Set<ComplexIntegerAxiom> toVisit = new HashSet<>();
				toVisit.addAll(toBeGrouped);
				for (ComplexIntegerAxiom currentAxiom : toVisit) {
					Set<Integer> classesInSignature = currentAxiom.getClassesInSignature();
					Set<Integer> propertiesInSignature = currentAxiom.getObjectPropertiesInSignature();
					if (containsAnyClass(classSet, classesInSignature)
							|| containsAnyProperty(propertySet, propertiesInSignature)) {
						toBeGrouped.remove(currentAxiom);
						propertySet.addAll(propertiesInSignature);
						classSet.addAll(classesInSignature);
						group.add(currentAxiom);
						found = true;
					}
				}
			}
			ret.add(group);
		}
		return ret;
	}

	@Override
	public IntegerHierarchicalGraph getClassHierarchy() {
		IntegerHierarchicalGraph ret = null;
		if (isReady()) {
			ret = this.classHierarchy;
		}
		return ret;
	}

	@Override
	public IntegerHierarchicalGraph getDataPropertyHierarchy() throws UnclassifiedOntologyException {
		IntegerHierarchicalGraph ret = null;
		if (isReady()) {
			ret = this.dataPropertyHierarchy;
		}
		return ret;
	}

	@Override
	public Map<Integer, Set<Integer>> getDirectTypes() {
		Map<Integer, Set<Integer>> ret = null;
		if (isReady()) {
			ret = this.directTypes;
		}
		return ret;
	}

	@Override
	public IntegerHierarchicalGraph getObjectPropertyHierarchy() {
		IntegerHierarchicalGraph ret = null;
		if (isReady()) {
			ret = this.objectPropertyHierarchy;
		}
		return ret;
	}

	@Override
	public Map<Integer, Set<Integer>> getSameIndividualMap() {
		Map<Integer, Set<Integer>> ret = null;
		if (isReady()) {
			ret = this.sameIndividualMap;
		}
		return ret;
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}

	private void preProcess(Set<ComplexIntegerAxiom> originalAxiomSet) {

		this.isReady = false;

		logger.fine("using " + getClass().getSimpleName() + " ...");

		this.dataPropertyHierarchy = new IntegerHierarchicalGraphImpl(new IntegerSubsumerGraphImpl(
				IntegerEntityManager.bottomDataPropertyId, IntegerEntityManager.topDataPropertyId));

		this.moduleList = findModules(originalAxiomSet);
		logger.fine("modules found : " + this.moduleList.size());
		IntStream.range(0, this.moduleList.size()).forEach(index -> {
			logger.fine("module " + index + " has " + this.moduleList.get(index).size() + " axioms");
		});

		this.moduleIndex = 0;
		this.classHierarchy = new IntegerHierarchicalGraphImpl(IntegerEntityManager.bottomClassId,
				IntegerEntityManager.topClassId);
		this.objectPropertyHierarchy = new IntegerHierarchicalGraphImpl(IntegerEntityManager.bottomObjectPropertyId,
				IntegerEntityManager.topObjectPropertyId);
		this.directTypes = new HashMap<>();
		this.sameIndividualMap = new HashMap<>();

		logger.fine("");
		logger.fine("");
		logger.fine("classifying module " + this.moduleIndex + " ...");

		this.processor = this.processorFactory.createProcessor(this.moduleList.get(this.moduleIndex));
	}

	@Override
	public boolean process() {
		if (!this.isReady) {
			boolean hasMoreEntries = this.processor.process();
			if (!hasMoreEntries) {
				this.classHierarchy.disjointUnion(this.processor.getClassHierarchy());
				this.objectPropertyHierarchy.disjointUnion(this.processor.getObjectPropertyHierarchy());
				this.directTypes.putAll(this.processor.getDirectTypes());
				this.sameIndividualMap.putAll(this.processor.getSameIndividualMap());
				this.processor = null;
				logger.fine("module " + this.moduleIndex + " has been classified.");
				logger.fine("");
				this.moduleIndex++;
				if (this.moduleIndex < this.moduleList.size()) {
					hasMoreEntries = true;
					logger.fine("classifying module " + this.moduleIndex + " ...");
					this.processor = this.processorFactory.createProcessor(this.moduleList.get(this.moduleIndex));
				}
				if (!hasMoreEntries) {
					this.isReady = true;
				}
			}
		}
		return !this.isReady;
	}

}
