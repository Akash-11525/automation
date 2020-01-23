package testscripts;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import helpers.DatabaseHelper;
import helpers.OPHelpers;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.PaymentInstructionFile;
import pageobjects.GMP.VarianceReports;
import pageobjects.OP.OPMakeAClaim;
import pageobjects.OP.OPOpticalVoucherRetrieval;
import pageobjects.OP.MyNotification;
import pageobjects.OP.OPContractorDeclaration;
import pageobjects.OP.OPGOS3PatientDeclaration;
import pageobjects.OP.OPGOS3PatientDetails;
import pageobjects.OP.OPGOS3PatientEligibility;
import pageobjects.OP.OPGOS3Prescription;
import pageobjects.OP.OPGOS3SupplierDeclaration;
import pageobjects.OP.OPHomePage;
import pageobjects.OP.OPPatientDeclaration;
import pageobjects.OP.OPPatientDetails;
import pageobjects.OP.OPPatientEligibility;
import pageobjects.OP.OPPerformerDeclaration;
import pageobjects.OP.OPSearchClaim;
import pageobjects.OP.OPSearchForClaim;
import pageobjects.OP.OPStatement;
import pageobjects.OP.OP_StatementDetails;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class OP_GOS1ClaimScripts extends BaseClass
{
	/* Amit R : - This is Sanity Test Cases for Patient Under 16 Yr Age Group & created Claim through GOS1 form. 
	//The submitted claim is found CRM with expected status "Accepted For Payment".
	// RBAC Test Case - Validate if user having user role "PCSEClaimsProcessor" with privilege "ManageAllGOSclaims" is logged in, 
	//then user is allowed to make a GOS 1 claim and can access all screens and submit the claim successfully*/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","CLONE","RegressionNewEnv"}, priority=0)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientUnderSixteenGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_BR_15";
		
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
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


		String key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}

	// Amit R : - This is Sanity Test Cases for Patient over 40 Yr Age Group & created Claim through GOS1 form. 
	//The submitted claim is found CRM with expected status "Accepted For Payment".
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Sanity","Regression"}, priority=1)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientoverFourtyGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_BR_61";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
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
		// Amit - 
		quit(browser);
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule, CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,key+"_PaymentLineStatus", key+"GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))//ExpectedpaymentLineStatus to be updated in assert also.
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}

	// Amit R. - Sanity Test Case Mandatory Fields verification on Patient Details Screen. 
	

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Sanity","Regression"}, priority=3)
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientDetailsRequiredFieldMessages(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException
	{
		String scriptKey="OPT_GOS1_PTR_BR_59";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String validationMsgSheet= ExcelUtilities.getExcelParameterByModule("GOS1","ValidationMessage");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expMsgCol= scriptKey;
		String expMsgCol2= scriptKey+"_2";
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		ObjPatientDetails.clearDateOfSightTestField();
		ObjPatientDetails.clickOnSaveandNextButton();
		if(evidence)
		{
			
			ObjPatientDetails.GOS1PatientDetailsErrorsSnap(key+"_ValidationError");
			
		}
		List<String> ActualErrorMessages = ObjPatientDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save and Next: "+ActualErrorMessages);
		setAssertMessage("Actual Error Messages on Save and Next: "+ActualErrorMessages, 1);
		List<String> ExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol);
		System.out.println("Expected Error Messages on Save and Next: "+ExpectedErrorMessages);
		//setAssertMessage("The expected error messages on Save and Next:: "+ExpectedErrorMessages, 2);
		
		List<String> unmatchedList = CommonFunctions.compareStrings(ActualErrorMessages, ExpectedErrorMessages);
		SoftAssert softAssertion= new SoftAssert();
		
		
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error list on Save and Next action is matching with expected list.");
			setAssertMessage("Actual error list on Save and Next action is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Save and Next action is not matching with Expected Error list.");
		}

		
		// Verify the Mandatory Error messages after Save Action
		ObjPatientDetails.clearDateOfSightTestField();
		ObjPatientDetails.clickOnSaveButton();
		if(evidence)
		{
			
			ObjPatientDetails.GOS1PatientDetailsErrorsSnap(key+"_ValidationError2");
			
		}
		List<String> ActualErrorMessagesonSave = ObjPatientDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol2);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list.");
		}

		/*if(ActualErrorMessagesonSave.equals(ExpectedErrorMessagesonSave))
		{
			setAssertMessage("Actual error messages are matched with expected error messages on Save and Next Action.", 2);
			System.out.println("Actual error messages are matched with expected error messages on Save and Next Action.");
		}
		else
		{
			Verify.verifyEquals(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave, "The actual error list & expected error list does not match on Save and Next Action.");
		}*/
		softAssertion.assertAll();
	}

	// Case 4: Regression Test - Patient Under 16 & Patient Partner receives Universal Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.
	// Patient details are entered with Sight Test Date selected as "First Test"

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","evidence","CloneSanity","CLONE","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientunderSixteenwithUniversalCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_BR_13";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule, CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}

	// Case 5: Regression Test - Patient Under 16 & Patient Partner receives Jobseeker allowance. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.
	// Patient details are entered with Last sight test as "Unknown"
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientunderSixteenwithJobseekerAllowanceGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_BR_15";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}
			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}


	// Case 6: Regression Test - Patient Under 16 & Patient Partner receives Pension Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientunderSixteenwithPensionCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_9";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
		
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}


	// Case 7: Regression Test - Patient over 40 & Patient Partner receives Universal Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientoverFourtywithUniversalCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_52";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+ " gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
							
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}

		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
		
	}


	// Case 8: Regression Test - Patient over 40 & Patient Partner receives Jobseeker Allowance. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientoverFourtywithJobseekerAllowanceGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_64";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}

	// Case 9: Regression Test - Patient over 40 & Patient Partner receives Pension Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" },enabled= false)
	@Parameters({ "browser","environment", "clientName" })
	public void patientoverFourtywithPensionCreditGOS1Claim(String browser,String environment, String clientName) throws InterruptedException, IOException
	{
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(8, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(8);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(1);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(1);

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber("CLAIM8");
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		quit(browser);


		// Creating new patient details & closing browser.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum =2;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM8",1);
		System.out.println("Claim Number:"+ValueForFieldValue);
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

				if (clmStatus.contains(claimStatus))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 4);
					System.out.println("The claim status in CRM is Accepted For Payment.");
				}
				Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

			}
			else
			{
				Assert.assertNotNull(title, "Title extracted from result record is NULL.");
			}

		}
		else
		{

			Assert.assertEquals(flag, true, "No records found under results");
		}

	}

	// Case 10: Regression Test - Patient over 40 & Patient receives Universal Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientoverFourtywithPatientReceivesUniversalCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_56";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}

		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
		
	}


	// Case 11: Regression Test - Patient over 40 & Patient receives Job seeker allowance. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientoverFourtywithPatientReceivesJobseekerAllowanceGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_58";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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
		

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");



							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}

		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
		
	}

	// Case 11: Regression Test - Patient over 40 & Patient receives Pension Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientoverFourtywithPatientReceivesPensionCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_60";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");



							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
		

	}
	// Case 12: Regression Test - Patient over 60, prisoner & Patient Partner receives Universal Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientoverSixtywithPartnerReceivesUniversalCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_31";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");



							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}

	// Case 13: Regression Test - Patient over 60 & Patient Partner receives Jobseeker Allowance. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientoverSixtywithPartnerReceivesJobseekerAllowanceCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_44";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
								// Verify Amount Due on Payment Line
								
								String PaymentLineAmountDue = paymentLineDetailsList.get(1);
								System.out.println(PaymentLineAmountDue);
								if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
								{
									System.out.println("The Payment Line amount due is matching");
									setAssertMessage("The Payment Line amount due is matching.", 3);
								}
								else
								{
									System.out.println("The Payment Line amount due is not matching");
									//setAssertMessage("The Payment Line status is not Pending.", 1);
									Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
								}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
		
	}	

	// Case 14: Regression Test - Patient over 60 & Patient Partner receives Pension Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientoverSixtywithPartnerReceivesPensionCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_46";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");

							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}	


	// Case 15: Regression Test - Patient over 60 & Patient receives Universal Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientoverSixtywithPatientReceivesUniversalCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_141";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence)
		{
			
			
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			
		}


		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
							
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}	


	// Case 16: Regression Test - Patient over 60 & Patient receives Pension Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" }, enabled= false)
	@Parameters({ "browser","environment", "clientName" })
	public void patientoverSixtywithPatientReceivesPensionCreditGOS1Claim(String browser,String environment, String clientName) throws InterruptedException, IOException
	{
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(16, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(16);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(1);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(1);

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber("CLAIM16");
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		quit(browser);


		// Creating new patient details & closing browser.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum =2;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM16",1);
		System.out.println("Claim Number:"+ValueForFieldValue);
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

				if (clmStatus.contains(claimStatus))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 4);
					System.out.println("The claim status in CRM is Accepted For Payment.");
				}
				Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

			}
			else
			{
				Assert.assertNotNull(title, "Title extracted from result record is NULL.");
			}

		}
		else
		{

			Assert.assertEquals(flag, true, "No records found under results");
		}

	}	

	// Case 17: Regression Test - Patient is Student & Patient receives Universal Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientStudentwithPartnerReceivesUniversalCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_15";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey,environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence)
		{
			
			
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
							setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}	

	// Case 18: Regression Test - Patient is Student & Patient receives Jobseeker allowance. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientStudentwithPartnerReceivesJobseekerAllowanceGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_17";
		
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
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
							setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}	

	// Case 19: Regression Test - Patient is Student & Patient receives Pension Credit. Verification here is claim is submitted successfully & Status of Claim in CRM is Accepted for Payment.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void patientStudentwithPartnerReceivesPensionCreditGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_EC_11";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence)
		{
			
			
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo+" gets submitted successfully.", 2);
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
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
							setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}	


	// Case 20: Regression Test - Last sight test date is mandatory when first test or not known checkbox not selected.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void lastSightTestDateisMandatory(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException
	{
		String scriptKey="OPT_GOS1_PTR_BR_72";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String validationMsgSheet= ExcelUtilities.getExcelParameterByModule("GOS1","ValidationMessage");

		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expMsgCol= scriptKey;
		
		setup(browser, environment, clientName,module);
		String key=strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		ObjPatientDetails.enterPerformerDetails(environment);
		ObjPatientDetails.enterPatientDetailswithoutLastSightTest(scriptKey);
		ObjPatientDetails.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjPatientDetails.GOS1PatientDetailsErrorsSnap(key+"_ValidationError");
		}
		
		List<String> ActualErrorMessages = ObjPatientDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages: "+ActualErrorMessages);
		setAssertMessage("The mandatory fields messages populated are: "+ActualErrorMessages, 1);
		List<String> ExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol);
		System.out.println("Expected Error Messages: "+ExpectedErrorMessages);

	/*	if(ActualErrorMessages.equals(ExpectedErrorMessages))
		{
			setAssertMessage("Actual error messages are matched with expected error messages on Save and Next Action.", 2);
			System.out.println("Actual error messages are matched with expected error messages on Save and Next Action.");
		}
		else
		{
			Verify.verifyEquals(ActualErrorMessages, ExpectedErrorMessages, "The actual error list & expected error list does not match on Save and Next Action.");
		}*/
		
		List<String> unmatchedList = CommonFunctions.compareStrings(ActualErrorMessages, ExpectedErrorMessages);
		SoftAssert softAssertion= new SoftAssert();
		
		
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error messages are matched with expected error messages when Last Sight Test date is required");
			setAssertMessage("Actual error messages are matched with expected error messages when Last Sight Test date is required.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "Actual error messages are not matching with expected error messages when Last Sight Test date is required");
		}
		
		softAssertion.assertAll();


	}	

	// Case 21: Regression Test - Verify Mandatory Fields on all screens.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP" })
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void allMandatoryErrorsAllPages(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException
	{
		String scriptKey="OPT_GOS1_PTR_BR_14";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String validationMsgSheet= ExcelUtilities.getExcelParameterByModule("GOS1","ValidationMessage");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expMsgCol= scriptKey;
		
		setup(browser, environment, clientName,module);
		String key=strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		//ObjPatientDetails.enterPatientDetailswithoutLastSightTest(20);
		ObjPatientDetails.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjPatientDetails.GOS1PatientDetailsErrorsSnap(key+"_ValidationError");
		}
		List<String> PatDetActualErrorMessages = ObjPatientDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages: "+PatDetActualErrorMessages);
		setAssertMessage("The mandatory fields messages populated for patient details are: "+PatDetActualErrorMessages, 1);
		List<String> PatDetExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol);
		System.out.println("Expected Error Messages: "+PatDetExpectedErrorMessages);
		
		setAssertMessage("The expected error messages on patient details: "+PatDetExpectedErrorMessages, 2);
		
		List<String> unmatchedList = CommonFunctions.compareStrings(PatDetActualErrorMessages, PatDetExpectedErrorMessages);
		SoftAssert softAssertion= new SoftAssert();
		
		
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error list on Patient Details is matching with expected list.");
			setAssertMessage("Actual error list on Patient Details is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Patient Details is not matching with Expected Error list.");
		}

		/*if(PatDetActualErrorMessages.equals(PatDetExpectedErrorMessages))
		{
			setAssertMessage("Actual error messages are matched with expected error messages in Patient Details Screen on Save and Next Action.", 2);
			System.out.println("Actual error messages are matched with expected error messages in Patient Details Screen on Save and Next Action.");
		}
		else
		{
			Verify.verifyEquals(PatDetActualErrorMessages, PatDetExpectedErrorMessages, "The actual error list & expected error list in Patient Details Screen does not match on Save and Next Action.");
		}*/

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		ObjOPPatientEligibility.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjOPPatientEligibility.GOS1PatientElgValErrors(key+"_ValidationError");
		}

		List<String> PatElgActualErrorMessages = ObjOPPatientEligibility.AcutalErrormessage();
		System.out.println("Actual Error Messages on Patient Eligibility Page: "+PatElgActualErrorMessages);
		setAssertMessage("The mandatory fields messages populated are on Patient Eligibility Page: "+PatElgActualErrorMessages, 4);
		List<String> PatElgExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol+"_2");
		System.out.println("Expected Error Messages on Patient Eligibility Page: "+PatElgExpectedErrorMessages);
		setAssertMessage("The expected error messages on Patient Eligibility Page: "+PatElgExpectedErrorMessages, 5);
		
		List<String> unmatchedListPatElg = CommonFunctions.compareStrings(PatElgActualErrorMessages, PatElgExpectedErrorMessages);
		
		
		
		if(unmatchedListPatElg.isEmpty())
		{
			System.out.println("Actual error list on Patient Eligibility is matching with expected list.");
			setAssertMessage("Actual error list on Patient Eligibility is matching with expected list.", 6);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedListPatElg.isEmpty(), "The Actual error list on Patient Eligibility is not matching with Expected Error list.");
		}
		/*if(PatElgActualErrorMessages.equals(PatElgExpectedErrorMessages))
		{
			setAssertMessage("Actual error messages are matched with expected error messages in Patient Eligibility Screen on Save and Next Action.", 4);
			System.out.println("Actual error messages are matched with expected error messages in Patient Eligibility Screen on Save and Next Action.");
		}
		else
		{
			Verify.verifyEquals(PatElgActualErrorMessages, PatElgExpectedErrorMessages, "The actual error list & expected error list in Patient Details Screen does not match on Save and Next Action.");
		}*/

		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		ObjOPPatientDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjOPPatientDeclaration.GOS1PatientDeclarionSnap(key+"_ValidationError");
		}


		List<String> PatDclActualErrorMessages = ObjOPPatientDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Patient Declaration Screen: "+PatDclActualErrorMessages);
		setAssertMessage("The mandatory fields messages on Patient Declaration Screen  populated are: "+PatDclActualErrorMessages, 7);
		List<String> PatDclExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol+"_3");
		System.out.println("Expected Error Messages on Patient Declaration Screen: "+PatDclExpectedErrorMessages);
		setAssertMessage("The expected error messages on Patient Declaration Screen: "+PatDclExpectedErrorMessages, 8);
		
		List<String> unmatchedListPatDlr = CommonFunctions.compareStrings(PatElgActualErrorMessages, PatElgExpectedErrorMessages);
		
		
		
		if(unmatchedListPatDlr.isEmpty())
		{
			System.out.println("Actual error list on Patient Declaration Screen is matching with expected list.");
			setAssertMessage("Actual error list on Patient Declaration Screen is matching with expected list.", 9);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedListPatDlr.isEmpty(), "The Actual error list on Patient Declaration Screen is not matching with Expected Error list.");
		}
		/*if(PatDclActualErrorMessages.equals(PatDclExpectedErrorMessages))
		{
			setAssertMessage("Actual error messages are matched with expected error messages in Patient Eligibility Screen on Save and Next Action.", 6);
			System.out.println("Actual error messages are matched with expected error messages in Patient Eligibility Screen on Save and Next Action.");
		}
		else
		{
			Verify.verifyEquals(PatDclActualErrorMessages, PatDclExpectedErrorMessages, "The actual error list & expected error list in Patient Details Screen does not match on Save and Next Action.");
		}*/

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		ObjOPPerformerDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjOPPerformerDeclaration.GOS1ProgressIndicatorSnap(key+"_ValidationError");
		}

		List<String> PerDclActualErrorMessages = ObjOPPerformerDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Performer Declaration Screen: "+PatDclActualErrorMessages);
		setAssertMessage("The mandatory fields messages on Performer Declaration Screen populated are: "+PerDclActualErrorMessages, 10);
		List<String> PerDclExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, expMsgCol+"_4");
		System.out.println("Expected Error Messages on Performer Declaration Screen: "+PerDclExpectedErrorMessages);
		
		setAssertMessage("The expected error messages on Performer Declaration Screen: "+PerDclExpectedErrorMessages, 11);
		
		List<String> unmatchedListPerDlr = CommonFunctions.compareStrings(PatElgActualErrorMessages, PatElgExpectedErrorMessages);
		
		
		
		if(unmatchedListPerDlr.isEmpty())
		{
			System.out.println("Actual error list on Performer Declaration Screen is matching with expected list.");
			setAssertMessage("Actual error list on Performer Declaration Screen is matching with expected list.", 12);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedListPerDlr.isEmpty(), "The Actual error list on Performer Declaration Screen is not matching with Expected Error list.");
		}

		/*if(PerDclActualErrorMessages.equals(PerDclExpectedErrorMessages))
		{
			setAssertMessage("Actual error messages are matched with expected error messages in Patient Eligibility Screen on Save and Next Action.", 8);
			System.out.println("Actual error messages are matched with expected error messages in Patient Eligibility Screen on Save and Next Action.");
		}
		else
		{
			Verify.verifyEquals(PerDclActualErrorMessages, PerDclExpectedErrorMessages, "The actual error list & expected error list in Patient Details Screen does not match on Save and Next Action.");
		}*/

		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);

		ObjOPContractorDeclaration.submitAction();
		softAssertion.assertAll();
	}	


	//Case 22 - Regression test - Verify close action before submitting an application
	// Amit - This test is not running end to end as Submitted Claim Page yet to develop.Currently no Close Claim button is available.[14/11]
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"} )
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void CloseActionGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException
	{
		String scriptKey="OPT_GOS1_PTR_BR_70";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expClaimStatus= values.get(4);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence)
		{
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);

		ObjOPHomePage = ObjOPContractorDeclaration.clickOnCloseButton();

		//String ClaimStatus = ObjOPHomePage.getStatusfromClaim(ClaimNo);
/*		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();*/ //commented by Akshay on 17052018 as home page is not being displayed now
		OPSearchClaim ObjOPSearchClaim= new OPSearchClaim(getDriver());
		String ClaimStatus = ObjOPSearchClaim.getStatusfromClaim(ClaimNo);
		

		if (ClaimStatus.equalsIgnoreCase(expClaimStatus))
		{
			System.out.println("Test Case is Passed.");
			setAssertMessage("The claim is" +ClaimNo+ " successfully closed & status is Draft.", 1);
		}

		else
		{
			Assert.assertEquals(ClaimStatus, expClaimStatus, "The Claim has status other than Closed. ");
		}
	}	
	
	//Case 23 - Regression test - Verify Cancel action before submitting an application
	// Amit - This test is not running end to end as Submitted Claim Page yet to develop. Cancel action is not working on 14/11/2017.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"},enabled= false)
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void CancelActionGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_BR_69";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String strAdvFindNum= values.get(4);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		String expClaimStatus= values.get(5);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		
		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);

		ObjOPContractorDeclaration.clickOnCancelClaimButton();
		String ExpCancelClaimBodyMsg = "Are you sure you wish to cancel this claim?";
		String ActCancelClaimBodyMsg = ObjOPContractorDeclaration.getCancelPopupMessage();
		
		if (ActCancelClaimBodyMsg.equalsIgnoreCase(ExpCancelClaimBodyMsg))
		{
			System.out.println("Test Case is Passed.");
			setAssertMessage("The cancel claim popup is displayed successfully.", 1);
		}

		else
		{
			Assert.assertEquals(ActCancelClaimBodyMsg, ExpCancelClaimBodyMsg, "The Cancel Claim Popup Body Text does not match. ");
		}
		
		ObjOPContractorDeclaration = ObjOPContractorDeclaration.clickOnNoButton();
		ObjOPContractorDeclaration.clickOnCancelClaimButton();
		ObjOPHomePage = ObjOPContractorDeclaration.clickOnYesButton();
		
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();
		String ClaimStatus = ObjOPSearchClaim.getStatusfromClaim(ClaimNo);

		//String ClaimStatus = ObjOPHomePage.getStatusfromClaim(ClaimNo);

		if (ClaimStatus.equalsIgnoreCase(expClaimStatus))
		{
			System.out.println("Test Case is Passed.Status of Claim is Cancelled.");
			setAssertMessage("The claim" +ClaimNo+ " is successfully Cancelled & status is Cancelled.", 2);
		}

		else
		{
			Assert.assertEquals(ClaimStatus, expClaimStatus, "The Claim has status other than Cancelled.");
		}
		
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify the status of respective claim is Cancelled in CRM application.
			setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum =advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM21",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ClaimNo);
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ClaimNo);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(expClaimStatus))
					{
						setAssertMessage("The claim status in CRM of claim" +ValueForFieldValue+ " is Cancelled.", 3);
						System.out.println("The claim status in CRM of claim" +ValueForFieldValue+ " is Cancelled.");
					}
					Assert.assertEquals(clmStatus, expClaimStatus, "The claim status in CRM is not expected as Cancelled.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");
			}

		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}	
	}	
		
	//Case 24 - Regression test - Verify Error message on conflicting options are selected.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void conflictingEleOptionGOS1Claim(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException
	{
		String scriptKey="OPT_GOS1_PTR_BR_20";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String validationMsgSheet= ExcelUtilities.getExcelParameterByModule("GOS1","ValidationMessage");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String[]keyArray= strKeys.split(",");
		String role= values.get(2);
		String tabName= values.get(3);
		
		setup(browser, environment, clientName,module);
		List<String> keys = Arrays.asList(keyArray);
		
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		
		if(evidence)
		{
			for (String key: keys)
			ObjOPPatientEligibility.GOS1PatientElgErrorSnap(key+"_ValidationError");
		}
		
		
		List<String> PatElgActualErrorMessages = ObjOPPatientEligibility.AcutalErrormessage();
		System.out.println("Actual Error Messages on Conflicting Patient Eligibility Options Selected: "+PatElgActualErrorMessages);
		setAssertMessage("Actual Error Messages on Conflicting Patient Eligibility Options Selected: "+PatElgActualErrorMessages, 1);
		List<String> PatElgExpectedErrorMessages = ExcelUtilities.getCellValuesByPosition(newTestDataFileName, validationMsgSheet, scriptKey);
		System.out.println("Expected Error Messages on Conflicting Patient Eligibility Options Selected: "+PatElgExpectedErrorMessages);
		setAssertMessage("The expected error messages on Conflicting Patient Eligibility Options Selected: "+PatElgExpectedErrorMessages, 2);
		
		List<String> unmatchedList = CommonFunctions.compareStrings(PatElgActualErrorMessages, PatElgExpectedErrorMessages);
				
		
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error list on Patient Details is matching with expected list.");
			setAssertMessage("Actual error list on Patient Details is matching with expected list.", 3);
		}
		
		else
		{
			Assert.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Patient Details is not matching with Expected Error list.");
		}
		

		
	}
	
	// Case 25: Regression Test - Verify Previous Action & Progress Indicator Tickmark is appearing.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","RegressionNewEnv" })
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void previousActionAndProgressIndicator(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException
	{
		String scriptKey="OPT_GOS1_PTR_BR_67";
		
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
		
		setup(browser, environment, clientName,module);
		List<String> keys= Arrays.asList(keyArray);
		String key1 =keys.get(0);
		String key2 =keys.get(1);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		ObjOPPerformerDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsSaved(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			ObjOPPerformerDeclaration.GOS1ProgressIndicatorSnap(key1+"_ProgressIndicator");
		}
		
		// Click on Previous button on Performer Declaration Screen
					
		ObjOPPatientDeclaration = ObjOPPerformerDeclaration.clickOnPreviousButton();
		if(evidence)
		{
			ObjOPPatientDeclaration.GOS1PatientDeclarionSnap(key2+"_PreviousActionSuccess");
		}
		System.out.println("The previous action is successful from Performer Declaration Screen");
		setAssertMessage("The previous action is successful from Performer Declaration Screen.", 1);
		
		SoftAssert softAssertion = new SoftAssert();
		
		
		if(ObjOPPatientDeclaration.verifyTickMarkOnPatientDeclaration())
		{
			System.out.println("The tick mark is present on Patient Declaration Menu");
			setAssertMessage("The tick mark is present on Patient Declaration Menu.", 2);
		}
		else
		{
			//Assert.assertEquals(ObjOPPatientDeclaration.verifyTickMarkOnPatientDeclaration(), true, "The tick mark is not present on Patient Declaration Menu");
			softAssertion.assertEquals(ObjOPPatientDeclaration.verifyTickMarkOnPatientDeclaration(), true, "The tick mark is not present on Patient Declaration Menu");
		}

		
		ObjOPPatientEligibility = ObjOPPatientDeclaration.clickOnPreviousButton();
		System.out.println("The previous action is successful from Patient Declaration Screen");
		setAssertMessage("The previous action is successful from Patient Declaration Screen", 3);
		
		if(ObjOPPatientEligibility.verifyTickMarkOnPatientEligibility())
		{
			System.out.println("The tick mark is present on Patient Eligibility Side Menu");
			setAssertMessage("The tick mark is present on Patient Eligibility Side Menu.", 4);
		}
		
		else
		{
			softAssertion.assertEquals(ObjOPPatientEligibility.verifyTickMarkOnPatientEligibility(), true, "The tick mark is not present on Patient Eligibility Side Menu");
		}

		
		ObjPatientDetails = ObjOPPatientEligibility.clickOnPreviousButton();
		System.out.println("The previous action is successful from Patient Eligibility Side Menu");
		setAssertMessage("The previous action is successful from Patient Eligibility Side Menu", 5);
		
		if(ObjPatientDetails.verifyTickMarkOnPatientDetails())
		{
			System.out.println("The tick mark is present on Patient Details Side Menu");
			setAssertMessage("The tick mark is present on Patient Details Side Menu.", 6);
		}
		else
		{
			softAssertion.assertEquals(ObjPatientDetails.verifyTickMarkOnPatientDetails(), true, "The tick mark is not present on Patient Details Side Menu");
		}
			softAssertion.assertAll();
	}
		
		// Case 26: Regression Test - user is allowed to submit the Claim In absence of a re-test code AND if the date of last sight test is present 
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void submitClaimInAbsenceOfRetestCode(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 6);
		int days= Integer.parseInt(strDays);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 7);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 8);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 9);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		String strDays2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 10);
		int days2= Integer.parseInt(strDays2);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_56", 11);
		
		setup(browser, environment, clientName,module);
		String key =strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
			
			
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails_FirstClaim");
			
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(key);
		
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
		
		// second claim generation
		
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days2, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
			
			
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails_SecondClaim");
			
		}

		String ClaimNo2 = ObjOPContractorDeclaration.getClaimNumber(key);
		Boolean clmSubFlag2 = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag2)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo2+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag2, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		
	}
		
		
	// Case 27: Regression Test - Verify Claim number generated format is valid Claim Number. 
	// Amit - This test will execute only after we get claim numbers on home page.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void validateClaimNumberFormat(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 6);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 7);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_66", 8);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		setup(browser, environment, clientName,module);
		String key = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();
		
		String LastClaimNumber = ObjOPSearchClaim.getFirstClaimNo();
		ObjOPHomePage = ObjOPSearchClaim.clickonOpthalmicLink();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
	

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(patDetNum, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
			ObjOPContractorDeclaration.getClaimNumberSnaps(key+"_ClaimNumber");
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(key);
		Boolean ClmNolen = ObjOPContractorDeclaration.checkClaimNoLength(ClaimNo);
		
		if(ClmNolen){
			setAssertMessage("The claim" +ClaimNo+" is 8 character sequence long.", 1);
			System.out.println("The claim" +ClaimNo+" is 8 character sequence long.");
		}
		
		Boolean ClmNoChars = ObjOPContractorDeclaration.checkalphanum(ClaimNo);
		
		if(ClmNoChars){
			setAssertMessage("The claim" +ClaimNo+" has first 3 characters are characters & last 5 are digits.", 2);
			System.out.println("The claim" +ClaimNo+" has first 3 characters are characters & last 5 are digits.");
		}
		
		
		Boolean ClmNoInc = ObjOPContractorDeclaration.checkClaimNumberIncremented(LastClaimNumber, ClaimNo);
		if(ClmNoInc){
			setAssertMessage("The claim" +ClaimNo+" has incremented from last claim number generated" +LastClaimNumber, 3);
			System.out.println("The claim" +ClaimNo+" has incremented from last claim number generated" +LastClaimNumber);
		}
		
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		
		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		
	
		
	}


// Case 28: Regression Test - user is allowed to submit the Claim In absence of a re-test code AND if the date of last sight test is present 

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void submitClaimInAbsenceOfRetestCodePatientFourty(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 6);
		int days= Integer.parseInt(strDays);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 7);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 8);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 9);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		String strDays2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 10);
		int days2= Integer.parseInt(strDays2);
		
		String strDays3= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 11);
		int days3= Integer.parseInt(strDays3);
		
		String strDays4= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 12);
		int days4= Integer.parseInt(strDays4);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_44", 13);
		
		setup(browser, environment, clientName,module);
		List<String>keys= Arrays.asList(keyArray);
		String key1 =keys.get(0);
		String key2 =keys.get(1);
		String key3= keys.get(2);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
								
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key1+"_Portal_ClaimDetails");
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(key1);
		
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		
		// second claim generation
		
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days2, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		
		if(evidence)
		{
								
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key2+"_Portal_ClaimDetails");
		}

		String ClaimNo2 = ObjOPContractorDeclaration.getClaimNumber(key2);
		Boolean clmSubFlag2 = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag2)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim" +ClaimNo2+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag2, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Third claim generation
		
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days3, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
								
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key3+"_Portal_ClaimDetails");
		}

		String ClaimNo3 = ObjOPContractorDeclaration.getClaimNumber(key3);
		Boolean clmSubFlag3 = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag3)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo3+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag3, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		
		
		// Fourth claim generation
		
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days4, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
								
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key3+"_Portal_ClaimDetails_SecondClaim");
		}

		String ClaimNo4 = ObjOPContractorDeclaration.getClaimNumber(key3);
		Boolean clmSubFlag4 = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag4)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo4+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag3, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		

		
	}
			
			
	// Case 29: Regression Test - user is allowed to submit the Claim In absence of a re-test code AND if patient is 15 or 69 years old 
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void submitClaimInAbsenceOfRetestCodePatientAt69(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 6);
		int days= Integer.parseInt(strDays);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 7);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 8);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 9);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		String strDays2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 10);
		int days2= Integer.parseInt(strDays2);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_50", 11);
		
		setup(browser, environment, clientName,module);
		String key =strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
								
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails_FirstClaim");
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(key);
		
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
		
		
		// second claim generation
		
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days2, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
								
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails_SecondClaim");
		}

		String ClaimNo2 = ObjOPContractorDeclaration.getClaimNumber(key);
		Boolean clmSubFlag2 = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag2)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim" +ClaimNo2+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag2, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
				
		

	}
			
// Case 30: Regression Test - user is  not allowed to submit the Claim In absence of a re-test code AND if patient is 15 or 69 years old and patient with age 70 years over.

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName","evidence" })
	public void InAbsenceOfRetestCodeValidationError(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 6);
		int days= Integer.parseInt(strDays);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 7);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 8);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 9);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		String strPatDetNum2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 10);
		int patDetNum2= Integer.parseInt(strPatDetNum2);
		
		String strDays2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 11);
		int days2= Integer.parseInt(strDays2);
		
		String strPatEleNum2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 12);
		int patEleNum2= Integer.parseInt(strPatEleNum2);
		
		String strDays3= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_GOS1_PTR_BR_49", 13);
		int days3= Integer.parseInt(strDays3);
		
		setup(browser, environment, clientName,module);
		List<String>keys= Arrays.asList(keyArray);
		String key1 = keys.get(0);
		String key2 =keys.get(1);
		String key3 = keys.get(2);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum, days, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPPerformerDeclaration = ObjOPPerformerDeclaration.PerformerDeclarationDetailsEntered(perDecNum);
		
		if(evidence)
		{
			ObjOPPerformerDeclaration.GOS1ErrorsnapOnPerformerDcl(key1+"_PerformerDeclaraionError");
		}
		List<String> ActualErrorMessages = ObjOPPerformerDeclaration.AcutalErrormessage();
		System.out.println(ActualErrorMessages);	
		//String ExceptedErrorMsg = "Early re-test code not provided";
		//List<String> ExceptedErrorMsgs = Arrays.asList(ExceptedErrorMsg.split("\\s*,\\s*"));
		List<String> ExceptedErrorMsgs = ExcelUtilities.getCellValuesInExcel("OPTESTDATA.xlsx", "PATIENTDETAILSMANDFIELDS", 11);
		
		List<String> unmatchedList = CommonFunctions.compareStrings(ActualErrorMessages, ExceptedErrorMsgs);
		
		
		SoftAssert softAssertion= new SoftAssert();
		if(unmatchedList.isEmpty())
		{
			System.out.println("Expected error message on Absence of Retest Code is matching with expected message.");
			setAssertMessage("Expected error message on Absence of Retest Code is matching with expected message.", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Patient Details is not matching with Expected Error list.");
		}
		
		ObjOPHomePage = ObjOPPerformerDeclaration.clickOnOpththalmicLink();
		//Thread.sleep(5000);
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum2, days2, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum2);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPPerformerDeclaration = ObjOPPerformerDeclaration.PerformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
			ObjOPPerformerDeclaration.GOS1ErrorsnapOnPerformerDcl(key2+"_PerformerDeclaraionError");
		}
		List<String> ActualErrorMessages2 = ObjOPPerformerDeclaration.AcutalErrormessage();
		System.out.println(ActualErrorMessages2);	
		
		List<String> unmatchedList2 = CommonFunctions.compareStrings(ActualErrorMessages2, ExceptedErrorMsgs);
		
		
		if(unmatchedList2.isEmpty())
		{
			System.out.println("Expected error message on Absence of Retest Code is matching with expected message.");
			setAssertMessage("Expected error message on Absence of Retest Code is matching with expected message.", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList2.isEmpty(), "The Actual error list on Patient Details is not matching with Expected Error list.");
		}
		
		ObjOPHomePage = ObjOPPerformerDeclaration.clickOnOpththalmicLink();
		//Thread.sleep(5000);
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();

		ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEnteredwithlastSightTest(patDetNum2, days3, environment);
		ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum2);

		ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		ObjOPPerformerDeclaration = ObjOPPerformerDeclaration.PerformerDeclarationDetailsEntered(perDecNum);
		if(evidence)
		{
			ObjOPPerformerDeclaration.GOS1ErrorsnapOnPerformerDcl(key3+"_PerformerDeclaraionError");
		}
		List<String> ActualErrorMessages3 = ObjOPPerformerDeclaration.AcutalErrormessage();
		System.out.println(ActualErrorMessages3);	
		
		List<String> unmatchedList3 = CommonFunctions.compareStrings(ActualErrorMessages3, ExceptedErrorMsgs);
		
		
		if(unmatchedList3.isEmpty())
		{
			System.out.println("Expected error message on Absence of Retest Code is matching with expected message.");
			setAssertMessage("Expected error message on Absence of Retest Code is matching with expected message.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList3.isEmpty(), "The Actual error list on Patient Details is not matching with Expected Error list.");
		}
		
		
		softAssertion.assertAll();

	}
						
						
	/* Amit R : - Validate if user having user role "PCSEClaimsProcessor" with privilege "ManageAllGOSclaims" is logged in, 
	 * then user can access all screens of existing GOS 1 claim with status "Draft" and can make changes to any fields*/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"}, priority=0, enabled= false)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS1ClaimInDraftState(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 6);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 7);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 8);
		int perDecNum= Integer.parseInt(strPerDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_RBAC_08", 9);
		
		setup(browser, environment, clientName,module);
		String keyname = strKeys;
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		//OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(1);
		ObjPatientDetails.enterPerformerDetails(environment);
		ObjPatientDetails.enterPatientDetails(patDetNum);
		ObjPatientDetails.clickOnSaveButton();
		String ClaimNo = ObjPatientDetails.getClaimNumber(keyname);
		quit(browser);
		
		// Now user re login to application & search the claim details, then submit it successfully. 
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();
		ObjPatientDetails = ObjOPSearchClaim.selectAndOpenClaim_GOS1(ClaimNo);
		ObjPatientDetails.enterPerformerDetails(environment);
		ObjPatientDetails.enterPatientDetails(patDetNum);
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.clickOnSaveandNextButton();
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleNum);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(patDecNum);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(perDecNum);
		
		if(evidence)
		{

			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(keyname+"_Portal_ClaimDetails");
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
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
	
	/*	else
		{

			Assert.assertEquals(flag, true, "No records found under results");

		}*/
		

				
			
	}
	
//This script is commented as this is not linked to any requirement.
/*	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"}, priority=0)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void Gos1Claims99(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, AWTException
	{
		int position = 1;
		String Key = "IncompleteClaimsNumber";
		for (int i = 1 ; i<101 ; i++)
		{
		setup(browser, environment, clientName,"OP");
		
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(1, environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(1);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(1);
		ObjOPPerformerDeclaration = ObjOPPerformerDeclaration.enterDetailsAndClickOnAwaitingContractor(1);
		if(evidence)
		{
			ObjOPPerformerDeclaration.screenshots("PerformerDeclaration_Claim"+i);
			
		}
		String ClaimsNumber = ObjOPPerformerDeclaration.getClaimNumber(Key+"_"+i, 1);
		if(ClaimsNumber != null)
		{
		System.out.println("The GOS1 Claims number is " +ClaimsNumber);
		setAssertMessage("The claim " +ClaimsNumber+" gets incompleted successfully.", 2);
		}
		Verify.verifyNotEquals((ClaimsNumber != null), "The is some issue while doing incomplete claims ");
		tearDown(browser);
		Thread.sleep(2000);
		
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}*/
			
		/*	***********************************************************************************************************
		 * Akshay S : - This is Regression Test Case - OPT_POS_13. Validate the POS calculation amount and sum of 
		 * Total number of payment lines per ophthalmic claim type is displayed within the Payment Header where 
		 * ‘Receive POS Payment’ is True for each ‘POS Payment Rate”
		 *******************************************************************************************************************/	
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
		@Parameters({ "browser","environment", "clientName", "evidence" })
		public void verifyPOSCalculation(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
		{
			String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 1);
			String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 2);
			String[]keyArray= strKeys.split(",");
			
			String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 3);
			String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 4);
			
			String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 5);
			int patDetNum= Integer.parseInt(strPatDetNum);
			
			String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 6);
			int patEleNum= Integer.parseInt(strPatEleNum);
			
			String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 7);
			int patDecNum= Integer.parseInt(strPatDecNum);
			
			String strPerDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 8);
			int perDecNum= Integer.parseInt(strPerDecNum);
			
			String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 9);
			String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 10);
			String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 11);
			
			String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 12);
			int advFindNum= Integer.parseInt(strAdvFindNum);
			
			String GMPModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 13);
			
			String paymentRole= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 14);
			
			String StmtModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_POS_13", 15);
			
			List<String> keys = Arrays.asList(keyArray);
			String keyname = keys.get(0);
			setup(browser, environment, clientName,module);
			LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
			OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
			SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
			ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
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

			String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(keyname);
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
			tearDown(browser);
			Thread.sleep(5000);


			// To verify status of GOS3 claim submitted in CRM.
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");
			String paymentDueDate="";
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();
					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 2);
						System.out.println("The claim status in CRM is Accepted For Payment.");					
						//String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							paymentDueDate= paymentLineDetailsList.get(2);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPTESTDATA_New.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
							setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 3);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status: "+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}

					}
					else{
						Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not Accepted for Payment.");
					}

				}

				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}
			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
			tearDown(browser);
			Thread.sleep(2000);
			setup(browser, environment, clientName, GMPModule);
			//String PaymentLineDueDate="4/16/2018";
			//setup(browser, environment, clientName,"GMP","PCSEPAYMENTSPROCESSOR");	
			ObjLoginScreen = new LoginScreen(getDriver());
			GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP(paymentRole, environment);
			//GMPHome ObjGMPHome = new GMPHome(getDriver());
			Date date= CommonFunctions.convertStringtoCalDate(paymentDueDate, "dd/MM/yyyy");
			String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
			ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
			ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
			String paymentRunName= ObjGMPHome.getPaymentRunName();
			System.out.println("Payment run name is: "+paymentRunName);
			setAssertMessage("Payment run name is: "+paymentRunName, 4);
			VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
			objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
/*			PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
			objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload();*/
			tearDown(browser);
			Thread.sleep(2000);
			
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			List<String>GroupTypeValueList= Arrays.asList("fld");
			List<String>FieldValueList= Arrays.asList("Payment Run");
			List<String>ConditionValueList= Arrays.asList("Contains");
			List<String>ValueTypeList= Arrays.asList("Text");
			List<String>ValueForFieldValueList= Arrays.asList(paymentRunName); //paymentRunName
			ObjAdvancedFilter.selectPrimaryEntityofCriteria("Payment Files");
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
			ObjAdvancedFilter.clickResults();	
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
			flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				int records= objAdvancedFindResult.getRecordCount();
				for(int i=0;i<records;i++){
					String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(i,2);
					if(title!= null)
					{
						objAdvancedFindResult= objAdvancedFindResult.updatePaymentFileStatus("ISFE Validated");
						System.out.println("Payment file status has been updated for index: "+i);
						setAssertMessage("Payment file status has been updated for index: "+i, 4);
					}
				}

			}
			tearDown(browser);
			Thread.sleep(5000);
			setup(browser, environment, clientName,StmtModule);
			ObjLoginScreen = new LoginScreen(getDriver());
			OPHomePage ObjHomePage = ObjLoginScreen.logintoOP_Home(paymentRole, environment);
			OPStatement ObjOPStatement = ObjHomePage.redirectToStatementPage(environment);
			ObjOPStatement= ObjOPStatement.enterFilterData();
			if (evidence)
			{
				for (String key: keys){
					ObjOPStatement.OPStatementSnaps(key+"StatementSearchRecord");
				}
			}
			OP_StatementDetails ObjOP_StatementDetails = ObjOPStatement.clickOnStatementRefLink(environment,"OPContractorCode","OpthoContractor");
			if(evidence)
			{
				for (String key:keys)
				ObjOP_StatementDetails.OPStatementDetailsSnaps(key+"StatementDetails");
			}
			System.out.println("Statement has been generated against a Payment File");
			setAssertMessage("Statement has been generated against a Payment File", 5);
		}
		
	/*	***********************************************************************************************************
	 * Akshay S : - 16587- OPT_Note_GOS1_PTR_10- This is Regression Test Case - OPT_POS_13. To verify, the Contractor 
	 * Signatory is notified that 
	 * the GOS 1 claim is awaiting their approval.  
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"},enabled= false)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyGOS1Notification(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 3);
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 4);
		int patDetCol= Integer.parseInt(strPatDetNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 5);
		int patEleCol= Integer.parseInt(strPatEleNum);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 6);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 7);
		String tabName2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 8);
		String organisation= ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "OPGOS1ScriptParameters", "OPT_Note_GOS1_PTR_10", 9);
		
		String keyname = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(patDetCol,environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(patEleCol);

		ObjOPPatientDeclaration=  ObjOPPatientDeclaration.enterDetailsAndClickOnAwaitingPerformer(patDecNum);
		String ClaimNo = ObjOPPatientDeclaration.getClaimNumber(keyname);
		ObjOPHomePage= ObjOPHomePage.ClickonTab("HOME", OPHomePage.class);
		MyNotification objMyNotification= ObjOPHomePage.ClickOnPageHeader(tabName, MyNotification.class);
		ObjOPPatientDeclaration= objMyNotification.clickOnReviewLink(ClaimNo,OPPatientDeclaration.class,keyname+"_PerformerAwaitingApproval_"+ClaimNo,"is awaiting Performer approval.");
		if(evidence){
			ObjOPPatientDeclaration.GOS1PatientDeclarionSnap(keyname+"_after clicking on Performer review");
		}
		ObjOPPatientDeclaration.clickOnSaveandNextButton();
		setAssertMessage("Performer awaiting approval notification has been displayed for claim: "+ClaimNo, 1);
		System.out.println("Performer awaiting approval notification has been displayed for claim: "+ClaimNo);
		OPPerformerDeclaration objOPPerformerDeclaration= new OPPerformerDeclaration(getDriver());
		//OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(1);
		objOPPerformerDeclaration = objOPPerformerDeclaration.enterDetailsAndClickOnAwaitingContractor(1);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName2, SelectOrganisation.class);
		ObjOPHomePage= objSelectOrganisation.selectOrganisation(organisation, OPHomePage.class);
		objMyNotification= ObjOPHomePage.ClickOnPageHeader(tabName, MyNotification.class);
		objOPPerformerDeclaration= objMyNotification.clickOnReviewLink(ClaimNo,OPPerformerDeclaration.class,keyname+"_ContractorSignatoryApproval_"+ClaimNo,"is awaiting Contractor Signatory approval.");
		if(evidence){
			objOPPerformerDeclaration.GOS1ErrorsnapOnPerformerDcl(keyname+"_after clicking on Contractor review");
		}
		objOPPerformerDeclaration.clickOnSaveandNextButton();
		setAssertMessage("Contractor awaiting approval notification has been displayed for claim: "+ClaimNo, 2);
		System.out.println("Contractor awaiting approval notification has been displayed for claim: "+ClaimNo);
		OPContractorDeclaration objOPContractorDeclaration= new OPContractorDeclaration(getDriver());
		if(evidence)
		{
			objOPContractorDeclaration.GOS1ClaimDetailssnaps(keyname+"_Portal_ClaimDetails");
		}

		ClaimNo = objOPContractorDeclaration.getClaimNumber(keyname);
		ObjOPHomePage = objOPContractorDeclaration.clickOnRevertToDraftButton();
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		objMyNotification= ObjOPHomePage.ClickOnPageHeader(tabName, MyNotification.class);
		boolean isReverted= objMyNotification.verifyNotificationText(ClaimNo,"has been reverted to draft.",keyname+"_Claim reverted to draft_"+ClaimNo);
		if(evidence){
			objMyNotification.captureNotificationSnap(keyname+"_claim reverted to Draft_"+ClaimNo);
		}
		Assert.assertEquals(isReverted, true);
		setAssertMessage("Claim is reverted to draft for claim: "+ClaimNo, 3);
		System.out.println("Claim is reverted to draft for claim: "+ClaimNo);

	}
	
	/*	***********************************************************************************************************
	 * Akshay S : - 12677- OPT_Statements_PRT_BR_5- This is Regression Test Case - To validate the functionality of 
	 * "Expand all" button on the Statements screen
	 *******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","Sanity","CloneSanity","CLONE"} ,enabled = true)
	@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void verifyExpandAllForStatement(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		String scriptKey="OPT_Statements_PRT_BR_5";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS1",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS1","PerDeclOption");
		
		String module= values.get(0);
		String[]moduleArray= module.split(",");
		String strKeys= values.get(1);
		String[]keyArray= strKeys.split(",");
		String role= values.get(2);
		String[]roleArray= role.split(",");
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		String paymentFileStatus= values.get(8);
		String GOSType= values.get(9);
		
		List<String> modules= Arrays.asList(moduleArray);
		String module_OP= modules.get(0);
		String module_GMP= modules.get(1);
		String module_Statement= modules.get(2);
		List<String> roles= Arrays.asList(roleArray);
		String clmProcessor= roles.get(0);
		String paymtProcessor= roles.get(1);
		List<String> keys= Arrays.asList(keyArray);
		String key = scriptKey;
		
		List<String>GroupTypeValueList= new ArrayList<String>();
		List<String>FieldValueList= new ArrayList<String>();
		List<String>ConditionValueList= new ArrayList<String>();
		List<String>ValueTypeList= new ArrayList<String>();
		List<String>ValueForFieldValueList= new ArrayList<String>();
		
		setup(browser, environment, clientName,module_OP);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(clmProcessor, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPPatientDetails ObjPatientDetails = ObjMakeAClaim.clickGOSONEButton();
		
		OPPatientEligibility ObjOPPatientEligibility = ObjPatientDetails.PatientDetailsEntered(scriptKey,environment);
		OPPatientDeclaration ObjOPPatientDeclaration = ObjOPPatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);

		OPPerformerDeclaration ObjOPPerformerDeclaration = ObjOPPatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet);
		OPContractorDeclaration ObjOPContractorDeclaration = ObjOPPerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			ObjOPContractorDeclaration.GOS1ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "Claim Number", ClaimNo, 1);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();
		SoftAssert softAssert=  new SoftAssert();
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
				softAssert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}

		}
		else{
			softAssert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPContractorDeclaration.clickCloseOnResultPopup();
		tearDown(browser);
		Thread.sleep(5000);


		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValueList= Arrays.asList("fld");
		FieldValueList= Arrays.asList("Claim Number");
		ConditionValueList= Arrays.asList("Equals");
		ValueTypeList= Arrays.asList("Text");
		ValueForFieldValueList= Arrays.asList(ClaimNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Ophthalmic Claims");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
		ObjAdvancedFilter.clickResults();
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");
		String paymentDueDate="";

		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = objAdvancedFindResult.getDetailsFromResultRecordScreen();
				if (clmStatus.contains(claimStatus))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 2);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					//String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = objAdvancedFindResult.ClaimsStatus(ClaimNo, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = objAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ClaimNo );
						paymentLineDetailsList = objAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						paymentDueDate= paymentLineDetailsList.get(2);
						System.out.println(PaymentLineStatus);
						if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
						{
							System.out.println("The Payment Line status is Pending");
							setAssertMessage("The Payment Line status is Pending.", 3);
						}
						else
						{
							System.out.println("The Payment Line status is not Pending");
							//setAssertMessage("The Payment Line status is not Pending.", 1);
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status: "+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
						}	
						
						// Verify Amount Due on Payment Line
						
						String PaymentLineAmountDue = paymentLineDetailsList.get(1);
						System.out.println(PaymentLineAmountDue);
						if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
						{
							System.out.println("The Payment Line amount due is matching");
							setAssertMessage("The Payment Line amount due is matching.", 3);
						}
						else
						{
							System.out.println("The Payment Line amount due is not matching");
							//setAssertMessage("The Payment Line status is not Pending.", 1);
							Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
						}
					}
					else
					{
						System.out.println("The Payment Line is not generated for claim: " +ClaimNo );
						softAssert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ClaimNo);
					}
					Boolean AlertPresent = objAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						objAdvancedFindResult.ClickSpaceBar();
					}

				}
				else{
					softAssert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not Accepted for Payment.");
				}

			}

			else
			{
				softAssert.assertNotNull(title, "Title extracted from result record is NULL.");
			}
		}
		else
		{

			softAssert.assertEquals(flag, true, "No records found under results");

		}
		tearDown(browser);
		Thread.sleep(3000);
		String PaymentDuedate_Excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate", 1);
		Date date= CommonFunctions.convertStringtoCalDate(PaymentDuedate_Excel, "dd/MM/yyyy");
		String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
		setup(browser, environment, clientName, module_GMP);
		ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP(paymtProcessor, environment);
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
		String paymentRunName= ObjGMPHome.getPaymentRunName();
		System.out.println("Payment run name is: "+paymentRunName);
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload(getDriver());
		System.out.println("The Dot 1 file is generated");
		setAssertMessage("The Dot 1 file is generated", 4);
		if(evidence)
		{
			objPaymentInstructionFile.takescreenshots(key+"Download_Dot1file");
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,CRMModule,CRMUSER);
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "Claim Number", 1);
		System.out.println("Claim Number:"+ValueForFieldValue);
		claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "ExpectedClaims", 1);
		objAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = objAdvancedFindResult.getDetailsFromResultRecordScreen();
				if (clmStatus.contains(claimStatus))
				{
				//	setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					String ClaimStatusCRM = objAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = objAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						String Paylinestatus = objAdvancedFindResult.getPaymentlinestatus();
						if(evidence)
						{
							objAdvancedFindResult.takescreenshots(key+"Paymentstatus_AfterGMPRun");
						}
						if(Paylinestatus.equalsIgnoreCase("Payment Instruction Issued"))
						{
							System.out.println("The GMP run is sucessfully completed");
							setAssertMessage("The GMP run is sucessfully completed", 5);
						}
						Verify.verifyNotEquals((Paylinestatus.equalsIgnoreCase("Payment Instruction Issued")), "The GMP is not run sucessfully ");	
						softAssert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
						
					}
				}
			}
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValueList.remove(GroupTypeValueList);
		GroupTypeValueList= Arrays.asList("fld");
		FieldValueList.remove(FieldValueList);
		FieldValueList= Arrays.asList("Payment Run");
		ConditionValueList.remove(ConditionValueList);
		ConditionValueList= Arrays.asList("Contains");
		ValueTypeList.remove(ValueTypeList);
		ValueTypeList= Arrays.asList("Text");
		ValueForFieldValueList.remove(ValueForFieldValueList);
		ValueForFieldValueList= Arrays.asList(paymentRunName); //paymentRunName
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Payment Files");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			int records= objAdvancedFindResult.getRecordCount();
			for(int i=0;i<records;i++){
				String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(i,2);
				if(title!= null)
				{
					objAdvancedFindResult= objAdvancedFindResult.updatePaymentFileStatus(paymentFileStatus);
					System.out.println("Payment file status has been updated for index: "+i);
					setAssertMessage("Payment file status has been updated for index: "+i, 6);
				}
			}

		}
		tearDown(browser);
		Thread.sleep(5000);
		setup(browser, environment, clientName,module_Statement);
		ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjHomePage = ObjLoginScreen.logintoOP_Home(paymtProcessor, environment);
		OPStatement ObjOPStatement = ObjHomePage.redirectToStatementPage(environment);
		ObjOPStatement= ObjOPStatement.enterFilterData();
		if (evidence)
		{
			ObjOPStatement.OPStatementSnaps(key+"_StatementSearchRecord");
		}
		//String ClaimNo= "ADA00926";
		OP_StatementDetails ObjOP_StatementDetails = ObjOPStatement.clickOnStatementRefLink(environment,"OPContractorCode","OpthoContractor");
		boolean isDocPresent= ObjOP_StatementDetails.verifyPresenceOfDocument(ClaimNo,GOSType);
		softAssert.assertEquals(isDocPresent, true,"Claim ID "+ClaimNo+" is not present inside statement details page");
		System.out.println("Claim number "+ClaimNo+" has been picked by Statement and is visible on portal");
		setAssertMessage("Claim number "+ClaimNo+" has been picked by Statement and is visible on portal", 7);
		if(evidence)
		{
			ObjOP_StatementDetails.OPStatementDetailsSnaps(key+"_StatementDetails for claim number_"+ClaimNo);
		}
		System.out.println("Statement has been generated against a Payment File");
		setAssertMessage("Statement has been generated against a Payment File", 8);
		softAssert.assertAll();
	}
	
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","Sanity","CloneSanity","CLONE"} ,enabled = true)
	@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void verifyClaimDetailsFromDatabase(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException, SQLException
	{
		String scriptKey="OPT_GOS1_PTR_BR_15";
		
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

		String ClaimNo = ObjOPContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
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
		
		String key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
			// Verify claim details on CRM
			setup(browser, environment, clientName,CRMModule,CRMUSER);	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = advFindNum;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			//String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "ExpectedClaimStatus",colNum);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();

					if (clmStatus.contains(claimStatus))
					{
						setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
						System.out.println("The claim status in CRM is Accepted For Payment.");
						
						
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
						
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 1);
								setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
							}
							else
							{
								System.out.println("The Payment Line status is not Pending");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}	
							
							// Verify Amount Due on Payment Line
							
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								setAssertMessage("The Payment Line amount due is matching.", 3);
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
							Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
						}
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}
					}
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not expected as Accepted For Payment.");

				}
				else
				{
					Assert.assertNotNull(title, "Title extracted from result record is NULL.");
				}

			}
			else
			{

				Assert.assertEquals(flag, true, "No records found under results");

			}
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00673";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,keyname,environment,dbEnvironment,ClaimNo,"PLDetails");
				for(String AssetMessage : objOPHelpers.AssertMessage)
				{
					setAssertMessage(AssetMessage, i);
					i = i + 1;
				}
				Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual Payment Line values are not matching");
			}
			Assert.assertEquals(objOPHelpers.Process, true,"Expected and Actual claim values are not matching");
		}
	}*/

}