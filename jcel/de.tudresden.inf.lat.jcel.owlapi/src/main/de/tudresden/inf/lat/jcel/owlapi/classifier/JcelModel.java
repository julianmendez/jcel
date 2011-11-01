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

import java.util.Set;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Objects of this class have the resulting model after the classification. It
 * has mainly the <code>OWLObjectProperty</code> hierarchy and the
 * <code>OWLEntity</code> hierarchy, which includes classes and individuals.
 * 
 * @author Julian Mendez
 */
public class JcelModel {

	private HierarchicalGraph<OWLEntity> entityGraph = null;
	private HierarchicalGraph<OWLObjectProperty> propertyGraph = null;
	private Set<OWLObjectProperty> reflexivePropertySet = null;
	private Set<OWLObjectProperty> transitivePropertySet = null;

	public JcelModel(HierarchicalGraph<OWLObjectProperty> propGraph,
			HierarchicalGraph<OWLEntity> entGraph,
			Set<OWLObjectProperty> reflexiveSet,
			Set<OWLObjectProperty> transitiveSet) {
		if (propGraph == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entGraph == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (reflexiveSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (transitiveSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.propertyGraph = propGraph;
		this.entityGraph = entGraph;
		this.reflexivePropertySet = reflexiveSet;
		this.transitivePropertySet = transitiveSet;
	}

	public HierarchicalGraph<OWLEntity> getOWLEntityGraph() {
		return this.entityGraph;
	}

	public HierarchicalGraph<OWLObjectProperty> getOWLObjectPropertyGraph() {
		return this.propertyGraph;
	}

	public Set<OWLObjectProperty> getReflexiveOWLObjectProperties() {
		return this.reflexivePropertySet;
	}

	public Set<OWLObjectProperty> getTransitiveOWLObjectProperties() {
		return this.transitivePropertySet;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n* properties: ");
		sbuf.append(this.propertyGraph.toString());
		sbuf.append("\n* classes and individuals: ");
		sbuf.append(this.entityGraph.toString());
		sbuf.append("\n* reflexive properties: ");
		sbuf.append(this.reflexivePropertySet.toString());
		sbuf.append("\n* transitive properties: ");
		sbuf.append(this.transitivePropertySet.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}
}
