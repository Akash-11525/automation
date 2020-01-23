package testscripts;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import pageobjects.CS.HPV_Vaccination;
import pageobjects.CS.PatientpersonaldetailPortal;
import pageobjects.CS.PortalHome;
import pageobjects.CS.UploadedHPVRecords;
import reporting.ListenerClass;
import utilities.CopyFileToNetworkLocation;
import utilities.ExcelUtilities;
import verify.Verify;
@Listeners(ListenerClass.class)
public class CS_HPVScripts extends BaseClass {
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni :16352- CSG_HPUP_TC015- HPV upload: GP user: Validate HPV tab and fields on HPV submission tab.*
	 * 16338- CSG_HPUP_TC021- HPV upload: GP user: Validate HPV vaccinations list on Patient details screen.
	 * 16334- CSG_HPUP_TC017- HPV upload: GP user: Validate Matched and Un matched Patient record in Current upload list section on hpv vaccination screen.
	 * 16337- CSG_HPUP_TC020 -HPV upload: GP user: Validate the HPV records in CRM
	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyHPVFields(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		List<String> keys= Arrays.asList("CSG_HPUP_TC015","CSG_HPUP_TC017","CSG_HPUP_TC021","CSG_HPUP_TC020");
		// to save GP Code in excel
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNumber=8;
		String GPCode="";
		
		// this logic will save the GP code in excel file
		
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNumber);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNumber);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNumber);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNumber);
		String valueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GP Name");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, valueForFieldValue);
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		boolean isCRMVerified= false;
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			GPCode= ObjAdvancedFindResult.getGPCodeForCS();
		}
		
		//Entry deletion logic to be written
		String NHS1= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", 1);
		String NHS2= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", 2);
		String NHS3= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", 5);
		List<String> deleteEntries= new ArrayList<String>();
		deleteEntries.add(NHS1);
		deleteEntries.add(NHS2);
		deleteEntries.add(NHS3);
		for(int i=0;i<deleteEntries.size();i++){
			String NHSNumber= deleteEntries.get(i).toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Vaccinations", "fld","NHS No","Equals", NHSNumber);
			boolean flag5 = ObjAdvancedFindResult.resultRecordFound();
			if (flag5)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 1);
		System.out.println("All the entries have been deleted");
		
		//tearDown(browser);
		//ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind(); to be uncommented
		colNumber=1;
		String colPrimaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectPrimaryEntity",colNumber);
		String groupType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectoptGroupType",colNumber);
		String secondEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "SelectSecondEntity",colNumber);
		String secondEntityGrpType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectoptGroupType2",colNumber);
		String secondEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectField",colNumber);
		String secondEntityFilterCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectfilterCondition",colNumber);
		String secondEntityValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GPCode");
		
		
		List<Object> organisationFilter= Arrays.asList(groupType,secondEntity,secondEntityGrpType,secondEntityField,secondEntityFilterCondn,secondEntityValue);
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterRelatedSearchCriteriaAndClickOnResult_HPV(colPrimaryEntity,organisationFilter);
		List<String> NHSNumbers= ObjAdvancedFindResult.getNHSNumbers_HPV();
		System.out.println("Matched NHS numbers are: "+NHSNumbers);
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		boolean isDataSaved  = ObjAdvancedFilter.getContactDetails_HPV("Contacts", "fld","NHS Number","Equals", NHSNumbers);
		Assert.assertEquals(isDataSaved, true,"Entries are not saved dynamically");
		tearDown(browser);
		Thread.sleep(3000);
		// this will process the portal flow as per TC
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_GP Practice Administrator", environment);
		HPV_Vaccination objHPV_Vaccination= objPortalHome.clickHPV();
		boolean oldData= objHPV_Vaccination.deleteExistingRecords();
		Assert.assertTrue(oldData, "All existing records are not deleted.");
		System.out.println("All existing records are deleted");
		objHPV_Vaccination.clickOnAddButton();
		if(evidence)
		{
			objHPV_Vaccination.captureHPVScreenshot(keys.get(0)+" validation messages on Add Button");
		}
		List<String> actualErrorMessages= objHPV_Vaccination.AcutalErrormessage();
		System.out.println("Actual Error Messages on Add: "+actualErrorMessages);
		setAssertMessage("Actual Error Messages on Add: "+actualErrorMessages, 2);
		List<String> ExpectedErrorMessages = ExcelUtilities.getCellValuesInExcel("CSTESTDATA.xlsx", "ValidationMessages", 2);
		System.out.println("Expected Error Messages on Add: "+ExpectedErrorMessages);
		List<String> unmatchedList = CommonFunctions.compareStrings(actualErrorMessages, ExpectedErrorMessages);
		SoftAssert softAssertion= new SoftAssert();
		if(unmatchedList.isEmpty())
		{
			System.out.println("Actual error list on Add click action is matching with expected list.");
			setAssertMessage("Actual error list on Add click action is matching with expected list.", 3);
		}else
		{
			softAssertion.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Add click action is not matching with Expected Error list.");
		}
		boolean isTypeVerified= objHPV_Vaccination.validateClaimTypes(2);
		Assert.assertEquals(isTypeVerified, true, "Claim types are mismatching.");
		setAssertMessage("Claim types are verified for key: "+keys.get(0), 4);
		TreeMap<String,Boolean> validationStatus= objHPV_Vaccination.enterHPVDataAndVerifyValidation(6,7,keys.get(0));
		for(Map.Entry<String, Boolean> data:validationStatus.entrySet())
		{
			String NHSNumber= data.getKey();
			Boolean isValidation= data.getValue();
			System.out.println("For NHS no.: "+NHSNumber+", validation message display value is "+isValidation);
			setAssertMessage("For NHS no.: "+NHSNumber+", validation message display value is "+isValidation, 5);
		}
		setAssertMessage("NHS number validation is done and no validations are being shown on saving valid records", 6);
		System.out.println("NHS number validation is done and no validations are being shown on saving valid records");
		boolean oldData2= objHPV_Vaccination.deleteExistingRecords();
		Assert.assertTrue(oldData2, "All existing records are not deleted.");
		int colCount= ExcelUtilities.getColumnCountFromExcel("CSTESTDATA.xlsx", "HPVTestData");
		objHPV_Vaccination= objHPV_Vaccination.enterHPVData(1,(colCount-2));
		System.out.println("Patient details are added");
		List<Integer> recordCount= objHPV_Vaccination.getCountFromPatientGrid();
		boolean isCountVerified= objHPV_Vaccination.verifyRecordCount(recordCount);
		if(evidence)
		{
			objHPV_Vaccination.captureHPVScreenshot(keys.get(1)+"_Record count");
		}
		Assert.assertEquals(isCountVerified, true, "Count is not matching");
		setAssertMessage("Record count for entries is verified", 7);
		System.out.println("Record count for entries is verified");
		List<String> matchedInputData= objHPV_Vaccination.getMatchedPatientData();
		System.out.println("Matched details are: "+matchedInputData);
		List<String> unmatchedInputData= objHPV_Vaccination.getUnmatchedPatientData();
		System.out.println("Unmatched details are: "+unmatchedInputData);
		boolean isVerified= objHPV_Vaccination.verifyPatientDetailsFromPatientSearch();
		assertEquals(isVerified, true, "Patient details are not matching with entered data");
		PatientpersonaldetailPortal objPatientpersonaldetailPortal= new PatientpersonaldetailPortal(getDriver());
		if(evidence)
		{
			objPatientpersonaldetailPortal.capturePatientDetailsOnHPVTab(keys.get(2)+"_Matched Patient Details");
		}
		setAssertMessage("Patient details are matching with entered data for key"+keys.get(2), 8);
		System.out.println("Patient details are matching with entered data for key"+keys.get(2));

		System.out.println("Patient details are matching with entered data");
		tearDown(browser);
		Thread.sleep(3000);
		
		
		//Verification of data from CRM
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 9;
		String primaryEntity2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String matchedNHSNumber= matchedInputData.get(0).replaceAll(" ", "").toString();
		String unmatchedNHSNumber= unmatchedInputData.get(0).replaceAll(" ", "").toString();
		
		// to be removed after verification
		//List<String> matchList= Arrays.asList("4723841687","Maria","19","05/08/1989","G - Gardasil","24/02/2018");
		//List<String> unmatchList= Arrays.asList("6113899551","Silva","Jani","22/02/1984","V - Vaccine administered but type not known","24/02/2018");
		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity2, GroupTypeValue2,FieldValue2,ConditionValue2, matchedNHSNumber);

		boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
		if (flag2)
		{

			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(3,1);
			if(title!= null)
			{
				isCRMVerified= ObjAdvancedFindResult.verifyHPVData(matchedInputData,"Matched",evidence);
				ObjAdvancedFindResult.closeWindow();
				Assert.assertEquals(isCRMVerified, true);
				setAssertMessage("Values are verified for matched records for key: "+keys.get(3), 9);
				System.out.println("Values are verified for matched records for key: "+keys.get(3));
			}
		}
		//ObjAdvancedFindResult.closeWindow();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity2, GroupTypeValue2,FieldValue2,ConditionValue2, unmatchedNHSNumber);
		boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
		if (flag3)
		{

			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				isCRMVerified= ObjAdvancedFindResult.verifyHPVData(unmatchedInputData,"Unmatched",evidence);
				ObjAdvancedFindResult.closeWindow();
				Assert.assertEquals(isCRMVerified, true);
				setAssertMessage("Values are verified for unmatched records for key: "+keys.get(3), 10);
				System.out.println("Values are verified for unmatched records for key: "+keys.get(3));
			}
		}
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Contacts", "fld","NHS Number","Equals", matchedNHSNumber);
		boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
		if (flag4)
		{

			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				isCRMVerified= ObjAdvancedFindResult.verifyContactData(matchedInputData,evidence);
				ObjAdvancedFindResult.closeWindow();
				Assert.assertEquals(isCRMVerified, true);
				setAssertMessage("Values are verified with Contacts entity for key: "+keys.get(3), 11);
				System.out.println("Values are verified with Contacts entity for key: "+keys.get(3));
			}
		}

		//Delete entries code is to be written
		
		//Entry deletion logic to be written
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		String matchedNHS= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", 1);
		String matchedNHS2= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", 2);
		String unmatchedNHS= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", 5);
		List<String> deleteNHSNos= new ArrayList<String>();
		deleteNHSNos.add(matchedNHS);
		deleteNHSNos.add(matchedNHS2);
		deleteNHSNos.add(unmatchedNHS);
		for(int i=0;i<deleteNHSNos.size();i++){
			String NHSNumber= deleteNHSNos.get(i).toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Vaccinations", "fld","NHS No","Equals", NHSNumber);
			boolean flag5 = ObjAdvancedFindResult.resultRecordFound();
			if (flag5)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 12);
		System.out.println("All the entries have been deleted");
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni :16336- CSG_HPUP_TC019- HPV upload: GP user: Validate filters on Uploaded HPV records tab *
	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifySearchOnHPVTab(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key="CSG_HPUP_TC019";
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_GP Practice Administrator", environment);
		HPV_Vaccination objHPV_Vaccination= objPortalHome.clickHPV();
		objHPV_Vaccination.clickOnRecordsTab();
		UploadedHPVRecords objUploadedHPVRecords= objHPV_Vaccination.clickOnRecordsTab();
		if(evidence)
		{
			objUploadedHPVRecords.captureHPVTabScreenshot(key+"_Before search");
		}
		boolean isSorted= objUploadedHPVRecords.searchHPVRecords();
		Verify.verifyTrue(isSorted, "Records are not sorted");
		if(evidence)
		{
			objUploadedHPVRecords.captureHPVTabScreenshot(key+"_After search");
		}
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni :13410- CSG_HPUP_TC014- E2E HPV upload: Validate overall functionality of HPV bulk upload. *
	 * 13404- CSG_HPUP_TC008- HPV upload: Validate Duplicate Patient from the file uploaded
	 * 16335- CSG_HPUP_TC018- HPV upload: GP user: Validate CSV file generated in Uploaded HPV Records.
	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void verifyE2EHPVUpload(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		//String key="CSG_HPUP_TC014";
		List<String> keys= Arrays.asList("CSG_HPUP_TC014","CSG_HPUP_TC008","CSG_HPUP_TC018");
		// to save GP Code in excel
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNumber=8;
		String GPCode="";
		
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
			ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			GPCode= ObjAdvancedFindResult.getGPCodeForCS();
		}

		colNumber=1;
		String colPrimaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectPrimaryEntity",colNumber);
		String groupType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectoptGroupType",colNumber);
		String secondEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "SelectSecondEntity",colNumber);
		String secondEntityGrpType = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectoptGroupType2",colNumber);
		String secondEntityField = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectField",colNumber);
		String secondEntityFilterCondn = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "CS_HPVAdvFind", "selectfilterCondition",colNumber);
		String secondEntityValue = utilities.ExcelUtilities.getKeyValueFromExcel("ConfigurationDetails.xlsx", "Config", "GPCode");
		
		
		List<Object> organisationFilter= Arrays.asList(groupType,secondEntity,secondEntityGrpType,secondEntityField,secondEntityFilterCondn,secondEntityValue);
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		ObjAdvancedFindResult = ObjAdvancedFilter.enterRelatedSearchCriteriaAndClickOnResult_HPV(colPrimaryEntity,organisationFilter);
		List<String> NHSNumbers= ObjAdvancedFindResult.getNHSNumbers_HPV();
		System.out.println("Matched NHS numbers are: "+NHSNumbers);
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		boolean isDataSaved  = ObjAdvancedFilter.getContactDetails_HPV("Contacts", "fld","NHS Number","Equals", NHSNumbers);
		Assert.assertEquals(isDataSaved, true,"Entries are not saved dynamically");
		ObjAdvancedFindResult.closeWindow();
		for(int i=0;i<NHSNumbers.size();i++){
			String NHSNumber= NHSNumbers.get(i).toString();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult("Vaccinations", "fld","NHS No","Equals", NHSNumber);
			boolean flag5 = ObjAdvancedFindResult.resultRecordFound();
			if (flag5)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.deleteEntriesFromEntity();
				System.out.println("Entries have been deleted for NHS number: "+NHSNumber);
			}
			ObjAdvancedFindResult.closeWindow();
		}
		setAssertMessage("All the entries have been deleted", 1);
		System.out.println("All the entries have been deleted");
		
		tearDown(browser);
		Thread.sleep(3000);

		//this will upload a file post creating a CSV file
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_GP Practice Administrator", environment);
		HPV_Vaccination objHPV_Vaccination= objPortalHome.clickHPV();
		//objHPV_Vaccination.clickOnRecordsTab();
		objHPV_Vaccination= objHPV_Vaccination.uploadHPVFile("Valid");
		boolean uploadMessage= objHPV_Vaccination.verifyUploadMessage();
		if(evidence)
		{
			objHPV_Vaccination.captureHPVScreenshot(keys.get(0)+"_File uploading in progress");
		}
		Verify.verifyEquals(uploadMessage, true);
		UploadedHPVRecords objUploadedHPVRecords= objHPV_Vaccination.clickOnRecordsTab();
		if(evidence)
		{
			for(String key: keys){
				objHPV_Vaccination.captureHPVScreenshot(key+"_File Uploading completed");
			}		
		}
		objUploadedHPVRecords= objUploadedHPVRecords.downloadHPVFile();
		setAssertMessage("HPV file has been downloaded", 6);
		System.out.println("HPV data has been downloaded");
		String destFilePath= CopyFileToNetworkLocation.createDirectory();
		String srcFilePath= Setup.downloadFilePath;
		List<String> copiedFiles= CopyFileToNetworkLocation.locateAndCopyToLocalFolder(srcFilePath, destFilePath, "HPVVaccination", ".csv");
		setAssertMessage("Downloaded files are: "+copiedFiles , 7);
		System.out.println("Files are copied to path: "+destFilePath);
		System.out.println("Automated flow for keys: "+keys+" is completed; manual verification is being carried out externally");
		setAssertMessage("Automated flow for keys: "+keys+" is completed; manual verification is being carried out externally", 8);
		
	}
	
	/*	***********************************************************************************************************
	 * Akshay Sohoni :13400- CSG_HPUP_TC004- HPV upload: Validate the uploaded file. *
	*******************************************************************************************************************/
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","CS","Regression","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void validateFileHPVStructure(String browser, String environment, String clientName, boolean evidence) throws InterruptedException, IOException, ParseException{
		String key="CSG_HPUP_TC004";
		boolean uploadMessage= false;
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_GP Practice Administrator", environment);
		HPV_Vaccination objHPV_Vaccination= objPortalHome.clickHPV();
		//objHPV_Vaccination.clickOnRecordsTab();
		objHPV_Vaccination= objHPV_Vaccination.uploadHPVFile("NonCSV");
		uploadMessage= objHPV_Vaccination.verifyUploadMessage();
		if(evidence)
		{
			objHPV_Vaccination.captureHPVScreenshot(key+"_non CSV File message");
		}
		Verify.verifyEquals(uploadMessage, true);
		setAssertMessage("Non CSV mesage is verified", 1);
		System.out.println("Non CSV mesage is verified");
		objHPV_Vaccination= objHPV_Vaccination.uploadHPVFile("InvalidCount");
		uploadMessage= objHPV_Vaccination.verifyUploadMessage();
		if(evidence)
		{
			objHPV_Vaccination.captureHPVScreenshot(key+"_InvalidCount File message");
		}
		Verify.verifyEquals(uploadMessage, true);
		setAssertMessage("Invalid count mesage is verified", 2);
		System.out.println("Invalid count mesage is verified");
		objHPV_Vaccination= objHPV_Vaccination.uploadHPVFile("MandatoryField");
		uploadMessage= objHPV_Vaccination.verifyUploadMessage();
		if(evidence)
		{
			objHPV_Vaccination.captureHPVScreenshot(key+"MandatoryField File message");
		}
		Verify.verifyEquals(uploadMessage, true);
		setAssertMessage("Mandatory mesage is verified", 3);
		System.out.println("Mandatory mesage is verified");
	}
		

}
