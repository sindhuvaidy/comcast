package com.comcast.rest.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.comcast.rest.data.Customer;
import com.comcast.rest.data.ExternalData;
import com.comcast.rest.data.FibonacciSeries;
import com.comcast.rest.data.Welcome;
import com.comcast.rest.exception.ComcastDataException;
import com.comcast.rest.service.CustomerService;
import com.comcast.rest.service.FibonacciService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(value = {"/comcast"})
public class ComcastController {
	
	@Autowired
	FibonacciService fibonacciService;
	
	@Autowired
	CustomerService customerService;
	
	private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

	/*
	 * REST end point thats welcomes "Hello World".
	 * 
	 */
	
	@RequestMapping(value = {"/welcome", "/welcome/{name}"}, method=RequestMethod.GET)
    public @ResponseBody Welcome sayHello(@PathVariable Optional<String> name) {
				
		if(name!=null && name.isPresent()) {
			return new Welcome("Hello, "+name.get());
		} else {
			return new Welcome("Hello, World");
		}
        
    }
	
	/*
	 * Fibonacci REST endpoint that Accepts a number, N, 
	 * and returns a JSON array with the first N Fibonacci numbers.
	 * Utilized Recursive method.
	 */
	@GetMapping(value = {"/fibonacci/{fibNumber}"})
	public @ResponseBody ResponseEntity<?> getFibonacciSeries(@PathVariable (value="fibNumber") String fibNumber) {
		
		final ObjectNode responseStatus = OBJECTMAPPER.createObjectNode();
		
		int fibNumer = Integer.valueOf(fibNumber);
		
		if ( fibNumer <= 0) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Input cannot be less than or equal to zero"),HttpStatus.BAD_REQUEST);
        }
		
		FibonacciSeries series = fibonacciService.getFibonnaciSeries(fibNumer);
		return new ResponseEntity<FibonacciSeries>(series,HttpStatus.OK);
	}
	
	/*
	 * REST endpoint that Creates/Inserts Customer in DB.
	 */
	@PostMapping(value = {"/addcustomer"})
	public ResponseEntity<ObjectNode> addCustomer(@RequestBody Customer customer) {
		
		int success = 0;
		ResponseEntity<ObjectNode> responseEntity = null;
		final ObjectNode responseStatus = OBJECTMAPPER.createObjectNode();
		
		try {
			success = customerService.addCustomer(customer);
		} catch(ComcastDataException e) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Customer ID already Exists. Choose different ID"),HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Unable to Process request"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(success > 0) {
			responseEntity = new ResponseEntity<ObjectNode>(responseStatus.put("SUCCESS", "Customer successfully added"), HttpStatus.OK);
		} 
		return responseEntity;
	}
	
	/*
	 * REST endpoint that Retrieves customer deatils based on customer id.
	 */
	@GetMapping(value = {"/customerdetails/{ID}"})
	public @ResponseBody ResponseEntity<?> getCustomerById(@PathVariable (value="ID") String id) {
		
		Customer customer = null;
		final ObjectNode responseStatus = OBJECTMAPPER.createObjectNode();
		
		try {
			customer = customerService.getCustomerById(id);
		} catch(ComcastDataException e) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Customer ID doesn't exist"),HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Unable to Process request"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Customer>(customer,HttpStatus.OK);
	}
	
	/*
	 * REST endpoint that Deletes customer records based on customer id.
	 */
	
	@DeleteMapping(value = {"/delete/{ID}"})
	public ResponseEntity<?> deleteCustomerById(@PathVariable (value="ID") String id) {
		
		final ObjectNode responseStatus = OBJECTMAPPER.createObjectNode();
		
		try {
			customerService.deleteCustomerbyId(id);
		} catch (Exception e) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Unable to Process request"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Customer successfully deleted", HttpStatus.OK);
	}
	
	/*
	 * REST endpoint that queries an external REST service using Spring Rest Template.
	 */
	
	@GetMapping(value = {"/external"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> callExternalService(){
		final ObjectNode responseStatus = OBJECTMAPPER.createObjectNode();
		ExternalData[] externalData;
		try {
			final String uri = "https://jsonplaceholder.typicode.com/posts";
			RestTemplate restTemplate = new RestTemplate();
			externalData = restTemplate.getForObject(uri, ExternalData[].class);
		} catch(HttpStatusCodeException e) {
			return new ResponseEntity<ObjectNode>(responseStatus.put("ERROR", "Get Failed with HttpStatusCode: "+e.getStatusCode()),e.getStatusCode());
		}
		return new ResponseEntity<ExternalData[]>(externalData, HttpStatus.OK);
	}
	
	/*
	 * REST endpoint that creates two threads that become deadlocked with each other.
	 * REST function to monitor the two threads and detect the deadlock
	 */
	@GetMapping(value = {"/detectdeadlock"})
	public void deadLock() throws InterruptedException{
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		
		final Object lock1 = new Object();
	    final Object lock2 = new Object();
	    Future future1 = service.schedule(new Runnable() {
	        @Override public void run() {
	            synchronized (lock1) {
	                System.out.println("Thread1 acquired lock1");
	                try {
	                    TimeUnit.MILLISECONDS.sleep(50);
	                } catch (InterruptedException ignore) {}
	                synchronized (lock2) {
	                    System.out.println("Thread1 acquired lock2");
	                }
	            }
	        }
	 
	    }, 1, TimeUnit.SECONDS);
	    Future future2 = service.schedule(new Runnable() {
	        @Override 
	        public void run() {
	            synchronized (lock2) {
	                System.out.println("Thread2 acquired lock2");
	                try {
	                    TimeUnit.MILLISECONDS.sleep(50);
	                } catch (InterruptedException ignore) {}
	                synchronized (lock1) {
	                    System.out.println("Thread2 acquired lock1");
	                }
	            }
	        }
	    }, 1, TimeUnit.SECONDS);
	     
	 
	    // Wait a little for threads to deadlock.
	    try {
	        TimeUnit.SECONDS.sleep(2);
	    } catch (InterruptedException ignore) {}
	    
	    future1.cancel(true);
	    future2.cancel(true);
	    service.shutdown();
	    service.awaitTermination(5, TimeUnit.SECONDS);
	    detectDeadLock();
	}
	
	private int detectDeadLock() {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
	    long[] threadIds = threadBean.findDeadlockedThreads();
	    int deadlockedThreads = threadIds != null? threadIds.length : 0;
	    System.out.println("Number of deadlocked threads: " + deadlockedThreads);
	    return deadlockedThreads;
	}
	
}
