/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
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

package de.tudresden.inf.lat.jcel.owlapi.main;

import java.util.Objects;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import javax.annotation.Nonnull;

/**
 * This factory creates an instance of a jcel reasoner based on an ontology and
 * a optional configuration.
 * 
 * @author Julian Mendez
 */
public class JcelReasonerFactory implements OWLReasonerFactory {

	// private static final Logger logger = Logger
	// .getLogger("de.tudresden.inf.lat.jcel");

	// @Override
	public JcelReasoner createNonBufferingReasoner(OWLOntology ontology, int startId) {
		Objects.requireNonNull(ontology);
		return new JcelReasoner(ontology, false, startId);
	}

	// @Override
	public JcelReasoner createNonBufferingReasoner(OWLOntology ontology, int startId, OWLReasonerConfiguration configuration)
			throws IllegalConfigurationException {
		Objects.requireNonNull(ontology);
		Objects.requireNonNull(configuration);
		return new JcelReasoner(ontology, false, configuration, startId);
	}

	// @Override
	public JcelReasoner createReasoner(OWLOntology ontology, int startId) {
		Objects.requireNonNull(ontology);
		return new JcelReasoner(ontology, true, startId);
	}

	// @Override
	public JcelReasoner createReasoner(OWLOntology ontology, OWLReasonerConfiguration configuration, int startId)
			throws IllegalConfigurationException {
		Objects.requireNonNull(ontology);
		Objects.requireNonNull(configuration);
		return new JcelReasoner(ontology, true, configuration, startId);
	}

	@Override
	public String getReasonerName() {
		return getClass().getPackage().getImplementationTitle();
	}

	@Nonnull
	@Override
	public OWLReasoner createNonBufferingReasoner(@Nonnull OWLOntology owlOntology) {
		return null;
	}

	@Nonnull
	@Override
	public OWLReasoner createReasoner(@Nonnull OWLOntology owlOntology) {
		return null;
	}

	@Nonnull
	@Override
	public OWLReasoner createNonBufferingReasoner(@Nonnull OWLOntology owlOntology, @Nonnull OWLReasonerConfiguration owlReasonerConfiguration) {
		return null;
	}

	@Nonnull
	@Override
	public OWLReasoner createReasoner(@Nonnull OWLOntology owlOntology, @Nonnull OWLReasonerConfiguration owlReasonerConfiguration) {
		return null;
	}

}
