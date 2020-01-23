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

public class OPGOS4PatientDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
/*	@FindBy(css="button[value='Draft']")
	WebElement saveButton;
	
	@FindBy(css="button[value='Save']")
	WebElement saveAndNextButton;*/
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[id='btnSaveNext']")
	WebElement saveAndNextButton;
	
//	@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(xpath="//div[@id='sidebar']//a[@class='list-group-item active']")
	WebElement activeSideMenuItem;
	
	@FindBy(id="DivPatientDeclarationli")
	WebElement PatDeclMenu;
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	@FindBy(id="DeclarationName")
	WebElement declarationNameTxt;
	
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
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	public OPGOS4PatientDeclaration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void clickOnSaveButton() throws InterruptedException
	{
		scrolltoElement(driver, saveButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Thread.sleep(2000);
	}
	
	public void clickOnSaveandNextButton() throws InterruptedException
	{
		OPHelpers.enterPatDecSignatoryDetails(driver);
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
	}
	
	public void selectDeclarationOptions(int columnNumber) throws IOException, InterruptedException
	{
		
		
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPTESTDATA.xlsx", "PATIENTDECLOPTIONS", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		scrolltoElement(driver, ele);
		wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
		//ele.click();
		Thread.sleep(1000);
		
		}

	}
	
	public <T>T PatientDeclarationDetailsEntered(int colNumber,Class<T>expectedPage) throws InterruptedException, IOException
	{
		Thread.sleep(1000);
		selectDeclarationOptions(colNumber);
		enterSigntatoryDetails();
		clickOnSaveandNextButton();
		Thread.sleep(2000);
		
		return PageFactory.initElements(driver, expectedPage);
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
			System.out.println("No such element is found. " +e);
		}
		return ArrMessage;
		
	}
	
	public OPPatientEligibility clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientEligibility(driver);
	}
	
	public boolean verifyTickMarkOnPatientDeclaration() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatDeclMenu);
		return tickMark;
	}
	
	public void enterSigntatoryDetails() throws InterruptedException
	{
		String declarationName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "DeclarationName", 1);
		Support.enterDataInTextField(declarationNameTxt, declarationName, wait);
		enterAddressManually(1);
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
	            }
        	}
		     catch(NoSuchElementException e)
		     {
		            System.out.println("The element is not found on GMC Address Pop up box" +e);
		     }
	}
	
	public <T>T PatientDeclarationDetailsEntered(String colName,String file,String sheet,Class<T>expectedPage) throws InterruptedException, IOException
	{
		Thread.sleep(8000);
		OPHelpers.selectProvidedOptions(colName, file, sheet, driver);
		enterSigntatoryDetails();
		//OPHelpers.enterPatDecSignatoryDetails(driver);
		clickOnSaveandNextButton();
		Thread.sleep(2000);
		
		return PageFactory.initElements(driver, expectedPage);
	}

	
}
