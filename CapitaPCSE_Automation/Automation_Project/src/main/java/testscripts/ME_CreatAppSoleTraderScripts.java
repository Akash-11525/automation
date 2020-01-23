package testscripts;
import java.awt.AWTException;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsersetup.BaseClass;
import pageobjects.LoginScreen;
import pageobjects.ME.AddressManually;
import pageobjects.ME.ApplicationType;
import pageobjects.ME.EnhancedServices;
import pageobjects.ME.FinalDeclaration;
import pageobjects.ME.MarketEntryApplication;
import pageobjects.ME.OpeningHours;
import pageobjects.ME.OrganizationDetailsApplicant;
import pageobjects.ME.OrganizationDetailsSoleTraderPharma;
import pageobjects.ME.PremiseDetails;
import pageobjects.ME.ServiceConfirmations;
import pageobjects.PL.EnterAddressManually;
import reporting.ListenerClass;
import utilities.ExcelUtilities;

@Listeners(ListenerClass.class)

public class ME_CreatAppSoleTraderScripts extends BaseClass {
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "Regression","PCSE","ME","CLONE","SECURITY"})
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void MECreateApp(String browser ,String environment, String clientName ,boolean evidence) throws InterruptedException, IOException, AWTException
	{
		
	/*	String testCaseName=new Object(){}.getClass().getEnclosingMethod().getName();
		String testCaseName1=Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(testCaseName+"====="+testCaseName1);*/
		String scriptKey= "Test1";
		List<String> values= ExcelUtilities.getScriptParameters("ME",scriptKey);
		System.out.println("Test Data value size is: "+values.size());	
		
		String module= values.get(0);
		String strKeys= values.get(1);
		String role= values.get(2);
		String tabName= values.get(3);	
		
	/*	String newTestDataFileName= ExcelUtilities.getExcelParameterByModule(module,"NewTestDataFile");
		String patEleOptionSheet= ExcelUtilities.getExcelParameterByModule(module,"PatientEleOption");
		String patDeclOptionSheet= ExcelUtilities.getExcelParameterByModule(module,"PatDeclOption");
		String perDeclOptionSheet= ExcelUtilities.getExcelParameterByModule(module,"PerDeclOption");*/
		
		setup(browser, environment, clientName, module);
		
		LoginScreen objLoginScreen = new LoginScreen(getDriver());		
	
		MarketEntryApplication ObjMarketEntry  = objLoginScreen.doLogintoME("MEPLAPPLICANT",environment,"MECreateApp");
		
		helpers.CommonFunctions.ClickOnButtonByText("New Application", getDriver());
		
		ApplicationType objApplicationType=new ApplicationType(getDriver());
		
		objApplicationType= objApplicationType.Create_ApplicationType(1);
		
		Boolean TickMarkflagApplication =ObjMarketEntry.VerifyTickMark("Applications");
		if(TickMarkflagApplication)
		{
			System.out.println("Application Type is completed.");
			setAssertMessage("Application Type is completed.", 1);	
		}		
			//objApplicationType.verify_ApplicationType(1);
		PremiseDetails objPremiseDetails=new PremiseDetails(getDriver());
		AddressManually objAddressManually =objPremiseDetails.EnterPremiseDetails(scriptKey);
		
		objAddressManually = objAddressManually.clickonEnterAddressManually();		
		objAddressManually.enterAddressOnAddressPopUp(scriptKey);		
		objPremiseDetails.selectPremisesOptions(scriptKey);
		
		Boolean TickMarkflagPremises =ObjMarketEntry.VerifyTickMark("Premises");
		if(TickMarkflagPremises)
		{
			System.out.println("Premises Details is completed.");
			setAssertMessage("Premises Details is completed.", 2);	
		}
		
		OrganizationDetailsApplicant objOrganizationDetailsApplicant = ObjMarketEntry.clickApplicantRepresentative();
		OrganizationDetailsSoleTraderPharma objOrganizationDetailsSoleTraderPharma = objOrganizationDetailsApplicant.enterApplicantRepresentative(scriptKey);
		
		objOrganizationDetailsSoleTraderPharma= ObjMarketEntry.clickSoleTraderPharma();
		
		ObjMarketEntry=objOrganizationDetailsSoleTraderPharma.enterSoleTraderPharmacist(scriptKey);		
		
		Boolean TickMarkflagOrgDetail =ObjMarketEntry.VerifyTickMark("Organization");
		if(TickMarkflagOrgDetail)
		{
			System.out.println("Organization Details is completed.");
			setAssertMessage("Organization Details is completed.", 3);	
		}
		
		OpeningHours objOpeningHours=new OpeningHours(getDriver());
		objOpeningHours.selectDaysForCoreHoursType(scriptKey);
		objOpeningHours.addOpeningClosingTimePeriodForCoreHours(scriptKey);
		
		objOpeningHours.selectDaysForSupplementaryHoursType(scriptKey);
		objOpeningHours.addOpeningClosingTimePeriodForSupplementaryHours(scriptKey);
		
		objOpeningHours.selectDaysForClosedallDayType(scriptKey);
		
		EnhancedServices objEnhancedServices=objOpeningHours.selectFloorPlanAndInformation(scriptKey);
		
		Boolean TickMarkflagOpeningH =ObjMarketEntry.VerifyTickMark("Opening");
		if(TickMarkflagOpeningH)
		{
			System.out.println("Opening Hours is completed.");
			setAssertMessage("Opening Hours is completed.", 3);	
		}
		
		ServiceConfirmations objServiceConfirmations=objEnhancedServices.selectEnhancedServices(scriptKey);
		
		Boolean TickMarkflagEnhance =ObjMarketEntry.VerifyTickMark("Enhanced");
		if(TickMarkflagEnhance)
		{
			System.out.println("Enhanced Services is completed.");
			setAssertMessage("Enhanced Services is completed.", 3);	
		}
		
		FinalDeclaration objFinalDeclaration=objServiceConfirmations.confirmChangeServiceDeclarations(scriptKey);
		
		Boolean TickMarkflagServiceConfirmations =ObjMarketEntry.VerifyTickMark("Service");
		if(TickMarkflagServiceConfirmations)
		{
			System.out.println("Service Confirmations is completed.");
			setAssertMessage("Service Confirmations is completed.", 3);	
		}
		
		objFinalDeclaration.selectDeclareRegisteredpharmacist(scriptKey);
	}
	
}
