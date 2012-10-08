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

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-6 : <b>if</b> &exist; r<sup>-</sup> <i>.</i> A &#8849; B &isin;
 * <i>T</i> , (r, x, y) &isin; R, <u>(x, A) &isin; S</u> , (y, B) &notin; S, y =
 * (B', &psi;) <br />
 * <b>then</b> v := (B', &psi; &cup; {&exist; r <sup>-</sup> <i>.</i> A}) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} ,
 * S := S &cup; {(v, k) | (y, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR6SAltRule implements SObserverRule {

	/**
	 * Constructs a new completion rule CR-6 (S).
	 */
	public CR6SAltRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int subClass, int superClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, subClass, superClass);
	}

	private boolean applyRule(ClassifierStatus status, int x, int a) {
		boolean ret = false;
		for (GCI3Axiom axiom : status.getExtendedOntology().getGCI3AAxioms(a)) {
			int rMinus = axiom.getPropertyInSubClass();
			int r = status.getInverseObjectPropertyOf(rMinus);
			int b = axiom.getSuperClass();
			for (int y : status.getSecondByFirst(r, x)) {
				if (!status.getSubsumers(y).contains(b)) {
					VNode psiNode = status.getNode(y);
					VNodeImpl newNode = new VNodeImpl(psiNode.getClassId());
					newNode.addExistentialsOf(psiNode);
					newNode.addExistential(rMinus, a);
					boolean inV = status.contains(newNode);
					int v = status.createOrGetNodeId(newNode);
					if (!inV) {
						for (int p : status.getSubsumers(y)) {
							ret |= status.addNewSEntry(v, p);
						}
					}
					ret |= status.addNewSEntry(v, b);
					ret |= status.addNewREntry(r, x, v);
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		return (o != null) && getClass().equals(o.getClass());
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
