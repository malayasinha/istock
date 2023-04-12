package com.malaya.istock.model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

public class StockPerf {
	private String slNo;
	private String name;
	private String symbol;
	private Double closingPrice;
	private Double smaDiff;
	private String lastCandleColor;
	private Double lastCandlePos;
	private Map<LocalDate,String> candlePos;
	
	public String getSlNo() {
		return slNo;
	}
	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getClosingPrice() {
		return closingPrice;
	}
	public void setClosingPrice(Double closingPrice) {
		this.closingPrice = closingPrice;
	}
	public Double getSmaDiff() {
		return smaDiff;
	}
	public void setSmaDiff(Double smaDiff) {
		this.smaDiff = smaDiff;
	}
	public Map<LocalDate, String> getCandlePos() {
		return candlePos;
	}
	public void setCandlePos(Map<LocalDate, String> candlePos) {
		this.candlePos = candlePos;
	}
	public String getLastCandleColor() {
		return lastCandleColor;
	}
	public void setLastCandleColor(String lastCandleColor) {
		this.lastCandleColor = lastCandleColor;
	}
	public Double getLastCandlePos() {
		return lastCandlePos;
	}
	public void setLastCandlePos(Double lastCandlePos) {
		this.lastCandlePos = lastCandlePos;
	}
	@Override
	public String toString() {
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		return name + "," + symbol + "," + numberFormat.format(closingPrice) + "," + numberFormat.format(smaDiff) + "," + lastCandleColor + "," + numberFormat.format(lastCandlePos) + "," + candlePos;
	}
	
}
