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

/**
 * This exception is thrown by an <code>OWLReasonerAdapter</code>.
 * 
 * @author Julian Mendez
 * 
 * @see OWLReasonerAdapter
 */
public class OWLReasonerAdapterException extends
		org.semanticweb.owl.inference.OWLReasonerException {

	private static final long serialVersionUID = -9000269573828129981L;

	public OWLReasonerAdapterException(String message) {
		super(message);
	}

	public OWLReasonerAdapterException(String message, Throwable cause) {
		super(message, cause);
	}

	public OWLReasonerAdapterException(Throwable cause) {
		super(cause);
	}
}
