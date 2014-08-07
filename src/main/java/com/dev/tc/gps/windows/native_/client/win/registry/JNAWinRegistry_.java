/**
 * 
 */
package com.dev.tc.gps.windows.native_.client.win.registry;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dev.tc.gps.client.session.GPSSession;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegMultiStringValue;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;
import com.ice.jni.registry.RegistryValue;

/**
 * Main class for handling Windows registry. This class used Windows Registry
 * API Native Interface to communicate with underlying Windows 32-bit registry.
 * 
 * Basically all folders in windows registry are keys and subkeys (folders &
 * subfolders). There are values for keys/subkeys. Each of those value have data
 * binded to them (which is in the righ in reg edit window).
 * 
 * 'Name' is the value name and 'Data' is the data attached to that value.
 * 
 * Only support 32-bit Java as the this class can only access 32-bit registry
 * information only.
 * 
 * @author NThusitha
 * @date 06-August-2014
 * 
 */
public class JNAWinRegistry_ {

	private static final Logger log = Logger.getLogger(GPSSession.class
			.getName());

	// private final static String GPS_DEVICE_REG_KEY =
	// "SYSTEM\\ControlSet001\\Enum\\USB\\VID_067B&PID_2303\\6&6f0c48d&0&2";

	//HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet
	
	
	//private final static String GPS_DEVICE_REG_KEY = "SYSTEM\\ControlSet001\\Enum\\USB\\VID_067B&PID_2303";
	// registry node for the ND100S GPS device (CurrentControlSet key is the best key to get the device data, not ControlSet001 or ControlSet002)
	private final static String GPS_DEVICE_REG_KEY = "SYSTEM\\CurrentControlSet\\Enum\\USB\\VID_067B&PID_2303";
	
	/**
	 * Read registry to get ND100S specific registry data.
	 * 
	 * @throws RegistryException
	 */
	public static void readRegistry() throws RegistryException {

		RegistryKey parentRegKey = Registry.openSubkey(
				Registry.HKEY_LOCAL_MACHINE, GPS_DEVICE_REG_KEY,
				RegistryKey.ACCESS_READ);
		int noOfSubKeys = parentRegKey.getNumberSubkeys();
		log.log(Level.INFO, "No of subKeys : {0}", noOfSubKeys);

		@SuppressWarnings("rawtypes")
		Enumeration enumeration = parentRegKey.keyElements();

		while (enumeration.hasMoreElements()) {

			String childKey = (String) enumeration.nextElement();
			openSubKey1(parentRegKey, childKey);

		}

		parentRegKey.closeKey();

	}

	/**
	 * Open sub-keys under GPS_DEVICE_REG_KEY. This is the parent node where the
	 * GPS device driver registry details are stored.
	 * 
	 * @param parentKey
	 * @param childKey
	 * @throws NoSuchValueException
	 * @throws RegistryException
	 */
	private static void openSubKey1(RegistryKey parentKey, String childKey)
			throws NoSuchValueException, RegistryException {

		RegistryKey registry = Registry.openSubkey(parentKey, childKey,
				RegistryKey.ACCESS_READ);

		RegistryValue classGUID = registry.getValue("ClassGUID");
		RegistryValue driver = registry.getValue("Driver");
		RegistryValue hardwareId = registry.getValue("HardwareID");
		RegistryValue mfg = registry.getValue("Mfg");
		RegistryValue friendlyName = registry.getValue("FriendlyName");

		showInfo(new RegistryValue[] { classGUID, driver, hardwareId, mfg,
				friendlyName });
	}

	/**
	 * @param registryValues
	 */
	private static void showInfo(RegistryValue... registryValues) {

		log.log(Level.INFO, "================================================");

		if (registryValues[0] instanceof RegStringValue) {
			RegStringValue strData = (RegStringValue) registryValues[0];

			// System.out.print("ClassGUID > " + strData.getData() + "\n");
			log.log(Level.INFO, "ClassGUID : {0}", strData.getData());

		}

		if (registryValues[1] instanceof RegStringValue) {
			RegStringValue strDriver = (RegStringValue) registryValues[1];

			log.log(Level.INFO, "Driver : {0}", strDriver.getData());

		}

		if (registryValues[2] instanceof RegMultiStringValue) {

			RegMultiStringValue hardwareIds = (RegMultiStringValue) registryValues[2];
			for (String id : hardwareIds.getData()) {

				log.log(Level.INFO, "Hardware ID : {0}", id);

			}
		}
		if (registryValues[3] instanceof RegStringValue) {

			RegStringValue strMfg = (RegStringValue) registryValues[3];

			log.log(Level.INFO, "Mfg : {0}", strMfg.getData());

		}

		if (registryValues[4] instanceof RegStringValue) {

			RegStringValue deviceName = (RegStringValue) registryValues[4];
			log.log(Level.INFO, "Device name : {0}", deviceName.getData());

		}

		log.log(Level.INFO, "================================================");

	}

}
