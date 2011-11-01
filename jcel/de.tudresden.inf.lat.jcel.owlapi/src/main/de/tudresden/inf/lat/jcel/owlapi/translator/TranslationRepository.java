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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * An object of this class is a repository used for the translation between OWL
 * API objects and the integer numbers. Each entity is identified by an integer
 * number.
 * 
 * @author Julian Mendez
 */
public class TranslationRepository {

	public static final Integer versionNumber = 1;

	private List<OWLClass> classList = null;
	private List<OWLNamedIndividual> individualList = null;
	private List<OWLObjectProperty> propertyList = null;

	public TranslationRepository() {
		clear();
	}

	/**
	 * Clears the repository.
	 */
	public void clear() {
		this.classList = new ArrayList<OWLClass>();
		this.propertyList = new ArrayList<OWLObjectProperty>();
		this.individualList = new ArrayList<OWLNamedIndividual>();
	}

	public OWLClass getOWLClass(int index) {
		return this.classList.get(index);
	}

	public List<OWLClass> getOWLClassList() {
		return Collections.unmodifiableList(this.classList);
	}

	public OWLEntity getOWLClassOrIndividual(int index) {
		OWLEntity ret = null;
		if (index < this.classList.size()) {
			ret = this.classList.get(index);
		} else {
			ret = this.individualList.get(index - this.classList.size());
		}
		return ret;
	}

	public List<OWLNamedIndividual> getOWLNamedIndividualList() {
		return Collections.unmodifiableList(this.individualList);
	}

	public OWLObjectProperty getOWLObjectProperty(int index) {
		return this.propertyList.get(index);
	}

	public List<OWLObjectProperty> getOWLObjectPropertyList() {
		return Collections.unmodifiableList(this.propertyList);
	}

	/**
	 * Loads the repository.
	 * 
	 * @param nothing
	 *            OWL class for 'nothing'
	 * @param thing
	 *            OWL class for 'thing'
	 * @param classSet
	 *            set of OWL classes
	 * @param propertySet
	 *            set of properties
	 * @param individualSet
	 *            set of individuals
	 */
	public void load(OWLClass nothing, OWLClass thing,
			OWLObjectProperty bottomObjectProperty,
			OWLObjectProperty topObjectProperty, Set<OWLClass> classSet,
			Set<OWLObjectProperty> propertySet,
			Set<OWLNamedIndividual> individualSet) {

		if (classSet != null) {
			Set<OWLClass> sorted = new TreeSet<OWLClass>();
			sorted.addAll(classSet);
			sorted.remove(nothing);
			sorted.remove(thing);
			this.classList = new ArrayList<OWLClass>();
			this.classList.add(nothing); // 0
			this.classList.add(thing); // 1
			this.classList.addAll(sorted);
		}

		if (propertySet != null) {
			Set<OWLObjectProperty> sorted = new TreeSet<OWLObjectProperty>();
			sorted.addAll(propertySet);
			this.propertyList = new ArrayList<OWLObjectProperty>();
			this.propertyList.add(bottomObjectProperty); // 0
			this.propertyList.add(topObjectProperty); // 1
			this.propertyList.addAll(sorted);
		}

		if (individualSet != null) {
			Set<OWLNamedIndividual> sorted = new TreeSet<OWLNamedIndividual>();
			sorted.addAll(individualSet);
			this.individualList = new ArrayList<OWLNamedIndividual>();
			this.individualList.addAll(sorted);
		}
	}

	/**
	 * Loads the repository from a <code>Reader</code>.
	 * 
	 * @param factory
	 *            factory to create the OWL API objects
	 * @param input
	 *            reader with the repository
	 * @throws IOException
	 *             if the stream cannot be read
	 */
	public void load(OWLDataFactory factory, Reader input) throws IOException {
		clear();
		BufferedReader reader = new BufferedReader(input);
		try {
			String version = reader.readLine();
			if (version.equals("" + versionNumber)) {

				int classListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < classListSize; i++) {
					String line = reader.readLine().trim();
					OWLClass elem = factory.getOWLClass(IRI.create(line));
					this.classList.add(elem);
				}

				int propListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < propListSize; i++) {
					String line = reader.readLine().trim();
					OWLObjectProperty elem = factory.getOWLObjectProperty(IRI
							.create(line));
					this.propertyList.add(elem);
				}

				int indivListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < indivListSize; i++) {
					String line = reader.readLine().trim();
					OWLNamedIndividual elem = factory.getOWLNamedIndividual(IRI
							.create(line));
					this.individualList.add(elem);

				}
			} else {
				throw new IOException("Version number does not match.");
			}
		} catch (NullPointerException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Stores the repository using a <code>Writer</code>.
	 * 
	 * @param output
	 *            writer to store the repository
	 * @throws IOException
	 *             if the stream cannot be read
	 */
	public void store(Writer output) throws IOException {
		BufferedWriter writer = new BufferedWriter(output);
		writer.write("" + versionNumber);
		writer.newLine();

		List<OWLClass> cList = getOWLClassList();
		writer.write("" + cList.size());
		writer.newLine();
		for (OWLClass elem : cList) {
			writer.write(elem.getIRI().toString());
			writer.newLine();
		}

		List<OWLObjectProperty> pList = getOWLObjectPropertyList();
		writer.write("" + pList.size());
		writer.newLine();
		for (OWLObjectProperty elem : pList) {
			writer.write(elem.getIRI().toString());
			writer.newLine();
		}

		List<OWLNamedIndividual> iList = getOWLNamedIndividualList();
		writer.write("" + iList.size());
		writer.newLine();
		for (OWLNamedIndividual elem : getOWLNamedIndividualList()) {
			writer.write(elem.getIRI().toString());
			writer.newLine();
		}

		writer.flush();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n");
		sbuf.append(this.classList.toString());
		sbuf.append("\n");
		sbuf.append(this.propertyList.toString());
		sbuf.append("\n");
		sbuf.append(this.individualList.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}
}
