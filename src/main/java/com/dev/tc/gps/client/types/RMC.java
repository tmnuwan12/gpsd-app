/**
 * 
 */
package com.dev.tc.gps.client.types;

import com.dev.tc.gps.client.contract.GPSDataIntegrity;
import com.dev.tc.gps.client.contract.GPSObject;

/**
 * NMEA has its own version of essential gps pvt (position, velocity, time)
 * data. It is called RMC, The Recommended Minimum.
 * 
 * @author NThusitha
 * @date 17-July-2014
 */
public class RMC implements GPSObject, GPSDataIntegrity {

	private static final char DEFAULT_CHAR = 'Z';

	private long timestamp = 0l;
	private char status = DEFAULT_CHAR; // 'A' active, 'V' void.
	private double lat = Double.NaN;
	private char latDirection = DEFAULT_CHAR;
	private double lon = Double.NaN;
	private char lonDirection = DEFAULT_CHAR;
	private float groundSpeedInKnots = 0.0f;
	private float trackAngle = 0.0f;
	private String date = "";
	private float magnaticVariation = 0.0f;
	private char magnaticVariationDirection = DEFAULT_CHAR;
	private char signalIntegrity = DEFAULT_CHAR;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.contract.GPSDataIntegrity#checkIntegrity()
	 */
	@Override
	public boolean checkIntegrity() {

		return getTimestamp() != 0 && getStatus() != DEFAULT_CHAR
				&& getLat() != Double.NaN && getLatDirection() != DEFAULT_CHAR
				&& getLon() != Double.NaN && getLonDirection() != DEFAULT_CHAR
				&& getGroundSpeedInKnots() > 0 && !getDate().isEmpty(); /* rest is optional*/

		// return false;
	}
	
	
	
	

	@Override
	public String toString() {
		return "RMC [timestamp=" + timestamp + ", status=" + status + ", lat="
				+ lat + ", latDirection=" + latDirection + ", lon=" + lon
				+ ", lonDirection=" + lonDirection + ", groundSpeedInKnots="
				+ groundSpeedInKnots + ", trackAngle=" + trackAngle + ", date="
				+ date + ", magnaticVariation=" + magnaticVariation
				+ ", magnaticVariationDirection=" + magnaticVariationDirection
				+ ", signalIntegrity=" + signalIntegrity + "]";
	}





	public static char getDefaultChar() {
		return DEFAULT_CHAR;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public char getStatus() {
		return status;
	}

	public double getLat() {
		return lat;
	}

	public char getLatDirection() {
		return latDirection;
	}

	public double getLon() {
		return lon;
	}

	public char getLonDirection() {
		return lonDirection;
	}

	public float getGroundSpeedInKnots() {
		return groundSpeedInKnots;
	}

	public float getTrackAngle() {
		return trackAngle;
	}

	public String getDate() {
		return date;
	}

	public float getMagnaticVariation() {
		return magnaticVariation;
	}

	public char getMagnaticVariationDirection() {
		return magnaticVariationDirection;
	}

	public char getSignalIntegrity() {
		return signalIntegrity;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLatDirection(char latDirection) {
		this.latDirection = latDirection;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void setLonDirection(char lonDirection) {
		this.lonDirection = lonDirection;
	}

	public void setGroundSpeedInKnots(float groundSpeedInKnots) {
		this.groundSpeedInKnots = groundSpeedInKnots;
	}

	public void setTrackAngle(float trackAngle) {
		this.trackAngle = trackAngle;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setMagnaticVariation(float magnaticVariation) {
		this.magnaticVariation = magnaticVariation;
	}

	public void setMagnaticVariationDirection(char magnaticVariationDirection) {
		this.magnaticVariationDirection = magnaticVariationDirection;
	}

	public void setSignalIntegrity(char signalIntegrity) {
		this.signalIntegrity = signalIntegrity;
	}

}
