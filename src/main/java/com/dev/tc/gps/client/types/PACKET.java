/**
 * 
 */
package com.dev.tc.gps.client.types;

import com.dev.tc.gps.client.contract.GPSObject;

/**
 * Packet of normalized GPS data. Used for broadcasting normalized data.
 * 
 * All member values are calculated during GPSSession lifetime.
 * 
 * @author NThusitha
 * @date 08-August-2014
 * 
 */
public class PACKET implements GPSObject {

	private String utcTimestamp;
	private String normalizedLon;
	private String normalizedlat;
	private String fixQuality;
	private double avgNoOfSvsPerReading;
	private double successfulFixPercentage;
	private double accuracy;

	public PACKET(String utcTimestamp, String normalizedLon,
			String normalizedlat, String fixQuality,
			double avgNoOfSvsPerReading, double avgNoOfFixesReachable,
			double accuracy) {
		super();
		this.utcTimestamp = utcTimestamp;
		this.normalizedLon = normalizedLon;
		this.normalizedlat = normalizedlat;
		this.fixQuality = fixQuality;
		this.avgNoOfSvsPerReading = avgNoOfSvsPerReading;
		this.successfulFixPercentage = avgNoOfFixesReachable;
		this.accuracy = accuracy;
	}

	public String getNormalizedLon() {
		return normalizedLon;
	}

	public String getNormalizedlat() {
		return normalizedlat;
	}

	public String getFixQuality() {
		return fixQuality;
	}

	public double getAvgNoOfSvsPerReading() {
		return avgNoOfSvsPerReading;
	}

	public double getSuccessfulFixPercentage() {
		return successfulFixPercentage;
	}

	public String getUtcTimestamp() {
		return utcTimestamp;
	}

	public double getAccuracy() {
		return accuracy;
	}

}
