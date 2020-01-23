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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.OPHelpers;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class OPGOS5PatientDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="IsPatient")
	WebElement isPatient;
	
	@FindBy(id="IsPatientParent")
	WebElement isPatientParent;
	
	@FindBy(id="IsPatientCarer")
	WebElement isPatientCarer;
	
	@FindBy(id="IsWitnessed")
	WebElement isWitnessed;
	
	@FindBy(id="DeclarationName")
	WebElement declarationNameTxtName;
	
	@FindBy(id="txtPostcode")
	WebElement txtPostcode;
	
	@FindBy(id="BtnsearchRecAdd")
	WebElement searchPostalCodeButton;
	
	@FindBy(id="btnAddAdress")
	WebElement addAddressButton;
	
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
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	@FindBy(id="DivPatientDeclarationli")
	WebElement PatDeclMenu;
	
	@FindBy(css="button[class*='btn btn-info']")
	WebElement enterAddressButton;
	
	@FindBy(id="DeclarationName")
	WebElement DeclarationNameTxt;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(xpath="//div[@id='DivContainer']/form/div[4]/div[10]/div[2]/input[1]")
	WebElement saveWaitingPerformerBtn;
	
	@FindBy(xpath="//div[@class='h5']/strong")
	WebElement claimNumberHeader;
	
	public OPGOS5PatientDeclaration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public OPGOS5PerformerDeclaration PatientDeclarationDetailsEntered(int colNumber) throws IOException, InterruptedException {
		selectProvidedOptions(colNumber);
		clickOnSaveandNextButton();
		
		return new OPGOS5PerformerDeclaration(driver);
	}

	public void selectProvidedOptions(int colNumber) throws IOException, InterruptedException {
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS5TESTDATA.xlsx", "PATIENTDECLOPTIONS", colNumber);
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
	
	public OPGOS5PerformerDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		
		/*scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);*/
		OPHelpers.enterPatDecSignatoryDetails(driver);
		
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		return new OPGOS5PerformerDeclaration(driver);
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
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatDeclMenu);
		return tickMark;
	}
	
	public void enterAddressManually(int colNumber) throws InterruptedException
	{
        try{
            
        	wait.until(ExpectedConditions.elementToBeClickable(DeclarationNameTxt)).clear();
        	DeclarationNameTxt.sendKeys("Test Name");
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
	
	public void clickOnSaveAwaitingPerformer() throws InterruptedException
	{
		scrolltoElement(driver, saveWaitingPerformerBtn);
		wait.until(ExpectedConditions.elementToBeClickable(saveWaitingPerformerBtn)).click();
		Support.verifyPageLoading(driver);
		Thread.sleep(2000);
	}
	
	public String getClaimNumber(String key)
	{
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPGOS5TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}
	
	public void GOS5ClaimDetailssnaps(String string) throws InterruptedException, IOException {
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}
	
	public OPGOS5PatientDeclaration enterSignatory() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);
		Thread.sleep(2000);
		return new OPGOS5PatientDeclaration(driver);
	}
	
	public OPGOS5PerformerDeclaration clickOnSaveandNextButtonWithoutSignatory() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		return new OPGOS5PerformerDeclaration(driver);
	}

	
	public void selectProvidedOptions(String colName,String file,String sheet) throws IOException, InterruptedException {
		OPHelpers.selectProvidedOptions(colName, file, sheet,driver);
	}
	
	public void enterAddressManually(String colName) throws InterruptedException
	{
        try{
	        	Support.enterDataInTextField(DeclarationNameTxt, "Test Name", wait);
	        	OPHelpers.enterAddressManually(driver);
        }
        catch(NoSuchElementException e)
        {
            System.out.println("The element is not found on GMC Address Pop up box" +e);
        }
	}
}
