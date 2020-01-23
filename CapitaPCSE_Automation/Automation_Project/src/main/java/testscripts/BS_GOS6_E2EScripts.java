package testscripts;
import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
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
public class BS_GOS6_E2EScripts extends BaseClass
{
	

	String MasterFile = "GOS6_BS_Master.xml";
	String MainFile = "BSGOS6XMLFILEDATA.xlsx";
	String PVNMasterFile ="GOS6_PVN_PMS.xml";
	/****************************************************************
	 * TC_GOS6_BS_ThirdPatientAtAddress - To verify E2E flow for GOS6 BS 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
//	@Parameters({"browser", "environment", "clientName","evidence"})
	/**@Parameters({"browser", "environment", "clientName","evidence"})*/
	public void GOS6_BS_ThirdPatientAtAddress (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		
		String key = "TC_GOS6_BS_ThirdPatientAtAddress";	
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
		pvnRef = pvnRef.substring(2);		
		ExcelUtilities.setKeyValueByPosition(MainFile, "XML", pvnRef, key, "PVN REF");
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
		objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
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
				
				List<String> ExpectedItemList = ExcelUtilities.getCellValuesInExcel(MainFile, "ExpectedValues", 1);
				for (String expectedItem : ExpectedItemList.subList(1, ExpectedItemList.size()))
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						
						XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
						objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment, XMLType);
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}	
						Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
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
	
	
	/****************************************************************
	 * GOS6_BS_Patient_Was_Substituted - To verify E2E flow for GOS6 BS 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void GOS6_BS_Patient_Was_Substituted (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		
		String key = "TC_GOS6_BS_Patient_Was_Substituted";	
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
		objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
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
				
				List<String> ExpectedItemList = ExcelUtilities.getCellValuesInExcel(MainFile, "ExpectedValues", 1);
				for (String expectedItem : ExpectedItemList.subList(1, ExpectedItemList.size()))
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						
						XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
						objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment, XMLType);
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}	
						Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
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
	
	
	/****************************************************************
	 * GOS6_BS_Patient_Was_Substituted - To verify E2E flow for GOS6 BS 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void GOS6_BS_Voucher_Type (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		
		String key = "TC_GOS6_BS_Voucher_Type";	
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
		objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
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
				
				List<String> ExpectedItemList = ExcelUtilities.getCellValuesInExcel(MainFile, "ExpectedValues", 1);
				for (String expectedItem : ExpectedItemList.subList(1, ExpectedItemList.size()))
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						
						XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
						objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment, XMLType);
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}	
						Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
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
		
	/****************************************************************
	 * TC_GOS6_BS_NHSNumber - To verify E2E flow for GOS6 BS 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void GOS6_BS_NHSNumber (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		
		String key = "TC_GOS6_BS_NHSNumber";	
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
		objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS6(XMLType,key, evidence, environment,MainFile,GOS6File,pvnRef);
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
				
				List<String> ExpectedItemList = ExcelUtilities.getCellValuesInExcel(MainFile, "ExpectedValues", 1);
				for (String expectedItem : ExpectedItemList.subList(1, ExpectedItemList.size()))
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						
						XMLhelpers objXMLhelpersComparison = new XMLhelpers(Process, AssertMessages);
						objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , MainFile, environment, XMLType);
						for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
						{
							setAssertMessage(AssetMessage, i);
							i = i + 1;
						}	
						Verify.verifyTrue(objXMLhelpersComparison.Process, "The "+expectedItem+" is not matching with expected value.");
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


