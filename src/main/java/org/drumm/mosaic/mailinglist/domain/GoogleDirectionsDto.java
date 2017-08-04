package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;
import java.util.List;

public class GoogleDirectionsDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<GoogleWaypoints> geocoded_waypoints;
	private List<GoogleRoutes> routes;
	private String error_message;
	private String status;

	public List<GoogleWaypoints> getGeocoded_waypoints() {
		return geocoded_waypoints;
	}

	public void setGeocoded_waypoints(List<GoogleWaypoints> geocoded_waypoints) {
		this.geocoded_waypoints = geocoded_waypoints;
	}

	public List<GoogleRoutes> getRoutes() {
		return routes;
	}

	public void setRoutes(List<GoogleRoutes> routes) {
		this.routes = routes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	@Override
	public String toString() {
		return "GoogleDirectionsDto [geocoded_waypoints=" + geocoded_waypoints
				+ ", routes=" + routes + ", error_message=" + error_message
				+ ", status=" + status + "]";
	}

}
