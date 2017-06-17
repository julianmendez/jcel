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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.common.OptMap;
import de.tudresden.inf.lat.jcel.coreontology.common.OptMapImpl;

/**
 * This class models an ontology with the property to look up by axiom type.
 * 
 * @author Julian Mendez
 */
public class ExtendedOntologyImpl implements ExtendedOntology, NormalizedIntegerAxiomVisitor<Boolean> {

	private final OptMap<Integer, Set<GCI0Axiom>> mapOfGCI0 = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<GCI1Axiom>> mapOfGCI1 = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<GCI2Axiom>> mapOfGCI2 = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<GCI3Axiom>> mapOfGCI3A = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<GCI3Axiom>> mapOfGCI3r = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, OptMap<Integer, Set<GCI3Axiom>>> mapOfGCI3rA = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<NominalAxiom>> mapOfNominalAxiom = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<RangeAxiom>> mapOfRangeAxiom = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<RI2Axiom>> mapOfRI2r = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<RI2Axiom>> mapOfRI2s = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<RI3Axiom>> mapOfRI3ByLeft = new OptMapImpl<>(new HashMap<>());
	private final OptMap<Integer, Set<RI3Axiom>> mapOfRI3ByRight = new OptMapImpl<>(new HashMap<>());
	private final Set<Integer> setOfAllObjectProperties = new HashSet<>();
	private final Set<Integer> setOfClasses = new HashSet<>();
	private final Set<Integer> setOfFunctionalObjectProperties = new HashSet<>();
	private final Set<Integer> setOfReflexiveObjectProperties = new HashSet<>();
	private final Set<Integer> setOfTransitiveObjectProperties = new HashSet<>();

	/**
	 * Constructs an empty ontology.
	 */
	public ExtendedOntologyImpl() {
	}

	@Override
	public void addClass(int classId) {
		this.setOfClasses.add(classId);
	}

	private void addEntities(NormalizedIntegerAxiom axiom) {
		this.setOfAllObjectProperties.addAll(axiom.getObjectPropertiesInSignature());
		this.setOfClasses.addAll(axiom.getClassesInSignature());
	}

	private void addGCI0Axiom(int classId, GCI0Axiom axiom) {
		if (!this.mapOfGCI0.get(classId).isPresent()) {
			this.mapOfGCI0.put(classId, new HashSet<>());
		}
		this.mapOfGCI0.get(classId).get().add(axiom);
	}

	private void addGCI1Axiom(int classId, GCI1Axiom axiom) {
		if (!this.mapOfGCI1.get(classId).isPresent()) {
			this.mapOfGCI1.put(classId, new HashSet<>());
		}
		this.mapOfGCI1.get(classId).get().add(axiom);
	}

	private void addGCI2Axiom(int classId, GCI2Axiom axiom) {
		if (!this.mapOfGCI2.get(classId).isPresent()) {
			this.mapOfGCI2.put(classId, new HashSet<>());
		}
		this.mapOfGCI2.get(classId).get().add(axiom);
	}

	private void addGCI3Axiom(GCI3Axiom axiom, int objectPropertyId, int classId) {
		if (!this.mapOfGCI3r.get(objectPropertyId).isPresent()) {
			this.mapOfGCI3r.put(objectPropertyId, new HashSet<>());
		}
		this.mapOfGCI3r.get(objectPropertyId).get().add(axiom);

		if (!this.mapOfGCI3A.get(classId).isPresent()) {
			this.mapOfGCI3A.put(classId, new HashSet<>());
		}
		this.mapOfGCI3A.get(classId).get().add(axiom);

		Optional<OptMap<Integer, Set<GCI3Axiom>>> optMap = this.mapOfGCI3rA.get(objectPropertyId);
		if (!optMap.isPresent()) {
			optMap = Optional.of(new OptMapImpl<>(new HashMap<>()));
			this.mapOfGCI3rA.put(objectPropertyId, optMap.get());
		}
		if (!optMap.get().get(classId).isPresent()) {
			optMap.get().put(classId, new HashSet<>());
		}
		optMap.get().get(classId).get().add(axiom);
	}

	private void addNominalAxiom(int individualId, NominalAxiom axiom) {
		if (!this.mapOfNominalAxiom.get(individualId).isPresent()) {
			this.mapOfNominalAxiom.put(individualId, new HashSet<>());
		}
		this.mapOfNominalAxiom.get(individualId).get().add(axiom);
	}

	@Override
	public void addObjectProperty(int objectProperty) {
		this.setOfAllObjectProperties.add(objectProperty);
	}

	private void addRangeAxiom(int propertyId, RangeAxiom axiom) {
		if (!this.mapOfRangeAxiom.get(propertyId).isPresent()) {
			this.mapOfRangeAxiom.put(propertyId, new HashSet<>());
		}
		this.mapOfRangeAxiom.get(propertyId).get().add(axiom);
	}

	private void addTo(int property, RI3Axiom axiom, OptMap<Integer, Set<RI3Axiom>> map) {
		Optional<Set<RI3Axiom>> optAxiomSet = map.get(property);
		if (!optAxiomSet.isPresent()) {
			optAxiomSet = Optional.of(new HashSet<>());
			map.put(property, optAxiomSet.get());
		}
		optAxiomSet.get().add(axiom);
	}

	@Override
	public void clear() {
		this.setOfClasses.clear();
		this.setOfAllObjectProperties.clear();
		this.mapOfGCI0.clear();
		this.mapOfGCI1.clear();
		this.mapOfGCI2.clear();
		this.mapOfGCI3A.clear();
		this.mapOfGCI3r.clear();
		this.mapOfGCI3rA.clear();
		this.mapOfRI2r.clear();
		this.mapOfRI2s.clear();
		this.mapOfRI3ByLeft.clear();
		this.mapOfRI3ByRight.clear();
		this.mapOfNominalAxiom.clear();
		this.mapOfRangeAxiom.clear();
		this.setOfTransitiveObjectProperties.clear();
		this.setOfFunctionalObjectProperties.clear();
		this.setOfReflexiveObjectProperties.clear();
	}

	@Override
	public Set<Integer> getClassSet() {
		return Collections.unmodifiableSet(this.setOfClasses);
	}

	@Override
	public Set<Integer> getFunctionalObjectProperties() {
		return Collections.unmodifiableSet(this.setOfFunctionalObjectProperties);
	}

	@Override
	public Set<GCI0Axiom> getGCI0Axioms(int classId) {
		Optional<Set<GCI0Axiom>> optSet = this.mapOfGCI0.get(classId);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<GCI1Axiom> getGCI1Axioms(int classId) {
		Optional<Set<GCI1Axiom>> optSet = this.mapOfGCI1.get(classId);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<GCI2Axiom> getGCI2Axioms(int classId) {
		Optional<Set<GCI2Axiom>> optSet = this.mapOfGCI2.get(classId);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<GCI3Axiom> getGCI3AAxioms(int classId) {
		Optional<Set<GCI3Axiom>> optSet = this.mapOfGCI3A.get(classId);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<GCI3Axiom> getGCI3rAAxioms(int objectPropertyId, int leftClassId) {
		Optional<Set<GCI3Axiom>> optSet = null;
		Optional<OptMap<Integer, Set<GCI3Axiom>>> optMap = this.mapOfGCI3rA.get(objectPropertyId);
		if (optMap.isPresent()) {
			optSet = optMap.get().get(leftClassId);
		}
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<GCI3Axiom> getGCI3rAxioms(int objectPropertyId) {
		Optional<Set<GCI3Axiom>> optSet = this.mapOfGCI3r.get(objectPropertyId);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<Integer> getObjectPropertySet() {
		return Collections.unmodifiableSet(this.setOfAllObjectProperties);
	}

	@Override
	public Set<Integer> getReflexiveObjectProperties() {
		return Collections.unmodifiableSet(this.setOfReflexiveObjectProperties);
	}

	@Override
	public Set<RI2Axiom> getRI2rAxioms(int elem) {
		Optional<Set<RI2Axiom>> optSet = this.mapOfRI2r.get(elem);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<RI2Axiom> getRI2sAxioms(int elem) {
		Optional<Set<RI2Axiom>> optSet = this.mapOfRI2s.get(elem);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<RI3Axiom> getRI3AxiomsByLeft(int elem) {
		Optional<Set<RI3Axiom>> optSet = this.mapOfRI3ByLeft.get(elem);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<RI3Axiom> getRI3AxiomsByRight(int elem) {
		Optional<Set<RI3Axiom>> optSet = this.mapOfRI3ByRight.get(elem);
		if (!optSet.isPresent()) {
			optSet = Optional.of(Collections.emptySet());
		}
		return Collections.unmodifiableSet(optSet.get());
	}

	@Override
	public Set<Integer> getTransitiveObjectProperties() {
		return Collections.unmodifiableSet(this.setOfTransitiveObjectProperties);
	}

	@Override
	public void load(Set<NormalizedIntegerAxiom> axiomSet) {
		Objects.requireNonNull(axiomSet);
		axiomSet.forEach(axiom -> {
			axiom.accept(this);
			addEntities(axiom);
		});
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[");
		sbuf.append("map of GCI0 =" + this.mapOfGCI0.toString());
		sbuf.append("map of GCI1 =" + this.mapOfGCI1.toString());
		sbuf.append("map of GCI2 =" + this.mapOfGCI2.toString());
		sbuf.append("map of GCI3 =" + this.mapOfGCI3r.toString());
		sbuf.append("map of RI2 =" + this.mapOfRI2r.toString());
		sbuf.append("set of functional =" + this.setOfFunctionalObjectProperties.toString());
		sbuf.append("set of reflexive =" + this.setOfReflexiveObjectProperties.toString());
		sbuf.append("set of transitive =" + this.setOfTransitiveObjectProperties.toString());
		sbuf.append("]");
		return sbuf.toString();
	}

	@Override
	public Boolean visit(FunctObjectPropAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.setOfFunctionalObjectProperties.add(axiom.getProperty());
		return true;
	}

	@Override
	public Boolean visit(GCI0Axiom axiom) {
		Objects.requireNonNull(axiom);
		addGCI0Axiom(axiom.getSubClass(), axiom);
		return true;
	}

	@Override
	public Boolean visit(GCI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		addGCI1Axiom(axiom.getLeftSubClass(), axiom);
		addGCI1Axiom(axiom.getRightSubClass(), axiom);
		return true;
	}

	@Override
	public Boolean visit(GCI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		addGCI2Axiom(axiom.getSubClass(), axiom);
		return true;
	}

	@Override
	public Boolean visit(GCI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		addGCI3Axiom(axiom, axiom.getPropertyInSubClass(), axiom.getClassInSubClass());
		return true;
	}

	@Override
	public Boolean visit(NominalAxiom axiom) {
		Objects.requireNonNull(axiom);
		addNominalAxiom(axiom.getIndividual(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		Objects.requireNonNull(axiom);
		addRangeAxiom(axiom.getProperty(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		Objects.requireNonNull(axiom);
		this.setOfReflexiveObjectProperties.add(axiom.getSuperProperty());
		return true;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		Objects.requireNonNull(axiom);
		Integer subProperty = axiom.getSubProperty();
		if (!this.mapOfRI2r.get(subProperty).isPresent()) {
			this.mapOfRI2r.put(subProperty, new HashSet<>());
		}
		this.mapOfRI2r.get(subProperty).get().add(axiom);

		Integer superProperty = axiom.getSuperProperty();
		if (!this.mapOfRI2s.get(superProperty).isPresent()) {
			this.mapOfRI2s.put(superProperty, new HashSet<>());
		}
		this.mapOfRI2s.get(superProperty).get().add(axiom);

		return true;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		Objects.requireNonNull(axiom);
		Integer left = axiom.getLeftSubProperty();
		Integer right = axiom.getRightSubProperty();
		addTo(left, axiom, this.mapOfRI3ByLeft);
		addTo(right, axiom, this.mapOfRI3ByRight);
		if (left.equals(axiom.getSuperProperty()) && right.equals(axiom.getSuperProperty())) {
			this.setOfTransitiveObjectProperties.add(left);
		}
		return true;
	}

}
