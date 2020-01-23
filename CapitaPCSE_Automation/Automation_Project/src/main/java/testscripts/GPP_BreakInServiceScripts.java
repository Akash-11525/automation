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
import helpers.CommonFunctions;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.GPP.CI.GPPHomePageNew;
import pageobjects.GPP.Pensions.BreakInService.BreakInServiceEntryForm;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.Pensions.PracticeJoiner.GPJoinerRetainerEntry;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPensionsHomePage;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPracticeJoiner;
import pageobjects.GPP.Pensions.PracticeJoiner.JoinerApprovalForm;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import verify.Verify;

@Listeners(ListenerClass.class)
public class GPP_BreakInServiceScripts extends BaseClass {
	
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyBreakInServiceFlow(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String key="12345";
		//data deletion
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "BISPracticeName");
		//ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "PracticeName", 1);
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
		objGPJoinerRetainerEntry= objGPJoinerRetainerEntry.enterPractitionerData(2,environment,"BISPensionEnrNo");
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
		objJoinerApprovalForm= objJoinerApprovalForm.performAction("Approve",2);
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
		// this will delete the entry present in monthly pensions entity
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
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			objAdvancedFindResult= objAdvancedFindResult.deleteEntriesFromEntityWithLongName();
			System.out.println("Entries have been deleted for Contact: "+ValueForFieldValue);
			setAssertMessage("Entries have been deleted from entity-Monthly Income and Pension Contributions for Contact: "+ValueForFieldValue, 5);
		}
		objAdvancedFindResult.closeWindow();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		// this will verify the entries present in annual pensions entity
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
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Annual Income and Pension Contributions");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{	
			objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			objAdvancedFindResult= objAdvancedFindResult.updatePensionEstimateStatusForAnnualIncome("Approved","Estimated Income");
			System.out.println("Status has been updated under annual income entity");
			setAssertMessage("Status has been updated under annual income entity", 6);
		}else{
			Assert.fail("Entry is not found in Annual Income entity for contact: "+contactName);
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,"GPP");
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org("GPSOLO", environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab("Pensions", GPPensionsHomePage.class);
		BreakInServiceEntryForm objBreakInServiceEntryForm= objGPPensionsHomePage.clickOnBreakInserviceTab();
		List<String> dates= objBreakInServiceEntryForm.enterBreakInServiceDataWithoutEndDate();
		String startDate= dates.get(0);
		String endDate= dates.get(1);
		int monthCount= CommonFunctions.getMonthsBetweenDates(startDate, endDate);
		objBreakInServiceEntryForm= objBreakInServiceEntryForm.clickOnSubmit();
		objBreakInServiceEntryForm= objBreakInServiceEntryForm.clickOnConfirmSubmit();
		appRefNo= objBreakInServiceEntryForm.getApplicationRefNo();
		System.out.println("Break In Service data has been submitted having application number "+appRefNo);
		setAssertMessage("Break In Service data has been submitted having application number "+appRefNo, 7);
		tearDown(browser);
		Thread.sleep(2000);
		//verification of inactive entries
		setup(browser, environment, clientName,"CRMGPP","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue.remove(GroupTypeValue);
		GroupTypeValue= Arrays.asList("fld","fld");
		FieldValue.remove(FieldValue);
		FieldValue= Arrays.asList("Contact","Status");
		ConditionValue.remove(ConditionValue);
		ConditionValue= Arrays.asList("Equals","Equals");
		ValueType.remove(ValueType);
		ValueType= Arrays.asList("Lookup","Multiselect");
		ValueForFieldValue.remove(ValueForFieldValue);
		ValueForFieldValue= Arrays.asList(contactName,"Inactive");//contactName
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Monthly Income and Pension Contributions");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();
		objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{	Thread.sleep(3000);
			int recordCount= objAdvancedFindResult.getRecordCount();
			if(recordCount==(monthCount-1)){
				System.out.println("Count for inactive records is matching. Expected: "+(monthCount-1)+" Actual: "+recordCount);
				setAssertMessage("Count for inactive records is matching. Expected: "+(monthCount-1)+" Actual: "+recordCount, 8);
			}else{
				setAssertMessage("Count for inactive records is not matching. Expected: "+(monthCount-1)+" Actual: "+recordCount, 8);
				Assert.fail("Count for inactive records is not matching. Expected: "+(monthCount-1)+" Actual: "+recordCount);
			}
			//objAdvancedFindResult.closeWindow();
		}else{
			Assert.fail("Entry is not found in Monthly Income entity for contact: "+contactName);
		}
		
	}

}
