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
public class PMS_GOS4_E2EScripts extends BaseClass {
	
	String MasterFile = "GOS4_PMS_Master.xml";
	String MainFile = "PMSGOS4XMLFILEDATA.xlsx";

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS4_XML_BR_121(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_121";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_101(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_101";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_100(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_100";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_108(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_108";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_119(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_119";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_118(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_118";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_122(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_122";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_126(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_126";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_125(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_125";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_22(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_22";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_25(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_25";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_104(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_104";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_105(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_105";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_106(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_106";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_107(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_107";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_15(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_15";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_149(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_149";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_191(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_191";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_182(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_182";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_176(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_176";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_63(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_63";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
	public void OPT_GOS4_XML_BR_70(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS4_XML_BR_70";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
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
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType,key, evidence, environment,MainFile);
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
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,MainFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual Prescription values are not matching.");
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
