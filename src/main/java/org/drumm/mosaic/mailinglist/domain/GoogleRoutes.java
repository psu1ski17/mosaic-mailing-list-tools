package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRoutes implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<GoogleRouteLeg> legs;
	private String copyrights;
	private GoogleBounds bounds;

	public List<GoogleRouteLeg> getLegs() {
		return legs;
	}

	public void setLegs(List<GoogleRouteLeg> legs) {
		this.legs = legs;
	}

	public String getCopyrights() {
		return copyrights;
	}

	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}

	public GoogleBounds getBounds() {
		return bounds;
	}

	public void setBounds(GoogleBounds bounds) {
		this.bounds = bounds;
	}

	@Override
	public String toString() {
		return "GoogleRoutes [legs=" + legs + ", copyrights=" + copyrights
				+ ", bounds=" + bounds + "]";
	}
}
