/**
 * 
 */
package com.dev.tc.gps.client.types;

import java.util.ArrayList;
import java.util.List;

import com.dev.tc.gps.client.contract.GPSDataIntegrity;
import com.dev.tc.gps.client.contract.GPSObject;

/**
 * GPS Satellites in view. Satellites in View shows data about the satellites
 * that the unit might be able to find based on its viewing mask and almanac
 * data. It also shows current ability to track this data. Note that one GSV
 * sentence only can provide data for up to 4 satellites and thus there may need
 * to be 3 sentences for the full information. It is reasonable for the GSV
 * sentence to contain more satellites than GGA might indicate since GSV may
 * include satellites that are not used as part of the solution. It is not a
 * requirment that the GSV sentences all appear in sequence. To avoid
 * overloading the data bandwidth some receivers may place the various sentences
 * in totally different samples since each sentence identifies which one it is.
 * 
 * @author NThusitha
 * @date 15-July-2014
 * 
 */
public class GSV implements GPSObject, GPSDataIntegrity {

	private short noOfSentencesForFullData = 0;
	private List<GSVSentence> sentences = new ArrayList<GSVSentence>();

	/*
	 * Check GSV data integrity. (non-Javadoc)
	 * 
	 * @see com.dev.tc.gps.client.endpoint.GPSDataIntegrity#checkIntegrity()
	 */
	@Override
	public boolean checkIntegrity() {
		boolean isOk = false;

		if (getNoOfSentencesForFullData() != 0 && getSentences().size() != 0) {

			if (getNoOfSentencesForFullData() == getSentences().size()) {

				boolean sentenceCheck = false, svCheck = false;
				for (GSVSentence sentence : getSentences()) {
					sentenceCheck = svCheck = false;

					if (sentence.getSentenceNo() > 0
							&& sentence.getSvsInView() > 0) {

						//if (sentence.getSvsInView() == sentence.getSvs().size()) {

							for (SpaceVehicle sv : sentence.getSvs()) {

								if (sv.getPrn() != 0
										&& sv.getElevation() != Integer.MIN_VALUE
										&& sv.getAzimuth() != Integer.MIN_VALUE
										/*&& sv.getSnr() != Integer.MIN_VALUE*/) { //SNR is not mandatory
									svCheck = true;
								} else {
									svCheck = false;
									break;
								}
							}

							if (svCheck) {
								sentenceCheck = true;
							} else {
								break;
							}
						//}
					}
				}
				if (sentenceCheck)
					isOk = true;
			}
		}

		return isOk;
	}

	public List<GSVSentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<GSVSentence> sentences) {
		this.sentences = sentences;
	}

	/**
	 * Add sentence
	 * 
	 * @param sentence
	 */
	public void addSentence(final GSVSentence sentence) {
		this.getSentences().add(sentence);
	}

	public short getNoOfSentencesForFullData() {
		return noOfSentencesForFullData;
	}

	public void setNoOfSentencesForFullData(short noOfSentencesForFullData) {
		this.noOfSentencesForFullData = noOfSentencesForFullData;
	}

	@Override
	public String toString() {

		StringBuffer sentences_ = new StringBuffer("");

		for (GSVSentence line : getSentences()) {
			sentences_.append(line.toString() + "\n");
		}
		return "GSV [noOfSentencesForFullData=" + noOfSentencesForFullData
				+ ", sentences=" + sentences_ + "]";
	}

	/**
	 * One GSV sentence only can provide data for up to 4 satellites and thus
	 * there may need to be 3 sentences for the full information
	 * 
	 * @author NThusitha
	 * @date 15-July-2014
	 * 
	 */
	public static class GSVSentence {
		// sv - space vehicle
		private short sentenceNo = 0;
		private short svsInView = 0;
		private List<SpaceVehicle> svs = new ArrayList<SpaceVehicle>();

		public short getSentenceNo() {
			return sentenceNo;
		}

		public void setSentenceNo(short sentenceNo) {
			this.sentenceNo = sentenceNo;
		}

		public short getSvsInView() {
			return svsInView;
		}

		public void setSvsInView(short svsInView) {
			this.svsInView = svsInView;
		}

		public List<SpaceVehicle> getSvs() {
			return svs;
		}

		public void setSvs(List<SpaceVehicle> svs) {
			this.svs = svs;
		}

		public void addSV(final SpaceVehicle sv) {
			if (sv != null) {
				this.getSvs().add(sv);
			}
		}

		@Override
		public String toString() {

			StringBuffer svs_ = new StringBuffer("");
			for (SpaceVehicle sv : getSvs()) {
				svs_.append(sv.toString() + "\n");
			}
			return "GSVSentence [sentenceNo=" + sentenceNo + ", svsInView="
					+ svsInView + ", svs=" + svs_.toString() + "]";
		}

	}

	/**
	 * Satellite in view
	 * 
	 * @author NThusitha
	 * @date 15-July-2014
	 * 
	 */
	public static class SpaceVehicle {

		private short prn = 0;
		private int elevation = Integer.MIN_VALUE; // degrees
		private int azimuth = Integer.MIN_VALUE; // degrees
		private int snr = Integer.MIN_VALUE; // SNR

		public short getPrn() {
			return prn;
		}

		public void setPrn(short prn) {
			this.prn = prn;
		}

		public int getElevation() {
			return elevation;
		}

		public void setElevation(int elevation) {
			this.elevation = elevation;
		}

		public int getAzimuth() {
			return azimuth;
		}

		public void setAzimuth(int azimuth) {
			this.azimuth = azimuth;
		}

		public int getSnr() {
			return snr;
		}

		public void setSnr(int snr) {
			this.snr = snr;
		}

		@Override
		public String toString() {
			return "SpaceVehicle [prn=" + prn + ", elevation=" + elevation
					+ ", azimuth=" + azimuth + ", snr=" + snr + "]";
		}

	}

}
