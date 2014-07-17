/**
 * 
 */
package com.dev.tc.gps.client;

import de.taimos.gpsd4java.api.DistanceListener;
import de.taimos.gpsd4java.types.TPVObject;

/**
 * @author NThusitha
 * 
 */
public class CustomDistanceListener extends DistanceListener {

	public CustomDistanceListener(double threshold) {
		super(threshold);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.taimos.gpsd4java.api.DistanceListener#handleLocation(de.taimos.gpsd4java
	 * .types.TPVObject)
	 */
	@Override
	protected void handleLocation(TPVObject tpv) {

		System.out.print("Custom distance listner invoked");
		
	}

}
