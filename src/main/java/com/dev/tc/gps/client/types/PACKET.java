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
public class PACKET implements GPSObject{

	private String utcTimestamp;
	private String normalizedLon;
	private String normalizedlat;
	private String fixQuality;
	private int avgNoOfSvsPerReading;
	private int avgNoOfFixesReachable;
	
	
	public PACKET(String utcTimestamp,String normalizedLon, String normalizedlat,
			String fixQuality, int avgNoOfSvsPerReading, int avgNoOfFixesReachable) {
		super();
		this.utcTimestamp = utcTimestamp;
		this.normalizedLon = normalizedLon;
		this.normalizedlat = normalizedlat;
		this.fixQuality = fixQuality;
		this.avgNoOfSvsPerReading = avgNoOfSvsPerReading;
		this.avgNoOfFixesReachable = avgNoOfFixesReachable;
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
	public int getAvgNoOfSvsPerReading() {
		return avgNoOfSvsPerReading;
	}
	public int getAvgNoOfFixesReachable() {
		return avgNoOfFixesReachable;
	}
	public String getUtcTimestamp() {
		return utcTimestamp;
	}
	
	
	


}
