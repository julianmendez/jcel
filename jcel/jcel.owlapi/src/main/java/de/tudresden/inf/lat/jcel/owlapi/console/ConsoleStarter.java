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

package de.tudresden.inf.lat.jcel.owlapi.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;

import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;

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
	 * @throws IOException
	 * @throws SecurityException
	 * @throws OWLOntologyCreationException
	 * @throws OWLRendererException
	 */
	public static void main(String[] args) throws OWLRendererException,
			OWLOntologyCreationException, SecurityException, IOException {
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
			instance.start(new File(args[0]), new File(args[1]), logLevel,
					System.out);
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
	 * @param ontologyFile
	 *            ontology file to be classified
	 * @param inferredFile
	 *            file to write the inferred data
	 * @param logLevel
	 *            log level
	 * @throws OWLOntologyCreationException
	 * @throws OWLRendererException
	 * @throws IOException
	 * @throws SecurityException
	 */
	public void start(File ontologyFile, File inferredFile, Level logLevel,
			OutputStream logOutput) throws OWLOntologyCreationException,
			OWLRendererException, SecurityException, IOException {
		if (ontologyFile == null) {
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

		logger.setLevel(logLevel);
		logger.addHandler(new OutputStreamHandler(logOutput));

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(ontologyFile);

		JcelReasoner reasoner = new JcelReasoner(ontology, false);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		OWLReasonerXMLOutput xmlDoc = new OWLReasonerXMLOutput(reasoner);
		xmlDoc.toXML(new FileOutputStream(inferredFile));
	}
}
