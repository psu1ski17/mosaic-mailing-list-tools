package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;

public class GooglePoint implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double lat;
	public double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public GooglePoint() {
	}

	@Override
	public String toString() {
		return "GooglePoint [lat=" + lat + ", lng=" + lng + "]";
	}
}