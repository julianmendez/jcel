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

package de.tudresden.inf.lat.jcel.protege.console;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;

import de.tudresden.inf.lat.jcel.owlapi.console.XMLGraph;

/**
 * An object of this class is an <code>XMLGraph</code> of <code>OWLClass</code>.
 * 
 * @author Julian Mendez
 */
class XMLGraphOWLClass extends XMLGraph<OWLClass> {

	protected static final String xmlMainTag = "classes";

	private Set<OWLClass> elements = null;
	private OWLReasoner reasoner = null;

	public XMLGraphOWLClass(OWLReasoner owlReasoner, OWLClass owlThing,
			OWLClass owlNothing) {
		this.reasoner = owlReasoner;
		if (owlReasoner != null && owlReasoner.getLoadedOntologies().size() > 0) {
			this.elements = new TreeSet<OWLClass>();
			this.elements.addAll(owlReasoner.getLoadedOntologies().iterator()
					.next().getClassesInSignature());
			this.elements.add(owlThing);
			this.elements.add(owlNothing);
		} else {
			throw new IllegalArgumentException(
					"The reasoner does not have any ontology.");
		}
	}

	@Override
	public Set<OWLClass> getElements() {
		return Collections.unmodifiableSet(this.elements);
	}

	@Override
	public Set<OWLClass> getEquivalentElements(OWLClass orig) {
		Set<OWLClass> ret = null;
		try {
			ret = this.reasoner.getEquivalentClasses(orig);
		} catch (OWLReasonerException e) {
			throw new RuntimeException(e);
		}
		return ret;
	}

	@Override
	public String getMainTag() {
		return xmlMainTag;
	}

	@Override
	public String getName(OWLClass elem) {
		return elem.toString();
	}

	@Override
	public Set<OWLClass> getFlattenedSubElements(OWLClass orig) {
		Set<OWLClass> ret = null;
		try {
			ret = flatten(this.reasoner.getSubClasses(orig));
		} catch (OWLReasonerException e) {
			throw new RuntimeException(e);
		}
		return ret;
	}
}
