/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * An object of this class creates a stream in a particular case of Turtle
 * (Terse RDF Triple Language).
 *
 * @author Julian Mendez
 */
public class TurtleRenderer {

	private static final String colonSlashSlash = "://";
	private static final String fragmentSeparator = "#";
	private static final String lineEnd = ".";
	private static final String prefixBeginning = "p";
	private static final String prefixKeyword = "@prefix";
	private static final String prefixSeparator = ":";
	private static final String separator = "\t";
	private static final String slash = "/";
	private static final String space = " ";
	private static final String uriDelimiterLeft = "<";
	private static final String uriDelimiterRight = ">";
	private final Map<String, String> mapOfPrefixes = new TreeMap<>();

	private final BufferedWriter output;

	/**
	 * Constructs a Turtle renderer.
	 *
	 * @param writer
	 *            writer
	 */
	public TurtleRenderer(Writer writer) {
		Objects.requireNonNull(writer);
		this.output = new BufferedWriter(writer);
	}

	private String getName(URI uri) {
		String ret = uri.getFragment();
		if (Objects.isNull(ret)) {
			String path = uri.getPath();
			if (Objects.nonNull(path)) {
				ret = path.substring(path.lastIndexOf(slash) + slash.length());
			}
		}
		return ret;
	}

	private String getPrefix(URI uri) {
		String ret = "";
		if (Objects.nonNull(uri.getHost())) {
			ret = uri.getScheme() + colonSlashSlash + uri.getHost();
			if (Objects.isNull(uri.getFragment())) {
				String path = uri.getPath();
				if (Objects.nonNull(path)) {
					ret += path.substring(0, path.lastIndexOf(slash) + slash.length());
				}
			} else {
				ret += uri.getPath() + fragmentSeparator;
			}

		}
		return ret;
	}

	private String getTurtleEntity(String name) {
		String ret = prefixSeparator + name;
		try {
			URI uri = new URI(name);
			String prefix = getPrefix(uri);
			if (prefix.length() > 0) {
				String prefixId = this.mapOfPrefixes.get(prefix);
				if (Objects.nonNull(prefixId)) {
					ret = prefixId + prefixSeparator + getName(uri);
				} else {
					ret = uriDelimiterLeft + name + uriDelimiterRight;
				}
			}
		} catch (URISyntaxException e) {
		}
		return ret;
	}

	/**
	 * Loads a set of names and gets the prefixes from these names.
	 *
	 * @param identifiers
	 *            names
	 * @return <code>true</code> if and only if at least one prefix has been
	 *         found
	 */
	public boolean loadPrefixes(Collection<String> identifiers) {
		boolean ret = false;
		for (String name : identifiers) {
			try {
				URI uri = new URI(name);
				String prefix = getPrefix(uri);
				if ((prefix.length() > 0) && !this.mapOfPrefixes.containsKey(prefix)) {
					this.mapOfPrefixes.put(prefix, prefixBeginning + this.mapOfPrefixes.keySet().size());
					ret = true;
				}
			} catch (URISyntaxException e) {
			}
		}
		return ret;
	}

	/**
	 * Renders the prefixes with their abbreviations.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void renderPrefixes() throws IOException {
		for (String prefix : this.mapOfPrefixes.keySet()) {
			String prefixId = this.mapOfPrefixes.get(prefix);
			this.output.write(prefixKeyword);
			this.output.write(space);
			this.output.write(prefixId);
			this.output.write(prefixSeparator);
			this.output.write(space);
			this.output.write(uriDelimiterLeft);
			this.output.write(prefix);
			this.output.write(uriDelimiterRight);
			this.output.write(space);
			this.output.write(lineEnd);
			this.output.newLine();
		}
		this.output.newLine();
		this.output.flush();
	}

	/**
	 * Renders a triple trying to replace prefixes by their abbreviations.
	 * 
	 * @param predicate
	 *            predicate
	 * @param subject
	 *            subject
	 * @param object
	 *            object
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void renderTriple(String predicate, String subject, String object) throws IOException {
		this.output.write(getTurtleEntity(subject));
		this.output.write(separator);
		this.output.write(getTurtleEntity(predicate));
		this.output.write(separator);
		this.output.write(getTurtleEntity(object));
		this.output.write(space);
		this.output.write(lineEnd);
		this.output.newLine();
		this.output.flush();
	}

}
