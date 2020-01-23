package pageobjects.OP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.OPHelpers;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class OPGOS6PerformerDeclaration extends Support	 {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="outcomemodel_PatientReferred")
	WebElement isPatientReferred;
	
	@FindBy(id="outcomemodel_PrescriptionIssuedorChanged")
	WebElement isprescriptionIssuedorChanged;
	
	@FindBy(id="outcomemodel.PrescriptionNotRequired")
	WebElement isStatementIssued;
	
	@FindBy(id="outcomemodel_PrescriptionUnchanged")
	WebElement isPrescriptionUnchanged;
	
	@FindBy(id="outcomemodel_VoucherIssued")
	WebElement isVoucherIssued;
	
	@FindBy(id="performerdeclaration_firstPatientAtAddress")
	WebElement isFirstPatientAtAddress;
	
	@FindBy(id="performerdeclaration_secondPatientAtAddress")
	WebElement isSecondPatientAtAddress;
	
	@FindBy(id="performerdeclaration_furtherPatientAtAddress")
	WebElement isFurtherPatientAtAddress;
	
	@FindBy(css="select[id='outcomemodel_FirstVoucherTypeCode']")
	WebElement firstVoucherTypeOptions;
	
	@FindBy(id="outcomemodel_IsFirstVoucherComplex")
	WebElement isFirstVoucherComplex;
	
	@FindBy(id="outcomemodel_IsFirstVoucherSupplementsPrism")
	WebElement isFirstVoucherPrism;
	
	@FindBy(id="outcomemodel_IsFirstVoucherSupplementsTint")
	WebElement isFirstVoucherTint;
	
	@FindBy(css="select[id='outcomemodel_SecondVoucherTypeCode']")
	WebElement secondVoucherTypeOptions;

	@FindBy(id="outcomemodel_IsSecondVoucherComplex")
	WebElement isSecondVoucherComplex;
	
	@FindBy(id="outcomemodel_IsSecondVoucherSupplementsPrism")
	WebElement isSecondVoucherPrism;
	
	@FindBy(id="outcomemodel_IsSecondVoucherSupplementsTint")
	WebElement isSecondVoucherTint;
	
	@FindBy(css="select[id='outcomemodel_RetestCode']")
	WebElement retestCodeOptions;
	
	@FindBy(id="txtsightTestFee")
	WebElement sightTestFee;
	
	@FindBy(id="txtdomiciliaryVisitFee")
	WebElement domiciliaryVisitFee;
	
	@FindBy(id="txtPostcode")
	WebElement txtPostcode;
	
	@FindBy(id="BtnsearchRecAdd")
	WebElement searchPostalCodeButton;
	
	@FindBy(css="button[class*='btn btn-info']")
	WebElement addAddressButton;
	
	@FindBy(css="div[class='modal fade in']")
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
	
	@FindBy(css="button[class='btn btn-default']")
	WebElement addressWindowCloseButton;
	
	@FindBy(css="input[name='btnBack']")
	WebElement previousButton;
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
	@FindBy(id="btnSaveNext")
	WebElement saveAndNextButton;
	
	@FindBy(css="li[id='DivPerformerDeclarationli']")
	WebElement perDecMenu;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(css="input[value='Save awaiting Contractor Signatory']")
	WebElement SaveAwaitingContrator;
	
	public OPGOS6PerformerDeclaration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public OPGOS6ContractorDeclaration PeformerDeclarationDetailsEntered(int colNumber) throws IOException, InterruptedException {
		selectProvidedOptions(colNumber);
		//enterClaimDeclarationData(colNumber);
		enterAddressManually(colNumber);
		clickOnSaveandNextButton();
		return new OPGOS6ContractorDeclaration(driver);
	}
	
	public OPGOS6ContractorDeclaration PeformerDeclarationDetailsEnteredWithRetestCode(int colNumber) throws IOException, InterruptedException {
		selectProvidedOptions(colNumber);
		enterClaimDeclarationData(colNumber);
		enterAddressManually(colNumber);
		selectRetestOption("2");
		clickOnSaveandNextButton();
		return new OPGOS6ContractorDeclaration(driver);
	}
	
	public void enterSignatoryDetails() throws InterruptedException
	{
		//enterSignatory(signatureCanvasbox, driver);
		OPHelpers.enterPatDecSignatoryDetails(driver);
	}
	
	public void enterAddressManually(int colNumber) throws InterruptedException {
        try{
        	Actions focus= new Actions(driver);
        	if(addAddressButton.isEnabled()){
            	scrolltoElement(driver, addAddressButton);
                wait.until(ExpectedConditions.elementToBeClickable(addAddressButton)).click();
                //commented by Akshay to verify insertion of data
                /*wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));*/
                String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Addressline1", colNumber);
        		String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Addressline2", colNumber);
        		String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Addressline3", colNumber);
        		String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "AddressCity", colNumber);
        		String addresscounty = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "AddressCounty", colNumber);
        		String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "AddressPostCode", colNumber);
                if(AddressModalWindow.isDisplayed())
                {		
                	   focus.moveToElement(addressLine1).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).clear();
                       wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).sendKeys(addressline1);
                       focus.moveToElement(addressLine2).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).clear();
                       wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).sendKeys(addressline2);
                       focus.moveToElement(addressLine3).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).clear();
                       wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).sendKeys(addressline3);
                       focus.moveToElement(addressCity).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressCity)).clear();
                       wait.until(ExpectedConditions.elementToBeClickable(addressCity)).sendKeys(addresscity);
                       Thread.sleep(500);
                       focus.moveToElement(addressCounty).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).clear();
                       wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).sendKeys(addresscounty);
                       focus.moveToElement(addressPostcode).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).clear();
                       wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).sendKeys(addresspostcode);
                       focus.moveToElement(addressWindowSaveButton).build().perform();
                       wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
                       Thread.sleep(2000);
                     // helpers.CommonFunctions.ClickOnButton("Save", driver);
                      
                }
        	}

     }
     catch(NoSuchElementException e)
     {
            System.out.println("The element is not found on GMC Address Pop up box" +e);
     }
		
	}

	public void enterClaimDeclarationData(int colNumber) 
	{
		String strLowerofNHSSiteFee= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "LowerofNHSsightFee", colNumber);
		String strDomiciliaryVisitFee= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "NHSDomiciliaryVisitFee", colNumber);
		
		Support.enterDataInTextField(sightTestFee, strLowerofNHSSiteFee, wait);
		Support.enterDataInTextField(domiciliaryVisitFee, strDomiciliaryVisitFee, wait);
	}

	public void selectProvidedOptions(int colNumber) throws IOException, InterruptedException 
	{
		Thread.sleep(5000);
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS6TESTDATA.xlsx", "PERFORMERDECLOPTIONS", colNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		Thread.sleep(2000);
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
	
	public OPGOS6ContractorDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
				
		Thread.sleep(2000);
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(18000);
		return new OPGOS6ContractorDeclaration(driver);
	}
	
	
	
	public OPPatientDetails clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientDetails(driver);
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
	
	public boolean verifyTickMarkOnPatientEligibility() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(perDecMenu);
		return tickMark;
	}

	public void selectRetestOption(String value)
	{
		CommonFunctions.selectOptionFromDropDown(retestCodeOptions, value);
	}
	
	public void selectFirstVoucherType(String type)
	{
		wait.until(ExpectedConditions.elementToBeClickable(firstVoucherTypeOptions)).click();
		CommonFunctions.selectOptionFromDropDown(firstVoucherTypeOptions, type);			
	}
	
	public void selectSecondVoucherType(String type)
	{
		wait.until(ExpectedConditions.elementToBeClickable(secondVoucherTypeOptions)).click();
		CommonFunctions.selectOptionFromDropDown(secondVoucherTypeOptions, type);			
	}
	
	public void clickOnSaveAwaitingContrator() throws InterruptedException
	{
		scrolltoElement(driver, SaveAwaitingContrator);
		wait.until(ExpectedConditions.elementToBeClickable(SaveAwaitingContrator)).click();
		Support.verifyPageLoading(driver);
		Thread.sleep(2000);
	}
	
	public void GOS6PerformerDeclarionSnap(String note) throws InterruptedException, IOException
	{
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
	}

}
