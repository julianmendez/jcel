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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
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
	private static final String space = " ";
	private static final String uriDelimiterLeft = "<";
	private static final String uriDelimiterRight = ">";

	private Map<String, String> mapOfPrefixes = new TreeMap<String, String>();
	private final BufferedWriter output;

	/**
	 * Constructs a Turtle renderer.
	 * 
	 * @param writer
	 *            writer
	 */
	public TurtleRenderer(Writer writer) {
		if (writer == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.output = new BufferedWriter(writer);
	}

	private String getNameWithoutPrefix(String name) {
		String ret = prefixSeparator + name;
		try {
			URI uri = new URI(name);
			if (uri.getHost() != null) {
				String prefixId = mapOfPrefixes.get(getPrefix(uri));
				if (prefixId != null) {
					ret = prefixId + prefixSeparator + uri.getFragment();
				} else {
					ret = uriDelimiterLeft + name + uriDelimiterRight;
				}
			}
		} catch (URISyntaxException e) {
		}
		return ret;
	}

	private String getPrefix(URI uri) {
		return uri.getScheme() + colonSlashSlash + uri.getHost()
				+ uri.getPath() + fragmentSeparator;
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
				if (uri.getHost() != null) {
					String prefix = getPrefix(uri);
					if (!this.mapOfPrefixes.containsKey(prefix)) {
						this.mapOfPrefixes.put(prefix, prefixBeginning
								+ this.mapOfPrefixes.keySet().size());
						ret = true;
					}
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
	 * @throws IOException
	 */
	public void renderTriple(String predicate, String subject, String object)
			throws IOException {
		this.output.write(getNameWithoutPrefix(subject));
		this.output.write(separator);
		this.output.write(getNameWithoutPrefix(predicate));
		this.output.write(separator);
		this.output.write(getNameWithoutPrefix(object));
		this.output.write(space);
		this.output.write(lineEnd);
		this.output.newLine();
		this.output.flush();
	}

}
