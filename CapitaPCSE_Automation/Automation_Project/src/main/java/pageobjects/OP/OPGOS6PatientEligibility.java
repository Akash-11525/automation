package pageobjects.OP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS6PatientEligibility extends Support {
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
	
	@FindBy(id="PatientBenefit_BenefitialPersonName")
	WebElement benName;
	
	@FindBy(id="txtNiNumber")
	WebElement benNINum;
	
	@FindBy(id="txtDateOfBirth")
	WebElement benDOB;
	
	@FindBy(xpath="a[@id='DivPatientDetails']/i")
	WebElement patDetailsImg;
	
	@FindBy(xpath="//div[@id='sidebar']//a[@class='list-group-item active']")
	WebElement activeSideMenuItem;
	
	@FindBy(id="DivPatientDetailsli")
	WebElement PatDetMenu;
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	@FindBy(tagName="h3")
	WebElement patElgHeader;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	
	public OPGOS6PatientEligibility(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void selectProvidedOptions(int columnNumber) throws IOException, InterruptedException
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS6TESTDATA.xlsx", "PATIENTELEOPTIONS", columnNumber);
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
	
	public void clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(6000);
	}
	
	public void enterSupplimentaryDetails(int colNumber)
	{
		String sName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTSELE", "SupplementaryName", colNumber);
		String sTown = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTSELE", "SupplementaryTown", colNumber);
		scrolltoElement(driver, supplimentaryName);
		Support.enterDataInTextField(supplimentaryName, sName, wait);
		Support.enterDataInTextField(supplimentaryTown, sTown, wait);
	}
	
	public void enterBeneficiaryDetails(int colNumber)
	{
		String bName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTSELE", "BeneficiaryName", colNumber);
		String bDOB = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTSELE", "BeneficiaryDOB", colNumber);
		scrolltoElement(driver, benName);
		String age = CommonFunctions.getDOB(bDOB);
		Support.enterDataInTextField(benName, bName, wait);
		Support.enterDataInTextField(benDOB, age, wait);
	}
	
	
	public OPGOS6PatientDeclaration PatientEligibilityDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		Thread.sleep(5000);
		selectProvidedOptions(colNumber);
		enterSupplimentaryDetails(colNumber);
		enterBeneficiaryDetails(colNumber);
		//enterSignatory();
		clickOnSaveandNextButton();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new OPGOS6PatientDeclaration(driver);
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

	public void GOS1PatientElgErrorSnap(String note) throws InterruptedException, IOException
	{
		
		scrolltoElement(driver, incomeSupportChkBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
	}
	
	public void GOS1PatientElgValErrors(String note) throws InterruptedException, IOException
	{
		
		scrolltoElement(driver, patElgHeader);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}
}
