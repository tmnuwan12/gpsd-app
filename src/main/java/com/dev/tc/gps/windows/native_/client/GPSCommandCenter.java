/**
 * 
 */
package com.dev.tc.gps.windows.native_.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Sending various commands to the GPS devices using Binary data protocol and
 * NMEA protocol.
 * 
 * @author NThusitha
 * @date 28-July-2014
 * 
 */
public class GPSCommandCenter {

	public static void sendCommand1(final OutputStream out) throws IOException {
		int[] startSeq = new int[] { 0xA0, 0xA2, 0x0018 };

		int[] payLoad = new int[] { 0x81, 0x02, 0x01, 0x01, 0x00, 0x01, 0x01,
				0x01, 0x05, 0x01, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00,
				0x01, 0x00, 0x01, 0x00, 0x01,
				// bitrate
				0x00 };
		
		int[] endSeq = new int[]{getChecksumForPayload(payLoad), 0xB0, 0xB3};
		
		for(int i = 0; i < startSeq.length; i++){
			
			out.write(startSeq[i]);
		}
		
		for(int j=0; j < payLoad.length; j++){
			out.write(payLoad[j]);
		}
		
		for(int k=0; k < endSeq.length; k++){
			out.write(endSeq[k]);
		}
		
		out.flush();

	}
	
	public static void sendSwitchToACIIProtocol(final OutputStream out) throws IOException{
		
		int[] startSeq = new int[]{ 0xA0, 0xA2, 0x00, 0x02};
		
		int[] payload = new int[]{0x87, 0x02};
		
		//int[] endSeq = new int[]{getChecksumForPayload(payload), 0xB0, 0xB3};
		//quick workaround to splitting 15 bit checksums
		int[] endSeq = new int[]{0x00, 0x89, 0xB0, 0xB3};
		
		
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		writeCommandAsBytes(startSeq, payload, endSeq, writer);
		
		
	}
	
/*	private int[] splitToTwoBytes(int[] input){
		
		int[] source = input;
		
		
		
	}*/
	
	
	
	public static void writeCommandAsBytes(int[] startSeq, int[] payload, int[] endSeq, Writer writer) throws IOException{
		
		
		for(int i = 0; i < startSeq.length; i++){
			
			writer.write(startSeq[i]);
		}
		
		for(int j=0; j < payload.length; j++){
			writer.write(payload[j]);
		}
		
		for(int k=0; k < endSeq.length; k++){
			writer.write(endSeq[k]);
		}
		
		writer.flush();
		
		
	}
	
	
	public static void sendSwitchToSiRfMode(final OutputStream out) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		//enable sirf mode
		writer.write("$PSRF100,0,9600,8,1,0*0C");
		//writer.write("$PSRF105,1*3E");
		
		writer.flush();
	}
	
	public static void debugDataOn(final OutputStream out) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		//enable sirf mode
		writer.write("$PSRF105,1*3E");
		//writer.write("$PSRF105,1*3E");
		writer.write(0x0D); //<CR> // \r carriage return
		writer.write(0x0A); // <LF> // \n line feed
		writer.flush();
	}
	
	public static void debugDataOff(final OutputStream out) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		//enable sirf mode
		writer.write("$PSRF105,0*3F");
		//writer.write("$PSRF105,1*3E");
		
		writer.write(0x0D); //<CR> // \r carriage return
		writer.write(0x0A); // <LF> // \n line feed
		writer.flush();
	}
	
	public static void sendPollSoftwareVersionCmd(final OutputStream out) throws IOException{
		
		int[] startSeq = new int[]{0xA0, 0xA2, 0x0002};
		
		int [] payLoad = new int[]{0x84, 0x00};
		
		int[]	endSeq = new int[]{getChecksumForPayload(payLoad), 0xB0, 0xB3};
	
		for(int i = 0; i < startSeq.length; i++){
			
			out.write(startSeq[i]);
		}
		
		for(int j=0; j < payLoad.length; j++){
			out.write(payLoad[j]);
		}
		
		for(int k=0; k < endSeq.length; k++){
			out.write(endSeq[k]);
		}
		
		out.flush();
		
	}

	private static int getChecksumForPayload(int[] msg) {

		int index = 0;
		int checksum = 0;
		while (index < msg.length) {
			checksum = checksum + msg[index];
			checksum = checksum & 0x7fff; // (2^15 -1)
			index++;
		}
		System.out.print("checksum : " + checksum);
		return checksum;
	}
	
}
