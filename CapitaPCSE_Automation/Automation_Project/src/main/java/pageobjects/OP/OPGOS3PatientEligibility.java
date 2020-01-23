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
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS3PatientEligibility extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	

	// New webelment for GOS3 Eligibility form
	@FindBy(css="input[id*='SupplierDeclaration_IsGlasses']")
	WebElement patientWishGlassesChkBox;
	
	@FindBy(css="input[id*='SupplierDeclaration_IsContactLense']")
	WebElement patientWishContactLenseChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsBelowSixteen']")
	WebElement patientElgBelowSixteenChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsStudent']")
	WebElement patientStudentChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsPrisoner']")
	WebElement patientPrisonerChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsSeen']")
	WebElement patientEveElgIsSeenChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsNotSeen']")
	WebElement patientEveElgIsNotSeenChkBox;
	
	@FindBy(css="input[id='PatientBenefit_BenefitialPersonPatient']")
	WebElement thePatientChkBox;
	
	@FindBy(css="input[id='PatientBenefit_BenefitialPersonPartner']")
	WebElement personPartnerChkBox;
	
	@FindBy(css="input[id='PatientBenefit_BenefitialPersonName']")
	WebElement BenefitialPersonTxtBox;
	
	@FindBy(css="input[id='PatientDeclaration_DeclarationName']")
	WebElement PatientDeclarationName;
	
	@FindBy(css="input[id='txtNiNumber']")
	WebElement benefitialPersonNITxtBox;
	
	@FindBy(css="input[id='txtDateOfBirth']")
	WebElement benefitialPersonDOBTxtBox;
	
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
	
	@FindBy(css="input[id='HCNumber']")
	WebElement hc2CertChkBox;
	
	@FindBy(css="input[id='PatientEligibility_IsHC3Person']")
	WebElement hc3CertChkBox;
	
	@FindBy(css="input[id='HCNumber']")
	WebElement certificateNumberTextBox;
	
	@FindBy(css="input[id='HC3VoucherValue']")
	WebElement hc3VoucherValue;
	
	@FindBy(css="input[id='PatientEligibility_IsComplexLense']")
	WebElement preComplexLenseChkBox;
	
	@FindBy(css="input[id='PatientDeclaration_IsPatient']")
	WebElement patientDclPatientChkBox;
	
	@FindBy(css="input[id='PatientDeclaration_IsPatientParent']")
	WebElement patientDclParentChkBox;
	
	@FindBy(css="input[id='PatientDeclaration_IsPatientCarer']")
	WebElement patientDclCarerChkBox;
	
	@FindBy(css="input[id='txtChequeBookNumber']")
	WebElement chequeBookNumberTxtBox;
	
	@FindBy(css="input[id='PatientDeclaration_IsWitnessed']")
	WebElement patientDclIsWitnessedChkBox;
	
	@FindBy(css="input[id='PatientDeclaration_DeclarationName']")
	WebElement patientDclNameTxtBox;
	
	@FindBy(css="button[data-book-id='txtAddress']")
	WebElement patientDclAddressButton;
	
	@FindBy(css= "input[id='txtVoucherCode']")
	WebElement voucherCodeTxtBox;
	
	@FindBy(css= "input[id='txtAuthorisationCode']")
	WebElement authorisationCodeTxtBox;
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[value='Save and Next']")
	WebElement saveAndNextButton;
	
	//@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(css="input[id='txtName']")
	WebElement partnerName;
	
	@FindBy(css="input[id='txtTown']")
	WebElement partnerTown;
	
	@FindBy(xpath="//span[contains(@class, 'field-validation-error')]")
	WebElement valError;
	
	@FindBy(css="button[class*='btn btn-info']")
	WebElement enterAddressButton;
@FindBy(css="div[id='AddressModal']")
	WebElement AddressModalWindow;
	
	@FindBy(css="input[id='AddressLine1']")
	WebElement addressLine1;
	
	@FindBy(css="input[id='AddressLine2']")
	WebElement addressLine2;
	
	@FindBy(css="input[id='AddressLine3']")
	WebElement addressLine3;
	
	@FindBy(css="input[id='City']")
	WebElement addressCity;
	
	@FindBy(css="input[id='County']")
	WebElement addressCounty;
	
	@FindBy(css="input[id='Postcode']")
	WebElement addressPostcode;
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement addressWindowSaveButton;
	
	@FindBy(xpath="//div[@id='DivContainer']/form/div[25]/div/strong")
	WebElement claimNumberEle;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	
	
	
	public OPGOS3PatientEligibility(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void selectProvidedOptions(int columnNumber) throws IOException, InterruptedException
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "PATIENTELEOPTIONS", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		wait.until(ExpectedConditions.elementToBeClickable(ele));
		scrolltoElement(driver, ele);
		if(!(ele.isSelected())){
			ele.click();
		}
		//ele.click();
		Thread.sleep(1000);
		
		}

	}
	
	
	
	public void clickOnSaveButton() throws InterruptedException
	{
		scrolltoElement(driver, saveButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Thread.sleep(2000);
	}
	
	public OPGOS3SupplierDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		//scrolltoElement(driver, signatureCanvasbox);		
		//enterSignatory(signatureCanvasbox, driver);
		OPHelpers.enterPatDecSignatoryDetails(driver);
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		//Thread.sleep(9000); Commented by Akshay to optimize execution
		return new OPGOS3SupplierDeclaration(driver);
	}
	
	public void enterSupplimentaryDetails(int colNumber)
	{
		String sName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "EstablishmentName", colNumber);
		String sTown = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "EstablishmentTown", colNumber);
		scrolltoElement(driver, partnerName);
		Support.enterDataInTextField(partnerName, sName, wait);
		Support.enterDataInTextField(partnerTown, sTown, wait);
	}
	
	public void enterBeneficiaryDetails(int colNumber)
	{
		try{
				String bName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "BeneficiaryName", colNumber);
				String bDOB = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "BeneficiaryDOB", colNumber);
				String declarationName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "DeclarationName", colNumber);
				scrolltoElement(driver, BenefitialPersonTxtBox);
				Support.enterDataInTextField(BenefitialPersonTxtBox, bName, wait);
				
				String age = CommonFunctions.getDOB(bDOB);
				Support.enterDataInTextField(benefitialPersonDOBTxtBox, age, wait);
				
				scrolltoElement(driver, PatientDeclarationName);
				Support.enterDataInTextField(PatientDeclarationName, declarationName, wait);
				enterAddressManually(1);
			}
		catch(Exception e)
	     {
	            System.out.println("Some Error occured while entering details on patinent eligibility screen");
	            e.printStackTrace();
	     }
	}
	
	
	public OPGOS3SupplierDeclaration PatientEligibilityDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		selectProvidedOptions(colNumber);
		enterSupplimentaryDetails(colNumber);
		enterBeneficiaryDetails(colNumber);
		clickOnSaveandNextButton();
		//Thread.sleep(9000); Commented by Akshay to optimize execution
		
		return new OPGOS3SupplierDeclaration(driver);
	}
	
	public  List<String> AcutalErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//span[contains(@class, 'field-validation-error')]"));
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
	
	/*public boolean verifyTickMarkOnPatientEligibility() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatElgMenu);
		return tickMark;
	}*/
	
	public void enterHC2CertificateDetails(String numb)
	{
		scrolltoElement(driver, hc2CertChkBox );
		Support.enterDataInTextField(hc2CertChkBox, numb, wait);
	}
	
	public void enterHC3CertificateDetails(String numb)
	{
		scrolltoElement(driver, hc3CertChkBox );
		if(!(hc3CertChkBox.isSelected())){
			wait.until(ExpectedConditions.elementToBeClickable(hc3CertChkBox)).click();
		}
		/*wait.until(ExpectedConditions.elementToBeClickable(hc3CertChkBox)).click();*/
		Support.enterDataInTextField(certificateNumberTextBox, numb, wait);
	}
	
	public void enterHCVoucherValue(String value)
	{
		scrolltoElement(driver, hc3VoucherValue);
		Support.enterDataInTextField(hc3VoucherValue, value, wait);
	}
	
	public void GOS3ErrorsnapOnPatElgOnPatEntitlement(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, patientWishGlassesChkBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void GOS3ErrorsnapOnPatElgOnWitnessSignFlag(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, patientDclIsWitnessedChkBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public List<String> getErrors()
	{
		
		List<String> ActErrors = AcutalErrormessage();
		return ActErrors;
	}
	
	public void GOS3ErrorsnapOnPatElgValidationError(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, valError);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	public void GOS3_NavigatedToPatEligibility(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, patientWishGlassesChkBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void selectNotSeenChekBox()
	{
		scrolltoElement(driver, patientEveElgIsNotSeenChkBox);
		patientEveElgIsNotSeenChkBox.click();
	}
	
	public void GOS3_SeenNotSeenCheckboxSnap(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, patientEveElgIsSeenChkBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void enterAddressManually(int colNumber) throws InterruptedException
	{
        try{
            wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
            String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline1", colNumber);
    		String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline2", colNumber);
    		String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline3", colNumber);
    		String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressCity", colNumber);
    		String addresscounty = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressCounty", colNumber);
    		String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressPostCode", colNumber);
            if(AddressModalWindow.isDisplayed())
            {
            	Support.enterDataInTextField(addressLine1,addressline1,wait);
                Support.enterDataInTextField(addressLine2,addressline2,wait);
                Support.enterDataInTextField(addressLine3,addressline3,wait);
                Support.enterDataInTextField(addressCity,addresscity,wait);
                Support.enterDataInTextField(addressCounty,addresscounty,wait);
                Support.enterDataInTextField(addressPostcode,addresspostcode,wait);
                wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
                Thread.sleep(2000);
                 // helpers.CommonFunctions.ClickOnButton("Save", driver);
                  
            }
     }
     catch(NoSuchElementException e)
     {
            System.out.println("The element is not found on GMC Address Pop up box" +e);
     }
     
    

	}
	
	public String getClaimNumber(String key) throws InterruptedException
	{
		Thread.sleep(1500);
		scrolltoElement(driver, claimNumberEle);
		String clmNo = claimNumberEle.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}

}
