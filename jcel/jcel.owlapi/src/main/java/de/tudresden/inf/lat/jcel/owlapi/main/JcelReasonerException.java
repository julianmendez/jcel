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
 * This exception is thrown by a <code>JcelReasonerProcessor</code>.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasonerProcessor
 */
public class JcelReasonerException extends RuntimeException {

	private static final long serialVersionUID = -3973550154860330100L;

	/**
	 * Constructs a new reasoner exception.
	 * 
	 * @param message
	 *            message to be displayed
	 */
	public JcelReasonerException(String message) {
		super(message);
	}

	/**
	 * Constructs a new reasoner exception.
	 * 
	 * @param message
	 *            message to be displayed
	 * @param cause
	 *            cause of the exception
	 */
	public JcelReasonerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new reasoner exception.
	 * 
	 * @param cause
	 *            cause of the exception
	 */
	public JcelReasonerException(Throwable cause) {
		super(cause);
	}

}
