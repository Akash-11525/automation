package pageobjects.OP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.OPHelpers;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS4PatientEligibility extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(css="button[class='btn btn-info']")
	WebElement enterAddressButton;
	
	
	@FindBy(css="input[id='PatientEligibility_IsAboveSixty']")
	WebElement patientElgAboveSixtyChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsBelowSixteen']")
	WebElement patientElgBelowSixteenChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsBetweenForty']")
	WebElement patientFourtyOrOverChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsStudent']")
	WebElement patientStudentChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsPrisoner']")
	WebElement patientPrisonerChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsDibetese']")
	WebElement patientIsDiabetesChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsGloucoma']")
	WebElement patientIsGloucomaChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsSeen']")
	WebElement patientEveElgIsSeenChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsNotSeen']")
	WebElement patientEveElgIsNotSeenChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsGlaucamaRisk']")
	WebElement patientElgIsGlaucamaRiskChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsBlind']")
	WebElement patientIsBlindChkBox;
	
	@FindBy(css="input[id='txtName']")
	WebElement nameTxtBox;
	
	@FindBy(css="input[id='txtTown']")
	WebElement townTxtBox;
	
	@FindBy(css="input[id='PatientBenefit_BenefitialPersonPatient']")
	WebElement thePatientChkBox;
	
	@FindBy(css="input[id='PatientBenefit_BenefitialPersonPartner']")
	WebElement personPartnerChkBox;
	
	@FindBy(css="input[id='PatientBenefit_ReceivesIncomeSupport']")
	WebElement incomeSupportChkBox;
	
	@FindBy(css="input[id='PatientBenefit_ReceivesUniversalCredit']")
	WebElement universalCreditChkBox;
	
	@FindBy(css="input[id='PatientBenefit_ReceivesPensionCreditGuaranteeCredit']")
	WebElement penCreditGauCreditChkBox;
	
	@FindBy(css="input[id='PatientBenefit_ReceivesIncomebasedJobseekersAllowance']")
	WebElement incomeBasedJobSeekerAllowanceChkBox;
	
	@FindBy(css="input[id='PatientBenefit_ReceivesEmploymentandSupportAllowance']")
	WebElement incomeRelatedtoEmploymentAndRcvChkBox;
	
	@FindBy(css="input[id='PatientBenefit_ReceivesTaxCredit']")
	WebElement taxCreditChkBox;
	
	@FindBy(css="input[id='PatientBenefit.BenefitialPersonName']")
	WebElement BenefitialPersonTxtBox;
	
	@FindBy(css="input[id='txtNiNumber']")
	WebElement benefitialPersonNITxtBox;
	
	@FindBy(css="input[id='txtDateOfBirth']")
	WebElement benefitialPersonDOBTxtBox;
	
	@FindBy(css="input[id='PatientEligibility_IsHCPerson']")
	WebElement hc2CertChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsComplexLense']")
	WebElement preComplexLenseChkBox;
	

	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[id='btnSaveNext']")
	WebElement saveAndNextButton;
	
	//@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(id="txtName")
	WebElement supplimentaryName;
	
	@FindBy(id="txtTown")
	WebElement supplimentaryTown;
	
	@FindBy(css="input[id*='BenefitialPersonName']")
	WebElement benName;
	
	@FindBy(id="txtNiNumber")
	WebElement benNINum;
	
	@FindBy(css="input[id*='txtDateOfBirth']")
	WebElement benDOB;
	
	@FindBy(xpath="a[@id='DivPatientDetails']/i")
	WebElement patDetailsImg;
	
	@FindBy(xpath="//div[@id='sidebar']//a[@class='list-group-item active']")
	WebElement activeSideMenuItem;
	
	@FindBy(id="DivPatientDetailsli")
	WebElement PatDetMenu;
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	@FindBy(css="input[id*='HCNumber']")
	WebElement HCNumberTextBox;
	
	@FindBy(css="textarea[id*='DamageReason']")
	WebElement DamageReasonTxtBox;
	
	@FindBy(css="input[id='VoucherValueReduction']")
	WebElement VoucherValueReductionTxtBox;
	
	
	public OPGOS4PatientEligibility(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void selectProvidedOptions(int columnNumber) throws IOException, InterruptedException
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "PATIENTELEOPTIONS", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		scrolltoElement(driver, ele);
		ele.click();
		Thread.sleep(1000);
		
		}

	}
	
	public void clickOnSaveButton() throws InterruptedException
	{
		scrolltoElement(driver, saveButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Thread.sleep(2000);
	}
	
	public OPGOS4PatientDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		return new OPGOS4PatientDeclaration(driver);
	}
	
	public void enterSupplimentaryDetails(int colNumber)
	{
		String sName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "SupplementaryName", colNumber);
		String sTown = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "SupplementaryTown", colNumber);
		scrolltoElement(driver, supplimentaryName);
		Support.enterDataInTextField(supplimentaryName, sName, wait);
		Support.enterDataInTextField(supplimentaryTown, sTown, wait);
	}
	
	public void enterBeneficiaryDetails(int colNumber)
	{
		String bName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "BeneficiaryName", colNumber);
		String bDOB = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "BeneficiaryDOB", colNumber);
		scrolltoElement(driver, benName);
		String age = CommonFunctions.getDOB(bDOB);
		Support.enterDataInTextField(benName, bName, wait);
		Support.enterDataInTextField(benDOB, age, wait);
	}

	public void PatientEligibilityDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		selectProvidedOptions(colNumber);
		enterPatientEligibilityDetails(colNumber);
		//clickOnSaveandNextButton();
		//Thread.sleep(2000);
		
		//return new OPGOS4PatientDeclaration(driver);
	}
	
	public  List<String> AcutalErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error text-danger']"));
			System.out.println(ActualErrorMesageList);
			ArrMessage = new ArrayList<String>();
			 for (WebElement ActualErrorMesage:ActualErrorMesageList)
			 {
				 scrolltoElement(driver, ActualErrorMesage);
				 String ActErr = ActualErrorMesage.getText();
				 if(!(ActErr.equalsIgnoreCase("")))
				 {
					 ArrMessage.add(ActErr);
				 }
				 
			 }
		}
		catch(NoSuchElementException e)
		{
			System.out.println("No element found " +e);
		}
		return ArrMessage;
		
	}
	
	/*public boolean checkPDtick() throws InterruptedException
	{
		boolean flag = false;
		flag = CommonFunctions.VerifyTickMarkOnTab(patDetailsImg);
		return flag;
	}*/
	
	public OPPatientDetails clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientDetails(driver);
	}
	
	public boolean verifyTickMarkOnPatientEligibility() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatElgMenu);
		return tickMark;
	}
	
	public void enterPatientEligibilityDetails(int colNumber)
	{
		String sName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "SupplementaryName", colNumber);
		String sTown = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "SupplementaryTown", colNumber);
		scrolltoElement(driver, supplimentaryName);
		Support.enterDataInTextField(supplimentaryName, sName, wait);
		Support.enterDataInTextField(supplimentaryTown, sTown, wait);
	
		String bName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "BeneficiaryName", colNumber);
		String bDOB = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "BeneficiaryDOB", colNumber);
		scrolltoElement(driver, benName);
		String age = CommonFunctions.getDOB(bDOB);
		Support.enterDataInTextField(benName, bName, wait);
		Support.enterDataInTextField(benDOB, age, wait);
		
		String certNum = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "CertificateNumber", colNumber);
		String reason = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "PATIENTSELE", "Reason", colNumber);
		scrolltoElement(driver, HCNumberTextBox);
		Support.enterDataInTextField(HCNumberTextBox, certNum, wait);
		
		scrolltoElement(driver, DamageReasonTxtBox);
		Support.enterDataInTextField(DamageReasonTxtBox, reason, wait);
	}
	
	public void enterVoucherValueReduction(String val)
	{
		scrolltoElement(driver, VoucherValueReductionTxtBox);
		Support.enterDataInTextField(VoucherValueReductionTxtBox, val, wait);
	}
	
	public void PatientEligibilityDetailsEntered(String colName,String file, String sheet) throws InterruptedException, IOException
	{
		OPHelpers.selectProvidedOptions(colName, file, sheet, driver);
		enterPatientEligibilityDetails(file,colName);
	}
	
	public void enterPatientEligibilityDetails(String file, String colName)
	{
		String sName = ExcelUtilities.getKeyValueByPosition(file, "PATIENTSELE", "SupplementaryName", colName);
		String sTown = ExcelUtilities.getKeyValueByPosition(file, "PATIENTSELE", "SupplementaryTown", colName);
		scrolltoElement(driver, supplimentaryName);
		Support.enterDataInTextField(supplimentaryName, sName, wait);
		Support.enterDataInTextField(supplimentaryTown, sTown, wait);
		
		
		String bName = ExcelUtilities.getKeyValueByPosition(file, "PATIENTSELE", "BeneficiaryName", colName);
		String bDOB = ExcelUtilities.getKeyValueByPosition(file, "PATIENTSELE", "BeneficiaryDOB", colName);
		scrolltoElement(driver, benName);
		//scrolltoElement(driver, benDOB);
		String age = CommonFunctions.getDOB(bDOB);
		Support.enterDataInTextField(benName, bName, wait);
		Support.enterDataInTextField(benDOB, age, wait);
		
		String certNum = ExcelUtilities.getKeyValueByPosition(file, "PATIENTSELE", "CertificateNumber", colName);
		String reason = ExcelUtilities.getKeyValueByPosition(file, "PATIENTSELE", "Reason", colName);
		scrolltoElement(driver, HCNumberTextBox);
		Support.enterDataInTextField(HCNumberTextBox, certNum, wait);
		
		scrolltoElement(driver, DamageReasonTxtBox);
		Support.enterDataInTextField(DamageReasonTxtBox, reason, wait);
	}

}
