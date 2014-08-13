/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.contract.Map;
import com.dev.tc.gps.client.types.GGA;

/**
 * GGA Parser.
 * 
 * @author NThusitha
 * @date 14-July-2014
 * 
 */
public class GGAMap extends NMEA0183Parser implements Map {

	// private static final String GGA_ANNOTATION_REGX = "\\$GPGGA.*";
	private static final String GGA_ANNOTATION_REGX = "$GPGGA";

	private static final Logger log = Logger.getLogger(GGAMap.class
			.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.NMEA0183Parser#getAnnotation()
	 */
	@Override
	protected String getAnnotation() {
		return GGA_ANNOTATION_REGX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.NMEA0183Parser#map(java.util.Deque)
	 */
	@Override
	public GPSObject map(Deque<String> rawData) {
		GGA gga = null;
		short criticalCount = 7, index = 1;
		if (rawData != null && !rawData.isEmpty()) {
			gga = new GGA();

			// check all required major 7 data elements are set.
			if (rawData.size() >= criticalCount) {

				while (!rawData.isEmpty()) {

					/*
					 * GPS dongle will send empty data when it's warming up. In
					 * order to counter any processing errors ignore then when
					 * first encountered.
					 * 
					 */
					if (rawData.peek().isEmpty()) {
						rawData.poll();
						index++;
						continue;
					}

					switch (index) {
					case 1:
						gga.setTimestamp(Double.parseDouble(rawData.poll()));
						break;
					case 2:
						String lat = rawData.poll();
						gga.setLat(Double.parseDouble(lat.substring(0,
								lat.length() - 2)));
						gga.setLatDirection(lat.charAt(lat.length() - 1));
						break;
					case 3:
						String lon = rawData.poll();
						gga.setLon(Double.parseDouble(lon.substring(0,
								lon.length() - 2)));
						gga.setLatDirection(lon.charAt(lon.length() - 1));
						break;
					case 4:
						gga.setFixQuality(Byte.parseByte(rawData.poll()));
						break;
					case 5:
						gga.setNumerOfSatellites(Integer.parseInt(rawData
								.poll()));
						break;
					case 7:
						gga.setHorizontalDiluationPos(Float.parseFloat(rawData
								.poll()));
						break;
					default:
						rawData.clear();
						break;
					}
					index++;
				}

			} else {
				log.log(Level.SEVERE,
						"critcal data count is not met, GGA data count: {0}",
						rawData.size());
				throw new IllegalArgumentException(
						"critcal data count is not met, minimum required 7, but received "
								+ rawData.size());
			}
			
			return gga;

		}
		return gga;
	}
}
