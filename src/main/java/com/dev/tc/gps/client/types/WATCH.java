/**
 * 
 */
package com.dev.tc.gps.client.types;

import com.dev.tc.gps.client.contract.GPSObject;

import de.taimos.gpsd4java.types.WatchObject;

/**
 * @author NThusitha
 * 
 */
public class WATCH implements GPSObject {

	private boolean enable = true;

	private boolean dump = false;

	/**
	 * Enable (true) or disable (false) watcher mode. Default is true.
	 * 
	 * @return the enable
	 */
	public boolean isEnable() {
		return this.enable;
	}

	/**
	 * Enable (true) or disable (false) watcher mode. Default is true.
	 * 
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(final boolean enable) {
		this.enable = enable;
	}

	/**
	 * Enable (true) or disable (false) dumping of JSON reports. Default is
	 * false.
	 * 
	 * @return the json
	 */
	public boolean isDump() {
		return this.dump;
	}

	/**
	 * Enable (true) or disable (false) dumping of JSON reports. Default is
	 * false.
	 * 
	 * @param dump
	 *            the dump to set
	 */
	public void setDump(final boolean dump) {
		this.dump = dump;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (this.dump ? 1231 : 1237);
		result = (prime * result) + (this.enable ? 1231 : 1237);
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
		final WATCH other = (WATCH) obj;
		if (this.dump != other.dump) {
			return false;
		}
		if (this.enable != other.enable) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WatchObject{enable=" + this.enable + ", dump=" + this.dump
				+ "}";
	}

}
