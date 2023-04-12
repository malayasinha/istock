package com.malaya.istock.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {

	private PropertyReader() {

	}

	public static Properties getPropertyInstance(String scriptFileLocation) {
		//String scriptFileLocation = "src/com/malaya/stock/resources/scrips.properties";
		
		//System.out.println(ResourceReader.getValueFromPropertiesBundle(Constants.APP_RESOURCE));
		
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(scriptFileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	public static Map<String,String> readPropertiesAsMap() throws IOException {
        InputStream inputStream = null;
        Map<String,String> mapOfProperties = new HashMap<String,String>();
        try {
            Properties properties = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            inputStream = loader.getResourceAsStream("scrips.properties");
            properties.load(inputStream);
            Enumeration<?> propertyNames = properties.propertyNames();
            
            while (propertyNames.hasMoreElements()) {
                String key = (String) propertyNames.nextElement();
                mapOfProperties.put(key, properties.getProperty(key));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return mapOfProperties;
    }
	
	public static Properties readProperties(String propFile) throws IOException {
        
        Properties properties = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(propFile);
            properties.load(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return properties;
    }
}
