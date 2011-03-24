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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * An object of this class can translate a set of <code>OWLAxiom</code>s into a
 * set of <code>IntegerAxiom</code>s.
 * 
 * @author Julian Mendez
 */
public class AxiomSetTranslator {

	private static final Logger logger = Logger
			.getLogger(AxiomSetTranslator.class.getName());

	private Map<OWLClass, Integer> classMap = null;
	private Map<OWLNamedIndividual, Integer> individualMap = null;
	private Map<OWLObjectProperty, Integer> propertyMap = null;

	public AxiomSetTranslator(TranslationRepository repository) {
		this.propertyMap = createPropertyMap(repository);
		this.classMap = createClassMap(repository);
		this.individualMap = createIndividualMap(this.classMap.size(),
				repository);
	}

	protected Map<OWLClass, Integer> createClassMap(
			TranslationRepository repository) {
		Map<OWLClass, Integer> ret = new HashMap<OWLClass, Integer>();
		Iterator<OWLClass> it = repository.getOWLClassList().iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLClass current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	protected Map<OWLNamedIndividual, Integer> createIndividualMap(
			Integer offset, TranslationRepository repository) {
		Map<OWLNamedIndividual, Integer> ret = new HashMap<OWLNamedIndividual, Integer>();
		Iterator<OWLNamedIndividual> it = repository
				.getOWLNamedIndividualList().iterator();
		for (int index = offset; it.hasNext(); index++) {
			OWLNamedIndividual current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	protected Map<OWLObjectProperty, Integer> createPropertyMap(
			TranslationRepository repository) {
		Map<OWLObjectProperty, Integer> ret = new HashMap<OWLObjectProperty, Integer>();
		Iterator<OWLObjectProperty> it = repository.getOWLObjectPropertyList()
				.iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLObjectProperty current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	public Map<OWLClass, Integer> getClassMap() {
		return Collections.unmodifiableMap(this.classMap);
	}

	public Map<OWLNamedIndividual, Integer> getIndividualMap() {
		return Collections.unmodifiableMap(this.individualMap);
	}

	public Map<OWLObjectProperty, Integer> getPropertyMap() {
		return Collections.unmodifiableMap(this.propertyMap);
	}

	public Set<IntegerAxiom> translate(Set<OWLAxiom> axiomSet)
			throws TranslationException {
		AxiomTranslator translator = new AxiomTranslator(this.propertyMap,
				this.classMap, this.individualMap);
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		for (OWLAxiom axiom : axiomSet) {
			IntegerAxiom translation = axiom.accept(translator);
			if (translation != null) {
				ret.add(translation);
			} else {
				logger.finer("Omitting axiom '" + axiom + "'.");
			}
		}
		return ret;
	}
}
