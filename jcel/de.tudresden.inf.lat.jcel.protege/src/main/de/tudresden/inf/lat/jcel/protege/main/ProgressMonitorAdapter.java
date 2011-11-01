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

import org.semanticweb.owlapi.util.ProgressMonitor;

/**
 * This class is used to adapt a progress monitor in the OWL API 2 to one in the
 * OWL API 3.
 * 
 * @author Julian Mendez
 */
public class ProgressMonitorAdapter implements ProgressMonitor {

	private org.semanticweb.owl.util.ProgressMonitor progressMonitor = null;

	public ProgressMonitorAdapter(
			org.semanticweb.owl.util.ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	@Override
	public boolean isCancelled() {
		return this.progressMonitor.isCancelled();
	}

	@Override
	public void setFinished() {
		this.progressMonitor.setFinished();
	}

	@Override
	public void setIndeterminate(boolean indeterminate) {
		this.progressMonitor.setIndeterminate(indeterminate);
	}

	@Override
	public void setMessage(String message) {
		this.progressMonitor.setMessage(message);
	}

	@Override
	public void setProgress(long progress) {
		this.progressMonitor.setProgress(progress);
	}

	@Override
	public void setSize(long size) {
		this.progressMonitor.setSize(size);
	}

	@Override
	public void setStarted() {
		this.progressMonitor.setStarted();
	}
}
