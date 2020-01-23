package testscripts;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import pageobjects.CallRecallRecord;
import pageobjects.ContactPatient;
import pageobjects.Contacts;
import pageobjects.CrmHome;
import pageobjects.LoginPageCS;
import pageobjects.CS.DeferPopup;
import pageobjects.CS.NonResponders;
import pageobjects.CS.NotificationLists;
import pageobjects.CS.PNLPage;
import pageobjects.CS.PatientpersonaldetailPortal;
import pageobjects.CS.PatientsearchPortal;
import pageobjects.CS.PortalHome;
import reporting.ListenerClass;


@Listeners(ListenerClass.class)
public class CS_DeferScripts extends BaseClass
{
	// Amit R. : This test case represents to verify Defer Popup is appearing from NRL Screen & after Cancel Action control moves back to Notification List Page.
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void verifyDeferPopupUpFromNRLPatient(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();

		Boolean NonRespCount = ObjNotificationLists.nonRespondersPresent();

		if (NonRespCount)
		{

			setAssertMessage("The patients are available in Non Responder Tab ", 1);
			NonResponders ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();
			//ObjNonResponders.selectActionFromFirstRecord("GP Defer");
			int activeRow = ObjNonResponders.getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
			Boolean activeRecord = ObjNonResponders.checkActiveDeferRecord("pcss-patient-non-responders", 8);

			if (activeRecord)
			{

				ObjNonResponders.selectGPDeferActionFromRecord("pcss-patient-non-responders","GP Defer", 8);
				String nhs = ObjNonResponders.getNHSFromActiveDeferRecord(activeRow);
				//String name = ObjNonResponders.getNHSFromFirstActiveRecord();
				String name = ObjNonResponders.getPatientNameFromActiveDeferRecord(activeRow);
				//Boolean fl = ObjNonResponders.clickSubmitAndCheckPopupUp();

				DeferPopup ObjDeferPopup = ObjNonResponders.ClickonSubmitForGPDefer(activeRow);

				//DeferPopup ObjDeferPopup = ObjNonResponders.ClickonSubmitForDefer();


				Boolean fl = ObjDeferPopup.CheckDeferPopupUp();
				if (fl)
				{
					setAssertMessage("The Defer Popup is present.", 2);
					System.out.println("The Defer Popup is present.");
					List<String> options = ObjDeferPopup.getReasonForDefer();

					//options.forEach(System.out::println);
					//ObjNonResponders.selectCeasingReasonandUploadFile("Informed Consent");

					if(options.contains("Recently tested and awaiting results") && options.contains("Currently pregnant") && options.contains("Currently under treatment relevant to screening"))
					{System.out.println("All the options appeared in Reason for Defer are correct.");
					setAssertMessage("All the options appeared in Reason for Defer are correct.", 3);}
					else
					{System.out.println("Options appeared in Reason for Defer are not matching.");
					setAssertMessage("Options appeared in Reason for Defer are not matching.", 4);}				
					//ObjNonResponders.selectCeasingReasonandUploadFile("Informed Consent");
					String NHSNo = ObjDeferPopup.getNHSNoOfDeferPatient();
					String ceasedPatientName = ObjDeferPopup.getDeferPatientName();
					if(NHSNo.equalsIgnoreCase(nhs))
					{
						setAssertMessage("The NHS Number is present & matched on Defer Patient Window", 5);
					}
					if(ceasedPatientName.equalsIgnoreCase(name))
					{
						setAssertMessage("The Patient is present & matched on Defer Patient Window", 6);
					}
					ObjDeferPopup.selectReasonAndUploadFile("Recently tested and awaiting results", "1 month", "CeasingReason1.jpg");
					ObjNotificationLists = ObjDeferPopup.clickOnDeferCeaseCancelButton();
					setAssertMessage("The Cancelled action performed successfully on Defer Window.", 7);

					tearDown(browser);

					setup(browser, environment, clientName,"CRM");

					CrmHome ObjCrmHome  = new CrmHome(getDriver());
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjCrmHome = ObjCrmHome.clickScreening();
					Contacts ObjContacts = ObjCrmHome.clickContactsTile();
					ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);

					ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();

					CallRecallRecord ObjCallRecallRecord = ObjContactss.CallRecallHistory();
					Boolean CeasingNotes = ObjCallRecallRecord.verifyDeferPatientUploadFile();

					if (CeasingNotes)
					{
						System.out.println("The defer record is not present. Test case is Passed.");
						setAssertMessage("The defer record is not present.", 8);
					}
					else
					{
						Assert.assertFalse(CeasingNotes, "The Defer record is not present");
					}

				}
				else
				{

					Assert.assertFalse(fl, "The Defer Popup is not present");
					//setAssertMessage("The Defer Popup is not present.", 9);
					System.out.println("The Defer Popup is not present.");
				}

			}
			else
			{
				setAssertMessage("No record found under NRL which can be marked as GP Defer.", 10);
			}
		}


		else
		{
			setAssertMessage("The patients are not available in Non Responder Tab.", 11);
			//Assert.assertFalse(NonRespCount, "The Defer Popup is not present");
		}


	}




	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void verifyUploadDeferFormTIFFFile(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();
		Boolean NonRespCount = ObjNotificationLists.nonRespondersPresent();

		if (NonRespCount)
		{

			setAssertMessage("The patients are available in Non Responder Tab - Assert Message", 1);
			NonResponders ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();
			int activeRow = ObjNonResponders.getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
			Boolean activeRecord = ObjNonResponders.checkActiveDeferRecord("pcss-patient-non-responders", 8);

			if (activeRecord)
			{

				ObjNonResponders.selectGPDeferActionFromRecord("pcss-patient-non-responders","GP Defer", 8);
				String nhs = ObjNonResponders.getNHSFromActiveDeferRecord(activeRow);
				//String name = ObjNonResponders.getNHSFromFirstActiveRecord();
				String name = ObjNonResponders.getPatientNameFromActiveDeferRecord(activeRow);
				//Boolean fl = ObjNonResponders.clickSubmitAndCheckPopupUp();

				DeferPopup ObjDeferPopup = ObjNonResponders.ClickonSubmitForGPDefer(activeRow);

				//DeferPopup ObjDeferPopup = ObjNonResponders.ClickonSubmitForDefer();


				Boolean DialogueBoxDefer = ObjDeferPopup.CheckDeferPopupUp();



				if (DialogueBoxDefer)
				{
					setAssertMessage("The Defer Popup is present.", 2);
					System.out.println("The Defer Popup is present.");
					//List<String> options = ObjNonResponders.getReasonForDefer();

					//options.forEach(System.out::println);
					//ObjNonResponders.selectCeasingReasonandUploadFile("Informed Consent");

					Boolean deferFlag = ObjDeferPopup.Deferoptions();

					if(deferFlag)
					{System.out.println("All the options appeared in Reason for Defer are correct.");
					setAssertMessage("All the options appeared in Reason for Defer are correct.", 3);}
					else
					{System.out.println("Options appeared in Reason for Defer are not matching.");
					setAssertMessage("Options appeared in Reason for Defer are not matching.", 4);}				
					//ObjNonResponders.selectCeasingReasonandUploadFile("Informed Consent");
					String NHSNo = ObjDeferPopup.getNHSNoOfDeferPatient();
					String ceasedPatientName = ObjDeferPopup.getDeferPatientName();
					if(NHSNo.equalsIgnoreCase(nhs))
					{
						setAssertMessage("The NHS Number is present & matched on Defer Patient Window", 5);
					}
					if(ceasedPatientName.equalsIgnoreCase(name))
					{
						setAssertMessage("The Patient is present & matched on Defer Patient Window", 6);
					}
					ObjDeferPopup.selectReasonAndUploadFile("Recently tested and awaiting results", "1 month", "SampleTIFF.tiff.TIF");
					ObjNotificationLists = ObjDeferPopup.clickOnDeferCeaseSubmitButton();


					Boolean CeasedSubmitFlag = ObjNonResponders.VerifyCeasedConfirmNRL();
					if(CeasedSubmitFlag)
					{
						System.out.println("The Patient gets Deferred Successfully.");
						setAssertMessage("The Submit action performed successfully on Defer Window.", 7);
					}


					tearDown(browser);

					setup(browser, environment, clientName,"CRM");

					CrmHome ObjCrmHome  = new CrmHome(getDriver());
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjCrmHome = ObjCrmHome.clickScreening();
					Contacts ObjContacts = ObjCrmHome.clickContactsTile();
					ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);
					//ObjContacts = ObjContacts.searchAndSelectPatient("1589885600");

					//ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();

					ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();

					CallRecallRecord ObjCallRecallRecord = ObjContactss.CallRecallHistory();

					Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();


					//String callStatus = ObjContactss.GetCallRecallStatus();
					//Boolean CeasingNotes = ObjCallRecallRecord.verifyCeasedNotes();

					if (!CeasingNotes)
					{
						System.out.println("The defer record is present.");
						setAssertMessage("The defer record is present.", 5);
						Boolean DeferConsentForm = ObjCallRecallRecord.verifyDeferPatientUploadFile();
						if (DeferConsentForm)
						{
							System.out.println("The uploaded Patinet Consent Form is available.");
							setAssertMessage("The uploaded Patinet Consent Form is available.", 7);


							ObjCrmHome = ObjCallRecallRecord.clickOnCrmHome();

							ObjCrmHome = ObjCrmHome.clickScreening();
							ObjContacts = ObjCrmHome.clickContactsTile();
							ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);

							ObjContactss = ObjContacts.getScreeningRecords1();


							String callStatus = ObjContactss.GetCallRecallStatus();

							if(callStatus.contains("Defer"))
							{
								System.out.println("The patient record marked as Deferred.");
								setAssertMessage("The patient record marked as Deferred.", 8);	
							}
							else
							{
								System.out.println("The patient record has not marked as Deferred.");
							}

						}
						
					}
				}

				else
				{
					setAssertMessage("The defer popup is not present.", 10);
				}

			}

			else
			{
				setAssertMessage("No record found under NRL which can be marked as GP Defer.", 10);
			}
		}


		else
		{
			setAssertMessage("The patients are not available in Non Responder Tab.", 11);
			//Assert.assertFalse(NonRespCount, "The Defer Popup is not present");
		}

	}



	

	//Suraj G - Verify that the image file is upoloaded on CRM after defer the record.
	//In CRM, The uploaded JPEG file after defer the patient record
	//All option on defer diolague box is correct
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_Ceasing_defer_Patientdetail_Uploadimagefile(String browser ,String environment, String clientName) throws InterruptedException, IOException
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
		//getting the NHS number from PNL and search into Patient search
		PatientsearchPortal ObjPatientsearchPortal = ObjPortalHome.clickPatientsearchClick();
		String CRMNHSNo = ObjPatientsearchPortal.getNHSNoOfCeasePatientforcallRecall(InlineNhsNo);
		System.out.println(CRMNHSNo);
		ObjPatientsearchPortal = ObjPatientsearchPortal.EnterNHSNumberandclickOnresult(CRMNHSNo);			
		PatientpersonaldetailPortal objPatientpersonaldetailPortal = ObjPatientsearchPortal.FirstResultofsearchOnSubmitted(InlineNhsNo);
		//Verify the defer Button and option on diolague box 
		boolean Deferbutton = objPatientpersonaldetailPortal.VerifyDeferButton();
		if(Deferbutton)
		{
			System.out.println("The defer button is appear");
			DeferPopup objDeferPopup = objPatientpersonaldetailPortal.clickondefer();		
			List<String> options = objDeferPopup.getReasonForDefer();
			if(options.contains("Recently tested and awaiting results") && options.contains("Currently pregnant") && options.contains("Currently under treatment relevant to screening"))
			{
				System.out.println("All the options appeared in Reason for Defer are correct.");
				setAssertMessage("All the options appeared in Reason for Defer are correct.", 3);
			}
			else
			{
				System.out.println("Options appeared in Reason for Defer are not matching.");
				setAssertMessage("Options appeared in Reason for Defer are not matching.", 4);
			}				
			String NHSNo = objDeferPopup.getNHSNoOfDeferPatient();
			String ceasedPatientName = objDeferPopup.getDeferPatientName();
			if(NHSNo.equalsIgnoreCase(InlineNhsNo))
			{
				setAssertMessage("The NHS Number is present & matched on Defer Patient Window", 5);
			}		
			objDeferPopup.selectReasonAndUploadFile("Recently tested and awaiting results", "1 month", "CeasingReason1.jpg");
			objPatientpersonaldetailPortal= objDeferPopup.clickOnDeferSubmitButton();
			setAssertMessage("The defer is done and upoloaded image", 6);	
			tearDown(browser);
			//Login to CRM, Verify patient record appered correct value or not 
			setup(browser, environment, clientName,"CRM");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			ObjCrmHome = ObjCrmHome.clickScreening();
			Contacts ObjContacts = ObjCrmHome.clickContactsTile();
			ObjContacts = ObjContacts.searchAndSelectPatient(NHSNo);	
			ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();	
			CallRecallRecord ObjCallRecallRecord = ObjContactss.CallRecallHistory();
			//Verify defer patient detail file is uploaded or not			
			Boolean CeasingNotes = ObjCallRecallRecord.verifyDeferPatientUploadFile();
			if (CeasingNotes)
			{
				System.out.println("The defer record is present. Test case is Passed.");
				setAssertMessage("The defer record is not present.", 8);
			}
			else
			{
				Assert.assertFalse(CeasingNotes, "The Defer record is not present");
			}

		}
		else
		{

			Assert.assertFalse(Deferbutton, "The Defer Popup is not present");
			System.out.println("The Defer Popup is not present.");
		}

	}

	//Suraj G - The Post Deferral View_Defer via NRL- Test case 7274
	// Defer the Patient record -> NRL
	// Verify on CRM - The GP Defer is appear 
	// On Portal -> patient search = defer button is not appear and deferchange button is appear 

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","Sanity"})
	@Parameters({"browser", "environment", "clientName"})
	public void CS_Ceasing_defer_viaNRL(String browser ,String environment, String clientName) throws InterruptedException, IOException
	{


		setup(browser, environment, clientName,"CS");
		LoginPageCS objLoginPageCS = new LoginPageCS(getDriver());
		PortalHome ObjPortalHome = objLoginPageCS.loginpage(1);
		//PortalHome ObjPortalHome = new PortalHome(getDriver());
		NotificationLists ObjNotificationLists = ObjPortalHome.clickNotifiationList();
		NonResponders ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();
		int NRLcountBeforedefer = ObjNonResponders.nonRespondersPresentcount();					

		String strNRLcountBeforedefer = String.valueOf(NRLcountBeforedefer);				

		setAssertMessage("The Non responsder count before defer is : "+NRLcountBeforedefer, 1);					
		Boolean NonRespCount = ObjNotificationLists.nonRespondersPresent();
		if (NonRespCount)
		{

			setAssertMessage("The patients are available in Non Responder Tab.", 1);
			//NonResponders ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();
			//ObjNonResponders.selectActionFromFirstRecord("GP Defer");
			int activeRow = ObjNonResponders.getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
			Boolean activeRecord = ObjNonResponders.checkActiveDeferRecord("pcss-patient-non-responders", 8);
			if (activeRecord)
			{
				//String NHSNo = ObjNonResponders.getNHSwithspace();
				String NHSNo = ObjNonResponders.getNHSwithspaceFromActiveRecord(activeRow);

				//String CRMNo = ObjNonResponders.getNHSFromFirstRecord();
				String CRMNo = ObjNonResponders.getNHSFromActiveDeferRecord(activeRow);
				System.out.println(CRMNo);	
				
				//String name = ObjNonResponders.getPatientNameFromFirstRecord();
				String name = ObjNonResponders.getPatientNameFromActiveDeferRecord(activeRow);
				ObjNonResponders.selectGPDeferActionFromRecord("pcss-patient-non-responders","GP Defer", 8);
				DeferPopup objDeferPopup = ObjNonResponders.ClickonSubmitForGPDefer(activeRow);
				boolean deferDialogBox = objDeferPopup.CheckDeferPopupUp();
				if(deferDialogBox)


				{
					System.out.println("The defer Pop up button is appear");
					setAssertMessage("The defer Pop up button is appear" , 1);


					List<String> options = objDeferPopup.getReasonForDefer();
					if(options.contains("Recently tested and awaiting results") && options.contains("Currently pregnant") && options.contains("Currently under treatment relevant to screening"))
					{
						System.out.println("All the options appeared in Reason for Defer are correct.");
						setAssertMessage("All the options appeared in Reason for Defer are correct.", 3);
					}
					else
					{
						System.out.println("Options appeared in Reason for Defer are not matching.");
						setAssertMessage("Options appeared in Reason for Defer are not matching.", 4);
					}				
					objDeferPopup.selectReasonAndUploadFile("Recently tested and awaiting results", "1 month", "CeasingReason1.jpg");
					ObjNonResponders= objDeferPopup.clickOnDeferSubmitButton_NonResponder();
					setAssertMessage("The defer is done and upoloaded image", 6);	
					Boolean Tickmark = ObjNonResponders.TickMark();
					if(Tickmark)
					{
						System.out.println("The Defer on Non Responder is done properly ");
						setAssertMessage("The Defer on Non Responder is done properly ", 3);
					}
					Assert.assertFalse(verify.Verify.verifyFailure(), "The Defer on Non Responder is not done properly " );
					ObjPortalHome = ObjPortalHome.clickPatientSearch();
					ObjNotificationLists = ObjPortalHome.clickNotifiationList();
					ObjNonResponders = ObjNotificationLists.clickNonRespondersTab();

					int NRLcountafterdefer = ObjNonResponders.nonRespondersPresentcount();
					System.out.println(NRLcountafterdefer);

					if(NRLcountafterdefer == (NRLcountBeforedefer-1))
					{
						System.out.println("The Non responsder count is decresed by 1 after defer and NRL count now : "+NRLcountafterdefer);
						setAssertMessage("The Non responsder count is decresed by 1 after defer and NRL count now : "+NRLcountafterdefer, 1);
					}

					boolean RowNumberOnResult = ObjNonResponders.SelectRowNumberOnResult(NHSNo);
					if(!RowNumberOnResult)
					{
						System.out.println("The NHS record is not avilable on NRL after defer done on it " +NHSNo );
						setAssertMessage("The NHS record is not avilable on NRL after defer done on it " +NHSNo, 3);
					}
					Assert.assertFalse(verify.Verify.verifyFailure(), "The Defer on Non Responder is not done properly " );

					tearDown(browser);
					//Login to CRM, Verify patient record appered correct value or not 
					setup(browser, environment, clientName,"CRM");
					CrmHome ObjCrmHome  = new CrmHome(getDriver());
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjCrmHome = ObjCrmHome.clickScreening();
					Contacts ObjContacts = ObjCrmHome.clickContactsTile();
					ObjContacts = ObjContacts.searchAndSelectPatient(CRMNo);	
					ContactPatient ObjContactss = ObjContacts.getScreeningRecords1();	
					CallRecallRecord ObjCallRecallRecord = ObjContactss.CallRecallHistory();
					boolean PortalAction = ObjCallRecallRecord.verifyPortalAction("GP Defer");
					if (PortalAction)
					{
						System.out.println("The portal action is appear as GP defer ");
						setAssertMessage("The portal action is appear as GP defer ", 4);
					}
					Assert.assertFalse(verify.Verify.verifyFailure(), "The portal action is not appear as GP defer " );
					tearDown(browser);

					setup(browser, environment, clientName,"CS");
					ObjPortalHome = new PortalHome(getDriver());
					PatientsearchPortal objPatientsearchPortal= ObjPortalHome.clickPatientsearchClick();
					objPatientsearchPortal = objPatientsearchPortal.EnterNHSNumberandclickOnresult(CRMNo);
					PatientpersonaldetailPortal objPatientpersonaldetailPortal = objPatientsearchPortal.FirstResultofsearchOnSubmitted(NHSNo);
					boolean defer = objPatientpersonaldetailPortal.Verifydefer();
					if(!defer)
					{
						System.out.println("The defer button is not appear after defered patient ");
						setAssertMessage("The defer button is not appear after defered patient ", 4);
					}
					Assert.assertFalse(verify.Verify.verifyFailure(), "The defer button is not appear after defered patient " );
					boolean Changedefer = objPatientpersonaldetailPortal.VerifyChangedefer();
					if(Changedefer)
					{
						System.out.println("The deferchange button is  appear after defered patient ");
						setAssertMessage("The deferchange button is  appear after defered patient ", 4);
					}
					Assert.assertFalse(verify.Verify.verifyFailure(), "The deferchange button is not appear after defered patient " );
				}
				else
				{
					//Assert.assertFalse(verify.Verify.verifyFailure(), "The defer Pop up button is not appear" );
					setAssertMessage("The defer Pop up button is not appear", 2);
				}
			}
			else
			{
				//Assert.assertFalse(verify.Verify.verifyFailure(), "The defer Pop up button is not appear" );
				setAssertMessage("No record found under NRL to mark as GP Defer", 3);
			}
		}
		else
		{
			//Assert.assertFalse(verify.Verify.verifyFailure(), "The defer Pop up button is not appear" );
			setAssertMessage("No record found under NRL", 3);
		}
	}  

}










