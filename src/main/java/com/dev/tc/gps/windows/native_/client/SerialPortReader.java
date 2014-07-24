/**
 * 
 */
package com.dev.tc.gps.windows.native_.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.GPSDClient;
import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.notification.GPSBroadcaster;
import com.dev.tc.gps.client.notification.Observer;
import com.dev.tc.gps.gps_reader.GPSReader;

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
					System.out.print("tyring to open port :" + port.getName()
							+ "\n");
					sPort = (SerialPort) port.open(OWNER, BLOCKING_TIME_MS);

					if (sPort != null) {

						GPSDataListener gpsListener = new GPSDataListener();
						sPort.addEventListener(gpsListener);
						gpsListener.setInputStream(sPort.getInputStream());
						gpsListener.setMutex(getMutex());
						sPort.notifyOnDataAvailable(true);
						sPort.setSerialPortParams(BAUD_RATE, DATA_BITS,
								STOP_BITS, PARITY_NONE);
						//sendCommand1(sPort.getOutputStream());
						sendPollSoftwareVersionCmd(sPort.getOutputStream());
						
						//sendSwitchToSiRfMode(sPort.getOutputStream());
						//debugDataOn(sPort.getOutputStream());
						//debugDataOff(sPort.getOutputStream());
						synchronized (getMutex()) {
							getMutex().wait(10000);
							while (getMutex().isReading) {
								getMutex().wait();
							}
						}

					} else {
						System.out.print("serial port is null, can't continue");
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

	private void sendCommand1(final OutputStream out) throws IOException {
		int[] startSeq = new int[] { 0xA0, 0xA2, 0x0018 };

		int[] payLoad = new int[] { 0x81, 0x02, 0x01, 0x01, 0x00, 0x01, 0x01,
				0x01, 0x05, 0x01, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00,
				0x01, 0x00, 0x01, 0x00, 0x01,
				// bitrate
				0x00 };
		
		int[] endSeq = new int[]{getChecksumForPayload(payLoad), 0xB0, 0xB3};
		
		for(int i = 0; i < startSeq.length; i++){
			
			out.write(startSeq[i]);
		}
		
		for(int j=0; j < payLoad.length; j++){
			out.write(payLoad[j]);
		}
		
		for(int k=0; k < endSeq.length; k++){
			out.write(endSeq[k]);
		}
		
		out.flush();

	}
	
	
	private void sendSwitchToSiRfMode(final OutputStream out) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		//enable sirf mode
		writer.write("$PSRF100,0,9600,8,1,0*0C");
		//writer.write("$PSRF105,1*3E");
		
		writer.flush();
	}
	
	private void debugDataOn(final OutputStream out) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		//enable sirf mode
		writer.write("$PSRF105,1*3E");
		//writer.write("$PSRF105,1*3E");
		writer.write(0x0D); //<CR> // \r carriage return
		writer.write(0x0A); // <LF> // \n line feed
		writer.flush();
	}
	
	private void debugDataOff(final OutputStream out) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		//enable sirf mode
		writer.write("$PSRF105,0*3F");
		//writer.write("$PSRF105,1*3E");
		
		writer.write(0x0D); //<CR> // \r carriage return
		writer.write(0x0A); // <LF> // \n line feed
		writer.flush();
	}
	
	private void sendPollSoftwareVersionCmd(final OutputStream out) throws IOException{
		
		int[] startSeq = new int[]{0xA0, 0xA2, 0x0002};
		
		int [] payLoad = new int[]{0x84, 0x00};
		
		int[]	endSeq = new int[]{getChecksumForPayload(payLoad), 0xB0, 0xB3};
	
		for(int i = 0; i < startSeq.length; i++){
			
			out.write(startSeq[i]);
		}
		
		for(int j=0; j < payLoad.length; j++){
			out.write(payLoad[j]);
		}
		
		for(int k=0; k < endSeq.length; k++){
			out.write(endSeq[k]);
		}
		
		out.flush();
		
	}

	private int getChecksumForPayload(int[] msg) {

		int index = 0;
		int checksum = 0;
		while (index < msg.length) {
			checksum = checksum + msg[index];
			checksum = checksum & 0x7fff; // (2^15 -1)
			index++;
		}
		System.out.print("checksum : " + checksum);
		return checksum;
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

		reader.start();
	}

}
