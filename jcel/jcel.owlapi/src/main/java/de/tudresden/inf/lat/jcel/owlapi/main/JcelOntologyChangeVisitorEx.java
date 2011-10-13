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

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyChangeVisitorEx;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.RemoveImport;
import org.semanticweb.owlapi.model.RemoveOntologyAnnotation;
import org.semanticweb.owlapi.model.SetOntologyID;

/**
 * 
 * @author Julian Mendez
 */
public class JcelOntologyChangeVisitorEx implements
		OWLOntologyChangeVisitorEx<Boolean> {

	private JcelReasoner reasoner;

	public JcelOntologyChangeVisitorEx(JcelReasoner reasoner) {
		if (reasoner == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.reasoner = reasoner;
	}

	@Override
	public Boolean visit(AddAxiom change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.reasoner.addAxiom(change.getAxiom());
	}

	@Override
	public Boolean visit(AddImport change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

	@Override
	public Boolean visit(AddOntologyAnnotation change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

	@Override
	public Boolean visit(RemoveAxiom change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.reasoner.removeAxiom(change.getAxiom());
	}

	@Override
	public Boolean visit(RemoveImport change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

	@Override
	public Boolean visit(RemoveOntologyAnnotation change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

	@Override
	public Boolean visit(SetOntologyID change) {
		if (change == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

}
