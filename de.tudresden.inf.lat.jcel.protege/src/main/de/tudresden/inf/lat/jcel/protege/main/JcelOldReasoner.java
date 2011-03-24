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

import java.util.Date;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import de.tudresden.inf.lat.jcel.adapter.OWLReasonerAdapter;
import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;

/**
 * An object of this class is a reasoner that is compatible with Protege.
 * 
 * @author Julian Mendez
 */
public class JcelOldReasoner extends OWLReasonerAdapter implements
		org.semanticweb.owl.inference.MonitorableOWLReasoner {

	private org.semanticweb.owl.util.ProgressMonitor progressMonitor = null;
	private Date start = new Date();

	public JcelOldReasoner(org.semanticweb.owl.model.OWLOntologyManager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		setOWLOntologyManager(manager);
	}

	public JcelOldReasoner(
			org.semanticweb.owl.model.OWLOntologyManager manager,
			org.semanticweb.owl.util.ProgressMonitor monitor) {
		if (manager == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (monitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		setOWLOntologyManager(manager);
		setProgressMonitor(monitor);
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology ontology) {
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLReasoner ret = null;
		if (progressMonitor != null) {
			ret = new JcelReasoner(ontology, new ProgressMonitorAdapter(
					progressMonitor));
		} else {
			ret = new JcelReasoner(ontology);
		}
		return ret;
	}

	@Override
	public org.semanticweb.owl.model.OWLEntity getCurrentEntity() {
		return null;
	}

	public org.semanticweb.owl.util.ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

	public Date getStartTime() {
		return this.start;
	}

	@Override
	public void setProgressMonitor(
			org.semanticweb.owl.util.ProgressMonitor monitor) {
		if (monitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.progressMonitor = monitor;
	}
}
