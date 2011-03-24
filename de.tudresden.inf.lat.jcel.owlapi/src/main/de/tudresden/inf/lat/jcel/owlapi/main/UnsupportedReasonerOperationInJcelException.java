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

package de.tudresden.inf.lat.jcel.owlapi.main;

/**
 * This exception is thrown when the reasoner is requested an operation that is
 * unsupported. This happens because the reasoner implements an interface that
 * requires a wider functionality than the one given by the reasoner. This
 * exception is thrown by <code>JcelReasoner</code>.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class UnsupportedReasonerOperationInJcelException extends
		RuntimeException {

	private static final long serialVersionUID = 5757365447889383439L;

	public UnsupportedReasonerOperationInJcelException() {
		super("This operation is not supported by jcel.");
	}

	public UnsupportedReasonerOperationInJcelException(String message) {
		super(message);
	}

	public UnsupportedReasonerOperationInJcelException(String message,
			Throwable cause) {
		super(message, cause);
	}

	public UnsupportedReasonerOperationInJcelException(Throwable cause) {
		super(cause);
	}
}
