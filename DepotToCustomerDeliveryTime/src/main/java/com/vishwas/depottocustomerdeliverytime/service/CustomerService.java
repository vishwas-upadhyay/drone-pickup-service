package com.vishwas.depottocustomerdeliverytime.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.vishwas.depottocustomerdeliverytime.beans.DistanceBetweenLocation;
import com.vishwas.depottocustomerdeliverytime.beans.Location;


/**
 * This class consists exclusively of public methods that operates on Location object,Double 
 * and return List <DistanceBetweenLocation> Double and String.This class has implemented
 * Haversine formula to calculate distance between two locations (Latitude and longitude).
 * reference : https://en.wikipedia.org/wiki/Haversine_formula 
 *
 * <p>
 * The methods of this class all throw a <tt>NullPointerException</tt> if the
 * collections or class objects provided to them are null.
 * </p>
 *
 * This class is member of Drone Pick-up service POC.
 *
 * @author Vishwas Upadhyay
 * @see List
 * @see Collection
 * @see Math
 * 
 *
 */
@Service
public class CustomerService {
    /**
     *Earth radius and Drone speed has been captured in final varible.
     */
	public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
	public final static double DRONE_SPEED_KM_PER_HOUR = 60;
	final static Logger LOGGER = Logger.getLogger(CustomerService.class);
	
	/**This method provides List of all possible combination with given locations
	 * along with distance between them.
	 * @param source List<Location>
	 * @param destination List<Location>
	 * @return DistanceBetweenLocation list
	 */
	public List<DistanceBetweenLocation> getLocationsDistance(List<Location> source,
			List<Location> destination) {
		LOGGER.info("CustomerService's getShortestDistance method has begun.");
		List<DistanceBetweenLocation> result = new ArrayList<DistanceBetweenLocation>();
		for (int i = 0; i < source.size(); i++) {
			for (int j = 0; j < destination.size(); j++) {
				DistanceBetweenLocation obj = new DistanceBetweenLocation();
				double distance = calculateDistanceInKilometer(source.get(i).getLatitude(),
						source.get(i).getLongitude(), destination.get(j).getLatitude(),
						destination.get(j).getLongitude());
				obj.setDistance(distance);
				obj.setDestination(destination.get(j));
				obj.setSource(source.get(i));
				result.add(obj);
			}
		}
		LOGGER.info("CustomerService's getShortestDistance method has ended.");
		return result;
	}

	/**
	 * This method provides distance in KM between two earth points considered that points
	 *  Coordinates as in latitudes and longitudes supplied. 
	 * 
	 * This mehtod is implementation of Haversine formula.More information can be found in 
	 * below link:https://en.wikipedia.org/wiki/Haversine_formula
	 * 
	 * @param lat1 double
	 * @param lng1 double
	 * @param lat2 double
	 * @param lng2 double
	 * @return
	 */
	public double calculateDistanceInKilometer(double lat1, double lng1, double lat2, double lng2) {
		LOGGER.info("CustomerService's calculateDistanceInKilometer method has begun.");
		double latDistance = Math.toRadians(lat1 - lat2);
		double lngDistance = Math.toRadians(lng1 - lng2);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		LOGGER.info("CustomerService's calculateDistanceInKilometer method has ended.");
		return AVERAGE_RADIUS_OF_EARTH_KM * c;
	}

	/** 
	 * This method provides travel time in minutes:seconds for given distance in kilometer and speed KMH.
	 * The speed of Drone has consider as fixed for now but it can we
	 * @param distance
	 * @return
	 */
	public String timeToDeliveryInMinutes(double distance) {
		LOGGER.info("CustomerService's timeToDeliveryInMinutes method has begun.");
		String timeStamp;
		double timeInMinutes = (distance / DRONE_SPEED_KM_PER_HOUR) * 60;
		BigDecimal bd = new BigDecimal(timeInMinutes - Math.floor(timeInMinutes));
		bd = bd.setScale(2, RoundingMode.HALF_DOWN);
		Double obj = new Double(bd.toString());
		Double d = (double) Math.round(obj * 60);
		Double e = (double) Math.floor(timeInMinutes);
		timeStamp = Math.round(e) + "minutes " + Math.round(d) + "seconds";
		LOGGER.info("CustomerService's timeToDeliveryInMinutes method has ended.");
		return timeStamp;
	}

}
