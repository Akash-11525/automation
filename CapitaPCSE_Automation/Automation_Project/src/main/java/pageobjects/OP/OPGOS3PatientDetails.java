package pageobjects.OP;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS3PatientDetails extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="titleCode")
	WebElement titleDropdown;
	
	@FindBy(css="input[id='FirstName']")
	WebElement firstNameTxtBox;
	
	@FindBy(css="input[id='Surname']")
	WebElement surnameTxtBox;
	
	@FindBy(css="input[id='PreviousSurname']")
	WebElement previousSurnameTxtBox;
	
	@FindBy(css="button[class*='btn btn-info']")
	WebElement enterAddressButton;
	
	@FindBy(css="input[id='txtDateOfBirth']")
	WebElement txtDateOfBirthTxtBox;
	
	@FindBy(css="input[id='txtLastSightTestDate']")
	WebElement DateOfLastSightTxtBox;
	
	@FindBy(css="input[id='IsFirstTest']")
	WebElement IsFirstTestChkBox;
	
	@FindBy(css="input[id='IsTestDateUnknow']")
	WebElement IsTestDateUnknowChkBox;
	
	@FindBy(css="input[id='txtNhsNumber']")
	WebElement nhsNumberTxtBox;
	
	@FindBy(css="input[id='txtNiNumber']")
	WebElement nINumberTxtBox;
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement addressWindowSaveButton;
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[id='btnSaveNext']")
	WebElement saveAndNextButton;
	
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
	
	@FindBy(css="input[id='txtSightTestDate']")
	WebElement sightTestDateTxt;
	
	@FindBy(xpath="//div[@id='sidebar']//a[@class='list-group-item active']")
	WebElement activeSideMenuItem;
	
	@FindBy(id="PerformerName")
	WebElement performerName;
	
	@FindBy(id="PerformerCode")
	WebElement performerNumber;
	
	@FindBy(css="ul[id='PerformerNameResult']")
	WebElement PerformerNameResult;
	
	@FindBy(id="ContractorName")
	WebElement contractorName;
	
	@FindBy(css="ul[id='ContractorNameResult']")
	WebElement ContractorNameResult;
	
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(id="DivPatientDetailsli")
	WebElement PatDetMenu;
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	@FindBy(css="span[id='txtAddress']")
	WebElement AddressDetails;
	
	@FindBy(xpath="//div[@class='h5']/strong")
	WebElement claimNumberHeader;

	public OPGOS3PatientDetails(WebDriver driver){

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
	
	public OPGOS3Prescription clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		
		return new OPGOS3Prescription(driver);
	}
	
		
	public void enterPatientDetails(int colNumber) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);
		Thread.sleep(2000);	
	}
	
	public void enterPatientDetailsLastSightDateUnknown(int colNumber) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);

		String lastsightdate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "LastSightDate", colNumber);
		Support.enterDataInTextField(DateOfLastSightTxtBox, lastsightdate, wait);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		scrolltoElement(driver, IsTestDateUnknowChkBox);
		IsTestDateUnknowChkBox.click();
		Thread.sleep(2000);
	}
	
	public void enterPatientDetailswithoutLastSightTest(int colNumber) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);
		Thread.sleep(2000);
	}
	
	public OPGOS3Prescription PatientDetailsEntered(int colNumber, int day, String environment) throws InterruptedException, ParseException
	{
		Thread.sleep(2000);
		setPrescriptionDate(day);
		enterPerformerDetails(environment);
		enterPatientDetails(colNumber);
		clickOnSaveandNextButton();
		Thread.sleep(3000);
		return new OPGOS3Prescription(driver);
	}
	
	public OPPatientEligibility PatientDetailsEnteredwithUnknownLastSightDate(int colNumber, String environment) throws InterruptedException
	{
		enterPerformerDetails(environment);
		enterPatientDetailsLastSightDateUnknown(colNumber);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPPatientEligibility(driver);
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
	
/*	public void captureErrorOnAddressModalWindow() throws InterruptedException
	{
        try{
           // wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
            if(AddressModalWindow.isDisplayed())
            {
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).sendKeys(addressline1);
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).sendKeys(addressline2);
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).sendKeys(addressline3);
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).sendKeys(addresscity);
                   wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).sendKeys(addresscounty);
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).sendKeys(addresspostcode);
                   wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
                   Thread.sleep(2000);
                 // helpers.CommonFunctions.ClickOnButton("Save", driver);
                  
            }
     }
     catch(NoSuchElementException e)
     {
            System.out.println("The element is not found on GMC Address Pop up box" +e);
     }
     
    

	}*/
	
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
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		return ArrMessage;
		
	}
	
	public  List<String> AcutalAddressErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			//ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error text-danger']"));
		//	ActualErrorMesageList=driver.findElements(By.xpath("//span[contains(text(),'field-validation-error text-danger')]"));
			ActualErrorMesageList=driver.findElements(By.cssSelector("span[class*='field-validation-valid text-danger active field-validation-error']"));
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
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		return ArrMessage;
		
	}
	
	public void clearDateOfSightTestField() 
	{
		scrolltoElement(driver, sightTestDateTxt);
		wait.until(ExpectedConditions.elementToBeClickable(sightTestDateTxt)).click();
		sightTestDateTxt.clear();
	}
	
	public boolean verifyTickMarkOnPatientDetails() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatDetMenu);
		return tickMark;
	}
	
	public void enterPatientDetailswithLastSightTest(int colNumber, int days) throws InterruptedException, ParseException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);

		Date dt = CommonFunctions.getDateBeforeDays(days);
		String lastsightdate = CommonFunctions.convertDateToString(dt, "dd-MM-yyyy");
		System.out.println(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(lastsightdate);
		Support.enterDataInTextField(DateOfLastSightTxtBox, lastsightdate, wait);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
	}
	
	public OPPatientEligibility PatientDetailsEnteredwithlastSightTest(int colNumber, int days, String environment) throws InterruptedException, ParseException
	{
		enterPerformerDetails(environment);
		enterPatientDetailswithLastSightTest(colNumber, days);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPPatientEligibility(driver);
	}
	
	public void enterPerformerDetails(String environment) throws InterruptedException
	{
		//Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOf(performerName));
		if (!(CommonFunctions.isAttribtuePresent(performerName, "readonly")))
		{
			String performer = ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
					
			/*String contractor = ConfigurationData.OPContractor;
			wait.until(ExpectedConditions.elementToBeClickable(contractorName));
			CommonFunctions.setText(contractorName, contractor);
			helpers.CommonFunctions.selectOptionFromList(ContractorNameResult, contractor );*/ //Commented by Akshay as contractor field will be disabled
			wait.until(ExpectedConditions.elementToBeClickable(performerName)).clear();
			CommonFunctions.setText(performerName, performer);//performer
			helpers.CommonFunctions.selectOptionFromList(PerformerNameResult, performer);
		}
	}
	
	public void setPrescriptionDate(int day) throws ParseException
	{
		scrolltoElement(driver, sightTestDateTxt);
	    Date dt = CommonFunctions.getDateBeforeDays(day);
	    String lastsightdate = CommonFunctions.convertDateToString(dt, "dd-MM-yyyy");
		System.out.println(lastsightdate);
		Support.enterDataInTextField(sightTestDateTxt, lastsightdate, wait);
	}
	
	public void setPrescDate(String dt)
	{
		scrolltoElement(driver, sightTestDateTxt);
		Support.enterDataInTextField(sightTestDateTxt, dt, wait);
	}
	
	public void ErrorsnapOnAddressPostCode(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, addressPostcode);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void ErrorsnapOnPatientDetails(String note1, String note2) throws InterruptedException, IOException
	{
		scrolltoElement(driver, contractorName);
		Screenshot.TakeSnap(driver, note1);
		Thread.sleep(1000);
		scrolltoElement(driver, surnameTxtBox);
		Screenshot.TakeSnap(driver, note2);
		Thread.sleep(1000);
				
	}
	
	public List<String> getPerformerDetails()
	{
		List<String> PerformerDetails = new ArrayList<>();
		scrolltoElement(driver, performerName);
		String PerfName = performerName.getText();
		PerformerDetails.add(PerfName);
		
		String PerfNumber = performerNumber.getText();
		PerformerDetails.add(PerfNumber);
				
		return PerformerDetails;
	}
	
	public void ErrorsnapOnInvalidSightTestDate(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, sightTestDateTxt);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
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
	
	public void snapForNHSNumberBlank(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, previousSurnameTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public String getClaimNumber(String key) throws InterruptedException
	{
		Thread.sleep(1000);
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}
	
}
