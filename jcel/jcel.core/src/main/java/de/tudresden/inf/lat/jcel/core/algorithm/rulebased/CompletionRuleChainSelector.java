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
import java.util.List;

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
import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;

/**
 * An object of this class creates appropriate completion rule chains to process
 * the ontology.
 * 
 * @author Julian Mendez
 * 
 */
public class CompletionRuleChainSelector {

	private RChain chainR = new RChain(new ArrayList<RObserverRule>());
	private SChain chainS = new SChain(new ArrayList<SObserverRule>());
	private final OntologyExpressivity expressivity;

	/**
	 * Constructs a new ontology preprocessor.
	 * 
	 * @param expressivity
	 *            expressivity
	 */
	public CompletionRuleChainSelector(OntologyExpressivity expressivity) {
		if (expressivity == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.expressivity = expressivity;

		if (getOntologyExpressivity().hasInverseObjectProperty()
				|| getOntologyExpressivity().hasFunctionalObjectProperty()) {
			activateExtendedRules();
		} else {
			activateSimpleRules();
		}
		if (getOntologyExpressivity().hasBottom()) {
			activateBottomRules();
		}
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

	/**
	 * Returns the ontology expressivity.
	 * 
	 * @return the ontology expressivity
	 */
	public OntologyExpressivity getOntologyExpressivity() {
		return this.expressivity;
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

}
