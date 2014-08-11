/**
 * 
 */
package com.dev.tc.gps.client.observer;

import java.util.List;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.notification.Observer;
import com.dev.tc.gps.client.session.GPSDataProcessor;
import com.dev.tc.gps.client.types.PACKET;
import com.dev.tc.gps.client.util.ConsoleAttributeManager;

/**
 * @author NThusitha
 * @date 08-August-2014
 * 
 */
public class Observer2 implements Observer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.notification.Observer#notify_(java.util.List)
	 */
	@Override
	public void notify_(List<GPSObject> gpsData) {

		ConsoleAttributeManager.printColums();
		
		if (!gpsData.isEmpty()) {

			for (GPSObject obj : gpsData) {

				if (obj instanceof PACKET) {

					PACKET pkt = (PACKET) obj;
					String timeStamp = pkt.getUtcTimestamp();
					String lon = GPSDataProcessor.DEFAULT_NORMALIZED_LON
							.intern();
					String lat = GPSDataProcessor.DEFAULT_NORMALIZED_LAT
							.intern();

					if (!pkt.getNormalizedLon().equalsIgnoreCase(
							GPSDataProcessor.DEFAULT_NORMALIZED_LON)) {
						lon = pkt.getNormalizedLon().substring(0, 10);
					}

					if (!pkt.getNormalizedlat().equalsIgnoreCase(
							GPSDataProcessor.DEFAULT_NORMALIZED_LAT)) {
						lat = pkt.getNormalizedlat().substring(0, 10);
					}

					double noOfSvs = pkt.getAvgNoOfSvsPerReading();
					double noOfFixes = pkt.getAvgNoOfFixesReachable();

					/*System.out.print("lat" + lat + "\n");
					System.out.print("lon" + lon + "\n");
					System.out.print("noOfSvs" + noOfSvs + "\n");
					System.out.print("noOfFixes" + noOfFixes + "\n");*/
					
					
					Object[] formatterArg = new Object[] { timeStamp, lat, lon,
							noOfSvs, noOfFixes };

					System.out.printf(
							"%1$s      %2$s   %3$s    %4$3f            %5$3f\n",
							formatterArg);

				}

			}
		}
	}

}
