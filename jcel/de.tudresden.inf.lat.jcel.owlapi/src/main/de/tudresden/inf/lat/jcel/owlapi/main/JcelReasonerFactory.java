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

package de.tudresden.inf.lat.jcel.owlapi.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


/**
 * This factory creates a <code>JcelReasoner</code> based on an ontology
 * manager.
 * 
 * FIXME
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class JcelReasonerFactory implements OWLReasonerFactory {

	private static final Logger logger = Logger
			.getLogger("de.tudresden.inf.lat.jcel");

	/**
	 * This constructor is called when the reasoner is selected in Protege.
	 */
	public OWLReasoner createReasoner(OWLOntologyManager owlOntologyManager) {
		JcelReasoner ret = new JcelReasoner(owlOntologyManager.getOntologies()
				.iterator().next());
		logger.setLevel(Level.FINE);
		Long ts = ret.getStartTime().getTime();
		String logId = "" + ts;
		String fileName = "jcel-" + ret.getStartTime().getTime() + ".log";
		try {
			logger.addHandler(new LogFileHandler(new File(fileName)));
		} catch (SecurityException e) {
		} catch (IOException e) {
		}
		logger.fine("ts: " + ts + " ms, log: " + logId);
		return ret;
	}

	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology arg0,
			OWLReasonerConfiguration arg1) throws IllegalConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology arg0,
			OWLReasonerConfiguration arg1) throws IllegalConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReasonerName() {
		// TODO Auto-generated method stub
		return null;
	}
}
