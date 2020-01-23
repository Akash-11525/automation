package testscripts;
import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xml.sax.SAXException;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import helpers.OPHelpers;
import pageobjects.XMLParseHelpers.XMLhelpers;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;


@Listeners(ListenerClass.class)
public class BS_GOS5_E2EScripts extends BaseClass
{
	

	String MasterFile = "GOS5_BS_Master.xml";
	String MainFile = "BSGOS5XMLFILEDATA.xlsx";
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_VR_81(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_VR_81";
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_BR_22(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_22";
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

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_BR_21(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_21";
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_VR_74(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_VR_74";
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

	//Akshay S: Commented script as it is not in scope
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_BR_9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_9";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		String expectedItemValue = null;
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
		}*/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_BR_27(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_27";
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_VR_80(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_VR_80";
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

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_VR_82(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_VR_82";
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
	public void OPT_GOS5_BS_BR_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_10";
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS5_BS_VR_81_Statement(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_VR_81";
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
					//Akshay S: This will get the claim number for desired unique claim identifier
					String claimNo= XMLhelpers.getClaimNumber(MainFile,key,environment,"CRMDB");
					setup(browser, environment, clientName,"CRMOP","SUPERUSER");
					String paymentRunName="";
					
					OPHelpers objOPHelpers = new OPHelpers(Process, assertMessage);
					List<Object> parameters= Arrays.asList(claimNo,MainFile,getDriver(),key,evidence,"BeforeGMP");
					//Akshay S: This will verify the status of payment line for OP claim
					objOPHelpers= objOPHelpers.verifyClaimDetailsForBSPMSInCRM(parameters);
					Verify.verifyTrue(objOPHelpers.Process, "Claim Details are not matching with CRM for Claim ID: "+claimNo);
					for(String AssetMessage : objOPHelpers.AssertMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
					
					tearDown(browser);
					Thread.sleep(2000);
					String PaymentDuedate_Excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate", 1);
					Date date= CommonFunctions.convertStringtoCalDate(PaymentDuedate_Excel, "dd/MM/yyyy");
					String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
					
					OPHelpers objOPHelpersGMP = new OPHelpers(assertMessage,paymentRunName);
					setup(browser, environment, clientName, "GMP");
					//Akshay S: This will create GMP for PMS claim
					objOPHelpersGMP= objOPHelpersGMP.CreateGMP(getDriver(),environment,evidence,key,strDueDate);
					paymentRunName= objOPHelpersGMP.paymentRunName;
					for(String AssetMessage : objOPHelpersGMP.AssertMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
					
					tearDown(browser);
					Thread.sleep(2000);
					setup(browser, environment, clientName,"CRMOP","SUPERUSER");
					OPHelpers objOPHelpersGMPUpdate = new OPHelpers(Process, assertMessage);
					//String paymentRunName= "11Dec18OphEng4";
					//String claimNo="ADA01154";
					parameters= Arrays.asList(claimNo,MainFile,getDriver(),key,evidence,"AfterGMP",true,paymentRunName);
					//Akshay S: This will verify the PL status after GMP and will update the Payment file status in CRM
					objOPHelpersGMPUpdate= objOPHelpersGMPUpdate.verifyClaimDetailsForBSPMSInCRM(parameters);
					Verify.verifyTrue(objOPHelpersGMPUpdate.Process, "Claim Details post GMP are not matching with CRM for Claim ID: "+claimNo);
					for(String AssetMessage : objOPHelpersGMPUpdate.AssertMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
					tearDown(browser);
					Thread.sleep(2000);
					
					setup(browser, environment, clientName,"OPSTATEMENT");
					OPHelpers objOPHelpersStatement = new OPHelpers(Process, assertMessage);
					//Akshay S: This will verify the OP claim in statement
					//String claimNo="ADA01154";
					objOPHelpersStatement= objOPHelpersStatement.verifyEntryInStatement(getDriver(),environment,evidence,key,claimNo,"GOS5");
					Assert.assertEquals(objOPHelpersStatement.Process, true,"Entry is not visible in statement for Claim Number: "+claimNo);
					for(String AssetMessage : objOPHelpersStatement.AssertMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
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
	public void OPT_GOS5_BS_BR_14(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_14";
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
	public void OPT_GOS5_BS_BR_66(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_66";
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
	public void OPT_GOS5_BS_BR_73(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_73";
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
	public void OPT_GOS5_BS_BR_29(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_29";
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
	public void OPT_GOS5_BS_BR_25(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_25";
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
	public void OPT_GOS5_BS_BR_86(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS5_BS_BR_86";
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void processAllBSGOS5Messages(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException, ParserConfigurationException, SAXException, TransformerException
	{
		String allClaimFile= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", "BSAllClaims", "FILE NAME");
		int copyTagCount=0,i=1,tempCount=0;
		boolean Process = false;
		List<String> assertMessage = null;
		List<String> stgVerifiedKeys = new ArrayList<String>();
		List<String> temp= new ArrayList<String>();
		boolean JobRun = false;
		List<String> keys= ExcelUtilities.getCellValuesByPosition(MainFile, "XML", "TESTID");
		//Arrays.asList("OPT_GOS5_BS_VR_74 ","OPT_GOS5_BS_BR_21");
		for(int keyIndex=0;keyIndex<keys.size();keyIndex++){
			String key= keys.get(keyIndex);
			if(!key.equalsIgnoreCase("BSAllClaims")){
				temp.add(key);
			}
		}
		keys=temp;
		SoftAssert softAssert= new SoftAssert();
		
		//this will update each xml file and copy a tag to Master file
		int size = 10;
		for(int j=0;j<keys.size();j += size){
			
			int end = Math.min(j + size, keys.size());
	        List<String> sublist = keys.subList(j, end);
	        System.out.println("Equal distribution list is: "+sublist);
	        
			//Akshay S: Added to create xml file during run time for each set of claim file
	        tempCount= tempCount+1;
			allClaimFile= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", "BSAllClaims", "FILE NAME");
			String[]allClaimFileName= allClaimFile.split("\\.");
			String firstPart= allClaimFileName[0]+"_"+(tempCount);
			allClaimFile= firstPart+".xml";
			boolean allTagsDeleted= XMLhelpers.copyAndRemoveSampleTagFromXML(MainFile, MasterFile, allClaimFile, "OPT_GOS1_BS_VR_22","Claim-Message");
			Assert.assertEquals(allTagsDeleted, true,"All tags have not been deleted from file: "+allClaimFile+" for instance "+j);
	        /*boolean allTagsDeleted= XMLhelpers.copyAndRemoveSampleTagFromXML(MainFile, MasterFile, allClaimFile, "OPT_GOS1_XML_VR_062","Claim-Message");
			Assert.assertEquals(allTagsDeleted, true,"All tags have not been deleted from file: "+allClaimFile+" for instance "+j);*/
			String XMLType = helpers.CommonFunctions.getXMLType(allClaimFile);
			XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile,environment, allClaimFile, XMLType,"BSAllClaims");
			if(objXMLhelpersUpdateMasterData.Process)
			{
				for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
				{
					setAssertMessage(AssetMessage+" for key "+"BSAllClaims", i);
					i = i + 1;
				}
				setAssertMessage("****************************************************************************", i);
			}
			Assert.assertTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully for key: "+"BSAllClaims");
			for(int index=0;index<sublist.size();index++){
				String key= sublist.get(index);
				String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
				helpers.CommonFunctions.copyFile(MainFile,MasterFile, key);
				XMLType = helpers.CommonFunctions.getXMLType(FileNames);
				objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
				objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, FileNames, XMLType,key);
				if(objXMLhelpersUpdateMasterData.Process)
				{
					for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
					{
						setAssertMessage(AssetMessage+" for key "+key, i);
						i = i + 1;
					}
					setAssertMessage("****************************************************************************", i);
					XMLhelpers.BULKBSXMLProcess_GOS156_UpdateXML(XMLType,key, evidence, environment,MainFile);
					XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
					copyTagCount= XMLhelpers.copyTagsToOtherXMLFile(allClaimFile, FileNames, "Claim-Message");
					System.out.println("Tag copy count is "+copyTagCount);
				}
				softAssert.assertTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully for key: "+key);
			}
			if(copyTagCount==sublist.size()){
				System.out.println(sublist.size()+" Claim files have been created and "+copyTagCount+" Claim-Message tags have been copied to "+allClaimFile+" for instance "+j);
			}else{
				System.out.println(sublist.size()+" Claim files have been created and "+copyTagCount+" Claim-Message tags have been copied to "+allClaimFile+" for instance "+j);
				Assert.fail("Tag count is not matching for file: "+allClaimFile+" for instance "+j+" Expected: "+sublist.size()+" Actual: "+copyTagCount);
			}
			
			XMLhelpers objXMLhelpers= new XMLhelpers(Process, assertMessage);
			//this will post a Master file to web service
			String Responsecode = helpers.PostXML.BULKgetResultCodeByPostXML(allClaimFile,"BSAllClaims", environment,MainFile);
			String postCode= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", "BSAllClaims", "ResponseCodeFromPost");
			if(postCode.equalsIgnoreCase("200")){
				for(int index=0;index<sublist.size();index++){
					String key= sublist.get(index);
					objXMLhelpers= objXMLhelpers.BULKBSXMLProcess_GOS156_verifyStagingStatus(key,environment,MainFile,Responsecode);
					if(objXMLhelpers.Process)
					{
						for(String AssetMessage : objXMLhelpers.AssetMessage)
						{
							setAssertMessage(AssetMessage+" for key "+key, i);
							i = i + 1;
						}
						setAssertMessage("****************************************************************************", i);
						stgVerifiedKeys.add(key);
					}
					softAssert.assertTrue((objXMLhelpers.Process), "The Staging Process is not completed for key: "+key);
				}
			}
			softAssert.assertEquals(postCode.equalsIgnoreCase("200"),true,"File has not been successfully posted to a Web service for BSGOS5");
		}
		//Akshay S: This will execute the job and verify values for the keys which have passed Staging process
		List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
		for(String SystemJob : SystemJobs)
		{
			JobRun = helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
		}
		if(JobRun){
			if(!stgVerifiedKeys.isEmpty()&&stgVerifiedKeys!=null){
				for(int keycount=0;keycount<stgVerifiedKeys.size();keycount++){
					String key= stgVerifiedKeys.get(keycount);
					String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
					String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
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
								softAssert.assertTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value for key: "+key);
								for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
								{
									setAssertMessage(AssetMessage+" for key "+key, i);
									i = i + 1;
								}
								setAssertMessage("****************************************************************************", i);
							}
						}
					}
					strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
					if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
					{
						objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify);
						softAssert.assertTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching for key: "+key);
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage+" for key "+key, i);
							i = i + 1;
						}
						setAssertMessage("****************************************************************************", i);
					}
				}
			}
			softAssert.assertEquals((!stgVerifiedKeys.isEmpty()&&stgVerifiedKeys!=null),true,"Staging DB verification has failed for all BS messages");
		}
		softAssert.assertTrue((JobRun), "The System job run is not done properly");
		softAssert.assertAll();
	}

}


