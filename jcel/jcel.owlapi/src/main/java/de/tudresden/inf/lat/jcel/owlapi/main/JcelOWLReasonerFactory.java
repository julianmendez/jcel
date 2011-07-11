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

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


/**
 * This factory creates an instance of a jcel reasoner based on an ontology and
 * a optional configuration.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class JcelOWLReasonerFactory implements OWLReasonerFactory {

	// private static final Logger logger = Logger
	// .getLogger("de.tudresden.inf.lat.jcel");

	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology ontology) {
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return new JcelReasoner(ontology);
	}

	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology ontology,
			OWLReasonerConfiguration configuration)
			throws IllegalConfigurationException {
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (configuration == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return new JcelReasoner(ontology, configuration);
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology ontology) {
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return new JcelReasoner(ontology);
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology ontology,
			OWLReasonerConfiguration configuration)
			throws IllegalConfigurationException {
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (configuration == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return new JcelReasoner(ontology, configuration);
	}

	@Override
	public String getReasonerName() {
		return getClass().getPackage().getImplementationTitle();
	}

}
