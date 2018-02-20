package com.comcast.rest.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * POJO class corresponding to JSON response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalData {
	private int userId;
	private int id;
	private String title;
	private String body;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "UserId: "+this.userId+", id: "+this.id+", title: "+this.title+", body: "+this.body;
	}
}
