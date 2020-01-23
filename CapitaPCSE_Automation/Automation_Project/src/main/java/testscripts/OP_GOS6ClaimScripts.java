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
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.PaymentInstructionFile;
import pageobjects.GMP.VarianceReports;
import pageobjects.OP.OPMakeAClaim;
import pageobjects.OP.MyNotification;
import pageobjects.OP.OPContractorDeclaration;
import pageobjects.OP.OPCreateGOS6PVN;
import pageobjects.OP.OPGOS4NHSEnglandApproval;
import pageobjects.OP.OPGOS4PatientDeclaration;
import pageobjects.OP.OPGOS4PatientDeclaration2;
import pageobjects.OP.OPGOS4PatientDetails;
import pageobjects.OP.OPGOS4PatientEligibility;
import pageobjects.OP.OPGOS4SupplierDeclaration;
import pageobjects.OP.OPGOS5PatientDeclaration;
import pageobjects.OP.OPGOS5PerformerDeclaration;
import pageobjects.OP.OPGOS6ContractorDeclaration;
import pageobjects.OP.OPGOS6Options;
import pageobjects.OP.OPGOS6PVNView;
import pageobjects.OP.OPGOS6PatientDeclaration;
import pageobjects.OP.OPGOS6PatientDetails;
import pageobjects.OP.OPGOS6PatientEligibility;
import pageobjects.OP.OPGOS6PerformerDeclaration;
import pageobjects.OP.OPHomePage;
import pageobjects.OP.OPPatientDeclaration;
import pageobjects.OP.OPPatientDetails;
import pageobjects.OP.OPPatientEligibility;
import pageobjects.OP.OPPerformerDeclaration;
import pageobjects.OP.OPSearchClaim;
import pageobjects.OP.OPSearchForClaim;
import pageobjects.OP.OPSearchGOS6PVN;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class OP_GOS6ClaimScripts extends BaseClass
{
	/************************************************************************************
	 * Amit R. - GOS6 Regression Test - 12395 - OPT_GOS6_PRT_BR_39 - To validate that GOS 6 claim is processed for payment for 1st patient at the address
	 * 17213 - OPT_GOS6 PVN_PRT_BR_02 - To validate Create GOS6 From GOS6 PVN
	 * @throws ParseException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","CLONE","CloneSanity","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS6Claim_SuccessfulFlow(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_GOS6 PVN_PRT_BR_02","OPT_GOS6_PRT_BR_39");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5,environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(1);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("Your PVN has been submitted"))
			{
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Your PVN has been submitted", "The GOS6 pvn reference number does not get submitted successfully.");
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
		OPGOS6PatientDeclaration ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientEligibility.PatientEligibilityDetailsEntered(1);
		OPGOS6PerformerDeclaration ObjOPGOS6PerformerDeclaration = ObjOPGOS6PatientDeclaration.PatientDeclarationDetailsEntered(1);
		ObjOPGOS6PerformerDeclaration.selectProvidedOptions(1);
		ObjOPGOS6PerformerDeclaration.selectFirstVoucherType("A");
		ObjOPGOS6PerformerDeclaration.selectSecondVoucherType("B");
		ObjOPGOS6PerformerDeclaration.enterAddressManually(1);
		ObjOPGOS6PerformerDeclaration.enterSignatoryDetails();
		OPGOS6ContractorDeclaration ObjOPGOS6ContractorDeclaration = ObjOPGOS6PerformerDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS6ContractorDeclaration.GOS6ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		String ClaimNo = ObjOPGOS6ContractorDeclaration.getClaimNumber(keyname);

		ObjOPGOS6ContractorDeclaration.enterSignatoryDetails();
		Boolean clmSubFlag = ObjOPGOS6ContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS6ContractorDeclaration.getMsgTxtOnPopup();
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
		ObjOPHomePage = ObjOPGOS6ContractorDeclaration.clickCloseOnResultPopup();


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
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
	 * Amit R. - GOS6 Regression Test - 12396 - OPT_GOS6_PRT_BR_40 - To validate that GOS 6 claim is processed for payment for 2nd patient at the address
	 * @throws ParseException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS6Claim_SuccessfulFlow_PaymentForSecondPatient(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_GOS6_PRT_BR_40");
		String PaymentLineDueDate = null;
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5,environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(2);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("Your PVN has been submitted"))
			{
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Your PVN has been submitted", "The GOS6 pvn reference number does not get submitted successfully.");
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
		OPGOS6PatientEligibility ObjOPGOS6PatientEligibility = ObjOPGOS6PatientDetails.PatientDetailsEntered(2,environment);
		OPGOS6PatientDeclaration ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientEligibility.PatientEligibilityDetailsEntered(2);
		OPGOS6PerformerDeclaration ObjOPGOS6PerformerDeclaration = ObjOPGOS6PatientDeclaration.PatientDeclarationDetailsEntered(1);
		ObjOPGOS6PerformerDeclaration.selectProvidedOptions(2);
		//ObjOPGOS6PerformerDeclaration.selectFirstVoucherType("A");
		//ObjOPGOS6PerformerDeclaration.selectSecondVoucherType("B");
		ObjOPGOS6PerformerDeclaration.enterAddressManually(1);
		ObjOPGOS6PerformerDeclaration.enterSignatoryDetails();
		OPGOS6ContractorDeclaration ObjOPGOS6ContractorDeclaration = ObjOPGOS6PerformerDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS6ContractorDeclaration.GOS6ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		String ClaimNo = ObjOPGOS6ContractorDeclaration.getClaimNumber(keyname);

		ObjOPGOS6ContractorDeclaration.enterSignatoryDetails();
		Boolean clmSubFlag = ObjOPGOS6ContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS6ContractorDeclaration.getMsgTxtOnPopup();
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
		ObjOPHomePage = ObjOPGOS6ContractorDeclaration.clickCloseOnResultPopup();


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
						setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1); 
						PaymentLineDueDate = paymentLineDetailsList.get(2);
						System.out.println(PaymentLineDueDate);
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
		/*quit(browser);

		setup(browser, environment, clientName, "GMP");
		String PaymentLineDueDate="4/13/2018";
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


	/************************************************************************************
	 * Amit R. - GOS6 Regression Test - 12397 - OPT_GOS6_PRT_BR_41 - To validate that GOS 6 claim is processed for payment for 3rd patient at the address
	 * @throws ParseException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS6Claim_SuccessfulFlow_PaymentForThirdPatient(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_GOS6_PRT_BR_41");
		String PaymentLineDueDate = null;
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5,environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(2);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("Your PVN has been submitted"))
			{
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Your PVN has been submitted", "The GOS6 pvn reference number does not get submitted successfully.");
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
		OPGOS6PatientEligibility ObjOPGOS6PatientEligibility = ObjOPGOS6PatientDetails.PatientDetailsEntered(2,environment);
		OPGOS6PatientDeclaration ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientEligibility.PatientEligibilityDetailsEntered(2);
		OPGOS6PerformerDeclaration ObjOPGOS6PerformerDeclaration = ObjOPGOS6PatientDeclaration.PatientDeclarationDetailsEntered(1);
		ObjOPGOS6PerformerDeclaration.selectProvidedOptions(3);
		//ObjOPGOS6PerformerDeclaration.selectFirstVoucherType("A");
		//ObjOPGOS6PerformerDeclaration.selectSecondVoucherType("B");
		ObjOPGOS6PerformerDeclaration.enterAddressManually(1);
		ObjOPGOS6PerformerDeclaration.enterSignatoryDetails();
		OPGOS6ContractorDeclaration ObjOPGOS6ContractorDeclaration = ObjOPGOS6PerformerDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS6ContractorDeclaration.GOS6ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		String ClaimNo = ObjOPGOS6ContractorDeclaration.getClaimNumber(keyname);
		ObjOPGOS6ContractorDeclaration.enterSignatoryDetails();

		Boolean clmSubFlag = ObjOPGOS6ContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS6ContractorDeclaration.getMsgTxtOnPopup();
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
		ObjOPHomePage = ObjOPGOS6ContractorDeclaration.clickCloseOnResultPopup();


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
						setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1); 
						PaymentLineDueDate = paymentLineDetailsList.get(2);
						System.out.println(PaymentLineDueDate);
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
		/*quit(browser);

		setup(browser, environment, clientName, "GMP");
		//String PaymentLineDueDate="4/13/2018";
		//setup(browser, environment, clientName,"GMP","PCSEPAYMENTSPROCESSOR");	
		ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		//GMPHome ObjGMPHome = new GMPHome(getDriver());
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(PaymentLineDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch();
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload();*/

	}


	/************************************************************************************
	 * Amit R. - GOS6 Regression Test - 12365 - OPT_GOS6_PRT_BR_9 - To validate sight test interval for a patient 16 years and over and under 70 years where patient declaration date  2years 1day from last sigh test date 
	 * @throws ParseException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS6Claim_SuccessfulFlow_PatientBelow70YearsLastSightDateBefore2Years1day(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_GOS6_PRT_BR_9");
		String PaymentLineDueDate = null;
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5,environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetailsWithLastSightDate(2,731);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("Your PVN has been submitted"))
			{
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Your PVN has been submitted", "The GOS6 pvn reference number does not get submitted successfully.");
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
		OPGOS6PatientDeclaration ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientEligibility.PatientEligibilityDetailsEntered(3);
		OPGOS6PerformerDeclaration ObjOPGOS6PerformerDeclaration = ObjOPGOS6PatientDeclaration.PatientDeclarationDetailsEntered(1);
		ObjOPGOS6PerformerDeclaration.selectProvidedOptions(3);
		//ObjOPGOS6PerformerDeclaration.selectFirstVoucherType("A");
		//ObjOPGOS6PerformerDeclaration.selectSecondVoucherType("B");
		ObjOPGOS6PerformerDeclaration.enterAddressManually(1);
		ObjOPGOS6PerformerDeclaration.enterSignatoryDetails();
		OPGOS6ContractorDeclaration ObjOPGOS6ContractorDeclaration = ObjOPGOS6PerformerDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPGOS6ContractorDeclaration.GOS6ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		String ClaimNo = ObjOPGOS6ContractorDeclaration.getClaimNumber(keyname);
		ObjOPGOS6ContractorDeclaration.enterSignatoryDetails();

		Boolean clmSubFlag = ObjOPGOS6ContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = ObjOPGOS6ContractorDeclaration.getMsgTxtOnPopup();
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
		ObjOPHomePage = ObjOPGOS6ContractorDeclaration.clickCloseOnResultPopup();


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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
						utilities.ExcelUtilities.setKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", ActAmountDue, keyname, "ACTGMPAMOUNTDUE");
						setAssertMessage("The Payment Amount Due is: "+ActAmountDue, 1); 
						PaymentLineDueDate = paymentLineDetailsList.get(2);
						System.out.println(PaymentLineDueDate);
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
		quit(browser);

		/*setup(browser, environment, clientName, "GMP");
		//String PaymentLineDueDate="4/13/2018";
		//setup(browser, environment, clientName,"GMP","PCSEPAYMENTSPROCESSOR");	
		ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		//GMPHome ObjGMPHome = new GMPHome(getDriver());
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(PaymentLineDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch();
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload();*/

	}

	/************************************************************************************
	 * Amit R. - GOS6 Regression Test - 17222 - OPT_GOS6_PRT_BR_11 - To validate Area Team is notified at exact 3 weeks for three or more patients
	 * @throws ParseException 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void GOS6Claim_ThreePatients(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_GOS6_PRT_BR_11");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(7,environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(1);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(4);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(5);
		if(evidence)
		{
			for (String key:keys)
			{
				ObjOPCreateGOS6PVN.GOS6CreatePVNsnaps(key+"_GOS6PVNDetails");
			}
		}

		Boolean pvnMsgFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnMsgFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnMsgPopup();
			if (msg.contains("At least three weeks in advance before a domiciliary visit to three or more patients at the same address."))
			{
				setAssertMessage("We have received expected message: "+msg, 2);
				System.out.println("We have received expected message: "+msg);
				ObjOPCreateGOS6PVN.clickCloseOnMessageResultPopup();
			}
			else
			{
				Assert.assertEquals(msg, "At least three weeks in advance before a domiciliary visit to three or more patients at the same address.", "Actual and Expected messages are not matching.");
			}
		}
		else{
			Assert.assertTrue(pvnMsgFlag, "The GOS6 PVN Message Popup does not appeared.");
		}

		ObjOPCreateGOS6PVN = ObjOPCreateGOS6PVN.enterDateOfVisit(27);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("Your PVN has been submitted"))
			{
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Your PVN has been submitted", "The GOS6 pvn reference number does not get submitted successfully.");
			}
		}
		else{
			Assert.assertTrue(pvnSubFlag, "The GOS6 PVN Claim Popup does not appeared.");
		}

		ObjOPHomePage = ObjOPCreateGOS6PVN.clickCloseOnResultPopup();

		quit(browser);


		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		//setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;

		String primaryEntity = "GOS6 Pre Visit Notifications";
		List<String> GroupTypeValueList = Arrays.asList("fld");
		List<String> FieldValueList = Arrays.asList("GOS6 Pre Visit Notification");
		List<String> ConditionValueList = Arrays.asList("Equals");
		List<String> ValueTypeList = Arrays.asList("Lookup");
		List<String> ValueForFieldValueList = Arrays.asList(refNo);
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity, GroupTypeValueList,FieldValueList,ConditionValueList, ValueTypeList, ValueForFieldValueList);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String pvnStatus = ObjAdvancedFindResult.getDetailsFromGOS6PVNResultRecordScreen(evidence, keys, "_PVNStatus");
				if (pvnStatus.contains("Accepted"))
				{
					setAssertMessage("The pvn status in CRM is Accepted.", 1);
					System.out.println("The pvn status in CRM is Accepted.");					
					

				}
				else{
					Assert.assertEquals(pvnStatus, "Accepted", "The pvn status in CRM is not Draft.");
				}

			}

			else
			{
				Assert.assertNotNull(title, "Title extracted from result record is NULL.");
			}
		}
		ObjCrmHome = ObjAdvancedFindResult .closeAdvanceFindWindow();


	}
	
	/************************************************************************************
	 * Amit R. - GOS6 Regression Test - 16654 - OPT_Note_GOS6_PTR_77 - To verify, for GOS 6, 
	 * Performer are notified if the "Revert to Draft" button is clicked by contractor.
	 ************************************************************************************/
	//Notification script commented by Akshay on 5th July 2018 as it is not in scope.
	
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void revertToDraftGOS6Claim(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_Note_GOS6_PTR_77");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("GOSCLAIMANT", environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(1);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("PVN submitted"))
			{
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
		OPGOS6PatientDeclaration ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientEligibility.PatientEligibilityDetailsEntered(1);
		ObjOPGOS6PatientDeclaration = ObjOPGOS6PatientDeclaration.enterDetailsAndClickOnAwaitingPerformer(1);
		String ClaimNo = ObjOPGOS6PatientDeclaration.getClaimNumber(keyname);
		ObjOPHomePage= ObjOPHomePage.ClickonTab("HOME", OPHomePage.class);
		MyNotification objMyNotification= ObjOPHomePage.ClickOnPageHeader("Messages", MyNotification.class);
		ObjOPGOS6PatientDeclaration= objMyNotification.clickOnReviewLink(ClaimNo,OPGOS6PatientDeclaration.class,keyname+"_PerformerAwaitingApproval_"+ClaimNo,"is awaiting Performer approval.");
		if(evidence){
			ObjOPGOS6PatientDeclaration.GOS1PatientDeclarionSnap(keyname+"_after clicking on Performer review");
		}
		ObjOPGOS6PatientDeclaration.clickOnSaveandNextButton();
		setAssertMessage("Performer awaiting approval notification has been displayed for claim: "+ClaimNo, 1);
		System.out.println("Performer awaiting approval notification has been displayed for claim: "+ClaimNo);
		OPGOS6PerformerDeclaration objOPGOS6PerformerDeclaration= new OPGOS6PerformerDeclaration(getDriver());
		objOPGOS6PerformerDeclaration.selectProvidedOptions(5);
		objOPGOS6PerformerDeclaration.selectFirstVoucherType("A");
		objOPGOS6PerformerDeclaration.selectSecondVoucherType("B");
		objOPGOS6PerformerDeclaration.enterAddressManually(1);
		objOPGOS6PerformerDeclaration.enterSignatoryDetails();
		objOPGOS6PerformerDeclaration.clickOnSaveAwaitingContrator();
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		//SelectOrganisation objSelectOrganisation = ObjLoginScreen.logintoOP("PCSECLAIMPROCESSOR", environment);
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim objOPMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjOPHomePage= objSelectOrganisation.selectOrganisation("BOOTS OPTICIANS (LONDONDERRY)", OPHomePage.class);
		objMyNotification= ObjOPHomePage.ClickOnPageHeader("Messages", MyNotification.class);
		objOPGOS6PerformerDeclaration= objMyNotification.clickOnReviewLink(ClaimNo,OPGOS6PerformerDeclaration.class,keyname+"_ContractorSignatoryApproval_"+ClaimNo,"is awaiting Contractor Signatory approval.");
		if(evidence){
			objOPGOS6PerformerDeclaration.GOS6PerformerDeclarionSnap(keyname+"_after clicking on Contractor review");
		}
		objOPGOS6PerformerDeclaration.clickOnSaveandNextButton();
		setAssertMessage("Contractor awaiting approval notification has been displayed for claim: "+ClaimNo, 2);
		System.out.println("Contractor awaiting approval notification has been displayed for claim: "+ClaimNo);
		OPGOS6ContractorDeclaration objOPGOS6ContractorDeclaration= new OPGOS6ContractorDeclaration(getDriver());
		if(evidence)
		{
			for (String key:keys)
			{
				objOPGOS6ContractorDeclaration.GOS6ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		ClaimNo = objOPGOS6ContractorDeclaration.getClaimNumber(keyname);

		objOPGOS6ContractorDeclaration.enterSignatoryDetails();
		ObjOPHomePage = objOPGOS6ContractorDeclaration.clickOnRevertToDraftButton();
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home("GOSCLAIMANT", environment);
		objMyNotification= ObjOPHomePage.ClickOnPageHeader("Messages", MyNotification.class);
		boolean isReverted= objMyNotification.verifyNotificationText(ClaimNo,"has been reverted to draft.",keyname+"_Claim reverted to draft_"+ClaimNo);
		if(evidence){
			objMyNotification.captureNotificationSnap(keyname+"_claim reverted to Draft_"+ClaimNo);
		}
		Assert.assertEquals(isReverted, true);
		setAssertMessage("Claim is reverted to draft for claim: "+ClaimNo, 3);
		System.out.println("Claim is reverted to draft for claim: "+ClaimNo);
		tearDown(browser);
		Thread.sleep(3000);
		
		// To verify status of GOS6 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValue= Arrays.asList("fld");
		List<String>FieldValue= Arrays.asList("Regarding");
		List<String>ConditionValue= Arrays.asList("Equals");
		List<String>ValueType= Arrays.asList("Lookup");
		String ClaimNo_Excel= ExcelUtilities.getKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", "OPT_Note_GOS6_PTR_77", "CLAIMID");
		List<String>ValueForFieldValue= Arrays.asList(ClaimNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Email Messages");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());

		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{	int records= objAdvancedFindResult.getRecordCount();
			for(int i=0;i<records;i++){
				String title = objAdvancedFindResult.clickOnLinkFromFirstRecordWithoutSorting(i,1);
				if(title!= null)
				{
					if (evidence)
					{
						objAdvancedFindResult.takeScreenshot("Email:",keyname+"_Notification email_"+i);
					}
				}
				objAdvancedFindResult.closeWindow();
				WindowHandleSupport.getRequiredWindowDriver(getDriver(),"Advanced Find");
			}
			setAssertMessage("All notifications are verified for claim no: "+ClaimNo, 4);
			System.out.println("All notifications are verified for claim no: "+ClaimNo);
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
	}*/
	
	/************************************************************************************
	 * Akshay S. - GOS6 Regression Test - 17244 - OPT_GOS6 PVN_PRT_BR_33 - To validate when 
	 * valid Reason is entered if Notification Date is less than 48 hrs than visit date. 
	 ************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateReasonForLessThan48Hrs(String browser,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key = "OPT_GOS4_PRT_BR_48";
		List<String> keys = Arrays.asList("OPT_GOS6 PVN_PRT_BR_33");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("GOSCLAIMANT", environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		//OPPatientDetails ObjPatientDetails = ObjOPHomePage.selectPerformer();
		OPGOS6Options ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPCreateGOS6PVN ObjOPCreateGOS6PVN = ObjOPGOS6Options.clickOnCreateGOS6PVN();
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(5,environment);
		String refNo = ObjOPCreateGOS6PVN.getPVNReferenceNumber();
		System.out.println(refNo);
		ObjOPCreateGOS6PVN=ObjOPCreateGOS6PVN.enterPersonalDetails(1);
		Boolean pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getMsgTxtOnPopup();
			if (msg.contains("Your PVN has been submitted"))
			{
				setAssertMessage("The GOS6 pvn reference number " +refNo+" gets submitted successfully.", 2);
			}
			else
			{
				Assert.assertEquals(msg, "Your PVN has been submitted", "The GOS6 pvn reference number does not get submitted successfully.");
			}
		}
		else{
			Assert.assertTrue(pvnSubFlag, "The GOS6 PVN Claim Popup does not appeared.");
		}
		ObjOPHomePage= ObjOPHomePage.ClickonTab("HOME", OPHomePage.class);
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		ObjOPGOS6Options = ObjMakeAClaim.clickGOSSIXButton();
		OPSearchGOS6PVN ObjOPSearchGOS6PVN = ObjOPGOS6Options.clickOnSearchGOS6PVN();
		ObjOPCreateGOS6PVN = ObjOPSearchGOS6PVN.searchGOS6VPNAndClickOnAmendBtn(refNo, environment);
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.enterPVNDetails(1,environment);
		ObjOPCreateGOS6PVN.clickOnSubmit();
		if(evidence){
			ObjOPCreateGOS6PVN.GOS6CreatePVNsnaps(keyname+"_Validation message on Submit_"+refNo);
		}
		ObjOPCreateGOS6PVN= ObjOPCreateGOS6PVN.TickOnOthersAndEnterReason();
		if(evidence){
			ObjOPCreateGOS6PVN.GOS6CreatePVNsnaps(keyname+"_Date updated_"+refNo);
		}
		pvnSubFlag = ObjOPCreateGOS6PVN.clickOnSubmit();
		if (pvnSubFlag)
		{
			String msg = ObjOPCreateGOS6PVN.getPeriodMsgTxtOnPopup();
			String expMessage= "The notice period has not been met: at least 48 hours notice is required where mobile services are to be provided to 2 or less patients at the same address";
			if (msg.contains(expMessage))
			{
				setAssertMessage("The GOS6 pvn reference message is validated successfully for "+refNo, 2);
			}
			else
			{
				Assert.assertEquals(msg, expMessage, "The GOS6 pvn reference number message is not matching.");
			}
		}
		else{
			Assert.assertTrue(pvnSubFlag, "The GOS6 PVN Claim Popup does not appeared.");
		}
		
	}

	

}


