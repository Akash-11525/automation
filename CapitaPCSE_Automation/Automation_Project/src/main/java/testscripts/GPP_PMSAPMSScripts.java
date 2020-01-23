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
import pageobjects.GPP.PMSAPMS.*;
import reporting.ListenerClass;
import verify.Verify;


@Listeners(ListenerClass.class)
public class GPP_PMSAPMSScripts extends BaseClass{
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","GPP","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PaymentInstructionDetails(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		List<String> keys = Arrays.asList("GPP_PMS/APMS_15", "GPP_PMS/APMS_06");
		setup(browser, environment, clientName, "GPP");
		GPPScreen ObjGPPScreen  = new GPPScreen(getDriver());	
		PMSinstructionScreen ObjPMSinstructionScreen = ObjGPPScreen.ClickonTab("PMS/APMS");
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.Enterpaymentinstructiondata();
		helpers.CommonFunctions.ClickOnButton("Search", getDriver());
		List<String> ExpectedResultLabel = ObjPMSinstructionScreen.ExpectedResultLabel("ResultTable",1);
		System.out.println(ExpectedResultLabel);
		List<String> AcutalResultLabel =  ObjPMSinstructionScreen.ActualResultLabel("ResultTable",1);
		System.out.println(AcutalResultLabel);
		if(evidence)
		{
			for(String key: keys)
            {
				ObjPMSinstructionScreen.PMSScreenshots(key+"ResultTablelabel");
            }

		}
		int count = ObjPMSinstructionScreen.verifyResultLabel(ExpectedResultLabel ,AcutalResultLabel );
		if(count == 0)
		{
			System.out.println("The All result table label is appear correctly ");
			setAssertMessage("The All result table label is appear correctly" ,1);	
		}
		Verify.verifyTrue((count == 0), "The All result table label is appear incorrectly");
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.clickonView();
		List<String> AcutalPaymentwindowLabel =  ObjPMSinstructionScreen.ActualPaymentwindowLabel("ResultTable",2);
		System.out.println(AcutalPaymentwindowLabel);
		List<String> ExpectedPaymentLabel = ObjPMSinstructionScreen.ExpectedResultLabel("ResultTable",2);
		System.out.println(ExpectedPaymentLabel);
		int count1 = ObjPMSinstructionScreen.verifyResultLabel(ExpectedPaymentLabel ,AcutalPaymentwindowLabel );
		if(evidence)
		{
			for(String key: keys)
            {
				ObjPMSinstructionScreen.PMSScreenshots(key+"Paymentwindowlabel");
            }

		}
		if(count1 == 0)
		{
			System.out.println("The All Paymentwindow label is appear correctly ");
			setAssertMessage("The All Paymentwindow label is appear correctly" ,1);	
		}
		Verify.verifyTrue((count1 == 0), "The All Paymentwindow label is appear incorrectly");
		
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.clickonclosewindow();
		Boolean Paymentwindow = ObjPMSinstructionScreen.verifypaymentwindow();
		if(!Paymentwindow)
		{
			System.out.println("The Payment window is closed after clicking on close button ");
			setAssertMessage("The Payment window is closed after clicking on close button" ,1);		
		}
		Verify.verifyTrue((!Paymentwindow), "The Payment window is opened after clicking on close button");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","GPP","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void ChangeAnnualBaseline(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		List<String> keys = Arrays.asList("GPP_PMS/APMS_17", "GPP_PMS/APMS_43");
		setup(browser, environment, clientName, "GPP");
		GPPScreen ObjGPPScreen  = new GPPScreen(getDriver());	
		PMSinstructionScreen ObjPMSinstructionScreen = ObjGPPScreen.ClickonTab("PMS/APMS");
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.selectBaseline();
		BaselineEntryScreen objBaselineEntryScreen = ObjPMSinstructionScreen.clickonAnnualBaseline();
		List<String> AcutalEntryScreenLabel =  objBaselineEntryScreen.ActualBaselineEntryLabel();
		System.out.println(AcutalEntryScreenLabel);
		List<String> ExpectedEntryScreenLabel = objBaselineEntryScreen.ExpectedResultLabel("ResultTable",3);
		System.out.println(ExpectedEntryScreenLabel);
		if(evidence)
		{
			for(String key: keys)
            {
				objBaselineEntryScreen.BaselineScreenshots(key+"Baselinelabel");
            }

		}
		int count = objBaselineEntryScreen.verifyResultLabel(ExpectedEntryScreenLabel ,AcutalEntryScreenLabel );
		if(count == 0)
		{
			System.out.println("The All base line label is appear correctly ");
			setAssertMessage("The All base line label is appear correctly " ,1);	
		}
		Verify.verifyTrue((count == 0), "The All base line label is appear incorrectly ");
		boolean financialYear = objBaselineEntryScreen.verifyfinancialyear();
		if(evidence)
		{
			for(String key: keys)
            {
				objBaselineEntryScreen.BaselineScreenshots(key+"Financialyear");
            }

		}
		if(financialYear)
		{
			System.out.println("The Correct Financial year is displayed on Entry screen page ");
			setAssertMessage("The Correct Financial year is displayed on Entry screen page " ,1);
		}
		Verify.verifyTrue(financialYear, "The incorrect Financial year is displayed on Entry screen page");
		helpers.CommonFunctions.ClickOnButton("Upload CSV File", getDriver());
		boolean CSVmessage = objBaselineEntryScreen.verifyCSVmessage();
		if(evidence)
		{
			for(String key: keys)
            {
				objBaselineEntryScreen.BaselineScreenshots(key+"CSVMessage");
            }

		}
		if(CSVmessage)
		{
			System.out.println("The CSV message is appered correctly");
			setAssertMessage("The CSV message is appered correctly" ,1);
		}
		Verify.verifyTrue(CSVmessage, "The CSV message is appered incorrectly");
		objBaselineEntryScreen = objBaselineEntryScreen.clickonCancelCSVPopUp();
		boolean CSVpopup = objBaselineEntryScreen.verifyCSVPopUp();
		if(!CSVpopup)
		{
			System.out.println("The CSV pop up is not displayed after clicking on Cancel");
			setAssertMessage("The CSV pop up is not displayed after clicking on Cancel" ,1);
		}
		Verify.verifyTrue(CSVmessage, "The CSV pop up is displayed after clicking on Cancel");
		helpers.CommonFunctions.ClickOnButton("Upload CSV File", getDriver());
		objBaselineEntryScreen = objBaselineEntryScreen.uploadCSVfile();
		objBaselineEntryScreen = objBaselineEntryScreen.clickonSubmitCSVPopUp();
		boolean CSVupload = objBaselineEntryScreen.verifyCSVupload();
		if(evidence)
		{
			for(String key: keys)
            {
				objBaselineEntryScreen.BaselineScreenshots(key+"CSVfileupload");
            }

		}
		if(CSVupload)
		{
			System.out.println("The CSV file upload sucessfully");
			setAssertMessage("The CSV file upload sucessfully" ,1);	
		}
		Verify.verifyTrue(CSVupload, "The CSV file is not upload sucessfully");
		helpers.CommonFunctions.ClickOnButton("Close", getDriver());
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","GPP","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void InstructionType(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "GPP_PMS/APMS_37";
		setup(browser, environment, clientName, "GPP");
		GPPScreen ObjGPPScreen  = new GPPScreen(getDriver());	
		PMSinstructionScreen ObjPMSinstructionScreen = ObjGPPScreen.ClickonTab("PMS/APMS");
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.selectBaseline();
		BaselineEntryScreen objBaselineEntryScreen = ObjPMSinstructionScreen.clickonAnnualBaseline();
		List<String> Actualalloption =  objBaselineEntryScreen.getalloptioninstruction();
		System.out.println(Actualalloption);
		List<String> ExpectedActualoptions = objBaselineEntryScreen.ExpectedResultLabel("InstructionOptions",1);
		System.out.println(ExpectedActualoptions);
		int count = objBaselineEntryScreen.verifyResultLabel(ExpectedActualoptions ,Actualalloption );
		if(count == 0)
		{
			System.out.println("The instruction type is containing the expected options ");
			setAssertMessage("The instruction type is containing the expected options " ,1);	
		}
		Verify.verifyTrue((count == 0), "The instruction type is containing the incorrect options  ");
		objBaselineEntryScreen =objBaselineEntryScreen.selectinstructionType("Flat Amount");
		
		boolean disabled = objBaselineEntryScreen.verifyNoofpatient_Custom();
		if(disabled)
		{
			System.out.println("The patient no and Custom field as disabled after clicking on flat amount");
			setAssertMessage("The patient no and Custom field as disabled after clicking on flat amount " ,1);
		}
		Verify.verifyTrue((disabled), "The patient no and Custom field as enabled after clicking on flat amount");
		boolean disabledAnnual = objBaselineEntryScreen.verifyannualContract();
		if(!disabledAnnual)
		{
			System.out.println("The Annual contract field as enabled after clicking on flat amount");
			setAssertMessage("The Annual contract field as enabled after clicking on flat amount " ,1);
		}
		Verify.verifyTrue((!disabledAnnual), "The Annual contract field as disabled after clicking on flat amount ");
		if(evidence)
		{
			objBaselineEntryScreen.BaselineScreenshots(key+"FlatamountSelect");
		}
		objBaselineEntryScreen =objBaselineEntryScreen.selectinstructionType("Custom");
		boolean disabled1 = objBaselineEntryScreen.verifyNoofpatient_Custom();
		if(!disabled1)
		{
			System.out.println("The patient no and Custom field as enabled after clicking on Custom");
			setAssertMessage("The patient no and Custom field as enabled after clicking on Custom" ,1);
		}
		Verify.verifyTrue((!disabled1), "The patient no and Custom field as disabled after clicking on Custom");
		boolean disabledAnnual1 = objBaselineEntryScreen.verifyannualContract();
		if(disabledAnnual1)
		{
			System.out.println("The Annual contract field as disabled after clicking on Custom");
			setAssertMessage("The Annual contract field as disabled after clicking on Custom" ,1);
		}
		Verify.verifyTrue((disabledAnnual1), "The Annual contract field as enabled after clicking on Custom");
		if(evidence)
		{
			objBaselineEntryScreen.BaselineScreenshots(key+"CustomSelect");
		}
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
	}
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","GPP","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void AddedBaselineentry(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "GPP_PMS/APMS_48";
		setup(browser, environment, clientName, "GPP");
		GPPScreen ObjGPPScreen  = new GPPScreen(getDriver());	
		PMSinstructionScreen ObjPMSinstructionScreen = ObjGPPScreen.ClickonTab("PMS/APMS");
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.selectBaseline();
		BaselineEntryScreen objBaselineEntryScreen = ObjPMSinstructionScreen.clickonAnnualBaseline();
		objBaselineEntryScreen = objBaselineEntryScreen.FillBaselineentry();
		helpers.CommonFunctions.ClickOnButton("Save", getDriver());
		boolean Savemessage = objBaselineEntryScreen.verifySavemessage();
		if(evidence)
		{
			objBaselineEntryScreen.BaselineScreenshots(key+"SaveMessage");
		}
		if(Savemessage)
		{
			System.out.println("The Correct Save message is appear on save window");
			setAssertMessage("The Correct Save message is appear on save window" ,1);
		}
		Verify.verifyTrue(Savemessage, "The incorrect Save message is appear on save window");
		objBaselineEntryScreen = objBaselineEntryScreen.clickoncancelonSavewindow();
		boolean Savewindow = objBaselineEntryScreen.verifysavewindow();
		if(!Savewindow)
		{
			System.out.println("The Save window is closed after clicking on cancel");
			setAssertMessage("The Save window is closed after clicking on cancel" ,1);
		}
		Verify.verifyTrue(Savemessage, "The Save window is not closed after clicking on cancel");
		helpers.CommonFunctions.ClickOnButton("Save", getDriver());
		ObjPMSinstructionScreen =objBaselineEntryScreen.clickonconfirm();
		ObjPMSinstructionScreen = ObjPMSinstructionScreen.Enterpaymentinstructiondata();
		helpers.CommonFunctions.ClickOnButton("Search", getDriver());
		String AnnualContractValue = ObjPMSinstructionScreen.getAnnualContractValue();
		if(evidence)
		{
			ObjPMSinstructionScreen.PMSScreenshots(key+"UpdatedContractvalue");
		}
		boolean AnnualContractorValue = ObjPMSinstructionScreen.verifyannualContractvalue(AnnualContractValue);
		if(AnnualContractorValue)
		{
			System.out.println("The Correct data is filled after Baseline entry");
			setAssertMessage("The Correct data is filled after Baseline entry" ,1);
		}
		Verify.verifyTrue(AnnualContractorValue, "The incorrect data is filled after Baseline entry");
		Assert.assertFalse(Verify.verifyFailure(), "There were some Failures as shown above.");	
		
		
	}
	
/*	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = { "PCSE","GPP","Regression"} )
	@Parameters({"browser", "environment", "clientName","evidence"})
	public void PaymentDue_Calculation(String browser ,String environment, String clientName , boolean evidence) throws InterruptedException, IOException, AWTException
	{
		String key = "GPP_PMS/APMS_48";
		setup(browser, environment, clientName, "CRMGPP");
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		int colNum = 1;
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
		String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
		String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
		String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
		String ValueForFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx", "AdvancedFind", "selectValueForField", 1);
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_GMP(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				OrgPaymentdetails objOrgPaymentdetails = ObjAdvancedFindResult.clickonorgenizationpaymentdetails();
			}
			}
	}*/
	
}
