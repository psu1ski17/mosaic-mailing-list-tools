package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;

public class GoogleBounds implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GooglePoint northeast;
	private GooglePoint southwest;

	public GooglePoint getNortheast() {
		return northeast;
	}

	public void setNortheast(GooglePoint northeast) {
		this.northeast = northeast;
	}

	public GooglePoint getSouthwest() {
		return southwest;
	}

	public void setSouthwest(GooglePoint southwest) {
		this.southwest = southwest;
	}

	@Override
	public String toString() {
		return "GoogleBounds [northeast=" + northeast + ", southwest="
				+ southwest + "]";
	}
}
