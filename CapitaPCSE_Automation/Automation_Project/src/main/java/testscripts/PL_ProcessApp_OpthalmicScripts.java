package testscripts;
import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
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
import pageobjects.PL.PerformerList;
import pageobjects.ProcessPL.*;
import pageobjects.PL.ProcessorPLHelpers;
import reporting.ListenerClass;
import testdata.ConfigurationData;
import verify.Verify;


@Listeners(ListenerClass.class)
public class PL_ProcessApp_OpthalmicScripts extends BaseClass {

	
	//Test case 4063
	// The Scotland, Wales, NI list checks is visible on Third Party check
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity"},priority= 1)
	@Parameters({"browser", "environment", "clientName"})
	public void ThirdPartyComplete_Ophthalmic(String browser ,String environment, String clientName) throws InterruptedException, IOException, AWTException
	{
		setup(browser, environment, clientName, "PL");
		System.out.println(browser);		
		pageobjects.PL.ProcessorPLHelpers.CreateApplication_Ophthalmic_Process(getDriver(), "environment");
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",1);
		System.out.println(ApplicationNumber);
		
	//	String ApplicationNumber = "PL2156";
		setup(browser, environment, clientName, "CRMPL","SUPERUSER");		
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		//String performer_PerformerPortal = ConfigurationData.Process_Ophthalmic;
		String performer_PerformerPortal = ConfigurationData.getRefDataDetails(environment, "Performer_Opthalmic");
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);		
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_Process(primaryEntity);
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			PerformerAppCase onbjPerformerAppCase = ObjAdvancedFindResult.clickOnLinkFromFirstRecord_Process(0,2);
			onbjPerformerAppCase = onbjPerformerAppCase.clickonButton();
			
		//	helpers.CommonFunctions.ClickOnButton("Get case details", getDriver());
			ProcessViewApp objProcessViewApp = onbjPerformerAppCase.clickOnStatus(performer_PerformerPortal, ApplicationNumber);
			PCSECheck objPCSECheck = objProcessViewApp.ClickonPCSECheck();
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			String ApplicationStatus = objPCSECheck.verifyApplicationStatus();
			if(ApplicationStatus.equalsIgnoreCase("Performing Detailed Checks"))
			{
				
			ThirdPartyCheck objThirdPartyCheck = objPCSECheck.clickonThirdParty();
			objThirdPartyCheck = objThirdPartyCheck.FillThirdPartyCheck();
			boolean ScoNIwales = objThirdPartyCheck.verifyselectoptions();
			if(ScoNIwales)
			{
				System.out.println("Scotland, Wales, NI list checks is visible");
				setAssertMessage("Scotland, Wales, NI list checks is visible",1);
			}
			Verify.verifyTrue(ScoNIwales, "Scotland, Wales, NI list checks is not visible ");
			int count = objThirdPartyCheck.verifydateThirdpartycheck();
			if(count == 1)
			{
				System.out.println("The Date complete with today's date ");
				setAssertMessage("The Date complete with today's date",2);
			}
			Verify.verifyNotEquals((count == 1), "The Date complete with not in today's date ");
			boolean Thirdpartycheckflag = objThirdPartyCheck.verifyThirdpartyCheck();
			if(Thirdpartycheckflag)
			{
				System.out.println("Tick box only enabled once all mandatory checks completed for all Visible check uploads for this Applicant");
				setAssertMessage("Tick box only enabled once all mandatory checks completed for all Visible check uploads for this Applicant",3);
			}
			Verify.verifyTrue(ScoNIwales, "Tick box only disabled once all mandatory checks completed for all Visible check uploads for this Applicant ");
			objThirdPartyCheck = objThirdPartyCheck.clickonThirdPartycheck();
			}
			
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
			
		}
	}
	
	// Suraj G : Email Notification is not developed Hence we completed to assign application to Case officer.
	// This method is used to verify the read only mode for process index
	// Assign to Case officer
	// Test cases 4231
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","PL","Sanity"},priority= 1)
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void ReviewerSubmittedApp(String browser ,String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, ParseException, AWTException
	{
		String key = "PL_PA_86";
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
		pageobjects.PL.ProcessorPLHelpers.CreateApplication_Ophthalmic_Process(getDriver(),environment);
		tearDown(browser);
		String ApplicationNumber = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumberSCNIWal",1);
		System.out.println(ApplicationNumber);
		setup(browser, environment, clientName, "CRMPL","PLPERFORMER");		
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "AdvancedFind", "selectPrimaryEntity",1);		
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
			ProcessViewApp objProcessViewApp = new ProcessViewApp(getDriver());
			ProcessIndex objProcessIndex = objProcessViewApp.clickonProcessIndex();
			boolean FlagEnabled = objProcessIndex.verifyReadonlyMode();
			if(!FlagEnabled)
			{
				System.out.println("The process index is in read only mode");
				setAssertMessage("The process index is in read only mode",1);
			}
			
			Verify.verifyTrue(!FlagEnabled, "The process index is in read only mode ");
			PCSECheck objPCSECheck = objProcessIndex.ClickonPCSECheck();
			
			objPCSECheck = objPCSECheck.selectCaseofficerAppComplete();
			String ApplicationStatus = objPCSECheck.verifyApplicationStatus();
			if(evidence)
			{
				objPCSECheck.Applicationstatus(key+"ApplicationStatus");
			}
			if(ApplicationStatus.equalsIgnoreCase("Performing Detailed Checks"))
			{
				System.out.println("Reviewer to assign case officer to application after marking it as Application complete");
				setAssertMessage("Reviewer to assign case officer to application after marking it as Application complete",1);
			}
			Verify.verifyNotEquals(ApplicationStatus.equalsIgnoreCase("Performing Detailed Checks"), "Reviewer is not to assign case officer to application after marking it as Application complete");
			Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");
	
		}
	}
	
		
	}
	
