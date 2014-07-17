/**
 * 
 */
package com.dev.tc.gps.client.notification;

import java.util.ArrayList;
import java.util.List;

import com.dev.tc.gps.client.contract.GPSObject;

/**
 * Notification manager for clients.
 * Singleton
 * 
 * @author NThusitha
 * 
 */
public class GPSBroadcaster {

	private List<Observer> observers = new ArrayList<Observer>();

	private static GPSBroadcaster BROADCASTER;

	private GPSBroadcaster() {

	}

	/**
	 * @return - Instacne of a GPSBroadcaster
	 */
	public static synchronized GPSBroadcaster instance() {

		if (BROADCASTER == null) {
			BROADCASTER = new GPSBroadcaster();
		}
		return BROADCASTER;
	}

	
	

	
	
	/**
	 * Subscribe for notifications
	 * 
	 * @param observer
	 */
	public void subscribe(Observer observer) {
		if (observer != null) {
			observers.add(observer);
		}
	}

	/**
	 * Will be invoked internally when GPS 3D fix has been captured.
	 */
	public void broadcast(List<GPSObject> gpsData) {
		for (Observer observer : getObservers()) {
			observer.notify_(gpsData);
		}
	}

	public List<Observer> getObservers() {
		return observers;
	}

	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}

}
