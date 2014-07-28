/**
 * 
 */
package com.dev.tc.gps.windows.native_.client;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Add a log filter.
 * @author NThusitha
 * @date 28-July-2014
 * 
 */
public class LogFilter implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
	 */
	@Override
	public boolean isLoggable(LogRecord record) {

		if (record.getLevel().equals(Level.INFO)) {

			return true;
		} else {
			return false;
		}
	}

}
