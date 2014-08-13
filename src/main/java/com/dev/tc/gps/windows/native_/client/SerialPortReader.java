/**
 * 
 */
package com.dev.tc.gps.windows.native_.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import sun.security.action.GetLongAction;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.notification.DelayMaker;
import com.dev.tc.gps.client.notification.GPSBroadcaster;
import com.dev.tc.gps.client.notification.Observer;
import com.dev.tc.gps.client.observer.Observer2;
import com.dev.tc.gps.client.types.GGA;
import com.dev.tc.gps.client.types.GSA;
import com.dev.tc.gps.client.types.GSV;
import com.dev.tc.gps.client.types.GSV.GSVSentence;
import com.dev.tc.gps.client.types.GSV.SpaceVehicle;
import com.dev.tc.gps.windows.native_.client.listener.GPSDataListener;

import gnu.io.CommPortIdentifier;
import gnu.io.CommPortOwnershipListener;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Serial port reader.
 * 
 * @author NThusitha
 * 
 */
public class SerialPortReader extends Thread {

	private static final Logger log = Logger.getLogger(SerialPortReader.class
			.getName());

	// private CommPortIdentifier portScanner;
	private static String OWNER = "APPXPRESS_GPS_CLIENT";
	private static int BLOCKING_TIME_MS = 2000;
	// private static int BAUD_RATE = 9600; // bit per second
	private static int BAUD_RATE = 4800; // bit per second

	private static int DATA_BITS = 8;
	private static int STOP_BITS = 1;
	private static int PARITY_NONE = 0;
	private Mutex mutex = new Mutex();

	/**
	 * Scan for serial ports and
	 */
	public void doPortScan() {

		Enumeration portIds = CommPortIdentifier.getPortIdentifiers();
		while (portIds.hasMoreElements()) {

			CommPortIdentifier port = (CommPortIdentifier) portIds
					.nextElement();
			if (!port.isCurrentlyOwned()
					&& port.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				SerialPort sPort = null;
				CommPortOwnershipListener portOwnerShipListenr = new com.dev.tc.gps.windows.native_.client.listener.CommPortOwnershipListener();
				try {
					log.log(Level.WARNING, "tyring to open port {0}",
							port.getName());

					port.addPortOwnershipListener(portOwnerShipListenr);
					sPort = (SerialPort) port.open(OWNER, BLOCKING_TIME_MS);

					if (sPort != null) {

						GPSDataListener gpsListener = new GPSDataListener();

						sPort.addEventListener(gpsListener);
						gpsListener.setInputStream(sPort.getInputStream());
						gpsListener.setMutex(getMutex());
						sPort.notifyOnDataAvailable(true);
						sPort.setSerialPortParams(BAUD_RATE, DATA_BITS,
								STOP_BITS, PARITY_NONE);
						// GPSCommandCenter.debugDataOff(sPort.getOutputStream());
						synchronized (getMutex()) {
							getMutex().wait(10000);
							while (getMutex().isReading) {
								getMutex().wait();
							}
						}

					} else {
						log.log(Level.WARNING,
								"serial port is null, can't continue");

					}
				} catch (PortInUseException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (TooManyListenersException e) {

					e.printStackTrace();
				} catch (UnsupportedCommOperationException e) {

					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					try {
						if (sPort != null) {
							if (sPort.getInputStream() != null) {
								sPort.getInputStream().close();

							}
							port.removePortOwnershipListener(portOwnerShipListenr);
							sPort.removeEventListener();
							sPort.close();
						}
					} catch (IOException e) {
						// ignore
						e.printStackTrace();
					}
				}

			}

		}
	}
	
	
	public void configLogs(){
		
		LogManager logManager = LogManager.getLogManager();

		// logManager.readConfiguration(new
		// FileInputStream(C:\sandbox\ethicalsourcing\gps-reader\src\main\resources\log\log.config"));
		try {
			logManager
					.readConfiguration(new FileInputStream(
							"C:\\sandbox\\ethicalsourcing\\gps-reader\\src\\main\\resources\\log\\log.config"));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// TODO: Add ownership change event listeners.

	@Override
	public void run() {
		this.doPortScan();
	}

	public Mutex getMutex() {
		return mutex;
	}

	public void setMutex(Mutex mutex) {
		this.mutex = mutex;
	}

	/**
	 * 
	 * @author NThusitha
	 * 
	 */
	public static class Mutex {

		private boolean isReading;

		public boolean isReading() {
			return isReading;
		}

		public void setReading(boolean isReading) {
			this.isReading = isReading;
		}

	}

	public static void main(String[] args) {
		SerialPortReader reader = new SerialPortReader();
		// Logger.getLogger("com.dev.tc.gps.windows.native_.client").setFilter(new
		// LogFilter());
		//reader.configLogs();
		
		log.log(Level.INFO, "start listening for gps data");
		//System.out.printf("%1$s      %2$s      %3$s      %4$s      %5$s\n", "Time", "Latitude", "Longitude", "Average Number of SVs per Reading", "Average Number of Fixes Reachable");
		if (args != null && args.length > 0) {
			// set broadcast delay
			DelayMaker.BROADCAST_DELAY = Integer.parseInt(args[0]);
		}
		GPSBroadcaster.instance().subscribe(new Observer2());

		reader.start();
	}
}
