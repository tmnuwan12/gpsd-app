/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.Deque;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.contract.Map;
import com.dev.tc.gps.client.types.RMC;

/**
 * RMC annotation parser.
 * 
 * @author NThusitha
 * @date 17-July-2014
 *
 */
public class RMCParser extends NMEA0183Parser implements Map {

	
	private static final String RMC_ANNOTATION_REGX = "$GPRMC";
	
	/* (non-Javadoc)
	 * @see com.dev.tc.gps.client.contract.Map#map(java.util.Deque)
	 */
	@Override
	public GPSObject map(Deque<String> rawData) {
	
		RMC rmc = new RMC();
		short index = 1;
		if(rawData != null && !rawData.isEmpty()){
			
			while(!rawData.isEmpty()){
				
				if (rawData.peek().isEmpty()) {
					rawData.poll();
					index++;
					continue;
				}
				
				switch (index){
				case 1 : 
					rmc.setTimestamp(Long.parseLong(rawData.poll()));
					break;
				case 2 : 
					rmc.setStatus(rawData.poll().charAt(0));
					break;
				case 3 : 
					rmc.setLat(Double.parseDouble(rawData.poll()));
					break;
				case 4 : 
					rmc.setLatDirection(rawData.poll().charAt(0));
					break;
				case 5 : 
					rmc.setLon(Double.parseDouble(rawData.poll()));
					break;
				case 6 : 
					rmc.setLonDirection(rawData.poll().charAt(0));
					break;
				case 7 : 
					rmc.setGroundSpeedInKnots(Float.parseFloat(rawData.poll()));
					break;
				case 8 : 
					rmc.setTrackAngle(Float.parseFloat(rawData.poll()));
					break;
				case 9 : 
					rmc.setDate(rawData.poll());
					break;
				case 10 : 
					rmc.setMagnaticVariation(Float.parseFloat(rawData.poll()));
					break;
				case 11 : 
					rmc.setMagnaticVariationDirection(rawData.poll().charAt(0));
					break;
				case 12 :
					String trimmedData = rawData.peek().trim();
					int checkSumStartIndex = trimmedData.indexOf("*");
					if(checkSumStartIndex != 0){
						String integrityFlag = rawData.poll().substring(0, checkSumStartIndex).trim();
						rmc.setSignalIntegrity(integrityFlag.charAt(0));
					}
				default : 
					rawData.clear();
					break;
				
				}
				index++;
				
			}
		}
		return rmc;
	}

	/* (non-Javadoc)
	 * @see com.dev.tc.gps.client.parser.NMEA0183Parser#getAnnotation()
	 */
	@Override
	protected String getAnnotation() {
		return RMC_ANNOTATION_REGX;
	}

}
