/**
 * 
 */
package com.dev.tc.gps.client.types;


/**
 * @author NThusitha
 *
 */
public enum EParity {



	/**
	 * 
	 */
	NO,
	/**
	 * 
	 */
	ODD,
	/**
	 * 
	 */
	EVEN;

	/**
	 * @param parity
	 *            the parity string
	 * @return {@link EParity}
	 */
	public static EParity fromString(final String parity) {
		if (parity == null) {
			return NO;
		}
		if (parity.equals("N")) {
			return NO;
		}
		if (parity.equals("O")) {
			return ODD;
		}
		if (parity.equals("E")) {
			return EVEN;
		}
		return NO;
	}

}
