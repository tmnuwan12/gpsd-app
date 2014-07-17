/**
 * 
 */
package com.dev.tc.gps.client.types;

import java.util.List;

import com.dev.tc.gps.client.contract.GPSObject;

/**
 * Generic container for storing response for GPSD commands.
 * Alternative for Version, WATCH, POLL objects.
 * @author NThusitha
 * 
 *
 */
public class CMDResponse implements GPSObject {

	private String line;
	private List<String> lines;
	

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		StringBuffer lines = new StringBuffer("");
		if(getLines() != null && !getLines().isEmpty()){
			for(String line : getLines()){
				lines.append(line + "\n");
			}
		}
	
		return "CMDResponse [line=" + line + ", " +
				"lines=" + lines.toString() + "]";
	}
	
	
	
}
