package testscripts;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import helpers.GPPHelpers;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.GPP.GPPaymentHomePage;
import pageobjects.GPP.CI.GPPHomePageNew;
import pageobjects.GPP.QOF.QOFPaymentScreen;

import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.CopyFileToNetworkLocation;
import utilities.ExcelUtilities;

@Listeners(ListenerClass.class)
public class GPP_QOFScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 10759- This is Regression Test Case - GPP_QOF_01. GPP QOF - CQRS file Validation  Verify the 
	 * CQRS generated data imported from the xml file by BizTalk
	 * 
	 * 10762 - GPP_QOF_04- GPP QOF - CQRS File Import Verify the data is imported to Staging DB from CQRS file
	 * 
	 * 10765- GPP_QOF_08- GPP QOF - Monthly Payment Calculation Verify the QOF Monthly payment calculation when 
	 * Aspirational data is available at start of financial year
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity","CloneSanity"}, priority=1)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyQOFDataFlow(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, SQLException, ParseException{
		
		List<String> keys= Arrays.asList("GPP_QOF_01","GPP_QOF_04","GPP_QOF_08","GPP_QOF_06");
		
		//data deletion before execution
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		//AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 5;
		for(int i=7;i>=colNum;i--){
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",i);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",i);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",i);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",i);
			String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectValueForField",i);
			//utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectValueForField",i);
/*			String  deleteEntities= utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "DeleteEntriesFor",colNum);
			String[] deleteEntityArray= deleteEntities.split(",");
			List<String> deleteEntity= Arrays.asList(deleteEntityArray);*/
			
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

			boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
			if (flag2)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted from entity: "+primaryEntity);

			}
			ObjAdvancedFindResult.closeWindow();
		}

		System.out.println("All the entries have been deleted");
		tearDown(browser);
		Thread.sleep(2000);
		String CQRSdestination= ConfigurationData.getRefDataDetails(environment, "CQRSBizTalkPath");
		//GPPHelpers.updateXMLAndDATFile("GPPQOF","GPQOFTestData","QOFAttribute","GPP_QOF_01",environment);
		
		List<String> aspirationFiles= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.aspSourcePath, CQRSdestination, "Aspiration",environment);
		List<String> achievementFiles= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.achSourcePath, CQRSdestination, "Achievement", environment);
		System.out.println("Aspiration and Achievement files have been placed to Network drive.");
		setAssertMessage("Aspiration and Achievement files have been placed to Network drive.", 1);
		setAssertMessage("Aspiration Data: "+aspirationFiles, 2);
		setAssertMessage("Achievement Data: "+achievementFiles, 3);
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		QOFPaymentScreen objQOFPaymentScreen= objGPPaymentHomePage.ClickOnQOFTab();
		objQOFPaymentScreen= objQOFPaymentScreen.searchQOFPaymentInstructionData();
		objQOFPaymentScreen= objQOFPaymentScreen.clickOnSearchButton();
		String strAnnualAspValue= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", 7);
		int numberOfMonths= 12;
		float monthlyAspValue= (Float.parseFloat(strAnnualAspValue))/numberOfMonths;
		System.out.println("Monthly aspiration value is: "+monthlyAspValue);
		
		String strAnnualAchValue= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Achievement", 7);
		float monthlyAchValue= (Float.parseFloat(strAnnualAchValue))/numberOfMonths;
		System.out.println("Monthly achievement value is: "+monthlyAchValue);
		List<String> fiscalMonths= GPPHelpers.getCurrentFiscalMonths("GPPQOFTESTDATA.xlsx", "FileStatus", "Achievement", 8);
		System.out.println("Fiscal months are: "+fiscalMonths);
		Map<String,Float> monthlyAmount= objQOFPaymentScreen.saveMonthlyDetails(fiscalMonths,monthlyAspValue);
		
		for(Map.Entry<String, Float> data:monthlyAmount.entrySet())
		{
			String month= data.getKey();
			Float amount= data.getValue();
			System.out.println("For a month: "+month+", amount is "+amount);
			ExcelUtilities.setKeyValueByPosition("GPPQOFTESTDATA.xlsx", "MonthlyValues", amount.toString(), month, "Value");
		}
		objQOFPaymentScreen= objQOFPaymentScreen.clickOnFinYearLink();
		boolean isAspVerified= objQOFPaymentScreen.verifyAspirationMonthlyAmount(monthlyAmount);
		if(evidence){
			for(String key:keys){
				objQOFPaymentScreen.capturePortalSnap(key+"_file uploading progress");
			}
			
		}
		Assert.assertEquals(isAspVerified, true, "Monthly Aspiration data is not matching");
		System.out.println("Monthly Aspirational data is verified");
		setAssertMessage("Monthly Aspirational data is verified", 4);
		objQOFPaymentScreen.clickOnCloseWindow();
		
		for (String key : keys) {
			System.out.println("End to end flow is completed for key: "+key);
			setAssertMessage("End to end flow is completed for key: "+key, 5);
		}
			
	}
	
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 10764- GPP_QOF_06- GPP QOF - CRM load on validation pass: Validate that GPP QOF data are loaded to CRM from 
	 * Staging by SSIS services

	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity","CloneSanity"},priority=2,dependsOnMethods={"verifyQOFDataFlow"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void validateDataInCRM(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, SQLException, ParseException{
		String key= "GPP_QOF_06";
		
		String practice= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", 6);
		String finYear= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", 8);
		String []finYearArray= finYear.split("-");
		String splitStartYear= finYearArray[0].toString().trim();
		
		String strAnnualAspValue= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", 7);
		String UKAspValue= CommonFunctions.convertToCurrencyFormat(strAnnualAspValue, "en","GB",false);
		
		String strAnnualAchValue= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Achievement", 7);
		String UKAchValue= CommonFunctions.convertToCurrencyFormat(strAnnualAchValue, "en","GB",false);
		
		String asprecdDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", 4);
		String achrecdDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Achievement", 4);
		
		List<String> inputData= Arrays.asList(practice,splitStartYear,UKAspValue,UKAchValue,asprecdDate,achrecdDate);
		
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 5;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectValueForField",colNum);
		
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		SoftAssert softAssert= new SoftAssert();
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				boolean isDataVerified= ObjAdvancedFindResult.verifyQOFData(key,inputData);
				if(isDataVerified){
					System.out.println("Value is: "+isDataVerified);
					setAssertMessage("Values in CRM are verified with the input data given through MESH.", 1);
					System.out.println("Values in CRM are verified with the input data given through MESH.");
				}else{
					System.out.println("Value is: "+isDataVerified);
					softAssert.assertEquals(isDataVerified, true);
					setAssertMessage("Values in CRM are not verified with the input data given through MESH.", 1);
					System.out.println("Values in CRM are not verified with the input data given through MESH.");
				}
			}
		}
		// CRM Data entry deletion code
		colNum = 5;
		for(int i=7;i>=colNum;i--){
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			String primaryEntity2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",i);
			String GroupTypeValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",i);
			String FieldValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",i);
			String ConditionValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",i);
			String ValueForFieldValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectValueForField",i);
/*			String  deleteEntities= utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "DeleteEntriesFor",colNum);
			String[] deleteEntityArray= deleteEntities.split(",");
			List<String> deleteEntity= Arrays.asList(deleteEntityArray);*/
			
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity2, GroupTypeValue2,FieldValue2,ConditionValue2, ValueForFieldValue2);

			boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
			if (flag2)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted from entity: "+primaryEntity2);

			}
			ObjAdvancedFindResult.closeWindow();
		}

		System.out.println("All the entries have been deleted");
		softAssert.assertAll();
	}		
	

/*	// to be checked

		***********************************************************************************************************
	 * Akshay Sohoni : - 10785- This is Regression Test Case - GPP_QOF_25. GPP QOF - aspirational Amount Iteration 
	 * Verify the Aspiration data Iterations and monthly value calculation accordingly
	 	******************************************************************************************************************
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyQOFIterationData(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, SQLException, AWTException{
		List<String> keys= Arrays.asList("GPP_QOF_25");
		
		// this will place aspiration file for BizTalk
		List<String>aspirationFiles= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.aspSourcePath, GPPHelpers.CQRSdestination, "Aspiration");
		System.out.println("Aspiration files have been placed to Network drive.");
		setAssertMessage("Aspiration files have been placed to Network drive.", 1);
		setAssertMessage("Aspiration Data: "+aspirationFiles, 2);
				
		setup(browser, environment, clientName,"GPP");
		GPPHomePage objGPPHomePage= new GPPHomePage(getDriver());
		QOFPaymentScreen objQOFPaymentScreen= objGPPHomePage.clickOnQofPaymentMenuButton();
		objQOFPaymentScreen= objQOFPaymentScreen.searchQOFPaymentInstructionData();
		objQOFPaymentScreen= objQOFPaymentScreen.clickOnSearchButton();
		String strAnnualAspValue= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", 6);
		int numberOfMonths= 12;
		float monthlyAspValue= (Float.parseFloat(strAnnualAspValue))/numberOfMonths;
		System.out.println("Monthly aspiration value is: "+monthlyAspValue);
		
		String strAnnualAchValue= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Achievement", 6);
		float monthlyAchValue= (Float.parseFloat(strAnnualAchValue))/numberOfMonths;
		System.out.println("Monthly achievement value is: "+monthlyAchValue);
		
		List<String> fiscalMonths= GPPHelpers.getCurrentFiscalMonths("GPPQOFTESTDATA.xlsx", "FileStatus", "Achievement", 7);
		System.out.println("Fiscal months are: "+fiscalMonths);
		Map<String,Float> monthlyAmount= objQOFPaymentScreen.saveMonthlyDetails(fiscalMonths,monthlyAspValue);
		
		for(Map.Entry<String, Float> data:monthlyAmount.entrySet())
		{
			String month= data.getKey();
			Float amount= data.getValue();
			System.out.println("For a month: "+month+", amount is "+amount);
			ExcelUtilities.setKeyValueByPosition("GPPQOFTESTDATA.xlsx", "MonthlyValues", amount.toString(), month, "Value");
		}
		objQOFPaymentScreen= objQOFPaymentScreen.clickOnFinYearLink();
		boolean isAspVerified= objQOFPaymentScreen.verifyAspirationMonthlyAmount(monthlyAmount);
		Assert.assertEquals(isAspVerified, true, "Monthly Aspiration data is not matching");
		System.out.println("Monthly Aspirational data is verified");
		setAssertMessage("Monthly Aspirational data is verified", 4);
		objQOFPaymentScreen= objQOFPaymentScreen.clickOnCloseWindow();
		
		// this will check if PL is created for QOF
		tearDown(browser);
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 6;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectValueForField",colNum);
		
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated_GPPQOF("Accepted");
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim");
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus_GPPQOF("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate",3,evidence, "_PaymentLineStatus_", "_GMPAmountDue_");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						if (PaymentLineStatus.equalsIgnoreCase("Pending"))
						{
							System.out.println("The Payment Line status is Pending");
							setAssertMessage("The Payment Line status is Pending.", 5);
						}
						else
						{
							System.out.println("The Payment Line status is not Pending");
							//setAssertMessage("The Payment Line status is not Pending.", 1);
							Assert.assertEquals(PaymentLineStatus, "Pending", "The Payment Line status is not matching with Expected Status. Expected Status:"+"Pending "+"But Actual Status: "+PaymentLineStatus);
						}					
					}
				else
				{
					System.out.println("The Payment Line is not generated for claim.");
					Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
				}
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}

				}
			else
			{
				Assert.assertNotNull(title, "Title extracted from result record is NULL.");
			}

			}
		
		// This will process the PL for GMP run
		Thread.sleep(3000);
		String PaymentDuedate_Excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate", 3);
		setup(browser, environment, clientName, "GMP");
		GMPHome ObjGMPHome = new GMPHome(getDriver());
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(PaymentDuedate_Excel,2);
		ObjGMPHome = ObjGMPHome.clickonSearch();
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload();
		if(evidence)
		{
			objPaymentInstructionFile.takescreenshots("QOF_01"+"Download_Dot1file");
		}

		// This will process the PL for GMP run
		tearDown(browser);
		//this will verify PL status post GMP run
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);

		boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
		if (flag1)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{

				// Verify Payment Line generated
				boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated("Accepted");
				List<String> paymentLineDetailsList = new ArrayList<String>();
				if(PaymentLine)
				{						
					System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
					String Paylinestatus = ObjAdvancedFindResult.getPaymentlinestatus();
					if(evidence)
					{
						ObjAdvancedFindResult.takescreenshots("QOF_01"+"Paymentstatus_AfterGMPRun");
					}
					if(Paylinestatus.equalsIgnoreCase("Payment Instruction Issued"))
					{
						System.out.println("The GMP run is sucessfully");
						setAssertMessage("The GMP run is sucessfully", 6);
					}
					Verify.verifyNotEquals((Paylinestatus.equalsIgnoreCase("Payment Instruction Issued")), "The GMP is not run sucessfully ");	

				}
			}
		}
		
		//this will place Aspiration file for iteration 2
		
		List<String>aspirationFiles2= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.asp_IterationPath, GPPHelpers.CQRSdestination, "Aspiration");
		System.out.println("Aspiration files have been placed to Network drive for iteration 2.");
		setAssertMessage("Aspiration files have been placed to Network drive for iteration 2.", 7);
		setAssertMessage("Aspiration Data: "+aspirationFiles2, 7);
		
		//Logic for checking paid amount to be written here
		
		
		// this will place Achievement file for Biztalk post payment of aspiration file 
		List<String>achievementFiles= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.achSourcePath, GPPHelpers.CQRSdestination, "Achievement");
		System.out.println("Achievement files have been placed to Network drive.");
		setAssertMessage("Achievement files have been placed to Network drive.", 3);
		setAssertMessage("Achievement Data: "+achievementFiles, 4);
	}*/
	
	//this is to be merged with existing script
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity"}, priority=1)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void updateXMLData(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, SQLException, ParseException{
		//setup(browser, environment, clientName,"GPP");
		String key="";
		GPPHelpers.updateXMLAndDATFile("GPPQOF","GPQOFTestData","QOFAttribute",key,environment);
		
		List<String> aspirationFiles= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.newAspSourcePath, GPPHelpers.CQRSdestination, "Aspiration", environment);
		List<String> achievementFiles= CopyFileToNetworkLocation.placeFileToNetworkPath(GPPHelpers.newAchSourcePath, GPPHelpers.CQRSdestination, "Achievement", environment);
		System.out.println("Aspiration and Achievement files have been placed to Network drive.");
		setAssertMessage("Aspiration and Achievement files have been placed to Network drive.", 1);
		setAssertMessage("Aspiration Data: "+aspirationFiles, 2);
		setAssertMessage("Achievement Data: "+achievementFiles, 3);
	}
}


	

