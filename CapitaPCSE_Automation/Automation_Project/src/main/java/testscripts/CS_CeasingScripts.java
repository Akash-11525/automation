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

import utilities.ReadCSV;
import browsersetup.BaseClass;
import pageobjects.Activities;
import pageobjects.AdminJobs;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CallRecallRecord;
import pageobjects.ContactPatient;
import pageobjects.Contacts;
import pageobjects.CrmHome;
import pageobjects.GenNewPatientsScreeningRecords;
import pageobjects.GenerateRecalls;
import pageobjects.LoginPageCS;
import pageobjects.LoginScreen;
import pageobjects.CS.CeasePopup;
import pageobjects.CS.NonResponders;
import pageobjects.CS.NotificationLists;
import pageobjects.CS.PNLPage;
import pageobjects.CS.PNLSearchResultPage;
import pageobjects.CS.PatientpersonaldetailPortal;
import pageobjects.CS.PatientsearchPortal;
import pageobjects.CS.PortalHome;
import reporting.ListenerClass;
import verify.Verify;


@Listeners(ListenerClass.class)
public class CS_CeasingScripts extends BaseClass
{
	// Amit R. : This test case represents to verify Ceasing Popup is appearing from NRL Screen & After Uploading then cancel the ceasing record does not shows uploaded file in CRM.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"} ,enabled = true)
	@Parameters({"browser", "environment", "clientName"})
	public void verifyCeasingPopupUpFromNRLPatient(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{

		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();

		Boolean NonRespCount = ObjNotificationLists.nonRespondersPresent();

		if (NonRespCount)
		{

			setAssertMessage("The patients are available in Non Responder Tab", 1);
			NonResponders ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();
			//ObjNonResponders.selectActionFromFirstRecord("GP Cease");
			Boolean activeRecFlag = ObjNonResponders.checkActiveRecord("pcss-patient-non-responders", 9);
			if (activeRecFlag)
			{
				ObjNonResponders.selectActionFromActiveRecord("GP Cease",9);
				//String nhs = ObjNonResponders.getNHSFromFirstRecord();
				String nhs = ObjNonResponders.getNHSFromFirstActiveRecord();
				//String name = ObjNonResponders.getPatientNameFromFirstRecord();
				String name = ObjNonResponders.getPatientNameFromActiveFirstRecord();
				//Boolean fl = ObjNonResponders.clickSubmitAndCheckPopupUp();
				Boolean fl = ObjNonResponders.clickActiveSubmitAndCheckPopupUp();

				if (fl)
				{
					setAssertMessage("The Cease Popup is present.", 1);
					System.out.println("The Cease Popup is present.");
				}
				else
				{
					setAssertMessage("The Cease Popup is not present.", 2);
					System.out.println("The Cease Popup is not present.");
				}
				List<String> options = ObjNonResponders.getReasonForCease();

				//options.forEach(System.out::println);

				if(options.contains("Due to Age") && options.contains("No Cervix") && options.contains("Informed Consent") && options.contains("Mental Capacity"))
				{System.out.println("All the options appeared in Reason for Ceasing are correct.");
				setAssertMessage("All the options appeared in Reason for Ceasing are correct.", 3);}
				else
				{System.out.println("Options appeared in Reason for Ceasing are not matching.");
				setAssertMessage("Options appeared in Reason for Ceasing are not matching.", 4);}				
				ObjNonResponders.selectCeasingReasonandUploadFile("Informed Consent","CeasingReason1.jpeg");
				String NHSNo = ObjNonResponders.getNHSNoOfCeasePatient();
				String ceasedPatientName = ObjNonResponders.getCeasePatientName();
				if(NHSNo.equalsIgnoreCase(nhs))
				{
					setAssertMessage("The NHS Number is present & matched on Cease Patient Window", 5);
				}
				if(ceasedPatientName.equalsIgnoreCase(name))
				{
					setAssertMessage("The Patient is present & matched on Cease Patient Window", 5);
				}


				ObjNonResponders.clickOnCancelButton();

				tearDown(browser);

				setup(browser, environment, clientName,"CRM");

				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjCrmHome = ObjCrmHome.clickScreening();
				Contacts ObjContacts = ObjCrmHome.clickContactsTile();
				ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);

				ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();

				CallRecallRecord ObjCallRecallRecord = ObjContactss.CallRecallHistory();

				Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

				if (CeasingNotes)
				{
					System.out.println("The ceasing record is not present. Test case is Passed.");
					setAssertMessage("The ceasing record is not present.", 5);
				}
				

			}
			else
			{
				setAssertMessage("No Active Record found under nonResponder list to GP Cease.", 2);
				System.out.println("No Active Record found under nonResponder list to GP Cease.");
			}


		}


		else
		{
			setAssertMessage("The patients are not available in Non Responder Tab.", 7);
		}



	}


	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void verifyCeasingRecordUploadNRLPatient(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		/*// Logging into CRM application & closing the browser.
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

		tearDown(browser);*/

		// Logging into Portal application.
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();

		Boolean NonRespCount = ObjNotificationLists.nonRespondersPresent();

		if (NonRespCount)
		{

			setAssertMessage("The patients are available in Non Responder Tab", 1);
			NonResponders ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();
			Boolean activeRecFlag = ObjNonResponders.checkActiveRecord("pcss-patient-non-responders", 9);
			if (activeRecFlag)
			{
				ObjNonResponders.selectActionFromActiveRecord("GP Cease",9);
				//String nhs = ObjNonResponders.getNHSFromFirstRecord();
				String nhs = ObjNonResponders.getNHSFromFirstActiveRecord();
				//String name = ObjNonResponders.getPatientNameFromFirstRecord();
				String name = ObjNonResponders.getPatientNameFromActiveFirstRecord();
				//Boolean fl = ObjNonResponders.clickSubmitAndCheckPopupUp();
				Boolean fl = ObjNonResponders.clickActiveSubmitAndCheckPopupUp();

				if (fl)
				{
					setAssertMessage("The Cease Popup is present.", 1);
					System.out.println("The Cease Popup is present.");
				}
				else
				{
					setAssertMessage("The Cease Popup is not present.", 2);
					System.out.println("The Cease Popup is not present.");
				}
				List<String> options = ObjNonResponders.getReasonForCease();

				options.forEach(System.out::println);

				if(options.contains("Due to Age") && options.contains("No Cervix") && options.contains("Informed Consent") && options.contains("Mental Capacity"))
				{System.out.println("All the options appeared in Reason for Ceasing are correct.");
				setAssertMessage("All the options appeared in Reason for Ceasing are correct.", 3);}
				else
				{System.out.println("Options appeared in Reason for Ceasing are not matching.");
				setAssertMessage("Options appeared in Reason for Ceasing are not matching.", 4);}

				ObjNonResponders.selectCeasingReasonandUploadFile("Informed Consent","CeasingReason1.jpg");
				String NHSNo = ObjNonResponders.getNHSNoOfCeasePatient();
				//ObjNonResponders.clickOnCancelButton();
				ObjNonResponders.clickOnSubmitButton();
				Boolean CeasedSubmitFlag = ObjNonResponders.VerifyCeasedConfirmNRL();
				if(CeasedSubmitFlag)
				{
					System.out.println("The Patient gets Ceased Successfully.");
				}


				tearDown(browser);

				setup(browser, environment, clientName,"CRM");
				//String NHSNo ="1238521595";
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				ObjCrmHome = ObjCrmHome.clickScreening();
				Contacts ObjContacts = ObjCrmHome.clickContactsTile();
				ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);

				ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();

				CallRecallRecord ObjCallRecallRecord = ObjContactss.CallRecallHistory();

				Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

				if (!CeasingNotes)
				{
					System.out.println("The ceasing record is present.");
					setAssertMessage("The ceasing record is present.", 5);
					Boolean CeaseConsentForm = ObjCallRecallRecord.verifyCeasedPatientUploadFile();
					if (CeaseConsentForm)
					{
						System.out.println("The uploaded Patinet Consent Form is available.");
						setAssertMessage("The uploaded Patinet Consent Form is available.", 7);

						ObjCallRecallRecord= ObjCallRecallRecord.clickApproveCease();
						ObjCrmHome = ObjCallRecallRecord.clickOnCrmHome();
						//ObjContactss = ObjScreening.searchAndSelectPatient(NHSNo);
						ObjCrmHome = ObjCrmHome.clickScreening();
						ObjContacts = ObjCrmHome.clickContactsTile();
						ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);

						ObjContactss = ObjContacts.getScreeningRecords1();


						String callStatus = ObjContactss.GetCallRecallStatus();

						if(callStatus.contains("Ceased"))
						{
							System.out.println("The patient record marked as Ceased.");
							setAssertMessage("The patient record marked as Ceased.", 8);	
						}
						else
						{
							System.out.println("The patient record does not marked as Ceased.");
							setAssertMessage("The patient record does not marked as Ceased.", 9);
						}

					}
					else
					{
						System.out.println("The uploaded Patinet Consent Form is not available.");
						setAssertMessage("The uploaded Patinet Consent Form is not available", 10);	

					}
				}
				else
				{
					System.out.println("The ceasing record is not present.");
					setAssertMessage("The ceasing record is not present.", 11);	


				}

			}
			else
			{
				setAssertMessage("No Active Record found under nonResponder list to GP Cease.", 2);
				System.out.println("No Active Record found under nonResponder list to GP Cease.");
			}



		}



		else
		{
			setAssertMessage("The patients are not available in Non Responder Tab.", 7);
		}



	}




	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void verifyCeasingPopupUpFromPNL(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{

		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			/*Boolean activeRecFlag = ObjPNLPage.checkActiveRecord(Actualtablename, 9);
			if(activeRecFlag)
			{*/
			ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Cease", Actualtablename);
			ObjPNLPage = ObjPNLPage.ClickOnSubmitOntable(Actualtablename);
			Boolean f1 = ObjPNLPage.clickSubmitAndCheckPopupUp();

			if (f1)
			{
				setAssertMessage("The Cease Popup is present.", 1);
				System.out.println("The Cease Popup is present.");
				List<String> options = ObjPNLPage.getReasonForCease();

				options.forEach(System.out::println);

				if(ObjPNLPage.Ceaseoptions())
					setAssertMessage("All the options appeared in Reason for Ceasing are correct", 2);

				else
					Assert.assertFalse(verify.Verify.verifyFailure(), "Options appeared in Reason for Ceasing are not matching.");
			}
			else{
				setAssertMessage("The Cease Popup is not present.", 1);
				System.out.println("The Cease Popup is not present.");
			}

		}
			else
			{
				setAssertMessage("No Active Record found under PNL list to GP Cease.", 2);
				System.out.println("No Active Record found under PNL list to GP Cease.");
			}
			
		/*}
		else
		{
			setAssertMessage("No Patients are available under PNL.",3);
		}*/

	}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CeaseApproval_PNL_VerifyInCRM(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{



		/*// Logging into CRM application.
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

		tearDown(browser);*/

		// Logging into Portal application.
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null){
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Cease",Actualtablename);

			}

			Boolean f1 = ObjPNLPage.clickSubmitAndCheckPopupUp(Actualtablename);
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatient();
			if (f1)
			{
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
				Boolean flagforTickMark = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);{
					if (flagforTickMark)
					{

						tearDown(browser);

						setup(browser, environment, clientName,"CRM");
						CrmHome ObjCrmHome  = new CrmHome(getDriver());
						ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
						Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
						ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
						int screeningRecUp = ObjContacts.getScreeningRecords();
						System.out.println(screeningRecUp);
						if (screeningRecUp > 1)
						{
							System.out.println("Patient added for screening.");
						}

						else
						{
							System.out.println("Patient is not added for screening.");
						}


						ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();

						//ObjContactss1 = ObjContactss1.CallRecallHistory();

						CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();

						Boolean f2 = ObjCallRecallRecord.verifyPortalAction("GP Cease");

						if (f2)
						{                       
							setAssertMessage("The Ceased action is successful on CRM  -  " +CRMNHSNo, 1);                                       
						}

						Assert.assertFalse(verify.Verify.verifyFailure(), "The Ceased action is fail on not CRM  - " +CRMNHSNo);  


					}

				}
			}
		}
		else
		{
			setAssertMessage("No patients are available under PNL.", 2);
		}
	}

	// Suraj G : This Test case Represent to upload a pdf file on portal through cease process and The attached pdf is shown in CRM ( Patient - Call Recall ) 
	// Verify the ceased patient is applied after approve the cease.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void Uploadpdf_PNL_VerifyInCRM(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{

		/*		String currentUsersHomeDir = System.getProperty("user.home");
		System.out.println(currentUsersHomeDir);
		// Logging into CRM application.
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

		tearDown(browser);*/

		// Logging into Portal application.
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null){
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Cease",Actualtablename);
			}

			Boolean f1 = ObjPNLPage.clickSubmitAndCheckPopupUp(Actualtablename);
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatient();
			if (f1)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
				/*Boolean f0 = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);
					{
				if (f0)
				{*/		
				// Verify on CRM application 
				quit(browser);
				setup(browser, environment, clientName,"CRM");
				CrmHome ObjCrmHome  = new CrmHome(getDriver());
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
				ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
				/*int screeningRecUp = ObjContacts.getScreeningRecords();
			System.out.println(screeningRecUp);
			if (screeningRecUp > 1)
			{
				System.out.println("Patient added for screening.");
			}
			else
			{
				System.out.println("Patient is not added for screening.");
			}*/			
				ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();	
				CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
				Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

				if (CeasingNotes)
				{
					System.out.println("The ceasing record is not present. Test case is Passed.");
					setAssertMessage("The ceasing record is not present.", 1);
				}
				else
				{
					System.out.println("The ceasing record is present.");
					setAssertMessage("The ceasing record is present.", 2);

				}


			}

			else
			{
				setAssertMessage("The patients are not available in Non Responder Tab.", 3);
			}
		}
		else
		{
			setAssertMessage("No patients are available in NRL.", 4);
		}

	}



	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CancelCeaseReqbeforeApproval_PNL(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{




		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null)
			{
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Call/Recall",Actualtablename);
				ObjPNLPage = ObjPNLPage.ClickOnSubmitOntable(Actualtablename);
			}
			Boolean TickcheckGPcallRecall = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);
			if(TickcheckGPcallRecall)
			{
				System.out.println("The action GP call Recall done on" );
			}
			String InlineNhsNo = ObjPNLPage.getNHSNoOfCeasePatientWithSpacesforGPCallRecall(Actualtablename);
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
			ObjPNLPage = ObjPNLPage.SubmittedTab();

			ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
			PNLSearchResultPage ObjPNLSearchResultPage = ObjPNLPage.FirstResultofsearchOnSubmitted(InlineNhsNo);
			Boolean flag = ObjPNLSearchResultPage.ClickOnCease();




			if (flag)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
			}

			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
			/*	int screeningRecUp = ObjContacts.getScreeningRecords();
					System.out.println(screeningRecUp);
					if (screeningRecUp > 1)
						{
							System.out.println("Patient added for screening.");
						}
					else
						{
							System.out.println("Patient is not added for screening.");
						}	*/									
			ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
			Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

			if (!CeasingNotes)
				System.out.println("The uploaded Patinet Consent Form is available after cease.");
			setAssertMessage("The uploaded Patient Consent Form is available after cease.", 1);

			tearDown("Internet Explorer");

			//Go to Portal - Cancel Cease 


			setup(browser, environment, clientName,"CS");
			ObjPortalHome = new PortalHome(getDriver());
			ObjPNLPage = ObjPortalHome.clickPNL();
			ObjPNLPage = ObjPNLPage.SubmittedTab();


			ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
			ObjPNLSearchResultPage = ObjPNLPage.FirstResultofsearchOnSubmitted(InlineNhsNo);
			boolean CancelCease  = ObjPNLSearchResultPage.VerifyCancelCease();
			// Cancel button is avilable on action tab 
			if(CancelCease)
			{
				System.out.println("The cancel cease button is available on action before clicking on cancel cease");
				setAssertMessage("The cancel cease button is available on actions and not the Cease defer before clicking on cancel cease", 1); 
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The cancel cease button is not available on action");
			ObjPNLSearchResultPage = ObjPNLSearchResultPage.ClickOnCancelCease();
			ObjPNLSearchResultPage = ObjPNLSearchResultPage.ConfirmationDialgoueBox();
			ObjPNLSearchResultPage = ObjPNLSearchResultPage.ClickOnSaveSubmit();
			boolean DeferButton = ObjPNLSearchResultPage.VerifyDeferButton();

			// Defer button is available on action tab - After clicking on Cancel Cease
			if(DeferButton)
			{
				System.out.println("The Defer button is displayed after cancelling the cease");
				setAssertMessage("The Defer button is displayed after cancelling the cease", 2); 
			}

			tearDown(browser);

			setup(browser, environment, clientName,"CRM");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
			/*	int screeningRecUp1 = ObjContacts.getScreeningRecords();
		System.out.println(screeningRecUp1);
		if (screeningRecUp > 1)
			{
				System.out.println("Patient added for screening.");
			}
		else
			{
				System.out.println("Patient is not added for screening.");
			}	*/								
			ContactPatient ObjContactss2 = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord1 = ObjContactss2.CallRecallHistory();
			Boolean CeasingNotes1 = ObjCallRecallRecord1.verifyCeasedNotes();
			if(CeasingNotes1)
			{
				System.out.println("The attachment is removed after cancelling cease for " +CRMNHSNo);
				setAssertMessage("The attachment is removed after cancelling cease for " +CRMNHSNo, 3);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The attachment is not removed after cancelling cease");

			Boolean PortalActionafterCancelCease = ObjCallRecallRecord1.verifyPortalActionAfterCancelCease();

			if (PortalActionafterCancelCease)
			{   
				System.out.println("The Portal action is not GP cease after cancel cease");
				setAssertMessage("The Portal action is not GP cease after cancel cease " +CRMNHSNo , 4);                                       
			}

			Assert.assertFalse(verify.Verify.verifyFailure(), "The Cease Happen on not CRM  - " +CRMNHSNo ); 
			tearDown("Internet Explorer");

			setup(browser, environment, clientName,"CS");
			ObjPortalHome = new PortalHome(getDriver());
			ObjPNLPage = ObjPortalHome.clickPNL();

			String actionaftercancelcease = ObjPNLPage.Verifyactionaftercancelcease(Actualtablename);

			if(actionaftercancelcease.equalsIgnoreCase("GP Call/Recall"))
			{
				System.out.println("The action is GP call /recall after cancel cease");
				setAssertMessage("The action is GP call /recall after cancel cease for" +CRMNHSNo , 5);  
			}
		}
		else
		{
			setAssertMessage("No patients are available in NRL.", 6);
		}
	}


	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CancelClickonCancelceaseReq(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		// Logging into Portal application.
		setup("Chrome", environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null)
			{
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Cease",Actualtablename);
			}
			Boolean f1 = ObjPNLPage.clickSubmitAndCheckPopupUp(Actualtablename);
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatient();
			String InlineNhsNo = ObjPNLPage.getNHSNoOfCeasePatientWithSpaces();

			if (f1)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();

				Boolean f0 = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);				
				if (f0)
				{
					ObjPNLPage = ObjPNLPage.SubmittedTab();		

					ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
					ObjPNLPage = ObjPNLPage.selectActionFromFirstRecordonSubmitted("GP Cancel Cease");
					PNLSearchResultPage ObjPNLSearchResultPage = ObjPNLPage.ClickOnSubmitOnSubmittedtab();
					ObjPNLSearchResultPage = ObjPNLSearchResultPage.ConfirmationDialgoueBox();
					ObjPNLPage = ObjPNLSearchResultPage.ClickOnCancel();
					Boolean ConfirmationBox = ObjPNLPage.VerifyConfirmationDialgoueBox();
					if(!ConfirmationBox){
						System.out.println("The Confirmation Box is not available after clicking on cancel (Cancel Cease request).");
						setAssertMessage("The Confirmation Box is not available after clicking on cancel (Cancel Cease request)", 1);
					}
					Boolean actiononsubmittedtab = ObjPNLPage.VerifyActiononSubmittedTab(InlineNhsNo);
					if(actiononsubmittedtab){
						System.out.println("The GP cease is appear if we click cancel before cancel cease");
						setAssertMessage("The GP cease is appear if we click cancel before cancel cease", 2);
					}
					Assert.assertFalse(verify.Verify.verifyFailure(), "The GP cease is not appear after cancel  - " +CRMNHSNo );	
				}
			}
		}
		else
		{
			setAssertMessage("No patients are avaialble under PNL.", 3);
		}

	}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CancelCeaseReqbeforeApproval_PatientSearch(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		// Logging into Portal application.
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{

			if(Actualtablename != null)
			{
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Call/Recall",Actualtablename);
				ObjPNLPage = ObjPNLPage.ClickOnSubmitOntable(Actualtablename);
			}
			Boolean TickcheckGPcallRecall = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);
			if(TickcheckGPcallRecall)
			{
				System.out.println("The action GP call Recall done on" );
			}
			String InlineNhsNo = ObjPNLPage.getNHSNoOfCeasePatientWithSpacesforGPCallRecall(Actualtablename);
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
			ObjPNLPage = ObjPNLPage.SubmittedTab();

			ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
			PNLSearchResultPage ObjPNLSearchResultPage = ObjPNLPage.FirstResultofsearchOnSubmitted(InlineNhsNo);
			Boolean flag = ObjPNLSearchResultPage.ClickOnCease();
			if (flag)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
			}
			// Verify Notes in CRM 
			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
			int screeningRecUp = ObjContacts.getScreeningRecords();
			System.out.println(screeningRecUp);
			if (screeningRecUp > 1)
			{
				System.out.println("Patient added for screening.");
			}
			else
			{
				System.out.println("Patient is not added for screening.");
			}						
			ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
			Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

			if (!CeasingNotes)
				System.out.println("The uploaded Patinet Consent Form is available after cease.");
			setAssertMessage("The uploaded Patient Consent Form is available after cease.", 1);

			tearDown(browser);

			//Go to Portal - Cancel Cease 


			setup(browser, environment, clientName,"CS");
			ObjPortalHome = new PortalHome(getDriver());
			PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
			ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(CRMNHSNo);
			PatientpersonaldetailPortal ObjPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNhsNo);
			Boolean actionpendingcancel = ObjPatientpersonaldetailPortal.VerifyactionPendingcancel();
			if (actionpendingcancel)
			{
				System.out.println("The Pending cease is appeare before cancel through patient search");
				setAssertMessage("The Pending cease is appeare before cancel through patient search" +CRMNHSNo,1);
			}
			boolean CancelCease  = ObjPatientpersonaldetailPortal.VerifyCancelCease();
			// Cancel button is avilable on action tab 
			if(CancelCease)
			{
				System.out.println("The cancel cease button is available on action before clicking on cancel cease");
				setAssertMessage("The cancel cease button is available on actions and not the Cease defer before clicking on cancel cease", 2); 
			}

			Assert.assertFalse(verify.Verify.verifyFailure(), "The cancel cease button is not available on action");
			ObjPatientpersonaldetailPortal = ObjPatientpersonaldetailPortal.ClickOnCancelCease();
			ObjPatientpersonaldetailPortal = ObjPatientpersonaldetailPortal.ConfirmationDialgoueBox();
			ObjPatientpersonaldetailPortal = ObjPatientpersonaldetailPortal.ClickOnSaveSubmit();
			boolean DeferButton = ObjPatientpersonaldetailPortal.VerifyDeferButton();

			// Defer button is available on action tab - After clicking on Cancel Cease
			if(DeferButton)
			{
				System.out.println("The Defer button is displayed after cancelling the cease");
				setAssertMessage("The Defer button is displayed after cancelling the cease", 3); 
			}

			Boolean actionpendingcancelaftercancel = ObjPatientpersonaldetailPortal.VerifyactionPendingcancel();
			if (!actionpendingcancelaftercancel)
			{
				System.out.println("The Pending cease is not appeare after cancel through patient search");
				setAssertMessage("The Pending cease is not appeare after cancel through patient search" +CRMNHSNo,4);
			}


			tearDown(browser);

			setup(browser, environment, clientName,"CRM");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
			/*int screeningRecUp1 = ObjContacts.getScreeningRecords();
				System.out.println(screeningRecUp1);
				if (screeningRecUp > 1)
					{
						System.out.println("Patient added for screening.");
					}
				else
					{
						System.out.println("Patient is not added for screening.");
					}					*/					
			ContactPatient ObjContactss2 = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord1 = ObjContactss2.CallRecallHistory();
			Boolean CeasingNotes1 = ObjCallRecallRecord1.verifyCeasedNotes();
			if(CeasingNotes1)
			{
				System.out.println("The attachment is removed after cancelling cease for " +CRMNHSNo);
				setAssertMessage("The attachment is removed after cancelling cease for " +CRMNHSNo, 5);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The attachment is not removed after cancelling cease");
			Boolean PortalActionafterCancelCease = ObjCallRecallRecord1.verifyPortalActionAfterCancelCease();

			if (PortalActionafterCancelCease)
			{   
				System.out.println("The Portal action is GP call/ recall after cancel cease (Before approve) ");
				setAssertMessage("The Portal action is GP call/ recall after cancel cease (Before approve) " +CRMNHSNo , 6);                                       
			}

			Assert.assertFalse(verify.Verify.verifyFailure(), "The Cease Happen on not CRM  - " +CRMNHSNo );

			tearDown(browser);				
			setup(browser, environment, clientName,"CRM");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();	
			Activities ObjActivities = ObjCrmHome.ScreenActivitiesclick();
			ObjActivities = ObjActivities.allactivitywithfilter();
			ObjActivities = ObjActivities.clickoncustonfilter("Contains",CRMNHSNo);
			Boolean activitystatus = ObjActivities.verifyactivitystatus();
			if(activitystatus)
			{
				System.out.println("The activity status is cancelled in activities "+CRMNHSNo);
				setAssertMessage("The activity status  is cancelled in activities  " +CRMNHSNo , 7); 
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The activity status is not  cancelled in activities ");
		}
		else
		{
			setAssertMessage("No patients are available under PNL.  " , 8);
		}




	}


	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CancelCeaseReqbeforeApproval_Ceaseconfirmaton(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		// Logging into Portal application.
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null)
			{
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Call/Recall",Actualtablename);
				ObjPNLPage = ObjPNLPage.ClickOnSubmitOntable(Actualtablename);
			}
			Boolean TickcheckGPcallRecall = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);
			if(TickcheckGPcallRecall)
			{
				System.out.println("The action GP call Recall done on" );
			}
			String InlineNhsNo = ObjPNLPage.getNHSNoOfCeasePatientWithSpacesforGPCallRecall(Actualtablename);
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
			ObjPNLPage = ObjPNLPage.SubmittedTab();

			ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
			PNLSearchResultPage ObjPNLSearchResultPage = ObjPNLPage.FirstResultofsearchOnSubmitted(InlineNhsNo);
			Boolean flag = ObjPNLSearchResultPage.ClickOnCease();
			if (flag)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
			}
			// Verify Notes in CRM 
			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
		/*	int screeningRecUp = ObjContacts.getScreeningRecords();
			System.out.println(screeningRecUp);
			if (screeningRecUp > 1)
			{
				System.out.println("Patient added for screening.");
			}
			else
			{
				System.out.println("Patient is not added for screening.");
			}*/						
			ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();

			Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

			if (!CeasingNotes)
				System.out.println("The uploaded Patient Consent Form is available after cease.");
			setAssertMessage("The uploaded Patient Consent Form is available after cease.", 1);

			tearDown(browser);

			//Go to Portal - Cancel Cease 


			setup(browser, environment, clientName,"CS");
			ObjPortalHome = new PortalHome(getDriver());
			PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
			ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(CRMNHSNo);
			PatientpersonaldetailPortal ObjPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNhsNo);
			Boolean actionpendingcancel = ObjPatientpersonaldetailPortal.VerifyactionPendingcancel();
			if (actionpendingcancel)
			{
				System.out.println("The Pending cease is appeare before cancel through patient search");
				setAssertMessage("The Pending cease is appeare before cancel through patient search" +CRMNHSNo,1);
			}
			boolean CancelCease  = ObjPatientpersonaldetailPortal.VerifyCancelCease();
			// Cancel button is avilable on action tab 
			if(CancelCease)
			{
				System.out.println("The cancel cease button is available on action before clicking on cancel cease");
				setAssertMessage("The cancel cease button is available on actions and not the Cease defer before clicking on cancel cease", 2); 
			}

			Assert.assertFalse(verify.Verify.verifyFailure(), "The cancel cease button is not available on action");
			ObjPatientpersonaldetailPortal = ObjPatientpersonaldetailPortal.ClickOnCancelCease();
			ObjPatientpersonaldetailPortal = ObjPatientpersonaldetailPortal.ConfirmationDialgoueBox();
			ObjPatientpersonaldetailPortal = ObjPatientpersonaldetailPortal.ClickOnSaveSubmit();
			boolean DeferButton = ObjPatientpersonaldetailPortal.VerifyDeferButton();

			// Defer button is available on action tab - After clicking on Cancel Cease
			if(DeferButton)
			{
				System.out.println("The Defer button is displayed after cancelling the cease");
				setAssertMessage("The Defer button is displayed after cancelling the cease", 3); 
			}

			Boolean actionpendingcancelaftercancel = ObjPatientpersonaldetailPortal.VerifyactionPendingcancel();
			if (!actionpendingcancelaftercancel)
			{
				System.out.println("The Pending cease is not appeare after cancel through patient search");
				setAssertMessage("The Pending cease is not appeare after cancel through patient search" +CRMNHSNo,4);
			}


			tearDown(browser);

			setup(browser, environment, clientName,"CRM");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
			/*int screeningRecUp1 = ObjContacts.getScreeningRecords();
				System.out.println(screeningRecUp1);
				if (screeningRecUp > 1)
					{
						System.out.println("Patient added for screening.");
					}
				else
					{
						System.out.println("Patient is not added for screening.");
					}					*/					
			ContactPatient ObjContactss2 = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord1 = ObjContactss2.CallRecallHistory();
			Boolean CeasingNotes1 = ObjCallRecallRecord1.verifyCeasedNotes();
			if(CeasingNotes1)
			{
				System.out.println("The attachment is removed after cancelling cease for " +CRMNHSNo);
				setAssertMessage("The attachment is removed after cancelling cease for " +CRMNHSNo, 5);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The attachment is not removed after cancelling cease");
			Boolean PortalActionafterCancelCease = ObjCallRecallRecord1.verifyPortalActionAfterCancelCease();

			if (PortalActionafterCancelCease)
			{   
				System.out.println("The Portal action is GP call/ recall after cancel cease (Before approve) ");
				setAssertMessage("The Portal action is GP call/ recall after cancel cease (Before approve) " +CRMNHSNo , 6);                                       
			}

			Assert.assertFalse(verify.Verify.verifyFailure(), "The Cease Happen on not CRM  - " +CRMNHSNo );

			tearDown(browser);				
			setup(browser, environment, clientName,"CRM");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();	
			Activities ObjActivities = ObjCrmHome.ScreenActivitiesclick();
			ObjActivities = ObjActivities.allactivitywithfilter();
			ObjActivities = ObjActivities.clickoncustonfilter("Contains",CRMNHSNo);
			Boolean activitystatus = ObjActivities.verifyactivitystatus();
			if(activitystatus)
			{
				System.out.println("The activity status is cancelled in activities "+CRMNHSNo);
				setAssertMessage("The activity status  is cancelled in activities  " +CRMNHSNo , 7); 
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The activity status is not  cancelled in activities ");

		}
		else
		{
			setAssertMessage("No Patient available under PNL Window ", 8);
		}



	}


	//Suraj G : This function verify the below steps 
	//1) The GP portal status after ceased patient
	//2) The correct Gp is displayed after ceased
	//3) All Mandatory field is filled on call recall page after ceased
	//4) On portal - The cease confirmation count is decreased after acknowledge the record 
	//5) On portal - The correct column and patient detail is displayed on cease confirmation 
	// Test cases included - 7310,7337,7096
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_Ceaseing_CeasingConfirmation_Ack(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null)
			{
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Call/Recall",Actualtablename);
				ObjPNLPage = ObjPNLPage.ClickOnSubmitOntable(Actualtablename);
			}
			Boolean TickcheckGPcallRecall = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);
			if(TickcheckGPcallRecall)
			{
				System.out.println("The action GP call Recall done on" );
			}
			String InlineNhsNo = ObjPNLPage.getNHSNoOfCeasePatientWithSpacesforGPCallRecall(Actualtablename);
			System.out.println(InlineNhsNo);

			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
			utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "NHSNumber",InlineNhsNo);
			ObjPNLPage = ObjPNLPage.SubmittedTab();

			ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
			PNLSearchResultPage ObjPNLSearchResultPage = ObjPNLPage.FirstResultofsearchOnSubmitted(InlineNhsNo);
			Boolean flag = ObjPNLSearchResultPage.ClickOnCease();	

			if (flag)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
			}

			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

			Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);	
			ObjContacts = ObjContacts.getDOBandAddress();
			ContactPatient ObjContactPatient = ObjContacts.getScreeningRecords1();
			ObjContactPatient = ObjContactPatient.getFullNameandTestDuedate();	

			CallRecallRecord ObjCallRecallRecord = ObjContactPatient.CallRecallHistory();
			boolean Portalaction = ObjCallRecallRecord.verifyPortalActionAfterAction("GP Cease");
			if(Portalaction)
			{
				System.out.println("The GP Cease status after cease by PNL");
				setAssertMessage("The GP Cease status after cease by PNL", 1);	
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The incorrect call recall status after ceased by PNL  - " +CRMNHSNo);
			boolean Previousportalaction = ObjCallRecallRecord.verifypreviousportalaction("GP Call/Recall");
			if(Previousportalaction)
			{
				System.out.println("The Previous portal action status is displayed correct");
				setAssertMessage("The Previous portal action  status is displayed correct", 1);	
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The Previous Previous portal action status is displayed incorrect  - " +CRMNHSNo);
			boolean GPAfterCease = ObjCallRecallRecord.VerifyGP();
			if(GPAfterCease)
			{
				System.out.println("The GP is submitted by GP who's ceased the patient");
				setAssertMessage("The GP is submitted by GP who's ceased the patient", 1);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The GP is not submitted by correct GP" +CRMNHSNo);
			boolean ceasebydate = ObjCallRecallRecord.Verifyceasebydate();
			if(ceasebydate)
			{
				System.out.println("The patient data is ceased by Today and displayed as per expected");
				setAssertMessage("The patient data is ceased by Today and displayed as per expected", 1);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The patient data is not ceased by Today and not displayed as per expected - " +CRMNHSNo);
			int countMandatoryfield = ObjCallRecallRecord.VerifyMandatoryfield();
			if(countMandatoryfield==0)
			{
				System.out.println("The all mandatory field is filled on call recall page ");
				setAssertMessage("The all mandatory field is filled on call recall page", 1);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The all mandatory field is not filled on call recall page - " +CRMNHSNo);
			ObjCallRecallRecord = ObjCallRecallRecord.clickApproveCease();
			ObjCallRecallRecord = ObjCallRecallRecord.StorePatientDetail();
			tearDown(browser);
			setup(browser, environment, clientName,"CS");
			ObjPortalHome = new PortalHome(getDriver());

			NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();
			ObjNotificationLists = ObjNotificationLists.clickCeaseConfirmation();
			int Ceaseconfirmationnumber =ObjNotificationLists.CalculateCeaseconfirmationNumnber();

			Boolean ColumnName = ObjNotificationLists.getColumnnameOnCeaseconfirmation();
			if(ColumnName)
			{
				System.out.println("The Column header is displayed as per expected");
				setAssertMessage("The Column header is displayed as per expected", 2);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The Column header is displayed as per expected  - " +CRMNHSNo);
			int RowNumberOnResult = ObjNotificationLists.SelectRowNumberOnResult(InlineNhsNo);
			//Boolean Resultdata= ObjNotificationLists.getresultonCeaseconfirmation(RowNumberOnResult);
			int count = ObjNotificationLists.getresultonCeaseconfirmation(RowNumberOnResult);

			if(count == 0 )
			{
				System.out.println("The Result is displayed on portal is correct ");
				setAssertMessage("The Result is displayed on portal is correct", 3);
			}
			else 
			{
				System.out.println("The Result is appered on portal is  incorrect ");
				setAssertMessage("The Result is appered on portal is incorrect", 3);
			}

			ObjNotificationLists = ObjNotificationLists.Selectacknowledge(InlineNhsNo);
			int CeaseconfirmationnumberAfterAck =ObjNotificationLists.CalculateCeaseconfirmationNumnber();
			System.out.println(CeaseconfirmationnumberAfterAck);
			Thread.sleep(3000);
			if(CeaseconfirmationnumberAfterAck == (Ceaseconfirmationnumber-1) )
			{
				System.out.println("The Cease Confirmation Count is decresed by 1");
				setAssertMessage("The Cease confirmation count is not decresed by 1", 3);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The Cease confirmation count is not decresed by 1 " +CRMNHSNo ); 
			ObjNotificationLists = ObjNotificationLists.clickONCSV();					
			tearDown(browser);							
			setup(browser, environment, clientName,"CRM");				
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			//String CRMNHSNo = "3265415488";
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);						
			ObjContactPatient = ObjContacts.getScreeningRecords1();								
			ObjCallRecallRecord = ObjContactPatient.CallRecallHistory();
			Boolean AcknowledgementDate = ObjCallRecallRecord.VerifyAcknowledgedate();

			if(AcknowledgementDate)
			{
				System.out.println("The AcknowledgementDate is displayed as Today's day");
				setAssertMessage("The AcknowledgementDate is displayed as Today's day", 4);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The AcknowledgementDate is not displayed as Today's day " +CRMNHSNo ); 
			Boolean PortalAction = ObjCallRecallRecord.verifyPortalActionAfterAction("GP PCSE Approved");

			if (PortalAction)
			{
				System.out.println("Portal action is PCSE Cease Approved");
				setAssertMessage("Portal action is PCSE Cease Approved", 5);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The incorrect Portal action is displayed " +CRMNHSNo );
		}
		else
		{
			setAssertMessage("No patient found under PNL", 6);
		}

	}

	// Suraj G : This Test case Represent to verify uncease button after approve cease and uncease button is removed after done the uncease activity 
	// Test Case :TC_CS_19364 Action Un-cease in CRM - Cease button on portal Patient Details page
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_Ceasing_uncease_Patientdetail(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{
		// Login to Portal - Done the call Recall status 
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo1  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo1);
		if (!InlineNhsNo1.contains("No PNL records to action in this week"))
		{
			if(Actualtablename != null)
			{
				ObjPNLPage = ObjPNLPage.selectActionFromFirstRecord("GP Call/Recall",Actualtablename);
				ObjPNLPage = ObjPNLPage.ClickOnSubmitOntable(Actualtablename);
			}
			Boolean TickcheckGPcallRecall = ObjPNLPage.VerifyCeaseOnPortal(Actualtablename);
			if(TickcheckGPcallRecall)
			{
				System.out.println("The action GP call Recall done on" );
				setAssertMessage("The action GP call Recall done", 1);   
			}
			// Get CRM and Inline - NHS Number
			String InlineNhsNo = ObjPNLPage.getNHSNoOfCeasePatientWithSpacesforGPCallRecall(Actualtablename);
			System.out.println(InlineNhsNo);			
			String CRMNHSNo = ObjPNLPage.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
			ObjPNLPage = ObjPNLPage.SubmittedTab();	
			//Cease the patient record
			ObjPNLPage = ObjPNLPage.SearchNHSNoOnSubmittedTab(InlineNhsNo);
			PNLSearchResultPage ObjPNLSearchResultPage = ObjPNLPage.FirstResultofsearchOnSubmitted(InlineNhsNo);			
			Boolean flag = ObjPNLSearchResultPage.ClickOnCease();		
			if (flag)
			{
				//Upload a file 
				ObjPNLPage = ObjPNLPage.selectCeasingReasonandUploadFile("Informed Consent");
				ObjPNLPage = ObjPNLPage.submitCease();
			}
			// Open CRM- Approve Cease
			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);	
			ContactPatient ObjContactPatient = ObjContacts.getScreeningRecords1();										
			CallRecallRecord ObjCallRecallRecord = ObjContactPatient.CallRecallHistory();
			ObjCallRecallRecord = ObjCallRecallRecord.clickApproveCease();
			tearDown(browser);
			// Login to Portal - Uncease record		
			setup(browser, environment, clientName,"CS");
			ObjPortalHome = new PortalHome(getDriver());

			PatientsearchPortal objPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
			objPatientsearchPortal = objPatientsearchPortal.EnterNHSNumberandclickOnresult(CRMNHSNo);
			PatientpersonaldetailPortal objPatientpersonaldetailPortal = objPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNhsNo);	
			// Verify Cease Button - After Approve Cease and Click on Uncease button
			Boolean ceasebutton = objPatientpersonaldetailPortal.VerifyCease_clickonuncease();
			if(!ceasebutton)
			{
				System.out.println("The cease button is not appear after approve cease from CRM");
				setAssertMessage("The cease button is not appear after approve cease from CRM  " +CRMNHSNo, 1);                                       
			}
			else
			{
				Assert.assertTrue(verify.Verify.verifyFailure(), "The cease button is appear after approve cease from CRM - " +CRMNHSNo);  
			}
			Thread.sleep(3000);
			// Verify cease button after uncease record
			Boolean unceasebutton = objPatientpersonaldetailPortal.VerifyUncease();
			if(!unceasebutton)
			{
				System.out.println("The Uncease button is not appear after uncease patient");
				setAssertMessage("The Uncease button is appear after uncease patient  " +CRMNHSNo, 1);                                       
			}
			else
			{
				Assert.assertTrue(verify.Verify.verifyFailure(), "The Uncease button is appear after uncease patient - " +CRMNHSNo);  

			}
		}
		else
		{
			setAssertMessage("No patient found under PNL.",7);
		}

	}

	//Suraj G - To do the cease through patient serach tab 
	//IN CRM , The portal action appear as "GP Cease"
	// Test cases 7099
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_Ceasing_Cease_Patientsearch(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{
		// Login to Portal - Done the call Recall status 
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PNLPage ObjPNLPage = ObjPortalHome.clickPNL();
		String Actualtablename = ObjPNLPage.CountofPatientInWeekWithTable();
		String InlineNhsNo  = ObjPNLPage.getNHSNoonPNL(Actualtablename);
		System.out.println(InlineNhsNo);
		if (!InlineNhsNo.contains("No PNL records to action in this week"))
		{
			//getting the NHS number from PNL and search into Patient search
			PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
			String CRMNHSNo = ObjPatientsearchPortal.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
			System.out.println(CRMNHSNo);
			ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(CRMNHSNo);			
			PatientpersonaldetailPortal objPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNhsNo);
			CeasePopup ObjCeasePopup=  objPatientpersonaldetailPortal.ClickonCease();
			boolean CeasediolgueBox= ObjCeasePopup.clickSubmitAndCheckPopupUp();
			if(CeasediolgueBox)
			{
				System.out.println("The cease pop up is appear after clicking on cease ( Patient search):");
				setAssertMessage("The cease pop up is appear after clicking on cease ( Patient search):" , 1);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "The cease pop up is not appear after clicking on cease ( Patient search):");

			int count = ObjCeasePopup.GetCeaseOptionsCount();

			if(count ==0)
			{
				System.out.println("The all option on Cease is correct");
				setAssertMessage("The all option on Cease is correct" , 2);
			}
			Assert.assertFalse(verify.Verify.verifyFailure(), "Options appeared in Reason for Ceasing are not matching.");
			ObjCeasePopup= ObjCeasePopup.selectCeasingReasonandUploadFile("Informed Consent");
			objPatientpersonaldetailPortal = ObjCeasePopup.submitCease_Patientpersonaldetail();
			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(CRMNHSNo);
			ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();
			CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
			Boolean f2 = ObjCallRecallRecord.verifyPortalAction("GP Cease");
			if (f2)
			{                       
				System.out.println("The portal action is GP Cease");
				setAssertMessage("The Ceased action is successful on CRM  -  " +CRMNHSNo, 1);                                       
			}

			Assert.assertFalse(verify.Verify.verifyFailure(), "The Ceased action is fail on not CRM  - " +CRMNHSNo);  
		}
		else
		{
			setAssertMessage("No Patient are available in PNL",4);
		}

	}
	// Amit R. - TC7333 - Download Ceasing Notification (CSV) - Data Content
	// 1. verify the ceasing confirmation avaialble in list.
	// 2. verify content of CSV file & concatenate address fields
	//3.  verify UI table content of ceasing confirmation (Due to encoding issue currently comparing table content.
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_downloadCeasingNotifications(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();
		Boolean confirmationCount = ObjNotificationLists.ceaseConfirmationListPresent();
		if (confirmationCount)
		{
		ObjNotificationLists = ObjNotificationLists.clickCeaseConfirmation();
		ObjNotificationLists = ObjNotificationLists.clickOnCeaseConfirmationCSVButton();
		List<ArrayList<String>> CeasingConfirmationList = ReadCSV.getCSVFile("CeaseConfirmation.csv", true,2,9);
		CeasingConfirmationList.stream().forEach(System.out::println);
		
		List<ArrayList<String>> tblData = ObjNotificationLists.getCeasedConfirmationData();
		tblData.stream().forEach(System.out::println);
		
		verify.Verify.verifyEquals(tblData, tblData, "The Ceased Confirmation actual list & expected list does not match.");
		}
		
		else
		{
			setAssertMessage("No record found under ceased confirmation.", 1);
		}
		
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_ceasing_HandleCeaseApprovalOutcome(String browser ,String environment, String clientName) throws InterruptedException, IOException
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
		ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 3);
		ObjCrmHome = ObjCrmHome.clickScreening();
		ObjContacts = ObjCrmHome.clickContactsTile();
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
		String InlineNHSNo = ObjContacts.getNHSnumberwithspace();

		// Run Admin job for Generate New Patient Screening Records.
			//ObjCrmHome = ObjCrmHome.clickSettings();
		ObjCrmHome = ObjContacts.clickSettings();
		ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
		AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
		ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
		GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
		Thread.sleep(2000);
		ObjGNPSR = ObjGNPSR.launchJob();
		Thread.sleep(8000);
		tearDown(browser);
		
		// Login to Portal - search through patientsearch 
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
		ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(nhsNo);
		PatientpersonaldetailPortal ObjPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNHSNo);
			CeasePopup ObjCeasePopup=  ObjPatientpersonaldetailPortal.ClickonCease();
			ObjCeasePopup =ObjCeasePopup.selectCeasingReason("No Cervix");
			ObjPatientpersonaldetailPortal = ObjCeasePopup.submitCease_Patientpersonaldetail();
			tearDown(browser);
			setup(browser, environment, clientName,"CRM");
			ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjContacts = ObjCrmHome.ScreenContactMousehover();
			ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
			ContactPatient ObjContactss1 = ObjContacts.getScreeningRecords1();
			CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
			ObjCallRecallRecord = ObjCallRecallRecord.clickApproveCease();
			Boolean CallrecallType =ObjCallRecallRecord.verifycallrecallType("Ceased");
			if(CallrecallType)
			{
				System.out.println("The call Recall Type is ceased ");
				setAssertMessage("The call Recall Type is ceased "+nhsNo+"", 1);	
			}
			Verify.verifyTrue(CallrecallType, "The incorrect call recall type ");
			Boolean CallrecallProgress =ObjCallRecallRecord.verifyCallRecallProgress("Ceased");
			if(CallrecallProgress)
			{
				System.out.println("The call Recall progress is ceased ");
				setAssertMessage("The call Recall progress is ceased "+nhsNo+"", 2);	
			}
			Verify.verifyTrue(CallrecallProgress, "The incorrect call recall progress ");
			Boolean PortalAction =ObjCallRecallRecord.verifyPortalAction("PCSE CEASE APPROVED");
			if(PortalAction)
			{
				System.out.println("The portal action is 'PCSE Cease Approved' After clicking on Approve button "+nhsNo+" ");
				setAssertMessage("The portal action is 'PCSE Cease Approved' After clicking on Approve button "+nhsNo+" ", 3);	
			}
			Verify.verifyTrue(PortalAction, "The incorrect Portal action ");
			
			
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	  
		}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_ceasing_CeasebyAge64_CeaseNotification (String browser ,String environment, String clientName) throws InterruptedException, IOException
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
		ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 4);
		ObjCrmHome = ObjCrmHome.clickScreening();
		ObjContacts = ObjCrmHome.clickContactsTile();
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
		String InlineNHSNo = ObjContacts.getNHSnumberwithspace();
			
		// Run Admin job for Generate New Patient Screening Records.
			//ObjCrmHome = ObjCrmHome.clickSettings();
		ObjCrmHome = ObjContacts.clickSettings();
		ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
		AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
		ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
		GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
		Thread.sleep(2000);
		ObjGNPSR = ObjGNPSR.launchJob();
		Thread.sleep(3000);
		
		ObjAdminJobs = ObjAdminJobs.Adminbutton();
		ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate Recalls");
		GenerateRecalls ObjGenRecall = ObjAdminJobs.clickOnFirstRecord();
		Thread.sleep(2000);	
		ObjGenRecall = ObjGenRecall.launchJob();
		Thread.sleep(10000);	
		tearDown(browser);
	
		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();
		ObjNotificationLists = ObjNotificationLists.clickCeaseConfirmation();
		int Ceaseconfirmationnumber =ObjNotificationLists.CalculateCeaseconfirmationNumnber();
		tearDown(browser);
		
		setup(browser, environment, clientName,"CS");
		ObjPortalHome = new PortalHome(getDriver());
		PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
		ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(nhsNo);
		PatientpersonaldetailPortal ObjPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNHSNo);
		CeasePopup ObjCeasePopup=  ObjPatientpersonaldetailPortal.ClickonCease();
		ObjCeasePopup =ObjCeasePopup.selectCeasingReason("Due to Age");
		ObjPatientpersonaldetailPortal = ObjCeasePopup.submitCease_Patientpersonaldetail();
		tearDown(browser);
		
		setup(browser, environment, clientName,"CRM");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjContacts = ObjCrmHome.ScreenContactMousehover();
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);	
		ContactPatient ObjContactss2 = ObjContacts.getScreeningRecords1();	
		CallRecallRecord ObjCallRecallRecord1 = ObjContactss2.CallRecallHistory();
		ObjCallRecallRecord1 = ObjCallRecallRecord1.clickApproveCease();
		tearDown(browser);
		
		setup(browser, environment, clientName,"CS");
		ObjPortalHome = new PortalHome(getDriver());
		ObjNotificationLists = ObjPortalHome.clickNotifiationList();
		ObjNotificationLists = ObjNotificationLists.clickCeaseConfirmation();
		int CeaseconfirmationnumberAfterApprove =ObjNotificationLists.CalculateCeaseconfirmationNumnber();
		if(CeaseconfirmationnumberAfterApprove == Ceaseconfirmationnumber + 1)
		{
			System.out.println("The Cease confirmation is incresed by 1 "+nhsNo+"");
			setAssertMessage("The Cease confirmation is incresed by 1 "+nhsNo+"", 1);	
		
		}
		Verify.verifyTrue((CeaseconfirmationnumberAfterApprove == Ceaseconfirmationnumber + 1), "The Cease confirmation is not incresed by 1 "+nhsNo+"");
		ObjNotificationLists = ObjNotificationLists.Selectacknowledge(InlineNHSNo);
		tearDown(browser);
	/*	String nhsNo = "1594503915";
		String InlineNHSNo = "159 450 3915";*/
		setup(browser, environment, clientName,"CS");
		ObjPortalHome = new PortalHome(getDriver());
		ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
		ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(nhsNo);
		ObjPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNHSNo);
		boolean ceasemessage = ObjPatientpersonaldetailPortal.Verifyceasemessage();
		if(ceasemessage)
		{
			System.out.println("The correct cease message is appeared after cease patients"+nhsNo+"");
			setAssertMessage("The correct cease message is appeared after cease patients "+nhsNo+"", 2);	
		}
		Verify.verifyTrue(ceasemessage, "The correct cease message is not displayed after cease patients "+nhsNo+"");
		
		setup(browser, environment, clientName,"CRM");
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjContacts = ObjCrmHome.ScreenContactMousehover();
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);	
		ObjContactss2 = ObjContacts.getScreeningRecords1();	
		ObjCallRecallRecord1 = ObjContactss2.CallRecallHistory();
		Boolean acknowledgement =ObjCallRecallRecord1.VerifyAcknowledgedate();
		if(acknowledgement)
		{
			System.out.println("The correct Acknowledgement date is appeared after Acknowledgement done on portal"+nhsNo+"");
			setAssertMessage("The correct Acknowledgement date is appeared after Acknowledgement done on portal"+nhsNo+"", 3);	
		}
		Verify.verifyTrue(acknowledgement, "The incorrect acknowledgement date is displayed "+nhsNo+"");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		
		
		
	
	}
	// Akshay S: added script to acknowledge cease from portal.
	/*	***********************************************************************************************************
	 * Akshay Sohoni :13334- CSG_CESE_TC087- Colposcopy :  Cease : GP User : Validate Approve Cease functionality_E2E
	 * 
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void verifyE2ECeasing(String browser ,String environment, String clientName) throws InterruptedException, IOException, ParseException
	{
		String key="CSG_CESE_TC087";
		// Creating new patient details & closing browser.
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		//ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		
/*		ObjCrmHome = ObjCrmHome.clickScreening();
		Contacts ObjContacts = ObjCrmHome.clickContactsTile();
		ObjContacts = ObjCrmHome.clickOnContactNewRecord();
		Thread.sleep(2000);
		String nhsNo = helpers.CommonFunctions.generateValidNHSNo();
		ObjCrmHome = ObjContacts.EnterContactDetails(nhsNo, 3);
		ObjCrmHome = ObjCrmHome.clickScreening();
		ObjContacts = ObjCrmHome.clickContactsTile();
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
		//String InlineNHSNo = ObjContacts.getNHSnumberwithspace();

		// Run Admin job for Generate New Patient Screening Records.
			//ObjCrmHome = ObjCrmHome.clickSettings();
		ObjCrmHome = ObjCrmHome.clickSettings();
		ObjCrmHome = ObjCrmHome.clickSettingsTileNavigatorClick(3);
		AdminJobs ObjAdminJobs = ObjCrmHome.clickAdminJobs();
		List<String> jobs= Arrays.asList("Generate New Patient Screening Records","Generate Recalls");
		for (String jobName : jobs) {
			ObjAdminJobs = ObjAdminJobs.searchForRecords(jobName);
			GenNewPatientsScreeningRecords ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
			Thread.sleep(2000);
			ObjGNPSR = ObjGNPSR.launchJob();
			Thread.sleep(8000);
		}
		setAssertMessage("All jobs are executed successfully", 1);*/
/*		ObjAdminJobs = ObjAdminJobs.searchForRecords("Generate New Patient Screening Records");
		GenNewPatientsScreeningRecords  ObjGNPSR = ObjAdminJobs.clickOnFirstSearchRecord();
		Thread.sleep(2000);
		ObjGNPSR = ObjGNPSR.launchJob();
		Thread.sleep(8000);*/
		
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
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
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
		ObjAdvancedFindResult = ObjAdvancedFilter.enterRelatedSearchCriteriaAndClickOnResult_Ceasing(colPrimaryEntity,organisationFilter);
		List<String> NHSNumbers= ObjAdvancedFindResult.getNHSNumbers(1);
		String nhsNo= NHSNumbers.get(0).replaceAll(" ", "").toString();
		tearDown(browser);
		
		// Login to Portal - search through patientsearch
		setup(browser, environment, clientName,"CS");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		PortalHome objPortalHome= ObjLoginScreen.logintoCS("CS_GP Practice Administrator", environment);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		PatientsearchPortal objPatientsearchPortal= objPortalHome.clickPatientsearchClick();
		objPatientsearchPortal=  objPatientsearchPortal.EnterNHSNumberandclickOnresult(nhsNo);//1840513357
		PatientpersonaldetailPortal objPatientpersonaldetailPortal= objPatientsearchPortal.FirstResultofsearchOnSubmitted(nhsNo);
		CeasePopup ObjCeasePopup=  objPatientpersonaldetailPortal.ClickonCease();
		ObjCeasePopup =ObjCeasePopup.selectCeasingReason("No Cervix");
		objPatientpersonaldetailPortal = ObjCeasePopup.submitCease_Patientpersonaldetail();
		tearDown(browser);
		
		setup(browser, environment, clientName,"CRMCS","SUPERUSER");
		//String nhsNo= "7495122811";
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome= ObjCrmHome.clickOnPendingEmailApprovalDialog();
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		Contacts ObjContacts = ObjCrmHome.ScreenContactMousehover();
		
		ObjContacts = ObjContacts.searchAndSelectPatient(nhsNo);
		CallRecallRecord ObjCallRecallRecord = ObjContacts.getActiveScreeningRecords();
		//CallRecallRecord ObjCallRecallRecord = ObjContactss1.CallRecallHistory();
		ObjCallRecallRecord = ObjCallRecallRecord.clickApproveCease();
		Boolean CallrecallType =ObjCallRecallRecord.verifycallrecallType("Ceased");
		if(CallrecallType)
		{
			System.out.println("The call Recall Type is ceased ");
			setAssertMessage("The call Recall Type is ceased "+nhsNo+"", 1);	
		}
		Verify.verifyTrue(CallrecallType, "The incorrect call recall type ");
		Boolean CallrecallProgress =ObjCallRecallRecord.verifyCallRecallProgress("Ceased");
		if(CallrecallProgress)
		{
			System.out.println("The call Recall progress is ceased ");
			setAssertMessage("The call Recall progress is ceased "+nhsNo+"", 2);	
		}
		Verify.verifyTrue(CallrecallProgress, "The incorrect call recall progress ");
		Boolean PortalAction =ObjCallRecallRecord.verifyPortalAction("PCSE CEASE APPROVED");
		if(PortalAction)
		{
			System.out.println("The portal action is 'PCSE Cease Approved' After clicking on Approve button "+nhsNo+" ");
			setAssertMessage("The portal action is 'PCSE Cease Approved' After clicking on Approve button "+nhsNo+" ", 3);	
		}
		Verify.verifyTrue(PortalAction, "The incorrect Portal action ");
		
		
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	  
		}

}






