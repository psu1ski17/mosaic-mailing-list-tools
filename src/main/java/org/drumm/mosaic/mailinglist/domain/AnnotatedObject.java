package org.drumm.mosaic.mailinglist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnotatedObject extends CcbObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int distanceElkridgeMeters;
	protected int distanceArundelMeters;
	protected long distanceElkridgeMillis;
	protected long distanceArundelMillis;
	protected String googleAddress;

	public AnnotatedObject(CcbObject entry) {
		this.Campus = entry.getCampus();
		this.familyPrimaryContact = entry.getFamilyPrimaryContact();
		this.firstName = entry.getFirstName();
		this.lastName = entry.getLastName();
		this.mailingCity = entry.getMailingCity();
		this.mailingState = entry.getMailingState();
		this.mailingStreet = entry.getMailingStreet();
		this.mailingZip = entry.getMailingZip();
	}

	@JsonProperty("Shortest Distance")
	public int getShortestDistance() {
		return (distanceArundelMeters < distanceElkridgeMeters ? distanceArundelMeters
				: distanceElkridgeMeters);
	}

	@JsonProperty("Shortest Drive Time")
	public long getShortestDriveTime() {
		return (distanceArundelMillis < distanceElkridgeMillis ? distanceArundelMillis
				: distanceElkridgeMillis);
	}

	@JsonProperty("Distance Elkridge")
	public int getDistanceElkridgeMeters() {
		return distanceElkridgeMeters;
	}

	@JsonProperty("Distance Elkridge")
	public void setDistanceElkridgeMeters(int distanceElkridgeMeters) {
		this.distanceElkridgeMeters = distanceElkridgeMeters;
	}

	@JsonProperty("Distance Arundel")
	public int getDistanceArundelMeters() {
		return distanceArundelMeters;
	}

	@JsonProperty("Distance Arundel")
	public void setDistanceArundelMeters(int distanceArundelMeters) {
		this.distanceArundelMeters = distanceArundelMeters;
	}

	@JsonProperty("Drive Time Elkridge")
	public long getDistanceElkridgeMillis() {
		return distanceElkridgeMillis;
	}

	@JsonProperty("Drive Time Elkridge")
	public void setDistanceElkridgeMillis(long distanceElkridgeMillis) {
		this.distanceElkridgeMillis = distanceElkridgeMillis;
	}

	@JsonProperty("Drive Time Arundel")
	public long getDistanceArundelMillis() {
		return distanceArundelMillis;
	}

	@JsonProperty("Drive Time Arundel")
	public void setDistanceArundelMillis(long distanceArundelMillis) {
		this.distanceArundelMillis = distanceArundelMillis;
	}

	@JsonProperty("Google Address")
	public String getGoogleAddress() {
		return googleAddress;
	}

	@JsonProperty("Google Address")
	public void setGoogleAddress(String GoogleAddress) {
		this.googleAddress = googleAddress;
	}

}
