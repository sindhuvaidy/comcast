package com.comcast.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.comcast.rest.data.Customer;
import com.comcast.rest.data.FibonacciSeries;
import com.comcast.rest.data.Welcome;
import com.comcast.rest.exception.ComcastDataException;
import com.comcast.rest.service.CustomerService;
import com.fasterxml.jackson.databind.node.ObjectNode;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/applicationcontext.xml"})
public class ComcastControllerTest extends TestCase {
	
	@Autowired
	ComcastController comcastController;
	
	@Test
	public void testGetFibonacciSeries() {
		ResponseEntity<?> response = comcastController.getFibonacciSeries("4");
		int[] series = {0,1,1,2};
		assertThat(response.getStatusCode(),is(HttpStatus.OK));
		assertThat(((FibonacciSeries) response.getBody()).getFibonacciSeries(),is(series));
	}
	
	@Test
	public void testGetFibonacciSeriesWithInvalidNumber() {
		ResponseEntity<?> response = comcastController.getFibonacciSeries("-4");
		assertThat(response.getStatusCode(),is(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void testSayHelloWithName() {
		Optional<String> opt = Optional.of("comcast");
		Welcome welcome = comcastController.sayHello(opt);
		assertThat(welcome.getMessage(), is("Hello, comcast"));
	}

	@Test
	public void testSayHelloWithNoName() {
	    Optional<String> opt = Optional.ofNullable(null);
		Welcome welcome = comcastController.sayHello(opt);
		assertThat(welcome.getMessage(), is("Hello, World"));
	}
	
	@Test
	public void testAddCustomer() throws ComcastDataException{
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.addCustomer(any())).thenReturn(1);
		comcastController.customerService = customerService;
		ResponseEntity<ObjectNode> response = comcastController.addCustomer(new Customer());
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	public void testAddCustomerExists() throws ComcastDataException{
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.addCustomer(any())).thenThrow(new ComcastDataException("Id exists"));
		comcastController.customerService = customerService;
		ResponseEntity<ObjectNode> response = comcastController.addCustomer(new Customer());
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void testAddCustomerWithServerError() throws ComcastDataException{
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.addCustomer(any())).thenThrow(new RuntimeException());
		comcastController.customerService = customerService;
		ResponseEntity<ObjectNode> response = comcastController.addCustomer(new Customer());
		assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	@Test
	public void testGetCustomerById() throws ComcastDataException{
		Customer customer = new Customer();
		customer.setCustomerId("002");
		customer.setEmail("xx@xx.com");
		customer.setFirstName("Henry");
		customer.setLastName("Rang");
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.getCustomerById(any())).thenReturn(customer);
		comcastController.customerService = customerService;
		ResponseEntity<?> response = comcastController.getCustomerById(any());
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(((Customer)response.getBody()).getCustomerId(),is("002"));
	}
	
	@Test
	public void testGetCustomerByIdWithNonExistingId() throws ComcastDataException{
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.getCustomerById(any())).thenThrow(new ComcastDataException("Id doesn't exists"));
		comcastController.customerService = customerService;
		ResponseEntity<?> response = comcastController.getCustomerById(any());
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testGetCustomerByIdWithServerError() throws ComcastDataException{
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.getCustomerById(any())).thenThrow(new RuntimeException());
		comcastController.customerService = customerService;
		ResponseEntity<?> response = comcastController.getCustomerById(any());
		assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	@Test
	public void testDeleteCustomerById(){
		CustomerService customerService = mock(CustomerService.class);
		when(customerService.deleteCustomerbyId(any())).thenReturn(1);
		comcastController.customerService = customerService;
		ResponseEntity<?> response = comcastController.deleteCustomerById(any());
		assertThat(response.getStatusCode(),is(HttpStatus.OK));
	}

}
