package testscripts;

import java.awt.AWTException;
import java.io.IOException;
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
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.VarianceReportDetail;
import pageobjects.GMP.VarianceReports;
import pageobjects.OP.OPMakeAClaim;
import pageobjects.OP.OPContractorDeclaration;
import pageobjects.OP.OPGOS4NHSEnglandApproval;
import pageobjects.OP.OPGOS4PatientDeclaration;
import pageobjects.OP.OPGOS4PatientDeclaration2;
import pageobjects.OP.OPGOS4PatientDetails;
import pageobjects.OP.OPGOS4PatientEligibility;
import pageobjects.OP.OPGOS4SupplierDeclaration;
import pageobjects.OP.OPHomePage;
import pageobjects.OP.OPPatientDeclaration;
import pageobjects.OP.OPPatientDetails;
import pageobjects.OP.OPPatientEligibility;
import pageobjects.OP.OPPerformerDeclaration;
import pageobjects.OP.OPSearchClaim;
import pageobjects.OP.OPSearchForClaim;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class OP_GOS4ClaimScripts extends BaseClass
{
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8073 - OPT_GOS4_PRT_BR_48 - Voucher Type Lookup: To validate GOS4, 
	 * if Under Reading and Distance Prescription Tag are passed as A  and distance and 
	 * Near pair are selected then the claims accepted.
	 * 12589 - OPT_RBAC_10 - Validate if user having user role "PCSEClaimsProcessor" with privilege "ManageAllGOSclaims" is logged in, then user can access all screens of existing GOS 4 claim with status "Draft" and can make changes to any fields
	 * @throws AWTException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression", "CLONE"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingAndDistancePrescTag(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_48";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		String supDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","SupDeclOptionSheet");
		String patDeclOptionSheet2= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOptionSheet2");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String[]keyArray= strKeys.split(",");
		String role= values.get(2);
		String tabName= values.get(3);
		String voucherType= values.get(4);
		String retailCostType= values.get(5);
		String recordColName= values.get(6);
		String expSubmitMsg= values.get(7);
		String CRMModule= values.get(8);
		String CRMUSER= values.get(9);
		String strAdvFindNum= values.get(10);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);

		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
			if(evidence)
			{
				for (String key:keys)
				{
					ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
				}
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
		}
/*		OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
		ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
		ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
		ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(recordColName,newTestDataFileName,supDeclOptionSheet);*/
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		/*if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}*/
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
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
					//String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8074 - OPT_GOS4_PRT_BR_49 - Voucher Type Lookup: To validate GOS4, 
	 * if Under Reading Prescription spherical power of not more than 6 diopters , 
	 * cylindrical power of not more than 2 diopters  and first voucher type is A is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingPrescSPHNotMoreThan6Dioters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_49";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		String supDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","SupDeclOptionSheet");
		String patDeclOptionSheet2= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOptionSheet2");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String voucherType= values.get(4);
		String retailCostType= values.get(5);
		String recordColName= values.get(6);
		String expSubmitMsg= values.get(7);
		String CRMModule= values.get(8);
		String CRMUSER= values.get(9);
		String strAdvFindNum= values.get(10);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		/*if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}*/
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
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
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 7783 - OPT_GOS4_PTR_VR_28 - To validate E2E flow of gos 4 claim form process successfully
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_E2EFlow(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_49";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		String supDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","SupDeclOptionSheet");
		String patDeclOptionSheet2= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOptionSheet2");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String voucherType= values.get(4);
		String retailCostType= values.get(5);
		String recordColName= values.get(6);
		String expSubmitMsg= values.get(7);
		String CRMModule= values.get(8);
		String CRMUSER= values.get(9);
		String strAdvFindNum= values.get(10);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_ClaimDetails");
		}
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
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
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8044 - OPT_GOS4_PRT_BR_19 - Eligibility-DOB: To validate GOS4, 
	 * if eligibility option patient is under 16 is selected and difference between DOB  and 
	 * Patient's Declaration date in Part 2 is less than 16 years, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PatientUnder16(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_19";

		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		String supDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","SupDeclOptionSheet");
		String patDeclOptionSheet2= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOptionSheet2");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String retailCostType= values.get(4);
		String recordColName= values.get(5);
		String expSubmitMsg= values.get(6);
		String CRMModule= values.get(7);
		String CRMUSER= values.get(8);
		String strAdvFindNum= values.get(9);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
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
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
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

	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8046 - OPT_GOS4_PRT_BR_21 - To validate GOS4, 
	 * if eligibility option patient is under 16 is selected and Patient's age in Part 2 is equal to 16 years, 
	 * then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PatientAgeUnder16_Case2(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_21";

		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		String supDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","SupDeclOptionSheet");
		String patDeclOptionSheet2= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOptionSheet2");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String retailCostType= values.get(4);
		String recordColName= values.get(5);
		String expSubmitMsg= values.get(6);
		String CRMModule= values.get(7);
		String CRMUSER= values.get(8);
		String strAdvFindNum= values.get(9);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		

		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
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
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8047 - OPT_GOS4_PRT_BR_22 - To validate GOS4, if eligibility option  
	 * patient is 16, 17,18,  is selected and Patient's age in Part 2 is greater than 16 years 
	 * and less than 18 years, then user is allowed to submit the form.
	 * This case is validating the error on Approval Code provided as per screenshot attached in TFS.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_StudentAgeBelow18_ApprovalCodeIsRequired(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_22";

		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();

		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			ObjGOS4NHSEnglandApproval.clickOnSaveandNextButton();
			if(evidence)
			{
				ObjGOS4NHSEnglandApproval.snapApprovalCodeRequired(key+"_ApprovalCodeRequiredError");
			}
		}
		OPGOS4NHSEnglandApproval objGOS4NHSEnglandApproval= new OPGOS4NHSEnglandApproval(getDriver());
		List<String> ActualErrorMessagesonSave = objGOS4NHSEnglandApproval.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel(newTestDataFileName, "VALIDATIONERRORS", 1);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		
		softAssertion.assertAll();
		
	

	}
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8050 - OPT_GOS4_PRT_BR_25 - To validate GOS4, if eligibility option  
	 * patient is 16, 17,18,  is selected and difference between DOB  and Patient's Declaration date 
	 * in Part 2 is equal to 16 years, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_StudentAgeBelow18_SubmitClaim(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_GOS4_PRT_BR_25";

		
		List<String> values= ExcelUtilities.getScriptParameters("GOS4",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS4","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOption");
		String supDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS4","SupDeclOptionSheet");
		String patDeclOptionSheet2= ExcelUtilities.getExcelParameterByModule("GOS4","PatDeclOptionSheet2");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String voucherType= values.get(4);
		String retailCostType= values.get(5);
		String recordColName= values.get(6);
		String expSubmitMsg= values.get(7);
		String CRMModule= values.get(8);
		String CRMUSER= values.get(9);
		String strAdvFindNum= values.get(10);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String strAge= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "PATIENTDETAILS", "Age", scriptKey);
		int age= Integer.parseInt(strAge);
		
		String key = strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(newTestDataFileName,scriptKey,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(scriptKey,newTestDataFileName);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType(voucherType);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType(retailCostType);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails(scriptKey,recordColName,newTestDataFileName,supDeclOptionSheet);
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(scriptKey,newTestDataFileName,patDeclOptionSheet2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8051 - OPT_GOS4_PRT_BR_26 - Eligibility-DOB: To validate GOS4, 
	 * if eligibility option  patient is 16, 17,18,  is selected and difference between DOB  and 
	 * Patient's Declaration date in Part 2 is equal to 18 years, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_StudentAgeBelow18_SubmitClaim_WithComplexLense(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{

		String key = "OPT_GOS4_PRT_BR_27";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 1);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(1,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(6);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		
/*		OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1);
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
		
		//ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
		ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
		ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}*/
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8052 - OPT_GOS4_PRT_BR_27 - To validate GOS4, if eligibility option 
	 * patient receive Pension Credit guarantee is selected and difference between DOB of patient and 
	 * Patient's Declaration date in Part 2 is greater than 63 years, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PatientAgeAbove63(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_26";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 1);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(1,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(6);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8054 - OPT_GOS4_PRT_BR_29 - To validate GOS4, if eligibility 
	 * option patient receive Pension Credit guarantee is selected and difference between DOB of patient 
	 * and Patient's Declaration date in Part 2 is equal to 63 years, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PatientAge63WithComplexLense(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_29";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 4);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(4,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(6);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
		}
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8055 - OPT_GOS4_PRT_BR_31 - To validate GOS4, 
	 * if eligibility option patient's partner receive Pension Credit guarantee is selected partner's age 
	 * is greater than 63 years, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PatientAgeAbove63WithComplexLense(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_31";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 1);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(1,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(6);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8057 - OPT_GOS4_PRT_BR_32 - To validate GOS4, if eligibility option 
	 * patient's partner receive Pension Credit guarantee is selected Patient's Partner age is equal to 63 years, 
	 * then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PatientPartnerAge63WithComplexLense(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_32";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(7);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8066 - OPT_GOS4_PRT_BR_41 - To validate GOS4, if Distance 
	 * and Near prescription is passed, the voucher is calculated with the Distance and Reading Addition
	 *  (i.e. Reading addition= Distance + Near), then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_VoucherWithDistAndReading(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_41";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD6",5);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD6",5);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8067 - OPT_GOS4_PRT_BR_42 - To validate GOS4, 
	 if Sph and Cyl prescription both are positive, voucher is determined after transposing the prescription, 
	 then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_SphAndCylPrescPositive(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_42";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD7",6);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD7",6);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());
		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8068 - OPT_GOS4_PRT_BR_43 - To validate GOS4, 
	 * if Sph and Cyl prescription both are negative, voucher is determined after transposing the prescription, 
	 * then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_SphAndCylPrescNegative(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_43";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD8",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD8",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8069 - OPT_GOS4_PRT_BR_44 - To validate GOS4, 
	 * if Cyl power is more than twice the Sph power, voucher is determined after 
	 * transposing the prescription, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_CylPowerTwiceSphPower(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_44";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD9",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD9",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8071 - OPT_GOS4_PRT_BR_46 - To validate GOS4, if  
	 * Under Distance Prescription spherical power of not more than 6 diopters , 
	 * cylindrical power of not more than 2 diopters  and First voucher type is A is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistanceSphPowerNotMoreThan6Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_46";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD10",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD10",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8076 - OPT_GOS4_PRT_BR_51 - To validate GOS4, if 
	 * Under Reading and Distance Prescription Tag are passed as A  and voucher type is E 
	 * is passed in claim, then the claims accepted
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistanceVoucherTypeE(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_51";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD11",8);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD11",8);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8078 - OPT_GOS4_PRT_BR_53 - To validate GOS4, 
	 * if Under Distance Prescription spherical power of more than 6 diopters but less than 10 diopters, 
	 * cyl power of not more than 6 diopters  and First voucher type is B is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_sphPowerofMorethan6AndLessThan10(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_53";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD12",9);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD12",9);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8080 - OPT_GOS4_PRT_BR_55 - To validate GOS4, if  
	 * Under Reading Prescription spherical power of  more than 6 diopters but less than 10 diopters, 
	 * cyl power of not more than 6 diopters  and First voucher type is B is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CloneSanity","CLONE","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_cylPowerofNotMorethan6(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_55";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD13",10);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD13",10);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8082 - OPT_GOS4_PRT_BR_57 - To validate GOS4, 
	 * if Under Distance Prescription spherical power of less than 10 diopters, 
	 * cyl power of more than 2 diopters but not more than 6 diopters  and 
	 * First voucher type is B is passed in claim, then the claims accepted
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_sphPowerLessThan10AndCyPowermoreThan2Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_57";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD14",9);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD14",9);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8084 - OPT_GOS4_PRT_BR_59 - To validate GOS4, if Under Reading
	 *  Prescription spherical power of  less than 10 diopters, cyl power of  more than 2 diopters 
	 *  but not more than 6 diopters  and Second voucher type is B is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_sphPowerLessThan10AndCyPowernotmoreThan6Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_59";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD15",10);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD15",10);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8086 - OPT_GOS4_PRT_BR_61 - To validate GOS4, if Under Reading and Distance Prescription are passed as B  
	 * and voucher type is F is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingWithVoucherTypeB(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_61";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD16",12);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD16",12);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8088 - OPT_GOS4_PRT_BR_63 - To validate GOS4, 
	 * if reading addition less than 4, and Bifocal is selected, 
	 * Bifocal voucher is determined with Distance segment, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingWithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_63";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD17",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD17",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8089 - OPT_GOS4_PRT_BR_64 - To validate GOS4, 
	 * if reading addition more than 4 AND Reading power determine higher voucher and 
	 * Bifocal is selected, Bifocal voucher is determined with Reading segment, 
	 * then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ReadingAdditonMoreThan4WithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_64";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD18",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD18",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8092 - OPT_GOS4_PRT_BR_67 - To validate GOS4, 
	 * if Under Distance Prescription spherical power of 10 or more diopters but not more than 14 diopters , 
	 * cylindrical power of not more than 6 diopters  and 
	 * First voucher type is C is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistanceSphPowerMoreThan10Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_67";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD19",14);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD19",14);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8094 - OPT_GOS4_PRT_BR_69 - To validate GOS4, if Under Reading and Distance 
	 * Prescription are passed as C  and distance and near pair are selected, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingAndDistancePrescPassedAsC(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_69";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD20",8);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD20",8);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8095 - OPT_GOS4_PRT_BR_70 - To validate GOS4, 
	 * if Under Reading Prescription spherical power of 10 or more diopters but not more than 14 diopters , 
	 * cylindrical power of not more than 6 diopters  and second voucher type is C is passed in claim, 
	 * then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingSphPowerLessThan14Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_70";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD21",15);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD21",15);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8097 - OPT_GOS4_PRT_BR_72 - To validate GOS4, 
	 * if Under Reading and Distance Prescription are passed as C  and 
	 * voucher type is G is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingAndDescriptionPassedasC(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_72";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD18",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD18",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8099 - OPT_GOS4_PRT_BR_74 - To validate GOS4, 
	 * if Under Distance Prescription is passed as below 10 diopters , 
	 * Reading Prescription are passed as 10 diopters or more and Complex Voucher, 
	 * Bifocal glasses are passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescBelow10Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_74";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			//ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD22",16);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			//ObjOPGOS4SupplierDeclaration.selectVoucherType("C");
			//ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD22",16);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8101 - OPT_GOS4_PRT_BR_76 - To validate GOS4, 
	 * if  Under Reading  Prescription are passed as C , 
	 * Distance Prescription are passed as A/B  and Bifocal glasses are determined by Near is passed in claim, 
	 * then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescAsAorBWithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_76";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD22",16);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD22",16);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 8102 - OPT_GOS4_PRT_BR_77 - To validate GOS4, if Under Reading  
	 * Prescription are passed as A/B , Distance Prescription are passed as C  and 
	 * Bifocal glasses are determined by Distance is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescAsGWithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_77";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD23",17);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD23",17);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8103 - OPT_GOS4_PRT_BR_78 - To validate GOS4, 
	 * if Under Distance Prescription spherical power of more than 14 diopters , 
	 * cylindrical power of any diopters  and First voucher type D is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescSphPowerMoreThan14Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_78";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD24",18);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD24",18);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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

	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8105 - OPT_GOS4_PRT_BR_80 - To validate GOS4, 
	 * if Under Reading Prescription spherical power of more than 14 diopters, 
	 * cylindrical power of any diopters  and second voucher type D is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescSecondVoucherTypeD(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_80";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD25",18);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD25",18);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8107 - OPT_GOS4_PRT_BR_82 - To validate GOS4, if  
	 * Under Distance Prescription spherical power of any diopters, 
	 * cylindrical power of more than 6 diopters  and First voucher type D is passed in claim, 
	 * then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescCylPowerMoreThan6(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_82";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD26",5);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD26",5);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8109 - OPT_GOS4_PRT_BR_84 - To validate GOS4, 
	 * if Under Reading Prescription spherical power of any diopters, 
	 * cylindrical power of more than 6 diopters  and second voucher type is D is passed in claim, 
	 * then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistancePrescCylPowerMoreThan6WithVoucherTypeD(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_84";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD27",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD27",7);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8111 - OPT_GOS4_PRT_BR_86 - To validate GOS4, 
	 * if Under Reading and Distance Prescription are passed as D  and voucher type is H is passed in claim, 
	 * then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingAndDistanceWithVoucherTypeD(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_86";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD28",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD28",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8115 - OPT_GOS4_PRT_BR_90 - To validate GOS4, 
	 * if Under Reading  Prescription are passed as D , Distance Prescription are passed as A/B/C  and 
	 * Bifocal glasses are determined by Near is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingPrescWithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_90";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD28",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD28",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8116 - OPT_GOS4_PRT_BR_91 - To validate GOS4, if 
	 * Under Reading  Prescription are passed as D , Distance Prescription are passed as A/B/C  
	 * and Bifocal glasses are determined by Distance is passed in claim, then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderReadingPrescPassedAsD(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_91";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD29",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD29",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8117 - OPT_GOS4_PRT_BR_92 - To validate GOS4, 
	 * if Under Distance Prescription spherical power of not more than 6 diopters, 
	 * cyl power of not more than 2 diopters, First voucher type is A 
	 * (now value of voucher A is changed in reference data) is passed in claim, then the claims accepted
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistanceSphPowerMoreThan6Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_92";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD29",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD29",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8117 - OPT_GOS4_PRT_BR_92 - To validate GOS4, 
	 * if Under Distance Prescription spherical power of not more than 6 diopters, 
	 * cyl power of not more than 2 diopters, First voucher type is A 
	 * (now value of voucher A is changed in reference data) is passed in claim, then the claims accepted
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_UnderDistanceSphPowerEqualTo6Diopters(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_92";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD29",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("G");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD29",13);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8118 - OPT_GOS4_PRT_BR_93 - To validate GOS4, 
	 * if the Distance and Near prescription is passed and  bifocal glasses are passed 
	 * in Supplier declaration , paitient declaration near pair is passed then user form is rejected 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_DistanceAndNearPrescWithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_93";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8138 - OPT_GOS4_PRT_BR_113 - To validate GOS 4, 
	 * if repaired/replaced of Distance pair consist Right Lens, Front frame with Supplements 
	 * and the correct values for each are provided  then user is allowed to submit the form
	
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_DistancePairWithRightLensAndFrontFrame(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_113";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		//String key2 = "OPT_GOS4_PRT_BR_97";
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD31",20);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD31",20);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8113 - OPT_GOS4_PRT_BR_88 - To validate GOS4, 
	 * if Under Claim prism-controlled bifocal lenses  are passed and voucher type is H is passed in claim, 
	 * then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_PrismControlledBifocalLense(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_88";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD32",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD32",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8120 - OPT_GOS4_PRT_BR_95 - To validate GOS4, 
	 * if the Distance and Near prescription is passed and  Distance and Near pair of  
	 * glasses are passed in Supplier declaration and patient declartion bifocal is selected 
	 * then user is not  allowed to submit the form 
	
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_DistanceAndNearWithBifocal(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "OPT_GOS4_PRT_BR_95";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		//List<String> keys = Arrays.asList("OPT_GOS4_PRT_BR_95", "OPT_GOS4_PRT_BR_97");
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD32",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD32",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
	//	Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);
		ObjOPGOS4PatientDeclaration2.selectDeclarationOptions(1);
		ObjOPGOS4PatientDeclaration2.clickOnSaveButton();
		
		
		if(evidence)
		{
			//ObjOPGOS3PatientEligibility.GOS3ErrorsnapOnPatElgValidationError(key+"_ValidationErrors");
			ObjOPGOS4PatientDeclaration2.GOS4ErrorPatientDeclaration(key+"_ValidationErrors_PatientDeclaration");
			
		}
		
			
		List<String> ActualErrorMessagesonSave = ObjOPGOS4PatientDeclaration2.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "VALIDATIONERRORS", 2);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}			
		
	}
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8122 - OPT_GOS4_PRT_BR_97 - To validate GOS4, 
	 * if correct Tint/ Prism values are passed then the claims accepted.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_WithCorrectTintAndPrismValues(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_97";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD31",20);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD31",20);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8124 - OPT_GOS4_PRT_BR_99 - To validate GOS4, 
	 * if boxed center distance is equal to 55mm and small glasses supplement is "Y"  in claim, 
	 * then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_CentralDistance55WithSmallGlass(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_99";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD33",20);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD33",20);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8127 - OPT_GOS4_PRT_BR_102 - To validate GOS4, 
	 * if boxed center distance is over  55mm and small glasses supplement is "N"  in claim, 
	 * then user is allowed to submit the form.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ValidateCenterDistanceAbove55WithoutSmallGlassSuppliment(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_102";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD34",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("H");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD34",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		List<String> ActualErrorMessagesonSave = ObjOPGOS4SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "VALIDATIONERRORS", 3);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		
	}
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8127 - OPT_GOS4_PRT_BR_103 - To validate GOS4, 
	 * if supplements are passed in prescription tag but not passed under the Claim tag  in claim, 
	 * still the claim is accepted
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_SupplimentsInPrescTagButNotPassedUnderClaimTag(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_103";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD35",22);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD35",22);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8132 - OPT_GOS4_PRT_BR_107 - To validate GOS4, 
	 * if number of tint / prism(0/1/2)  lenses are passed in supplier section and 
	 * tint/prism is selected in prescription section in claim, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_LensesPassedInSupplierWithTintPrismSelected(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_107";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD33",22);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD33",22);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8130 - OPT_GOS4_PRT_BR_105 - To validate GOS4, 
	 * if small glasses and special facial characteristics are supplied  then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ValidationIfSmallGlassesAndSpecialFacialCharSupplied(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_105";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD36",23);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("D");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD36",23);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ErrorSupplierDeclaration(key+"_ValidationErrors");
		}
		List<String> ActualErrorMessagesonSave = ObjOPGOS4SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "VALIDATIONERRORS", 4);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		softAssertion.assertAll();
	}
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8133 - OPT_GOS4_PRT_BR_108 - To validate GOS4, 
	 * if Voucher type-Complex is passed along with all the supplements in claim, 
	 * then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ValidationOnSupplierDetails(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_108";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD37",19);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD37",19);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8123 - OPT_GOS4_PRT_BR_98 - To validate GOS4, 
	 * if incorrect Tint/ Prism values are passed in Prescription and Claim tag   in XML file, 
	 * then the claim is rejected.
	 * 24/01 : Based on defect raised through SIT this case will be passed & claim will get generated.
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ValidationIncorrectTintPrismValues(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_98";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD39",19);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD39",19);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
	/*	if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ErrorPrismSuppliement(key+"_ValidationErrors");
		}
		List<String> ActualErrorMessagesonSave = ObjOPGOS4SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "VALIDATIONERRORS", 5);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		softAssertion.assertAll();*/
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8121 - OPT_GOS4_PRT_BR_96 - To validate GOS4, 
	 * if the Distance prescription is passed and  Distance and Near pair of  glasses are passed in 
	 * Supplier declaration  then the claim is rejected
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ClaimRejectionIfDistancePrescriptionWithSupplierDeclarationAsDistanceAndNear(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_96";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD37",19);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD37",19);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		ObjOPGOS4PatientDeclaration2.selectDeclarationOptions(2);
		ObjOPGOS4PatientDeclaration2.submitAction();
	
			
		if(evidence)
		{
			ObjOPGOS4PatientDeclaration2.GOS4ErrorPatientDeclaration(key+"_ValidationError");
		}
		List<String> ActualErrorMessagesonSave = ObjOPGOS4SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "VALIDATIONERRORS", 7);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		softAssertion.assertAll();
	}
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8114 - To validate GOS4, if Under Claim prism-controlled bifocal lenses
	 *   are passed and voucher type is E is passed  then the claim is rejected
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_ClaimRejectionIfPrismControlledBifocalWithVoucherTypeE(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_89";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("E");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD39",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("E");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD39",21);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		if(evidence)
		{
			ObjOPGOS4SupplierDeclaration.GOS4ErrorPrismSuppliement(key+"_ValidationErrors");
		}
		List<String> ActualErrorMessagesonSave = ObjOPGOS4SupplierDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "VALIDATIONERRORS", 6);
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
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on patient eligibility are not appeared correctly");
		}	
		softAssertion.assertAll();
	}
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8139 - OPT_GOS4_PRT_BR_114 - To validate GOS 4, 
	 * if repaired/replaced of Near pair consist Left Lens, Whole frame with Supplements and 
	 * the correct values then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_NearPairWithLeftLenseWholeFrame(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_114";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD40",24);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD40",24);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8140 - OPT_GOS4_PRT_BR_115 - To validate GOS 4, 
	 * if repaired/replaced of Distance consist Both Lens, Side frame with Supplements and 
	 * the correct values for each are provided  then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_DistancePairWithBothLensesSideFrame(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_115";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("A");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD41",25);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("A");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD41",25);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8141 - OPT_GOS4_PRT_BR_116 - To validate GOS 4, 
	 * if repaired/replaced of Bifocal pair consist Right Lens, Front frame with Supplements 
	 * and the correct values for each are provided then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_BifocalPairWithRightLensesFrontFrameSuppliment(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_116";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("F");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD42",26);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("F");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD42",26);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8142 - OPT_GOS4_PRT_BR_117 - To validate GOS 4, 
	 * if repaired/replaced of Bifocal pair consist Both Lens, Whole frame with Supplements 
	 * and the correct values for each are provided in the XML, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_BifocalPairWithBothLensesWholeFrameSuppliment(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_117";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("F");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD43",27);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("F");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD43",27);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(4);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8143 - OPT_GOS4_PRT_BR_118 - To validate GOS 4, 
	 * if repaired/replaced of Distance pair consist Right Lens, Front frame with Supplements 
	 * and the correct values for each are provided in the XML, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_DistancePairWithRightLensesFrontFrameSuppliment(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_118";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD44",28);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD44",28);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8144 - OPT_GOS4_PRT_BR_119 - To validate GOS 4, 
	 * if repaired/replaced of Near pair consist Left Lens, Whole frame with Supplements 
	 * and the correct values for each are provided in the XML, then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_NearPairWithLeftLensesWholeFrameSuppliment(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_119";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD45",29);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD45",29);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(2);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Regression Test - 8145 - OPT_GOS4_PRT_BR_120 - To validate GOS 4, 
	 * if repaired/replaced of Distance pair consist Both Lens, Side frame with Supplements 
	 * and the correct values for each are provided  then user is allowed to submit the form
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_DistancePairWithBothLensesSideFrameSuppliment(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
		String key = "OPT_GOS4_PRT_BR_120";
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 2);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(2,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(2);
		//ObjOPGOS4PatientEligibility.enterVoucherValueReduction("13.66");
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD46",30);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("CONTACT LENSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD46",30);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(key+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(key);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 1);
			}
			else
			{
				Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
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
				String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "CLAIMID");
				System.out.println("Claim Number:"+ValueForFieldValue);
				String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPCLAIMSTATUS");
				String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPPAYMENTLINESTATUS");
				String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", key, "EXPGMPAMOUNTDUE");

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
								paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								String ActAmountDue = paymentLineDetailsList.get(1);
								utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, key, "ACTGMPAMOUNTDUE");
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
	
	/************************************************************************************
	 * Amit R. - GOS4 Sanity Test - 12589 - OPT_RBAC_10 - Validate if user having user role 
	 * "PCSEClaimsProcessor" with privilege "ManageAllGOSclaims" is logged in, 
	 * then user can access all screens of existing GOS 4 claim with status 
	 * @throws AWTException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS4Claim_Draft_RBAC(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_RBAC_10");
		String keyname = keys.get(0);
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 1);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		ObjGOS4PatientDetails.enterPatientDetails(1);
		ObjGOS4PatientDetails.clickOnSaveButton();
		String ClaimNo =ObjGOS4PatientDetails.getClaimNumber(keyname);
		quit(browser);
		
		
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();
		ObjGOS4PatientDetails = ObjOPSearchClaim.selectAndOpenClaim_GOS4(ClaimNo);
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(1,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(1);
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD1",1);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(keyname+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD1",1);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(keyname+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(keyname);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		
	
		quit(browser);
	}
	
	/************************************************************************************
	 * Akshay S. - Validate POS Payment is not done if "Receive POS Payment flag" is set "NO"
	 *  in CRM against claim type "GOS 3 / GOS 4"
	 * @throws ParseException
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPOSPaymentFlag(String browser,String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, AWTException, ParseException
	{
		List<String> keys = Arrays.asList("OPT_POS_18");
		String keyname = keys.get(0);
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 1);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValueList= Arrays.asList("fld");
		List<String>FieldValueList= Arrays.asList("Claim Form Name");
		List<String>ConditionValueList= Arrays.asList("Equals");
		List<String>ValueTypeList= Arrays.asList("Text");
		List<String>ValueForFieldValueList= Arrays.asList("GOS4");
		try{
			ObjAdvancedFilter.selectPrimaryEntityofCriteria("Ophthalmic Claim Types");
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
			ObjAdvancedFilter.clickResults();
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
			boolean flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					boolean isSelected= objAdvancedFindResult.updateReceivePOSPaymentFlag("Untick");
					System.out.println("POS Flag value is set to- "+isSelected);
					setAssertMessage("POS Flag value is set to- "+isSelected, 1);
				}
			}
			tearDown(browser);
			Thread.sleep(2000);
			setup(browser, environment, clientName,"OP");
			LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
			OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
			SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
			ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
			OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
			OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
			
			OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(1,environment);
			ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(1);
			OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
			if(age>16){
				OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
				OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
				ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
				ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
				if(evidence)
				{
					ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(keyname+"ClaimDetails");
				}
			}else{
				OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
				ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
				ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD4",4);
				if(evidence)
				{
					ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(keyname+"ClaimDetails");
				}
			}
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

			OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
			String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(keyname);
			Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

			if (clmSubFlag)
			{
				setAssertMessage("The Submit Claim Popup does appeared.", 2);
				String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
				if (msg.contains("Claim submitted"))
				{
					setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 3);
				}
				else
				{
					Assert.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
				}

			}
			else{
				Assert.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
			}

			ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
			tearDown(browser);
			Thread.sleep(2000);
			// To verify status of GOS4 claim submitted in CRM.
			setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int colNum = 1;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");
			String paymentDueDate="";
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			flag = ObjAdvancedFindResult.resultRecordFound();
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
						//String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
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
							utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
							setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1); 
							if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
							{
								System.out.println("The Payment Line status is Pending");
								setAssertMessage("The Payment Line status is Pending.", 4);
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
			ObjLoginScreen = new LoginScreen(getDriver());
			GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
			Date date= CommonFunctions.convertStringtoCalDate(paymentDueDate, "dd/MM/yyyy");//15/06/2018
			String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
			ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
			ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
			String paymentRunName= ObjGMPHome.getPaymentRunName();
			System.out.println("Payment run name is: "+paymentRunName);
			setAssertMessage("Payment run name is: "+paymentRunName, 5);
			VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
			objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
			boolean isHeader= objVarianceReports.verifyPaymentHeaderPresence();
			Verify.verifyTrue(isHeader, "Payment header is present");
			System.out.println("Payment header is present");
			setAssertMessage("Payment header is present", 6);
			VarianceReportDetail objVarianceReportDetail= objVarianceReports.clickOnContractorName(environment);
			boolean isPOSPaymtLine= objVarianceReportDetail.verifyPaymentLine("OPGOS4POS",keyname);
			Assert.assertEquals(isPOSPaymtLine, false, "GOS4 POS Payment Line is present");
			System.out.println("GOS4 POS Payment Line is not present");
			setAssertMessage("GOS4 POS Payment Line is not present", 7);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
/*			tearDown(browser);
			Thread.sleep(2000);*/
			setup(browser, environment, clientName,"CRMOP","SUPERUSER");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			GroupTypeValueList= Arrays.asList("fld");
			FieldValueList= Arrays.asList("Claim Form Name");
			ConditionValueList= Arrays.asList("Equals");
			ValueTypeList= Arrays.asList("Text");
			ValueForFieldValueList= Arrays.asList("GOS4");
			ObjAdvancedFilter.selectPrimaryEntityofCriteria("Ophthalmic Claim Types");
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
			ObjAdvancedFilter.clickResults();	
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
			boolean flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
				if(title!= null)
				{
					boolean isSelected= objAdvancedFindResult.updateReceivePOSPaymentFlag("Tick");
					System.out.println("POS Flag value is set to- "+isSelected);
					setAssertMessage("POS Flag value is set to- "+isSelected, 8);
				}
			}
		}
		

		
	}
	
	/************************************************************************************
	 * Akshay S. - 16736- OPT_POS_16- Validate if 'POS Payment Rate' with 'POS effective from' and 
	 * 'effective to date' is updated, then the Payment line generated displays the new POS Payment Rate
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPOSPaymentRate(String browser,String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, AWTException, ParseException
	{
		List<String> keys = Arrays.asList("OPT_POS_16");
		String keyname = keys.get(0);
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTDETAILS", "Age", 1);
		int age= Integer.parseInt(strAge);
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValueList= Arrays.asList("fld");
		List<String>FieldValueList= Arrays.asList("Claim Form Name");
		List<String>ConditionValueList= Arrays.asList("Equals");
		List<String>ValueTypeList= Arrays.asList("Text");
		List<String>ValueForFieldValueList= Arrays.asList("GOS4");
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Ophthalmic Claim Types");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				boolean isSelected= objAdvancedFindResult.updateReceivePOSPaymentFlag("Tick");
				System.out.println("POS Flag value is set to- "+isSelected);
				setAssertMessage("POS Flag value is set to- "+isSelected, 1);
			}
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValueList= Arrays.asList("fld");
		FieldValueList= Arrays.asList("Name");
		ConditionValueList= Arrays.asList("Contains");
		ValueTypeList= Arrays.asList("Text");
		ValueForFieldValueList= Arrays.asList("GOS4");
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Opthalmic Payment Rates");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				System.out.println("Entry is present for GOS4 POS payment");
				setAssertMessage("Entry is present for GOS4 POS payment", 2);
			}
		}
		Assert.assertEquals(flag, true,"Payment rate for GOS4 is not present");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS4PatientDetails ObjGOS4PatientDetails = ObjMakeAClaim.clickGOSFOURButton();
		
		OPGOS4PatientEligibility ObjOPGOS4PatientEligibility = ObjGOS4PatientDetails.PatientDetailsEntered(1,environment);
		ObjOPGOS4PatientEligibility.PatientEligibilityDetailsEntered(1);
		OPGOS4PatientDeclaration ObjOPGOS4PatientDeclaration = ObjOPGOS4PatientEligibility.clickOnSaveandNextButton();
		if(age>16){
			OPGOS4NHSEnglandApproval ObjGOS4NHSEnglandApproval = ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4NHSEnglandApproval.class);
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration = ObjGOS4NHSEnglandApproval.NHSEnglandApprovalDetailsEntered(1);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD1",1);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(keyname+"ClaimDetails");
			}
		}else{
			OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration =ObjOPGOS4PatientDeclaration.PatientDeclarationDetailsEntered(1,OPGOS4SupplierDeclaration.class);
			ObjOPGOS4SupplierDeclaration.selectVoucherType("B");
			ObjOPGOS4SupplierDeclaration.selectActualRetailCostType("GLASSES");
			ObjOPGOS4SupplierDeclaration.enterGOS4SupplierDetails("RECORD1",1);
			if(evidence)
			{
				ObjOPGOS4SupplierDeclaration.GOS4ClaimDetailssnaps(keyname+"ClaimDetails");
			}
		}
		OPGOS4SupplierDeclaration ObjOPGOS4SupplierDeclaration= new OPGOS4SupplierDeclaration(getDriver());

		OPGOS4PatientDeclaration2 ObjOPGOS4PatientDeclaration2 = ObjOPGOS4SupplierDeclaration.clickOnSaveandNextButton();
		String ClaimNo = ObjOPGOS4PatientDeclaration2.getClaimNumber(keyname);
		Boolean clmSubFlag = ObjOPGOS4PatientDeclaration2.PatientDeclarationDetailsEntered(1);

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS4PatientDeclaration2.getMsgTxtOnPopup();
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

		ObjOPHomePage = ObjOPGOS4PatientDeclaration2.clickCloseOnResultPopup();
		tearDown(browser);
		Thread.sleep(2000);
		// To verify status of GOS4 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");
		String paymentDueDate="";
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		flag = ObjAdvancedFindResult.resultRecordFound();
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
					//String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, key+"_ClaimStatus");
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
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
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
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName, "GMP");
		ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		Date date= CommonFunctions.convertStringtoCalDate(paymentDueDate, "dd/MM/yyyy");
		String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
		String paymentRunName= ObjGMPHome.getPaymentRunName();
		System.out.println("Payment run name is: "+paymentRunName);
		setAssertMessage("Payment run name is: "+paymentRunName, 4);
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		boolean isHeader= objVarianceReports.verifyPaymentHeaderPresence();
		Verify.verifyTrue(isHeader, "Payment header is present");
		System.out.println("Payment header is present");
		setAssertMessage("Payment header is present", 5);
		VarianceReportDetail objVarianceReportDetail= objVarianceReports.clickOnContractorName(environment);
		boolean isPOSPaymtLine= objVarianceReportDetail.verifyPaymentLine("OPGOS4POS",keyname);
		Assert.assertEquals(isPOSPaymtLine, true, "GOS4 POS Payment Line is missing");
		System.out.println("GOS4 POS Payment Line is present");
		setAssertMessage("GOS4 POS Payment Line is present", 6);
	}

}


