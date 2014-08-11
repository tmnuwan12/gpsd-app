/**
 * 
 */
package com.dev.tc.gps.client.session;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.GPSDataIntegrity;
import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.notification.DelayMaker;
import com.dev.tc.gps.client.notification.GPSBroadcaster;
import com.dev.tc.gps.client.types.GGA;
import com.dev.tc.gps.client.types.GSA;
import com.dev.tc.gps.client.types.GST;
import com.dev.tc.gps.client.types.GSV;
import com.dev.tc.gps.client.types.PACKET;
import com.dev.tc.gps.client.types.RMC;
import com.dev.tc.gps.client.util.DateTimeUtil;

/**
 * Virtual GPS session. Class will act as a container for GPS data for a given
 * time 'n'.
 * 
 * @author NThusitha
 * 
 */
public class GPSSession implements GPSObject {

	private static final Logger log = Logger.getLogger(GPSSession.class
			.getName());

	private GPSBroadcaster broadcaster = GPSBroadcaster.instance();

	private static List<GGA> GGA_BUFF = new ArrayList<GGA>();
	private static List<GSA> GSA_BUFF = new ArrayList<GSA>();
	private static List<GSV> GSV_BUFF = new ArrayList<GSV>();
	private static List<GST> GST_BUFF = new ArrayList<GST>();
	private static List<RMC> RMC_BUFF = new ArrayList<RMC>();

	private static volatile long LAST_BROADCAST_UTC_TIMESTAMP = 0l;

	/*
	 * private GGA gga; private GSA gsa; private GSV gsv; private GST gst;
	 * private RMC rmc;
	 */

	/**
	 * Invoked internally by GPS parsers.
	 * 
	 * @param input
	 */
	public synchronized void push(GPSObject input) {

		if (input != null) {

			if (validate1(input)) {
				log.log(Level.FINER, "adding data to internal buffer");
				
				if (input instanceof GGA) {
					// this.gga = (GGA) input;
					GGA_BUFF.add((GGA) input);
				} else if (input instanceof GST) {
					// this.gst = (GST) input;
					GST_BUFF.add((GST) input);
				} else if (input instanceof GSA) {
					// this.gsa = (GSA) input;
					GSA_BUFF.add((GSA) input);
				} else if (input instanceof GSV) {
					// this.gsv = (GSV) input;
					GSV_BUFF.add((GSV) input);
				} else if (input instanceof RMC) {
					// this.rmc = (RMC) input;
					RMC_BUFF.add((RMC) input);
				}

			}
			// publish();
			if (canBroadcast()) {
				broadcast();
			}else{
				log.log(Level.FINER, "can't broadcast yet");
			}
		}

	}

	private boolean validate1(GPSObject input){
		
		//for now always return true
		return true;
	}
	
	/**
	 * Check for data integrity.
	 * 
	 * @param input
	 * @return - true if integrity check passes
	 * @deprecated
	 */
	private boolean validate(GPSObject input) {

		boolean isValid = false;
		log.log(Level.FINER, "validating input {0}", input.toString());
		if (input instanceof GPSDataIntegrity) {
			isValid = ((GPSDataIntegrity) input).checkIntegrity();
		} else {
			log.log(Level.WARNING,
					"input {0} do not implement {1}",
					new Object[] { input.toString(),
							GPSDataIntegrity.class.getName() });
		}
		log.log(Level.FINER, "validation result {0} \n", isValid);
		return isValid;
	}

	/**
	 * @return - true if the application never broadcasted anything before or
	 *         required delay has been reached.
	 */
	private boolean canBroadcast() {
		boolean goAhead = false;
		if (LAST_BROADCAST_UTC_TIMESTAMP > 0) {
			// broadcast with delay.
			log.log(Level.FINER, "time diff {0} \n", DateTimeUtil.nowUTC() - LAST_BROADCAST_UTC_TIMESTAMP);
			if ((DateTimeUtil.nowUTC() - LAST_BROADCAST_UTC_TIMESTAMP) >= DelayMaker.BROADCAST_DELAY) {
				LAST_BROADCAST_UTC_TIMESTAMP = DateTimeUtil.nowUTC();
				goAhead = true;
			}

		} else {
			LAST_BROADCAST_UTC_TIMESTAMP = DateTimeUtil.nowUTC();
			goAhead = true;
		}

		return goAhead;

	}

	/**
	 * Broadcast data.
	 */
	private void broadcast() {
		
		PACKET packet = GPSDataProcessor.processData(GGA_BUFF, GSA_BUFF,
				GSV_BUFF, GST_BUFF, RMC_BUFF);
		
		log.log(Level.FINER, "broadcasting now");
		
		List<GPSObject> gpsData = new ArrayList<GPSObject>();
		gpsData.add(packet);
		
		broadcaster.broadcast(gpsData);
		log.log(Level.FINER, "broadcasting done, start emptying buffer");
		emptyBuffer();
		
	}
	
	
	/**
	 * @return
	 */
	private void emptyBuffer() {
		
		GGA_BUFF.clear();
		GSA_BUFF.clear();
		GSV_BUFF.clear();
		GST_BUFF.clear();
		RMC_BUFF.clear();
	}


	/**
	 * Check all the required data has been populated for this session. If true
	 * then publish the GPS session data.
	 * 
	 * @Deprecated. Use broadcast instead.
	 */
/*	private void publish() {
		// TODO: Add more null checks
		if (getGga() != null && getGsa() != null && getGsv() != null) {
			List<GPSObject> gpsData = new ArrayList<GPSObject>();

			if (checkIntegrity()) {
				gpsData.add(getGga());
				gpsData.add(getGsa());
				gpsData.add(getGsv());
				gpsData.add(getRmc());
				try {
					// add n sec delay when outputting data in console (only for
					// demos)
					Thread.sleep(DelayMaker.BROADCAST_DELAY);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.log(Level.FINER, "broadcasting now");
				broadcaster.broadcast(gpsData);
			} else {
				log.log(Level.WARNING,
						"abort broadcasting due to integrity check failure");
			}
		}

	}*/

	/**
	 * @return - true if all integrity checks are passed.
	 */
/*	private boolean checkIntegrity() {
		return getGga().checkIntegrity() && getGsa().checkIntegrity()
				&& getGsv().checkIntegrity();

	}*/

/*	public GGA getGga() {
		return gga;
	}

	public GST getGst() {
		return gst;
	}

	public GSA getGsa() {
		return gsa;
	}

	public void setGsa(GSA gsa) {
		this.gsa = gsa;
	}

	public GSV getGsv() {
		return gsv;
	}

	public RMC getRmc() {
		return rmc;
	}
*/
}
