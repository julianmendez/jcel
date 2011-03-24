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

package de.tudresden.inf.lat.jcel.core.completion.ext;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;

/**
 * <p>
 * <ul>
 * <li>CR10 : <b>if</b> <u>(r<sub>1</sub>, x, y) &isin; R</u>,
 * <u>(r<sub>2</sub>, x, z) &isin; R</u>, r<sub>1</sub> &#8849;
 * <sub><i>T</i></sub> s , r<sub>2</sub> &#8849; <sub><i>T</i></sub> s , f(s) ,
 * <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; y = (&#8868; , &psi; ) , z = (&#8868; , &phi; ) <br />
 * <b>then</b> v := (&#8868; , &psi; &cup; &phi; ) , <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; { v }
 * <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, k) | (y, k) &isin; S} &cup; {(v,
 * k) | (z, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r<sub>1</sub>, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR10Rule implements RObserverRule {

	public CR10Rule() {
	}

	@Override
	public List<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<XEntry> ret = new ArrayList<XEntry>();
		ret.addAll(apply1(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
		ret.addAll(apply2(status, entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
		return ret;
	}

	private List<XEntry> apply1(ClassifierStatus status, Integer r2, Integer x,
			Integer z) {
		List<XEntry> ret = new ArrayList<XEntry>();
		if (status.getNode(z).getClassId().equals(status.getClassTopElement())) {
			VNode phiNode = status.getNode(z);
			for (Integer s : status.getSuperObjectProperties(r2)) {
				if (status.getExtendedOntology()
						.getFunctionalObjectProperties().contains(s)) {
					for (Integer r1 : status.getSubObjectProperties(s)) {
						for (Integer y : status.getRelationBySecond(r1, x)) {
							if (status.getNode(y).getClassId()
									.equals(status.getClassTopElement())) {
								VNode newPsiNode = status.getNode(y).clone();
								newPsiNode.setClassId(status
										.getClassTopElement());
								newPsiNode.addExistentialsOf(phiNode);
								Integer v = status
										.createOrGetNodeId(newPsiNode);
								for (Integer p : status.getSubsumers(y)) {
									ret.add(new SEntryImpl(v, p));
								}
								for (Integer p : status.getSubsumers(z)) {
									ret.add(new SEntryImpl(v, p));
								}

							}
						}
					}
				}
			}
		}
		return ret;
	}

	private List<XEntry> apply2(ClassifierStatus status, Integer r1, Integer x,
			Integer y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		if (status.getNode(y).getClassId().equals(status.getClassTopElement())) {
			VNode psiNode = status.getNode(y);
			for (Integer s : status.getSuperObjectProperties(r1)) {
				if (status.getExtendedOntology()
						.getFunctionalObjectProperties().contains(s)) {
					for (Integer r2 : status.getSubObjectProperties(s)) {
						for (Integer z : status.getRelationBySecond(r2, x)) {
							if (status.getNode(z).getClassId()
									.equals(status.getClassTopElement())) {
								VNode phiNode = status.getNode(z);
								VNode newPhiNode = phiNode.clone();
								newPhiNode.setClassId(status
										.getClassTopElement());
								newPhiNode.addExistentialsOf(psiNode);
								Integer v = status
										.createOrGetNodeId(newPhiNode);
								for (Integer p : status.getSubsumers(y)) {
									ret.add(new SEntryImpl(v, p));
								}
								for (Integer p : status.getSubsumers(z)) {
									ret.add(new SEntryImpl(v, p));
								}
							}
						}
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
