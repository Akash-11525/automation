package testscripts;

import java.awt.AWTException;
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
import pageobjects.LabInfo;
import pageobjects.ScreeningInfo;
import reporting.ListenerClass;

@Listeners(ListenerClass.class)
public class CS_ResultsScripts extends BaseClass
{
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser","environment", "clientName"})
	public void VerifyTestLabFields(String browser, String environment, String clientName) throws InterruptedException, IOException, AWTException
	{
		// Creating new patient details & closing browser.
				setup(browser, environment, clientName,"CRMCS","SUPERUSER");
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjCrmHome = ObjCrmHome.clickScreening();
				Contacts ObjContacts = ObjCrmHome.clickContactsTile();
				ObjContacts = ObjCrmHome.clickOnContactNewRecord();
				Thread.sleep(2000);
				String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
				ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 1);
				
		// Search for patient record  & select that record.
				ObjCrmHome = ObjCrmHome.clickScreening();
				ObjContacts = ObjCrmHome.clickContactsTile();
				ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
				
		// Verify Screening records against newly added patient.
				
				int screeningRec = ObjContacts.getScreeningRecords();
				System.out.println("Initial Screening Records: "+screeningRec);
				
				
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
				Thread.sleep(8000);	
				ObjGenRecall = ObjGenRecall.launchJob();
				ObjContacts = ObjCrmHome.ScreenContactMousehover();
				ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
				
				ContactPatient ContactPatient = ObjContacts.getScreeningRecords1();
				
				// Change reference to CRMHOME
				ObjCrmHome = ContactPatient.AddCervicalResult();
			 
			 ObjCrmHome = ObjCrmHome.clickScreening();
				ObjContacts = ObjCrmHome.clickContactsTile();
				ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);				
				ContactPatient ObjContactPatient = ObjContacts.getScreeningRecords1();
				ScreeningInfo ObjScreeningInfo = ObjContactPatient.screeningrecordclick();
				LabInfo ObjLabInfo = ObjScreeningInfo.CarvialTestResult();
		 		boolean f2 = ObjLabInfo.VerifyDistrictTextField();
         	if (f2)
         	{                       
                setAssertMessage("The District field not contain the strings.", 1);                                       
         	}
                         
         	Assert.assertFalse(verify.Verify.verifyFailure(), "The District field contain the strings.");  
		 		boolean f1 = ObjLabInfo.LBC_HPV();
		 		if (f1)
		 			{                  
		 				setAssertMessage("The field is with LBC and HPV option.", 2);
                 }
                      
		 			Assert.assertFalse(verify.Verify.verifyFailure(), "The field with not correct option.");
		 			
		 		         
			}

				
		
				
	}


