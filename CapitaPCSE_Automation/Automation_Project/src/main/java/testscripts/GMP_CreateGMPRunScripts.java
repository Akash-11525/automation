package testscripts;
import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.PaymentInstructionFile;
import pageobjects.GMP.VarianceReports;
import verify.Verify;



public class GMP_CreateGMPRunScripts extends BaseClass {
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","OP","Sanity","CLONE","CloneSanity"} ,enabled = true)
	@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void verifyCreateGMPRun(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		
		String key ="OPT_GOS1_PTR_BR_15";
		setup(browser, environment, clientName,"OP");
		pageobjects.GMP.GMPhelpers.GOS1Claim(getDriver(),environment,evidence);
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		pageobjects.GMP.GMPhelpers.VerifyOnCRM_GMP(getDriver());
		tearDown(browser);
		Thread.sleep(3000);
		String PaymentDuedate_Excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate", 1);
		Date date= CommonFunctions.convertStringtoCalDate(PaymentDuedate_Excel, "dd/MM/yyyy");
		String strDueDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
		//String strDueDate = "09/01/2019";
		setup(browser, environment, clientName, "GMP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload(getDriver());
		System.out.println("The Dot file is generated");
		if(evidence)
		{
			objPaymentInstructionFile.takescreenshots(key+"_Downloaded_Dot1file");
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "Claim Number", 1);
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "ExpectedClaims", 1);
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
						String Paylinestatus = ObjAdvancedFindResult.getPaymentlinestatus();
						if(evidence)
						{
							ObjAdvancedFindResult.takescreenshots(key+"_Paymentstatus_AfterGMPRun");
						}
						if(Paylinestatus.equalsIgnoreCase("Payment Instruction Issued"))
						{
							System.out.println("The GMP run is sucessfully");
							setAssertMessage("The GMP run is sucessfully", 1);
						}
						Verify.verifyNotEquals((Paylinestatus.equalsIgnoreCase("Payment Instruction Issued")), "The GMP is not run sucessfully ");	
						Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
						
					}
				}
			}
		
		
		
		}
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"} ,enabled = true)
	@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void verifyCreateGMPRunForGPP(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException, ParseException
	{
		
		String key ="2";
		setup(browser, environment, clientName,"GPP");
		pageobjects.GMP.GMPhelpers.CreateGPPStdClaim(getDriver(),environment);
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"CRMGPP");
		pageobjects.GMP.GMPhelpers.VerifyGPPClaimOnCRM_GMP(getDriver());
		tearDown(browser);
		Thread.sleep(3000);
		String PaymentDuedate_Excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate", 2);
		setup(browser, environment, clientName, "GMP");
		GMPHome ObjGMPHome = new GMPHome(getDriver());
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(PaymentDuedate_Excel,2);
		ObjGMPHome = ObjGMPHome.clickonSearch(getDriver());
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload(getDriver());
		if(evidence)
		{
			objPaymentInstructionFile.takescreenshots(key+"_Downloaded_Dot1file");
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "Claim Number", 2);
		System.out.println("Claim Number:"+ValueForFieldValue);
		String claimStatus = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "ExpectedClaims", 2);
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
						String Paylinestatus = ObjAdvancedFindResult.getPaymentlinestatus();
						if(evidence)
						{
							ObjAdvancedFindResult.takescreenshots(key+"_Paymentstatus_AfterGMPRun");
						}
						if(Paylinestatus.equalsIgnoreCase("Payment Instruction Issued"))
						{
							System.out.println("The GMP run is sucessfully");
							setAssertMessage("The GMP run is sucessfully", 1);
						}
						Verify.verifyNotEquals((Paylinestatus.equalsIgnoreCase("Payment Instruction Issued")), "The GMP is not run sucessfully ");	
						
						
					}
				}
			}
		}
	}
}
