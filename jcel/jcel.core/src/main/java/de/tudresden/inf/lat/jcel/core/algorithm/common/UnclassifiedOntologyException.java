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

package de.tudresden.inf.lat.jcel.core.algorithm.common;

/**
 * This exception is thrown when accessing a data structure given by the
 * processor before classification.
 * 
 * @author Julian Mendez
 */
public class UnclassifiedOntologyException extends RuntimeException {

	private static final long serialVersionUID = -2635386871352905968L;

	public UnclassifiedOntologyException() {
		super();
	}

	public UnclassifiedOntologyException(String message) {
		super(message);
	}

	public UnclassifiedOntologyException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnclassifiedOntologyException(Throwable cause) {
		super(cause);
	}

}
