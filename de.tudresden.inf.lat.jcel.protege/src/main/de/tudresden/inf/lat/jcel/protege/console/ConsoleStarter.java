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

package de.tudresden.inf.lat.jcel.protege.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.owlapi.console.OutputStreamHandler;
import de.tudresden.inf.lat.jcel.protege.main.JcelOldReasoner;

/**
 * This class makes possible to start a classifier instance from the command
 * line.
 * 
 * @author Julian Mendez
 */
public class ConsoleStarter {

	private static final Logger logger = Logger
			.getLogger("de.tudresden.inf.lat.jcel");

	/**
	 * Starts a classifier instance from the command line.
	 * 
	 * @param args
	 *            a list containing the command line parameters, they are first
	 *            parameter: input file (required), second parameter: output
	 *            file (required), third parameter: log level (optional)
	 */
	public static void main(String[] args) throws Exception {
		if (args == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean helpNeeded = true;
		ConsoleStarter instance = new ConsoleStarter();
		if (args.length > 1) {
			helpNeeded = false;
			Level logLevel = Level.FINE;
			if (args.length > 2) {
				logLevel = Level.parse(args[2]);
			}
			instance.start(args[0], new File(args[1]), logLevel, System.out);
		}
		if (helpNeeded) {
			System.out.println(instance.minihelp);
		}
	}

	/** A very small help about how to start a new instance. */
	private String minihelp = "\nUsage:\njava -cp .:<list of jars> "
			+ this.getClass().getCanonicalName()
			+ " <input ontology file name> <inferred data file name> [<log level>]\n";

	public ConsoleStarter() {
	}

	/**
	 * Executes the classifier on a given ontology.
	 * 
	 * @param ontologyFileName
	 *            ontology file to be classified
	 * @param inferredFile
	 *            file to write the inferred data
	 * @param logLevel
	 *            log level
	 */
	public void start(String ontologyFileName, File inferredFile,
			Level logLevel, OutputStream logOutput) throws Exception {
		if (ontologyFileName == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (inferredFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (logLevel == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (logOutput == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		org.semanticweb.owl.model.OWLOntologyManager manager = org.semanticweb.owl.apibinding.OWLManager
				.createOWLOntologyManager();
		URI physicalURI = URI.create("file:" + ontologyFileName);
		org.semanticweb.owl.model.OWLOntology ontology = manager
				.loadOntologyFromPhysicalURI(physicalURI);

		JcelOldReasoner reasoner = new JcelOldReasoner(manager);
		logger.setLevel(logLevel);
		logger.addHandler(new OutputStreamHandler(logOutput));

		Set<org.semanticweb.owl.model.OWLOntology> ontologySet = new HashSet<org.semanticweb.owl.model.OWLOntology>();
		ontologySet.add(ontology);

		reasoner.loadOntologies(ontologySet);
		reasoner.classify();

		OWLReasonerXMLOutput xmlDoc = new OWLReasonerXMLOutput(reasoner);
		xmlDoc.toXML(new FileOutputStream(inferredFile));
	}
}
