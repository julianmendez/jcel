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

package de.tudresden.inf.lat.jcel.owlapi.classifier;


/**
 * This exception is thrown when a <code>Processor</code> is not able to create
 * a model.
 * 
 * @author Julian Mendez
 * 
 * @see ModelConstructor
 */
public class ModelConstructionException extends Throwable {

	private static final long serialVersionUID = 2325219055229225496L;

	public ModelConstructionException(String message) {
		super(message);
	}

	public ModelConstructionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelConstructionException(Throwable cause) {
		super(cause);
	}
}
