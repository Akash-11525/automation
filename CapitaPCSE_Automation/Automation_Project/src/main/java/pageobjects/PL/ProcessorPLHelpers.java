package pageobjects.PL;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import browsersetup.BaseClass;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.EmailDescription;
import pageobjects.LoginScreen;
import pageobjects.Registration;
import utilities.ExcelUtilities;
import verify.Verify;

public class ProcessorPLHelpers extends BaseClass{
	
	WebDriver driver;
	WebDriverWait wait;
	    
	    @FindBy(xpath="//div[@class='loader']")
		static
	    WebElement Spinner;
	
	//public static String GMC = null;
	
	public static void CreateApplication_Ophthalmic_Process(WebDriver driver, String environment) throws InterruptedException, IOException, AWTException
	{
		
		LoginScreen objLoginScreen = new LoginScreen(driver);
		PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
		
		boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			if(!DraftStatus)
			
			{
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
			Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
			if(disable && (!(disableWithdraw)))
					{
						helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
						ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
					}
		//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
			helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
				NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			}
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
		
				
			}
			CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);

			NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
			ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
			EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
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
			//	setAssertMessage("The Employment History is completed", 3);	
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
			//	setAssertMessage("The Refereee is completed", 4);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		//Capacity
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",2);
			String ActualTableName = objCapacity.selectCapacity(10, GMCNumbertxt);
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
			//setAssertMessage("The Capacity is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
			//	setAssertMessage("The Insurance tab is completed", 6);	
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
			//	setAssertMessage("The Nationality tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(3);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
			//	setAssertMessage("The Employment tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
			
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
			//	setAssertMessage("The Trainee tab is completed", 9);	
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
			//setAssertMessage("The Qualification tab is completed", 10);	
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
		//	setAssertMessage("The Professional tab is completed", 11);	
			}
			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
			
	// Appraisal History 
		 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
			String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(10); 
			objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
			Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagAppraisal)
			{
			System.out.println("The Appraisal tab is completed ");
			//setAssertMessage("The Appraisal tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(3); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			//setAssertMessage("The Training Details tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			//setAssertMessage("The Communication Skills tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
			if(TickMarkflagAdditioanlInfo)
			{
			System.out.println("The Additional Info tab is completed ");
			//setAssertMessage("The Additional Info tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
			String ActualTablename_Declaration = objDeclaration.getactualtablename();
			objDeclaration = objDeclaration.Selectdeclaration(4); 			
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
			if(TickMarkflagDecalration)
			{
			System.out.println("The Declaration tab is completed ");
			//setAssertMessage("The Declaration tab is completed", 16);	
			}
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
			String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
			if(TickMarkflagDecalrationBody)
			{
			System.out.println("The Declaration Body tab is completed ");
			//setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
	// Police Check tab
			PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
			String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
		//	setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
		//	setAssertMessage("The Team Preference is completed", 19);	
			}
			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
			String MedPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",10);
			if(MedPerformer_cap.equalsIgnoreCase("Ophthalmic medical practitioner"))
			{
				// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(10);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				if(TickMarkflagHealthClerance)
				{
				System.out.println("The Health Clerance is completed ");
				//setAssertMessage("The Health Clerance is completed", 20);	
				}
				Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
			}
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);

	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
			Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);

			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			System.out.println("The Application Number is " +Application_Number );
			//setAssertMessage("The Application Number is " +Application_Number, 23);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",Application_Number, 1);
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
			//	setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	
	
		
		}
	
	public static void CreateApplication_Medical_Process(WebDriver driver, String environment) throws InterruptedException, IOException, AWTException
	{
		LoginScreen objLoginScreen = new LoginScreen(driver);
		PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLProcess Applicant Medical","environment");
		boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			if(!DraftStatus)
			
			{
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
			Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
			if(disable && (!(disableWithdraw)))
					{
						helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
						ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
					}
		//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
			helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
				NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			}
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
		
				
			}
			CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);
		//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
			NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
			ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
			EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				//setAssertMessage("The Personal detail is completed", 2);	
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
			//	setAssertMessage("The Employment History is completed", 3);	
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
			//	setAssertMessage("The Refereee is completed", 4);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		//Capacity
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
			String ActualTableName = objCapacity.selectCapacity(1, GMCNumbertxt);
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
			//setAssertMessage("The Capacity is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
			//	setAssertMessage("The Insurance tab is completed", 6);	
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
			//	setAssertMessage("The Nationality tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(1);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
			//	setAssertMessage("The Employment tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
			
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
			//	setAssertMessage("The Trainee tab is completed", 9);	
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
			//setAssertMessage("The Qualification tab is completed", 10);	
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
		//	setAssertMessage("The Professional tab is completed", 11);	
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
			//setAssertMessage("The Appraisal tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			//setAssertMessage("The Training Details tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			//setAssertMessage("The Communication Skills tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
			if(TickMarkflagAdditioanlInfo)
			{
			System.out.println("The Additional Info tab is completed ");
			//setAssertMessage("The Additional Info tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
			String ActualTablename_Declaration = objDeclaration.getactualtablename();
			objDeclaration = objDeclaration.Selectdeclaration(4); 			
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
			if(TickMarkflagDecalration)
			{
			System.out.println("The Declaration tab is completed ");
			//setAssertMessage("The Declaration tab is completed", 16);	
			}
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
			String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
			if(TickMarkflagDecalrationBody)
			{
			System.out.println("The Declaration Body tab is completed ");
			//setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
	// Police Check tab
			PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
			String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
		//	setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
		//	setAssertMessage("The Team Preference is completed", 19);	
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
			//setAssertMessage("The Health Clerance is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
	/*		if(TickMarkflagUndertaking)
			{
			System.out.println("The Undertaking is completed ");
			setAssertMessage("The Undertaking is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");*/
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
/*			Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			System.out.println("The Application Number is " +Application_Number );
			//setAssertMessage("The Application Number is " +Application_Number, 23);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",Application_Number, 1);
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
			//	setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	
	
		
		}
	
	
	public static void CreateApplication_Medical_Process_Approve(WebDriver driver, String environment) throws InterruptedException, IOException, AWTException
	{

	
		PerformerHome ObjPerformerHome = new PerformerHome(driver);
	//	String performer_PerformerPortal = ConfigurationData.ProcessPL_Approve;
		String performer_PerformerPortal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ProcessApprove", 1);
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();
	//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
		Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
		Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
		if(disable && (!(disableWithdraw)))
				{
					helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
					ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
				}
	//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
		helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
			
			NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
		//	NewAppPersonalDetail ObjNewApp_PersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
			ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
			EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				//setAssertMessage("The Personal detail is completed", 2);	
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
			//	setAssertMessage("The Employment History is completed", 3);	
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
			//	setAssertMessage("The Refereee is completed", 4);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		//Capacity
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
			String ActualTableName = objCapacity.selectCapacity(1, GMCNumbertxt);
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
			//setAssertMessage("The Capacity is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
			//	setAssertMessage("The Insurance tab is completed", 6);	
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
			//	setAssertMessage("The Nationality tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(1);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
			//	setAssertMessage("The Employment tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
			
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
			//	setAssertMessage("The Trainee tab is completed", 9);	
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
			//setAssertMessage("The Qualification tab is completed", 10);	
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
		//	setAssertMessage("The Professional tab is completed", 11);	
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
			//setAssertMessage("The Appraisal tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			//setAssertMessage("The Training Details tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			//setAssertMessage("The Communication Skills tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
			if(TickMarkflagAdditioanlInfo)
			{
			System.out.println("The Additional Info tab is completed ");
			//setAssertMessage("The Additional Info tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
			String ActualTablename_Declaration = objDeclaration.getactualtablename();
			objDeclaration = objDeclaration.Selectdeclaration(4); 			
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
			if(TickMarkflagDecalration)
			{
			System.out.println("The Declaration tab is completed ");
			//setAssertMessage("The Declaration tab is completed", 16);	
			}
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
			String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
			if(TickMarkflagDecalrationBody)
			{
			System.out.println("The Declaration Body tab is completed ");
			//setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
	// Police Check tab
			PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
			String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
		//	setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
		//	setAssertMessage("The Team Preference is completed", 19);	
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
			//setAssertMessage("The Health Clerance is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
	/*		if(TickMarkflagUndertaking)
			{
			System.out.println("The Undertaking is completed ");
			setAssertMessage("The Undertaking is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");*/
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
/*			Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			System.out.println("The Application Number is " +Application_Number );
			//setAssertMessage("The Application Number is " +Application_Number, 23);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",Application_Number, 1);
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
			//	setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	
	
		
		}
	

	public static void CreateApplication_Dental_Process(WebDriver driver, String environment) throws InterruptedException, IOException, AWTException
	{
		LoginScreen objLoginScreen = new LoginScreen(driver);
		PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PL Applicant Dental", "environment");
	
		/*PerformerHome ObjPerformerHome = new PerformerHome(driver);
		String performer_PerformerPortal = ConfigurationData.Process_Dental;
		ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
		PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();*/
	//	CreateNewApp objCreateNewApp = new CreateNewApp(driver);
		
	//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
	/*	Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
		Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
		if(disable && (!(disableWithdraw)))
				{
					helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
					ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
				}
	//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
		helpers.CommonFunctions.ClickOnButton("Create New Application", driver);*/
		
		
		boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			if(!DraftStatus)
			
			{
		//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
			Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
			if(disable && (!(disableWithdraw)))
					{
						helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
						ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
					}
		//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
			helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
				NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			}
			if(DraftStatus)
			{
				ObjPerformerList = ObjPerformerList.clickonEdit_draft();
		
				
			}
			CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);
		//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
		//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
			NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
			ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
			EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
			objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
			ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
			helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
			CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
			Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
			if(TickMarkflagPersonal)
			{
				System.out.println("The Personal detail is completed ");
				//setAssertMessage("The Personal detail is completed", 2);	
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
			//	setAssertMessage("The Employment History is completed", 3);	
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
			//	setAssertMessage("The Refereee is completed", 4);	
			}
			Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
		//Capacity
			Capacity objCapacity = objCreateNewApp.clickonCapacity();
			String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",3);
			String ActualTableName = objCapacity.selectCapacity(2, GMCNumbertxt);
			System.out.println(ActualTableName);
			objCreateNewApp = objCapacity.ClickOnSave_Submit();	
			Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
			if(TickMarkflagCap)
			{
				System.out.println("The Capacity  is completed ");
			//setAssertMessage("The Capacity is completed", 5);	
			}
			Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			
			InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
			String ActualTableName_Ins = objInsuranceDetails.Selectcover();
			objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
			Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
			if(TickMarkflagIns)
			{
				System.out.println("The Insurance tab is completed ");
			//	setAssertMessage("The Insurance tab is completed", 6);	
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
			//	setAssertMessage("The Nationality tab is completed", 7);	
			}
			Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
			
		//Proposed Employment 
		// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
		
			Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
			String ActualTablename_Emp = objEmployment.selectstatement();
			objEmployment =objEmployment.AddPractice(2);
			objCreateNewApp = objEmployment.ClickOnSave_Submit();	
			Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
			if(TickMarkflagEmployment)
			{
				System.out.println("The Employment tab is completed ");
			//	setAssertMessage("The Employment tab is completed", 8);	
			}
			Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
			
			
			Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
			String ActualTablename_Trainee = objTrainee.selectTrainee();
			objCreateNewApp = objTrainee.ClickOnSave_Submit();	
			Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
			if(TickMarkflagTrainee)
			{
				System.out.println("The Trainee tab is completed ");
			//	setAssertMessage("The Trainee tab is completed", 9);	
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
			//setAssertMessage("The Qualification tab is completed", 10);	
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
		//	setAssertMessage("The Professional tab is completed", 11);	
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
			//setAssertMessage("The Appraisal tab is completed", 12);	
			}
			Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
			
	// Training Details
			TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
			String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(2); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
			if(TickMarkflagTrainingDetails)
			{
			System.out.println("The Training Details tab is completed ");
			//setAssertMessage("The Training Details tab is completed", 13);	
			}
			Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
			
	// Communication Skills
			CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
			String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
			if(TickMarkflagCommunicationSkills)
			{
			System.out.println("The Communication Skills Details tab is completed ");
			//setAssertMessage("The Communication Skills tab is completed", 14);	
			}
			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
			
	// Additional Info
			AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
			String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
			if(TickMarkflagAdditioanlInfo)
			{
			System.out.println("The Additional Info tab is completed ");
			//setAssertMessage("The Additional Info tab is completed", 15);	
			}
			Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
			
	// Declaration Tab 	
		 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
			String ActualTablename_Declaration = objDeclaration.getactualtablename();
			objDeclaration = objDeclaration.Selectdeclaration(4); 			
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
			if(TickMarkflagDecalration)
			{
			System.out.println("The Declaration tab is completed ");
			//setAssertMessage("The Declaration tab is completed", 16);	
			}
			Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
			
	// Declaration Body Tab 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
			String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
			if(TickMarkflagDecalrationBody)
			{
			System.out.println("The Declaration Body tab is completed ");
			//setAssertMessage("The Declaration Body tab is completed", 17);	
			}
			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
	// Police Check tab
			PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
			String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
			if(TickMarkflagPolicecheck)
			{
			System.out.println("The PoliceCheck tab is completed ");
		//	setAssertMessage("The PoliceCheck tab is completed", 18);	
			}
			Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
	// Team Preference
			TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
			String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
			if(TickMarkflagTeamPreference)
			{
			System.out.println("The Team Preference is completed ");
		//	setAssertMessage("The Team Preference is completed", 19);	
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
			//setAssertMessage("The Health Clerance is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
	// Undertaking 
		//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
			String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
			Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
	/*		if(TickMarkflagUndertaking)
			{
			System.out.println("The Undertaking is completed ");
			setAssertMessage("The Undertaking is completed", 21);	
			}
			Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");*/
	// Submit Application 
			SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
			String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
			helpers.CommonFunctions.ClickOnAllCheckBox(driver);
			helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
/*			Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
			if(TickMarkflagSubmitApp)
			{
			System.out.println("The Submit App is completed ");
			setAssertMessage("The Submit App is completed", 22);	
			}
			Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
			String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
			ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
			String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
			System.out.println("The Application Number is " +Application_Number );
			//setAssertMessage("The Application Number is " +Application_Number, 23);	
			if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
			{
				utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",Application_Number, 1);
				System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
			//	setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
			}
			Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	
	
		
		}
	public  static void CreateApplication_MedicalListFinal(WebDriver driver, String environment, String browser) throws InterruptedException, IOException, AWTException
	{
		//CreateApplication_MedicalList_Amit(driver,environment,browser);
	}
	
	
	public static void CreateApplication_MedicalList_Amit(WebDriver driver, String environment, String browser, String appType) throws InterruptedException, IOException, AWTException
	{
		
		LoginScreen objLoginScreen = new LoginScreen(driver);		
		
		// Amit : Enter the Registration details & get USERID for Applicant.
		Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
		List<String> UD = objRegistration.CompleteRegistration(environment,appType);
		String user = UD.get(0);
		String GMC = UD.get(1);
		String firstName = UD.get(2);
		String Surname = UD.get(3);
		System.out.println("GMC Number" +GMC);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "PLUSER",user, 1);
		//utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ConfigurationDetails.xlsx", "USERDETAILS", "PLAPPLICANTNHSE_ID",user, 2);
		
	}
	
	public static void CreateApplication_MedicalList_Amit_Activate(WebDriver driver, String environment, String browser, String user) throws InterruptedException, IOException, AWTException
	{

		CrmHome ObjCrmHome  = new CrmHome(driver);
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
			
		}
		EmailDescription ObjEmailDescription = new EmailDescription(driver);
		ObjEmailDescription.getEmailActivationLink();
		Thread.sleep(2000);
	}
	
		//	quit(driver);
	public static void CreateApplication_MedicalList_Amit_ActivationLink(WebDriver driver, String environment, String browser) throws InterruptedException, IOException, AWTException
	{
			
			EmailDescription ObjEmailDescription = new EmailDescription(driver);
			ObjEmailDescription  = new EmailDescription(driver);
			ObjEmailDescription.browseEmailActivationLink();
	}

	public static void CreateAPP(WebDriver driver, String environment, String browser) throws InterruptedException, IOException, AWTException
	{
	//		quit(browser);
			
			//Integrated with Suraj's code for create app
			
			boolean evidence = true;
			String key ="1";
			String clientName = "PCSE";

			LoginScreen objLoginScreen = new LoginScreen(driver);
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
			//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				if(!DraftStatus)
				
				{
			//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
				Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
				if(disable && (!(disableWithdraw)))
						{
							helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
							ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
						}
			//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
				helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
					NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				}
				if(DraftStatus)
				{
					ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			
					
				}
				CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();				
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				if(TickMarkflagPersonal)
				{
					System.out.println("The Personal detail is completed ");
			//		setAssertMessage("The Personal detail is completed", 3);	
				}
		//	Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
			// Employment History 
				NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
				objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
				objNewApp_EmpHistory = objNewApp_EmpHistory.AddEmpHistoryRecord();
				objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
				Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
				if(TickMarkflagEmp)
				{
					System.out.println("The Employment History is completed ");
				//	setAssertMessage("The Employment History is completed", 4);	
				}
		//		Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
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
			//		setAssertMessage("The Refereee is completed", 5);	
				}
		//		Verify.verifyTrue(TickMarkflagRef, "The Referee is not completed");
			// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			// Capacity tab
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
				String ActualTableName = objCapacity.selectCapacity(4, GMCNumbertxt);
				System.out.println(ActualTableName);
				if(evidence)
				{
					
							objCapacity.ScreenshotofCapacity(key+"Capacityselected");
	                   
				}
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				if(TickMarkflagCap)
				{
					System.out.println("The Capacity  is completed ");
			//		setAssertMessage("The Capacity is completed", 6);	
				}
		//		Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			//Insurance Tab 
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
				if(TickMarkflagIns)
				{
					System.out.println("The Insurance tab is completed ");
				//	setAssertMessage("The Insurance tab is completed", 7);	
				}
		//		Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
				
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
				//	setAssertMessage("The Nationality tab is completed", 8);	
				}
		//		Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
				
		//Proposed Employment 
			// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement();
				objEmployment =objEmployment.AddPractice(1);
			//	objEmployment.AddAnotherLocaloffice(environment,"777");
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				if(TickMarkflagEmployment)
				{
					System.out.println("The Employment tab is completed ");
				//	setAssertMessage("The Employment tab is completed", 9);	
				}
		//		Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
				
							// Trainee
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				if(TickMarkflagTrainee)
				{
					System.out.println("The Trainee tab is completed ");
				//	setAssertMessage("The Trainee tab is completed", 10);	
				}
	//			Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
			
			// Professional Qualification 	
				Qualification objQualification = objCreateNewApp.clickonQualification();
				
				objQualification = objQualification.AddProfessionalQualification(1);
				String ActualTablename_Qualification = objQualification.Uploadfile();
				objCreateNewApp = objQualification.ClickOnSave_Submit();	
				Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
				if(TickMarkflagQualification)
				{
				System.out.println("The Qualification tab is completed ");
			//	setAssertMessage("The Qualification tab is completed", 11);	
				}
		//		Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
				
			// Professional Details 
				ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
				String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails(environment);
				objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
				if(TickMarkflagProfessional)
				{
				System.out.println("The Professional tab is completed ");
			//	setAssertMessage("The Professional tab is completed", 12);	
				}
	//			Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
				
		// Appraisal History 
			 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
				objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagAppraisal)
				{
				System.out.println("The Appraisal tab is completed ");
			//	setAssertMessage("The Appraisal tab is completed", 13);	
				}
		//		Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
				
		// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
				objCreateNewApp = objTrainingDetails.ClickOnSave_Submit();
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
				if(TickMarkflagTrainingDetails)
				{
				System.out.println("The Training Details tab is completed ");
			//	setAssertMessage("The Training Details tab is completed", 14);	
				}
		//		Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
				
		// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				objCreateNewApp = objCommunicationSkills.ClickOnSave_Submit();
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				if(TickMarkflagCommunicationSkills)
				{
				System.out.println("The Communication Skills Details tab is completed ");
		//		setAssertMessage("The Communication Skills tab is completed", 15);	
				}
	//			Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
				
		// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				objCreateNewApp = objAdditionalInfo.ClickOnSave_Submit();
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				if(TickMarkflagAdditioanlInfo)
				{
				System.out.println("The Additional Info tab is completed ");
		//		setAssertMessage("The Additional Info tab is completed", 15);	
				}
		//		Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
				
		// Declaration Tab 	
			 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
				String ActualTablename_Declaration = objDeclaration.getactualtablename();
				objDeclaration = objDeclaration.Selectdeclaration(2);
				
				objCreateNewApp = objDeclaration.ClickOnSave_Submit();
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				if(TickMarkflagDecalration)
				{
				System.out.println("The Declaration tab is completed ");
		//		setAssertMessage("The Declaration tab is completed", 16);	
				}
		//		Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
				
		// Declaration Body Tab 
			//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				objCreateNewApp = objDeclarationBody.ClickOnSave_Submit();
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				if(TickMarkflagDecalrationBody)
				{
				System.out.println("The Declaration Body tab is completed ");
		//		setAssertMessage("The Declaration Body tab is completed", 17);	
				}
	//			Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
		// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				boolean TextMessageonpage = objPoliceCheck.verifytextmessageonpage();
				if(TextMessageonpage)
				{
					System.out.println("The DBS certificate text and abroad text field value is appear correctly ");
		//			setAssertMessage("The DBS certificate text and abroad text field value is appear correctly", 18);	
				}
				objCreateNewApp = objPoliceCheck.ClickOnSave_Submit();
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				if(TickMarkflagPolicecheck)
				{
				System.out.println("The PoliceCheck tab is completed ");
		//		setAssertMessage("The PoliceCheck tab is completed", 19);	
				}
		//		Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
		// Team Preference
				TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
				String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
				objCreateNewApp = objTeamPreference.ClickOnSave_Submit();
				Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
				if(TickMarkflagTeamPreference)
				{
				System.out.println("The Team Preference is completed ");
		//		setAssertMessage("The Team Preference is completed", 20);	
				}
	//			Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
		// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(1);
				objCreateNewApp = objHealthClearance.ClickOnSave_Submit();
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				if(TickMarkflagHealthClerance)
				{
				System.out.println("The Health Clerance is completed ");
		//		setAssertMessage("The Health Clerance is completed", 21);	
				}
		//		Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
		// Undertaking 
			//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
				objCreateNewApp = objUndertaking.ClickOnSave_Submit();
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				if(TickMarkflagUndertaking)
				{
				System.out.println("The Undertaking is completed ");
		//		setAssertMessage("The Undertaking is completed", 22);	
				}
		//		Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
		// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(driver);
			//	objCreateNewApp = objSubmitApplication.ClickOnSave_Submit();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
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
			//	setAssertMessage("The Application Number is " +Application_Number, 24);	
				if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
				{
					System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
					utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber",Application_Number, 1);
			//		setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
				}
				else
				{
					System.out.println("The Application Number is not same as on Sumitted action paragraph  " +Application_Number );
			//		setAssertMessage("The Application Number is  not same as on Sumitted action paragraph " +Application_Number, 23);		
					
				}
				
				Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
		//		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
						
		
		
	}
	
	public static void CreateAPP_OPTHAL(WebDriver driver, String environment, String browser) throws InterruptedException, IOException, AWTException
	{
	//		quit(browser);
			
			//Integrated with Suraj's code for create app
			
			boolean evidence = true;
			String key ="1";
			String clientName = "PCSE";

			

			LoginScreen objLoginScreen = new LoginScreen(driver);
				PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);

				boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");

			
				if(!DraftStatus)

				{
					//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
					Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
					Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
					if(disable && (!(disableWithdraw)))
					{
						helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
						ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
					}
					//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
					helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
					NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				}
				if(DraftStatus)
				{
					ObjPerformerList = ObjPerformerList.clickonEdit_draft();


				}
				CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);
				//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();

				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				if(TickMarkflagPersonal)
				{
					System.out.println("The Personal detail is completed ");
						
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
						
				}
				Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
				//Capacity
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String GMC = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",2);
				String ActualTableName = objCapacity.selectCapacity(3, GMC);
				System.out.println(ActualTableName);
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				if(TickMarkflagCap)
				{
					System.out.println("The Capacity  is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");

				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
				if(TickMarkflagIns)
				{
					System.out.println("The Insurance tab is completed ");
						
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
						
				}
				Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");

				//Proposed Employment 
				// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());

				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement();
				objEmployment =objEmployment.AddPractice(3);
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				if(TickMarkflagEmployment)
				{
					System.out.println("The Employment tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");


				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				if(TickMarkflagTrainee)
				{
					System.out.println("The Trainee tab is completed ");
						
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
						
				}
				Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");

				// Appraisal History 
				Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(3); 
				objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagAppraisal)
				{
					System.out.println("The Appraisal tab is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");

				// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(3); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagTrainingDetails)
				{
					System.out.println("The Training Details tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");

				// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				if(TickMarkflagCommunicationSkills)
				{
					System.out.println("The Communication Skills Details tab is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");

				// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				if(TickMarkflagAdditioanlInfo)
				{
					System.out.println("The Additional Info tab is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");

				// Declaration Tab 	
				Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
				String ActualTablename_Declaration = objDeclaration.getactualtablename();
				objDeclaration = objDeclaration.Selectdeclaration(2); 			
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				if(TickMarkflagDecalration)
				{
					System.out.println("The Declaration tab is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");

				// Declaration Body Tab 
				//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				if(TickMarkflagDecalrationBody)
				{
					System.out.println("The Declaration Body tab is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
				// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				if(TickMarkflagPolicecheck)
				{
					System.out.println("The PoliceCheck tab is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
				// Team Preference
				TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
				String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
				if(TickMarkflagTeamPreference)
				{
					System.out.println("The Team Preference is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
				String MedPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",3);
				if(MedPerformer_cap.equalsIgnoreCase("Ophthalmic medical practitioner"))
				{
					// HealthClearance
					HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
					String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(3);
					helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
					Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
					if(TickMarkflagHealthClerance)
					{
						System.out.println("The Health Clerance is completed ");
							
					}
					Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
				}
				// Undertaking 
				//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				if(TickMarkflagUndertaking)
				{
					System.out.println("The Undertaking is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
				// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(driver);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
				/*Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
				if(TickMarkflagSubmitApp)
				{
				System.out.println("The Submit App is completed ");
				setAssertMessage("The Submit App is completed", 22);	
				}
				Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");*/
				String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
				System.out.println("The Application Number is " +Application_Number );
					
				if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
				{
					System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
							
				}
				Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			//	Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	

			}
	
	public static void CreateAPP_DENTAL(WebDriver driver, String environment, String browser) throws InterruptedException, IOException, AWTException
	{
	//		quit(browser);
			
			//Integrated with Suraj's code for create app
			
			boolean evidence = true;
			String key ="1";
			String clientName = "PCSE";

			

			LoginScreen objLoginScreen = new LoginScreen(driver);
				PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);

				boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");

			
				if(!DraftStatus)

				{
					//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
					Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
					Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
					if(disable && (!(disableWithdraw)))
					{
						helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
						ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
					}
					//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
					helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
					NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				}
				if(DraftStatus)
				{
					ObjPerformerList = ObjPerformerList.clickonEdit_draft();


				}
				CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);
				//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();
				
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				if(TickMarkflagPersonal)
				{
					System.out.println("The Personal detail is completed ");
						
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
						
				}
				Verify.verifyTrue(TickMarkflagRef, "The Employment History is not completed");
			// Capacity tab	
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",3);
				String ActualTableName = objCapacity.selectCapacity(8, GMCNumbertxt);
				/*if(evidence)
				{
					for(String key: keys)
	                    {
						objCapacity.ScreenshotofCapacity(key+"Capacity_dental");
	                    }
				}*/

				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				if(TickMarkflagCap)
				{
					System.out.println("The Capacity  is completed ");
						
				}
				Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
				
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
				if(TickMarkflagIns)
				{
					System.out.println("The Insurance tab is completed ");
						
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
						
				}
				Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
				
			//Proposed Employment 
			// CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
			
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement_No();
				//objEmployment =objEmployment.AddPractice(1);
				objEmployment.AddAnotherLocaloffice(environment , "999");
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				if(TickMarkflagEmployment)
				{
					System.out.println("The Employment tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
				
				
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				if(TickMarkflagTrainee)
				{
					System.out.println("The Trainee tab is completed ");
						
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
					
				}
				Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
				
		// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(2); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagTrainingDetails)
				{
				System.out.println("The Training Details tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
				
		// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				if(TickMarkflagCommunicationSkills)
				{
				System.out.println("The Communication Skills Details tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
				
		// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				if(TickMarkflagAdditioanlInfo)
				{
				System.out.println("The Additional Info tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
				
		// Declaration Tab 	
			 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
				String ActualTablename_Declaration = objDeclaration.getactualtablename();
				objDeclaration = objDeclaration.Selectdeclaration(2); 			
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				if(TickMarkflagDecalration)
				{
				System.out.println("The Declaration tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
				
		// Declaration Body Tab 
			//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				if(TickMarkflagDecalrationBody)
				{
				System.out.println("The Declaration Body tab is completed ");
				
				}
				Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
		// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				if(TickMarkflagPolicecheck)
				{
				System.out.println("The PoliceCheck tab is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
		// Team Preference
				TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
				String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
				if(TickMarkflagTeamPreference)
				{
				System.out.println("The Team Preference is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
		// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(2);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				if(TickMarkflagHealthClerance)
				{
				System.out.println("The Health Clerance is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
		// Undertaking 
			//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Save and Next" , driver);
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				if(TickMarkflagUndertaking)
				{
				System.out.println("The Undertaking is completed ");
					
				}
				Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
		// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(driver);
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
				String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
				Boolean ActualSubmittedmessage = objCreateNewApp.verifysubmittedmessage(ApplicationNumberOnparagraph);
				if(ActualSubmittedmessage)
				{
					System.out.println("The Submitted message is displayed as expected");
					
				}
			//	Verify.verifyTrue(ActualSubmittedmessage, "The Application message is different on Submitted tab. ");
				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
				utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber",Application_Number, 1);
				/*if(evidence)
				{
					for(String key: keys)
	                    {
						ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
	                    }
				}*/

				System.out.println("The Dental Application Number is " +Application_Number );
					
			//	Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
			}
			/*else
			{
				Assert.assertEquals(flag, true, "No records found under results");
			}

			}*/
	
	
	public static void CreateAPP_MedReg(WebDriver driver, String environment, String browser) throws InterruptedException, IOException, AWTException
	{
	//		quit(browser);
			
			//Integrated with Suraj's code for create app
			
			boolean evidence = true;
			String key ="1";
			String clientName = "PCSE";

			LoginScreen objLoginScreen = new LoginScreen(driver);
			PerformerList ObjPerformerList  = objLoginScreen.logintoPL("PLAPPLICANT", environment);
			
			boolean DraftStatus = ObjPerformerList.getapplicationstatus("Draft");
			//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				if(!DraftStatus)
				
				{
			//	CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Boolean disable =helpers.CommonFunctions.VerifyEnabledButton(driver, "Create New Application");
				Boolean disableWithdraw =helpers.CommonFunctions.VerifyEnabledButton(driver, "Withdraw Application");
				if(disable && (!(disableWithdraw)))
						{
							helpers.CommonFunctions.ClickOnButton("Withdraw Application", driver);
							ObjPerformerList = ObjPerformerList.EnterWithdrawapplicationDetails();
						}
			//	CreateNewApp objCreateNewApp =  ObjPerformerList.clickonResult();
				helpers.CommonFunctions.ClickOnButton("Create New Application", driver);
					NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
				}
				if(DraftStatus)
				{
					ObjPerformerList = ObjPerformerList.clickonEdit_draft();
			
					
				}
				CreateNewApp ObjCreateNewApp = new CreateNewApp(driver);
			//	NewAppPersonalDetail ObjNewAppPersonalDetail = ObjPerformerList.clickonOK();
			//	CreateNewApp ObjCreateNewApp = ObjPerformerList.clickonOK();
				NewAppPersonalDetail ObjNewAppPersonalDetail =  ObjCreateNewApp.clickonPersonaldetail();	
				
				
				
				ObjNewAppPersonalDetail = ObjNewAppPersonalDetail.EnterPatientDetail();
				EnterAddressManually objEnterAddressManually = ObjNewAppPersonalDetail.ClickonResidentalAdd();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterResAddressManually();
				objEnterAddressManually = ObjNewAppPersonalDetail.clickOnGMCaddress();
				ObjNewAppPersonalDetail = objEnterAddressManually.EnterGMCAddressManually();
				helpers.CommonFunctions.ClickOnRadioButton("Male", driver);
				CreateNewApp objCreateNewApp = ObjNewAppPersonalDetail.clickOnAgreeandSave();
				Boolean TickMarkflagPersonal =objCreateNewApp.VerifyTickMarkPersonalDetail("Personal Details");
				if(TickMarkflagPersonal)
				{
					System.out.println("The Personal detail is completed ");
			//		setAssertMessage("The Personal detail is completed", 3);	
				}
		//	Verify.verifyTrue(TickMarkflagPersonal, "The Personal detail is not completed");
			// Employment History 
				NewApp_EmpHistory objNewApp_EmpHistory = objCreateNewApp.clickonEmploymentHistory();
				objNewApp_EmpHistory = objNewApp_EmpHistory.Upload();
				objCreateNewApp = objNewApp_EmpHistory.SubmitClick();
				Boolean TickMarkflagEmp =objCreateNewApp.VerifyTickMarkPersonalDetail("Employment History");
				if(TickMarkflagEmp)
				{
					System.out.println("The Employment History is completed ");
				//	setAssertMessage("The Employment History is completed", 4);	
				}
			//	Verify.verifyTrue(TickMarkflagEmp, "The Employment History is not completed");
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
			//		setAssertMessage("The Refereee is completed", 5);	
				}
			//	Verify.verifyTrue(TickMarkflagRef, "The Referee is not completed");
			// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			// Capacity tab
				Capacity objCapacity = objCreateNewApp.clickonCapacity();
				String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",1);
				String ActualTableName = objCapacity.selectCapacity(5, GMCNumbertxt);
				System.out.println(ActualTableName);
				if(evidence)
				{
					
							objCapacity.ScreenshotofCapacity(key+"Capacityselected");
	                   
				}
				objCreateNewApp = objCapacity.ClickOnSave_Submit();	
				Boolean TickMarkflagCap = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName);	
				if(TickMarkflagCap)
				{
					System.out.println("The Capacity  is completed ");
			//		setAssertMessage("The Capacity is completed", 6);	
				}
			//	Verify.verifyTrue(TickMarkflagCap, "The capacity tab is not completed ");
			//Insurance Tab 
				InsuranceDetails objInsuranceDetails = objCreateNewApp.clickonInsuranceTab();
				String ActualTableName_Ins = objInsuranceDetails.Selectcover();
				objCreateNewApp = objInsuranceDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagIns = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTableName_Ins);	
				if(TickMarkflagIns)
				{
					System.out.println("The Insurance tab is completed ");
				//	setAssertMessage("The Insurance tab is completed", 7);	
				}
			//	Verify.verifyTrue(TickMarkflagIns, "The Insurance tab is not completed ");
				
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
				//	setAssertMessage("The Nationality tab is completed", 8);	
				}
			//	Verify.verifyTrue(TickMarkflagNationality, "The Nationality tab is not completed ");
				
		//Proposed Employment 
			// CreateNewApp objCreateNewApp = new CreateNewApp(driver);
			
				Employment objEmployment = objCreateNewApp.clickonEmploymentTab();
				String ActualTablename_Emp = objEmployment.selectstatement();
				objEmployment =objEmployment.AddPractice(1);
				//objEmployment.AddAnotherLocaloffice("999");
				objCreateNewApp = objEmployment.ClickOnSave_Submit();	
				Boolean TickMarkflagEmployment = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Emp);	
				if(TickMarkflagEmployment)
				{
					System.out.println("The Employment tab is completed ");
				//	setAssertMessage("The Employment tab is completed", 9);	
				}
			//	Verify.verifyTrue(TickMarkflagEmployment, "The Nationality tab is not completed ");
				
							// Trainee
				Trainee objTrainee = objCreateNewApp.ClickOnTraineeTab();
				String ActualTablename_Trainee = objTrainee.selectTrainee();
				objCreateNewApp = objTrainee.ClickOnSave_Submit();	
				Boolean TickMarkflagTrainee = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Trainee);
				if(TickMarkflagTrainee)
				{
					System.out.println("The Trainee tab is completed ");
				//	setAssertMessage("The Trainee tab is completed", 10);	
				}
		//		Verify.verifyTrue(TickMarkflagTrainee, "The Trainee tab is not completed ");
			
			// Professional Qualification 	
				Qualification objQualification = objCreateNewApp.clickonQualification();
				
				objQualification = objQualification.AddProfessionalQualification(1);
				String ActualTablename_Qualification = objQualification.Uploadfile();
				objCreateNewApp = objQualification.ClickOnSave_Submit();	
				Boolean TickMarkflagQualification = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Qualification);
				if(TickMarkflagQualification)
				{
				System.out.println("The Qualification tab is completed ");
			//	setAssertMessage("The Qualification tab is completed", 11);	
				}
			//	Verify.verifyTrue(TickMarkflagQualification, "The Qualification tab is not completed ");
				
			// Professional Details 
				ProfessionalDetails objProfessionalDetails = objCreateNewApp.clickonProfessionaldetails();
				String ActualTablename_ProfessionalDetails = objProfessionalDetails.EnterProfessionalDetails(environment);
				objCreateNewApp = objProfessionalDetails.ClickOnSave_Submit();	
				Boolean TickMarkflagProfessional = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_ProfessionalDetails);
				if(TickMarkflagProfessional)
				{
				System.out.println("The Professional tab is completed ");
			//	setAssertMessage("The Professional tab is completed", 12);	
				}
			//	Verify.verifyTrue(TickMarkflagProfessional, "The Professional tab is not completed ");
				
		// Appraisal History 
			 Appraisal objAppraisal = objCreateNewApp.clickonAppraisalTab();
				String ActualTablename_Appraisal = objAppraisal.EnterApprisalDetails(1); 
				objCreateNewApp = objAppraisal.ClickOnSave_Submit();	
				Boolean TickMarkflagAppraisal = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Appraisal);
				if(TickMarkflagAppraisal)
				{
				System.out.println("The Appraisal tab is completed ");
			//	setAssertMessage("The Appraisal tab is completed", 13);	
				}
		//		Verify.verifyTrue(TickMarkflagAppraisal, "The Appraisal tab is not completed ");
				
		// Training Details
				TrainingDetails objTrainingDetails = objCreateNewApp.clickonTrainingDetailsTab();
				String ActualTablename_TrainingDetails = objTrainingDetails.EnterTrainingDetails(1); 
				objCreateNewApp = objTrainingDetails.ClickOnSave_Submit();
				Boolean TickMarkflagTrainingDetails = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TrainingDetails);
				if(TickMarkflagTrainingDetails)
				{
				System.out.println("The Training Details tab is completed ");
			//	setAssertMessage("The Training Details tab is completed", 14);	
				}
			//	Verify.verifyTrue(TickMarkflagTrainingDetails, "The Training Details is not completed ");
				
		// Communication Skills
				CommunicationSkills objCommunicationSkills = objCreateNewApp.ClickOnCommunicationSkillsDetails();
				String ActualTablename_CommunicationSkills = objCommunicationSkills.EnterCommunicationSkillDetails("Yes"); 
				objCreateNewApp = objCommunicationSkills.ClickOnSave_Submit();
				Boolean TickMarkflagCommunicationSkills = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_CommunicationSkills);
				if(TickMarkflagCommunicationSkills)
				{
				System.out.println("The Communication Skills Details tab is completed ");
		//		setAssertMessage("The Communication Skills tab is completed", 15);	
				}
		//		Verify.verifyTrue(TickMarkflagCommunicationSkills, "The Communication Skills is not completed ");
				
		// Additional Info
				AdditionalInfo objAdditionalInfo = objCreateNewApp.ClickOnAdditionalInfo();
				String ActualTablename_AdditioanlInfo = objAdditionalInfo.EnterAdditionalInfo(); 
				objCreateNewApp = objAdditionalInfo.ClickOnSave_Submit();
				Boolean TickMarkflagAdditioanlInfo = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_AdditioanlInfo);
				if(TickMarkflagAdditioanlInfo)
				{
				System.out.println("The Additional Info tab is completed ");
		//		setAssertMessage("The Additional Info tab is completed", 15);	
				}
			//	Verify.verifyTrue(TickMarkflagAdditioanlInfo, "The Additional Info tab is not completed ");
				
		// Declaration Tab 	
			 	Declaration objDeclaration = objCreateNewApp.ClickOndeclarationTab();
				String ActualTablename_Declaration = objDeclaration.getactualtablename();
				objDeclaration = objDeclaration.Selectdeclaration(2);
				
				objCreateNewApp = objDeclaration.ClickOnSave_Submit();
				Boolean TickMarkflagDecalration = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Declaration);
				if(TickMarkflagDecalration)
				{
				System.out.println("The Declaration tab is completed ");
		//		setAssertMessage("The Declaration tab is completed", 16);	
				}
		//		Verify.verifyTrue(TickMarkflagDecalration, "The Declaration tab is not completed ");
				
		// Declaration Body Tab 
			//CreateNewApp objCreateNewApp = new CreateNewApp(driver);
				DeclarationBody objDeclarationBody = objCreateNewApp.ClickOnDeclarationBody();
				String ActualTablename_DeclarationBody = objDeclarationBody.SelectCorporateDeclaration("No");
				objCreateNewApp = objDeclarationBody.ClickOnSave_Submit();
				Boolean TickMarkflagDecalrationBody = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_DeclarationBody);
				if(TickMarkflagDecalrationBody)
				{
				System.out.println("The Declaration Body tab is completed ");
		//		setAssertMessage("The Declaration Body tab is completed", 17);	
				}
		//		Verify.verifyTrue(TickMarkflagDecalrationBody, "The Declaration Body tab is not completed ");
		// Police Check tab
				PoliceCheck objPoliceCheck = objCreateNewApp.ClickOnPoliceCheck();
				String ActualTablename_PoliceCheck = objPoliceCheck.EnterPolicecheckdetails();
				objCreateNewApp = objPoliceCheck.ClickOnSave_Submit();
				Boolean TickMarkflagPolicecheck = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_PoliceCheck);
				if(TickMarkflagPolicecheck)
				{
				System.out.println("The PoliceCheck tab is completed ");
		//		setAssertMessage("The PoliceCheck tab is completed", 19);	
				}
		//		Verify.verifyTrue(TickMarkflagPolicecheck, "The PoliceCheck tab is not completed ");
		// Team Preference
				TeamPreference objTeamPreference = objCreateNewApp.ClickOnTeamPreference();
				String ActualTablename_TeamPreference = objTeamPreference.EnterTeamPreferencekdetails();
				objCreateNewApp = objTeamPreference.ClickOnSave_Submit();
				Boolean TickMarkflagTeamPreference = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_TeamPreference);
				if(TickMarkflagTeamPreference)
				{
				System.out.println("The Team Preference is completed ");
		//		setAssertMessage("The Team Preference is completed", 20);	
				}
		//		Verify.verifyTrue(TickMarkflagTeamPreference, "The Team Preference is not completed ");
		// HealthClearance
				HealthClearance objHealthClearance = objCreateNewApp.ClickOnHealthClearance();
				String ActualTablename_HealthClerance = objHealthClearance.EnterHealthClerancedetails(1);
				objCreateNewApp = objHealthClearance.ClickOnSave_Submit();
				Boolean TickMarkflagHealthClerance = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_HealthClerance);
				if(TickMarkflagHealthClerance)
				{
				System.out.println("The Health Clerance is completed ");
		//		setAssertMessage("The Health Clerance is completed", 21);	
				}
		//		Verify.verifyTrue(TickMarkflagHealthClerance, "The Health Clerance is not completed ");
		// Undertaking 
			//CreateNewApp objCreateNewApp = new CreateNewApp(getDriver());
				Undertaking objUndertaking = objCreateNewApp.ClickOnUndertaking();
				String ActualTablename_Undertaking = objUndertaking.EnterUndertakingdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox_Type(driver);
				objCreateNewApp = objUndertaking.ClickOnSave_Submit();
				Boolean TickMarkflagUndertaking = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Undertaking);
				if(TickMarkflagUndertaking)
				{
				System.out.println("The Undertaking is completed ");
		//		setAssertMessage("The Undertaking is completed", 22);	
				}
		//		Verify.verifyTrue(TickMarkflagUndertaking, "The Undertaking is not completed ");
		// Submit Application 
				SubmitApplication objSubmitApplication = objCreateNewApp.ClickOnSubmitApp();
				String ActualTablename_Submitapplication = objSubmitApplication.EnterSubmitAppdetails();
				helpers.CommonFunctions.ClickOnAllCheckBox(driver);
			//	objCreateNewApp = objSubmitApplication.ClickOnSave_Submit();
				helpers.CommonFunctions.ClickOnButton_TypeSubmit("Submit Application" , driver);
				Boolean TickMarkflagSubmitApp = objCreateNewApp.VerifyTickMarkWithanotherTableId(ActualTablename_Submitapplication);
				if(TickMarkflagSubmitApp)
				{
				System.out.println("The Submit App is completed ");
		//		setAssertMessage("The Submit App is completed", 23);	
				}
			//	Verify.verifyTrue(TickMarkflagSubmitApp, "The Submit App is not completed ");
				String ApplicationNumberOnparagraph = objCreateNewApp.getApplicationNumberonParagraph("PL");
				if(evidence)
				{
				
						objCreateNewApp.Screenshots(key+"ApplicationNumberOnsubmittab");
	                    
				}
			//	Verify.verifyTrue(ActualSubmittedmessage, "The Application message is different on Submitted tab. ");
				ObjPerformerList = objCreateNewApp.ClickOnSubmittedApp();
				String Application_Number = ObjPerformerList.getApplicationNumberwithstatus("Submitted");
				
				if(evidence)
				{
					
						ObjPerformerList.Screenshots(key+"ApplicationNumberOnperformerlist");
	                 
				}
				System.out.println("The Application Number is " +Application_Number );
			//	setAssertMessage("The Application Number is " +Application_Number, 24);	
				if(ApplicationNumberOnparagraph.equalsIgnoreCase(Application_Number))
				{
					System.out.println("The Application Number is same as on Sumitted action paragraph  " +Application_Number );
					utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber",Application_Number, 1);
			//		setAssertMessage("The Application Number is same as on Sumitted action paragraph " +Application_Number, 23);		
				}
				else
				{
					System.out.println("The Application Number is not same as on Sumitted action paragraph  " +Application_Number );
			//		setAssertMessage("The Application Number is  not same as on Sumitted action paragraph " +Application_Number, 23);		
					
				}
				
		//		Verify.verifyEquals(ApplicationNumberOnparagraph, Application_Number, "The Application Number different on Create app page. ");
			//	Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
						
		
		
	}


	public static boolean Ispresentelement () throws InterruptedException
	{
		boolean ispresent = false;
			try	{
					ispresent = Spinner.isDisplayed();
					System.out.println(ispresent);
					ispresent = true;
				}
		catch(NullPointerException e)
		{
			ispresent = false;			
		}
			catch(Exception e)
			{
				ispresent = false;			
			}
		return ispresent;
	}
	
	public static void PageLoadExternalwait() throws InterruptedException
	{
		boolean Spinnerispresent = false;
		Spinnerispresent  = Ispresentelement();
		System.out.println(Spinnerispresent);
		while(Spinnerispresent)
		{
			Thread.sleep(100);
			Spinnerispresent  = Ispresentelement();
		}
	}
	

}
