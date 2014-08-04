/**
 * 
 */
package com.dev.tc.gps.windows.native_.client.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.Protocol;
import com.dev.tc.gps.client.parser.NMEA0183;
import com.dev.tc.gps.windows.native_.client.SerialPortReader;
import com.dev.tc.gps.windows.native_.client.SerialPortReader.Mutex;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * Event listener for GPS data.
 * 
 * Reads the data off from the serial port.
 * 
 * @author NThusitha
 * @date 21-July-2014
 * 
 */
public class GPSDataListener implements SerialPortEventListener {

	private static final Logger log = Logger.getLogger(GPSDataListener.class
			.getName());

	
	private InputStream iStream;
	private Mutex mutex;
	private Protocol protocol = new NMEA0183();

	/*
	 * (non-Javadoc)
	 * 
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 */
	@Override
	public void serialEvent(SerialPortEvent ev) {

		if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE
				&& iStream != null) {

			BufferedReader buffReader = null;
			try {
				buffReader = new BufferedReader(new InputStreamReader(
						getiStream(), "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			try {
				synchronized (getMutex()) {
					getMutex().setReading(true);
					if (buffReader.ready()) {
						/*
						int val = 0;
						int index = 0;
						char[] dataChunk = new char[256];
						StringBuffer buff = new StringBuffer();
						while ((val = buffReader.read()) != -1) {
							System.out.print("val:" + val + "\n");
							if (index < dataChunk.length) {
								dataChunk[index] = (char) val;
							} else {
								buff.append(dataChunk);
								System.out.print(buff.toString());
								dataChunk = new char[256];
							}
							index++;

						}
						
						*/
						String ln = buffReader.readLine();
						if (ln != null) {
							//System.out.print("line received > : " + ln + "\n");
							log.log(Level.FINER, "line received > : {0}", ln);
							handle(ln);
						}

					} else {
						//System.out.print("buff reader not ready");
						log.log(Level.WARNING, "buff reader not ready");
					}

					getMutex().notify();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					buffReader.close();
					getiStream().close();
				} catch (IOException e) {
					// ignore
					e.printStackTrace();
				}

			}

		} else {

			/*System.out
					.println("serial event is not DATA_AVAILABLE and iSteam is not null");*/
			
			log.log(Level.WARNING, "serial event is not DATA_AVAILABLE and iSteam is not null");
		}
	}

	/**
	 * Read incoming data from the GPS.
	 * 
	 * @param line
	 */
	public void handle(String line) {
		protocol.read(line);
	}

	public void setInputStream(final InputStream input) {
		this.iStream = input;
	}

	public void setMutex(final Mutex obj) {
		this.mutex = obj;
	}

	public InputStream getiStream() {
		return iStream;
	}

	public Mutex getMutex() {
		return mutex;
	}

}
