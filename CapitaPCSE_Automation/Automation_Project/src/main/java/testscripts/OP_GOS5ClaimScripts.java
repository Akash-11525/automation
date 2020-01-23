package testscripts;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
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
import helpers.OPHelpers;
import helpers.Screenshot;
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

public class OP_GOS5ClaimScripts extends BaseClass 
{
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7254- This is Regression Test Case - OPT_GOS5_PTR_BR_35. To verify GOS 5 claim is accepted, 
	 * (E2E) To validate when "lower of the private charge or NHS domiciliary visit fee" does not 
	 * exceeds current NHS Domiciliary fee for 3rd or subsequent patient at the address
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CLONE","CloneSanity","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateDomiciliaryVisitFeeForSubsequentAddress(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_35";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
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
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		/*if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}*/
		
		if(evidence)
		{
			for (String key:keys)
			{
				objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		String key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
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
							
							//Verify Amount Due on Payment Line
							
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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7253- This is Regression Test Case - OPT_GOS5_PTR_BR_34. To verify GOS 5 claim is accepted, 
	 * (E2E) To validate when "lower of the private charge or NHS domiciliary visit fee" does not exceeds 
	 * current NHS Domiciliary fee for 2nd patient at the address
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateDomiciliaryVisitFeeForSecondAddress(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_34";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String []keyArray= strKeys.split(",");
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		List<String> keys= Arrays.asList(keyArray);
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			for(String strKey:keys){
				objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(strKey+"_Portal_ClaimDetails");
			}
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "CRMMODE");
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
			String ValueForFieldValue = ClaimNo;
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPGMPAMOUNTDUE");

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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
			//String keyname = scriptKey;
			boolean isMatched= false;
			int i=1;
			List<String> AssertMessage= new ArrayList<String>();
			//String ClaimNo= "ADA00661";	
			OPHelpers objOPHelpers= new OPHelpers(isMatched, AssertMessage);
			objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,scriptKey,environment,dbEnvironment,ClaimNo,"ClaimDetails");	
			for(String AssetMessage : objOPHelpers.AssertMessage)
			{
				setAssertMessage(AssetMessage, i);
				i = i + 1;
			}
			if(objOPHelpers.Process){
				objOPHelpers= new OPHelpers(isMatched, AssertMessage);
				objOPHelpers= objOPHelpers.validateOPClaimDetails(newTestDataFileName,scriptKey,environment,dbEnvironment,ClaimNo,"PLDetails");
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
	 * Akshay Sohoni : - 7223- This is Regression Test Case - OPT_GOS5_PTR_BR_19. To validate claim is  submitted successfully 
	 * when Patient's Declaration Date is within 1 year and 1 year 1month  from the last sight test date. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateClaimForSightTestDateWithinOneYearAndMore(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, 
	AWTException, ParseException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_19";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
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
		String strPatDetDays= values.get(8);
		int patDetDays= Integer.parseInt(strPatDetDays);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		// 405 is days count for a last sight test
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEnteredwithlastSightTest(scriptKey, patDetDays, environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPGMPAMOUNTDUE");

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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7252- This is Regression Test Case - OPT_GOS5_PTR_BR_33. To validate when "lower of the private charge or 
	 * NHS domiciliary visit fee" does not exceeds current NHS Domiciliary fee for 1st patient at the address
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateDomiciliaryVisitFeeForFirstAddress(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_33";

		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey, environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPGMPAMOUNTDUE");

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
		}else{
			String dbEnvironment = "CRMDB";
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
	

	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7250- This is Regression Test Case - OPT_GOS5_PTR_BR_31. To validate  The 'lower of the private 
	 * charge or NHS domiciliary visit fee' when 3rd or subsequent patient at the address is selected
	 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateLowerOfPrivateChargeForSubsequentAddress(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_31";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPGMPAMOUNTDUE");

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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7224- This is Regression Test Case - OPT_GOS5_PTR_BR_20. To validate claim is  submitted successfully 
	 * when Patient's Declaration Date is within 11months and 1 year from the last sight test date. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","RegressionNewEnv"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateClaimForSightTestDateWithinOneYear(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, 
	AWTException, ParseException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_20";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
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
		String strPatDetDays= values.get(8);
		int patDetDays= Integer.parseInt(strPatDetDays);
		String earlyReTestCode= values.get(9);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		// 335 is days count for a last sight test.
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEnteredwithlastSightTest(scriptKey, patDetDays,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEnteredWithRetestCode(scriptKey,newTestDataFileName,perDeclOptionSheet,earlyReTestCode);
		
		if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", key, "CRMMODE");
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
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "CLAIMID");
			System.out.println("Claim Number:"+ValueForFieldValue);
			String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPCLAIMSTATUS");
			String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPPAYMENTLINESTATUS");
			String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", scriptKey, "EXPGMPAMOUNTDUE");

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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 16598- This is Regression Test Case - OPT_Note_GOS5_PTR_21. To verify, Performer is notified 
	 * that the GOS 5 claim is awaiting their approval.  
	 	*******************************************************************************************************************/
	/*@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyGOS5Notification(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_Note_GOS5_PTR_21";

		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String[]keyArray= strKeys.split(",");
		String role= values.get(2);
		String []roleArray= role.split(",");
		String tabName= values.get(3);
		String []tabArray= tabName.split(",");
		String CRMModule= values.get(4);
		String CRMUSER= values.get(5);
		String HomePageTabName=values.get(6);
		String orgName= values.get(7);
		List<String> keys = Arrays.asList(keyArray);
		List<String> roles= Arrays.asList(roleArray);
		List<String> tabs= Arrays.asList(tabArray);
		String keyname = keys.get(0);
		String claimantRole= roles.get(0);
		String claimProcessorRole= roles.get(1);
		String MessageTab= tabs.get(0);
		String orgTab= tabs.get(1);
		
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(claimantRole, environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey, environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		objGOS5PatientDeclaration.enterSignatory();
		objGOS5PatientDeclaration.clickOnSaveAwaitingPerformer();
		String ClaimNo = objGOS5PatientDeclaration.getClaimNumber(keyname);
		//ObjOPHomePage= ObjOPHomePage.ClickonTab(HomePageTabName, OPHomePage.class);
		MyNotification objMyNotification= ObjOPHomePage.ClickOnPageHeader(MessageTab, MyNotification.class);
		objGOS5PatientDeclaration= objMyNotification.clickOnReviewLink(ClaimNo,OPGOS5PatientDeclaration.class,keyname+"_PerformerAwaitingApproval_"+ClaimNo,"is awaiting Performer approval.");
		if(evidence){
			objGOS5PatientDeclaration.GOS5ClaimDetailssnaps(keyname+"_after clicking on Performer review");
		}
		objGOS5PatientDeclaration.clickOnSaveandNextButtonWithoutSignatory();
		setAssertMessage("Performer awaiting approval notification has been displayed for claim: "+ClaimNo, 1);
		System.out.println("Performer awaiting approval notification has been displayed for claim: "+ClaimNo);
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= new OPGOS5PerformerDeclaration(getDriver());
		objGOS5PerformerDeclaration= objGOS5PerformerDeclaration.enterDetailsAndClickOnAwaitingContractor(scriptKey,newTestDataFileName,patDeclOptionSheet);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home(claimProcessorRole, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(orgTab, SelectOrganisation.class);
		ObjOPHomePage= objSelectOrganisation.selectOrganisation(orgName, OPHomePage.class);
		objMyNotification= ObjOPHomePage.ClickOnPageHeader(MessageTab, MyNotification.class);
		objGOS5PerformerDeclaration= objMyNotification.clickOnReviewLink(ClaimNo,OPGOS5PerformerDeclaration.class,keyname+"_ContractorSignatoryApproval_"+ClaimNo,"is awaiting Contractor Signatory approval.");
		if(evidence){
			objGOS5PerformerDeclaration.GOS5ClaimDetailssnaps(keyname+"_after clicking on Contractor review");
		}
		objGOS5PerformerDeclaration.clickOnSaveandNextButton();
		setAssertMessage("Contractor awaiting approval notification has been displayed for claim: "+ClaimNo, 2);
		System.out.println("Contractor awaiting approval notification has been displayed for claim: "+ClaimNo);
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= new OPGOS5ContractorDeclaration(getDriver());
		if(evidence)
		{
			for (String key:keys)
			{
				objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}

		ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		ObjOPHomePage = objGOS5ContractorDeclaration.clickOnRevertToDraftButton();
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage = ObjLoginScreen.logintoOP_Home(claimantRole, environment);
		objMyNotification= ObjOPHomePage.ClickOnPageHeader(MessageTab, MyNotification.class);
		boolean isReverted= objMyNotification.verifyNotificationText(ClaimNo,"has been reverted to draft.",keyname+"_Claim reverted to draft_"+ClaimNo);
		if(evidence){
			objMyNotification.captureNotificationSnap(keyname+"_claim reverted to Draft_"+ClaimNo);
		}
		Assert.assertEquals(isReverted, true);
		setAssertMessage("Claim is reverted to draft for claim: "+ClaimNo, 3);
		System.out.println("Claim is reverted to draft for claim: "+ClaimNo);

		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		tearDown(browser);
		Thread.sleep(3000);
		
		// To verify status of GOS3 claim submitted in CRM.
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValue= Arrays.asList("fld");
		List<String>FieldValue= Arrays.asList("Regarding");
		List<String>ConditionValue= Arrays.asList("Equals");
		List<String>ValueType= Arrays.asList("Lookup");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18532- This is Regression Test Case - OPT_CR06_GOS5_PTR_03. Validate user can not submit GOS 5  
	 * with a negative value
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateNegativeValue(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		String scriptKey="OPT_CR06_GOS5_PTR_03";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String[]keyArray= strKeys.split(",");
		String role= values.get(2);
		String tabName= values.get(3);
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim objOPMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= objOPMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey, environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			for (String key:keys)
			{
				objGOS5PerformerDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails_validation message");
			}
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(keyname);
		List<String> ActualErrorMessagesonSave = objGOS5ContractorDeclaration.getErrors();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("OPGOS5TESTDATA.xlsx", "PATIENTDETAILSMANDFIELDS", 10);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient signatory on contractor declaration are appeared correctly", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on contractor declaration are not appeared correctly");
		}
		softAssertion.assertAll();
	}
	
/*		***********************************************************************************************************
	 * Akshay Sohoni : - 7254- This is Regression Test Case - OPT_GOS5_PTR_BR_35. To verify GOS 5 claim is accepted, 
	 * (E2E) To validate when "lower of the private charge or NHS domiciliary visit fee" does not 
	 * exceeds current NHS Domiciliary fee for 3rd or subsequent patient at the address
	 	******************************************************************************************************************
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void VerifyChangeInDataFlow(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		String scriptKey= "OPT_GOS5_PTR_BR_35";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
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
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			for (String key1:keys)
			{
				objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key1+"_Portal_ClaimDetails");
			}
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(keyname,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Sanity","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateDomiciliaryVisitFeeForSecondAddress_New(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String scriptKey="OPT_GOS5_PTR_BR_34";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","TestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String expSubmitMsg= values.get(4);
		String CRMModule= values.get(5);
		String CRMUSER= values.get(6);
		String strAdvFindNum= values.get(7);
		int advFindNum= Integer.parseInt(strAdvFindNum);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation();
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(key,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
	}*/
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7225- This is Regression Test Case - OPT_GOS5_PTR_BR_21. To validate claim is  submitted 
	 * successfully when Patient's Declaration Date is less than  11months from the last sight test date. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateClaimForSightTestDateWithNoRetestCode(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, 
	AWTException, ParseException
	{
		String scriptKey="OPT_GOS5_PTR_BR_21";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		String strPatDetDays= values.get(4);
		int patDetDays= Integer.parseInt(strPatDetDays);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		// 335 is days count for a last sight test.
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEnteredwithlastSightTest(scriptKey, patDetDays,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence){
			objGOS5PerformerDeclaration.GOS5ClaimDetailssnaps(key+"_validation message on submit");
		}
		List<String> ActualErrorMessagesonSave = objGOS5PerformerDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel(newTestDataFileName, "PATIENTDETAILSMANDFIELDS", 9);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient signatory on performer declaration are appeared correctly", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on performer declaration are not appeared correctly");
		}
		softAssertion.assertAll();
	}	
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7226- This is Regression Test Case - OPT_GOS5_PTR_BR_22. To validate missing HC3 Certificate details
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateHC3Message(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, 
	AWTException, ParseException
	{
		String scriptKey="OPT_GOS5_PTR_BR_22";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");

		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		// 335 is days count for a last sight test.
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		if(evidence){
			objGOS5PatientEligibility.GOS5ClaimDetailssnaps(key+"_HC3 Certificate message");
		}
		List<String> ActualErrorMessagesonSave = objGOS5PatientEligibility.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel(newTestDataFileName, "PATIENTDETAILSMANDFIELDS", 11);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient eligibility are appeared correctly", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient eligibility are not appeared correctly");
		}
		softAssertion.assertAll();
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7228- This is Regression Test Case - OPT_GOS5_PTR_BR_24. To validate when a patient selects 
	 * complex lenses
	 * 7229- OPT_GOS5_PTR_BR_25- To validate when The 'lower of private charge or NHS sight test fee' exceeds the current NHS sight test fee
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateComplexLensSelection(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, 
	AWTException, ParseException
	{
		String scriptKey="OPT_GOS5_PTR_BR_24";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		// 335 is days count for a last sight test.
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence){
			objGOS5PerformerDeclaration.GOS5ClaimDetailssnaps(key+"_validation message on submit");
		}
		List<String> ActualErrorMessagesonSave = objGOS5PerformerDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel(newTestDataFileName, "PATIENTDETAILSMANDFIELDS", 12);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient signatory on performer declaration are appeared correctly", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on performer declaration are not appeared correctly");
		}
		softAssertion.assertAll();
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7245- This is Regression Test Case - OPT_GOS5_PTR_BR_26. To validate missing  The 'lower of 
	 * private charge or NHS sight test fee' field 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateMissingSightFee(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, 
	AWTException, ParseException
	{
		String scriptKey="OPT_GOS5_PTR_BR_26";
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);
		
		String key= strKeys;
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		// 335 is days count for a last sight test.
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		if(evidence){
			objGOS5PerformerDeclaration.GOS5ClaimDetailssnaps(key+"_validation message on submit");
		}
		List<String> ActualErrorMessagesonSave = objGOS5PerformerDeclaration.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel(newTestDataFileName, "PATIENTDETAILSMANDFIELDS", 13);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		//setAssertMessage("The expected error messages: "+ExpectedErrorMessagesonSave, 2);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
		SoftAssert softAssertion= new SoftAssert();
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("All errors for patient signatory on performer declaration are appeared correctly", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedErrorList.isEmpty(), "All errors for patient signatory on performer declaration are not appeared correctly");
		}
		softAssertion.assertAll();
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7248- This is Regression Test Case - OPT_GOS5_PTR_BR_29. To validate  The 'lower of the private 
	 * charge or NHS domiciliary visit fee' when 1st patient at the address is selected
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CLONE"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateDomiciliaryVisitFeeForFirstPatient(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_29";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
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
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		/*if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}*/
		
		if(evidence)
		{
			for (String key:keys)
			{
				objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 7249- This is Regression Test Case - OPT_GOS5_PTR_BR_30. To validate  The 'lower of the private 
	 * charge or NHS domiciliary visit fee' when 2nd patient at the address is selected
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression","CLONE"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateDomiciliaryVisitFeeForSecondPatient(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, SQLException
	{
		String scriptKey="OPT_GOS5_PTR_BR_30";
		
		List<String> values= ExcelUtilities.getScriptParameters("GOS5",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule("GOS5","PerDeclOption");
		
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
		
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList(keyArray);
		String keyname = keys.get(0);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage = ObjLoginScreen.logintoOP_Home(role, environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader(tabName, SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		OPMakeAClaim ObjMakeAClaim = ObjOPHomePage.clickOnMakeAClaimButton();
		OPGOS5PatientDetails objGOS5PatientDetails= ObjMakeAClaim.clickGOSFIVEButton();
		
		OPGOS5PatientEligibility objGOS5PatientEligibility= objGOS5PatientDetails.PatientDetailsEntered(scriptKey,environment);
		
		OPGOS5PatientDeclaration objGOS5PatientDeclaration= objGOS5PatientEligibility.PatientEligibilityDetailsEntered(scriptKey,newTestDataFileName,patEleOptionSheet);
		objGOS5PatientDeclaration.selectProvidedOptions(scriptKey,newTestDataFileName,patDeclOptionSheet);
		objGOS5PatientDeclaration.enterAddressManually(scriptKey);
		
		OPGOS5PerformerDeclaration objGOS5PerformerDeclaration= objGOS5PatientDeclaration.clickOnSaveandNextButton();
		
		OPGOS5ContractorDeclaration objGOS5ContractorDeclaration= objGOS5PerformerDeclaration.PeformerDeclarationDetailsEntered(scriptKey,newTestDataFileName,perDeclOptionSheet);
		
		/*if(evidence)
		{
			objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
		}*/
		
		if(evidence)
		{
			for (String key:keys)
			{
				objGOS5ContractorDeclaration.GOS5ClaimDetailssnaps(key+"_Portal_ClaimDetails");
			}
		}
		
		String ClaimNo = objGOS5ContractorDeclaration.getClaimNumber(scriptKey,newTestDataFileName);
		Boolean clmSubFlag = objGOS5ContractorDeclaration.clickOnSubmitButton();
		
		if (clmSubFlag)
		{
			setAssertMessage("The Submit Claim Popup does appeared.", 1);
			String msg = objGOS5ContractorDeclaration.getMsgTxtOnPopup();
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

		ObjOPHomePage = objGOS5ContractorDeclaration.clickCloseOnResultPopup();
		
		// Amit : Below code is commented as IE part was not working on CI CD Server.
		quit(browser);
		
		String key = scriptKey;
		String CRMMode= ExcelUtilities.getKeyValueByPosition(newTestDataFileName, "EXPECTEDCLAIMDETAILS", keyname, "CRMMODE");
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
			//String newTestDataFileName= ExcelUtilities.getExcelParameterByModule("GOS5","NewTestDataFile");
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
}	
