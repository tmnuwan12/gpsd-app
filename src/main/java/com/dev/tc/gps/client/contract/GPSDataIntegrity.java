/**
 * 
 */
package com.dev.tc.gps.client.contract;

/**
 * 
 * All GPSObjects need to implement this if they need to provide a mechanism for
 * data integrity check.
 * 
 * @author NThusitha
 * 
 */
public interface GPSDataIntegrity extends GPSObject{

	boolean checkIntegrity();
	
}
