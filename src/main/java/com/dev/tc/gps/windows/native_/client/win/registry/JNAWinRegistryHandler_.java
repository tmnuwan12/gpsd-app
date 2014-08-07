/**
 * 
 */
package com.dev.tc.gps.windows.native_.client.win.registry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.LogManager;

import com.ice.jni.registry.RegistryException;

/**
 * Registry handler executor.
 * Main class for run stuff.
 * @author NThusitha
 *
 */
public class JNAWinRegistryHandler_ {

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			InstantiationException, SecurityException, FileNotFoundException, IOException {
		
		LogManager logManager = LogManager.getLogManager();

		// logManager.readConfiguration(new
		// FileInputStream(C:\sandbox\ethicalsourcing\gps-reader\src\main\resources\log\log.config"));
		logManager
				.readConfiguration(new FileInputStream(
						"C:\\sandbox\\ethicalsourcing\\gps-reader\\src\\main\\resources\\log\\log.config"));
		
		try {
			JNAWinRegistry_.readRegistry();
		} catch (RegistryException e) {
			e.printStackTrace();
		}
		
		
		

	}
	
}
