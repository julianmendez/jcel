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

package de.tudresden.inf.lat.jcel.core.completion.alt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.GCI3Axiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-7 : <b>if</b> &exist; r<sup>-</sup> <i>.</i> A &#8849; B &isin;
 * <i>T</i> , <u>(r, x, y) &isin; R</u>, x = (A', &phi;) , y = (B', &psi;) , r
 * &#8728; r &#8849; r &isin; <i>T</i> , s &#8849; r &isin; <i>T</i> , &exist;
 * s<sup>-</sup> <i>.</i> A &isin; &phi; <br />
 * <b>then</b> v := (B', &psi; &cup; {&exist; r<sup>-</sup> <i>.</i> A}) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} ,
 * S := S &cup; {(v, k) | (y, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR7AltRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-7.
	 */
	public CR7AltRule() {
	}

	@Override
	public Collection<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableCollection(applyRule(status,
				entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
	}

	private Collection<XEntry> applyRule(ClassifierStatus status, Integer r,
			Integer x, Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		VNode phiNode = status.getNode(x);
		VNode psiNode = status.getNode(y);
		if (status.getExtendedOntology().getTransitiveObjectProperties()
				.contains(r)) {
			Integer rMinus = status.getInverseObjectPropertyOf(r);
			for (GCI3Axiom axiom : status.getExtendedOntology().getGCI3rAxioms(
					rMinus)) {
				Integer a = axiom.getClassInSubClass();
				Integer b = axiom.getSuperClass();
				for (RI2Axiom riAxiom : status.getExtendedOntology()
						.getRI2sAxioms(r)) {
					Integer s = riAxiom.getSubProperty();
					Integer sMinus = status.getInverseObjectPropertyOf(s);
					if (phiNode.containsExistential(sMinus, a)) {
						VNodeImpl newNode = new VNodeImpl(psiNode.getClassId());
						newNode.addExistentialsOf(psiNode);
						newNode.addExistential(rMinus, a);
						boolean inV = status.contains(newNode);
						Integer v = status.createOrGetNodeId(newNode);
						if (!inV) {
							for (Integer p : status.getSubsumers(y)) {
								ret.add(new SEntryImpl(v, p));
							}
						}
						ret.add(new SEntryImpl(v, b));
						ret.add(new REntryImpl(r, x, v));
					}
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		return getClass().equals(o.getClass());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
