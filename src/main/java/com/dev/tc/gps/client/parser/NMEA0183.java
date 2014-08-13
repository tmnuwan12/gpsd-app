/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.Map;
import com.dev.tc.gps.client.contract.Protocol;
import com.dev.tc.gps.client.session.GPSSession;
import com.dev.tc.gps.client.util.DateTimeUtil;

/**
 * NMEA Sentence
 * 
 * @author NThusitha
 * 
 */
public class NMEA0183 implements Protocol {

	private static final Logger log = Logger
			.getLogger(NMEA0183.class.getName());

	private static List<Map> MAPS = new ArrayList<Map>();
	private static GPSSession GLOBAL_SESSION;
	private static long TIMESTAMP = 0l;
	private static long MAX_ACTIVE_TIME_MS = 1000 * 60 * 5; // 5min

	public NMEA0183() {

		MAPS.add(new GGAMap());
		MAPS.add(new GSAMap());
		MAPS.add(new GPSDCMDParser());
		MAPS.add(new GSVMap());
		MAPS.add(new RMCMap());
		// TODO: Add any required parsers
		
	}

	/**
	 * Read the NMEA line and notify Listeners.
	 * 
	 * @param line
	 */
	@Override
	public void read(String line) {

		GPSSession gpsSession = getSession();

		for (Map map_ : MAPS) {
			//log.log(Level.FINEST, "nmea line {0}", line);
			gpsSession.push(map_.map(map_.parseSentence(line)));
		}

	}

	/**
	 * Decide when to create a new GPSSession. When the existing GPSSession get
	 * expired create a new one.
	 * 
	 * @return - GPSSession
	 */
	public GPSSession getSession() {
		long now = DateTimeUtil.nowUTC();
		if (GLOBAL_SESSION == null) {
			GLOBAL_SESSION = new GPSSession();
		} /*else if (now - TIMESTAMP >= MAX_ACTIVE_TIME_MS) {

			log.log(Level.FINE, "created a new gps session, discarded old one");
			GLOBAL_SESSION = new GPSSession();
		}*/
		TIMESTAMP = now;
		return GLOBAL_SESSION;
	}

}
