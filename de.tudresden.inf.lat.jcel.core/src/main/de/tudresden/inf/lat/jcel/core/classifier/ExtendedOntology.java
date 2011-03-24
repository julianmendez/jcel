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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.GCI0Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.GCI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.NormalizedIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.core.axiom.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RangeAxiom;

/**
 * This class models an extended ontology. This is referred in the documentation
 * as <i>&Ocirc;</i>.
 * 
 * @author Julian Mendez
 */
public class ExtendedOntology implements NormalizedIntegerAxiomVisitor<Boolean> {

	private Set<RI3Axiom> emptySet = new HashSet<RI3Axiom>();
	private Map<Integer, Set<ExtensionEntry>> ohatOfClass = null;
	private Map<ExistentialKey, Set<ExtensionEntry>> ohatOfExistential = null;
	private Map<Integer, Set<RI3Axiom>> subPropertyAxiomSetByLeft = null;
	private Map<Integer, Set<RI3Axiom>> subPropertyAxiomSetByRight = null;

	public ExtendedOntology() {
		reset();
	}

	protected void addEntry(Integer classId, ExtensionEntry entry) {
		Set<ExtensionEntry> set = this.ohatOfClass.get(classId);
		if (set == null) {
			set = new HashSet<ExtensionEntry>();
			this.ohatOfClass.put(classId, set);
		}
		set.add(entry);
	}

	protected void addTo(Integer property, RI3Axiom axiom,
			Map<Integer, Set<RI3Axiom>> map) {
		Set<RI3Axiom> axiomSet = map.get(property);
		if (axiomSet == null) {
			axiomSet = new HashSet<RI3Axiom>();
			map.put(property, axiomSet);
		}
		axiomSet.add(axiom);
	}

	public Set<ExtensionEntry> getClassEntries(Integer classId) {
		Set<ExtensionEntry> ret = this.ohatOfClass.get(classId);
		if (ret == null) {
			ret = new HashSet<ExtensionEntry>();
			this.ohatOfClass.put(classId, ret);
		}
		return ret;
	}

	public Set<Integer> getClassSet() {
		return Collections.unmodifiableSet(this.ohatOfClass.keySet());
	}

	public Set<ExtensionEntry> getExistentialEntries(Integer propertyId,
			Integer classId) {
		ExistentialKey key = new ExistentialKey(propertyId, classId);
		Set<ExtensionEntry> ret = this.ohatOfExistential.get(key);
		if (ret == null) {
			ret = new HashSet<ExtensionEntry>();
			this.ohatOfExistential.put(key, ret);
		}
		return ret;
	}

	public Set<RI3Axiom> getSubPropertyAxiomSetByLeft(Integer elem) {
		Set<RI3Axiom> ret = this.subPropertyAxiomSetByLeft.get(elem);
		if (ret == null) {
			ret = this.emptySet;
		}
		return Collections.unmodifiableSet(ret);
	}

	public Set<RI3Axiom> getSubPropertyAxiomSetByRight(Integer elem) {
		Set<RI3Axiom> ret = this.subPropertyAxiomSetByRight.get(elem);
		if (ret == null) {
			ret = this.emptySet;
		}
		return Collections.unmodifiableSet(ret);
	}

	public void load(Set<NormalizedIntegerAxiom> axiomSet) {
		reset();
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			axiom.accept(this);
			if (axiom instanceof RI3Axiom) {
				RI3Axiom subPropAxiom = (RI3Axiom) axiom;
				Integer left = subPropAxiom.getLeftSubProperty();
				Integer right = subPropAxiom.getRightSubProperty();
				addTo(left, subPropAxiom, this.subPropertyAxiomSetByLeft);
				addTo(right, subPropAxiom, this.subPropertyAxiomSetByRight);
			}
		}
	}

	public void reset() {
		this.ohatOfClass = new HashMap<Integer, Set<ExtensionEntry>>();
		this.ohatOfExistential = new HashMap<ExistentialKey, Set<ExtensionEntry>>();
		this.subPropertyAxiomSetByLeft = new HashMap<Integer, Set<RI3Axiom>>();
		this.subPropertyAxiomSetByRight = new HashMap<Integer, Set<RI3Axiom>>();
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

	/**
	 * Intersection extension.
	 */
	@Override
	public Boolean visit(GCI0Axiom axiom) {
		addEntry(axiom.getSubClass(), new ImplicationEntry(
				new HashSet<Integer>(), axiom.getSuperClass()));
		return true;
	}

	/**
	 * Intersection extension.
	 */
	@Override
	public Boolean visit(GCI1Axiom axiom) {
		Integer superClass = axiom.getSuperClass();
		List<Integer> operandSet = axiom.getOperands();
		for (Integer currentOperand : operandSet) {
			Set<Integer> currentSet = new HashSet<Integer>();
			currentSet.addAll(operandSet);
			currentSet.remove(currentOperand);
			addEntry(currentOperand, new ImplicationEntry(currentSet,
					superClass));
		}
		return true;
	}

	/**
	 * Existential extension
	 */
	@Override
	public Boolean visit(GCI2Axiom axiom) {
		addEntry(axiom.getSubClass(), new ExistentialEntry(axiom
				.getPropertyInSuperClass(), axiom.getClassInSuperClass()));
		return true;
	}

	/**
	 * Symmetric existential extension.
	 */
	@Override
	public Boolean visit(GCI3Axiom axiom) {
		ExtensionEntry entry = new ImplicationEntry(new HashSet<Integer>(),
				axiom.getSuperClass());
		ExistentialKey key = new ExistentialKey(axiom.getPropertyInSubClass(),
				axiom.getClassInSubClass());
		Set<ExtensionEntry> set = this.ohatOfExistential.get(key);
		if (set == null) {
			set = new HashSet<ExtensionEntry>();
			this.ohatOfExistential.put(key, set);
		}
		set.add(entry);
		return true;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		return false;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		return false;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		return false;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		return false;
	}
}
