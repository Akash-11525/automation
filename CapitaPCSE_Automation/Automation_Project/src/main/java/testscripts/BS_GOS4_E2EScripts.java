package testscripts;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import pageobjects.XMLParseHelpers.XMLhelpers;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class BS_GOS4_E2EScripts extends BaseClass{
	String MasterFile = "GOS4_BS_Master.xml";
	String MainFile = "BSGOS4XMLFILEDATA.xlsx";
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_08_01(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_08_01";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_08_04(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_08_04";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_09_01(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_09_01";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_09_04(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_09_04";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_1";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_2";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_3";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_4";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_7";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_8";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_9";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS4_Portal_06_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_WOP_Prescription_GOS4_Portal_06_10";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC001(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC001";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC002(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC002";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC003(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC003";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC004(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC004";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC005(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC005";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC006(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC006";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC007(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC007";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC008(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC008";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC009(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC009";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC10";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC11";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC12(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC12";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC13(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC13";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC14(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC14";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC15(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC15";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_25944_TC16(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_25944_TC16";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC001(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC001";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC002(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC002";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC003(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC003";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC004(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC004";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC005(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC005";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC006(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC006";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC007(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC007";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC008(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC008";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC009(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC009";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC010(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC010";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC011(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC011";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC012(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC012";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC013(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC013";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC014(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC014";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC015(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC015";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC016(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC016";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC017(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC017";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC018(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC018";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void TCP_27994_TC019(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "TCP_27994_TC019";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		/*String expectedItemValue = null;*/
		List<String> assertMessage = null;
		// Copy Master file to Test Case File
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
			objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
			XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
			if(objXMLhelpers.Process)
			{
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}	
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
					JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
					List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
					objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
					for (String expectedItem : ExpectedItemList)
					{
						if(!expectedItem.isEmpty() && expectedItem!=null)
						{
							String expectedValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", expectedItem, key);
							if(expectedValue != null && !expectedValue.isEmpty())
							{
								objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment,XMLType );
								Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							//Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}
					}
				}
				Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			//setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
			}
			Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		}
}
