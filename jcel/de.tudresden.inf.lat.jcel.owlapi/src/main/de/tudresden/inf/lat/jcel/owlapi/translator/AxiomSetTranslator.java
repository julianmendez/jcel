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

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiom;

/**
 * An object of this class can translate a set of <code>OWLAxiom</code>s into a
 * set of <code>IntegerAxiom</code>s.
 * 
 * @author Julian Mendez
 */
public class AxiomSetTranslator {

	// private static final Logger logger = Logger
	// .getLogger(AxiomSetTranslator.class.getName());

	private Map<OWLClass, Integer> classMap = null;
	private Map<OWLDataProperty, Integer> dataPropertyMap = null;
	private Map<OWLNamedIndividual, Integer> individualMap = null;
	private Map<OWLLiteral, Integer> literalMap = null;
	private Map<OWLObjectProperty, Integer> objectPropertyMap = null;

	public AxiomSetTranslator(TranslationRepository repository) {
		if (repository == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.objectPropertyMap = createPropertyMap(repository);
		this.classMap = createClassMap(repository);
		this.individualMap = createIndividualMap(this.classMap.size(),
				repository);
		this.dataPropertyMap = createDataPropertyMap(repository);
		this.literalMap = createLiteralMap(repository);
	}

	private Map<OWLClass, Integer> createClassMap(
			TranslationRepository repository) {
		Map<OWLClass, Integer> ret = new HashMap<OWLClass, Integer>();
		Iterator<OWLClass> it = repository.getOWLClassList().iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLClass current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLDataProperty, Integer> createDataPropertyMap(
			TranslationRepository repository) {
		Map<OWLDataProperty, Integer> ret = new HashMap<OWLDataProperty, Integer>();
		Iterator<OWLDataProperty> it = repository.getOWLDataPropertyList()
				.iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLDataProperty current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLNamedIndividual, Integer> createIndividualMap(
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

	private Map<OWLLiteral, Integer> createLiteralMap(
			TranslationRepository repository) {
		Map<OWLLiteral, Integer> ret = new HashMap<OWLLiteral, Integer>();
		Iterator<OWLLiteral> it = repository.getOWLLiteralList().iterator();
		for (int index = 0; it.hasNext(); index++) {
			OWLLiteral current = it.next();
			ret.put(current, index);
		}
		return ret;
	}

	private Map<OWLObjectProperty, Integer> createPropertyMap(
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

	public Map<OWLDataProperty, Integer> getDataPropertyMap() {
		return Collections.unmodifiableMap(this.dataPropertyMap);
	}

	public Map<OWLNamedIndividual, Integer> getIndividualMap() {
		return Collections.unmodifiableMap(this.individualMap);
	}

	public Map<OWLLiteral, Integer> getLiteralMap() {
		return Collections.unmodifiableMap(this.literalMap);
	}

	public Map<OWLObjectProperty, Integer> getObjectPropertyMap() {
		return Collections.unmodifiableMap(this.objectPropertyMap);
	}

	public Set<ComplexIntegerAxiom> translate(Set<OWLAxiom> axiomSet)
			throws TranslationException {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		AxiomTranslator translator = new AxiomTranslator(
				this.objectPropertyMap, this.classMap, this.individualMap,
				this.dataPropertyMap, this.literalMap);
		Set<ComplexIntegerAxiom> ret = new HashSet<ComplexIntegerAxiom>();
		for (OWLAxiom axiom : axiomSet) {
			ret.addAll(axiom.accept(translator));
		}
		return ret;
	}
}
