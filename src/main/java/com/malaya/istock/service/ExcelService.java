package com.malaya.istock.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.malaya.istock.constants.Constants;
import com.malaya.istock.model.StockPerf;
import com.malaya.istock.utility.PropertyReader;

public class ExcelService {
	public static Logger logger = Logger.getLogger(ExcelService.class);

	public void createExcelFile(List<StockPerf> scriptList) {
		Calendar calTime = Calendar.getInstance();
		calTime.add(Calendar.DAY_OF_MONTH, -1); // set previous date (current
												// date - 1 day)
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileDate = formatter.format(calTime.getTime());

		Properties appProps = null;
		try {
			appProps = PropertyReader.readProperties("app.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(appProps.getProperty(Constants.STOCK_HISTORY_URL));

		String filePath = appProps.getProperty(Constants.EXCEL_DOWNLOAD_PATH) + File.separator + "myscrip_" + fileDate
				+ ".xls";

		createExcel(scriptList, filePath);

		logger.info(filePath + " file created for ");
	}
	public void createExcel(List<StockPerf> scripList, String filePath) {
		try {
			// declare file name to be create
			String filename = filePath;
			// creating an instance of HSSFWorkbook class
			HSSFWorkbook workbook = new HSSFWorkbook();
			// invoking creatSheet() method and passing the name of the sheet to
			// be created
			HSSFSheet sheet = workbook.createSheet("More is good");
			// creating the 0th row using the createRow() method
			HSSFRow rowhead = sheet.createRow((short) 0);
			// creating cell by using the createCell() method and setting the
			// values to the cell by using the setCellValue() method
			rowhead.createCell(0).setCellValue("Symbol");
			rowhead.createCell(1).setCellValue("Name");
			rowhead.createCell(2).setCellValue("Closing");
			rowhead.createCell(3).setCellValue("Diff");
			rowhead.createCell(4).setCellValue("Last Type");
			rowhead.createCell(5).setCellValue("Last Pos");

			// creating the 1st row
			for (int i = 0; i < scripList.size(); i++) {
				HSSFRow row = sheet.createRow((short) i + 1);
				// inserting data in the first row
				row.createCell(0).setCellValue(scripList.get(i).getSymbol());
				row.createCell(1).setCellValue(scripList.get(i).getName());
				row.createCell(2).setCellValue(scripList.get(i).getClosingPrice());
				row.createCell(3).setCellValue(scripList.get(i).getSmaDiff());
				row.createCell(4).setCellValue(scripList.get(i).getLastCandleColor());
				row.createCell(5).setCellValue(scripList.get(i).getLastCandlePos());
			}

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			// closing the Stream
			fileOut.close();
			// closing the workbook
			workbook.close();
			// prints the message on the console
			System.out.println("Excel file has been generated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateExcelFile() {
		Calendar calTime = Calendar.getInstance();
		calTime.add(Calendar.DAY_OF_MONTH, -1); // set previous date (current
												// date - 1 day)
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileDate = formatter.format(calTime.getTime());

		Properties appProps = null;
		try {
			appProps = PropertyReader.readProperties("app.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(appProps.getProperty(Constants.STOCK_HISTORY_URL));

		String filePath = appProps.getProperty(Constants.EXCEL_DOWNLOAD_PATH) + File.separator + "myscrip_" + fileDate
				+ ".xls";

		try {
			// declare file name to be create
			String filename = filePath;
			// creating an instance of HSSFWorkbook class
			Workbook workbook = new HSSFWorkbook();
			// invoking creatSheet() method and passing the name of the sheet to
			// be created
			Sheet sheet = workbook.createSheet(fileDate);
			// creating the 0th row using the createRow() method
			Row rowhead = sheet.createRow((short) 0);
			// creating cell by using the createCell() method and setting the
			// values to the cell by using the setCellValue() method
			rowhead.createCell(0).setCellValue("Symbol");
			rowhead.createCell(1).setCellValue("Name");
			rowhead.createCell(2).setCellValue("Closing");
			rowhead.createCell(3).setCellValue("RSI1");
			rowhead.createCell(4).setCellValue("RSI2");
			rowhead.createCell(5).setCellValue("RSI3");
			rowhead.createCell(6).setCellValue("RSI4");
			rowhead.createCell(7).setCellValue("RSI5");

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			// closing the Stream
			fileOut.close();
			// closing the workbook
			workbook.close();
			// prints the message on the console
			System.out.println("Excel file has been generated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info(filePath + " file created for ");
		return filePath;
	}

	

	public void updateExistingExcelFile(String filePath, String scripName, String name, Double lastClose, double[] d) {

		// Creating file object of existing excel file
		File xlsFile = new File(filePath);

		try {
			// Creating input stream
			FileInputStream inputStream = new FileInputStream(xlsFile);

			// Creating workbook from input stream
			Workbook workbook=WorkbookFactory.create(inputStream);

			// Reading first sheet of excel file
			Sheet sheet = workbook.getSheetAt(0);

			// Getting the count of existing records
			int rowCount = sheet.getLastRowNum();
			
			Row row = sheet.createRow((short) rowCount + 1);
			// inserting data in the first row
			row.createCell(0).setCellValue(scripName);
			row.createCell(1).setCellValue(name);
			row.createCell(2).setCellValue(lastClose);
			row.createCell(3).setCellValue(d[d.length-1-4]);
			row.createCell(4).setCellValue(d[d.length-1-3]);
			row.createCell(5).setCellValue(d[d.length-1-2]);
			row.createCell(6).setCellValue(d[d.length-1-1]);
			row.createCell(7).setCellValue(d[d.length-1-0]);
			// Close input stream
			inputStream.close();

			// Crating output stream and writing the updated workbook
			FileOutputStream os = new FileOutputStream(xlsFile);
			workbook.write(os);

			// Close the workbook and output stream
			workbook.close();
			os.close();

			System.out.println("Excel file has been updated successfully.");

		} catch (EncryptedDocumentException | IOException e) {
			System.err.println("Exception while updating an existing excel file.");
			e.printStackTrace();
		}
	}

}
