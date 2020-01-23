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

public class OPGOS5PerformerDeclaration extends Support	 {
	
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
	
	@FindBy(css="select#outcomemodel_FirstVoucherTypeCode")
	WebElement firstVoucherTypeOptions;
	
	@FindBy(id="outcomemodel_IsFirstVoucherComplex")
	WebElement isFirstVoucherComplex;
	
	@FindBy(id="outcomemodel_IsFirstVoucherSupplementsPrism")
	WebElement isFirstVoucherPrism;
	
	@FindBy(id="outcomemodel_IsFirstVoucherSupplementsTint")
	WebElement isFirstVoucherTint;
	
	@FindBy(css="select#outcomemodel_SecondVoucherTypeCode")
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
	
	@FindBy(css="input[value='Save and Next']")
	WebElement saveAndNextButton;
	
	@FindBy(css="li[id='DivPerformerDeclarationli']")
	WebElement perDecMenu;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(css="input[value='Save awaiting Contractor Signatory']")
	WebElement SaveAwaitingContrator;
	
	public OPGOS5PerformerDeclaration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public OPGOS5ContractorDeclaration PeformerDeclarationDetailsEntered(int colNumber) throws IOException, InterruptedException {
		selectProvidedOptions(colNumber);
		enterClaimDeclarationData(colNumber);
		enterAddressManually(colNumber);
		enterSignatoryDetails();
		clickOnSaveandNextButton();
		return new OPGOS5ContractorDeclaration(driver);
	}
	
	public OPGOS5ContractorDeclaration PeformerDeclarationDetailsEnteredWithRetestCode(int colNumber) throws IOException, InterruptedException {
		selectProvidedOptions(colNumber);
		enterClaimDeclarationData(colNumber);
		enterAddressManually(colNumber);
		selectRetestOption("2");
		enterSignatoryDetails();
		clickOnSaveandNextButton();
		return new OPGOS5ContractorDeclaration(driver);
	}
	
	public void enterSignatoryDetails() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);
	}

	private void enterAddressManually(int colNumber) throws InterruptedException {
		Actions actions= new Actions(driver);
        try{
        	actions.moveToElement(addAddressButton).build().perform();
        	scrolltoElement(driver, addAddressButton);
            wait.until(ExpectedConditions.elementToBeClickable(addAddressButton)).click();
            Thread.sleep(2000);
            //commented by Akshay to verify insertion of data
            /*wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));*/
            String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "Addressline1", colNumber);
    		String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "Addressline2", colNumber);
    		String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "Addressline3", colNumber);
    		String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "AddressCity", colNumber);
    		String addresscounty = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "AddressCounty", colNumber);
    		String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "AddressPostCode", colNumber);
            if(AddressModalWindow.isDisplayed())
            {
            	   actions.moveToElement(addressLine1).build().perform();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).sendKeys(addressline1);
                   actions.moveToElement(addressLine2).build().perform();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).sendKeys(addressline2);
                   actions.moveToElement(addressLine3).build().perform();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).sendKeys(addressline3);
                   actions.moveToElement(addressCity).build().perform();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).sendKeys(addresscity);
                   actions.moveToElement(addressCounty).build().perform();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).sendKeys(addresscounty);
                   actions.moveToElement(addressPostcode).build().perform();
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).sendKeys(addresspostcode);
                   actions.moveToElement(addressWindowSaveButton).build().perform();
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

	public void enterClaimDeclarationData(int colNumber) 
	{
		String strLowerofNHSSiteFee= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "LowerofNHSsightFee", colNumber);
		String strDomiciliaryVisitFee= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PERFORMERDECLARATIONS", "NHSDomiciliaryVisitFee", colNumber);

		Support.enterDataInTextField(sightTestFee, strLowerofNHSSiteFee, wait);
		Support.enterDataInTextField(domiciliaryVisitFee, strDomiciliaryVisitFee, wait);
	}

	public void selectProvidedOptions(int colNumber) throws IOException, InterruptedException 
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS5TESTDATA.xlsx", "PERFORMERDECLOPTIONS", colNumber);
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
		Thread.sleep(8000);
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
			ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error text-danger']"));//field-validation-error
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
	
	public OPGOS5PerformerDeclaration enterDetailsAndClickOnAwaitingContractor(int colNumber) throws IOException, InterruptedException {
		selectProvidedOptions(colNumber);
		enterClaimDeclarationData(colNumber);
		enterAddressManually(colNumber);
		enterSignatoryDetails();
		clickOnSaveAwaitingContrator();
		return new OPGOS5PerformerDeclaration(driver);
	}
	
	public void clickOnSaveAwaitingContrator() throws InterruptedException
	{
		scrolltoElement(driver, SaveAwaitingContrator);
		wait.until(ExpectedConditions.elementToBeClickable(SaveAwaitingContrator)).click();
		Support.verifyPageLoading(driver);
		Thread.sleep(2000);
	}
	
	public void GOS5ClaimDetailssnaps(String string) throws InterruptedException, IOException {
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}
	
	public OPGOS5ContractorDeclaration PeformerDeclarationDetailsEntered(String colName,String file,String sheet) throws IOException, InterruptedException {
		OPHelpers.selectProvidedOptions(colName,file,sheet,driver);
		enterClaimDeclarationData(colName);
		OPHelpers.enterAddressManually(driver);
		enterSignatoryDetails();
		clickOnSaveandNextButton();
		Support.PageLoadExternalwait(driver);
		return new OPGOS5ContractorDeclaration(driver);
	}
	
	public void enterClaimDeclarationData(String colName) 
	{
		String strLowerofNHSSiteFee= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PERFORMERDECLARATIONS", "LowerofNHSsightFee", colName);
		String strDomiciliaryVisitFee= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PERFORMERDECLARATIONS", "NHSDomiciliaryVisitFee", colName);
		
		wait.until(ExpectedConditions.elementToBeClickable(sightTestFee)).clear();
		sightTestFee.sendKeys(strLowerofNHSSiteFee);
		
		wait.until(ExpectedConditions.elementToBeClickable(domiciliaryVisitFee)).clear();
		domiciliaryVisitFee.sendKeys(strDomiciliaryVisitFee);
		
	}
	
	public OPGOS5PerformerDeclaration enterDetailsAndClickOnAwaitingContractor(String colName,String file,String sheet) throws IOException, InterruptedException {
		OPHelpers.selectProvidedOptions(colName,file,sheet,driver);
		enterClaimDeclarationData(colName);
		OPHelpers.enterAddressManually(driver);
		enterSignatoryDetails();
		clickOnSaveAwaitingContrator();
		return new OPGOS5PerformerDeclaration(driver);
	}
	
	public OPGOS5ContractorDeclaration PeformerDeclarationDetailsEnteredWithRetestCode(String colName,String file,String sheet,String earlyReTestCode) throws IOException, InterruptedException {
		OPHelpers.selectProvidedOptions(colName,file,sheet,driver);
		enterClaimDeclarationData(colName);
		OPHelpers.enterAddressManually(driver);
		selectRetestOption(earlyReTestCode);
		enterSignatoryDetails();
		clickOnSaveandNextButton();
		return new OPGOS5ContractorDeclaration(driver);
	}
	
}
