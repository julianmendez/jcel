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

package de.tudresden.inf.lat.jcel.owlapi.classifier;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;

import de.tudresden.inf.lat.jcel.core.algorithm.common.Processor;
import de.tudresden.inf.lat.jcel.core.algorithm.rulebased.RuleBasedProcessor;
import de.tudresden.inf.lat.jcel.core.axiom.complex.ComplexIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.owlapi.translator.AxiomSetTranslator;
import de.tudresden.inf.lat.jcel.owlapi.translator.TranslationRepository;

/**
 * Classifies an ontology.
 * 
 * @author Julian Mendez
 * 
 */
public class JcelClassifierImpl implements JcelClassifier {

	private static final Integer checkpoint00start = 0;
	private static final Integer checkpoint01translationRepository = 3;
	private static final Integer checkpoint02translationOfAxioms = 5;
	private static final Integer checkpoint03configuration = 20;
	private static final Integer checkpoint04iterations = 70;
	private static final Integer checkpoint05translation = 100;
	private static final Integer checkpoint06end = 100;
	private static final String classificationText = "Classifying ontology ...";
	private static final Integer gcFrequency = 0x400000;
	private static final Logger logger = Logger
			.getLogger(JcelClassifierImpl.class.getName());
	private OWLClass bottomClass = null;
	private OWLDataProperty bottomDataProperty;
	private OWLObjectProperty bottomObjectProperty = null;
	private OWLReasonerConfiguration configuration = null;
	private Boolean interruptRequested = false;
	private HierarchicalGraph<OWLEntity> owlClassAndIndividualGraph = null;
	private HierarchicalGraph<OWLObjectProperty> owlObjectPropertyGraph = null;
	private Processor processor = null;
	private Integer progress = null;
	private Set<OWLObjectProperty> reflexivePropertySet = null;
	private Date start = new Date();
	private OWLClass topClass = null;
	private OWLDataProperty topDataProperty;
	private OWLObjectProperty topObjectProperty = null;
	private Set<OWLObjectProperty> transitivePropertySet = null;

	public JcelClassifierImpl(OWLClass nothing, OWLClass thing,
			OWLObjectProperty bottomObjectProp,
			OWLObjectProperty topObjectProp, OWLDataProperty bottomDataProp,
			OWLDataProperty topDataProp) {
		if (nothing == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (thing == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomObjectProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topObjectProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomDataProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topDataProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.bottomClass = nothing;
		this.topClass = thing;
		this.bottomObjectProperty = bottomObjectProp;
		this.topObjectProperty = topObjectProp;
		this.bottomDataProperty = bottomDataProp;
		this.topDataProperty = topDataProp;
	}

	public JcelClassifierImpl(OWLClass nothing, OWLClass thing,
			OWLObjectProperty bottomObjectProp,
			OWLObjectProperty topObjectProp, OWLDataProperty bottomDataProp,
			OWLDataProperty topDataProp, OWLReasonerConfiguration config) {
		if (nothing == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (thing == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (bottomObjectProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (topObjectProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (config == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.bottomClass = nothing;
		this.topClass = thing;
		this.bottomObjectProperty = bottomObjectProp;
		this.topObjectProperty = topObjectProp;
		this.bottomDataProperty = bottomDataProp;
		this.topDataProperty = topDataProp;
		this.configuration = config;
	}

	private Set<OWLClass> collectClasses(Set<OWLAxiom> axiomSet) {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		for (OWLAxiom axiom : axiomSet) {
			ret.addAll(axiom.getClassesInSignature());
		}
		ret.add(getOWLNothing());
		ret.add(getOWLThing());
		return ret;
	}

	private Set<OWLDataProperty> collectDataProperties(Set<OWLAxiom> axiomSet) {
		Set<OWLDataProperty> ret = new HashSet<OWLDataProperty>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLDataProperty> entities = axiom
					.getDataPropertiesInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	private Set<OWLNamedIndividual> collectIndividuals(Set<OWLAxiom> axiomSet) {
		Set<OWLNamedIndividual> ret = new HashSet<OWLNamedIndividual>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLNamedIndividual> entities = axiom
					.getIndividualsInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	private Set<OWLLiteral> collectLiterals(Set<OWLAxiom> axiomSet) {
		Set<OWLLiteral> ret = new HashSet<OWLLiteral>();
		for (OWLAxiom axiom : axiomSet) {
			if ((axiom instanceof OWLDataPropertyAssertionAxiom)) {
				ret.add(((OWLDataPropertyAssertionAxiom) axiom).getObject());
			}
			if (axiom instanceof OWLNegativeDataPropertyAssertionAxiom) {
				ret.add(((OWLNegativeDataPropertyAssertionAxiom) axiom)
						.getObject());
			}
			Set<OWLClassExpression> classExpressions = axiom
					.getNestedClassExpressions();
			for (OWLClassExpression classExpr : classExpressions) {
				if (classExpr instanceof OWLDataHasValue) {
					ret.add(((OWLDataHasValue) classExpr).getValue());
				}
				if (classExpr instanceof OWLDataOneOf) {
					ret.addAll(((OWLDataOneOf) classExpr).getValues());
				}
			}

		}
		return ret;
	}

	private Set<OWLObjectProperty> collectObjectProperties(
			Set<OWLAxiom> axiomSet) {
		Set<OWLObjectProperty> ret = new HashSet<OWLObjectProperty>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLObjectProperty> entities = axiom
					.getObjectPropertiesInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	private Processor createProcessor(Set<ComplexIntegerAxiom> axiomSet) {
		return new RuleBasedProcessor(axiomSet);
	}

	private TranslationRepository createTranslationRepository(
			Set<OWLAxiom> axiomSet) {
		TranslationRepository ret = null;
		if (axiomSet != null) {
			Set<OWLClass> conceptNameSet = collectClasses(axiomSet);
			Set<OWLObjectProperty> objectPropertySet = collectObjectProperties(axiomSet);
			Set<OWLNamedIndividual> individualSet = collectIndividuals(axiomSet);
			Set<OWLDataProperty> dataPropertySet = collectDataProperties(axiomSet);
			Set<OWLLiteral> literalSet = collectLiterals(axiomSet);
			ret = new TranslationRepository();
			ret.load(getOWLNothing(), getOWLThing(),
					getOWLBottomObjectProperty(), getOWLTopObjectProperty(),
					getOWLBottomDataProperty(), getOWLTopDataProperty(),
					conceptNameSet, objectPropertySet, individualSet,
					dataPropertySet, literalSet);
		}
		return ret;
	}

	public void finishMonitor() {
		this.progress = checkpoint06end;
		if (this.configuration != null) {
			this.configuration
					.getProgressMonitor()
					.reasonerTaskProgressChanged(this.progress, checkpoint06end);
			this.configuration.getProgressMonitor().reasonerTaskStopped();
		}
	}

	public long getMemoryUsage() {
		return Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
	}

	public OWLDataProperty getOWLBottomDataProperty() {
		return this.bottomDataProperty;
	}

	public OWLObjectProperty getOWLBottomObjectProperty() {
		return this.bottomObjectProperty;
	}

	public OWLClass getOWLNothing() {
		return this.bottomClass;

	}

	public OWLClass getOWLThing() {
		return this.topClass;

	}

	public OWLDataProperty getOWLTopDataProperty() {
		return this.topDataProperty;
	}

	public OWLObjectProperty getOWLTopObjectProperty() {
		return this.topObjectProperty;
	}

	@Override
	public OWLReasonerConfiguration getReasonerConfiguration() {
		return this.configuration;
	}

	@Override
	public Set<OWLObjectProperty> getReflexiveProperties() {
		return this.reflexivePropertySet;
	}

	@Override
	public HierarchicalGraph<OWLObjectProperty> getRelationGraph() {
		return this.owlObjectPropertyGraph;
	}

	@Override
	public Set<OWLObjectProperty> getTransitiveProperties() {
		return this.transitivePropertySet;
	}

	@Override
	public HierarchicalGraph<OWLEntity> getTypeGraph() {
		return this.owlClassAndIndividualGraph;
	}

	private void incrementMonitor(Integer limit) {
		this.progress++;
		if (this.progress > limit) {
			this.progress = limit;
		}
		updateMonitor();
	}

	@Override
	public void interrupt() {
		this.interruptRequested = true;
	}

	private void log(String str) {
		logger.fine("(" + ((new Date()).getTime() - this.start.getTime())
				+ " ms, " + this.progress + " %) " + str);
	}

	private Set<OWLObjectProperty> pickReflexiveOWLObjectProperties(
			Set<OWLAxiom> owlAxiomSet) {
		Set<OWLObjectProperty> ret = new HashSet<OWLObjectProperty>();
		for (OWLAxiom axiom : owlAxiomSet) {
			if (axiom instanceof OWLReflexiveObjectPropertyAxiom) {
				OWLReflexiveObjectPropertyAxiom reflexivePropertyAxiom = (OWLReflexiveObjectPropertyAxiom) axiom;
				ret.add(reflexivePropertyAxiom.getProperty()
						.asOWLObjectProperty());
			} else if (axiom instanceof OWLSubPropertyChainOfAxiom) {
				OWLSubPropertyChainOfAxiom chainPropertyAxiom = (OWLSubPropertyChainOfAxiom) axiom;
				if (chainPropertyAxiom.getPropertyChain().isEmpty()) {
					ret.add(chainPropertyAxiom.getSuperProperty()
							.asOWLObjectProperty());
				}
			}
		}
		return ret;
	}

	private Set<OWLObjectProperty> pickTransitiveOWLObjectProperties(
			Set<OWLAxiom> owlAxiomSet) {
		Set<OWLObjectProperty> ret = new HashSet<OWLObjectProperty>();
		for (OWLAxiom axiom : owlAxiomSet) {
			if (axiom instanceof OWLTransitiveObjectPropertyAxiom) {
				OWLTransitiveObjectPropertyAxiom transitivePropertyAxiom = (OWLTransitiveObjectPropertyAxiom) axiom;
				ret.add(transitivePropertyAxiom.getProperty()
						.asOWLObjectProperty());
			} else if (axiom instanceof OWLSubPropertyChainOfAxiom) {
				OWLSubPropertyChainOfAxiom chainPropertyAxiom = (OWLSubPropertyChainOfAxiom) axiom;
				List<OWLObjectPropertyExpression> propertyList = chainPropertyAxiom
						.getPropertyChain();
				if (propertyList.size() == 2) {
					if (propertyList.get(0).equals(propertyList.get(1))
							&& propertyList.get(0).equals(
									chainPropertyAxiom.getSuperProperty())) {
						ret.add(propertyList.get(0).asOWLObjectProperty());
					}
				}
			}
		}
		return ret;
	}

	@Override
	public void resetAndLoad(Set<OWLAxiom> owlAxiomSet) {
		if (owlAxiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		log("starting classification ...\n");
		startMonitor();
		setProgress(checkpoint00start);

		this.reflexivePropertySet = pickReflexiveOWLObjectProperties(owlAxiomSet);
		this.transitivePropertySet = pickTransitiveOWLObjectProperties(owlAxiomSet);

		log("creating translation repository ...");
		TranslationRepository translationRep = null;
		translationRep = createTranslationRepository(owlAxiomSet);
		setProgress(checkpoint01translationRepository);

		log("translating axioms ...");
		AxiomSetTranslator translator = new AxiomSetTranslator(translationRep);
		Set<ComplexIntegerAxiom> axiomSet = translator.translate(owlAxiomSet);
		translator = null;
		setProgress(checkpoint02translationOfAxioms);

		log("configuring processor ...");
		this.processor = createProcessor(axiomSet);
		axiomSet.clear();
		setProgress(checkpoint03configuration);

		log("processing queue ...");
		long iteration = 0;
		for (; this.processor.process(); iteration++) {
			if (this.interruptRequested) {
				this.interruptRequested = false;
				throw new RuntimeException("Classification interrupted.");
			}
			if (iteration % gcFrequency == 0) {
				log("entry number " + iteration + " ...");
				incrementMonitor(checkpoint04iterations);
			}
		}
		log("entries processed : " + iteration);
		setProgress(checkpoint04iterations);

		log("starting translation ...");
		this.owlClassAndIndividualGraph = translateToOWLClassOrIndividual(
				this.processor.getClassHierarchy(),
				this.processor.getDirectTypes(),
				this.processor.getSameIndividualMap(), translationRep);
		this.owlObjectPropertyGraph = translateToOWLObjectProperty(
				this.processor.getObjectPropertyHierarchy(), translationRep);
		setProgress(checkpoint05translation);

		finishMonitor();
		log("classification finished.\n");

	}

	private void setProgress(Integer value) {
		this.progress = value;
		updateMonitor();
	}

	@Override
	public void setReasonerConfiguration(OWLReasonerConfiguration config) {
		if (config == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.configuration = config;
	}

	private void startMonitor() {
		this.progress = 0;
		if (this.configuration != null) {
			this.configuration.getProgressMonitor().reasonerTaskStarted(
					classificationText);
			this.configuration
					.getProgressMonitor()
					.reasonerTaskProgressChanged(this.progress, checkpoint06end);
		}
	}

	private HierarchicalGraph<OWLEntity> translateToOWLClassOrIndividual(
			IntegerHierarchicalGraph graph,
			Map<Integer, Set<Integer>> directTypes,
			Map<Integer, Set<Integer>> sameIndividual,
			TranslationRepository translator) {
		HierarchicalGraphImpl<OWLEntity> ret = new HierarchicalGraphImpl<OWLEntity>();

		for (Integer orig : graph.getElements()) {
			OWLClass element = translator.getOWLClass(orig);
			for (Integer elem : graph.getEquivalents(orig)) {
				ret.addEquivalent(element, translator.getOWLClass(elem));
			}
			for (Integer elem : graph.getChildren(orig)) {
				ret.addSubElement(element, translator.getOWLClass(elem));
			}
		}

		for (Integer orig : directTypes.keySet()) {
			OWLNamedIndividual element = translator.getOWLNamedIndividual(orig);
			for (Integer elem : sameIndividual.get(orig)) {
				ret.addEquivalent(element,
						translator.getOWLNamedIndividual(elem));
			}
			for (Integer elem : directTypes.get(orig)) {
				ret.addSubElement(translator.getOWLClass(elem), element);
			}
		}
		return ret;
	}

	private HierarchicalGraph<OWLObjectProperty> translateToOWLObjectProperty(
			IntegerHierarchicalGraph graph, TranslationRepository translator) {
		HierarchicalGraphImpl<OWLObjectProperty> ret = new HierarchicalGraphImpl<OWLObjectProperty>();
		Integer propertyBottomElement = graph.getBottomElement();
		Integer propertyTopElement = graph.getTopElement();
		for (Integer orig : graph.getElements()) {
			if (!orig.equals(propertyBottomElement)
					&& !orig.equals(propertyTopElement)) {
				OWLObjectProperty origProp = translator
						.getOWLObjectProperty(orig);
				for (Integer elem : graph.getEquivalents(orig)) {
					if (!elem.equals(propertyBottomElement)
							&& !elem.equals(propertyTopElement)) {
						ret.addEquivalent(origProp,
								translator.getOWLObjectProperty(elem));
					}
				}
				for (Integer elem : graph.getChildren(orig)) {
					if (!elem.equals(propertyBottomElement)
							&& !elem.equals(propertyTopElement)) {
						ret.addSubElement(origProp,
								translator.getOWLObjectProperty(elem));
					}
				}
			}
		}
		return ret;
	}

	private void updateMonitor() {
		if (this.configuration != null) {
			this.configuration
					.getProgressMonitor()
					.reasonerTaskProgressChanged(this.progress, checkpoint06end);
		}
	}

}
