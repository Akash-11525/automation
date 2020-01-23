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
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.OP.*;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;
import pageobjects.TraineeOptometristResults;

@Listeners(ListenerClass.class)

public class OP_PRTClaimScripts extends BaseClass 
{

		
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12433- This is Regression Test Case - OPT_PRT_PTR_BR_02. Mandatory fields (all): To validate on PRT Portal,
	 *  if the mandatory data is present, respective claim is processed for payment
	 *  
	 *  12446 - OPT_PRT_PTR_BR_15 - PRT Validation: To validate that PRT claim is accepted, if claim is made by a registered
	 *   ophthalmic contractors 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CLONE","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validatePRTClaim(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		TraineeOptometristResults ObjTraineeOptometristResults= ObjAdvancedFilter.enterPrimaryEntityAndClickOnResult("Trainee Optometrists", TraineeOptometristResults.class);
		ObjTraineeOptometristResults.clickOnCreateTraineeOptometrist();
		String traineeName= ObjTraineeOptometristResults.createTraineeOptometrist(environment);
		System.out.println("Trainee name is: "+traineeName);
		tearDown(browser);
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_02", "OPT_PRT_PTR_BR_15");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(1, 365, environment,traineeName);
		OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPContractorDeclaration.PRTClaimDetailssnaps(key+"_PRT_ClaimDetails");
			}
		}
		
		String ClaimNo = objOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPContractorDeclaration.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
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
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
				//	String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
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
	
	//This script wil not work as different types of trainees are to be created
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12436- This is Regression Test Case - OPT_PRT_PTR_BR_05. PRT Payment: To validate that PRT claim 
	 * is accepted and there is no time-limit for a PRT claim to be submitted after completion of the training. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTClaimWithNoTimeLimit(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		TraineeOptometristResults ObjTraineeOptometristResults= ObjAdvancedFilter.enterPrimaryEntityAndClickOnResult("Trainee Optometrists", TraineeOptometristResults.class);
		ObjTraineeOptometristResults.clickOnCreateTraineeOptometrist();
		String traineeName= ObjTraineeOptometristResults.createTraineeOptometrist(environment);
		System.out.println("Trainee name is: "+traineeName);
		tearDown(browser);
		
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_05");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(2, 165, environment,traineeName);
		OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPContractorDeclaration.PRTClaimDetailssnaps(key+"_PRT_ClaimDetails");
			}
		}
		
		String ClaimNo = objOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPContractorDeclaration.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
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
				//	String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
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
	
	//This script wil not work as different types of trainees are to be created
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12437- This is Regression Test Case - OPT_PRT_PTR_BR_06. PRT payment: To validate that for 
	 * 6months claim - period is calculated by the difference between the training start date & the training end date and 
	 * respective claim is processed for payment 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTClaimWithSixMonthPeriod(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		TraineeOptometristResults ObjTraineeOptometristResults= ObjAdvancedFilter.enterPrimaryEntityAndClickOnResult("Trainee Optometrists", TraineeOptometristResults.class);
		ObjTraineeOptometristResults.clickOnCreateTraineeOptometrist();
		String traineeName= ObjTraineeOptometristResults.createTraineeOptometrist(environment);
		System.out.println("Trainee name is: "+traineeName);
		tearDown(browser);
		
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_06");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		//setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(3, 183, environment,traineeName);
		OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPContractorDeclaration.PRTClaimDetailssnaps(key+"_PRT_ClaimDetails");
			}
		}
		
		String ClaimNo = objOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPContractorDeclaration.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","PCSECLAIMPROCESSOR");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
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
				//	String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
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
	
	//This script wil not work as different types of trainees are to be created
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12439- This is Regression Test Case - OPT_PRT_PTR_BR_08. PRT Payment: To validate that user is 
	 * not navigated to next screen, if the training end date is before the start date
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTClaimEndDateBeforeStartDate(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		TraineeOptometristResults ObjTraineeOptometristResults= ObjAdvancedFilter.enterPrimaryEntityAndClickOnResult("Trainee Optometrists", TraineeOptometristResults.class);
		ObjTraineeOptometristResults.clickOnCreateTraineeOptometrist();
		String traineeName= ObjTraineeOptometristResults.createTraineeOptometrist(environment);
		System.out.println("Trainee name is: "+traineeName);
		tearDown(browser);
		
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_08");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		objOPTraineeDetails.enterTraineeDetails(4,-1,traineeName);
		OPSupervisorsDeclaration objOPSupervisorsDeclaration = objOPTraineeDetails.clickOnSaveandNextButton();
		//OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(1, -1);
		
		//OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPTraineeDetails.PRTTraineeDetailsErrorsSnap(key+"_PRT_TraineeDetailsError");
			}
		}
		
		List<String> ActualErrorMessagesonSave = objOPTraineeDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPPRTTESTDATA.xlsx", "VALIDATIONERRORS", 1);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		SoftAssert softAssertion = new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on PRT Trainee Details is not matching with Expected Error list.");
		}
		
		softAssertion.assertAll();
		
		
		
	}
	
	//This script wil not work as different types of trainees are to be created
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12442- This is Regression Test Case - OPT_PRT_PTR_BR_11. PRT payment: To validate that claim is 
	 * processed for payment if the Training Start date is matched to the reference data sent from the college of optometrists. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTClaimWithStartDateMatchingRefData(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		TraineeOptometristResults ObjTraineeOptometristResults= ObjAdvancedFilter.enterPrimaryEntityAndClickOnResult("Trainee Optometrists", TraineeOptometristResults.class);
		ObjTraineeOptometristResults.clickOnCreateTraineeOptometrist();
		String traineeName= ObjTraineeOptometristResults.createTraineeOptometrist(environment);
		System.out.println("Trainee name is: "+traineeName);
		tearDown(browser);
		
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_11");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(6, 183, environment,traineeName);
		OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPContractorDeclaration.PRTClaimDetailssnaps(key+"_PRT_ClaimDetails");
			}
		}
		
		String ClaimNo = objOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPContractorDeclaration.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
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
				//	String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
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
	
	//This script wil not work as different types of trainees are to be created
	
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12444- This is Regression Test Case - OPT_PRT_PTR_BR_13 -PRT payment: To validate that PRT claim is processed for payment, 
	 * if user is submitting two 6 month claims and the dates are concurrent. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTClaimWithconcurrentDates(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		TraineeOptometristResults ObjTraineeOptometristResults= ObjAdvancedFilter.enterPrimaryEntityAndClickOnResult("Trainee Optometrists", TraineeOptometristResults.class);
		ObjTraineeOptometristResults.clickOnCreateTraineeOptometrist();
		String traineeName= ObjTraineeOptometristResults.createTraineeOptometrist(environment);
		System.out.println("Trainee name is: "+traineeName);
		tearDown(browser);
		
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_13");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(7, 183, environment,traineeName);
		OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPContractorDeclaration.PRTClaimDetailssnaps(key+"_PRT_ClaimDetails_Claim1");
			}
		}
		
		String ClaimNo = objOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPContractorDeclaration.clickOnSubmitButton();
		
		SoftAssert softAssertion = new SoftAssert();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo+" gets submitted successfully.", 2);
			}
			else
			{
				softAssertion.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			softAssertion.assertTrue(clmSubFlag, "The Submit Claim Popup does not appeared.");
		}
		
		ObjOPHomePage = objOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
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
				//	String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
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
							softAssertion.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
						}					
					}
					else
					{
						System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
						softAssertion.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
					}
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}

				}
				else{
					softAssertion.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not Accepted for Payment.");
				}

			}

			else
			{
				softAssertion.assertNotNull(title, "Title extracted from result record is NULL.");
			}
		}
		else
		{

			softAssertion.assertEquals(flag, true, "No records found under results");

		}
		quit(browser);
		
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home("CETPRTCLAIMANT", environment);
		//OPHomePage ObjOPHomePage = ObjSelectOrganisation.selectOrganisation();
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		
		objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(7, 183, environment,traineeName);
		objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPContractorDeclaration.PRTClaimDetailssnaps(key+"_PRT_ClaimDetails_Claim1");
			}
		}
		
		String ClaimNo1 = objOPContractorDeclaration.getClaimNumber(keyname);
		Boolean clmSubFlag1 = objOPContractorDeclaration.clickOnSubmitButton();

		if (clmSubFlag1)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPContractorDeclaration.getMsgTxtOnPopup();
			if (msg.contains("Claim submitted"))
			{
				setAssertMessage("The claim " +ClaimNo1+" gets submitted successfully.", 2);
			}
			else
			{
				softAssertion.assertEquals(msg, "Claim submitted", "The claim does not get submitted successfully.");
			}

		}
		else{
			softAssertion.assertTrue(clmSubFlag1, "The Submit Claim Popup does not appeared.");
		}
		
		ObjOPHomePage = objOPContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		/*int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);*/
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		//claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		//ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		//ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
		if (flag2)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();
				if (clmStatus.contains(claimStatus))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
				//	String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence,key+"_ClaimStatus");
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ValueForFieldValue, evidence, keys,"_ClaimStatus");
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						//paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
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
							softAssertion.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
						}					
					}
					else
					{
						System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
						softAssertion.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
					}
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}

				}
				else{
					softAssertion.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not Accepted for Payment.");
				}

			}

			else
			{
				softAssertion.assertNotNull(title, "Title extracted from result record is NULL.");
			}
		}
		else
		{

			softAssertion.assertEquals(flag, true, "No records found under results");

		}
		
		softAssertion.assertAll();
		
	}
	
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12445- This is Regression Test Case - OPT_PRT_PTR_BR_14. PRT payment: To validate that PRT claim 
	 * is not processed for payment, if user is submitting two 6 month claims and the dates are not concurrent.
	 * 
	 * 12/04/2018 - Not on PRT form dates are non editable, its populated by system hence below case is not valid.
	 	*******************************************************************************************************************/
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTClaimWithNonConcurrentDates(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_PRT_PTR_BR_14");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation = ObjLoginScreen.logintoOP("PCSECLAIMPROCESSOR", environment);
		OPHomePage ObjOPHomePage = ObjSelectOrganisation.selectOrganisation();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		objOPTraineeDetails.enterTraineeDetails(9,-1);
		//objOPTraineeDetails.clickOnSaveButton();
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.clickOnSaveandNextButton();
		
		//OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPTraineeDetails.PRTTraineeDetailsErrorsSnap(key+"_PRT_TraineeDetailsError");
			}
		}
		
		List<String> ActualErrorMessagesonSave = objOPTraineeDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPPRTTESTDATA.xlsx", "VALIDATIONERRORS", 2);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		SoftAssert softAssertion = new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on PRT Trainee Details is not matching with Expected Error list.");
		}
		quit(browser);
		
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation = ObjLoginScreen.logintoOP("PCSECLAIMPROCESSOR", environment);
		ObjOPHomePage = ObjSelectOrganisation.selectOrganisation();
		ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		objOPTraineeDetails.enterTraineeDetails(10,-1);
		//objOPTraineeDetails.clickOnSaveButton();
		objOPSupervisorsDeclaration= objOPTraineeDetails.clickOnSaveandNextButton();
		
		//OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPTraineeDetails.PRTTraineeDetailsErrorsSnap(key+"_PRT_TraineeDetailsError");
			}
		}
		
		List<String> ActualErrorMessagesonSave2 = objOPTraineeDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave2 = ExcelUtilities.getCellValuesInExcel("OPPRTTESTDATA.xlsx", "VALIDATIONERRORS", 3);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList2 = CommonFunctions.compareStrings(ActualErrorMessagesonSave2, ExpectedErrorMessagesonSave2);
		//SoftAssert softAssertion = new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on PRT Trainee Details is not matching with Expected Error list.");
		}
		
		
		
		softAssertion.assertAll();
		
		
		
	}*/
	
	//This script wil not work as different types of trainees are to be created
	/*	***********************************************************************************************************
	 * Akshay S : - 18580- This is Regression Test Case - OPT_CR17_PRT_03. Validate PRT claim is not processed for 
	 * payment, if both Training Start date and Training End date are missing
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validatePRTMessages(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_CR17_PRT_03");
		String keyname = keys.get(0);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPTraineeDetails objOPTraineeDetails= ObjMakeAClaim.clickGOSPRTButton();
		OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.clickOnSaveandNextButton();
		if(evidence)
		{
			for (String key:keys)
			{
				objOPTraineeDetails.PRTTraineeDetailsErrorsSnap(key+"_PRT_ClaimDetails");
			}
		}
		List<String> ActualErrorMessagesonSave = objOPTraineeDetails.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPPRTTESTDATA.xlsx", "VALIDATIONERRORS", 4);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		SoftAssert softAssertion = new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 3);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on PRT Trainee Details is not matching with Expected Error list.");
		}
		softAssertion.assertAll();
	}
	
}	
