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
			
			System.out.printf("%1$8s %2$22s %3$15s %4$20s %5$15s %6$15s\n",
					"Time", "Latitude", "Longitude",
					"AvgSVs/Reading",
					"Fixed Status",
					"Accuracy(m)");
			PRINT_HEADER = true;
		}
	}

}
