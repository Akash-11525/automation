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
import helpers.CommonFunctions;
import pageobjects.XMLParseHelpers.XMLhelpers;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;


@Listeners(ListenerClass.class)
public class PMS_GOS6_E2EScripts extends BaseClass
{
	

	String MasterFile = "GOS6_PMS_Master.xml";
	String MainFile = "PMSGOS6XMLFILEDATA.xlsx";
	String PVNMasterFile ="GOS6_PVN_PMS.xml";
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_GOS6_XML_BR_36(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS6_XML_BR_36";	
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> pvnDetails = null;
		List<String> AssertMessages = null;
		// Copy PVN File to XML folder location.
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		String[] outputFileNames = outputFileName.split(",");
		String pvnFileName = outputFileNames[0];
		String GOS6File = outputFileNames[1];
		helpers.CommonFunctions.copyFile(MainFile, PVNMasterFile, key, 0);
		pvnDetails = CommonFunctions.getPVNReference(MainFile, key, pvnFileName, environment);
		String pvnRef = pvnDetails.get(0);
		System.out.println("The PVN Ref Number from main: "+pvnRef);
		
		
		for (String AssertMessage : pvnDetails.subList(1,pvnDetails.size()))
				{
				setAssertMessage(AssertMessage, i);
				i = i + 1;
				}
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key, 1);
		String XMLType = helpers.CommonFunctions.getXMLType(GOS6File);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, AssertMessages);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, GOS6File, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, AssertMessages);
		objXMLhelpers = objXMLhelpers.BULKPMSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
		XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		if(objXMLhelpers.Process)
		{
			for(String AssetMessage : objXMLhelpers.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel(MainFile, "SystemJob", 1);
			for(String SystemJob : SystemJobs)
			{
				JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
			}
			if(JobRun)
			{
				
				XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
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
	public void OPT_GOS6_XML_BR_26(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS6_XML_BR_26";	
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> pvnDetails = null;
		List<String> AssertMessages = null;
		// Copy PVN File to XML folder location.
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		String[] outputFileNames = outputFileName.split(",");
		String pvnFileName = outputFileNames[0];
		String GOS6File = outputFileNames[1];
		helpers.CommonFunctions.copyFile(MainFile, PVNMasterFile, key, 0);
		pvnDetails = CommonFunctions.getPVNReference(MainFile, key, pvnFileName, environment);
		String pvnRef = pvnDetails.get(0);
		System.out.println("The PVN Ref Number from main: "+pvnRef);
		
		
		for (String AssertMessage : pvnDetails.subList(1,pvnDetails.size()))
				{
				setAssertMessage(AssertMessage, i);
				i = i + 1;
				}
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key, 1);
		String XMLType = helpers.CommonFunctions.getXMLType(GOS6File);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, AssertMessages);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, GOS6File, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, AssertMessages);
		objXMLhelpers = objXMLhelpers.BULKPMSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
		XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		if(objXMLhelpers.Process)
		{
			for(String AssetMessage : objXMLhelpers.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel(MainFile, "SystemJob", 1);
			for(String SystemJob : SystemJobs)
			{
				JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
			}
			if(JobRun)
			{
				
				XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
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
	public void OPT_GOS6_XML_VR_185(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS6_XML_VR_185";	
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> pvnDetails = null;
		List<String> AssertMessages = null;
		// Copy PVN File to XML folder location.
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		String[] outputFileNames = outputFileName.split(",");
		String pvnFileName = outputFileNames[0];
		String GOS6File = outputFileNames[1];
		helpers.CommonFunctions.copyFile(MainFile, PVNMasterFile, key, 0);
		pvnDetails = CommonFunctions.getPVNReference(MainFile, key, pvnFileName, environment);
		String pvnRef = pvnDetails.get(0);
		System.out.println("The PVN Ref Number from main: "+pvnRef);
		
		
		for (String AssertMessage : pvnDetails.subList(1,pvnDetails.size()))
				{
				setAssertMessage(AssertMessage, i);
				i = i + 1;
				}
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key, 1);
		String XMLType = helpers.CommonFunctions.getXMLType(GOS6File);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, AssertMessages);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, GOS6File, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, AssertMessages);
		objXMLhelpers = objXMLhelpers.BULKPMSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
		XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		if(objXMLhelpers.Process)
		{
			for(String AssetMessage : objXMLhelpers.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel(MainFile, "SystemJob", 1);
			for(String SystemJob : SystemJobs)
			{
				JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
			}
			if(JobRun)
			{
				
				XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
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
	public void OPT_GOS6_XML_VR_185_C2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		
		String key = "OPT_GOS6_XML_VR_185_C2";	
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> pvnDetails = null;
		List<String> AssertMessages = null;
		// Copy PVN File to XML folder location.
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		String[] outputFileNames = outputFileName.split(",");
		String pvnFileName = outputFileNames[0];
		String GOS6File = outputFileNames[1];
		helpers.CommonFunctions.copyFile(MainFile, PVNMasterFile, key, 0);
		pvnDetails = CommonFunctions.getPVNReference(MainFile, key, pvnFileName, environment);
		String pvnRef = pvnDetails.get(0);
		System.out.println("The PVN Ref Number from main: "+pvnRef);
		
		
		for (String AssertMessage : pvnDetails.subList(1,pvnDetails.size()))
				{
				setAssertMessage(AssertMessage, i);
				i = i + 1;
				}
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key, 1);
		String XMLType = helpers.CommonFunctions.getXMLType(GOS6File);
		System.out.println("XML Type is: "+XMLType);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, AssertMessages);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, GOS6File, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, AssertMessages);
		objXMLhelpers = objXMLhelpers.BULKPMSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
		XMLhelpers.readAndSaveXMLAttributeValues(key,MainFile,"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		if(objXMLhelpers.Process)
		{
			for(String AssetMessage : objXMLhelpers.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel(MainFile, "SystemJob", 1);
			for(String SystemJob : SystemJobs)
			{
				JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
			}
			if(JobRun)
			{
				
				XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
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


