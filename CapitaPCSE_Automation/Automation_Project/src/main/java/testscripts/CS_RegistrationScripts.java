package testscripts;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import browsersetup.BaseClass;
import pageobjects.AdminJobs;
import pageobjects.ContactPatient;
import pageobjects.Contacts;
import pageobjects.CrmHome;
import pageobjects.GenNewPatientsScreeningRecords;
import pageobjects.GenerateRecalls;
import pageobjects.LoginPageCS;
import pageobjects.CS.PNLPage;
import pageobjects.CS.PatientpersonaldetailPortal;
import pageobjects.CS.PatientsearchPortal;
import pageobjects.CS.PortalHome;
import reporting.ListenerClass;
import verify.Verify;

@Listeners(ListenerClass.class)

public class CS_RegistrationScripts extends BaseClass {
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void MandatoryfieldOn_PatientRecord(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{
		setup(browser, environment, clientName,"CRM");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjCrmHome = ObjCrmHome.clickScreening();
		Contacts ObjContacts = ObjCrmHome.clickContactsTile();
		ObjContacts = ObjCrmHome.clickOnContactNewRecord();
		Thread.sleep(2000);
		String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
		// Verify Forname field error 
		ObjContacts = ObjContacts.DoubleClickOnSave_Close();
		String Errormessage_forename = ObjContacts.geterrormessageonPatientpage();
		String ExpectedErrorMessaage_forename = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "FornameError",1);
		if(Errormessage_forename.equalsIgnoreCase(ExpectedErrorMessaage_forename))
		{
			System.out.println("The Correct error message is printed for forename fields "+nhsNo+"");
			setAssertMessage("The Correct error message is printed for forename fields "+nhsNo+"", 1);
		}
		Verify.verifyTrue(Errormessage_forename.equalsIgnoreCase(ExpectedErrorMessaage_forename), "The incorrect error message is printed for forename fields  "+nhsNo+"");
		// Verify the Surname field error 
		ObjContacts = ObjContacts.Enterforename(1);
		ObjContacts = ObjContacts.DoubleClickOnSave_Close();
		String Errormessage_Surname = ObjContacts.geterrormessageonPatientpage();
		String ExpectedErrorMessaage_Surname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "SurnameError",1);
		if(Errormessage_Surname.equalsIgnoreCase(ExpectedErrorMessaage_Surname))
		{
			System.out.println("The Correct error message is printed for Surname fields "+nhsNo+"");
			setAssertMessage("The Correct error message is printed for Surname fields "+nhsNo+"", 2);
		}
		Verify.verifyTrue(Errormessage_Surname.equalsIgnoreCase(ExpectedErrorMessaage_Surname), "The incorrect error message is printed for Surname fields "+nhsNo+"");
		// Verify the DOB field error
		ObjContacts = ObjContacts.EnterSurname(1);
		ObjContacts = ObjContacts.DoubleClickOnSave_Close();
		String Errormessage_DOB = ObjContacts.geterrormessageonPatientpage();
		String ExpectedErrorMessaage_DOB = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "DOBError",1);
		if(Errormessage_DOB.equalsIgnoreCase(ExpectedErrorMessaage_DOB))
		{
			System.out.println("The Correct error message is printed for DOB fields "+nhsNo+"");
			setAssertMessage("The Correct error message is printed for DOB fields "+nhsNo+"", 3);
		}
		Verify.verifyTrue(Errormessage_DOB.equalsIgnoreCase(ExpectedErrorMessaage_DOB), "The incorrect error message is printed for DOB fields "+nhsNo+"");
		// Verify the Sex field error
		ObjContacts = ObjContacts.EnterDOB(1);
		ObjContacts = ObjContacts.DoubleClickOnSave_Close();
		String Errormessage_Sex = ObjContacts.geterrormessageonPatientpage();
		String ExpectedErrorMessaage_Sex = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "SexError",1);
		if(Errormessage_Sex.equalsIgnoreCase(ExpectedErrorMessaage_Sex))
		{
			System.out.println("The Correct error message is printed for Sex fields "+nhsNo+"");
			setAssertMessage("The Correct error message is printed for Sex fields "+nhsNo+"", 4);
		}
		Verify.verifyTrue(Errormessage_Sex.equalsIgnoreCase(ExpectedErrorMessaage_Sex), "The incorrect error message is printed for Sex fields "+nhsNo+"");
		// Verify the Postal Code field error
		ObjContacts = ObjContacts.EnterSex(1);
		ObjContacts = ObjContacts.DoubleClickOnSave_Close();
		String Errormessage_PostalCode = ObjContacts.geterrormessageonPatientpage();
		String ExpectedErrorMessaage_PostalCode  = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "PostcodeError",1);
		if(Errormessage_PostalCode.equalsIgnoreCase(ExpectedErrorMessaage_PostalCode))
		{
			System.out.println("The Correct error message is printed for Postal Code fields "+nhsNo+"");
			setAssertMessage("The Correct error message is printed for Postal Code fields "+nhsNo+"", 5);
		}
		Verify.verifyTrue(Errormessage_PostalCode.equalsIgnoreCase(ExpectedErrorMessaage_PostalCode), "The incorrect error message is printed for Postal Code fields "+nhsNo+"");
		ObjContacts = ObjContacts.EnterPostalcode(1);
		ObjContacts = ObjContacts.EnterNHSNo(nhsNo);
		ObjContacts = ObjContacts.DoubleClickOnSave_Close();
		ObjCrmHome = ObjCrmHome.clickScreening();
		ObjContacts = ObjCrmHome.clickContactsTile();
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
		Boolean PatientRecord = ObjContacts.verifyNHSNO(nhsNo);
		if(PatientRecord)
		{
			System.out.println("The patient date is saved after entered all mandatory fields  "+nhsNo+"");
			setAssertMessage("The patient date is saved after entered all mandatory fields  "+nhsNo+"", 6);
		}
		Verify.verifyTrue(PatientRecord, "The patient date is not saved after entered all mandatory fields  "+nhsNo+"");
	}
	
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void ManuallyEnteredScreeningRecord(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			// Creating new patient details & closing browser.
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
			int screeningRecBeforeAdd = ObjContacts.getScreeningRecords();
			System.out.println("Initial Screening Records: "+screeningRecBeforeAdd);
			String PatientName = ObjCrmHome.getpatientname();
		//	String PatientName = "suraj Gudekar";
			ObjCrmHome = ObjCrmHome.clickonQuicksearch();
			boolean labelonscreening = ObjCrmHome.verifylabelonscreening("Cease Reason","Defer Reason","Suspension Reason");
			if(labelonscreening)
			{
				System.out.println("One of them the Cease Reason,Defer Reason,Suspension Reason is displayed before select firstcall "+nhsNo);
				setAssertMessage("One of them the Cease Reason,Defer Reason,Suspension Reason is displayed before select firstcall " +nhsNo, 1);	
			}
			Verify.verifyTrue(labelonscreening, "The Cease Reason,Defer Reason,Suspension Reason is not displayed before select firstcall ");
			ObjCrmHome =ObjCrmHome.enterscreeningrecordmanually("First Call",1,PatientName);
			boolean labelonscreeningafterfirstcall = ObjCrmHome.verifylabelonscreening("Cease Reason","Defer Reason","Suspension Reason");
			if(!labelonscreeningafterfirstcall)
			{
				System.out.println("The Cease Reason,Defer Reason,Suspension Reason is not displayed after select firstcall "+nhsNo);
				setAssertMessage("The Cease Reason,Defer Reason,Suspension Reason is not displayed after select firstcall " +nhsNo, 1);	
			}
			Verify.verifyTrue((!labelonscreeningafterfirstcall), "The Cease Reason,Defer Reason,Suspension Reason is displayed after select firstcall ");
			ObjCrmHome = ObjCrmHome.clickSaveonScreening();
			Thread.sleep(3000);
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
			
	// Verify Screening records against newly added patient.
			
			int screeningRecAfterAdd = ObjContacts.getScreeningRecords();
			System.out.println("After add Screening Records: "+screeningRecAfterAdd);
			if(screeningRecAfterAdd == screeningRecBeforeAdd +1)
			{
				System.out.println("The screening data is added by manually" +nhsNo);
				setAssertMessage("The screening data is added by manually" +nhsNo, 1);	
			}
			Verify.verifyTrue((screeningRecAfterAdd == screeningRecBeforeAdd +1), "The screening data is not added ");
			Boolean ScreeningRecord_TestDuedate = ObjContacts.verifyTestDuedate();
			if(ScreeningRecord_TestDuedate)
			{
				System.out.println("The correct Test Due date is displayed" +nhsNo);
				setAssertMessage("The correct Test Due date is displayed" +nhsNo, 1);	
			}
			Verify.verifyTrue(ScreeningRecord_TestDuedate, "The incorrect Test Due date is displayed ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
					

		}
		
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void EstimatedDOB_Portal(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
			String NHSNOWithSpace = ObjContacts.getNHSnumberwithspace();
			tearDown(browser);
			/*String nhsNo = "1043188401";
			String NHSNOWithSpace = "104 318 8401";*/

			setup(browser, environment, clientName,"CS");
			LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
			PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
			//PortalHome ObjPortalHome = new PortalHome(getDriver());
			PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
			ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(nhsNo);
			PatientpersonaldetailPortal ObjPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(NHSNOWithSpace);
			String ToolTipValue =ObjPatientpersonaldetailPortal.verifyToolTipValue();
			System.out.println("The actual Tip top value "+ToolTipValue);
			String ExpectedToolTipValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "PatientSearchDOB", "DOBexpectedError",1);
			System.out.println(ExpectedToolTipValue);
			Boolean ToolTip = ObjPatientpersonaldetailPortal.verifyToolTipvalue(ToolTipValue, ExpectedToolTipValue);
			if(ToolTip)
			{
				System.out.println("The correct ToolTip message is printed" +nhsNo);
				setAssertMessage("The correct ToolTip message is printed" +nhsNo, 1);
			}
			Verify.verifyTrue(ToolTip, "The incorrect ToolTip message is printed");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
			
		}
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void OneCallRecallActive1(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
			int screeningRecBeforeAdd = ObjContacts.getScreeningRecords();
			System.out.println("Initial Screening Records: "+screeningRecBeforeAdd);
		//	String PatientName = ObjCrmHome.getpatientname();
			//String PatientName = "suraj Gudekar";
			ObjCrmHome = ObjCrmHome.clickonScreeningrecordPlussign();
			ObjCrmHome =ObjCrmHome.enterscreeningrecordPlussign("First Call",1);		
			ObjCrmHome = ObjCrmHome.clickSaveonScreeningWithPlussign();
			Thread.sleep(3000);
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);			
	// Verify Screening records against newly added patient.
			
			int screeningRecAfterAdd = ObjContacts.getScreeningRecords();
			System.out.println("After add Screening Records: "+screeningRecAfterAdd);
			if(screeningRecAfterAdd == screeningRecBeforeAdd +1)
			{
				System.out.println("The screening data is added by manually" +nhsNo);
				setAssertMessage("The screening data is added by manually" +nhsNo, 1);	
			}
			Verify.verifyTrue((screeningRecAfterAdd == screeningRecBeforeAdd +1), "The screening data is not added ");
			ObjCrmHome = ObjContacts.clickSettings();
			ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
			AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
			GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
			Thread.sleep(2000);
			ObjGNPSR = ObjGNPSR.launchJob();
			Thread.sleep(2000);
			
			ObjAdminJobs = ObjAdminJobs.Adminbutton();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate Recalls");
			GenerateRecalls ObjGenRecall = ObjAdminJobs.clickOnFirstRecord();
			Thread.sleep(8000);	
			ObjGenRecall = ObjGenRecall.launchJob();
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
			int screeningRecAfterRecall = ObjContacts.getScreeningRecords();
			System.out.println("After add Screening Records After Recall: "+screeningRecAfterRecall);
			
		}
		// Suraj G; The two patient records is generated if we used the same data except postal code
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void DuplicateDetection_ExcludingPostalCode(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails_Duplicate(nhsNo, 5);
			
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo2 = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails_Duplicate(nhsNo2, 6);
			String Fname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Forename", 5);
			String Lname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Surname", 5);
			String Fullname = (Fname+" "+Lname);
			System.out.println(Fullname);
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchPatient(Fullname);
			int records = ObjContacts.verifyresultrecord();
			if(records == 2)
			{
				System.out.println("The two records is generate if we entered different postal code" +Fullname);
				setAssertMessage("The two records is generate if we entered different postal code" +Fullname, 1);				
			}
			Verify.verifyTrue((records == 2), "The two record is not created if we entered different postal code ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
			ObjContacts = ObjContacts.deleteallrecord();
			
		}
		// Suraj G : This test cases verify the One call recall on Call recall history table
		// Added manually call recall after generated first call
		// if PNL date is today then it listed pn current week on PNL 
		// The First call is resolved after generated new call by manually.
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void OneCallRecallActive(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
			//String nhsNo = "1420771531";
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
			String PortalNHSNO = ObjContacts.getNHSnumberwithspace();
			
	// Search for patient record  & select that record.
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);

			
	// Run Admin job for Generate New Patient Screening Records.
				//ObjCrmHome = ObjCrmHome.clickSettings();
			ObjCrmHome = ObjContacts.clickSettings();
			ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
			AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
			GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
			Thread.sleep(2000);
			ObjGNPSR = ObjGNPSR.launchJob();
			Thread.sleep(2000);
			
			ObjAdminJobs = ObjAdminJobs.Adminbutton();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate Recalls");
			GenerateRecalls ObjGenRecall = ObjAdminJobs.clickOnFirstRecord();
			ObjGenRecall = ObjGenRecall.launchJob();
			Thread.sleep(80000);	
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
			
			ContactPatient ObjContactPatient = ObjContacts.getScreeningRecords1();
		
			//System.out.println("Initial Screening Records: "+screeningRec);
			ObjContactPatient = ObjContactPatient.AddManuallyCallRecallRecords("Recall");
			int TotalNoRows = ObjContactPatient.gettotalnoOfRows_CallRecallHistory();
			if(TotalNoRows == 2)
			{
			Boolean FirstRow = ObjContactPatient.verifyStatus("Recall","Active",1);
			if(FirstRow)
			{
				System.out.println("The Added Manually Call Recall is in active status " +nhsNo);
				setAssertMessage("The Added Manually Call Recall is in active status" +nhsNo, 1);	
			}
			Verify.verifyTrue(FirstRow, "The Added Manually Call Recall is not in active status ");
			Boolean SecondRow = ObjContactPatient.verifyStatus("First call","Resolved",2);
			if(SecondRow)
			{
				System.out.println("The Previously added call recall is in Resolved status and only one call is active " +nhsNo);
				setAssertMessage("The Previously added call recall is in Resolved status and Only one call is active" +nhsNo, 2);	
			}
			Verify.verifyTrue(SecondRow, "The Previously added call recall is in Resolved status.");
			}
			
			tearDown(browser);
		// Verify the generated NHS number is displayed on PNL portal.
			setup(browser, environment, clientName,"CS");
			LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
			PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
			//PortalHome ObjPortalHome = new PortalHome(getDriver());
			PNLPage ObjPNLPage = ObjPortalHome.clickPNL();			
			Boolean PresentNHSNumber = ObjPNLPage.VerifyCurrentWeek(PortalNHSNO);
			if(PresentNHSNumber)
			{
				System.out.println("The NHSno "+nhsNo+ " is present on Current week " );
				setAssertMessage("The NHSno "+nhsNo+ " is present on Current week ", 3);		
			}
			Verify.verifyTrue(PresentNHSNumber, "The NHSno "+nhsNo+ " is not present on Current week ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		
		//Suraj G : By using this test script , we are able to add multiple cervial Test Result.
		
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void MulitpleTestResult(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
	//		String nhsNo = "1753298253";
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
	// Search for patient record  & select that record.
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);			
	// Run Admin job for Generate New Patient Screening Records.
	//ObjCrmHome = ObjCrmHome.clickSettings();
			ObjCrmHome = ObjContacts.clickSettings();
			ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
			AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
			GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
			Thread.sleep(2000);
			ObjGNPSR = ObjGNPSR.launchJob();
			Thread.sleep(2000);	
			ObjAdminJobs = ObjAdminJobs.Adminbutton();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate Recalls");
			GenerateRecalls ObjGenRecall = ObjAdminJobs.clickOnFirstRecord();
			ObjGenRecall = ObjGenRecall.launchJob();
			Thread.sleep(8000);	
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);			
			ContactPatient ObjContactPatient = ObjContacts.getScreeningRecords1();
			ObjContactPatient = ObjContactPatient.clickOnQuicksearch_CervialTestResult();
			ObjContactPatient = ObjContactPatient.AddCervicalResult_QuickSearch();
			int TotalNoRows_callTestResult = ObjContactPatient.VerifyCervialTestResult();
			if(TotalNoRows_callTestResult == 1)
			{
				System.out.println("The NHSno "+nhsNo+ " is present on Current week " );
				setAssertMessage("The NHSno "+nhsNo+ " is present on Current week ", 3);			
			}
			Verify.verifyTrue((TotalNoRows_callTestResult == 1), "The NHSno "+nhsNo+ " is not present on Current week ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");		
			
		}
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
		@Parameters({"browser", "environment", "clientName"})
		public void ScreeningRecordData(String browser ,String environment, String clientName) throws InterruptedException, IOException
		{
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
	//		String nhsNo = "1753298253";
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjCrmHome.clickOnContactNewRecord();
			Thread.sleep(2000);
			String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
			ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
	// Run Admin job for Generate New Patient Screening Records.
			//ObjCrmHome = ObjCrmHome.clickSettings();
			ObjCrmHome = ObjContacts.clickSettings();
			ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
			AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
			ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
			GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
			Thread.sleep(2000);
			ObjCrmHome = ObjGNPSR.launchJob_CRM();
			Thread.sleep(2000);	
			ObjCrmHome = ObjCrmHome.clickonCRM();
			ObjCrmHome = ObjCrmHome.clickScreening();
			ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
			Boolean TestDuedate = ObjContacts.CalculatedTestDuedate(30);
			if(TestDuedate)
			{
				System.out.println("The correct Test Due date is displayed" +nhsNo );
				setAssertMessage("The correct Test Due date is displayed "+nhsNo, 1);
			}
			Verify.verifyTrue(TestDuedate, "The incorrect Test Due date is displayed ");
			Boolean ScreeningType = ObjContacts.VerifyScreeningType("Cervial");
			{
				System.out.println("The correct screening Type is displayed" +nhsNo );
				setAssertMessage("The correct screening Type is displayed "+nhsNo, 2);
			}
			Verify.verifyTrue(ScreeningType, "The incorrect screening Type is displayed ");
			Boolean Status = ObjContacts.VerifyStatusOnScreening("Active");
			{
				System.out.println("The correct Status is appered" +nhsNo );
				setAssertMessage("The correct Status is appered"+nhsNo, 3);
			}
			Verify.verifyTrue(Status, "The incorrect Status is appered ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
}
