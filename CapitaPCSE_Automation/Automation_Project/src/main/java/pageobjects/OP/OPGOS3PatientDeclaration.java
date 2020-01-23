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


public class OPGOS3PatientDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(css="button[name='btnSaveNext']")
	WebElement submitButton;
	
	@FindBy(id="modalResult")
	WebElement modalResultPopUp;
	
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
	
	@FindBy(xpath="//strong")
	WebElement claimNumberHeader;
	
	@FindBy(xpath="//div[@id='modalResult']//div[@class='modal-body']")
	WebElement modalResultPopupText;
	
	/*@FindBy(css = "button[class='btn btn-default']")
	WebElement modalResultCloseBtn;*/
	
	@FindBy(xpath="//div[@id='modalResult']//button[@class='btn btn-default']")
	WebElement modalResultCloseBtn;

	@FindBy(css="span[id='txtAddress']")
	WebElement AddressDetails;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(css="input[id='IsPatientParent')")
	WebElement isPatientParentChkBox;
	
	@FindBy(css="input[id='DeclarationName']")
	WebElement nameTxtBox;
	
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
	
	
	public OPGOS3PatientDeclaration(WebDriver driver){

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
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
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
	
	public void selectDeclarationOptions(int columnNumber) throws IOException, InterruptedException
	{
		
		boolean isSpinnerVisible= verifyPageLoading();
		if(!isSpinnerVisible){
			List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "PATDCLOPTIONS", columnNumber);
			for (int i = 0; i < CellValues.size(); i++) {
			String CellValue = CellValues.get(i);
			System.out.println("The cell value is: "+CellValue);
			//WebElement ele = driver.findElement(By.id(CellValue));
			
			WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')][@type='checkbox']"));
			scrolltoElement(driver, ele);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			//
			if(!(ele.isSelected())){
				ele.click();
			}
			//ele.click();
			Thread.sleep(1000);
			
			}
		}
	}
	
	public String getClaimNumber(String key)
	{
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPGOS3TESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPGOS3TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}

	public boolean clickOnSubmitButton() throws InterruptedException
	{
		boolean flag = false;
		//scrolltoElement(driver, signatureCanvasbox);		
		//enterSignatory(signatureCanvasbox, driver);
		OPHelpers.enterPatDecSignatoryDetails(driver);
		enterSignatoryDetails();
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(7000);
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn));

		if (modalResultPopUp.isDisplayed())
		{
			flag = true;
		}

		return flag;
	}
	
	public String getMsgTxtOnPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalResultPopupText)).getText();

		return msg;
	}
	
	public OPHomePage clickCloseOnResultPopup()
	{
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn)).click();
		return new OPHomePage(driver);
	}
	
	public String getAddressDetails(String note) throws InterruptedException, IOException
	{
		String Addr = null;
		scrolltoElement(driver, AddressDetails);
		Addr = AddressDetails.getText();
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		return Addr;
	}
	
	public boolean verifyPageLoading() throws InterruptedException
	{	
		boolean ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		while(ispresent)
		{
			Thread.sleep(2000);
			ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		}
		return ispresent;
	}
	
	public boolean clickOnSubmitButtonWithoutSignatory() throws InterruptedException
	{
		boolean flag = false;
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(7000);
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn));

		if (modalResultPopUp.isDisplayed())
		{
			flag = true;
		}

		return flag;
	}
	
	public void enterSignatoryDetails() throws InterruptedException
	{
		scrolltoElement(driver, nameTxtBox);
		String sigName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "SignatoryName", 1);
		Support.enterDataInTextField(nameTxtBox, sigName, wait);
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
                 // helpers.CommonFunctions.ClickOnButton("Save", driver);
                  
            }
     }
     catch(NoSuchElementException e)
     {
            System.out.println("The element is not found on GMC Address Pop up box" +e);
     }
     
    

	}
}
