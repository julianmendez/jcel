/*
 *
 * Copyright 2009-2015 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.net.URI;
import java.util.Objects;

/**
 * An object of this class is an annotation.
 * 
 * @author Julian Mendez
 * 
 */
public class AnnotationImpl implements Annotation, Comparable<Annotation> {

	private final URI annotationProperty;
	private final String annotationValue;

	/**
	 * Constructs a new default implementation of annotation.
	 * 
	 * @param annotationProperty
	 *            annotation property
	 * @param annotationValue
	 *            annotation value
	 */
	public AnnotationImpl(URI annotationProperty, String annotationValue) {
		Objects.requireNonNull(annotationProperty);
		Objects.requireNonNull(annotationValue);
		this.annotationProperty = annotationProperty;
		this.annotationValue = annotationValue;
	}

	@Override
	public URI getAnnotationProperty() {
		return this.annotationProperty;
	}

	@Override
	public String getAnnotationValue() {
		return this.annotationValue;
	}

	@Override
	public int hashCode() {
		return this.annotationProperty.hashCode() + (0x1F * this.annotationValue.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Annotation)) {
			return false;
		} else {
			Annotation other = (Annotation) obj;
			return getAnnotationProperty().equals(other.getAnnotationProperty())
					&& getAnnotationValue().equals(getAnnotationValue());
		}
	}

	@Override
	public String toString() {
		return this.annotationProperty + " " + this.annotationValue;
	}

	@Override
	public int compareTo(Annotation other) {
		Objects.requireNonNull(other);
		int ret = getAnnotationProperty().compareTo(other.getAnnotationProperty());
		if (ret == 0) {
			return getAnnotationValue().compareTo(other.getAnnotationValue());
		} else {
			return ret;
		}
	}

}
