package com.malaya.istock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.malaya.istock.model.Candlestick;
import com.malaya.istock.model.ScripPrice;
import com.malaya.istock.model.StockPerf;
import com.malaya.istock.service.ExcelService;
import com.malaya.istock.service.HelperService;
import com.malaya.istock.utility.PropertyReader;

public class App {
    public static void main(String[] args) {
 
		ExcelService excel = new ExcelService();
		String filePath = excel.generateExcelFile();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		Map<String,String> stocks = new HashMap<String,String>();
		try {
			stocks = PropertyReader.readPropertiesAsMap();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (Map.Entry<String,String> entry : stocks.entrySet()) {
			String symbol=entry.getKey();
			String name=entry.getValue();
			System.out.println("symbol = " + symbol +
                             ", name = " + name);
			Long startEpoch = HelperService.getSpecificDate(200);
			Long endEpoch = HelperService.getSpecificDate(0);
			
			List<ScripPrice> list = new ArrayList<>();
			try {
				list = HelperService.getStockHistory(symbol, startEpoch, endEpoch);
			
				System.out.println(objectMapper.writeValueAsString(list));
				
				
				
				Candlestick[] candlesticks = list.stream()
		                .map(p -> new Candlestick(p.getTime(), p.getOpen(), p.getClose()))
		                .toArray(Candlestick[] :: new);
		                
		                //.collect(Collectors.toList());
				
				double[] d = HelperService.calculateRSIValues(candlesticks, 14);
				
				//for (int i=0; i<d.length; i++)
		          //  System.out.println(d[i] + " ");
				
				excel.updateExistingExcelFile(filePath, symbol, name, candlesticks[candlesticks.length-1].getClose(), d);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}
}
//StockEntity [time=1620950400, date=2021-05-14, open=25.15, high=25.6, low=24.75, close=25.2, volume=1246277]

//https://www.equitymaster.com/charting/chartingdata/search?limit=30&query=&type=&exchange=