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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

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

/**
 * An object of this class collects symbols in an normalized axiom.
 * 
 * @author Julian Mendez
 *
 */
public class DefaultIdentifierCollector {

	private final Set<Integer> classesOnTheLeft = new TreeSet<>();
	private final Set<Integer> classesOnTheRight = new TreeSet<>();
	private final Set<Integer> objectPropertiesOnTheLeft = new TreeSet<>();
	private final Set<Integer> objectPropertiesOnTheRight = new TreeSet<>();
	private final NormalizedIntegerAxiom axiom;

	/**
	 * Constructs a new BornIdentifier collector.
	 * 
	 * @param axiom
	 *            normalized axiom
	 */
	public DefaultIdentifierCollector(NormalizedIntegerAxiom axiom) {
		Objects.requireNonNull(axiom);
		this.axiom = axiom;
		axiom.accept(new AuxBornIdentifierCollector());
	}

	/**
	 * Returns the normalized axiom.
	 * 
	 * @return the normalized axiom
	 */
	public NormalizedIntegerAxiom getAxiom() {
		return this.axiom;
	}

	/**
	 * Returns the class BornIdentifiers found on the left-hand side of the
	 * given axiom.
	 *
	 * @return the class BornIdentifiers found on the left-hand side of the
	 *         given axiom
	 */
	public Set<Integer> getClassesOnTheLeft() {
		return Collections.unmodifiableSet(this.classesOnTheLeft);
	}

	/**
	 * Returns the class BornIdentifiers found on the right-hand side of the
	 * given axiom.
	 * 
	 * @return the class BornIdentifiers found on the right-hand side of the
	 *         given axiom
	 */
	public Set<Integer> getClassesOnTheRight() {
		return Collections.unmodifiableSet(this.classesOnTheRight);
	}

	/**
	 * Returns the object property BornIdentifiers found on the left-hand side
	 * of the given axiom.
	 * 
	 * @return the object property BornIdentifiers found on the left-hand side
	 *         of the given axiom
	 */
	public Set<Integer> getObjectPropertiesOnTheLeft() {
		return Collections.unmodifiableSet(this.objectPropertiesOnTheLeft);
	}

	/**
	 * Returns the object property found on the right-hand side of the given
	 * axiom.
	 * 
	 * @return the object property BornIdentifiers found on the right-hand side
	 *         of the given axiom
	 */
	public Set<Integer> getObjectPropertiesOnTheRight() {
		return Collections.unmodifiableSet(this.objectPropertiesOnTheRight);
	}

	@Override
	public int hashCode() {
		return this.axiom.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DefaultIdentifierCollector)) {
			return false;
		} else {
			DefaultIdentifierCollector other = (DefaultIdentifierCollector) obj;
			return getAxiom().equals(other.getAxiom());
		}
	}

	@Override
	public String toString() {
		return this.axiom.toString();
	}

	/**
	 * This is an auxiliary class used to collect the BornIdentifiers.
	 * 
	 * @author Julian Mendez
	 *
	 */
	private class AuxBornIdentifierCollector implements NormalizedIntegerAxiomVisitor<Boolean> {

		AuxBornIdentifierCollector() {
		}

		@Override
		public Boolean visit(FunctObjectPropAxiom axiom) {
			objectPropertiesOnTheLeft.add(axiom.getProperty());
			return true;
		}

		@Override
		public Boolean visit(GCI0Axiom axiom) {
			classesOnTheLeft.add(axiom.getSubClass());
			classesOnTheRight.add(axiom.getSuperClass());
			return true;
		}

		@Override
		public Boolean visit(GCI1Axiom axiom) {
			classesOnTheLeft.add(axiom.getLeftSubClass());
			classesOnTheLeft.add(axiom.getRightSubClass());
			classesOnTheRight.add(axiom.getSuperClass());
			return true;
		}

		@Override
		public Boolean visit(GCI2Axiom axiom) {
			classesOnTheLeft.add(axiom.getSubClass());
			objectPropertiesOnTheRight.add(axiom.getPropertyInSuperClass());
			classesOnTheRight.add(axiom.getClassInSuperClass());
			return true;
		}

		@Override
		public Boolean visit(GCI3Axiom axiom) {
			objectPropertiesOnTheLeft.add(axiom.getPropertyInSubClass());
			classesOnTheLeft.add(axiom.getClassInSubClass());
			classesOnTheRight.add(axiom.getSuperClass());
			return true;
		}

		@Override
		public Boolean visit(NominalAxiom axiom) {
			classesOnTheLeft.add(axiom.getClassExpression());
			classesOnTheRight.add(axiom.getClassExpression());
			return true;
		}

		@Override
		public Boolean visit(RangeAxiom axiom) {
			objectPropertiesOnTheLeft.add(axiom.getProperty());
			classesOnTheRight.add(axiom.getRange());
			return true;
		}

		@Override
		public Boolean visit(RI1Axiom axiom) {
			objectPropertiesOnTheRight.add(axiom.getSuperProperty());
			return true;
		}

		@Override
		public Boolean visit(RI2Axiom axiom) {
			objectPropertiesOnTheLeft.add(axiom.getSubProperty());
			objectPropertiesOnTheRight.add(axiom.getSuperProperty());
			return true;
		}

		@Override
		public Boolean visit(RI3Axiom axiom) {
			objectPropertiesOnTheLeft.add(axiom.getLeftSubProperty());
			objectPropertiesOnTheLeft.add(axiom.getRightSubProperty());
			objectPropertiesOnTheRight.add(axiom.getSuperProperty());
			return true;
		}
	}

}
