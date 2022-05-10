package com.svs.farm_app.entities;

public class TransportModes {

	private String transportModeId, transportMode;

	public TransportModes(String transportModeId, String transportMode) {

		this.transportModeId = transportModeId;
		this.transportMode = transportMode;
	}

	public TransportModes() {
	}

	public String getTransportModeId() {
		return transportModeId;
	}

	public void setTransportModeId(String transportModeId) {
		this.transportModeId = transportModeId;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

}
