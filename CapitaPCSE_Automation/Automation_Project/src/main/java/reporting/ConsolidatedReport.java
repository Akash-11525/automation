package reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.log4testng.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import helpers.CommonFunctions;
import helpers.DatabaseHelper;

public class ConsolidatedReport {

	public void generateExcelReport() throws ParserConfigurationException, SAXException, IOException, InterruptedException, SQLException
	{
		String path = ConsolidatedReport.class.getClassLoader().getResource("./").getPath();
	
		XSSFWorkbook book = new XSSFWorkbook();
	
		XSSFSheet sheet = book.createSheet("Results");
		
		// Amit : Getting latest Execution ID from Automation DB
		List<String> executionID = DatabaseHelper.CreateDataListForAListOfRows("SELECT EXECUTION_ID FROM automationdb.execution_details Where Exec_Status = 'Completed' Order By Date desc;", "EXECUTION_ID", "automationdb", "Local");
		int latestExecutionID = Integer.parseInt(executionID.get(0));
		System.out.println("Execution ID : " + latestExecutionID);
		
		//Amit : Getting total numbers of tests in Execution
		String query = "select TC.MANUAL_TEST_ID,AM.MODULE_NAME,AF.FEATURE_NAME,TC.TEST_OBJECTIVE,TC.SCRIPT_NAME,STATUS,RR.TEST_OUTCOME,RR.EXECUTION_ID FROM automationdb.testcase TC INNER JOIN automationdb.raw_results RR ON TC.SCRIPT_NAME = RR.SCRIPT_NAME INNER JOIN automationdb.feature AF ON AF.FEATURE_ID = TC.FEATURE INNER JOIN automationdb.module AM ON AM.MODULE_ID = TC.MODULE	WHERE TC.TEST_ID IN(SELECT ETL.TEST_ID FROM automationdb.execution_test_list ETL WHERE EXECUTION_ID="+latestExecutionID+") AND RR.EXECUTION_ID="+latestExecutionID+"";
		//String query = "select TC.MANUAL_TEST_ID,TC.TEST_OBJECTIVE,TC.SCRIPT_NAME,STATUS,RR.TEST_OUTCOME,RR.EXECUTION_ID FROM automationdb.testcase TC INNER JOIN automationdb.raw_results RR ON TC.SCRIPT_NAME = RR.SCRIPT_NAME WHERE TC.TEST_ID IN(SELECT ETL.TEST_ID FROM automationdb.execution_test_list ETL WHERE EXECUTION_ID="+latestExecutionID+") AND RR.EXECUTION_ID="+latestExecutionID+";";

		List<String> tests = DatabaseHelper.CreateDataListForAListOfRows(query, "EXECUTION_ID", "automationdb", "Local");
		int len = tests.size();
		System.out.println("Len: "+len);
		//Amit : Set the Column Header in the report.
		List<String>tablecolumnnames = Arrays.asList("MANUAL_TEST_ID","FEATURE_NAME","MODULE_NAME", "TEST_OBJECTIVE", "SCRIPT_NAME", "STATUS","TEST_OUTCOME", "EXECUTION_ID");
		// Amit : Get the list of list of results stored in database against latest execution id.
		List<ArrayList<String>> resultlist = DatabaseHelper.createMulipleColumnDataListForMultipleRowsInDB(query, tablecolumnnames, "automationdb", "Local");
		//Reading Test Suite or Test name
		for(int i=0; i < len+1; i++)
		{
			//int r = 0;
		//	Node testNode = testNodeList.item(i);
			if(i == 0)
			{
				//Creating the Headers
				XSSFRow row = sheet.createRow(i);
				//Creating Feature Column
				XSSFCell serialNoHeader = row.createCell(0);
				serialNoHeader.setCellValue("Manual Test ID");

				XSSFCell moduleNameHeader = row.createCell(1);
				moduleNameHeader.setCellValue("Module Name");

				XSSFCell featureNameHeader = row.createCell(2);
				featureNameHeader.setCellValue("Feature Name");
				
				XSSFCell featureHeader = row.createCell(3);
				featureHeader.setCellValue("Test Objective");

				XSSFCell testCaseHeader = row.createCell(4);
				testCaseHeader.setCellValue("Test Script Name");

				XSSFCell statusHeader = row.createCell(5);
				statusHeader.setCellValue("Status");

				XSSFCell browserHeader = row.createCell(6);
				browserHeader.setCellValue("Test Outcome");

				XSSFCell exceptionHeader = row.createCell(7);
				exceptionHeader.setCellValue("Test Execution ID");

			
			}	
			else
			{
				//int j = i+1;
				XSSFRow row = sheet.createRow(i);
				List<String> resultlistItem = resultlist.get(i-1);
				
				
				//Creating Feature Column
				XSSFCell testID = row.createCell(0);
				//tring testIDValue = resultlist.get(0).toString()
				System.out.println(resultlistItem.get(0).toString());
				testID.setCellType(CellType.STRING);
				testID.setCellValue(resultlistItem.get(0).toString());

				XSSFCell moduleHeader = row.createCell(1);
				System.out.println(resultlistItem.get(1).toString());
				moduleHeader.setCellType(CellType.STRING);
				moduleHeader.setCellValue(resultlistItem.get(1).toString());

				XSSFCell featureNameHeader = row.createCell(2);
				System.out.println(resultlistItem.get(2).toString());
				featureNameHeader.setCellType(CellType.STRING);
				featureNameHeader.setCellValue(resultlistItem.get(2).toString());
				
				XSSFCell featureHeader = row.createCell(3);
				System.out.println(resultlistItem.get(3).toString());
				featureHeader.setCellType(CellType.STRING);
				featureHeader.setCellValue(resultlistItem.get(3).toString());

				XSSFCell testCaseHeader = row.createCell(4);
				System.out.println(resultlistItem.get(4).toString());
				testCaseHeader.setCellType(CellType.STRING);
				testCaseHeader.setCellValue(resultlistItem.get(4).toString());

				XSSFCell statusHeader = row.createCell(5);
				System.out.println(resultlistItem.get(5).toString());
				statusHeader.setCellType(CellType.STRING);
				statusHeader.setCellValue(resultlistItem.get(5).toString());

				XSSFCell browserHeader = row.createCell(6);
				System.out.println(resultlistItem.get(6).toString());
				browserHeader.setCellType(CellType.STRING);
				browserHeader.setCellValue(resultlistItem.get(6).toString());

				XSSFCell exceptionHeader = row.createCell(7);
				System.out.println(resultlistItem.get(7).toString());
				exceptionHeader.setCellType(CellType.STRING);
				exceptionHeader.setCellValue(resultlistItem.get(7).toString());
			}
		}
		
			String exeID = Integer.toString(latestExecutionID);
			FileOutputStream fout = new FileOutputStream(path + "../../Reports/ConsolidatedReport_"+exeID+".xlsx");
			book.write(fout);
			fout.close();
			System.out.println("Consolidated Report Generated");
			
		}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException, SQLException 
	{
		
		final Logger LOGGER = Logger.getLogger(ConsolidatedReport.class);
		LOGGER.info("START : ConsolidatedReport : main");
		new ConsolidatedReport().generateExcelReport();
		LOGGER.info("STOP : ConsolidatedReport : main");

	}

}
