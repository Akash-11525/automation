package testscripts;
import java.awt.AWTException;
import java.io.IOException;
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
import pageobjects.PerformerAppCase;
import pageobjects.PL.CreateNewApp;
import pageobjects.PL.HealthClearance;
import pageobjects.PL.PerformerList;
import pageobjects.PL.PoliceCheck;
import pageobjects.PL.ProcessorPLHelpers;
import pageobjects.PL.SubmitApplication;
import pageobjects.PL.TeamPreference;
import pageobjects.PL.Undertaking;
import pageobjects.ProcessPL.CaseofficerReview;
import pageobjects.ProcessPL.FacetoFaceAppointment;
import pageobjects.ProcessPL.MangerApproval;
import pageobjects.ProcessPL.NHSCViewApp;
import pageobjects.ProcessPL.NHSDecision;
import pageobjects.ProcessPL.NetTeamCheck;
import pageobjects.ProcessPL.PCSECheck;
import pageobjects.ProcessPL.ProcessViewApp;
import pageobjects.ProcessPL.ThirdPartyCheck;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

	@Listeners(ListenerClass.class)
	public class PL_Process_PerformerScripts extends BaseClass
	{
		//Suraj G : Create appointment with application complete
		//Test Case 4068
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
		@Parameters({"browser", "environment", "clientName","evidence"})
	public void Process_F2F_Others(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
		{
			String key = "PL_PA_36";
			setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());		
			String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
			System.out.println(performer_PerformerPortal);
			  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
			//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
				boolean ApplicationONCRM = onbjPerformerAppCase.VerifyApplicationNumber("MedicalAutoFirst", "Submitted" );
				tearDown(browser);
			
				if(!ApplicationONCRM)
				{
					setup(browser, environment, clientName, "PL");
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
					String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
					tearDown(browser);
					setup(browser, environment, clientName, "ActivationLink");
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
					tearDown(browser);
					setup(browser, environment, clientName, "PL");			
					ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
					tearDown(browser);
				}
			}
			setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
		//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
			
			ObjCrmHome  = new CrmHome(getDriver());	
		//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
				
			
				/*LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
				PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);*/
				
				String PortalUser = onbjPerformerAppCase.clickOnCaseCommenceCheck_PortalUser(ApplicationNumber, "Submitted" );
		//		String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
				setup(browser, environment, clientName, "CRMPL","PLASSIGNERINT");
				PerformerList ObjPerformerList  = new PerformerList(getDriver());
				PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
				objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
				String ApplicationStatus = objPCSECheck.verifyApplicationStatus();

				if(ApplicationStatus.equalsIgnoreCase("Performing Detailed Checks"))
				{
					LoginScreen objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
				//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag11 = ObjAdvancedFindResult.resultRecordFound();
					if (flag11)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);

						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
					//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
						//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							//objNetTeamCheck = objNetTeamCheck.clickonnetTeamcheck();
							boolean isdisplayed = objNetTeamCheck.verifyCancelappointment();
							if(isdisplayed)
							{
								System.out.println("The Appointment is done properly and we have selected location as others" + ApplicationNumber);
								setAssertMessage("The Appointment is done properly and we have selected location as others "+ ApplicationNumber, 2);	
							}
							if(evidence)
							{
								objNetTeamCheck.AppointmentScreenshot(key+"AppointmentDone");
							}
							Verify.verifyTrue(isdisplayed, "The Appointment is not done properly and we have selected location as others " + ApplicationNumber);

						}	
						Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
						System.out.println("Application Status is appearing correctly.");
					}
				}
			}
		}
		// Amit : This test will take at last

		//Suraj G : Create appointment with application complete
		//Test Case 4232 - We select Application incomplete on PCSE check -> Returned status 
		//Go back to PL = Delete one pdf from DBS tab and added new one pdf
		// Submit the applicationa and assign to case officer with complete application.
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
		@Parameters({"browser", "environment", "clientName","evidence"})
	public void ApplicationIncomplete(String browser ,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
		{
			String key = "PL_PA_87";
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
			
					setup(browser, environment, clientName, "PL");
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
					tearDown(browser);					setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
					String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
					tearDown(browser);
					setup(browser, environment, clientName, "ActivationLink");
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
					tearDown(browser);
					setup(browser, environment, clientName, "PL");			
					ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
					tearDown(browser);
				
			
			setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
			String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
			CrmHome ObjCrmHome  = new CrmHome(getDriver());	
		//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			AdvancedFindResult  ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag11 = ObjAdvancedFindResult.resultRecordFound();
			if (flag11)
			{
				PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
				LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
				PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
				PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
				objPCSECheck = objPCSECheck.selectCaseofficerAppInComplete();
				String ApplicationStatus = objPCSECheck.verifyApplicationStatus();
				if(ApplicationStatus.equalsIgnoreCase("Returned"))
				{
					System.out.println("The Returned status after incomplete application is selected");
					setAssertMessage("The Returned status after incomplete application is selected ", 1);	
				}
				if(evidence)
				{
					objPCSECheck.screenshotAppStatus(key+"ReturnStatus");
				}
				Verify.verifyNotEquals(ApplicationStatus.equalsIgnoreCase("Returned"), "The Returned status is not appear after incomplete application is selected ");

			}	
			tearDown(browser);
			//	String ApplicationNumber = "PL2421";
			setup(browser, environment, clientName, "PL");
			LoginScreen objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			String AppNoOnStatus = ObjPerformerList.getApplicationNumberwithstatus("Under consideration");
			String ApplicationNumberAfterReturned = ObjPerformerList.getApplicationNumberwithstatus("Returned");
			if(evidence)
			{
				ObjPerformerList.screenshotAppStatus_portal(key+"Draft");
			}
			if(ApplicationNumberAfterReturned.equalsIgnoreCase(ApplicationNumber))
			{
				System.out.println("The Applicatin is in returned status after incomplete selected"+ApplicationNumber);
				setAssertMessage("The Applicatin is not in returned status after incomplete selected "+ApplicationNumber, 1);
			}
			Verify.verifyNotEquals(ApplicationNumberAfterReturned.equalsIgnoreCase(ApplicationNumber), "The Applicatin is not in returned status after incomplete selected  "+ApplicationNumber);

			CreateNewApp objCreateNewApp = ObjPerformerList.clickonEdit();
			PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
			objPoliceCheck = objPoliceCheck.deletePdf();
			objPoliceCheck = objPoliceCheck.AddPdf();
			objCreateNewApp = objPoliceCheck.ClickOnSave_Submit();
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			objCreateNewApp = objTeamPreference.ClickOnSave_Submit();
			HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
			objCreateNewApp = objHealthClearance.ClickOnSave_Submit();
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			objCreateNewApp = objUndertaking.ClickOnSave_Submit();
			SubmitApplication objSubmitApplication= objCreateNewApp.ClickOnSubmitApp();
			objCreateNewApp = objSubmitApplication.ClickOnSave_Submit();
			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_NumberAfteradddoc = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			System.out.println("The Application Number is " +Application_NumberAfteradddoc );
			setAssertMessage("The Application Number is " +Application_NumberAfteradddoc, 24);	
			if(evidence)
			{
				ObjPerformerList.screenshotAppStatus_portal(key+"Submitted");
			}
			if(ApplicationNumber.equalsIgnoreCase(Application_NumberAfteradddoc))
			{
				System.out.println("The Application is submitted after added new document " +ApplicationNumber );
				setAssertMessage("The Application is submitted after added new document " +ApplicationNumber, 23);		
			}
			Verify.verifyNotEquals(ApplicationNumber.equalsIgnoreCase(Application_NumberAfteradddoc), "The Application is submitted after added new document"+ApplicationNumber);
			tearDown(browser);
			Thread.sleep(5000);
			//	String ApplicationNumber = "PL2421";
			setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
			ObjCrmHome  = new CrmHome(getDriver());
		//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);		
			//AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
				
			
				objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
				ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
				PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
				objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			
				String StatusAfterReurned = objPCSECheck.verifyApplicationStatus();
				if(evidence)
				{
					objPCSECheck.screenshotAppStatus(key+"Performing Detailed Checks");
				}
				if(StatusAfterReurned.equalsIgnoreCase("Performing Detailed Checks"))
				{
					System.out.println("Reviewer to assign case officer to application" +ApplicationNumber);
					setAssertMessage("Reviewer to assign case officer to application"+ApplicationNumber,1);
				}
				Verify.verifyNotEquals(StatusAfterReurned.equalsIgnoreCase("Performing Detailed Checks"), "Reviewer is not to assign case officer to application");
				Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	

			}
		}


		//Suraj G : Test Cases 4233 and 4234
		// Process PL - Send Application to manager for approval 
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
		@Parameters({"browser", "environment", "clientName","evidence"})
    public void SendAppDossier_Approval_PCSEManagerApprove(String browser ,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
		{
			List<String> keys = Arrays.asList("PL_PA_88", "PL_PA_89");
			
			setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
			CrmHome ObjCrmHome  = new CrmHome(getDriver());		
			String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
			System.out.println(performer_PerformerPortal);
	//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);			
			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
				boolean ApplicationONCRM = onbjPerformerAppCase.VerifyApplicationNumber("MedicalAutoFirst", "Submitted" );
				tearDown(browser);
			
				if(!ApplicationONCRM)
				{
					setup(browser, environment, clientName, "PL");
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
					String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
					tearDown(browser);
					setup(browser, environment, clientName, "ActivationLink");
					ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
					tearDown(browser);
					setup(browser, environment, clientName, "PL");			
					ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
					tearDown(browser);
				}
			}
			setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
			String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
			ObjCrmHome  = new CrmHome(getDriver());
	//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
				
			
				LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
				PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
				PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
				objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
				
				objLoginScreen = objPCSECheck.logout();
				tearDown(browser);
				
				System.out.println("Case office assigned successfully.");
				//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
				//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
				setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
				ObjCrmHome  = new CrmHome(getDriver());
			//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag11 = ObjAdvancedFindResult.resultRecordFound();
				if (flag11)
				{
					onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
					objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

					// Login to Portal Application & enter third Party details & logout
					ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

					ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
					//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
					objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
					objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
					objLoginScreen = objThirdPartyCheck.logout();
					tearDown(browser);
					System.out.println("Third Party checks are successfully.");
					// Login in CRM with NET TEAM USER & click on task
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());
			//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
					if (flag2)
					{
						 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						 objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
						 ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

						//-- logout & login using Caseofficer & select Perform Third Party Checks task.


						NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


						//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
						objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
						FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}

						}
						objLoginScreen = ObjFacetoFaceAppointment.logout();
						tearDown(browser);
						
						System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
						// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
						setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
						ObjCrmHome  = new CrmHome(getDriver());
			//			  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
						//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
						if (flag3)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
							objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

							// Login to Portal Application & enter third Party details & logout
							ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
							objPCSECheck = ObjPerformerList.ClickonPCSECheck();

							objPCSECheck = objPCSECheck.ClickonEditRef();
							objPCSECheck = objPCSECheck.clickonPCSECheck();
							objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

							CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
							
							Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
							if(!AllCheckbox)
							{
								System.out.println("The all checkbox on case review tab is clicked");
								setAssertMessage("The all checkbox on case review tab is clicked", 1);
							}
							Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
							objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
							Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
							if(evidence)
							{
								for(String key: keys)
								{
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");
								}

							}
							if(Status)
							{
								System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
								setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
							}
							Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
							tearDown(browser);
							System.out.println("Application is successfully assigned to Manager.");
							// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
							///
							setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
							ObjCrmHome  = new CrmHome(getDriver());
					//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
								//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
							if (flag4)
							{
								 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
								 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
								 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
								MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
								Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
								if(!Enabled)
								{
									System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
									setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
								}
								Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
								Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
								if(!ApproveDossierButton)
								{
									System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
									setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
								}
								Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

								//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
								objMangerApproval = objMangerApproval.clickonAppovedossier();
								Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
								if(ApproveDossierButton1)
								{
									System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
									setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
								}
								Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
								objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
								Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
								if(evidence)
								{
									for(String key: keys)
									{
										objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");
									}

								}
								if(appstatus)
								{
									System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager" + ApplicationNumber);
									setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager" +ApplicationNumber, 3);	
								}
								Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
							}
						}
							}
				}
			}

			}

			//Suraj G :After Returned to Case officer - The tickmark on Case review page is not removed .
			// Hence we are developed the script till to the returned to manager
			// Test Case 4235
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
		@Parameters({"browser", "environment", "clientName" , "evidence"})
    public void SendAppDossier_Approval_PCSEManagerReject(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
		{
				
				String key = "PL_PA_87";			
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				CrmHome ObjCrmHome  = new CrmHome(getDriver());		
				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
			//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);		
				//AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					boolean ApplicationONCRM = onbjPerformerAppCase.VerifyApplicationNumber("MedicalAutoFirst", "Submitted" );
					tearDown(browser);
				
					if(!ApplicationONCRM)
					{
						setup(browser, environment, clientName, "PL");
						ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
						tearDown(browser);
						setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
						String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
						ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
						tearDown(browser);
						setup(browser, environment, clientName, "ActivationLink");
						ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
						tearDown(browser);
						setup(browser, environment, clientName, "PL");			
						ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
						tearDown(browser);
					}
				}
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				ObjCrmHome  = new CrmHome(getDriver());	
		//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag11 = ObjAdvancedFindResult.resultRecordFound();
				if (flag11)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);				
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
				//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
					//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
							//	ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
						//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
						//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.ReturntoCaseofficer();
									boolean Casestatus = objMangerApproval.verifyCasestatus("Undergoing Detailed Checks");
									if(evidence)
									{
										objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterReturntoCaseofficer");
									}
									if(Casestatus)
									{
										System.out.println("The ProcessPL is in 'undergoing Detailed checks status after returned from manager");
										setAssertMessage("The ProcessPL is in 'undergoing Detailed checks status after returned from manager", 2);
									}
									Verify.verifyTrue((Casestatus), "The ProcessPL is not in 'undergoing Detailed checks status after returned from manager"+ApplicationNumber);
									tearDown(browser);
								}
							}
						}
					}
				
								// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
									setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
									ObjCrmHome  = new CrmHome(getDriver());
							//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
									if (flag3)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
										objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Detailed Checks");

										// Login to Portal Application & enter third Party details & logout
										ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
										objPCSECheck = ObjPerformerList.ClickonPCSECheck();

										objPCSECheck = objPCSECheck.ClickonEditRef();
										objPCSECheck = objPCSECheck.clickonPCSECheck();
										objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

										CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
										
										Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
										if(!AllCheckbox)
										{
											System.out.println("The all checkbox on case review tab is clicked");
											setAssertMessage("The all checkbox on case review tab is clicked", 1);
										}
										Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
										objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
										Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
										if(evidence)
										{
											
											objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

										}
										if(Status)
										{
											System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
											setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
										}
										Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
										tearDown(browser);
										System.out.println("Application is successfully assigned to Manager.");
										// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
										///
										setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
										ObjCrmHome  = new CrmHome(getDriver());
								//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
										ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
										ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
											//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
											ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
										boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
										if (flag4)
										{
											 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
											onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
											 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
											 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
											MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
											Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
											if(!Enabled)
											{
												System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
												setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
											}
											Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
											Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
											if(!ApproveDossierButton)
											{
												System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
												setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
											}
											Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

											//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
											objMangerApproval = objMangerApproval.clickonAppovedossier();
											Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
											if(ApproveDossierButton1)
											{
												System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
												setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
											}
											Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
											objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
											Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
											if(evidence)
											{
												
											objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

											}
											if(appstatus)
											{
												System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
												setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager" +ApplicationNumber, 3);	
											}
											Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
											Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
										}
										
									}
									
								}
					
			}


			@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
			@Parameters({"browser", "environment", "clientName", "evidence"})
	public void NHSE_AppApprove(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
			{
				
				String key = "PL_PA_91";
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				CrmHome ObjCrmHome  = new CrmHome(getDriver());		
				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
			//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);		
				//AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flagFirst = ObjAdvancedFindResult.resultRecordFound();
				if (flagFirst)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					boolean ApplicationONCRM = onbjPerformerAppCase.VerifyApplicationNumber("OpthoAutoFirst", "Submitted" );
					tearDown(browser);
				
					if(!ApplicationONCRM)
					{
						setup(browser, environment, clientName, "PL");
						ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
						tearDown(browser);
						setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
						String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
						ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
						tearDown(browser);
						setup(browser, environment, clientName, "ActivationLink");
						ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
						tearDown(browser);
						setup(browser, environment, clientName, "PL");			
						ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
						tearDown(browser);
					}
				}
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				ObjCrmHome  = new CrmHome(getDriver());
				//String performer_PerformerPortal = ConfigurationData.Process_Medical;

				
				System.out.println(performer_PerformerPortal);
		//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
				//	  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
			//			  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
							//	ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
					//		  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
					//			  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
									Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
									if(evidence)
									{
										
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

									}
									if(appstatus)
									{
										System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
										setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									if(appstatus)
									{

										tearDown(browser);
						//String ApplicationNumber = "PL1115";
										setup(browser, environment, clientName, "NHSE");
										objLoginScreen = new LoginScreen(getDriver());
										NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								
										objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
										ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
										NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
										boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
										if(Defaultcheck)
										{
											System.out.println("The Under Consideration option is by default is checked");
											setAssertMessage("The Under Consideration option is by default is checked", 1);
										}
										Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
										objNHSDecision = objNHSDecision.ApproveApp("Approve");
										String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
										String FinalPLONMessage1 = objNHSDecision.RemovePerformerName("MedicalAuto");
										String FinalPLONMessage = objNHSDecision.RemovePerformerName("MedicalAuto");
										
										String FinalPLONMessage2 = objNHSDecision.RemovePerformerName2("Auto" ,FinalPLONMessage1);
										Boolean ActualConfirmationmessage = objNHSDecision.verifysubmittedmessage_NHSE(PLONMessage, FinalPLONMessage1,FinalPLONMessage,"Approve" );
										if(ActualConfirmationmessage)
										{
											System.out.println("The Correct Confirmation Message is appered if we clicked on Submit Decision");
											setAssertMessage("The Correct Confirmation Message is appered if we clicked on Submit Decision", 1);
										}
									//	Verify.verifyTrue((ActualConfirmationmessage), "The incorrect Confirmation Message is appered if we clicked on Submit Decision"+ApplicationNumber);
										objNHSDecision =objNHSDecision.clickonContinueconfirmation();
										String AppStatus = objNHSDecision.verifyApplicationStatus();
										if(evidence)
										{
												objNHSDecision.Applicationstatus(key+"NHSEApprove");
										}
										if(AppStatus.equalsIgnoreCase("Approved"))
										{
											System.out.println("The App Status is in Approved status after approve through NHS decision");
											setAssertMessage("The App Status is in Approved status after approve through NHS decision", 1);
										}
										Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
										tearDown(browser);
								
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
									
								}
								
							}
						}
					}
					
				}
				
			}

			// Test Case id 4237 
			// Suraj G : This test script used to Approved with condition the application through NHSE 
			@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
			@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void NHSE_AppApproveWithCondition(String browser ,String environment, String clientName , Boolean evidence) throws InterruptedException, IOException, AWTException
			{
				String key = "PL_PA_92";
				
				setup(browser, environment, clientName, "PL");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
				String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
				tearDown(browser);
				setup(browser, environment, clientName, "ActivationLink");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "PL");			
				ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				//String performer_PerformerPortal = ConfigurationData.Process_Medical;

				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
				       //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //         //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //         //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
							//	ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
							  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
									Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
									if(evidence)
									{
										
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

									}
									if(appstatus)
									{
										System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
										setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									if(appstatus)
									{

										tearDown(browser);
										setup(browser, environment, clientName, "NHSE");
										objLoginScreen = new LoginScreen(getDriver());
										NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
										objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
										ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
										NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
										boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
										if(evidence)
										{
											objNHSDecision.Applicationstatus(key+"Default_Underconsideration");
										}
										if(Defaultcheck)
										{
											System.out.println("The Under Consideration option is by default is checked");
											setAssertMessage("The Under Consideration option is by default is checked", 1);
										}
										Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked"+ApplicationNumber);
										objNHSDecision = objNHSDecision.ApproveApp("Approve with conditions");
										objNHSDecision = objNHSDecision.SubmitDecision_ApproveCondition();
										objNHSDecision =objNHSDecision.clickonContinueconfirmation();

										String AppStatus = objNHSDecision.verifyApplicationStatus();
										if(evidence)
										{
											objNHSDecision.Applicationstatus(key+"Awaiting Applicant Response");
										}
										if(AppStatus.equalsIgnoreCase("Awaiting Applicant Response"))
										{
											System.out.println("The App Status is in Approved with condition  status after approve with condittion through NHS decision"+ApplicationNumber);
											setAssertMessage("The App Status is in Approved with condition  status after approve with condittion through NHS decision" +ApplicationNumber, 1);
										}
										Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved with conditions"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
										tearDown(browser);
										setup(browser, environment, clientName, "PL");
									//	setup(browser, environment, clientName, "NHSE");
										objLoginScreen = new LoginScreen(getDriver());
										ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
										String Application_Number1 = ObjPerformerList.getApplicationNumberwithstatus("Awaiting Applicant Response");
										if(evidence)
										{
											ObjPerformerList.Applicationstatus(key+"Approvedwithconditions_portal");
										}
										if(ApplicationNumber.equalsIgnoreCase(Application_Number1))
										{
											System.out.println("The Approved with conditions  Application Number is " +Application_Number1 );
											setAssertMessage("The Approved with conditions  Application Number is " +Application_Number1, 1);	
										}
										Verify.verifyNotEquals(ApplicationNumber.equalsIgnoreCase(Application_Number1), "The Approved with conditions  is not happen on "+Application_Number1);

									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								}
								
								}
							}
								}
					}
					
				}
				
			// Test Case id 4238
			// Suraj G : This test script used to Refused the application through NHSE 
			@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
			@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void NHSE_Refused(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
			{
				String key = "PL_PA_93";
			//String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				setup(browser, environment, clientName, "PL");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
				String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
				tearDown(browser);
				setup(browser, environment, clientName, "ActivationLink");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "PL");			
				ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				//String performer_PerformerPortal = ConfigurationData.Process_Medical;

				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
				  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
							//	ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
							  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
									Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
									if(evidence)
									{
										
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

									}
									if(appstatus)
									{
										System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
										setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									if(appstatus)
									{

										tearDown(browser);
										setup(browser, environment, clientName, "NHSE");
										objLoginScreen = new LoginScreen(getDriver());
										NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
										objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
										ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
										NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
									//	String RadioValue = objNHSDecision.verifycheckbox();
										boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
										if(evidence)
										{
											objNHSDecision.Applicationstatus(key+"Default_Underconsideration");
										}
										if(Defaultcheck)
										{
											System.out.println("The Under Consideration option is by default is checked");
											setAssertMessage("The Under Consideration option is by default is checked", 1);
										}
										Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked"+ApplicationNumber);
										objNHSDecision = objNHSDecision.ApproveApp("Refuse");

										String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
										String FinalPLONMessage1 = objNHSDecision.RemovePerformerName("MedicalAutoFirst");
										String FinalPLONMessage = objNHSDecision.RemovePerformerName("MedicalAutoSurName");
										
									//	String FinalPLONMessage2 = objNHSDecision.RemovePerformerName2("Auto" ,FinalPLONMessage1);
										/*Boolean ActualConfirmationmessage = objNHSDecision.verifysubmittedmessage_NHSE(PLONMessage, FinalPLONMessage1,FinalPLONMessage,"Approve" );
										if(ActualConfirmationmessage)
										{
											System.out.println("The Correct Confirmation Message is appered if we clicked on Submit Decision");
											setAssertMessage("The Correct Confirmation Message is appered if we clicked on Submit Decision", 1);
										}
										Verify.verifyTrue((ActualConfirmationmessage), "The incorrect Confirmation Message is appered if we clicked on Submit Decision"+ApplicationNumber);*/
										objNHSDecision =objNHSDecision.clickonContinueconfirmation();
										//	objNHSDecision = objNHSDecision.SubmitDecision();
										String AppStatus = objNHSDecision.verifyApplicationStatus();
										if(evidence)
										{
											objNHSDecision.Applicationstatus(key+"RefuseStatus");
										}
										if(AppStatus.equalsIgnoreCase("Refused Pending Appeal"))
										{
											System.out.println("The App Status is in Refused status after Refuse through NHS decision");
											setAssertMessage("The App Status is in Refused status after Refuse through NHS decision", 1);
										}
										Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Refused Pending Appeal"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
										tearDown(browser);
										setup(browser, environment, clientName, "PL");
										objLoginScreen = new LoginScreen(getDriver());
										ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
										String Application_Number1 = ObjPerformerList.getApplicationNumberwithstatus("Refused Pending Appeal");
										if(evidence)
										{
											ObjPerformerList.Applicationstatus(key+"RefuseStatus_Portal");
										}
										if(ApplicationNumber.equalsIgnoreCase(Application_Number1))
										{
											System.out.println("The Refused Application Number is " +Application_Number1 );
											setAssertMessage("The Refused Application Number is " +Application_Number1, 1);	
										}
										Verify.verifyNotEquals(ApplicationNumber.equalsIgnoreCase(Application_Number1), "The Refused is not happen on "+Application_Number1);
									
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								}
								
							}
							}
								}
					}
					
				}		
	
			// Test Case id 4239
			// Suraj G : This test script used to Approved with condition the application through NHSE 
			@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
			@Parameters({"browser", "environment", "clientName", "evidence"})
	public void NHSE_Defered(String browser ,String environment, String clientName  , boolean  evidence) throws InterruptedException, IOException, AWTException
			{
				String key = "PL_PA_94";
					
				setup(browser, environment, clientName, "PL");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
				String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
				tearDown(browser);
				setup(browser, environment, clientName, "ActivationLink");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "PL");			
				ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				//String performer_PerformerPortal = ConfigurationData.Process_Medical;

				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
				  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
							//	ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
							  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
									Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
									if(evidence)
									{
										
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

									}
									if(appstatus)
									{
										System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
										setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									if(appstatus)
									{

										tearDown(browser);
									//	String ApplicationNumber = "PL113";
									//	setup(browser, environment, clientName, "NHSE");
										setup(browser, environment, clientName, "NHSE");
										objLoginScreen = new LoginScreen(getDriver());
										NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
										objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
										ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
										NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
									//	String RadioValue = objNHSDecision.verifycheckbox();
										boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
										if(evidence)
										{
											objNHSDecision.Applicationstatus(key+"Default_Underconsideration");
										}
										if(Defaultcheck)
										{
											System.out.println("The Under Consideration option is by default is checked");
											setAssertMessage("The Under Consideration option is by default is checked", 1);
										}
										Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked"+ApplicationNumber);
										objNHSDecision = objNHSDecision.ApproveApp("Defer");

										objNHSDecision = objNHSDecision.SubmitDecision();
										String AppStatus = objNHSDecision.verifyApplicationStatus();
										if(evidence)
										{
											objNHSDecision.Applicationstatus(key+"WithNHSEngland-Deferred");
										}
										if(AppStatus.equalsIgnoreCase("With NHS England-Deferred"))
										{
											System.out.println("The App Status is in 'With NHS England-Deferred' status after Defer through NHS decision");
											setAssertMessage("The App Status is in 'With NHS England-Deferred' status after Defer through NHS decision", 1);
										}
										Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("With NHS England-Deferred"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
										tearDown(browser);
										setup(browser, environment, clientName, "PL");
										objLoginScreen = new LoginScreen(getDriver());
										ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
										String Application_Number1 = ObjPerformerList.getApplicationNumberwithstatus("Deferred");
										if(evidence)
										{
											ObjPerformerList.Applicationstatus(key+"Deferred_Portal");
										}
										if(ApplicationNumber.equalsIgnoreCase(Application_Number1))
										{
											System.out.println("The Deferred Application Number is " +Application_Number1 );
											setAssertMessage("The Deferred Application Number is " +Application_Number1, 1);	
										}
										Verify.verifyNotEquals(ApplicationNumber.equalsIgnoreCase(Application_Number1), "The Deferred is not happen on "+Application_Number1);


									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);

								}
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
							}
							}
								}
					}
					
				}		
			
			//Test Case 4240 : We clicked at Return to PCSE through NHSE 
			// The Application is in "Performing detailed checks" status.
			@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
			@Parameters({"browser", "environment", "clientName", "evidence"})
	public void NHSE_ReturntoNHSE(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
			{
				String key = "PL_PA_95";
				setup(browser, environment, clientName, "PL");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
				String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
				tearDown(browser);
				setup(browser, environment, clientName, "ActivationLink");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "PL");			
				ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				//String performer_PerformerPortal = ConfigurationData.Process_Medical;

				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
				  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
							  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
									Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
									if(evidence)
									{
										
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

									}
									if(appstatus)
									{
										System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
										setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									if(appstatus)
									{

										tearDown(browser);
										setup(browser, environment, clientName, "NHSE");
										objLoginScreen = new LoginScreen(getDriver());
										NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
										objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
										objNHSCViewApp = objNHSCViewApp.clickonReturnPCSE(ApplicationNumber);
										String ActualReturnApp = objNHSCViewApp.getReturnAppMessage();
										boolean ReturnAppPopMessage = objNHSCViewApp.VerifyPopUpMessage(ActualReturnApp);
										if(evidence)
										{
											objNHSCViewApp.ScreenShotofPopUP(key+"ReturnAppMessage");
										}
										if(ReturnAppPopMessage)
										{
											System.out.println("The Return App Message is appered correctly" );
											setAssertMessage("The Return App Message is appered correctly " +ApplicationNumber, 1);
										}	
										Verify.verifyTrue((ReturnAppPopMessage), "The Return App Message is appered incorrectly "+ApplicationNumber);
										objNHSCViewApp = objNHSCViewApp.fillreturnApp();
										tearDown(browser);
										Thread.sleep(2000);
										setup(browser, environment, clientName, "PL");
										objLoginScreen = new LoginScreen(getDriver());
										ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
										String Application_Number1 = ObjPerformerList.getApplicationNumberwithstatus("Performing Detailed Checks");
										if(evidence)
										{
											ObjPerformerList.screenshotAppStatus_portal(key+"Perform");
										}
										if(ApplicationNumber.equalsIgnoreCase(Application_Number1))
										{
											System.out.println("The return Application Number is " +Application_Number1 );
											setAssertMessage("The return Application Number is " +Application_Number1, 1);	
										}
										Verify.verifyNotEquals(ApplicationNumber.equalsIgnoreCase(Application_Number1), "The Application is not in performing detailed checks "+Application_Number1);

									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);

								}
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
							}
							}
								
					}
					
				}	
			}
				
			//Suraj Gudekar : By using this test Script we will withdraw the application when PCSE Manager has Approved Application Dossier
			// TestCase : 4084
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void Withdraw_PCSEMangerApproved(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
			{
				String key = "PL_PA_52";
				setup(browser, environment, clientName, "PL");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
				String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
				tearDown(browser);
				setup(browser, environment, clientName, "ActivationLink");
				ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "PL");			
				ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
				tearDown(browser);
				setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
				String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				//String performer_PerformerPortal = ConfigurationData.Process_Medical;

				String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
				System.out.println(performer_PerformerPortal);
				  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",2);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",2);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",2);
				//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
					PerformerList ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
					PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
					objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
					
					objLoginScreen = objPCSECheck.logout();
					tearDown(browser);
					
					System.out.println("Case office assigned successfully.");
					//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
					if (flag1)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");

						// Login to Portal Application & enter third Party details & logout
						ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);

						ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
						//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
						objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
						objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
						objLoginScreen = objThirdPartyCheck.logout();
						tearDown(browser);
						System.out.println("Third Party checks are successfully.");
						// Login in CRM with NET TEAM USER & click on task
						setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
						if (flag2)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
							objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);

							//-- logout & login using Caseofficer & select Perform Third Party Checks task.


							NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();


							//	objNetTeamCheck = objThirdPartyCheck.clickonnetTeamcheck();
							objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
							FacetoFaceAppointment ObjFacetoFaceAppointment = objNetTeamCheck.clickonFaceApp();
							ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
							if(evidence)
							{
								
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}						
							objLoginScreen = ObjFacetoFaceAppointment.logout();
							tearDown(browser);
							
							System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
							// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
							setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
							ObjCrmHome  = new CrmHome(getDriver());
							  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
							ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							
						//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
							boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
							if (flag3)
							{
								onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
								onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
								objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");

								// Login to Portal Application & enter third Party details & logout
								ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								objPCSECheck = ObjPerformerList.ClickonPCSECheck();

								objPCSECheck = objPCSECheck.ClickonEditRef();
								objPCSECheck = objPCSECheck.clickonPCSECheck();
								objPCSECheck = objPCSECheck.clickonRefinfocomplete(environment);

								CaseofficerReview objCaseofficerReview = objPCSECheck.clickonCaseofficerreview();
								
								Boolean AllCheckbox = objCaseofficerReview.verifyallcheckbox();
								if(!AllCheckbox)
								{
									System.out.println("The all checkbox on case review tab is clicked");
									setAssertMessage("The all checkbox on case review tab is clicked", 1);
								}
								Verify.verifyTrue((!AllCheckbox), "The all checkbox is not clicked  "+ApplicationNumber);
								objCaseofficerReview = objCaseofficerReview.clickonSendmanager();
								Boolean Status = objCaseofficerReview.verifyCasestatus("Awaiting Dossier Approval");
								if(evidence)
								{
									
									objCaseofficerReview.ScreenshotofStatus(key+"CaseStatus_AfterSendManger");							

								}
								if(Status)
								{
									System.out.println("The application send to the manager and case status is Awaiting Dossier Approval");
									setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval", 2);
								}
								Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
								tearDown(browser);
								System.out.println("Application is successfully assigned to Manager.");
								// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
								///
								setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
									//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
								if (flag4)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
									 objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
									 ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
									MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
									Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
									if(!Enabled)
									{
										System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
										setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
									}
									Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
									Boolean ApproveDossierButton = objMangerApproval.verifyApproveDossier();
									if(!ApproveDossierButton)
									{
										System.out.println("The Approve dossier button is disabled if we are not selected Dossier approved");
										setAssertMessage("The Approve dossier button is disabled if we are not selected Dossier approved", 3);
									}
									Verify.verifyTrue((!ApproveDossierButton), "The Approve dossier button is enabled if we are not selected Dossier approved"+ApplicationNumber);

									//  Logon to CRM using PL Approver & review the task. Log to portal using PLApprover to click on Manager approval
									objMangerApproval = objMangerApproval.clickonAppovedossier();
									Boolean ApproveDossierButton1 = objMangerApproval.verifyApproveDossier();
									if(ApproveDossierButton1)
									{
										System.out.println("The Approve dossier button is Enabled if we are selected Dossier approved");
										setAssertMessage("The Approve dossier button is Enabled if we are selected Dossier approved", 3);
									}
									Verify.verifyTrue((ApproveDossierButton1), "The Approve dossier button is disabled if we are selected Dossier approved"+ApplicationNumber);
									objMangerApproval = objMangerApproval.clickonApproveDossierbutton();
									Boolean appstatus = objMangerApproval.verifyapplicationstatus("With NHS England-Under Consideration");
									if(evidence)
									{
										
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");							

									}
									if(appstatus)
									{
										System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
										setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
									}
									Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
									if(appstatus)
									{
										
										setup(browser, environment, clientName, "PL");
										objLoginScreen = new LoginScreen(getDriver());
										ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
										String AppNoOnStatus = ObjPerformerList.getApplicationNumberwithstatus("Under consideration");
										if(AppNoOnStatus.equalsIgnoreCase(ApplicationNumber))
										{
											System.out.println("The Under consideration Application Number is " +AppNoOnStatus );
											setAssertMessage("The Under consideration Application Number is " +AppNoOnStatus, 1);	
										}
										Verify.verifyNotEquals(AppNoOnStatus.equalsIgnoreCase(ApplicationNumber), "The Under consideration is not happen on "+AppNoOnStatus);
										Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
										Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
										if(disable && (!(disableWithdraw)))
										{
											helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
											ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
											String AppNoOnWithdraw = ObjPerformerList.getApplicationNumberwithstatus("Withdrawn");
											if(evidence)
											{
												
												ObjPerformerList.screenshotAppStatus_portal(key+"Withdrawapplication");							

											}
											if(AppNoOnWithdraw.equalsIgnoreCase(ApplicationNumber))
											{
												System.out.println("The Application is in Withdraw staus after approve by PCSE" );
												setAssertMessage("The Application is in Withdraw staus after approve by PCSE " +AppNoOnWithdraw, 1);	
											}
											Verify.verifyNotEquals(AppNoOnStatus.equalsIgnoreCase(ApplicationNumber), "The Application is not in Withdraw staus after approve by PCSE  "+AppNoOnWithdraw);
										}					
									}

								}
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
							}
						}
										
										
					}
				}
			}
				

	}
	