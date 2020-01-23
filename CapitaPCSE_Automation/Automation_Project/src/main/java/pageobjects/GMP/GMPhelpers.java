package pageobjects.GMP;
import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import browsersetup.BaseClass;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GPP.CI.GPPHomePage;
import pageobjects.GPP.SC.SC_RegistrarExpenseClaim;
import pageobjects.GPP.SC.StdClaimsApprovalWindow;
import pageobjects.GPP.SC.StdClaimsPortal;
import pageobjects.OP.*;
import utilities.ExcelUtilities;
import verify.Verify;
public class GMPhelpers extends BaseClass {

	public static void CreateClaim(WebDriver driver, String environment) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key = "OPT_GOS3_PRT_BR_70";
		boolean evidence = true;
	//	setup(browser, environment, clientName,"OP");
		OPHomePage ObjOPHomePage = new OPHomePage(driver);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS3PatientDetails ObjOPGOS3PatientDetails = ObjMakeAClaim.clickGOSThreeButton();
		OPGOS3Prescription ObjOPGOS3Prescription = ObjOPGOS3PatientDetails.PatientDetailsEntered(2,0,environment);
		ObjOPGOS3Prescription.enterPrescriptionDetails("RECORD3");
		ObjOPGOS3Prescription.selectFirstVoucherType("E");
		
		OPGOS3PatientEligibility ObjOPGOS3PatientEligibility = ObjOPGOS3Prescription.clickOnSaveandNextButton();
		OPGOS3SupplierDeclaration ObjOPGOS3SupplierDeclaration = ObjOPGOS3PatientEligibility.PatientEligibilityDetailsEntered(2);
		ObjOPGOS3SupplierDeclaration.selectProvidedOptions(3);
		ObjOPGOS3SupplierDeclaration.enterGOS3SupplierDetails("RECORD3");
		ObjOPGOS3SupplierDeclaration.setDateofFirstPairSupplied();
		
		if(evidence)
		{
			ObjOPGOS3SupplierDeclaration.GOS3ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}

		OPGOS3PatientDeclaration ObjOPGOS3PatientDeclaration = ObjOPGOS3SupplierDeclaration.clickOnSaveandNextButton();
		ObjOPGOS3PatientDeclaration.selectDeclarationOptions(2);
		String ClaimNo = ObjOPGOS3PatientDeclaration.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS3PatientDeclaration.clickOnSubmitButton();
		if (clmSubFlag)
		{
		//	setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS3PatientDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				System.out.println("The claim " +ClaimNo+" gets submitted successfully.");
			//	setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 2);
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
	}

	public static void VerifyOnCRM_GMP(WebDriver driver) throws InterruptedException, IOException, AWTException
	{
		// To verify status of GOS3 claim submitted in CRM.
		String key = "OPT_GOS1_PTR_BR_15";
		boolean evidence = true;	
		CrmHome ObjCrmHome  = new CrmHome(driver);
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
				//	setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
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
						if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
						{
							System.out.println("The Payment Line status is Pending");
							//setAssertMessage("The Payment Line status is Pending.", 1);
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

	public static void CreateGPPStdClaim(WebDriver driver,String environment) throws InterruptedException, IOException, AWTException, ParseException
	{
		String key= "GPP_SC_165"; 
		boolean evidence = true;
		//setup(browser, environment, clientName,"GPP");
		GPPHomePage objGPPHomePage= new GPPHomePage(driver);
		StdClaimsPortal objStdClaimsPortal= objGPPHomePage.clickOnStdClaimsMenuButton();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(3);
		objSC_RegistrarExpenseClaim.createRegistrarClaim(3,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(3);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		objStdClaimsPortal.setClaimNumberforGMP(claimNo,2);
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		//setAssertMessage("Registrar claim no " +claimNo+ " has been created", 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
	
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objStdClaimsPortal.clickOnClaimApproval();
		objStdClaimsApprovalWindow.approveStdClaim(claimNo,"RegistrarExpenseClaimData",3);
		objStdClaimsPortal= objStdClaimsApprovalWindow.clickOnClaimPortal();
		
		String finalClaimAndPLStatus= objStdClaimsPortal.getClaimStatus();
		String statusArray[]= finalClaimAndPLStatus.split("-");
		String finalClaimStatus= statusArray[0].toString().trim();
		assertEquals(finalClaimStatus, "Approved");
		objStdClaimsPortal.setClaimStatusforGMP(finalClaimStatus,2);
		System.out.println("Claim status is "+claimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_Approved_RegistrarClaims_"+claimNo);
		}
		//setAssertMessage("Claim approved", 1);
	}
	
	public static void VerifyGPPClaimOnCRM_GMP(WebDriver driver) throws InterruptedException, IOException, AWTException
	{
		// To verify status of GOS3 claim submitted in CRM.
		String key= "GPP_SC_165"; 
		boolean evidence = true;	
		CrmHome ObjCrmHome  = new CrmHome(driver);
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 4;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "EXPPAYMENTLINESTATUS");
		//Commented by Akshay to check
		/*String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");*/

		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen_GPPSC();
				if (clmStatus.contains(claimStatus))
				{
					//setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus_GPPSC(ValueForFieldValue, evidence,key+"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
						{
							System.out.println("The Payment Line status is Pending");
							//setAssertMessage("The Payment Line status is Pending.", 1);
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
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not Approved.");
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
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"}, priority=0)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public static void GOS1Claim(WebDriver driver,String environment ,boolean evidence) throws InterruptedException, IOException, AWTException
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
		
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		LoginScreen ObjLoginScreen = new LoginScreen(driver);
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		boolean isHeaderPresent= ObjOPHomePage.verifyHeaderPresence(tabName);
		if(isHeaderPresent){
			SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
			ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		}
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
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
		ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "Claim Number", ClaimNo, 1);
		Boolean clmSubFlag = ObjOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			
			String msg = ObjOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains(expSubmitMsg))
			{
				System.out.println("The claim " +ClaimNo+" gets submitted successfully.");
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

	}
}
