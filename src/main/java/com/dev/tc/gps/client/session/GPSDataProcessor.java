/**
 * 
 */
package com.dev.tc.gps.client.session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.types.GGA;
import com.dev.tc.gps.client.types.GSA;
import com.dev.tc.gps.client.types.GST;
import com.dev.tc.gps.client.types.GSV;
import com.dev.tc.gps.client.types.PACKET;
import com.dev.tc.gps.client.types.RMC;
import com.dev.tc.gps.client.util.DateTimeUtil;

/**
 * @author NThusitha
 * @date 08-August-2014
 * 
 */
public class GPSDataProcessor {

	public static final String DEFAULT_NORMALIZED_LON = "Unknown";
	public static final String DEFAULT_NORMALIZED_LAT = "Unknown";
	public static final String DEAULT_FIXED_QUALITY = "Unknown";

	public static final double STANDARD_DEVIATION_OF_USER_EQUILALENT_RANGE_ERR = Math
			.sqrt(Math.pow(3, 2) + Math.pow(5, 2) + Math.pow(2.5, 2)
					+ Math.pow(2, 2) + Math.pow(1, 2) + Math.pow(0.5, 2));

	/**
	 * @param ggaBuff
	 * @param gsaBuff
	 * @param gsvBuff
	 * @param gstBuff
	 * @param rmcBuff
	 * @return - PACKET which contains processed GPS data.
	 */
	public static PACKET processData(List<GGA> ggaBuff, List<GSA> gsaBuff,
			List<GSV> gsvBuff, List<GST> gstBuff, List<RMC> rmcBuff) {

		String normalizedLon = DEFAULT_NORMALIZED_LON;
		String normalizedLat = DEFAULT_NORMALIZED_LAT;
		// String direction = "N/A"; //ignore changes of direction in given time
		// period between broadcasted packets
		String fixQuality = DEAULT_FIXED_QUALITY;
		double avgNoOfSvsPerReading = 0;
		double successfulFixPercentage = 0;
		double accuracy = 0.0;

		if (!ggaBuff.isEmpty()) {

			double normalizedLonDegree = 0.00;
			double normalizedLonMin = 0.00;
			double accumulatedLonDegrees = 0;
			double accumulatedLonMins = 0;

			double normalizedLatDegree = 0.0;
			double normalizedLatMin = 0.0;
			double accumulatedLatDegrees = 0.0;
			double accumulatedLatMins = 0.0;

			int totalNoOfSVs = 0;

			int ggaCount = 0;
			for (GGA gga : ggaBuff) {
				// ignore direction for now.

				if (gga.getLat() != Double.NaN && gga.getLon() != Double.NaN
						&& gga.getNumerOfSatellites() > 0) {

					String currentLat = Double.toString(gga.getLat());
					String currentLon = Double.toString(gga.getLon());

					accumulatedLonDegrees += getDegrees(currentLon);
					accumulatedLonMins += getMinutes(currentLon);

					accumulatedLatDegrees += getDegrees(currentLat);
					accumulatedLatMins += getMinutes(currentLat);

					totalNoOfSVs += gga.getNumerOfSatellites();

					ggaCount++;
				}
			}
			if (ggaCount > 0) {

				normalizedLonDegree = accumulatedLonDegrees / ggaCount;
				normalizedLonMin = accumulatedLonMins / ggaCount;

				normalizedLatDegree = accumulatedLatDegrees / ggaCount;
				normalizedLatMin = accumulatedLatMins / ggaCount;

				avgNoOfSvsPerReading = totalNoOfSVs / ggaCount;
				normalizedLon = Double.toString(normalizedLonDegree) + " "
						+ Double.toString(normalizedLonMin);
				normalizedLat = Double.toString(normalizedLatDegree) + " "
						+ Double.toString(normalizedLatMin);

			}

		}

		if (!gsaBuff.isEmpty()) {

			int totalNoOfSuccessfulFixes = 0;
			double totalPdop = 0;
			int gsaCount = 0;

			for (GSA gsa : gsaBuff) {

				if (gsa.getFixVal() > 1) {
					// 2D or 3D fix

					totalNoOfSuccessfulFixes++;
				}
				totalPdop += gsa.getPdop();
				gsaCount++;

			}
			if (gsaCount > 0) {
				successfulFixPercentage = totalNoOfSuccessfulFixes / gsaCount;
				accuracy = calculateAccuracy(totalPdop / gsaCount);
			}

		}

		PACKET packet = new PACKET(DateTimeUtil.nowUTCString(), normalizedLon,
				normalizedLat, fixQuality, avgNoOfSvsPerReading,
				successfulFixPercentage, accuracy);

		return packet;
	}

	/**
	 * @param rawLocation
	 * @return - minutes from a rawLocation string.
	 */
	private static double getMinutes(String rawLocation) {

		int decimalPointIndex = rawLocation.indexOf('.');
		String minutes = rawLocation.substring(decimalPointIndex - 2);
		return Double.parseDouble(minutes);
	}

	/**
	 * @param rawLocation
	 * @return - Degree from a rawLocation string.
	 */
	private static double getDegrees(String rawLocation) {

		int decimalPointIndex = rawLocation.indexOf('.');
		String degrees = rawLocation.substring(0, decimalPointIndex - 2);
		return Double.parseDouble(degrees);

	}

	/**
	 * Calculate the standard deviation of error in receiver position using
	 * following algorithm. http://en.wikipedia.org/wiki/
	 * Error_analysis_for_the_Global_Positioning_System
	 * 
	 * @param pdop
	 *            - average pdop
	 * @return - Accuracy in meters.
	 */
	private static double calculateAccuracy(double pdop) {

		double standardDeviationOfError = Math.sqrt(Math.pow(pdop, 2)
				+ Math.pow(
						(STANDARD_DEVIATION_OF_USER_EQUILALENT_RANGE_ERR / 3),
						2) + Math.pow(1, 2));

		return standardDeviationOfError;

	}

}
