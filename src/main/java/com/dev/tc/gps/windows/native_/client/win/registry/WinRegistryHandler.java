/**
 * 
 */
package com.dev.tc.gps.windows.native_.client.win.registry;

import java.lang.reflect.InvocationTargetException;
import java.util.prefs.Preferences;

/**
 * 
 * 
 * @author NThusitha
 * @date 06-August-2014
 * 
 */
public class WinRegistryHandler {

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			InstantiationException {

		WinRegistry_ reg = new WinRegistry_();
		// HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Enum\USB\VID_067B&PID_2303
		System.out.print(reg
				.getStringValueFromRegistryNode("\\SYSTEM\\CurrentControlSet"));

	}

}
