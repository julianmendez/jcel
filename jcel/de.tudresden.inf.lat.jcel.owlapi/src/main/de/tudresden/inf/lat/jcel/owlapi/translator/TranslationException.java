/*
 * Copyright 2009 Julian Mendez
 *
 *
 *  This file is part of jcel.
 *
 *  jcel is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jcel is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.owlapi.translator;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * This exception is thrown when an <code>AxiomTranslator</code> cannot make a
 * translation between the OWL API objects and the integer numbers.
 * 
 * @author Julian Mendez
 * 
 * @see AxiomTranslator
 */
public class TranslationException extends RuntimeException {

	private static final long serialVersionUID = 6883202061243751672L;

	public static TranslationException newIncompleteMapException(String item) {
		return new TranslationException(
				"The translation map is incomplete. Item id was not found: '"
						+ item + "'.");
	}

	public static TranslationException newUnsupportedAxiomException(
			OWLAxiom axiom) {
		return new TranslationException("This axiom is not supported: '"
				+ axiom + "'.");
	}

	public TranslationException(String message) {
		super(message);
	}

	public TranslationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TranslationException(Throwable cause) {
		super(cause);
	}
}
