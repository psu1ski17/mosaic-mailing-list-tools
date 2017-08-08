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

	public long findDriveTimeSeconds() {
		if (validateLeg()) {
			TextValue dur = this.getRoutes().get(0).getLegs().get(0)
					.getDuration();
			if (dur != null) {
				return dur.getValue();
			}
		}
		return -1;
	}

	public int findDistanceMeters() {
		if (validateLeg()) {
			TextValue dist = this.getRoutes().get(0).getLegs().get(0)
					.getDistance();
			if (dist != null) {
				return dist.getValue();
			}
		}
		return -1;
	}

	public String findStartAddress() {
		if (validateLeg()) {
			return this.getRoutes().get(0).getLegs().get(0).getStart_address();
		} else {
			return null;
		}
	}

	private boolean validateLeg() {
		if (this.getRoutes() != null && this.getRoutes().size() > 0) {
			GoogleRoutes route = this.getRoutes().get(0);
			if (route != null && route.getLegs() != null
					&& route.getLegs().size() > 0) {
				GoogleRouteLeg leg = route.getLegs().get(0);
				return leg != null;
			}
		}
		return false;
	}

	public String findEndAddress() {
		if (validateLeg()) {
			return this.getRoutes().get(0).getLegs().get(0).getEnd_address();
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "GoogleDirectionsDto [geocoded_waypoints=" + geocoded_waypoints
				+ ", routes=" + routes + ", error_message=" + error_message
				+ ", status=" + status + "]";
	}

}
