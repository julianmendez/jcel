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
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * <p>
 * <ul>
 * <li>CR-9 : <b>if</b> <u>(r, x, y) &isin; R</u>, (r, x, z) &isin; R, y =
 * (&#8868; , &psi;) , z = (&#8868; , &phi;), y &ne; z, f(r) <br />
 * <b>then</b> v := (&#8868; , &psi; &cup; &phi;) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, k) | (y, k) &isin; S} &cup; {(v,
 * k) | (z, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR9AltRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-9.
	 */
	public CR9AltRule() {
	}

	@Override
	public boolean apply(ClassifierStatus status, int property, int leftClass,
			int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return applyRule(status, property, leftClass, rightClass);
	}

	private boolean applyRule(ClassifierStatus status, int r, int x, int y) {
		boolean ret = false;
		VNode psiNode = status.getNode(y);
		if (psiNode.getClassId() == IntegerEntityManager.topClassId) {
			if (status.getExtendedOntology().getFunctionalObjectProperties()
					.contains(r)) {
				for (int z : status.getSecondByFirst(r, x)) {
					VNode phiNode = status.getNode(z);
					if (phiNode.getClassId() == IntegerEntityManager.topClassId) {
						if (z != y) {
							VNodeImpl newNode = new VNodeImpl(
									IntegerEntityManager.topClassId);
							newNode.addExistentialsOf(psiNode);
							newNode.addExistentialsOf(phiNode);
							int v = status.createOrGetNodeId(newNode);
							for (int p : status.getSubsumers(y)) {
								ret |= status.addNewSEntry(v, p);
							}
							for (int p : status.getSubsumers(z)) {
								ret |= status.addNewSEntry(v, p);
							}
							ret |= status.addNewREntry(r, x, v);
						}
					}
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
