package com.malaya.istock.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
	public static void main(String[] args) {
	    List<Double> latencies = new ArrayList<Double>();
	    
	    
	    //More
	    /*latencies.add(6246.0);
	    latencies.add(8161.0);
	    latencies.add(7168.0);
	    latencies.add(7038.0);
	    latencies.add(6186.0);
	    latencies.add(6182.0);
	    latencies.add(4957.0);
	    latencies.add(9437.0);
	    latencies.add(6934.0);
	    latencies.add(6849.0);*/
	    
	    //Less
	    latencies.add(762.81);
	    latencies.add(868.33);
	    latencies.add(879.29);
	    latencies.add(926.87);
	    latencies.add(1136.32);
	    latencies.add(1163.86);
	    latencies.add(1246.32);
	    latencies.add(1300.76);
	    latencies.add(1344.36);
	    latencies.add(1396.8);

	    
	    System.out.println(findAverage(latencies,25,"Less"));
	    System.out.println(findAverage(latencies, 75,"Less"));
	    System.out.println(findAverage(latencies, 100,"All"));
	    
	    //System.out.println(percentile(latencies, 50));
	    
	}

	public static Double findAverage(List<Double> latencies, double percentile,String sortOrder) {
	   System.out.println("==============================================================================");
		Collections.sort(latencies);
	    if(sortOrder.contentEquals("More")) {
	    	Collections.reverse(latencies);
	    }
	    
	    int index = (int) Math.ceil(percentile / 100.0 * latencies.size());
	    Double percentileValue = latencies.get(index-1); 
	    System.out.println("percentile:"+percentileValue);
	    
	    
	    List<Double> result = new ArrayList<Double>();  
	    
	    switch (sortOrder) {
		case "More":
			if(percentile == 25) {
				result = latencies.stream()
				        .filter(val -> val.doubleValue() >= percentileValue)
				        .collect(Collectors.toList());
			} else {
				result = latencies.stream()
				        .filter(val -> val.doubleValue() <= percentileValue)
				        .collect(Collectors.toList());
			}
			break;
		case "Less":
			if(percentile == 25) {
				result = latencies.stream()
				        .filter(val -> val.doubleValue() <= percentileValue)
				        .collect(Collectors.toList());
			} else {
				result = latencies.stream()
				        .filter(val -> val.doubleValue() >= percentileValue)
				        .collect(Collectors.toList());
			}
			break;
		default:
			result.addAll(latencies);
			break;
		}
	    
		System.out.println(result);
		
	    return result.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0); 
	}
	
	public static Double percentile(List<Double> latencies, double percentile) {
	    Collections.sort(latencies);
	    Collections.reverse(latencies);
	    //System.out.println(latencies);
	    //System.out.println();
	    int index = (int) Math.ceil(percentile / 100.0 * latencies.size());
	    return latencies.get(index-1);
	}
}
