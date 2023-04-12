package com.malaya.istock.service;

public class Test1 {
	public static void main(String[] args) {
		System.out.println(Test1.randomAlphaNumeric("10785107",10));
	}

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String randomAlphaNumeric(String customerId,int count) {
		StringBuilder builder = new StringBuilder();
		builder.append(customerId).append("-");
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		
		return builder.toString();
	}

}
