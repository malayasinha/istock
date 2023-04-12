package com.malaya.istock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.malaya.istock.model.StockPerf;
import com.malaya.istock.service.ExcelService;
import com.malaya.istock.service.HelperService;
import com.malaya.istock.utility.PropertyReader;

public class App {
    public static void main(String[] args) {
		
		int smaDays = 44;
		int daysForAnalysis = 10;
		int numOfCandlePos = 2;
		List<StockPerf> scripList = new ArrayList<>(); 
				
		Map<String,String> stocks = new HashMap<String,String>();
		try {
			stocks = PropertyReader.readPropertiesAsMap();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Map.Entry<String,String> entry : stocks.entrySet()) {
			String symbol=entry.getKey();
			String name=entry.getValue();
			System.out.println("symbol = " + symbol +
                             ", name = " + name);
			StockPerf sp = null;
			try {
				sp = HelperService.calculatePerformance(symbol,name, smaDays, daysForAnalysis, numOfCandlePos);
				if(ObjectUtils.isNotEmpty(sp.getClosingPrice()))
					scripList.add(sp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		ExcelService es = new ExcelService();
		if(scripList.size()>0) {
			es.createExcelFile(scripList);
		}
		
		
		//StockPerf sp = HelperService.calculatePerformance(symbol, smaDays, daysForAnalysis, numOfCandlePos);
		
		//System.out.println(sp);
		
		
	    
	}
}
//StockEntity [time=1620950400, date=2021-05-14, open=25.15, high=25.6, low=24.75, close=25.2, volume=1246277]

//https://www.equitymaster.com/charting/chartingdata/search?limit=30&query=&type=&exchange=