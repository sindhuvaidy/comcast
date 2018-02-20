package com.comcast.rest.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.comcast.rest.data.Customer;
import com.comcast.rest.exception.ComcastDataException;

/*
 * Service class performing DB operations - INSERT, SELECT, DELETE
 */
@Service
public class CustomerService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * Creates customer record in comcast_user table.
	 */
	public int addCustomer(Customer customer) throws ComcastDataException {
		String sql = "INSERT INTO comcast_customer(first_name, last_name, customer_id, email) VALUES(?,?,?,?)";
		try {
			return jdbcTemplate.update(sql,customer.getFirstName(),customer.getLastName(),customer.getCustomerId(),customer.getEmail());
		} catch(DuplicateKeyException e) {
			throw new ComcastDataException(e.getMessage());
		}
		
	}
	
	/*
	 * Queries customer by uniques ID from comcast_user table.
	 */
	public Customer getCustomerById(String customerId) throws ComcastDataException {
		String sql = "SELECT * FROM comcast_customer WHERE customer_id=?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {customerId}, new RowMapper<Customer>() {

				@Override
				public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Customer customer = new Customer();
					customer.setCustomerId(rs.getString("customer_id"));
					customer.setFirstName(rs.getString("first_name"));
					customer.setLastName(rs.getString("last_name"));
					customer.setEmail(rs.getString("email"));
					
					return customer;
				}
				
			});
		} catch(EmptyResultDataAccessException e) {
			throw new ComcastDataException(e.getMessage());
		}
		
	}
	
	/*
	 * Deletes customer by unique ID from comcast_customer table.
	 */
	public int deleteCustomerbyId(String id) {
		String sql = "DELETE FROM comcast_customer WHERE customer_id = ?";
		return jdbcTemplate.update(sql,id);
	}
}
