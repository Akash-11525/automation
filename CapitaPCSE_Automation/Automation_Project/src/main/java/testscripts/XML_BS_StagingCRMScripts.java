package testscripts;
import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.OP.OPContractorDeclaration;
import pageobjects.OP.OPCreateGOS6PVN;
import pageobjects.OP.OPGOS6ContractorDeclaration;
import pageobjects.OP.OPGOS6Options;
import pageobjects.OP.OPGOS6PVNView;
import pageobjects.OP.OPGOS6PatientDeclaration;
import pageobjects.OP.OPGOS6PatientDetails;
import pageobjects.OP.OPGOS6PatientEligibility;
import pageobjects.OP.OPGOS6PerformerDeclaration;
import pageobjects.OP.OPHomePage;
import pageobjects.OP.OPMakeAClaim;
import pageobjects.OP.OPPatientDeclaration;
import pageobjects.OP.OPPatientDetails;
import pageobjects.OP.OPPatientEligibility;
import pageobjects.OP.OPPerformerDeclaration;
import pageobjects.OP.OPSearchGOS6PVN;
import pageobjects.PL.ProcessorPLHelpers;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;
import pageobjects.XMLParseHelpers.*;


@Listeners(ListenerClass.class)
public class XML_BS_StagingCRMScripts extends BaseClass
{
	String MainFile = "XMLFILEDATA.xlsx";
	String VOCMasterFile = "GOS3 VO Master.xml";
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BSXMLProcess(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS5_BS_BR_21";
		boolean JobRun = false;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames ,key);
		CommonFunctions.haltExecution();
		
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String StagingStatus = XMLhelpers.getStagingStatus(key);
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				setAssertMessage("The Response code is generated " + StagingStatus, 2);
				String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					setAssertMessage("The Correct staging status is displayed " + StagingStatus, 3);
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						int count = XMLhelpers.StagingStatus_Rejected(key);
						if (count == 0)
						{
							System.out.println("The errors are matching with expected errors.");

						}
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						System.out.println("The Staging status is  " + StagingStatus);
						setAssertMessage("The Response code is generated " + StagingStatus, 4);
					}
					System.out.println("The Staging Process is Completed  " + StagingStatus);
					setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

					//	XMLhelpers.StagingProcess(StagingStatus,key);
					List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
					for(String SystemJob : SystemJobs)
					{
						JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
					}
					if(JobRun)
					{
						System.out.println("The Job Run Sucessfully " );
						setAssertMessage("The Job Run Sucessfully " , 6);
						Thread.sleep(3000);
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
						String ClaimStatusCRM = XMLhelpers.GetClaimsCRM_BS(getDriver(), key);
						String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"Expected CLAIM STATUS IN CRM");
						AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(getDriver());
						String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"CLAIM ID");
						String ClaimStaCRM =  ObjAdvancedFindResult.getClaimCRM();
						if(evidence)
						{
							ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"CRMStatus");
						}					
						setAssertMessage("The Claims status on CRM is  " + ClaimStaCRM, 7);
						boolean ClaimstatusOnCRM = ObjAdvancedFindResult.VerifyClaimsStatus(ClaimID,ClaimStatus);
						if(ClaimstatusOnCRM)
						{
							System.out.println("The Correct Claims status is appered on CRM  " + ClaimStaCRM);
							setAssertMessage("The Correct Claims status is appered on CRM " + ClaimStaCRM, 8);
						}
						Verify.verifyTrue(ClaimstatusOnCRM, "The Correct Claims status is not appered on CRM"+ClaimStaCRM);
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						if(evidence)
						{
							ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"PaymentLines");
						}
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated " +ClaimID );
							setAssertMessage("The Payment Line is generated" +ClaimID, 9);	

							Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
							if(AlertPresent)
							{
								ObjAdvancedFindResult.ClickSpaceBar();
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated " +ClaimID );
							setAssertMessage("The Payment Line is not generated" +ClaimID, 4);	
							Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
							if(AlertPresent)
							{
								ObjAdvancedFindResult.ClickSpaceBar();
							}
						}

						if(ClaimStatusCRM.equalsIgnoreCase("Rejected"))
						{
							ObjAdvancedFindResult.XMLCRM_Rejected(key ,ClaimID, evidence);
							int count = ObjAdvancedFindResult.verifyXMLerrorCRM(key);
							if(count == 0)
							{
								System.out.println("The correct CRM error is displayed " +ClaimID );
								setAssertMessage("The correct CRM error is displayed " +ClaimID, 9);
							}
							Verify.verifyEquals(count == 0, "The incorrect CRM error is displayed ");
						}
						else
						{
							System.out.println("The CRM claims status is  " +ClaimStatusCRM );
							setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
						}

					}
					Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}
				Verify.verifyEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched"+StagingStatus);

			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
	}*/

/*	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BSXMLProcess_GOS156(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS5_BS_BR_21";
		boolean JobRun = false;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
			helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(FileNames,"UpdatedAttribute", AttributeName,key);
		}
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String StagingStatus = XMLhelpers.getStagingStatus(key);
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				setAssertMessage("The Response code is generated " + StagingStatus, 2);
				String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					setAssertMessage("The Correct staging status is displayed " + StagingStatus, 3);
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						int count = XMLhelpers.StagingStatus_Rejected(key);
						if (count == 0)
						{
							System.out.println("The errors are matching with expected errors.");

						}
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						System.out.println("The Staging status is  " + StagingStatus);
						setAssertMessage("The Response code is generated " + StagingStatus, 4);
					}
					System.out.println("The Staging Process is Completed  " + StagingStatus);
					setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

					//	XMLhelpers.StagingProcess(StagingStatus,key);
					List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
					for(String SystemJob : SystemJobs)
					{
						JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
					}
					if(JobRun)
					{
						System.out.println("The Job Run Sucessfully " );
						setAssertMessage("The Job Run Sucessfully " , 6);
						Thread.sleep(3000);
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
						String ClaimStatusCRM = XMLhelpers.GetClaimsCRM_BS(getDriver(), key);
						String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"Expected CLAIM STATUS IN CRM");
						AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(getDriver());
						String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"CLAIM ID");
						String ClaimStaCRM =  ObjAdvancedFindResult.getClaimCRM();
						setAssertMessage("The Claims status on CRM is  " + ClaimStaCRM, 7);
						if(evidence)
						{
							ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"CRMStatus");
						}
						boolean ClaimstatusOnCRM = ObjAdvancedFindResult.VerifyClaimsStatus(ClaimID,ClaimStatus);
						if(ClaimstatusOnCRM)
						{
							System.out.println("The Correct Claims status is appered on CRM  " + ClaimStaCRM);
							setAssertMessage("The Correct Claims status is appered on CRM " + ClaimStaCRM, 8);
						}
						Verify.verifyTrue(ClaimstatusOnCRM, "The Correct Claims status is not appered on CRM"+ClaimStaCRM);
						if(evidence)
						{
							ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"PaymentLines");
						}
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);

						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated " +ClaimID );
							setAssertMessage("The Payment Line is generated" +ClaimID, 9);	

							Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
							if(AlertPresent)
							{
								ObjAdvancedFindResult.ClickSpaceBar();
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated " +ClaimID );
							setAssertMessage("The Payment Line is not generated" +ClaimID, 4);	
							Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
							if(AlertPresent)
							{
								ObjAdvancedFindResult.ClickSpaceBar();
							}
						}

						if(ClaimStatusCRM.equalsIgnoreCase("Rejected"))
						{
							ObjAdvancedFindResult.XMLCRM_Rejected(key ,ClaimID, evidence);
							int count = ObjAdvancedFindResult.verifyXMLerrorCRM(key);
							if(count == 0)
							{
								System.out.println("The correct CRM error is displayed " +ClaimID );
								setAssertMessage("The correct CRM error is displayed " +ClaimID, 9);
							}
							else
							{
								setup(browser, environment, clientName,"CRMOP","SUPERUSER");
							}
							Verify.verifyEquals(count == 0, "The incorrect CRM error is displayed ");
						}
						else
						{
							System.out.println("The CRM claims status is  " +ClaimStatusCRM );
							setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
						}

					}
					
					else
					{
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
					}
					
					Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}
				else
				{
					setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				}
				Verify.verifyEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched with expected. ActualStatus: "+StagingStatus+". Expected Status: "+ExpStatus);

			}
			else
			{
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
		else
		{
			setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		}

		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");

	}*/


	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS5_7170 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS5_BS_BR_21";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156_Duplicate(key, evidence,environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				//XMLhelpers objXMLhelpers1 = new XMLhelpers(Process, assertMessage);
				XMLhelpers objXMLhelpersCRM = new XMLhelpers(Process, assertMessage);
				objXMLhelpersCRM = objXMLhelpersCRM.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpersCRM.Process)
				{
					for(String AssetMessage : objXMLhelpersCRM.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM is not happen" +objXMLhelpersCRM.Process);				
			}
			else
			{
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
			
		}
		else
		{
			setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS6_11750 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS6_BS_BR_74";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS6(XMLType, key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpersCRM = new XMLhelpers(Process, assertMessage);
				objXMLhelpersCRM = objXMLhelpersCRM.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpersCRM.Process)
				{
					for(String AssetMessage : objXMLhelpersCRM.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM is not happen" +objXMLhelpersCRM.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS6_11752 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{

		String key = "OPT_GOS6_BS_BR_76";
		int count = 0;
		String sheetName = "PVNAttributes";
		String dataFileName = "XMLStagingCRM.xlsx";
		boolean JobRun = false;
		int i = 1;
		boolean Process = false;
		List<String> assertMessage = null;
		List<String> assertMessageCRM = null;
	//	setup(browser, environment, clientName,"CRMOP","PCSECLAIMPROCESSOR");
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetPvnCodefile = filenameList[0];
		String GetPvnFile = filenameList[1];
		

		String ExpStatusUpdate = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
		//System.out.println(FileName);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(dataFileName, sheetName, 1);
		for(String AttributeName : AttributeNames )
		{
			//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
			helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_Test(filenameList[0],dataFileName,sheetName, AttributeName,key,22);
			 
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key, environment);
		String pvnReference = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Success PVN-Reference=");
		String pvnReference_updated = pvnReference.substring(2);
		System.out.println("PVN_Ref: "+pvnReference);


		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String StagingStatus = XMLhelpers.getPVNStatus(key, pvnReference, environment);
			if (StagingStatus.equalsIgnoreCase("Rejected"))
			{
				count = XMLhelpers.PVNStatus_Rejected(key, environment);
			}
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				setAssertMessage("The Response code is generated " + StagingStatus, 2);
				//String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				String ExpStatus = "Accepted";
				if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					System.out.println("The Correct staging status is displayed for GOS6 PVN" + StagingStatus);
					setAssertMessage("The Correct staging status is displayed for GOS6 PVN" + StagingStatus, 3);
					helpers.CommonFunctions.ReadXML_UpdateCodeValue(filenameList[1], "Claim-Message", "PVN-Reference", pvnReference_updated, key);
					//String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[1],key);





					//	String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
					XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
					String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
					objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS6_Duplicate(XMLType,filenameList[1], key, evidence,environment);
					if(objXMLhelpersstaging.Process)
					{
						for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
							setup(browser, environment, clientName, "CRMOP", "SUPERUSER");
							XMLhelpers objXMLhelpersCRM = new XMLhelpers(Process, assertMessageCRM);
							objXMLhelpersCRM = objXMLhelpersCRM.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
							if(objXMLhelpersCRM.Process)
							{
								for(String AssetMessage : objXMLhelpersCRM.AssetMessage)
								{
									setAssertMessage(AssetMessage, i);
									i = i + 1;
								}
							}
							Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM is not happen" +objXMLhelpersCRM.Process);				
						}
						Verify.verifyTrue((JobRun), "The System job run is not done properly");
					}
					Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed. There are errors reported.");
				}
				Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
					Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
				}
			}
		}}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS1_6745 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS1_BS_VR_44";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not executed properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS1_6453 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS1_BS_BR_37";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}


	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS3_7058 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS3_BS_BR_118";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		String XMLType = null;
		List<String> assertMessage = null;	
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		int FileNumber = filenameList.length;
		for(int j= 0;j<FileNumber;j++)
		{
			if(filenameList[j].contains("VOC"))
			{
				System.out.println(filenameList[j]);
				helpers.CommonFunctions.copyFile(VOCMasterFile, key);
			}
		}	
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS3(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS3_7068 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS3_BS_BR_128";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		int FileNumber = filenameList.length;
		for(int j= 0;j<FileNumber;j++)
		{
			if(filenameList[j].contains("VOC"))
			{
				System.out.println(filenameList[j]);
				helpers.CommonFunctions.copyFile(VOCMasterFile, key);
			}
		}
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS3(XMLType, key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}


	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS4_5975 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS4_BS_BR_17";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		//objXMLhelpers = objXMLhelpers.BSXMLProcess_GOS4(XMLType, key, evidence, environment);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType, key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
			for(String SystemJob : SystemJobs)
			{
				JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob,environment);
			}
			if(JobRun)
			{
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers1 = new XMLhelpers(Process, assertMessage);
				objXMLhelpers1 = objXMLhelpers1.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers1.Process)
				{
					for(String AssetMessage : objXMLhelpers1.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers1.Process), "The Verification on CRM is not happen" +objXMLhelpers1.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");	
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	/************************************************************************************************************
	 * TC 19000 -CR_15_GOS 4 BS_003 - Verify that the GOS 4 BS Claim is successfully processed even if 
	 * (no_signature is present in Bulk-Scan-Signature-Reference tag) within the XML
	 * @throws ParseException 
	 * 
	 ************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS4_CR_19000 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_CR15_GOS4_BS_003";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not executed properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");	
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	
	/****************************************************************
	 * TC 19037 -OPT_CR15_GOS6_BS_010 - Verify that the GOS 6 BS Claim is 
	 * failed in schema itself if details are missing in  Bulk-Scan-Signature-Reference tag within the XML for 
	 * Patient, Performer, Contractor & Supplier
	 * 
	 ********************************************************************************************************** */
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS4_CR_19037 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_CR15_GOS6_BS_010";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;	
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		String FailureMessage = XMLhelpers.BSXMLProcess_GOS156_SchFailure(XMLType, key,evidence,environment);
		
		String ExpFailureMessage = "The 'http://www.capita.co.uk/pcse/schema/ophthalmicpayments/GOS:Patient-Declaration-Bulk-Scan-Signature-Reference' element has an invalid value according to its data type";
		
		Assert.assertTrue(FailureMessage.contains(ExpFailureMessage), "The failure message does not have expected contains. Expected: "+ExpFailureMessage+"But Found Actual: "+FailureMessage);
		
		
	}

	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_GOS6_PVN (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS3_BS_BR_118";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpers = objXMLhelpers.BSXMLProcess_GOS3(XMLType, key, evidence, environment);
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
				setup(browser, environment, clientName, "CRMOP");
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");	
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}*/

	/****************************************************************
	 * TC 18544 -OPT_ClaimDuplication_02 - Validate system accepts claim if GOS 1 Claim is submitted first
	 * via PMS followed by GOS 5 Claim via BS with same patient details where patient age is under 16 yrs.
	 *  and sight test is 11 months and retest code not present
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_GOS1PMS_DuplicateGOS5 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_ClaimDuplication_02";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String FirstName = "Automation";
		String HrMin = CommonFunctions.getCurrentHourMin();
    	String userID = FirstName+HrMin;
    	utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", userID, "First-Names", key);    	
  		String NHSNUmber = helpers.CommonFunctions.generateValidNHSNo();
  		utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", NHSNUmber, "NHSNumber", key); 
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_Duplicate(filenameList[0],key, evidence, "PMS",environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			XMLhelpers objDuplicateXMLhelpers = new XMLhelpers(Process, assertMessage);
			String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
			objDuplicateXMLhelpers = objDuplicateXMLhelpers.BSXMLProcess_Duplicate(XMLType, filenameList[1],key, evidence, "BS",environment);
			if(objDuplicateXMLhelpers.Process)
			{
				for(String AssetMessage : objDuplicateXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}			
				List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
					{
						JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob,environment);
					}
					if(JobRun)
					{
						setup(browser, environment, clientName, "CRMOP");
						XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
						objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
						if(objXMLhelpers.Process)
						{
							for(String AssetMessage : objXMLhelpers.AssetMessage)
							{
								setAssertMessage(AssetMessage, i);
								i = i + 1;
							}
						}
						Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
					}
					Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			Verify.verifyTrue((objDuplicateXMLhelpers.Process), "The staging for GOS 5 is not completed");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	/****************************************************************
	 * TC 18561 -OPT_ClaimDuplication_05 - Validate system rejects duplicate claim if GOS 5 Claim is submitted first via BS
	 * followed by GOS 5 claim via BS with same patient details where patient age is 16 years and over and under
	 *  70 years and sight test is < 23 mnth and no retest code
	 ********************************************************************************************************** */

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_Duplicate_GOS5 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_ClaimDuplication_05";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String FirstName = "Automation";
		String HrMin = CommonFunctions.getCurrentHourMin();
    	String userID = FirstName+HrMin;
    	utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", userID, "First-Names", key);    	
  		String NHSNUmber = helpers.CommonFunctions.generateValidNHSNo();
  		utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", NHSNUmber, "NHSNumber", key); 
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_Duplicate(XMLType,filenameList[0],key, evidence,XMLType,environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			XMLhelpers objDuplicateXMLhelpers = new XMLhelpers(Process, assertMessage);
			objDuplicateXMLhelpers = objDuplicateXMLhelpers.BSXMLProcess_Duplicate(XMLType,filenameList[1],key, evidence,XMLType,environment);
			if(objDuplicateXMLhelpers.Process)
			{
				if(objDuplicateXMLhelpers.VerifyStagingError)
				{
					for(String AssetMessage : objDuplicateXMLhelpers.AssetMessage)
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
						setup(browser, environment, clientName, "CRMOP");
						XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
						objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
						if(objXMLhelpers.Process)
						{
							for(String AssetMessage : objXMLhelpers.AssetMessage)
							{
								setAssertMessage(AssetMessage, i);
								i = i + 1;
							}
						}
						setup(browser, environment, clientName, "CRMOP");
						Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
					}
					Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			Verify.verifyTrue((objDuplicateXMLhelpers.Process), "The staging for GOS 5 is not completed");
		}
			Verify.verifyTrue((objDuplicateXMLhelpers.VerifyStagingError), "The staging error is not populated correctly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	
	/****************************************************************
	 * TC 18562 -Validate system rejects claim, if GOS1 Claim is submitted first via BS
	 *  followed by GOS 6 claim via PMS with same Patient Details where patient is 16 y and over and 
	 *  under 70 y and eligibility"glaucoma" is selected, sight test <23months and no retest code
	 ********************************************************************************************************** */

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_Duplicate_GOS1BS_GOS6PMS (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_ClaimDuplication_20";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameListMaster = FileNamesMaster.split(",");
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		
		String FirstName = "Automation";
		String HrMin = CommonFunctions.getCurrentHourMin();
    	String userID = FirstName+HrMin;
    	utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", userID, "First-Names", key);    	
  		String NHSNUmber = helpers.CommonFunctions.generateValidNHSNo();
  		utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", NHSNUmber, "NHSNumber", key); 
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_Duplicate(XMLType, filenameList[0],key, evidence,"BS",environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			XMLhelpers objDuplicateXMLhelpers = new XMLhelpers(Process, assertMessage);
			objDuplicateXMLhelpers = objDuplicateXMLhelpers.XMLProcess_Duplicate(filenameList[1],key, evidence,"PMS",environment);
			if(!objDuplicateXMLhelpers.Process)
			{
				if(objDuplicateXMLhelpers.VerifyStagingError)
				{
					for(String AssetMessage : objDuplicateXMLhelpers.AssetMessage)
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
						setup(browser, environment, clientName, "CRMOP");
						XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
						objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
						if(objXMLhelpers.Process)
						{
							for(String AssetMessage : objXMLhelpers.AssetMessage)
							{
								setAssertMessage(AssetMessage, i);
								i = i + 1;
							}
						}
						
						Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
					}
					Verify.verifyTrue((JobRun), "The System job run is not done properly");
			}
			Verify.verifyTrue((!objDuplicateXMLhelpers.Process), "The staging for GOS 5 is not completed");
		}
			Verify.verifyTrue((objDuplicateXMLhelpers.VerifyStagingError), "The staging error is not populated correctly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"}, priority=0)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public String patientSixteenGOS1Claim(String browser,String environment, String clientName, Boolean evidence, String scriptKey ) throws InterruptedException, IOException, AWTException
	{
		String ClaimNo = null;
		/*String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 6);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 7);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 8);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 9);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 10);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 11);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_ClaimDuplication_11", 12);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		setup(browser, environment, clientName,module);
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation("BOOTS OPTICIANS (LONDONDERRY)",OPHomePage.class);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(patDetNum, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		
		if(evidence)
		{
			for (String key:keys)
			{
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}

		ClaimNo = ObjOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}
		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();		
		quit(browser);*/
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String[]keyArray= strKeys.split(",");
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		setup(browser, environment, clientName,module);
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		boolean isHeaderPresent= ObjOPHomePage.verifyHeaderPresence(tabName);
		if(isHeaderPresent){
			SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
			ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		}
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey,environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			for (String key:keys)
			{
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}

		ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);

		return ClaimNo;
	}
	
	
	
	/****************************************************************
	 * TC 18552 - Validate system rejects claim, if GOS 1 Claim is submitted first via Portal followed by GOS 1 
	 * claim via BS with same Patient Details where Patient's age is 16yrs, sight test less than 23months and retest code absent
	 ********************************************************************************************************** */

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_Duplicate_GOS1Portal_GOS1BS (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_ClaimDuplication_11";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNamesMaster = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
	/*	String[] filenameListMaster = FileNamesMaster.split(",");
		int FileNumber = filenameListMaster.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameListMaster[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameListMaster[j], XMLType,key);			
		}*/
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		String ClaimNumber = patientSixteenGOS1Claim(browser,environment,clientName, evidence, key);
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		pageobjects.XMLParseHelpers.XMLhelpers.ChangeDateCRM(ClaimNumber, getDriver());
		tearDown(browser);
	
		XMLhelpers.CopyExcelData("OPTESTDATA.xlsx", "PATIENTDETAILS", 22, "XMLStagingCRM.xlsx", "DuplicateAttribute", key);		
		FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		XMLType = helpers.CommonFunctions.getXMLType(FileNamesMaster);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_Duplicate(XMLType,FileNames,key, evidence,"BS",environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
						setup(browser, environment, clientName, "CRMOP");
						XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
						objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
						if(objXMLhelpers.Process)
						{
							for(String AssetMessage : objXMLhelpers.AssetMessage)
							{
								setAssertMessage(AssetMessage, i);
								i = i + 1;							}
						}
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
						Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
					}
					Verify.verifyTrue((JobRun), "The System job run is not done properly");
		
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");	
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}

	

	
	/****************************************************************
	 * TC 6406 - To validate GOS 1 claim for Mandatory data present
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS1_6406 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS1_BS_BR_7";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String MasterFile = "GOS1_BS_Master.xml";
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence,environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not executed properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}	
	Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}


	/****************************************************************
	 * TC 7069 - GOS3 is accepted if  Voucher type-Complex is passed along with all the supplements in XML file, then the claim is accepted
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS3_7069 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS3_BS_BR_129";
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		int FileNumber = filenameList.length;
		for(int j= 0;j<FileNumber;j++)
		{
			if(filenameList[j].contains("VOC"))
			{
				System.out.println(filenameList[j]);
				helpers.CommonFunctions.copyFile(VOCMasterFile, key);
			}
		}	
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		filenameList = FileNames.split(",");
		FileNumber = filenameList.length;
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		for(int j= 0;j<FileNumber;j++)
		{
			String XMLType = helpers.CommonFunctions.getXMLType(filenameList[j]);
			objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, filenameList[j], XMLType,key);			
		}
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}	
			String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS3(XMLType, key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers.Process)
				{
					for(String AssetMessage : objXMLhelpers.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}

	/****************************************************************
	 * TC 6926 - To validate that GOS 5 claim is processed if the mandatory data is present 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS5_6926 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS5_BS_BR_12";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String MasterFile = "GOS5_BS_Master.xml";
		helpers.CommonFunctions.copyFile(MainFile, MasterFile, key);
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, "BS_GOS5",key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				//XMLhelpers objXMLhelpers1 = new XMLhelpers(Process, assertMessage);
				XMLhelpers objXMLhelpersCRM = new XMLhelpers(Process, assertMessage);
				objXMLhelpersCRM = objXMLhelpersCRM.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpersCRM.Process)
				{
					for(String AssetMessage : objXMLhelpersCRM.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM is not happen" +objXMLhelpersCRM.Process);				
			}
			else
			{
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
			
		}
		else
		{
			setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	


	/****************************************************************
	 * TC 11683 - To validate GOS 6 claim for Mandatory data present 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS6_11683 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS6_BS_BR_7";
		//String key = "TC_GOS1_BS_Patient_under_16";	
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
	/*	String MasterType = helpers.CommonFunctions.Getmasterfilename(FileNames);
		helpers.CommonFunctions.copyFile(MasterType, key);*/
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, XMLType,key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers1 = new XMLhelpers(Process, assertMessage);
				objXMLhelpers1 = objXMLhelpers1.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers1.Process)
				{
					for(String AssetMessage : objXMLhelpers1.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers1.Process), "The Verification on CRM is not happen" +objXMLhelpers1.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
	//	setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}

	/****************************************************************
	 * TC 6112 - To validate GOS 4, if reading addition more than 4 AND Reading power determine higher voucher and Bifocal is selected, 
	 * Bifocal voucher is determined with Reading segment, then the claim should be accepted.
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_GOS4_6112 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS4_BS_BR_105";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, "BS_GOS4",key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.BSXMLProcess_GOS156(XMLType,key, evidence, environment);
		if(objXMLhelpersstaging.Process)
		{
			for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				XMLhelpers objXMLhelpers1 = new XMLhelpers(Process, assertMessage);
				objXMLhelpers1 = objXMLhelpers1.XMLProcess_VerifyONCRM(getDriver(),key, evidence);
				if(objXMLhelpers1.Process)
				{
					for(String AssetMessage : objXMLhelpers1.AssetMessage)
					{
						setAssertMessage(AssetMessage, i);
						i = i + 1;
					}
				}
				Verify.verifyTrue((objXMLhelpers1.Process), "The Verification on CRM is not happen" +objXMLhelpers1.Process);				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}

	
}


