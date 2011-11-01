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
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.util.ProgressMonitor;

import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.classifier.ProcessingException;
import de.tudresden.inf.lat.jcel.core.classifier.Processor;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraph;
import de.tudresden.inf.lat.jcel.core.graph.IntegerHierarchicalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraph;
import de.tudresden.inf.lat.jcel.owlapi.translator.AxiomSetTranslator;
import de.tudresden.inf.lat.jcel.owlapi.translator.TranslationRepository;

/**
 * Classifies an ontology using the CEL algorithm.
 * 
 * @author Julian Mendez
 * 
 */
public class JcelClassifierImpl implements JcelClassifier {

	protected static final Integer checkpoint00start = 0;
	protected static final Integer checkpoint01translationRepository = 3;
	protected static final Integer checkpoint02translationOfAxioms = 5;
	protected static final Integer checkpoint03configuration = 12;
	protected static final Integer checkpoint04garbageCollection = 15;
	protected static final Integer checkpoint05iterations = 75;
	protected static final Integer checkpoint06remotions = 85;
	protected static final Integer checkpoint07garbageCollection = 90;
	protected static final Integer checkpoint08translation = 100;
	protected static final Integer checkpoint09end = 100;
	private static final Logger logger = Logger
			.getLogger(JcelClassifierImpl.class.getName());
	private HierarchicalGraph<OWLEntity> owlClassAndIndividualGraph = null;
	private HierarchicalGraph<OWLObjectProperty> owlObjectPropertyGraph = null;
	private Integer progress = null;
	private ProgressMonitor progressMonitor = null;
	private Set<OWLObjectProperty> reflexivePropertySet = null;
	private Date start = new Date();
	private Set<OWLObjectProperty> transitivePropertySet = null;
	private OWLClass bottomClass = null;
	private OWLClass topClass = null;

	public JcelClassifierImpl(OWLClass nothing, OWLClass thing) {
		this.bottomClass = nothing;
		this.topClass = thing;
	}

	public JcelClassifierImpl(OWLClass nothing, OWLClass thing,
			ProgressMonitor monitor) {
		this.bottomClass = nothing;
		this.topClass = thing;
		this.progressMonitor = monitor;
	}

	protected Set<OWLClass> collectClasses(Set<OWLAxiom> axiomSet) {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		for (OWLAxiom axiom : axiomSet) {
			ret.addAll(axiom.getClassesInSignature());
		}
		ret.add(getOWLNothing());
		ret.add(getOWLThing());
		return ret;
	}

	protected Set<OWLNamedIndividual> collectIndividuals(Set<OWLAxiom> axiomSet) {
		Set<OWLNamedIndividual> ret = new HashSet<OWLNamedIndividual>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLNamedIndividual> entities = axiom
					.getIndividualsInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	protected Set<OWLObjectProperty> collectProperties(Set<OWLAxiom> axiomSet) {
		Set<OWLObjectProperty> ret = new HashSet<OWLObjectProperty>();
		for (OWLAxiom axiom : axiomSet) {
			Set<OWLObjectProperty> entities = axiom
					.getObjectPropertiesInSignature();
			ret.addAll(entities);
		}
		return ret;
	}

	protected TranslationRepository createTranslationRepository(
			Set<OWLAxiom> axiomSet) {
		TranslationRepository ret = null;
		if (axiomSet != null) {
			Set<OWLClass> conceptNameSet = collectClasses(axiomSet);
			Set<OWLObjectProperty> propertySet = collectProperties(axiomSet);
			Set<OWLNamedIndividual> individualSet = collectIndividuals(axiomSet);
			ret = new TranslationRepository();
			ret.load(getOWLNothing(), getOWLThing(), conceptNameSet,
					propertySet, individualSet);
		}
		return ret;
	}

	public void finishMonitor() {
		this.progress = checkpoint09end;
		if (this.progressMonitor != null) {
			this.progressMonitor.setProgress(this.progress);
			this.progressMonitor.setFinished();
		}
	}

	public long getMemoryUsage() {
		return Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
	}

	public OWLClass getOWLNothing() {
		return this.bottomClass;

	}

	public OWLClass getOWLThing() {
		return this.topClass;

	}

	@Override
	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
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

	protected void incrementMonitor(Integer limit)
			throws ModelConstructionException {
		this.progress++;
		if (this.progress > limit) {
			this.progress = limit;
		}
		updateMonitor();
	}

	protected void log(String str) {
		logger.fine("(" + ((new Date()).getTime() - this.start.getTime())
				+ " ms, " + this.progress + " %) " + str);
	}

	protected Set<OWLObjectProperty> pickReflexiveOWLObjectProperties(
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

	protected Set<OWLObjectProperty> pickTransitiveOWLObjectProperties(
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

	public void resetAndLoad(Set<OWLAxiom> owlAxiomSet)
			throws ModelConstructionException {
		try {

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
			AxiomSetTranslator translator = new AxiomSetTranslator(
					translationRep);
			Set<IntegerAxiom> axiomSet = translator.translate(owlAxiomSet);
			translator = null;
			setProgress(checkpoint02translationOfAxioms);

			log("configuring processor ...");
			Processor processor = new Processor();
			processor.configure(axiomSet, translationRep
					.getOWLObjectPropertyList().size(), translationRep
					.getOWLClassList().size()
					+ translationRep.getOWLNamedIndividualList().size());
			setProgress(checkpoint03configuration);

			log("calling garbage collector ...");
			System.gc();
			setProgress(checkpoint04garbageCollection);

			log("processing all queues ...");
			long iteration = 0;
			for (boolean processing = true; processing;) {
				log("\titeration " + iteration + ":");
				processing = processor.processAllQueues();
				iteration++;
				incrementMonitor(checkpoint05iterations);
				log("calling garbage collector ...");
				System.gc();
				log("garbage collector called.\n");
				incrementMonitor(checkpoint05iterations);
			}
			setProgress(checkpoint05iterations);

			log("removing auxiliary entities ...");
			processor.removeAuxEntities();
			setProgress(checkpoint06remotions);

			log("calling garbage collector ...");
			System.gc();
			setProgress(checkpoint07garbageCollection);

			this.owlClassAndIndividualGraph = translateToOWLClassOrIndividual(
					processor.getClassGraph(), translationRep);
			this.owlObjectPropertyGraph = translateToOWLObjectProperty(
					processor.getPropertyGraph(), translationRep);
			setProgress(checkpoint08translation);

			finishMonitor();
			log("classification finished.\n");

		} catch (IllegalStateException e) {
			throw new ModelConstructionException(e);
		} catch (ProcessingException e) {
			throw new ModelConstructionException(e);
		}
	}

	protected void setProgress(Integer value) throws ModelConstructionException {
		this.progress = value;
		updateMonitor();
	}

	@Override
	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	protected void startMonitor() {
		this.progress = 0;
		if (this.progressMonitor != null) {
			this.progressMonitor.setSize(checkpoint09end);
			this.progressMonitor.setProgress(this.progress);
			this.progressMonitor.setStarted();
		}
	}

	protected HierarchicalGraph<OWLEntity> translateToOWLClassOrIndividual(
			IntegerSubsumerGraph origGraph, TranslationRepository translator) {
		HierarchicalGraphImpl<OWLEntity> ret = new HierarchicalGraphImpl<OWLEntity>();
		IntegerHierarchicalGraph graph = new IntegerHierarchicalGraphImpl(
				origGraph);
		for (Integer orig : graph.getElements()) {
			OWLEntity element = translator.getOWLClassOrIndividual(orig);
			for (Integer elem : graph.getEquivalents(orig)) {
				ret.addEquivalent(element, translator
						.getOWLClassOrIndividual(elem));
			}
			for (Integer elem : graph.getChildren(orig)) {
				ret.addSubElement(element, translator
						.getOWLClassOrIndividual(elem));
			}
		}
		return ret;
	}

	protected HierarchicalGraph<OWLObjectProperty> translateToOWLObjectProperty(
			IntegerSubsumerGraph origGraph, TranslationRepository translator) {
		HierarchicalGraphImpl<OWLObjectProperty> ret = new HierarchicalGraphImpl<OWLObjectProperty>();
		IntegerHierarchicalGraph graph = new IntegerHierarchicalGraphImpl(
				origGraph);
		Integer propertyBottomElement = origGraph.getBottomElement();
		Integer propertyTopElement = origGraph.getTopElement();
		for (Integer orig : graph.getElements()) {
			if (!orig.equals(propertyBottomElement)
					&& !orig.equals(propertyTopElement)) {
				OWLObjectProperty origProp = translator
						.getOWLObjectProperty(orig);
				for (Integer elem : graph.getEquivalents(orig)) {
					if (!elem.equals(propertyBottomElement)
							&& !elem.equals(propertyTopElement)) {
						ret.addEquivalent(origProp, translator
								.getOWLObjectProperty(elem));
					}
				}
				for (Integer elem : graph.getChildren(orig)) {
					if (!elem.equals(propertyBottomElement)
							&& !elem.equals(propertyTopElement)) {
						ret.addSubElement(origProp, translator
								.getOWLObjectProperty(elem));
					}
				}
			}
		}
		return ret;
	}

	protected void updateMonitor() throws ModelConstructionException {
		if (this.progressMonitor != null) {
			if (this.progressMonitor.isCancelled()) {
				throw new ModelConstructionException(
						"Classification cancelled by user.");
			}
			this.progressMonitor.setProgress(this.progress);
		}
	}
}
