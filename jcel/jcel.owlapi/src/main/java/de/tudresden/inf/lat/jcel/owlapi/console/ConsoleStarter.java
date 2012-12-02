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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;

import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;
import de.tudresden.inf.lat.jcel.reasoner.main.VersionInfo;

/**
 * This class makes possible to start a classifier instance from the command
 * line.
 * 
 * @author Julian Mendez
 */
public class ConsoleStarter {

	public static final String cmdClassHierarchy = "classhierarchy";
	public static final String cmdEntailment = "entailment";

	public static final String licenseInfo = ""
			+ "Copyright (C) 2009-2012 Julian Mendez"
			+ "\nLicense GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>."
			+ "\nThis is free software: you are free to change and redistribute it."
			+ "\njcel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY."
			+ "\n";

	private static final Logger logger = Logger
			.getLogger("de.tudresden.inf.lat.jcel");

	public static final int modeClassHierarchy = 1;
	public static final int modeEntailment = 2;
	public static final int modeNothing = 0;
	public static final String optConclusion = "--conclusion=";
	public static final String optHelp = "--help";
	public static final String optLogLevel = "--loglevel=";
	public static final String optOntology = "--ontology=";
	public static final String optOutput = "--output=";
	public static final String optVersion = "--version";
	public static final String versionInfo = VersionInfo.reasonerName + " "
			+ VersionInfo.reasonerVersion;

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

		(new ConsoleStarter()).start(args);
	}

	/** A very small help about how to start a new instance. */
	private String minihelp = "\nusage: java -jar jcel.jar [COMMAND] [OPTION]..."
			+ "\n\n\nthe available commands are:" + "\n   "
			+ cmdClassHierarchy
			+ "            compute the class hierarchy and the object property hierarchy of the given ontology"
			+ "\n   "
			+ cmdEntailment
			+ "                determine whether the given ontology entails the given conclusion"
			+ "\n\n\nthe available options are:"
			+ "\n   "
			+ optOntology
			+ "FILE           ontology to be classified (or premise ontology)"
			+ "\n   "
			+ optOutput
			+ "FILE             output with the inferred data"
			+ "\n   "
			+ optConclusion
			+ "FILE         conclusion ontology"
			+ "\n   "
			+ optLogLevel
			+ "LEVEL          log level"
			+ "\n   "
			+ optHelp
			+ "                    display this help"
			+ "\n   "
			+ optVersion
			+ "                 output version" + "\n\n\n";

	public ConsoleStarter() {
	}

	/**
	 * Classifies a given ontology and checks whether another ontology is
	 * entailed by the former.
	 * 
	 * @param premiseFile
	 *            ontology file to be classified and used as premise
	 * @param conclusionFile
	 *            file with the conclusion
	 * @throws FileNotFoundException
	 * @throws OWLOntologyCreationException
	 * @throws OWLRendererException
	 */
	public boolean checkEntailment(File premiseFile, File conclusionFile)
			throws OWLOntologyCreationException, OWLRendererException,
			FileNotFoundException {
		if (premiseFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (conclusionFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.fine("starting jcel console ...");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		logger.fine("loading premise ontology using the OWL API ...");
		OWLOntology premiseOntology = manager
				.loadOntologyFromOntologyDocument(premiseFile);

		logger.fine("loading conclusion ontology using the OWL API ...");
		OWLOntology conclusionOntology = manager
				.loadOntologyFromOntologyDocument(conclusionFile);

		logger.fine("starting reasoner ...");
		JcelReasoner reasoner = new JcelReasoner(premiseOntology, false);

		logger.fine("precomputing inferences ...");
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		boolean ret = true;
		for (OWLAxiom axiom : conclusionOntology.getAxioms()) {
			ret = ret && reasoner.isEntailed(axiom);
		}

		logger.fine("jcel console finished.");
		return ret;
	}

	/**
	 * Classifies a given ontology and computes the class hierarchy and the
	 * object property hierarchy.
	 * 
	 * @param ontologyFile
	 *            ontology file to be classified
	 * @param inferredFile
	 *            file to write the inferred data
	 * @throws FileNotFoundException
	 * @throws OWLOntologyCreationException
	 * @throws OWLRendererException
	 * @throws SecurityException
	 */
	public void computeHierarchy(File ontologyFile, File inferredFile)
			throws OWLOntologyCreationException, OWLRendererException,
			SecurityException, FileNotFoundException {
		if (ontologyFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (inferredFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.fine("starting jcel console ...");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		logger.fine("loading ontology using the OWL API ...");
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(ontologyFile);

		System.gc();
		System.gc();

		logger.fine("starting reasoner ...");
		JcelReasoner reasoner = new JcelReasoner(ontology, false);

		logger.fine("precomputing inferences ...");
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		logger.fine("generating output ...");
		OWLReasonerXMLOutput xmlDoc = new OWLReasonerXMLOutput(reasoner);
		xmlDoc.toXML(new FileOutputStream(inferredFile));

		logger.fine("jcel console finished.");
	}

	public void start(String[] args) throws OWLRendererException,
			OWLOntologyCreationException, SecurityException, IOException {

		if (args.length == 0) {
			System.out.println(minihelp);
		} else {
			Set<String> arguments = toSet(args);
			if (arguments.contains(optHelp)) {
				System.out.println(minihelp);
			} else if (arguments.contains(optVersion)) {
				System.out.println(versionInfo);
				System.out.println(licenseInfo);
			} else {

				File ontologyFile = null;
				File outputFile = null;
				File conclusionFile = null;
				Level logLevel = Level.OFF;
				int mode = modeClassHierarchy;

				for (String argument : arguments) {
					if (argument.startsWith(optOntology)) {
						ontologyFile = new File(argument.substring(optOntology
								.length()));
					} else if (argument.startsWith(optOutput)) {
						outputFile = new File(argument.substring(optOutput
								.length()));
					} else if (argument.startsWith(optConclusion)) {
						conclusionFile = new File(
								argument.substring(optConclusion.length()));
					} else if (argument.startsWith(optLogLevel)) {
						logLevel = Level.parse(argument.substring(optLogLevel
								.length()));
					} else if (argument.equals(cmdClassHierarchy)) {
						mode = modeClassHierarchy;
					} else if (argument.equals(cmdEntailment)) {
						mode = modeEntailment;
					} else {
						throw new IllegalArgumentException(
								"Unrecognized option: '" + argument + "'");
					}
				}

				logger.setLevel(logLevel);
				logger.addHandler(new OutputStreamHandler(System.out));

				if (mode == modeClassHierarchy) {
					if (ontologyFile == null) {
						throw new IllegalArgumentException(
								"No input file has been defined.");
					}
					if (outputFile == null) {
						throw new IllegalArgumentException(
								"No output file has been defined.");
					}

					computeHierarchy(ontologyFile, outputFile);

				} else if (mode == modeEntailment) {
					if (ontologyFile == null) {
						throw new IllegalArgumentException(
								"No input file has been defined.");
					}
					if (conclusionFile == null) {
						throw new IllegalArgumentException(
								"No conclusion file has been defined.");
					}

					System.out.println(checkEntailment(ontologyFile,
							conclusionFile));

				}
			}
		}
	}

	private Set<String> toSet(String[] args) {
		Set<String> ret = new HashSet<String>();
		ret.addAll(Arrays.asList(args));
		return ret;
	}

}
