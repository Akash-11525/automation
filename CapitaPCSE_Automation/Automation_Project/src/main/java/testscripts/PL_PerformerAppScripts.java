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
import pageobjects.PerformerPL.ActivitiesQueues;
import pageobjects.PerformerPL.ChangeDetails;
import pageobjects.PerformerPL.EmpDetails;
import pageobjects.PerformerPL.HomeTab;
import pageobjects.PerformerPL.HourRetirement;
import pageobjects.PerformerPL.Localoffice;
import pageobjects.PerformerPL.PerformerManagement;
import pageobjects.PerformerPL.PerformerType;
import pageobjects.PerformerPL.PersonalDetails_performer;
import pageobjects.PerformerPL.WithdrawPerformer;
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
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class PL_PerformerAppScripts extends BaseClass {
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void PerformerManagement_ChangeNameAddTel(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{

		List<String> keys = Arrays.asList("PL_PM_15", "PL_PM_21","PL_PM_26","PL_PM_31","PL_PM_155");
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
			String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
		System.out.println(performer_PerformerPortal);
		  //       
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
			PerformerList ObjPerformerList = new PerformerList(getDriver());
			if(environment.equalsIgnoreCase("TEST"))
			{
				String PortalUser = onbjPerformerAppCase.clickOnCaseCommenceCheck_PortalUser(ApplicationNumber, "Submitted");
				String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
				setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
				ObjPerformerList  = new PerformerList(getDriver());
			}
			else
			{
			LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
			ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
			}
			PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();	
			if(evidence)
			{
				for(String key: keys)
				{
					objPCSECheck.screenshotAppStatus(key+"Case Office Assigned");
				}

			}
		
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			ObjPerformerList = new PerformerList(getDriver());
			  //       
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
				if(environment.equalsIgnoreCase("TEST"))
				{
					String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Third Party Checks",environment);
					setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
				}
				else
				{
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");
					// Login to Portal Application & enter third Party details & logout
					ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
					
				}

				ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
				//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
				objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
				objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
				if(evidence)
				{
					for(String key: keys)
					{
						objThirdPartyCheck.screenshot(key+"ThirdPartyCheck");
					}

				}
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());
				  //       
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				 ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
				if (flag2)
				{
					onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
					if(environment.equalsIgnoreCase("TEST"))
					{
						String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Arrange Appointment",environment);
						setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
				
					}
					else
					{
						LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
					}
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						for(String key: keys)
						{
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						}
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Conduct Appointment",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
						
						}
						else
						{
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);							
						}
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
					if (flag3)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Referee Checks",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
						}
						else
						{
											
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");
							// Login to Portal Application & enter third Party details & logout						
							ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								
						}
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
						{							for(String key: keys)
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
						Thread.sleep(2000);
						System.out.println("Application is successfully assigned to Manager.");
						// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
						///
						setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //       
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
						if (flag4)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
							if(environment.equalsIgnoreCase("TEST"))
							{
								String PortalUser = onbjPerformerAppCase.clickOnDQTTask_PLINTUSER(ApplicationNumber, "Approve Application",environment);
								setup(browser, environment, clientName, "PLPORTALINT","PLDATAQUALITYTEAMINT");
							
							}
							else
							{
								
								LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
								ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
							}
							MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
							Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
							if(!Enabled)
							{
								System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
								setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
							}
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
						
								for(String key: keys)
								{
	{
									objMangerApproval.ScreenshotofStatus(key+"CaseStatus_AfterClickDossierButtonr");
								}		

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
								LoginScreen objLoginScreen = new LoginScreen(getDriver());
								NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
								ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
								NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
							//	String RadioValue = objNHSDecision.verifycheckbox();
								boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
								String FinalPLONMessage1 = objNHSDecision.RemovePerformerName("MedicalAutoFirst");
								String FinalPLONMessage = objNHSDecision.RemovePerformerName("MedicalAutoSurName");
								
							//	String FinalPLONMessage2 = objNHSDecision.RemovePerformerName2("Auto" ,FinalPLONMessage1);
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
							
									for(String key: keys)
									{
									{
									objNHSDecision.Applicationstatus(key+"NHSEApprove");
									}
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision");
									setAssertMessage("The App Status is in Approved status after approve through NHS decision", 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
							
									
									setup(browser, environment, clientName, "PL");
									objLoginScreen = new LoginScreen(getDriver());
									ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
									PersonalDetails_performer ObjPersonalDetails_performer = ObjPerformerList.clickonPersonal_Pef();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.ClickChangeTelephone();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.updateNewTelephonenumber();								
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonSubmit_Telephone();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonOK_Submit();
									ObjPersonalDetails_performer = ObjPerformerList.clickonPersonal_Pef();
									boolean UpdateTelephoneNumber = ObjPersonalDetails_performer.verifyTelephonenumber();
									if(evidence)
									{
										for(String key: keys)
										{

											ObjPersonalDetails_performer.Screenshots(key+"TelephoneUpdate_Performer");
										}
									}
									if(UpdateTelephoneNumber)
									{
										System.out.println("The Telephone number is updated sucessfully");
										setAssertMessage("The Telephone number is updated sucessfully", 1);
									}
									Verify.verifyTrue((UpdateTelephoneNumber), "The Telephone number is not updated sucessfully");
									ObjPersonalDetails_performer = ObjPerformerList.clickonPersonal_Pef();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.ChangeAddress();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.SelectGMC();
									ObjPersonalDetails_performer =ObjPersonalDetails_performer.updateaddressManually();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonSubmit_Address();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonOK_Submit();
									ObjPersonalDetails_performer = ObjPerformerList.clickonPersonal_Pef();
									boolean Correctupdatedaddress = ObjPersonalDetails_performer.verifyupdatedaddress();
									if(evidence)
									{
										for(String key: keys)
										{

											ObjPersonalDetails_performer.Screenshots(key+"UpdatedAddress_Performer");
										}
									}
									if(Correctupdatedaddress)
									{
										System.out.println("The correct updated address is displayed");
										setAssertMessage("The correct updated address is displayed", 1);
									}
									Verify.verifyTrue((Correctupdatedaddress), "The incorrect updated address is displayed");
									ObjPersonalDetails_performer = ObjPerformerList.clickonPersonal_Pef();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.ClickOnChangeName();
									if(evidence)
									{
										for(String key: keys)
										{

											ObjPersonalDetails_performer.Screenshots(key+"Label_ChangeName");
										}
									}
									boolean CorrectLabel_Performer = ObjPersonalDetails_performer.VerifyCorrectLabel();
									if(CorrectLabel_Performer)
									{
										System.out.println("The correct label is displated for Change label name");
										setAssertMessage("The correct label is displated for Change label name", 1);
									}
									Verify.verifyTrue((CorrectLabel_Performer), "The incorrect label name is displayed");
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.ChangeName();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonSubmit();
									
									if(evidence)
									{
										for(String key: keys)
										{

											ObjPersonalDetails_performer.Screenshots(key+"PopUpAfterSubmit");
										}
									}
									boolean PopupMessage_submit = ObjPersonalDetails_performer.verifypopupmessage();
									if(PopupMessage_submit)
									{
										System.out.println("The Pop up message is populated after clicking on Submit Button");
										setAssertMessage("The Pop up message is populated after clicking on Submit Button", 1);	
									}
									Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button");
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonCancel_Submit();
									if(evidence)
									{
										for(String key: keys)
										{

											ObjPersonalDetails_performer.Screenshots(key+"PopUpAfterCancel");
										}
									}
									boolean PopupMessage_Cancel_submit = ObjPersonalDetails_performer.verifypopupmessage();
									if(!PopupMessage_Cancel_submit)
									{
										System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button");
										setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button", 1);	
									}
									Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button");
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonSubmit();
									ObjPersonalDetails_performer = ObjPersonalDetails_performer.clickonOK_Submit();
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
									ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
									if (flag17)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
										String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
										onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
										if(evidence)
										{
											for(String key: keys)
											{

												onbjPerformerAppCase.Screenshot_assign(key+"AssignCaseOffier");
											}
										}
										onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
										tearDown(browser);
								// Login as PL case officer in performer 
									setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
									ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
									if (flag37)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
										String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
										System.out.println(Datecreated);
										if(environment.equalsIgnoreCase("TEST"))
										{
											String PortalUser = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of Name",environment);
											String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
											setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
											ObjPerformerList  = new PerformerList(getDriver());
										}
										else
										{
											objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of Name");
											ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);	
										}
																		
										ChangeDetails objChangeDetails = ObjPerformerList.clickonconfirmGMC();
										boolean status_confirmed = objChangeDetails.verifystatus("Assigned");
										if(evidence)
										{
											for(String key: keys)
											{

												objChangeDetails.Screenshot(key+"Assigned_AfterConfirm");
											}
										}
										if(status_confirmed)
										{
											System.out.println("The assigned status after clicking on confirmed checkbox");
											setAssertMessage("The assigned status after clicking on confirmed checkbox", 1);										
										}
										Verify.verifyTrue((status_confirmed), "It is not in assigned status after clicking on confirmed checkbox");
										objChangeDetails = objChangeDetails.clickonApprove_performer();
										boolean status_Approved = objChangeDetails.verifystatus("Approved");
										if(evidence)
										{
											for(String key: keys)
											{

												objChangeDetails.Screenshot(key+"Approved_AfterApprove");
											}
										}
										if(status_Approved)
										{
											System.out.println("The Approved status after approve the change name request");
											setAssertMessage("The Approved status after approve the change name request", 1);										
										}
										Verify.verifyTrue((status_Approved), "It is not in Approved status after approve the change name request");
										objChangeDetails = objChangeDetails.ClickonLegacySystem();
										boolean status_Closed = objChangeDetails.verifystatus("Closed");
										if(evidence)
										{
											for(String key: keys)
											{

												objChangeDetails.Screenshot(key+"Closed_AfterApprove");
											}
										}
										if(status_Closed)
										{
											System.out.println("The Closed status after approve the change name request");
											setAssertMessage("The Closed status after approve the change name request", 1);										
										}
										Verify.verifyTrue((status_Closed), "It is not in Closed status after approve the change name request");
										Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
									}
									}
							
								}			
								
								

							
							Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
					}
					}
				}
			}
			
	 }

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void PerformerManagement_ChangePerfomerType(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{		
		String key = "PL_PM_158";
//		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
		tearDown(browser);
		setup(browser, environment, clientName, "ActivationLink");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "PL");			
		ProcessorPLHelpers.CreateAPP_MedReg(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		//String performer_PerformerPortal = ConfigurationData.Process_Medical;

		String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
		System.out.println(performer_PerformerPortal);
	//	  //       
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
			PerformerList ObjPerformerList = new PerformerList(getDriver());
			if(environment.equalsIgnoreCase("TEST"))
			{
				String PortalUser = onbjPerformerAppCase.clickOnCaseCommenceCheck_PortalUser(ApplicationNumber, "Submitted");
				String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
				setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
				ObjPerformerList  = new PerformerList(getDriver());
			}
			else
			{
			LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
			ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
			}
			PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
		//	  //       
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
				if(environment.equalsIgnoreCase("TEST"))
				{
					String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Third Party Checks",environment);
					setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
				}
				else
				{
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");
					// Login to Portal Application & enter third Party details & logout
					ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
					
				}

				ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
				//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
				objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
				objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				 ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
				if (flag2)
				{
					onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
					if(environment.equalsIgnoreCase("TEST"))
					{
						String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Arrange Appointment",environment);
						setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
				
					}
					else
					{
						LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
					}
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");					
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Conduct Appointment",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
						
						}
						else
						{
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);							
						}
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
						
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
				//	  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
					if (flag3)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Referee Checks",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
						}
						else
						{
											
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");
							// Login to Portal Application & enter third Party details & logout						
							ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								
						}
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
						Thread.sleep(2000);
						System.out.println("Application is successfully assigned to Manager.");
						// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
						///
						setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
					//	  //       
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
						if (flag4)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
							if(environment.equalsIgnoreCase("TEST"))
							{
								String PortalUser = onbjPerformerAppCase.clickOnDQTTask_PLINTUSER(ApplicationNumber, "Approve Application",environment);
								setup(browser, environment, clientName, "PLPORTALINT","PLDATAQUALITYTEAMINT");
							
							}
							else
							{
								
								LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
								ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
							}
							MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
							Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
							if(!Enabled)
							{
								System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
								setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
							}
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								LoginScreen objLoginScreen = new LoginScreen(getDriver());
								NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
								ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
								NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
							//	String RadioValue = objNHSDecision.verifycheckbox();
								boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
								String FinalPLONMessage1 = objNHSDecision.RemovePerformerName("MedicalAutoF");
								String FinalPLONMessage = objNHSDecision.RemovePerformerName("MedicalAutoS");
								
							//	String FinalPLONMessage2 = objNHSDecision.RemovePerformerName2("Auto" ,FinalPLONMessage1);
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
					// Login to CRM and added Organization and Role to Performer							
								setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
						//		  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
											onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
										}
									tearDown(browser);
									
									setup(browser, environment, clientName, "PL");
									objLoginScreen = new LoginScreen(getDriver());
									ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
									PerformerType ObjPerformerType = ObjPerformerList.clickonPerformerType();
									ObjPerformerType = ObjPerformerType.changeperformerType("GP Performer");
									ObjPerformerType = ObjPerformerType.clickonSubmit();
									if(evidence)
									{
										ObjPerformerType.Screenshots(key+"ChangedPerformer");
									}
									boolean PopupMessage_submit = ObjPerformerType.verifypopupmessage();
									if(PopupMessage_submit)
									{
										System.out.println("The Pop up message is populated after clicking on Submit Button");
										setAssertMessage("The Pop up message is populated after clicking on Submit Button", 1);	
									}
									Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button");
									ObjPerformerType = ObjPerformerType.clickonCancel_Submit();
									boolean PopupMessage_Cancel_submit = ObjPerformerType.verifypopupmessage();
									if(!PopupMessage_Cancel_submit)
									{
										System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button");
										setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button", 1);	
									}
									Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button");
									ObjPerformerType = ObjPerformerType.clickonSubmit();
									if(evidence)
									{
										ObjPerformerType.Screenshots(key+"ClickonSubmit");
									}
									ObjPerformerType = ObjPerformerType.clickonOK_Submit();
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
									ObjCrmHome  = new CrmHome(getDriver());
								//	  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
									if (flag17)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
										String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
										onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
										onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
										tearDown(browser);
										setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
										ObjCrmHome  = new CrmHome(getDriver());
									//	  //       
										ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
										ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
									//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
										ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
										boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
										if (flag37)
										{
											onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
											onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
											String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
											System.out.println(Datecreated);
											if(environment.equalsIgnoreCase("TEST"))
											{
												String PortalUser = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of Performer Type",environment);
												String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
												setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
												ObjPerformerList  = new PerformerList(getDriver());
											}
											else
											{
												objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of Performer Type");
												ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);	
											}
										/*	objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of Performer Type");											
											// Login to Portal Application & enter third Party details & logout
											ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);*/									
											ChangeDetails objChangeDetails = ObjPerformerList.clickonconfirmGMC_PerformerType();
											
											boolean status_confirmed = objChangeDetails.verifystatus_PerformerType("Assigned");
											if(evidence)
											{
												objChangeDetails.Screenshot_Perfomer(key+"Assigned_PerformerType");
											}
											if(status_confirmed)
											{
												System.out.println("The assigned status after clicking on confirmed checkbox");
												setAssertMessage("The assigned status after clicking on confirmed checkbox", 1);										
											}
											Verify.verifyTrue((status_confirmed), "It is not in assigned status after clicking on confirmed checkbox");
											objChangeDetails = objChangeDetails.clickonApprove_performerType();
											boolean status_Approved = objChangeDetails.verifystatus_PerformerType("Approved");
											if(evidence)
											{
												objChangeDetails.Screenshot_Perfomer(key+"Approved_PerformerType");
											}
											if(status_Approved)
											{
												System.out.println("The Approved status after approve the change Performer type request");
												setAssertMessage("The Approved status after approve the change Performer type request", 1);										
											}
											Verify.verifyTrue((status_Approved), "It is not in Closed status after approve the change Performer type request");
											objChangeDetails = objChangeDetails.ClickonLegacySystem_Pratice();
											boolean status_closed = objChangeDetails.verifystatus_PerformerType("closed");
											if(evidence)
											{
												objChangeDetails.Screenshot_Perfomer(key+"Closed_PerformerType");
											}
											if(status_Approved)
											{
												System.out.println("The Closed status after legacy Update");
												setAssertMessage("The Closed status after legacy Update", 1);										
											}
											Verify.verifyTrue((status_closed), "It is not in Closed status after legacy Update");
										}
								}
						}
					}
							Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);

							Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
				}
				}
	}
		}
		}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void PerformerManagement_24HoursApprove(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_123";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);

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
		  //       
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
			PerformerList ObjPerformerList = new PerformerList(getDriver());
			if(environment.equalsIgnoreCase("TEST"))
			{
				String PortalUser = onbjPerformerAppCase.clickOnCaseCommenceCheck_PortalUser(ApplicationNumber, "Submitted");
				String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
				setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
				ObjPerformerList  = new PerformerList(getDriver());
			}
			else
			{
			LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
			ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
			}
			PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
				if(environment.equalsIgnoreCase("TEST"))
				{
					String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Third Party Checks",environment);
					setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
				}
				else
				{
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");
					// Login to Portal Application & enter third Party details & logout
					ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
					
				}
				ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
				//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
				objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
				objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				 ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
				if (flag2)
				{
					onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
					if(environment.equalsIgnoreCase("TEST"))
					{
						String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Arrange Appointment",environment);
						setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
				
					}
					else
					{
						LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
					}
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Conduct Appointment",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
						
						}
						else
						{
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);							
						}
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
					if (flag3)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Referee Checks",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
						}
						else
						{
											
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");
							// Login to Portal Application & enter third Party details & logout						
							ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								
						}
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
						  //       
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
						if (flag4)
						{
							 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
							if(environment.equalsIgnoreCase("TEST"))
							{
								String PortalUser = onbjPerformerAppCase.clickOnDQTTask_PLINTUSER(ApplicationNumber, "Approve Application",environment);
								setup(browser, environment, clientName, "PLPORTALINT","PLDATAQUALITYTEAMINT");
							
							}
							else
							{
								
								LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
								ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
							}
							MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
							Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
							if(!Enabled)
							{
								System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
								setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
							}
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								LoginScreen objLoginScreen = new LoginScreen(getDriver());
								NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
								ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
								NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
							//	String RadioValue = objNHSDecision.verifycheckbox();
								boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
								String FinalPLONMessage1 = objNHSDecision.RemovePerformerName("MedicalAutoFirst");
								String FinalPLONMessage = objNHSDecision.RemovePerformerName("MedicalAutoSurName");
								
							//	String FinalPLONMessage2 = objNHSDecision.RemovePerformerName2("Auto" ,FinalPLONMessage1);
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
												
									setup(browser, environment, clientName, "PL");
									objLoginScreen = new LoginScreen(getDriver());
									ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
									HourRetirement ObjHourRetirement = ObjPerformerList.clickonHourRetirement();
									ObjHourRetirement = ObjHourRetirement.FillHourRetirement();
									ObjHourRetirement = ObjHourRetirement.clickonSubmit();
									if(evidence)
									{
										ObjHourRetirement.Screenshots(key+"ClickSubmit_24Hours");
									}
									boolean PopupMessage_submit = ObjHourRetirement.verifypopupmessage();
									if(PopupMessage_submit)
									{
										System.out.println("The Pop up message is populated after clicking on Submit Button - 24 Hours Retirement");
										setAssertMessage("The Pop up message is populated after clicking on Submit Button - 24 Hours Retirement", 1);	
									}
									Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button -24 Hours Retirement");
									ObjHourRetirement = ObjHourRetirement.clickonCancel_Submit();
									if(evidence)
									{
										ObjHourRetirement.Screenshots(key+"Clickcancel_24Hours");
									}
									boolean PopupMessage_Cancel_submit = ObjHourRetirement.verifypopupmessage();
									if(!PopupMessage_Cancel_submit)
									{
										System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button - 24 Hours Retirement");
										setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button -24 Hours Retirement", 1);	
									}
									Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button -24 Hours Retirement");
									ObjHourRetirement = ObjHourRetirement.clickonSubmit();
									ObjHourRetirement = ObjHourRetirement.clickonOK_Submit();
									HomeTab ObjHomeTab = ObjHourRetirement.clickonHometab();
									ObjHomeTab = ObjHomeTab.clickonChangeHistory();
									String RefNo = ObjHomeTab.getRefNo();
									if(evidence)
									{
										ObjHomeTab.Screenshots(key+"GetRefNo");
									}
									tearDown(browser);
									setup(browser, environment, clientName, "NHSE");
									objLoginScreen = new LoginScreen(getDriver());
									objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
									objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
									ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
									ObjActivitiesQueues = ObjActivitiesQueues.clickonApproveHours();
									ObjActivitiesQueues = ObjActivitiesQueues.Clickonconfirm();
									if(evidence)
									{
										ObjActivitiesQueues.Screenshots(key+"ApproveNHSE_24Hours");
									}
									boolean ApproveNHSE = ObjActivitiesQueues.verifyApproveButton();
									if(!ApproveNHSE)
									{
										System.out.println("The 24 Hours Retirement is Approve by NHSE");
										setAssertMessage("The 24 Hours Retirement is Approve by NHSE", 1);	
									}
									Verify.verifyTrue((!ApproveNHSE), "The 24 Hours Retirement is not Approve by NHSE");
							}
								
							}
							

							Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
						}
					}
				
				}
			}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void PerformerManagement_24HoursReject(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{		
		String key = "PL_PM_174";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);

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
		  //       
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
			PerformerList ObjPerformerList = new PerformerList(getDriver());
			if(environment.equalsIgnoreCase("TEST"))
			{
				String PortalUser = onbjPerformerAppCase.clickOnCaseCommenceCheck_PortalUser(ApplicationNumber, "Submitted");
				String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
				setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
				ObjPerformerList  = new PerformerList(getDriver());
			}
			else
			{
			LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
			ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
			}
			PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			
			//objLoginScreen = objPCSECheck.logout();
			if(evidence)
			{
				objPCSECheck.screenshotAppStatus(key+"CaseAssign");
			}
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
				if(environment.equalsIgnoreCase("TEST"))
				{
					String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Third Party Checks",environment);
					setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
				}
				else
				{
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");
					// Login to Portal Application & enter third Party details & logout
					ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
					
				}

				ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
				//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
				objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
				objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
				//objLoginScreen = objThirdPartyCheck.logout();
				if(evidence)
				{
					objThirdPartyCheck.screenshot(key+"ThirdPartyChecks");
				}
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				 ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
				if (flag2)
				{
					onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
					if(environment.equalsIgnoreCase("TEST"))
					{
						String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Arrange Appointment",environment);
						setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
				
					}
					else
					{
						LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
					}
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Conduct Appointment",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
						
						}
						else
						{
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);							
						}
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
						
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					tearDown(browser);
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
					if (flag3)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Referee Checks",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
						}
						else
						{
											
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");
							// Login to Portal Application & enter third Party details & logout						
							ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								
						}
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
						  //       
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
						if (flag4)
						{
							 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
							if(environment.equalsIgnoreCase("TEST"))
							{
								String PortalUser = onbjPerformerAppCase.clickOnDQTTask_PLINTUSER(ApplicationNumber, "Approve Application",environment);
								setup(browser, environment, clientName, "PLPORTALINT","PLDATAQUALITYTEAMINT");
							
							}
							else
							{
								
								LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
								ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
							}
							MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
							Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
							if(!Enabled)
							{
								System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
								setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
							}
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								LoginScreen objLoginScreen = new LoginScreen(getDriver());
								NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
								ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
								NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
							//	String RadioValue = objNHSDecision.verifycheckbox();
								boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
								String FinalPLONMessage1 = objNHSDecision.RemovePerformerName("MedicalAutoFirst");
								String FinalPLONMessage = objNHSDecision.RemovePerformerName("MedicalAutoSurName");
								
							//	String FinalPLONMessage2 = objNHSDecision.RemovePerformerName2("Auto" ,FinalPLONMessage1);
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
											
									setup(browser, environment, clientName, "PL");
									objLoginScreen = new LoginScreen(getDriver());
									ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
									HourRetirement ObjHourRetirement = ObjPerformerList.clickonHourRetirement();
									ObjHourRetirement = ObjHourRetirement.FillHourRetirement();
									ObjHourRetirement = ObjHourRetirement.clickonSubmit();
									boolean PopupMessage_submit = ObjHourRetirement.verifypopupmessage();
									if(PopupMessage_submit)
									{
										System.out.println("The Pop up message is populated after clicking on Submit Button - 24 Hours Retirement");
										setAssertMessage("The Pop up message is populated after clicking on Submit Button - 24 Hours Retirement", 1);	
									}
									Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button -24 Hours Retirement");
									ObjHourRetirement = ObjHourRetirement.clickonCancel_Submit();
									boolean PopupMessage_Cancel_submit = ObjHourRetirement.verifypopupmessage();
									if(!PopupMessage_Cancel_submit)
									{
										System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button - 24 Hours Retirement");
										setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button -24 Hours Retirement", 1);	
									}
									Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button -24 Hours Retirement");
									ObjHourRetirement = ObjHourRetirement.clickonSubmit();
									if(evidence)
									{
										ObjHourRetirement.Screenshots(key+"HourRetirement");
									}
									ObjHourRetirement = ObjHourRetirement.clickonOK_Submit();
									HomeTab ObjHomeTab = ObjHourRetirement.clickonHometab();
									ObjHomeTab = ObjHomeTab.clickonChangeHistory();
									String RefNo = ObjHomeTab.getRefNo();
									if(evidence)
									{
										ObjHomeTab.Screenshots(key+"CreatedRefNo");
									}
									tearDown(browser);
									setup(browser, environment, clientName, "NHSE");
									objLoginScreen = new LoginScreen(getDriver());
									objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
									objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
									ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
									ObjActivitiesQueues = ObjActivitiesQueues.clickonRejectHours();
									if(evidence)
									{
										ObjActivitiesQueues.Screenshots(key+"RejectNHSE");
									}
									ObjActivitiesQueues = ObjActivitiesQueues.Clickonconfirm_Reject();
									boolean ApproveNHSE = ObjActivitiesQueues.verifyRejectButton();
									if(!ApproveNHSE)
									{
										System.out.println("The 24 Hours Retirement is Reject by NHSE");
										setAssertMessage("The 24 Hours Retirement is Reject by NHSE", 1);	
									}
									Verify.verifyTrue((!ApproveNHSE), "The 24 Hours Retirement is not Reject by NHSE");
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
								}
								
							}
							

							
						}
					}
				
				}
			}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void PerformerManagement_ChangePerfomerType_Dental(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{		
		String key = "PL_PM_99";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
		tearDown(browser);
		setup(browser, environment, clientName, "ActivationLink");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "PL");			
		ProcessorPLHelpers.CreateAPP_DENTAL(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		//String performer_PerformerPortal = ConfigurationData.Process_Medical;

		String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
		System.out.println(performer_PerformerPortal);
		  //       
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
						
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
			
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");
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
										
									setup(browser, environment, clientName, "PL");
									objLoginScreen = new LoginScreen(getDriver());
									ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
									PerformerType ObjPerformerType = ObjPerformerList.clickonPerformerType();
									ObjPerformerType = ObjPerformerType.changeperformerType_Dental("Dental performer");
									ObjPerformerType = ObjPerformerType.clickonSubmit();
									 if(evidence)
										{
										 ObjPerformerType.Screenshots(key+"Dental PerformerSelected");
										}
									boolean PopupMessage_submit = ObjPerformerType.verifypopupmessage();
									if(PopupMessage_submit)
									{
										System.out.println("The Pop up message is populated after clicking on Submit Button");
										setAssertMessage("The Pop up message is populated after clicking on Submit Button", 1);	
									}
									Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button");
									ObjPerformerType = ObjPerformerType.clickonCancel_Submit();
									 if(evidence)
										{
										 ObjPerformerType.Screenshots(key+"PopUp_Cancel");
										}
									boolean PopupMessage_Cancel_submit = ObjPerformerType.verifypopupmessage();
									if(!PopupMessage_Cancel_submit)
									{
										System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button");
										setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button", 1);	
									}
									Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button");
									ObjPerformerType = ObjPerformerType.clickonSubmit();
									ObjPerformerType = ObjPerformerType.clickonOK_Submit();
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
									ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
									if (flag17)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
										String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
										onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
										onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
										tearDown(browser);
										setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
										ObjCrmHome  = new CrmHome(getDriver());
										  //       
										ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
										ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
									//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
										ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
										boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
										if (flag37)
										{
											onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
											onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
											String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
											System.out.println(Datecreated);
											objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of Performer Type");
											
											// Login to Portal Application & enter third Party details & logout
											ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);									
											ChangeDetails objChangeDetails = ObjPerformerList.clickonconfirmGMC_PerformerType();
											
											boolean status_confirmed = objChangeDetails.verifystatus_PerformerType("Assigned");
											if(evidence)
											{
												objChangeDetails.Screenshot_Perfomer(key+"Assigned_PerformerType");
											}
											if(status_confirmed)
											{
												System.out.println("The assigned status after clicking on confirmed checkbox");
												setAssertMessage("The assigned status after clicking on confirmed checkbox", 1);										
											}
											Verify.verifyTrue((status_confirmed), "It is not in assigned status after clicking on confirmed checkbox");
											objChangeDetails = objChangeDetails.clickonApprove_performerType();
											boolean status_Approved = objChangeDetails.verifystatus_PerformerType("Approved");
											if(evidence)
											{
												objChangeDetails.Screenshot_Perfomer(key+"Approved_PerformerType");
											}
											if(status_Approved)
											{
												System.out.println("The Approved status after approve the change Performer type request");
												setAssertMessage("The Approved status after approve the change Performer type request", 1);										
											}
											Verify.verifyTrue((status_Approved), "It is not in Closed status after approve the change Performer type request");
											objChangeDetails = objChangeDetails.ClickonLegacySystem_Pratice();
											boolean status_closed = objChangeDetails.verifystatus_PerformerType("closed");
											if(evidence)
											{
												objChangeDetails.Screenshot_Perfomer(key+"Approved_PerformerType");
											}
											if(status_Approved)
											{
												System.out.println("The Closed status after legacy Update");
												setAssertMessage("The Closed status after legacy Update", 1);										
											}
											Verify.verifyTrue((status_closed), "It is not in Closed status after legacy Update");
										}
								}
							}
					

							Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
				}
				}
			}
		}
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity","CLONE"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void EditPerformerManagement(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		List<String> keys = Arrays.asList("PL_PM_130", "PL_PM_144","PL_PM_148","PL_PM_151");
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
		tearDown(browser);
		setup(browser, environment, clientName, "ActivationLink");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "PL");			
		ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
		tearDown(browser);
	//	String ApplicationNumber = "PL298";
		setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
		
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setAssertMessage ("The Created Medical application number is "+ApplicationNumber, 1);
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		//String performer_PerformerPortal = ConfigurationData.Process_Medical;

		String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
		System.out.println(performer_PerformerPortal);
		//;
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
			PerformerList ObjPerformerList = new PerformerList(getDriver());
			if(environment.equalsIgnoreCase("TEST"))
			{
				String PortalUser = onbjPerformerAppCase.clickOnCaseCommenceCheck_PortalUser(ApplicationNumber, "Submitted");
				String InternalURL = helpers.CommonFunctions.getfinalInternalportaluserLink(environment, PortalUser);
				setup(browser, environment, clientName, "PLPORTALINT","PLASSIGNERINT");
				ObjPerformerList  = new PerformerList(getDriver());
			}
			else
			{
			LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnCaseCommenceCheck(ApplicationNumber, "Submitted" );
			ObjPerformerList = objLoginScreen.logintoPL("PLASSIGNERINT", environment);
			}
			PCSECheck objPCSECheck = ObjPerformerList.ClickonPCSECheck();
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			if(evidence)
			{
				for(String key: keys)
				{
					objPCSECheck.screenshotAppStatus(key+"Caseofficerassign");
				}

			}

			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			setAssertMessage ("The Case office assigned successfully = "+ApplicationNumber, 2);
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");	
			
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
			ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
			boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
			if (flag1)
			{
				onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
				onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
				if(environment.equalsIgnoreCase("TEST"))
				{
					String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Third Party Checks",environment);
					setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
				}
				else
				{
					LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Third Party Checks");
					// Login to Portal Application & enter third Party details & logout
					ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
					
				}
				ObjPerformerList = new PerformerList(getDriver());
				ThirdPartyCheck objThirdPartyCheck = ObjPerformerList.clickonThirdParty();
				//ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
				objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
				objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				setAssertMessage ("Third Party checks are successfully = "+ApplicationNumber, 3);
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());
				  //       
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
				ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
				boolean flag2 = ObjAdvancedFindResult.resultRecordFound();
				if (flag2)
				{
					onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
					onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
					if(environment.equalsIgnoreCase("TEST"))
					{
						String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Arrange Appointment",environment);
						setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
				
					}
					else
					{
						LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Arrange Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
					}
					ObjPerformerList = new PerformerList(getDriver());
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						for(String key: keys)
						{
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						}
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnActivityTask_PLPortal(ApplicationNumber, "Conduct Appointment",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLNETTEAMINT");
						
						}
						else
						{
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
							ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);							
						}
						ObjPerformerList = new PerformerList(getDriver());
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag3 = ObjAdvancedFindResult.resultRecordFound();
					if (flag3)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
						if(environment.equalsIgnoreCase("TEST"))
						{
							String PortalUser = onbjPerformerAppCase.clickOnTask_PLPortal(ApplicationNumber, "Perform Referee Checks",environment);
							setup(browser, environment, clientName, "PLPORTALINT","PLCASEOFFICERINT");
						}
						else
						{
											
							LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnTask(ApplicationNumber, "Perform Referee Checks");
							// Login to Portal Application & enter third Party details & logout						
							ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);
								
						}
						ObjPerformerList = new PerformerList(getDriver());
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
							System.out.println("The application send to the manager and case status is Awaiting Dossier Approval" );
							setAssertMessage("The application send to the manager and case status is Awaiting Dossier Approval" + ApplicationNumber, 2);
						}
						Verify.verifyTrue((Status), "The application is not send to the manager "+ApplicationNumber);
						tearDown(browser);
						System.out.println("Application is successfully assigned to Manager.");
						// Logon to CRM using DQT user & review the task. Log to portal to click on Manager approval
						///
						setup(browser, environment, clientName, "CRMPL","PLDATAQUALITYTEAM");		
						ObjCrmHome  = new CrmHome(getDriver());
						  //       
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
						ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
						boolean flag4 = ObjAdvancedFindResult.resultRecordFound();
						if (flag4)
						{
							onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
							onbjPerformerAppCase = onbjPerformerAppCase.clickonDQTGetActivityDetails();
							if(environment.equalsIgnoreCase("TEST"))
							{
								String PortalUser = onbjPerformerAppCase.clickOnDQTTask_PLINTUSER(ApplicationNumber, "Approve Application",environment);
								setup(browser, environment, clientName, "PLPORTALINT","PLDATAQUALITYTEAMINT");
							
							}
							else
							{
								
								LoginScreen objLoginScreen = onbjPerformerAppCase.clickOnDQTTask(ApplicationNumber, "Approve Application");
								ObjPerformerList = objLoginScreen.logintoPL("PLDATAQUALITYTEAMINT", environment);
							}
							ObjPerformerList =  new PerformerList(getDriver());
							MangerApproval objMangerApproval = ObjPerformerList.clickonMangerApproval();
							Boolean Enabled =objMangerApproval.verifyallcheckboxDisabled("IsDossierApproved");
							if(!Enabled)
							{
								System.out.println("The All Check box except Dossier Approval is disabled on Manager approval");
								setAssertMessage("The All Check box except Dossier Approval is disabled on Manager approval", 2);
							}
						//	// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								System.out.println("The Application status is 'With NHS England-Under Consideration' status after approve by manager");
								setAssertMessage("The Application status is 'With NHS England-Under Consideration' status after approve by manager", 3);	
							}
							Verify.verifyTrue((appstatus), "The Application status is not the  'With NHS England-Under Consideration' status after approve by manager"+ApplicationNumber);
							if(appstatus)
							{
								tearDown(browser);
								setup(browser, environment, clientName, "NHSE");
								LoginScreen objLoginScreen = new LoginScreen(getDriver());
								NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonAssignApp();
								ProcessViewApp objProcessViewApp = objNHSCViewApp.clickonApplication(ApplicationNumber);
								NHSDecision objNHSDecision = objProcessViewApp.clickonNHSDecision();
							//	String RadioValue = objNHSDecision.verifycheckbox();
								boolean Defaultcheck = objNHSDecision.verifycheckedradio("Under Consideration");
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{	for(String key: keys)
									{
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									}
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision");
									setAssertMessage("The App Status is in Approved status after approve through NHS decision", 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
													
									setup(browser, environment, clientName, "NHSE");
									objLoginScreen = new LoginScreen(getDriver());
									objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
									PerformerManagement ObjPerformerManagement  = objNHSCViewApp.clickonPerformerMangament();
									ObjPerformerManagement = ObjPerformerManagement.addpracticecode();
									boolean PresentDataResultPostalcode = ObjPerformerManagement.verifypresentdataResultPostalcode();
									 if(evidence)
										{	for(String key: keys)
											{
											ObjPerformerManagement.Screenshots_Postalcode(key+"Postalcode_PerformerManagement");
											}
										}
									if(PresentDataResultPostalcode)
									{
										System.out.println("All Fields value are appered after postal code search");
										setAssertMessage("All Fields value are appered after postal code search", 1);										
									}
									String GMCNumber = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
									ObjPerformerManagement =ObjPerformerManagement.FillGMCnumber(GMCNumber);
									ObjPerformerManagement = ObjPerformerManagement.clickonEditPerformer();
									Boolean performerstatus_Approve = ObjPerformerManagement.verifyperformerstatus("Included");
									 if(evidence)
										{	for(String key: keys)
											{
											ObjPerformerManagement.Screenshots(key+"Approved_Perfomrer");
											}
										}
									if(performerstatus_Approve)
									{
										System.out.println("The Performer Status is approved");
										setAssertMessage("The Performer Status is approved", 1);
									}
									Verify.verifyTrue(performerstatus_Approve, "The Performer Status is not in approved status"+ApplicationNumber);
									ObjPerformerManagement = ObjPerformerManagement.clickonSuspendPerformer();
									ObjPerformerManagement = ObjPerformerManagement.FillSuspendPerformer();
									Boolean performerstatus_Suspend = ObjPerformerManagement.verifyperformerstatus("Suspended");
									 if(evidence)
										{	for(String key: keys)
											{
											ObjPerformerManagement.Screenshots(key+"Suspended_Perfomrer");
											}
										}
									if(performerstatus_Suspend)
									{
										System.out.println("The Performer Status is Suspended After clicking on Suspend ");
										setAssertMessage("The Performer Status is Suspended After clicking on Suspend ", 1);
									}
									Verify.verifyTrue(performerstatus_Suspend, "The Performer Status is not in Suspended status After clicking on Suspend"+ApplicationNumber);
									ObjPerformerManagement =ObjPerformerManagement.clickonConfirmingSuspension();
									ObjPerformerManagement =ObjPerformerManagement.FillConfirmingSuspension();
									Boolean performerstatus_ConfirmSuspend = ObjPerformerManagement.verifyperformerstatus("Suspended");
									 if(evidence)
										{	for(String key: keys)
											{
											ObjPerformerManagement.Screenshots(key+"AfterConfirmSuspended_Perfomrer");
											}
										}
									if(performerstatus_ConfirmSuspend)
									{
										System.out.println("The Performer Status is Suspended After done Confirming Suspension ");
										setAssertMessage("The Performer Status is Suspended After done Confirming Suspension  ", 1);
									}
									Verify.verifyTrue(performerstatus_ConfirmSuspend, "The Performer Status is not in Suspended status After done Confirming Suspension "+ApplicationNumber);
									ObjPerformerManagement = ObjPerformerManagement.clickonRemovePerformer();
									ObjPerformerManagement = ObjPerformerManagement.fillRemovePerformer();
									Boolean performerstatus_Removed = ObjPerformerManagement.verifyperformerstatus("Removed");
									 if(evidence)
										{	for(String key: keys)
											{
											ObjPerformerManagement.Screenshots(key+"Removed_Perfomrer");
											}
										}
									if(performerstatus_Removed)
									{
										System.out.println("The Performer Status is Removed After done Remove performer ");
										setAssertMessage("The Performer Status is Removed After done Remove performer ", 1);
									}
									Verify.verifyTrue(performerstatus_Removed, "The Performer Status is not in Removed status After done Remove performer"+ApplicationNumber);
									ObjPerformerManagement = ObjPerformerManagement.clickonReincludePef();
									ObjPerformerManagement = ObjPerformerManagement.fillReinclude();
									 if(evidence)
										{	for(String key: keys)
											{
											ObjPerformerManagement.Screenshots(key+"Reincluded");
											}
										}
									Boolean performerstatus_Reinclude = ObjPerformerManagement.verifyperformerstatus("Included");
									if(performerstatus_Reinclude)
									{
										System.out.println("The Performer Status is Included After done Reinclude performer ");
										setAssertMessage("The The Performer Status is Included After done Reinclude performer ", 1);
									}
									Verify.verifyTrue(performerstatus_Reinclude, "The Performer Status is not in Included status After done Reinclude performer"+ApplicationNumber);
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								}
						 }
						}
				}
			}
			}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ChangePratice(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		List<String> keys = Arrays.asList("PL_PM_40", "PL_PM_47","PL_PM_55","PL_PM_156");
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);	
		
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
		tearDown(browser);
		setup(browser, environment, clientName, "ActivationLink");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "PL");			
		ProcessorPLHelpers.CreateAPP_MedReg(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL","PLASSIGNER");	
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		//String performer_PerformerPortal = ConfigurationData.Process_Medical;

		String performer_PerformerPortal = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
		System.out.println(performer_PerformerPortal);
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						for(String key: keys)
						{
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						}
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
									for(String key: keys)
									{
									
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									}
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
												
								
								setup(browser, environment, clientName, "PL");
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								EmpDetails ObjEmpDetails = ObjPerformerList.clickonEmpDetails();
								boolean DefaultPrincipal = ObjEmpDetails.VerifyPratice("Principal");
								if(DefaultPrincipal)
								{
									System.out.println("The Principal option is by default selected if we create app with GP contractor"+ApplicationNumber);
									setAssertMessage("The Principal option is by default selected if we create app with GP contractor"+ApplicationNumber, 1);
								}
								Verify.verifyTrue((DefaultPrincipal), "The Principal option is not selected as default selected if we create app with GP contractor"+ApplicationNumber);
								ObjEmpDetails = ObjEmpDetails.AddPratice("111");
								ObjEmpDetails = ObjEmpDetails.clickonSubmit();
								ObjEmpDetails = ObjEmpDetails.clickonOK_Submit();
								HomeTab ObjHomeTab = ObjEmpDetails.clickonHometab();
								ObjHomeTab = ObjHomeTab.clickonChangeHistory();
								String RefNo = ObjHomeTab.getRefNo();
								if(evidence)
								{
									for(String key: keys)
									{
									
										ObjHomeTab.Screenshots(key+"RefNO");
									}
								}
								tearDown(browser);
								setup(browser, environment, clientName, "PL");						
										
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLPRATICE", environment);
								ObjPerformerList = ObjPerformerList.clickonReviewpractice(RefNo);
								ObjPerformerList = ObjPerformerList.clickonacceptStartdate();
								
								tearDown(browser);
							//	String RefNo = "CAS-02187-N9K9D4";
								setup(browser, environment, clientName, "NHSE");
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								ObjActivitiesQueues = ObjActivitiesQueues.clickonApprovePractice();
								if(evidence)
								{
									for(String key: keys)
									{
									
										ObjActivitiesQueues.Screenshots(key+"ApprovePractice");
									}
								}
								ObjActivitiesQueues = ObjActivitiesQueues.Clickonconfirm_Pratice();
								boolean ApprovePracticechange = ObjActivitiesQueues.verifyApproveButton_Practice();
								if(!ApprovePracticechange)
								{
									System.out.println("The Practice change is approve by NHSE");
									setAssertMessage("The Practice change is approve by NHSE", 1);	
								}
								Verify.verifyTrue((!ApprovePracticechange), "The Practice change is not in approve by NHSE");
								tearDown(browser);
								setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
								if (flag17)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
									String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
								 ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
									if (flag37)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
										String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
										System.out.println(Datecreated);
										objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of employment");
										
										// Login to Portal Application & enter third Party details & logout
										ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);									
										ChangeDetails objChangeDetails = new ChangeDetails(getDriver());
										if(evidence)
										{
											for(String key: keys)
											{
											
												objChangeDetails.Screenshot_Legacy_Pratice(key+"LegacyUpdate");
											}
										}
										objChangeDetails = objChangeDetails.ClickonLegacySystem_Pratice();
										
										boolean status_Legacy = objChangeDetails.legecyCheckbox_Pratice();
										if(!status_Legacy)
										{
											System.out.println("The Legacy update is done after change practice");
											setAssertMessage("The Legacy update is done after change practice", 1);										
										}
										Verify.verifyTrue((!status_Legacy), "The Legacy update is not done after change practice");
									}
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
									System.out.println("Application Status is appearing correctly.");
								
							
							}
					}
					}
				}
			}
			
			}
		}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ChangeLocaloffice(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{		
		List<String> keys = Arrays.asList("PL_PM_67", "PL_PM_71","PL_PM_72","PL_PM_85","PL_PM_85","PL_PM_157");
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);	
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
		setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
			//	//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						for(String key: keys)
						{
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						}
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
									for(String key: keys)
									{
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									}
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
								setup(browser, environment, clientName, "PL");
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								Localoffice ObjLocaloffice = ObjPerformerList.clickonLocaloffice();
								ObjLocaloffice = ObjLocaloffice.clickonEdit();
								ObjLocaloffice = ObjLocaloffice.RemoveAddoffice();
								ObjLocaloffice = ObjLocaloffice.AddnewLocaloffice(environment);
								if(evidence)
								{
									for(String key: keys)
									{
										ObjLocaloffice.Screenshot(key+"AddedNewLocaloffice");
									}

								}
								ObjLocaloffice = ObjLocaloffice.clickonSubmit();
								ObjLocaloffice = ObjLocaloffice.clickonOK_Submit();
								HomeTab ObjHomeTab = ObjLocaloffice.clickonHometab();
								ObjHomeTab = ObjHomeTab.clickonChangeHistory();
								String RefNo = ObjHomeTab.getRefNo();
								System.out.println(RefNo);
								tearDown(browser);
								setup(browser, environment, clientName, "NHSE");
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								ObjActivitiesQueues = ObjActivitiesQueues.Clickonconfirm_LocalOffice();
								ObjActivitiesQueues = ObjActivitiesQueues.FillDeclarationForm();
								ObjActivitiesQueues = ObjActivitiesQueues.clickonSubmit_leaveLocalOffice();
								if(evidence)
								{
									for(String key: keys)
									{
										ObjActivitiesQueues.Screenshots(key+"LosingLocalOffice_NHSE");
									}

								}
								ObjActivitiesQueues = ObjActivitiesQueues.clickonOK_Submit();
								
								boolean ApproveAddLocalOld = ObjActivitiesQueues.verifyConfirmLocaloffice();
								if(!ApproveAddLocalOld)
								{
									System.out.println("The Losing Local office is approve by NHSE");
									setAssertMessage("The Losing Local office is approve by NHSE" + RefNo, 1);	
								}
								Verify.verifyTrue((!ApproveAddLocalOld), "The Losing Local office is not in approve by NHSE");
								tearDown(browser);
								setup(browser, environment, clientName, "NHSE");
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVERNEW", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								ObjActivitiesQueues = ObjActivitiesQueues.clickonApproveLocalOfficegaining();
								if(evidence)
								{
									for(String key: keys)
									{
										ObjActivitiesQueues.Screenshots(key+"GainingLocalOffice_NHSE");
									}

								}
								ObjActivitiesQueues = ObjActivitiesQueues.ClickonconfirmgainingLocaloffice();
								boolean ApproveAddLocalNew = ObjActivitiesQueues.verifyconfirmgainingLocaloffice();
								if(!ApproveAddLocalNew)
								{
									System.out.println("The gaining Local office is approve by NHSE");
									setAssertMessage("The gaining  Local office is approve by NHSE" + RefNo, 1);	
								}
								Verify.verifyTrue((!ApproveAddLocalNew), "The gaining Local office is not in approve by NHSE");
								tearDown(browser);
								setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
								if (flag17)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
									String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
									if(evidence)
									{
										for(String key: keys)
										{
											onbjPerformerAppCase.Screenshot_assign(key+"ChangeLcoaloffice_AssignCase");
										}

									}
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
									ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
							//		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
									if (flag37)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
										String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
										System.out.println(Datecreated);
										objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Local office changed Approved");
										
										// Login to Portal Application & enter third Party details & logout
										ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);									
										ChangeDetails objChangeDetails = new ChangeDetails(getDriver());
										if(evidence)
										{
											for(String key: keys)
											{
												objChangeDetails.Screenshot_Legacy(key+"ChangeLcoaloffice_LegacyCheckbox");
											}

										}
										objChangeDetails = objChangeDetails.ClickonLegacySystem();
										boolean status_Closed = objChangeDetails.verifystatus("Closed");
										if(evidence)
										{
											for(String key: keys)
											{

												objChangeDetails.Screenshot(key+"Closed_AfterLegacyCheck");
											}
										}
										if(status_Closed)
										{
											System.out.println("The Closed status after approve the change local request");
											setAssertMessage("The Closed status after approve the change Local request", 1);										
										}
										Verify.verifyTrue((status_Closed), "It is not in Closed status after approve the change name request");
									}
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
									System.out.println("Application Status is appearing correctly.");
								
								
						}
			}
		}
				}
			}
			}
		}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ChangePratice_NHSEREject(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_60";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
						
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
									
									
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
								setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								  //       
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
										
											
												onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											
										}
									tearDown(browser);
								}
								setup(browser, environment, clientName, "PL");
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								EmpDetails ObjEmpDetails = ObjPerformerList.clickonEmpDetails();
								ObjEmpDetails = ObjEmpDetails.AddPratice("111");
								ObjEmpDetails = ObjEmpDetails.clickonSubmit();
								ObjEmpDetails = ObjEmpDetails.clickonOK_Submit();
								HomeTab ObjHomeTab = ObjEmpDetails.clickonHometab();
								ObjHomeTab = ObjHomeTab.clickonChangeHistory();
								String RefNo = ObjHomeTab.getRefNo();
								tearDown(browser);
								setup(browser, environment, clientName, "PL");						
										
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLPRATICE", environment);
								ObjPerformerList = ObjPerformerList.clickonReviewpractice(RefNo);
								ObjPerformerList = ObjPerformerList.clickonacceptStartdate();
								tearDown(browser);
							//	String RefNo = "CAS-02187-N9K9D4";
								setup(browser, environment, clientName, "NHSE");
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								ObjActivitiesQueues = ObjActivitiesQueues.clickonRejectPractice();
								if(evidence)
								{
									
									
										ObjActivitiesQueues.Screenshots(key+"RejectByNHSE");
									
								}
								ObjActivitiesQueues = ObjActivitiesQueues.ClickonRejectConfirm_Pratice();
								boolean ApprovePracticechange = ObjActivitiesQueues.verifyRejectButton_Practice();
								if(!ApprovePracticechange)
								{
									System.out.println("The Practice change is Reject by NHSE");
									setAssertMessage("The Practice change is Reject by NHSE", 1);	
								}
								Verify.verifyTrue((!ApprovePracticechange), "The Practice change is not in Reject by NHSE");
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								System.out.println("Application Status is appearing correctly.");
							}
						}
					}
				}
			}
		}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ChangePratice_DefaultPrincipal(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_40";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
						
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
								
									
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
								setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								  //       
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
						//		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
										
											
												onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											
										}
									tearDown(browser);
								}
								setup(browser, environment, clientName, "PL");
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								EmpDetails ObjEmpDetails = ObjPerformerList.clickonEmpDetails();
								boolean DefaultPrincipal = ObjEmpDetails.VerifyPratice("Principal");
								if(evidence)
								{
								
									
									ObjEmpDetails.screenshot(key+"PrincipalDefault");
									
								}
								if(DefaultPrincipal)
								{
									System.out.println("The Principal option is by default selected if we create app with GP contractor"+ApplicationNumber);
									setAssertMessage("The Principal option is by default selected if we create app with GP contractor"+ApplicationNumber, 1);
								}
								Verify.verifyTrue((DefaultPrincipal), "The Principal option is not selected as default selected if we create app with GP contractor"+ApplicationNumber);
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								System.out.println("Application Status is appearing correctly.");
							}
						}
					}
				}
			}
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ExistingLocaloffice(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_71";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
		setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
								
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
							setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
										
												onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											

										}
									tearDown(browser);
								}
								setup(browser, environment, clientName, "PL");
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								Localoffice ObjLocaloffice = ObjPerformerList.clickonLocaloffice();
								ObjLocaloffice = ObjLocaloffice.clickonEdit();
								ObjLocaloffice = ObjLocaloffice.RemoveAddoffice();
								ObjLocaloffice = ObjLocaloffice.LinkedNewLocaloffice("No");
								ObjLocaloffice = ObjLocaloffice.clickonSubmit();
								ObjLocaloffice = ObjLocaloffice.clickonOK_Submit();
								if(evidence)
								{
								
									ObjLocaloffice.Screenshot(key+"Localoffice_clickSubmit");
									

								}
								boolean NewLocaloffice = ObjLocaloffice.verifyErrormessage();
								if(NewLocaloffice)
								{
									System.out.println("The existing local office can not remove by system");
									setAssertMessage("The existing local office can not remove by system" + ApplicationNumber, 1);
								}
								Verify.verifyTrue((NewLocaloffice), "The existing local office remove by system" +ApplicationNumber);
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								System.out.println("Application Status is appearing correctly.");
							}
					}
					}
				}
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void E2E_WithdrawPerformer(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		List<String> keys = Arrays.asList("PL_PM_160", "PL_PM_107");
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
		setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						for(String key: keys)
						{
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						}
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
									for(String key: keys)
									{
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									}
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
							setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
											for(String key: keys)
											{
												onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											}

										}
									tearDown(browser);
								
								setup(browser, environment, clientName, "PL");
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								WithdrawPerformer ObjWithdrawPerformer = ObjPerformerList.clickonWithdrawPerformer();
								ObjWithdrawPerformer = ObjWithdrawPerformer.fillWithdrawPL("Resignation");								
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonSubmit();
								if(evidence)
								{
									for(String key: keys)
									{
									ObjWithdrawPerformer.Screenshots(key+"ClickSubmit_WithdrawPL");
									}
								}
								boolean PopupMessage_submit = ObjWithdrawPerformer.verifypopupmessage();
								if(PopupMessage_submit)
								{
									System.out.println("The Pop up message is populated after clicking on Submit Button - WithdrawPL");
									setAssertMessage("The Pop up message is populated after clicking on Submit Button - WithdrawPL", 1);	
								}
								Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button -WithdrawPL");
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonCancel_Submit();
								if(evidence)
								{
									for(String key: keys)
									{
									ObjWithdrawPerformer.Screenshots(key+"Clickcancel_WithdrawPL");
									}
								}
								boolean PopupMessage_Cancel_submit = ObjWithdrawPerformer.verifypopupmessage();
								if(!PopupMessage_Cancel_submit)
								{
									System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button - WithdrawPL");
									setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button -WithdrawPL", 1);	
								}
								Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button -WithdrawPL");
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonSubmit();
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonOK_Submit();
								HomeTab ObjHomeTab = ObjWithdrawPerformer.clickonHometab();
								ObjHomeTab = ObjHomeTab.clickonChangeHistory();
								String RefNo = ObjHomeTab.getRefNo();
								System.out.println(RefNo);
								if(evidence)
								{
									for(String key: keys)
									{
									ObjHomeTab.Screenshots(key+"GetRefNo");
									}
								}
								tearDown(browser);
						
								setup(browser, environment, clientName, "NHSE");
								//String RefNo ="CAS-02657-T4K0B3";
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								
								ObjActivitiesQueues = ObjActivitiesQueues.clickonApproveWithdraw();
								ObjActivitiesQueues = ObjActivitiesQueues.ClickonconfirmWithdraw();
								if(evidence)
								{
									for(String key: keys)
									{
									ObjActivitiesQueues.Screenshots(key+"WithdrawApprove");
									}
								}
								boolean ApproveWithdraw = ObjActivitiesQueues.verifyWithdrawApproveButton();
								if(!ApproveWithdraw)
								{
									System.out.println("The Withdrawal is Approve by NHSE");
									setAssertMessage("The Withdrawal is Approve by NHSE", 1);	
								}
								Verify.verifyTrue((!ApproveWithdraw), "The Withdrawal is not Approve by NHSE");
								tearDown(browser);
								setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
								if (flag17)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
									String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
									if(evidence)
									{
										for(String key: keys)
										{
											onbjPerformerAppCase.Screenshot_assign(key+"ChangeLcoaloffice_AssignCase");
										}

									}
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
									ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
									if (flag37)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
										String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
										System.out.println(Datecreated);
										objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Withdrawal Approved");
										
										// Login to Portal Application & enter third Party details & logout
										ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);									
										ChangeDetails objChangeDetails = new ChangeDetails(getDriver());
										if(evidence)
										{
											for(String key: keys)
											{
												objChangeDetails.Screenshot_Legacy(key+"ChangeLcoaloffice_LegacyCheckbox");
											}

										}
										objChangeDetails = objChangeDetails.ClickonLegacySystem();
										boolean status_Legacy = objChangeDetails.legecyCheckbox();
									
										if(!status_Legacy)
										{
											System.out.println("The Legacy update is done after Withdraw");
											setAssertMessage("The Legacy update is done after Withdraw", 1);										
										}
										Verify.verifyTrue((!status_Legacy), "The Legacy update is not done after Withdraw");
									}
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
									System.out.println("Application Status is appearing correctly.");
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void E2E_WithdrawPerformer_NHSEReject(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_116";
//		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
		setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
									
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
							setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
							
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
										
												onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											

										}
									tearDown(browser);
								
								setup(browser, environment, clientName, "PL");
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								WithdrawPerformer ObjWithdrawPerformer = ObjPerformerList.clickonWithdrawPerformer();
								ObjWithdrawPerformer = ObjWithdrawPerformer.fillWithdrawPL("Resignation");								
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonSubmit();
								if(evidence)
								{
								
									ObjWithdrawPerformer.Screenshots(key+"ClickSubmit_WithdrawPL");
									
								}
								boolean PopupMessage_submit = ObjWithdrawPerformer.verifypopupmessage();
								if(PopupMessage_submit)
								{
									System.out.println("The Pop up message is populated after clicking on Submit Button - WithdrawPL");
									setAssertMessage("The Pop up message is populated after clicking on Submit Button - WithdrawPL", 1);	
								}
								Verify.verifyTrue((PopupMessage_submit), "The Pop up message is not populated after clicking on Submit Button -WithdrawPL");
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonCancel_Submit();
								if(evidence)
								{
								
									ObjWithdrawPerformer.Screenshots(key+"Clickcancel_WithdrawPL");
								}
								
								boolean PopupMessage_Cancel_submit = ObjWithdrawPerformer.verifypopupmessage();
								if(!PopupMessage_Cancel_submit)
								{
									System.out.println("The Pop up message is not  populated after clicking on Cancel on Submit Button - WithdrawPL");
									setAssertMessage("The Pop up message is not  populated after clicking on Cancel on Submit Button -WithdrawPL", 1);	
								}
								Verify.verifyTrue((!PopupMessage_Cancel_submit), "The Pop up message is populated after clicking on Cancel on Submit Button -WithdrawPL");
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonSubmit();
								ObjWithdrawPerformer = ObjWithdrawPerformer.clickonOK_Submit();
								HomeTab ObjHomeTab = ObjWithdrawPerformer.clickonHometab();
								ObjHomeTab = ObjHomeTab.clickonChangeHistory();
								String RefNo = ObjHomeTab.getRefNo();
								System.out.println(RefNo);
								if(evidence)
								{
								
									ObjHomeTab.Screenshots(key+"GetRefNo");
									
								}
								tearDown(browser);
						
								setup(browser, environment, clientName, "NHSE");
								//String RefNo ="CAS-02657-T4K0B3";
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								ObjActivitiesQueues = ObjActivitiesQueues.clickonRejectWithdraw();
								ObjActivitiesQueues = ObjActivitiesQueues.ClickonRejectConfirmWithdraw();
								if(evidence)
								{
								
									ObjActivitiesQueues.Screenshots(key+"WithdrawReject");
									
								}
								boolean ApproveWithdraw = ObjActivitiesQueues.verifyWithdrawRejectButton();
								if(!ApproveWithdraw)
								{
									System.out.println("The Withdrawal is Approve by NHSE");
									setAssertMessage("The Withdrawal is Approve by NHSE", 1);	
								}
								Verify.verifyTrue((!ApproveWithdraw), "The Withdrawal is not Approve by NHSE");
								Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								System.out.println("Application Status is appearing correctly.");
								}
							}
						}
					}
				}
			}
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ReassignLocaloffice_NHSE(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_84";
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			if(evidence)
			{
				
					objPCSECheck.screenshotAppStatus(key+"Caseofficerassign");
				

			}

			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
						
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
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
											
									setup(browser, environment, clientName, "NHSE");
									objLoginScreen = new LoginScreen(getDriver());
									objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
									PerformerManagement ObjPerformerManagement  = objNHSCViewApp.clickonPerformerMangament();
									String GMCNumber = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
									ObjPerformerManagement =ObjPerformerManagement.FillGMCnumber(GMCNumber);
									ObjPerformerManagement = ObjPerformerManagement.clickonEditPerformer();
									ObjPerformerManagement =ObjPerformerManagement.clickonchangeLocaloffice();									
									String CorrectLocaloffice = ObjPerformerManagement.getLocalOffice();
									String ExpectedLocaloffice = testdata.ConfigurationData.getRefDataDetails(environment, "PLContractor");
									
									if(CorrectLocaloffice.equalsIgnoreCase(ExpectedLocaloffice))
									{
										System.out.println("The Correct Local office is displayed on NHSE");
										setAssertMessage("The Correct Local office is displayed on NHSE", 1);
									}
									Verify.verifyNotEquals(CorrectLocaloffice.equalsIgnoreCase(ExpectedLocaloffice), "The incorrect Local office is displayed on NHSE"+ApplicationNumber);
									String ExpectedupdatedLocaloffice = testdata.ConfigurationData.getRefDataDetails(environment, "NewLocaloffice");
									ObjPerformerManagement = ObjPerformerManagement.AddNewLocaloffice(ExpectedupdatedLocaloffice);
									 if(evidence)
										{	
										 ObjPerformerManagement.Screenshots_Performer(key+"Localoffice_NHSE");
											
										}
									ObjPerformerManagement = ObjPerformerManagement.clickonSubmitLocaloffice();
									
									ObjPerformerManagement = ObjPerformerManagement.clickonOK_Submit();
									Boolean Updatedlocaloffice = ObjPerformerManagement.verifyChangeLocaloffice(); 
									 if(evidence)
										{	
										 ObjPerformerManagement.Screenshots_Performer(key+"ChangedLocaloffice_NHSE");
											
										}
									if(!Updatedlocaloffice)
									{
										System.out.println("The Change local office is done through NHSE");
										setAssertMessage("The Change local office is done through NHSE", 1);
									}
									Verify.verifyTrue((!Updatedlocaloffice), "The Change local office is not done through NHSE "+ApplicationNumber);
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								
									
								}
							}
						}
					}
				}
				
			}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void FutureDateDeath(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		String key = "PL_PM_139";
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
	/*	setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			if(evidence)
			{
				
					objPCSECheck.screenshotAppStatus(key+"Caseofficerassign");
				

			}

			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
					
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
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
					// Login to CRM and added Organization and Role to Performer							
								setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								  //       
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									 onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
									 if(evidence)
										{	
											onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											
										}
									tearDown(browser);*/
									
									setup(browser, environment, clientName, "NHSE");
									LoginScreen objLoginScreen = new LoginScreen(getDriver());
									NHSCViewApp objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
									PerformerManagement ObjPerformerManagement  = objNHSCViewApp.clickonPerformerMangament();
									String GMCNumber = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "GMCNumber");
									ObjPerformerManagement =ObjPerformerManagement.FillGMCnumber(GMCNumber);
									ObjPerformerManagement = ObjPerformerManagement.clickonEditPerformer();
									ObjPerformerManagement = ObjPerformerManagement.RecordDeath();
									ObjPerformerManagement = ObjPerformerManagement.clickonOK_Death();
									ObjPerformerManagement = ObjPerformerManagement.clickonOK_Submit();
									
									 if(evidence)
										{	
										 ObjPerformerManagement.Screenshots(key+"DeathErrorMessage");
											
										}
									Boolean Error_Death = ObjPerformerManagement.verifyErrorDeath();
									if(Error_Death)
									{
									System.out.println("The Correct Error message is appered after entering Future date death");
									setAssertMessage("The Correct Error message is appered after entering Future date death", 1);
									}
									Verify.verifyTrue((Error_Death), "The inorrect Error message is appered after entering Future date death "+ApplicationNumber);
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
								}
		
	// Suraj Gudekar 
	// Test cse id - E2E Performer - Practice change request affecting a Local office change
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Regression","Sanity"} )
	@Parameters({"browser", "environment", "clientName", "evidence"})
	public void ChangePratice_AffectLocaloffice(String browser ,String environment, String clientName ,boolean evidence ) throws InterruptedException, IOException, AWTException
	{
		
		List<String> keys = Arrays.asList("PL_PM_40", "PL_PM_47","PL_PM_55","PL_PM_156");
	//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);	
		
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER", 1);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",user, 3);
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
		//;
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
			
			//objLoginScreen = objPCSECheck.logout();
			tearDown(browser);
			
			System.out.println("Case office assigned successfully.");
			//-- logout & login using Case officer in CRM & select Perform Third Party Checks task.
			//String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
			setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
			ObjCrmHome  = new CrmHome(getDriver());
			  //       
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
				//objLoginScreen = objThirdPartyCheck.logout();
				tearDown(browser);
				System.out.println("Third Party checks are successfully.");
				// Login in CRM with NET TEAM USER & click on task
				setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
				ObjCrmHome  = new CrmHome(getDriver());			    
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
					NetTeamCheck objNetTeamCheck = ObjPerformerList.clickonnetTeamcheck();
					objNetTeamCheck = objNetTeamCheck.FillNetTeamAppointment();
				
					if(evidence)
					{
						for(String key: keys)
						{
							objNetTeamCheck.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
						}
					}	
				}
					tearDown(browser);
					setup(browser, environment, clientName, "CRMPL","PLNETTEAM");		
					ObjCrmHome  = new CrmHome(getDriver());			  
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
					ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
					boolean flag22 = ObjAdvancedFindResult.resultRecordFound();
					if (flag22)
					{
						onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
						onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityLog();
						objLoginScreen = onbjPerformerAppCase.clickOnActivityTask(ApplicationNumber, "Conduct Appointment");
						ObjPerformerList = objLoginScreen.logintoPL("PLNETTEAMINT", environment);
						FacetoFaceAppointment ObjFacetoFaceAppointment = ObjPerformerList.clickonFaceApp();
						ObjFacetoFaceAppointment = ObjFacetoFaceAppointment.clickonNetTeamComplete();
						if(evidence)
						{
							for(String key: keys)
							{
								ObjFacetoFaceAppointment.ScreenshotofFaceapp(key+"AllcheckBox_onFaceAppTab");
							}
						}
					}
					
					System.out.println("Team appointment has been taken & Net Team has completed activities successfully.");
					setAssertMessage ("Team appointment has been taken & Net Team has completed activities successfully = "+ApplicationNumber, 4);
					tearDown(browser);
					// Log in using Case Office in CRM & Select Net Team Checks complete task. Then logon using Case officer
					setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
					ObjCrmHome  = new CrmHome(getDriver());
					  //       
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
						  //       
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
							// Verify.verifyTrue((!Enabled), "Apart form Dossier approval one or more check box is enabled on manager approval tab"+ApplicationNumber);
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
								if(Defaultcheck)
								{
									System.out.println("The Under Consideration option is by default is checked");
									setAssertMessage("The Under Consideration option is by default is checked", 1);
								}
								Verify.verifyTrue((Defaultcheck), "The Under Consideration option is not by default is checked "+ApplicationNumber);
								objNHSDecision = objNHSDecision.ApproveApp("Approve");
								String PLONMessage = objNHSDecision.ClickonSubmitDecisionApprove("PL");							
								objNHSDecision =objNHSDecision.clickonContinueconfirmation();
								String AppStatus = objNHSDecision.verifyApplicationStatus();
								if(evidence)
								{
									for(String key: keys)
									{
									
										objNHSDecision.Applicationstatus(key+"NHSEApprove");
									}
								}
								if(AppStatus.equalsIgnoreCase("Approved"))
								{
									System.out.println("The App Status is in Approved status after approve through NHS decision"+ApplicationNumber);
									setAssertMessage("The App Status is in Approved status after approve through NHS decision" +ApplicationNumber, 1);
								}
								Verify.verifyNotEquals(AppStatus.equalsIgnoreCase("Approved"), "The App Status is not  in Approved status after approve through NHS decision"+ApplicationNumber);
								tearDown(browser);
					// Login to CRM and added Organization and Role to Performer							
								setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
						//		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
								String primaryEntity_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",3);
								String GroupTypeValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",3);
								String FieldValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",3);
								String ConditionValue_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",3);
								String PerformerName_Performer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectValueForField",3);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Performer(primaryEntity_Performer,GroupTypeValue_Performer,FieldValue_Performer,ConditionValue_Performer,PerformerName_Performer);
								boolean flag_Performer = ObjAdvancedFindResult.resultRecordFound();
								if (flag_Performer)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									 onbjPerformerAppCase = onbjPerformerAppCase.AddPortalRole();
										if(evidence)
										{
											for(String key: keys)
											{
											
												onbjPerformerAppCase.Screenshots(key+"CreatedPerformer");
											}
										}
									tearDown(browser);
								}
								setup(browser, environment, clientName, "PL");
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
								EmpDetails ObjEmpDetails = ObjPerformerList.clickonEmpDetails();
								boolean DefaultPrincipal = ObjEmpDetails.VerifyPratice("Principal");
								if(DefaultPrincipal)
								{
									System.out.println("The Principal option is by default selected if we create app with GP contractor"+ApplicationNumber);
									setAssertMessage("The Principal option is by default selected if we create app with GP contractor"+ApplicationNumber, 1);
								}
								Verify.verifyTrue((DefaultPrincipal), "The Principal option is not selected as default selected if we create app with GP contractor"+ApplicationNumber);
								ObjEmpDetails = ObjEmpDetails.clickonedit();
								ObjEmpDetails = ObjEmpDetails.addnewcommitment("999");
								ObjEmpDetails = ObjEmpDetails.clickonSubmit();
								ObjEmpDetails = ObjEmpDetails.clickonOK_Submit();
								HomeTab ObjHomeTab = ObjEmpDetails.clickonHometab();
								ObjHomeTab = ObjHomeTab.clickonChangeHistory();
								String RefNo = ObjHomeTab.getRefNo();
								if(evidence)
								{
									for(String key: keys)
									{
									
										ObjHomeTab.Screenshots(key+"RefNO");
									}
								}
								tearDown(browser);
								setup(browser, environment, clientName, "PL");						
								//String RefNo ="CAS-06479-Q5Y5D4";
								objLoginScreen = new LoginScreen(getDriver());
								ObjPerformerList  = objLoginScreen.logintoPL("PLPRATICE", environment);
								ObjPerformerList = ObjPerformerList.clickonReviewpractice(RefNo);
								ObjPerformerList = ObjPerformerList.clickonacceptStartdate();
								
								tearDown(browser);
							//	String RefNo = "CAS-02187-N9K9D4";
								setup(browser, environment, clientName, "NHSE");
								objLoginScreen = new LoginScreen(getDriver());
								objNHSCViewApp = objLoginScreen.logintoNHSE("PLAPPROVER", environment);
								objNHSCViewApp =objNHSCViewApp.clickonActiviesQueue();
								ActivitiesQueues ObjActivitiesQueues = objNHSCViewApp.clickonRefNo(RefNo);
								ObjActivitiesQueues = ObjActivitiesQueues.clickonApprovePractice();
								if(evidence)
								{
									for(String key: keys)
									{
									
										ObjActivitiesQueues.Screenshots(key+"ApprovePractice");
									}
								}
								ObjActivitiesQueues = ObjActivitiesQueues.Clickonconfirm_Pratice();
								boolean ApprovePracticechange = ObjActivitiesQueues.verifyApproveButton_Practice();
								if(!ApprovePracticechange)
								{
									System.out.println("The Practice change is approve by NHSE");
									setAssertMessage("The Practice change is approve by NHSE", 1);	
								}
								Verify.verifyTrue((!ApprovePracticechange), "The Practice change is not in approve by NHSE");
								setup(browser, environment, clientName, "CRMPL","PLASSIGNER");		
								ObjCrmHome  = new CrmHome(getDriver());
								  //       
								ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
								ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
								ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
								boolean flag17 = ObjAdvancedFindResult.resultRecordFound();
								if (flag17)
								{
									onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
									onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
									String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonAssignCase(GMCNumbertxt);
									onbjPerformerAppCase = onbjPerformerAppCase.ClickonassignCaseofficer();
									tearDown(browser);
									setup(browser, environment, clientName, "CRMPL","PLCASEOFFICER");		
								 ObjCrmHome  = new CrmHome(getDriver());
									  //       
									ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
									ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();								
								//	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",2);
									ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
									boolean flag37 = ObjAdvancedFindResult.resultRecordFound();
									if (flag37)
									{
										onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
										onbjPerformerAppCase = onbjPerformerAppCase.clickonGetActivityDetails();
										String Datecreated = helpers.CommonFunctions.getTodayDate_DotFormat();
										System.out.println(Datecreated);
										objLoginScreen = onbjPerformerAppCase.clickOnTask_performer(Datecreated, "Change of employment");
										
										// Login to Portal Application & enter third Party details & logout
										ObjPerformerList = objLoginScreen.logintoPL("PLCASEOFFICERINT", environment);									
										ChangeDetails objChangeDetails = new ChangeDetails(getDriver());
										if(evidence)
										{
											for(String key: keys)
											{
											
												objChangeDetails.Screenshot_Legacy_Pratice(key+"LegacyUpdate");
											}
										}
										objChangeDetails = objChangeDetails.ClickonLegacySystem_Pratice();
										
										boolean status_Legacy = objChangeDetails.legecyCheckbox_Pratice();
										if(!status_Legacy)
										{
											System.out.println("The Legacy update is done after change practice");
											setAssertMessage("The Legacy update is done after change practice", 1);										
										}
										Verify.verifyTrue((!status_Legacy), "The Legacy update is not done after change practice");
									}
									tearDown(browser);
									setup(browser, environment, clientName, "PL");
									//	String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
										objLoginScreen = new LoginScreen(getDriver());
										ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
										Localoffice ObjLocaloffice = ObjPerformerList.clickonLocaloffice();
										ObjLocaloffice = ObjLocaloffice.clickonEdit();
										ObjLocaloffice = ObjLocaloffice.RemoveAddoffice();
										ObjLocaloffice = ObjLocaloffice.AddnewLocaloffice(environment);
										
									Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
									System.out.println("Application Status is appearing correctly.");
								
							
							}
						}
						}
				}
			}
			
			}
		}	
		}
	
	

	



					


		
		









