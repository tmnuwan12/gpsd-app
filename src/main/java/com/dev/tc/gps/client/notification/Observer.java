/**
 * 
 */
package com.dev.tc.gps.client.notification;

import java.util.List;

import com.dev.tc.gps.client.contract.GPSObject;

/**
 * Any party who needs to get notified when enough gps data has been collected
 * to determine the location at any given time.
 * 
 * @author NThusitha
 * @date 14-June-2014
 */
public interface Observer {

	void notify_(List<GPSObject> gpsData);
}
