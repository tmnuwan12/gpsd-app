/**
 * 
 */
package com.dev.tc.gps.client.types;

import com.dev.tc.gps.client.contract.GPSObject;

import de.taimos.gpsd4java.types.VersionObject;

/**
 * @author NThusitha
 *
 */
public class Version implements GPSObject {



	private String release;

	private String rev;

	private double protocolMajor;

	private double protocolMinor;

	/**
	 * @return the release
	 */
	public String getRelease() {
		return this.release;
	}

	/**
	 * @param release
	 *            the release to set
	 */
	public void setRelease(final String release) {
		this.release = release;
	}

	/**
	 * @return the rev
	 */
	public String getRev() {
		return this.rev;
	}

	/**
	 * @param rev
	 *            the rev to set
	 */
	public void setRev(final String rev) {
		this.rev = rev;
	}

	/**
	 * @return the protocolMajor
	 */
	public double getProtocolMajor() {
		return this.protocolMajor;
	}

	/**
	 * @param protocolMajor
	 *            the protocolMajor to set
	 */
	public void setProtocolMajor(final double protocolMajor) {
		this.protocolMajor = protocolMajor;
	}

	/**
	 * @return the protocolMinor
	 */
	public double getProtocolMinor() {
		return this.protocolMinor;
	}

	/**
	 * @param protocolMinor
	 *            the protocolMinor to set
	 */
	public void setProtocolMinor(final double protocolMinor) {
		this.protocolMinor = protocolMinor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.protocolMajor);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.protocolMinor);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		result = (prime * result) + ((this.release == null) ? 0 : this.release.hashCode());
		result = (prime * result) + ((this.rev == null) ? 0 : this.rev.hashCode());
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
		final Version other = (Version) obj;
		if (Double.doubleToLongBits(this.protocolMajor) != Double.doubleToLongBits(other.protocolMajor)) {
			return false;
		}
		if (Double.doubleToLongBits(this.protocolMinor) != Double.doubleToLongBits(other.protocolMinor)) {
			return false;
		}
		if (this.release == null) {
			if (other.release != null) {
				return false;
			}
		} else if (!this.release.equals(other.release)) {
			return false;
		}
		if (this.rev == null) {
			if (other.rev != null) {
				return false;
			}
		} else if (!this.rev.equals(other.rev)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("VersionObject{release=");
		sb.append(this.release);
		sb.append(", rev=");
		sb.append(this.rev);
		sb.append(", protocolMajor=");
		sb.append(this.protocolMajor);
		sb.append(", protocolMinor=");
		sb.append(this.protocolMinor);
		sb.append("}");
		return sb.toString();
	}


}
