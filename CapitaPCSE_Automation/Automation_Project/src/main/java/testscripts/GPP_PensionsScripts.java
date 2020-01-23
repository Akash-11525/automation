package testscripts;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GPP.CI.GPPHomePageNew;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.Pensions.PracticeJoiner.GPJoinerRetainerEntry;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPensionsHomePage;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPracticeJoiner;
import pageobjects.GPP.Pensions.PracticeJoiner.JoinerApprovalForm;
import pageobjects.GPP.Pensions.PracticeLeaver.GPLeaverApplicationForm;
import pageobjects.GPP.Pensions.PracticeLeaver.GPPracticeLeaver;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;
@Listeners(ListenerClass.class)
public class GPP_PensionsScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18037- TC_JL_15- Joiner (Non-practitioner) application form screen - Application reference 
	 * number Verify the functionality of application reference number 
	 * 18033- TC_JL_11- GP Joiner application form screen - Submit Verify functionality of submit button 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void submitJoinerApplication(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("TC_JL_15","TC_JL_11");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterNewJoinerData(1);
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnCancelSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmSubmit();
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(keys.get(1));
		if(evidence){
			for(String key:keys){
				objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
			}
		}
		System.out.println("Pensions Joiner form is submitted and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is submitted and ref no is "+appRefNo, 1);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+keys, 2);
		System.out.println("Format is matched for key: "+keys);
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18025- TC_JL_03- GP Joiner application form screen - Application reference number Verify the 
	 * functionality of application reference number  
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void savePractitionerJoinerApplication(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("TC_JL_03");
		
		//data deletion
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "PracticeName", 1);
		List<String> GroupTypeValue= new ArrayList<String>();
		List<String> FieldValue= new ArrayList<String>();
		List<String> ConditionValue= new ArrayList<String>();
		List<String> ValueType= new ArrayList<String>();
		List<String> ValueForFieldValue= new ArrayList<String>();
		List<String> entityList= Arrays.asList("Assignments","Practice and Pension Scheme Enrollments","Additional Voluntary Contributions","Annual Income and Pension Contributions","Monthly Income and Pension Contributions");
		
		for(int i=0;i<entityList.size();i++){
			String entityName= entityList.get(i);
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			GroupTypeValue= Arrays.asList("fld");
			FieldValue= Arrays.asList("Contact");
			ConditionValue= Arrays.asList("Equals");
			ValueType= Arrays.asList("Lookup");
			ValueForFieldValue= Arrays.asList(contactName);
			ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
			ObjAdvancedFilter.clickResults();	
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
			flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				if(i==1||i==3||i==4||i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithLongName();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}/*else if(i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithOptionAtLeftSide();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}*/
				else{
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntity();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}
			}
			GroupTypeValue.remove(GroupTypeValue);
			FieldValue.remove(FieldValue);
			ConditionValue.remove(ConditionValue);
			ValueType.remove(ValueType);
			ValueForFieldValue.remove(ValueForFieldValue);
			objAdvancedFindResult.closeWindow();
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(1,environment,"PensionEnrollmentNo");
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSaveForLater();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnCancelDraft();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSaveForLater();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmDraft();
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(keys.get(0));
		if(evidence){
			for(String key:keys){
				objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
			}
		}
		System.out.println("Pensions Joiner form is saved and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is saved and ref no is "+appRefNo, 1);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+keys, 2);
		System.out.println("Format is matched for key: "+keys);
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18042- TC_JL_20- Joiner (Non-practitioner) application form screen - Voluntary contributions 
	 * Verify the fields of voluntary contributions 
	 * 18044- TC_JL_22- Joiner (Non-practitioner) application form screen - Cancel,save for later Verify functionality 
	 * of cancel,save for later buttons 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"},enabled=false)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void saveJoinerWithVoluntaryContribution(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("TC_JL_20","TC_JL_22");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterNewJoinerData(1);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterVoluntaryContributions(1,true);
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSaveForLater();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnCancelDraft();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSaveForLater();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmDraft();
		setAssertMessage("Pensions joiner form is submitted with voluntary contribution", 1);
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(keys.get(0));
		if(evidence){
			for(String key:keys){
				objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
			}
		}
		System.out.println("Pensions Joiner form is submitted and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is submitted and ref no is "+appRefNo, 2);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+keys, 2);
		System.out.println("Format is matched for key: "+keys);
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18030- TC_JL_08- GP Joiner application form screen - Voluntary contributions Verify the fields 
	 * of voluntary contributions 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"},enabled=false)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void savePractitionerWithVoluntaryContribution(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("TC_JL_08");
		
		//data deletion
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "PracticeName"); 
		
		List<String> GroupTypeValue= new ArrayList<String>();
		List<String> FieldValue= new ArrayList<String>();
		List<String> ConditionValue= new ArrayList<String>();
		List<String> ValueType= new ArrayList<String>();
		List<String> ValueForFieldValue= new ArrayList<String>();
		List<String> entityList= Arrays.asList("Assignments","Practice and Pension Scheme Enrollments","Additional Voluntary Contributions","Annual Income and Pension Contributions","Monthly Income and Pension Contributions");
		
		for(int i=0;i<entityList.size();i++){
			String entityName= entityList.get(i);
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			GroupTypeValue= Arrays.asList("fld");
			FieldValue= Arrays.asList("Contact");
			ConditionValue= Arrays.asList("Equals");
			ValueType= Arrays.asList("Lookup");
			ValueForFieldValue= Arrays.asList(contactName);
			ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
			ObjAdvancedFilter.clickResults();	
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
			flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				if(i==1||i==3||i==4||i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithLongName();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}/*else if(i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithOptionAtLeftSide();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}*/
				else{
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntity();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}
			}
			GroupTypeValue.remove(GroupTypeValue);
			FieldValue.remove(FieldValue);
			ConditionValue.remove(ConditionValue);
			ValueType.remove(ValueType);
			ValueForFieldValue.remove(ValueForFieldValue);
			objAdvancedFindResult.closeWindow();
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(1,environment,"PensionEnrollmentNo");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterVoluntaryContributions(1,true);
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnCancelSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmSubmit();
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(keys.get(0));
		if(evidence){
			for(String key:keys){
				objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
			}
		}
		System.out.println("Pensions Joiner form is saved and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is saved and ref no is "+appRefNo, 1);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+keys, 2);
		System.out.println("Format is matched for key: "+keys);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approval Pending",0);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Approve",1);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been approved for "+appRefNo, 3);
		System.out.println("Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab("GP Payments", GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approved",0);
		objCommissionerAppListing.sortApplicationNumber();
		boolean isNoPresent= objCommissionerAppListing.isApplicationPresent(appRefNo);
		Verify.verifyEquals(isNoPresent, true, "Application number is not present");
		setAssertMessage("Application no. "+appRefNo+" is visible under search records", 4);
		System.out.println("Application no. "+appRefNo+" is visible under search records");
		if(evidence){
			for(String key:keys){
				objCommissionerAppListing.captureCommissionerListingSnaps(key+"_performed actions_"+appRefNo);
			}
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18068- TC_JL_46- Joiner Application Form for Approval / Rejection Process Screen - Application 
	 * reference number Verify the functionality of application reference number 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void approveJoinerForm(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("TC_JL_46");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterNewJoinerData(1);
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnCancelSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmSubmit();
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(keys.get(0));
		if(evidence){
			for(String key:keys){
				objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
			}
		}
		System.out.println("Pensions Joiner form is submitted and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is submitted and ref no is "+appRefNo, 1);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+keys, 2);
		System.out.println("Format is matched for key: "+keys);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approval Pending",0);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Approve",1);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been approved for "+appRefNo, 3);
		System.out.println("Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab("GP Payments", GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approved",0);
		objCommissionerAppListing.sortApplicationNumber();
		boolean isNoPresent= objCommissionerAppListing.isApplicationPresent(appRefNo);
		Verify.verifyEquals(isNoPresent, true, "Application number is not present");
		setAssertMessage("Application no. "+appRefNo+" is visible under search records", 4);
		System.out.println("Application no. "+appRefNo+" is visible under search records");
		if(evidence){
			for(String key:keys){
				objCommissionerAppListing.captureCommissionerListingSnaps(key+"_performed actions_"+appRefNo);
			}
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18068- TC_JL_46- Joiner Application Form for Approval / Rejection Process Screen - Application 
	 * reference number Verify the functionality of application reference number 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void rejectJoinerForm(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys=Arrays.asList("TC_JL_46");
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterNewJoinerData(1);
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnCancelSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmSubmit();
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(keys.get(0));
		if(evidence){
			for(String key:keys){
				objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
			}
		}
		System.out.println("Pensions Joiner form is submitted and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is submitted and ref no is "+appRefNo, 1);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+keys, 2);
		System.out.println("Format is matched for key: "+keys);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approval Pending",0);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Reject",1);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been rejected for "+appRefNo, 3);
		System.out.println("Joiner form has been rejected for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab("GP Payments", GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Rejected",0);
		objCommissionerAppListing.sortApplicationNumber();
		boolean isNoPresent= objCommissionerAppListing.isApplicationPresent(appRefNo);
		Verify.verifyEquals(isNoPresent, true, "Application number is not present");
		setAssertMessage("Application no. "+appRefNo+" is visible under search records", 4);
		System.out.println("Application no. "+appRefNo+" is visible under search records");
		if(evidence){
			for(String key:keys){
				objCommissionerAppListing.captureCommissionerListingSnaps(key+"_performed actions_"+appRefNo);
			}
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18047- TC_JL_25- Retainer process screen - Application reference number Verify the functionality 
	 * of application reference number 
	 * 18078- TC_JL_56- Leavers Application Form - Application reference number and date Verify Application reference number and date fields
	 * 18092- TC_JL_69- Leaver approval screen - Approval /Rejection Verify approval /rejection process 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyRetainerProcess(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		String key="TC_JL_25";
		String key2="TC_JL_56";
		//data deletion
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "PracticeName");
		List<String> GroupTypeValue= new ArrayList<String>();
		List<String> FieldValue= new ArrayList<String>();
		List<String> ConditionValue= new ArrayList<String>();
		List<String> ValueType= new ArrayList<String>();
		List<String> ValueForFieldValue= new ArrayList<String>();
		List<String> entityList= Arrays.asList("Assignments","Practice and Pension Scheme Enrollments","Additional Voluntary Contributions","Annual Income and Pension Contributions","Monthly Income and Pension Contributions");
		
		for(int i=0;i<entityList.size();i++){
			String entityName= entityList.get(i);
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			GroupTypeValue= Arrays.asList("fld");
			FieldValue= Arrays.asList("Contact");
			ConditionValue= Arrays.asList("Equals");
			ValueType= Arrays.asList("Lookup");
			ValueForFieldValue= Arrays.asList(contactName);
			ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
			ObjAdvancedFilter.clickResults();	
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
			flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				if(i==1||i==3||i==4||i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithLongName();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}/*else if(i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithOptionAtLeftSide();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}*/
				else{
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntity();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}
			}
			GroupTypeValue.remove(GroupTypeValue);
			FieldValue.remove(FieldValue);
			ConditionValue.remove(ConditionValue);
			ValueType.remove(ValueType);
			ValueForFieldValue.remove(ValueForFieldValue);
			objAdvancedFindResult.closeWindow();
		}
		tearDown(browser);
		Thread.sleep(2000);
		//Creation of pensions joiner
		setup(browser, environment, clientName,"GPP");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation=ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(1,environment,"PensionEnrollmentNo");
		//objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterVoluntaryContributions(1,true);
		System.out.println("Data has been entered on UI");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmSubmit();
		String appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(key);
		if(evidence){
			objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_Joiner form_"+appRefNo);
		}
		System.out.println("Pensions Joiner form is saved and ref no is "+appRefNo);
		setAssertMessage("Pensions Joiner form is saved and ref no is "+appRefNo, 1);
		boolean isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+key, 2);
		System.out.println("Format is matched for key: "+key);
		tearDown(browser);
		Thread.sleep(2000);
		//authorisation of pensions joiner
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approval Pending",0);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Approve",1);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been approved for "+appRefNo, 3);
		System.out.println("Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab("GP Payments", GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("New Joiner","Commissioner Approved",0);
		objCommissionerAppListing.sortApplicationNumber();
		boolean isNoPresent= objCommissionerAppListing.isApplicationPresent(appRefNo);
		Verify.verifyEquals(isNoPresent, true, "Application number is not present");
		setAssertMessage("Application no. "+appRefNo+" is visible under search records", 4);
		System.out.println("Application no. "+appRefNo+" is visible under search records");
		if(evidence){
			objCommissionerAppListing.captureCommissionerListingSnaps(key+"_new joiner performed actions_"+appRefNo);
		}
		tearDown(browser);
		Thread.sleep(2000);
		//this will update the date present in assignments entity
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter= ObjCrmHome.clickAdvanceFind();
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Assignments");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				objAdvancedFindResult= objAdvancedFindResult.setTodayDateAsStartDateInAssignmentsEntity();
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
		tearDown(browser);
		Thread.sleep(2000);
		//Creation of pensions leaver
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation=ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		GPPracticeLeaver objGPPracticeLeaver= objGPPensionsHomePage.ClickonTab("Practice Leaver", GPPracticeLeaver.class);
		GPLeaverApplicationForm objGPLeaverApplicationForm= objGPPracticeLeaver.ClickOnLeaverEntryForm();
		objGPLeaverApplicationForm= objGPLeaverApplicationForm.enterLeaverData(1,environment,"PracticeName");
		objGPLeaverApplicationForm= objGPLeaverApplicationForm.clickOnSubmit();
		objGPLeaverApplicationForm= objGPLeaverApplicationForm.clickOnConfirmSubmit();
		appRefNo= objGPLeaverApplicationForm.getApplicationRefNo(key2);
		setAssertMessage("Leaver form has been submitted for application: "+appRefNo+" having key: "+key, 5);
		System.out.println("Leaver form has been submitted for application: "+appRefNo+" having key: "+key);
		isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+key2, 6);
		System.out.println("Format is matched for key: "+key2);
		tearDown(browser);
		Thread.sleep(2000);
		//authorisation of pensions leaver has been commented by Akshay as fr Salaried person, authorisation is not needed
/*		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeLeaver= objGPPensionsHomePage.clickOnPracticeLeaverTab();
		objCommissionerAppListing= objGPPracticeLeaver.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("Leaver","Commissioner Approval Pending",0);
		objCommissionerAppListing.sortApplicationNumber();
		objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Approve",1);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Leaver form has been approved for "+appRefNo+" for key "+key3, 7);
		System.out.println("Leaver form has been approved for "+appRefNo+" for key "+key3);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab("GP Payments", GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeLeaver= objGPPensionsHomePage.ClickonTab("Practice Leaver", GPPracticeLeaver.class);
		objCommissionerAppListing= objGPPracticeLeaver.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("Leaver","Commissioner Approved",0);
		objCommissionerAppListing.sortApplicationNumber();
		isNoPresent= objCommissionerAppListing.isApplicationPresent(appRefNo);
		Verify.verifyEquals(isNoPresent, true, "Application number is not present");
		setAssertMessage("Application no. "+appRefNo+" is visible under search records", 8);
		System.out.println("Application no. "+appRefNo+" is visible under search records");
		if(evidence){
			objCommissionerAppListing.captureCommissionerListingSnaps(key3+"_leaver performed actions_"+appRefNo);
		}
		tearDown(browser);
		Thread.sleep(2000);*/
		//this will update the date present in assignments entity
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter= ObjCrmHome.clickAdvanceFind();
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Assignments");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				objAdvancedFindResult= objAdvancedFindResult.setTodayDateAsStartDateInAssignmentsEntity();
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
		tearDown(browser);
		Thread.sleep(2000);
		//Creation of pensions retainer
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - GP Practice Management", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab("Practice Joiner", GPPracticeJoiner.class);
		objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab("GP Joiner Entry Form", GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterRetainerData(1,environment,"PensionEnrollmentNo");
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnSubmit();
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.clickOnConfirmSubmit();
		appRefNo= objGPJoinerRetainerEntry.getApplicationRefNo(key);
		if(evidence){
			objGPJoinerRetainerEntry.captureJoinerFormSnaps(key+"_retainer Joiner form_"+appRefNo);
		}
		System.out.println("Pensions retainer Joiner form is saved and ref no is "+appRefNo);
		setAssertMessage("Pensions retainer Joiner form is saved and ref no is "+appRefNo, 9);
		isFormatmatched= objGPJoinerRetainerEntry.verifyAppRefFormat(appRefNo);
		Assert.assertEquals(isFormatmatched, true, "Reference number is not matching with format defined");
		setAssertMessage("Format is matched for key: "+key, 10);
		System.out.println("Format is matched for key: "+key);
		tearDown(browser);
		Thread.sleep(2000);
		//authorisation of pensions retainer
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPP - NHSE Commissioner", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("Returning Retainer","Commissioner Approval Pending",0);
		objCommissionerAppListing.sortApplicationNumber();
		objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Approve",1);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Retainer Joiner form has been approved for "+appRefNo, 11);
		System.out.println("Retainer Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab("GP Payments", GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication("Returning Retainer","Commissioner Approved",0);
		objCommissionerAppListing.sortApplicationNumber();
		isNoPresent= objCommissionerAppListing.isApplicationPresent(appRefNo);
		Verify.verifyEquals(isNoPresent, true, "Application number is not present");
		setAssertMessage("Retainer Application no. "+appRefNo+" is visible under search records", 12);
		System.out.println("Retainer Application no. "+appRefNo+" is visible under search records");
		if(evidence){
			objCommissionerAppListing.captureCommissionerListingSnaps(key+"_retainer performed actions_"+appRefNo);
		}
	}
}
