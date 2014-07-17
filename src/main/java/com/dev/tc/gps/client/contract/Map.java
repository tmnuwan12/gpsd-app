/**
 * 
 */
package com.dev.tc.gps.client.contract;

import java.util.Deque;


/**
 * @author NThusitha
 * 
 */
public interface Map {

	/**
	 * @param rawData
	 * @return
	 */
	 abstract GPSObject map(Deque<String> rawData);
	 
	 /**
	 * @param input
	 * @return
	 */
	abstract Deque<String> parseSentence(String input);
	
}
