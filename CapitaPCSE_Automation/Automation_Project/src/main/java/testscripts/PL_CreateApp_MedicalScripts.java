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
import pageobjects.Contacts;
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
//@Listeners(CustomTestNGReport.class)

public class PL_CreateApp_MedicalScripts extends BaseClass
{
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE","SECURITY"}, priority=2 )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_MedicalList(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{
	
		List<String> keys = Arrays.asList("PL_CA_6", "PL_CA_17","PL_CA_38","PL_CA_90","PL_CA_129","PL_CA_4","PL_CA_11","PL_CA_34","PL_CA_116","PL_CA_122","PL_CA_139");
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		tearDown(browser);
		Thread.sleep(2000);
	//	quit(browser);
		
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
		//	String GMC = "65491";
			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
			if(!DraftStatus)		
			{
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
			}
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();		
				
			}
			CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
			NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();			
			
			ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
			EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
			List<String> ActualLabel_Address = objEnterAddressManually.getActualLabelList();
			System.out.println(ActualLabel_Address);
			List<String> ExpectedLabel_Address = objEnterAddressManually.getExpectedList("Label_Address",1);
			System.out.println(ExpectedLabel_Address);
			int count_Label = objEnterAddressManually.VerifyLabelonAddress(ExpectedLabel_Address,ActualLabel_Address);
			if(count_Label == 0)
			{
			System.out.println("The all label on address is matched with expected result");	
			setAssertMessage("The Personal detail is completed", 2);
			}
			
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				setAssertMessage("The Personal detail is completed", 3);	
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
			if(evidence)
			{
				for(String key: keys)
                    {
					objNewApp_EmpHistory.screenshots(key+"Upload_Document_Emp History");
                    }
			}
			objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
			Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
			if(TickMarkflagEmp)
			{
				System.out.println("The Employment History is completed ");
				setAssertMessage("The Employment History is completed", 4);	
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
				setAssertMessage("The Refereee is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Referee is not completed");
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		// Capacity tab
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String ActualTableName = objCapacity.selectCapacity(1,GMC );
			System.out.println(ActualTableName);
			if(evidence)
			{
				for(String key: keys)
                    {
						objCapacity.ScreenshotofCapacity(key+"Capacityselected");
                    }
			}
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
		//Insurance Tab 
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			Boolean FileuploadIns = objInsuranceDetails.verifyCount(Expecteddownload);
			if(FileuploadIns)
			{
				System.out.println("The Expected Document is loaded on Employment History " + Expecteddownload);
				
			}
			Verify.verifyTrue(FileuploadIns, "The Expected Document is loaded on Insurance");
			if(evidence)
			{
				for(String key: keys)
                    {
					objInsuranceDetails.Screenshot(key+"Capacityselected");
                    }
			}

			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
			
		// Nationality 
			Nationality objNationality = objCreateNewApp.clickonNationalityTab();
			objNationality = objNationality.selectbirth();
			objNationality = objNationality.uploadpassport();
			objNationality = objNationality.uploadSignature();
			objNationality = objNationality.ClickOnupload();
			if(evidence)
			{
				for(String key: keys)
                    {
					objNationality.Screenshots(key+"Upload_Document_Nationality");
                    }
			}
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
				setAssertMessage("The Nationality tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(1);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
		// Trainee
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 10);	
			}
			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
			Qualification objQualification = objCreateNewApp.clickonQualification();
			
			objQualification = objQualification.AddProfessionalQualification(1);
			String ActualTablename_Qualification = objQualification.Uploadfile();
			if(evidence)
			{
				for(String key: keys)
                    {
					objQualification.screenshot(key+"Upload_Document_ProfessionalQualification");
                    }
			}
			Boolean Fileuplaod3 = objQualification.verifyCount(Expecteddownload);
			if(Fileuplaod3)
			{
				System.out.println("The Expected Document is loaded Signature of Nationality" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod3, "The Expected Document is not loaded Signature of Nationality");
			objCreateNewApp = objQualification.ClickOnSave_Submit();	
			Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
			if(TickMarkflagQualification)
			{
			System.out.println("The Qualification tab is completed ");
			setAssertMessage("The Qualification tab is completed", 11);	
			}
			Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
			
		// Professional Details 
			ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
			String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails_WithAllOption(environment);
			objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
			if(TickMarkflagProfessional)
			{
			System.out.println("The Professional tab is completed ");
			setAssertMessage("The Professional tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
		 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
			String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
			objAppraisal = objAppraisal.upload();
			if(evidence)
			{
				for(String key: keys)
                    {
					objAppraisal.screenshot(key+"Upload_Document_Appraisal History");
                    }
			}
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
			setAssertMessage("The Appraisal tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			if(evidence)
			{
				for(String key: keys)
                    {
					objTrainingDetails.screenshot(key+"Upload_Document_Training Tab");
                    }
			}
			Boolean Fileuplaod5 = objTrainingDetails.verifyCount(Expecteddownload);
			if(Fileuplaod5)
			{
				System.out.println("The Expected Document is loaded on Training" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod5, "The Expected Document is not loaded on Training");
			objCreateNewApp = objTrainingDetails.ClickOnSave_Submit();
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			setAssertMessage("The Training Details tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes");
			Boolean Fileuplaod6 = objCommunicationSkills.verifyCount(Expecteddownload);
		
			if(Fileuplaod6)
			{
				System.out.println("The Expected Document is loaded on Communication" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod6, "The Expected Document is not loaded on Communication");
			if(evidence)
			{
				for(String key: keys)
                    {
					objCommunicationSkills.screenshot(key+"Upload_Document_Communication_Tab");
                    }
			}
			objCreateNewApp = objCommunicationSkills.ClickOnSave_Submit();
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			setAssertMessage("The Communication Skills tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			objAdditionalInfo = objAdditionalInfo.upload();
			if(evidence)
			{
				for(String key: keys)
                    {
					objAdditionalInfo.screenshot(key+"Upload_Document_AdditionalInfo_Tab");
                    }
			}
			Boolean Fileuplaod7 = objAdditionalInfo.verifyCount(Expecteddownload);
			if(Fileuplaod7)
			{
				System.out.println("The Expected Document is loaded on Additional Info" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod7, "The Expected Document is not loaded on Additional Info");
			objCreateNewApp = objAdditionalInfo.ClickOnSave_Submit();
			Thread.sleep(10000);
			Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
			if(TickMarkflagAdditioanlInfo)
			{
			System.out.println("The Additional Info tab is completed ");
			setAssertMessage("The Additional Info tab is completed", 15);	
			}
	//		//   Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
			String ActualTablename_Declaration = objDeclaration.getactualtablename();
			objDeclaration = objDeclaration.Selectdeclaration(2);
			if(evidence)
			{
				for(String key: keys)
                    {
					objDeclaration.screenshot(key+"Upload_Document_Declaration_Tab");
                    }
			}
			Boolean Fileuplaod8 = objDeclaration.SelectdeclarationUploadcount(Expecteddownload);
			if(Fileuplaod8)
			{
				System.out.println("The Expected Document is loaded on Declaration for each declaration" + Expecteddownload);
				
			}
		//	Verify.verifyTrue(Fileuplaod8, "The Expected Document is not loaded on Declaration for each declaration");
			
			objCreateNewApp = objDeclaration.ClickOnSave_Submit();
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
			objCreateNewApp = objDeclarationBody.ClickOnSave_Submit();
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
			boolean TextMessageonpage = objPoliceCheck.verifytextmessageonpage();
			if(TextMessageonpage)
			{
				System.out.println("The DBS certificate text and abroad text field value is appear correctly ");
				setAssertMessage("The DBS certificate text and abroad text field value is appear correctly", 18);	
			}
			objCreateNewApp = objPoliceCheck.ClickOnSave_Submit();
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
			setAssertMessage("The PoliceCheck tab is completed", 19);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			objCreateNewApp = objTeamPreference.ClickOnSave_Submit();
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
			setAssertMessage("The Team Preference is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
	// HealthClearance
			HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
			String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(1);
			objCreateNewApp = objHealthClearance.ClickOnSave_Submit();
			Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
			if(TickMarkflagHealthClerance)
			{
			System.out.println("The Health Clerance is completed ");
			setAssertMessage("The Health Clerance is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(getDriver());
			objCreateNewApp = objUndertaking.ClickOnSave_Submit();
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
			if(TickMarkflagUndertaking)
			{
			System.out.println("The Undertaking is completed ");
			setAssertMessage("The Undertaking is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
		//	objCreateNewApp = objSubmitApplication.ClickOnSave_Submit();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
			Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 23);	
			}
		//	Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			if(evidence)
			{
				for(String key: keys)
                    {
					objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
                    }
			}
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
			System.out.println("The Application Number is " +Application_Number );
			setAssertMessage("The Application Number is " +Application_Number, 24);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
				setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
			else
			{
				System.out.println("The Application Number is not same as on Sumitted action paragraph  " +Application_Number );
				setAssertMessage("The Application Number is  not same as on Sumitted action paragraph " +Application_Number, 23);		
				
			}
			
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}
		
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"}, priority=2)
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void VerifyErrorMessage_MedicalList(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException	
	{
		String key ="PL_CA_127";
		//setup(browser, environment, clientName, "PL");
	/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		

		setup(browser, environment, clientName, "PL");
		List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS" );
	//	List<String> colValues = new ArrayList<String>();

		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		
		// Amit : Enter the Registration details & get USERID for Applicant.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
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
				}
				if(DraftStatus)
				{
					ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			
				}
				CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
			//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();	
		
		
		ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.KeepBlankonallfield();
		EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
		ObjNewAppPersonalDetail = objEnterAddressManually.KeepBlankResAddress();
		objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
		ObjNewAppPersonalDetail = objEnterAddressManually.KeepblankGMCaddress();
		ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.clickonSave();
		
	//	helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
		List<String> AcutalErrormessage =  ObjNewAppPersonalDetail.AcutalErrormessage();
		System.out.println(AcutalErrormessage);
		List<String> FinalAcutalErrormessage =  ObjNewAppPersonalDetail.AcutalErrormessageOnRes(AcutalErrormessage);
		System.out.println("This is included Address Error" +FinalAcutalErrormessage);
		List<String> FinalExpectedErrormessage = ObjNewAppPersonalDetail.ExpectedErrorMessage("Expected Error",1);
		System.out.println(FinalExpectedErrormessage);
		if(evidence)
		{
			ObjNewAppPersonalDetail.Screenshots(key+"ErrormsgOnpersonaldetails");
		}
		int Count = ObjNewAppPersonalDetail.VerifyErrorMessage(FinalExpectedErrormessage,FinalAcutalErrormessage);
		if (Count == 0)
		{
			System.out.println("The All error message is appear correctly ");
			setAssertMessage("The All error message is appear correctly" ,1);	
		}
		Verify.verifyTrue((Count == 0), "The All error message is appear incorrectly");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"Regression","PCSE","PL","Sanity","CLONE"}) 
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void WithdrawApp_MedicalList(String browser ,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		//dependsOnMethods={"CreateApplication"} )
		List<String> keys = Arrays.asList("PL_CA_126", "PL_CA_121");
		setup(browser, environment, clientName, "PL");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit(getDriver(), environment, browser,"MEDICAL");
		tearDown(browser);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		String user = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "UserEmail",1);
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_Activate(getDriver(), environment, browser,user);
		tearDown(browser);
		setup(browser, environment, clientName, "ActivationLink");
		ProcessorPLHelpers.CreateApplication_MedicalList_Amit_ActivationLink(getDriver(), environment, browser);
		tearDown(browser);
		setup(browser, environment, clientName, "PL");			
		ProcessorPLHelpers.CreateAPP(getDriver(), environment, browser);
		tearDown(browser);
		//setup(browser, environment, clientName, "CRMPL","PLAPPLICANT");	
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber", 1);
		
	//	String ApplicationNumber = "PL101";
		
	/*	tearDown(browser);
		setup(browser, environment, clientName, "PL");
		PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		
		
/*		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
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
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/			
			
/*		boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
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
			}
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
		
				
			}
			CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
		//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
			NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
		
			
			ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail(performer_PerformerPortal);
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
				setAssertMessage("The Personal detail is completed", 3);	
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
			setAssertMessage("The Employment History is completed", 4);	
		}
		Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
		ObjPerformerList = objCreateNewApp.clickonlink("PCS Home");
		String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Draft");
		System.out.println(Application_Number);
		tearDown(browser);*/
		
		setup(browser, environment, clientName, "PL");
		
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
		//PerformerList ObjPerformerList = new PerformerList(getDriver());
		if(evidence)
		{
			for(String key: keys)
                {
				ObjPerformerList.screenshotAppStatus_portal(key+"SubmittedApplication");
                }
		}
		Boolean disable1 =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
		Boolean disableWithdraw1 =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
		if(disable1 && (!(disableWithdraw1)))
				{
					helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
					ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
				}
		String Application_Number1 = ObjPerformerList.getApplicationNumberwithstatus("Withdrawn");
		if(evidence)
		{
			for(String key: keys)
                {
				ObjPerformerList.screenshotAppStatus_portal(key+"WithdrawApplication");
                }
		}
		if(ApplicationNumber.equalsIgnoreCase(Application_Number1))
		{
		System.out.println("The withdrawn Application Number is " +Application_Number1 );
		setAssertMessage("The withdrawn Application Number is " +Application_Number1, 1);	
		}
		Verify.verifyNotEquals(ApplicationNumber.equalsIgnoreCase(Application_Number1), "The Withdrawn is not happen on "+Application_Number1);
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		
}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","CS","CLONE"}  ) 
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void EmpHistory_MedicalList(String browser ,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key="PL_CA_31";
	//	ExtentTestManager.getTest().setDescription("Invalid Login Scenario with wrong username and password.");
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
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
	/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		
/*		
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
		NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();*/
		
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
	//	objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
		Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
		if(TickMarkflagPersonal)
		{
			System.out.println("The Personal detail is completed ");
			setAssertMessage("The Personal detail is completed", 3);	
		}
		Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
	// Employment History 
		NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
	
		objNewApp_EmpHistory = objNewApp_EmpHistory.AddEmpHistoryRecord();
		
		int rowcount = objNewApp_EmpHistory.verifyrowisadded();
		System.out.println(rowcount);
		if(rowcount == 1)
		{
			System.out.println("The employment history record is added sucessfully");
			setAssertMessage("The employment history record is added sucessfully", 4);	
		}
		Verify.verifyTrue((rowcount == 1), "The emp record added sucessfully");
		boolean Addeddata = objNewApp_EmpHistory.verifydataadded();
		if(evidence)
		{
			objNewApp_EmpHistory.screenshots(key+"Emprecordadded");
		}
		if(Addeddata)
		{
			System.out.println("The correct employee data is added  ");
			setAssertMessage("The correct employee data is added ", 4);	
		}
		Verify.verifyTrue(Addeddata, "The incorrect data is added in emp history record");
		objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
		objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
		Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
		if(TickMarkflagEmp)
		{
			System.out.println("The Employment History is completed ");
			setAssertMessage("The Employment History is completed", 4);	
		}
		Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
		objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
	
		objNewApp_EmpHistory = objNewApp_EmpHistory.editAddemphistory();
		if(evidence)
		{
			objNewApp_EmpHistory.screenshots(key+"EditEmpdata");
		}
		boolean Updateddata = objNewApp_EmpHistory.verifydataupdatedadded();
		System.out.println(Updateddata);
		if(Updateddata)
		{
			System.out.println("The correct employee data is updated ");
			setAssertMessage("The correct employee data is updated ", 4);	
		}
		Verify.verifyTrue(Addeddata, "The incorrect data is updated in emp history record");
		Boolean TickMarkflagEmp1 =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
		if(TickMarkflagEmp1)
		{
			System.out.println("The Employment History is completed ");
			setAssertMessage("The Employment History is completed", 4);	
		}
		Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"}  ) 
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void AddQualification_MedicalList(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException{
	
		String key="PL_CA_74";
		setup(browser, environment, clientName, "PL");

		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue,user);
		
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			System.out.println("The user record found");
			EmailDescription ObjEmailDescription = ObjAdvancedFindResult.clickOnLinkFromRecord(0,1);
			ObjEmailDescription.getEmailActivationLink();
		Thread.sleep(2000);
			quit(browser);
			setup(browser, environment, clientName, "ActivationLink");
			ObjEmailDescription  = new EmailDescription(getDriver());
			ObjEmailDescription.browseEmailActivationLink();
			quit(browser);
			Thread.sleep(3000);
			//Integrated with Suraj's code for create app
			
			setup(browser, environment, clientName, "PL");

			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
	/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		
		/*
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
		Boolean PopupdiolgueBox =ObjPerformerList.VerifyPopUp();
		if(PopupdiolgueBox)
		{
			System.out.println("The Pop Up is displayed after clicking on Create application ");
			setAssertMessage("The Pop Up is displayed after clicking on Create application" , 1);
			int count = ObjPerformerList.VerifyPopupText();
			if(count == 0)
			{
				System.out.println("The Paragraph on Pop Up is displayed as correct ");
				setAssertMessage("The Paragraph on Pop Up is displayed as correct", 1);	
			}
			
			NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/			
			
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
			if(evidence)
			{
				objEnterAddressManually.screenshot(key+"Residentallabel");
			}
			List<String> ActualLabel_Address = objEnterAddressManually.getActualLabelList();
			System.out.println(ActualLabel_Address);
			List<String> ExpectedLabel_Address = objEnterAddressManually.getExpectedList("Label_Address",1);
			System.out.println(ExpectedLabel_Address);
			int count_Label = objEnterAddressManually.VerifyLabelonAddress(ExpectedLabel_Address,ActualLabel_Address);
			if(count_Label == 0)
			{
			System.out.println("The all label on address is matched with expected result");	
			setAssertMessage("The Personal detail is completed", 2);
			}
			
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				setAssertMessage("The Personal detail is completed", 3);	
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
				setAssertMessage("The Employment History is completed", 4);	
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
				setAssertMessage("The Refereee is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
	//	 CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		// Capacity tab
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String ActualTableName = objCapacity.selectCapacity(1, GMC);
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
		//Insurance Tab 
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 7);	
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
				setAssertMessage("The Nationality tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(1);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
			
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 10);	
			}
			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
			Qualification objQualification = objCreateNewApp.clickonQualification();
			int Existingqulaification = objQualification.countofexistingqulaification();			
			objQualification = objQualification.AddProfessionalQualification(1);
			int Existingqulaification_afterfirstadd = objQualification.countofexistingqulaification(); 
			if(evidence)
			{
				objQualification.screenshot(key+"AddedQualification");
			}
			if((Existingqulaification_afterfirstadd == Existingqulaification + 1))
			{
				System.out.println("The first qualification is added sucessfully");
				setAssertMessage("The first qualification is added sucessfully", 11);	
			}
			Verify.verifyTrue((Existingqulaification_afterfirstadd == Existingqulaification + 1), "The first qualification is not added sucessfully ");
			objQualification = objQualification.AddProfessionalQualification(2);
			int Existingqulaification_aftersecondadd = objQualification.countofexistingqulaification(); 
			if(evidence)
			{
				objQualification.screenshot(key+"AddedSecondQualification");
			}
			if((Existingqulaification_aftersecondadd == Existingqulaification_afterfirstadd + 1))
			{
				System.out.println("The second qualification is added sucessfully");
				setAssertMessage("The second qualification is added sucessfully", 11);	
			}
			Verify.verifyTrue((Existingqulaification_aftersecondadd == Existingqulaification_afterfirstadd + 1), "The second qualification is not added sucessfully ");
			boolean verifybutton_disable = objQualification.verifybutton_disabled();
			if(!(verifybutton_disable))
			{
				System.out.println("The Edit , Delete and Add qualification button is enabled after added record");
				setAssertMessage("The Edit , Delete and Add qualification button is enabled after added the record", 12);	
			}
			Verify.verifyTrue(verifybutton_disable, "The Edit , Delete and Add qualification button is disabled after added the record ");
			
			objQualification = objQualification.clickonAddprofessionalqualification();
			if(evidence)
			{
				objQualification.screenshot(key+"EditDeleteAddDisabled");
			}
			boolean verifybutton_disable1 = objQualification.verifybutton_disabled();
			if(verifybutton_disable1)
			{
				System.out.println("The Edit , Delete and Add qualification button is disbaled while we are adding the record");
				setAssertMessage("The Edit , Delete and Add qualification button is disbaled while we are adding the record", 12);	
			}
			Verify.verifyTrue(verifybutton_disable1, "The Edit , Delete and Add qualification button is enabled while we are adding the record ");
			objQualification = objQualification.clickonClosebutton();			
			String ActualTablename_Qualification = objQualification.Uploadfile();
			objCreateNewApp = objQualification.ClickOnSave_Submit();	
			Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
			if(TickMarkflagQualification)
			{
			System.out.println("The Qualification tab is completed ");
			setAssertMessage("The Qualification tab is completed", 11);	
			}
			Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
			
	}
	
//}
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void Personaldetails_PLCRM(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key="PL_CA_22";
		String Role = "PL Applicant Medical";
	/*	setup(browser, environment, clientName, "PL");
		String Role = "PL Applicant Medical";
		PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();
		

		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
		String Surname = UD.get(3);
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		
		quit(browser);*/
		
		setup(browser, environment, clientName, "PL");

		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
		String Surname = UD.get(3);
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		
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
		
		
/*		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
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
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/			
			
		
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
				setAssertMessage("The Personal detail is completed", 3);	
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
			setAssertMessage("The Employment History is completed", 4);	
		}
		Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
		ObjPerformerList = objCreateNewApp.clickonlink("PCS Home");
		String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Draft");
		if(evidence)
		{
			ObjPerformerList.screenshotAppStatus_portal(key+"DraftApplication");
		}
		System.out.println(Application_Number);
		tearDown(browser);
		//String performer_PerformerPortal = "AutomationPL Medical";
		//String Application_Number = "PL2107";
		
		setup(browser, environment, clientName, "CRMPL","PLPERFORMER");
		//String FullName = ExcelUtilities.getKeyValueByPosition("ConfigurationDetails.xlsx", "PLUSER", Role, "FullName");
		//Amit made changes
		//String Application_Number = "PL120";
/*		String user="MedicalUser183238@capitapcsetest.com";
		String FullName = "MedicalAutoFirst MedicalAutoSur";
		String Application_Number = "PL89";*/
		//String Role = "";
		String FullName = firstName+" "+Surname;
		ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		ObjCrmHome = ObjCrmHome.clickonsales_Contanct();
		Contacts objContacts = ObjCrmHome.searchandclick(user);
		String Telephone_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "CONTACT TELEPHONE NUMBER");
		Boolean CRMData = objContacts.verifyCRMdata(FullName, Role);
		if(evidence)
		{
			objContacts.screenshots(key+"PerformerdataOnCRM");
		}
		if(CRMData)
		{
			System.out.println("The Correct performer portal is displayed on CRM");
			setAssertMessage("The Correct performer portal is displayed on CRM", 4);	
		}
		Verify.verifyTrue(CRMData, "The incorrect performer portal displayed on CRM");
		
		ObjCrmHome = objContacts.clickonCRM();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
	/*	String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvancedFind", "selectoptGroupType",1);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvancedFind", "selectField",1);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvancedFind", "selectfilterCondition",1);
		*/
		ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, Application_Number);

		boolean fl = ObjAdvancedFindResult.resultRecordFound();
		if (fl)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				System.out.println(Application_Number);
				boolean ApplicationNumberOnCRM = ObjAdvancedFindResult.VerifyApplicationNumber(Application_Number);
				if(evidence)
				{
					ObjAdvancedFindResult.screenshots(key+"ApplicationnumberonCRM");
				}
				if(ApplicationNumberOnCRM)
				{
					System.out.println("The Correct Application Number is displayed on CRM");
					setAssertMessage("The Correct Application Number is displayed on CRM", 4);	
				}
				Verify.verifyTrue(ApplicationNumberOnCRM, "The incorrect Application Number is displayed on CRM");
			}
			
			
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
	else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
		
	}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void InvalidDOB_MedicalList(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		setup(browser, environment, clientName, "PL");
		String key ="PL_CA_17";
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
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
	/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		
	/*	
		//CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
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
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/		
		
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
			}
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
		
				
			}
			CreateNewApp ObjCreateNewApp = new CreateNewApp(getDriver());
		//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
			NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
		
			String Invalid_DOB = ObjNewAppPersonalDetail.EnterPatientDetail_InvalidDOB(2);
			EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", getDriver());
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			if(evidence)
			{
				ObjNewAppPersonalDetail.Screenshots(key+"InvalidDOBerror");
			}
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(!TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is not completed because of invalid DOB");
				setAssertMessage("The Personal detail is not completed because of invalid DOB", 1);	
			}
			Verify.verifyFalse(TickMarkflagPersonal, "The Personal detail is completed because of invalid DOB");
			List<String> AcutalErrormessage =  ObjNewAppPersonalDetail.AcutalErrormessage();
			System.out.println("Actual Value " +AcutalErrormessage);
			String ExpectedErrormessage = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "PersonalDetail", "InvalidDOBError",1);	
			System.out.println(ExpectedErrormessage);
		/*	String FinalExpected_InvalidError = ObjNewAppPersonalDetail.addDOBon_ExpectedError(ExpectedErrormessage, Invalid_DOB);
			System.out.println("Expectecd value "+FinalExpected_InvalidError);*/
			String ActualInvaliderro2 = AcutalErrormessage.toString();
			String ActualInvaliderro1 = ActualInvaliderro2.replace("[", "");
			String ActualInvaliderro = ActualInvaliderro1.replace("]", "");
			System.out.println("Actual Value " +ActualInvaliderro);
			if(ActualInvaliderro.equalsIgnoreCase(ExpectedErrormessage))
			{
				System.out.println("The correct invalid DOB error is appear if we entered invalid DOB");
				setAssertMessage("The correct invalid DOB error is appear if we entered invalid DOB", 2);	
			}
			Verify.verifyTrue(ActualInvaliderro.equalsIgnoreCase(ExpectedErrormessage), "The incorrect invalid DOB error message is displayed ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
			
		
	}

	// Suraj G = We create a medical application for GP Contractor on PL 
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_Medical_GPContractor(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		List<String> keys = Arrays.asList("PL_CA_63", "PL_CA_130");
		setup(browser, environment, clientName, "PL");
		List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
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
	/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		
		
/*		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
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
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/			
			
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
				setAssertMessage("The Personal detail is completed", 3);	
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
				setAssertMessage("The Employment History is completed", 4);	
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
				setAssertMessage("The Refereee is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		// Capacity tab
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String ActualTableName = objCapacity.selectCapacity(4, GMC);
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
		//Insurance Tab 
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 7);	
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
				setAssertMessage("The Nationality tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(4);
		/*	boolean noneditable = objEmployment.VerifyNonEditablefield();
			if(noneditable)
			{
				System.out.println("The Pratice name and Level is non editable field ");
				setAssertMessage("The Pratice name and Level is non editable field ", 9);	
			}
			Verify.verifyTrue(noneditable, "The Pratice name and Level is editable field  ");
			Boolean Edit_deletebutton = objEmployment.verifybuttononResult("Edit","Delete");
			{
				System.out.println("The Edit and delete button is displayed infront of result ");
				setAssertMessage("The Edit and delete button is displayed infront of result ", 10);
			}
			Verify.verifyTrue(noneditable, "The Edit and delete button is not displayed infront of result");
			Boolean disable_addanother =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Add Another Practice");
			if(!disable_addanother)
			{
				System.out.println("The ADD ANOTHER PRATICE button is enabled after added emp data ");
				setAssertMessage("The ADD ANOTHER PRATICE button is enabled after added emp data  ", 10);
			}
			Verify.verifyTrue(!disable_addanother, "The ADD ANOTHER PRATICE button is disabled after added emp data ");*/
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
		// Trainee
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 10);	
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
			setAssertMessage("The Qualification tab is completed", 11);	
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
			setAssertMessage("The Professional tab is completed", 12);	
			}
		Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
		 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
			String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
			objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
			Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagAppraisal)
			{
			System.out.println("The Appraisal tab is completed ");
			setAssertMessage("The Appraisal tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			setAssertMessage("The Training Details tab is completed", 14);	
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
			setAssertMessage("The Communication Skills tab is completed", 15);	
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
			boolean TextMessageonpage = objPoliceCheck.verifytextmessageonpage();
			if(TextMessageonpage)
			{
				System.out.println("The DBS certificate text and abroad text field value is appear correctly ");
				setAssertMessage("The DBS certificate text and abroad text field value is appear correctly", 18);	
			}
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
			setAssertMessage("The PoliceCheck tab is completed", 19);	
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
			setAssertMessage("The Team Preference is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
	// HealthClearance
			HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
			String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(4);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
			if(TickMarkflagHealthClerance)
			{
			System.out.println("The Health Clerance is completed ");
			setAssertMessage("The Health Clerance is completed", 21);	
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
			setAssertMessage("The Undertaking is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
			
		/*	Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 23);	
			}
			Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			if(evidence)
			{
				for(String key: keys)
                    {
					objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
                    }
			}

			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			if(evidence)
			{
				for(String key: keys)
                    {
					ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
                    }
			}

			System.out.println("The Application Number is " +Application_Number );
			setAssertMessage("The Application Number is " +Application_Number, 24);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",firstName,Surname,"SUBMITTED","MEDICAL");
				ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
				setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
		//	Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	
	}
	
	// Suraj G = We create a medical application for GP registrar on PL 
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"} )
	@Parameters({"browser", "environment", "clientName" , "evidence"})
	public void CreateApplication_Medical_GPRegistrar(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key ="PL_CA_131";
		setup(browser, environment, clientName, "PL");
		List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
				setAssertMessage("The Personal detail is completed", 3);	
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
				setAssertMessage("The Employment History is completed", 4);	
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
				setAssertMessage("The Refereee is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		// Capacity tab
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String ActualTableName = objCapacity.selectCapacity(5, GMC);
			if(evidence)
			{
				objCapacity.ScreenshotofCapacity(key+"Medical_GPRegistrar");
			}
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
		//Insurance Tab 
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 7);	
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
				setAssertMessage("The Nationality tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(5);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
		// Trainee
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 10);	
			}
			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
			Qualification objQualification = objCreateNewApp.clickonQualification();			
			objQualification = objQualification.AddProfessionalQualification(1);
			String ActualTablename_Qualification = objQualification.Uploadfile();
			objCreateNewApp = objQualification.ClickOnSave_Submit();	
			Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
			if(TickMarkflagQualification)
			{
			System.out.println("The Qualification tab is completed ");
			setAssertMessage("The Qualification tab is completed", 11);	
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
			setAssertMessage("The Professional tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
			Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
			String ActualTablename_Appraisal = objAppraisal.AddNewAppraisal(1);
			objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
			Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagAppraisal)
			{
			System.out.println("The Appraisal tab is completed ");
			setAssertMessage("The Appraisal tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			setAssertMessage("The Training Details tab is completed", 14);	
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
			setAssertMessage("The Communication Skills tab is completed", 15);	
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
			boolean TextMessageonpage = objPoliceCheck.verifytextmessageonpage();
			if(TextMessageonpage)
			{
				System.out.println("The DBS certificate text and abroad text field value is appear correctly ");
				setAssertMessage("The DBS certificate text and abroad text field value is appear correctly", 18);	
			}
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
			setAssertMessage("The PoliceCheck tab is completed", 19);	
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
			setAssertMessage("The Team Preference is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
	// HealthClearance
			HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
			String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(5);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
			Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
			if(TickMarkflagHealthClerance)
			{
			System.out.println("The Health Clerance is completed ");
			setAssertMessage("The Health Clerance is completed", 21);	
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
			setAssertMessage("The Undertaking is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
		/*	Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 23);	
			}
			Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			if(evidence)
			{
				
					objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
                   
			}

			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			if(evidence)
			{
			
					ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
                    
			}

			System.out.println("The Application Number is " +Application_Number );
			setAssertMessage("The Application Number is " +Application_Number, 24);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
				setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);
				List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",FirstName,Surname,"SUBMITTED","MEDICAL");
				ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
			}
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
			
	}
	// Suraj G = We create a medical application for Armed Services Type1 on PL 
		@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"} )
		@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_Medical_ArmedServicesType1(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
		{
			String key="PL_CA_132";
			List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
			setup(browser, environment, clientName, "PL");
			LoginScreen objLoginScreen = new LoginScreen(getDriver());
			
			// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
			Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
			List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
			//	String GMC = "12540";
				objLoginScreen = new LoginScreen(getDriver());
				PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
		
		/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
			String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
			ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
			PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
			
			
		/*	// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
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
			//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
			//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/				
				
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
					setAssertMessage("The Personal detail is completed", 3);	
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
					setAssertMessage("The Employment History is completed", 4);	
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
					setAssertMessage("The Refereee is completed", 5);	
				}
				Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
			// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			// Capacity tab
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String ActualTableName = objCapacity.selectCapacity(6, GMC);
				System.out.println(ActualTableName);
				if(evidence)
				{
					objCapacity.ScreenshotofCapacity(key+"Capacity_Medical_ArmedServicesType1");
				}
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				if(TickMarkflagCap)
				{
					System.out.println("The Capacity  is completed ");
					setAssertMessage("The Capacity is completed", 6);	
				}
				Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			//Insurance Tab 
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
				if(TickMarkflagIns)
				{
					System.out.println("The Insurance tab is completed ");
					setAssertMessage("The Insurance tab is completed", 7);	
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
					setAssertMessage("The Nationality tab is completed", 8);	
				}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
				
			//Proposed Employment 
			// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement();
				objEmployment =objEmployment.AddPractice(6);
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				if(TickMarkflagEmployment)
				{
					System.out.println("The Employment tab is completed ");
					setAssertMessage("The Employment tab is completed", 9);	
				}
				Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
				
			// Trainee
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				if(TickMarkflagTrainee)
				{
					System.out.println("The Trainee tab is completed ");
					setAssertMessage("The Trainee tab is completed", 10);	
				}
				Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
			
			// Professional Qualification 	
				Qualification objQualification = objCreateNewApp.clickonQualification();			
				objQualification = objQualification.AddProfessionalQualification(1);
				String ActualTablename_Qualification = objQualification.Uploadfile();
				objCreateNewApp = objQualification.ClickOnSave_Submit();	
				Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
				if(TickMarkflagQualification)
				{
				System.out.println("The Qualification tab is completed ");
				setAssertMessage("The Qualification tab is completed", 11);	
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
				setAssertMessage("The Professional tab is completed", 12);	
				}
				Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
				
		// Appraisal History 
			 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
				objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagAppraisal)
				{
				System.out.println("The Appraisal tab is completed ");
				setAssertMessage("The Appraisal tab is completed", 13);	
				}
				Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
				
		// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagTrainingDetails)
				{
				System.out.println("The Training Details tab is completed ");
				setAssertMessage("The Training Details tab is completed", 14);	
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
				setAssertMessage("The Communication Skills tab is completed", 15);	
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
				boolean TextMessageonpage = objPoliceCheck.verifytextmessageonpage();
				if(TextMessageonpage)
				{
					System.out.println("The DBS certificate text and abroad text field value is appear correctly ");
					setAssertMessage("The DBS certificate text and abroad text field value is appear correctly", 18);	
				}
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				if(TickMarkflagPolicecheck)
				{
				System.out.println("The PoliceCheck tab is completed ");
				setAssertMessage("The PoliceCheck tab is completed", 19);	
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
				setAssertMessage("The Team Preference is completed", 20);	
				}
				Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
		// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(6);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				if(TickMarkflagHealthClerance)
				{
				System.out.println("The Health Clerance is completed ");
				setAssertMessage("The Health Clerance is completed", 21);	
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
				setAssertMessage("The Undertaking is completed", 22);	
				}
				Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
		// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
				/*Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
				if(TickMarkflagSubmitApp)
				{
				System.out.println("The Submit App is completed ");
				setAssertMessage("The Submit App is completed", 23);	
				}*/
		//		Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");
				String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
				if(evidence)
				{
					
						objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
	                  
				}

				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");

				if(evidence)
				{
				
					ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
                   
				}

				System.out.println("The Application Number is " +Application_Number );
				setAssertMessage("The Application Number is " +Application_Number, 24);	
				if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
				{
					List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",FirstName,Surname,"SUBMITTED","MEDICAL");
					ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
					System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
					setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
				}
				Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
				Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
			}
			else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}
		
		}
	// Suraj G = We create a medical application for Armed Services Type2  on PL 
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_Medical_ArmedServicesType2(String browser ,String environment, String clientName, boolean evidence) throws InterruptedException, IOException, AWTException
				{
					String key ="PL_CA_133";
					List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
					setup(browser, environment, clientName, "PL");
					LoginScreen objLoginScreen = new LoginScreen(getDriver());
					
					// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
					Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
					List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
				/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
					String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
					ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
					PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
					
	/*				
					// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
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
					//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
					//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/						
						
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
							setAssertMessage("The Personal detail is completed", 3);	
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
							setAssertMessage("The Employment History is completed", 4);	
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
							setAssertMessage("The Refereee is completed", 5);	
						}
						Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
					// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
					// Capacity tab
						Capacity objCapacity = objCreateNewApp.clickonCapacity();
						String ActualTableName = objCapacity.selectCapacity(7, GMC);
						System.out.println(ActualTableName);
						if(evidence)
						{
							
							objCapacity.ScreenshotofCapacity(key+"Medical_ArmedServicesType2");
			                
						}
						objCreateNewApp = objCapacity.ClickOnSave_Submit();	
						Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
						if(TickMarkflagCap)
						{
							System.out.println("The Capacity  is completed ");
							setAssertMessage("The Capacity is completed", 6);	
						}
						Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
					//Insurance Tab 
						InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
						String ActualTableName_Ins = objInsuranceDetails.Selectcover();
						objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
						Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
						if(TickMarkflagIns)
						{
							System.out.println("The Insurance tab is completed ");
							setAssertMessage("The Insurance tab is completed", 7);	
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
							setAssertMessage("The Nationality tab is completed", 8);	
						}
						Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
						
					//Proposed Employment 
					// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
					
						Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
						String ActualTablename_Emp = objEmployment.selectstatement();
						objEmployment =objEmployment.AddPractice(7);
						objCreateNewApp = objEmployment.ClickOnSave_Submit();	
						Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
						if(TickMarkflagEmployment)
						{
							System.out.println("The Employment tab is completed ");
							setAssertMessage("The Employment tab is completed", 9);	
						}
						Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
						
					// Trainee
						Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
						String ActualTablename_Trainee = objTrainee.selectTrainee();
						objCreateNewApp = objTrainee.ClickOnSave_Submit();	
						Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
						if(TickMarkflagTrainee)
						{
							System.out.println("The Trainee tab is completed ");
							setAssertMessage("The Trainee tab is completed", 10);	
						}
						Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
					
					// Professional Qualification 	
						Qualification objQualification = objCreateNewApp.clickonQualification();			
						objQualification = objQualification.AddProfessionalQualification(1);
						String ActualTablename_Qualification = objQualification.Uploadfile();
						objCreateNewApp = objQualification.ClickOnSave_Submit();	
						Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
						if(TickMarkflagQualification)
						{
						System.out.println("The Qualification tab is completed ");
						setAssertMessage("The Qualification tab is completed", 11);	
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
						setAssertMessage("The Professional tab is completed", 12);	
						}
						Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
						
				// Appraisal History 
					 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
						String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
						objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
						Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
						if(TickMarkflagAppraisal)
						{
						System.out.println("The Appraisal tab is completed ");
						setAssertMessage("The Appraisal tab is completed", 13);	
						}
						Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
						
				// Training Details
						TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
						String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
						helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
						Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
						if(TickMarkflagTrainingDetails)
						{
						System.out.println("The Training Details tab is completed ");
						setAssertMessage("The Training Details tab is completed", 14);	
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
						setAssertMessage("The Communication Skills tab is completed", 15);	
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
						boolean TextMessageonpage = objPoliceCheck.verifytextmessageonpage();
						if(TextMessageonpage)
						{
							System.out.println("The DBS certificate text and abroad text field value is appear correctly ");
							setAssertMessage("The DBS certificate text and abroad text field value is appear correctly", 18);	
						}
						helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
						Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
						if(TickMarkflagPolicecheck)
						{
						System.out.println("The PoliceCheck tab is completed ");
						setAssertMessage("The PoliceCheck tab is completed", 19);	
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
						setAssertMessage("The Team Preference is completed", 20);	
						}
						Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
				// HealthClearance
						HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
						String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(7);
						helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , getDriver());
						Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
						if(TickMarkflagHealthClerance)
						{
						System.out.println("The Health Clerance is completed ");
						setAssertMessage("The Health Clerance is completed", 21);	
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
						setAssertMessage("The Undertaking is completed", 22);	
						}
						Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
				// Submit Application 
						SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
						String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
						helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
						helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
						Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
					/*	if(TickMarkflagSubmitApp)
						{
						System.out.println("The Submit App is completed ");
						setAssertMessage("The Submit App is completed", 23);	
						}*/
		//				Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");
						String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
						if(evidence)
						{
							
								objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
			                
						}

						ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
						String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
						if(evidence)
						{
						
								ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
			                    
						}

						System.out.println("The Application Number is " +Application_Number );
						setAssertMessage("The Application Number is " +Application_Number, 24);	
						if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
						{
							List<String> colValues = Arrays.asList(Application_Number, user, "M8$tek12",FirstName,Surname,"SUBMITTED","MEDICAL");
							ExcelUtilities.setValuesInExcel("PerformerPortal.xlsx", "Application",colValues, excelKeys);
							System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
							setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
						}
						Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
						Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
					}
					else
					{
						Assert.assertEquals(flag, true, "No records found under results");
					}
					
				
				}

	// Suraj G - If on capacity tab - we select no for license then system is not allowed to go further
	// Withdraw the same application and verify that View link is appear after withdraw
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"}  )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void  MedicalList_NotLicensed_Withdrawdraftapp(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{
					String key ="PL_CA_125";
					List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS","TYPE" );
					setup(browser, environment, clientName, "PL");
					LoginScreen objLoginScreen = new LoginScreen(getDriver());
					
					// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
					Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
					List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
				/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
					String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
					ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
					PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
					// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
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
					//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
					//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
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
							setAssertMessage("The Personal detail is completed", 3);	
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
							setAssertMessage("The Employment History is completed", 4);	
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
							setAssertMessage("The Refereee is completed", 5);	
						}
						Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
					// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
					// Capacity tab
						Capacity objCapacity = objCreateNewApp.clickonCapacity();
						String ActualTableName = objCapacity.SelectNoforLicensed(1);	

						if(evidence)
						{
										
							objCapacity.ScreenshotofCapacity(key+"Notlicensed_capacity");
						                   
						}

						System.out.println(ActualTableName);
						objCreateNewApp = objCapacity.ClickOnSave_Submit();	
						Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkPersonalDetail(ActualTableName);	
						if(!TickMarkflagCap)
						{
							System.out.println("The Capacity  is not completed if we entered as no license");
							setAssertMessage("The Capacity  is not completed if we entered as no license", 6);	
						}
						Verify.verifyTrue((!TickMarkflagCap), "The Capacity  is completed if we entered as no license");
						Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
						PerformerList objPerformerList = objCreateNewApp.clickonlink("PCS HOME");
						String ApplicationNumber =objPerformerList.getApplicationNumberwithstatus("draft");
						if(evidence)
						{
							objPerformerList.screenshotAppStatus_portal(key+"DraftStatus_applicationnumber");
						}
						System.out.println("This "+ApplicationNumber+" is in draft status");
						setAssertMessage("This "+ApplicationNumber+" is in draft status", 6);
						Boolean disable1 =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
						Boolean disableWithdraw1 =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
						if(disable1 && (!(disableWithdraw1)))
								{
									helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
									ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
								}
					Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
					}
					else
					{
						Assert.assertEquals(flag, true, "No records found under results");
					}
					
				}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","PL","Sanity","CLONE"} ,priority = 1 )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_MedicalList_Sanity(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{
		List<String> keys = Arrays.asList("PL_CA_139", "PL_CA_17","PL_CA_6");
		setup(browser, environment, clientName, "PL");
		/*PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
		String performer_PerformerPortal = ConfigurationData.Process_Medical;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
		
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		
		// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
/*		
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
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
*/		
		
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
				setAssertMessage("The Personal detail is completed", 3);	
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
				setAssertMessage("The Employment History is completed", 4);	
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
				setAssertMessage("The Refereee is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Referee is not completed");
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		// Capacity tab
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String ActualTableName = objCapacity.selectCapacity(1, GMC);
			if(evidence)
			{
				for(String key: keys)
				{
					objCapacity.ScreenshotofCapacity(key+"Capacity_sanity");
				}
			}
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
		//Insurance Tab 
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 7);	
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
				setAssertMessage("The Nationality tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(1);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
		// Trainee
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 10);	
			}
			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
			Qualification objQualification = objCreateNewApp.clickonQualification();
			
			objQualification = objQualification.AddProfessionalQualification(1);
			String ActualTablename_Qualification = objQualification.Uploadfile();
			objCreateNewApp = objQualification.ClickOnSave_Submit();	
			Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
			if(TickMarkflagQualification)
			{
			System.out.println("The Qualification tab is completed ");
			setAssertMessage("The Qualification tab is completed", 11);	
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
			setAssertMessage("The Professional tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
		 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
			String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
			objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
			Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagAppraisal)
			{
			System.out.println("The Appraisal tab is completed ");
			setAssertMessage("The Appraisal tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			objCreateNewApp = objTrainingDetails.ClickOnSave_Submit();
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			setAssertMessage("The Training Details tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
			objCreateNewApp = objCommunicationSkills.ClickOnSave_Submit();
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			setAssertMessage("The Communication Skills tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			objCreateNewApp = objAdditionalInfo.ClickOnSave_Submit();
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
			objCreateNewApp = objDeclaration.ClickOnSave_Submit();
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
			objCreateNewApp = objDeclarationBody.ClickOnSave_Submit();
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
			objCreateNewApp = objPoliceCheck.ClickOnSave_Submit();
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
			setAssertMessage("The PoliceCheck tab is completed", 19);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			objCreateNewApp = objTeamPreference.ClickOnSave_Submit();
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
			setAssertMessage("The Team Preference is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
	// HealthClearance
			HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
			String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(1);
			objCreateNewApp = objHealthClearance.ClickOnSave_Submit();
			Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
			if(TickMarkflagHealthClerance)
			{
			System.out.println("The Health Clerance is completed ");
			setAssertMessage("The Health Clerance is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(getDriver());
			objCreateNewApp = objUndertaking.ClickOnSave_Submit();
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
			if(TickMarkflagUndertaking)
			{
			System.out.println("The Undertaking is completed ");
			setAssertMessage("The Undertaking is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
		//	objCreateNewApp = objSubmitApplication.ClickOnSave_Submit();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			if(evidence)
			{
				for(String key: keys)
				{
					objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
				}
			}
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
			System.out.println("The Application Number is " +Application_Number );
			setAssertMessage("The Application Number is " +Application_Number, 24);	
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	
	}
		
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE"}  )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void  MedicalList_NotLicensed_draftapp(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
				{
					String key="PL_CA_40";
					setup(browser, environment, clientName, "PL");
					List<String> excelKeys = Arrays.asList("APPNO","USER","PASSWORD","FIRSTNAME","SURNAME","STATUS" );
					LoginScreen objLoginScreen = new LoginScreen(getDriver());
					
					// Amit : Enter the Registration details & get USERID for Applicant. Amended this existing test case but no changes made to Employment details to add additional local office.
					Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
					List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
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
				/*	PerformerHome ObjPerformerHome = new PerformerHome(getDriver());
					String performer_PerformerPortal = ConfigurationData.PerformerName_Medical;
					ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
					PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
					boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
				
						if(!DraftStatus)
						
						{
				
						Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Create New Application");
						Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(getDriver(), "Withdraw Application");
						if(disable && (!(disableWithdraw)))
								{
									helpers.CommonFunctions.ClickOnButton("Withdraw Application", getDriver());
									ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
								}
							helpers.CommonFunctions.ClickOnButton("Create New Application", getDriver());
							NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
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
							setAssertMessage("The Personal detail is completed", 3);	
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
							setAssertMessage("The Employment History is completed", 4);	
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
							setAssertMessage("The Refereee is completed", 5);	
						}
						Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
					// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
					// Capacity tab
						Capacity objCapacity = objCreateNewApp.clickonCapacity();
						String ActualTableName = objCapacity.SelectNoforLicensed(1);	
						if(evidence)
						{
							objCapacity.ScreenshotofCapacity(key+"Not licesed_Capacity");
						}
						System.out.println(ActualTableName);
						objCreateNewApp = objCapacity.ClickOnSave_Submit();	
						Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkPersonalDetail(ActualTableName);	
						if(!TickMarkflagCap)
						{
							System.out.println("The Capacity  is not completed if we entered as no license");
							setAssertMessage("The Capacity  is not completed if we entered as no license", 6);	
						}
						Verify.verifyTrue((!TickMarkflagCap), "The Capacity  is completed if we entered as no license");
						Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
						PerformerList objPerformerList = objCreateNewApp.clickonlink("PCS HOME");
						String ApplicationNumber =objPerformerList.getApplicationNumberwithstatus("draft");
						if(evidence)
						{
							objPerformerList.screenshotAppStatus_portal(key+"DraftStatusApplicationNumber");
						}
						System.out.println("This "+ApplicationNumber+" is in draft status");
						setAssertMessage("This "+ApplicationNumber+" is in draft status", 6);
						Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
					}
					else
					{
						Assert.assertEquals(flag, true, "No records found under results");
					}
				}

	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","CLONE","SECURITY"}, priority=2 )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void CreateApplication_MedicalList_Upload(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{
		List<String> keys = Arrays.asList("PL_CA_6", "PL_CA_17");
		List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
		int Expecteddownload = Extensions.size();
		setup(browser, environment, clientName, "PL");
		LoginScreen objLoginScreen = new LoginScreen(getDriver());
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,"MEDICAL");
		String user = UD.get(0);
		String GMC = UD.get(1);
		setAssertMessage("The user "+user+"is successfully registerted.", 1);
		tearDown(browser);
		Thread.sleep(2000);
		setup(browser, environment, clientName, "CRMPL", "PLPERFORMER");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectPrimaryEntity",1);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectoptGroupType",1);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectField",1);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvanceFind", "selectfilterCondition",1);	
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
		
			setup(browser, environment, clientName, "PL");
			objLoginScreen = new LoginScreen(getDriver());
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
			if(!DraftStatus)		
			{
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
				setAssertMessage("The Personal detail is completed", 3);	
			}
		Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
		// Employment History 
			NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
			objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
			Boolean Fileuplaod = objNewApp_EmpHistory.verifyCount(Expecteddownload);
			if(Fileuplaod)
			{
				System.out.println("The Expected Document is loaded on Employment History " + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod, "The Expected Document is not loaded on Employment History ");
			if(evidence)
			{
				for(String key: keys)
                    {
					objNewApp_EmpHistory.screenshots(key+"Upload_Document_Emp History");
                    }
			}
			objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
			Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
			if(TickMarkflagEmp)
			{
				System.out.println("The Employment History is completed ");
				setAssertMessage("The Employment History is completed", 4);	
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
				setAssertMessage("The Refereee is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Referee is not completed");
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		// Capacity tab
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String ActualTableName = objCapacity.selectCapacity(1,GMC );
			System.out.println(ActualTableName);
			if(evidence)
			{
				for(String key: keys)
                    {
						objCapacity.ScreenshotofCapacity(key+"Capacityselected");
                    }
			}
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
				setAssertMessage("The Capacity is completed", 6);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
		//Insurance Tab 
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			if(evidence)
			{
				for(String key: keys)
                    {
					objInsuranceDetails.Screenshot(key+"InsuranceUpload");
                    }
			}
			Boolean FileuploadIns = objInsuranceDetails.verifyCount(Expecteddownload);
			if(FileuploadIns)
			{
				System.out.println("The Expected Document is loaded on loaded on Insurance" + Expecteddownload);
				
			}
			Verify.verifyTrue(FileuploadIns, "The Expected Document is loaded on Insurance");
		

			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
				setAssertMessage("The Insurance tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
			
		// Nationality 
			Nationality objNationality = objCreateNewApp.clickonNationalityTab();
			objNationality = objNationality.selectbirth();
			objNationality = objNationality.uploadpassport();
			objNationality = objNationality.uploadSignature();
			objNationality = objNationality.ClickOnupload();
			if(evidence)
			{
				for(String key: keys)
                    {
					objNationality.Screenshots(key+"Upload_Document_Nationality");
                    }
			}
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
				setAssertMessage("The Nationality tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(1);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
				setAssertMessage("The Employment tab is completed", 9);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
		// Trainee
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
				setAssertMessage("The Trainee tab is completed", 10);	
			}
			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
		
		// Professional Qualification 	
			Qualification objQualification = objCreateNewApp.clickonQualification();
			
			objQualification = objQualification.AddProfessionalQualification(1);
			String ActualTablename_Qualification = objQualification.Uploadfile();
			if(evidence)
			{
				for(String key: keys)
                    {
					objQualification.screenshot(key+"Upload_Document_ProfessionalQualification");
                    }
			}
			Boolean Fileuplaod3 = objQualification.verifyCount(Expecteddownload);
			if(Fileuplaod3)
			{
				System.out.println("The Expected Document is loaded Signature of Nationality" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod3, "The Expected Document is not loaded Signature of Nationality");
			objCreateNewApp = objQualification.ClickOnSave_Submit();	
			Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
			if(TickMarkflagQualification)
			{
			System.out.println("The Qualification tab is completed ");
			setAssertMessage("The Qualification tab is completed", 11);	
			}
			Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
			
		// Professional Details 
			ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
			String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails_WithAllOptionUpload(environment);
			if(evidence)
			{
				for(String key: keys)
                    {
					objProfessionalDetails.screenshot(key+"Upload_Document_ProfessionalQualification");
                    }
			}
			Boolean FileuplaodRefused = objProfessionalDetails.verifyCountRefused(Expecteddownload);
			if(Fileuplaod3)
			{
				System.out.println("The Expected Document is loaded Refused section on Professional Details" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod3, "The Expected Document is loaded Refused section on Professional Details");
			Boolean FileuplaodSanction = objProfessionalDetails.verifyCountSanction(Expecteddownload);
			if(FileuplaodSanction)
			{
				System.out.println("The Expected Document is loaded Sanctional section on Professional Details" + Expecteddownload);
				
			}
			Verify.verifyTrue(FileuplaodSanction, "The Expected Document is loaded Refused section on Professional Details");
			Boolean FileuplaodNational = objProfessionalDetails.verifyCountNational(Expecteddownload);
			if(FileuplaodNational)
			{
				System.out.println("The Expected Document is loaded Refused section on Professional Details" + Expecteddownload);
				
			}
			Verify.verifyTrue(FileuplaodNational, "The Expected Document is loaded National section on Professional Details");
			objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
			if(TickMarkflagProfessional)
			{
			System.out.println("The Professional tab is completed ");
			setAssertMessage("The Professional tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
		 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
			String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
			objAppraisal = objAppraisal.upload();
			if(evidence)
			{
				for(String key: keys)
                    {
					objAppraisal.screenshot(key+"Upload_Document_Appraisal History");
                    }
			}
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
			setAssertMessage("The Appraisal tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			if(evidence)
			{
				for(String key: keys)
                    {
					objTrainingDetails.screenshot(key+"Upload_Document_Training Tab");
                    }
			}
			Boolean Fileuplaod5 = objTrainingDetails.verifyCount(Expecteddownload);
			if(Fileuplaod5)
			{
				System.out.println("The Expected Document is loaded on Training" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod5, "The Expected Document is not loaded on Training");
			objCreateNewApp = objTrainingDetails.ClickOnSave_Submit();
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			setAssertMessage("The Training Details tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes");
			Boolean Fileuplaod6 = objCommunicationSkills.verifyCount(Expecteddownload);
		
			if(Fileuplaod6)
			{
				System.out.println("The Expected Document is loaded on Communication" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod6, "The Expected Document is not loaded on Communication");
			if(evidence)
			{
				for(String key: keys)
                    {
					objCommunicationSkills.screenshot(key+"Upload_Document_Communication_Tab");
                    }
			}
			objCreateNewApp = objCommunicationSkills.ClickOnSave_Submit();
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			setAssertMessage("The Communication Skills tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			objAdditionalInfo = objAdditionalInfo.upload();
			if(evidence)
			{
				for(String key: keys)
                    {
					objAdditionalInfo.screenshot(key+"Upload_Document_AdditionalInfo_Tab");
                    }
			}
			Boolean Fileuplaod7 = objAdditionalInfo.verifyCount(Expecteddownload);
			if(Fileuplaod7)
			{
				System.out.println("The Expected Document is loaded on Additional Info" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod7, "The Expected Document is not loaded on Additional Info");
			objCreateNewApp = objAdditionalInfo.ClickOnSave_Submit();
			Thread.sleep(10000);
			Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
			if(TickMarkflagAdditioanlInfo)
			{
			System.out.println("The Additional Info tab is completed ");
			setAssertMessage("The Additional Info tab is completed", 15);	
		//	}
	//		//   Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
			String ActualTablename_Declaration = objDeclaration.getactualtablename();
			objDeclaration = objDeclaration.SelectdeclarationUpload(2);
			if(evidence)
			{
				for(String key: keys)
                    {
					objDeclaration.screenshot(key+"Upload_Document_Declaration_Tab");
                    }
			}
			Boolean Fileuplaod8 = objDeclaration.SelectdeclarationUploadcount(Expecteddownload);
			if(Fileuplaod8)
			{
				System.out.println("The Expected Document is loaded on Declaration for each declaration" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod8, "The Expected Document is not loaded on Declaration for each declaration");
			
			objCreateNewApp = objDeclaration.ClickOnSave_Submit();
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
			String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration_Yes("Yes");
			objDeclarationBody = objDeclarationBody.selectcorporateDeclarationUpload(2);
			if(evidence)
			{
				for(String key: keys)
                    {
					objDeclarationBody.screenshot(key+"Upload_Document_Declaration_Tab");
                    }
			}
			Boolean Fileuplaod9 = objDeclarationBody.SelectdeclarationBodyUploadcount(Expecteddownload);
			if(Fileuplaod9)
			{
				System.out.println("The Expected Document is loaded on Declaration body for each declaration" + Expecteddownload);
				
			}
		//	Verify.verifyTrue(Fileuplaod9, "The Expected Document is not loaded on Declaration Body for each declaration");
			objCreateNewApp = objDeclarationBody.ClickOnSave_Submit();
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
			Boolean Fileuplaod10 = objPoliceCheck.verifycountDBSCheck(Expecteddownload);
			if(Fileuplaod10)
			{
				System.out.println("The Expected Document is loaded on DBSCheck" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod10, "The Expected Document is not loaded on DBSCheck");
			Boolean Fileuplaod11 = objPoliceCheck.verifycountForeign(Expecteddownload);
			if(Fileuplaod11)
			{
				System.out.println("The Expected Document is loaded on Foreign Check - DBS Check" + Expecteddownload);
				
			}
			Verify.verifyTrue(Fileuplaod11, "The Expected Document is not Foreign Check - DBS Check");
			objCreateNewApp = objPoliceCheck.ClickOnSave_Submit();
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
			setAssertMessage("The PoliceCheck tab is completed", 19);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			objCreateNewApp = objTeamPreference.ClickOnSave_Submit();
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
			setAssertMessage("The Team Preference is completed", 20);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
	// HealthClearance
			HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
			String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(1);
			objCreateNewApp = objHealthClearance.ClickOnSave_Submit();
			Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
			if(TickMarkflagHealthClerance)
			{
			System.out.println("The Health Clerance is completed ");
			setAssertMessage("The Health Clerance is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(getDriver());
			objCreateNewApp = objUndertaking.ClickOnSave_Submit();
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
			if(TickMarkflagUndertaking)
			{
			System.out.println("The Undertaking is completed ");
			setAssertMessage("The Undertaking is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(getDriver());
		//	objCreateNewApp = objSubmitApplication.ClickOnSave_Submit();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , getDriver());
		/*	Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 23);	
			}*/
		//	Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			if(evidence)
			{
				for(String key: keys)
                    {
					objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
                    }
			}
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
			System.out.println("The Application Number is " +Application_Number );
			setAssertMessage("The Application Number is " +Application_Number, 24);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
				setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
			else
			{
				System.out.println("The Application Number is not same as on Sumitted action paragraph  " +Application_Number );
				setAssertMessage("The Application Number is  not same as on Sumitted action paragraph " +Application_Number, 23);		
				
			}
			
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		}
		else
		{
			Assert.assertEquals(flag, true, "No records found under results");
		}
	}

}
}





