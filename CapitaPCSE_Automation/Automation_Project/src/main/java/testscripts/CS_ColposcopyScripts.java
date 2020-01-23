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
import org.testng.asserts.SoftAssert;

import browsersetup.BaseClass;
import browsersetup.Setup;
import helpers.CommonFunctions;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.CS.ColposcopyDischargeReport;
import pageobjects.CS.PortalHome;
import pageobjects.CS.UploadedColposcopyRecords;
import reporting.ListenerClass;
import utilities.CopyFileToNetworkLocation;
import utilities.ExcelUtilities;
import verify.Verify;
@Listeners(ListenerClass.class)
public class CS_ColposcopyScripts extends BaseClass{
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyColposcopyFlow(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNumber=8;
		
		// this logic will save the GP code in excel file
		
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNumber);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNumber);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNumber);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNumber);
		String valueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GP Name");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, valueForFieldValue);
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			String GPCode= ObjAdvancedFindResult.getGPCodeForCS();
		}
		
		// this will give the results based on the related entity condition

		colNumber=1;
		String colPrimaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectPrimaryEntity",colNumber);
		String RelatedGroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType",colNumber);
		String secondEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectSecondEntity",colNumber);
		String secondEntityGroup = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType2",colNumber);
		String secondEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectField2",colNumber);
		String secondEntityCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectfilterCondition",colNumber);
		String secondEntityValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectValueForField",colNumber);
		
		List<Object> contactFilter= Arrays.asList(RelatedGroupTypeValue,secondEntity,secondEntityGroup,secondEntityField,secondEntityCondn,secondEntityValue);
		int contactSize= contactFilter.size();
		
		String RelatedGroupTypeValue_3 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType3",colNumber);
		String thirdEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectThirdEntity",colNumber);
		String thirdEntityFieldType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType4",colNumber);
		String thirdEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectField3",colNumber);
		String thirdEntityCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectfilterCondition2",colNumber);
		String thirdEntityValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GPCode");
		
		List<Object> organisationFilter= Arrays.asList(RelatedGroupTypeValue_3,thirdEntity,thirdEntityFieldType,thirdEntityField,thirdEntityCondn,thirdEntityValue);
		int organisationSize= organisationFilter.size();
		
		List<Object> finalList= new ArrayList<Object>();
		finalList.addAll(contactFilter);
		finalList.addAll(organisationFilter);
		
		List<Integer> listSize= Arrays.asList(contactSize,organisationSize);
		
		// this will store the NHS numbers in a list
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterRelatedSearchCriteriaAndClickOnResult(colPrimaryEntity,finalList,listSize);
		List<String> NHSNumbers= ObjAdvancedFindResult.getNHSNumbers();
		System.out.println("Matched NHS numbers are: "+NHSNumbers);
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		//List<String> NHSNumbers= Arrays.asList("4089618967","9527011132");
		boolean isDataSaved  = ObjAdvancedFilter.getContactDetails("Contacts", "fld","NHS Number","Equals", NHSNumbers);
		Assert.assertEquals(isDataSaved, true,"Entries are not saved dynamically");
		System.out.println("Contact status is: "+isDataSaved);
/*		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			
		}*/
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni :
	 * 
	 * 13090- CSG_CODR_TC013- Colposcopy :  Validate the Colposcopy Functionality_E to E
	 * 
	 * 13081- CSG_CODR_TC004- Colposcopy :  Validate the Patient displayed record status on Colposcopy Discharge report
	 * 
	 * 13082- CSG_CODR_TC005- Colposcopy :  Validate the unmatched patient displayed records on Colposcopy Discharge report
	 * 
	 * 13083- CSG_CODR_TC006- Colposcopy :  Validate the failure reason displayed records on Colposcopy Discharge report

	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyColposcopyE2EFlow(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		List<String> keys= Arrays.asList("CSG_CODR_TC004","CSG_CODR_TC005","CSG_CODR_TC006","CSG_CODR_TC013");
		//String key="CSG_CODR_TC013";
		
		// CRM dynamic data saving starts
		
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNumber=8;
		
		// this logic will save the GP code in excel file
		
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNumber);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNumber);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNumber);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNumber);
		String valueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GP Name");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, valueForFieldValue);
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			String GPCode= ObjAdvancedFindResult.getGPCodeForCS();
		}
		
		// this will give the results based on the related entity condition

		colNumber=1;
		String colPrimaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectPrimaryEntity",colNumber);
		String RelatedGroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType",colNumber);
		String secondEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectSecondEntity",colNumber);
		String secondEntityGroup = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType2",colNumber);
		String secondEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectField2",colNumber);
		String secondEntityCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectfilterCondition",colNumber);
		String secondEntityValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectValueForField",colNumber);
		
		List<Object> contactFilter= Arrays.asList(RelatedGroupTypeValue,secondEntity,secondEntityGroup,secondEntityField,secondEntityCondn,secondEntityValue);
		int contactSize= contactFilter.size();
		
		String RelatedGroupTypeValue_3 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType3",colNumber);
		String thirdEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectThirdEntity",colNumber);
		String thirdEntityFieldType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType4",colNumber);
		String thirdEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectField3",colNumber);
		String thirdEntityCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectfilterCondition2",colNumber);
		String thirdEntityValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GPCode");
		
		List<Object> organisationFilter= Arrays.asList(RelatedGroupTypeValue_3,thirdEntity,thirdEntityFieldType,thirdEntityField,thirdEntityCondn,thirdEntityValue);
		int organisationSize= organisationFilter.size();
		
		List<Object> finalList= new ArrayList<Object>();
		finalList.addAll(contactFilter);
		finalList.addAll(organisationFilter);
		
		List<Integer> listSize= Arrays.asList(contactSize,organisationSize);
		
		// this will store the NHS numbers in a list
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterRelatedSearchCriteriaAndClickOnResult(colPrimaryEntity,finalList,listSize);
		List<String> NHSNumbers= ObjAdvancedFindResult.getNHSNumbers();
		System.out.println("Matched NHS numbers are: "+NHSNumbers);
		//ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		//Entry deletion logic to be written for uploading and manually added records
		for(int i=0;i<NHSNumbers.size();i++){
			String NHSNumber= NHSNumbers.get(i).replaceAll(" ", "").toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Discharge", "fld","NHS No","Equals", NHSNumber);
			boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
			if (flag2)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted before data entry", 14);
		System.out.println("All the entries have been deleted before data entry");
		//delete entry for manual records
		String matchedNHS= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "NHSNo", 2);
		List<String> deleteNHSNos= new ArrayList<String>();
		deleteNHSNos.add(matchedNHS);
		for(int i=0;i<deleteNHSNos.size();i++){
			String NHSNumber= deleteNHSNos.get(i).toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Discharge", "fld","NHS No","Equals", NHSNumber);
			boolean flag5 = ObjAdvancedFindResult.resultRecordFound();
			if (flag5)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 14);
		System.out.println("All the entries have been deleted");
		
		//List<String> NHSNumbers= Arrays.asList("4089618967","9527011132");
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		boolean isDataSaved  = ObjAdvancedFilter.getContactDetails("Contacts", "fld","NHS Number","Equals", NHSNumbers);
		Assert.assertEquals(isDataSaved, true,"Entries are not saved dynamically");
		System.out.println("Contact status is: "+isDataSaved);
		tearDown(browser);
		Thread.sleep(3000);
		// CRM dynamic data saving ends
		
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_Coloposcopy Administrator", environment);
		ColposcopyDischargeReport objColposcopyDischargeReport= objPortalHome.clickColposcopy();
		//this will delete the records present in the list before test execution
		boolean oldData= objColposcopyDischargeReport.deleteExistingRecords();
		Assert.assertTrue(oldData, "All existing records are not deleted.");
		System.out.println("All existing records are deleted");
		objColposcopyDischargeReport.clickOnAddButton();
		if(evidence)
		{
			objColposcopyDischargeReport.captureColposcopyScreenshot(keys.get(3)+" validation messages on Add Button");
		}
		List<String> actualErrorMessages= objColposcopyDischargeReport.AcutalErrormessage();
		System.out.println("Actual Error Messages on Add: "+actualErrorMessages);
		setAssertMessage("Actual Error Messages on Add: "+actualErrorMessages, 1);
		List<String> ExpectedErrorMessages = ExcelUtilities.getCellValuesInExcel("CSTESTDATA.xlsx", "ValidationMessages", 1);
		System.out.println("Expected Error Messages on Add: "+ExpectedErrorMessages);
		List<String> unmatchedList = CommonFunctions.compareStrings(actualErrorMessages, ExpectedErrorMessages);
		SoftAssert softAssertion= new SoftAssert();
				
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error list on Add click action is matching with expected list.");
			setAssertMessage("Actual error list on Add click action is matching with expected list.", 2);
		}
		
		else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Add click action is not matching with Expected Error list.");
		}
		List<String> manualEntries= objColposcopyDischargeReport.enterColposcopyData();
		setAssertMessage("Manual entries are added for NHS numbers: "+manualEntries, 3);
		System.out.println("Manual entries are added for NHS numbers: "+manualEntries);
		if(evidence)
		{
			for (String key : keys) {
				objColposcopyDischargeReport.captureColposcopyScreenshot(key+"_Manually added entries");
			}
			
		}
		objColposcopyDischargeReport= objColposcopyDischargeReport.uploadColposcopyFile();
		setAssertMessage("File has been uploaded successfully on portal",4);
		System.out.println("File has been uploaded successfully on portal");
		if(evidence)
		{
			for(String key: keys){
				objColposcopyDischargeReport.captureColposcopyScreenshot(key+"_Uploaded entries");
			}
			
		}
		List<Integer> recordCount= objColposcopyDischargeReport.validateFailureReason();
		setAssertMessage("Failure reason for all added patients has been verified successfully", 5);
		System.out.println("Failure reason for all added patients has been verified successfully");
		boolean isCountVerified= objColposcopyDischargeReport.verifyRecordCount(recordCount);
		Assert.assertEquals(isCountVerified, true, "Count is not matching");
		setAssertMessage("Record count for uploaded and manually added entries is verified", 6);
		System.out.println("Record count for uploaded and manually added entries is verified");
		if(evidence)
		{
			for(String key: keys){
				objColposcopyDischargeReport.captureColposcopyScreenshot(key+"_Record Count");
			}
			
		}
/*		boolean isButtonVisible=  objColposcopyDischargeReport.updateRecordAndCheckButtonVisibility();
		Verify.verifyEquals(isButtonVisible, true, "Buttons are not visible");
		setAssertMessage("Details are updated and Cance/Update buttons are visible", 7);
		System.out.println("Details are updated and Cancel/Update buttons are visible");
		if(evidence)
		{
			objColposcopyDischargeReport.captureColposcopyScreenshot(keys.get(3)+"_Record Updated");
		}*/
		boolean messageAndCancelDelete= objColposcopyDischargeReport.cancelDeleteAction();
		if(messageAndCancelDelete){
			setAssertMessage("Clicked on Cancel button under Delete window", 8);
			System.out.println("Clicked on Cancel button under Delete window");
		}else{
			setAssertMessage("Cancel button not clicked under Delete window", 9);
		}
		boolean isCountUpdated= objColposcopyDischargeReport.deleteRecord(recordCount);
		Verify.verifyEquals(isCountUpdated, true, "Count is not updated after deletion of record");
		setAssertMessage("Record is deleted and count is validated", 10);
		List<Integer> updatedCount= objColposcopyDischargeReport.getRecordCount();
		System.out.println("Count after deletion is: "+updatedCount);
		setAssertMessage("Count after deletion is: "+updatedCount, 11);
		List<String> matchedInputData= objColposcopyDischargeReport.getMatchedNHSNos();
		System.out.println("Matched details are: "+matchedInputData);
		boolean isSubmit= objColposcopyDischargeReport.clickOnSubmitButton();
		Assert.assertEquals(isSubmit, true, "Data is submitted");
		setAssertMessage("Colposcopy data has been submitted", 12);
		UploadedColposcopyRecords objUploadedColposcopyRecords= objColposcopyDischargeReport.clickOnUploadedRecordsTab();
		if(evidence)
		{
			objUploadedColposcopyRecords.captureColposcopyScreenshot(keys.get(3)+"_Before Search");
		}
		boolean isSorted= objUploadedColposcopyRecords.searchColposcopyRecords();
		Verify.verifyTrue(isSorted, "Records are not sorted");
		if(evidence)
		{
			objUploadedColposcopyRecords.captureColposcopyScreenshot(keys.get(3)+"_After Search");
		}
		setAssertMessage("Records have been fitered on upload coloscopy tab", 13);
		System.out.println("Records have been fitered on upload coloscopy tab");
		int totalRecords= updatedCount.get(0);
		boolean verified= objUploadedColposcopyRecords.verifyTabDetails(totalRecords);
		Assert.assertEquals(verified, true, "Grid contents are not matching");
		setAssertMessage("Tab details are verified", 14);
		System.out.println("Tab details are verified");
		
		tearDown(browser);
		Thread.sleep(3000);
		
		//Entry deletion logic to be written
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		for(int i=0;i<matchedInputData.size();i++){
			String NHSNumber= matchedInputData.get(i).replaceAll(" ", "").toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Discharge", "fld","NHS No","Equals", NHSNumber);
			boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
			if (flag2)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 15);
		System.out.println("All the entries have been deleted");
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni :13088- CSG_CODR_TC011- Colposcopy :  Validate the Batch Upload format on Colposcopy Discharge report
	 * 13085- CSG_CODR_TC008- Colposcopy :  Validate the CSV Outbound file for Current Upload List on Colposcopy Discharge report
	 * 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateBatchUploadFormat(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("CSG_CODR_TC011","CSG_CODR_TC008");

		// CRM dynamic data saving starts
		
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNumber=8;
		
		// this logic will save the GP code in excel file
		
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNumber);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNumber);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNumber);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNumber);
		String valueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GP Name");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, valueForFieldValue);
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			String GPCode= ObjAdvancedFindResult.getGPCodeForCS();
		}
		
		// this will give the results based on the related entity condition

		colNumber=1;
		String colPrimaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectPrimaryEntity",colNumber);
		String RelatedGroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType",colNumber);
		String secondEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectSecondEntity",colNumber);
		String secondEntityGroup = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType2",colNumber);
		String secondEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectField2",colNumber);
		String secondEntityCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectfilterCondition",colNumber);
		String secondEntityValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectValueForField",colNumber);
		
		List<Object> contactFilter= Arrays.asList(RelatedGroupTypeValue,secondEntity,secondEntityGroup,secondEntityField,secondEntityCondn,secondEntityValue);
		int contactSize= contactFilter.size();
		
		String RelatedGroupTypeValue_3 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType3",colNumber);
		String thirdEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectThirdEntity",colNumber);
		String thirdEntityFieldType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectoptGroupType4",colNumber);
		String thirdEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectField3",colNumber);
		String thirdEntityCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_ColposcopyAdvFind", "selectfilterCondition2",colNumber);
		String thirdEntityValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GPCode");
		
		List<Object> organisationFilter= Arrays.asList(RelatedGroupTypeValue_3,thirdEntity,thirdEntityFieldType,thirdEntityField,thirdEntityCondn,thirdEntityValue);
		int organisationSize= organisationFilter.size();
		
		List<Object> finalList= new ArrayList<Object>();
		finalList.addAll(contactFilter);
		finalList.addAll(organisationFilter);
		
		List<Integer> listSize= Arrays.asList(contactSize,organisationSize);
		
		// this will store the NHS numbers in a list
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterRelatedSearchCriteriaAndClickOnResult(colPrimaryEntity,finalList,listSize);
		List<String> NHSNumbers= ObjAdvancedFindResult.getNHSNumbers();
		System.out.println("Matched NHS numbers are: "+NHSNumbers);
		//ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		//Entry deletion logic to be written for uploading and manually added records
		for(int i=0;i<NHSNumbers.size();i++){
			String NHSNumber= NHSNumbers.get(i).replaceAll(" ", "").toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Discharge", "fld","NHS No","Equals", NHSNumber);
			boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
			if (flag2)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted before data entry", 1);
		System.out.println("All the entries have been deleted before data entry");
		//delete entry for manual records
		String matchedNHS= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "NHSNo", 2);
		List<String> deleteNHSNos= new ArrayList<String>();
		deleteNHSNos.add(matchedNHS);
		for(int i=0;i<deleteNHSNos.size();i++){
			String NHSNumber= deleteNHSNos.get(i).toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Discharge", "fld","NHS No","Equals", NHSNumber);
			boolean flag5 = ObjAdvancedFindResult.resultRecordFound();
			if (flag5)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 2);
		System.out.println("All the entries have been deleted");
		
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		boolean isDataSaved  = ObjAdvancedFilter.getContactDetails("Contacts", "fld","NHS Number","Equals", NHSNumbers);
		Assert.assertEquals(isDataSaved, true,"Entries are not saved dynamically");
		System.out.println("Contact status is: "+isDataSaved);
		tearDown(browser);
		Thread.sleep(3000);
		// CRM dynamic data saving ends
		
		
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_Coloposcopy Administrator", environment);
		ColposcopyDischargeReport objColposcopyDischargeReport= objPortalHome.clickColposcopy();
		//this will delete the records present in the list before test execution
		boolean oldData= objColposcopyDischargeReport.deleteExistingRecords();
		Assert.assertTrue(oldData, "All existing records are not deleted.");
		System.out.println("All existing records are deleted");
		List<String> manualEntries= objColposcopyDischargeReport.enterColposcopyData();
		setAssertMessage("Manual entries are added for NHS numbers: "+manualEntries, 3);
		System.out.println("Manual entries are added for NHS numbers: "+manualEntries);
		objColposcopyDischargeReport= objColposcopyDischargeReport.uploadColposcopyFile();
		setAssertMessage("File has been uploaded successfully on portal",4);
		System.out.println("File has been uploaded successfully on portal");
		if(evidence)
		{	
			for(String key:keys){
			objColposcopyDischargeReport.captureColposcopyScreenshot(key+"_uploaded records");
			}			
		}
		List<Integer> updatedCount= objColposcopyDischargeReport.getRecordCount();
		System.out.println("Count after deletion is: "+updatedCount);
		List<String> matchedInputData= objColposcopyDischargeReport.getMatchedNHSNos();
		System.out.println("Matched details are: "+matchedInputData);
		boolean isSubmit= objColposcopyDischargeReport.clickOnSubmitButton();
		Assert.assertEquals(isSubmit, true, "Data is submitted");
		setAssertMessage("Colposcopy data has been submitted", 5);
		System.out.println("Colposcopy data has been submitted");
		UploadedColposcopyRecords objUploadedColposcopyRecords= objColposcopyDischargeReport.clickOnUploadedRecordsTab();
		objUploadedColposcopyRecords= objUploadedColposcopyRecords.downloadColposcopyFile();
		setAssertMessage("Colposcopy file has been downloaded", 6);
		System.out.println("Colposcopy data has been downloaded");
		String destFilePath= CopyFileToNetworkLocation.createDirectory();
		String srcFilePath= Setup.downloadFilePath;
		List<String> copiedFiles= CopyFileToNetworkLocation.locateAndCopyToLocalFolder(srcFilePath, destFilePath, "Colposcopy", ".csv");
		setAssertMessage("Downloaded files are: "+copiedFiles , 7);
		System.out.println("Files are copied to path: "+destFilePath);
		System.out.println("Automated flow for keys: "+keys+" is completed; manual verification is being carried out externally");
		setAssertMessage("Automated flow for keys: "+keys+" is completed; manual verification is being carried out externally", 8);
		
		tearDown(browser);
		Thread.sleep(3000);
		//Entry deletion logic to be written
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		for(int i=0;i<matchedInputData.size();i++){
			String NHSNumber= matchedInputData.get(i).toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Discharge", "fld","NHS No","Equals", NHSNumber);
			boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
			if (flag2)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 9);
		System.out.println("All the entries have been deleted");
	}

}
