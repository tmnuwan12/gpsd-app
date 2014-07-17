/**
 * 
 */
package com.dev.tc.gps.client.types;

import java.util.List;

import com.dev.tc.gps.client.contract.GPSObject;


/**
 * @author NThusitha
 * 
 */
public class POLL implements GPSObject {

	private double timestamp;

	private int active;

	private List<TPV> fixes;

	private List<SKY> skyviews;

	/**
	 * Seconds since the Unix epoch, UTC. May have a fractional part of up to
	 * .001sec precision.
	 * 
	 * @return the timestamp
	 */
	public double getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Seconds since the Unix epoch, UTC. May have a fractional part of up to
	 * .001sec precision.
	 * 
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(final double timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Count of active devices.
	 * 
	 * @return the active
	 */
	public int getActive() {
		return this.active;
	}

	/**
	 * Count of active devices.
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(final int active) {
		this.active = active;
	}

	/**
	 * list of TPV objects
	 * 
	 * @return the fixes
	 */
	public List<TPV> getFixes() {
		return this.fixes;
	}

	/**
	 * list of TPV objects
	 * 
	 * @param fixes
	 *            the fixes to set
	 */
	public void setFixes(final List<TPV> fixes) {
		this.fixes = fixes;
	}

	/**
	 * list of SKY objects
	 * 
	 * @return the skyviews
	 */
	public List<SKY> getSkyviews() {
		return this.skyviews;
	}

	/**
	 * list of SKY objects
	 * 
	 * @param skyviews
	 *            the skyviews to set
	 */
	public void setSkyviews(final List<SKY> skyviews) {
		this.skyviews = skyviews;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.active;
		result = (prime * result)
				+ ((this.fixes == null) ? 0 : this.fixes.hashCode());
		result = (prime * result)
				+ ((this.skyviews == null) ? 0 : this.skyviews.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.timestamp);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
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
		final POLL other = (POLL) obj;
		if (this.active != other.active) {
			return false;
		}
		if (this.fixes == null) {
			if (other.fixes != null) {
				return false;
			}
		} else if (!this.fixes.equals(other.fixes)) {
			return false;
		}
		if (this.skyviews == null) {
			if (other.skyviews != null) {
				return false;
			}
		} else if (!this.skyviews.equals(other.skyviews)) {
			return false;
		}
		if (Double.doubleToLongBits(this.timestamp) != Double
				.doubleToLongBits(other.timestamp)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("PollObject{timestamp=");
		sb.append(this.timestamp);
		sb.append(", active=");
		sb.append(this.active);
		sb.append(", fixes=");
		sb.append(this.fixes.size());
		sb.append(", skyviews=");
		sb.append(this.skyviews.size());
		sb.append("}");

		return sb.toString();
	}

}
