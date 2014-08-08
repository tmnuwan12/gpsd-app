/**
 * 
 */
package com.dev.tc.gps.client.util;

/**
 * @author NThusitha
 * @date 08-August-2014
 * 
 */
public class ConsoleAttributeManager {

	public static boolean PRINT_HEADER = false;

	/**
	 * Print columns if not printed (happens once).
	 * 
	 * @return
	 */
	public static void printColums() {
		if (!PRINT_HEADER) {
		/*	System.out.printf("%1$s                 %2$s     %3$s    %4$s      %5$s\n",
					"Time", "Latitude", "Longitude",
					"Average Number of SVs per Reading",
					"Average Number of Fixes Reachable");
			PRINT_HEADER = true;*/
			
			System.out.printf("%1$21s                 %2$s     %3$s    %4$s      %5$s\n",
					"Time", "Latitude", "Longitude",
					"Average Number of SVs per Reading",
					"Average Number of Fixes Reachable");
			PRINT_HEADER = true;
		}
	}

}
