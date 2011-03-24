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

package de.tudresden.inf.lat.jcel.core.normalization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeValuesFrom;

/**
 * <p>
 * <ul>
 * <li>NR 2-3 : &exist; r <i>.</i> C' &#8849; D &#8605; C' &#8849; A, &exist; r
 * <i>.</i> A &#8849; D</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_3 implements NormalizationRule {

	private IdGenerator nameGenerator = null;

	public NormalizerNR2_3(IdGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.nameGenerator = generator;
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
		if (subClass instanceof IntegerObjectSomeValuesFrom) {
			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			IntegerObjectPropertyExpression propertyExpression = restriction
					.getProperty();
			IntegerClassExpression filler = restriction.getFiller();
			if (!filler.isLiteral()) {
				ret = new HashSet<IntegerAxiom>();
				IntegerClass newClass = new IntegerClass(this.nameGenerator
						.createNewClassId());
				IntegerObjectSomeValuesFrom newExistential = new IntegerObjectSomeValuesFrom(
						propertyExpression, newClass);
				ret.add(new IntegerSubClassOfAxiom(filler, newClass));
				ret.add(new IntegerSubClassOfAxiom(newExistential, superClass));
			}
		}
		return ret;
	}

}
