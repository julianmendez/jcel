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

package de.tudresden.inf.lat.jcel.core.algorithm.cel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.FunctObjectPropAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NominalAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI1Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RangeAxiom;
import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * This class models an extended ontology. This is referred in the documentation
 * as <i>&Ocirc;</i>.
 * 
 * @author Julian Mendez
 */
public class CelExtendedOntology implements NormalizedIntegerAxiomVisitor<Boolean> {

	private final OptMap<Integer, Set<ExtensionEntry>> ohatOfClass = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OptMap<Integer, Set<ExtensionEntry>>> ohatOfExistential = new OptMapImpl<>(
			new HashMap<>());
	private final OptMap<Integer, Set<RI3Axiom>> subPropertyAxiomSetByLeft = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<RI3Axiom>> subPropertyAxiomSetByRight = new OptMapImpl<>(new HashMap<>());

	/**
	 * Constructs a new CEL extended ontology.
	 */
	public CelExtendedOntology() {
	}

	private void addClassEntry(Integer classId, ExtensionEntry entry) {
		if (!this.ohatOfClass.get(classId).isPresent()) {
			this.ohatOfClass.put(classId, new HashSet<>());
		}
		this.ohatOfClass.get(classId).get().add(entry);
	}

	private void addTo(Integer property, RI3Axiom axiom, OptMap<Integer, Set<RI3Axiom>> map) {
		Optional<Set<RI3Axiom>> optAxiomSet = map.get(property);
		if (!optAxiomSet.isPresent()) {
			optAxiomSet = Optional.of(new HashSet<>());
			map.put(property, optAxiomSet.get());
		}
		optAxiomSet.get().add(axiom);
	}

	/**
	 * Clears all the internal sets.
	 */
	public void clear() {
		this.ohatOfClass.clear();
		this.ohatOfExistential.clear();
		this.subPropertyAxiomSetByLeft.clear();
		this.subPropertyAxiomSetByRight.clear();
	}

	/**
	 * Returns the class entries related to the specified class.
	 * 
	 * @param classId
	 *            class identifier
	 * @return the class entries related to the specified class
	 */
	public Set<ExtensionEntry> getClassEntries(Integer classId) {
		Objects.requireNonNull(classId);
		Optional<Set<ExtensionEntry>> optSet = this.ohatOfClass.get(classId);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	/**
	 * Returns the set of classes.
	 * 
	 * @return the set of classes
	 */
	public Set<Integer> getClassSet() {
		return Collections.unmodifiableSet(this.ohatOfClass.keySet());
	}

	/**
	 * Returns the existential entries related to the specified object property
	 * and class.
	 * 
	 * @param propertyId
	 *            object property identifier
	 * @param classId
	 *            class identifier
	 * @return the existential entries related to the specified object property
	 *         and class.
	 */
	public Set<ExtensionEntry> getExistentialEntries(Integer propertyId, Integer classId) {
		Objects.requireNonNull(propertyId);
		Objects.requireNonNull(classId);
		Set<ExtensionEntry> ret = Collections.emptySet();
		Optional<OptMap<Integer, Set<ExtensionEntry>>> optMap = this.ohatOfExistential.get(propertyId);
		if (optMap.isPresent()) {
			Optional<Set<ExtensionEntry>> optSet = optMap.get().get(classId);
			if (optSet.isPresent()) {
				ret = optSet.get();
			} else {
				ret = Collections.emptySet();
			}
		}
		return Collections.unmodifiableSet(ret);

	}

	/**
	 * Returns the set of sub object property axioms related where the specified
	 * object property occurs on the left-hand part of the composition.
	 * 
	 * @param elem
	 *            object property
	 * @return the set of sub object property axioms related where the specified
	 *         object property occurs on the left-hand part of the composition.
	 */
	public Set<RI3Axiom> getSubPropertyAxiomSetByLeft(Integer elem) {
		Objects.requireNonNull(elem);
		Optional<Set<RI3Axiom>> optSet = this.subPropertyAxiomSetByLeft.get(elem);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	/**
	 * Returns the set of sub object property axioms related where the specified
	 * object property occurs on the right-hand part of the composition.
	 * 
	 * @param elem
	 *            object property
	 * @return the set of sub object property axioms related where the specified
	 *         object property occurs on the right-hand part of the composition.
	 */
	public Set<RI3Axiom> getSubPropertyAxiomSetByRight(Integer elem) {
		Objects.requireNonNull(elem);
		Optional<Set<RI3Axiom>> optSet = this.subPropertyAxiomSetByRight.get(elem);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	/**
	 * Loads a set of normalized axioms.
	 * 
	 * @param axiomSet
	 *            set of normalized axioms
	 */
	public void load(Set<NormalizedIntegerAxiom> axiomSet) {
		Objects.requireNonNull(axiomSet);
		clear();
		axiomSet.forEach(axiom -> {
			axiom.accept(this);
			if (axiom instanceof RI3Axiom) {
				RI3Axiom subPropAxiom = (RI3Axiom) axiom;
				Integer left = subPropAxiom.getLeftSubProperty();
				Integer right = subPropAxiom.getRightSubProperty();
				addTo(left, subPropAxiom, this.subPropertyAxiomSetByLeft);
				addTo(right, subPropAxiom, this.subPropertyAxiomSetByRight);
			}
		});
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[");
		sbuf.append("OHat(cl)=" + this.ohatOfClass.toString());
		sbuf.append("OHat(ex)=" + this.ohatOfExistential.toString());
		sbuf.append("]");
		return sbuf.toString();
	}

	@Override
	public Boolean visit(FunctObjectPropAxiom axiom) {
		Objects.requireNonNull(axiom);
		return false;
	}

	@Override
	public Boolean visit(GCI0Axiom axiom) {
		Objects.requireNonNull(axiom);
		addClassEntry(axiom.getSubClass(), new ImplicationEntry(new HashSet<>(), axiom.getSuperClass()));
		return true;
	}

	@Override
	public Boolean visit(GCI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		Integer superClass = axiom.getSuperClass();
		List<Integer> operandSet = new ArrayList<>();
		operandSet.add(axiom.getLeftSubClass());
		operandSet.add(axiom.getRightSubClass());
		operandSet.forEach(currentOperand -> {
			Set<Integer> currentSet = new HashSet<>();
			currentSet.addAll(operandSet);
			currentSet.remove(currentOperand);
			addClassEntry(currentOperand, new ImplicationEntry(currentSet, superClass));
		});
		return true;
	}

	@Override
	public Boolean visit(GCI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		addClassEntry(axiom.getSubClass(),
				new ExistentialEntry(axiom.getPropertyInSuperClass(), axiom.getClassInSuperClass()));
		return true;
	}

	@Override
	public Boolean visit(GCI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		ExtensionEntry entry = new ImplicationEntry(new HashSet<>(), axiom.getSuperClass());
		Integer propertyId = axiom.getPropertyInSubClass();
		Integer classId = axiom.getClassInSubClass();
		Optional<OptMap<Integer, Set<ExtensionEntry>>> optMap = this.ohatOfExistential.get(propertyId);
		OptMap<Integer, Set<ExtensionEntry>> map = null;
		if (optMap.isPresent()) {
			map = optMap.get();
		} else {
			map = new OptMapImpl<>(new HashMap<>());
			this.ohatOfExistential.put(propertyId, map);
		}
		Optional<Set<ExtensionEntry>> optSet = map.get(classId);
		Set<ExtensionEntry> set = null;
		if (optSet.isPresent()) {
			set = optSet.get();
		} else {
			set = new HashSet<>();
			map.put(classId, set);
		}
		set.add(entry);
		return true;
	}

	@Override
	public Boolean visit(NominalAxiom axiom) {
		Objects.requireNonNull(axiom);
		return false;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		return false;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		return false;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		return false;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		return false;
	}

}
