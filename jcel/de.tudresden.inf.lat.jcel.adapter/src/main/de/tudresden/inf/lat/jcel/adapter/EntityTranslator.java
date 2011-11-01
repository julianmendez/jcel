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

package de.tudresden.inf.lat.jcel.adapter;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * An object of this class is a visitor used to translate an
 * <code>OWLEntity</code> in the OWL API 2 into an <code>OWLEntity</code> in the
 * OWL API 3. This is an auxiliary class used by <code>SimpleTranslator</code>.
 * 
 * @author Julian Mendez
 * 
 */
public class EntityTranslator implements
		org.semanticweb.owl.model.OWLEntityVisitorEx<OWLEntity> {

	private SimpleTranslator translator = null;

	public EntityTranslator(SimpleTranslator trans) {
		this.translator = trans;
	}

	public OWLDataFactory getOWLDataFactory() {
		return this.translator.getOWLDataFactory();
	}

	public SimpleTranslator getTranslator() {
		return this.translator;
	}

	@Override
	public OWLEntity visit(org.semanticweb.owl.model.OWLClass cls) {
		return getTranslator().translate(cls);
	}

	@Override
	public OWLEntity visit(
			org.semanticweb.owl.model.OWLDataProperty dataProperty) {
		return getTranslator().translate(dataProperty);
	}

	@Override
	public OWLEntity visit(org.semanticweb.owl.model.OWLDataType dataType) {
		return getTranslator().translate(dataType);
	}

	@Override
	public OWLEntity visit(org.semanticweb.owl.model.OWLIndividual individual) {
		return getTranslator().translate(individual);
	}

	@Override
	public OWLEntity visit(
			org.semanticweb.owl.model.OWLObjectProperty objectProperty) {
		return getTranslator().translate(objectProperty);
	}
}
