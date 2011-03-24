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

package de.tudresden.inf.lat.jcel.core.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;

/**
 * An object of this class classifies an ontology. It divides a set of axioms in
 * subsets such that they have disjoint sets of classes and properties. Then, it
 * uses the default processor to classify each subset. The resulting class and
 * property hierarchies are the union of the partial ones.
 * 
 * @author Julian Mendez
 */
public class ModuleProcessor implements Processor {

	private static final Logger logger = Logger.getLogger(ModuleProcessor.class
			.getName());

	private IntegerHierarchicalGraphImpl classHierarchy = null;
	private boolean isReady = false;
	private Integer moduleIndex = 0;
	private List<Set<IntegerAxiom>> moduleList = null;
	private IntegerHierarchicalGraphImpl objectPropertyHierarchy = null;
	private Processor processor = null;

	public ModuleProcessor(Set<IntegerAxiom> axiomSet) {
		preProcess(axiomSet);
	}

	private boolean containsAnyClass(Set<Integer> a, Set<Integer> b) {
		boolean ret = false;
		for (Iterator<Integer> it = b.iterator(); !ret && it.hasNext();) {
			Integer elem = it.next();
			if (!elem.equals(IntegerDatatype.classBottomElement)
					&& !elem.equals(IntegerDatatype.classTopElement)) {
				ret = ret || a.contains(elem);
			}
		}
		return ret;
	}

	private boolean containsAnyProperty(Set<Integer> a, Set<Integer> b) {
		boolean ret = false;
		for (Iterator<Integer> it = b.iterator(); !ret && it.hasNext();) {
			Integer elem = it.next();
			if (!elem.equals(IntegerDatatype.propertyBottomElement)
					&& !elem.equals(IntegerDatatype.propertyTopElement)) {
				ret = ret || a.contains(elem);
			}
		}
		return ret;
	}

	/**
	 * Divides a set of axioms in subsets that have disjoint sets of classes and
	 * properties.
	 * 
	 * @param axiomSet
	 *            the set of axioms
	 * @return the subsets with disjoint symbols
	 */
	protected List<Set<IntegerAxiom>> findModules(Set<IntegerAxiom> axiomSet) {
		List<Set<IntegerAxiom>> ret = new ArrayList<Set<IntegerAxiom>>();
		Set<IntegerAxiom> toBeGrouped = new HashSet<IntegerAxiom>();
		toBeGrouped.addAll(axiomSet);
		while (!toBeGrouped.isEmpty()) {
			IntegerAxiom firstAxiom = toBeGrouped.iterator().next();
			toBeGrouped.remove(firstAxiom);
			Set<IntegerAxiom> group = new HashSet<IntegerAxiom>();
			group.add(firstAxiom);

			Set<Integer> classSet = new HashSet<Integer>();
			classSet.addAll(firstAxiom.getClassesInSignature());
			Set<Integer> propertySet = new HashSet<Integer>();
			propertySet.addAll(firstAxiom.getObjectPropertiesInSignature());

			boolean found = true;
			while (found) {
				found = false;
				Set<IntegerAxiom> toVisit = new HashSet<IntegerAxiom>();
				toVisit.addAll(toBeGrouped);
				for (IntegerAxiom currentAxiom : toVisit) {
					Set<Integer> classesInSignature = currentAxiom
							.getClassesInSignature();
					Set<Integer> propertiesInSignature = currentAxiom
							.getObjectPropertiesInSignature();
					if (containsAnyClass(classSet, classesInSignature)
							|| containsAnyProperty(propertySet,
									propertiesInSignature)) {
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
	public IntegerHierarchicalGraph getObjectPropertyHierarchy() {
		IntegerHierarchicalGraph ret = null;
		if (isReady()) {
			ret = this.objectPropertyHierarchy;
		}
		return ret;
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}

	protected void preProcess(Set<IntegerAxiom> axioms) {
		this.isReady = false;
		this.moduleList = findModules(axioms);
		logger.fine("modules found : " + this.moduleList.size());
		for (int index = 0; index < this.moduleList.size(); index++) {
			logger.fine("module " + index + " has "
					+ this.moduleList.get(index).size() + " axioms");
		}

		this.moduleIndex = 0;
		this.classHierarchy = new IntegerHierarchicalGraphImpl(
				IntegerDatatype.classBottomElement,
				IntegerDatatype.classTopElement);
		this.objectPropertyHierarchy = new IntegerHierarchicalGraphImpl(
				IntegerDatatype.propertyBottomElement,
				IntegerDatatype.propertyTopElement);

		logger.fine("");
		logger.fine("classifying module " + this.moduleIndex + " ...");

		this.processor = new DefaultProcessor(this.moduleList
				.get(this.moduleIndex));
	}

	@Override
	public boolean process() {
		if (!this.isReady) {
			boolean hasMoreEntries = this.processor.process();
			if (!hasMoreEntries) {
				this.classHierarchy.disjointUnion(this.processor
						.getClassHierarchy());
				this.objectPropertyHierarchy.disjointUnion(this.processor
						.getObjectPropertyHierarchy());
				this.processor = null;
				logger.fine("running garbage collector ...");
				System.gc();
				logger.fine("module " + this.moduleIndex
						+ " has been classified.");
				logger.fine("");
				this.moduleIndex++;
				if (this.moduleIndex < this.moduleList.size()) {
					hasMoreEntries = true;
					logger.fine("classifying module " + this.moduleIndex
							+ " ...");
					this.processor = new DefaultProcessor(this.moduleList
							.get(this.moduleIndex));
				}
				if (!hasMoreEntries) {
					this.isReady = true;
				}
			}
		}
		return !this.isReady;
	}
}
