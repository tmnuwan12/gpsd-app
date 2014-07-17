/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.contract.Map;
import com.dev.tc.gps.client.types.CMDResponse;

/**
 * @author NThusitha
 * 
 */
public class GPSDCMDParser extends NMEA0183Parser implements Map {

	private static final Logger log = Logger.getLogger(GPSDCMDParser.class
			.getName());
	// base annotations for NMEA 0183 protocol.
	private static final String NMEA_ANNOTATION_REGX = "((\\$GPGGA)|(\\$GPRMC)|(\\$GPGSA)|(\\$GPGLL)|(\\$GPGSV)|(\\$GPVTG)).*";

	private static final String GPSD_ANNOTATION_REGX = "GPSD";

	@Override
	public Deque<String> parseSentence(String input) {
		//log.log(Level.FINEST, "RECEIVED CMD RESPONSE MESSAGE FROM GPSD >>> {0}", line);
		// TODO: Implement parsing logic

		Deque<String> processedData = new ArrayDeque<String>();
		if (input != null)

			if (input.startsWith(NMEA_ANNOTATION_REGX, 0)) {

				// GGA

			} else if (input.startsWith(getAnnotation(), 0)) {
				log.log(Level.FINEST, "RECEIVED CMD RESPONSE MESSAGE FROM GPSD >>> {0}", input);
				
				
				if (input.startsWith(getAnnotation(), 0)) {
					// remove the annotation
					String rawData = input.substring(5);

					String[] data = rawData.trim().split(",");

					for (String line : data) {
							processedData.addLast(line);
						}
					}

				}

		return processedData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.Map#map(java.util.Deque)
	 */
	@Override
	public GPSObject map(Deque<String> rawData) {
		CMDResponse cmdResponse = new CMDResponse();
		if(!rawData.isEmpty()){
			List<String> lines = new ArrayList<String>();
			
			while(rawData.isEmpty()){
				lines.add(rawData.poll());
			}
			
			cmdResponse.setLines(lines);
		}
		
		return cmdResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.NMEA0183Parser#getAnnotation()
	 */
	@Override
	protected String getAnnotation() {
		return GPSD_ANNOTATION_REGX;
	}

}
