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

import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

/**
 * An object of this class is a configuration for jcel.
 * 
 * @author Julian Mendez
 */
public class JcelConfiguration implements OWLReasonerConfiguration {

	private FreshEntityPolicy freshEntityPolicy = null;
	private IndividualNodeSetPolicy individualNodeSetPolicy = null;
	private ReasonerProgressMonitor progressMonitor = null;
	private Long timeOut = Long.MAX_VALUE;

	public JcelConfiguration(ReasonerProgressMonitor monitor) {
		if (monitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.progressMonitor = monitor;
	}

	@Override
	public FreshEntityPolicy getFreshEntityPolicy() {
		return this.freshEntityPolicy;
	}

	@Override
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		return this.individualNodeSetPolicy;
	}

	@Override
	public ReasonerProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

	@Override
	public long getTimeOut() {
		return this.timeOut;
	}

}
