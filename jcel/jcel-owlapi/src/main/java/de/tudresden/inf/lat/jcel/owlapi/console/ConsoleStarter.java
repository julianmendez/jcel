/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.owlapi.console;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxRenderer;
import org.coode.owlapi.latex.LatexRenderer;
import org.coode.owlapi.owlxml.renderer.OWLXMLRenderer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.AbstractOWLRenderer;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxRenderer;
import uk.ac.manchester.owl.owlapi.tutorialowled2011.OWLTutorialSyntaxRenderer;
import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;
import de.tudresden.inf.lat.jcel.reasoner.main.VersionInfo;
import de.uulm.ecs.ai.owlapi.krssrenderer.KRSS2OWLSyntaxRenderer;
import de.uulm.ecs.ai.owlapi.krssrenderer.KRSS2SyntaxRenderer;
import de.uulm.ecs.ai.owlapi.krssrenderer.KRSSSyntaxRenderer;

/**
 * This class makes possible to start a classifier instance from the command
 * line.
 * 
 * @author Julian Mendez
 */
public class ConsoleStarter {

	public enum Mode {
		CLASSIFICATION, CONSISTENCY, ENTAILMENT, NOTHING, QUERY, SATISFIABILITY
	}

	public static final String cmdClassification = "classification";
	public static final String cmdConsistency = "consistency";
	public static final String cmdEntailment = "entailment";
	public static final String cmdQuery = "query";
	public static final String cmdSat = "sat";
	public static final String cmdSatisfiability = "satisfiability ";

	private static final String errorSuffix = "_err";

	public static final String licenseInfo = ""
			+ "Copyright (C) 2009-2013 Julian Mendez"
			+ "\nLicenses:"
			+ "\n  GNU Lesser General Public License version 3 <http://www.gnu.org/licenses/lgpl.txt>"
			+ "\n  Apache License, Version 2.0 <http://www.apache.org/licenses/LICENSE-2.0.txt>"
			+ "\nThis is free software: you are free to change and redistribute it."
			+ "\njcel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY."
			+ "\n";

	private static final Logger logger = Logger
			.getLogger("de.tudresden.inf.lat.jcel");
	private static final String msgPartCompleted = "Completed ";
	private static final String msgPartOn = " on ";
	private static final String msgPartOperationTime = "Operation time: ";

	private static final String msgPartStarted = "Started ";
	public static final String optClassURI = "--classuri=";
	public static final String optConclusion = "--conclusion=";
	public static final String optHelp = "--help";
	public static final String optLogLevel = "--loglevel=";
	public static final String optOntology = "--ontology=";
	public static final String optOperation = "--operation=";
	public static final String optOutput = "--output=";
	public static final String optRenderer = "--renderer=";
	public static final String optTimeOut = "--timeout=";
	public static final String optVerbose = "--verbose";
	public static final String optVersion = "--version";
	public static final String rendererFunctional = "functional";
	public static final String rendererKRSS = "krss";
	public static final String rendererKRSS2 = "krss2";
	public static final String rendererKRSS2OWL = "krss2owl";
	public static final String rendererLatex = "latex";
	public static final String rendererManchester = "manchester";
	public static final String rendererTutorial = "tutorial";

	public static final String rendererXML = "xml";

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
	private final String minihelp = "\nusage: java -jar jcel.jar <Operation> <OntologyFile> <Output> [<ClassURI>] [Options]..."
			+ "\n\n\n<Operation>:" + "\n   "
			+ cmdConsistency
			+ "               determine whether the given ontology is consistent"
			+ "\n   "
			+ cmdSat
			+ "                       determine whether the given class (<ClassURI>) is satisfiable with respect to the given ontology"
			+ "\n   "
			+ cmdClassification
			+ "            compute the class hierarchy and the object property hierarchy of the given ontology"
			+ "\n   "
			+ cmdEntailment
			+ "                determine whether the given ontology entails the given conclusion"
			+ "\n\n"
			+ "<OntologyFile>               ontology to be classified (or premise ontology)"
			+ "\n\n"
			+ "<Output>                     output with the inferred data"
			+ "\n\n"
			+ "<ClassURI>                   (only for "
			+ cmdSat
			+ ") URI of the class to check satisfiability"
			+ "\n\n\nthe available options are:"
			+ "\n   "
			+ optConclusion
			+ "FILE         conclusion ontology"
			+ "\n   "
			+ optRenderer
			+ "RENDERER       renderer for the class hierarchy computed by the classification operation"
			+ "\n   "
			+ optTimeOut
			+ "MILLISECONDS    force a time-out after a given number of milliseconds"
			+ "\n   "
			+ optLogLevel
			+ "LEVEL          log level"
			+ "\n   "
			+ optVerbose
			+ "                 run verbose mode"
			+ "\n   "
			+ optHelp
			+ "                    display this help"
			+ "\n   "
			+ optVersion
			+ "                 output version"
			+ "\n\n"
			+ "these options override the main arguments:"
			+ "\n   "
			+ optOperation
			+ "OPERATION     select an operation to execute"
			+ "\n   "
			+ optOntology
			+ "FILE           ontology to be classified (or premise ontology)"
			+ "\n   "
			+ optOutput
			+ "FILE             output with the inferred data"
			+ "\n   "
			+ optClassURI
			+ "CLASS_URI      (only for "
			+ cmdSat
			+ ") URI of the class to check satisfiability"
			+ "\n\nthe types are:"
			+ "\n   CLASS_URI                 a class URI, e.g.: http://www.w3.org/2002/07/owl#Thing"
			+ "\n   FILE                      a file name, e.g.: /tmp/inputOntology.owl"
			+ "\n   LEVEL                     a "
			+ (Level.class).getName()
			+ " number or string, e.g. "
			+ Level.INFO.intValue()
			+ " or "
			+ " \n                             "
			+ Level.OFF.getName()
			+ " | "
			+ Level.SEVERE.getName()
			+ " | "
			+ Level.WARNING.getName()
			+ " | "
			+ Level.INFO.getName()
			+ " | "
			+ Level.CONFIG.getName()
			+ " | "
			+ Level.FINE.getName()
			+ " | "
			+ Level.FINER.getName()
			+ " | "
			+ Level.FINEST.getName()
			+ " | "
			+ Level.ALL.getName()
			+ "\n   MILLISECONDS              a natural number, e.g.: 300000"
			+ "\n   OPERATION                 "
			+ cmdConsistency
			+ " | "
			+ cmdSat
			+ " | "
			+ cmdClassification
			+ " | "
			+ cmdEntailment
			+ "\n   RENDERER                  "
			+ rendererFunctional
			+ " | "
			+ rendererKRSS
			+ " | "
			+ rendererKRSS2
			+ " | "
			+ rendererKRSS2OWL
			+ " | "
			+ rendererLatex
			+ " | "
			+ rendererManchester
			+ " | "
			+ rendererTutorial + " | " + rendererXML + "\n\n\n\n";
	private long timeOut = 0;
	private boolean timeOutMode = false;
	private boolean verboseMode = false;

	private final PrintStream verboseModeOutput = System.out;

	public ConsoleStarter() {
	}

	/**
	 * Checks the consistency of a given ontology.
	 * 
	 * @param ontologyFile
	 *            ontology file to be checked
	 * @throws OWLOntologyCreationException
	 */
	public boolean checkConsistency(File ontologyFile)
			throws OWLOntologyCreationException {
		if (ontologyFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		JcelReasoner reasoner = createReasoner(ontologyFile);
		OWLDataFactory dataFactory = reasoner.getRootOntology()
				.getOWLOntologyManager().getOWLDataFactory();
		boolean ret = !reasoner.getEquivalentClasses(dataFactory.getOWLThing())
				.contains(dataFactory.getOWLNothing());
		return ret;
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
	 * Checks satisfiability of a given concept with respect to an ontology.
	 * 
	 * @param ontologyFile
	 *            ontology file to be checked
	 * @param conceptIRI
	 *            concept IRI
	 * @throws OWLOntologyCreationException
	 */
	public boolean checkSatisfiability(File ontologyFile, IRI conceptIRI)
			throws OWLOntologyCreationException {
		if (ontologyFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (conceptIRI == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		JcelReasoner reasoner = createReasoner(ontologyFile);
		OWLDataFactory dataFactory = reasoner.getRootOntology()
				.getOWLOntologyManager().getOWLDataFactory();
		boolean ret = !reasoner.getEquivalentClasses(
				dataFactory.getOWLClass(conceptIRI)).contains(
				dataFactory.getOWLNothing());
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
	 */
	public void computeClassification(File ontologyFile, File inferredFile,
			AbstractOWLRenderer renderer) throws OWLOntologyCreationException,
			OWLRendererException, FileNotFoundException {
		if (ontologyFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (inferredFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		JcelReasoner reasoner = createReasoner(ontologyFile);

		logger.fine("precomputing inferences ...");
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		logger.fine("generating output ...");
		OWLInferredOntologyWrapper inferredOntology = new OWLInferredOntologyWrapper(
				reasoner);

		renderer.render(inferredOntology.getOWLOntology(),
				new FileOutputStream(inferredFile));

		logger.fine("jcel console finished.");
	}

	/**
	 * Creates an instance of jcel reasoner using the given ontology file.
	 * 
	 * @param ontologyFile
	 *            ontology file
	 * @return an instance of jcel reasoner
	 * @throws OWLOntologyCreationException
	 */
	public JcelReasoner createReasoner(File ontologyFile)
			throws OWLOntologyCreationException {
		if (ontologyFile == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		logger.fine("starting jcel console ...");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		logger.fine("loading ontology using the OWL API ...");
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(ontologyFile);

		logger.fine("starting reasoner ...");

		long wallClockTimeBeginning = (new Date()).getTime();

		JcelReasoner ret = new JcelReasoner(ontology, false);

		long wallClockTimeMidPoint = (new Date()).getTime();

		TimeOutMonitor monitor = new TimeOutMonitor(ret, this.timeOut
				- (wallClockTimeMidPoint - wallClockTimeBeginning));

		if (this.timeOutMode) {
			monitor.start();
		}

		logger.fine("precomputing inferences ...");

		ret.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		long wallClockTimeEnd = (new Date()).getTime();

		if (this.timeOutMode) {
			monitor.interrupt();
		}

		if (this.verboseMode) {
			this.verboseModeOutput.println(msgPartOperationTime
					+ (wallClockTimeEnd - wallClockTimeBeginning));
		}

		return ret;
	}

	public Mode parseMode(String argument) {
		if (argument == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Mode mode = Mode.NOTHING;
		if (argument.equals(cmdClassification)) {
			mode = Mode.CLASSIFICATION;
		} else if (argument.equals(cmdConsistency)) {
			mode = Mode.CONSISTENCY;
		} else if (argument.equals(cmdSatisfiability)
				|| argument.equals(cmdSat)) {
			mode = Mode.SATISFIABILITY;
		} else if (argument.equals(cmdQuery)) {
			mode = Mode.QUERY;
		} else if (argument.equals(cmdEntailment)) {
			mode = Mode.ENTAILMENT;
		} else {
			throw new IllegalArgumentException("Unrecognized mode: '"
					+ argument + "'");
		}
		return mode;
	}

	public AbstractOWLRenderer parseRenderer(String argument) {
		if (argument == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		AbstractOWLRenderer ret = null;
		if (argument.equals(rendererKRSS2OWL)) {
			ret = new KRSS2OWLSyntaxRenderer();
		} else if (argument.equals(rendererKRSS2)) {
			ret = new KRSS2SyntaxRenderer();
		} else if (argument.equals(rendererKRSS)) {
			ret = new KRSSSyntaxRenderer();
		} else if (argument.equals(rendererLatex)) {
			ret = new LatexRenderer();
		} else if (argument.equals(rendererManchester)) {
			ret = new ManchesterOWLSyntaxRenderer();
		} else if (argument.equals(rendererFunctional)) {
			ret = new OWLFunctionalSyntaxRenderer();
		} else if (argument.equals(rendererTutorial)) {
			ret = new OWLTutorialSyntaxRenderer();
		} else if (argument.equals(rendererXML)) {
			ret = new OWLXMLRenderer();
		}

		return ret;
	}

	public String renderMode(Mode mode) {
		if (mode == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		String ret = "";
		if (mode.equals(Mode.CLASSIFICATION)) {
			ret = cmdClassification;
		} else if (mode.equals(Mode.CONSISTENCY)) {
			ret = cmdConsistency;
		} else if (mode.equals(Mode.SATISFIABILITY)) {
			ret = cmdSat;
		} else if (mode.equals(Mode.QUERY)) {
			ret = cmdQuery;
		} else if (mode.equals(Mode.ENTAILMENT)) {
			ret = cmdEntailment;
		} else if (mode.equals(Mode.NOTHING)) {
			ret = "";
		} else {
			throw new IllegalStateException("Unrecognized mode: '" + mode + "'");
		}
		return ret;
	}

	public void start(String[] args) throws OWLRendererException,
			OWLOntologyCreationException, SecurityException, IOException {

		if (args.length == 0) {
			System.out.println(this.minihelp);
		} else {
			List<String> arguments = Arrays.asList(args);
			if (arguments.contains(optHelp)) {
				System.out.println(this.minihelp);
			} else if (arguments.contains(optVersion)) {
				System.out.println(versionInfo);
				System.out.println(licenseInfo);
			} else if (arguments.size() >= 3) {

				Mode operation = null;
				File ontologyFile = null;
				File outputFile = null;
				File conclusionFile = null;
				Level logLevel = Level.OFF;
				IRI classIRI = null;
				AbstractOWLRenderer renderer = null;

				for (String argument : arguments) {

					if (argument.startsWith(optOperation)) {
						operation = parseMode(argument.substring(optOperation
								.length()));

					} else if (argument.startsWith(optOntology)) {
						ontologyFile = new File(argument.substring(optOntology
								.length()));

					} else if (argument.startsWith(optOutput)) {
						outputFile = new File(argument.substring(optOutput
								.length()));

					} else if (argument.startsWith(optClassURI)) {
						classIRI = IRI.create(argument.substring(optClassURI
								.length()));

					} else if (argument.startsWith(optConclusion)) {
						conclusionFile = new File(
								argument.substring(optConclusion.length()));

					} else if (argument.startsWith(optRenderer)) {
						renderer = parseRenderer(argument.substring(optRenderer
								.length()));

					} else if (argument.startsWith(optLogLevel)) {
						logLevel = Level.parse(argument.substring(optLogLevel
								.length()));

					} else if (argument.startsWith(optTimeOut)) {
						this.timeOutMode = true;
						this.timeOut = Long.parseLong(argument
								.substring(optTimeOut.length()));

					} else if (argument.startsWith(optVerbose)) {
						this.verboseMode = true;

					}
				}

				if (operation == null) {
					operation = parseMode(arguments.get(0));
				}
				if (ontologyFile == null) {
					ontologyFile = new File(arguments.get(1));
				}
				if (outputFile == null) {
					outputFile = new File(arguments.get(2));
				}
				if ((classIRI == null) && operation.equals(Mode.SATISFIABILITY)) {
					classIRI = IRI.create(arguments.get(3));
				}
				if (renderer == null) {
					renderer = new OWLFunctionalSyntaxRenderer();
				}

				if (outputFile.getParentFile() != null) {
					outputFile.getParentFile().mkdirs();
				}

				logger.setLevel(logLevel);
				logger.addHandler(new OutputStreamHandler(System.out));

				try {

					if (this.verboseMode) {
						this.verboseModeOutput.println(msgPartStarted
								+ renderMode(operation) + msgPartOn
								+ ontologyFile.getAbsolutePath());
					}

					if (operation == Mode.CLASSIFICATION) {

						computeClassification(ontologyFile, outputFile,
								renderer);

					} else if (operation == Mode.SATISFIABILITY) {

						storeInFile("" + classIRI.toString() + ", "
								+ checkSatisfiability(ontologyFile, classIRI),
								outputFile);

					} else if (operation == Mode.CONSISTENCY) {

						storeInFile("" + checkConsistency(ontologyFile),
								outputFile);

					} else if (operation == Mode.QUERY) {
						throw new UnsupportedOperationException(
								"Operation is not supported yet.");

					} else if (operation == Mode.ENTAILMENT) {
						if (conclusionFile == null) {
							throw new IllegalArgumentException(
									"No conclusion file has been defined.");
						}

						storeInFile(
								""
										+ checkEntailment(ontologyFile,
												conclusionFile), outputFile);

					}

					if (this.verboseMode) {
						this.verboseModeOutput.println(msgPartCompleted
								+ renderMode(operation) + msgPartOn
								+ ontologyFile.getAbsolutePath());
					}

				} catch (RuntimeException e) {

					// TODO change the way the exception is displayed
					BufferedWriter output = new BufferedWriter(new FileWriter(
							outputFile.getAbsoluteFile() + errorSuffix, true));
					output.write(e.toString());
					output.flush();
					output.close();

				}

			} else {
				System.out.println(this.minihelp);
			}

		}
	}

	private void storeInFile(String output, File outputFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile,
				true));
		writer.write(output);
		writer.newLine();
		writer.flush();
		writer.close();
	}

}
