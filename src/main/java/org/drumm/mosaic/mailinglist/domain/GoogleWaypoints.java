package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;
import java.util.List;

public class GoogleWaypoints implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String geocoder_status;
	private String place_id;
	private List<String> types;
	private boolean partial_match;

	public boolean isPartial_match() {
		return partial_match;
	}

	public void setPartial_match(boolean partial_match) {
		this.partial_match = partial_match;
	}

	public String getGeocoder_status() {
		return geocoder_status;
	}

	public void setGeocoder_status(String geocoder_status) {
		this.geocoder_status = geocoder_status;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "GoogleWaypoints [geocoder_status=" + geocoder_status
				+ ", place_id=" + place_id + ", types=" + types
				+ ", partial_match=" + partial_match + "]";
	}

}
