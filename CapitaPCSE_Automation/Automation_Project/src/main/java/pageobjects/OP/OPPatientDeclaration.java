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

public class OPPatientDeclaration extends Support {
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
	
	@FindBy(xpath="//div[@class='h3 margin-top-0']")
	WebElement PatEleHeader;
	
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
	
	@FindBy(xpath="//div[@id='DivContainer']/form/div[13]/div[2]/input[1]")
	WebElement saveWaitingPerformerBtn;
	
	@FindBy(xpath="//h5/strong")
	WebElement claimNumberHeader;
	
	@FindBy(xpath="//div[@id='QRImage']/a/img")
	WebElement QRImage;
	
	@FindBy(css="button#btnSubmit")
	WebElement submitSignatory;
	
	@FindBy(xpath="//*[@id='modalSignSuccess']/div/div/div[3]/button")
	WebElement closeSignWindow;
	
	public OPPatientDeclaration(WebDriver driver){

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
		//Thread.sleep(8000);
		wait.until(ExpectedConditions.visibilityOf(saveAndNextButton)); //Added by Akshay to optimize code execution
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait_OP(driver);
	}
	
	public void selectDeclarationOptions(int columnNumber) throws IOException, InterruptedException
	{
		
		
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPTESTDATA.xlsx", "PATIENTDECLOPTIONS", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		Thread.sleep(2000);
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		scrolltoElement(driver, ele);
		wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
		//ele.click();
		Thread.sleep(1000);
		
		}

	}
	
	public OPPerformerDeclaration PatientDeclarationDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		Thread.sleep(1000);
		selectDeclarationOptions(colNumber);
		enterSigntatoryDetails();
		clickOnSaveandNextButton();
		Thread.sleep(2000);
		
		return new OPPerformerDeclaration(driver);
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
	
	public void GOS1PatientDeclarionSnap(String note) throws InterruptedException, IOException
	{
		wait.until(ExpectedConditions.visibilityOf(PatEleHeader));
		scrolltoElement(driver, PatEleHeader);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
	}
	
	public void enterSigntatoryDetails() throws InterruptedException
	{
		String declarationName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "DeclarationName", 1);
		Support.enterDataInTextField(declarationNameTxt, declarationName, wait);
		OPHelpers.enterAddressManually(driver);
		OPHelpers.enterPatDecSignatoryDetails(driver);
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
	
	public OPPatientDeclaration enterDetailsAndClickOnAwaitingPerformer(int colNumber) throws InterruptedException, IOException
	{
		Thread.sleep(1000);
		selectDeclarationOptions(colNumber);
		enterSigntatoryDetails();
		clickOnSaveAwaitingPerformer();
		Thread.sleep(2000);
		//div[@id='DivContainer']/form/div[13]/div[2]/input[1]
		
		return new OPPatientDeclaration(driver);
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
		ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}
	
	public OPPerformerDeclaration PatientDeclarationDetailsEntered(String colName,String file,String sheet) throws InterruptedException, IOException
	{
		Thread.sleep(1000);
		OPHelpers.selectProvidedOptions(colName,file,sheet,driver);
		enterSigntatoryDetails();
		clickOnSaveandNextButton();
		Thread.sleep(2000);
		
		return new OPPerformerDeclaration(driver);
	}

}
