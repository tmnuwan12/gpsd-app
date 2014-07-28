/**
 * 
 */
package com.dev.tc.gps.windows.native_.client;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.notification.GPSBroadcaster;
import com.dev.tc.gps.client.notification.Observer;
import gnu.io.CommPortIdentifier;
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
	//private static int BAUD_RATE = 9600; // bit per second
	private static int BAUD_RATE = 4800; // bit per second

	private static int DATA_BITS = 8;
	private static int STOP_BITS = 1;
	private static int PARITY_NONE = 0;
	private Mutex mutex = new Mutex();

	/**
	 * Scan for srial ports and
	 */
	public void doPortScan() {

		Enumeration portIds = CommPortIdentifier.getPortIdentifiers();
		while (portIds.hasMoreElements()) {

			CommPortIdentifier port = (CommPortIdentifier) portIds
					.nextElement();
			if (!port.isCurrentlyOwned()
					&& port.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				SerialPort sPort = null;
				try {
					log.log(Level.WARNING, "tyring to open port {0}", port.getName());
					
					sPort = (SerialPort) port.open(OWNER, BLOCKING_TIME_MS);

					if (sPort != null) {

						GPSDataListener gpsListener = new GPSDataListener();
						sPort.addEventListener(gpsListener);
						gpsListener.setInputStream(sPort.getInputStream());
						gpsListener.setMutex(getMutex());
						sPort.notifyOnDataAvailable(true);
						sPort.setSerialPortParams(BAUD_RATE, DATA_BITS,
								STOP_BITS, PARITY_NONE);
						//GPSCommandCenter.debugDataOff(sPort.getOutputStream());
						synchronized (getMutex()) {
							getMutex().wait(10000);
							while (getMutex().isReading) {
								getMutex().wait();
							}
						}

					} else {
						log.log(Level.WARNING, "serial port is null, can't continue");
						
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
		//Logger.getLogger("com.dev.tc.gps.windows.native_.client").setFilter(new LogFilter());
		GPSBroadcaster.instance().subscribe(new Observer() {

			@Override
			public void notify_(List<GPSObject> gpsData) {

				if (gpsData != null && !gpsData.isEmpty()) {

					log.log(Level.INFO, "Received GPS data with size {0}",
							gpsData.size());

					log.log(Level.INFO,
							"============== BEGIN GPS BEACON====================="
									+ "\n");
					for (GPSObject obj : gpsData) {

						log.log(Level.INFO, "{0} \n", obj.toString());
					}
					log.log(Level.INFO,
							"============== END GPS BEACON====================="
									+ "\n");
				}

			}

		});

		reader.start();
	}
	
/*	private void configLog(){
		ConsoleHandler cHandler = new ConsoleHandler();
		cHandler.setFilter(new LogFilter());
		
	}*/

}
