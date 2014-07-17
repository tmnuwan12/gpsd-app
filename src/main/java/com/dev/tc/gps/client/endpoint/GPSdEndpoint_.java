/**
 * 
 */
package com.dev.tc.gps.client.endpoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.dev.tc.gps.client.contract.GPSObject;
import com.dev.tc.gps.client.contract.Protocol;
import com.dev.tc.gps.client.parser.NMEA0183;
import com.dev.tc.gps.client.types.CMDResponse;

import de.taimos.gpsd4java.backend.AbstractResultParser;
import de.taimos.gpsd4java.types.IGPSObject;
import de.taimos.gpsd4java.types.ParseException;
import de.taimos.gpsd4java.types.VersionObject;
import de.taimos.gpsd4java.types.WatchObject;

/**
 * @author NThusitha
 * 
 */
public class GPSdEndpoint_ {

	private static final Logger log = Logger.getLogger(GPSdEndpoint_.class
			.getName());
	private final Socket socket;
	private final BufferedReader in;
	private final BufferedWriter out;
	private Thread listenThread;
	/*
	 * private final List<IObjectListener> listeners = new
	 * ArrayList<IObjectListener>( 1);
	 */

	private Protocol protocol = null;
	private IGPSObject asnycResult = null;
	private final Object asyncMutex = new Object();
	private final Object asyncWaitMutex = new Object();

	// private final AbstractResultParser resultParser;

	// ########################################################

	// ########################################################

	/**
	 * Instantiate this class to connect to a GPSd server
	 * 
	 * @param server
	 *            the server name or IP
	 * @param port
	 *            the server port
	 * @param resultParser
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public GPSdEndpoint_(final String server, final int port, final Protocol protocol)
			throws UnknownHostException, IOException {
		if (server == null) {
			throw new IllegalArgumentException("serrver can not be null!");
		}
		if ((port < 0) || (port > 65535)) {
			throw new IllegalArgumentException("Illegal port number: " + port);
		}
		if(protocol == null){
			throw new IllegalArgumentException("protocol can not be null!");
		}

		this.socket = new Socket(server, port);
		this.in = new BufferedReader(new InputStreamReader(
				this.socket.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(
				this.socket.getOutputStream()));
		this.protocol = protocol;
	}

	/**
	 * start the endpoint
	 */
	public void start() {
		this.listenThread = new SocketReaderThread(this.in, this);
		this.listenThread.start();

		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
			log.log(Level.FINE, null, e);
		}
	}

	/**
	 * send WATCH command
	 * 
	 * @param enable
	 *            enable/disable watch mode
	 * @param dumpData
	 *            enable/disable dumping of data
	 * @return {@link WatchObject}
	 * @throws IOException
	 *             on IO error in socket
	 * @throws JSONException
	 */
	public CMDResponse watch(final boolean enable, final boolean dumpData)
			throws IOException, JSONException {
		return this.watch(enable, dumpData, null);
	}

	/**
	 * send WATCH command
	 * 
	 * @param enable
	 *            enable/disable watch mode
	 * @param dumpData
	 *            enable/disable dumping of data
	 * @param device
	 *            If present, enable watching only of the specified device
	 *            rather than all devices
	 * @return {@link WatchObject}
	 * @throws IOException
	 *             on IO error in socket
	 * @throws JSONException
	 */
	private CMDResponse watch(final boolean enable, final boolean dumpData,
			final String device) throws IOException, JSONException {
		final JSONObject watch = new JSONObject();
		watch.put("class", "WATCH");
		watch.put("enable", enable);
		watch.put("json", dumpData);
		if (device != null) {
			watch.put("device", device);
		}
		return this
				.syncCommand("?WATCH=" + watch.toString(), CMDResponse.class);
	}

	/**
	 * Poll GPSd for Message
	 * 
	 * @throws IOException
	 *             on IO error in socket
	 * @throws ParseException
	 *             on illegal response
	 * @throws JSONException
	 */
	public void poll() throws IOException, ParseException, JSONException {
		final JSONObject poll = new JSONObject();
		poll.put("enable", true);
	//	return this.syncCommand("?POLL=" + poll.toString(), CMDResponse.class);
		this.syncCommand("?POLL=" + poll.toString());
	}

	/**
	 * Poll GPSd version
	 * 
	 * @return {@link VersionObject}
	 * @throws IOException
	 *             on IO error in socket
	 * @throws ParseException
	 *             on illegal response
	 */
	public CMDResponse version() throws IOException, ParseException {
		return this.syncCommand("?VERSION;", CMDResponse.class);
	}

	// ########################################################

	/*
	 * send command to GPSd and wait for response
	 * @Deprecated
	 */
	private <T extends GPSObject> T syncCommand(final String command,
			final Class<T> responseClass) throws IOException {
		synchronized (this.asyncMutex) {
			log.log(Level.FINEST, "SENDING COMMAND [ {0} ] TO GPSD", command);
			this.out.write(command + "\n");
			this.out.flush();

			// while (true) {
			// wait for awaited message
			final IGPSObject result = this.waitForResult();
			T response = null;
			if ((result == null) || result.getClass().equals(responseClass)) {
				response = responseClass.cast(result);
			}
			return response;
			// }
		}
	}
	
	private void syncCommand(final String command) throws IOException {
		synchronized (this.asyncMutex) {
			log.log(Level.FINEST, "SENDING COMMAND [ {0} ] TO GPSD", command);
			this.out.write(command + "\n");
			this.out.flush();
			// }
		}
	}
	

	/*
	 * send command without response
	 */
	@SuppressWarnings("unused")
	private void voidCommand(final String command) throws IOException {
		synchronized (this.asyncMutex) {
			this.out.write(command + "\n");
			this.out.flush();
		}
	}

	/*
	 * wait for a response for one second
	 * @Deprecated
	 */
	private IGPSObject waitForResult() {
		synchronized (this.asyncWaitMutex) {
			this.asnycResult = null;
			try {
				if (waitForGPSD() == -1) {
					log.log(Level.FINEST, "waiting until notify event");
					this.asyncWaitMutex.wait();
				} else {
					log.log(Level.FINEST, "waiting {0} ms", waitForGPSD());
					this.asyncWaitMutex.wait(waitForGPSD());
				}
			} catch (final InterruptedException e) {
				log.log(Level.INFO, null, e);
			}
			if (this.asnycResult != null) {
				return this.asnycResult;
			}
		}
		return null;
	}


	/**
	 * Handle incoming data with given protocol
	 * @param sentence
	 */
	void handle(String sentence) {

		protocol.read(sentence);
	}

	/**
	 * @return - wait time in ms for the gpsd responses. if -1 wait untill
	 *         notify event is fired
	 * 
	 */
	protected long waitForGPSD() {
		return -1;
	}
}
