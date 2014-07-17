/**
 * 
 */
package com.dev.tc.gps.client.parser;

import java.util.Deque;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.contract.Map;
import com.dev.tc.gps.client.types.GSA;

/**
 * GSA parser
 * 
 * @author NThusitha
 * @date 14-July-2014
 * 
 */
public class GSAParser extends NMEA0183Parser implements Map {

	private static final String GGA_ANNOTATION_REGX = "$GPGSA";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.parser.NMEA0183Parser#getAnnotation()
	 */
	@Override
	protected String getAnnotation() {
		return GGA_ANNOTATION_REGX;
	}

	@Override
	public GPSObject map(Deque<String> rawData) {
		GSA gsa = new GSA();
		short index = 1;
		if (rawData != null && !rawData.isEmpty()) {

			while (!rawData.isEmpty()) {
				
				/*
				 * GPS dongle will send empty data when it's warming up. In
				 * order to counter any processing errors ignore then when
				 * first encountered.
				 * 
				 */
				if (rawData.peek().isEmpty()) {
					rawData.poll();
					index++;
					continue;
				}
				
				switch (index) {

				case 1:
					gsa.setFixMode(rawData.poll().charAt(0));
					break;
				case 2:
					gsa.setFixVal(Short.parseShort(rawData.poll()));
					break;
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
					if (!rawData.peek().isEmpty()) {
						gsa.addPrns(rawData.poll());
					}else{
						rawData.poll();
					}
					break;
				case 15:
					gsa.setPdop(Float.parseFloat(rawData.poll()));
					break;
				case 16:
					gsa.setHdop(Float.parseFloat(rawData.poll()));
					break;
				case 17:
					int checksumIndex = rawData.peek().indexOf("*");
					String vdop = rawData.poll().substring(0, checksumIndex);
					gsa.setVdop(Float.parseFloat(vdop));
					break;
				default:
					rawData.clear();
					break;
				}
				index++;
			}

		}

		return gsa;
	}

}
