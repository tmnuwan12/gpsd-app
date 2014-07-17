package com.dev.tc.gps.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.json.JSONException;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.endpoint.GPSdEndpoint_;
import com.dev.tc.gps.client.notification.GPSBroadcaster;
import com.dev.tc.gps.client.notification.Observer;
import com.dev.tc.gps.client.parser.NMEA0183;
import de.taimos.gpsd4java.types.ParseException;

public class GPSDClient {

	private static final Logger log = Logger.getLogger(GPSDClient.class
			.getName());

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 * @throws JSONException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException, JSONException, ParseException {

		LogManager logManager = LogManager.getLogManager();

		// logManager.readConfiguration(new
		// FileInputStream(C:\sandbox\ethicalsourcing\gps-reader\src\main\resources\log\log.config"));
		logManager
				.readConfiguration(new FileInputStream(
						"C:\\sandbox\\ethicalsourcing\\gps-reader\\src\\main\\resources\\log\\log.config"));
		GPSdEndpoint_ gpsdEndPoint = new GPSdEndpoint_("localhost", 2947,
				new NMEA0183());

		// gpsdEndPoint.addListener(new CustomDistanceListener(300));

		// log.log(Level.INFO, "Version: {0}", gpsdEndPoint.version());

		GPSBroadcaster.instance().subscribe(new Observer() {

			@Override
			public void notify_(List<GPSObject> gpsData) {

				if (gpsData != null && !gpsData.isEmpty()) {

					log.log(Level.INFO, "Received GPS data with size {0}",
							gpsData.size());

					log.log(Level.FINEST,
							"============== BEGIN GPS BEACON====================="
									+ "\n");
					for (GPSObject obj : gpsData) {
						
						log.log(Level.FINEST, "{0} \n", obj.toString());
					}
					log.log(Level.FINEST,
							"============== END GPS BEACON====================="
									+ "\n");
				}

			}

		});

		gpsdEndPoint.start();
		/*
		 * try {
		 * 
		 * log.log(Level.INFO, "Watch >>>: {0}", gpsdEndPoint.watch(true,
		 * false)); } catch (Exception e) {
		 * 
		 * }
		 */

		gpsdEndPoint.poll();

		// TODO: Make it to a windows service
		Thread.sleep(5000000);

	}

}
