package com.malaya.istock.model;

public class Candlestick {
	
	private Long openTime;
	
    private double open;
	
	private double close;
	public Long getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Long openTime) {
		this.openTime = openTime;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	@Override
	public String toString() {
		return "Candlestick [openTime=" + openTime + ", open=" + open + ", close=" + close + "]";
	}
	public Candlestick(Long openTime, double open, double close) {
		super();
		this.openTime = openTime;
		this.open = open;
		this.close = close;
	}
	public Candlestick() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
