package com.malaya.istock.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.malaya.istock.model.ScripPrice;
import com.malaya.istock.model.StockPerf;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HelperService {
	private static Logger logger = Logger.getLogger(HelperService.class);

	public static LocalDate getDateFromEpoch(Long epochTime) {
		// return new Timestamp(epochTime*1000L);
		LocalDate date = Instant.ofEpochMilli(epochTime * 1000L).atZone(ZoneId.systemDefault()).toLocalDate();
		return date;
	}

	public static Long getEpochFromDate(LocalDate date) {
		ZoneId zoneId = ZoneId.systemDefault();
		Long epoch = date.atStartOfDay(zoneId).toEpochSecond();

		return epoch;
	}

	public static List<ScripPrice> getStockHistory(String symbol, Long startEpoch, Long endEpoch) {

		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = null;
		HttpGet httpGet = null;
		try {
			builder = new URIBuilder("https://www.equitymaster.com/charting/chartingdata/marks/");
			builder.setParameter("symbol", symbol).setParameter("from", startEpoch.toString())
					.setParameter("to", endEpoch.toString()).setParameter("resolution", "D");

			httpGet = new HttpGet(builder.build());

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}

		JSONObject obj = new JSONObject(sb.toString());
		ScripPrice entity = new ScripPrice();
		ArrayList<ScripPrice> list = new ArrayList<ScripPrice>();

		try {
			JSONArray timeData = obj.getJSONArray("t");
			JSONArray highData = obj.getJSONArray("h");
			JSONArray lowData = obj.getJSONArray("l");
			JSONArray closeData = obj.getJSONArray("c");
			JSONArray openData = obj.getJSONArray("o");
			JSONArray volData = obj.getJSONArray("v");

			for (int i = 0; i < timeData.length(); i++) {
				entity = new ScripPrice();
				entity.setTime(timeData.getLong(i));
				entity.setDate(HelperService.getDateFromEpoch(timeData.getLong(i)));
				entity.setHigh(highData.getDouble(i));
				entity.setLow(lowData.getDouble(i));
				entity.setClose(closeData.getDouble(i));
				entity.setOpen(openData.getDouble(i));
				entity.setVolume(volData.getLong(i));
				// System.out.println(entity);
				list.add(entity);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return list;
	}

	public static Long getSpecificDate(Integer days) {
		LocalDate date = LocalDate.now().minusDays(days);

		ZoneId zoneId = ZoneId.systemDefault();
		Long epoch = date.atStartOfDay(zoneId).toEpochSecond();

		//System.out.println(date);
		// System.out.println(epoch);

		return epoch;
	}

	public static List<ScripPrice> getStockListOld(String symbol, Long startEpoch, Long endEpoch) {

		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = null;
		try {
			builder = new URIBuilder("http://example.com/");
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		builder.setParameter("parts", "all").setParameter("action", "finish");

		HttpGet httpGet = new HttpGet(
				"https://www.equitymaster.com/charting/chartingdata/marks?symbol=GMRI&from=1613088000&to=1620777600&resolution=D");

		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}

		JSONObject obj = new JSONObject(sb.toString());
		JSONArray timeData = obj.getJSONArray("t");
		JSONArray highData = obj.getJSONArray("h");
		JSONArray lowData = obj.getJSONArray("l");
		JSONArray closeData = obj.getJSONArray("c");
		JSONArray openData = obj.getJSONArray("o");
		JSONArray volData = obj.getJSONArray("v");

		ScripPrice entity = new ScripPrice();
		ArrayList<ScripPrice> list = new ArrayList<ScripPrice>();

		for (int i = 0; i < timeData.length(); i++) {
			entity = new ScripPrice();
			entity.setTime(timeData.getLong(i));
			entity.setDate(HelperService.getDateFromEpoch(timeData.getLong(i)));
			entity.setHigh(highData.getDouble(i));
			entity.setLow(lowData.getDouble(i));
			entity.setClose(closeData.getDouble(i));
			entity.setOpen(openData.getDouble(i));
			entity.setVolume(volData.getLong(i));

			list.add(entity);
		}

		return list;
	}
	
	public static StockPerf calculatePerformance(String symbol, String name, int smaDays, int daysForAnalysis, int numOfCandlePos) throws Exception{
		
		Long startEpoch = HelperService.getSpecificDate(100);
		Long endEpoch = HelperService.getSpecificDate(0);
		
		//System.out.println(Math.atan(1)*180/Math.PI);  
		
		//System.exit(0);
		StockPerf sp = new StockPerf();
		sp.setName(name);
		sp.setSymbol(symbol);
		
		List<ScripPrice> list = HelperService.getStockHistory(symbol, startEpoch, endEpoch);
		//System.out.println(list);
		Double prevSMA = 0.0;
		Double sumSMADiff = 0.0;
		
		if(list.size()>0) {
			Double sum = 0.0;
			LinkedHashMap<LocalDate,String> candlePositions = new LinkedHashMap<>();
			for(int j=0;j<daysForAnalysis;j++) {
				int start = list.size()-1-j;
				int end = list.size()-1-j-smaDays;
				//System.out.println("=================================================================");
				//System.out.println(list.get(start));
				//System.out.println(list.get(end));
				sum = 0.0;
				String candleType = "";
				String candlePos = "";
				 
				for(int i = start; i>end;i--) {
					//System.out.println(list.get(i));
					sum+=list.get(i).getClose();	
				}
				if(start==list.size()-1) {
					sp.setClosingPrice(list.get(start).getClose());
					if(list.get(start).getOpen() > list.get(start).getClose()) {
						sp.setLastCandleColor("RED");
					} else {
						sp.setLastCandleColor("GREEN");
					}
					sp.setLastCandlePos(list.get(start).getClose() - (sum/smaDays));
				}
				
				if(numOfCandlePos>0) {
					if(list.get(start).getOpen() > list.get(start).getClose()) {
						candleType = "RED";
					} else {
						candleType = "GREEN";
					}
					if(list.get(start).getClose()>(sum/smaDays)) {
						candlePos = "ABOVE";
					} else {
						candlePos = "BELOW";
					}
					numOfCandlePos--;
					candlePositions.put(list.get(start).getDate(), candleType+"-"+candlePos);
				}
				
				if(prevSMA == 0.0) {
					prevSMA = sum/smaDays;
					continue;
				} else {
					sumSMADiff+=prevSMA - (sum/smaDays);
					prevSMA = sum/smaDays;
				}
				
				//System.out.println(sum/smaDays+"   "+list.get(start).getDate());
				//simpleMA.put(list.get(start).getDate(),sum/smaDays);
				
			}
			Map<LocalDate,String> sorted = candlePositions
					.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByKey())
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
			
			//System.out.println(sumSMADiff);
			sp.setSmaDiff(sumSMADiff);
			sp.setCandlePos(sorted);
			//System.out.println(sp);
		}
	    
		return sp;
	}
	
	final OkHttpClient httpClient = new OkHttpClient();
	public String sendGETSync(String symbol) throws IOException {
		HttpUrl.Builder urlBuilder 
	      = HttpUrl.parse("https://www.equitymaster.com/charts/graphhc.aspx").newBuilder();
	    urlBuilder.addQueryParameter("symbol", symbol);
	    urlBuilder.addQueryParameter("type", "ch");
	    urlBuilder.addQueryParameter("period", "6");
	    //urlBuilder.addQueryParameter("resolution", "D");
		
		Request request = new Request.Builder()
				.url(urlBuilder.build().toString())
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			
			 ResponseBody respBody = response.body();
			 byte[] bytes = respBody.bytes();
			 String s = new String(bytes, StandardCharsets.UTF_8);
			 return s;
		} catch (Exception e) {
			return null;
		}
    }
}