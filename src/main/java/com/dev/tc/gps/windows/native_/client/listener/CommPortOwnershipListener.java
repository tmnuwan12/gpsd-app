/**
 * 
 */
package com.dev.tc.gps.windows.native_.client.listener;

import java.util.logging.Logger;

import com.dev.tc.gps.windows.native_.client.SerialPortReader;

/**
 * Port ownership listeners.
 * 
 * @author NThusitha
 * @date 04-August-2014
 * 
 */
public class CommPortOwnershipListener implements
		gnu.io.CommPortOwnershipListener {

	private static final Logger log = Logger.getLogger(SerialPortReader.class
			.getName());
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see gnu.io.CommPortOwnershipListener#ownershipChange(int)
	 */
	@Override
	public void ownershipChange(int type) {

		switch (type) {
		
		//TODO: Do something meaningful instead of adding logs.
		case gnu.io.CommPortOwnershipListener.PORT_OWNED:
			log.info("port ownership acquired");
			break;

		case gnu.io.CommPortOwnershipListener.PORT_UNOWNED:
			log.info("do not have port ownership");
			break;
		case gnu.io.CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED:
			
			log.info("port is already in use");
			break;
		}
		;

	}

}
