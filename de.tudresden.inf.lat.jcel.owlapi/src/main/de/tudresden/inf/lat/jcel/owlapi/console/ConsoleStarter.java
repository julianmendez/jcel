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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerException;
import org.w3c.dom.DOMException;

import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;
import de.tudresden.inf.lat.jcel.owlapi.main.LogFileHandler;

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
	 *            file (optional)
	 * @throws OWLReasonerException
	 * @throws OWLOntologyCreationException
	 * @throws SecurityException
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void main(String[] args) throws OWLReasonerException,
			OWLOntologyCreationException, SecurityException, IOException,
			DOMException, ParserConfigurationException, TransformerException {
		boolean helpNeeded = true;
		ConsoleStarter instance = new ConsoleStarter();
		if (args.length > 0) {
			helpNeeded = false;
			if (args.length > 1) {
				instance.setOutput(new FileOutputStream(args[1]));
			}
			instance.start(args[0]);
		}
		if (helpNeeded) {
			System.out.println(instance.getMiniHelp());
		}
	}

	/** A very small help about how to start a new instance. */
	private String minihelp = "\nUsage:\njava -cp .:<list of jars> "
			+ this.getClass().getCanonicalName()
			+ " <input ontology> [<output file name>]\n";

	private OutputStream output = System.out;

	public ConsoleStarter() {
	}

	public String getMiniHelp() {
		return this.minihelp;
	}

	public OutputStream getOutput() {
		return this.output;
	}

	/**
	 * @throws IOException
	 * @throws SecurityException
	 */
	public void initialise() throws SecurityException, IOException {
	}

	public void setOutput(OutputStream out) {
		this.output = out;
	}

	/**
	 * Executes the classifier on a given ontology.
	 * 
	 * @param filename
	 *            ontology file to be classified
	 * @throws OWLReasonerException
	 * @throws OWLOntologyCreationException
	 * @throws SecurityException
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void start(String filename) throws OWLReasonerException,
			OWLOntologyCreationException, SecurityException, IOException,
			DOMException, ParserConfigurationException, TransformerException {
		initialise();

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(new File(filename));

		JcelReasoner reasoner = new JcelReasoner(ontology);
		logger.setLevel(Level.FINE);
		Long ts = reasoner.getStartTime().getTime();
		String logId = "" + ts;
		String fileName = "jcel-" + reasoner.getStartTime().getTime() + ".log";
		logger.addHandler(new LogFileHandler(new File(fileName)));
		logger.fine("ts: " + ts + " ms, log: " + logId);

		Set<OWLOntology> ontologySet = new HashSet<OWLOntology>();
		ontologySet.add(ontology);

		reasoner.prepareReasoner();

		OWLReasonerXMLOutput xmlDoc = new OWLReasonerXMLOutput(reasoner,
				manager.getOWLDataFactory().getOWLNothing(), manager
						.getOWLDataFactory().getOWLThing());
		xmlDoc.toXML(this.output);
	}
}
