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
import pageobjects.GPP.CI.MyNotification;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.Pensions.PracticeJoiner.GPJoinerRetainerEntry;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPensionsHomePage;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPracticeJoiner;
import pageobjects.GPP.Pensions.PracticeJoiner.JoinerApprovalForm;
import pageobjects.GPP.Pensions.SalaryChange.GPSalaryChangeForm;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class GPP_SalaryChangeScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 18309- GPP_SLC_11- GPP - Salary Change Form Verify the Declaration section. 
	 * 18311- GPP_SLC_13- GPP - Salary Change Form Verify submitting the form. 
	 * 18310- GPP_SLC_12- GPP - Salary Change Form Verify cancelling the form. 
	 * 18313- GPP_SLC_15- GPP - Notification Verify notification generated after submitting the salary change form. 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","Sanity","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifySalaryChangeFlow(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String scriptKey= "GPP_SLC_11";
		List<String> values= ExcelUtilities.getScriptParameters("GPSalChange",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String testDataFileName= ExcelUtilities.getExcelParameterByModule("GPSalChange","TestDataFile");
		String testDataSheet= ExcelUtilities.getExcelParameterByModule("GPSalChange","TestDataSheet");
		String amtTierSheet= ExcelUtilities.getExcelParameterByModule("GPSalChange","SalChangeAmtTierSheet");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String []keyArray= strKeys.split(",");
		String role= values.get(2);
		String[]roleArray= role.split(",");
		String CRMModule= values.get(3);
		String CRMUSER= values.get(4);
		values.get(5);
		String tab= values.get(6);
		String[]tabArray= tab.split(",");
		values.get(7);
		String applType= values.get(8);
		String applStatus= values.get(9);
		String[]applStatusArray= applStatus.split(",");
		String strDays= values.get(10);
		int daysToAdd= Integer.parseInt(strDays);
		String action= values.get(11);
		values.get(12);
		String upperTabName= values.get(13);
		String notificationBody= values.get(14);
		
		
/*		List<String>GroupTypeValue= new ArrayList<String>();
		List<String>FieldValue= new ArrayList<String>();
		List<String>ConditionValue= new ArrayList<String>();
		List<String>ValueType= new ArrayList<String>();
		List<String>ValueForFieldValue= new ArrayList<String>();
		String contactName= ConfigurationData.getRefDataDetails(environment, "PracticeName");
		List<String>keys= Arrays.asList(keyArray);*/
		
		//data deletion
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
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
		List<String> keys= Arrays.asList(keyArray);
		String key= keyArray[0].toString();
		keyArray[1].toString();
		String key3= keyArray[2].toString();
		List<String> roles= Arrays.asList(roleArray);
		List<String> tabs= Arrays.asList(tabArray);
		List<String> applStatuses= Arrays.asList(applStatusArray);
		String GPPracticeRole= roles.get(0);
		String NHSCommissioner= roles.get(1);
		String GPHomeTab= tabs.get(0);
		String pensionsTab= tabs.get(1);
		String joinerTab= tabs.get(2);
		tabs.get(3);
		String beforeApproval= applStatuses.get(0);
		String afterApproval= applStatuses.get(1);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GPPracticeRole, environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		GPPensionsHomePage objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		GPPracticeJoiner objGPPracticeJoiner= objGPPensionsHomePage.ClickonTab(pensionsTab, GPPracticeJoiner.class);
		GPJoinerRetainerEntry objGPJoinerRetainerEntry= objGPPracticeJoiner.ClickonTab(joinerTab, GPJoinerRetainerEntry.class);
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(scriptKey,testDataFileName,testDataSheet,environment,"PensionEnrollmentNo");
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
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GPPracticeRole, environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		GPSalaryChangeForm objGPSalaryChangeForm= objGPPensionsHomePage.clickOnSalaryChangeTab();
		objGPSalaryChangeForm= objGPSalaryChangeForm.enterSalaryChangeDetails(testDataFileName,testDataSheet,scriptKey,environment,"PracticeName");
		System.out.println("Salary change details have been entered on form");
		objGPSalaryChangeForm= objGPSalaryChangeForm.clickOnSubmit();
		objGPSalaryChangeForm= objGPSalaryChangeForm.clickOnCancelSubmit();
		System.out.println("Cancel Submit functionality is working properly for key: "+key3);
		setAssertMessage("Cancel Submit functionality is working properly for key: "+key3, 5);
		objGPSalaryChangeForm= objGPSalaryChangeForm.clickOnSubmit();
		objGPSalaryChangeForm= objGPSalaryChangeForm.clickOnConfirmSubmit();
		MyNotification objMyNotification= objGPPHomePageNew.ClickOnPageHeader(upperTabName, MyNotification.class);
		notificationBody= notificationBody+contactName+".";
		boolean isNotificationPresent= objMyNotification.verifyNotificationText(notificationBody, "Salary Change Notification for_"+contactName);
		Assert.assertEquals(isNotificationPresent, true,"Notification is not present for contact "+contactName);
		setAssertMessage("Salary change details have been submitted and notification has been verified", 6);
		System.out.println("Salary change details have been submitted and notification has been verified");
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue.remove(GroupTypeValue);
		GroupTypeValue= Arrays.asList("fld");
		FieldValue.remove(FieldValue);
		FieldValue= Arrays.asList("Contact");
		ConditionValue.remove(ConditionValue);
		ConditionValue= Arrays.asList("Equals");
		ValueType.remove(ValueType);
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue.remove(ValueForFieldValue);
		ValueForFieldValue= Arrays.asList(contactName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Monthly Income and Pension Contributions");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithLongName();
			System.out.println("Entries have been deleted for Contact: "+ValueForFieldValue);
			setAssertMessage("Entries have been deleted from entity-Monthly Income and Pension Contributions for Contact: "+ValueForFieldValue, 5);
		}
		objAdvancedFindResult.closeWindow();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		// to be commented
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Annual Income and Pension Contributions");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{	
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!=null){
				objAdvancedFindResult= objAdvancedFindResult.updatePensionEstimateStatusForAnnualIncome("Approved","Estimated Income");
				System.out.println("Status has been updated under annual income entity");
				setAssertMessage("Status has been updated under annual income entity", 7);
			}
		}else{
			Assert.fail("Entry is not found in Annual Income entity for contact: "+contactName);
		}
		//data verification logic is to be written
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue.remove(GroupTypeValue);
		GroupTypeValue= Arrays.asList("fld");
		FieldValue.remove(FieldValue);
		FieldValue= Arrays.asList("Contact");
		ConditionValue.remove(ConditionValue);
		ConditionValue= Arrays.asList("Equals");
		ValueType.remove(ValueType);
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue.remove(ValueForFieldValue);
		ValueForFieldValue= Arrays.asList(contactName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Monthly Income and Pension Contributions");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				boolean isDataMatched= objAdvancedFindResult.verifySalaryChangeDetails(testDataFileName,testDataSheet,scriptKey,amtTierSheet);
				Assert.assertEquals(isDataMatched, true,"Salary change details are not matching with expected values");
				System.out.println("Salary change details are matching with expected values");
				for(String testKey: keys){
					setAssertMessage("Salary change details are matching with expected values for key: "+testKey, 8);
				}
				
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}
	
}
