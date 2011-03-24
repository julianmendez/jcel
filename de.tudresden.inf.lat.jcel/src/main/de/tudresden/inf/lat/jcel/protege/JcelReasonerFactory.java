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

package de.tudresden.inf.lat.jcel.protege;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.protege.editor.owl.model.inference.ProtegeOWLReasonerFactoryAdapter;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLOntologyManager;

import de.tudresden.inf.lat.jcel.owlapi.JcelReasoner;

/**
 * This factory creates a <code>JcelReasoner</code> based on an ontology
 * manager.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class JcelReasonerFactory extends ProtegeOWLReasonerFactoryAdapter {

	private static final Logger logger = Logger
			.getLogger("de.tudresden.inf.lat.jcel");

	/**
	 * This constructor is called when the reasoner is selected in Protege.
	 */
	@Override
	public OWLReasoner createReasoner(OWLOntologyManager owlOntologyManager) {
		JcelReasoner ret = new JcelReasoner(owlOntologyManager,
				new SwingProgressMonitor());
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
	public void dispose() throws Exception {
	}

	/**
	 * This function is invoked when protege starts its execution. The logger
	 * and logging level is defined here.
	 * 
	 * @see org.protege.editor.core.plugin.ProtegePluginInstance#initialise()
	 */
	@Override
	public void initialise() throws Exception {
	}
}
