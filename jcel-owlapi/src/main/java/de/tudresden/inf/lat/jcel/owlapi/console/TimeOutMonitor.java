/*
 *
 * Copyright (C) 2009-2015 Julian Mendez
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

package de.tudresden.inf.lat.jcel.owlapi.console;

import java.util.Objects;
import java.util.logging.Logger;

import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;

/**
 * An object of this class implements a monitor that interrupts the reasoner
 * after a time-out.
 * 
 * @author Julian Mendez
 */
public class TimeOutMonitor extends Thread {

	private static final Logger logger = Logger.getLogger(TimeOutMonitor.class.getName());

	private final JcelReasoner reasoner;
	private final long timeout;

	public TimeOutMonitor(JcelReasoner reasoner, long timeout) {
		Objects.requireNonNull(reasoner);
		this.reasoner = reasoner;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		try {
			if (this.timeout > 0) {
				Thread.sleep(this.timeout);
			}
			this.reasoner.getReasoner().interrupt();
			logger.fine("The reasoner has timed out.");
		} catch (InterruptedException e) {
			logger.fine("The time-out monitor has been interrupted.");
		}
	}

}