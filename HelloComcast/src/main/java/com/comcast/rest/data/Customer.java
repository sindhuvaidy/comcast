package com.comcast.rest.data;

/*
 * POJO Customer model
 */
public class Customer {
	private String firstName;
	private String lastName;
	private String customerId;
	private String email;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Customer{" +
				"CustomerId = " + this.customerId + ", "+
				"FirstName = " + this.firstName + ", "+
				"LastName = " + this.lastName + ", "+
				"eMail = " + this.email + "}";
	}
}
