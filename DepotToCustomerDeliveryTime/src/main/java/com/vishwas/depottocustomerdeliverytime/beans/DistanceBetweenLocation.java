package com.vishwas.depottocustomerdeliverytime.beans;

import java.util.Comparator;

public class DistanceBetweenLocation  {

	private Location source;
	private Location destination;
	private Double distance;

	public Location getSource() {
		return source;
	}

	public void setSource(Location source) {
		this.source = source;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/*public int compareTo(DistanceBetweenLocation o) {

		if (distance > o.getDistance())
			return 1;
		if (o.getDistance() == distance)
			return 0;
		else
			return -1;
	}*/

}