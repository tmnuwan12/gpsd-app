/**
 * 
 */
package com.dev.tc.gps.client.types;


/**
 *  * NMEA Mode of GPSd response
 * @author NThusitha
 *
 */
public enum ENMEAMode {



	/**
	 * no mode value yet seen
	 */
	NotSeen,
	/**
	 * No fix available
	 */
	NoFix,
	/**
	 * two dimensional fix
	 */
	TwoDimensional,
	/**
	 * three dimensional fix
	 */
	ThreeDimensional;

	/**
	 * @param mode
	 *            - mode integer
	 * @return {@link ENMEAMode}
	 */
	public static ENMEAMode fromInt(final int mode) {
		switch (mode) {
		case 0:
			return NotSeen;
		case 1:
			return NoFix;
		case 2:
			return TwoDimensional;
		case 3:
			return ThreeDimensional;
		}
		return NotSeen;
	}

}
