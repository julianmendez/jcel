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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IdGenerator;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * <p>
 * <ul>
 * <li>NR-3.1 : C' &#8849; &exist; r <i>.</i> D &#8605; C' &#8849; A, A &#8849;
 * &exist; r <i>.</i> D</li>
 * </ul>
 * </p>
 * 
 * This is a modified version of NR3-1 since the original rule is:
 * 
 * <ul>
 * <li>NR-3.1 : C' &#8849; D' &#8605; C' &#8849; A, A &#8849; D'</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR3_1 implements NormalizationRule {

	private ComplexIntegerAxiomFactory axiomFactory = null;

	private IdGenerator nameGenerator = null;

	/**
	 * Constructs a new normalizer rule NR-3.1.
	 * 
	 * @param generator
	 *            an identifier generator
	 * @param factory
	 *            axiom factory
	 */
	public NormalizerNR3_1(IdGenerator generator,
			ComplexIntegerAxiomFactory factory) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.nameGenerator = generator;
		this.axiomFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubClassOfAxiom) {
			ret = applyRule((IntegerSubClassOfAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(IntegerSubClassOfAxiom classAxiom) {
		Set<IntegerAxiom> ret = Collections.emptySet();
		IntegerClassExpression subClass = classAxiom.getSubClass();
		IntegerClassExpression superClass = classAxiom.getSuperClass();
		if (!subClass.isLiteral()
				&& superClass instanceof IntegerObjectSomeValuesFrom) {
			ret = new HashSet<IntegerAxiom>();
			IntegerClass newClass = new IntegerClass(
					this.nameGenerator.createNewClassId());
			ret.add(this.axiomFactory.createSubClassOfAxiom(subClass,
					newClass));
			ret.add(this.axiomFactory.createSubClassOfAxiom(newClass,
					superClass));
		}
		return ret;
	}

}
