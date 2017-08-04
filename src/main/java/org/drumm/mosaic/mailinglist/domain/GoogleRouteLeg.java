package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRouteLeg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextValue distance;
	private TextValue duration;
	private String end_address;
	private GooglePoint end_location;
	private String start_address;
	private GooglePoint start_location;

	public TextValue getDistance() {
		return distance;
	}

	public void setDistance(TextValue distance) {
		this.distance = distance;
	}

	public TextValue getDuration() {
		return duration;
	}

	public void setDuration(TextValue duration) {
		this.duration = duration;
	}

	public String getEnd_address() {
		return end_address;
	}

	public void setEnd_address(String end_address) {
		this.end_address = end_address;
	}

	public GooglePoint getEnd_location() {
		return end_location;
	}

	public void setEnd_location(GooglePoint end_location) {
		this.end_location = end_location;
	}

	public String getStart_address() {
		return start_address;
	}

	public void setStart_address(String start_address) {
		this.start_address = start_address;
	}

	public GooglePoint getStart_location() {
		return start_location;
	}

	public void setStart_location(GooglePoint start_location) {
		this.start_location = start_location;
	}

	@Override
	public String toString() {
		return "GoogleRouteLeg [distance=" + distance + ", duration="
				+ duration + ", end_address=" + end_address + ", end_location="
				+ end_location + ", start_address=" + start_address
				+ ", start_location=" + start_location + "]";
	}
	
}
