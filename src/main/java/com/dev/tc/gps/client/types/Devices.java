/**
 * 
 */
package com.dev.tc.gps.client.types;

import java.util.List;

import com.dev.tc.gps.client.contract.GPSObject;

import de.taimos.gpsd4java.types.DeviceObject;
import de.taimos.gpsd4java.types.DevicesObject;

/**
 * @author NThusitha
 *
 */
public class Devices implements GPSObject {



	private List<DeviceObject> devices;

	/**
	 * list of devices
	 * 
	 * @return the devices
	 */
	public List<DeviceObject> getDevices() {
		return this.devices;
	}

	/**
	 * list of devices
	 * 
	 * @param devices
	 *            the devices to set
	 */
	public void setDevices(final List<DeviceObject> devices) {
		this.devices = devices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.devices == null) ? 0 : this.devices.hashCode());
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
		final Devices other = (Devices) obj;
		if (this.devices == null) {
			if (other.devices != null) {
				return false;
			}
		} else if (!this.devices.equals(other.devices)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DevicesObject{devices=" + this.devices.size() + "}";
	}


}
