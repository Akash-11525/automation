package testscripts;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import pageobjects.AdminJobs;
import pageobjects.CallRecallRecord;
import pageobjects.Cohortselect;
import pageobjects.ContactPatient;
//import pageobjects.AdminJobs;
import pageobjects.Contacts;
import pageobjects.CrmHome;
import pageobjects.GenNewPatientsScreeningRecords;
import pageobjects.GenerateRecalls;
import reporting.ListenerClass;

@Listeners(ListenerClass.class)
public class CS_CohortsScripts  extends BaseClass
{
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({ "browser","environment", "clientName" })
	public void verifyCreateCohortRule(String browser,String environment, String clientName) throws InterruptedException, IOException
	{
		// Creating new patient details & closing browser.
				setup(browser, environment, clientName,"CRM");
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				Cohortselect ObjCohortselect = ObjCrmHome.Cohorttabselect();
				//ObjCohortselect = ObjCohortselect.clickNewRule();
				String rulename = ObjCohortselect.Cohortselectrulename();
				
				ObjCohortselect = ObjCohortselect.Addingcohortrule(rulename);
				/*String FirstRulePriorityExcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "FirstRulePriority");
				System.out.println(FirstRulePriorityExcel);*/
				ObjCohortselect = ObjCohortselect.addapplicableCCG("1");
				
				Boolean RulePresentflag = ObjCohortselect.rulePresent(rulename);
				
				System.out.println(RulePresentflag);
				
				if (RulePresentflag)
	         	{                       
	                setAssertMessage("The added Cohort Selection Rule stored successfully.", 1);                                       
	         	}
	                         
	         	Assert.assertFalse(verify.Verify.verifyFailure(), "Cohort selection rule is not added.");  
	         	
	         	ObjCohortselect.deleteCreatedRule();
	         	setAssertMessage("The added Cohort Selection Rule deleted successfully.", 2);        
				
			

}
	
	// Amit R: TC8439 - TC_CS_23235_Select CCG on Cohort Selection Rule - Where a GP is included in more than one Cohort Rule
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"}, enabled = false)
	@Parameters({ "browser","environment", "clientName" })
	public void verifySelectHighPriorityRule(String browser,String environment, String clientName) throws InterruptedException, IOException
	{
		// Creating new patient details & closing browser.
				setup(browser, environment, clientName, "CRM");
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				String FirstCohortRuleNameExcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "FirstCohortruleName");
				System.out.println(FirstCohortRuleNameExcel);
				//Cohortselect ObjCohortselect = ObjCrmHome.Cohorttabselect();
				//ObjCohortselect = ObjCohortselect.DeleteexistingCohortrule(FirstCohortRuleNameExcel);
				
				
				Cohortselect ObjCohortselect = ObjCrmHome.Cohorttabselect();
			
				ObjCohortselect = ObjCohortselect.Addingcohortrule(FirstCohortRuleNameExcel);
				String FirstRulePriorityExcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "FirstRulePriority");
				System.out.println(FirstRulePriorityExcel);
				
				ObjCohortselect = ObjCohortselect.addapplicableCCG(FirstRulePriorityExcel);
				
				ObjCohortselect = ObjCrmHome.Cohorttabselect();
				String SecondCohortruleNameExcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "SecondCohortruleName");
				System.out.println(SecondCohortruleNameExcel);
				ObjCohortselect = ObjCohortselect.Addingcohortrule(SecondCohortruleNameExcel);
				String SecondRulePriorityExcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "SecondRulePriority");
				System.out.println(FirstRulePriorityExcel);
				
				ObjCohortselect = ObjCohortselect.addapplicableCCG(SecondRulePriorityExcel);
				
				Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
				ObjCrmHome = ObjCrmHome.clickScreening();
				ObjContacts  = ObjCrmHome.clickContactsTile();
				
				ObjContacts = ObjCrmHome.clickOnContactNewRecord();
				String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
				ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo , 2);
				
		
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
				Thread.sleep(2000);	
				ObjGenRecall = ObjGenRecall.launchJob();
				Thread.sleep(60000);
				
				
				ObjContacts = ObjCrmHome.ScreenContactMousehover();
				ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
				/*int screeningRecUp = ObjContacts.getScreeningRecords();
				System.out.println(screeningRecUp);
				
				if (screeningRecUp > 1)
				{
					System.out.println("Patient added for screening.");
				}
				
				else
				{
					System.out.println("Patient is not added for screening.");
				}
						
				*/
				ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();
				
				//ObjContactss1 = ObjContactss1.CallRecallHistory();
				
				CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
				
				boolean f2 = ObjCallRecallRecord.VerifyCohortPriorityRule();
				
				if (f2)
            	{                       
                   setAssertMessage("The Highest Cohort priority Rule get applied for NHS NO -  " +nhsNo, 1);                                       
            	}
                            
            	Assert.assertFalse(verify.Verify.verifyFailure(), "The Highest Cohort priority Rule get applied for NHS NO - " +nhsNo);  

}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"} )
	@Parameters({ "browser","environment", "clientName" })
	public void UpdatedCohortRuleApplyonPatient(String browser,String environment, String clientName) throws InterruptedException, IOException
	{
	/*	CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();	
		Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
		ObjCrmHome = ObjContacts.clickSettings();
		ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
		AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
		ObjAdminJobs = ObjAdminJobs.Adminbutton();
		ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate Recalls");
		ObjGenRecall = ObjAdminJobs.clickOnFirstRecord();*/
			
		
		// Deleting Existing Cohort Rule
				setup(browser, environment, clientName, "CRM");
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();								
				String FirstCohortRuleNameExcel =  utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Cohort", "FirstCohortruleName",2);
				System.out.println(FirstCohortRuleNameExcel);
				String SecondCohortruleNameExcel =  utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Cohort", "SecondCohortruleName",2);
				System.out.println(SecondCohortruleNameExcel);				
				Cohortselect ObjCohortselect = ObjCrmHome.Cohorttabselect();
				ObjCohortselect = ObjCohortselect.DeleteexistingCohortrule(FirstCohortRuleNameExcel);			
				ObjCohortselect = ObjCohortselect.DeleteexistingCohortrule(SecondCohortruleNameExcel);	
				
		// Adding the First Cohort Rule 
			//	ObjCohortselect = ObjCrmHome.Cohorttabselect(); 
				ObjCohortselect = ObjCohortselect.Addingcohortrule(FirstCohortRuleNameExcel);
				String FirstRulePriorityExcel =  utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Cohort", "FirstRulePriority",2);
				System.out.println(FirstRulePriorityExcel);
				ObjCohortselect = ObjCohortselect.addapplicableCCG(FirstRulePriorityExcel);
				
		// Adding the Second Cohort Rule	
				ObjCohortselect = ObjCrmHome.Cohorttabselect();
				ObjCohortselect = ObjCohortselect.Addingcohortrule(SecondCohortruleNameExcel);
				String SecondRulePriorityExcel =  utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Cohort", "SecondRulePriority",2);
				System.out.println(SecondRulePriorityExcel);				
				ObjCohortselect = ObjCohortselect.addapplicableCCG(SecondRulePriorityExcel);
		
		// Adding Patient	
					
				Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
				ObjCrmHome = ObjCrmHome.clickScreening();
				ObjContacts = ObjCrmHome.clickContactsTile();				
				ObjContacts = ObjCrmHome.clickOnContactNewRecord();
				String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
				ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 2);
				
		// Running Batch Job 
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
				Thread.sleep(2000);	
				ObjGenRecall = ObjGenRecall.launchJob();
				Thread.sleep(60000);
				
		//Verify the Cohort Rule apply 
				ObjContacts = ObjCrmHome.ScreenContactMousehover();
				ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
			
				
				ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();					
				CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
				String CohortSelectionruleName = ObjCallRecallRecord.CohortRuleApplyOnPatient();
				System.out.println(CohortSelectionruleName);
				if(CohortSelectionruleName.equalsIgnoreCase(FirstCohortRuleNameExcel))
				{
			// Updating cohort Rule
					Thread.sleep(5000);
					ObjCohortselect = ObjCrmHome.Cohorttabselect();
					ObjCohortselect = ObjCohortselect.searchFirstCohortRule(FirstCohortRuleNameExcel);
					String UpdateCohortpriority =  utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Cohort", "UpdateFirstRulePriority",2);
					System.out.println(UpdateCohortpriority);	
					ObjCrmHome = ObjCohortselect.updateFirstCohortRule(UpdateCohortpriority);
					Thread.sleep(5000);
					ObjCrmHome = ObjContacts.clickSettings();
					ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
					ObjAdminJobs = ObjCrmHome.clickAdminJobs();
					ObjAdminJobs = ObjAdminJobs.Adminbutton();
					ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate Recalls");
					ObjGenRecall = ObjAdminJobs.clickOnFirstRecord();
					Thread.sleep(2000);	
					ObjGenRecall = ObjGenRecall.launchJob();
					Thread.sleep(60000);
					tearDown(browser);
		
	/*	//By Amit:
		setup(browser, environment, clientName, "CRM");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();	
		Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
		String nhsNo = "1014274281";
		String SecondCohortruleNameExcel = "LowestPriorityName";*/
		
			// Verify existing cohort rule apply on patient
					setup(browser, environment, clientName, "CRM");
					ObjCrmHome  = new CrmHome(getDriver());
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();	
					
					ObjContacts = ObjCrmHome.ScreenContactMousehover();
					ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
					
					
					//ContactPatient
					ObjContactss1 = ObjContacts.getScreeningRecords1();					
					//CallRecallRecord
					ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
					 String CohortRuleNameOnExistingAfterupdate = ObjCallRecallRecord.CohortRuleApplyOnPatient();
					 System.out.println(CohortRuleNameOnExistingAfterupdate);
					 if(CohortRuleNameOnExistingAfterupdate.equalsIgnoreCase(SecondCohortruleNameExcel) )
					 {
						 setAssertMessage("The updated cohort rule " +CohortRuleNameOnExistingAfterupdate+" is apply on " +nhsNo, 1); 
					 }
					
					 Assert.assertFalse(verify.Verify.verifyFailure(),"The updated cohort rule " +CohortRuleNameOnExistingAfterupdate+" is apply on " +nhsNo);  
				}
				

		
	}
}


	