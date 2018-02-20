package com.comcast.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.comcast.rest.service.CustomerService;

@SpringBootApplication
public class HelloComcastApplication{

	@Autowired 
	CustomerService customerService;
	
	public static void main(String[] args) {
		SpringApplication.run(HelloComcastApplication.class, args);	
	}
}
