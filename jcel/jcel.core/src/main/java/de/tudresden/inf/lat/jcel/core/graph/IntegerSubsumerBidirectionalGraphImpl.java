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

import java.util.Collection;
import java.util.Iterator;

/**
 * This class implements a subsumer invGraph. This implementation keeps a set of
 * subsumers for each vertex. It also considers bottom and its equivalents as
 * special cases.
 * 
 * @author Julian Mendez
 */
public class IntegerSubsumerBidirectionalGraphImpl implements
		IntegerSubsumerBidirectionalGraph {

	private IntegerSubsumerGraphImpl graph;
	private IntegerSubsumerGraphImpl invGraph;

	/**
	 * Constructs an empty subsumer bidirectional graph.
	 * 
	 * @param bottom
	 *            bottom class identifier
	 * @param top
	 *            top class identifier
	 */
	public IntegerSubsumerBidirectionalGraphImpl(int bottom, int top) {
		this.graph = new IntegerSubsumerGraphImpl(bottom, top);
		this.invGraph = new IntegerSubsumerGraphImpl(top, bottom);
	}

	/**
	 * Adds a new vertex to the invGraph.
	 * 
	 * @param vertex
	 */
	public void add(int vertex) {
		this.graph.add(vertex);
		this.invGraph.add(vertex);
	}

	/**
	 * Defines that one vertex is a subsumee of another vertex. If one or both
	 * elements do not belong to the vertex collection, they are added anyway.
	 * 
	 * @param subId
	 *            the subsumee
	 * @param superId
	 *            the subsumer
	 */
	public void addAncestor(int subId, int superId) {
		add(subId);
		add(superId);
		this.graph.addAncestor(subId, superId);
		this.invGraph.addAncestor(superId, subId);
	}

	@Override
	public boolean containsPair(int subsumee, int subsumer) {
		return getSubsumers(subsumee) != null
				&& getSubsumers(subsumee).contains(subsumer);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof IntegerSubsumerBidirectionalGraph) {
			IntegerSubsumerBidirectionalGraph other = (IntegerSubsumerBidirectionalGraph) o;
			ret = (getBottomElement() == other.getBottomElement())
					&& (getTopElement() == other.getTopElement())
					&& getElements().equals(other.getElements());
			for (Iterator<Integer> it = getElements().iterator(); ret
					&& it.hasNext();) {
				Integer elem = it.next();
				ret = ret
						&& getSubsumers(elem).equals(other.getSubsumers(elem));
			}
		}
		return ret;
	}

	@Override
	public int getBottomElement() {
		return this.graph.getBottomElement();
	}

	@Override
	public Collection<Integer> getElements() {
		return this.graph.getElements();
	}

	@Override
	public Collection<Integer> getSubsumees(int vertex) {
		return this.invGraph.getSubsumers(vertex);
	}

	@Override
	public Collection<Integer> getSubsumers(int vertex) {
		return this.graph.getSubsumers(vertex);
	}

	@Override
	public int getTopElement() {
		return this.graph.getTopElement();
	}

	@Override
	public int hashCode() {
		return this.graph.hashCode();
	}

	/**
	 * Retains only the elements contained in the collection.
	 * 
	 * @param collection
	 *            collection of elements to be retained
	 */
	public void retainAll(Collection<Integer> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.graph.retainAll(collection);
		this.invGraph.retainAll(collection);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		for (Integer id : this.graph.getElements()) {
			Collection<Integer> related = getSubsumers(id);
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