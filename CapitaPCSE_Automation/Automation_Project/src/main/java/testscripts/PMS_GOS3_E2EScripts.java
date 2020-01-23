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

public class PMS_GOS3_E2EScripts extends BaseClass{
	
	String MasterFile = "GOS3_Claim_Master.xml";
	String VocMasterFile = "GOS3_VO_Master.xml";
	String TestDataFile = "PMSGOS3XMLFILEDATA.xlsx";
	String VoucherTestDataFile= "PMSGOS3VOXMLFILEDATA.xlsx";
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_1";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_3";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_4";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_5";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_6";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_7";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_8";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_9";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_10";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_11_11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_11_11";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_1";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_3";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_4";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_5";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_6";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_7";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_8";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_9(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_9";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_10";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_11";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_12(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_12";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_13(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_13";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_14(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_14";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_15(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_15";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_12_16(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_12_16";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_13_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_13_1";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_13_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_13_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_13_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_13_3";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_13_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_13_4";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_14_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_14_1";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_14_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_14_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_14_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_14_3";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_14_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_14_4";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_15_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_15_1";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_15_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_15_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_15_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_15_3";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_15_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_15_4";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_1";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_3";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_4";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_5(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_5";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_6(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_6";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_7(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_7";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_WOP_Prescription_GOS3_PMS_16_8(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_WOP_Prescription_GOS3_PMS_16_8";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_01(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_01";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_02(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_02";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_03(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_03";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_04(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_04";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_05(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_05";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_06(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_06";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_07(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_07";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_08(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_08";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_09(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_09";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_10(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_10";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_11(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_11";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_12(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_12";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_13(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_13";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_14(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_14";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_15(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_15";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_16(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_16";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_17_2(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_17_2";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_18(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_18";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_19(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_19";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_20(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_20";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_21(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_21";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_22(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_22";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_23(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_23";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void OPT_Prescription_Complex_GOS3_PMS_24(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String key = "OPT_Prescription_Complex_GOS3_PMS_24";
		/*List<String> fileNames = Arrays.asList(VocMasterFile,MasterFile);*/
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
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(testDataFiles.get(j),environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS3(TestDataFile, XMLType,key, evidence, environment,VoucherTestDataFile);
		
		for(int count=0;count<testDataFiles.size();count++){
			XMLhelpers.readAndSaveXMLAttributeValues(key,testDataFiles.get(count),"ExpectedValues","GetAttributeDetails","GetChildTagAttributeDetails");
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


