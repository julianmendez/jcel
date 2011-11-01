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
import java.util.Collections;
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
import org.semanticweb.owlapi.model.OWLObjectOneOf;
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

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerClassAssertionAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerClassDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDifferentIndividualsAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerEquivalentObjectPropertiesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerNamedIndividualDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerNegativeObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerObjectPropertyAssertionAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerObjectPropertyDeclarationAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerPropertyRangeAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSameIndividualAxiom;
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
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectOneOf;
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
class AxiomTranslator implements OWLAxiomVisitorEx<Set<IntegerAxiom>> {

	// private static final Logger logger =
	// Logger.getLogger(AxiomTranslator.class
	// .getName());

	private Map<OWLClass, Integer> classMap = null;
	private Map<OWLNamedIndividual, Integer> individualMap = null;
	private Map<OWLObjectProperty, Integer> propertyMap = null;

	public AxiomTranslator(Map<OWLObjectProperty, Integer> propMap,
			Map<OWLClass, Integer> clMap,
			Map<OWLNamedIndividual, Integer> indivMap) {
		if (propMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (clMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (indivMap == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.propertyMap = propMap;
		this.classMap = clMap;
		this.individualMap = indivMap;
	}

	private Integer getId(OWLClass owlClass) throws TranslationException {
		Integer ret = this.classMap.get(owlClass);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(owlClass
					.toString());
		}
		return ret;
	}

	private Integer getId(OWLIndividual individual) throws TranslationException {
		Integer ret = this.individualMap.get(individual);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(individual
					.toString());
		}
		return ret;
	}

	private Integer getId(OWLObjectProperty property)
			throws TranslationException {
		Integer ret = this.propertyMap.get(property);
		if (ret == null) {
			throw TranslationException.newIncompleteMapException(property
					.toString());
		}
		return ret;
	}

	private IntegerClassExpression translate(OWLClassExpression owlDescription)
			throws TranslationException {
		IntegerClassExpression ret = null;
		if (owlDescription instanceof OWLClass) {
			ret = translateClass((OWLClass) owlDescription);
		} else if (owlDescription instanceof OWLObjectSomeValuesFrom) {
			ret = translateObjectSomeValuesFrom((OWLObjectSomeValuesFrom) owlDescription);
		} else if (owlDescription instanceof OWLObjectIntersectionOf) {
			ret = translateObjectIntersectionOf((OWLObjectIntersectionOf) owlDescription);
		} else if (owlDescription instanceof OWLObjectOneOf) {
			ret = translateObjectOneOf((OWLObjectOneOf) owlDescription);
		} else {
			throw new TranslationException(
					"This description is not supported: '" + owlDescription
							+ "'.");
		}
		return ret;
	}

	private IntegerClassExpression translateClass(OWLClass owlClass)
			throws TranslationException {
		return new IntegerClass(getId(owlClass));
	}

	private Integer translateIndividual(OWLIndividual owlIndividual)
			throws TranslationException {
		return getId(owlIndividual);
	}

	private IntegerClassExpression translateObjectIntersectionOf(
			OWLObjectIntersectionOf intersection) throws TranslationException {
		Set<OWLClassExpression> operands = intersection.getOperands();
		Set<IntegerClassExpression> descriptionList = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression elem : operands) {
			descriptionList.add(translate(elem));
		}
		return new IntegerObjectIntersectionOf(descriptionList);
	}

	private IntegerClassExpression translateObjectOneOf(
			OWLObjectOneOf owlDescription) {
		Set<OWLIndividual> indivSet = owlDescription.getIndividuals();
		if (indivSet.isEmpty()) {
			throw new TranslationException(
					"It is not possible to translate ObjectOneOf with empty set.");
		}
		if (indivSet.size() > 1) {
			throw new TranslationException(
					"It is not possible to translate ObjectOneOf with multiple individuals: '"
							+ indivSet + "'.");
		}
		return new IntegerObjectOneOf(translateIndividual(indivSet.iterator()
				.next()));
	}

	private Integer translateObjectProperty(OWLObjectProperty objectProperty)
			throws TranslationException {
		return getId(objectProperty);
	}

	private IntegerClassExpression translateObjectSomeValuesFrom(
			OWLObjectSomeValuesFrom restriction) throws TranslationException {
		OWLObjectPropertyExpression propExpr = restriction.getProperty();
		if (!(propExpr instanceof OWLObjectProperty)) {
			throw new TranslationException(
					"Property expression cannot be translated.");
		}

		Integer propertyId = getId(propExpr.asOWLObjectProperty());
		OWLClassExpression desc = restriction.getFiller();
		IntegerClassExpression description = translate(desc);
		return new IntegerObjectSomeValuesFrom(propertyId, description);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLAnnotationAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(OWLAnnotationPropertyDomainAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(OWLAnnotationPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLClassAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer individualId = getId(axiom.getIndividual());
		IntegerAxiom ret = new IntegerClassAssertionAxiom(translate(axiom
				.getClassExpression()), individualId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDataPropertyDomainAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDataPropertyRangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDatatypeDefinitionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDeclarationAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLEntity entity = axiom.getEntity();
		IntegerAxiom ret = null;
		if (entity.isOWLClass()) {
			ret = new IntegerClassDeclarationAxiom(getId(entity.asOWLClass()));
		} else if (entity.isOWLObjectProperty()) {
			ret = new IntegerObjectPropertyDeclarationAxiom(getId(entity
					.asOWLObjectProperty()));
		} else if (entity.isOWLNamedIndividual()) {
			ret = new IntegerNamedIndividualDeclarationAxiom(getId(entity
					.asOWLNamedIndividual()));
		} else {
			throw TranslationException.newUnsupportedAxiomException(axiom);
		}
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDifferentIndividualsAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<Integer> individualIdSet = new HashSet<Integer>();
		for (OWLIndividual individual : individualSet) {
			individualIdSet.add(translateIndividual(individual));
		}
		IntegerAxiom ret = new IntegerDifferentIndividualsAxiom(individualIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDisjointClassesAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClassExpression> descriptionSet = axiom.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression description : descriptionSet) {
			classIdSet.add(translate(description));
		}
		IntegerAxiom ret = new IntegerDisjointClassesAxiom(classIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDisjointDataPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDisjointObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLDisjointUnionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLEquivalentClassesAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLClassExpression> descriptionSet = axiom.getClassExpressions();
		Set<IntegerClassExpression> classIdSet = new HashSet<IntegerClassExpression>();
		for (OWLClassExpression description : descriptionSet) {
			classIdSet.add(translate(description));
		}
		IntegerAxiom ret = new IntegerEquivalentClassesAxiom(classIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLEquivalentDataPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLEquivalentObjectPropertiesAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLObjectPropertyExpression> propertySet = axiom.getProperties();
		Set<Integer> propertyIdSet = new HashSet<Integer>();
		for (OWLObjectPropertyExpression propertyExpr : propertySet) {
			if (propertyExpr instanceof OWLObjectProperty) {
				propertyIdSet.add(getId(propertyExpr.asOWLObjectProperty()));
			} else {
				throw new IllegalStateException();
			}

		}
		IntegerAxiom ret = new IntegerEquivalentObjectPropertiesAxiom(
				propertyIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLFunctionalDataPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLHasKeyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLInverseObjectPropertiesAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectPropertyExpression propExpr = axiom.getProperty();
		if (!(propExpr instanceof OWLObjectProperty)) {
			throw new TranslationException(
					"Property expression cannot be translated: '" + propExpr
							+ "'.");
		}
		Integer propertyId = translateObjectProperty(propExpr
				.asOWLObjectProperty());
		Integer subjectId = translateIndividual(axiom.getSubject());
		Integer objectId = translateIndividual(axiom.getObject());
		IntegerAxiom ret = new IntegerNegativeObjectPropertyAssertionAxiom(
				propertyId, subjectId, objectId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLObjectPropertyAssertionAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectPropertyExpression propExpr = axiom.getProperty();
		if (!(propExpr instanceof OWLObjectProperty)) {
			throw new TranslationException(
					"Property expression cannot be translated: '" + propExpr
							+ "'.");
		}
		Integer propertyId = translateObjectProperty(propExpr
				.asOWLObjectProperty());
		Integer subjectId = translateIndividual(axiom.getSubject());
		Integer objectId = translateIndividual(axiom.getObject());
		IntegerAxiom ret = new IntegerObjectPropertyAssertionAxiom(propertyId,
				subjectId, objectId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLObjectPropertyDomainAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

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
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLObjectPropertyRangeAxiom rangeAxiom)
			throws TranslationException {
		if (rangeAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectPropertyExpression propertyExpr = rangeAxiom.getProperty();
		OWLClassExpression description = rangeAxiom.getRange();
		if (!(propertyExpr instanceof OWLObjectProperty)) {
			throw TranslationException.newUnsupportedAxiomException(rangeAxiom);
		}
		IntegerAxiom ret = new IntegerPropertyRangeAxiom(getId(propertyExpr
				.asOWLObjectProperty()), translate(description));
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLReflexiveObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		Integer propId = getId(property);
		IntegerAxiom ret = new RI1Axiom(propId);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSameIndividualAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLIndividual> individualSet = axiom.getIndividuals();
		Set<Integer> individualIdSet = new HashSet<Integer>();
		for (OWLIndividual individual : individualSet) {
			individualIdSet.add(translateIndividual(individual));
		}
		IntegerAxiom ret = new IntegerSameIndividualAxiom(individualIdSet);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.emptySet();
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSubClassOfAxiom classAxiom)
			throws TranslationException {
		if (classAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLClassExpression owlSubClass = classAxiom.getSubClass();
		OWLClassExpression owlSuperClass = classAxiom.getSuperClass();
		IntegerClassExpression leftDescription = translate(owlSubClass);
		IntegerClassExpression rightDescription = translate(owlSuperClass);
		IntegerAxiom ret = new IntegerSubClassOfAxiom(leftDescription,
				rightDescription);
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSubDataPropertyOfAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSubObjectPropertyOfAxiom propAxiom)
			throws TranslationException {
		if (propAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectPropertyExpression leftPropExpr = propAxiom.getSubProperty();
		OWLObjectPropertyExpression rightPropExpr = propAxiom
				.getSuperProperty();
		if (!(leftPropExpr instanceof OWLObjectProperty && rightPropExpr instanceof OWLObjectProperty)) {
			throw TranslationException.newUnsupportedAxiomException(propAxiom);
		}
		IntegerAxiom ret = new RI2Axiom(getId(leftPropExpr
				.asOWLObjectProperty()), getId(rightPropExpr
				.asOWLObjectProperty()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSubPropertyChainOfAxiom propAxiom)
			throws TranslationException {
		if (propAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<OWLObjectPropertyExpression> propChain = propAxiom
				.getPropertyChain();
		OWLObjectPropertyExpression rightPart = propAxiom.getSuperProperty();
		if (!(rightPart instanceof OWLObjectProperty)) {
			throw TranslationException.newUnsupportedAxiomException(propAxiom);
		}
		List<Integer> chain = new ArrayList<Integer>();
		for (OWLObjectPropertyExpression property : propChain) {
			if (property instanceof OWLObjectProperty) {
				chain.add(getId(property.asOWLObjectProperty()));
			} else {
				throw new TranslationException(
						"Unexpected property expression " + property.toString());
			}
		}
		IntegerAxiom ret = new IntegerSubPropertyChainOfAxiom(chain,
				getId(rightPart.asOWLObjectProperty()));
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLSymmetricObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}

	@Override
	public Set<IntegerAxiom> visit(OWLTransitiveObjectPropertyAxiom axiom)
			throws TranslationException {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();
		IntegerAxiom ret = new IntegerTransitiveObjectPropertyAxiom(
				getId(property));
		return Collections.singleton(ret);
	}

	@Override
	public Set<IntegerAxiom> visit(SWRLRule axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw TranslationException.newUnsupportedAxiomException(axiom);
	}
}
