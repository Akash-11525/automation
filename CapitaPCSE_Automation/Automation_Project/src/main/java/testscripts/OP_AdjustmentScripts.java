package testscripts;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import browsersetup.BaseClass;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.Adjustments.AdjustmentBatch;
import pageobjects.Adjustments.AdjustmentDetails;
import pageobjects.Adjustments.AdjustmentHomePage;
import pageobjects.OP.OPHomePage;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

@Listeners(ListenerClass.class)
public class OP_AdjustmentScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 17423- OPT_AD_68- OPT- Home-Adjustment-Adjustment Detail  Verify the updated User Interface 
	 * w.r.t Home-Adjustment-Adjustment Detail
	 * 12753- OPT_AD_36- OPT Adjustments Batch Creation - Submit  Verify clicking on Submit button on Adjustment Batch creation screen.
	 * 12639- OPT_RBAC_60- Adjustments - Validate if user having user role "OPHRLTPaymentsUser" with privilege "ManageAllAdjustments" 
	 * is logged in, then user is allowed to edit all fields in the adjustments screen.
	 * 12750- OPT_AD_33- OPT Adjustments Batch Creation - Add a New Adjustments  Verify clicking on Add a new Adjustment hyperlink.
	 * 12762- OPT_AD_45- OPT Adjustment Detail Search Screen - Search Header validation  Verify clicking on search button after selecting the required filter.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyAdjustmentDataOnUI(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("OPT_AD_68","OPT_AD_36","OPT_RBAC_60","OPT_AD_33","OPT_AD_45");
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValue= Arrays.asList("fld","fld");
		List<String>FieldValue= Arrays.asList("Portal User","Portal Role");
		List<String>ConditionValue= Arrays.asList("Equals","Equals");
		List<String>ValueType= Arrays.asList("Lookup","Lookup");
		String userID= ExcelUtilities.getKeyValueByPosition("ConfigurationDetails.xlsx", "USERDETAILS", "OPHRLTPaymentsUser_ID", "TEST");
		List<String>ValueForFieldValue= Arrays.asList(userID,"OPHRLTPaymentsUser");
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Portal User Roles");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				objAdvancedFindResult= objAdvancedFindResult.clickOnPortalRole(keys.get(2)+"_Role Privilege");
			}
		}
		
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		OPHomePage ObjOPHomePage = new OPHomePage(getDriver());
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatchWithStatus(7,"Draft");
		//AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();
		if(evidence){
			for(String key:keys){
				objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_records filtered");
			}
		}
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		boolean saveMessage= objAdjustmentBatch.verifySaveBatchMessage();
		SoftAssert softAsert=  new SoftAssert();
		softAsert.assertEquals(saveMessage, true);
		if(saveMessage){
			System.out.println("Save batch message is validated for key: "+keys.get(1));
			setAssertMessage("Save batch message is validated for key: "+keys.get(1), 1);
		}else{
			System.out.println("Save batch message is not validated for key: "+keys.get(1));
			setAssertMessage("Save batch message is not validated for key: "+keys.get(1), 2);
		}
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_adjustment batch is saved.");
			}
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,2,environment,"OP"); // 0 stands for months to be added in current month
		System.out.println("As values are being entered, all fields are visible for key: "+keys.get(3));
		setAssertMessage("As values are being entered, all fields are visible for key: "+keys.get(3), 3);
		List<String> expectedData= objAdjustmentBatch.getEnteredData(environment,2);
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
		Assert.assertEquals(submitMessage, true, "Submit message is not verified");
		setAssertMessage("Submit message is verified for key: "+keys.get(1), 4);
		System.out.println("Submit message is verifiedfor key: "+keys.get(1));
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Adjustment is submitted", 5);
		System.out.println("Adjustment is submitted");
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Entered data");
			}
		}
		boolean isStatusUpdated= objAdjustmentBatch.verifyBatchStatus("Approved");
		Assert.assertEquals(isStatusUpdated, true, "Status is not changed after submission");
		setAssertMessage("Status is changed after submission for key: "+keys.get(1), 6);
		System.out.println("Submit message is verifiedfor key: "+keys.get(1));
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_updated batch status after submit");
			}
		}
		
		boolean isBatchDataVerified= objAdjustmentBatch.verifyAdjustmentTableData(expectedData);
		Assert.assertEquals(isBatchDataVerified, true, "Grid data is not matching");
		setAssertMessage("Grid data is matching for key: "+keys.get(1), 7);
		System.out.println("Grid data is matching for key: "+keys.get(1));
		String batchName= objAdjustmentBatch.getAdjustmentBatchName();
		objAdjustmentHomePage= objAdjustmentBatch.clickOnAdjBreadCrumb();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,batchName);
		if(evidence){
			for(String key:keys){
				objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_records filtered by batch name");
			}
		}
		AdjustmentDetails objAdjustmentDetails= objAdjustmentHomePage.clickOnViewAdjLink();
		objAdjustmentDetails= objAdjustmentDetails.searchBatch(0,batchName);
		if(evidence){
				objAdjustmentDetails.captureAdjBatchPortalSnap(keys.get(4)+"_Adj Details filtered data");
		}
		if(evidence){
			for(String key:keys){
				objAdjustmentDetails.captureAdjBatchPortalSnap(key+"_data updated On UI");
			}
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		ObjOPHomePage = new OPHomePage(getDriver());
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		
/*		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Draft");
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		
		objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		String adjOrgName= objAdjustmentBatch.getAdjustmentOrgName();
		objAdjustmentBatch= objAdjustmentBatch.clickOnEnterBulkAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterBulkAdjustmentData("AdjustmentTestData.xlsx","BulkAdjData",3,true,false,environment,"OP");
		objAdjustmentBatch= objAdjustmentBatch.selectRLTForBulkAdj(adjOrgName);
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddToBatchButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBulkAdjReqOKButton();
/*		WebDriver driver= getDriver();
		driver.navigate().refresh();
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0, 0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Bulk adj submitted and Approved");
			}
		}
/*		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		ObjOPHomePage = new OPHomePage(getDriver());
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Pending Approval");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForAdjustment("Approve",key+"_Approved records");
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_completed action");
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();*/
		setAssertMessage("Approve action is performed on adjustment batches", 8);
		System.out.println("Approve action is performed on adjustment batches");
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 17423- OPT_AD_68- OPT- Home-Adjustment-Adjustment Detail  Verify the updated User Interface 
	 * w.r.t Home-Adjustment-Adjustment Detail
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyAdjustmentDataOnUI_PCSEProcessor(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		String key= "OPT_AD_68";
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoOP("PCSEPaymentsProcessor", environment);
		OPHomePage ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatchWithStatus(7,"Draft");
		//AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();
		if(evidence){
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_records filtered");
		}
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2, environment,"OP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		boolean saveMessage= objAdjustmentBatch.verifySaveBatchMessage();
		SoftAssert softAsert=  new SoftAssert();
		softAsert.assertEquals(saveMessage, true);
		if(saveMessage){
			System.out.println("Save batch message is validated for key: "+key);
			setAssertMessage("Save batch message is validated for key: "+key, 1);
		}else{
			System.out.println("Save batch message is not validated for key: "+key);
			setAssertMessage("Save batch message is not validated for key: "+key, 2);
		}
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_adjustment batch is saved.");
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,2,environment,"OP"); // 0 stands for months to be added in current month
		List<String> expectedData= objAdjustmentBatch.getEnteredData(environment,2);
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
		Assert.assertEquals(submitMessage, true, "Submit message is not verified");
		setAssertMessage("Submit message is verified for key: "+key, 3);
		System.out.println("Submit message is verifiedfor key: "+key);
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Adjustment is submitted", 4);
		System.out.println("Adjustment is submitted");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Entered data");
		}
		boolean isStatusUpdated= objAdjustmentBatch.verifyBatchStatus("Pending Approval");
		Assert.assertEquals(isStatusUpdated, true, "Status is not changed after submission");
		setAssertMessage("Status is changed after submission for key: "+key, 5);
		System.out.println("Submit message is verifiedfor key: "+key);
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_updated batch status after submit");
		}
		
		boolean isBatchDataVerified= objAdjustmentBatch.verifyAdjustmentTableData(expectedData);
		Assert.assertEquals(isBatchDataVerified, true, "Grid data is not matching");
		setAssertMessage("Grid data is matching for key: "+key, 6);
		System.out.println("Grid data is matching for key: "+key);
		String batchName= objAdjustmentBatch.getAdjustmentBatchName();
		objAdjustmentHomePage= objAdjustmentBatch.clickOnAdjBreadCrumb();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,batchName);
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_records filtered by batch name");
		}
		AdjustmentDetails objAdjustmentDetails= objAdjustmentHomePage.clickOnViewAdjLink();
		objAdjustmentDetails= objAdjustmentDetails.searchBatch(0,batchName);
		if(evidence){
			
			objAdjustmentDetails.captureAdjBatchPortalSnap(key+"_data updated On UI");
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		ObjOPHomePage = new OPHomePage(getDriver());
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		
/*		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Draft");
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		
		objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName",2, environment,"OP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		String adjOrgName= objAdjustmentBatch.getAdjustmentOrgName();
		objAdjustmentBatch= objAdjustmentBatch.clickOnEnterBulkAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterBulkAdjustmentData("AdjustmentTestData.xlsx","BulkAdjData",3,true,false,environment,"OP");
		objAdjustmentBatch= objAdjustmentBatch.selectRLTForBulkAdj(adjOrgName);
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddToBatchButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBulkAdjReqOKButton();
/*		WebDriver driver= getDriver();
		driver.navigate().refresh();
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0, 0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Bulk adj submitted");
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		ObjOPHomePage = new OPHomePage(getDriver());
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Pending Approval");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForAdjustment("Approve",key+"_Approved records");
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_completed action");
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Approved");
		objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_"+"Approved"+" status");
		setAssertMessage("Approve action is performed on adjustment batches", 7);
		System.out.println("Approve action is performed on adjustment batches");
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 12732- OPT_AD_15- OPT Adjustment Batch landing - Search Results Validation  Verify clicking 
	 * on the Apply button.
	 * 12718- OPT_AD_01- OPT- Adjustment batch status  Verify Adjustment Batch statuses.
	 * 12728- OPT_AD_11- OPT Adjustment Batch landing - Search Results Validation  Verify clicking on New Adjustment hyperlink.
	 * 12725- OPT_AD_08- OPT Adjustment Batch landing - Search Results Validation  Verify the Search Results – Details displayed after clicking on search button.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyAdjustmentStatuses(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("OPT_AD_15","OPT_AD_01","OPT_AD_11","OPT_AD_08");
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjOPHomePage= ObjLoginScreen.logintoOP_Home("PCSEPaymentsProcessor", environment);
		SelectOrganisation objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		for(int i=0;i<3;i++){
			AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
			if(evidence){
				for(String key:keys){
					objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Adjustment batch screen");
				}
				
			}
			objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName",2,environment,"OP");
			//objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);j
			objAdjustmentBatch= objAdjustmentBatch.saveBatch();
			objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
			if(evidence){
				for(String key:keys){
					objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record saved as Draft");
				}
				
			}
			objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
			objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,2,environment,"OP");
			objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
			boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
			Assert.assertEquals(submitMessage, true, "Submit message is not verified");
			setAssertMessage("Submit message is verified.", 1);
			System.out.println("Submit message is verified.");
			objAdjustmentBatch= objAdjustmentBatch.confirmAction();
			objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
			setAssertMessage("Adjustment is submitted for index: "+i, 2);
			System.out.println("Adjustment is submitted for index: "+i);
			if(evidence){
				for(String key:keys){
					objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record submitted_"+i);
				}
				
			}
			/*WebDriver driver= getDriver();
			driver.navigate().refresh();
			objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();*/
			ObjOPHomePage= ObjOPHomePage.ClickonTab("OPHTHALMIC", OPHomePage.class);
			objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		}
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"OP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjOPHomePage= ObjLoginScreen.logintoOP_Home("PAYMENTSSUPERVISOR", environment);
		objSelectOrganisation= ObjOPHomePage.ClickOnPageHeader("Change Organisation", SelectOrganisation.class);
		ObjOPHomePage = objSelectOrganisation.selectOrganisation(environment);
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Pending Approval");
		if(evidence){
				objAdjustmentHomePage.captureAdjBatchPortalSnap(keys.get(3)+"_Filtered Adj batch data");
		}
		
		if(evidence){
			for(String key:keys){
				objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_Pending Approval Status");
			}
			
		}
		List<String> actions= Arrays.asList("Approve","Reject","Delete");
		/*boolean isApplyDisabled= objAdjustmentHomePage.verifyElementState();
		Assert.assertEquals(isApplyDisabled, true, "Apply button is not disabled");
		setAssertMessage("Apply button is disabled.", 3);
		System.out.println("Apply button is disabled.");*/
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForMultipleAdjustments(actions,3,keys.get(1));
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.cancelAction();
		setAssertMessage("Clicked on Cancel button before updating multiple records", 4);
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			for(String key:keys){
				objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_completed actions");
			}
			
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();
		setAssertMessage("Approve,Reject and Delete actions are performed on adjustment batches", 5);
		System.out.println("Approve,Reject and Delete actions are performed on adjustment batches");
		List<String> filterStatus= Arrays.asList("Approved","Rejected");
		for(int i=0;i<filterStatus.size();i++){
			String status= filterStatus.get(i%filterStatus.size());
			objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,status);
			objAdjustmentHomePage.captureAdjBatchPortalSnap(keys.get(1)+"_"+status+" status");
		}
		setAssertMessage("All the statuses are verified", 6);
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 12743- OPT_AD_26- OPT Adjustments Batch Creation - Bulk Upload  Verify uploading Bulk adjustments.
	 * 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyBulkUpload(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("OPT_AD_26");
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		OPHomePage ObjOPHomePage = new OPHomePage(getDriver());
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record saved as Draft");
			}
		}
		List<Integer> rows= Arrays.asList(1,2,3);
		boolean CSVCreated= objAdjustmentBatch.generateGPPAdjCSVFile("OPAdj",rows,',',2);
		Assert.assertEquals(CSVCreated, true, "File creation has been failed");
		setAssertMessage("CSV file has been created successfully", 1);
		System.out.println("CSV file has been created successfully");
		objAdjustmentBatch= objAdjustmentBatch.uploadFile("OPValid");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_file uploading progress");
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBrowseOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnCSVRequestButton();
		boolean isCompleted= objAdjustmentBatch.verifyCompletedDataOnCSVRequest();
		Assert.assertEquals(isCompleted, true, "Status is not completed");
		setAssertMessage("Status is completed and there are zero rejected records", 2);
		System.out.println("Status is completed and there are zero rejected records");
		objAdjustmentBatch= objAdjustmentBatch.clickOnBrowseOKButton();
		/*WebDriver driver= getDriver();
		driver.navigate().refresh();
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		//objAdjustmentHomePage= objGPPHomePage.clickOnAdjustmentMenuButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_Uploaded records");
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnSaveForLater();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Record has been saved as Draft", 3);
		System.out.println("Record has been saved as Draft");
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 12796- OPT_AD_79- OPT Adjustments - Payment Line generation  Verify payment line generation 
	 * for adjustment.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPaymentLineCreation(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException, AWTException{
		String key="OPT_AD_79";
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		OPHomePage ObjOPHomePage = new OPHomePage(getDriver());
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,2,environment,"OP");
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
		Assert.assertEquals(submitMessage, true, "Submit message is not verified");
		setAssertMessage("Submit message is verified for key: "+key, 1);
		System.out.println("Submit message is verifiedfor key: "+key);
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Adjustment is submitted", 2);
		System.out.println("Adjustment is submitted");
		if(evidence){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record submitted");
		}
		String batchNo= objAdjustmentBatch.getAdjustmentBatchNo();
		
		// To verify status of SC claim submitted in CRM.
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValue= Arrays.asList("fld");
		List<String>FieldValue= Arrays.asList("Adjustment Batch");
		List<String>ConditionValue= Arrays.asList("Equals");
		List<String>ValueType= Arrays.asList("Lookup");
		List<String>ValueForFieldValue= Arrays.asList(batchNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Adjustments");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = objAdvancedFindResult.getDetailsFromResultRecordScreen_GPPAdj();
				if (clmStatus.contains("Approved and Awaiting Payment"))
				{
					setAssertMessage("The batch status in CRM is Approved and Awaiting Payment.", 3);
					System.out.println("The claim status in CRM is Approved and Awaiting Payment.");					
					String ClaimStatusCRM = objAdvancedFindResult.ClaimsStatus_GPPAdj(batchNo, evidence,key+"_BatchStatus_"+batchNo);
					// Verify Payment Line generated
					boolean PaymentLine = objAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = objAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key+"_PaymentLineStatus_"+batchNo, key+"_GMPAmountDue_"+batchNo);
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						System.out.println(PaymentLineStatus);
						if (PaymentLineStatus.equalsIgnoreCase("Pending"))
						{
							System.out.println("The Payment Line status is Pending");
							setAssertMessage("The Payment Line status is Pending.", 4);
						}
						else
						{
							System.out.println("The Payment Line status is not Pending");
							//setAssertMessage("The Payment Line status is not Pending.", 1);
							Assert.assertEquals(PaymentLineStatus, "Pending", "The Payment Line status is not matching with Expected Status. Expected Status: "+"Pending"+" But Actual Status: "+PaymentLineStatus);
						}					
					}
					else
					{
						System.out.println("The Payment Line is not generated for claim: " +ValueForFieldValue );
						Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
					}
					Boolean AlertPresent = objAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						objAdvancedFindResult.ClickSpaceBar();
					}

				}
				else{
					Assert.assertEquals(clmStatus, "Approved and Awaiting Payment", "The claim status in CRM is not Approved.");
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
	 * Akshay Sohoni : - 12797- OPT_AD_80- GPP Adjustments - Payment Line generation  Verify payment line generation
	 * for multiple month adjustment.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPLForMultipleMonths(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException, AWTException{
		String key="OPT_AD_80";
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		/*OPHomePage ObjOPHomePage= objSelectOrganisation.selectOrganisation("NHS ENGLAND MIDLANDS AND EAST (EAST)",OPHomePage.class);
		//OPHomePage ObjOPHomePage = objSelectOrganisation.selectOrganisation_dropdown("NHS ENGLAND MIDLANDS AND EAST (EAST)");
*/		OPHomePage ObjOPHomePage = new OPHomePage(getDriver());
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.clickOnEnterBulkAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterBulkAdjustmentData("AdjustmentTestData.xlsx","BulkAdjData",3,true,false,environment,"OP");
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddToBatchButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBulkAdjReqOKButton();
		int portalRecordCount= objAdjustmentBatch.getRecordCount();
		System.out.println("Record count is: "+portalRecordCount);
		/*WebDriver driver= getDriver();
		driver.navigate().refresh();
		objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0, 0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		String batchNo= objAdjustmentBatch.getAdjustmentBatchNo();
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Bulk adj submitted and Approved");
		}
		setAssertMessage("Approve action is performed on adjustment batches", 3);
		System.out.println("Approve action is performed on adjustment batches");
		tearDown(browser);
		Thread.sleep(3000);
		
		// To verify status of SC claim submitted in CRM.
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValue= Arrays.asList("fld");
		List<String>FieldValue= Arrays.asList("Adjustment Batch");
		List<String>ConditionValue= Arrays.asList("Equals");
		List<String>ValueType= Arrays.asList("Lookup");
		List<String>ValueForFieldValue= Arrays.asList(batchNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Adjustments");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		//String batchNo= "201830000314";
		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			int records= objAdvancedFindResult.getRecordCount();
			if(portalRecordCount==records){
				for(int i=0;i<records;i++){
					String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(i,2);
					if(title!= null)
					{
						String clmStatus = objAdvancedFindResult.getDetailsFromResultRecordScreen_GPPAdj();
						if (clmStatus.contains("Approved and Awaiting Payment"))
						{
							setAssertMessage("The batch status in CRM is Approved and Awaiting Payment for row index: "+i, 4);
							System.out.println("The batch status in CRM is Approved and Awaiting Payment for row index: "+i);					
							String ClaimStatusCRM = objAdvancedFindResult.ClaimsStatus_GPPAdj(batchNo, evidence,key+"_BatchStatus_"+batchNo+"_"+i);
							// Verify Payment Line generated
							boolean PaymentLine = objAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
							List<String> paymentLineDetailsList = new ArrayList<String>();
							if(PaymentLine)
							{						
								System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
								paymentLineDetailsList = objAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key+"_PaymentLineStatus_"+batchNo+"_"+i, key+"_GMPAmountDue_"+batchNo+"_"+i);
								String PaymentLineStatus = paymentLineDetailsList.get(0);
								System.out.println(PaymentLineStatus);
								if (PaymentLineStatus.equalsIgnoreCase("Pending"))
								{
									System.out.println("The Payment Line status is Pending for row index: "+i);
									setAssertMessage("The Payment Line status is Pending for row index: "+i, 5);
								}
								else
								{
									System.out.println("The Payment Line status is not Pending for row index: "+i);
									//setAssertMessage("The Payment Line status is not Pending.", 1);
									Assert.assertEquals(PaymentLineStatus, "Pending", "The Payment Line status is not matching with Expected Status. Expected Status: "+"Pending"+" But Actual Status: "+PaymentLineStatus);
								}					
							}
							else
							{
								System.out.println("The Payment Line is not generated for batch: " +ValueForFieldValue );
								Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +ValueForFieldValue);
							}

						}
						else{
							Assert.assertEquals(clmStatus, "Approved and Awaiting Payment", "The claim status in CRM is not Approved.");
						}

					}

					else
					{
						Assert.assertNotNull(title, "Title extracted from result record is NULL.");
					}
				}
			}else{
				Assert.fail("Record count on Portal and CRM is not matching.");
				setAssertMessage("Record count on Portal and CRM is not matching.", 6);
			}

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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 12736- OPT_AD_19- OPT Adjustments Batch Creation - Batch Header validation  Verify clicking 
	 * on save button after entering data in Batch information section.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyBatchHeaderAndSaveBatchFeature(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		String key="OPT_AD_19";
		setup(browser, environment, clientName,"OP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		ObjLoginScreen.logintoOP("OPHRLTPaymentsUser", environment);
		OPHomePage ObjOPHomePage = new OPHomePage(getDriver());
		AdjustmentHomePage objAdjustmentHomePage= ObjOPHomePage.clickOnAdjustmentButton();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		boolean headerVerified= objAdjustmentBatch.validateBatchHeader();
		Assert.assertEquals(headerVerified, true);
		System.out.println("Batch details are verified for key: "+key);
		setAssertMessage("Batch details are verified for key: "+key, 1);
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_batch header before saving");
		}
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch_NHSE("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 2);
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		boolean saveMessage= objAdjustmentBatch.verifySaveBatchMessage();
		SoftAssert softAsert=  new SoftAssert();
		softAsert.assertEquals(saveMessage, true);
		if(saveMessage){
			System.out.println("Save batch message is validated for key: "+key);
			setAssertMessage("Save batch message is validated for key: "+key, 2);
		}else{
			System.out.println("Save batch message is not validated for key: "+key);
			setAssertMessage("Save batch message is not validated for key: "+key, 3);
		}
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_adjustment batch is saved.");
		}
		boolean elementVisible= objAdjustmentBatch.validateBatchNoState();
		softAsert.assertEquals(saveMessage, true);
		if(elementVisible){
			System.out.println("Batch number is disabled");
			setAssertMessage("Batch number is disabled", 4);
		}else{
			System.out.println("Batch number is not disabled");
			setAssertMessage("Batch number is not disabled", 5);
		}
		if(evidence){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_batch button is disabled.");
		}
		boolean isBtnVisible= objAdjustmentBatch.verifyButtonVisibility();
		softAsert.assertEquals(saveMessage, true);
		if(isBtnVisible){
			System.out.println("Update,Search and Bulk Request buttons are displayed");
			setAssertMessage("Update,Search and Bulk Request buttons are displayed", 6);
		}else{
			System.out.println("Update,Search and Bulk Request buttons are not displayed");
			setAssertMessage("Update,Search and Bulk Request buttons are not displayed", 7);
		}
		boolean warningMessage= objAdjustmentBatch.verifyWarningMessage();
		softAsert.assertEquals(saveMessage, true);
		if(warningMessage){
			System.out.println("Warning message is verified.");
			setAssertMessage("Warning message is verified.", 8);
		}else{
			System.out.println("Warning message is not verified.");
			setAssertMessage("Warning message is not verified.", 9);
		}

	}
}
