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
import org.testng.collections.Lists;

import com.mysql.fabric.xmlrpc.base.Array;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.OP.*;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)

public class OP_CETClaimScripts extends BaseClass 
{
	/*	***********************************************************************************************************
	 * Amit Rasal : - 16710 - This is Regression Test Case - OPT_CET_PRT_VR_02. To validate CET portal claim is accepted
	 *  and processed for payment, if all valid details are present
	 *  
	 *  16707 -OPT_CET_PTR_BR_08- To validate updated grant amount is considered for payment, if grant amount is modified
	 *   by Department of Health for respective financial year
	 *   
	
	
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateCETClaim(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_CET_PRT_VR_02", "OPT_CET_PTR_BR_08");
		String keyname = keys.get(0);
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
	
		String primaryEntity = "Ophthalmic Claims";
		List<String> GroupTypeValueList = Arrays.asList("fld","fld","fld");
		List<String> FieldValueList = Arrays.asList("Claim Type","Contractor Number","Claim Status");
		List<String> ConditionValueList = Arrays.asList("Equals","Equals","Equals");
		List<String> ValueTypeList = Arrays.asList("Lookup", "Text","Lookup");
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "OPContractorCode");
		List<String> ValueForFieldValueList = Arrays.asList("CET", contractorCode, "Accepted For Payment");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity, GroupTypeValueList,FieldValueList,ConditionValueList, ValueTypeList, ValueForFieldValueList);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
			System.out.println("Claim Entries have been deleted for Contractor ");
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No Claim record avaialble for Contractor");

		}
		ObjCrmHome = ObjAdvancedFindResult .closeAdvanceFindWindow();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		//int colNum = 1;
		
		String primaryEntity1 = "Opthalmic Payment Rates";
		List<String> GroupTypeValueList1 = Arrays.asList("fld");
		List<String> FieldValueList1 = Arrays.asList("Name");
		List<String> ConditionValueList1 = Arrays.asList("Equals");
		List<String> ValueTypeList1 = Arrays.asList("Text");
		List<String> ValueForFieldValueList1 = Arrays.asList("CET");
		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity1, GroupTypeValueList1,FieldValueList1,ConditionValueList1, ValueTypeList1, ValueForFieldValueList1);
		
		boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
		if (flag1)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			String rate = ObjAdvancedFindResult.getDetailsFromOpthClaimTypeResultRecordScreen(keyname);
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No record found for payment rates");

		}
		
		quit(browser);
		
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		//SelectOrganisation ObjSelectOrganisation = ObjLoginScreen.logintoOP("CETPRTCLAIMANT", environment);
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("PCSECLAIMPROCESSOR", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		//OPHomePage ObjOPHomePage = ObjSelectOrganisation.selectOrganisation_dropdown();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPCETPerformerSignatory objOPCETPerformerSignatory= ObjMakeAClaim.clickGOSCETButton();
		
		//OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(1, 365);
		//OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETPerformerSignatory.CETPerformerSignatorySnaps(key+"_CET_PerformerSignatoryDetails");
			}
		}
		
		OPCETContractorSignatory objOPCETContractorSignatory = objOPCETPerformerSignatory.clickOnSaveAndNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETContractorSignatory.CETContractorSignatorySnaps(key+"_CET_ContractorSignatoryDetails");
			}
		}
		
		String ClaimNo = objOPCETContractorSignatory.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPCETContractorSignatory.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPCETContractorSignatory.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPCETContractorSignatory.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		colNum = 1;
		primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
						}	
						String PayAmountDue = paymentLineDetailsList.get(1);
						System.out.println(PayAmountDue);
						if (PayAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
						{
							System.out.println("The Payment Line amount due is matching, Expected: "+ExpectedgmpAmountDue+",Actual: "+PayAmountDue);
							setAssertMessage("The Payment Line amount due is matching, Expected: "+ExpectedgmpAmountDue+",Actual: "+PayAmountDue, 1);
						}
						else
						{
							System.out.println("The Payment Line amount due is not matching, Expected: "+ExpectedgmpAmountDue+",Actual: "+PayAmountDue);
							//setAssertMessage("The Payment Line status is not Pending.", 1);
							Assert.assertEquals(PayAmountDue, ExpectedgmpAmountDue, "The Payment Line due amount is not matching with Expected amount. Expected Amount:"+ExpectedgmpAmountDue+"But Actual Amount Due: "+PayAmountDue);
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
		
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation = ObjLoginScreen.logintoOP("CETPRTCLAIMANT", environment);
		ObjOPHomePage = objSelectOrganisation.selectOrganisations_dropdown(environment);
		OPSearchForClaim ObjOPSearchForClaim = ObjOPHomePage.clickOnSearchClaimButton();
		OPSearchClaim ObjOPSearchClaim = ObjOPSearchForClaim.clickOnGOSClaimSearch();
		objOPCETPerformerSignatory = ObjOPSearchClaim.searchAndOpenClaimByType("CET");
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETPerformerSignatory.CETPerformerSignatorySnaps(key+"_CET_PerformerSignatoryDetails_ReadonlyMode");
			}
		}
		
	/*	OPCETContractorSignatory objOPCETContractorSignatory = objOPCETPerformerSignatory.clickOnSaveAndNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETContractorSignatory.CETContractorSignatorySnaps(key+"_CET_ContractorSignatoryDetails_ReadonlyMode");
			}
		}*/
		
	}
	
	/*	***********************************************************************************************************
	 * Amit Rasal : - 16703 - This is Regression Test Case - OPT_CET_PRT_BR_04. To validate CET Portal claim is processed
	 *  for payment, if the Claim is made in respect of ophthalmic performers	 *  
	
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateCETClaim_CETPRTClaimant(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_CET_PTR_BR_04");
		String keyname = keys.get(0);
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
	
		String primaryEntity = "Ophthalmic Claims";
		List<String> GroupTypeValueList = Arrays.asList("fld","fld","fld");
		List<String> FieldValueList = Arrays.asList("Claim Type","Contractor Number","Claim Status");
		List<String> ConditionValueList = Arrays.asList("Equals","Equals","Equals");
		List<String> ValueTypeList = Arrays.asList("Lookup", "Text","Lookup");
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "OPContractorCode");
		List<String> ValueForFieldValueList = Arrays.asList("CET", contractorCode, "Accepted For Payment");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity, GroupTypeValueList,FieldValueList,ConditionValueList, ValueTypeList, ValueForFieldValueList);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
			System.out.println("Claim Entries have been deleted for Contractor ");
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No Claim record avaialble for Contractor");

		}
		
		quit(browser);
		
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home("CETPRTCLAIMANT", environment);
		//OPHomePage ObjOPHomePage = ObjSelectOrganisation.selectOrganisation();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPCETPerformerSignatory objOPCETPerformerSignatory= ObjMakeAClaim.clickGOSCETButton();
		
		//OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(1, 365);
		//OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETPerformerSignatory.CETPerformerSignatorySnaps(key+"_CET_PerformerSignatoryDetails");
			}
		}
		
		OPCETContractorSignatory objOPCETContractorSignatory = objOPCETPerformerSignatory.clickOnSaveAndNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETContractorSignatory.CETContractorSignatorySnaps(key+"_CET_ContractorSignatoryDetails");
			}
		}
		
		String ClaimNo = objOPCETContractorSignatory.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPCETContractorSignatory.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPCETContractorSignatory.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPCETContractorSignatory.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		colNum = 1;
		primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
		if (flag1)
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
	
	/*
	 * TC 12613 - OPT_RBAC_34 - Validate if user having user role "CETPRTClaimant" with privilege "ManageMyCETPRTclaims" is logged in, then user is allowed to access all CET claims screens but only view 
	*/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateCETClaim_CETPRTCLAIMANT(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_RBAC_34");
		String keyname = keys.get(0);
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
	
		String primaryEntity = "Ophthalmic Claims";
		List<String> GroupTypeValueList = Arrays.asList("fld","fld","fld");
		List<String> FieldValueList = Arrays.asList("Claim Type","Contractor Number","Claim Status");
		List<String> ConditionValueList = Arrays.asList("Equals","Equals","Equals");
		List<String> ValueTypeList = Arrays.asList("Lookup", "Text","Lookup");
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "OPContractorCode");
		List<String> ValueForFieldValueList = Arrays.asList("CET", contractorCode, "Accepted For Payment");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity, GroupTypeValueList,FieldValueList,ConditionValueList, ValueTypeList, ValueForFieldValueList);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
			System.out.println("Claim Entries have been deleted for Contractor ");
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No Claim record avaialble for Contractor");

		}
		ObjCrmHome = ObjAdvancedFindResult .closeAdvanceFindWindow();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		//int colNum = 1;
		
		String primaryEntity1 = "Opthalmic Payment Rates";
		List<String> GroupTypeValueList1 = Arrays.asList("fld");
		List<String> FieldValueList1 = Arrays.asList("Name");
		List<String> ConditionValueList1 = Arrays.asList("Equals");
		List<String> ValueTypeList1 = Arrays.asList("Text");
		List<String> ValueForFieldValueList1 = Arrays.asList("CET");
		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity1, GroupTypeValueList1,FieldValueList1,ConditionValueList1, ValueTypeList1, ValueForFieldValueList1);
		
		boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
		if (flag1)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			String rate = ObjAdvancedFindResult.getDetailsFromOpthClaimTypeResultRecordScreen(keyname);
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No record found for payment rates");

		}
		
		quit(browser);
		
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation = ObjLoginScreen.logintoOP("CETPRTCLAIMANT", environment);
		//SelectOrganisation ObjSelectOrganisation = ObjLoginScreen.logintoOP("PCSECLAIMPROCESSOR", environment);
	//	OPHomePage ObjOPHomePage = ObjSelectOrganisation.selectOrganisation();
		OPHomePage ObjOPHomePage = ObjSelectOrganisation.selectOrganisations_dropdown(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPCETPerformerSignatory objOPCETPerformerSignatory= ObjMakeAClaim.clickGOSCETButton();
		
		//OPSupervisorsDeclaration objOPSupervisorsDeclaration= objOPTraineeDetails.TraineeDetailsEntered(1, 365);
		//OPContractorDeclaration objOPContractorDeclaration = objOPSupervisorsDeclaration.clickOnSaveandNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETPerformerSignatory.CETPerformerSignatorySnaps(key+"_CET_PerformerSignatoryDetails");
			}
		}
		
		OPCETContractorSignatory objOPCETContractorSignatory = objOPCETPerformerSignatory.clickOnSaveAndNextButton();
		
		if(evidence)
		{
			for (String key:keys)
			{
				objOPCETContractorSignatory.CETContractorSignatorySnaps(key+"_CET_ContractorSignatoryDetails");
			}
		}
		
		String ClaimNo = objOPCETContractorSignatory.getClaimNumber(keyname);
		Boolean clmSubFlag = objOPCETContractorSignatory.clickOnSubmitButton();

		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objOPCETContractorSignatory.getMsgTxtOnPopup();
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
		
		ObjOPHomePage = objOPCETContractorSignatory.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","CETPRTCLAIMANT");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		colNum = 1;
		primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");

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
							Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status:"+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
						}	
						String PayAmountDue = paymentLineDetailsList.get(1);
						System.out.println(PayAmountDue);
						if (PayAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
						{
							System.out.println("The Payment Line amount due is matching, Expected: "+ExpectedgmpAmountDue+",Actual: "+PayAmountDue);
							setAssertMessage("The Payment Line amount due is matching, Expected: "+ExpectedgmpAmountDue+",Actual: "+PayAmountDue, 1);
						}
						else
						{
							System.out.println("The Payment Line amount due is not matching, Expected: "+ExpectedgmpAmountDue+",Actual: "+PayAmountDue);
							//setAssertMessage("The Payment Line status is not Pending.", 1);
							Assert.assertEquals(PayAmountDue, ExpectedgmpAmountDue, "The Payment Line due amount is not matching with Expected amount. Expected Amount:"+ExpectedgmpAmountDue+"But Actual Amount Due: "+PayAmountDue);
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
	
	
	
	
	
}	
