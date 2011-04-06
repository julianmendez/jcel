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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.completion.basic.CR1Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR2Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR3Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR4RRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR4SRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR5Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR6Rule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CRBottomRule;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR3ExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR4RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR4SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR5ExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR6RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR6SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR7ExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR8RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR8SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR9ExtOptRule;
import de.tudresden.inf.lat.jcel.core.saturation.SubPropertyNormalizer;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.ExpressivityDetector;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.OntologyExpressivity;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.ExtendedOntology;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.ExtendedOntologyImpl;
import de.tudresden.inf.lat.jcel.ontology.datatype.IdGenerator;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.ontology.normalization.OntologyNormalizer;

/**
 * An object of this class preprocesses an ontology. This preprocessing includes
 * <ul>
 * <li>finding the class identifiers and object property identifiers</li>
 * <li>creating an identifier generator for the auxiliary classes and object
 * properties</li>
 * <li>detecting the required expressivity for the ontology</li>
 * <li>creating appropriate completion rule chains to process the ontology</li>
 * <li>normalizing the ontology</li>
 * <li>extending the ontology</li>
 * </ul>
 * 
 * @author Julian Mendez
 * 
 */
public class OntologyPreprocessor {

	// private static final Logger logger = Logger
	// .getLogger(OntologyPreprocessor.class.getName());

	private static final Integer classBottomElement = IntegerDatatype.classBottomElement;
	private static final Integer classTopElement = IntegerDatatype.classTopElement;
	private static final Integer propertyBottomElement = IntegerDatatype.propertyBottomElement;
	private static final Integer propertyTopElement = IntegerDatatype.propertyTopElement;

	private RChain chainR = new RChain(new ArrayList<RObserverRule>());
	private SChain chainS = new SChain(new ArrayList<SObserverRule>());
	private OntologyExpressivity expressivityDetector = null;
	private ExtendedOntologyImpl extendedOntology = null;
	private IdGenerator idGenerator = null;
	private Set<Integer> originalClassSet = null;
	private Set<Integer> originalObjectPropertySet = null;

	/**
	 * Constructs a new ontology preprocessor.
	 * 
	 * @param axiomSet
	 *            set of axioms
	 */
	public OntologyPreprocessor(Set<ComplexIntegerAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		preProcess(axiomSet);
	}

	private void activateBottomRules() {
		List<RObserverRule> listR = new ArrayList<RObserverRule>();
		listR.addAll(this.chainR.getList());
		listR.add(new CRBottomRule());
		this.chainR = new RChain(listR);
	}

	private void activateExtendedRules() {
		List<SObserverRule> listS = new ArrayList<SObserverRule>();
		listS.addAll(this.chainS.getList());
		listS.add(new CR1Rule());
		listS.add(new CR2Rule());
		listS.add(new CR3ExtRule());
		listS.add(new CR4SExtRule());
		listS.add(new CR6SExtRule());
		listS.add(new CR8SExtRule());
		this.chainS = new SChain(listS);

		List<RObserverRule> listR = new ArrayList<RObserverRule>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR4RExtRule());
		listR.add(new CR5ExtRule());
		listR.add(new CR6RExtRule());
		listR.add(new CR7ExtRule());
		listR.add(new CR8RExtRule());
		listR.add(new CR9ExtOptRule());
		this.chainR = new RChain(listR);
	}

	/**
	 * Activates a profiler for the completion rule chains.
	 */
	public void activateProfiler() {
		List<SObserverRule> listS = this.chainS.getList();
		List<SObserverRule> listSWithProfiler = new ArrayList<SObserverRule>();
		for (SObserverRule current : listS) {
			listSWithProfiler.add(new RuleProfiler(current));
		}
		this.chainS = new SChain(listSWithProfiler);

		List<RObserverRule> listR = this.chainR.getList();
		List<RObserverRule> listRWithProfiler = new ArrayList<RObserverRule>();
		for (RObserverRule current : listR) {
			listRWithProfiler.add(new RuleProfiler(current));
		}
		this.chainR = new RChain(listRWithProfiler);
	}

	private void activateSimpleRules() {
		List<SObserverRule> listS = new ArrayList<SObserverRule>();
		listS.addAll(this.chainS.getList());
		listS.add(new CR1Rule());
		listS.add(new CR2Rule());
		listS.add(new CR3Rule());
		listS.add(new CR4SRule());
		this.chainS = new SChain(listS);

		List<RObserverRule> listR = new ArrayList<RObserverRule>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR4RRule());
		listR.add(new CR5Rule());
		listR.add(new CR6Rule());
		this.chainR = new RChain(listR);
	}

	private void findEntities(Set<ComplexIntegerAxiom> axiomSet) {
		this.originalClassSet = new HashSet<Integer>();
		this.originalObjectPropertySet = new HashSet<Integer>();
		for (ComplexIntegerAxiom axiom : axiomSet) {
			this.originalClassSet.addAll(axiom.getClassesInSignature());
			this.originalObjectPropertySet.addAll(axiom
					.getObjectPropertiesInSignature());
		}
		this.originalClassSet.add(classBottomElement);
		this.originalClassSet.add(classTopElement);
		this.originalObjectPropertySet.add(propertyBottomElement);
		this.originalObjectPropertySet.add(propertyTopElement);
	}

	/**
	 * Returns the ontology expressivity.
	 * 
	 * @return the ontology expressivity
	 */
	public OntologyExpressivity getExpressivity() {
		return this.expressivityDetector;
	}

	/**
	 * Returns the extended ontology.
	 * 
	 * @return the extended ontology
	 */
	public ExtendedOntology getExtendedOntology() {
		return this.extendedOntology;
	}

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	public IdGenerator getIdGenerator() {
		return this.idGenerator;
	}

	private Integer getMaximum(Set<Integer> set) {
		if (set.isEmpty()) {
			throw new IllegalArgumentException(
					"Computing maximum of empty set.");
		}

		Integer ret = set.iterator().next();
		for (Integer elem : set) {
			if (elem > ret) {
				ret = elem;
			}
		}
		return ret;
	}

	/**
	 * Returns the set of classes that were before the normalization.
	 * 
	 * @return the set of classes that were before the normalization
	 */
	public Set<Integer> getOriginalClassSet() {
		return this.originalClassSet;
	}

	/**
	 * Returns the set of object properties that were before the normalization.
	 * 
	 * @return the set of object properties that were before the normalization
	 */
	public Set<Integer> getOriginalObjectPropertySet() {
		return this.originalObjectPropertySet;
	}

	/**
	 * Returns the completion rule chain for the set of relations.
	 * 
	 * @return the completion rule chain for the set of relations.
	 */
	public RChain getRChain() {
		return this.chainR;
	}

	/**
	 * Returns the completion rule chain for the set of subsumers.
	 * 
	 * @return the completion rule chain for the set of subsumers
	 */
	public SChain getSChain() {
		return this.chainS;
	}

	private void preProcess(Set<ComplexIntegerAxiom> axiomSet) {
		findEntities(axiomSet);

		this.idGenerator = new IdGenerator(
				getMaximum(this.originalClassSet) + 1,
				getMaximum(this.originalObjectPropertySet) + 1);

		this.expressivityDetector = new ExpressivityDetector(axiomSet);

		if (this.expressivityDetector.hasInverseObjectProperty()
				|| this.expressivityDetector.hasFunctionalObjectProperty()) {
			activateExtendedRules();
		} else {
			activateSimpleRules();
		}
		if (this.expressivityDetector.hasBottom()) {
			activateBottomRules();
		}

		OntologyNormalizer axiomNormalizer = new OntologyNormalizer();
		SubPropertyNormalizer subPropNormalizer = new SubPropertyNormalizer(
				getIdGenerator());
		this.extendedOntology = new ExtendedOntologyImpl();
		this.extendedOntology.load(subPropNormalizer.apply(axiomNormalizer
				.normalize(axiomSet, getIdGenerator())));

		for (Integer elem : this.originalObjectPropertySet) {
			this.extendedOntology.addObjectProperty(elem);
		}
		for (Integer elem : this.originalClassSet) {
			this.extendedOntology.addClass(elem);
		}
	}

}
