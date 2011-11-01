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

package de.tudresden.inf.lat.jcel.owlapi.console;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * This class is a <code>Handler</code> that logs some benchmarking data, such
 * as used memory and running time.
 * 
 * @author Julian Mendez
 */
public class OutputStreamHandler extends Handler {

	private BufferedWriter output = null;
	private Date start = null;

	public OutputStreamHandler(OutputStream output) throws IOException {
		this.output = new BufferedWriter(new OutputStreamWriter(output));
		this.start = new Date();
	}

	@Override
	public void close() throws SecurityException {
		try {
			this.output.close();
		} catch (IOException e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * Creates a new message using the original one, and adding memory usage and
	 * running time.
	 * 
	 * @param originalMessage
	 *            original message
	 * @return the new message
	 */
	protected String createMessage(String originalMessage) {
		long difference = (new Date()).getTime() - this.start.getTime();
		long totalMemory = Runtime.getRuntime().totalMemory() / 1048576;
		long freeMemory = Runtime.getRuntime().freeMemory() / 1048576;
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(");
		sbuf.append("" + difference);
		sbuf.append(" ms) ([X]:");
		sbuf.append("" + (totalMemory - freeMemory));
		sbuf.append(" MB, [ ]:");
		sbuf.append("" + freeMemory);
		sbuf.append(" MB) ");
		sbuf.append(originalMessage);
		return sbuf.toString();
	}

	@Override
	public void flush() {
		try {
			this.output.flush();
		} catch (IOException e) {
		}
	}

	@Override
	public void publish(LogRecord record) {
		try {
			this.output.write(createMessage(record.getMessage()));
			this.output.newLine();
			this.output.flush();
		} catch (IOException e) {
		}
	}
}
