/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.tudresden.inf.lat.jcel.core.completion.basic.CR1SRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR2SRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR3SRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR4RRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR4SRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR5RRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR6RRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR6RTrRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR7RRule;
import de.tudresden.inf.lat.jcel.core.completion.basic.CR7SRule;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR3SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR4RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR4SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR5RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR6RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR6SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR7RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR8RExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR8SExtRule;
import de.tudresden.inf.lat.jcel.core.completion.ext.CR9RExtRule;
import de.tudresden.inf.lat.jcel.coreontology.datatype.OntologyExpressivity;

/**
 * An object of this class creates appropriate completion rule chains to process
 * the ontology.
 * 
 * @author Julian Mendez
 * 
 */
public class CompletionRuleChainSelector {

	private RChain chainR = new RChain(new ArrayList<>());
	private SChain chainS = new SChain(new ArrayList<>());
	private final OntologyExpressivity expressivity;

	/**
	 * Constructs a new ontology preprocessor.
	 * 
	 * @param expressivity
	 *            expressivity
	 */
	public CompletionRuleChainSelector(OntologyExpressivity expressivity) {
		Objects.requireNonNull(expressivity);
		this.expressivity = expressivity;

		if (getOntologyExpressivity().hasInverseObjectProperty()
				|| getOntologyExpressivity().hasFunctionalObjectProperty()) {
			activateExtendedRules();
		} else {
			activateSimpleRules();
			if (getOntologyExpressivity().hasSubPropertyChainOf()) {
				activatePropertyChainRules();
			} else if (getOntologyExpressivity().hasTransitiveObjectProperty()) {
				activateTransitivePropertyRules();
			}
		}
		if (getOntologyExpressivity().hasBottom()) {
			activateBottomRules();
		}
	}

	private void activateBottomRules() {
		List<SObserverRule> listS = new ArrayList<>();
		listS.addAll(this.chainS.getList());
		listS.add(new CR7SRule());
		this.chainS = new SChain(listS);

		List<RObserverRule> listR = new ArrayList<>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR7RRule());
		this.chainR = new RChain(listR);
	}

	private void activateExtendedRules() {
		List<SObserverRule> listS = new ArrayList<>();
		listS.addAll(this.chainS.getList());
		listS.add(new CR1SRule());
		listS.add(new CR2SRule());
		listS.add(new CR3SExtRule());
		listS.add(new CR4SExtRule());
		listS.add(new CR6SExtRule());
		listS.add(new CR8SExtRule());
		this.chainS = new SChain(listS);

		List<RObserverRule> listR = new ArrayList<>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR4RExtRule());
		listR.add(new CR5RExtRule());
		listR.add(new CR6RExtRule());
		listR.add(new CR7RExtRule());
		listR.add(new CR8RExtRule());
		listR.add(new CR9RExtRule());
		this.chainR = new RChain(listR);
	}

	/**
	 * Activates a profiler for the completion rule chains.
	 */
	public void activateProfiler() {
		List<SObserverRule> listS = this.chainS.getList();
		List<SObserverRule> listSWithProfiler = new ArrayList<>();
		listS.forEach(current -> listSWithProfiler.add(new RuleProfiler(current)));
		this.chainS = new SChain(listSWithProfiler);

		List<RObserverRule> listR = this.chainR.getList();
		List<RObserverRule> listRWithProfiler = new ArrayList<>();
		listR.forEach(current -> listRWithProfiler.add(new RuleProfiler(current)));
		this.chainR = new RChain(listRWithProfiler);
	}

	private void activatePropertyChainRules() {
		List<RObserverRule> listR = new ArrayList<>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR6RRule());
		this.chainR = new RChain(listR);
	}

	private void activateSimpleRules() {
		List<SObserverRule> listS = new ArrayList<>();
		listS.addAll(this.chainS.getList());
		listS.add(new CR1SRule());
		listS.add(new CR2SRule());
		listS.add(new CR3SRule());
		listS.add(new CR4SRule());
		this.chainS = new SChain(listS);

		List<RObserverRule> listR = new ArrayList<>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR4RRule());
		listR.add(new CR5RRule());
		this.chainR = new RChain(listR);
	}

	private void activateTransitivePropertyRules() {
		List<RObserverRule> listR = new ArrayList<>();
		listR.addAll(this.chainR.getList());
		listR.add(new CR6RTrRule());
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
