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
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
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
import pageobjects.OP.OPSearchGOS6PVN;
import pageobjects.PL.ProcessorPLHelpers;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;
import pageobjects.XMLParseHelpers.*;


@Listeners(ListenerClass.class)
public class XML_StagingCRMScripts extends BaseClass
{
	String MainFile = "XMLFILEDATA.xlsx";
	String VOCMasterFile = "GOS3 VO Master.xml";
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_XML_BR_17";
		boolean JobRun = false;
		String Responsecode = XMLhelpers.GetResponsecodeXML(key);
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
					setup(browser, environment, clientName,"CRMOP","PCSECLAIMPROCESSOR");
					String ClaimStatusCRM = XMLhelpers.GetClaimsCRM_BS(getDriver(), key);
					String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"Expected CLAIM STATUS IN CRM");
					AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(getDriver());
					String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"CLAIM ID");
					String ClaimStaCRM =  ObjAdvancedFindResult.getClaimCRM();
					setAssertMessage("The Claims status on CRM is  " + ClaimStaCRM, 7);
					if(evidence)
					{
						ObjAdvancedFindResult.screenshots(key + ClaimStaCRM+"CRMStatus");
					}
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
						ObjAdvancedFindResult.screenshots(key + ClaimStaCRM+"PaymentLines");
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
						Verify.verifyNotEquals(count == 0, "The incorrect CRM error is displayed ");
					}
					else
					{
						System.out.println("The CRM claims status is  " +ClaimStatusCRM );
						setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
					}
			
				}
				Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}
			Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched"+StagingStatus);
				
			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		
	}*/
	
	
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_GOS3(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_XML_BR_17";
		boolean JobRun = false;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetVocherCodefile = filenameList[0];
		//System.out.println(FileName);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
		//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
		 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(filenameList[0],"UpdatedAttribute", AttributeName,key);
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
			String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
			ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Voucher-Code", VoucherCode, 2);
			ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Authorisation-Code", AuthorisationCode, 2);
			List<String> AttributeNamesVocAuthCodes = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "VocAuthCode", 1);
			for(String AttributeNamesVocAuthCode : AttributeNamesVocAuthCodes )
			{
			//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
			 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(filenameList[1],"VocAuthCode" ,AttributeNamesVocAuthCode,key);
			}
			String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[1],key,environment);
			if(ResponsecodeUpdate != null)
			{
				System.out.println("The Response code are " + ResponsecodeUpdate);
				setAssertMessage("The Response code is generated " + ResponsecodeUpdate, 1);
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
					setup(browser, environment, clientName,"CRMOP","PCSECLAIMPROCESSOR");
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
						Verify.verifyNotEquals(count == 0, "The incorrect CRM error is displayed ");
					}
					else
					{
						System.out.println("The CRM claims status is  " +ClaimStatusCRM );
						setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
					}
			
				}
				Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}
			Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched"+StagingStatus);
				
			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
			Verify.verifyTrue((ResponsecodeUpdate != null), "The Response code after update Auth and Voucher code  is blank "+ResponsecodeUpdate);
		}
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		
	}
	*/

	
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_GOS4(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_XML_BR_17";
		boolean JobRun = false;
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetVocherCodefile = filenameList[0];
		//System.out.println(FileName);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
		//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
		 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(filenameList[0],"UpdatedAttribute", AttributeName,key);
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
			String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
			ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Voucher-Code", VoucherCode, 2);
			ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Authorisation-Code", AuthorisationCode, 2);
			List<String> AttributeNamesVocAuthCodes = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "VocAuthCode", 1);
			for(String AttributeNamesVocAuthCode : AttributeNamesVocAuthCodes )
			{
			 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(filenameList[1],"VocAuthCode" ,AttributeNamesVocAuthCode,key);
			}
			List<String> GOS4AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
			for(String GOS4AttributeName : GOS4AttributeNames )
			{
			 helpers.CommonFunctions.ReadXML_UpdateCodeValue_AttributeGOS4(filenameList[2],"UpdatedAttribute", GOS4AttributeName,key);
			}
			String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[2],key);
			if(ResponsecodeUpdate != null)
			{
				System.out.println("The Response code are " + ResponsecodeUpdate);
				setAssertMessage("The Response code is generated " + ResponsecodeUpdate, 1);
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
					setup(browser, environment, clientName,"CRMOP","PCSECLAIMPROCESSOR");
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
						Verify.verifyNotEquals(count == 0, "The incorrect CRM error is displayed ");
					}
					else
					{
						System.out.println("The CRM claims status is  " +ClaimStatusCRM );
						setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
					}
			
				}
				Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}
			Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched"+StagingStatus);
				
			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
			Verify.verifyTrue((ResponsecodeUpdate != null), "The Response code after update Auth and Voucher code  is blank "+ResponsecodeUpdate);
		}
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		
	}
	*/
	
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_GOS156(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
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
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(FileNames,key);
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
						Verify.verifyNotEquals(count == 0, "The incorrect CRM error is displayed ");
					}
					else
					{
						System.out.println("The CRM claims status is  " +ClaimStatusCRM );
						setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
					}
			
				}
				Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}
			Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched"+StagingStatus);
				
			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
		
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		
	}*/
	
	
	/*************************************************************************************
	 * TC1183 - To validate that system processes GOS1 Claims & generate Payment Lines,
	 *  if The patient is under 16 & is eligible for Free Sight Test.
	 *************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS1_1183 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, SQLException, ParseException
	{
		String key = "OPT_GOS1_XML_EC_3";
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType,key, evidence,environment);
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
	
/*	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS5_1458 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS5_XML_BR_13";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		objXMLhelpers = objXMLhelpers.XMLProcess_GOS156(key, evidence,environment);
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
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
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	*/
		
	
	/*************************************************************************************
	 * TC366 - "Supplements Validation" -To validate for GOS 3, if correct Tint/ Prism values are passed in Prescription Tag and Claim tag  
	 * is passed in XML file, then the claim is  processed for payment.
	 *************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS3_366 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS3_XML_BR_32";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		int FileNumber = filenameList.length;
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
			XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
			String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
			objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS3(XMLType,key, evidence,environment);
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
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_5741 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS4_XML_BR_163";
		// Below section is commented out as previouosly GOS4 form is dependent on GOS3 form which is not a case for now.
		/*int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		objXMLhelpers = objXMLhelpers.XMLProcess_GOS4(key, evidence);
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
				setup(browser, environment, clientName,"CRMOP","SUPERUSER");
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
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");*/
		
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType, key, evidence,environment);
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

	/*	***********************************************************************************************************
	 * Amit Rasal : - 16968- This is Regression Test Case - OPT_GOS6PVN_PMS_BR_09. To verify, GOS6 PVN is created  
	 * successfully for 6 patients with notification date 3 weeks before.
	 	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS6_PVN_16968(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS6PVN_PMS_BR_09";
		String sheetName = "PVNAttributes";
		String dataFileName = "XMLStagingCRM.xlsx";
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		int count = 0;
		int i=1;
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
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
		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetPvnCodefile = filenameList[0];
		String GetPvnFile = filenameList[1];
		
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		
		//System.out.println(FileName);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(dataFileName, sheetName, 1);
		for(String AttributeName : AttributeNames )
		{
		//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
		 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_Test(filenameList[0],dataFileName,sheetName, AttributeName,key, 22);
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String pvnReference = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Success PVN-Reference=");
			System.out.println(pvnReference);
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(GetPvnFile, "PCSE-OP-PMS-GOS6-PVN-Get-v2", "PVN-Reference", pvnReference, key);
			String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[1],key,environment);
			if(ResponsecodeUpdate != null)
			{
				System.out.println("The Response code are " + ResponsecodeUpdate);
				setAssertMessage("The Response code is generated " + ResponsecodeUpdate, 1);
			String StagingStatus = XMLhelpers.getPVNStatus(key, pvnReference, environment);
			if (StagingStatus.equalsIgnoreCase("Rejected"))
			{
				count = XMLhelpers.PVNStatus_Rejected(key,environment);
			}
			
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
						//count = XMLhelpers.PVNStatus_Rejected(key,environment);
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
					
					
				/*	List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
					for(String SystemJob : SystemJobs)
					{
					 JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
					}
					if(JobRun)
					{
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
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
					
					}*/
				}
				
					Verify.verifyTrue((StagingStatus.equalsIgnoreCase(ExpStatus)), "The expected & actual staging status are not matching.");
				
		
				}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
			
			Verify.verifyTrue((ResponsecodeUpdate != null), "The Response code after update Auth and Voucher code  is blank "+ResponsecodeUpdate);
		}
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
			
		
	}
	

	/*************************************************************************************
	 * TC16972 - To Verify, while creation of second GOS6 PVN with the same name, same address 
	 * by different contractor  a warning message is displayed
	 *************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS6_PVN_16972(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS6PVN_PMS_BR_13";
		String sheetName = "PVNAttributes";
		String dataFileName = "XMLStagingCRM.xlsx";
		boolean JobRun = false;
		int count =0;
		int i=1;
		boolean Process = false;
		List<String> assertMessage = null;
	//	setup(browser, environment, clientName,"CRMOP","SUPERUSER");
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
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetPvnCodefile = filenameList[0];
		String GetPvnFile = filenameList[1];
	
	//	boolean JobRun = false;
	
		XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
		String ExpStatusUpdate = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
		//System.out.println(FileName);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(dataFileName, sheetName, 1);
		for(String AttributeName : AttributeNames )
		{
		//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
		 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_Test(filenameList[0],dataFileName,sheetName, AttributeName,key,5);
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key,environment);
		String pvnReference = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Success PVN-Reference=");
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			setAssertMessage("The Response code is generated " + Responsecode, 1);
			String StagingStatus = XMLhelpers.getPVNStatus(key, pvnReference, environment);
			if (StagingStatus.equalsIgnoreCase("Rejected"))
			{
				count = XMLhelpers.PVNStatus_Rejected(key,environment);
			}
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				setAssertMessage("The Response code is generated " + StagingStatus, 2);
				//String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				String ExpStatus = "Accepted";
				if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					setAssertMessage("The Correct staging status is displayed " + StagingStatus, 3);
					String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[1],key,environment);
					String pvnReferenceUpdate = helpers.CommonFunctions.GetValueFormParagaraph(ResponsecodeUpdate, "Success PVN-Reference=");
					System.out.println(pvnReferenceUpdate);
					if(ResponsecodeUpdate != null)
					{
						System.out.println("The Response code are " + ResponsecodeUpdate);
						setAssertMessage("The Response code is generated " + ResponsecodeUpdate, 1);
						String StagingStatusUpdate = XMLhelpers.getPVNStatus(key, pvnReferenceUpdate,environment);
					if (StagingStatusUpdate != null)
						{
						if (StagingStatusUpdate.equalsIgnoreCase("Rejected"))
						{
							count = XMLhelpers.PVNStatus_Rejected(key,environment);
						}
						
						if(StagingStatusUpdate.equalsIgnoreCase(ExpStatusUpdate))
						{
							System.out.println("The Correct staging status is displayed " + StagingStatusUpdate);
							setAssertMessage("The Correct staging status is displayed " + StagingStatusUpdate, 3);
							
							if(StagingStatusUpdate.equalsIgnoreCase("Rejected"))
							{
								//count = XMLhelpers.PVNStatus_Rejected(key,environment);
								if (count == 0)
								{
									System.out.println("The errors are matching with expected errors.");
									
								}
								Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatusUpdate);
							}
							if(StagingStatusUpdate.equalsIgnoreCase("Accepted"))
							{
								System.out.println("The Staging status is  " + StagingStatusUpdate);
								setAssertMessage("The Response code is generated " + StagingStatusUpdate, 4);
							}
							System.out.println("The Staging Process is Completed  " + StagingStatusUpdate);
							setAssertMessage("The Staging Process is Completed " + StagingStatusUpdate, 5);
						}
						
							
				Verify.verifyTrue((StagingStatusUpdate.equalsIgnoreCase(ExpStatusUpdate)), "The expected & actual staging status of Duplicate GOS6 does not match are not matching.");
					}
					Verify.verifyTrue((StagingStatusUpdate != null), "The Staging status for second message is not caputured "+StagingStatusUpdate);
					List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
					for(String SystemJob : SystemJobs)
					{
					 JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
					}
					if(JobRun)
					{
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
						objXMLhelpers = objXMLhelpers.XMLProcess_VerifyONCRM_PVNStatus(getDriver(),key, evidence, pvnReferenceUpdate);
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
					
					}
					Verify.verifyTrue((ResponsecodeUpdate != null), "The Response code is blank "+ResponsecodeUpdate);
				}
			//	XMLhelpers.StagingProcess(StagingStatus,key);
				/*List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
				for(String SystemJob : SystemJobs)
				{
				 JobRun = 	helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
				}
				if(JobRun)
				{
					System.out.println("The Job Run Sucessfully " );
					setAssertMessage("The Job Run Sucessfully " , 6);
					Thread.sleep(3000);
					setup(browser, environment, clientName,"CRMOP","PCSECLAIMPROCESSOR");
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
						Verify.verifyNotEquals(count == 0, "The incorrect CRM error is displayed ");
					}
					else
					{
						System.out.println("The CRM claims status is  " +ClaimStatusCRM );
						setAssertMessage("The CRM claims status is " +ClaimStatusCRM, 9);
					}
			
				}
				Verify.verifyTrue(JobRun, "The Job Run is not happened sucessfully");
				}*/
			Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Staging status is not matched"+StagingStatus);
				
			}
			Verify.verifyTrue((StagingStatus != null), "The Staging status is not caputured "+StagingStatus);
		}
		
		Verify.verifyTrue((Responsecode != null), "The Response code is blank "+Responsecode);
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		
		
	}
	
	/*************************************************************************************
	 * TC18613 - OPT_CR06_GOS4_PMS_07 - Verify that the error message is displayed in the BRE (BizTalk End)
	 *  if any negative claim value is passed within the Claim section for GOS 4 PMS
	 * @throws ParseException 
	 *************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_CR_18613 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_CR06_GOS4_PMS_07";
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType,key, evidence,environment);
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
				Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM has failed, please check assert messages.");				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	
	/*************************************************************************************
	 * TC18575 - OPT_CR10_GOS4_PMS_08_C1 - Verify prescription change to Prism and Base in GOS 4 PMS
	 *************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_18575_C1 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_CR10_GOS4_PMS_08_C1";
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
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		
		String FailureMessage = XMLhelpers.XMLProcess_GOS156_SchFailure(XMLType,key,evidence, environment);
		
		String ExpFailureMessage = "The 'http://www.capita.co.uk/pcse/schema/ophthalmicpayments/GOS:Prism' element has an invalid value according to its data type.&quot;.";
			
		Assert.assertTrue(FailureMessage.contains(ExpFailureMessage), "The failure message does not have expected contains. Expected: "+ExpFailureMessage+"But Found Actual: "+FailureMessage);
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		
		
	}
	
	
	/*************************************************************************************
	 * TC18575 - OPT_CR10_GOS4_PMS_08_C2 - Verify prescription change to Prism and Base in GOS 4 PMS
	 *************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_18575_C2 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_CR10_GOS4_PMS_08_C2";
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
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		String FailureMessage = XMLhelpers.XMLProcess_GOS156_SchFailure(XMLType, key,evidence, environment);
		
		String ExpFailureMessage = "The 'http://www.capita.co.uk/pcse/schema/ophthalmicpayments/GOS:Prism' element has an invalid value according to its data type.&quot;.";
		
		
		Assert.assertTrue(FailureMessage.contains(ExpFailureMessage), "The failure message does not have expected contains. Expected: "+ExpFailureMessage+"But Found Actual: "+FailureMessage);
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		
		
	}
	
	/*************************************************************************************
	 * TC18575 - OPT_CR10_GOS4_PMS_08_C3 - Verify prescription change to Prism and Base in GOS 4 PMS
	 *************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_18575_C3 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_CR10_GOS4_PMS_08_C3";
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
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		String FailureMessage = XMLhelpers.XMLProcess_GOS156_SchFailure(XMLType,key,evidence, environment);
		
		String ExpFailureMessage = "The 'http://www.capita.co.uk/pcse/schema/ophthalmicpayments/GOS:Prism' element has an invalid value according to its data type.&quot;.";
		
		
		Assert.assertTrue(FailureMessage.contains(ExpFailureMessage), "The failure message does not have expected contains. Expected: "+ExpFailureMessage+"But Found Actual: "+FailureMessage);
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		
	}
	
	
	/*************************************************************************************
	 * TC18575 - OPT_CR10_GOS4_PMS_08_C4 - Verify prescription change to Prism and Base in GOS 4 PMS
	 * @throws ParseException 
	 *************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_18575_C4 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_CR10_GOS4_PMS_08_C4";
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
	
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
		objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.UpdateMasterAttribute(environment, FileNames, "PMS_GOS4",key);
		if(objXMLhelpersUpdateMasterData.Process)
		{
			for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			String XMLType = helpers.CommonFunctions.getXMLType(FileNames);
			XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
			objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType,key, evidence,environment);
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
				Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM has failed, please check assert messages.");				
			}
			Verify.verifyTrue((JobRun), "The System job run is not done properly");
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	/****************************************************************
	 * TC 18562 -Validate system rejects claim if GOS 6 Claim is submitted via portal first
	 * followed by GOS 1 claim via PMS with same patient details where  patient age is 70 years
	 * and over and sight test less than 11 months and retest code not present
	 * @throws ParseException 
	 ********************************************************************************************************** */

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_Duplicate_GOS6Portal_GOS1PMS (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_ClaimDuplication_07";
		int i = 1;
		String ClaimNumber = GOS6Claim_SuccessfulFlow_PatientAbove70YearsLastSightDateBefore11Months(browser,environment,clientName, evidence);
	//	String ClaimNumber = "ADA19321";
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		pageobjects.XMLParseHelpers.XMLhelpers.ChangeDateCRM(ClaimNumber, getDriver());
		tearDown(browser);
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
		XMLhelpers.CopyExcelData("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", 6, "XMLStagingCRM.xlsx", "DuplicateAttribute", key);
		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpersstaging = new XMLhelpers(Process, assertMessage);
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_Duplicate(FileNames,key, evidence,"PMS", environment);
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
						Verify.verifyTrue((objXMLhelpers.Process), "The Verification on CRM is not happen" +objXMLhelpers.Process);				
					}
					Verify.verifyTrue((JobRun), "The System job run is not done properly");
		
		}
		Verify.verifyTrue((objXMLhelpersstaging.Process), "The Staging Process is not completed");
		}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	/************************************************************************************
	 * Amit R. - GOS6 Regression Test - 12365 - OPT_GOS6_PRT_BR_9 - Validate system rejects claim if GOS 6 Claim is submitted via
	 *  portal first followed by GOS 1 claim via PMS with same patient details where  patient age is 70 years and over 
	 *  and sight test less than 11 months and retest code not present
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","CLONE"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public String GOS6Claim_SuccessfulFlow_PatientAbove70YearsLastSightDateBefore11Months(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String ClaimsNumber = null;
		List<String> keys = Arrays.asList("OPT_GOS6_PRT_BR_9");
		String PaymentLineDueDate = null;
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation("BOOTS OPTICIANS (LONDONDERRY)",OPHomePage.class);
	//	SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
	//	ObjOPHomePage = objSelectOrganisation.selectOrganisation();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5, environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetailsWithLastSightDate(6,330);
		ObjOPCreateGOS6PVN = ObjOPCreateGOS6PVN.addreasonforSubmit();
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("PVN submitted"))
			{
				System.out.println("The GOS6 pvn reference number " +refNo+" gets submitted successfully.");
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "PVN submitted", "The GOS6 pvn reference number does not get submitted successfully.");
			}
		}
		else{
			Assert.assertTrue(pvnSubFlag, "The GOS6 PVN Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPCreateGOS6PVN.clickCloseOnResultPopup();
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPSearchGOS6PVN ObjOPSearchGOS6PVN = ObjOPGOS6Options.clickOnSearchGOS6PVN();
		OPGOS6PVNView ObjOPGOS6PVNView = ObjOPSearchGOS6PVN.searchGOS6VPN(refNo, environment);
		OPGOS6PatientDetails ObjOPGOS6PatientDetails = ObjOPGOS6PVNView.clickONOpenGOSSixPVN();
		OPGOS6PatientEligibility ObjOPGOS6PatientEligibility = ObjOPGOS6PatientDetails.PatientDetailsEntered(1,environment);
		//OPGOS6PatientEligibility ObjOPGOS6PatientEligibility = ObjGOS6PatientDetails.PatientDetailsEnteredWithLastSightDate(3, 731);
		OPGOS6PatientDeclaration ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientEligibility.PatientEligibilityDetailsEntered(6);
		OPGOS6PerformerDeclaration ObjOPGOS6PerformerDeclaration = ObjOPGOS6PatientDeclaration.PatientDeclarationDetailsEntered(1);
		ObjOPGOS6PerformerDeclaration.selectProvidedOptions(3);
		//ObjOPGOS6PerformerDeclaration.selectFirstVoucherType("A");
		//ObjOPGOS6PerformerDeclaration.selectSecondVoucherType("B");
		ObjOPGOS6PerformerDeclaration.enterAddressManually(6);
		ObjOPGOS6PerformerDeclaration.enterSignatoryDetails();
		OPGOS6ContractorDeclaration ObjOPGOS6ContractorDeclaration = ObjOPGOS6PerformerDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS6ContractorDeclaration.GOS6ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		ClaimsNumber = ObjOPGOS6ContractorDeclaration.getClaimNumber(keyname);
		ObjOPGOS6ContractorDeclaration.enterSignatoryDetails();

		Boolean clmSubFlag = ObjOPGOS6ContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS6ContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimsNumber+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}
		ObjOPHomePage = ObjOPGOS6ContractorDeclaration.clickCloseOnResultPopup();
		quit(browser);
		
		

		return ClaimsNumber;
	}
	
		
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void XMLProcess_ReadAllAttribute (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"OP");
		String key = "OPT_ClaimDuplication_07";
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLhelpers objXMLhelpers = new XMLhelpers(getDriver());
		objXMLhelpers.ReadAllAttribute(FileNames,key, evidence);
	}
	
	/****************************************************************
	 * TC 846 - Mandatory Fields" To validate that GOS 1 claim is processed if the mandatory data is present
	 * @throws ParseException 
	 ********************************************************************************************************** */
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS1_846 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS1_XML_BR_16";
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType,key, evidence,environment);
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
	 * TC 1428 - Mandatory Fields To validate that GOS 5 claim is processed if the mandatory data is present 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS5_1458 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS5_XML_BR_13";
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType, key, evidence,environment);
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
	 * TC 5683 - To validate GOS 4, if Under Reading  Prescription are passed as A/B/C , Distance Prescription are passed as D
	 *  and Bifocal glasses are determined by Distance is passed in XML file, then the claims accepted 
	 * @throws ParseException 
	 ********************************************************************************************************** */
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS4_5683 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS4_XML_BR_132";
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType,key, evidence,environment);
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
				JobRun = helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
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
	 * TC 12028 - To validate GOS 6 claim Validity over 6months
	 * @throws ParseException 
	 ********************************************************************************************************** */
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity","Regression","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PMS_GOS6_12028 (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS6_XML_BR_3";
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
		objXMLhelpersstaging = objXMLhelpersstaging.XMLProcess_GOS156(XMLType, key, evidence,environment);
		for(String AssetMessage : objXMLhelpersstaging.AssetMessage)
		{
			setAssertMessage(AssetMessage, i);
			System.out.println(AssetMessage);
			i = i + 1;
		}
		if(objXMLhelpersstaging.Process)
		{
			
			List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
			for(String SystemJob : SystemJobs)
			{
				JobRun = helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
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
	 * TC 12028 - To validate GOS 6 claim Validity over 6months
	 * @throws ParseException 
	 ********************************************************************************************************** */
	
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","XML","Sanity"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void BS_PMS_XML (String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		int i = 1;
		boolean Process = false;
		boolean JobRun = false;
		List<String> assertMessage = null;
		List<String>currentOptions =  ExcelUtilities.GetAllKeyInArray("BULKXMLFILEDATA.xlsx", "XML");
		System.out.println(currentOptions);
		for (String key : currentOptions) 
		{
			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("BULKXMLFILEDATA.xlsx", "XML", key,1);
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
				XMLhelpers objXMLhelpers = new XMLhelpers(Process, assertMessage);
				if(FileNames.contains("_BS"))
				{
					objXMLhelpers = objXMLhelpers.BULKBSXMLProcess_GOS156(XMLType, key, evidence,environment);
				}
				else
				{
					objXMLhelpers = objXMLhelpers.BULKXMLProcess_GOS156(XMLType, key, evidence,environment);
				}
				for(String AssetMessage : objXMLhelpers.AssetMessage)
				{
					setAssertMessage(AssetMessage, i);
					System.out.println(AssetMessage);
					i = i + 1;
				}
				if(objXMLhelpers.Process)
				{			
					List<String> SystemJobs = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
					for(String SystemJob : SystemJobs)
					{
						JobRun = helpers.CommonFunctions.ExcuteSystemJob_XML(SystemJob, environment);
					}
					if(JobRun)
					{
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
						XMLhelpers objXMLhelpersCRM = new XMLhelpers(Process, assertMessage);
						objXMLhelpersCRM = objXMLhelpersCRM.BULKXMLProcess_VerifyONCRM(getDriver(),key, evidence);
						if(objXMLhelpersCRM.Process)
						{
							for(String AssetMessage : objXMLhelpersCRM.AssetMessage)
							{
								setAssertMessage(AssetMessage, i);
								i = i + 1;
							}
					
						}
						Verify.verifyTrue((objXMLhelpersCRM.Process), "The Verification on CRM is not happen for " +key );
						
					}
					else
					{
						setup(browser, environment, clientName,"CRMOP","SUPERUSER");
					}
						Verify.verifyTrue((JobRun), "The System job run is not done properly " +key);
			
				}
				else
				{
					setup(browser, environment, clientName,"CRMOP","SUPERUSER");
				}
				Verify.verifyTrue((objXMLhelpers.Process), "The Staging Process is not completed");
				tearDown(browser);
				}
		Verify.verifyTrue((objXMLhelpersUpdateMasterData.Process), "The Master Data is not updated Sucessfully");
		
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}*/
	
	
	
	
}


