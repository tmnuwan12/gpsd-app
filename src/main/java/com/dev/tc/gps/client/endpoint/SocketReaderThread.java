/**
 * 
 */
package com.dev.tc.gps.client.endpoint;

import java.io.BufferedReader;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.contract.Protocol;

import de.taimos.gpsd4java.backend.AbstractResultParser;
import de.taimos.gpsd4java.backend.GPSdEndpoint;
import de.taimos.gpsd4java.backend.SocketThread;

/**
 * @author NThusitha
 *
 */
public class SocketReaderThread extends Thread {

	private static final Logger log = Logger.getLogger(SocketReaderThread.class.getName());
	private final BufferedReader reader;
	private final GPSdEndpoint_ endpoint;

	/**
	 * @param reader
	 *            the socket input
	 * @param endpoint
	 *            the endpoint
	 * @param resultParser
	 */
	public SocketReaderThread(final BufferedReader reader, final GPSdEndpoint_ endpoint) {
		
		if (reader == null) {
			throw new IllegalArgumentException("reader can not be null!");
		}
		if (endpoint == null) {
			throw new IllegalArgumentException("endpoint can not be null!");
		}

		this.reader = reader;
		this.endpoint = endpoint;

		this.setDaemon(true);
		this.setName("GPS Socket Thread");
	}

	@Override
	public void run() {
		while (true) {
			try {
				// read line from socket
				final String s = this.reader.readLine();
				
				if (s == null) {
					//break;
				}
				if (!s.isEmpty()) {
					// parse line and handle it accordingly
					log.log(Level.INFO, "reading line: {0}", s);
					this.endpoint.handle(s);
				}
			} catch (final SocketException e) {
				// stop if socket fails
				break;
			} catch (final Exception e) {
				// TODO handle this better
				log.log(Level.WARNING, null, e);
			}
		}
	}
}
