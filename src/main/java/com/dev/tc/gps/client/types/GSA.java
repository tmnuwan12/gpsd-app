/**
 * 
 */
package com.dev.tc.gps.client.types;

import java.util.ArrayList;
import java.util.List;

import com.dev.tc.gps.client.contract.GPSDataIntegrity;
import com.dev.tc.gps.client.contract.GPSObject;

/**
 * GPS DOP and active satellites. This sentence provides details on the nature
 * of the fix. It includes the numbers of the satellites being used in the
 * current solution and the DOP. DOP (dilution of precision) is an indication of
 * the effect of satellite geometry on the accuracy of the fix. It is a unitless
 * number where smaller is better. For 3D fixes using 4 satellites a 1.0 would
 * be considered to be a perfect number, however for over-determined solutions it
 * is possible t.o see numbers below 1.0.
 * 
 * @author NThusitha
 * @date 14-July-2014
 * 
 */
public class GSA implements GPSObject, GPSDataIntegrity {

	private char fixMode = 'A';
	private short fixVal = 0; /*
							 * 1 = no fix 2 = 2D fix 3 = 3D fix
							 */
	private List<String> prns = new ArrayList<String>(); // PRNs of satellites
															// used for fix
															// (space for 12)
	private float pdop = Float.NaN; // PDOP (position dilution of precision)
	private float hdop = Float.NaN; // Horizontal dilution of precision (HDOP)
	private float vdop = Float.NaN; // Vertical dilution of precision (VDOP)

	public char getFixMode() {
		return fixMode;
	}

	public void setFixMode(char fixMode) {
		this.fixMode = fixMode;
	}

	public short getFixVal() {
		return fixVal;
	}

	public void setFixVal(short fixVal) {
		this.fixVal = fixVal;
	}

	public List<String> getPrns() {
		return prns;
	}

	public void setPrns(List<String> prns) {
		this.prns = prns;
	}

	public void addPrns(String prn) {
		this.prns.add(prn);
	}

	public float getPdop() {
		return pdop;
	}

	public void setPdop(float pdop) {
		this.pdop = pdop;
	}

	public float getHdop() {
		return hdop;
	}

	public void setHdop(float hdop) {
		this.hdop = hdop;
	}

	public float getVdop() {
		return vdop;
	}

	public void setVdop(float vdop) {
		this.vdop = vdop;
	}

	@Override
	public boolean checkIntegrity() {

		boolean isOK = false;
		if (getFixVal() != 0 && getPrns().size() > 0 && getPdop() != Float.NaN
				&& getHdop() != Float.NaN && getVdop() != Float.NaN) {
			isOK = true;
		}
		return isOK;
	}

	@Override
	public String toString() {
		
		StringBuffer prnsVals = new StringBuffer("");
		for(String val : getPrns()){
			prnsVals.append(val).append(",");
		}
		if(prnsVals.length() > 0){
			prnsVals.deleteCharAt(prnsVals.length() - 1);
		}
		
		return "GSA [fixMode=" + fixMode + ", fixVal=" + fixVal + ", prns="
				+ prnsVals.toString() + ", pdop=" + pdop + ", hdop=" + hdop + ", vdop=" + vdop
				+ "]";
	}

	
	
}
