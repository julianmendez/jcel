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

package de.tudresden.inf.lat.jcel.translation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLAxiomVisitorEx;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDeclarationAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owl.model.SWRLRule;

import de.tudresden.inf.lat.jcel.core.axiom.GCI3Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerObjectPropertyChainSubPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerTransitiveObjectPropertyAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectSomeRestriction;

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

	private Map<OWLClass, Integer> classMap = null;
	private Map<OWLIndividual, Integer> individualMap = null;
	private Map<OWLObjectProperty, Integer> propertyMap = null;

	public AxiomTranslator(Map<OWLObjectProperty, Integer> propMap,
			Map<OWLClass, Integer> clMap, Map<OWLIndividual, Integer> indivMap) {
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

	protected IntegerDescription translate(OWLDescription owlDescription)
			throws TranslationException {
		IntegerDescription ret = null;
		if (owlDescription instanceof OWLClass) {
			ret = translateClass((OWLClass) owlDescription);
		} else if (owlDescription instanceof OWLObjectSomeRestriction) {
			ret = translateSomeRestriction((OWLObjectSomeRestriction) owlDescription);
		} else if (owlDescription instanceof OWLObjectIntersectionOf) {
			ret = translateIntersection((OWLObjectIntersectionOf) owlDescription);
		} else {
			throw new TranslationException(
					"This description is not supported: '" + owlDescription
							+ "'.");
		}
		return ret;
	}

	protected IntegerDescription translateClass(OWLClass owlClass)
			throws TranslationException {
		return new IntegerClass(getId(owlClass));
	}

	protected IntegerDescription translateIndividual(OWLIndividual owlIndividual)
			throws TranslationException {
		return new IntegerClass(getId(owlIndividual));
	}

	protected IntegerDescription translateIntersection(
			OWLObjectIntersectionOf intersection) throws TranslationException {
		Set<OWLDescription> operands = intersection.getOperands();
		Set<IntegerDescription> descriptionList = new HashSet<IntegerDescription>();
		for (OWLDescription elem : operands) {
			descriptionList.add(translate(elem));
		}
		return new IntegerObjectIntersectionOf(descriptionList);
	}

	protected IntegerDescription translateSomeRestriction(
			OWLObjectSomeRestriction restriction) throws TranslationException {
		IntegerDescription ret = null;
		OWLObjectPropertyExpression propExpr = restriction.getProperty();
		if (propExpr instanceof OWLObjectProperty) {
			Integer propertyId = getId(propExpr.asOWLObjectProperty());
			OWLDescription desc = restriction.getFiller();
			IntegerDescription description = translate(desc);
			ret = new IntegerObjectSomeRestriction(propertyId, description);
		} else {
			throw new TranslationException(
					"Property expression cannot be translated.");
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLAxiomAnnotationAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLClassAssertionAxiom axiom) {
		Integer individualId = getId(axiom.getIndividual());
		return new IntegerSubClassAxiom(new IntegerClass(individualId),
				translate(axiom.getDescription()));
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
	public IntegerAxiom visit(OWLDataSubPropertyAxiom axiom) {
		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public IntegerAxiom visit(OWLDeclarationAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLDifferentIndividualsAxiom axiom) {
		IntegerAxiom ret = null;
		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<IntegerDescription> classIdSet = new HashSet<IntegerDescription>();
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
		Set<OWLDescription> descriptionSet = axiom.getDescriptions();
		Set<IntegerDescription> classIdSet = new HashSet<IntegerDescription>();
		for (OWLDescription description : descriptionSet) {
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
	public IntegerAxiom visit(OWLEntityAnnotationAxiom axiom) {
		return null;
	}

	@Override
	public IntegerAxiom visit(OWLEquivalentClassesAxiom axiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		Set<OWLDescription> descriptionSet = axiom.getDescriptions();
		Set<IntegerDescription> classIdSet = new HashSet<IntegerDescription>();
		for (OWLDescription description : descriptionSet) {
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
	public IntegerAxiom visit(OWLImportsDeclaration axiom) {
		throw new TranslationException(new IllegalStateException(
				"Import declation axiom should have been already processed."));
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
			IntegerObjectSomeRestriction restriction = new IntegerObjectSomeRestriction(
					propertyId, new IntegerClass(objectId));
			Integer subjectId = getId(axiom.getSubject());
			ret = new IntegerSubClassAxiom(new IntegerClass(subjectId),
					restriction);
		} else {
			throw new TranslationException(
					"Property expression cannot be translated: '" + propExpr
							+ "'.");
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLObjectPropertyChainSubPropertyAxiom propAxiom)
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

			ret = new IntegerObjectPropertyChainSubPropertyAxiom(chain,
					getId(rightPart.asOWLObjectProperty()));

		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLObjectPropertyDomainAxiom axiom)
			throws TranslationException {
		IntegerAxiom ret = null;
		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		Integer propertyId = getId(property);
		OWLDescription owlDescription = axiom.getDomain();
		if (owlDescription instanceof OWLClass) {
			Integer classId = getId(owlDescription.asOWLClass());
			ret = new GCI3Axiom(propertyId, IntegerDescription.THING, classId);
		} else {
			IntegerDescription superDescription = translate(owlDescription);
			IntegerDescription subDescription = new IntegerObjectSomeRestriction(
					propertyId, new IntegerClass(IntegerDescription.THING));
			ret = new IntegerSubClassAxiom(subDescription, superDescription);
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
		OWLDescription description = rangeAxiom.getRange();
		if (propertyExpr instanceof OWLObjectProperty) {

			ret = new IntegerRangeAxiom(getId(propertyExpr
					.asOWLObjectProperty()), translate(description));
		}
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLObjectSubPropertyAxiom propAxiom)
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
	public IntegerAxiom visit(OWLOntologyAnnotationAxiom axiom) {
		return null;
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
	public IntegerAxiom visit(OWLSameIndividualsAxiom axiom) {
		IntegerAxiom ret = null;
		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<IntegerDescription> classIdSet = new HashSet<IntegerDescription>();
		for (OWLIndividual individual : individualSet) {
			classIdSet.add(translateIndividual(individual));
		}
		ret = new IntegerEquivalentClassesAxiom(classIdSet);
		return ret;
	}

	@Override
	public IntegerAxiom visit(OWLSubClassAxiom classAxiom)
			throws TranslationException {
		OWLDescription owlSubClass = classAxiom.getSubClass();
		OWLDescription owlSuperClass = classAxiom.getSuperClass();
		IntegerDescription leftDescription = translate(owlSubClass);
		IntegerDescription rightDescription = translate(owlSuperClass);
		IntegerAxiom ret = new IntegerSubClassAxiom(leftDescription,
				rightDescription);
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
