package com.comcast.rest.data;

import java.util.Arrays;

/*
 * POJO fibonacci response model
 */
public class FibonacciSeries {

	private String fibonacciNumber;
	private int[] fibonacciSeries;
	
	public FibonacciSeries() {
		
	}
	
	public String getFibonacciNumber() {
		return fibonacciNumber;
	}
	public void setFibonacciNumber(String fibonacciNumber) {
		this.fibonacciNumber = fibonacciNumber;
	}
	public int[] getFibonacciSeries() {
		return fibonacciSeries;
	}
	public void setFibonacciSeries(int[] fibonacciSeries) {
		this.fibonacciSeries = fibonacciSeries;
	}
	
	@Override
	public String toString() {
		return "FibonacciSeries{" +
				"number='" + fibonacciNumber + '\'' +
                ", series=" + Arrays.toString(fibonacciSeries) +
                "}\n";
	}
}
