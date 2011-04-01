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

import org.protege.editor.owl.model.inference.AbstractProtegeOWLReasonerInfo;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import de.tudresden.inf.lat.jcel.owlapi.main.JcelOWLReasonerFactory;
import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;

/**
 * This factory is used by Protege 4.1 .
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class JcelProtegeReasonerFactory extends AbstractProtegeOWLReasonerInfo {

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void initialise() throws Exception {
	}

	@Override
	public OWLReasonerFactory getReasonerFactory() {
		return new JcelOWLReasonerFactory();
	}

	@Override
	public BufferingMode getRecommendedBuffering() {
		return BufferingMode.NON_BUFFERING;
	}

}
