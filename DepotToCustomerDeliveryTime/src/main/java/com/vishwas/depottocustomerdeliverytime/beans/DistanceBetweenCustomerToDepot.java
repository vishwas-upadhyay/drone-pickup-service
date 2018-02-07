package com.vishwas.depottocustomerdeliverytime.beans;

public class DistanceBetweenCustomerToDepot  {
	private Location depot;
	private Location store;
	private Location customer;
	private Double distance;
	private String time;

	public DistanceBetweenCustomerToDepot() {
	}

	public DistanceBetweenCustomerToDepot(Location depot, Location store, Location customer, Double distance,
			String time) {
		this.depot = depot;
		this.store = store;
		this.customer = customer;
		this.distance = distance;
		this.time = time;

	}

	public Location getDepot() {
		return depot;
	}

	public void setDepot(Location depot) {
		this.depot = depot;
	}

	public Location getStore() {
		return store;
	}

	public void setStore(Location store) {
		this.store = store;
	}

	public Location getCustomer() {
		return customer;
	}

	public void setCustomer(Location customer) {
		this.customer = customer;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double depotDistance, Double customerDistance) {
		this.distance = depotDistance + customerDistance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {

		this.time = time;
	}

	/*public int compareTo(DistanceBetweenCustomerToDepot o) {
		if (distance > o.getDistance())
			return 1;
		if (o.getDistance() == distance)
			return 0;
		else
			return -1;
	}*/

}
