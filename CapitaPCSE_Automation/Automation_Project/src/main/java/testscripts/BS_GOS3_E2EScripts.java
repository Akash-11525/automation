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
public class BS_GOS3_E2EScripts extends BaseClass
{
	

	String MasterFile = "GOS3_BS_Master.xml";
	String VocMasterFile = "GOS3_VO_Master.xml";
	String TestDataFile = "BSGOS3XMLFILEDATA.xlsx";
	String VoucherTestDataFile= "PMSGOS3VOXMLFILEDATA.xlsx";
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_5";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_6";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_7";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_8";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_9";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_10";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_11_11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_11_11";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_5";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_6";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_7";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_8";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_9";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_10";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_11";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_12(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_12";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_13(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_13";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_14(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_14";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_15(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_15";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_12_16(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_12_16";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_13_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_13_1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_13_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_13_2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_13_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_13_3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_13_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_13_4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_14_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_14_1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_14_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_14_2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_14_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_14_3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_14_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_14_4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_15_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_15_1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_15_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_15_2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_15_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_15_3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_15_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_15_4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_5";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_6";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_7";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_BS_16_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_BS_16_8";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_Portal_39(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_Portal_39";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_Portal_40(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_Portal_40";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_Portal_44(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_Portal_44";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_Portal_45(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_Portal_45";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_Portal_46(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_Portal_46";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_26(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_26";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_01(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_01";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_02(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_02";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_03(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_03";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_04(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_04";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_05(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_05";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_06(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_06";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_07(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_07";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_08(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_08";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_09(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_09";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_10";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_11";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_12(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_12";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_13(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_13";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_14(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_14";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_15(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_15";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_16(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_16";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_17(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_17";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_18(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_18";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_19(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_19";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_20(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_20";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_21(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_21";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_22(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_22";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_23(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_23";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_24(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_24";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case1";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case2";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case3";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case4";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case5";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case6";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case7";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case8";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case9";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case10";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case11";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case12(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case12";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","RegressionNewEnv","CloneSanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_BS_25_Case13(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_BS_25_Case13";
		String voucherKey= "OPT_WOP_Prescription_GOS3_PMS_11_1";
		List<String> keys= Arrays.asList(voucherKey,key);
		//List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);
		List<String> testDataFiles= Arrays.asList(VoucherTestDataFile,TestDataFile);
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition(TestDataFile, "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		for(String filenameList : filenameListMaster)
		{
			if(filenameList.contains("_VOC"))
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,VocMasterFile,filenameList, key);
			}
			else
			{
				helpers.CommonFunctions.copyFileDestinationFile(TestDataFile,MasterFile,filenameList, key);
			}
			
		}
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,keys.get(j));			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile,"PMS_GOS3",voucherKey);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(keys.get(count),testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
		}
		
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
				String strEntitiesToVerify= ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "NONDBENTITIES", key);
				List<String> ExpectedItemList = Arrays.asList(strEntitiesToVerify.split(","));
				objXMLhelpersComparison = new XMLhelpers(Process, assertMessage);
				for (String expectedItem : ExpectedItemList)
				{
					if(!expectedItem.isEmpty() && expectedItem!=null)
					{
						String expectedValue = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", expectedItem, key);
						if(expectedValue != null && !expectedValue.isEmpty())
						{
							objXMLhelpersComparison = objXMLhelpersComparison.BULKXMLProcess_VerifyExpected( key,  expectedItem , TestDataFile, environment,XMLType );
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
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "DBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"CLAIMDETAILS");
					Verify.verifyTrue(objXMLhelpersComparison.Process, "Expected and Actual values are not matching.");
					for(String AssetMessage : objXMLhelpersComparison.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				strEntitiesToVerify = ExcelUtilities.getKeyValueByPosition(TestDataFile, "ExpectedValues", "PRESDBENTITIESTOVERIFY", key);
				if(strEntitiesToVerify != null && !strEntitiesToVerify.isEmpty())
				{
					objXMLhelpersComparison= objXMLhelpersComparison.BULKXMLProcess_VerifyExpectedFromDatabase(key,TestDataFile, environment,strEntitiesToVerify,"PRESCRIPTIONDETAILS");
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
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	
}
	


