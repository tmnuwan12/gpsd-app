/**
 * 
 */
package com.dev.tc.gps.client.types;

import com.dev.tc.gps.client.contract.GPSDataIntegrity;
import com.dev.tc.gps.client.contract.GPSObject;

/**
 * The most important NMEA sentences include the GGA which provides the current
 * Fix data, the RMC which provides the minimum gps sentences information, and
 * the GSA which provides the Satellite status data.
 * 
 * GGA - essential fix data which provide 3D location and accuracy data.
 * 
 * @author NThusitha
 * 
 */
public class GGA implements GPSObject, GPSDataIntegrity {

	private double timestamp = Double.NaN;

	private double lat = Double.NaN;

	private char latDirection = 'N';

	private double lon = Double.NaN;

	private char lonDirection = 'E';

	private byte fixQuality = 0;

	private int numerOfSatellites = 0;

	private float horizontalDiluationPos = 0;

	private float alt = Float.NaN;

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public byte getFixQuality() {
		return fixQuality;
	}

	public void setFixQuality(byte fixQuality) {
		this.fixQuality = fixQuality;
	}

	public int getNumerOfSatellites() {
		return numerOfSatellites;
	}

	public void setNumerOfSatellites(int numerOfSatellites) {
		this.numerOfSatellites = numerOfSatellites;
	}

	public float getHorizontalDiluationPos() {
		return horizontalDiluationPos;
	}

	public void setHorizontalDiluationPos(float horizontalDiluationPos) {
		this.horizontalDiluationPos = horizontalDiluationPos;
	}

	public float getAlt() {
		return alt;
	}

	public void setAlt(float alt) {
		this.alt = alt;
	}

	public char getLatDirection() {
		return latDirection;
	}

	public void setLatDirection(char latDirection) {
		this.latDirection = latDirection;
	}

	public char getLonDirection() {
		return lonDirection;
	}

	public void setLonDirection(char lonDirection) {
		this.lonDirection = lonDirection;
	}

	@Override
	public boolean checkIntegrity() {
		boolean isOk = false;
		if (getTimestamp() != Double.NaN && getLat() != Double.NaN
				&& getLon() != Double.NaN && getFixQuality() != 0
				&& getNumerOfSatellites() != 0
						/*&& getHorizontalDiluationPos() != 0 && getAlt() != Float.NaN*/) {

			isOk = true;
		}
		return isOk;
	}

	@Override
	public String toString() {
		return "GGA [timestamp=" + timestamp + ", lat=" + lat
				+ ", latDirection=" + latDirection + ", lon=" + lon
				+ ", lonDirection=" + lonDirection + ", fixQuality="
				+ fixQuality + ", numerOfSatellites=" + numerOfSatellites
				+ ", horizontalDiluationPos=" + horizontalDiluationPos
				+ ", alt=" + alt + "]";
	}
	
	

}
