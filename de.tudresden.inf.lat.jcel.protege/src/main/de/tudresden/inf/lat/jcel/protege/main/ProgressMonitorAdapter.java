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

import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

/**
 * This class is used to adapt a progress monitor in the OWL API 2 to a reasoner
 * progress monitor in the OWL API 3.
 * 
 * @author Julian Mendez
 */
public class ProgressMonitorAdapter implements ReasonerProgressMonitor {

	private org.semanticweb.owl.util.ProgressMonitor progressMonitor = null;

	public ProgressMonitorAdapter(
			org.semanticweb.owl.util.ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	@Override
	public void reasonerTaskBusy() {
		if (this.progressMonitor.isCancelled()) {
			throw new ReasonerInterruptedException(
					"Execution cancelled by user.");
		}
		this.progressMonitor.setIndeterminate(true);
	}

	@Override
	public void reasonerTaskProgressChanged(int progress, int size) {
		if (this.progressMonitor.isCancelled()) {
			throw new ReasonerInterruptedException(
					"Execution cancelled by user.");
		}
		this.progressMonitor.setIndeterminate(false);
		this.progressMonitor.setSize(size);
		this.progressMonitor.setProgress(progress);
	}

	@Override
	public void reasonerTaskStarted(String message) {
		this.progressMonitor.setMessage(message);
		this.progressMonitor.setStarted();
	}

	@Override
	public void reasonerTaskStopped() {
		this.progressMonitor.setFinished();
	}
}
