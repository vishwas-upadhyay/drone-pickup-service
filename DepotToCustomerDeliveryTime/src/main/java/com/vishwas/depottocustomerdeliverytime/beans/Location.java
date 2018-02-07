package com.vishwas.depottocustomerdeliverytime.beans;

public class Location {

	private Double latitude;
	private Double longitude;
	private String name;

	public Location() {
	}

	public Location(Double latitude, Double longitude, String name) {

		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
