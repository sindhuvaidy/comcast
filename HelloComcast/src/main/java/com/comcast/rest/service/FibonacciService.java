package com.comcast.rest.service;

import org.springframework.stereotype.Component;

import com.comcast.rest.data.FibonacciSeries;

@Component
public class FibonacciService {

	public FibonacciSeries getFibonnaciSeries(int fibNumer) {
		//Integer Array sized based on input, to store N fib numbers
        int[] series = new int[fibNumer];
        
        /*
         * For all numbers up to fibNumber fibonacci method is called recursively and 
         * final values are collected in an integer array
         */
        for (int i = 0; i < fibNumer; i++) {
        	series[i] = fibonacci(i);
        }
       
        //constructing return data object
        FibonacciSeries fibSeries = new FibonacciSeries();
        fibSeries.setFibonacciNumber(Integer.toString(fibNumer));
        fibSeries.setFibonacciSeries(series);
        return fibSeries;
	}
	
	/*
	 * This method is called recursively to calculate N fibonnaci numbers
	 */
	public int fibonacci(int num) {

        if (num == 0) {
            return 0;
        }
        else if(num == 1)
        {
            return 1;
        }
     
       return fibonacci(num-1) + fibonacci(num-2);
    }

}
