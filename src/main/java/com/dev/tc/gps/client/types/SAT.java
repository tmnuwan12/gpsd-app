/**
 * 
 */
package com.dev.tc.gps.client.types;

import com.dev.tc.gps.client.contract.GPSObject;


/**
 * @author NThusitha
 *
 */
public class SAT implements GPSObject {

	


	private int PRN = -1;

	private int azimuth = -1;

	private int elevation = -1;

	private int signalStrength = -1;

	private boolean used = false;

	/**
	 * PRN ID of the satellite. 1-63 are GNSS satellites, 64-96 are GLONASS satellites, 100-164 are SBAS satellites
	 * 
	 * @return PRN
	 */
	public int getPRN() {
		return this.PRN;
	}

	/**
	 * PRN ID of the satellite. 1-63 are GNSS satellites, 64-96 are GLONASS satellites, 100-164 are SBAS satellites
	 * 
	 * @param PRN
	 *            the PRN to set
	 */
	public void setPRN(final int PRN) {
		this.PRN = PRN;
	}

	/**
	 * Azimuth, degrees from true north.
	 * 
	 * @return azimuth
	 */
	public int getAzimuth() {
		return this.azimuth;
	}

	/**
	 * Azimuth, degrees from true north.
	 * 
	 * @param azimuth
	 *            the azimuth to set
	 */
	public void setAzimuth(final int azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * Elevation in degrees.
	 * 
	 * @return elevation
	 */
	public int getElevation() {
		return this.elevation;
	}

	/**
	 * Elevation in degrees.
	 * 
	 * @param elevation
	 *            the elevation to set
	 */
	public void setElevation(final int elevation) {
		this.elevation = elevation;
	}

	/**
	 * Signal strength in dB.
	 * 
	 * @return signal strength
	 */
	public int getSignalStrength() {
		return this.signalStrength;
	}

	/**
	 * Signal strength in dB.
	 * 
	 * @param signalStrength
	 *            the signal strength to set
	 */
	public void setSignalStrength(final int signalStrength) {
		this.signalStrength = signalStrength;
	}

	/**
	 * Used in current solution? (SBAS/WAAS/EGNOS satellites may be flagged used if the solution has corrections from them, but not all drivers make this information available.)
	 * 
	 * @return used
	 */
	public boolean getUsed() {
		return this.used;
	}

	/**
	 * Used in current solution? (SBAS/WAAS/EGNOS satellites may be flagged used if the solution has corrections from them, but not all drivers make this information available.)
	 * 
	 * @param used
	 *            the used flag to set
	 */
	public void setUsed(final boolean used) {
		this.used = used;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.PRN);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.azimuth);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.elevation);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.signalStrength);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		result = (prime * result) + ((this.used) ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final SAT other = (SAT) obj;
		if (Double.doubleToLongBits(this.PRN) != Double.doubleToLongBits(other.PRN)) {
			return false;
		}
		if (Double.doubleToLongBits(this.azimuth) != Double.doubleToLongBits(other.azimuth)) {
			return false;
		}
		if (Double.doubleToLongBits(this.elevation) != Double.doubleToLongBits(other.elevation)) {
			return false;
		}
		if (Double.doubleToLongBits(this.signalStrength) != Double.doubleToLongBits(other.signalStrength)) {
			return false;
		}
		if (this.used != other.used) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SATObject{PRN=");
		sb.append(this.PRN);
		sb.append(", az=");
		sb.append(this.azimuth);
		sb.append(", el=");
		sb.append(this.elevation);
		sb.append(", ss=");
		sb.append(this.signalStrength);
		sb.append(", used=");
		sb.append(this.used ? "Y" : "N");
		sb.append("}");
		return sb.toString();
	}

}
