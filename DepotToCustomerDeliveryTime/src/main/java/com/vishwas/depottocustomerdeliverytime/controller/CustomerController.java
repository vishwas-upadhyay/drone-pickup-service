package com.vishwas.depottocustomerdeliverytime.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.vishwas.depottocustomerdeliverytime.beans.DistanceBetweenCustomerToDepot;
import com.vishwas.depottocustomerdeliverytime.beans.DistanceBetweenLocation;
import com.vishwas.depottocustomerdeliverytime.beans.Location;
import com.vishwas.depottocustomerdeliverytime.service.CustomerService;

/**
 * This class consists exclusively of Rest APIs that operate on Location object
 * and return DistanceBetweenCustomerToDepot object.
 *
 * <p>
 * The methods of this class all throw a <tt>NullPointerException</tt> if the
 * collections or class objects provided to them are null.Also @ControllerAdvise
 * is default exception handler for this class hence no customize exception
 * handler available.</p>
 *
 * This class is member of Drone Pick-up service POC.
 *
 * @author Vishwas Upadhyay
 * @see CostumerService
 * @see List
 * @see Collection
 * 
 *
 */
@Controller
public class CustomerController {
	/**
	 * CustomerService dependency injected here to get the basic services for
	 * CustomerController class.
	 */
	@Autowired
	private CustomerService customerService;

	// Logger has been setup here to log all activity in myapplication.log file
	final static Logger LOGGER = Logger.getLogger(CustomerController.class);
	/**
	 * The below lists are temporary to hold the drone depots and Stores
	 * information. This could be stored in some database for actual implementation.
	 *
	 */
	private static List<Location> depots = Arrays.asList(
			new Location(51.235047, 6.825566, "Metrostrasse 12, 40235 Düsseldorf"),
			new Location(51.237172, 6.724178, "Am Albertussee 1, 40549 Düsseldorf"));
	private static List<Location> stores = Arrays.asList(
			new Location(51.237810, 6.719590, "Schiessstraße 31, 40549 Düsseldorf"),
			new Location(51.209180, 6.778798, "Friedrichstraße 152, 40217 Düsseldorf"),
			new Location(51.202195, 6.718651, "Breslauer Str. 2, 41460 Neuss "),
			new Location(51.231429, 6.685710, "Bataverstraße 93, 41462 Neuss"),
			new Location(51.296702, 6.831470, "Am Sandbach 30, 40878 Ratingen"));

	/**
	 * This method returns the index page for http://<server>:8080. Index page is
	 * starting page for this application.
	 * 
	 * @return index.html
	 * 
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * This method accepts Customer details as in Location object.Compare with all
	 * possible combination of depot to store and store to customer distance and
	 * return shortest path (depot to store to customer) along with travel
	 * time(minutes : seconds) at considered drone speed.
	 * 
	 * @param Location
	 * @return DistanceBetweenCustomerToDepot
	 */
	@RequestMapping(value = "/getPath", method = RequestMethod.POST)
	public ModelAndView getPath(@ModelAttribute Location location) {
		LOGGER.info("Customer Controller has Begun");
		ModelAndView modelAndView = new ModelAndView();
		List<Location> customers = new ArrayList<Location>();
		// Location object received from request has been added to Customers list.
		customers.add(location);
		/**
		 * Below are the Lists which will hold the data for depot to store,store to
		 * customer and depot to customer. This data will be used for finding best route
		 * for customer.
		 *
		 */
		List<DistanceBetweenLocation> nearestDepots = new ArrayList<DistanceBetweenLocation>();
		List<DistanceBetweenLocation> nearestStores = new ArrayList<DistanceBetweenLocation>();
		List<DistanceBetweenCustomerToDepot> shortestDeliveryRoute = new ArrayList<DistanceBetweenCustomerToDepot>();
		/**
		 * Distance between all depots and stores will be calculated using
		 * getLocationsDistance and stored in nearestDepots
		 */
		nearestDepots = customerService.getLocationsDistance(depots, stores);
		/**
		 * The nearestDepots list has sorted by their distance from stores to find out
		 * best store for delivery.
		 */
		Collections.sort(nearestDepots, (d1, d2) -> {
			return d1.getDistance().compareTo(d2.getDistance());
		});
		/**
		 * Distance between customer and all stores will be calculated using
		 * getLocationsDistance and stored in nearestStores
		 */
		nearestStores = customerService.getLocationsDistance(customers, stores);
		/**
		 * The nearestStores list has sorted by their distance from customer to find out
		 * best store for delivery.
		 */
		Collections.sort(nearestStores, (d1, d2) -> {
			return d1.getDistance().compareTo(d2.getDistance());
		});

		/**
		 * Logic for shortest path between customer and depot As we have distance map
		 * for depots to stores in nearestDepots and same as we have distance map of
		 * customer to stores in nearestStore, hence we are processing this already
		 * sorted data on their common destination to get all possible routes from depot
		 * to store to customer.
		 */
		for (int i = 0; i < nearestDepots.size(); i++) {
			for (int j = 0; j < nearestStores.size(); j++) {
				if (nearestDepots.get(i).getDestination().getName().trim()
						.equalsIgnoreCase(nearestStores.get(j).getDestination().getName())) {
					DistanceBetweenCustomerToDepot obj = new DistanceBetweenCustomerToDepot();
					obj.setDepot(nearestDepots.get(i).getSource());
					obj.setCustomer(nearestStores.get(j).getSource());
					obj.setStore(nearestStores.get(j).getDestination());
					obj.setDistance(nearestDepots.get(i).getDistance(), nearestStores.get(j).getDistance());
					obj.setTime(customerService.timeToDeliveryInMinutes(obj.getDistance()));
					shortestDeliveryRoute.add(obj);
				}

			}

		}
		/**
		 * After getting all possible routes for customer,let's find out shortest path
		 * by sorting it over distance.
		 */
		Collections.sort(shortestDeliveryRoute, (d1, d2) -> {
			return d1.getDistance().compareTo(d2.getDistance());
		});
		// Logging all the routes for customer in ascending order
		shortestDeliveryRoute.forEach(d -> LOGGER.info("Depot :-->" + d.getDepot().getName() + " Store :-->"
				+ d.getStore().getName() + "  Customer:-->" + d.getCustomer().getName() + "Distance :-->"
				+ d.getDistance() + " Time to deliver:-->" + d.getTime()));
		// Setting view page and model for response to client.
		modelAndView.setViewName("depot-to-customer");
		// If two routes has same distance then we are considering only first one.
		modelAndView.addObject("depotToCustomer", shortestDeliveryRoute.get(0));
		LOGGER.info("Customer Controller has ended");
		return modelAndView;
	}

}
