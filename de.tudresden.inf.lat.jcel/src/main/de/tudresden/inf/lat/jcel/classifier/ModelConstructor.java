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

package de.tudresden.inf.lat.jcel.classifier;

import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owl.model.OWLOntology;

/**
 * An object of this class can create a model from an ontology.
 * 
 * @author Julian Mendez
 */
public class ModelConstructor {

	private static final Logger logger = Logger
			.getLogger(ModelConstructor.class.getName());

	private JcelClassifier classifier = null;

	public ModelConstructor(JcelClassifier classif) {
		this.classifier = classif;
	}

	/**
	 * Creates a model from an ontology. Although the parameter takes a set of
	 * ontologies, only one is taken into account.
	 * 
	 * @param ontologySet
	 *            the ontology set which should be a singleton
	 * @return a classified model for the given ontology
	 * @throws ModelConstructionException
	 *             if the model could not be constructed
	 */
	public JcelModel createModel(Set<OWLOntology> ontologySet)
			throws ModelConstructionException {

		OWLOntology ontology = ontologySet.iterator().next();
		logger.fine("classifying " + ontology.getURI().normalize());
		getClassifier().resetAndLoad(ontology.getAxioms());
		return new JcelModel(getClassifier().getRelationGraph(),
				getClassifier().getTypeGraph(), getClassifier()
						.getReflexiveProperties(), getClassifier()
						.getTransitiveProperties());
	}

	/**
	 * Returns the classifier used for the classification.
	 * 
	 * @return the classifier used for the classification
	 */
	public JcelClassifier getClassifier() {
		return this.classifier;
	}
}
