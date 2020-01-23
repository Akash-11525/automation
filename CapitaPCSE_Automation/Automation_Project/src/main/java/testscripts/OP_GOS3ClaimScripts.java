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

import com.opencsv.bean.opencsvUtils;
//import com.thoughtworks.selenium.condition.Presence;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import helpers.OPHelpers;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.PaymentInstructionFile;
import pageobjects.GMP.VarianceReports;
import pageobjects.OP.*;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class OP_GOS3ClaimScripts extends BaseClass
{
	String fileName= "OPGOS3TESTDATA.xlsx";
	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case - OPT_GOS3_PRT_BR_60. To verify GOS 3 claim is accepted, 
	 * if Distance and Near prescription is passed, the voucher is calculated with the Distance and Reading Addition (i.e. Reading addition= Distance + Near).
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CLONE"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void distanceAndNearPrescriptionIsPassed(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_60", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		//String key = "OPT_GOS3_PRT_BR_60";
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		//OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(supDecNum, supColName);
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}

		

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
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
		
		//commented by Akshay on 10/05/2018 to execute test script for Drop 5.2
/*		setup(browser, environment, clientName, "GMP");
		String PaymentLineDueDate="4/16/2018";
		//setup(browser, environment, clientName,"GMP","PCSEPAYMENTSPROCESSOR");	
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		//GMPHome ObjGMPHome = new GMPHome(getDriver());
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(PaymentLineDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch();
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload();*/

	}
	
	
	/*	**********************************************************************************************************************
	 * Amit R : - This is Regression Test Case 5210 - OPT_GOS3_PRT_BR_67. To verify GOS 3 claim is accepted, 
	 * if Under Reading and Distance Prescription Tag are passed as A  and distance and Near pair are selected then the claims accepted.
	 ***************************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAndDistancePrescriptionIsPassed(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_67", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(2);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(supDecNum, supColName);
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}

	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case 5212 - OPT_GOS3_PRT_BR_70. To verify GOS 3 claim is accepted, 
	 * if Under Reading and Distance Prescription Tag are passed as A  and voucher type is E is passed in claim.
	 *****************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underreadingAndPrescriptionIsPassedwithVoucherE(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_70", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5213 - OPT_GOS3_PRT_BR_72. To verify GOS 3 claim is accepted, 
	 * if Under Distance Prescription sph power of more than 6 diopters but less than 10 diopters , 
	 * cyl power of not more than 6 diopters  and First voucher type is B is passed in claim. .
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underdistancePrescriptionSPHmoreThanDiopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_72", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
	//	OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration= ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5214 - GOS 3 claim is accepted, 
	 * if  Under Reading Prescription sph power of  more than 6 diopters but less than 10 diopters , 
	 * cyl power of not more than 6 diopters  and First voucher type is B is passed in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionSPHmoreThanDiopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_74", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(3);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5215 - OPT_GOS3_PRT_BR_76 - verify if GOS 3 claim is accepted, 
	 * if Under Distance Prescription sph power of less than 10 diopters , 
	 * cyl power of more than 2 diopters but not more than 6 diopters  and First voucher type is B is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underDistancePrescriptionSPHlessThan10Diopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_76", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(4);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5216 - OPT_GOS3_PRT_BR_78 - verify GOS 3 claim is accepted, 
	 * if Under Reading Prescription sph power of  less than 10 diopters , 
	 * cyl power of  more than 2 diopters but not more than 6 diopters  and Second voucher type is B is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionSPHlessThan10Diopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_78", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5217 - OPT_GOS3_PRT_BR_80 - verify GOS 3 claim is accepted, 
	 * if Under Reading and Distance Prescription are passed as B  and voucher type is F is selected
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAndDistancePrescriptionwithVoucherBF(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_80", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5218 - OPT_GOS3_PRT_BR_82 - verify GOS 3 claim is accepted, 
	 * if reading addition less than 4, and Bifocal is selected, Bifocal voucher is determined with Distance segment.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAdditionLessThan4withBifocal(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_82", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5219 - OPT_GOS3_PRT_BR_83 - verify GOS 3 claim is accepted, 
	 * if reading addition more than 4 AND Reading power determine higher voucher and Bifocal is selected, 
	 * Bifocal voucher is determined with Reading segment.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAdditionMoreThan4withBifocal(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_83", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDecNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5220 - OPT_GOS3_PRT_BR_86 - verify GOS 3 claim is accepted, 
	 * if Under Distance Prescription sph power of 10 or more diopters but not more than 14 diopters , 
	 * cyl power of not more than 6 diopters  and First voucher type is C is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underDistancePrescriptionSPHMoreThan10(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_86", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("B");
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5221 - OPT_GOS3_PRT_BR_88 - verify GOS 3 claim is accepted, 
	 * if Under Reading and Distance Prescription are passed as C  and distance and near pair are selected.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAndDistancePrescriptionPassedAsC(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_88", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}

	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5222 - OPT_GOS3_PRT_BR_89 - verify GOS 3 claim is accepted, 
	 * if Under Reading Prescription sph power of 10 or more diopters but not more than 14 diopters , 
	 * cyl power of not more than 6 diopters  and second voucher type is C is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionSPHof10(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_89", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5223 - OPT_GOS3_PRT_BR_91 - verify GOS 3 claim is accepted, 
	 * if Under Reading and Distance Prescription are entered such that Voucher C is determined 
	 * then voucher type G is calculated if bifocal is supplied
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAndDistancePrescriptionWithVoucherCG(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_91", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5225 - OPT_GOS3_PRT_BR_95 - verify GOS 3 claim is accepted, 
	 * if  Under Reading  Prescription are passed as C , Distance Prescription are passed as A/B  
	 * and Bifocal glasses are determined by Near is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionWithBifocalglasses(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_95", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5226 - OPT_GOS3_PRT_BR_96 - verify GOS 3 claim is accepted, 
	 * if Under Reading  Prescription are passed as C , Distance Prescription are passed as A/B  and 
	 * Bifocal glasses are determined by Distance is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionPassedWithVouchersABandBifocalglasses(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_96", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5224 - OPT_GOS3_PRT_BR_93 - verify GOS 3 claim is accepted, 
	 * if Under Distance Prescription is passed as below 10 diopters , 
	 * Reading Prescription are passed as 10 diopters or more and Complex Voucher, Bifocal glasses are passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underDistancePrescriptionBelow10diopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 7);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 8);
		int patEleNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 9);
		int presNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_93", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(5);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("F");
		//ObjOPGOS3Prescription.selectSecondVoucherType("A");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5227 - OPT_GOS3_PRT_BR_97 - verify GOS 3 claim is accepted, 
	 * if Under Distance Prescription sph power of more than 14 diopters , 
	 * cyl power of any diopters  and First voucher type D is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underDistancePrescriptionSPHMoreThan14(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 8);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 9);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String firstPair= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 12);
		String secondPair= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 13);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 14);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 15);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 16);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 17);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 18);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_97", 19);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("A");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPair);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(secondPair);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5228 - OPT_GOS3_PRT_BR_99 - verify GOS3 claim is accepted, 
	 * if Under Reading Prescription sph power of more than 14 diopters, cyl power of any diopters  and 
	 * second voucher type D is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionSPHMoreThan14(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 9);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 10);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 11);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 12);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String firstPair= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 13);
		String secondPair= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 14);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,0,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.selectSecondPairPrism(firstPair);
		ObjOPGOS3SupplierDeclaration.selectSecondPairTint(secondPair);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5229 - OPT_GOS3_PRT_BR_101 - verify GOS3 claim is accepted, 
	 * if Under Distance Prescription sph power of any diopters, cyl power of more than 6 diopters  and 
	 * First voucher type D is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underDistancePrescriptionSPHCYLMoreThan6Diopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_101", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		//ObjOPGOS3Prescription.selectProvidedOptions(3);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5230 - OPT_GOS3_PRT_BR_103 - verify GOS 3 claim is accepted, 
	 * if Under Reading Prescription sph power of any diopters, cyl power of more than 6 diopters  and 
	 * second voucher type is D is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionSPHCYLMoreThan6Diopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 9);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_99", 10);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 11);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 12);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String firstPair= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 13);
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 14);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 15);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 16);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 17);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 18);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_103", 19);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.selectSecondPairPrism(firstPair);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5231 - OPT_GOS3_PRT_BR_105 - verify GOS 3 claim is accepted, 
	 * if Under Reading and Distance Prescription are passed as D  and voucher type is H is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingAndDistancePrescriptionWithVouchersDH(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_105", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5232 - OPT_GOS3_PRT_BR_107 - verify GOS 3 claim is accepted, 
	 * if Under Claim prism-controlled bifocal lenses  are passed and voucher type is H is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underPrismControlledBifocalLenses(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_107", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5233 - OPT_GOS3_PRT_BR_109 - verify GOS 3 claim is accepted, 
	 * if Under Reading  Prescription are passed as D , Distance Prescription are passed as A/B/C  and 
	 * Bifocal glasses are determined by Near is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionAndDistancePrescriptionBifocal(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_109", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5234 - OPT_GOS3_PRT_BR_110 - verify GOS 3 claim is accepted, 
	 * if Under Reading  Prescription are passed as D , Distance Prescription are passed as A/B/C  and 
	 * Bifocal glasses are determined by Distance is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underReadingPrescriptionAndDistancePrescriptionWithGAVouchers(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_110", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5235 - OPT_GOS3_PRT_BR_111 - verify GOS 3 claim is accepted, 
	 * if Under Distance Prescription sph power less than 6 diopters , cyl power of not more than 2 diopters , 
	 * First voucher type is A (voucher value changed in RD)
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void underDistancePrescriptionSPHLessThan6Diopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_111", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
	//	ObjOPGOS3Prescription.selectSecondVoucherType("A");
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5236 - OPT_GOS3_PRT_BR_112 - verify GOS 3 claim is accepted, 
	 * if the Distance and Near prescription is passed and  bifocal glasses are passed in Supplier declaration.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void DistanceAndNearPrescriptionPassed(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_112", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5237 - OPT_GOS3_PRT_BR_114 - verify GOS 3 claim is accepted, 
	 * if the Distance and Near prescription is passed and  Distance and Near pair of  glasses are passed in Supplier declaration.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CloneSanity","CLONE"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void DistanceAndNearPrescriptionAndGlassesPassed(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_114", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(4);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
						}
						
						// Verify Amount Due on Payment Line
						
						/*String PaymentLineAmountDue = paymentLineDetailsList.get(1);
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
						}*/
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5238 - OPT_GOS3_PRT_BR_116 - verify GOS 3 claim is accepted, 
	 * if correct Tint/ Prism values are passed in Prescription Tag and Claim tag  is passed in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void TintPrismValuesPassed(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 12);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 13);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 14);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 15);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 16);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 17);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 18);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_116", 19);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		//ObjOPGOS3PatientEligibility.enterHC2CertificateDetails(strCertificateNum);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();//delay to be added
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
						setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1);
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5239 - OPT_GOS3_PRT_BR_118 - verify GOS 3 claim is accepted, 
	 * if boxed center distance is = 55mm and small glasses supplement is "Y"  in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void CenterDistance55WithGlassSupplimentY(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 12);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 13);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 14);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 15);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 16);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 17);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_118", 18);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		//ObjOPGOS3PatientEligibility.enterHC2CertificateDetails("123456789");
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5240 - OPT_GOS3_PRT_BR_120 - verify GOS 3 claim is accepted, 
	 * if boxed center distance is = 55mm and small glasses supplement is "Y"  in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void CenterDistance55WithHC2CertAndGlassSuppliment(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 12);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 13);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 14);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 15);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 16);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 17);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 18);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 19);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 20);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_120", 21);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC2CertificateDetails(strCertificateNum);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(firstPairTint);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5240 - OPT_GOS3_PRT_BR_118 - verify GOS 3 claim is accepted, 
	 * if boxed center distance is = 55mm and small glasses supplement is "Y"  in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void CenterDistanceAndSmallGlassessWithHC2(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 12);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 13);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 14);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 15);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 16);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 17);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 18);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 19);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 20);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121", 21);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC2CertificateDetails(strCertificateNum);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(firstPairTint);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, expSubmitMsg, "The claim does not get submitted successfully.");
			}
		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5241 - OPT_GOS3_PRT_BR_121 - verify GOS 3 claim is NOT accepted, 
	 * if boxed center distance is over  55mm and small glasses supplement is "N"  in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void CenterDistanceOver55mm(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 12);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_121_2", 13);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		//ObjOPGOS3PatientEligibility.enterHC2CertificateDetails("123456789");
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		//ObjOPGOS3SupplierDeclaration.selectSecondPairPrism("2");
		//ObjOPGOS3SupplierDeclaration.selectSecondPairTint("2");
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ErrorsnapOnSupDclFirstPair(key+"_Error_SmallMesurement");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3SupplierDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 20);
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
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list./n Actual Error: "+ActualErrorMessagesonSave+"/n Expected Error: "+ExpectedErrorMessagesonSave+"/n");
		}			
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5243 - OPT_GOS3_PRT_BR_124 - verify GOS 3 claim is accepted, 
	 * if small glasses and special facial characteristics are supplied but not prescribed.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void smallGlassesAndSplFacCharSupplied(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 8);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_124", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("B");
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5244 - OPT_GOS3_PRT_BR_126 - GOS 3 claim is accepted, 
	 * if number of tint / prism(0/1/2)  lenses are passed in supplier section and tint/prism is selected
	 *  in prescription section in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void TintAndPrismLensesSupplied(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 12);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 13);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 14);
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 15);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 16);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 17);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 18);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 19);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 20);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_126", 21);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue("30.50");
		
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
			
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectSecondPairPrism(firstPairTint);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5242 - OPT_GOS3_PRT_BR_86 - verify GOS 3 claim is accepted, 
	 * if supplements are passed in prescription tag but not passed under the Claim tag  in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void supplementsPassedinPrescription(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 8);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 9);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_122", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days, environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	

	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5245 - OPT_GOS3_PRT_BR_86 - verify GOS 3 claim is accepted, 
	 * if Voucher type-Complex is passed along with all the supplements in claim.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void voucherTypeComplexSelected(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 11);
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 12);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 13);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 14);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 15);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 16);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 17);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_127", 18);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days, environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("C");
		//ObjOPGOS3Prescription.selectSecondVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(firstPairTint);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5246 - OPT_GOS3_PRT_BR_128 - verify GOS 3 claim is accepted, 
	 * if single pair Bifocal glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void singlePairBifocalGlassesWithMaxClaimValue(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 9);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 10);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 12);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 12);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 13);
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 14);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_128", 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(firstPairTint);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5247 - OPT_GOS3_PRT_BR_129 - verify GOS 3 claim is accepted, 
	 * if single pair Near glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is available in claim.  
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void singlePairNearGlassesWithMaxClaimValue(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_129", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(8);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		//ObjOPGOS3SupplierDeclaration.selectFirstPairPrism("1");
		//ObjOPGOS3SupplierDeclaration.selectFirstPairTint("1");
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5248 - OPT_GOS3_PRT_BR_130 - verify GOS 3 claim is accepted, 
	 * if single pair Distance glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void singlePairNearGlassesAndHC3CertWithMaxClaimValue(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 8);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 9);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 10);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 11);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 12);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 13);
		String strHC3Value= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 14);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 15);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 16);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 17);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 18);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 19);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 20);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_130", 21);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHC3Value);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(3);
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		//ObjOPGOS3SupplierDeclaration.selectFirstPairPrism("1");
		//ObjOPGOS3SupplierDeclaration.selectFirstPairTint("1");
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5249 - OPT_GOS3_PRT_BR_131 - verify GOS 3 claim is accepted, 
	 * if one pair Distance and one pair Near glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void oneDistancePairAndOnePairNearGlasses(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_131", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(2);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		/*ObjOPGOS3PatientEligibility.selectProvidedOptions(3);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(1);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(1);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails("123456789");
		ObjOPGOS3PatientEligibility.enterHCVoucherValue("999.98");
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();*/
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		//ObjOPGOS3SupplierDeclaration.selectFirstPairPrism("1");
		//ObjOPGOS3SupplierDeclaration.selectFirstPairTint("1");
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							System.out.println("The Payment Line amount due is matching");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5250 - OPT_GOS3_PRT_BR_132 - verify GOS 3 claim is accepted, 
	 * if single pair Bifocal glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is not available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void pairBifocalGlasseswithMaxClaimValue(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 8);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 9);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 12);
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 13);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 14);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 15);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 16);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 17);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 18);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_132", 19);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		/*ObjOPGOS3PatientEligibility.selectProvidedOptions(3);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(1);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(1);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails("123456789");
		ObjOPGOS3PatientEligibility.enterHCVoucherValue("999.98");
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();*/
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		//ObjOPGOS3Prescription.selectSecondVoucherType("D");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(firstPairTint);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5251 - OPT_GOS3_PRT_BR_133 - GOS 3 claim is accepted, 
	 * if single pair Near glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is not available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void pairNearGlasseswithMaxClaimValue(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_133", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		/*ObjOPGOS3PatientEligibility.selectProvidedOptions(3);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(1);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(1);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails("123456789");
		ObjOPGOS3PatientEligibility.enterHCVoucherValue("999.98");
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();*/
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		//ObjOPGOS3Prescription.selectProvidedOptions(2);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		//ObjOPGOS3SupplierDeclaration.selectFirstPairPrism("1");
		//ObjOPGOS3SupplierDeclaration.selectFirstPairTint("1");
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5252 - OPT_GOS3_PRT_BR_134 - verify GOS 3 claim is accepted, 
	 * if single pair Distance glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is not available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void singlePairDistanceGlasseswithMaxClaimValue(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_134", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		/*ObjOPGOS3PatientEligibility.selectProvidedOptions(3);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(1);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(1);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails("123456789");
		ObjOPGOS3PatientEligibility.enterHCVoucherValue("999.98");
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();*/
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
	//	ObjOPGOS3Prescription.selectProvidedOptions(2);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		//ObjOPGOS3SupplierDeclaration.selectFirstPairPrism("1");
		//ObjOPGOS3SupplierDeclaration.selectFirstPairTint("1");
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5253 - OPT_GOS3_PRT_BR_135 - verify GOS 3 claim is rejected, 
	 * if one pair Distance and one pair Near glasses if correct maximum claimable value  is passed and  
	 * Actual Retail cost is not available in claim. 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void claimError_whenActualRetailCostNotPresent(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 9);
		
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 10);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 11);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 12);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 13);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 14);
		String strHCVoucherValue= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 15);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 16);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 17);
		String firstPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 18);
		String firstPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 19);
		String secondPairPrism= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 20);
		String secondPairTint= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_135", 21);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHCVoucherValue);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.selectFirstPairPrism(firstPairPrism);
		ObjOPGOS3SupplierDeclaration.selectFirstPairTint(firstPairTint);
		ObjOPGOS3SupplierDeclaration.selectSecondPairPrism(secondPairPrism);
		ObjOPGOS3SupplierDeclaration.selectSecondPairTint(secondPairTint);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		/*if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();*/
		
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ErrorsnapOnSupDcltotalClaimForGlasses(key+"_Error_ActualRetailCostNotPresent");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3SupplierDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 2);
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
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list./n Expected Error Message: "+ExpectedErrorMessagesonSave+"\n Actual Error Message: "+ActualErrorMessagesonSave+"/n");
		}	
		
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5191 - OPT_GOS3_PRT_BR_29 - verify GOS 3 claim is accepted, 
	 * if eligibility  patient is under 16 is selected and difference between DOB  and 
	 * Patient's Declaration date in Part 2 is less than 16 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientUnder16withDOBAndDlrdateDiffLessThan16Yrs(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_29", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("F");
		//ObjOPGOS3Prescription.selectSecondVoucherType("A");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
	}
	
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5192 - OPT_GOS3_PRT_BR_31 - verify GOS 3 claim is accepted, 
	 * if eligibility  patient is under 16 is selected and difference between DOB  and Patient's Declaration 
	 * date in Part 2 is = 16 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","Sanity", "Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void claimErrorValidationErrorOnPatientAge(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_31", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		
		if(evidence)
		{
			ObjOPGOS3PatientEligibility.GOS3ErrorsnapOnPatElgOnPatEntitlement(key+"_Error_PatientEligibility");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3PatientEligibility.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 21);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			System.out.println("The Actual error list on Save Action is not matching with Expected Error list. \n Actual Error List: "+ActualErrorMessagesonSave+"\n Expected Error Message: "+ExpectedErrorMessagesonSave);
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list. \n Actual Error List: "+ActualErrorMessagesonSave+"\n Expected Error Message: "+ExpectedErrorMessagesonSave);
		}	
		
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5193 - OPT_GOS3_PRT_BR_32 - verify GOS 3 claim is accepted, 
	 * if eligibility   patient is 16, 17,18,  is selected and difference in DOB  and 
	 * Patient's Declaration date in Part 2 is greater than 16 years and less than 18 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientStudent17YrOld(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_32", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5194 - OPT_GOS3_PRT_BR_35 - verify GOS 3 claim is accepted, 
	 * if eligibility   patient is 16, 17,18,  is selected and difference between DOB  and 
	 * Patient's Declaration date in Part 2 is = 16 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientStudent17YrOldwithComplexLense(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_35", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5195 - OPT_GOS3_PRT_BR_36 - verify GOS 3 claim is accepted, 
	 * if eligibility   patient is 16, 17,18,  is selected and difference between DOB  and 
	 * Patient's Declaration date in Part 2 is = 18 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientStudent17YrwithComplexLense(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_36", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5196 - OPT_GOS3_PRT_BR_37 - verify GOS 3 claim is accepted, 
	 * if eligibility  patient receive Pension Credit guarantee is selected and difference in DOB of patient and 
	 * Patient's Declaration date in Part 2 is greater than 63 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientAge64YrwithPatientGaurantee(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_37", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5197 - OPT_GOS3_PRT_BR_39 - verify GOS 3 claim is accepted, 
	 * if eligibility  patient receive Pension Credit guarantee is selected and difference in DOB of patient and 
	 * Patient's Declaration date in Part 2 is 63 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientAge63YrwithPatientGaurantee(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_39", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	

	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5198 - OPT_GOS3_PRT_BR_40 - verify GOS 3 claim is accepted, 
	 * if patient's partner receive Pension Credit guarantee is selected and difference in DOB of Partner and 
	 * Patient's Declaration date in Part 2 is > 63 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientPartnerAgeMoreThan63YrwithPatientGaurantee(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_40", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 5199 - OPT_GOS3_PRT_BR_42 - verify GOS 3 claim is accepted, 
	 * if eligibility  patient's partner receive Pension Credit guarantee is selected and 
	 * difference between DOB of Partner and Patient's Declaration date in Part 2 is = 63 years.
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void patientPartnerAge63YrwithPatientGaurantee(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 9);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 10);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 11);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 12);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 13);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 15);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_42", 16);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
	
			//	PatientDetailsEntered(3,0);
		
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		//ObjOPGOS3Prescription.selectFirstVoucherType("B");
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		//ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 8002 - OPT_GOS3_PTR_VR_146 - verify  E2E flow of GOS3 claim form process successfully 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3ClaimE2EFlow(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_146", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(supDecNum, supColName);
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						String ActAmountDue = paymentLineDetailsList.get(1);
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 7913 - OPT_GOS3_PTR_VR_95 - verify   To validate on GOS3 Portal Patient Declaration screen 
	 * if Postcode is of valid format 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3ClaimInvalidPostCode(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_95", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_95", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_95", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_95", 4);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_95", 5);
		int days= Integer.parseInt(strDays);
		
		String strAddNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_95", 6);
		int addNum= Integer.parseInt(strAddNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		ObjOPGOS3PatientDetails.setPrescriptionDate(days);
		ObjOPGOS3PatientDetails.enterPerformerDetails(environment);
		ObjOPGOS3PatientDetails.enterAddressManually(addNum);
		
		if(evidence)
		{
			ObjOPGOS3PatientDetails.ErrorsnapOnAddressPostCode(key+"_Error_AddressPostCode");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3PatientDetails.AcutalAddressErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 4);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list. \n Expected Errors: "+ExpectedErrorMessagesonSave+"\n Actual Errors: "+ActualErrorMessagesonSave+"\n");
		}	
		
	}
	
	//Test script commented as this won't be applicable anymore 
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 7866 - OPT_GOS3_PTR_VR_48 -  To validate on GOS3 Portal Patient Declaration screen 
	 * "I have witnessed the signatory sign and date the cheque book" checkbox is mandatory
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"},enabled= false)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3ClaimPatElgWitnessSignMandatory(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_48", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(2);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
	//	ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(2, "RECORD2");
		
		if(evidence)
		{
			ObjOPGOS3PatientEligibility.GOS3ErrorsnapOnPatElgOnWitnessSignFlag(key+"_ErrorOnPatientEligibility_WitnessSignMandatoryFlag");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3PatientEligibility.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 5);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list. \n Expected Errors: "+ExpectedErrorMessagesonSave+"\n Actual Errors: "+ActualErrorMessagesonSave+"\n");
		}	
		
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 7792 - OPT_GOS3_PTR_VR_12 -   To validate on GOS3 Portal 
	 * Patient Postcode is a mandatory free text box that allows up to 8 characters 
	 * TC 7543 -  To validate on GOS3 Portal Performer's Name and Performer's List Number is mandatory
	 * TC 7540 -  To validate that on GOS3 Portal Contractor's Name is a type in field and auto complete list is displayed as the user start typing Contractor's name
	 * TC 7541 -  To validate that on GOS3 Portal  Contractor's Name is mandatory
	 * TC 7787 -  To validate on GOS3 Portal Patient's First Name field is mandatory
	 * TC 7788 -  To validate on GOS3 Portal Patient's SurName field is mandatory
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3PatientDetailsMandaotyFields(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_12", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_12", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_12", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_12", 4);
		
		//String key = "OPT_GOS3_PTR_VR_12";
		List<String> keys = Arrays.asList(keyArray);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		/*OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(2,0);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(14);*/
		ObjOPGOS3PatientDetails.clickOnSaveandNextButton();
		if(evidence)
		{
			for(String key: keys)
			{
			ObjOPGOS3PatientDetails.ErrorsnapOnPatientDetails(key+"_ErrorOnPatientDetails_1", key+"_ErrorOnPatientDetails_2");
			}
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3PatientDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 6);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list. \n Expected Errors: "+ExpectedErrorMessagesonSave+"\n Actual Errors: "+ActualErrorMessagesonSave+"\n");
		}	
		
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 7894 - OPT_GOS3_PTR_VR_76 -    To Validate GOS3, 
	 * if there is a non-0 value for the cyl, Axis value is mandatory 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3Validation_AxisValueIsMandatory(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_76", 9);
		
		String key = strKeys;
		
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		if(evidence)
		{
			
			ObjOPGOS3Prescription.GOS3ErrorsnapOnPrescriptionRightDistance(key+"AxisValueIsMandatory");
			
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3Prescription.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 7);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Validation Error Message for Axis Value is mandatory appeared when non zero cylinder value.", 3);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "Validation Error Message for Axis Value is mandatory is not matching with Expected Error list. \n Expected Message: "+ExpectedErrorMessagesonSave+"\n Actual Error Message: "+ActualErrorMessagesonSave+"\n");
		}	
		
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 7903 - OPT_GOS3_PTR_VR_85 -  To validate performer's details in the prescription form 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3Validation_PerformerDetailsOnPrescription(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_85", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_85", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_85", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_85", 4);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_85", 5);
		int days= Integer.parseInt(strDays);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_85", 6);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String key = strKeys;
		
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		ObjOPGOS3PatientDetails.setPrescriptionDate(days);
		ObjOPGOS3PatientDetails.enterPerformerDetails(environment);
		ObjOPGOS3PatientDetails.enterPatientDetails(patDetNum);
		List<String> PerfDetailsonPatientDet = ObjOPGOS3PatientDetails.getPerformerDetails();
	//	OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.clickOnSaveandNextButton();
	//	OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		
		if(evidence)
		{
			ObjOPGOS3Prescription.GOS3PerformerDetailsOnPrescription(key+"_PerformerDetails");
		}
		
		List<String> PerformerDetails = ObjOPGOS3Prescription.GOS3PerformerDetailsonPrescription();
		System.out.println("Actual Details: "+PerformerDetails);
		System.out.println("Expected Details: "+PerfDetailsonPatientDet);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(PerformerDetails, PerfDetailsonPatientDet);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Performer Details on Patient Details are matching with Prescription-Performer details");
			setAssertMessage("Performer Details on Patient Details are matching with Prescription-Performer details", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "Performer Details on Patient Details are not matching with Prescription-Performer details. /n Expected Details: "+PerfDetailsonPatientDet+"/n Actual Details: "+PerformerDetails+"\n");
		}	
		
	}
	
	/***************************************************************************************************
	 * 	Amit R : - This is Regression Test Case 7904 - OPT_GOS3_PTR_VR_86 - To validate 
	 * Date of Prescription field of patient  details screen on GOS3 Portal 
	 ****************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3Validation_DateOfPrescriptionOnPatientDetails(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_86", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_86", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_86", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_86", 4);
		
		String strDate= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_86", 5);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_86", 6);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String key = strKeys;
		
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		ObjOPGOS3PatientDetails.setPrescDate(strDate);
		ObjOPGOS3PatientDetails.enterPerformerDetails(environment);
		ObjOPGOS3PatientDetails.enterPatientDetails(patDetNum);
		List<String> PerfDetailsonPatientDet = ObjOPGOS3PatientDetails.getPerformerDetails();
	//	OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjOPGOS3PatientDetails.ErrorsnapOnInvalidSightTestDate(key+"_InvalidSightTestDateError");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3PatientDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 8);
		//ExpectedErrorMessagesonSave.add("Signature is missing");//Added by Akshay to verify script on UI.
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Error Message - Date of sight test is invalid - appeared correctly", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "Validation Error Message for Invalid Date of Sight Date is not matching with Expected Error./n Expected Messages: "+ExpectedErrorMessagesonSave+"/n Actual Messages: "+ActualErrorMessagesonSave+"\n");
		}	
	}
	
	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case 7905 - OPT_GOS3_PRT_VR_87.  To validate mandatory field selection of 
	 * supplier's declaration
	 * TC 7906 - OPT_GOS3_PTR_VR_88 -To validate mandatory field selection of supplier's declaration of 2nd Pair of values
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"},enabled= false)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3_SuppDeclValidationErrors(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 12);
		
		String strSupDecNum2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 13);
		int supDecNum2= Integer.parseInt(strSupDecNum2);
		
		String strSupDecNum3= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 14);
		int supDecNum3= Integer.parseInt(strSupDecNum3);
		
		String firstPairGlassValue= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 15);
		String firstPairGlassValue2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_87", 16);
		
		List<String>keys= Arrays.asList(keyArray);
		String key = keys.get(0);
		String key2 = keys.get(1);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3ErrorsnapOnSuppValidationError(key+"_MandatoryFieldsValidationErrors");
		}
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 9);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All mandatory errors on supplier declaration are not appeared correctly. \n Expected Error Message: "+ExpectedErrorMessagesonSave+"\n Actual Error Message: "+ActualErrorMessagesonSave+"\n");
		}	
		
		//ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(18, "RECORD101");
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3snapOnSuppValidationError(key+"_ValidationError1");
		}
		List<String> ActualErrorMessages1 = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessages1);
		List<String> ExpectedErrorMessages1 = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 10);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessages1);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList1 = CommonFunctions.compareStrings(ActualErrorMessages1, ExpectedErrorMessages1);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList1);
					
		if(unmatchedErrorList1.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Selecting one option is mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList1.isEmpty(), "Selecting one option is mandatory errors on supplier declaration are not appeared correctly. \n Expected Error: "+ExpectedErrorMessages1+"\n Actual Error Message: "+ActualErrorMessages1+"\n");
		}	
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum2);
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3snapOnSuppValidationError(key+"_ValidationError2");
		}
		List<String> ActualErrorMessages2 = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessages2);
		List<String> ExpectedErrorMessages2 = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 11);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessages2);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList2 = CommonFunctions.compareStrings(ActualErrorMessages2, ExpectedErrorMessages2);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList2);
					
		if(unmatchedErrorList2.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Selecting one option is mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList2.isEmpty(), "Selecting one option is mandatory errors on supplier declaration are not appeared correctly. \n Expected Error Message: "+ExpectedErrorMessages2+"\n Actual Error Message: "+ActualErrorMessages2+"\n");
		}	
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum3);
		ObjOPGOS3SupplierDeclaration.clearFirstPairGlassValue();
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3snapOnSuppValidationError(key+"_ValidationError3");
		}
		
		List<String> ActualErrorMessages3 = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessages3);
		List<String> ExpectedErrorMessages3 = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 14);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessages3);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList3 = CommonFunctions.compareStrings(ActualErrorMessages3, ExpectedErrorMessages3);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList3);
					
		if(unmatchedErrorList3.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Selecting one option is mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList3.isEmpty(), "Selecting one option is mandatory errors on supplier declaration are not appeared correctly. \n Expected Error Message: "+ExpectedErrorMessages3+"\n Actual Error Message: "+ActualErrorMessages3+"\n");
		}	
		
		ObjOPGOS3SupplierDeclaration.setValueInFirstPairGlassValue(firstPairGlassValue);
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3snapOnSuppValidationError(key+"_ValidationError4");
		}
		
		List<String> ActualErrorMessages4 = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessages4);
		List<String> ExpectedErrorMessages4 = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 15);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessages4);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList4 = CommonFunctions.compareStrings(ActualErrorMessages4, ExpectedErrorMessages4);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList4);
					
		if(unmatchedErrorList4.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Selecting one option is mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList4.isEmpty(), "Selecting one option is mandatory errors on supplier declaration are not appeared correctly. \n Expected Error Message: "+ExpectedErrorMessages4+"\n Actual Error Message: "+ActualErrorMessages4+"\n");
		}	
		
		ObjOPGOS3SupplierDeclaration.setValueInFirstPairGlassValue(firstPairGlassValue2);
		ObjOPGOS3SupplierDeclaration.selectnearPairCheckbox();
		ObjOPGOS3SupplierDeclaration.selectSecondPairGlassCheckBox();
		ObjOPGOS3SupplierDeclaration.setDateofSecondPairSupplied();
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3snapOnSuppValidationError(key2+"_ValidationError1");
		}
		
		List<String> ActualErrorMessages5 = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessages5);
		List<String> ExpectedErrorMessages5 = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 16);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessages5);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList5 = CommonFunctions.compareStrings(ActualErrorMessages5, ExpectedErrorMessages5);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList5);
					
		if(unmatchedErrorList5.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Selecting one option is mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList5.isEmpty(), "Selecting one option is mandatory errors on supplier declaration are not appeared correctly. \n Expected Error Message: "+ExpectedErrorMessages5+"\n Actual Error Message: "+ActualErrorMessages5+"\n");
		}	
		
		ObjOPGOS3SupplierDeclaration.setValueInSecondPairGlassValue(firstPairGlassValue);
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();
		
		if(evidence){
			ObjOPGOS3SupplierDeclaration.GOS3snapOnSuppValidationError(key2+"_ValidationError2");
		}
		
		List<String> ActualErrorMessages6 = ObjOPGOS3SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessages6);
		List<String> ExpectedErrorMessages6 = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 17);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessages6);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList6 = CommonFunctions.compareStrings(ActualErrorMessages6, ExpectedErrorMessages6);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList6);
					
		if(unmatchedErrorList6.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Selecting one option is mandatory errors on supplier declaration are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList5.isEmpty(), "Selecting one option is mandatory errors on supplier declaration are not appeared correctly. \n Expected Error Message: "+ExpectedErrorMessages6+"\n Actual Error Message: "+ActualErrorMessages6+"\n");
		}	
		
		softAssertion.assertAll();

	}
	
	
	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case 7907 - OPT_GOS3_PRT_VR_89.  To validate on GOS3 Portal Patient Eligibility screen 
	 * "Declaration… I am the…" if none are selected and if multiple are selected.
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3_PatElgValidationErrors(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 9);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 10);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 11);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_89", 12);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjOPGOS3PatientEligibility.GOS3ErrorsnapOnPatElgValidationError(key+"_ValidationErrors");
			
		}
		
		/*OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails("RECORD1", "C", "D");
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3SupplierDeclaration.clickOnSaveNNextButton();*/
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS3PatientEligibility.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 19);//12
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient signatory on patient eligibility are appeared correctly", 1);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		
				
		
		softAssertion.assertAll();

	}
	
	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case 7908 - OPT_GOS3_PTR_VR_90.   To validate on GOS3 Portal Patient Declaration screen 
	 * if user has selected signatory is the patient, the details should populate from the Patient Details screen 
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3_VerifyAddressOnPatientDecl(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 4);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 5);
		int days= Integer.parseInt(strDays);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 6);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PTR_VR_90", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		ObjOPGOS3PatientDetails.setPrescriptionDate(days);
		ObjOPGOS3PatientDetails.enterPerformerDetails(environment);
		ObjOPGOS3PatientDetails.enterPatientDetails(patDetNum);
		String ExpAddressDetails = ObjOPGOS3PatientDetails.getAddressDetails(key+"_ExpectedAddressDetails");
		System.out.println(ExpAddressDetails);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.clickOnSaveandNextButton();
		
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(supDecNum, supColName);

//		if(evidence)
//		{
//			ObjOPGOS3SupplierDeclaration.	(key+"_Portal_ClaimDetails");
//		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		ObjOPGOS3PatientDeclaration.enterSignatoryDetails();
		String ActAddrDetails = ObjOPGOS3PatientDeclaration.getAddressDetails(key+"_ActualAddressDetails");
		System.out.println(ActAddrDetails);
		
		Assert.assertEquals(ActAddrDetails, ExpAddressDetails,"The Address details on Patient Declaration is not matching with Patient Details Screen");
		

	}
	
	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case 5180 - OPT_GOS3_PRT_BR_13.  GOS3 claim is rejected, 
	 * if Eligible for complex is checked, Prescription is of less than 10 diopters and Complex voucher is selected.
	 * TC 7800 - OPT_GOS3_PTR_VR_20 -  To validate on GOS3 Portal Patient NHS Number is an optional field 
	 * TC 7805 - OPT_GOS3_PTR_VR_25 - GOS3 Portal user is not allowed to select both "Seen" and "Not Seen"
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3_ClaimRejection_ComplexLenseWithPrescriptionLessThan10Diaopters(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 2);
		String[]keyArray= strKeys.split(",");
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 4);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 5);
		int days= Integer.parseInt(strDays);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 6);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 7);
		String strPresNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 8);
		int presNum= Integer.parseInt(strPresNum);
		
		String colName2= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 9);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 10);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 11);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 12);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 13);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3_PRT_BR_13", 14);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		List<String>keys= Arrays.asList(keyArray);
		
		String key = keys.get(0);
		String key2 =keys.get(1);
		String key3 = keys.get(2);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		//OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,0);
		ObjOPGOS3PatientDetails.setPrescriptionDate(days);
		ObjOPGOS3PatientDetails.enterPerformerDetails(environment);
		ObjOPGOS3PatientDetails.enterPatientDetails(patDetNum);
		if(evidence)
		{
			ObjOPGOS3PatientDetails.snapForNHSNumberBlank(key2+"_NHSNumberBlank");
		}
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.clickOnSaveandNextButton();
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectProvidedOptions(presNum);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		if(evidence)
		{
			//ObjOPGOS3PatientEligibility.GOS3ErrorsnapOnPatElgValidationError(key+"_ValidationErrors");
			ObjOPGOS3Prescription.GOS3ErrorsnapOnPrescriptionRightDistance(key+"_ValidationErrors_PrescLessThan10Diaopters");			
		}
		List<String> ActualErrorMessagesonSave = ObjOPGOS3Prescription.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", 13);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient signatory on patient eligibility are appeared correctly", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "Actual Error Message is not matching with Expected Error Message. /n Expected Errors: "+ExpectedErrorMessagesonSave+"/n Actual Errors: "+ActualErrorMessagesonSave+"/n");
		}	
		
		//ObjOPGOS3Prescription.enterPrescriptionDetails("RECORD50");
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName2, firstVoucher, secondVoucher);
		ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		if (evidence)
		{
			ObjOPGOS3PatientEligibility.GOS3_SeenNotSeenCheckboxSnap(key3+"_SeenOptionSelected");
		}
		ObjOPGOS3PatientEligibility.selectNotSeenChekBox();
		if (evidence)
		{
			ObjOPGOS3PatientEligibility.GOS3_SeenNotSeenCheckboxSnap(key3+"_NotSeenOptionSelected");
		}
		setAssertMessage("GOS3 Portal user is not allowed to select only one option at at time either SEEN or NOT SEEN.",1);
		
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(5);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
//		if (evidence)
//		{
//			ObjOPGOS3PatientEligibility.GOS3_NavigatedToPatEligibility(key2+"_NaviagatedToPatientEligibility");
//		}
		
		softAssertion.assertAll();

	}
	
	/*	***********************************************************************************************************
	 * Amit R : - This is Regression Test Case - 12588 - OPT_RBAC_09 - Validate if user having user role "PCSEClaimsProcessor" with 
	 * privilege "ManageAllGOSclaims" is logged in, then user can access all screens of existing GOS 3 claim with status "Draft" and can make changes to any fields
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3ClaimInDraft_RBAC(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_RBAC_09", 14);
		
		String keyname = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		//OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS3PatientEligibility.getClaimNumber(keyname);
		quit(browser);
		
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();
		ObjOPGOS3Prescription = ObjOPSearchClaim.selectAndOpenClaim_GOS3(ClaimNo);
		ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButtonWithoutSignatory();
		
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(supDecNum, supColName);
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(keyname+"_Portal_ClaimDetails");
		}

		

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		


	}
	
	
	// this has been commented as end to end flow for Statement is completed in GOS1 script...
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - OPT_POS_13. Validate the POS calculation amount and sum of 
	 * Total number of payment lines per ophthalmic claim type is displayed within the Payment Header where 
	 * ‘Receive POS Payment’ is True for each ‘POS Payment Rate”
	 ******************************************************************************************************************	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPOSCalculation(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		List<String> keys = Arrays.asList("OPT_POS_13");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		//OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1);
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1,environment);
		//OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails("RECORD1", "C", "D");
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(1, "RECORD1");
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}

		

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(1);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");
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
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
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
		setup(browser, environment, clientName, "GMP");
		//String PaymentLineDueDate="4/16/2018";
		//setup(browser, environment, clientName,"GMP","PCSEPAYMENTSPROCESSOR");	
		ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		//GMPHome ObjGMPHome = new GMPHome(getDriver());
		Date date= CommonFunctions.convertStringtoCalDate(paymentDueDate, "MM/dd/yyyy");
		String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
		String paymentRunName= ObjGMPHome.getPaymentRunName();
		System.out.println("Payment run name is: "+paymentRunName);
		setAssertMessage("Payment run name is: "+paymentRunName, 4);
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload();
		tearDown(browser);
		Thread.sleep(2000);
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValueList= Arrays.asList("fld");
		List<String>FieldValueList= Arrays.asList("Payment Run");
		List<String>ConditionValueList= Arrays.asList("Equals");
		List<String>ValueTypeList= Arrays.asList("Lookup");
		List<String>ValueForFieldValueList= Arrays.asList(paymentRunName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Payment Files");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			int records= objAdvancedFindResult.getRecordCount();
			for(int i=0;i<records;i++){
				String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(i,2);
				if(title!= null)
				{
					ObjAdvancedFindResult= ObjAdvancedFindResult.updatePaymentFileStatus("ISFE Validated");
					System.out.println("Payment file status has been updated for index: "+i);
					setAssertMessage("Payment file status has been updated for index: "+i, 4);
				}
			}

		}
		tearDown(browser);
		Thread.sleep(5000);
		setup(browser, environment, clientName,"OPSTATEMENT");
		ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjHomePage = ObjLoginScreen.logintoOP_Home("PCSEPaymentsProcessor", environment);
		OPStatement ObjOPStatement = ObjHomePage.redirectToStatementPage(environment);
		ObjOPStatement= ObjOPStatement.enterFilterData();
		if (evidence)
		{
			for (String key: keys){
				ObjOPStatement.OPStatementSnaps(key+"StatementSearchRecord");
			}
		}
		OP_StatementDetails ObjOP_StatementDetails = ObjOPStatement.clickOnStatementRefLink(environment);
		if(evidence)
		{
			for (String key:keys)
			ObjOP_StatementDetails.OPStatementDetailsSnaps(key+"StatementDetails");
		}
		System.out.println("Statement has been generated against a Payment File");
		setAssertMessage("Statement has been generated against a Payment File", 5);
	}
*/
	
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - 23364 - OPT_WOP_Prescription_GOS3_Portal_11 - Verify GOS3 portal claim , 
	 * if  the near add SPH is blank for both eyes, first voucher type is mandatory (A to D only), and second voucher type should be disabled
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void firstVoucherAndBlankSphForBothEyes(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException, SQLException
	{
		String key= "OPT_WOP_Prescription_GOS3_Portal_11_1";
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 8);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 12);
		String strHC3Value= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 13);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 14);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHC3Value);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


		String CRMMode= ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - 23365 - OPT_WOP_Prescription_GOS3_Portal_12_2 - Verify GOS3 portal claim , 
	 * if  the near add SPH has a value it is permitted to have, first voucher type E to H, in which case second voucher 
	 * type should be disabled or
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void firstVoucherTypeAsEAndSecondVoucherDisabled(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException, SQLException
	{
		String key= "OPT_WOP_Prescription_GOS3_Portal_12_2";
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 8);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 12);
		String strHC3Value= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 13);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 14);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHC3Value);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


		String CRMMode= ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - 23365 - OPT_WOP_Prescription_GOS3_Portal_12_12 - Verify GOS3 portal claim , 
	 * if  the near add SPH has a value it is permitted to have, first voucher type E to H, in which case second voucher 
	 * type should be disabled or
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void firstVoucherTypeAsHAndSecondVoucherDisabled(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException, SQLException
	{
		String key= "OPT_WOP_Prescription_GOS3_Portal_12_12";
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 8);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 12);
		String strHC3Value= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 13);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 14);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHC3Value);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


		String CRMMode= ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - 23366 - OPT_WOP_Prescription_GOS3_Portal_13 - Verify GOS3 portal claim , 
	 * if  the near add SPH has a value it is permitted to have, first voucher type A to D and second voucher type A to D  or
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void bothGOS3Voucher(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException, SQLException
	{
		String key= "OPT_WOP_Prescription_GOS3_Portal_13_3";
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(patEleNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDeclarationDetails(supDecNum, supColName);
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);

		String CRMMode= ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - 23367 - OPT_WOP_Prescription_GOS3_Portal_14 - Verify GOS3 portal claim , 
	 * If the near add SPH has a value it is permitted to have, first voucher type A to D only or
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void firstVoucherTypeAsDAndSecondVoucherDisabled(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException, SQLException
	{
		String key= "OPT_WOP_Prescription_GOS3_Portal_14_11";
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 8);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 12);
		String strHC3Value= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 13);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 14);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectFirstVoucherType(firstVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHC3Value);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


		String CRMMode= ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	
	/*	***********************************************************************************************************
	 * Akshay S : - This is Regression Test Case - 23368 - OPT_WOP_Prescription_GOS3_Portal_15 - Verify GOS3 portal claim , 
	 * if  the near add SPH has a value it is permitted to have, second voucher type A to D only
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void firstVoucherDisabledAndSecondVoucherAsD(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException, SQLException
	{
		String key= "OPT_WOP_Prescription_GOS3_Portal_15_1";
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 7);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 8);
		
		String strPatEleOptNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 9);
		int patEleOptNum= Integer.parseInt(strPatEleOptNum);
		
		String strPatEleSuplNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 10);
		int patEleSuplNum= Integer.parseInt(strPatEleSuplNum);
		
		String strPatEleBenNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 11);
		int patEleBenNum= Integer.parseInt(strPatEleBenNum);
		
		String strCertificateNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 12);
		String strHC3Value= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 13);
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 14);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 15);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 16);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 17);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 18);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 19);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "OPGOS3ScriptParameters", key, 20);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails(colName);
		ObjOPGOS3Prescription.selectSecondVoucherType(secondVoucher);
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		ObjOPGOS3PatientEligibility.selectProvidedOptions(patEleOptNum);
		ObjOPGOS3PatientEligibility.enterSupplimentaryDetails(patEleSuplNum);
		ObjOPGOS3PatientEligibility.enterBeneficiaryDetails(patEleBenNum);
		ObjOPGOS3PatientEligibility.enterHC3CertificateDetails(strCertificateNum);
		ObjOPGOS3PatientEligibility.enterHCVoucherValue(strHC3Value);
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.clickOnSaveandNextButton();
		
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(supDecNum);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails(supColName);
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(patDecNum);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS3PatientDeclaration.clickCloseOnResultPopup();
		quit(browser);


		String CRMMode= ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
		if(CRMMode.equalsIgnoreCase("CRMUI")){
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
						List<String> paymentLineDetailsList = new ArrayList<String>();
						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
							paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
							String PaymentLineStatus = paymentLineDetailsList.get(0);
							System.out.println(PaymentLineStatus);
							String ActAmountDue = paymentLineDetailsList.get(1);
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS1","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(fileName,key,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	
}


