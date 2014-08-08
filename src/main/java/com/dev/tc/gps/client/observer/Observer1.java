package com.dev.tc.gps.client.observer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.notification.Observer;
import com.dev.tc.gps.client.types.GGA;
import com.dev.tc.gps.client.types.GSA;
import com.dev.tc.gps.client.types.GSV;
import com.dev.tc.gps.client.types.GSV.GSVSentence;
import com.dev.tc.gps.client.types.GSV.SpaceVehicle;
import com.dev.tc.gps.windows.native_.client.SerialPortReader;

/**
 * @author NThusitha
 * 
 */
public class Observer1 implements Observer {

	private static final Logger log = Logger.getLogger(Observer1.class
			.getName());

	@Override
	public void notify_(List<GPSObject> gpsData) {

		if (gpsData != null && !gpsData.isEmpty()) {

			log.log(Level.FINE, "Received GPS data with size {0}",
					gpsData.size());

			log.log(Level.INFO,
					"============== BEGIN GPS BEACON====================="
							+ "\n");

			StringBuffer gpsDataBanner = new StringBuffer();
			StringBuffer ggaDetails = new StringBuffer();
			StringBuffer gsaDetails = new StringBuffer();
			StringBuffer gsvDetails = new StringBuffer();
			StringBuffer rmcDetails = new StringBuffer();

			for (GPSObject obj : gpsData) {

				// log.log(Level.INFO, "{0} \n", obj.toString());

				if (obj instanceof GGA) {
					GGA gga = (GGA) obj;
					String rawTimestamp = Double.toString(gga.getTimestamp());
					// Sample time - 123519 Fix taken at 12:35:19 UTC
					String hours = (String) rawTimestamp.substring(0, 2);
					String mins = (String) rawTimestamp.substring(2, 4);
					String secs = (String) rawTimestamp.substring(4, 6);

					String formattedLat = formatLocation(
							Double.toString(gga.getLat()),
							gga.getLatDirection());

					String formattedLon = formatLocation(
							Double.toString(gga.getLon()),
							gga.getLonDirection());

					ggaDetails.append("Timestamp : ").append(hours).append(":")
							.append(mins).append(":").append(secs)
							.append(" UTC").append("\n").append("Latitude : ")
							.append(formattedLat).append("\n")
							.append("Longitude : ").append(formattedLon)
							.append(" ").append(gga.getLonDirection())
							.append("\n").append("Fix Quality : ")
							.append(gga.getFixQuality()).append("\n")
							.append("\n").append("Number of Satellites : ")
							.append(gga.getNumerOfSatellites()).append("\n");
				} else if (obj instanceof GSA) {
					GSA gsa = (GSA) obj;

					StringBuffer prnBuff = new StringBuffer();
					for (String prn : gsa.getPrns()) {

						prnBuff.append(prn).append(",");
					}
					if (prnBuff.length() > 0) {
						prnBuff.deleteCharAt(prnBuff.length() - 1);
					}

					gsaDetails.append("PRNS : ").append(prnBuff.toString())
							.append("\n").append("Fix Mode : ")
							.append(gsa.getFixMode()).append("\n")
							.append("Fix Value : :").append(gsa.getFixVal())
							.append("\n");
				} else if (obj instanceof GSV) {

					GSV gsv = (GSV) obj;
					// StringBuffer gsvDetails = new StringBuffer();
					for (GSVSentence s : gsv.getSentences()) {

						gsvDetails.append("Space Vehicle Information").append(
								"\n");
						short index = 1;
						for (SpaceVehicle sv : s.getSvs()) {
							gsvDetails.append("Space Vehicle").append(index)
									.append("\n");
							gsvDetails.append("PRN : ").append(sv.getPrn())
									.append("\n").append("Elevation : ")
									.append(sv.getElevation()).append("\n")
									.append("Azimuth : ")
									.append(sv.getAzimuth()).append("\n")
									.append("SNR : ").append(sv.getSnr())
									.append("\n");
							index++;
						}

					}
				}

			}

			gpsDataBanner.append(ggaDetails).append(gsaDetails)
					.append(gsvDetails);

			log.log(Level.INFO, gpsDataBanner.toString());

			log.log(Level.INFO,
					"============== END GPS BEACON=====================" + "\n");

		}

	}

	/**
	 * @param location
	 * @param direction
	 * @return location in Degrees minutes formats (DDD MM + compass direction)
	 *         i.e.48 deg 07.038' N
	 * 
	 */
	private String formatLocation(String location, char direction) {
		String rawLat = location;
		int decimalPointIndex = rawLat.indexOf('.');
		// String latSecs = rawLat.substring(decimalPointIndex + 1);
		String minutes = rawLat.substring(decimalPointIndex - 2);
		String degrees = rawLat.substring(0, decimalPointIndex - 2);

		StringBuffer formattedLocation = new StringBuffer();
		formattedLocation.append(degrees).append("deg").append(minutes)
				.append("'").append(" ").append(direction);

		return formattedLocation.toString();

	}

}
