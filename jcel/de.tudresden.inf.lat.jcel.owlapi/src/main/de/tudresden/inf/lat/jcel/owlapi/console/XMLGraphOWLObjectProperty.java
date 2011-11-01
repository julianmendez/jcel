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

package de.tudresden.inf.lat.jcel.owlapi.console;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * An object of this class is an <code>XMLGraph</code> of
 * <code>OWLObjectProperty</code>.
 * 
 * @author Julian Mendez
 */
class XMLGraphOWLObjectProperty extends XMLGraph<OWLObjectProperty> {

	protected static final String xmlMainTag = "properties";

	private Set<OWLObjectProperty> elements = null;
	private OWLReasoner reasoner = null;

	public XMLGraphOWLObjectProperty(OWLReasoner owlReasoner) {
		this.reasoner = owlReasoner;
		if (owlReasoner != null && owlReasoner.getRootOntology() != null) {
			this.elements = new TreeSet<OWLObjectProperty>();
			this.elements.addAll(owlReasoner.getRootOntology()
					.getObjectPropertiesInSignature());
		} else {
			throw new IllegalArgumentException(
					"The reasoner does not have any ontology.");
		}
	}

	@Override
	public Set<OWLObjectProperty> getElements() {
		return Collections.unmodifiableSet(this.elements);
	}

	@Override
	public Set<OWLObjectProperty> getEquivalentElements(OWLObjectProperty orig) {
		Set<OWLObjectProperty> ret = new TreeSet<OWLObjectProperty>();
		ret.addAll(this.reasoner.getEquivalentObjectProperties(orig)
				.getEntities());
		return ret;
	}

	@Override
	public Set<OWLObjectProperty> getFlattenedSubElements(OWLObjectProperty orig) {
		Set<OWLObjectProperty> ret = new TreeSet<OWLObjectProperty>();
		ret.addAll(this.reasoner.getSubObjectProperties(orig, true)
				.getFlattened());
		return ret;
	}

	@Override
	public String getMainTag() {
		return xmlMainTag;
	}

	@Override
	public String getName(OWLObjectProperty elem) {
		String ret = elem.getIRI().toString();
		int pos = ret.lastIndexOf('/');
		if (pos != -1) {
			ret = ret.substring(pos + 1);
		}
		return ret;
	}
}
