package testscripts;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import helpers.WindowHandleSupport;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.OP.*;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class OP_GOS3VoucherScripts extends BaseClass
{
	/*	***********************************************************************************************************
	 * Amit R : - This is Sanity Test Case - OPT_GOS3VO_PRT_02. TC-11660 - Verify GOS 3 Voucher is created 
	 * successfully in Portal, the same Voucher is retrieved and completed via Portal.
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3VoucherSuccessfulFlow(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
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
		String vCode = ObjOPGOS3Prescription.getVoucherCode();
		String aCode = ObjOPGOS3Prescription.getAuthCode();
		ObjOPGOS3Prescription.enterPrescriptionSignatory();
		ObjOPHomePage = ObjOPGOS3Prescription.clickOnCreateVoucherButton(key);
		
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		
		OPOpticalVoucherRetrieval ObjOPOpticalVoucherRetrieval = ObjOPSearchForClaim.clickOnVoucherClaimSearch();
		ObjOPOpticalVoucherRetrieval.searchVoucherDetails(vCode,aCode);
		ObjOPGOS3Prescription = ObjOPOpticalVoucherRetrieval.clickOnSearchRecord();
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButtonWithoutSignatory();
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
	 * Amit R : - This is Sanity Test Case - OPT_GOS3VO_PRT_02. TC-11660 - Verify GOS 3 Voucher is created 
	 * successfully in Portal, the same Voucher is retrieved and completed via Portal.
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS3VoucherSearch(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 6);
		int days= Integer.parseInt(strDays);
		
		String colName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 7);
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 8);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 9);
		
		String strPatEleNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 10);
		int patEleNum= Integer.parseInt(strPatEleNum);
		
		String strSupDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 11);
		int supDecNum= Integer.parseInt(strSupDecNum);
		
		String supColName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 12);
		
		String strPatDecNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 13);
		int patDecNum= Integer.parseInt(strPatDecNum);
		
		String expSubmitMsg= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 14);
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 15);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 16);
		
		String strAdvFindNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_GOS3VO_PRT_01", 17);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key = strKeys;
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
		String vCode = ObjOPGOS3Prescription.getVoucherCode();
		String aCode = ObjOPGOS3Prescription.getAuthCode();
		ObjOPGOS3Prescription.enterPrescriptionSignatory();
		ObjOPHomePage = ObjOPGOS3Prescription.clickOnCreateVoucherButton(key);
		
		quit(browser);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		
		OPOpticalVoucherRetrieval ObjOPOpticalVoucherRetrieval = ObjOPSearchForClaim.clickOnVoucherClaimSearch();
		ObjOPOpticalVoucherRetrieval.searchVoucherDetails(vCode,aCode);
		ObjOPGOS3Prescription = ObjOPOpticalVoucherRetrieval.clickOnSearchRecord();
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButtonWithoutSignatory();
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
	 * Akshay S : - This is Regression Test Case - OPT_CR10_GOS3VO_PTR_01. TC-18568 - Verify prescription change to Prism and 
	 * Base in GOS 3 Voucher Only Portal.
	 *******************************************************************************************************************/	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateGOS3VoucherPrismAndBaseChange(String browser,String environment, String clientName, Boolean evidence) throws InterruptedException, IOException, ParseException, AWTException
	{
		String module= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 1);
		String strKeys= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 2);
		
		String role= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 3);
		String tabName= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 4);
		
		String strPatDetNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 5);
		int patDetNum= Integer.parseInt(strPatDetNum);
		
		String strDays= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 6);
		int days= Integer.parseInt(strDays);
		
		String recordCountStart= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 7);
		int recordCount= Integer.parseInt(recordCountStart);
		
		String strRecordCountEnd= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 8);
		int recordCountEnd= Integer.parseInt(strRecordCountEnd);
		
		String strVerificationNo= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 9);
		int verificationNo= Integer.parseInt(strVerificationNo);
		
		String firstVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 10);
		String secondVoucher= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 11);
		
		String strPresMsgNum= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 12);
		int presMsgNum= Integer.parseInt(strPresMsgNum);
		
		String strValidRecordCount= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 13);
		int validRecordCount= Integer.parseInt(strValidRecordCount);
		
		String CRMModule= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 14);
		String CRMUSER= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "OPGOS3ScriptParameters", "OPT_CR10_GOS3VO_PTR_01", 15);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(patDetNum,days,environment);
		int verificationNumber=verificationNo;
		//validation message condition checking
		for(int i=recordCount;i<=recordCountEnd;i++){
			
			String strRecordCount= Integer.toString(i);
			String colName= "RECORD"+strRecordCount;
			ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
			ObjOPGOS3Prescription.enterPrescriptionSignatory();
			System.out.println("Details entered for column name: "+colName);
			ObjOPHomePage = ObjOPGOS3Prescription.clickOnCreateVoucherButton(key);
			if(evidence){
				ObjOPGOS3Prescription.GOS3ErrorsnapOnPrescriptionRightDistance(key+"_Validation messages recod number_"+colName);
			}
			List<String> ActualErrorMessagesonSave = ObjOPGOS3Prescription.getErrors();
			System.out.println("Actual Error Messages on Save for recordcount: "+colName+": "+ActualErrorMessagesonSave);
			List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "VALIDATIONERRORS", presMsgNum);
			System.out.println("Expected Error Messages on Save for recordcount: "+colName+": "+ExpectedErrorMessagesonSave);
			List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
			System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			SoftAssert softAssertion= new SoftAssert();
			if(unmatchedErrorList.isEmpty())
			{
				System.out.println("Actual error list on save action is matching with expected list for column name: "+colName);
				setAssertMessage("All errors for patient signatory on contractor declaration are appeared correctly for column name: "+colName, verificationNumber);
			}
			else
			{
				softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on contractor declaration are not appeared correctly for column name: "+colName);
			}
			softAssertion.assertAll();
			ActualErrorMessagesonSave.remove(ActualErrorMessagesonSave);
			verificationNumber++;
		}
		//valid entry starts
		recordCount= validRecordCount;
		String strRecordCount= Integer.toString(recordCount);
		String colName= "RECORD"+strRecordCount;
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		ObjOPGOS3Prescription.enterPrescriptionSignatory();
		if(evidence){
			ObjOPGOS3Prescription.GOS3ErrorsnapOnPrescriptionRightDistance(key+"_valid data for column number_"+colName);
		}
		System.out.println("Details entered for column name: "+colName);
		ObjOPHomePage = ObjOPGOS3Prescription.clickOnCreateVoucherButton(key);
		System.out.println("All valid entries are verified for column name: "+colName);
		setAssertMessage("All valid entries are verified for column name: "+colName, verificationNumber);
		recordCount++;
		verificationNumber++;
		//WindowHandleSupport.getRequiredWindowDriver(getDriver(), "PCSE");
		Thread.sleep(4000);
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(1,1,environment);
		strRecordCount= Integer.toString(recordCount);
		colName= "RECORD"+strRecordCount;
		ObjOPGOS3Prescription.enterPatientPrescriptionDetails(colName, firstVoucher, secondVoucher);
		String vCode = ObjOPGOS3Prescription.getVoucherCode();
		System.out.println("Voucher code is: "+vCode);
		//String aCode = ObjOPGOS3Prescription.getAuthCode();
		ObjOPGOS3Prescription.enterPrescriptionSignatory();
		System.out.println("Details entered for column name: "+colName);
		if(evidence){
			ObjOPGOS3Prescription.GOS3ErrorsnapOnPrescriptionRightDistance(key+"_valid data for column number_"+colName);
		}
		ObjOPHomePage = ObjOPGOS3Prescription.clickOnCreateVoucherButton(key);
		//WindowHandleSupport.getRequiredWindowDriver(getDriver(), "PCSE");
		System.out.println("All valid entries are verified for column name: "+colName);
		setAssertMessage("All valid entries are verified for column name: "+colName, verificationNumber);
		System.out.println("All valid and invalid entries are verified");
		verificationNumber++;
		setAssertMessage("All valid and invalid entries are verified", verificationNumber);
		tearDown(browser);
		Thread.sleep(2000);
		// To verify status of GOS3 voucher submitted in CRM.
		setup(browser, environment, clientName,CRMModule,CRMUSER);
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValue= Arrays.asList("fld");
		List<String>FieldValue= Arrays.asList("Voucher Code");
		List<String>ConditionValue= Arrays.asList("Equals");
		List<String>ValueType= Arrays.asList("Text");
		List<String>ValueForFieldValue= Arrays.asList(vCode);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("GOS3 Vouchers");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());

		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{	
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecordWithoutSorting(0,1);
			if(title!= null)
			{
				if (evidence){
					objAdvancedFindResult.takeScreenshot("GOS3 Voucher:",key+"_voucher entry_"+vCode);
				}
			}
			objAdvancedFindResult.closeWindow();
			WindowHandleSupport.getRequiredWindowDriver(getDriver(),"Advanced Find");
			setAssertMessage("Entry is present for voucher no: "+vCode, verificationNumber+1);
			System.out.println("Entry is present for voucher no: "+vCode);
			Boolean AlertPresent = objAdvancedFindResult.isAlertPresent();
			if(AlertPresent)
			{
				objAdvancedFindResult.ClickSpaceBar();
			}
		}
		
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}	
}


