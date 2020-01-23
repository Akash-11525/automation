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
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.Pensions.PracticeJoiner.GPJoinerRetainerEntry;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPensionsHomePage;
import pageobjects.GPP.Pensions.PracticeJoiner.GPPracticeJoiner;
import pageobjects.GPP.Pensions.PracticeJoiner.JoinerApprovalForm;
import pageobjects.GPP.Pensions.Solo.SoloIncomeApproval;
import pageobjects.GPP.Pensions.Solo.SoloIncomeEntry;
import pageobjects.GPP.Pensions.Solo.SoloWorkHomePage;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class GPP_SoloScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni : - 19286- TC_SL_06- Solo income entry screen - Employment details Verify the employment details
	 * 19291- TC_SL_11- Solo income approval form - Pre-populated fields Verify the pre-populated fields in approval form
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","CloneSanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyEmploymentDetails(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String scriptKey= "TC_SL_06";
		List<String> values= ExcelUtilities.getScriptParameters("GPSolo",scriptKey);
		System.out.println("Test Data value size is: "+values.size());
		
		String testDataFileName= ExcelUtilities.getExcelParameterByModule("GPSolo","TestDataFile");
		String testDataSheet= ExcelUtilities.getExcelParameterByModule("GPSolo","TestDataSheet");
		String amtTierSheet= ExcelUtilities.getExcelParameterByModule("GPSolo","SoloAmtTierSheet");
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String []keyArray= strKeys.split(",");
		String role= values.get(2);
		String[]roleArray= role.split(",");
		String CRMModule= values.get(3);
		String CRMUSER= values.get(4);
		String GPOrganisation= values.get(5);
		String tab= values.get(6);
		String[]tabArray= tab.split(",");
		String commissionerOrg= values.get(7);
		String applType= values.get(8);
		String applStatus= values.get(9);
		String[]applStatusArray= applStatus.split(",");
		String strDays= values.get(10);
		int daysToAdd= Integer.parseInt(strDays);
		String action= values.get(11);
		String headerTabName= values.get(12);
		String upperTabName= values.get(13);
		String notificationBody= values.get(14);
		
		//data deletion
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter= new AdvancedFilter(getDriver());
		boolean flag=false;
		String contactName= ConfigurationData.getRefDataDetails(environment, "PracticeName"); 
		//ExcelUtilities.getKeyValueByPosition(testDataFileName, testDataSheet, "GPSoloName", scriptKey);
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
		String key2= keyArray[1].toString();
		List<String> roles= Arrays.asList(roleArray);
		List<String> tabs= Arrays.asList(tabArray);
		List<String> applStatuses= Arrays.asList(applStatusArray);
		String GPPracticeRole= roles.get(0);
		String NHSCommissioner= roles.get(1);
		String SoloProvider= roles.get(2);
		String GP_Solo= roles.get(3);
		String GPHomeTab= tabs.get(0);
		String pensionsTab= tabs.get(1);
		String joinerTab= tabs.get(2);
		String soloTab= tabs.get(3);
		String beforeApproval= applStatuses.get(0);
		String afterApproval= applStatuses.get(1);
		setup(browser, environment, clientName,module);
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		SelectOrganisation objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GPPracticeRole, environment);
		GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		//GPPHomePageNew objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(GPOrganisation, GPPHomePageNew.class);
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
		//objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(commissionerOrg, GPPHomePageNew.class);
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
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objGPPHomePageNew= ObjLoginScreen.logintoGPP(SoloProvider, environment,GPPHomePageNew.class);
		objGPPensionsHomePage= objGPPHomePageNew.ClickonTab(GPHomeTab, GPPensionsHomePage.class);
		SoloWorkHomePage objSoloWorkHomePage= objGPPensionsHomePage.clickOnSoloWorkTab();
		SoloIncomeEntry objSoloIncomeEntry= objSoloWorkHomePage.clickOnIncomeEntryTab();
		objSoloIncomeEntry= objSoloIncomeEntry.enterSoloMonthlyDetails(scriptKey,testDataFileName,testDataSheet,environment);
		System.out.println("Past and Future dates have been entered successfully for key: "+key);
		setAssertMessage("Past and Future dates have been entered successfully for key: "+key, 5);
		boolean pensionableMatched= objSoloIncomeEntry.verifyPensionableAmount(scriptKey,testDataFileName,testDataSheet);
		Assert.assertEquals(pensionableMatched, true, "Pensionable amount is not matching");
		System.out.println("Pensionable amount under 'C' section is matching with expected amount for key: "+key);
		setAssertMessage("Pensionable amount under 'C' section is matching with expected amount for key: "+key, 6);
		boolean contributionMatched= objSoloIncomeEntry.verifyEmployeeContribution(scriptKey,testDataFileName,testDataSheet,amtTierSheet);
		Assert.assertEquals(contributionMatched, true, "Contribution amount is not matching");
		System.out.println("Contribution amount under 'D' section and Tier Criteria is matching with expected amount for key: "+key);
		setAssertMessage("Contribution amount under 'D' section and Tier Criteria is matching with expected amount for key: "+key, 7);
		boolean contriAndAdminLevy= objSoloIncomeEntry.verifyContributionAndLevy(scriptKey,testDataFileName,testDataSheet);
		Assert.assertEquals(contriAndAdminLevy, true, "Contribution and Admin amount is not matching");
		System.out.println("Contribution and Admin Levy value under 'H' section is matching with expected amount for key: "+key);
		setAssertMessage("Contribution and Admin Levy value under 'H' section is matching with expected amount for key: "+key, 8);
		boolean isTotalNHSContribution= objSoloIncomeEntry.verifyTotalContribution(scriptKey,testDataFileName,testDataSheet,amtTierSheet);
		Assert.assertEquals(isTotalNHSContribution, true, "Total contribution amount is not matching");
		System.out.println("Total Contribution value under 'I' section is matching with expected amount for key: "+key);
		setAssertMessage("Total Contribution value under 'I' section is matching with expected amount for key: "+key, 9);
		objSoloIncomeEntry= objSoloIncomeEntry.clickOnSubmit();
		objSoloIncomeEntry= objSoloIncomeEntry.clickOnCancelSubmit();
		objSoloIncomeEntry= objSoloIncomeEntry.clickOnSubmit();
		objSoloIncomeEntry= objSoloIncomeEntry.clickOnConfirmSubmit();
		String refNumber= objSoloIncomeEntry.getIncomeRefNo();
		System.out.println("Application has been submitted with reference number: "+refNumber+" for key: "+key);
		setAssertMessage("Application has been submitted with reference number: "+refNumber+" for key: "+key, 10);
		if(evidence){
			objSoloIncomeEntry.captureSoloIncomeEntrySnaps(key+"_submitted SOLO record");
		}
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName,module);
		ObjLoginScreen = new LoginScreen(getDriver());
		objSelectOrganisation= ObjLoginScreen.logintoGPP_Org(GP_Solo, environment);
		objGPPHomePageNew= objSelectOrganisation.selectOrganisation_DropDown(environment, GPPHomePageNew.class,"GPPContractor");
		MyNotification objMyNotification= objGPPHomePageNew.ClickOnPageHeader(upperTabName, MyNotification.class);
		SoloIncomeApproval objSoloIncomeApproval= objMyNotification.clickOnReviewLink(refNumber, SoloIncomeApproval.class, key+"_NotificationSnaps", notificationBody);
		boolean isApplicationPresent= objSoloIncomeApproval.verifyPresenceOfDocument(refNumber);
		Assert.assertEquals(isApplicationPresent, true, "Total contribution amount is not matching on Approval screen");
		System.out.println("Application number is present on Notification menu for key: "+key2);
		setAssertMessage("Application number is present on Notification menu for key: "+key2, 11);
		pensionableMatched= objSoloIncomeApproval.verifyPensionableAmount(scriptKey,testDataFileName,testDataSheet);
		Assert.assertEquals(pensionableMatched, true, "Pensionable amount is not matching on Approval screen");
		System.out.println("Pensionable amount under 'C' section of Approval screen is matching with expected amount for key: "+key2);
		setAssertMessage("Pensionable amount under 'C' section of Approval screen is matching with expected amount for key: "+key2, 12);
		contributionMatched= objSoloIncomeApproval.verifyEmployeeContribution(scriptKey,testDataFileName,testDataSheet,amtTierSheet);
		Assert.assertEquals(contributionMatched, true, "Contribution amount is not matching on Approval screen");
		System.out.println("Contribution amount under 'D' section of Approval screen and Tier Criteria is matching with expected amount for key: "+key2);
		setAssertMessage("Contribution amount under 'D' section of Approval screen and Tier Criteria is matching with expected amount for key: "+key2, 13);
		isTotalNHSContribution= objSoloIncomeApproval.verifyTotalContribution(scriptKey,testDataFileName,testDataSheet,amtTierSheet);
		Assert.assertEquals(isTotalNHSContribution, true, "Total contribution amount is not matching on Approval screen");
		System.out.println("Total Contribution value under 'I' section under Approval screen is matching with expected amount for key: "+key2);
		setAssertMessage("Total Contribution value under 'I' section under Approval screen is matching with expected amount for key: "+key2, 14);
		objSoloIncomeApproval= objSoloIncomeApproval.clickOnApprove(scriptKey,testDataFileName,testDataSheet);
		objSoloIncomeApproval= objSoloIncomeApproval.clickOnCancelApprove();
		objSoloIncomeApproval= objSoloIncomeApproval.clickOnApprove(scriptKey,testDataFileName,testDataSheet);
		objSoloIncomeApproval= objSoloIncomeApproval.clickOnConfirmApprove();
		boolean isButtonDisabled= objSoloIncomeApproval.verifyButtonState();
		Assert.assertEquals(isButtonDisabled, true, "Buttons are not disabled on Approval screen");
		System.out.println("Buttons are disabled and record number "+refNumber+" has been approved successfully for key: "+key2);
		setAssertMessage("Buttons are disabled and record number "+refNumber+" has been approved successfully for key: "+key2, 15);
		tearDown(browser);//2018/2019-SOLO-00001
		Thread.sleep(2000);
		setup(browser, environment, clientName,CRMModule,CRMUSER);	
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter= new AdvancedFilter(getDriver());
		flag=false;
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		GroupTypeValue= Arrays.asList("fld");
		FieldValue= Arrays.asList("Contact");
		ConditionValue= Arrays.asList("Equals");
		ValueType= Arrays.asList("Lookup");
		ValueForFieldValue= Arrays.asList(contactName);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Solo and Locum Income");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValue, FieldValue, ConditionValue, ValueType, ValueForFieldValue);
		ObjAdvancedFilter.clickResults();	
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
		flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				Thread.sleep(5000);
				Screenshot.TakeSnap(getDriver(), key2+"_Record Approved"+refNumber);
				System.out.println("Entry is present for document: "+refNumber+" for key: "+key2);
				setAssertMessage("Entry is present for document: "+refNumber+" for key: "+key2, 16);
				
			}else{
				Assert.assertNotNull(title, "Title extracted from result record is NULL.");
			}
		}else{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}

}
