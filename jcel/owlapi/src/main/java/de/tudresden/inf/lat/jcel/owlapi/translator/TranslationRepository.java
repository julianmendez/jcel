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
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
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
	private List<OWLDataProperty> dataPropertyList = null;
	private List<OWLNamedIndividual> individualList = null;
	private List<OWLLiteral> literalList = null;
	private List<OWLObjectProperty> objectPropertyList = null;

	public TranslationRepository() {
		clear();
	}

	/**
	 * Clears the repository.
	 */
	public void clear() {
		this.classList = new ArrayList<OWLClass>();
		this.objectPropertyList = new ArrayList<OWLObjectProperty>();
		this.individualList = new ArrayList<OWLNamedIndividual>();
		this.dataPropertyList = new ArrayList<OWLDataProperty>();
		this.literalList = new ArrayList<OWLLiteral>();
	}

	public OWLClass getOWLClass(int index) {
		return this.classList.get(index);
	}

	public OWLClass getOWLClass(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (index >= this.classList.size()) {
			throw new IllegalArgumentException("Wrong index for OWLClass : '"
					+ index + "'.");
		}
		return this.classList.get(index);
	}

	public List<OWLClass> getOWLClassList() {
		return Collections.unmodifiableList(this.classList);
	}

	public List<OWLDataProperty> getOWLDataPropertyList() {
		return Collections.unmodifiableList(this.dataPropertyList);
	}

	public List<OWLLiteral> getOWLLiteralList() {
		return Collections.unmodifiableList(this.literalList);
	}

	public OWLNamedIndividual getOWLNamedIndividual(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (index < this.classList.size()) {
			throw new IllegalArgumentException(
					"Wrong index for OWLNamedIndividual : '" + index + "'.");
		}

		return this.individualList.get(index - this.classList.size());
	}

	public List<OWLNamedIndividual> getOWLNamedIndividualList() {
		return Collections.unmodifiableList(this.individualList);
	}

	public OWLObjectProperty getOWLObjectProperty(int index) {
		return this.objectPropertyList.get(index);
	}

	public List<OWLObjectProperty> getOWLObjectPropertyList() {
		return Collections.unmodifiableList(this.objectPropertyList);
	}

	/**
	 * Loads the repository.
	 * 
	 * @param nothing
	 *            OWL class for 'nothing'
	 * @param thing
	 *            OWL class for 'thing'
	 * @param bottomObjectProperty
	 *            OWL bottom object property
	 * @param topObjectProperty
	 *            OWL top object property
	 * @param bottomDataProperty
	 *            OWL bottom data property
	 * @param topDataProperty
	 *            OWL top data property
	 * @param classSet
	 *            set of OWL classes
	 * @param objectPropertySet
	 *            set of object properties
	 * @param individualSet
	 *            set of individuals
	 * @param dataPropertySet
	 *            set of data properties
	 * @param literalSet
	 *            set of literals
	 */
	public void load(OWLClass nothing, OWLClass thing,
			OWLObjectProperty bottomObjectProperty,
			OWLObjectProperty topObjectProperty,
			OWLDataProperty bottomDataProperty,
			OWLDataProperty topDataProperty, Set<OWLClass> classSet,
			Set<OWLObjectProperty> objectPropertySet,
			Set<OWLNamedIndividual> individualSet,
			Set<OWLDataProperty> dataPropertySet, Set<OWLLiteral> literalSet) {

		if (nothing == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (thing == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomObjectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topObjectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topDataProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (objectPropertySet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (individualSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (dataPropertySet == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (literalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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

		if (objectPropertySet != null) {
			Set<OWLObjectProperty> sorted = new TreeSet<OWLObjectProperty>();
			sorted.addAll(objectPropertySet);
			this.objectPropertyList = new ArrayList<OWLObjectProperty>();
			this.objectPropertyList.add(bottomObjectProperty); // 0
			this.objectPropertyList.add(topObjectProperty); // 1
			this.objectPropertyList.addAll(sorted);
		}

		if (individualSet != null) {
			Set<OWLNamedIndividual> sorted = new TreeSet<OWLNamedIndividual>();
			sorted.addAll(individualSet);
			this.individualList = new ArrayList<OWLNamedIndividual>();
			this.individualList.addAll(sorted);
		}

		if (dataPropertySet != null) {
			Set<OWLDataProperty> sorted = new TreeSet<OWLDataProperty>();
			sorted.addAll(dataPropertySet);
			this.dataPropertyList = new ArrayList<OWLDataProperty>();
			this.dataPropertyList.add(bottomDataProperty); // 0
			this.dataPropertyList.add(topDataProperty); // 1
			this.dataPropertyList.addAll(sorted);
		}

		if (literalSet != null) {
			Set<OWLLiteral> sorted = new TreeSet<OWLLiteral>();
			sorted.addAll(literalSet);
			this.literalList = new ArrayList<OWLLiteral>();
			this.literalList.addAll(sorted);
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
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (input == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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

				int objPropListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < objPropListSize; i++) {
					String line = reader.readLine().trim();
					OWLObjectProperty elem = factory.getOWLObjectProperty(IRI
							.create(line));
					this.objectPropertyList.add(elem);
				}

				int indivListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < indivListSize; i++) {
					String line = reader.readLine().trim();
					OWLNamedIndividual elem = factory.getOWLNamedIndividual(IRI
							.create(line));
					this.individualList.add(elem);
				}

				int dataPropListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < dataPropListSize; i++) {
					String line = reader.readLine().trim();
					OWLDataProperty elem = factory.getOWLDataProperty(IRI
							.create(line));
					this.dataPropertyList.add(elem);
				}

				int litListSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < litListSize; i++) {
					String line = reader.readLine().trim();
					OWLLiteral elem = factory.getOWLLiteral(line);
					this.literalList.add(elem);
				}

			} else {
				throw new IOException("Version numbers do not match.");
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
		if (output == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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

		List<OWLDataProperty> dList = getOWLDataPropertyList();
		writer.write("" + dList.size());
		writer.newLine();
		for (OWLDataProperty elem : dList) {
			writer.write(elem.getIRI().toString());
			writer.newLine();
		}

		List<OWLLiteral> lList = getOWLLiteralList();
		writer.write("" + lList.size());
		writer.newLine();
		for (OWLLiteral elem : getOWLLiteralList()) {
			writer.write(elem.getLiteral());
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
		sbuf.append(this.objectPropertyList.toString());
		sbuf.append("\n");
		sbuf.append(this.individualList.toString());
		sbuf.append("\n");
		sbuf.append(this.dataPropertyList.toString());
		sbuf.append("\n");
		sbuf.append(this.literalList.toString());
		sbuf.append("\n");
		return sbuf.toString();
	}
}
