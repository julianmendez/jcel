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

package de.tudresden.inf.lat.jcel.reasoner.main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiomVisitor;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDataPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDataPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDifferentIndividualsAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerFunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseFunctionalObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerInverseObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerNamedIndividualDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerNegativeObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerReflexiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSameIndividualAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubObjectPropertyOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * An object of this class checks ontology entailment.
 * 
 * @author Julian Mendez
 */
public class OntologyEntailmentChecker implements
		ComplexIntegerAxiomVisitor<Boolean> {

	private static final String errorMsg = "Unsupported entailment with axiom:";
	private final RuleBasedReasoner reasoner;

	/**
	 * Constructs a new ontology entailment checker.
	 * 
	 * @param reasoner
	 *            reasoner
	 */
	public OntologyEntailmentChecker(RuleBasedReasoner reasoner) {
		if (reasoner == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.reasoner = reasoner;
	}

	public RuleBasedReasoner getReasoner() {
		return this.reasoner;
	}

	@Override
	public Boolean visit(IntegerClassAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerClassDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = getReasoner().getProcessor().getClassHierarchy()
				.getElements().contains(axiom.getEntity());
		return ret;
	}

	@Override
	public Boolean visit(IntegerDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerDataPropertyDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerDifferentIndividualsAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerDisjointClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerEquivalentClassesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = true;
		Set<IntegerClassExpression> set = axiom.getClassExpressions();
		if (!set.isEmpty()) {
			IntegerClass representative = getReasoner().flattenClassExpression(
					set.iterator().next());
			Set<IntegerClass> classSet = new HashSet<IntegerClass>();
			for (IntegerClassExpression classExpr : set) {
				classSet.add(getReasoner().flattenClassExpression(classExpr));
			}
			getReasoner().classify();
			for (Iterator<IntegerClass> it = classSet.iterator(); ret
					&& it.hasNext();) {
				IntegerClass currentClass = it.next();
				ret = ret
						&& getReasoner().getProcessor().getClassHierarchy()
								.getEquivalents(currentClass.getId())
								.contains(representative.getId());

			}
		}
		return ret;
	}

	@Override
	public Boolean visit(IntegerEquivalentObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerInverseFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerInverseObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerNamedIndividualDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerNegativeObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerObjectPropertyDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return getReasoner().getProcessor().getObjectPropertyHierarchy()
				.getElements().contains(axiom.getEntity());
	}

	@Override
	public Boolean visit(IntegerPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerReflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerSameIndividualAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerSubClassOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerClassExpression subClassExpr = axiom.getSubClass();
		IntegerClassExpression superClassExpr = axiom.getSuperClass();
		IntegerClass subClass = getReasoner().flattenClassExpression(
				subClassExpr);
		IntegerClass superClass = getReasoner().flattenClassExpression(
				superClassExpr);
		getReasoner().classify();

		boolean isAncestor = getReasoner().getSuperClasses(subClass, false)
				.contains(superClass);
		boolean isEquivalent = getReasoner().getEquivalentClasses(subClass)
				.contains(superClass);

		return (isAncestor || isEquivalent);
	}

	@Override
	public Boolean visit(IntegerSubObjectPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		IntegerObjectPropertyExpression subObjectPropExpr = axiom
				.getSubProperty();
		IntegerObjectPropertyExpression superObjectPropExpr = axiom
				.getSuperProperty();
		getReasoner().classify();

		boolean isAncestor = getReasoner().getSuperObjectProperties(
				subObjectPropExpr, false).contains(superObjectPropExpr);
		boolean isEquivalent = getReasoner().getEquivalentObjectProperties(
				subObjectPropExpr).contains(superObjectPropExpr);

		return (isAncestor || isEquivalent);
	}

	@Override
	public Boolean visit(IntegerSubPropertyChainOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

	@Override
	public Boolean visit(IntegerTransitiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new UnsupportedQueryException(errorMsg + axiom);
	}

}
