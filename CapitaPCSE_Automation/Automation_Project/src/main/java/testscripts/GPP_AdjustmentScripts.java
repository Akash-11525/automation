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
import pageobjects.Adjustments.AdjustmentBatch;
import pageobjects.Adjustments.AdjustmentHomePage;
import pageobjects.GPP.GPPaymentHomePage;
import pageobjects.GPP.CI.GPPHomePageNew;
import reporting.ListenerClass;
import utilities.ExcelUtilities;

@Listeners(ListenerClass.class)
public class GPP_AdjustmentScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9938- GPP_AD_19- GPP Adjustments Batch Creation - Batch Header validation  Verify clicking 
	 * on save button after entering data in Batch information section.
	 * 9936- GPP_AD_17- GPP Adjustments Batch Creation - Batch Header validation  Verify the Batch 
	 * Information – Header Details on the Adjustment Batch creation screen.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyBatchHeaderAndSaveBatchFeature(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("GPP_AD_19","GPP_AD_17");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		boolean headerVerified= objAdjustmentBatch.validateBatchHeader();
		Assert.assertEquals(headerVerified, true);
		System.out.println("Batch details are verified for key: "+keys.get(1));
		setAssertMessage("Batch details are verified for key: "+keys.get(1), 1);
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_batch header before saving");
		}
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1, environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		boolean saveMessage= objAdjustmentBatch.verifySaveBatchMessage();
		SoftAssert softAsert=  new SoftAssert();
		softAsert.assertEquals(saveMessage, true);
		if(saveMessage){
			System.out.println("Save batch message is validated for key: "+keys.get(0));
			setAssertMessage("Save batch message is validated for key: "+keys.get(0), 2);
		}else{
			System.out.println("Save batch message is not validated for key: "+keys.get(0));
			setAssertMessage("Save batch message is not validated for key: "+keys.get(0), 3);
		}
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		if(evidence){
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_adjustment batch is saved.");
		}
		boolean elementVisible= objAdjustmentBatch.validateBatchNoState();
		softAsert.assertEquals(elementVisible, true);
		if(elementVisible){
			System.out.println("Batch number is disabled");
			setAssertMessage("Batch number is disabled", 4);
		}else{
			System.out.println("Batch number is not disabled");
			setAssertMessage("Batch number is not disabled", 5);
		}
		if(evidence){
			for(String key: keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_batch button is disabled.");
			}
		}
		boolean isBtnVisible= objAdjustmentBatch.verifyButtonVisibility();
		softAsert.assertEquals(isBtnVisible, true);
		if(isBtnVisible){
			System.out.println("Update,Search and Bulk Request buttons are displayed");
			setAssertMessage("Update,Search and Bulk Request buttons are displayed", 6);
		}else{
			System.out.println("Update,Search and Bulk Request buttons are not displayed");
			setAssertMessage("Update,Search and Bulk Request buttons are not displayed", 7);
		}
		boolean warningMessage= objAdjustmentBatch.verifyWarningMessage();
		softAsert.assertEquals(warningMessage, true);
		if(warningMessage){
			System.out.println("Warning message is verified.");
			setAssertMessage("Warning message is verified.", 8);
		}else{
			System.out.println("Warning message is not verified.");
			setAssertMessage("Warning message is not verified.", 9);
		}

	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9943- GPP_AD_24- GPP Adjustments Batch Creation - Adjustment Records Verify Adjustment records section.
	 * 9949- GPP_AD_34- GPP Adjustments Batch Creation - Verify clicking on Save For Later button.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifySaveForLater(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("GPP_AD_24","GPP_AD_34");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
		boolean isEmptyGrid= objAdjustmentBatch.verifyEmptyGrid();
		Assert.assertEquals(isEmptyGrid, true, "3 empty grids are not present");
		setAssertMessage("3 empty grid are present on UI", 1);
		System.out.println("3 empty grid are present on UI");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_empty grids on UI");
		}
		objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,1,environment,"GPP");// 0 stands for months to be added in current month
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_data fields displayed");
		}
		System.out.println("As values are being entered, all fields are visible for key: "+keys.get(0));
		setAssertMessage("As values are being entered, all fields are visible for key: "+keys.get(0), 2);
		boolean actionButtons= objAdjustmentBatch.verifyActionButtons();
		Assert.assertEquals(actionButtons, true, "Action buttons are not displayed");
		setAssertMessage("Revert to Draft, Save as Draft and Submit buttons are displayed", 3);
		System.out.println("Revert to Draft, Save as Draft and Submit buttons are displayed");
		objAdjustmentBatch= objAdjustmentBatch.clickOnSaveForLater();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(1)+"_record saved as DRAFT");
		}
		boolean gridActionButtons= objAdjustmentBatch.verifyGridActionButtons();
		Assert.assertEquals(gridActionButtons, true, "Grid action buttons are not displayed");
		setAssertMessage("Edit & Delete buttons are displayed", 3);
		System.out.println("Edit & Delete buttons are displayed");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(1)+"_Edit Delete buttons");
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9951- GPP_AD_36- GPP Adjustments Batch Creation - Submit  Verify clicking on Submit button 
	 * on Adjustment Batch creation screen.
	 * 9950- GPP_AD_35- GPP Adjustments Batch Creation - Revert to Draft  Verify clicking on Revert to Draft button.
	 * 9952- GPP_AD_37- GPP Adjustments Batch Creation - Submit  Verify the status after clicking on confirm button on Submission verification pop-up.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifySubmitAndRevertToDraft(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("GPP_AD_36","GPP_AD_35","GPP_AD_37");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,1,environment,"GPP"); // 0 stands for months to be added in current month
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
		Assert.assertEquals(submitMessage, true, "Submit message is not verified");
		setAssertMessage("Submit message is verified for key: "+keys.get(0), 1);
		System.out.println("Submit message is verifiedfor key: "+keys.get(0));
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Adjustment is submitted", 2);
		System.out.println("Adjustment is submitted");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(1)+"_record submitted");
		}
		boolean isStatusUpdated= objAdjustmentBatch.verifyBatchStatus("Pending Approval");
		Assert.assertEquals(isStatusUpdated, true, "Status is not changed after submission");
		setAssertMessage("Status is changed after submission for key: "+keys.get(0), 3);
		System.out.println("Submit message is verifiedfor key: "+keys.get(0));
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(1)+"_updated batch status after submit");
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnRevertToDraftButton();
		boolean revertMessage= objAdjustmentBatch.verifyRevertMessage();
		Assert.assertEquals(revertMessage, true, "Revert message is not verified");
		setAssertMessage("Revert message is verified for key: "+keys.get(1), 4);
		System.out.println("Revert message is verifiedfor key: "+keys.get(1));
		objAdjustmentBatch= objAdjustmentBatch.cancelAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnRevertToDraftButton();
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		boolean isRevert= objAdjustmentBatch.verifyBatchStatus("Draft");
		Assert.assertEquals(isRevert, true, "Status is not changed after revert action");
		setAssertMessage("Status is changed after submission for key: "+keys.get(1), 5);
		System.out.println("Submit message is verifiedfor key: "+keys.get(1));
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(1)+"_updated batch status after revert");
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9945- GPP_AD_26- GPP Adjustments Batch Creation - Bulk Upload  Verify uploading Bulk adjustments.
	 * 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyBulkUpload(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("GPP_AD_26");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		String batchNo= objAdjustmentBatch.getAdjustmentBatchNo();
		System.out.println("Batch number is: "+batchNo);
		List<Integer> rows= Arrays.asList(1,2,3);
		boolean CSVCreated= objAdjustmentBatch.generateGPPAdjCSVFile("GPPAdj",rows,',',2);
		Assert.assertEquals(CSVCreated, true, "File creation has been failed");
		setAssertMessage("CSV file has been created successfully", 1);
		System.out.println("CSV file has been created successfully");
		objAdjustmentBatch= objAdjustmentBatch.uploadFile("GPPValid");
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
		objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
		//objAdjustmentHomePage= objGPPHomePage.clickOnAdjustmentMenuButton();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(keys.get(0)+"_Uploaded records");
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnSaveForLater();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Record has been saved as Draft", 3);
		System.out.println("Record has been saved as Draft");
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9960- GPP_AD_30- GPP Adjustments Batch Creation - Bulk Upload Failure  Verify possible reasons 
	 * for rejection of adjustments after upload.
	 * 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyRejectionReasons(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		String key= "GPP_AD_30";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.uploadFile("GPPInvalidData");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_file uploading progress");
		}
		setAssertMessage("File has been uploaded", 1);
		System.out.println("File has been uploaded");
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBrowseOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnCSVRequestButton();
		boolean isVerified= objAdjustmentBatch.verifyReasonOnFailure("GPP","GPPInvalidData");
		Assert.assertEquals(isVerified, true, "Reason is not matching");
		setAssertMessage("Failure reasons are matching", 2);
		System.out.println("Failure reasons are matching");
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_failure reason");
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnBrowseOKButton();
		objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();
		if(evidence){
			
			objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_Valid uploaded records");
		}
/*		objAdjustmentBatch= objAdjustmentBatch.clickOnSaveForLater();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();*/ //commented on 24th May as these are disabled now
		setAssertMessage("Record has been saved as Draft", 3);
		System.out.println("Record has been saved as Draft");
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9932- GPP_AD_13- GPP Adjustment Batch landing - Search Results Validation  Verify the Action
	 *  dropdown as per the status of batch.
	 *  9934- GPP_AD_15- GPP Adjustment Batch landing - Search Results Validation  Verify clicking on the Apply button.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyAdjustmentActions(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("GPP_AD_13","GPP_AD_15");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		for(int i=0;i<3;i++){
			AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
			objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1, environment,"GPP");
			objAdjustmentBatch= objAdjustmentBatch.saveBatch();
			objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
			objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
			objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,1,environment,"GPP"); // 0 stands for months to be added in current month
			objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
			boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
			Assert.assertEquals(submitMessage, true, "Submit message is not verified");
			setAssertMessage("Submit message is verified for key: "+keys.get(0), 1);
			System.out.println("Submit message is verifiedfor key: "+keys.get(0));
			objAdjustmentBatch= objAdjustmentBatch.confirmAction();
			objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
			setAssertMessage("Adjustment is submitted for index: "+i, 2);
			System.out.println("Adjustment is submitted for index: "+i);
			if(evidence){
				for(String key:keys){
					objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record submitted");
				}
				
			}
			objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
			/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
					objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
			
		}
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Supervisor", environment,GPPHomePageNew.class);
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(1,0,"Pending Approval");
		List<String> actions= Arrays.asList("Approve","Reject","Delete");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForMultipleAdjustments(actions,3,keys.get(1));
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.cancelAction();
		setAssertMessage("Clicked on Cancel button before updating multiple records", 3);
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(keys.get(1)+"_completed actions");
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();
		setAssertMessage("Approve,Reject and Delete actions are performed on adjustment batches", 4);
		System.out.println("Approve,Reject and Delete actions are performed on adjustment batches");
/*		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Rejected");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForAdjustment("Delete");
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			for(String key:keys){
				objAdjustmentHomePage.captureAdjBatchPortalSnap(keys.get(1)+"_deleted rejected claim");
			}
			
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();*/
	}
	
	/***********************************************************************************************************
	 * Akshay Sohoni : - 9973- GPP_AD_54- GPP Adjustments - Bulk Upload Pop Up  Verify the mandatory fields to create 
	 * bulk adjustments
	 * 9982- GPP_AD_63- GPP Adjustments - Bulk Upload Pop Up  Verify clicking on Add to Batch button.
	******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyBulkAdjEntry(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("GPP_AD_54","GPP_AD_63");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
/*		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(5, 5,"Draft");
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();*/
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1, environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		String batchNo= objAdjustmentBatch.getAdjustmentBatchNo();
		System.out.println("Batch number is: "+batchNo);
		setAssertMessage("Adjustment batch is saved", 1);
		System.out.println("Adjustment batch is saved");
		objAdjustmentBatch= objAdjustmentBatch.clickOnEnterBulkAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddToBatchButton();
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_validation messages on Add button");
			}
			
		}
		List<String> actualErrorMessages= objAdjustmentBatch.AcutalErrormessage();
		System.out.println("Actual Error Messages on Add: "+actualErrorMessages);
		setAssertMessage("Actual Error Messages on Add: "+actualErrorMessages, 1);
		List<String> ExpectedErrorMessages = ExcelUtilities.getCellValuesInExcel("AdjustmentTestData.xlsx", "ValidationMessages", 1);
		System.out.println("Expected Error Messages on Add: "+ExpectedErrorMessages);
		List<String> unmatchedList = CommonFunctions.compareStrings(actualErrorMessages, ExpectedErrorMessages);
		SoftAssert softAssertion= new SoftAssert();
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error list on Add click action is matching with expected list.");
			setAssertMessage("Actual error list on Add click action is matching with expected list.", 2);
		}else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Add click action is not matching with Expected Error list.");
		}
		objAdjustmentBatch= objAdjustmentBatch.enterBulkAdjustmentData("AdjustmentTestData.xlsx","BulkAdjData",1,false,true,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddToBatchButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBulkAdjReqOKButton();
		setAssertMessage("Bulk adjustment is saved", 2);
		System.out.println("Bulk adjustment is saved");
		objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0, 0);
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_bulk data added");
			}
		}
		objAdjustmentBatch= objAdjustmentBatch.clickOnSaveForLater();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		if(evidence){
			for(String key:keys){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_bulk adjustment data saved");
			}
			
		}
		setAssertMessage("Bulk adjustment entry is saved", 3);
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9998- GPP_AD_79- GPP Adjustments - Payment Line generation  Verify payment line generation 
	 * for adjustment.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPaymentLineCreation(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException, AWTException{
		String key="GPP_AD_79";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,1,environment,"GPP"); // 0 stands for months to be added in current month
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
		objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
		/*objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
			
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Supervisor", environment,GPPHomePageNew.class);
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Pending Approval");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForAdjustment("Approve",key+"_Approved records");
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_completed action");
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();
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
		List<String> GroupTypeValue= Arrays.asList("fld");
		List<String> FieldValue= Arrays.asList("Adjustment Batch");
		List<String> ConditionValue= Arrays.asList("Equals");
		List<String> ValueType= Arrays.asList("Lookup");
		List<String> ValueForFieldValue= Arrays.asList(batchNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Adjustments");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		
		/*int colNum = 10;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		//String batchNo="201830000312";
		String ValueForFieldValue = batchNo;
		System.out.println("Claim Number:"+ValueForFieldValue);

		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
*/
		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				String clmStatus = objAdvancedFindResult.getDetailsFromResultRecordScreen_GPPAdj();
				if (clmStatus.contains("Approved and Awaiting Payment"))
				{
					setAssertMessage("The batch status in CRM is Approved and Awaiting Payment.", 4);
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
							setAssertMessage("The Payment Line status is Pending.", 5);
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
	 * Akshay Sohoni : - 9999- GPP_AD_80- GPP Adjustments - Payment Line generation  Verify payment line generation
	 * for multiple month adjustment.
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyPLForMultipleMonths(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException, AWTException{
		String key="GPP_AD_80";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1, environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.saveBatch();
		objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
		objAdjustmentBatch= objAdjustmentBatch.clickOnEnterBulkAdjustment();
		objAdjustmentBatch= objAdjustmentBatch.enterBulkAdjustmentData("AdjustmentTestData.xlsx","BulkAdjData",2,true,false,environment,"GPP");
		objAdjustmentBatch= objAdjustmentBatch.clickOnAddToBatchButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		objAdjustmentBatch= objAdjustmentBatch.clickOnBulkAdjReqOKButton();
		int portalRecordCount= objAdjustmentBatch.getRecordCount();
		System.out.println("Record count is: "+portalRecordCount);
		objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0, 0, "Draft");
		objAdjustmentBatch= objAdjustmentHomePage.clickOnFirstRecordFromResult();
		objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
		boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
		Assert.assertEquals(submitMessage, true, "Submit message is not verified");
		setAssertMessage("Submit message is verified for key: "+key, 1);
		System.out.println("Submit message is verifiedfor key: "+key);
		objAdjustmentBatch= objAdjustmentBatch.confirmAction();
		objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
		setAssertMessage("Adjustment is submitted", 1);
		System.out.println("Adjustment is submitted");
		if(evidence){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record submitted");
		}
		String batchNo= objAdjustmentBatch.getAdjustmentBatchNo();
		objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
			
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Supervisor", environment,GPPHomePageNew.class);
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Pending Approval");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForAdjustment("Approve",key+"_Approved records");
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_completed action");
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();
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
		List<String> GroupTypeValue= Arrays.asList("fld");
		List<String> FieldValue= Arrays.asList("Adjustment Batch");
		List<String> ConditionValue= Arrays.asList("Equals");
		List<String> ValueType= Arrays.asList("Lookup");
		List<String> ValueForFieldValue= Arrays.asList(batchNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Adjustments");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		//String batchNo= "201830000314";
		/*int colNum = 10;
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "CLAIMS", "CLAIM1",1);
		//String batchNo="201830000312";
		String ValueForFieldValue = batchNo;
		System.out.println("Batch Number is: "+ValueForFieldValue);

		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
*/
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
	 * Akshay Sohoni : - 9932- GPP_AD_01- GPP- Adjustment batch status  Verify Adjustment Batch statuses.
	 * 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyAdjustmentStatuses(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		String key= "GPP_AD_01";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		GPPHomePageNew objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		AdjustmentHomePage objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		for(int i=0;i<3;i++){
			AdjustmentBatch objAdjustmentBatch= objAdjustmentHomePage.clickOnNewAdjustment();
			objAdjustmentBatch= objAdjustmentBatch.createAdjustmentBatch("AdjustmentTestData.xlsx", "GPPTestData", "BatchName", 1, environment,"GPP");
			objAdjustmentBatch= objAdjustmentBatch.saveBatch();
			objAdjustmentBatch= objAdjustmentBatch.confirmRecordCreation();
			if(evidence){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record saved as Draft");
			}
			objAdjustmentBatch= objAdjustmentBatch.clickOnAddAdjustment();
			objAdjustmentBatch= objAdjustmentBatch.enterGPPAdjustmentData(0,1,environment,"GPP"); // 0 stands for months to be added in current month
			objAdjustmentBatch= objAdjustmentBatch.clickOnSubmit();
			boolean submitMessage= objAdjustmentBatch.verifySubmitMessage();
			Assert.assertEquals(submitMessage, true, "Submit message is not verified");
			setAssertMessage("Submit message is verified for key: "+key, 1);
			System.out.println("Submit message is verifiedfor key: "+key);
			objAdjustmentBatch= objAdjustmentBatch.confirmAction();
			objAdjustmentBatch= objAdjustmentBatch.clickOnOKButton();
			setAssertMessage("Adjustment is submitted for index: "+i, 2);
			System.out.println("Adjustment is submitted for index: "+i);
			if(evidence){
				objAdjustmentBatch.captureAdjBatchPortalSnap(key+"_record submitted_"+i);
			}
			objAdjustmentHomePage= objGPPHomePageNew.ClickonBreadcrumbTab("Adjustments", AdjustmentHomePage.class);
			/*		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
					objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();*/
			
		}
		tearDown(browser);
		Thread.sleep(3000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Supervisor", environment,GPPHomePageNew.class);
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objAdjustmentHomePage= objGPPaymentHomePage.ClickOnAdjustmentTab();
		objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,"Pending Approval");
		if(evidence){
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_Pending Approval Status");
		}
		List<String> actions= Arrays.asList("Approve","Reject","Delete");
		objAdjustmentHomePage= objAdjustmentHomePage.changeStatusForMultipleAdjustments(actions,3,key);
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.cancelAction();
		setAssertMessage("Clicked on Cancel button before updating multiple records", 3);
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnApplyButton();
		objAdjustmentHomePage= objAdjustmentHomePage.confirmAction();
		if(evidence){
			
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_completed actions");
		}
		objAdjustmentHomePage= objAdjustmentHomePage.clickOnOKButton();
		setAssertMessage("Approve,Reject and Delete actions are performed on adjustment batches", 4);
		System.out.println("Approve,Reject and Delete actions are performed on adjustment batches");
		List<String> filterStatus= Arrays.asList("Approved","Rejected");
		for(int i=0;i<filterStatus.size();i++){
			String status= filterStatus.get(i%filterStatus.size());
			objAdjustmentHomePage= objAdjustmentHomePage.searchBatch(0,0,status);
			objAdjustmentHomePage.captureAdjBatchPortalSnap(key+"_"+status+" status");
		}
		setAssertMessage("All the statuses are verified", 5);
	}

}
