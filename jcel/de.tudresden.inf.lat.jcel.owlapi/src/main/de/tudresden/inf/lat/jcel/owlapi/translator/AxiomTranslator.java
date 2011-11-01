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

package de.tudresden.inf.lat.jcel.owlapi.translator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object of this class is a visitor use to translate an
 * <code>OWLAxiom</code> into an <code>IntegerAxiom</code>. This is an auxiliary
 * class used by <code>AxiomSetTranslator</code>.
 * 
 * @author Julian Mendez
 * 
 * @see AxiomSetTranslator
 */
class AxiomTranslator implements OWLAxiomVisitorEx<IntegerAxiom> {

	// private static final Logger logger =
	// Logger.getLogger(AxiomTranslator.class
	// .getName());

	private Map<OWLClass, Integer> classMap = null;
	private Map<OWLNamedIndividual, Integer> individualMap = null;
	private Map<OWLObjectProperty, Integer> propertyMap = null;

	public AxiomTranslator(Map<OWLObjectProperty, Integer> propMap,
			Map<OWLClass, Integer> clMap,
			Map<OWLNamedIndividual, Integer> indivMap) {
		this.propertyMap = propMap;
		this.classMap = clMap;
		this.individualMap = indivMap;
	}

	protected Integer getId(OWLClass owlClass) throws TranslationException {
		Integer ret = this.classMap.get(owlClass);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(owlClass
					.toString());
		}
		return ret;
	}

	protected Integer getId(OWLIndividual individual)
			throws TranslationException {
		Integer ret = this.individualMap.get(individual);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(individual
					.toString());
		}
		return ret;
	}

	protected Integer getId(OWLObjectProperty property)
			throws TranslationException {
		Integer ret = this.propertyMap.get(property);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(property
					.toString());
		}
		return ret;
	}

	protected IntegerClassExpression translate(OWLClassExpression owlDescription)
			throws TranslationException {
		IntegerClassExpression ret = null;
		if (owlDescription instanceof OWLClass) {
			ret = translateClass((OWLClass) owlDescription);
		} else if (owlDescription instanceof OWLObjectSomeValuesFrom) {
			ret = translateSomeRestriction((OWLObjectSomeValuesFrom) owlDescription);
		} else if (owlDescription instanceof OWLObjectIntersectionOf) {
			ret = translateIntersection((OWLObjectIntersectionOf) owlDescription);
		} else {
			throw new TranslationException(
					"This description is not supported: '" + owlDescription
							+ "'.");
		}
		return ret;
	}

	protected IntegerClassExpression translateClass(OWLClass owlClass)
			throws TranslationException {
		return new IntegerClass(getId(owlClass));
	}

	protected IntegerClassExpression translateIndividual(
			OWLIndividual owlIndividual) throws TranslationException {
		return new IntegerClass(getId(owlIndividual));
	}

	protected IntegerClassExpression translateIntersection(
			OWLObjectIntersectionOf intersection) throws TranslationException {
		Set<OWLClassExpression> operands = intersection.getOperands();
		Set<IntegerClassExpression> descriptionList = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression elem : operands) {
			descriptionList.add(translate(elem));
		}
		return new IntegerObjectIntersectionOf(descriptionList);
	}

	protected IntegerClassExpression translateSomeRestriction(
			OWLObjectSomeValuesFrom restriction) throws TranslationException {
		IntegerClassExpression ret = null;
		OWLObjectPropertyExpression propExpr = restriction.getProperty();
		if (propExpr instanceof OWLObjectProperty) {
			Integer propertyId = getId(propExpr.asOWLObjectProperty());
			OWLClassExpression desc = restriction.getFiller();
			IntegerClassExpression description = translate(desc);
			ret = new IntegerObjectSomeValuesFrom(propertyId, description);
		} else {
			throw new TranslationException(
					"Property expression cannot be translated.");
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLAnnotationAssertionAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLAnnotationPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLAnnotationPropertyRangeAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLClassAssertionAxiom axiom) {
		Integer individualId = getId(axiom.getIndividual());
		return new IntegerSubClassOfAxiom(new IntegerClass(individualId),
				translate(axiom.getClassExpression()));
	}

	@Override
	public IntegerAxiom visit(OWLDataPropertyAssertionAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDataPropertyDomainAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDataPropertyRangeAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDatatypeDefinitionAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDeclarationAxiom axiom) {
		IntegerAxiom ret = null;
		OWLEntity entity = axiom.getEntity();
		if (entity.isOWLClass()) {
			ret = new IntegerClassDeclarationAxiom(getId(entity.asOWLClass()));
		} else if (entity.isOWLObjectProperty()) {
			ret = new IntegerObjectPropertyDeclarationAxiom(getId(entity
					.asOWLObjectProperty()));
		} else {
			throw TranslationException.newUnsupportedAxiomException(axiom);
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLDifferentIndividualsAxiom axiom) {
		IntegerAxiom ret = null;
		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLIndividual individual : individualSet) {
			classIdSet.add(translateIndividual(individual));
		}
		ret = new IntegerDisjointClassesAxiom(classIdSet);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLDisjointClassesAxiom axiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		Set<OWLClassExpression> descriptionSet = axiom.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression description : descriptionSet) {
			classIdSet.add(translate(description));
		}
		ret = new IntegerDisjointClassesAxiom(classIdSet);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLDisjointDataPropertiesAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDisjointObjectPropertiesAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDisjointUnionAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLEquivalentClassesAxiom axiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		Set<OWLClassExpression> descriptionSet = axiom.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression description : descriptionSet) {
			classIdSet.add(translate(description));
		}
		ret = new IntegerEquivalentClassesAxiom(classIdSet);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLEquivalentDataPropertiesAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLEquivalentObjectPropertiesAxiom axiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		Set<OWLObjectPropertyExpression> propertySet = axiom.getProperties();
		Set<Integer> propertyIdSet = new HashSet<Integer>();
		for (OWLObjectPropertyExpression propertyExpr : propertySet) {
			if (propertyExpr instanceof OWLObjectProperty) {
				propertyIdSet.add(getId(propertyExpr.asOWLObjectProperty()));
			} else {
				throw new IllegalStateException();
			}

		}
		ret = new IntegerEquivalentObjectPropertiesAxiom(propertyIdSet);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLFunctionalDataPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLFunctionalObjectPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLHasKeyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLInverseObjectPropertiesAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {
		IntegerAxiom ret = null;
		OWLObjectPropertyExpression propExpr = axiom.getProperty();
		if (propExpr instanceof OWLObjectProperty) {
			Integer propertyId = getId(propExpr.asOWLObjectProperty());
			Integer objectId = getId(axiom.getObject());
			IntegerObjectSomeValuesFrom restriction = new IntegerObjectSomeValuesFrom(
					propertyId, new IntegerClass(objectId));
			Integer subjectId = getId(axiom.getSubject());
			ret = new IntegerSubClassOfAxiom(new IntegerClass(subjectId),
					restriction);
		} else {
			throw new TranslationException(
					"Property expression cannot be translated: '" + propExpr
							+ "'.");
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLObjectPropertyDomainAxiom axiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		Integer propertyId = getId(property);
		OWLClassExpression owlDescription = axiom.getDomain();
		if (owlDescription instanceof OWLClass) {
			Integer classId = getId(owlDescription.asOWLClass());
			ret = new GCI3Axiom(propertyId, IntegerDatatype.classTopElement,
					classId);
		} else {
			IntegerClassExpression superDescription = translate(owlDescription);
			IntegerClassExpression subDescription = new IntegerObjectSomeValuesFrom(
					propertyId, new IntegerClass(
							IntegerDatatype.classTopElement));
			ret = new IntegerSubClassOfAxiom(subDescription, superDescription);
		}
		Integer propId = getId(property);
		ret = new RI1Axiom(propId);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLObjectPropertyRangeAxiom rangeAxiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		OWLObjectPropertyExpression propertyExpr = rangeAxiom.getProperty();
		OWLClassExpression description = rangeAxiom.getRange();
		if (propertyExpr instanceof OWLObjectProperty) {

			ret = new IntegerPropertyRangeAxiom(getId(propertyExpr
					.asOWLObjectProperty()), translate(description));
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLReflexiveObjectPropertyAxiom axiom) {
		IntegerAxiom ret = null;
		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		Integer propId = getId(property);
		ret = new RI1Axiom(propId);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLSameIndividualAxiom axiom) {
		IntegerAxiom ret = null;
		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLIndividual individual : individualSet) {
			classIdSet.add(translateIndividual(individual));
		}
		ret = new IntegerEquivalentClassesAxiom(classIdSet);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLSubClassOfAxiom classAxiom)
			throws TranslationException {
		OWLClassExpression owlSubClass = classAxiom.getSubClass();
		OWLClassExpression owlSuperClass = classAxiom.getSuperClass();
		IntegerClassExpression leftDescription = translate(owlSubClass);
		IntegerClassExpression rightDescription = translate(owlSuperClass);
		IntegerAxiom ret = new IntegerSubClassOfAxiom(leftDescription,
				rightDescription);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLSubDataPropertyOfAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLSubObjectPropertyOfAxiom propAxiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		OWLObjectPropertyExpression leftPropExpr = propAxiom.getSubProperty();
		OWLObjectPropertyExpression rightPropExpr = propAxiom
				.getSuperProperty();
		if (leftPropExpr instanceof OWLObjectProperty
				&& rightPropExpr instanceof OWLObjectProperty) {
			ret = new RI2Axiom(getId(leftPropExpr.asOWLObjectProperty()),
					getId(rightPropExpr.asOWLObjectProperty()));
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLSubPropertyChainOfAxiom propAxiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		List<OWLObjectPropertyExpression> propChain = propAxiom
				.getPropertyChain();
		OWLObjectPropertyExpression rightPart = propAxiom.getSuperProperty();
		if (rightPart instanceof OWLObjectProperty) {

			List<Integer> chain = new ArrayList<Integer>();
			for (OWLObjectPropertyExpression property : propChain) {
				if (property instanceof OWLObjectProperty) {
					chain.add(getId(property.asOWLObjectProperty()));
				} else {
					throw new TranslationException(
							"Unexpected property expression "
									+ property.toString());
				}
			}

			ret = new IntegerSubPropertyChainOfAxiom(chain, getId(rightPart
					.asOWLObjectProperty()));

		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLSymmetricObjectPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLTransitiveObjectPropertyAxiom axiom)
			throws TranslationException {
		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		return new IntegerTransitiveObjectPropertyAxiom(getId(property));
	}

	@Override
	public IntegerAxiom visit(SWRLRule axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}
}
