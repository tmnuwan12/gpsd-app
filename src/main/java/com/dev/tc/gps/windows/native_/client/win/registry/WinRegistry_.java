/**
 * 
 */
package com.dev.tc.gps.windows.native_.client.win.registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

/**
 * Windows Registry class for manipulating Windows System registry.
 * 
 * @author NThusitha
 * @date 05-August-2014
 * 
 */
public class WinRegistry_ {

	private static final Preferences SYSTEM_ROOT = Preferences.systemRoot();
	//private static final Preferences USER_ROOT = Preferences.userRoot();

	private static Class<? extends Preferences> WINDOWS_PREFERENCES = SYSTEM_ROOT
			.getClass();
	private static Method GET_SPI;
	private static Method OPEN_KEY;
	private static Method WINDOWS_ABS_PATH;
	static {

		try {
			GET_SPI = WINDOWS_PREFERENCES.getDeclaredMethod("getSpi",
					new Class[] { String.class });
			GET_SPI.setAccessible(true);
			
			OPEN_KEY = WINDOWS_PREFERENCES.getDeclaredMethod("openKey", new Class[]{int.class, byte[].class, int.class, int.class});
			OPEN_KEY.setAccessible(true);
			WINDOWS_ABS_PATH = WINDOWS_PREFERENCES.getDeclaredMethod("windowsAbsolutePath", new Class[]{});
			WINDOWS_ABS_PATH.setAccessible(true);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getStringValueFromRegistryNode(String javaName)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {

		byte[]	byteArray = (byte[]) WINDOWS_ABS_PATH.invoke(SYSTEM_ROOT, new Object[]{});
		String res = new String(byteArray);
		System.out.print(res);
		//oh crap java allows only registry add/remove/updates inside Software\\JavaSoft\\Prefs path.
		OPEN_KEY.invoke(SYSTEM_ROOT, new Object[]{});
		
		return (String) GET_SPI.invoke(SYSTEM_ROOT,
				javaName);
	}

}
