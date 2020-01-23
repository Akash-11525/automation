package testscripts;

import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
import pageobjects.GPP.ClaimsHomePage;
import pageobjects.GPP.GPPaymentHomePage;
//import pageobjects.GPP.CI.GPPHomePage;
import pageobjects.GPP.CI.GPPHomePageNew;
import pageobjects.GPP.SC.LocumPreApprovalClaim;
import pageobjects.GPP.SC.SC_LocumCostClaim;
import pageobjects.GPP.SC.SC_PremisesClaim;
import pageobjects.GPP.SC.SC_RegistrarExpenseClaim;
import pageobjects.GPP.SC.SC_RetainerSessionClaim;
import pageobjects.GPP.SC.StdClaimsApprovalWindow;
import pageobjects.GPP.SC.StdClaimsPortal;
import pageobjects.GPP.SC.StdClaimsPreApprovalPortal;
import pageobjects.GPP.SC.StdClaimsPreApprovalWindow;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class GPP_StandardClaimScripts extends BaseClass {
	
	// Generic test cases started
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9733- This is Regression Test Case - GPP_SC_02. Standard claims view screen (GP Practice) - 
	 * Select new claim type Verify the functionality of select new claim type dropdown
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void validateClaimTypesFromPortal(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException{
		String key= "GPP_SC_02"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		boolean isTypeVerified= objStdClaimsPortal.validateClaimTypes(1);
		Assert.assertEquals(isTypeVerified, true, "Claim types are mismatching.");
		setAssertMessage("Claim types are verified for key: "+key, 1);
	}
	
	//pre approval Locum test scripts started
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9782- This is Regression Test Case - GPP_SC_50. Locum cost pre-approval application form screen
	 *  - Submit Verify the functionality of Submit button
	 *  9761-GPP_SC_29- Pre-approval view screen (GP Practice) - Revert to draft Verify the Revert to draft functionality
	 *  9736- GPP_SC_05- Standard claims view screen (GP Practice) - Apply for locum pre-approval Verify the functionality 
	 *  of apply for locum pre-approval button
	 	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void submitPreApprovalClaim(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, ParseException{
		String key1= "GPP_SC_50";
		String key2= "GPP_SC_29";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(1,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(1);
		/*StdClaimsApprovalWindow objStdClaimsApprovalWindow= objStdClaimsPreApprovalPortal.clickOnClaimApproval();*/
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key1);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key1);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key1, 1);

		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key1+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String claimStatus= objStdClaimsPreApprovalPortal.verifyRevertToDraftClaim(claimNo);
		
		assertEquals(claimStatus, "Draft");
		
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key2+"_Claim Reverted_"+claimNo);
		}
		objStdClaimsPreApprovalPortal.getClaimNumber(key2);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been reverted to "+claimStatus+ " for key "+key2);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been reverted to "+claimStatus+ " for key "+key2, 2);
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9776- This is Regression Test Case - GPP_SC_44. Locum cost pre-approval application form screen 
	 * - Browse Verify the functionality of file attachments
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void validateFileFormatsForPreApproval(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_44"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		boolean allUploaded= objLocumPreApprovalClaim.validateFileFormatAndClaimData(2,environment);
		SoftAssert softAssert= new SoftAssert();
		if(allUploaded){
			System.out.println("All desired file formats are uploaded successfully for key: "+key);
			setAssertMessage("All desired file formats are uploaded successfully for key: "+key, 1);
		}else{
			System.out.println("All desired file formats are not uploaded successfully for key: "+key);
			softAssert.assertEquals(allUploaded, true,"Expected and Actual file count is not matching");
		}
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSaveForLaterButton(2);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumberWithClaimStatus(key, "Draft");
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);

		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_Claim Status as Draft_"+claimNo);
		}
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatusByValue("Draft");
		softAssert.assertEquals(claimStatus, "Draft");
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been saved with multiple files for key "+key, 2);
		objStdClaimsPreApprovalPortal= objStdClaimsPreApprovalPortal.searchStdClaims("Draft");
		objStdClaimsPreApprovalPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objLocumPreApprovalClaim.capturePreApprovalClaimPortalSnap(key+"_attached file types_"+claimNo);
		}
		softAssert.assertAll();	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9781- This is Regression Test Case - GPP_SC_49. Locum cost pre-approval application form 
	 * screen - Cancel Verify the functionality of Cancel button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyCancelPreApprovalClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_49"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.cancelPreApprovalClaim(3,environment);
		System.out.println("Premises claim has been canceled");
		setAssertMessage("Premises claim has been canceled", 1);

		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApprovalClaim Canceled");
		}
			
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9793- This is Regression Test Case - GPP_SC_61. Approval/Rejection of pre-approved claims - 
	 * Approve Button Verify the functionality of Approve button
	 * 9764- GPP_SC_36- Locum cost pre-approval application form screen - Contractor code Verify the Contractor Code on the Locum 
	 * cost pre-approval application form
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyApprovePreApprovalClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_61"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(4,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(4);
		/*StdClaimsApprovalWindow objStdClaimsApprovalWindow= objStdClaimsPreApprovalPortal.clickOnClaimApproval();*/
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);

		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",4);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Claim status for "+claimNo+" is "+claimStatus);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9795- This is Regression Test Case - GPP_SC_62. Approval/Rejection of pre-approved claims - 
	 * Reject button Verify the functionality of Reject button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyRejectPreApprovalClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_62"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(5,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(5);
		/*StdClaimsApprovalWindow objStdClaimsApprovalWindow= objStdClaimsPreApprovalPortal.clickOnClaimApproval();*/
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);

		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.rejectLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",5);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_RejectedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Rejected");
		System.out.println("Claim status for "+claimNo+" is "+claimStatus);
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been rejected", 1);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9757- This is Regression Test Case - GPP_SC_25. Pre-approval view screen (GP Practice)  - 
	 * Search Verify the search button functionality
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
		public void searchPreApprovalClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException{
			String key= "GPP_SC_25";
			
			setup(browser, environment, clientName,"GPP");
			LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
			SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
			GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
			GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
			ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
			StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
			StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
			objStdClaimsPreApprovalPortal.verifySearchResult();
	
			if(evidence)
			{
				objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApprovalClaim searched");
			}
			setAssertMessage("Searching of records for pre approval claim is completed", 1);
			
		}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9779- This is Regression Test Case - GPP_SC_47. Locum cost pre-approval application form screen 
	 * - Tick to confirm Verify the functionality of tick to confirm box
	 	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyFieldValidationsForPreApproval(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_47";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createPreApprovalWithoutFieldValidations(6,environment);
		if(evidence)
		{
			objLocumPreApprovalClaim.captureValidationMessages(key+"_ErrorOnPreAppClaim_WithoutFieldValidations");
		}
		List<String> ActualErrorMessagesonSave = objLocumPreApprovalClaim.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("GPPSCTESTDATA.xlsx", "ValidationErrors", 2);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list.");
		}


	}
	// Std claim generic script for search functionality
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9743- This is Regression Test Case - GPP_SC_12. Standard claims view screen (GP Practice) -
	 *  Search Verify the search button functionality
	 	*******************************************************************************************************************/
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
		@Parameters({ "browser","environment", "clientName", "evidence" })
		
		public void searchStdClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException{
			String key= "GPP_SC_12";
			
			setup(browser, environment, clientName,"GPP");
			LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
			SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
			GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
			GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
			ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
			StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
			objStdClaimsPortal.verifySearchResult();
	
			if(evidence)
			{
				objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PreApprovalClaim searched");
			}
			setAssertMessage("Searching of records for standard claim is completed", 1);
			
		}
	
	// Locum cost cover claim scripts started
		/***********************************************************************************************************
	 * Akshay Sohoni : - 9824- This is Regression Test Case - GPP_SC_90. Locum cost claim form screen - Submit Verify 
	 * the functionality of submit button
	 * 9899- GPP_SC_165- Approval/Rejection of standard claims - Approve Button Verify the functionality of Approve button
		 * @throws ParseException 
	 	******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void submitAndApproveLocumCoverCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_90";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(1,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(1);
		/*StdClaimsApprovalWindow objStdClaimsApprovalWindow= objStdClaimsPreApprovalPortal.clickOnClaimApproval();*/
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);

		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",1);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Pre approval claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.createLocumCostClaimApplication(1);
		objSC_LocumCostClaim.clickOnSubmitButton(1);
		String LocumClaimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key);
		setAssertMessage("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key, 2);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumClaim_PortalDetails_"+LocumClaimNo);
		}
		String LocumClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(LocumClaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		objStdClaimsApprovalWindow.approveStdClaim(LocumClaimNo,"LocumCostClaimData",1);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		String finalClaimAndPLStatus= objStdClaimsPortal.getClaimStatus();
		String statusArray[]= finalClaimAndPLStatus.split("-");
		String finalClaimStatus= statusArray[0].toString().trim();
		assertEquals(finalClaimStatus, "Approved");
		System.out.println("Claim status is "+claimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_Approved_LocumCostClaims_"+LocumClaimNo);
		}
		setAssertMessage("Locum cost Claim "+LocumClaimNo+" is approved", 3);
		
		// To verify status of SC claim submitted in CRM.
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
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
		String expclaimStatusCRM = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "EXPCLAIMSTATUS");
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
				if (clmStatus.contains(expclaimStatusCRM))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus_GPPSC(ValueForFieldValue, evidence,key+"_ClaimStatus_"+claimNo);
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key+"_PaymentLineStatus_"+claimNo, key+"_GMPAmountDue_"+claimNo);
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
					Assert.assertEquals(clmStatus, finalClaimStatus, "The claim status in CRM is not Approved.");
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
	
		/***********************************************************************************************************
	 * Akshay Sohoni : - 9900- This is Regression Test Case - GPP_SC_166. Approval/Rejection of standard claims - 
	 * Reject Button Verify the functionality of Reject button
		 * @throws ParseException 
	 	******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyRejectLocumCoverCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_166";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(2,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(2);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
	
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",2);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Locum cost claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.createLocumCostClaimApplication(2);
		objSC_LocumCostClaim.clickOnSubmitButton(2);
		String LocumClaimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key);
		setAssertMessage("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key, 2);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumClaim_PortalDetails_"+LocumClaimNo);
		}
		String LocumClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(LocumClaimStatus, "Pending");
	
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		objStdClaimsApprovalWindow.rejectStdClaim(LocumClaimNo,"LocumCostClaimData",2);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		
		String finalClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(finalClaimStatus, "Rejected");
		System.out.println("Claim status is "+finalClaimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_Rejected_LocumCostClaims_"+LocumClaimNo);
		}
		setAssertMessage("Locum cost Claim is rejected", 3);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/***********************************************************************************************************
 * Akshay Sohoni : - 9816- This is Regression Test Case - GPP_SC_82. Locum cost claim form - Browse Verify the 
 * functionality of browse
 * 9823- GPP_SC_89- Locum cost claim form screen - Save for Later Verify the functionality of Save for Later
	 * @throws ParseException 
 	******************************************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void validateFileFormatsForLocumCoverCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_82";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(3,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(3);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
	
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",3);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Locum cost claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		boolean allUploaded= objSC_LocumCostClaim.validateFileFormatAndClaimData(3);
		SoftAssert softAssert= new SoftAssert();
		if(allUploaded){
			System.out.println("All desired file formats are uploaded successfully for key: "+key);
			setAssertMessage("All desired file formats are uploaded successfully for key: "+key, 2);
		}else{
			System.out.println("All desired file formats are not uploaded successfully for key: "+key);
			softAssert.assertEquals(allUploaded, true,"Expected and Actual file count is not matching");
		}
		objStdClaimsPortal= objSC_LocumCostClaim.clickOnSaveForLaterButton(3);
		String LocumClaimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 3, "LocumCostClaimData",true);
		System.out.println("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key);
		setAssertMessage("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key, 3);
	
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumCostClaim Status as Draft_"+LocumClaimNo);
		}
		String finalClaimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "LocumCostClaimData", 3);
		softAssert.assertEquals(finalClaimStatus, "Draft");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
		setAssertMessage("Locum cost claim no " +LocumClaimNo+ " has been saved with multiple files for key "+key, 3);
		softAssert.assertAll();
	}
	
	/***********************************************************************************************************
 * Akshay Sohoni : - 9822- This is Regression Test Case - GPP_SC_88. Locum cost claim form screen - Cancel Verify 
 * the functionality of Cancel button
	 * @throws ParseException 
 	******************************************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyCancelLocumCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_88";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(4,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(4);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
	
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",4);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Locum cost claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.cancelLocumCostClaim(4);
		System.out.println("Locum cost claim has been canceled");
		setAssertMessage("Locum cost claim has been canceled", 1);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumCostClaim Canceled");
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/***********************************************************************************************************
 * Akshay Sohoni : - 9746- This is Regression Test Case - GPP_SC_15. Standard claims view screen (GP Practice) - 
 * Revert to Draft Verify user is able to update the Claim details after reverting the claim
	 * @throws ParseException 
 	******************************************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void revertToDraftLocumCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_15";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(5,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(5);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
	
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",5);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Locum cost claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.createLocumCostClaimApplication(5);
		objSC_LocumCostClaim.clickOnSubmitButton(5);
		String LocumClaimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key);
		setAssertMessage("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key, 2);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumClaim_PortalDetails_"+LocumClaimNo);
		}
		String LocumClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(LocumClaimStatus, "Pending");
		String finalClaimStatus= objStdClaimsPortal.verifyRevertToDraftClaim(LocumClaimNo);
		
		Verify.verifyEquals(finalClaimStatus, "Draft","Expected is Pending but actual is "+finalClaimStatus);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumCostClaim Reverted_"+LocumClaimNo);
		}
		objStdClaimsPortal.getClaimNumber(key);
		System.out.println("Locum Cost Claim no " +LocumClaimNo+ " has been reverted to "+finalClaimStatus+ " for key "+key);
		setAssertMessage("Locum CostClaim no " +LocumClaimNo+ " has been reverted to "+finalClaimStatus+ " for key "+key, 2);
		objStdClaimsPortal.clickOnClaimNumber(LocumClaimNo);
		objSC_LocumCostClaim.updateClaimDetails(5);
		objStdClaimsPortal= objSC_LocumCostClaim.clickOnSubmitButton(5);
		System.out.println("Locum CostClaim no " +LocumClaimNo+ " has been updated for key "+key);
		setAssertMessage("Locum Cost Claim no " +LocumClaimNo+ " has been updated for key "+key, 1);
		String claimStatus2= objStdClaimsPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumCostClaim_UpdatedAfterRevert_"+LocumClaimNo);
		}
		Assert.assertEquals(claimStatus2, "Pending", "Expected is Pending but actual is "+claimStatus2);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}

/***********************************************************************************************************
* Akshay Sohoni : - 9748- GPP_SC_16- Standard claims view screen (GP Practice) - Delete Verify the functionality of delete button
 * @throws ParseException 
	******************************************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyDeleteLocumCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_16";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(5,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(5);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
	
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",5);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Locum cost claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.createLocumCostClaimApplication(5);
		objStdClaimsPortal= objSC_LocumCostClaim.clickOnSaveForLaterButton(5);
		String LocumClaimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 5, "LocumCostClaimData",true);
		System.out.println("Locum cost claim no " +LocumClaimNo+ " has been created for key "+key);
	
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumCostClaim_Before Delete_"+LocumClaimNo);
		}
		String finalClaimStatus= objStdClaimsPortal.getClaimStatusByValues(key, "Draft", "LocumCostClaimData", 5);
		Verify.verifyEquals(finalClaimStatus, "Draft");
		objStdClaimsPortal.verifyDeleteClaim("LocumCostClaimData",5,LocumClaimNo,key,true);
		setAssertMessage("Locum cost claim no " +LocumClaimNo+ " has been deleted", 2);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumCostClaim_After Delete_"+LocumClaimNo);
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}

	/***********************************************************************************************************
	* Akshay Sohoni : - 9835- This is Regression Test Case - GPP_SC_91. Locum cost claim form screen - Generation
	*  of claim ID Verify the generation of claim ID
	 * @throws ParseException 
		******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyClaimFormatForLocumCoverCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_91";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(1,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(1);
		/*StdClaimsApprovalWindow objStdClaimsApprovalWindow= objStdClaimsPreApprovalPortal.clickOnClaimApproval();*/
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
		
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",1);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Pre approval claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.createLocumCostClaimApplication(1);
		objSC_LocumCostClaim.clickOnSubmitButton(1);
		String LocumClaimNo= objStdClaimsPortal.getClaimNumber(key);
		boolean isFormatVerified= objStdClaimsPortal.verifyClaimNoFormat(LocumClaimNo);
		Assert.assertEquals(isFormatVerified, true, "Claim Number format is not as per expected one");
		System.out.println("Standard Claim number format verification status is "+isFormatVerified);
		setAssertMessage("Standard Claim number format verification status is "+isFormatVerified, 2);
		System.out.println("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key);
		setAssertMessage("For pre-approval "+claimNo +", Locum cost claim no " +LocumClaimNo+ " has been created for key "+key, 2);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_LocumClaim_PortalDetails_"+LocumClaimNo);
		}
		String LocumClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(LocumClaimStatus, "Pending");
		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/***********************************************************************************************************
 * Akshay Sohoni : - 9822- This is Regression Test Case - GPP_SC_88. Locum cost claim form screen - Cancel Verify 
 * the functionality of Cancel button
	 * @throws ParseException 
 	******************************************************************************************************************/

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyFieldValidationsForLocumCostClaim(String browser, String environment, String clientName, boolean evidence ) throws 
	InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_88";
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		LocumPreApprovalClaim objLocumPreApprovalClaim= objStdClaimsPortal.clickOnApplyPreApprovalClaimButton();
		objLocumPreApprovalClaim= objLocumPreApprovalClaim.createLocumPreApprovalClaimApplication(7,environment);
		StdClaimsPreApprovalPortal objStdClaimsPreApprovalPortal= objLocumPreApprovalClaim.clickOnSubmitButton(7);
		String claimNo= objStdClaimsPreApprovalPortal.getClaimNumber(key);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);
		
		setAssertMessage("Locum pre-approval claim no " +claimNo+ " has been created for key "+key, 1);
	
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ClaimDetails_"+claimNo);
		}
		
		String preAppclaimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		System.out.println("Pre approval claim status is "+preAppclaimStatus);
		
		assertEquals(preAppclaimStatus, "Pending");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		StdClaimsPreApprovalWindow objStdClaimsPreApprovalWindow= objStdClaimsApprovalWindow.clickOnPreApprovalTab();
		objStdClaimsPreApprovalWindow.approveLocumPreAppClaim(claimNo,"LocumPreApprovalClaimData",7);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		objStdClaimsPreApprovalPortal= objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		String claimStatus= objStdClaimsPreApprovalPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPreApprovalPortal.capturePreApprovalClaimPortalSnap(key+"_PreApproval_Portal_ApprovedClaimDetails_"+claimNo);
		}
		assertEquals(claimStatus, "Approved");
		System.out.println("Locum cost claim status for "+claimNo+" is "+claimStatus);
		System.out.println("********pre approval claim is approved*********");
		objStdClaimsPortal.clickOnPreApprovalClaimPortalTab();
		SC_LocumCostClaim objSC_LocumCostClaim= objStdClaimsPreApprovalPortal.clickOnClaimButton();
		objSC_LocumCostClaim.createLocumCostClaimWithoutFieldValidations(7);
		
		if(evidence)
		{
			objSC_LocumCostClaim.captureValidationMessages(key+"_ErrorOnLocumCostClaim_WithoutFieldValidations");
		}
		List<String> ActualErrorMessagesonSave = objSC_LocumCostClaim.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("GPPSCTESTDATA.xlsx", "ValidationErrors", 2);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list.");
		}
	}


	// Registrar claim scripts started
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9846- This is Regression Test Case - GPP_SC_112. Registrar Claims  - Submit Verify 
	 * the Submit functionality
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void submitRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_112"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(1);
		objSC_RegistrarExpenseClaim= objSC_RegistrarExpenseClaim.createRegistrarClaim(1,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(1);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureStdClaimPortalSnap(key+"_RegistrarClaim_EnteredData_"+claimNo);
		}
					
	}
	
	
	/***********************************************************************************************************
	 * Akshay Sohoni : - 9842- This is Regression Test Case - GPP_SC_108. Registrar Claims  - Registrar Expense Claim - 
	 * Tick  to confirm Verify the tick to confirm field in Declaration area
	 * @throws ParseException 
	 	******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyFieldValidationsForRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_108"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(2);
		objSC_RegistrarExpenseClaim= objSC_RegistrarExpenseClaim.createRegistrarClaimWithoutFieldValidations(2,environment);
				
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureValidationMessages(key+"_ErrorOnRegistrarClaim_WithoutFieldValidations");
		}
		List<String> ActualErrorMessagesonSave = objSC_RegistrarExpenseClaim.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("GPPSCTESTDATA.xlsx", "ValidationErrors", 1);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list.");
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9899- This is Regression Test Case - GPP_SC_165. Approval/Rejection of standard claims - 
	 * Approve Button Verify the functionality of Approve button
	 * 9846 -GPP_SC_112 Registrar Claims  - Submit Verify the Submit functionality
	 * 9734- GPP_SC_03- Standard claims view screen (GP Practice) - Create new claim Verify the functionality of create a new claim button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyApproveRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_165"; 
		String key2= "GPP_SC_112";
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(3);
		objSC_RegistrarExpenseClaim= objSC_RegistrarExpenseClaim.createRegistrarClaim(3,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(3);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureStdClaimPortalSnap(key+"_RegistrarClaim_EnteredData_"+claimNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Clerk();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("PCSEClerk");
		objStdClaimsApprovalWindow.approveStdClaim(claimNo,"RegistrarExpenseClaimData",3);
		objStdClaimsPortal= objStdClaimsApprovalWindow.clickOnClaimPortal();
		
		String finalClaimAndPLStatus= objStdClaimsPortal.getClaimStatus();
		String statusArray[]= finalClaimAndPLStatus.split("-");
		String finalClaimStatus= statusArray[0].toString().trim();
		assertEquals(finalClaimStatus, "Approved");
		System.out.println("Claim status is "+claimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_Approved_RegistrarClaims_"+claimNo);
		}
		setAssertMessage("Claim approved", 1);
		quit(browser);
		
		// To verify status of SC claim submitted in CRM.
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
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
		String expclaimStatusCRM = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "EXPCLAIMSTATUS");
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
				if (clmStatus.contains(expclaimStatusCRM))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus_GPPSC(ValueForFieldValue, evidence,key2+"_ClaimStatus_"+claimNo);
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key2+"_PaymentLineStatus_"+claimNo, key2+"_GMPAmountDue_"+claimNo);
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
					Assert.assertEquals(clmStatus, finalClaimStatus, "The claim status in CRM is not Approved.");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9900- This is Regression Test Case - GPP_SC_166. Approval/Rejection of standard claims - Reject 
	 * Button Verify the functionality of Reject button
	 * 9846- Registrar Claims  - Submit Verify the Submit functionality
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyRejectRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_166"; 
			
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(4);
		objSC_RegistrarExpenseClaim= objSC_RegistrarExpenseClaim.createRegistrarClaim(4,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(4);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureStdClaimPortalSnap(key+"_RegistrarClaim_EnteredData_"+claimNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP("GPP - PCSE Payments Clerk", environment,GPPHomePageNew.class);
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Clerk();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("PCSEClerk");
		objStdClaimsApprovalWindow.rejectStdClaim(claimNo,"RegistrarExpenseClaimData",4);
		objStdClaimsPortal= objStdClaimsApprovalWindow.clickOnClaimPortal();
		
		String finalClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(finalClaimStatus, "Rejected");
		System.out.println("Claim status is "+finalClaimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_Rejected_RegistrarClaims_"+claimNo);
		}
		setAssertMessage("Registrar Claim number "+claimNo+" is rejected", 2);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9834- This is Regression Test Case - GPP_SC_104. Registrar Expense Claim - Browse button Verify 
	 * the Browse button on the  Registrar Expense Claim page
	 * 9845- This is Regression Test Case - GPP_SC_104. Registrar Claims  - Save for Later  Verify the Save for Later functionality
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void validateFileFormatsForRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_104"; 
		String key2= "GPP_SC_111";
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(5);
		boolean allUploaded= objSC_RegistrarExpenseClaim.validateFileFormatAndClaimData(5,environment);
		SoftAssert softAssert= new SoftAssert();
		if(allUploaded){
			System.out.println("All desired file formats are uploaded successfully for key: "+key);
			setAssertMessage("All desired file formats are uploaded successfully for key: "+key, 1);
		}else{
			System.out.println("All desired file formats are not uploaded successfully for key: "+key);
			softAssert.assertEquals(allUploaded, true,"Expected and Actual file count is not matching");
		}
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureStdClaimPortalSnap(key+"_RegistrarClaim_EnteredData");
		}
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSaveForLaterButton(5);
		String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 5, "RegistrarExpenseClaimData",true);
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key2+"_RegistrarClaim Status as Draft_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "RegistrarExpenseClaimData", 5);
		softAssert.assertEquals(claimStatus, "Draft");
		setAssertMessage("Registrar claim no " +claimNo+ " has been saved with multiple files for key "+key, 2);
		softAssert.assertAll();	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9844- This is Regression Test Case - GPP_SC_110. Registrar Expense Claim  - Cancel Button 
	 * Verify the Cancel Button for retainer claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyCancelRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_110"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(6);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.cancelRegistrarClaim(6,environment);
		System.out.println("Registrar claim has been canceled");
		setAssertMessage("Registrar claim has been canceled", 1);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim Canceled");
		}
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9745- This is Regression Test Case - GPP_SC_14. Standard claims view screen (GP Practice) - 
	 * Revert to Draft Verify the Revert to Draft functionality
	 * 9746- GPP_SC_15- Standard claims view screen (GP Practice) - Revert to Draft Verify user is able to update the 
	 * Claim details after reverting the claim
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void revertToDraftRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_14"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(7);
		objSC_RegistrarExpenseClaim= objSC_RegistrarExpenseClaim.createRegistrarClaim(7,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(7);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		Verify.verifyEquals(claimStatus, "Pending", "Expected is Pending but actual is "+claimStatus);
		
		String finalClaimStatus= objStdClaimsPortal.verifyRevertToDraftClaim(claimNo);
		
		Verify.verifyEquals(finalClaimStatus, "Draft","Expected is Draft but actual is "+finalClaimStatus);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim Reverted_"+claimNo);
		}
		objStdClaimsPortal.getClaimNumber(key);
		System.out.println("Registrar claim no " +claimNo+ " has been reverted to "+finalClaimStatus+ " for key "+key);
		
		setAssertMessage("Registrar claim no " +claimNo+ " has been reverted to "+finalClaimStatus+ " for key "+key, 2);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		objSC_RegistrarExpenseClaim.updateClaimDetails(7);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(7);
		System.out.println("Registrar claim no " +claimNo+ " has been updated for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been updated for key "+key, 1);
		String claimStatus2= objStdClaimsPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_UpdatedAfterRevert_"+claimNo);
		}
		Assert.assertEquals(claimStatus2, "Pending", "Expected is Pending but actual is "+claimStatus2);
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus2);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureStdClaimPortalSnap(key+"_RegistrarClaim_EnteredData_"+claimNo);
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9748- This is Regression Test Case - GPP_SC_16. Standard claims view screen (GP Practice) - 
	 * Delete Verify the functionality of delete button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyDeleteRegistrarClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_16"; 
			
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(8);
		objSC_RegistrarExpenseClaim= objSC_RegistrarExpenseClaim.createRegistrarClaim(8,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSaveForLaterButton(8);
		String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 8, "RegistrarExpenseClaimData",true);
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_Before Delete_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "RegistrarExpenseClaimData", 8);
		Verify.verifyEquals(claimStatus, "Draft");
		objStdClaimsPortal.verifyDeleteClaim("RegistrarExpenseClaimData",8,claimNo,key,true);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_After Delete_"+claimNo);
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9744- This is Regression Test Case - GPP_SC_13. Standard claims view screen (GP Practice) - 
	 * Result Area Verify the fields displayed under Results area in standard claim view
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyRegistrarClaimDataWithGrid(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException{
		String key= "GPP_SC_13";
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RegistrarExpenseClaim objSC_RegistrarExpenseClaim= objStdClaimsPortal.clickToCreateRegistrarClaim(9);
		List<String> formData= objSC_RegistrarExpenseClaim.createVerifyRegistrarClaimData(9,environment);
		objStdClaimsPortal= objSC_RegistrarExpenseClaim.clickOnSubmitButton(9);
		boolean isDataVerified= objStdClaimsPortal.verifyGridData(formData);
		System.out.println("Data verification status is: "+isDataVerified);
		Assert.assertEquals(isDataVerified, true, "Grid data verification has failed");
		setAssertMessage("Data under Standard Claims portal grid is verified", 1);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created", 2);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RegistrarClaim_PortalDetails_"+claimNo);
		}
		objStdClaimsPortal.verifySortingFunctionality(key);
		setAssertMessage("Sorting of records is completed", 3);

		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims();
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RegistrarExpenseClaim.captureStdClaimPortalSnap(key+"_RegistrarClaim_EnteredData_"+claimNo);
		}
		boolean elementsDisabled= objSC_RegistrarExpenseClaim.verifyElementState();
		Verify.verifyEquals(elementsDisabled, true, "Elements are not disabled state");
		System.out.println("Disability status for element is: "+elementsDisabled);
		setAssertMessage("Disability status for element is: "+elementsDisabled, 4);
		
		boolean isDateValid= objSC_RegistrarExpenseClaim.validateDateFormat();
		System.out.println("Date validation value is: "+isDateValid);
		setAssertMessage("Date validation value is: "+isDateValid, 5);
		Verify.verifyEquals(isDateValid, true, "Date format is not valid");
		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
			
	}
	
	//Retainer claim scripts started
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9868- This is Regression Test Case - GPP_SC_134. Retainer Claims  - Submit Verify the 
	 * Submit button for retainer claim page
	 * 9899- GPP_SC_165- Approval/Rejection of standard claims - Approve Button Verify the functionality of Approve button
	 * 9734- GPP_SC_03- Standard claims view screen (GP Practice) - Create new claim Verify the functionality of create a new claim button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifySubmitApproveRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_134"; 
		String key2= "GPP_SC_165";
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(1);
		objSC_RetainerSessionClaim= objSC_RetainerSessionClaim.createRetainerClaim(1,environment);
		System.out.println("Data entered");
		objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSubmitButton(1);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RetainerSessionClaim.captureStdClaimPortalSnap(key+"_RetainerClaim_EnteredData_"+claimNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		objStdClaimsApprovalWindow.approveStdClaim(claimNo,"RetainerClaimData",1);
		//objStdClaimsPortal= objStdClaimsApprovalWindow.clickOnClaimPortal();
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		String finalClaimAndPLStatus= objStdClaimsPortal.getClaimStatus();
		String statusArray[]= finalClaimAndPLStatus.split("-");
		String finalClaimStatus= statusArray[0].toString().trim();
		assertEquals(finalClaimStatus, "Approved");
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key2+"_Approved_RetainerClaims_"+claimNo);
		}
		setAssertMessage("Retainer Claim number "+claimNo+" is approved", 2);
		quit(browser);
		
		// To verify status of SC claim submitted in CRM.
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
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
		String expclaimStatusCRM = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "EXPCLAIMSTATUS");
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
				if (clmStatus.contains(expclaimStatusCRM))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus_GPPSC(ValueForFieldValue, evidence,key2+"_ClaimStatus_"+claimNo);
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key2+"_PaymentLineStatus_"+claimNo, key2+"_GMPAmountDue_"+claimNo);
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
					Assert.assertEquals(clmStatus, finalClaimStatus, "The claim status in CRM is not Approved.");
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
	
	
		/***********************************************************************************************************
		 * Akshay Sohoni : - 9860- This is Regression Test Case - GPP_SC_126. Retainer Claims - Browse button Verify the
		 *  Browse button on the  Retainer Claim page
		 * @throws ParseException 
	 	*******************************************************************************************************************/
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
		@Parameters({ "browser","environment", "clientName", "evidence" })
		
		public void validateFileFormatsForRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
			String key= "GPP_SC_126"; 
			
			setup(browser, environment, clientName,"GPP");
			LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
			SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
			GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
			GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
			ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
			StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
			SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(2);
			boolean allUploaded= objSC_RetainerSessionClaim.validateFileFormatAndClaimData(2,environment);
			SoftAssert softAssert= new SoftAssert();
			if(allUploaded){
				System.out.println("All desired file formats are uploaded successfully for key: "+key);
				setAssertMessage("All desired file formats are uploaded successfully for key: "+key, 1);
			}else{
				System.out.println("All desired file formats are not uploaded successfully for key: "+key);
				softAssert.assertEquals(allUploaded, true,"Expected and Actual file count is not matching");
			}
			if(evidence)
			{
				objSC_RetainerSessionClaim.captureStdClaimPortalSnap(key+"_RetainerClaim_EnteredData");
			}
			objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSaveForLaterButton(2);
			String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 2, "RetainerClaimData",true);
			System.out.println("Retainer claim no " +claimNo+ " has been created for key "+key);
			if(evidence)
			{
				objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim Status as Draft_"+claimNo);
			}
			String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "RetainerClaimData", 2);
			softAssert.assertEquals(claimStatus, "Draft");
			System.out.println("Retainer claim no " +claimNo+ " has been saved with multiple files for key "+key);
			setAssertMessage("Retainer claim no " +claimNo+ " has been saved with multiple files for key "+key, 2);
			softAssert.assertAll();			
		}
	

	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9900- This is Regression Test Case - GPP_SC_166. Approval/Rejection of standard claims - Reject 
	 * Button Verify the functionality of Reject button
	 * 9868- This is Regression Test Case - GPP_SC_134. Retainer Claims  - Submit Verify the 
	 * Submit button for retainer claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyRejectRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_166"; 
			
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(3);
		objSC_RetainerSessionClaim= objSC_RetainerSessionClaim.createRetainerClaim(3,environment);
		objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSubmitButton(3);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RetainerSessionClaim.captureStdClaimPortalSnap(key+"_RetainerClaim_EnteredData_"+claimNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		objStdClaimsApprovalWindow.rejectStdClaim(claimNo,"RetainerClaimData",3);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		String finalClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(finalClaimStatus, "Rejected");
		System.out.println("Claim status is "+finalClaimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_"+claimNo+"_Rejected_RetainerClaims_"+claimNo);
		}
		setAssertMessage("Retainer Claim number "+claimNo+" is rejected", 2);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9866- This is Regression Test Case - GPP_SC_132. Retainer Claims  - Cancel Button Verify 
	 * clicking the Cancel Button for retainer claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyCancelRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_132"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(4);
		objStdClaimsPortal= objSC_RetainerSessionClaim.cancelRetainerClaim(4,environment);
		System.out.println("Retainer claim has been canceled");
		setAssertMessage("Retainer claim has been canceled", 1);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainersClaim Canceled");
		}
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9867- This is Regression Test Case - GPP_SC_133. Retainer Claims  - Save As Draft  Verify the 
	 * Save As Draft  for retainer claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifySaveRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_133"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(5);
		objSC_RetainerSessionClaim= objSC_RetainerSessionClaim.createRetainerClaim(5,environment);
		if(evidence)
		{
			objSC_RetainerSessionClaim.captureStdClaimPortalSnap(key+"_RetainerClaim_EnteredData");
		}
		objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSaveForLaterButton(5);
		String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 5, "RetainerClaimData",true);
		System.out.println("Retainer claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Retainer claim no " +claimNo+ " has been created for key "+key, 1);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim Status as Draft_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "RetainerClaimData", 5);
		assertEquals(claimStatus, "Draft");
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9745- This is Regression Test Case - GPP_SC_14. Standard claims view screen (GP Practice) - 
	 * Revert to Draft Verify the Revert to Draft functionality
	 * 9746- GPP_SC_15- Standard claims view screen (GP Practice) - Revert to Draft Verify user is able to update the 
	 * Claim details after reverting the claim
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void revertToDraftRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_14"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(6);
		objSC_RetainerSessionClaim= objSC_RetainerSessionClaim.createRetainerClaim(6,environment);
		objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSubmitButton(6);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Retainer claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Retainer claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		Verify.verifyEquals(claimStatus, "Pending", "Expected is Pending but actual is "+claimStatus);
		
		String finalClaimStatus= objStdClaimsPortal.verifyRevertToDraftClaim(claimNo);
		
		Verify.verifyEquals(finalClaimStatus, "Draft","Expected is Pending but actual is "+finalClaimStatus);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim Reverted_"+claimNo);
		}
		objStdClaimsPortal.getClaimNumber(key);
		System.out.println("Retainer claim no " +claimNo+ " has been reverted to "+claimStatus+ " for key "+key);
		
		setAssertMessage("Retainer claim no " +claimNo+ " has been reverted to "+claimStatus+ " for key "+key, 2);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		objSC_RetainerSessionClaim.updateClaimDetails(6);
		objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSubmitButton(6);
		System.out.println("Retainer claim no " +claimNo+ " has been updated for key "+key);
		setAssertMessage("Retainer claim no " +claimNo+ " has been updated for key "+key, 1);
		String claimStatus2= objStdClaimsPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim_UpdatedAfterRevert_"+claimNo);
		}
		Assert.assertEquals(claimStatus2, "Pending", "Expected is Pending but actual is "+claimStatus2);
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus2);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_RetainerSessionClaim.captureStdClaimPortalSnap(key+"_RetainerClaim_EnteredData_"+claimNo);
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9748- This is Regression Test Case - GPP_SC_16. Standard claims view screen (GP Practice) - 
	 * Delete Verify the functionality of delete button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyDeleteRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_16"; 
			
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(7);
		objSC_RetainerSessionClaim= objSC_RetainerSessionClaim.createRetainerClaim(7,environment);
		objStdClaimsPortal= objSC_RetainerSessionClaim.clickOnSaveForLaterButton(7);
		String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 7, "RetainerClaimData",true);
		System.out.println("Retainer claim no " +claimNo+ " has been created for key "+key);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim_Before Delete_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "RetainerClaimData", 7);
		Verify.verifyEquals(claimStatus, "Draft");
		objStdClaimsPortal.verifyDeleteClaim("RetainerClaimData",7,claimNo,key,true);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_RetainerClaim_After Delete_"+claimNo);
		}
		setAssertMessage("Retainer claim number "+claimNo+" is deleted", 1);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/***********************************************************************************************************
	 * Akshay Sohoni : - 9864- This is Regression Test Case - GPP_SC_130. Retainer Claims - Tick to confirm Verify 
	 * the tick confirm field in Declaration area
	 * @throws ParseException 
	 	******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyFieldValidationsForRetainerClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_130"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_RetainerSessionClaim objSC_RetainerSessionClaim= objStdClaimsPortal.clickToCreateRetainerClaim(8);
		objSC_RetainerSessionClaim= objSC_RetainerSessionClaim.createRetainerClaimWithoutFieldValidations(8,environment);
				
		if(evidence)
		{
			objSC_RetainerSessionClaim.captureValidationMessages(key+"_ErrorOnRetainerClaim_WithoutFieldValidations");
		}
		List<String> ActualErrorMessagesonSave = objSC_RetainerSessionClaim.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("GPPSCTESTDATA.xlsx", "ValidationErrors", 1);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list.");
		}
	}
	
	//Premises claim scripts started
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9888- This is Regression Test Case - GPP_SC_154. Premises Claims  - Submit Verify the Submit 
	 * button for Premises claim page
	 * 9899- GPP_SC_165- Approval/Rejection of standard claims - Approve Button Verify the functionality of Approve button
	 * 9734- GPP_SC_03- Standard claims view screen (GP Practice) - Create new claim Verify the functionality of create a new claim button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifySubmitApprovePremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_154"; 
		String key2= "GPP_SC_165";
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(1);
		objSC_PremisesClaim= objSC_PremisesClaim.createPremisesClaim(1,environment);
		System.out.println("Data entered");
		objStdClaimsPortal= objSC_PremisesClaim.clickOnSubmitButton(1);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_PremisesClaim.captureStdClaimPortalSnap(key+"_PremisesClaim_EnteredData_"+claimNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		objStdClaimsApprovalWindow.approveStdClaim(claimNo,"PremisesClaimData",1);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		String finalClaimAndPLStatus= objStdClaimsPortal.getClaimStatus();
		String statusArray[]= finalClaimAndPLStatus.split("-");
		String finalClaimStatus= statusArray[0].toString().trim();
		assertEquals(finalClaimStatus, "Approved");
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key2+"_Approved_PremisesClaims_"+claimNo);
		}
		setAssertMessage("Premises Claim number "+claimNo+" is approved", 2);
		quit(browser);
		
		// To verify status of SC claim submitted in CRM.
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
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
		String expclaimStatusCRM = utilities.ExcelUtilities.getKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", key, "EXPCLAIMSTATUS");
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
				if (clmStatus.contains(expclaimStatusCRM))
				{
					setAssertMessage("The claim status in CRM is Accepted For Payment.", 1);
					System.out.println("The claim status in CRM is Accepted For Payment.");					
					String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus_GPPSC(ValueForFieldValue, evidence,key2+"_ClaimStatus_"+claimNo);
					// Verify Payment Line generated
					boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated_GPPSC(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +ValueForFieldValue );
						paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus_GPPSC(evidence, key2+"_PaymentLineStatus_"+claimNo, key2+"_GMPAmountDue_"+claimNo);
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
					Assert.assertEquals(clmStatus, finalClaimStatus, "The claim status in CRM is not Approved.");
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
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9900- This is Regression Test Case - GPP_SC_166. Approval/Rejection of standard claims - Reject 
	 * Button Verify the functionality of Reject button
	 * 9868- This is Regression Test Case - GPP_SC_134. Retainer Claims  - Submit Verify the 
	 * Submit button for retainer claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyRejectPremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException, ParseException{
		String key= "GPP_SC_166"; 
			
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(2);
		objSC_PremisesClaim= objSC_PremisesClaim.createPremisesClaim(2,environment);
		objStdClaimsPortal= objSC_PremisesClaim.clickOnSubmitButton(2);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Registrar claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Registrar claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(claimStatus, "Pending");
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_PremisesClaim.captureStdClaimPortalSnap(key+"_PremisesClaim_EnteredData_"+claimNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab_Commissioner();
		StdClaimsApprovalWindow objStdClaimsApprovalWindow= objClaimsHomePage.ClickOnClaimsApprovalTab("NHSECommissioner");
		objStdClaimsApprovalWindow.rejectStdClaim(claimNo,"PremisesClaimData",2);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		String finalClaimStatus= objStdClaimsPortal.getClaimStatus();
		assertEquals(finalClaimStatus, "Rejected");
		System.out.println("Claim status is "+finalClaimStatus);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_"+claimNo+"_Rejected_PremisesClaims_"+claimNo);
		}
		setAssertMessage("Registrar claim no " +claimNo+ " has been rejected", 2);
				
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9880- This is Regression Test Case - GPP_SC_146. Premises Claims - Browse button Verify the 
	 * Browse button on the Premises Expense Claim page
	 * 9887- This is Regression Test Case - GPP_SC_153. Premises Claims  - Save for Later  Verify the Save for Later for
	 *  Premises claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void validateFileFormatsForPremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_146"; 
		String key2= "GPP_SC_153";
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(3);
		boolean allUploaded= objSC_PremisesClaim.validateFileFormatAndClaimData(3,environment);
		SoftAssert softAssert= new SoftAssert();
		if(allUploaded){
			System.out.println("All desired file formats are uploaded successfully for key: "+key);
			setAssertMessage("All desired file formats are uploaded successfully for key: "+key, 1);
		}else{
			System.out.println("All desired file formats are not uploaded successfully for key: "+key);
			softAssert.assertEquals(allUploaded, true,"Expected and Actual file count is not matching");
		}
		if(evidence)
		{
			objSC_PremisesClaim.captureStdClaimPortalSnap(key+"_PremisesClaim_EnteredData_");
		}
		objStdClaimsPortal= objSC_PremisesClaim.clickOnSaveForLaterButton(3);
		String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 3, "PremisesClaimData",true);
		System.out.println("Locum pre-approval claim no " +claimNo+ " has been created for key "+key);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key2+"_PremisesClaim Status as Draft_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "PremisesClaimData", 3);
		softAssert.assertEquals(claimStatus, "Draft");
		setAssertMessage("Premises claim no " +claimNo+ " has been saved with multiple files for key "+key, 2);
		softAssert.assertAll();
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9886- This is Regression Test Case - GPP_SC_152. Premises Claims  - Cancel Button Verify the 
	 * Cancel Button for Premises claim page
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyCancelPremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_152"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(4);
		objStdClaimsPortal= objSC_PremisesClaim.cancelPremisesClaim(4,environment);
		System.out.println("Premises claim has been canceled");
		setAssertMessage("Premises claim has been canceled", 1);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim Canceled");
		}
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9745- This is Regression Test Case - GPP_SC_14. Standard claims view screen (GP Practice) - 
	 * Revert to Draft Verify the Revert to Draft functionality
	 * 9746- GPP_SC_15- Standard claims view screen (GP Practice) - Revert to Draft Verify user is able to update the 
	 * Claim details after reverting the claim
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void revertToDraftPremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_14"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(5);
		objSC_PremisesClaim= objSC_PremisesClaim.createPremisesClaim(5,environment);
		objStdClaimsPortal= objSC_PremisesClaim.clickOnSubmitButton(5);
		String claimNo= objStdClaimsPortal.getClaimNumber(key);
		
		System.out.println("Premises claim no " +claimNo+ " has been created for key "+key);
		setAssertMessage("Premises claim no " +claimNo+ " has been created for key "+key, 1);
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim_PortalDetails_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatus();
		Verify.verifyEquals(claimStatus, "Pending", "Expected is Pending but actual is "+claimStatus);
		
		String finalClaimStatus= objStdClaimsPortal.verifyRevertToDraftClaim(claimNo);
		
		Verify.verifyEquals(finalClaimStatus, "Draft","Expected is Pending but actual is "+finalClaimStatus);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim Reverted_"+claimNo);
		}
		objStdClaimsPortal.getClaimNumber(key);
		System.out.println("Premises claim no " +claimNo+ " has been reverted to "+claimStatus+ " for key "+key);
		
		setAssertMessage("Premises claim no " +claimNo+ " has been reverted to "+claimStatus+ " for key "+key, 2);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		objSC_PremisesClaim.updateClaimDetails(5);
		objStdClaimsPortal= objSC_PremisesClaim.clickOnSubmitButton(5);
		System.out.println("Premises claim no " +claimNo+ " has been updated for key "+key);
		setAssertMessage("Premises claim no " +claimNo+ " has been updated for key "+key, 1);
		String claimStatus2= objStdClaimsPortal.getClaimStatus();
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim_UpdatedAfterRevert_"+claimNo);
		}
		Assert.assertEquals(claimStatus2, "Pending", "Expected is Pending but actual is "+claimStatus2);
		objStdClaimsPortal= objStdClaimsPortal.searchStdClaims(claimStatus2);
		objStdClaimsPortal.clickOnClaimNumber(claimNo);
		if(evidence)
		{
			objSC_PremisesClaim.captureStdClaimPortalSnap(key+"_PremisesClaim_EnteredData_"+claimNo);
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 9748- This is Regression Test Case - GPP_SC_16. Standard claims view screen (GP Practice) - 
	 * Delete Verify the functionality of delete button
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyDeletePremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_16"; 
			
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(6);
		objSC_PremisesClaim= objSC_PremisesClaim.createPremisesClaim(6,environment);
		objStdClaimsPortal= objSC_PremisesClaim.clickOnSaveForLaterButton(6);
		String claimNo= objStdClaimsPortal.getClaimNumberWithClaimStatusAndType(key, "Draft", 6, "PremisesClaimData",true);
		System.out.println("Premises claim no " +claimNo+ " has been created for key "+key);

		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim_Before Delete_"+claimNo);
		}
		String claimStatus= objStdClaimsPortal.getClaimStatusByValues(key,"Draft", "PremisesClaimData", 6);
		Verify.verifyEquals(claimStatus, "Draft");
		objStdClaimsPortal.verifyDeleteClaim("PremisesClaimData",6,claimNo,key,true);
		
		if(evidence)
		{
			objStdClaimsPortal.captureStdClaimPortalSnap(key+"_PremisesClaim_After Delete_"+claimNo);
		}
		setAssertMessage("Premises claim no " +claimNo+ " has been deleted for key "+key, 1);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}


	/***********************************************************************************************************
	 * Akshay Sohoni : - 9884- This is Regression Test Case - GPP_SC_150. Premises Claims - Tick to confirm Verify 
	 * the tick to confirm field in Declaration area
	 * @throws ParseException 
	 	******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	
	public void verifyFieldValidationsForPremisesClaim(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key= "GPP_SC_150"; 
		
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation ObjSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= ObjSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPaymentHomePage objGPPaymentHomePage= objGPPHomePageNew.ClickonTab("Payments", GPPaymentHomePage.class);
		ClaimsHomePage objClaimsHomePage= objGPPaymentHomePage.ClickOnClaimsTab();
		StdClaimsPortal objStdClaimsPortal= objClaimsHomePage.ClickOnClaimsPortalTab();
		SC_PremisesClaim objSC_PremisesClaim= objStdClaimsPortal.clickToCreatePremisesClaim(8);
		objSC_PremisesClaim= objSC_PremisesClaim.createPremisesClaimWithoutFieldValidations(8,environment);
				
		if(evidence)
		{
			objSC_PremisesClaim.captureValidationMessages(key+"_ErrorOnPremisesClaim_WithoutFieldValidations");
		}
		List<String> ActualErrorMessagesonSave = objSC_PremisesClaim.AcutalErrormessage();
		System.out.println("Actual Error Messages on Save: "+ActualErrorMessagesonSave);
		List<String> ExpectedErrorMessagesonSave = ExcelUtilities.getCellValuesInExcel("GPPSCTESTDATA.xlsx", "ValidationErrors", 2);
		System.out.println("Expected Error Messages on Save: "+ExpectedErrorMessagesonSave);
		
		List<String> unmatchedErrorList = CommonFunctions.compareStrings(ActualErrorMessagesonSave, ExpectedErrorMessagesonSave);
		System.out.println("UnmatchedErrorList: "+unmatchedErrorList);
			
		if(unmatchedErrorList.isEmpty())
		{
			System.out.println("Actual error list on save action is matching with expected list.");
			setAssertMessage("Actual error list on save action is matching with expected list.", 1);
		}
		
		else
		{
			Assert.assertTrue(unmatchedErrorList.isEmpty(), "The Actual error list on Save Action is not matching with Expected Error list.");
		}
	}

}

