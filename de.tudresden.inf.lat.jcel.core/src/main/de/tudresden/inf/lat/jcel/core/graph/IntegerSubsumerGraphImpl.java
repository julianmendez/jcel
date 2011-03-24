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

package de.tudresden.inf.lat.jcel.core.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a subsumer graph. This is implementation keeps a set of
 * subsumers for each vertex. It also considers bottom and its equivalents as
 * special cases.
 * 
 * @author Julian Mendez
 */
public class IntegerSubsumerGraphImpl implements IntegerSubsumerGraph {

	private Integer bottomElement = 0;
	private Collection<Integer> emptyCollection = Collections
			.unmodifiableCollection(new ArrayList<Integer>());
	private Set<Integer> equivToBottom = new HashSet<Integer>();
	private Map<Integer, Collection<Integer>> setS = new HashMap<Integer, Collection<Integer>>();
	private Integer topElement = 1;

	// private static final Logger logger = Logger
	// .getLogger(IntegerHierarchicalGraphImpl.class.getName());

	public IntegerSubsumerGraphImpl(Integer bottom, Integer top) {
		this.bottomElement = bottom;
		this.topElement = top;
		this.setS.put(this.bottomElement, this.emptyCollection);
		this.setS.put(this.topElement, new ArrayList<Integer>());
		this.equivToBottom.add(this.bottomElement);
	}

	@Override
	public void add(Integer vertex) {
		if (!this.setS.containsKey(vertex)) {
			Collection<Integer> aux = new ArrayList<Integer>();
			aux.add(vertex);
			this.setS.put(vertex, aux);
		}
	}

	@Override
	public void addAncestor(Integer subId, Integer superId) {
		if (!this.equivToBottom.contains(subId)) {
			if (this.bottomElement.equals(superId)) {
				this.equivToBottom.add(subId);
				this.setS.put(subId, this.emptyCollection);
			} else {
				add(subId);
				add(superId);
				this.setS.get(subId).add(superId);
			}
		}
	}

	@Override
	public Integer getBottomElement() {
		return this.bottomElement;
	}

	@Override
	public Collection<Integer> getElements() {
		return Collections.unmodifiableSet(this.setS.keySet());
	}

	@Override
	public Collection<Integer> getSubsumers(Integer vertex) {
		Collection<Integer> ret = null;
		if (this.equivToBottom.contains(vertex)) {
			ret = getElements();
		} else {
			ret = Collections.unmodifiableCollection(this.setS.get(vertex));
		}
		return ret;
	}

	@Override
	public Integer getTopElement() {
		return this.topElement;
	}

	@Override
	public void removeElem(Integer start) {
		Set<Integer> keySet = new HashSet<Integer>();
		keySet.addAll(this.setS.keySet());
		for (Integer key : keySet) {
			if (key < start) {
				Collection<Integer> value = new ArrayList<Integer>();
				for (Integer elem : this.setS.get(key)) {
					if (elem < start) {
						value.add(elem);
					}
				}
				this.setS.put(key, value);
			} else {
				this.setS.remove(key);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("equivalent to bottom: ");
		for (Integer elem : this.equivToBottom) {
			ret.append(" " + elem);
		}
		ret.append("\n");
		for (Integer id : this.setS.keySet()) {
			Collection<Integer> related = null;
			if (this.equivToBottom.contains(id)) {
				related = this.setS.keySet();
				ret.append("* ");
			} else {
				related = this.setS.get(id);
			}
			ret.append(id);
			ret.append(" (");
			ret.append(related.size());
			ret.append(") : ");
			for (Integer elem : related) {
				ret.append(elem + " ");
			}
			ret.append("\n");
		}
		return ret.toString();
	}
}
