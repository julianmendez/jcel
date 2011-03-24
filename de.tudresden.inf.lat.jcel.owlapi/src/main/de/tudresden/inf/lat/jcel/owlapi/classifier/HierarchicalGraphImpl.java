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

package de.tudresden.inf.lat.jcel.owlapi.classifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is an implementation of <code>HierarchicalGraph</code>. It is based on
 * maps containing children (direct descendants), parents (direct ancestors),
 * and equivalents.
 * 
 * @author Julian Mendez
 * 
 * @param <T>
 *            type of vertices.
 */
public class HierarchicalGraphImpl<T> implements HierarchicalGraph<T> {

	// private static final Logger logger = Logger
	// .getLogger(HierarchicalGraphImpl.class.getName());

	private Map<T, Set<T>> children = new HashMap<T, Set<T>>();
	private Map<T, Set<T>> equivalents = new HashMap<T, Set<T>>();
	private Map<T, Set<T>> parents = new HashMap<T, Set<T>>();

	public HierarchicalGraphImpl() {
	}

	public void add(T elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		if (!this.equivalents.containsKey(elem)) {
			this.equivalents.put(elem, new HashSet<T>());
		}
		if (!this.children.containsKey(elem)) {
			this.children.put(elem, new HashSet<T>());
		}
		if (!this.parents.containsKey(elem)) {
			this.parents.put(elem, new HashSet<T>());
		}
	}

	public void addEquivalent(T orig, T other) {
		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (other == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		add(orig);
		add(other);

		Set<T> newEquiv = this.equivalents.get(orig);
		newEquiv.addAll(this.equivalents.get(other));
		newEquiv.add(orig);
		newEquiv.add(other);
		for (T elem : newEquiv) {
			this.equivalents.put(elem, newEquiv);
		}

		Set<T> newChildren = this.children.get(orig);
		newChildren.addAll(this.children.get(other));
		this.children.put(other, newChildren);

		Set<T> newParents = this.parents.get(orig);
		newParents.addAll(this.parents.get(other));
		this.parents.put(other, newParents);
	}

	public void addSubElement(T orig, T other) {
		// this method assumes that equivalent elements have a reference
		// to exactly the same set of children and exactly the same set of
		// parents.

		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (other == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		add(orig);
		add(other);
		if (!orig.equals(other)) {
			this.children.get(orig).add(other);
			this.parents.get(other).add(orig);
		}
	}

	private Set<T> getAncestors(T orig) {
		Set<T> queue = new HashSet<T>();
		Set<T> visited = new HashSet<T>();
		queue.add(orig);
		while (!queue.isEmpty()) {
			T elem = queue.iterator().next();
			queue.remove(elem);
			visited.add(elem);
			Set<T> related = new HashSet<T>();
			related.addAll(this.parents.get(elem));
			related.removeAll(visited);
			queue.addAll(related);
		}
		visited.removeAll(this.equivalents.get(orig));
		return visited;
	}

	private Set<T> getDescendants(T orig) {
		Set<T> queue = new HashSet<T>();
		Set<T> visited = new HashSet<T>();
		queue.add(orig);
		while (!queue.isEmpty()) {
			T elem = queue.iterator().next();
			queue.remove(elem);
			visited.add(elem);
			Set<T> related = new HashSet<T>();
			related.addAll(this.children.get(elem));
			related.removeAll(visited);
			queue.addAll(related);
		}
		visited.removeAll(this.equivalents.get(orig));
		return visited;
	}

	@Override
	public Set<T> getElements() {
		return Collections.unmodifiableSet(this.equivalents.keySet());
	}

	@Override
	public Set<T> getEquivalents(T orig) {
		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<T> ret = new HashSet<T>();
		ret.addAll(this.equivalents.get(orig));
		return ret;
	}

	@Override
	public Set<Set<T>> getSubElements(T orig, boolean direct) {
		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<T> flatSet = null;
		if (direct) {
			flatSet = this.children.get(orig);
		} else {
			flatSet = getDescendants(orig);
		}
		return makeEquivClasses(flatSet);
	}

	@Override
	public Set<Set<T>> getSuperElements(T orig, boolean direct) {
		if (orig == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<T> flatSet = null;
		if (direct) {
			flatSet = this.parents.get(orig);
		} else {
			flatSet = getAncestors(orig);
		}
		return makeEquivClasses(flatSet);
	}

	private Set<Set<T>> makeEquivClasses(Set<T> set) {
		if (set == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Set<T>> ret = new HashSet<Set<T>>();
		Set<T> visited = new HashSet<T>();
		for (T elem : set) {
			if (!visited.contains(elem)) {
				Set<T> equivalentProperties = Collections
						.unmodifiableSet(this.equivalents.get(elem));
				ret.add(equivalentProperties);
				visited.addAll(equivalentProperties);
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n* children: ");
		sbuf.append(this.children.toString());
		sbuf.append("\n* parents: ");
		sbuf.append(this.parents.toString());
		sbuf.append("\n* equivalents: ");
		sbuf.append(this.equivalents.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}
}
