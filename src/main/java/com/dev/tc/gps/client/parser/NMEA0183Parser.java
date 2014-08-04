/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

/**
 * 
 * Customized GPS result parser for NMEA 0183 Protocol.
 * 
 * @author NThusitha
 * @date 11-July-2014
 */
public abstract class NMEA0183Parser {

	private static final Logger log = Logger.getLogger(NMEA0183Parser.class
			.getName());

/*	// base annotations for NMEA 0183 protocol.
	private static final String NMEA_ANNOTATION_REGX = "((\\$GPGGA)|(\\$GPRMC)|(\\$GPGSA)|(\\$GPGLL)|(\\$GPGSV)|(\\$GPVTG)).*";

	private static final String GPSD_ANNOTATION_REGX = "\\GPSD.*";*/



	private static final String METADATA_REX = "((N)|(E)|(M))";



	/**
	 * @param input
	 * @return - Queue containing data with units extracted from NMEA message.
	 */
	public Deque<String> parseSentence(String input) {

		Deque<String> processedData = new ArrayDeque<String>();

		// if (input.startsWith(GGA_ANNOTATION_REGX, 0)) {

		if (input.startsWith(getAnnotation(), 0)) {
			// remove the annotation
			String rawData = input.substring(7);

			String[] data = rawData.trim().split(",");

			for (String line : data) {
				if (line.matches(METADATA_REX)) {
					if (!processedData.isEmpty()) {
						// data without units
						String currenctData = processedData.pollLast();
						// add the unit and push data back again
						processedData.addLast(currenctData + line);
					}

				} else {
					processedData.addLast(line);
				}
			}

		}

		return processedData;

	}

	/**
	 * @return - Return the NMEA annotation
	 */
	protected abstract String getAnnotation();

}
