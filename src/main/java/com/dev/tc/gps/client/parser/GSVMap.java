/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.Deque;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.contract.Map;
import com.dev.tc.gps.client.notification.DelayMaker;
import com.dev.tc.gps.client.types.GSV;
import com.dev.tc.gps.client.types.GSV.GSVSentence;
import com.dev.tc.gps.client.types.GSV.SpaceVehicle;
import com.dev.tc.gps.client.util.DateTimeUtil;

/**
 * Parser for GSV annotation.
 * 
 * @author NThusitha
 * 
 */
public class GSVMap extends NMEA0183Parser implements Map {

	private static final String GSV_ANNOTATION_REGX = "$GPGSV";
	private static GSV GLOBAL_GSV = new GSV();
	private static long TIMESTAMP = 0l;
	private static long MAX_WAIT_TIME = 1000 * 20; // 20s
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.Map#map(java.util.Deque)
	 */
	@Override
	public GPSObject map(Deque<String> rawData) {
		// TODO : Implement the mapping in different sentences.

		//synchronized (this) {

			GSV gsv = null;
			short index = 1;
			if (rawData != null && !rawData.isEmpty()) {
				
				if (isGSVExpectingAnotherSentence()) {
					gsv = GLOBAL_GSV;
				} else {
					gsv = new GSV();
					// GLOBAL_GSV = gsv;
				}

				GSVSentence sentence = new GSVSentence();
				SpaceVehicle sv1 = new SpaceVehicle();
				SpaceVehicle sv2 = new SpaceVehicle();
				SpaceVehicle sv3 = new SpaceVehicle();
				SpaceVehicle sv4 = new SpaceVehicle();

				while (!rawData.isEmpty()) {
					/*
					 * GPS dongle will send empty data when it's warming up. In
					 * order to counter any processing errors ignore then when
					 * first encountered.
					 */
					if (rawData.peek().isEmpty()) {
						rawData.poll();
						index++;
						continue;
					}

					switch (index) {

					case 1:
						gsv.setNoOfSentencesForFullData(Short
								.parseShort(rawData.poll()));
						break;
					case 2:
						sentence.setSentenceNo(Short.parseShort(rawData.poll()));
						break;
					case 3:
						sentence.setSvsInView(Short.parseShort(rawData.poll()));
						break;
					case 4:
						sv1.setPrn(Short.parseShort(rawData.poll()));
						break;
					case 5:
						sv1.setElevation(Integer.parseInt(rawData.poll()));
						break;
					case 6:
						sv1.setAzimuth(Integer.parseInt(rawData.poll()));
						break;
					case 7:
						sv1.setSnr(Integer.parseInt(rawData.poll()));
						sentence.addSV(sv1);
						break;
					case 8:
						sv2.setPrn(Short.parseShort(rawData.poll()));
						break;
					case 9:
						sv2.setElevation(Integer.parseInt(rawData.poll()));
						break;
					case 10:
						sv2.setAzimuth(Integer.parseInt(rawData.poll()));
						break;
					case 11:
						sv2.setSnr(Integer.parseInt(rawData.poll()));
						sentence.addSV(sv2);
						break;
					case 12:
						sv3.setPrn(Short.parseShort(rawData.poll()));
						break;
					case 13:
						sv3.setElevation(Integer.parseInt(rawData.poll()));
						break;
					case 14:
						sv3.setAzimuth(Integer.parseInt(rawData.poll()));
						break;
					case 15:
						sv3.setSnr(Integer.parseInt(rawData.poll()));
						sentence.addSV(sv3);
						break;
					case 16:
						sv4.setPrn(Short.parseShort(rawData.poll()));
						break;
					case 17:
						sv4.setElevation(Integer.parseInt(rawData.poll()));
						break;
					case 18:
						sv4.setAzimuth(Integer.parseInt(rawData.poll()));
						break;
					case 19:
						String trimmedData = rawData.peek().trim();
						int checkSumStartIndex = trimmedData.indexOf("*");
						if(checkSumStartIndex != 0){
							//only add snr when available
							String snr = rawData.poll().substring(0, checkSumStartIndex);
							sv4.setSnr(Integer.parseInt(snr));
						}else{
							rawData.peek();
						}
						sentence.addSV(sv4);
						break;
					default:
						rawData.clear();
						break;
					}
					index++;

				}

				gsv.addSentence(sentence);
				TIMESTAMP = DateTimeUtil.nowUTC();
				GLOBAL_GSV = gsv;

			}

		//}

		return gsv;
	}


	
	
	/**
	 * When client start monitoring data the GPS dongle will send the sentence without any correlation.
	 * Meaning it can totally omit 1st line and send line 2 and 3. In order to remove extra redundancy of data
	 * we flush GSV data within given time and start listening for data fresh.
	 * @return - true if more sentences are expected in GSV messages.
	 */
	private boolean isGSVExpectingAnotherSentence() {
		
		long now = DateTimeUtil.nowUTC();
		boolean hasNext = false;
		if(now - TIMESTAMP > DelayMaker.BROADCAST_DELAY){
			hasNext = false;
		}else if(GLOBAL_GSV != null && GLOBAL_GSV.getNoOfSentencesForFullData() > GLOBAL_GSV
				.getSentences().size()){
			hasNext = true;
		}
		return hasNext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.NMEA0183Parser#getAnnotation()
	 */
	@Override
	protected String getAnnotation() {
		return GSV_ANNOTATION_REGX;
	}

}
