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
import com.dev.tc.gps.client.notification.GPSBroadcaster;
import com.dev.tc.gps.client.types.GGA;
import com.dev.tc.gps.client.types.GSA;
import com.dev.tc.gps.client.types.GST;
import com.dev.tc.gps.client.types.GSV;
import com.dev.tc.gps.client.types.RMC;

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
	private GGA gga;
	private GSA gsa;
	private GSV gsv;
	private GST gst;
	private RMC rmc;

	/**
	 * Invoked internally by GPS parsers.
	 * 
	 * @param input
	 */
	public void push(GPSObject input) {

		if (input != null) {

			if (validate(input)) {

				if (input instanceof GGA) {
					this.gga = (GGA) input;
				} else if (input instanceof GST) {
					this.gst = (GST) input;
				} else if (input instanceof GSA) {
					this.gsa = (GSA) input;
				} else if (input instanceof GSV) {
					this.gsv = (GSV) input;
				}else if (input instanceof RMC){
					this.rmc = (RMC) input;
				}
					
			}
			publish();
		}

	}

	/**
	 * Check for data integrity.
	 * 
	 * @param input
	 * @return - true if integrity check passes
	 */
	private boolean validate(GPSObject input) {

		boolean isValid = false;
		log.log(Level.FINER, "validating input {0}", input.toString());
		if (input instanceof GPSDataIntegrity) {
			isValid = ((GPSDataIntegrity) input).checkIntegrity();
		}else{
			log.log(Level.WARNING, "input {0} do not implement {1}", new Object[]{input.toString(), GPSDataIntegrity.class.getName()});
		}
		log.log(Level.FINER, "validation result {0} \n", isValid);
		return isValid;
	}

	/**
	 * Check all the required data has been populated for this session. If true
	 * then publish the GPS session data.
	 */
	private void publish() {
		// TODO: Add more null checks
		if (getGga() != null && getGsa() != null && getGsv() != null) {
			List<GPSObject> gpsData = new ArrayList<GPSObject>();

			if (checkIntegrity()) {
				gpsData.add(getGga());
				gpsData.add(getGsa());
				gpsData.add(getGsv());
				gpsData.add(getRmc());
				log.log(Level.FINER, "broadcasting now");
				broadcaster.broadcast(gpsData);
			} else {
				log.log(Level.WARNING,
						"abort broadcasting due to integrity check failure");
			}
		}

	}

	/**
	 * @return - true if all integrity checks are passed.
	 */
	private boolean checkIntegrity() {
		return getGga().checkIntegrity() && getGsa().checkIntegrity()
				&& getGsv().checkIntegrity();

	}

	public GGA getGga() {
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

}
