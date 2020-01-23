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
import pageobjects.EmailDescription;
import pageobjects.LoginScreen;
import pageobjects.Registration;
import pageobjects.PL.AdditionalInfo;
import pageobjects.PL.Appraisal;
import pageobjects.PL.Capacity;
import pageobjects.PL.CommunicationSkills;
import pageobjects.PL.CreateNewApp;
import pageobjects.PL.Declaration;
import pageobjects.PL.DeclarationBody;
import pageobjects.PL.Employment;
import pageobjects.PL.EnterAddressManually;
import pageobjects.PL.HealthClearance;
import pageobjects.PL.InsuranceDetails;
import pageobjects.PL.Nationality;
import pageobjects.PL.NewAppPersonalDetail;
import pageobjects.PL.NewApp_EmpHistory;
import pageobjects.PL.PerformerList;
import pageobjects.PL.PoliceCheck;
import pageobjects.PL.ProcessorPLHelpers;
import pageobjects.PL.ProfessionalDetails;
import pageobjects.PL.Qualification;
import pageobjects.PL.Referees;
import pageobjects.PL.SubmitApplication;
import pageobjects.PL.TeamPreference;
import pageobjects.PL.Trainee;
import pageobjects.PL.TrainingDetails;
import pageobjects.PL.Undertaking;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)
public class PL_CreateApp_DentalScripts extends BaseClass{
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"} ,priority = 1)
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_Dental(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		int count =Thread.activeCount();
		System.out.println("PL_CreateApp_DentalScripts count "+count);
		//LOGGER.info("START : CreateApplication_Dental : Execution");
		List<String> keys = Arrays.asList("PL_CA_134", "PL_CA_45");
		List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
	//	testCaseName(getITestContext());
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"DENTAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String FirstName = UD.get(2);
		String Surname = UD.get(3);
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		
		quit(browser);
		
	//Activate the registered account
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",1);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",1);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",1);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",1);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, user);
		
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			System.out.println("The user record found");
			EmailDescription ObjEmailDescription = ObjAdvancedFindResult.clickOnLinkFromRecord(0,1);
			ObjEmailDescription.getEmailActivationLink();
			
			quit(browser);
			
			setup(browser, environment, clientName, "ActivationLink");
			ObjEmailDescription  = new EmailDescription(getDriver());
			ObjEmailDescription.browseEmailActivationLink();
			
			quit(browser);
			
			//Integrated with Suraj's code for create app
			
			setup(browser, environment, clientName, "PL");

			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);

			
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			if(!DraftStatus)
			{
				//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
				Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
				
				if(disable && (!(disableWithdraw)))
				{
					helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
					ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
				}
				
				//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
				helpers.CommonFunctions.ClickOnButton("Create New Application", getDriver());
				
				NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				//ObjPerformerList.clickonOK();
			}
			
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			}			
				CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();		
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				setAssertMessage("The Personal detail is completed", 2);	
			}
			
				Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
				// Employment History 
				NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
				objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
				List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
				int Expecteddownload = Extensions.size();
				Boolean Fileuplaod = objNewApp_EmpHistory.verifyCount(Expecteddownload);
				
			if(Fileuplaod)
			{
				System.out.println("The Expected Document is loaded on Employment History " + Expecteddownload);				
			}
				
				Verify.verifyTrue(Fileuplaod, "The Expected Document is not loaded on Employment History ");
				objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
				Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
				
			if(TickMarkflagEmp)
			{
				System.out.println("The Employment History is completed ");
				setAssertMessage("The Employment History is completed", 3);	
			}
				
				Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
				// Referees			
				Referees objNewApp_Referees = objCreateNewApp.clickonRefrees();
				objNewApp_Referees =objNewApp_Referees.clickonRefreree("Referee 1");
				objNewApp_Referees = objNewApp_Referees.EnterReferee1();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress1();
				objNewApp_Referees = objEnterAddressManually.EnterFirstRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref1();			
				objNewApp_Referees =objNewApp_Referees.clickonSecondRefreree("Referee 2");
				objNewApp_Referees = objNewApp_Referees.EnterReferee2();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress2();
				objNewApp_Referees = objEnterAddressManually.EnterSecondRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref2();
				objNewApp_Referees = objNewApp_Referees.ClickOnSave_Submit();			
				Boolean TickMarkflagRef =objCreateNewApp.VerifyTickMarkPersonalDetail("Referees");
				
			if(TickMarkflagRef)
			{
				System.out.println("The Refereee  is completed ");
				setAssertMessage("The Refereee is completed", 4);	
			}
				Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		// Capacity tab	
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String ActualTableName = objCapacity.selectCapacity(2, GMC);
				
			if(evidence)
			{
				for(String key: keys)
                    {
						objCapacity.ScreenshotofCapacity(key+"Capacity_dental");
                    }
			}

				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 5);	
			}
				Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);
				
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 6);	
			}
				Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
			
		// Nationality 
				Nationality objNationality = objCreateNewApp.clickonNationalityTab();
				objNationality = objNationality.selectbirth();
				objNationality = objNationality.uploadpassport();
				objNationality = objNationality.uploadSignature();
				objNationality = objNationality.ClickOnupload();
				Boolean Fileuplaod1 = objNationality.verifyCountIdentification(Expecteddownload);
				
			if(Fileuplaod1)
			{
				System.out.println("The Expected Document is loaded Identification of Nationality " + Expecteddownload);				
			}
			
				Verify.verifyTrue(Fileuplaod1, "The Expected Document is not loaded Identification of Nationality");
				Boolean Fileuplaod2 = objNationality.verifyCountSignature(Expecteddownload);
				
			if(Fileuplaod2)
			{
				System.out.println("The Expected Document is loaded Signature of Nationality" + Expecteddownload);				
			}
			
				Verify.verifyTrue(Fileuplaod2, "The Expected Document is not loaded Signature of Nationality");
				objCreateNewApp = objNationality.ClickOnSave_Submit();			
				Boolean TickMarkflagNationality = objCreateNewApp.VerifyTickMarkPersonalDetail("Nationality");	
				
			if(TickMarkflagNationality)
			{
				System.out.println("The Nationality tab is completed ");
				setAssertMessage("The Nationality tab is completed", 7);	
			}
			
				Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
				
				// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());		
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement_No();
				objEmployment.AddAnotherLocaloffice(environment, "999");
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 8);	
			}
			
				Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 9);	
			}
			
				Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
				Qualification objQualification = objCreateNewApp.clickonQualification();
				String ActualTablename_Qualification = objQualification.Uploadfile();
				objQualification = objQualification.AddProfessionalQualification(1);
				objCreateNewApp = objQualification.ClickOnSave_Submit();	
				Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
				
			if(TickMarkflagQualification)
			{
				System.out.println("The Qualification tab is completed ");
				setAssertMessage("The Qualification tab is completed", 10);	
			}
				Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
			
		// Professional Details 
				ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
				String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails(environment);
				objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
				
			if(TickMarkflagProfessional)
			{
				System.out.println("The Professional tab is completed ");
				setAssertMessage("The Professional tab is completed", 11);	
			}
			
				Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
				Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(2); 
				objAppraisal = objAppraisal.upload();
				Boolean Fileuplaod4 = objAppraisal.verifyCount(Expecteddownload);
				
			if(Fileuplaod4)
			{
				System.out.println("The Expected Document is loaded on Appraisal" + Expecteddownload);
			}
			
				Verify.verifyTrue(Fileuplaod4, "The Expected Document is not loaded on Appraisal");
				objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				
			if(TickMarkflagAppraisal)
			{
				System.out.println("The Appraisal tab is completed ");
				setAssertMessage("The Appraisal tab is completed", 12);	
			}
			
				Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(2); 
				//objTrainingDetails.EnterTrainingDetails(2); 
				Boolean Fileuplaod5 = objTrainingDetails.verifyCount(Expecteddownload);
				
			if(Fileuplaod5)
			{
				System.out.println("The Expected Document is loaded on Training" + Expecteddownload);				
			}
			
				Verify.verifyTrue(Fileuplaod5, "The Expected Document is not loaded on Training");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				
			if(TickMarkflagTrainingDetails)
			{
				System.out.println("The Training Details tab is completed ");
				setAssertMessage("The Training Details tab is completed", 13);	
			}
			
				Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				Boolean Fileuplaod6 = objCommunicationSkills.verifyCount(Expecteddownload);
			
			if(Fileuplaod6)
			{
				System.out.println("The Expected Document is loaded on Training" + Expecteddownload);				
			}
			
				Verify.verifyTrue(Fileuplaod6, "The Expected Document is not loaded on Training");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				
			if(TickMarkflagCommunicationSkills)
			{
				System.out.println("The Communication Skills Details tab is completed ");
				setAssertMessage("The Communication Skills tab is completed", 14);	
			}
				
				Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				objAdditionalInfo = objAdditionalInfo.upload();
				Boolean Fileuplaod7 = objAdditionalInfo.verifyCount(Expecteddownload);
				
			if(Fileuplaod7)
			{
				System.out.println("The Expected Document is loaded on Training" + Expecteddownload);				
			}
			
				Verify.verifyTrue(Fileuplaod7, "The Expected Document is not loaded on Training");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				
			if(TickMarkflagAdditioanlInfo)
			{
				System.out.println("The Additional Info tab is completed ");
				setAssertMessage("The Additional Info tab is completed", 15);	
			}
			
			//   Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 		Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
		 		String ActualTablename_Declaration = objDeclaration.getactualtablename();
		 		objDeclaration = objDeclaration.Selectdeclaration(2); 
		 		Boolean Fileuplaod8 = objDeclaration.SelectdeclarationUploadcount(Expecteddownload);
		 		
			if(Fileuplaod8)
			{
				System.out.println("The Expected Document is loaded on Declaration for each declaration" + Expecteddownload);				
			}
			
		//	Verify.verifyTrue(Fileuplaod8, "The Expected Document is not loaded on Declaration for each declaration");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				
			if(TickMarkflagDecalration)
			{
				System.out.println("The Declaration tab is completed ");
				setAssertMessage("The Declaration tab is completed", 16);	
			}
			
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				
			if(TickMarkflagDecalrationBody)
			{
				System.out.println("The Declaration Body tab is completed ");
				setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			
				Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
				
	// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				
			if(TickMarkflagPolicecheck)
			{
				System.out.println("The PoliceCheck tab is completed ");
				setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			
				Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
				
	// Team Preference
				TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
				String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
				
			if(TickMarkflagTeamPreference)
			{
				System.out.println("The Team Preference is completed ");
				setAssertMessage("The Team Preference is completed", 19);	
			}
				Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
	// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(2);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				
			if(TickMarkflagHealthClerance)
			{
				System.out.println("The Health Clerance is completed ");
				setAssertMessage("The Health Clerance is completed", 20);	
			}
			
				Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				
			if(TickMarkflagUndertaking)
			{
				System.out.println("The Undertaking is completed ");
				setAssertMessage("The Undertaking is completed", 21);	
			}
				Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
				
	// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
				//objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
				String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
				Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
				if(TickMarkflagSubmitApp)
				{
				System.out.println("The Submit App is completed ");
				setAssertMessage("The Submit App is completed", 22);	
				}
				Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");
				Boolean ActualSubmittedmessage = objCreateNewApp.verifysubmittedmessage(ApplicationNumberOnparagraph);
				
			if(ActualSubmittedmessage)
			{
				System.out.println("The Submitted message is displayed as expected");
				setAssertMessage("The Submitted message is displayed as expected ", 23);
			}
			
			//	Verify.verifyTrue(ActualSubmittedmessage, "The Application message is different on Submitted tab. ");
				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			if(evidence)
			{
				for(String key: keys)
                    {
						ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
                    }
			}

				System.out.println("The Dental Application Number is " +Application_Number );
				setAssertMessage("The Dental Application Number is " +Application_Number, 23);	
				List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",FirstName,Surname,"SUBMITTED","DENTAL");
				ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
				Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"}  ,priority = 2 ) 
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void WithdrawApplication_Dental(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key="PL_CA_126";
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"DENTAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "UserEmail",3);
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
		tearDown(browser);
		setup(browser, environment, clientName, "ActivationLink");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "PL");			
		ProcessorPLHelpers.CreateAPP_DENTAL(getDriver(), environment, browser);
		tearDown(browser);
		
		setup(browser, environment, clientName, "PL");
		
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",1);
		System.out.println(ApplicationNumber);
		//dependsOnMethods={"CreateApplication"} )
	//	setup(browser, environment, clientName, "PL");
	/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Dental;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		ObjPerformerList = new PerformerList(getDriver());
		
		if(evidence)
		{
			ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
		}
		
			Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
			Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
			
		if(disable && (!(disableWithdraw)))
		{
			helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
			ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
		}
		
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Withdrawn");
			
		if(evidence)
		{
			ObjPerformerList.Screenshots(key+"Withdrawapplication");
		}
		
			System.out.println("The withdrawn Dental Application Number is " +Application_Number );
			setAssertMessage("The withdrawn Dental Application Number is " +Application_Number, 1);			
	}
		
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void VerifyErrorOnNationality_Dental(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{

		String key="PL_CA_19";
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		
		List<String> UD = objRegistration.CompleteRegistration(environment,"DENTAL");		
		String user = UD.get(0);
		String GMC = UD.get(1);
		
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		
		quit(browser);
		
		//Activate the registered account
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",1);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",1);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",1);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",1);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, user);		
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		
		if (flag)
		{
			System.out.println("The user record found");
			EmailDescription ObjEmailDescription = ObjAdvancedFindResult.clickOnLinkFromRecord(0,1);
			ObjEmailDescription.getEmailActivationLink();
			quit(browser);
			setup(browser, environment, clientName, "ActivationLink");
			ObjEmailDescription  = new EmailDescription(getDriver());
			ObjEmailDescription.browseEmailActivationLink();
			quit(browser);
			
			//Integrated with Suraj's code for create app
			
			setup(browser, environment, clientName, "PL");

			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			
		
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
			
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		if(!DraftStatus)
		{
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
			Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
			
			if(disable && (!(disableWithdraw)))
			{
					helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
					ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
			}
			
		//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
			helpers.CommonFunctions.ClickOnButton("Create New Application", getDriver());
			NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			// ObjPerformerList.clickonOK();
		}
		
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			}
			
				CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
		//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
			
			
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				setAssertMessage("The Personal detail is completed", 2);	
			}
			
				Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
				
		// Employment History 
				NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
				objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
				objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
				Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
				
			if(TickMarkflagEmp)
			{
				System.out.println("The Employment History is completed ");
				setAssertMessage("The Employment History is completed", 3);	
			}
				Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
				
		// Referees			
				Referees objNewApp_Referees = objCreateNewApp.clickonRefrees();
				objNewApp_Referees =objNewApp_Referees.clickonRefreree("Referee 1");
				objNewApp_Referees = objNewApp_Referees.EnterReferee1();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress1();
				objNewApp_Referees = objEnterAddressManually.EnterFirstRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref1();			
				objNewApp_Referees =objNewApp_Referees.clickonSecondRefreree("Referee 2");
				objNewApp_Referees = objNewApp_Referees.EnterReferee2();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress2();
				objNewApp_Referees = objEnterAddressManually.EnterSecondRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref2();
				objNewApp_Referees = objNewApp_Referees.ClickOnSave_Submit();			
				Boolean TickMarkflagRef =objCreateNewApp.VerifyTickMarkPersonalDetail("Referees");
				
			if(TickMarkflagRef)
			{
				System.out.println("The Refereee  is completed ");
				setAssertMessage("The Refereee is completed", 4);	
			}
			
				Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
	// Capacity tab	
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String ActualTableName = objCapacity.selectCapacity(2, GMC);
				
			if(evidence)
			{
				objCapacity.ScreenshotofCapacity(key+"Capacity_Dental");	
			}
			
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 5);	
			}
				Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);
				
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 6);	
			}
				Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
			
		// Nationality 
				Nationality objNationality = objCreateNewApp.clickonNationalityTab();
				objNationality = objNationality.deselectBirth();
				objCreateNewApp = objNationality.ClickOnSave_Submit();	
				
			if(evidence)
			{
				objNationality.Screenshots(key+"ErrormessageonNationality");	
			}
			
				List<String> AcutalErrormessageOnReferee1 =  objNationality.AcutalErrormessage();
				System.out.println(AcutalErrormessageOnReferee1);
			/*	List<String> FinalAcutalErrormessageOnReferee1 =  objNationality.AcutalErrormessageFirstRefereeaddress(AcutalErrormessageOnReferee1);
				System.out.println("This is included Address Error" +FinalAcutalErrormessageOnReferee1);*/
				List<String> FinalExpectedErrormessageOnReferee1 = objNationality.ExpectedErrorMessage("Expectederror_Nationality", 1);
				System.out.println(FinalExpectedErrormessageOnReferee1);
				int CountonOnReferee1 = ObjNewAppPersonalDetail.VerifyErrorMessage(FinalExpectedErrormessageOnReferee1,AcutalErrormessageOnReferee1);
				
			if (CountonOnReferee1 == 0)
			{
				System.out.println("The All error message is appear correctly if keep blank as nationality ");
				setAssertMessage("The All error message is appear correctly if keep blank as nationality" ,1);	
			}
			
				Verify.verifyTrue((CountonOnReferee1 == 0), "The All error message is not appear correctly if keep blank as nationality");
			
				Assert.assertFalse(Verify.verifyFailure(), "There incorrect error message is appear on page if keep blank as nationlity.");
		}
		else
		{
				Assert.assertEquals(flag, true, "No records found under results");
		}

	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_Dental_Foundationdentist(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{

		String key="PL_CA_135";
		List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		
		List<String> UD = objRegistration.CompleteRegistration(environment,"DENTAL");		
		String user = UD.get(0);
		String GMC = UD.get(1);
		String FirstName = UD.get(2);
		String Surname = UD.get(3);
		
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		
		quit(browser);
		
		//Activate the registered account
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",1);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",1);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",1);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",1);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, user);
		
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		
		if (flag)
		{
			System.out.println("The user record found");
			EmailDescription ObjEmailDescription = ObjAdvancedFindResult.clickOnLinkFromRecord(0,1);
			ObjEmailDescription.getEmailActivationLink();
			quit(browser);
			setup(browser, environment, clientName, "ActivationLink");
			ObjEmailDescription  = new EmailDescription(getDriver());
			ObjEmailDescription.browseEmailActivationLink();
			quit(browser);
			
			//Integrated with Suraj's code for create app
			
			setup(browser, environment, clientName, "PL");

			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			
		if(!DraftStatus)			
		{
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
			Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
			if(disable && (!(disableWithdraw)))
			{
				helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
				ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
			}
			
		//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
			helpers.CommonFunctions.ClickOnButton("Create New Application", getDriver());
			//ObjPerformerList.clickonOK();
			NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		}
		
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			}
			
				CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
				//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
			
			
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				setAssertMessage("The Personal detail is completed", 2);	
			}
			
				Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
				
		// Employment History 
				NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
				objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
				objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
				Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
				
			if(TickMarkflagEmp)
			{
				System.out.println("The Employment History is completed ");
				setAssertMessage("The Employment History is completed", 3);	
			}
			
				Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
		// Referees			
				Referees objNewApp_Referees = objCreateNewApp.clickonRefrees();
				objNewApp_Referees =objNewApp_Referees.clickonRefreree("Referee 1");
				objNewApp_Referees = objNewApp_Referees.EnterReferee1();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress1();
				objNewApp_Referees = objEnterAddressManually.EnterFirstRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref1();			
				objNewApp_Referees =objNewApp_Referees.clickonSecondRefreree("Referee 2");
				objNewApp_Referees = objNewApp_Referees.EnterReferee2();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress2();
				objNewApp_Referees = objEnterAddressManually.EnterSecondRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref2();
				objNewApp_Referees = objNewApp_Referees.ClickOnSave_Submit();			
				Boolean TickMarkflagRef =objCreateNewApp.VerifyTickMarkPersonalDetail("Referees");
				
			if(TickMarkflagRef)
			{
				System.out.println("The Refereee  is completed ");
				setAssertMessage("The Refereee is completed", 4);	
			}
			
				Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
				
		// Capacity tab	
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String ActualTableName = objCapacity.selectCapacity(8, GMC);
				
			if(evidence)
			{
				objCapacity.ScreenshotofCapacity(key+"CapacityDental_Foundationdentist");
				
			}
			
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 5);	
			}
			
				Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);
				
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 6);	
			}
				Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
			
		// Nationality 
				Nationality objNationality = objCreateNewApp.clickonNationalityTab();
				objNationality = objNationality.selectbirth();
				objNationality = objNationality.uploadpassport();
				objNationality = objNationality.uploadSignature();
				objNationality = objNationality.ClickOnupload();
				objCreateNewApp = objNationality.ClickOnSave_Submit();			
				Boolean TickMarkflagNationality = objCreateNewApp.VerifyTickMarkPersonalDetail("Nationality");	
				
			if(TickMarkflagNationality)
			{
				System.out.println("The Nationality tab is completed ");
				setAssertMessage("The Nationality tab is completed", 7);	
			}
			
				Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement_No();
				objEmployment.AddAnotherLocaloffice(environment , "999");
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 8);	
			}
			
				Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 9);	
			}			
				Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
				Qualification objQualification = objCreateNewApp.clickonQualification();
				String ActualTablename_Qualification = objQualification.Uploadfile();
				objQualification = objQualification.AddProfessionalQualification(1);
				objCreateNewApp = objQualification.ClickOnSave_Submit();	
				Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
				
			if(TickMarkflagQualification)
			{
				System.out.println("The Qualification tab is completed ");
				setAssertMessage("The Qualification tab is completed", 10);	
			}
				Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
			
		// Professional Details 
				ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
				String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails(environment);
				objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
			if(TickMarkflagProfessional)
			{
				System.out.println("The Professional tab is completed ");
				setAssertMessage("The Professional tab is completed", 11);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
				 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				 String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(2); 
				 objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				 Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				 
			if(TickMarkflagAppraisal)
			{
				System.out.println("The Appraisal tab is completed ");
				setAssertMessage("The Appraisal tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(2); 
				//objTrainingDetails.EnterTrainingDetails(2); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				
			if(TickMarkflagTrainingDetails)
			{
				System.out.println("The Training Details tab is completed ");
				setAssertMessage("The Training Details tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				
			if(TickMarkflagCommunicationSkills)
			{
				System.out.println("The Communication Skills Details tab is completed ");
				setAssertMessage("The Communication Skills tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				
			if(TickMarkflagAdditioanlInfo)
			{
				System.out.println("The Additional Info tab is completed ");
				setAssertMessage("The Additional Info tab is completed", 15);	
			}
			//   Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
			 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
				String ActualTablename_Declaration = objDeclaration.getactualtablename();
				objDeclaration = objDeclaration.Selectdeclaration(2); 			
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				
			if(TickMarkflagDecalration)
			{
				System.out.println("The Declaration tab is completed ");
				setAssertMessage("The Declaration tab is completed", 16);	
			}
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				
			if(TickMarkflagDecalrationBody)
			{
				System.out.println("The Declaration Body tab is completed ");
				setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
			
	// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				
			if(TickMarkflagPolicecheck)
			{
				System.out.println("The PoliceCheck tab is completed ");
				setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
			
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			
			if(TickMarkflagTeamPreference)
			{
				System.out.println("The Team Preference is completed ");
				setAssertMessage("The Team Preference is completed", 19);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
			
	// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(8);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				
			if(TickMarkflagHealthClerance)
			{
				System.out.println("The Health Clerance is completed ");
				setAssertMessage("The Health Clerance is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
			
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				
			if(TickMarkflagUndertaking)
			{
				System.out.println("The Undertaking is completed ");
				setAssertMessage("The Undertaking is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
			
	// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			//	objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
				/*Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
				if(TickMarkflagSubmitApp)
				{
				System.out.println("The Submit App is completed ");
				setAssertMessage("The Submit App is completed", 22);	
				}
				Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
				
				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
				
			if(evidence)
			{
	             ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
			}

			List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",FirstName,Surname,"SUBMITTED","DENTAL");
			ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
			System.out.println("The Dental Application Number is " +Application_Number );
			setAssertMessage("The Dental Application Number is " +Application_Number, 23);	
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}	
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_Dental_DentistTraining(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{

		String key="PL_CA_136";
		List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"DENTAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String FirstName = UD.get(2);
		String Surname = UD.get(3);
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		
		quit(browser);
		
		//Activate the registered account
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",1);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",1);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",1);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",1);
		//String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", keyname, "CLAIMID");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, user);
		
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			System.out.println("The user record found");
			EmailDescription ObjEmailDescription = ObjAdvancedFindResult.clickOnLinkFromRecord(0,1);
			ObjEmailDescription.getEmailActivationLink();
			quit(browser);
			setup(browser, environment, clientName, "ActivationLink");
			ObjEmailDescription  = new EmailDescription(getDriver());
			ObjEmailDescription.browseEmailActivationLink();
			quit(browser);
			
			//Integrated with Suraj's code for create app
			
			setup(browser, environment, clientName, "PL");
		//	String GMC = "12530";
			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			
			if(!DraftStatus)
			{
				//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
				Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
				
				if(disable && (!(disableWithdraw)))
				{
					helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
					ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
				}
				
				//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
				helpers.CommonFunctions.ClickOnButton("Create New Application", getDriver());
				NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			//	ObjPerformerList.clickonOK();
			}
			
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			}
			
				CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
			//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
				
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				setAssertMessage("The Personal detail is completed", 2);	
			}
			Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
			
		// Employment History 
			
				NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
				objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
				objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
				Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
				
			if(TickMarkflagEmp)
			{
				System.out.println("The Employment History is completed ");
				setAssertMessage("The Employment History is completed", 3);	
			}
			Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
			
		// Referees				
				Referees objNewApp_Referees = objCreateNewApp.clickonRefrees();
				objNewApp_Referees =objNewApp_Referees.clickonRefreree("Referee 1");
				objNewApp_Referees = objNewApp_Referees.EnterReferee1();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress1();
				objNewApp_Referees = objEnterAddressManually.EnterFirstRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref1();			
				objNewApp_Referees =objNewApp_Referees.clickonSecondRefreree("Referee 2");
				objNewApp_Referees = objNewApp_Referees.EnterReferee2();
				objEnterAddressManually = objNewApp_Referees.clickOnReferaddress2();
				objNewApp_Referees = objEnterAddressManually.EnterSecondRefAddressManually();
				objNewApp_Referees = objNewApp_Referees.clickonRadio_Agree_ref2();
				objNewApp_Referees = objNewApp_Referees.ClickOnSave_Submit();			
				Boolean TickMarkflagRef =objCreateNewApp.VerifyTickMarkPersonalDetail("Referees");
				
			if(TickMarkflagRef)
			{
				System.out.println("The Refereee  is completed ");
				setAssertMessage("The Refereee is completed", 4);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
			
		// Capacity tab	
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String ActualTableName = objCapacity.selectCapacity(9, GMC);
				
			if(evidence)
			{
				objCapacity.ScreenshotofCapacity(key+"CapacityDental_DentistTraining");
			}
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
				
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
			
		// Nationality 
				Nationality objNationality = objCreateNewApp.clickonNationalityTab();
				objNationality = objNationality.selectbirth();
				objNationality = objNationality.uploadpassport();
				objNationality = objNationality.uploadSignature();
				objNationality = objNationality.ClickOnupload();
				objCreateNewApp = objNationality.ClickOnSave_Submit();			
				Boolean TickMarkflagNationality = objCreateNewApp.VerifyTickMarkPersonalDetail("Nationality");
				
			if(TickMarkflagNationality)
			{
				System.out.println("The Nationality tab is completed ");
				setAssertMessage("The Nationality tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement();
				objEmployment =objEmployment.AddPractice(9);
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
				Qualification objQualification = objCreateNewApp.clickonQualification();
				String ActualTablename_Qualification = objQualification.Uploadfile();
				objQualification = objQualification.AddProfessionalQualification(1);
				objCreateNewApp = objQualification.ClickOnSave_Submit();	
				Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
				
			if(TickMarkflagQualification)
			{
				System.out.println("The Qualification tab is completed ");
				setAssertMessage("The Qualification tab is completed", 10);	
			}
			Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
			
		// Professional Details 
				ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
				String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails(environment);
				objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
				
			if(TickMarkflagProfessional)
			{
				System.out.println("The Professional tab is completed ");
				setAssertMessage("The Professional tab is completed", 11);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
				Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(2); 
				objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				
			if(TickMarkflagAppraisal)
			{
				System.out.println("The Appraisal tab is completed ");
				setAssertMessage("The Appraisal tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(2); 
				//objTrainingDetails.EnterTrainingDetails(2); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				
			if(TickMarkflagTrainingDetails)
			{
				System.out.println("The Training Details tab is completed ");
				setAssertMessage("The Training Details tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				
			if(TickMarkflagCommunicationSkills)
			{
				System.out.println("The Communication Skills Details tab is completed ");
				setAssertMessage("The Communication Skills tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				
			if(TickMarkflagAdditioanlInfo)
			{
				System.out.println("The Additional Info tab is completed ");
				setAssertMessage("The Additional Info tab is completed", 15);	
			}
			//   Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
			 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
				String ActualTablename_Declaration = objDeclaration.getactualtablename();
				objDeclaration = objDeclaration.Selectdeclaration(2); 			
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				
			if(TickMarkflagDecalration)
			{
				System.out.println("The Declaration tab is completed ");
				setAssertMessage("The Declaration tab is completed", 16);	
			}
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				
			if(TickMarkflagDecalrationBody)
			{
				System.out.println("The Declaration Body tab is completed ");
				setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
			
	// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				
			if(TickMarkflagPolicecheck)
			{
				System.out.println("The PoliceCheck tab is completed ");
				setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
			
	// Team Preference
				TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
				String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
				
			if(TickMarkflagTeamPreference)
			{
				System.out.println("The Team Preference is completed ");
				setAssertMessage("The Team Preference is completed", 19);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
			
	// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(9);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				
			if(TickMarkflagHealthClerance)
			{
				System.out.println("The Health Clerance is completed ");
				setAssertMessage("The Health Clerance is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
			
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				
			if(TickMarkflagUndertaking)
			{
				System.out.println("The Undertaking is completed ");
				setAssertMessage("The Undertaking is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
			
	// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			//	objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
				/*Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
				if(TickMarkflagSubmitApp)
				{
				System.out.println("The Submit App is completed ");
				setAssertMessage("The Submit App is completed", 22);	
				}
				Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
				
			if(evidence)
			{			
					ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");                
			}

				System.out.println("The Dental Application Number is " +Application_Number );
				setAssertMessage("The Dental Application Number is " +Application_Number, 23);	
				List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",FirstName,Surname,"SUBMITTED","DENTAL");
				ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
				Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}
}
