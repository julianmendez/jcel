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

package de.tudresden.inf.lat.jcel.protege.main;

import org.protege.editor.owl.model.inference.ProtegeOWLReasonerFactoryAdapter;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;

/**
 * This factory creates a <code>JcelOldReasoner</code> based on an ontology.
 * 
 * @author Julian Mendez
 * 
 * @see JcelOldReasoner
 */
public class JcelOldReasonerFactory extends ProtegeOWLReasonerFactoryAdapter {

	// private static final Logger logger = Logger
	// .getLogger("de.tudresden.inf.lat.jcel");

	/**
	 * This constructor is called when the reasoner is selected in Protege 4.0.
	 */
	public org.semanticweb.owl.inference.OWLReasoner createReasoner(
			org.semanticweb.owl.model.OWLOntologyManager ontologyManager) {
		return new JcelOldReasoner(ontologyManager, null);
	}

	/**
	 * This constructor is called when the reasoner is selected in Protege 4.1.
	 */
	public OWLReasoner createReasoner(OWLOntology ontology,
			ReasonerProgressMonitor progressMonitor) {
		return new JcelReasoner(ontology, progressMonitor);
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void initialise() throws Exception {
	}
}
