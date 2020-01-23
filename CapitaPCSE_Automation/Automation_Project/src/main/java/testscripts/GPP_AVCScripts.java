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
import helpers.Screenshot;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GPP.CI.GPPHomePageNew;
import pageobjects.GPP.CI.MyNotification;
import pageobjects.GPP.Pensions.AVC.AVCApprovalForm;
import pageobjects.GPP.Pensions.AVC.AVCHomePage;
import pageobjects.GPP.Pensions.AVC.AVCPurchaseForm;
import pageobjects.GPP.Pensions.AVC.ERRBOApplication;
import pageobjects.GPP.Pensions.AVC.ERRBOApprovalForm;
import pageobjects.GPP.Pensions.AVC.MPAVCInstructionForm;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.Pensions.PracticeJoiner.GPJoinerRetainerEntry;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPensionsHomePage;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPracticeJoiner;
import pageobjects.GPP.Pensions.PracticeJoiner.JoinerApprovalForm;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class GPP_AVCScripts extends BaseClass {

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void submitAVCForm(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String scriptKey= "GPP_AVC_01";//Key to be changed in code,test data file
		List<String> values= ExcelUtilities.getScriptParameters("GPAVC",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String testDataFileName= ExcelUtilities.getExcelParameterByModule("GPAVC","TestDataFile");
		String testDataSheet= ExcelUtilities.getExcelParameterByModule("GPAVC","TestDataSheet");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String []keyArray= strKeys.split(",");
		String role= values.get(2);
		String[]roleArray= role.split(",");
		String CRMModule= values.get(3);
		String CRMUSER= values.get(4);
		String tab= values.get(6);
		String[]tabArray= tab.split(",");
		String applType= values.get(8);
		String applStatus= values.get(9);
		String[]applStatusArray= applStatus.split(",");
		String strDays= values.get(10);
		int daysToAdd= Integer.parseInt(strDays);
		String action= values.get(11);
		String upperTabName= values.get(13);
		String notificationBody= values.get(14);
		//String contactName= ConfigurationData.getRefDataDetails(environment, "AVCPracticeName"); 
		//data deletion
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "AVCPracticeName"); 
		//ExcelUtilities.getKeyValueByPosition(testDataFileName, testDataSheet, "PracticeName", scriptKey);
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
		//Creation of Pensions Joiner
		String key= keyArray[0].toString();
		//String key2= keyArray[1].toString();
		List<String> roles= Arrays.asList(roleArray);
		List<String> tabs= Arrays.asList(tabArray);
		List<String> applStatuses= Arrays.asList(applStatusArray);
		String GPPracticeRole= roles.get(0);
		String NHSCommissioner= roles.get(1);
		String GPPrincipal= roles.get(2);
		String GPaymtClerk= roles.get(3);
		String GPHomeTab= tabs.get(0);
		String pensionsTab= tabs.get(1);
		String joinerTab= tabs.get(2);
		String beforeApproval= applStatuses.get(0);
		String afterApproval= applStatuses.get(1);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GPPracticeRole, environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab(pensionsTab, GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab(joinerTab, GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(scriptKey,testDataFileName,testDataSheet,environment,"AVCPensionEnrNo");
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
		//Authorisation of pensions joiner
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(NHSCommissioner, environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication(applType,beforeApproval,daysToAdd);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction(action,testDataFileName,testDataSheet,scriptKey);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been approved for "+appRefNo, 3);
		System.out.println("Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab(headerTabName, GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication(applType,afterApproval,daysToAdd);
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
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
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
		}
		tearDown(browser);
		Thread.sleep(2000);
		//AVC Entry
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP(GPPrincipal, environment,GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		AVCHomePage objAVCHomePage= objGPPensionsHomePage.clickOnAVCTab_Commissioner();
		AVCPurchaseForm objAVCPurchaseForm= objAVCHomePage.ClickOnAVCAppForm();
		objAVCPurchaseForm= objAVCPurchaseForm.enterDetailsForSalariedWithInstallment(testDataFileName,testDataSheet,scriptKey,environment);
		System.out.println("Data has been entered on UI.");
		objAVCPurchaseForm= objAVCPurchaseForm.clickOnSubmit();
		objAVCPurchaseForm= objAVCPurchaseForm.clickOnCancelSubmit();
		objAVCPurchaseForm= objAVCPurchaseForm.clickOnSubmit();
		objAVCPurchaseForm= objAVCPurchaseForm.clickOnConfirmSubmit();
		String refNumber= objAVCPurchaseForm.getApplicationRefNo();
		if(evidence){
			objAVCPurchaseForm.captureAVCEntrySnaps(key+"_Entered data on AVC form_"+refNumber);
		}
		System.out.println("AVC form has been submitted for document number: "+refNumber);
		setAssertMessage("AVC form has been submitted for document number: "+refNumber, 5);
		tearDown(browser);
		Thread.sleep(2000);
		//AVC approval
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP(GPaymtClerk, environment,GPPHomePageNew.class);// role to be changed to PCSE Clerk
		//objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		MyNotification objMyNotification= objGPPHomePageNew.ClickOnPageHeader(upperTabName, MyNotification.class);
		AVCApprovalForm objAVCApprovalForm= objMyNotification.clickOnReviewLink(refNumber, AVCApprovalForm.class, key+"_NotificationSnaps", notificationBody);
		boolean isApplicationPresent= objAVCApprovalForm.verifyPresenceOfDocument(refNumber);
		Assert.assertEquals(isApplicationPresent, true, "Application Number is not matching on Approval screen");
		System.out.println("Application number is present on Notification menu for key: "+key);
		setAssertMessage("Application number is present on Notification menu for key: "+key, 6);
		objAVCApprovalForm= objAVCApprovalForm.clickOnSubmit();
		objAVCApprovalForm= objAVCApprovalForm.clickOnCancelSubmit();
		objAVCApprovalForm= objAVCApprovalForm.clickOnSubmit();
		objAVCApprovalForm= objAVCApprovalForm.clickOnConfirmSubmit();
		if(evidence){
			objAVCApprovalForm.captureAVCApprovalSnaps(key+"_Approved data on AVC form_"+refNumber);
		}
		System.out.println("Record number "+refNumber+" has been approved successfully for key: "+key);
		setAssertMessage("Record number "+refNumber+" has been approved successfully for key: "+key, 7);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter= new AdvancedFilter(getDriver());
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		String entityName = entityList.get(4);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				boolean isAmtPresent= objAdvancedFindResult.verifyPurchaseAmount("AVC",testDataFileName,testDataSheet,scriptKey);
				Assert.assertEquals(isAmtPresent, true,"AVC Purchase Amount is not updated");
				System.out.println("AVC Purchase amount has been updated in CRM for document number: "+refNumber);
				setAssertMessage("AVC Purchase amount has been updated in CRM for document number: "+refNumber, 8);
				if(evidence){
					Screenshot.TakeSnap(getDriver(), key+"_updated monthly value_"+refNumber);
				}
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void submitMPAVCForm(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String scriptKey= "GPP_AVC_02";//Key to be changed in code,test data file
		List<String> values= ExcelUtilities.getScriptParameters("GPAVC",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String testDataFileName= ExcelUtilities.getExcelParameterByModule("GPAVC","TestDataFile");
		String testDataSheet= ExcelUtilities.getExcelParameterByModule("GPAVC","TestDataSheet");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String []keyArray= strKeys.split(",");
		String role= values.get(2);
		String[]roleArray= role.split(",");
		String CRMModule= values.get(3);
		String CRMUSER= values.get(4);
		String tab= values.get(6);
		String[]tabArray= tab.split(",");
		String applType= values.get(8);
		String applStatus= values.get(9);
		String[]applStatusArray= applStatus.split(",");
		String strDays= values.get(10);
		int daysToAdd= Integer.parseInt(strDays);
		String action= values.get(11);
		//data deletion
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "AVCPracticeName");
		//ExcelUtilities.getKeyValueByPosition(testDataFileName, testDataSheet, "PracticeName", scriptKey);
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
				}else if(i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithOptionAtLeftSide();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}
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
		//Creation of Pensions Joiner
		String key= keyArray[0].toString();
		//String key2= keyArray[1].toString();
		List<String> roles= Arrays.asList(roleArray);
		List<String> tabs= Arrays.asList(tabArray);
		List<String> applStatuses= Arrays.asList(applStatusArray);
		String GPPracticeRole= roles.get(0);
		String NHSCommissioner= roles.get(1);
		String GPPrincipal= roles.get(2);
		String GPHomeTab= tabs.get(0);
		String pensionsTab= tabs.get(1);
		String joinerTab= tabs.get(2);
		String beforeApproval= applStatuses.get(0);
		String afterApproval= applStatuses.get(1);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GPPracticeRole, environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab(pensionsTab, GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab(joinerTab, GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(scriptKey,testDataFileName,testDataSheet,environment,"AVCPensionEnrNo");
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
		//Authorisation of pensions joiner
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(NHSCommissioner, environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication(applType,beforeApproval,daysToAdd);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction(action,testDataFileName,testDataSheet,scriptKey);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been approved for "+appRefNo, 3);
		System.out.println("Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab(headerTabName, GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication(applType,afterApproval,daysToAdd);
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
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
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
		//MPAVC entry
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP(GPPrincipal, environment,GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		AVCHomePage objAVCHomePage= objGPPensionsHomePage.clickOnAVCTab_Commissioner();
		MPAVCInstructionForm objMPAVCInstructionForm= objAVCHomePage.ClickOnMPAVC();
		objMPAVCInstructionForm= objMPAVCInstructionForm.enterDetailsForSalariedWithInstallment(testDataFileName,testDataSheet,scriptKey,environment);
		System.out.println("Data has been entered on UI.");
		objMPAVCInstructionForm= objMPAVCInstructionForm.clickOnSubmit();
		objMPAVCInstructionForm= objMPAVCInstructionForm.clickOnCancelSubmit();
		objMPAVCInstructionForm= objMPAVCInstructionForm.clickOnSubmit();
		objMPAVCInstructionForm= objMPAVCInstructionForm.clickOnConfirmSubmit();
		String refNumber= objMPAVCInstructionForm.getApplicationRefNo();
		if(evidence){
			objMPAVCInstructionForm.captureAVCEntrySnaps(key+"_Entered data on MPAVC form_"+refNumber);
		}
		System.out.println("MPAVC form has been submitted for document number: "+refNumber);
		setAssertMessage("MPAVC form has been submitted for document number: "+refNumber, 5);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter= new AdvancedFilter(getDriver());
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		String entityName = entityList.get(4);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				boolean isAmtPresent= objAdvancedFindResult.verifyPurchaseAmount("MPAVC",testDataFileName,testDataSheet,scriptKey);//element to be changed to MPAVC amount
				Assert.assertEquals(isAmtPresent, true,"AVC Purchase Amount is not updated");
				System.out.println("MPAVC Purchase amount has been updated in CRM for document number: ");
				setAssertMessage("MPAVC Purchase amount has been updated in CRM for document number: ", 6);
				if(evidence){
					Screenshot.TakeSnap(getDriver(), key+"_updated monthly value_"+refNumber);
				}
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void submitERRBOForm(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String scriptKey= "GPP_AVC_03";//Key to be changed in code,test data file
		List<String> values= ExcelUtilities.getScriptParameters("GPAVC",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String testDataFileName= ExcelUtilities.getExcelParameterByModule("GPAVC","TestDataFile");
		String testDataSheet= ExcelUtilities.getExcelParameterByModule("GPAVC","TestDataSheet");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String []keyArray= strKeys.split(",");
		String role= values.get(2);
		String[]roleArray= role.split(",");
		String CRMModule= values.get(3);
		String CRMUSER= values.get(4);
		String tab= values.get(6);
		String[]tabArray= tab.split(",");
		String applType= values.get(8);
		String applStatus= values.get(9);
		String[]applStatusArray= applStatus.split(",");
		String strDays= values.get(10);
		int daysToAdd= Integer.parseInt(strDays);
		String action= values.get(11);
		String upperTabName= values.get(13);
		String notificationBody= values.get(14);
		//String contactName= ConfigurationData.getRefDataDetails(environment, "AVCPracticeName"); 
		//data deletion
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "AVCPracticeName"); 
		//ExcelUtilities.getKeyValueByPosition(testDataFileName, testDataSheet, "PracticeName", scriptKey);
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
				}else if(i==2){
					objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithOptionAtLeftSide();
					System.out.println("Entries have been deleted for Contact under "+entityName +" entity for value : "+ValueForFieldValue);
				}
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
		//Creation of Pensions Joiner
		String key= keyArray[0].toString();
		//String key2= keyArray[1].toString();
		List<String> roles= Arrays.asList(roleArray);
		List<String> tabs= Arrays.asList(tabArray);
		List<String> applStatuses= Arrays.asList(applStatusArray);
		String GPPracticeRole= roles.get(0);
		String NHSCommissioner= roles.get(1);
		String GPPrincipal= roles.get(2);
		String GPaymtClerk= roles.get(3);
		String GPHomeTab= tabs.get(0);
		String pensionsTab= tabs.get(1);
		String joinerTab= tabs.get(2);
		String beforeApproval= applStatuses.get(0);
		String afterApproval= applStatuses.get(1);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GPPracticeRole, environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab(pensionsTab, GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab(joinerTab, GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(scriptKey,testDataFileName,testDataSheet,environment,"AVCPensionEnrNo");
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
		//Authorisation of pensions joiner
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(NHSCommissioner, environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPLocalOffice");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab();
		CommissionerAppListing objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication(applType,beforeApproval,daysToAdd);
		objCommissionerAppListing.sortApplicationNumber();
		JoinerApprovalForm objJoinerApprovalForm= objCommissionerAppListing.clickOnFirstRecord(appRefNo);//ref no to be passed
		objJoinerApprovalForm= objJoinerApprovalForm.performAction(action,testDataFileName,testDataSheet,scriptKey);
		objJoinerApprovalForm= objJoinerApprovalForm.clickOnSubmit();
		objCommissionerAppListing= objJoinerApprovalForm.clickOnConfirmSubmit();
		setAssertMessage("Joiner form has been approved for "+appRefNo, 3);
		System.out.println("Joiner form has been approved for "+appRefNo);
		objGPPHomePageNew= objGPPHomePageNew.ClickonBreadcrumbTab("Home", GPPHomePageNew.class);
		//objGPPHomePageNew= objGPPHomePageNew.ClickonHeaderTab(headerTabName, GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		objGPPracticeJoiner= objGPPensionsHomePage.clickOnPracticeJoinerTab_Commissioner();
		objCommissionerAppListing= objGPPracticeJoiner.ClickOnAppCommissionerApproval();
		objCommissionerAppListing= objCommissionerAppListing.searchApplication(applType,afterApproval,daysToAdd);
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
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
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
		//ERRBO Entry
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP(GPPrincipal, environment,GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		AVCHomePage objAVCHomePage= objGPPensionsHomePage.clickOnAVCTab_Commissioner();
		ERRBOApplication objERRBOApplication= objAVCHomePage.ClickOnERRBOForm();
		objERRBOApplication= objERRBOApplication.enterERRBODetails(testDataFileName,testDataSheet,scriptKey,environment);
		System.out.println("Data has been entered on UI.");
		objERRBOApplication= objERRBOApplication.clickOnSubmit();
		objERRBOApplication= objERRBOApplication.clickOnCancelSubmit();
		objERRBOApplication= objERRBOApplication.clickOnSubmit();
		objERRBOApplication= objERRBOApplication.clickOnConfirmSubmit();
		String refNumber= objERRBOApplication.getApplicationRefNo();
		if(evidence){
			objERRBOApplication.captureERRBOEntrySnaps(key+"_Entered data on AVC form_"+refNumber);
		}
		System.out.println("ERRBO form has been submitted for document number: "+refNumber);
		setAssertMessage("ERRBO form has been submitted for document number: "+refNumber, 5);
		tearDown(browser);
		Thread.sleep(2000);
		//ERRBO approval
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP(GPaymtClerk, environment,GPPHomePageNew.class);// role to be changed to PCSE Clerk
		//objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		MyNotification objMyNotification= objGPPHomePageNew.ClickOnPageHeader(upperTabName, MyNotification.class);
		ERRBOApprovalForm objERRBOApprovalForm= objMyNotification.clickOnReviewLink(refNumber, ERRBOApprovalForm.class, key+"_NotificationSnaps", notificationBody);
		boolean isApplicationPresent= objERRBOApprovalForm.verifyPresenceOfDocument(refNumber);
		Assert.assertEquals(isApplicationPresent, true, "Application Number is not matching on Approval screen");
		System.out.println("Application number is present on Notification menu for key: "+key);
		setAssertMessage("Application number is present on Notification menu for key: "+key, 6);
		objERRBOApprovalForm= objERRBOApprovalForm.clickOnSubmit();
		objERRBOApprovalForm= objERRBOApprovalForm.clickOnCancelSubmit();
		objERRBOApprovalForm= objERRBOApprovalForm.clickOnSubmit();
		objERRBOApprovalForm= objERRBOApprovalForm.clickOnConfirmSubmit();
		if(evidence){
			objERRBOApprovalForm.captureERRBOApprovalSnaps(key+"_Approved data on AVC form_"+refNumber);
		}
		System.out.println("Record number "+refNumber+" has been approved successfully for key: "+key);
		setAssertMessage("Record number "+refNumber+" has been approved successfully for key: "+key, 7);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter= new AdvancedFilter(getDriver());
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		String entityName = entityList.get(4);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				boolean isAmtPresent= objAdvancedFindResult.verifyPurchaseAmount("ERRBO",testDataFileName,testDataSheet,scriptKey);
				Assert.assertEquals(isAmtPresent, true,"AVC Purchase Amount is not updated");
				System.out.println("AVC Purchase amount has been updated in CRM for document number: "+refNumber);
				setAssertMessage("AVC Purchase amount has been updated in CRM for document number: "+refNumber, 8);
				if(evidence){
					Screenshot.TakeSnap(getDriver(), key+"_updated monthly value_"+refNumber);
				}
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}

	
}
