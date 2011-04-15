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

package de.tudresden.inf.lat.jcel.core.algorithm.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubObjectPropertyOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectProperty;

/**
 * An object of this class classifies an ontology. It divides a set of axioms in
 * subsets such that they have disjoint sets of classes. These subsets are
 * called modules. If two classes share at least one axiom, they are in the same
 * module. Axioms without classes are in all the modules. <br />
 * After classifying each module, the processor creates a set of axioms which is
 * the result of the classification. After classifying all modules, it
 * classifies the accumulated set of axioms.
 * 
 * @author Julian Mendez
 * 
 */
public class ClassModuleProcessor implements Processor {

	private static final Integer classBottomElement = IntegerDatatype.classBottomElement;
	private static final Integer classTopElement = IntegerDatatype.classTopElement;
	private static final Logger logger = Logger
			.getLogger(ClassModuleProcessor.class.getName());

	private Set<ComplexIntegerAxiom> accumulatedAxiomSet = null;
	private IntegerHierarchicalGraph classHierarchy = null;
	private Map<Integer, Set<ComplexIntegerAxiom>> classToAxiom = new HashMap<Integer, Set<ComplexIntegerAxiom>>();
	private Map<Integer, Set<Integer>> classToClass = new HashMap<Integer, Set<Integer>>();
	private Map<Integer, Set<Integer>> directTypes = null;
	private boolean finalClassification = false;
	private boolean isReady = false;
	private Integer moduleIndex = 0;
	private List<Set<ComplexIntegerAxiom>> moduleList = null;
	private IntegerHierarchicalGraph objectPropertyHierarchy = null;
	private Set<ComplexIntegerAxiom> originalAxiomSet = null;
	private Processor processor = null;
	private ModuleProcessorFactory processorFactory = null;
	private Map<Integer, Set<Integer>> sameIndividualMap = null;
	private Set<ComplexIntegerAxiom> sharedAxioms = new HashSet<ComplexIntegerAxiom>();

	/**
	 * Constructs a class module processor. It uses an auxiliary processor to
	 * classify each module.
	 * 
	 * @param axiomSet
	 *            set of axioms
	 * @param factory
	 *            factory to create the auxiliary processor
	 */
	public ClassModuleProcessor(Set<ComplexIntegerAxiom> axiomSet,
			ModuleProcessorFactory factory) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.processorFactory = factory;
		preProcess(axiomSet);
	}

	private Set<ComplexIntegerAxiom> convertClassHierarchyToAxioms(
			IntegerHierarchicalGraph classGraph) {
		Set<ComplexIntegerAxiom> ret = new HashSet<ComplexIntegerAxiom>();
		for (Integer subClass : classGraph.getElements()) {

			Set<Integer> superSet = classGraph.getParents(subClass);
			for (Integer superClass : superSet) {
				ret.add(new IntegerSubClassOfAxiom(new IntegerClass(subClass),
						new IntegerClass(superClass)));
			}

			Set<Integer> equivSet = classGraph.getEquivalents(subClass);
			Set<IntegerClassExpression> equivalentClassSet = new HashSet<IntegerClassExpression>();
			for (Integer equivalentClass : equivSet) {
				equivalentClassSet.add(new IntegerClass(equivalentClass));
			}
			ret.add(new IntegerEquivalentClassesAxiom(equivalentClassSet));

		}

		return ret;
	}

	private Set<ComplexIntegerAxiom> convertObjectPropertyHierarchyToAxioms(
			IntegerHierarchicalGraph objectPropertyGraph) {
		Set<ComplexIntegerAxiom> ret = new HashSet<ComplexIntegerAxiom>();
		for (Integer subObjectProperty : objectPropertyGraph.getElements()) {

			Set<Integer> set = objectPropertyGraph
					.getParents(subObjectProperty);
			for (Integer superObjectProperty : set) {
				ret.add(new IntegerSubObjectPropertyOfAxiom(
						new IntegerObjectProperty(subObjectProperty),
						new IntegerObjectProperty(superObjectProperty)));
			}

			ret.add(new IntegerEquivalentObjectPropertiesAxiom(
					objectPropertyGraph.getEquivalents(subObjectProperty)));
		}

		return ret;
	}

	private void createMaps(Set<ComplexIntegerAxiom> axiomSet) {

		this.classToAxiom.clear();
		this.classToClass.clear();
		this.sharedAxioms.clear();

		for (ComplexIntegerAxiom axiom : axiomSet) {
			Set<Integer> classSet = axiom.getClassesInSignature();
			if (classSet.isEmpty()) {
				this.sharedAxioms.add(axiom);
			} else {
				for (Integer classId : classSet) {

					Set<ComplexIntegerAxiom> complexAxioms = this.classToAxiom
							.get(classId);
					if (complexAxioms == null) {
						complexAxioms = new HashSet<ComplexIntegerAxiom>();
						this.classToAxiom.put(classId, complexAxioms);
					}
					complexAxioms.add(axiom);

					Set<Integer> otherClasses = this.classToClass.get(classId);
					if (otherClasses == null) {
						otherClasses = new HashSet<Integer>();
						this.classToClass.put(classId, otherClasses);
					}
					otherClasses.addAll(classSet);

				}
			}
		}

	}

	private List<Set<ComplexIntegerAxiom>> findModules(
			Set<ComplexIntegerAxiom> axiomSet) {

		createMaps(axiomSet);

		List<Set<ComplexIntegerAxiom>> ret = new ArrayList<Set<ComplexIntegerAxiom>>();
		List<Set<Integer>> clustersOfClasses = getClustersOfClasses(axiomSet);
		for (Set<Integer> classSet : clustersOfClasses) {
			Set<ComplexIntegerAxiom> currentModule = new HashSet<ComplexIntegerAxiom>();
			for (Integer classId : classSet) {
				Set<ComplexIntegerAxiom> reachable = this.classToAxiom
						.get(classId);
				if (reachable != null) {
					currentModule.addAll(reachable);
				}
			}
			currentModule.addAll(this.sharedAxioms);
			ret.add(currentModule);
		}

		return ret;
	}

	@Override
	public Set<ComplexIntegerAxiom> getAxiomSet() {
		return this.originalAxiomSet;
	}

	@Override
	public IntegerHierarchicalGraph getClassHierarchy() {
		IntegerHierarchicalGraph ret = null;
		if (isReady()) {
			ret = this.classHierarchy;
		}
		return ret;
	}

	private List<Set<Integer>> getClustersOfClasses(
			Set<ComplexIntegerAxiom> axiomSet) {

		List<Set<Integer>> ret = new ArrayList<Set<Integer>>();

		Set<Integer> allClasses = new TreeSet<Integer>();
		for (ComplexIntegerAxiom axiom : axiomSet) {
			allClasses.addAll(axiom.getClassesInSignature());
		}
		allClasses.remove(classBottomElement);
		allClasses.remove(classTopElement);

		while (!allClasses.isEmpty()) {
			Integer representativeId = allClasses.iterator().next();
			allClasses.remove(representativeId);
			Set<Integer> classSet = getReachableClasses(representativeId,
					axiomSet);
			allClasses.removeAll(classSet);
			ret.add(classSet);
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

	private Set<Integer> getReachableClasses(Integer firstClassId,
			Set<ComplexIntegerAxiom> axiomSet) {
		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> toVisit = new HashSet<Integer>();
		toVisit.add(firstClassId);
		while (!toVisit.isEmpty()) {
			Integer classId = toVisit.iterator().next();
			ret.add(classId);
			Set<Integer> reachable = this.classToClass.get(classId);
			if (reachable != null) {
				toVisit.addAll(reachable);
			}
			toVisit.removeAll(ret);
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

	private void preProcess(Set<ComplexIntegerAxiom> axioms) {

		this.isReady = false;
		this.finalClassification = false;

		logger.fine("using " + getClass().getSimpleName() + " ...");

		this.moduleList = findModules(axioms);
		logger.fine("modules found : " + this.moduleList.size());
		for (int index = 0; index < this.moduleList.size(); index++) {
			logger.fine("module " + index + " has "
					+ this.moduleList.get(index).size() + " axioms");
		}

		this.moduleIndex = 0;
		this.accumulatedAxiomSet = new HashSet<ComplexIntegerAxiom>();
		this.directTypes = new HashMap<Integer, Set<Integer>>();
		this.sameIndividualMap = new HashMap<Integer, Set<Integer>>();

		logger.fine("");
		logger.fine("");
		logger.fine("classifying module " + this.moduleIndex + " ...");

		this.processor = this.processorFactory.createProcessor(this.moduleList
				.get(this.moduleIndex));
	}

	@Override
	public boolean process() {
		if (!this.isReady) {
			boolean hasMoreEntries = this.processor.process();
			if (!hasMoreEntries) {
				if (this.finalClassification) {
					this.classHierarchy = this.processor.getClassHierarchy();
					this.objectPropertyHierarchy = this.processor
							.getObjectPropertyHierarchy();
					this.isReady = true;

				} else {
					hasMoreEntries = true;
					this.directTypes.putAll(this.processor.getDirectTypes());
					this.sameIndividualMap.putAll(this.processor
							.getSameIndividualMap());
					this.accumulatedAxiomSet
							.addAll(convertClassHierarchyToAxioms(this.processor
									.getClassHierarchy()));
					this.accumulatedAxiomSet
							.addAll(convertObjectPropertyHierarchyToAxioms(this.processor
									.getObjectPropertyHierarchy()));
					this.processor = null;
					logger.fine("module " + this.moduleIndex
							+ " has been classified.");
					logger.fine("");
					this.moduleIndex++;
					if (this.moduleIndex < this.moduleList.size()) {
						logger.fine("classifying module " + this.moduleIndex
								+ " ...");
						this.processor = this.processorFactory
								.createProcessor(this.moduleList
										.get(this.moduleIndex));
					} else {
						this.finalClassification = true;
						logger.fine("classifying integration module ...");
						this.processor = this.processorFactory
								.createProcessor(this.accumulatedAxiomSet);
					}
				}
			}
		}
		return !this.isReady;
	}

}
